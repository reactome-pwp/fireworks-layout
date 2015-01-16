package org.reactome.web.fireworks.util;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.user.client.Timer;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class ClickManager extends Timer implements HasHandlers, ClickHandler, DoubleClickHandler {
    private HandlerManager handlerManager;
    private ClickEvent event;

    public ClickManager(Canvas canvas) {
        this.handlerManager = new HandlerManager(this);
        canvas.addClickHandler(this);
        canvas.addDoubleClickHandler(this);
    }

    @Override
    public synchronized void run() {
        if(this.event!=null){
            this.fireEvent(this.event);
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        this.event = event;
        this.schedule(250);
    }

    @Override
    public void onDoubleClick(DoubleClickEvent event) {
        this.cancel();
        this.event = null;
        this.fireEvent(event);
    }

    public HandlerRegistration addClickHandler(ClickHandler handler){
        return this.handlerManager.addHandler(ClickEvent.getType(), handler);
    }

    public HandlerRegistration addDoubleClickHandler(DoubleClickHandler handler){
        return this.handlerManager.addHandler(DoubleClickEvent.getType(), handler);
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {
        this.handlerManager.fireEvent(event);
    }
}
