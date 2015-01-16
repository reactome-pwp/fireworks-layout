package org.reactome.web.fireworks.client;

import com.google.gwt.event.shared.EventBus;
import org.reactome.web.fireworks.events.FireworksResizedEvent;
import org.reactome.web.fireworks.events.FireworksVisibleAreaChangedEvent;
import org.reactome.web.fireworks.events.FireworksZoomEvent;
import org.reactome.web.fireworks.events.ThumbnailAreaMovedEvent;
import org.reactome.web.fireworks.handlers.FireworksResizeEventHandler;
import org.reactome.web.fireworks.handlers.FireworksZoomEventHandler;
import org.reactome.web.fireworks.handlers.ThumbnailAreaMovedEventHandler;
import org.reactome.web.fireworks.model.Edge;
import org.reactome.web.fireworks.model.FireworksStatus;
import org.reactome.web.fireworks.model.Graph;
import org.reactome.web.fireworks.model.Node;
import org.reactome.web.fireworks.util.Coordinate;
import org.reactome.web.fireworks.util.FocusingAnimation;
import org.reactome.web.fireworks.util.MovementAnimation;
import uk.ac.ebi.pwp.structures.quadtree.interfaces.QuadTreeBox;
import uk.ac.ebi.pwp.structures.quadtree.model.Box;
import uk.ac.ebi.pwp.structures.quadtree.model.QuadTree2D;

import java.util.Set;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
class FireworksViewerManager implements MovementAnimation.FireworksZoomAnimationHandler, FocusingAnimation.FocusingAnimationHandler,
        FireworksZoomEventHandler, FireworksResizeEventHandler, ThumbnailAreaMovedEventHandler {
    static final int ZOOM_MIN = 1;
    static final int ZOOM_MAX = 100;
    static final double ZOOM_FACTOR = 0.25; //0.50;

    private EventBus eventBus;

    //The max number of elements for every QuadTree quadrant node
    static final int NUMBER_OF_ELEMENTS = 500;

    private Graph graph;

    private QuadTree2D<QuadTreeBox> quadTree;

    private double width;
    private double height;

    private FireworksStatus currentStatus;

    MovementAnimation movementAnimation;

    FireworksViewerManager(EventBus eventBus, Graph graph) {
        this.eventBus = eventBus;
        this.currentStatus = new FireworksStatus(1.0, new Coordinate(0,0));
        this.setGraph(graph);
        this.initHandlers();
    }

    @Override
    public void onFireworksResized(FireworksResizedEvent event) {
        this.width = event.getWidth();
        this.height = event.getHeight();
        this.fireFireworksVisibleAreaChangedEvent();
    }

    @Override
    public void onThumbnailAreaMoved(ThumbnailAreaMovedEvent event) {
        Coordinate delta = this.currentStatus.getDistance(event.getCoordinate());
        translate(delta.getX(), delta.getY());
    }

    @Override
    public void onFireworksZoomChanged(FireworksZoomEvent event) {
        this.setZoom(event.getStatus().getFactor(), event.getCoordinate());
    }

    public void onMouseScrolled(int delta, Coordinate coordinate) {
        double factor = this.currentStatus.getFactor() - delta * ZOOM_FACTOR;
        this.setZoom(factor, coordinate);
    }

    @Override
    public void translate(double dX, double dY){
        //IMPORTANT: trick to avoid recalculating every time drawing happens
        //           is to move the model in the normal way
        Coordinate delta = new Coordinate(dX, dY);
        for (Node node : this.graph.getNodes()) {
            node.move(delta);
        }
        for (Edge edge : this.graph.getEdges()) {
            edge.setControl();
        }

        //IMPORTANT: translation for the viewer windows goes the opposite way
        this.currentStatus.translate(delta);
        this.fireFireworksVisibleAreaChangedEvent();
    }

    @Override
    public void zoomToCoordinate(Coordinate model, Coordinate canvas, double factor){
        //DELTA is RELATIVE to the CURRENT STATUS of the model
        Coordinate delta = model.multiply(factor).minus(canvas);
        for (Node node : this.graph.getNodes()) {
            node.zoom(factor, delta);
        }
        for (Edge edge : this.graph.getEdges()) {
            edge.setControl();
        }
        //The translation is RELATIVE to the ORIGINAL model
        this.currentStatus = new FireworksStatus(factor, delta.divide(factor));

        this.eventBus.fireEventFromSource(new FireworksZoomEvent(this.currentStatus, this.width/factor, this.height/factor, canvas), this);
    }

    protected void displayNodeAndParents(Node node){
        //1- Calculate the outer box containing the node and all its parents
        double minX = node.getMinX(); double maxX = node.getMaxX();
        double minY = node.getMinY(); double maxY = node.getMaxY();
        for (Node ancestor : node.getAncestors()) {
            minX = Math.min(minX, ancestor.getMinX());
            maxX = Math.max(maxX, ancestor.getMaxX());
            minY = Math.min(minY, ancestor.getMinY());
            maxY = Math.max(maxY, ancestor.getMaxY());
        }
        //2- Growing the box a "space" bigger as the view offset
        double space = 20;
        minX -= space; minY -= space; maxX += space; maxY += space;

        //3- Calculate the
        double width = (maxX - minX);
        double height = (maxY - minY);
        double p = this.height / this.width;

        //4- Calculating proportions (and corrections for positioning)
        if(width > height){
            double aux = height;
            height = width * p;
            minY -= (height - aux)/2.0; //if wider then height and minY corrected
        }else{
            double aux = width;
            width = height / p;
            minX -= (width - aux)/2.0; //if higher then width and minX corrected
        }

        //5- Factor calculation and correction
        double factor = (this.width / width);
        if(factor<ZOOM_MIN){
            factor = ZOOM_MIN;
        }else if(factor > 3.0){
            factor = 3.0; //Never deeper than 3 (the view result is a little bit useless)
        }

        //6- Current and new positions are based in the centre of the box containing the selected element
        Coordinate centre = new Coordinate(minX + width/2.0, minY + height/2.0);
        Coordinate canvasCentre = new Coordinate(this.width/2.0, this.height/2.0);

        //7- Animates the movement
        if(this.movementAnimation!=null) this.movementAnimation.cancel();
        Coordinate currentCentre = this.currentStatus.getModelCoordinate(canvasCentre);
        this.movementAnimation = new MovementAnimation(this, currentCentre, canvasCentre, this.currentStatus.getFactor());
        this.movementAnimation.moveTo(centre, canvasCentre, factor);
    }

    //TODO: UNDER TEST!!
    public void focusNode(Node node){
        double minX = node.getMinX(); double maxX = node.getMaxX();
        double minY = node.getMinY(); double maxY = node.getMaxY();

        //3- Calculate the
        double width = (maxX - minX);
        double height = (maxY - minY);
        double p = this.height / this.width;

        //4- Calculating proportions (and corrections for positioning)
        if(width > height){
            double aux = height;
            height = width * p;
            minY -= (height - aux)/2.0; //if wider then height and minY corrected
        }else{
            double aux = width;
            width = height / p;
            minX -= (width - aux)/2.0; //if higher then width and minX corrected
        }

        double factor = (this.width / width);

        //6- Current and new positions are based in the centre of the box containing the selected element
//        Coordinate centre = new Coordinate(minX + width/2.0, minY + height/2.0);
        Coordinate canvasCentre = new Coordinate(this.width/2.0, this.height/2.0);

        //7- Animates the movement
//        if(this.animation!=null) this.animation.cancel();
//        Coordinate currentCentre = this.currentStatus.getCanvasCoordinate();
        FocusingAnimation uff = new FocusingAnimation(this, node.getCurrentPosition(), this.currentStatus.getFactor());
        uff.moveTo(canvasCentre, factor);

    }

    protected Node getHoveredNode(Coordinate mouse){
        Coordinate c = this.currentStatus.getModelCoordinate(mouse);
        double f = 1 / this.currentStatus.getFactor();
        //TODO: Test if the following strategy currently works
//        for (QuadTreeBox item : quadTree.getItems(c.getX(), c.getY())) {
//            if(item instanceof Node){
//                return (Node) item;
//            }
//        }
        for (QuadTreeBox item : quadTree.getItems(new Box(c.getX()-f, c.getY()-f, c.getX()+f, c.getY()+f))) {
            if(item instanceof Node){
                return (Node) item;
            }
        }
        return null;
    }

    protected Set<QuadTreeBox> getVisibleElements(){
        return this.quadTree.getItems(this.currentStatus.getVisibleModelArea(this.width, this.height));
    }

    protected boolean isNodeVisible(Node node){
        if(node==null) return false;
        for (QuadTreeBox item : getVisibleElements()) {
            if(item instanceof Node){
                Node target = (Node) item;
                if(target.equals(node)) return true;
            }
        }
        return false;
    }

    @Override
    public void focusZoom(double factor, Coordinate mouse) {
        if(factor==this.currentStatus.getFactor()) return;

        Coordinate model = this.currentStatus. getModelCoordinate(mouse);
        this.zoomToCoordinate(model, mouse, factor);
    }

    protected void setGraph(Graph graph){
        this.graph = graph;
        this.quadTree = new QuadTree2D<QuadTreeBox>(this.graph.getMaxX(), this.graph.getMaxY(), NUMBER_OF_ELEMENTS);
        for (Node node : this.graph.getNodes()) {
            this.quadTree.add(node);
        }
        for (Edge edge : this.graph.getEdges()) {
            this.quadTree.add(edge);
        }
    }

    protected void setZoom(double factor, Coordinate mouse) {
        if(factor<ZOOM_MIN){
            factor = ZOOM_MIN;
        }else if(factor > ZOOM_MAX){
            factor = ZOOM_MAX;
        }

        if(factor==this.currentStatus.getFactor()) return;

        Coordinate model = this.currentStatus. getModelCoordinate(mouse);
        this.zoomToCoordinate(model, mouse, factor);
    }

    private void fireFireworksVisibleAreaChangedEvent(){
        Coordinate offset = this.currentStatus.getOffset();
        double w = this.width / this.currentStatus.getFactor();
        double h = this.height / this.currentStatus.getFactor();
        this.eventBus.fireEventFromSource(new FireworksVisibleAreaChangedEvent(offset, w, h), this);
    }

    private void initHandlers(){
        this.eventBus.addHandler(FireworksResizedEvent.TYPE, this);
        this.eventBus.addHandler(ThumbnailAreaMovedEvent.TYPE, this);
    }
}