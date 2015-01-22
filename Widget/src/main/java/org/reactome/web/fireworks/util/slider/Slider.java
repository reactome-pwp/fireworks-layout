package org.reactome.web.fireworks.util.slider;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.user.client.ui.Composite;

/**
 * A basic implementation for a progress slider based on canvas
 *
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class Slider extends Composite implements HasHandlers, MouseMoveHandler, MouseDownHandler, MouseOutHandler, MouseUpHandler {
    private Canvas slider;
    private SliderBar bar;
    private SliderPin pin;
    private PinStatus pinStatus = PinStatus.STD;
    private double percentage = 0.0;

    public Slider(int width, int height) {
        this.slider = Canvas.createIfSupported();
        if(this.slider!=null){
            this.slider.setWidth(width + "px");
            this.slider.setHeight(height + "px");
            this.slider.setCoordinateSpaceWidth(width);
            this.slider.setCoordinateSpaceHeight(height);

            this.initWidget(this.slider);
            this.initialise(height);
        }
    }

    public HandlerRegistration addSliderValueChangedHandler(SliderValueChangedHandler handler){
        return addHandler(handler, SliderValueChangedEvent.TYPE);
    }

    @Override
    public void onMouseDown(MouseDownEvent event) {
        this.pinStatus = this.pinHovered(event) ? PinStatus.CLICKED : PinStatus.STD ;
        this.pin.setDownPoint(getMousePosition(event));
        draw();
    }


    @Override
    public void onMouseMove(MouseMoveEvent event) {
        if(this.pinHovered(event)){
            getElement().getStyle().setCursor(Style.Cursor.POINTER);
        }else{
            getElement().getStyle().setCursor(Style.Cursor.DEFAULT);
        }
        if(!this.pinStatus.equals(PinStatus.CLICKED)){
            this.pinStatus = pinHovered(event) ? PinStatus.HOVERED : PinStatus.STD;
        }else{
            this.pin.setPos(getMousePosition(event), this.slider.getOffsetWidth(), (int) this.pin.r);
        }
        draw();
    }

    @Override
    public void onMouseOut(MouseOutEvent event) {
        checkPinMoved();
        this.pinStatus = PinStatus.STD;
        draw();
    }


    @Override
    public void onMouseUp(MouseUpEvent event) {
        checkPinMoved();
        this.pinStatus = pinHovered(event) ? PinStatus.HOVERED : PinStatus.STD;
        draw();
    }

    private void checkPinMoved(){
        int x = this.pin.pos.x - (int) this.pin.r;
        double w = this.slider.getOffsetWidth() - 2 * this.pin.r;
        double percentage = Math.round( (x / w) * 100) / 100.0;
        if(this.percentage!=percentage){
            this.percentage = percentage;
            fireEvent(new SliderValueChangedEvent(percentage));
        }
    }

    private void draw(){
        Context2d ctx = this.slider.getContext2d();
        ctx.clearRect(0,0,this.slider.getOffsetWidth(), this.slider.getOffsetHeight());
        this.bar.draw(ctx);
        this.pin.draw(ctx, this.pinStatus.colour);
    }

    private Point getMousePosition(MouseEvent event){
        int x = event.getRelativeX(this.slider.getElement());
        int y = event.getRelativeY(this.slider.getElement());
        return new Point(x,y);
    }

    private void initHandlers(){
        this.slider.addMouseDownHandler(this);
        this.slider.addMouseMoveHandler(this);
        this.slider.addMouseOutHandler(this);
        this.slider.addMouseUpHandler(this);
    }

    private void initialise(double height){
        initHandlers();

        double tick = height / 7.0;
        double y = tick * 3;
        int cX = (int) Math.round(tick * 2);
        int cY = (int) Math.round(height / 2.0);
        int cR = (int) Math.round(tick * 2);

        this.pin = new SliderPin(cX, cY, cR);
        this.bar = new SliderBar(tick, y, cR);

        this.draw();
    }

    private boolean pinHovered(MouseEvent event){
        return this.pin.isPointInside(getMousePosition(event));
    }
}
