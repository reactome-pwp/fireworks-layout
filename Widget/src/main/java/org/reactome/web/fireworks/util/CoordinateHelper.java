package org.reactome.web.fireworks.util;

/**
 * When the user zooms in or out the viewport, the mouse position coordinates
 * need to be translated into the original location. Note the opposite is also
 * needed when drawing objects in a zoomed viewport
 *
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class CoordinateHelper {
    private final Double mW;  // Original model width
    private final Double mH;  // Original model height

    private double cW;  // Current widget width
    private double cH;  // Current widget height
    private double tX;  // Translation in the x edge
    private double tY;  // Translation in the y edge
    private double zoom;  //The zoom level

    public CoordinateHelper(double width, double height) {
        this.mW = this.cW = width;  // We initially suppose the widget width is the same as the model
        this.mH = this.cH = height; // We initially suppose the widget height is the same as the model
        this.tX = this.tY = 0d;     // No translation
        this.zoom = 1d;             // No zoom
    }

    /**
     * For a given coordinate in the widget it returns the corresponding
     * model coordinate taking into account the current status
     * @param coordinate a widget coordinate
     * @return the corresponding model coordinate taking into account the current status
     */
    public Coordinate getOriginalCoordinate(Coordinate coordinate){
        double x = this.tX + this.cW / this.mW * coordinate.getX();
        double y = this.tY + this.cH / this.mH * coordinate.getY();
        return new Coordinate(x,y);
    }

    /**
     * For a given original coordinate and the current status it returns
     * the corresponding coordinate
     * @param original the original model coordinate
     * @return the corresponding coordinate taking into account the current status
     */
    public Coordinate getTranslatedCoordinate(Coordinate original){
        double x = original.getX() * this.zoom - this.tX;
        double y = original.getY() * this.zoom - this.tY;
        return new Coordinate(x, y);
    }

    /**
     * Zoom the viewport centered in a certain position
     * @param coordinate position coordinate
     * @param zoom zoom factor
     */
    public void zoom(Coordinate coordinate, double zoom){
        this.cW = this.mW * zoom;
        this.cH = this.mH * zoom;
        this.tX = coordinate.getX() * zoom / 2d;
        this.tY = coordinate.getY() * zoom / 2d;
        this.zoom = zoom;
    }

    /**
     * Zoom into a user selected area
     * @param coordinate starting point coordinate
     * @param width the width of the selected area
     * @param height the height of the selected area
     */
    public void zoom(Coordinate coordinate, double width, double height){
        //The zoom for each edge is calculated
        double heightZoom = this.mW / width * this.zoom;
        double widthZoom = this.mH / height * this.zoom;
        //The lower zoom is kept to ensure everything in the selected area is shown after zooming
        double zoom = Math.min(heightZoom, widthZoom);
        //X and Y need to be recalculated as the center of the new visible area
        double x = coordinate.getX() + width / 2d; double y = coordinate.getY() + height / 2d;
        zoom(new Coordinate(x,y), zoom); //Now the "basic" zoom method can be used
    }
}
