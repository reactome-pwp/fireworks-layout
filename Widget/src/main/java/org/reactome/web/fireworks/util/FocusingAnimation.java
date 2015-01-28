package org.reactome.web.fireworks.util;

import com.google.gwt.animation.client.Animation;

/**
 * First centers the node and then apply the zoom until it fits the visible area
 *
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 *
 * @deprecated because a way keep the context while zooming completely in is needed
 */
@Deprecated
public class FocusingAnimation extends Animation {

    public interface FocusingAnimationHandler {
        public void translate(double dX, double dY);
        public void focusZoom(double factor, Coordinate mouse);
        public void focusFinished();
    }

    /**
     * The maximum duration of the animation.
     */
    private static final int MAX_ANIMATION_DURATION = 2000;

    private FocusingAnimationHandler handler;

    private Coordinate currentCanvasPoint;
    private double currentFactor;

    private Coordinate targetCanvasPoint;
    private boolean targetPositionReached;
    private double targetFactor;

    private boolean canceled;

    public FocusingAnimation(FocusingAnimationHandler handler, Coordinate canvasPoint, double factor) {
        this.handler = handler;
        this.currentCanvasPoint = canvasPoint;
        this.currentFactor = factor;
        this.canceled = false;
    }

    public void moveTo(Coordinate targetCanvasPoint, double factor){
        int time = time(distance(this.currentCanvasPoint, targetCanvasPoint));
        this.moveTo(targetCanvasPoint, factor, time);
    }

    public void moveTo(Coordinate targetCanvasPoint, double factor, int time){
        this.canceled = false;
        this.targetCanvasPoint = targetCanvasPoint;
        this.targetFactor = factor;

        if(time > 0) { //If no need to move time is zero because distance is zero :)
            run(time); //DO NOT RUN THIS WHEN TIME IS ZERO
        }
    }

    @Override
    protected void onCancel() {
        this.canceled = true;
        super.onCancel();
    }

    @Override
    protected void onComplete() {
        if(!canceled){
            super.onComplete(); //By avoiding the call to "super" if cancelled, a composition of movement is created
            this.handler.focusFinished();
        }
    }

    @Override
    protected void onUpdate(double progress) {
        if(!targetPositionReached) {
            Coordinate delta = this.targetCanvasPoint.minus(this.currentCanvasPoint).multiply(progress);
            this.currentCanvasPoint = this.currentCanvasPoint.add(delta);
            handler.translate(delta.getX(), delta.getY());
            targetPositionReached = this.targetCanvasPoint.equals(this.currentCanvasPoint);
            if(targetPositionReached){
                cancel();
                this.canceled = false;
                run(1000);
            }
        }else{
            double deltaFactor = this.targetFactor - this.currentFactor;
            handler.focusZoom(this.currentFactor + deltaFactor * progress, targetCanvasPoint);
        }
    }

    private int time(double distance){
        int d = (int) Math.ceil(distance) * 10;
        return d > MAX_ANIMATION_DURATION ? MAX_ANIMATION_DURATION : d;
    }

    private double distance(Coordinate a, Coordinate b){
        double dX = b.getX() - a.getX();
        double dY = b.getY() - a.getY();
        return Math.sqrt(dX*dX + dY*dY);
    }
}
