package org.reactome.web.fireworks.controls.top.key;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.TextResource;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import org.reactome.web.fireworks.controls.top.common.AbstractMenuDialog;
import org.reactome.web.fireworks.events.NodeOpenedEvent;
import org.reactome.web.fireworks.events.ProfileChangedEvent;
import org.reactome.web.fireworks.handlers.NodeOpenedHandler;
import org.reactome.web.fireworks.handlers.ProfileChangedHandler;
import org.reactome.web.fireworks.profiles.FireworksColours;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class PathwayOverviewKey extends AbstractMenuDialog implements ProfileChangedHandler, NodeOpenedHandler {

    private EventBus eventBus;

    private Context2d ctx;

    public PathwayOverviewKey(EventBus eventBus) {
        super("Pathway Overview Key");
        this.eventBus = eventBus;
        this.initHandlers();

        AbsolutePanel canvases = new AbsolutePanel();
        canvases.setStyleName(RESOURCES.getCSS().canvases());
        this.ctx = this.createCanvas(canvases, 170, 100);
        add(canvases);

        HTMLPanel bullets = new HTMLPanel(RESOURCES.fireworkskey().getText());
        bullets.setStyleName(RESOURCES.getCSS().bullets());
        add(bullets);
        draw();
    }

    private Context2d createCanvas(AbsolutePanel container, int width, int height) {
        Canvas canvas = Canvas.createIfSupported();
        canvas.setCoordinateSpaceWidth(width);
        canvas.setCoordinateSpaceHeight(height);
        canvas.setPixelSize(width, height);
        container.add(canvas, 10, 10);
        return canvas.getContext2d();
    }

    private void draw(){
        ctx.setFillStyle(FireworksColours.PROFILE.getNodeInitialColour());
        ctx.beginPath();
        ctx.arc(50, 25, 10, 0, 2 * Math.PI);
        ctx.fill();

        ctx.setStrokeStyle(FireworksColours.PROFILE.getEdgeInitialColour());
        ctx.setLineWidth(3);
        ctx.beginPath();
        ctx.moveTo(20, 70);
        ctx.quadraticCurveTo(50, 60, 80, 70);
        ctx.stroke();

        ctx.setFillStyle("#000000");
        ctx.setFont("12px Arial");
        ctx.fillText("Pathway", 100, 28);
        ctx.fillText("Parent-child", 100, 69);
        ctx.fillText("relationship", 100, 82);
    }

    private void initHandlers() {
        this.eventBus.addHandler(NodeOpenedEvent.TYPE, this);
        this.eventBus.addHandler(ProfileChangedEvent.TYPE, this);
    }

    @Override
    public void onNodeOpened(NodeOpenedEvent event) {
        hide();
    }

    @Override
    public void onProfileChanged(ProfileChangedEvent event) {
        draw();
    }


    public static Resources RESOURCES;
    static {
        RESOURCES = GWT.create(Resources.class);
        RESOURCES.getCSS().ensureInjected();
    }

    public interface Resources extends ClientBundle {
        @Source(ResourceCSS.CSS)
        ResourceCSS getCSS();

        @Source("data/fireworkskey.html")
        TextResource fireworkskey();
    }

    @CssResource.ImportedWithPrefix("fireworks-PathwayOverviewKey")
    public interface ResourceCSS extends CssResource {
        /**
         * The path to the default CSS styles used by this resource.
         */
        String CSS = "org/reactome/web/fireworks/controls/top/key/FireworksKey.css";

        String canvases();

        String bullets();
    }
}
