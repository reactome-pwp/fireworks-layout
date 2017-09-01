package org.reactome.web.fireworks.client;

import com.google.gwt.animation.client.AnimationScheduler;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Touch;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import org.reactome.web.analysis.client.AnalysisClient;
import org.reactome.web.analysis.client.AnalysisHandler;
import org.reactome.web.analysis.client.model.AnalysisError;
import org.reactome.web.analysis.client.model.AnalysisType;
import org.reactome.web.analysis.client.model.SpeciesFilteredResult;
import org.reactome.web.fireworks.controls.navigation.ControlAction;
import org.reactome.web.fireworks.events.*;
import org.reactome.web.fireworks.handlers.*;
import org.reactome.web.fireworks.model.Edge;
import org.reactome.web.fireworks.model.FireworksData;
import org.reactome.web.fireworks.model.Graph;
import org.reactome.web.fireworks.model.Node;
import org.reactome.web.fireworks.model.factory.ModelException;
import org.reactome.web.fireworks.model.factory.ModelFactory;
import org.reactome.web.fireworks.search.fallback.events.SuggestionHoveredEvent;
import org.reactome.web.fireworks.search.fallback.events.SuggestionSelectedEvent;
import org.reactome.web.fireworks.search.fallback.handlers.SuggestionHoveredHandler;
import org.reactome.web.fireworks.search.fallback.handlers.SuggestionSelectedHandler;
import org.reactome.web.fireworks.search.searchonfire.graph.model.GraphEntry;
import org.reactome.web.fireworks.util.Coordinate;
import org.reactome.web.fireworks.util.FireworksEventBus;
import org.reactome.web.fireworks.util.flag.Flagger;
import org.reactome.web.pwp.model.client.classes.Pathway;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
class FireworksViewerImpl extends ResizeComposite implements FireworksViewer,
        MouseDownHandler, MouseMoveHandler, MouseUpHandler, MouseOutHandler, MouseWheelHandler,
        TouchStartHandler, TouchEndHandler, TouchMoveHandler, TouchCancelHandler,
        FireworksVisibleAreaChangedHandler, FireworksZoomHandler, ClickHandler, /*DoubleClickHandler,*/
        AnalysisResetHandler, ExpressionColumnChangedHandler,
        ControlActionHandler, ProfileChangedHandler,
        SuggestionSelectedHandler, SuggestionHoveredHandler,
        IllustrationSelectedHandler, CanvasExportRequestedHandler,
        KeyDownHandler, SearchFilterHandler, SearchResetHandler,
        GraphEntryHoveredHandler, GraphEntrySelectedHandler,
        NodeFlaggedResetHandler {

    private EventBus eventBus;

    private FireworksViewerManager manager;

    private FireworksCanvas canvases;

    private FireworksData data;

    private String token;

    private String resource;

    private boolean forceFireworksDraw = true;

    // mouse positions relative to canvas (not the model)
    // Do not assign the same value at the beginning
    private Coordinate mouseCurrent = new Coordinate(-100, -100);
    private Coordinate mousePrevious = new Coordinate(-200, -200);

    private Coordinate mouseDown = null;
    private boolean fireworksMoved = false;
    private Double fingerDistance;

    private Node hovered = null;
    private Node selected = null;
    private Set<Node> nodesToFlag = null;
    private Set<Edge> edgesToFlag = null;

    FireworksViewerImpl(String json) {
        this.eventBus = new FireworksEventBus();
        try {
            Graph graph = ModelFactory.getGraph(json);
            this.data = new FireworksData(graph);
            this.canvases = new FireworksCanvas(eventBus, graph); //Graph needed for the thumbnail
            this.manager = new FireworksViewerManager(eventBus, graph);
            initWidget(canvases);
        } catch (FireworksCanvas.CanvasNotSupportedException e) {
            initWidget(new Label("Canvas not supported"));
            fireEvent(new CanvasNotSupportedEvent());
        } catch (ModelException e) {
            initWidget(new Label(e.getMessage()));
            e.printStackTrace();
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    public FireworksViewerImpl() {
        initialize();
    }

    @Override
    public HandlerRegistration addAnalysisResetHandler(AnalysisResetHandler handler) {
        return this.eventBus.addHandler(AnalysisResetEvent.TYPE, handler);
    }

    @Override
    public HandlerRegistration addCanvasNotSupportedHandler(CanvasNotSupportedHandler handler){
        return this.eventBus.addHandler(CanvasNotSupportedEvent.TYPE, handler);
    }

    @Override
    public HandlerRegistration addFireworksLoaded(FireworksLoadedHandler handler) {
        return this.eventBus.addHandler(FireworksLoadedEvent.TYPE, handler);
    }

    @Override
    public HandlerRegistration addExpressionColumnChangedHandler(ExpressionColumnChangedHandler handler) {
        return this.eventBus.addHandler(ExpressionColumnChangedEvent.TYPE, handler);
    }

    @Override
    public HandlerRegistration addNodeHoverHandler(NodeHoverHandler handler){
        return this.eventBus.addHandler(NodeHoverEvent.TYPE, handler);
    }

    @Override
    public HandlerRegistration addNodeHoverResetHandler(NodeHoverResetHandler handler) {
        return this.eventBus.addHandler(NodeHoverResetEvent.TYPE, handler);
    }

    @Override
    public HandlerRegistration addNodeFlaggedResetHandler(NodeFlaggedResetHandler handler){
        return this.eventBus.addHandler(NodeFlaggedResetEvent.TYPE, handler);
    }

    @Override
    public HandlerRegistration addNodeOpenedHandler(NodeOpenedHandler handler) {
        return this.eventBus.addHandler(NodeOpenedEvent.TYPE, handler);
    }

    @Override
    public HandlerRegistration addNodeSelectedHandler(NodeSelectedHandler handler) {
        return this.eventBus.addHandler(NodeSelectedEvent.TYPE, handler);
    }

    @Override
    public HandlerRegistration addNodeSelectedResetHandler(NodeSelectedResetHandler handler) {
        return this.eventBus.addHandler(NodeSelectedResetEvent.TYPE, handler);
    }

    @Override
    public HandlerRegistration addProfileChangedHandler(ProfileChangedHandler handler) {
        return this.eventBus.addHandler(ProfileChangedEvent.TYPE, handler);
    }


    @Override
    public Node getSelected() {
        return this.selected;
    }

    @Override
    public void flagItems(String identifier) {
        if (identifier == null || identifier.isEmpty()) {
            resetFlaggedItems();
        } else {
            eventBus.fireEventFromSource(new NodeFlagRequestedEvent(identifier), this);

            Flagger.findPathwaysToFlag(identifier, data.getSpeciesId(), new Flagger.PathwaysToFlagHandler() {
                @Override
                public void onPathwaysToFlag(List<Pathway> result) {
                    Set<Edge> edgesToFlag = new HashSet<>();
                    Set<Node> nodesToFlag = new HashSet<>();
                    for (Pathway pathway : result) {
                        Node node = data.getNode(pathway.getDbId());
                        if (node != null){
                            nodesToFlag.add(node);
                            nodesToFlag.addAll(node.getAncestors());
                        }
                    }
                    for (Node node : nodesToFlag) {
                        edgesToFlag.addAll(node.getEdgesTo());
                    }
                    setFlaggedElements(identifier, nodesToFlag, edgesToFlag);
                }

                @Override
                public void onPathwaysToFlagError() {
                    //TODO: Nothing to flag
                }
            });
        }
    }

    @Override
    public void flagNodes(String term, String... stIds) {
        eventBus.fireEventFromSource(new NodeFlagRequestedEvent(term), this);
        Set<Node> toFlag = new HashSet<>();
        for (String stId : stIds) {
            Node node = data.getNode(stId);
            if (node != null) toFlag.add(node);
        }
        setFlaggedElements(term, toFlag, new HashSet<>());
    }

    @Override
    public void highlightNode(String stableIdentifier) {
        Node node = this.data.getNode(stableIdentifier); if(node==null) return;
        this.setHoveredNode(node);
    }

    @Override
    public void highlightNode(Long dbIdentifier) {
        Node node = this.data.getNode(dbIdentifier); if(node==null) return;
        this.setHoveredNode(node);
    }

    @Override
    public void openPathway(String stableIdentifier) {
        Node node = this.data.getNode(stableIdentifier); if(node==null) return;
        this.selectNode(node, false); this.openNode(node);
    }

    @Override
    public void openPathway(Long dbIdentifier) {
        Node node = this.data.getNode(dbIdentifier); if(node==null) return;
        this.selectNode(node, false); this.openNode(node);
    }

    @Override
    public void onResize() {
        if(!isVisible()) return;
        super.onResize();
        this.forceFireworksDraw = true;
    }

    @Override
    public void onFireworksVisibleAreaChanged(FireworksVisibleAreaChangedEvent event) {
        //TODO: We need a way to specify the redraw is because of canvas is moving and not zooming
        this.forceFireworksDraw = true;
    }

    @Override
    public void onFireworksZoomChanged(FireworksZoomEvent event) {
        this.forceFireworksDraw = true;
    }

    @Override
    public void onAnalysisReset() {
        this.token = this.resource = null;
        this.data.resetPathwaysAnalysisResult();
        this.forceFireworksDraw = true;
    }

    @Override
    public void onExpressionColumnChanged(ExpressionColumnChangedEvent e) {
        this.canvases.setColumn(e.getColumn()); //First the column needs to be set in the canvases
        this.forceFireworksDraw = true;
    }

    @Override
    public void onControlAction(ControlActionEvent event) {
        switch (event.getAction()){
            case FIT_ALL:   this.manager.displayAllNodes(true); break;
            case ZOOM_IN:   this.manager.zoom(0.25);            break;
            case ZOOM_OUT:  this.manager.zoom(-0.25);           break;
            case UP:        this.manager.translate(0, 10);      break;
            case RIGHT:     this.manager.translate(-10, 0);     break;
            case DOWN:      this.manager.translate(0, -10);     break;
            case LEFT:      this.manager.translate(10, 0);      break;
            case OPEN:      this.openNode(this.selected);       break;
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        event.stopPropagation(); event.preventDefault();
        Node clickedNode = this.manager.getHoveredNode(mouseCurrent);

        if(clickedNode!=null){
            //After a usability testing it was seen people hardly double
            //clicked the nodes, so when the already selected node is
            //clicked again, it automatically expands the node
            if(clickedNode.equals(this.selected)){
                this.manager.expandNode(this.selected);
            } else{
                this.selectNode(clickedNode, false);
            }
        }else{
            if(!this.fireworksMoved && this.selected!=null){
                this.selectNode(null, false);
            }
        }
        this.fireworksMoved = false;
    }

    @Override
    public void onDiagramExportRequested(CanvasExportRequestedEvent event) {
        this.canvases.exportImage(data.getSpeciesId().toString());
    }

    @Override
    public void onGraphEntryHovered(GraphEntryHoveredEvent event) {
        if(event.getHoveredEntry()!=null) {
            highlightNode(event.getHoveredEntry().getStId());
        } else {
            resetHighlight();
        }
    }

    @Override
    public void onGraphEntrySelected(GraphEntrySelectedEvent event) {
        selectNode(event.getSelectedEntry().getStId());
    }

    @Override
    public void onIllustrationSelected(IllustrationSelectedEvent event) {
        this.canvases.setIllustration(event.getUrl());
    }

    @Override
    public void onKeyDown(KeyDownEvent keyDownEvent) {
        if (isVisible() && FireworksFactory.RESPOND_TO_SEARCH_SHORTCUT) {
            int keyCode = keyDownEvent.getNativeKeyCode();
            String platform = Window.Navigator.getPlatform();
            // If this is a Mac, check for the cmd key. In case of any other platform, check for the ctrl key
            boolean isModifierKeyPressed = platform.toLowerCase().contains("mac") ? keyDownEvent.isMetaKeyDown() : keyDownEvent.isControlKeyDown();
            if (keyCode == KeyCodes.KEY_F && isModifierKeyPressed) {
                keyDownEvent.preventDefault();
                keyDownEvent.stopPropagation();
                eventBus.fireEventFromSource(new SearchKeyPressedEvent(), this);
            }
        }
    }

    @Override
    public void onNodeFlaggedReset() {
        this.nodesToFlag = null;
        forceFireworksDraw = true;
    }

    @Override
    public void onMouseDown(MouseDownEvent event) {
        event.stopPropagation(); event.preventDefault();

        this.fireworksMoved = false;
        setMouseDownPosition(event.getRelativeElement(), event);
    }

    @Override
    public void onMouseMove(MouseMoveEvent event) {
        event.stopPropagation(); event.preventDefault();
        if(mouseDown!=null){
            this.fireworksMoved = true;
            translateGraphObjects(event.getRelativeElement(), event);
            setMouseDownPosition(event.getRelativeElement(), event);
            forceFireworksDraw = true;
        }else {
            setMousePosition(event.getRelativeElement(), event);
        }
    }

    @Override
    public void onMouseOut(MouseOutEvent event) {
        event.stopPropagation(); event.preventDefault();
        this.fireworksMoved = false;
        mouseDown = null;
        mouseCurrent = new Coordinate(-200, -200);
    }

    @Override
    public void onMouseUp(MouseUpEvent event) {
        event.stopPropagation(); event.preventDefault();
        mouseDown = null;
        setMousePosition(event.getRelativeElement(), event);
    }

    @Override
    public void onMouseWheel(MouseWheelEvent event) {
        event.stopPropagation(); event.preventDefault();
        Element element = event.getRelativeElement();
        Coordinate mouse = new Coordinate(event.getRelativeX(element), event.getRelativeY(element));
        this.manager.onMouseScrolled(event.getDeltaY(), mouse);
    }

    @Override
    public void onProfileChanged(ProfileChangedEvent event) {
        data.updateColours();
        forceFireworksDraw = true;
    }

    @Override
    public void onSuggestionHovered(SuggestionHoveredEvent event) {
        if (event.getHoveredObject() != null) {
            if (!event.getToFocus()) {
                this.highlightNode(event.getHoveredObject().getDbId()); // Simply highlight the node
            } else {
                this.selectNode(event.getHoveredObject(), true);        // Select and focus on the node
            }
        }
    }

    @Override
    public void onSuggestionSelected(SuggestionSelectedEvent event) {
        if (event.getSelectedObject() != null) {
            if (!event.getToOpen()) {
                this.highlightNode(event.getSelectedObject().getDbId());// Simply highlight the node
            } else {
                this.selectNode(event.getSelectedObject(), false);      // Expand the node
                this.openNode(event.getSelectedObject());
            }
        }
    }

    @Override
    public void onSearchFilterEvent(SearchFilterEvent event) {
        Set<Node> filteredNodes = new HashSet<>();
        for (GraphEntry graphEntry : event.getResult()) {
            Node node = data.getNode(graphEntry.getStId());
            if(node!=null) {
                filteredNodes.add(node);
            }
        }
        data.setPathwaysFilteredResult(filteredNodes);
        manager.displayNodesAndParents(filteredNodes);
        this.forceFireworksDraw = true;
    }

    @Override
    public void onSearchReset(SearchResetEvent event) {
        data.resetPathwaysFiltered();
        manager.displayAllNodes(true);
        resetHighlight();
        resetSelection();
        this.forceFireworksDraw = true;
    }

    @Override
    public void onTouchCancel(TouchCancelEvent event) {
        event.stopPropagation(); event.preventDefault();
        mouseDown = null;
        fireworksMoved = false;
        fingerDistance = null;
    }

    @Override
    public void onTouchEnd(TouchEndEvent event) {
        event.stopPropagation(); event.preventDefault();
        if (!fireworksMoved) {
            setMousePosition(getTouchCoordinate(event.getRelativeElement(), event.getChangedTouches().get(0)));
            Node clickedNode = this.manager.getHoveredNode(mouseCurrent);

            if (clickedNode != null) {
                if( clickedNode.equals(selected)) {
                    this.manager.expandNode(selected);
                } else {
                    this.selectNode(clickedNode, false);
                }
            } else {
                if (this.selected != null) {
                    this.selectNode(null, false);
                }
            }
        }
        mouseDown = null;
        fireworksMoved = false;
        fingerDistance = null;
    }

    @Override
    public void onTouchMove(TouchMoveEvent event) {
        event.stopPropagation(); event.preventDefault();
        int numberOfTouches =  event.getTouches().length();
        if (numberOfTouches == 1) {
            Coordinate finger = getTouchCoordinate(event.getRelativeElement(), event.getTouches().get(0)); // Get the first touch
            if (mouseDown == null) {
                setMouseDownPosition(finger);
            } else {
                Coordinate delta = finger.minus(this.mouseDown);
                // On mouse move is sometimes called for delta 0 (we cannot control that, but only consider it)
                if (isDeltaValid(delta)) {
                    this.manager.translate(delta.getX(), delta.getY());
                    forceFireworksDraw = true;
                    setMouseDownPosition(finger);
                    this.fireworksMoved = true;                               //Selection is denied in case of panning
                }
            }
        } else if (numberOfTouches == 2){
            Coordinate finger1 = getTouchCoordinate(event.getRelativeElement(), event.getTouches().get(0));
            Coordinate finger2 = getTouchCoordinate(event.getRelativeElement(), event.getTouches().get(1));
            Coordinate delta = finger2.minus(finger1);
            Double newFingerDistance = Math.sqrt(delta.getX() * delta.getX() + delta.getY() * delta.getY());
            int deltaFactor = (int) ((fingerDistance - newFingerDistance) * 0.25);
            this.manager.onMouseScrolled(deltaFactor, finger1.add(delta.divide(2)));

            this.fingerDistance = newFingerDistance;
            this.fireworksMoved = true;                       //Selection is denied in case of zooming
        }
    }

    @Override
    public void onTouchStart(TouchStartEvent event) {
        event.stopPropagation(); event.preventDefault();
        int numberOfTouches =  event.getTouches().length();
        if (numberOfTouches == 1) {
            Coordinate finger = getTouchCoordinate(event.getRelativeElement(), event.getTouches().get(0)); // Get the first touch
            setMouseDownPosition(finger);
            this.fireworksMoved = false;                                                  //Selection is denied in case of zooming
        } else if (numberOfTouches == 2){
            Coordinate finger1 = getTouchCoordinate(event.getRelativeElement(), event.getTouches().get(0));
            Coordinate finger2 = getTouchCoordinate(event.getRelativeElement(), event.getTouches().get(1));
            Coordinate delta = finger2.minus(finger1);
            fingerDistance = Math.sqrt(delta.getX() * delta.getX() + delta.getY() * delta.getY());
        }
    }
    @Override
    public void resetHighlight() {
        this.setHoveredNode(null);
    }

    @Override
    public void resetSelection() {
        this.selectNode(null, false);
    }

    @Override
    public void selectNode(String stableIdentifier) {
        this.selectNode(this.data.getNode(stableIdentifier), true);
    }

    @Override
    public void selectNode(Long dbIdentifier) {
        this.selectNode(this.data.getNode(dbIdentifier), true);
    }

    @Override
    public void setAnalysisToken(String token, final String resource){
        if(token==null || resource==null) return;
        if(Objects.equals(this.token, token) && Objects.equals(this.resource, resource)) return;
        this.token = token; this.resource = resource;

        this.canvases.onAnalysisReset();
        this.data.resetPathwaysAnalysisResult();
        AnalysisClient.filterResultBySpecies(token, resource, this.data.getSpeciesId(), new AnalysisHandler.Pathways() {
            @Override
            public void onPathwaysSpeciesFiltered(SpeciesFilteredResult result) {
                result.setAnalysisType(AnalysisType.getType(result.getType()));
                data.setPathwaysAnalysisResult(result); //Data has to be set in the first instance
                eventBus.fireEventFromSource(new AnalysisPerformedEvent(result), FireworksViewerImpl.this);
                forceFireworksDraw = true;
            }

            @Override
            public void onPathwaysSpeciesError(AnalysisError error) {
                forceFireworksDraw = true;
            }

            @Override
            public void onAnalysisServerException(String message) {
                forceFireworksDraw = true;
            }
        });
    }

    @Override
    public void showAll() {
        this.eventBus.fireEventFromSource(new ControlActionEvent(ControlAction.FIT_ALL), this);
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) onResize();
    }

    @Override
    public void resetAnalysis() {
        this.token = null; this.resource=null;
        eventBus.fireEventFromSource(new AnalysisResetEvent(), this);
    }

    @Override
    public void resetFlaggedItems() {
        this.setFlaggedElements(null, null, null);
    }

    private void doUpdate(){
        this.doUpdate(false);
    }

    @SuppressWarnings("SameParameterValue")
    private void doUpdate(boolean force){
        if(this.forceFireworksDraw){
            this.forceFireworksDraw = false;
            this.drawFireworks();
            return;
        }
        if(force || !mouseCurrent.equals(mousePrevious)){
            Node node = this.manager.getHoveredNode(mouseCurrent);
            if(node==null){
                if(this.hovered!=null){
                    this.hovered = null;
                    canvases.getTopCanvas().getElement().getStyle().setCursor(Style.Cursor.DEFAULT);
                    this.eventBus.fireEventFromSource(new NodeHoverResetEvent(), this);
                }
            }else{
                canvases.getTopCanvas().getElement().getStyle().setCursor(Style.Cursor.POINTER);
                if(!node.equals(this.hovered)){
                    this.hovered = node;
                    if(!node.equals(this.selected)) {
                        this.eventBus.fireEventFromSource(new NodeHoverEvent(node), this);
                    }
                }
            }
        }
        this.mousePrevious = this.mouseCurrent;
    }

    private void drawFireworks(){
        this.canvases.drawElements(this.manager.getVisibleElements());
        this.canvases.drawText(this.selected);
        this.canvases.selectNode(this.selected);
        this.canvases.highlightNode(this.hovered);
        this.canvases.flagElements(this.nodesToFlag, this.edgesToFlag);
    }

    private Coordinate getTouchCoordinate(Element element, Touch touch) {
        return new Coordinate(touch.getRelativeX(element), touch.getRelativeY(element));
    }

    @Override
    protected void initWidget(Widget widget) {
        super.initWidget(widget);
        //We need to defer the program counter to the parents in order to finish DOM tasks
        Scheduler.get().scheduleDeferred(this::initialize);
    }

    private void initialize(){
        this.onResize(); // Adjusts size before rendering the Fireworks for the first time
        this.initHandlers();
        this.forceFireworksDraw = true; //IMPORTANT! Do NOT place it inside the scheduler class
        this.manager.displayAllNodes(false);
        this.eventBus.fireEventFromSource(new FireworksLoadedEvent(this.data.getSpeciesId()), this);
        AnimationScheduler.get().requestAnimationFrame(new AnimationScheduler.AnimationCallback() {
            @Override
            public void execute(double timestamp) {
                doUpdate();
                AnimationScheduler.get().requestAnimationFrame(this); // Call it again.
            }
        });
    }

    protected void initHandlers() {
        //Attaching this as a KeyDownHandler
        RootPanel.get().addDomHandler(this, KeyDownEvent.getType());

        this.canvases.addClickHandler(this);
        this.canvases.addMouseDownHandler(this);
        this.canvases.addMouseMoveHandler(this);
        this.canvases.addMouseOutHandler(this);
        this.canvases.addMouseUpHandler(this);
        this.canvases.addMouseWheelHandler(this);

        //Registration of touch events
        this.canvases.addTouchStartHandler(this);
        this.canvases.addTouchMoveHandler(this);
        this.canvases.addTouchEndHandler(this);
        this.canvases.addTouchCancelHandler(this);

        this.eventBus.addHandler(ControlActionEvent.TYPE, this);
        this.eventBus.addHandler(AnalysisResetEvent.TYPE, this);
        this.eventBus.addHandler(ExpressionColumnChangedEvent.TYPE, this);
        this.eventBus.addHandler(FireworksVisibleAreaChangedEvent.TYPE, this);
        this.eventBus.addHandler(FireworksZoomEvent.TYPE, this);
        this.eventBus.addHandler(IllustrationSelectedEvent.TYPE, this);
        this.eventBus.addHandler(NodeFlaggedResetEvent.TYPE, this);
        this.eventBus.addHandler(ProfileChangedEvent.TYPE, this);
        this.eventBus.addHandler(CanvasExportRequestedEvent.TYPE, this);

        this.eventBus.addHandler(SuggestionSelectedEvent.TYPE, this);
        this.eventBus.addHandler(SuggestionHoveredEvent.TYPE, this);
        this.eventBus.addHandler(SearchFilterEvent.TYPE, this);
        this.eventBus.addHandler(SearchResetEvent.TYPE, this);
        this.eventBus.addHandler(GraphEntryHoveredEvent.TYPE, this);
        this.eventBus.addHandler(GraphEntrySelectedEvent.TYPE, this);
    }

    private boolean isDeltaValid(Coordinate delta) {
        return delta.getX() >= 4  || delta.getX() <= -4  || delta.getY() >= 4 || delta.getY() <= -4;
    }

    private void openNode(Node node){
        if(node!=null){
            this.manager.expandNode(node);
        }
    }

    private void setMouseDownPosition(Element element, MouseEvent event){
        this.mouseDown = new Coordinate(event.getRelativeX(element), event.getRelativeY(element));
    }

    private void setMouseDownPosition(Coordinate coordinate) {
        this.mouseDown = coordinate;
    }

    private void setMousePosition(Element element, MouseEvent event) {
        this.mouseCurrent = new Coordinate(event.getRelativeX(element), event.getRelativeY(element));
    }

    private void setMousePosition(Coordinate coordinate) {
        this.mouseCurrent = coordinate;
    }

    private void translateGraphObjects(Element element, MouseEvent event){
        double dX = event.getRelativeX(element) - mouseDown.getX();
        double dY = event.getRelativeY(element) - mouseDown.getY();
        this.manager.translate(dX, dY);
    }

    private void selectNode(Node toSelect, boolean displayNodeAndParents){
        setHoveredNode(null);
        if(toSelect!=null){
            if(displayNodeAndParents) {
                this.manager.displayNodeAndParents(toSelect);
            }
            if(!toSelect.equals(this.selected)) {
                this.selected = toSelect;
                //Note: the selection happens because other classes are listening to this event
                this.eventBus.fireEventFromSource(new NodeSelectedEvent(this.selected), this);
            }
        }else{
            if(this.selected!=null) {
                this.selected = null;
                this.eventBus.fireEventFromSource(new NodeSelectedResetEvent(), this);
            }
        }
    }

    private void setHoveredNode(Node toHighlight){
        if(toHighlight!=null){
            if(!toHighlight.equals(this.hovered) && !toHighlight.equals(this.selected)){
                this.hovered = toHighlight;
                //Note: the highlighting happens because other classes are listening to this event
                this.eventBus.fireEventFromSource(new NodeHoverEvent(this.hovered), this);
            }
        }else{
            if(this.hovered!=null){
                this.hovered = null;
                this.eventBus.fireEventFromSource(new NodeHoverResetEvent(), this);
            }
        }
    }

    private void setFlaggedElements(String term, Set<Node> nodesToFlag, Set<Edge> edgesToFlag) {
        if (nodesToFlag == null || nodesToFlag.isEmpty()) {
            this.nodesToFlag = new HashSet<>();
            this.edgesToFlag = new HashSet<>();
        } else {
            this.nodesToFlag = new HashSet<>(nodesToFlag);
            this.edgesToFlag = new HashSet<>(edgesToFlag);
        }
        this.eventBus.fireEventFromSource(new NodeFlaggedEvent(term, this.nodesToFlag), this);
        forceFireworksDraw = true;
    }
}