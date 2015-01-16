package org.reactome.web.fireworks.events;

import com.google.gwt.event.shared.GwtEvent;
import org.reactome.web.fireworks.handlers.FireworksVisibleAreaChangedEventHandler;
import org.reactome.web.fireworks.util.Coordinate;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class FireworksVisibleAreaChangedEvent extends GwtEvent<FireworksVisibleAreaChangedEventHandler> {
    public static Type<FireworksVisibleAreaChangedEventHandler> TYPE = new Type<FireworksVisibleAreaChangedEventHandler>();

    private Coordinate translation;
    private double width;
    private double height;

    public FireworksVisibleAreaChangedEvent(Coordinate translation, double width, double height) {
        this.translation = translation;
        this.width = width;
        this.height = height;
    }

    @Override
    public Type<FireworksVisibleAreaChangedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(FireworksVisibleAreaChangedEventHandler handler) {
        handler.onFireworksVisibleAreaChanged(this);
    }

    public Coordinate getTranslation() {
        return translation;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }


    @Override
    public String toString() {
        return "FireworksVisibleAreaChangedEvent{" +
                "translation=" + translation +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
