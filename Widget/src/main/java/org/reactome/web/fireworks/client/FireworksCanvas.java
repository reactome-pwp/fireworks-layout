package org.reactome.web.fireworks.client;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RequiresResize;
import org.reactome.web.fireworks.controls.navigation.NavigationControlPanel;
import org.reactome.web.fireworks.controls.settings.HideableContainerPanel;
import org.reactome.web.fireworks.controls.settings.RightContainerPanel;
import org.reactome.web.fireworks.controls.top.LeftTopLauncherPanel;
import org.reactome.web.fireworks.controls.top.RightTopLauncherPanel;
import org.reactome.web.fireworks.events.*;
import org.reactome.web.fireworks.handlers.*;
import org.reactome.web.fireworks.legends.*;
import org.reactome.web.fireworks.model.AnalysisInfo;
import org.reactome.web.fireworks.model.Edge;
import org.reactome.web.fireworks.model.Graph;
import org.reactome.web.fireworks.model.Node;
import org.reactome.web.fireworks.profiles.FireworksColours;
import org.reactome.web.fireworks.util.ToolTipContainer;
import org.reactome.web.fireworks.util.popups.ImageDownloadDialog;
import uk.ac.ebi.pwp.structures.quadtree.client.QuadTreeBox;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
class FireworksCanvas extends AbsolutePanel implements HasHandlers, RequiresResize,
        FireworksVisibleAreaChangedHandler, FireworksZoomHandler,
        NodeSelectedHandler, NodeSelectedResetHandler, NodeHoverHandler, NodeHoverResetHandler,
        AnalysisPerformedHandler, AnalysisResetHandler {

    class CanvasNotSupportedException extends Exception {
    }

    static final double INITIAL_FONT = 7;
    static final double MIN_FONT_SIZE = 3;
    static final double MAX_FONT_SIZE = 12;
    static final double TRANSPARENCY_THRESHOLD = 4;

    static final int FACTOR_TEXT_THRESHOLD = 3;

    private EventBus eventBus;

    private final AnalysisInfo analysisInfo = new AnalysisInfo();

    private Node selected;
    private double fontSize;
    private double factor = 1;
    private double aura = 2;

    private Canvas edgesFlagged;
    private Canvas edgesHighlight;
    private Canvas edgesSelection;
    private Canvas edges;

    private Canvas nodesFlagged;
    private Canvas nodesHighlight;
    private Canvas nodesSelection;
    private Canvas nodes;

    private Canvas textAllNodes;
    private Canvas textSelection;
    private Canvas textTLP;

    private ToolTipContainer tooltipContainer;

    private IllustrationPanel illustration;

    private FireworksThumbnail thumbnail;
    private FireworksInfo info;

    private List<Canvas> canvases = new LinkedList<>();

    private Set<Node> drawnNodes = new HashSet<>();

    FireworksCanvas(EventBus eventBus, Graph graph) throws CanvasNotSupportedException {
        this.getElement().setClassName("pwp-FireworksCanvas");
        this.eventBus = eventBus;
        this.thumbnail = new FireworksThumbnail(eventBus, graph);

        int width = (int) Math.ceil(graph.getMaxX());
        int height = (int) Math.ceil(graph.getMaxY());

        this.edgesFlagged = createCanvas(width, height);
        this.edgesHighlight = createCanvas(width, height);
        this.edgesSelection = createCanvas(width, height);
        this.edges = createCanvas(width, height);

        this.nodesFlagged = createCanvas(width, height);
        this.nodesHighlight = createCanvas(width, height);
        this.nodesSelection = createCanvas(width, height);
        this.nodes = createCanvas(width, height);

        this.textAllNodes = createCanvas(width, height);
        this.textSelection = createCanvas(width, height);
        this.textTLP = createCanvas(width, height);

        //DO NOT CHANGE THE ORDER OF THE FOLLOWING TWO LINES
        this.tooltipContainer = createToolTipContainer(width, height);
        createCanvas(width, height); //Top-level canvas (mouse ctrl only)

        if (FireworksFactory.SHOW_INFO) {
            this.info = new FireworksInfo(eventBus);
            this.add(this.info);
        }
        this.add(thumbnail);

        //Control panel
        this.add(new NavigationControlPanel(eventBus));

        //Right container
        RightContainerPanel rightContainerPanel = new RightContainerPanel();
        this.add(rightContainerPanel);

        //Bottom Controls container
        BottomContainerPanel bottomContainerPanel = new BottomContainerPanel();
        this.add(bottomContainerPanel);

        //Flagged Objects control panel
        bottomContainerPanel.add(new FlaggedItemsControl(eventBus));

        //Enrichment legend and control panels
        rightContainerPanel.add(new EnrichmentLegend(eventBus));
        bottomContainerPanel.add(new EnrichmentControl(eventBus));

        //Expression legend and control panels
        rightContainerPanel.add(new ExpressionLegend(eventBus));
        bottomContainerPanel.add(new ExpressionControl(eventBus));

        //Launcher panels
        this.add(new LeftTopLauncherPanel(eventBus, graph));
        this.add(new RightTopLauncherPanel(eventBus));

        //Settings panel
        rightContainerPanel.add(new HideableContainerPanel(eventBus));


        //Illustration panel
        this.add(this.illustration = new IllustrationPanel(), 0, 0);

        this.initialiseHandlers();
    }

    private void initialiseHandlers() {
        this.eventBus.addHandler(FireworksVisibleAreaChangedEvent.TYPE, this);
        this.eventBus.addHandler(FireworksZoomEvent.TYPE, this);
        this.eventBus.addHandler(NodeSelectedEvent.TYPE, this);
        this.eventBus.addHandler(NodeSelectedResetEvent.TYPE, this);
        this.eventBus.addHandler(NodeHoverEvent.TYPE, this);
        this.eventBus.addHandler(NodeHoverResetEvent.TYPE, this);

        this.eventBus.addHandler(AnalysisPerformedEvent.TYPE, this);
        this.eventBus.addHandler(AnalysisResetEvent.TYPE, this);
    }


//    public HandlerRegistration addDoubleClickHandler(DoubleClickHandler handler){
//        return getTopCanvas().addDoubleClickHandler(handler);
//    }

    public HandlerRegistration addClickHandler(ClickHandler handler) {
        return getTopCanvas().addClickHandler(handler);
    }

    public HandlerRegistration addMouseDownHandler(MouseDownHandler handler) {
        return getTopCanvas().addMouseDownHandler(handler);
    }

    public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
        return getTopCanvas().addMouseOutHandler(handler);
    }

    public HandlerRegistration addMouseUpHandler(MouseUpHandler handler) {
        return getTopCanvas().addMouseUpHandler(handler);
    }

    public HandlerRegistration addMouseMoveHandler(MouseMoveHandler handler) {
        return getTopCanvas().addMouseMoveHandler(handler);
    }

    public HandlerRegistration addMouseWheelHandler(MouseWheelHandler handler) {
        return getTopCanvas().addMouseWheelHandler(handler);
    }

    // Touch events registration
    public HandlerRegistration addTouchStartHandler(TouchStartHandler handler) {
        return getTopCanvas().addTouchStartHandler(handler);
    }

    public HandlerRegistration addTouchEndHandler(TouchEndHandler handler) {
        return getTopCanvas().addTouchEndHandler(handler);
    }

    public HandlerRegistration addTouchMoveHandler(TouchMoveHandler handler) {
        return getTopCanvas().addTouchMoveHandler(handler);
    }

    public HandlerRegistration addTouchCancelHandler(TouchCancelHandler handler) {
        return getTopCanvas().addTouchCancelHandler(handler);
    }

    void drawElements(Set<QuadTreeBox> items) {
        this.cleanMainCanvas();

        int column = this.analysisInfo.getColumn();
        Context2d ctx = this.edges.getContext2d();
        ctx.setStrokeStyle(FireworksColours.PROFILE.getEdgeInitialColour());
        for (QuadTreeBox item : items) {
            if (item instanceof Edge) {
                Edge edge = (Edge) item;
                switch (this.analysisInfo.getType()) {
                    case SPECIES_COMPARISON:
                    case OVERREPRESENTATION:
                        ctx.setStrokeStyle(edge.getColour());
                        edge.draw(ctx);
                        break;
                    case EXPRESSION:
                        ctx.setStrokeStyle(edge.getExpressionColor(column));
                        edge.draw(ctx);
                        break;
                    case NONE:
                    default:
                        edge.draw(ctx);
                }
            }
        }

        this.drawnNodes = new HashSet<>();
        ctx = this.nodes.getContext2d();
        String colour = FireworksColours.PROFILE.getNodeInitialColour();
        ctx.setFillStyle(colour);
        ctx.setStrokeStyle(colour);
        for (QuadTreeBox item : items) {
            if (item instanceof Node) {
                Node node = (Node) item;
                switch (this.analysisInfo.getType()) {
                    case SPECIES_COMPARISON:
                    case OVERREPRESENTATION:
                        colour = node.getColour();
                        ctx.setFillStyle(colour);
                        ctx.setStrokeStyle(colour);
                        node.draw(ctx);
                        break;
                    case EXPRESSION:
                        colour = node.getExpressionColor(column);
                        ctx.setFillStyle(colour);
                        ctx.setStrokeStyle(colour);
                        node.draw(ctx);
                        break;
                    case NONE:
                    default:
                        node.draw(ctx);
                }
                this.drawnNodes.add((node));
            }
        }

        if (this.info != null) {
            this.info.setNodes(this.drawnNodes.size());
        }
    }

    void drawText(Node node) {
        cleanTextCanvas();

        boolean textForAll = this.factor > FACTOR_TEXT_THRESHOLD;
        Set<Node> aux = getNodeAndAncestorsWithText(node, textForAll);
        aux.addAll(getNodeAndAncestorsWithText(this.selected, textForAll));

        Context2d tlp = this.textTLP.getContext2d();
        Context2d sel = this.textSelection.getContext2d();
        Context2d all = this.textAllNodes.getContext2d();
        for (Node item : this.drawnNodes) {
            if (aux.contains(item)) {
                if (item.equals(node) && item.isTopLevel()) {
                    double tlpFontSize = fontSize * 1.5;
                    if (!node.equals(this.selected)) {
                        tlp.save(); // Highlighting a top level pathway
                        tlp.setFillStyle("#003366"); //("#D11D1D");
                        tlp.setFont(tlpFontSize + "pt Arial");
                        item.drawText(tlp, tlpFontSize, 5, false);
                        tlp.restore();
                    } else {
                        sel.save(); // Selecting a top level pathway
                        sel.setFont(tlpFontSize + "pt Arial");
                        item.drawText(sel, tlpFontSize, 5, false);
                        sel.restore();
                    }
                } else {
                    if (item.equals(selected) && item.isTopLevel()) {
                        double tlpFontSize = fontSize * 1.5;
                        sel.save(); // Selecting a top level pathway
                        sel.setFont(tlpFontSize + "pt Arial");
                        item.drawText(sel, tlpFontSize, 5, false);
                        sel.restore();
                    } else { // Here any node in the selection list (depending on the factor)
                        if (item.isTopLevel()) {
                            double tlpFontSize = fontSize * 1.25;
                            sel.save(); // Selecting a top level pathway
                            sel.setFont(tlpFontSize + "pt Arial");
                            item.drawText(sel, tlpFontSize, 5, false);
                            sel.restore();
                        } else {
                            item.drawText(sel, fontSize, 5, item.equals(this.selected));
                        }
                    }
                }
            } else if (item.isTopLevel()) {
                double tlpFontSize = fontSize * 1.25;
                tlp.setFont(tlpFontSize + "pt Arial");
                item.drawText(tlp, tlpFontSize, 5, false);
            } else if (textForAll) {
                item.drawText(all, fontSize, 5, false);
            }
        }
    }

    void cleanCanvas(Canvas canvas) {
        canvas.getContext2d().clearRect(0, 0, canvas.getOffsetWidth(), canvas.getOffsetHeight());
    }

    void cleanCanvas(Context2d ctx) {
        ctx.clearRect(0, 0, ctx.getCanvas().getWidth(), ctx.getCanvas().getHeight());
    }

    void cleanFlaggedCanvas() {
        this.cleanCanvas(this.nodesFlagged);
        this.cleanCanvas(this.edgesFlagged);
        this.thumbnail.clearFlags();
    }

    void cleanHighlightCanvas() {
        this.cleanCanvas(this.nodesHighlight);
        this.cleanCanvas(this.edgesHighlight);
        this.thumbnail.clearHighlights();
    }

    void cleanMainCanvas() {
        this.cleanCanvas(this.nodes);
        this.cleanCanvas(this.edges);
    }

    void cleanSelectionCanvas() {
        this.cleanCanvas(this.nodesSelection);
        this.cleanCanvas(this.edgesSelection);
        this.thumbnail.clearSelection();
    }

    void cleanTextCanvas() {
        this.cleanCanvas(this.textAllNodes);
        this.cleanCanvas(this.textSelection);
        this.cleanCanvas(this.textTLP);
    }

    public void exportImage(final String diagramStId) {
        final Context2d ctx = this.canvases.get(this.canvases.size() - 1).getContext2d();
        //This is silly but gives some visual feedback of the picture taking :D
        (new Timer() {
            double alpha = 1.0;

            @Override
            public void run() {
                cleanCanvas(ctx);
                if (alpha > 0) {
                    ctx.save();
                    ctx.setGlobalAlpha(alpha);
                    ctx.setFillStyle("#FFFFFF");
                    ctx.fillRect(0, 0, ctx.getCanvas().getWidth(), ctx.getCanvas().getHeight());
                    ctx.restore();
                    alpha -= 0.1;
                } else {
                    this.cancel();
                    for (int i = 0; i < canvases.size() - 1; i++) {
                        ctx.drawImage(canvases.get(i).getCanvasElement(), 0, 0);
                    }
                    Image image = new Image();
                    image.setUrl(ctx.getCanvas().toDataUrl("image/png"));
                    final ImageDownloadDialog downloadDialogBox = new ImageDownloadDialog(image, diagramStId);
                    downloadDialogBox.show();
                    cleanCanvas(ctx);
                }
            }
        }).scheduleRepeating(20);
    }

    Canvas getTopCanvas() {
        return this.canvases.get(this.canvases.size() - 1);
    }

    public void flagElements(Set<Node> nodes, Set<Edge> edges) {
        cleanFlaggedCanvas();
        if (nodes == null || nodes.isEmpty()) return;

        Context2d ctx = this.nodesFlagged.getContext2d();
        double aura = this.aura * 1.5; //In this case we need it slightly bigger
        String color = FireworksColours.PROFILE.getNodeFlagColour();
        ctx.setFillStyle(color);
        ctx.setStrokeStyle(color);
        for (Node node : nodes) {
            node.highlight(ctx, aura);
        }

        color = FireworksColours.PROFILE.getEdgeFlagColour();
        ctx = this.edgesFlagged.getContext2d();
        ctx.setFillStyle(color);
        ctx.setStrokeStyle(color);
        ctx.setLineWidth(aura);
        for (Edge edge : edges) {
            edge.highlight(ctx, aura);
        }

        this.thumbnail.flagNode(nodes, edges);
    }

    private void flagNode(Node node) {



    }

    private void flagEdge(Edge edge) {
        Context2d ctx = this.nodesFlagged.getContext2d();


    }

    public void highlightNode(Node node) {
        cleanHighlightCanvas();
        if (node == null) return;

        Context2d ctx = this.nodesHighlight.getContext2d();

        double expandedAura = (factor > FACTOR_TEXT_THRESHOLD) ? aura * 2 : aura;

        String color = FireworksColours.PROFILE.getNodeHighlightColour();
        ctx.setFillStyle(color);
        ctx.setStrokeStyle(color);
        node.highlight(ctx, aura);

        Set<Edge> edges = node.getEdgesTo();
        //noinspection Duplicates
        for (Node ancestor : node.getAncestors()) {
            if (ancestor.isTopLevel()) {
                if (this.selected != null && this.selected.getTopLevelPathways().contains(ancestor)) {
                    ancestor.highlight(ctx, aura);
                } else {
                    ancestor.highlight(ctx, expandedAura);
                }
            } else {
                ancestor.highlight(ctx, aura);
            }
            edges.addAll(ancestor.getEdgesTo());
        }

        color = FireworksColours.PROFILE.getEdgeHighlightColour();
        ctx = this.edgesHighlight.getContext2d();
        ctx.setFillStyle(color);
        ctx.setStrokeStyle(color);
        ctx.setLineWidth(aura);
        for (Edge edge : edges) {
            edge.highlight(ctx, aura);
        }

        this.thumbnail.highlightEdges(node, edges);

        if (node.isTopLevel()) {
            drawText(node);
        }
    }

    public void selectNode(Node node) {
        this.selected = node;
        cleanHighlightCanvas(); //This one is needed in case the user clicks and does not move the mouse :)
        cleanSelectionCanvas();
        if (node == null) return;

        String color = FireworksColours.PROFILE.getNodeSelectionColour();
        Context2d ctx = this.nodesSelection.getContext2d();
        ctx.setFillStyle(color);
        ctx.setStrokeStyle(color);
        node.highlight(ctx, aura);

        Set<Edge> edges = node.getEdgesTo();
        for (Node ancestor : node.getAncestors()) {
            ancestor.highlight(ctx, aura);
            edges.addAll(ancestor.getEdgesTo());
        }

        color = FireworksColours.PROFILE.getEdgeSelectionColour();
        ctx = this.edgesSelection.getContext2d();
        ctx.setFillStyle(color);
        ctx.setStrokeStyle(color);
        ctx.setLineWidth(aura);
        for (Edge edge : edges) {
            edge.highlight(ctx, aura);
        }

        drawText(node);

        this.thumbnail.selectNode(node, edges);
    }

    @Override
    public void onAnalysisPerformed(AnalysisPerformedEvent e) {
        this.analysisInfo.setInfo(e.getAnalysisType(), e.getExpressionSummary());
    }

    @Override
    public void onAnalysisReset() {
        this.analysisInfo.reset();
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
        this.highlightNode(event.getNode());
    }

    @Override
    public void onNodeHoverReset() {
        drawText(this.selected);
        this.cleanHighlightCanvas();
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
        this.tooltipContainer.setWidth(width);
        this.tooltipContainer.setHeight(height);
        this.setFontParameters();
        this.eventBus.fireEventFromSource(new FireworksResizedEvent(width, height), this);
    }

    public void setIllustration(String url) {
        this.illustration.setUrl(url);
    }

    public void resetIllustration() {
        if (this.illustration != null) {
            this.illustration.reset();
        }
    }

    public void setColumn(int column) {
        this.analysisInfo.setColumn(column);
    }

    private Canvas createCanvas(int width, int height) {
        Canvas canvas = Canvas.createIfSupported();
        canvas.getElement().setClassName("pwp-FireworksCanvas");
        canvas.setCoordinateSpaceWidth(width);
        canvas.setCoordinateSpaceHeight(height);
        canvas.setPixelSize(width, height);
        this.add(canvas, 0, 0);
        this.canvases.add(canvas);
        return canvas;
    }

    private ToolTipContainer createToolTipContainer(int width, int height) {
        ToolTipContainer tooltipContainer = new ToolTipContainer(this.eventBus, width, height);
        this.add(tooltipContainer, 0, 0);
        return tooltipContainer;
    }

    private Set<Node> getNodeAndAncestorsWithText(Node node, boolean textForAll) {
        Set<Node> rtn = new HashSet<>();
        if (node != null) {
            rtn.add(node);
            if (textForAll) {
                rtn.addAll(node.getAncestors());
            } else {
                rtn.addAll(node.getTopLevelPathways());
            }
        }
        return rtn;
    }

    //INITIALIZE THE CANVAS taking into account the CanvasProperties
    private void setCanvasProperties(Canvas canvas, int width, int height) {
        canvas.setCoordinateSpaceWidth(width);
        canvas.setCoordinateSpaceHeight(height);
        canvas.setPixelSize(width, height);
        cleanCanvas(canvas);
    }

    private void setFontParameters() {
        this.fontSize = this.factor * MAX_FONT_SIZE / 30;
        double fontOpacity = fontSize / TRANSPARENCY_THRESHOLD;
        fontOpacity = fontOpacity > 1 ? 1 : fontOpacity;

        if (fontSize < MIN_FONT_SIZE) {
            fontSize = MIN_FONT_SIZE;
        } else if (fontSize > MAX_FONT_SIZE) {
            fontSize = MAX_FONT_SIZE;
        }

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
