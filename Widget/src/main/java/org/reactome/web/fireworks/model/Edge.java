package org.reactome.web.fireworks.model;

import com.google.gwt.canvas.dom.client.Context2d;
import org.reactome.web.fireworks.interfaces.Drawable;
import org.reactome.web.fireworks.util.Coordinate;
import uk.ac.ebi.pwp.structures.quadtree.interfaces.QuadTreeBox;

/**
 * This object is meant to be drawn in the canvas but it is not a
 * Fireworks object
 *
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class Edge implements Drawable, QuadTreeBox {

    private Node from;
    private Node to;

    private Coordinate originalControl;
    private Coordinate control;

    public Edge(Node from, Node to) {
        this.from = from;
        this.to = to;
        this.setControl();
        this.originalControl = this.control;
    }

    @Override
    public void draw(Context2d ctx) {
        ctx.beginPath();
//        ctx.moveTo(from.getX(), from.getY());
//        ctx.lineTo(to.getX(), to.getY());
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
//        ctx.moveTo(from.getX(), from.getY());
//        ctx.lineTo(to.getX(), to.getY());
        ctx.bezierCurveTo(from.getX(), from.getY(), control.getX(), control.getY(), to.getX(), to.getY());
        ctx.stroke();
    }

    @Override
    public void highlitght(Context2d ctx, double auraSize) {
        this.draw(ctx); //For the time being is the same
    }

    public Node getFrom() {
        return from;
    }

    public Node getTo() {
        return to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Edge edge = (Edge) o;

        if (from != null ? !from.equals(edge.from) : edge.from != null) return false;
        if (to != null ? !to.equals(edge.to) : edge.to != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = from != null ? from.hashCode() : 0;
        result = 31 * result + (to != null ? to.hashCode() : 0);
        return result;
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

    public void setControl(){
        double dX = to.getX() - from.getX();
        double dY = to.getY() - from.getY();
        double angle = Math.atan2(dY, dX) - (Math.PI / 4);

        double r = Math.sqrt(dX*dX + dY*dY) / 2.0;

        double x = from.getX() + r * Math.cos(angle);
        double y = from.getY() + r * Math.sin(angle);

        this.control = new Coordinate(x, y);
    }

}