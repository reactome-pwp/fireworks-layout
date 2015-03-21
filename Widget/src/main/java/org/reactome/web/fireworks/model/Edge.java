package org.reactome.web.fireworks.model;

import com.google.gwt.canvas.dom.client.Context2d;
import org.reactome.web.fireworks.interfaces.Drawable;
import org.reactome.web.fireworks.profiles.FireworksColours;
import org.reactome.web.fireworks.util.Coordinate;
import uk.ac.ebi.pwp.structures.quadtree.interfaces.QuadTreeBox;

import java.util.List;

/**
 * This object is meant to be drawn in the canvas but it is not a
 * Fireworks object
 *
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class Edge implements Drawable, QuadTreeBox {

    public static final int SEGMENT_WIDTH = 3; //TODO: Figure out the correct number
    private static final float EPSILON = SEGMENT_WIDTH * 30f;// 0.001f;

    private Node from;
    private Node to;

    private Coordinate originalControl;
    private Coordinate control;

    String colour;
    List<String> expColours;

    public Edge(Node from, Node to) {
        this.from = from;
        this.to = to;
        this.setControl();
        this.originalControl = this.control;
    }

    @Override
    public void draw(Context2d ctx) {
        ctx.beginPath();
        ctx.bezierCurveTo(from.getX(), from.getY(), control.getX(), control.getY(), to.getX(), to.getY());
        ctx.stroke();
    }

    @Override
    public void drawText(Context2d ctx, double fontSize, double space, boolean selected) {
        //Nothing here
    }

    @Override
    public void drawThumbnail(Context2d ctx, double factor) {
        Coordinate from = this.from.originalPosition.multiply(factor);
        Coordinate to = this.to.originalPosition.multiply(factor);
        Coordinate control = this.originalControl.multiply(factor);
        ctx.beginPath();
        ctx.bezierCurveTo(from.getX(), from.getY(), control.getX(), control.getY(), to.getX(), to.getY());
        ctx.stroke();
    }

    @Override
    public void highlight(Context2d ctx, double auraSize) {
        this.draw(ctx); //For the time being is the same
    }

    /**
     * Can be used either for normal visualisation, overrepresentation analysis or species comparison.
     *
     * @return the color associated with this node for normal visualisation, overrepresentation
     *         analysis or species comparison.
     */
    public String getColour() {
        return this.colour;
    }

    public String getExpressionColor(int column){
        if(this.expColours!=null){
            if( column >= 0 && column < this.expColours.size()) {
                return this.expColours.get(column);
            }
        }
        return FireworksColours.PROFILE.getEdgeFadeoutColour();
    }

    @SuppressWarnings("UnusedDeclaration")
    public Node getFrom() {
        return from;
    }

    public Node getTo() {
        return to;
    }

    public boolean isPointInEdge(Context2d ctx, Coordinate point) {
        Coordinate from = this.from.getOriginalPosition();
        Coordinate to = this.to.getOriginalPosition();
        Coordinate control = this.originalControl;
        ctx.beginPath();
        ctx.bezierCurveTo(from.getX(), from.getY(), control.getX(), control.getY(), to.getX(), to.getY());
        return ctx.isPointInPath(point.getX(), point.getY()) && !isInSegment(point);
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    //Used to set a value only when it is needed.
    public void setFadeoutColour(){
        this.colour = FireworksColours.PROFILE.getEdgeFadeoutColour();
    }

    public void setExpColours(List<String> expColours) {
        this.expColours = expColours;
    }

    public void setControl(){
        double dX = to.getX() - from.getX();
        double dY = to.getY() - from.getY();
        double angle = Math.atan2(dY, dX) - (Math.PI / 4);

        double r = Math.sqrt(dX*dX + dY*dY) / 2.0;

        double x = from.getX() + r * Math.cos(angle);
        double y = from.getY() + r * Math.sin(angle);

        this.control = new Coordinate(x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Edge edge = (Edge) o;

        if (from != null ? !from.equals(edge.from) : edge.from != null) return false;
        //noinspection RedundantIfStatement
        if (to != null ? !to.equals(edge.to) : edge.to != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = from != null ? from.hashCode() : 0;
        result = 31 * result + (to != null ? to.hashCode() : 0);
        return result;
    }

    private boolean isInSegment(Coordinate position){
        Coordinate from = this.from.getOriginalPosition();
        Coordinate to = this.to.getOriginalPosition();
        double crossProduct = crossProduct(from, to, position);
        if( Math.abs(crossProduct) > EPSILON ){
            return false;
        }

        double dotProduct = dotProduct(from, to, position);
        if( dotProduct < 0 ){
            return false;
        }

        double squaredLengthBA = squaredLengthBA(from, to);
        return ( dotProduct <= squaredLengthBA );
    }

    private double crossProduct(Coordinate a, Coordinate b, Coordinate c){
        return (c.getY() - a.getY()) * (b.getX() - a.getX()) - (c.getX() - a.getX()) * (b.getY() - a.getY());
    }

    private double dotProduct(Coordinate a, Coordinate b, Coordinate c){
        return (c.getX() - a.getX()) * (b.getX() - a.getX()) + (c.getY() - a.getY()) * (b.getY() - a.getY());
    }

    private double squaredLengthBA(Coordinate a, Coordinate b){
        return (b.getX() - a.getX()) * (b.getX() - a.getX()) + (b.getY() - a.getY()) * (b.getY() -a.getY());
    }


    // ####################################
    //  QuadTreeBox methods implementation
    // ####################################

    @Override
    public double getMinX() {
        double rtn = Math.min(from.getX(), control.getX());
        return Math.min(rtn, to.getX());
    }

    @Override
    public double getMinY() {
        double rtn = Math.min(from.getY(), control.getY());
        return Math.min(rtn, to.getY());
    }

    @Override
    public double getMaxX(){
        double rtn = Math.max(from.getX(), control.getX());
        return Math.max(rtn, to.getX());
    }

    @Override
    public double getMaxY() {
        double rtn = Math.max(from.getY(), control.getY());
        return Math.max(rtn, to.getY());
    }

}