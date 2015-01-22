package org.reactome.web.fireworks.client;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.RequiresResize;
import org.reactome.web.fireworks.analysis.AnalysisType;
import org.reactome.web.fireworks.events.*;
import org.reactome.web.fireworks.handlers.*;
import org.reactome.web.fireworks.legends.EnrichmentControl;
import org.reactome.web.fireworks.legends.EnrichmentLegend;
import org.reactome.web.fireworks.legends.ExpressionControl;
import org.reactome.web.fireworks.legends.ExpressionLegend;
import org.reactome.web.fireworks.model.Edge;
import org.reactome.web.fireworks.model.Graph;
import org.reactome.web.fireworks.model.Node;
import org.reactome.web.fireworks.profiles.FireworksProfile;
import org.reactome.web.fireworks.profiles.StandardFireworksProfile;
import org.reactome.web.fireworks.util.Tooltip;
import uk.ac.ebi.pwp.structures.quadtree.interfaces.QuadTreeBox;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
class FireworksCanvas extends AbsolutePanel implements HasHandlers, RequiresResize,
        FireworksVisibleAreaChangedHandler, FireworksZoomHandler,
        NodeSelectedHandler, NodeSelectedResetHandler,
        NodeHoverHandler, NodeHoverResetHandler,
        AnalysisPerformedHandler, AnalysisResetHandler {

    class CanvasNotSupportedException extends Exception {}

    static final double INITIAL_FONT = 7;
    static final double MIN_FONT_SIZE = 3;
    static final double MAX_FONT_SIZE = 12;
    static final double TRANSPARENCY_THRESHOLD = 4;

    static final int FACTOR_TEXT_THRESHOLD = 3;
    static final FireworksProfile PROFILE = new StandardFireworksProfile();

    private EventBus eventBus;

    private AnalysisType analysisType;

    private Node selected;
    private double fontSize;
    private double factor = 1;
    private double aura = 2;
    private boolean tooltipNeeded = true;

    private Canvas edgesHighlight;
    private Canvas edgesSelection;
    private Canvas edges;

    private Canvas nodesHighlight;
    private Canvas nodesSelection;
    private Canvas nodes;

    private Canvas textAllNodes;
    private Canvas textSelection;
    private Canvas textTLP;

    private FireworksThumbnail thumbnail;
    private FireworksInfo info;

    private List<Canvas> canvases = new LinkedList<Canvas>();

    private Set<Node> drawnNodes = new HashSet<Node>();

    FireworksCanvas(EventBus eventBus, Graph graph) throws CanvasNotSupportedException {
        this.eventBus = eventBus;
        this.thumbnail = new FireworksThumbnail(eventBus, graph, PROFILE);

        int width = (int) Math.ceil(graph.getMaxX());
        int height = (int) Math.ceil(graph.getMaxY());

        this.edgesHighlight = createCanvas(width, height);
        this.edgesSelection = createCanvas(width, height);
        this.edges = createCanvas(width, height);

        this.nodesHighlight = createCanvas(width, height);
        this.nodesSelection = createCanvas(width, height);
        this.nodes = createCanvas(width, height);

        this.textAllNodes = createCanvas(width, height);
        this.textSelection = createCanvas(width, height);
        this.textTLP = createCanvas(width, height);

        if(FireworksFactory.SHOW_INFO) {
            this.info = new FireworksInfo(eventBus);
            this.add(this.info);
        }
        this.add(thumbnail);

        //TODO: Under TEST vv
        this.add(new EnrichmentLegend(eventBus, PROFILE));
        this.add(new EnrichmentControl(eventBus));

        this.add(new ExpressionLegend(eventBus, PROFILE));
        this.add(new ExpressionControl(eventBus));
        //TODO: Under TEST ^^

        this.initialiseHandlers();
    }

    private void initialiseHandlers(){
        this.eventBus.addHandler(FireworksVisibleAreaChangedEvent.TYPE, this);
        this.eventBus.addHandler(FireworksZoomEvent.TYPE, this);
        this.eventBus.addHandler(NodeSelectedEvent.TYPE, this);
        this.eventBus.addHandler(NodeSelectedResetEvent.TYPE, this);
        this.eventBus.addHandler(NodeHoverEvent.TYPE, this);
        this.eventBus.addHandler(NodeHoverResetEvent.TYPE, this);

        this.eventBus.addHandler(AnalysisPerformedEvent.TYPE, this);
        this.eventBus.addHandler(AnalysisResetEvent.TYPE, this);
    }


    public HandlerRegistration addDoubleClickHandler(DoubleClickHandler handler){
        return getTopCanvas().addDoubleClickHandler(handler);
    }

    public HandlerRegistration addClickHandler(ClickHandler handler){
        return getTopCanvas().addClickHandler(handler);
    }

    public HandlerRegistration addMouseDownHandler(MouseDownHandler handler){
        return getTopCanvas().addMouseDownHandler(handler);
    }

    public HandlerRegistration addMouseOutHandler(MouseOutHandler handler){
        return getTopCanvas().addMouseOutHandler(handler);
    }

    public HandlerRegistration addMouseUpHandler(MouseUpHandler handler){
        return getTopCanvas().addMouseUpHandler(handler);
    }

    public HandlerRegistration addMouseMoveHandler(MouseMoveHandler handler){
        return getTopCanvas().addMouseMoveHandler(handler);
    }

    public HandlerRegistration addMouseWheelHandler(MouseWheelHandler handler){
        return getTopCanvas().addMouseWheelHandler(handler);
    }

    void drawElements(Set<QuadTreeBox> items) {
        this.clearMain();

        Context2d ctx = this.edges.getContext2d();
        String edgeColor = analysisType==null ? PROFILE.getEdgeStandardColour() : PROFILE.getEdgeFadeoutColour();
        ctx.setStrokeStyle(edgeColor);
        for (QuadTreeBox item : items) {
            if (item instanceof Edge) {
                Edge edge = (Edge) item;
                Node node = edge.getTo();
                if(node.getStatistics()!=null) {
                    ctx.save();
                    ctx.setStrokeStyle(PROFILE.getEdgeEnrichmentColour(node.getStatistics().getpValue()));
                    edge.draw(ctx);
                    ctx.restore();
                }else{
                    edge.draw(ctx);
                }
            }
        }

        this.drawnNodes = new HashSet<Node>();
        ctx = this.nodes.getContext2d();
        String nodeColor = analysisType==null ? PROFILE.getNodeStandardColour() : PROFILE.getNodeFadeoutColour();
        ctx.setFillStyle(nodeColor);
        ctx.setStrokeStyle(nodeColor);
        for (QuadTreeBox item : items) {
            if (item instanceof Node) {
                Node node = (Node) item;
                if(node.getStatistics()!=null){
                    double pValue = node.getStatistics().getpValue();
                    ctx.save();
                    String c = PROFILE.getNodeEnrichmentColour(pValue);
                    ctx.setFillStyle(c); ctx.setStrokeStyle(c);
                    node.draw(ctx);
                    ctx.restore();
                }else {
                    node.draw(ctx);
                }
                this.drawnNodes.add((node));
            }
        }

        if(this.info!=null) {
            this.info.setNodes(this.drawnNodes.size());
        }
    }

    void drawText(Node node){
        clearText();

        boolean textForAll = this.factor > FACTOR_TEXT_THRESHOLD;
        Set<Node> aux = getNodeAndAncestorsWithText(node, textForAll);
        aux.addAll(getNodeAndAncestorsWithText(this.selected, textForAll));

        Context2d tlp = this.textTLP.getContext2d();
        Context2d sel = this.textSelection.getContext2d();
        Context2d all = this.textAllNodes.getContext2d();
        for (Node item : this.drawnNodes) {
            if (aux.contains(item)) {
                if(item.equals(node) && item.isTopLevel()){
                    double tlpFontSize = fontSize * 1.5;
                    if(!node.equals(this.selected)) {
                        tlp.save(); // Highlighting a top level pathway
                        tlp.setFillStyle("#003366"); //("#D11D1D");
                        tlp.setFont(tlpFontSize + "pt Arial");
                        item.drawText(tlp, tlpFontSize, 5, false);
                        tlp.restore();
                    }else{
                        sel.save(); // Selecting a top level pathway
                        sel.setFont(tlpFontSize + "pt Arial");
                        item.drawText(sel, tlpFontSize, 5, false);
                        sel.restore();
                    }
                }else{
                    if(item.equals(selected) && item.isTopLevel()){
                        double tlpFontSize = fontSize * 1.5;
                        sel.save(); // Selecting a top level pathway
                        sel.setFont(tlpFontSize + "pt Arial");
                        item.drawText(sel, tlpFontSize, 5, false);
                        sel.restore();
                    }else { // Here any node in the selection list (depending on the factor)
                        if(item.isTopLevel()) {
                            double tlpFontSize = fontSize * 1.25;
                            sel.save(); // Selecting a top level pathway
                            sel.setFont(tlpFontSize + "pt Arial");
                            item.drawText(sel, tlpFontSize, 5, false);
                            sel.restore();
                        }else{
                            item.drawText(sel, fontSize, 5, item.equals(this.selected));
                        }
                    }
                }
            } else if (item.isTopLevel()) {
                double tlpFontSize = fontSize * 1.25;
                tlp.setFont(tlpFontSize + "pt Arial");
                item.drawText(tlp, tlpFontSize, 5, false);
            } else if (textForAll){
                item.drawText(all, fontSize, 5, false);
            }
        }
    }

    void clearCanvas(Canvas canvas){
        canvas.getContext2d().clearRect(0, 0, canvas.getOffsetWidth(), canvas.getOffsetHeight());
    }

    void clearHighlight(){
        this.clearCanvas(this.nodesHighlight);
        this.clearCanvas(this.edgesHighlight);
        this.thumbnail.clearHighlights();
    }

    void clearMain(){
        this.clearCanvas(this.nodes);
        this.clearCanvas(this.edges);
    }

    void clearSelection(){
        this.clearCanvas(this.nodesSelection);
        this.clearCanvas(this.edgesSelection);
        this.thumbnail.clearSelection();
    }

    void clearText(){
        this.clearCanvas(this.textAllNodes);
        this.clearCanvas(this.textSelection);
        this.clearCanvas(this.textTLP);
    }

    Canvas getTopCanvas(){
        return this.canvases.get(this.canvases.size() -1 );
    }

    public void highlightNode(Node node){
        clearHighlight();
        if(node==null) return;

        Context2d ctx = this.nodesHighlight.getContext2d();

        double expandedAura = (factor > FACTOR_TEXT_THRESHOLD)? aura * 2 : aura;

        String  color = PROFILE.getNodeHighlightColour(); // "#E1ED55";
        ctx.setFillStyle(color);
        ctx.setStrokeStyle(color);
        Set<Edge> edges = node.getEdgesTo();
        for (Node ancestor : node.getAncestors()) {
            if(ancestor.isTopLevel()){
                if(this.selected!=null && this.selected.getTopLevelPathways().contains(ancestor)) {
                    ancestor.highlitght(ctx, aura);
                }else {
                    ancestor.highlitght(ctx, expandedAura);
                }
            }else {
                ancestor.highlitght(ctx, aura);
            }
            edges.addAll(ancestor.getEdgesTo());
        }

        ctx = this.edgesHighlight.getContext2d();
        ctx.setFillStyle(color);
        ctx.setStrokeStyle(color);
        ctx.setLineWidth(aura);
        for (Edge edge : edges) {
            edge.highlitght(ctx, aura);
        }

        this.thumbnail.highlightEdges(node, edges);

        if(node.isTopLevel()){
            drawText(node);
        }else if(tooltipNeeded){
            Tooltip.getTooltip().show(
                    getTopCanvas().getCanvasElement(),
                    (int) node.getX(),
                    (int) node.getY(),
                    node.getSize(),
                    new PathwayInfoPanel(node)
            );
        }
    }

    public void selectNode(Node node){
        this.selected = node;
        clearHighlight(); //This one is needed in case the user clicks and does not move the mouse :)
        clearSelection();
        if(node==null) return;

        String color = PROFILE.getNodeSelectionColour(); // "#BBBBFF";
        Context2d ctx = this.nodesSelection.getContext2d();
        ctx.setFillStyle(color);
        ctx.setStrokeStyle(color);
        node.highlitght(ctx, aura);

        Set<Edge> edges = node.getEdgesTo();
        for (Node ancestor : node.getAncestors()) {
            ancestor.highlitght(ctx, aura);
            edges.addAll(ancestor.getEdgesTo());
        }

        color = PROFILE.getEdgeSelectionColour(); // "#EEEEFF";
        ctx = this.edgesSelection.getContext2d();
        ctx.setFillStyle(color);
        ctx.setStrokeStyle(color);
        ctx.setLineWidth(aura);
        for (Edge edge : edges) {
            edge.highlitght(ctx, aura);
        }

        drawText(node);

        this.thumbnail.selectNode(node, edges);
    }

    @Override
    public void onAnalysisPerformed(AnalysisPerformedEvent e) {
        this.analysisType = e.getAnalysisType();
    }

    @Override
    public void onAnalysisReset() {
        this.analysisType = null;
    }


    @Override
    public void onFireworksVisibleAreaChanged(FireworksVisibleAreaChangedEvent event) {
        this.thumbnail.setVisibleArea(event.getTranslation(), event.getWidth(), event.getHeight());
    }

    //Todo: revisit this method
    @Override
    public void onFireworksZoomChanged(FireworksZoomEvent event) {
        this.thumbnail.setVisibleArea(event.getStatus().getOffset(), event.getWidth(), event.getHeight());
        this.factor = event.getStatus().getFactor();
        double aux = 1 + this.factor;
        this.aura = aux > 4 ? 4 : aux;
        this.setFontParameters();
    }

    @Override
    public void onNodeHover(NodeHoverEvent event) {
        Tooltip.getTooltip().setPreventShowing(false);
        this.highlightNode(event.getNode());
    }

    @Override
    public void onNodeHoverReset() {
        Tooltip.getTooltip().hide();
        drawText(this.selected);
        this.clearHighlight();
        this.thumbnail.clearHighlights();
    }

    @Override
    public void onNodeSelected(NodeSelectedEvent event) {
        this.selectNode(event.getNode());
        this.drawText(event.getNode());
    }

    @Override
    public void onNodeSelectionReset() {
        this.selectNode(null);
        this.drawText(null);
    }

    @Override
    public void onResize() {
        int width = this.getOffsetWidth();
        int height = this.getOffsetHeight();
        for (Canvas canvas : this.canvases) {
            this.setCanvasProperties(canvas, width, height);
        }
        this.setFontParameters();
        this.eventBus.fireEventFromSource(new FireworksResizedEvent(width, height), this);
    }

    private Canvas createCanvas(int width, int height) {
        Canvas canvas = Canvas.createIfSupported();
        canvas.setCoordinateSpaceWidth(width);
        canvas.setCoordinateSpaceHeight(height);
        canvas.setPixelSize(width, height);
        this.add(canvas, 0, 0);
        this.canvases.add(canvas);
        return canvas;
    }

    private Set<Node> getNodeAndAncestorsWithText(Node node, boolean textForAll){
        Set<Node> rtn = new HashSet<Node>();
        if(node!=null){
            rtn.add(node);
            if(textForAll){
                rtn.addAll(node.getAncestors());
            }else{
                rtn.addAll(node.getTopLevelPathways());
            }
        }
        return rtn;
    }

    //INITIALIZE THE CANVAS taking into account the CanvasProperties
    private void setCanvasProperties(Canvas canvas, int width, int height){
        canvas.setCoordinateSpaceWidth(width);
        canvas.setCoordinateSpaceHeight(height);
        canvas.setPixelSize(width, height);
        clearCanvas(canvas);
    }

    private void setFontParameters(){
        this.fontSize = this.factor * MAX_FONT_SIZE / 30;
        double fontOpacity = fontSize / TRANSPARENCY_THRESHOLD;
        fontOpacity = fontOpacity > 1 ? 1 : fontOpacity;

        if(fontSize < MIN_FONT_SIZE){
            fontSize = MIN_FONT_SIZE;
        }else if(fontSize > MAX_FONT_SIZE){
            fontSize = MAX_FONT_SIZE;
        }
        this.tooltipNeeded = fontSize < 7;

        Context2d ctx = this.textAllNodes.getContext2d();
        ctx.setFont(fontSize + "pt Arial"); //ctx.setFont(fontSize + "pt Calibri");
        ctx.setFillStyle("rgba(0,0,0, " + fontOpacity + ")");

        fontSize = fontSize > INITIAL_FONT ? fontSize : INITIAL_FONT;

        ctx = this.textSelection.getContext2d();
        ctx.setFont(fontSize + "pt Arial"); //ctx.setFont(fontSize + "pt Calibri");
        ctx.setFillStyle("#0000FF");

        ctx = this.textTLP.getContext2d();
        ctx.setFont(fontSize + "pt Arial"); //ctx.setFont(fontSize + "pt Calibri");
        ctx.setFillStyle("#000000");
    }
}
