package org.reactome.web.fireworks.launcher.controls;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.FlowPanel;
import org.reactome.web.fireworks.events.ControlActionEvent;
import org.reactome.web.fireworks.events.NodeSelectedEvent;
import org.reactome.web.fireworks.events.NodeSelectedResetEvent;
import org.reactome.web.fireworks.handlers.NodeSelectedHandler;
import org.reactome.web.fireworks.handlers.NodeSelectedResetHandler;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class MainControlPanel extends FlowPanel implements ClickHandler, NodeSelectedHandler, NodeSelectedResetHandler {

    private EventBus eventBus;
    private ControlButton fitAll;
    private ControlButton open;

    public MainControlPanel(EventBus eventBus) {
        this.eventBus = eventBus;

        getElement().getStyle().setFloat(Style.Float.LEFT);
        this.fitAll = new ControlButton("Show all", RESOURCES.getCSS().fitall(), this);
        this.open = new ControlButton("Open pathway diagram", RESOURCES.getCSS().diagram(), this);
        this.open.setEnabled(false);

        this.add(this.fitAll);
        this.add(this.open);

        this.eventBus.addHandler(NodeSelectedEvent.TYPE, this);
        this.eventBus.addHandler(NodeSelectedResetEvent.TYPE, this);
    }

    @Override
    public void onClick(ClickEvent event) {
        ControlButton btn = (ControlButton) event.getSource();
        if(btn.equals(this.fitAll)) {
            this.eventBus.fireEventFromSource(new ControlActionEvent(ControlAction.FIT_ALL), this);
        }else if(btn.equals(this.open)){
            this.eventBus.fireEventFromSource(new ControlActionEvent(ControlAction.OPEN), this);
        }
    }

    @Override
    public void onNodeSelected(NodeSelectedEvent event) {
        this.open.setEnabled(true);
    }

    @Override
    public void onNodeSelectionReset() {
        this.open.setEnabled(false);
    }


    public static Resources RESOURCES;
    static {
        RESOURCES = GWT.create(Resources.class);
        RESOURCES.getCSS().ensureInjected();
    }

    /**
     * A ClientBundle of resources used by this widget.
     */
    public interface Resources extends ClientBundle {
        /**
         * The styles used in this widget.
         */
        @Source(ResourceCSS.CSS)
        ResourceCSS getCSS();

        @Source("images/fitall_clicked.png")
        ImageResource fitallClicked();

        @Source("images/fitall_disabled.png")
        ImageResource fitallDisabled();

        @Source("images/fitall_hovered.png")
        ImageResource fitallHovered();

        @Source("images/fitall_normal.png")
        ImageResource fitallNormal();

        @Source("images/diagram_clicked.png")
        ImageResource diagramClicked();

        @Source("images/diagram_disabled.png")
        ImageResource diagramDisabled();

        @Source("images/diagram_hovered.png")
        ImageResource diagramHovered();

        @Source("images/diagram_normal.png")
        ImageResource diagramNormal();
    }

    /**
     * Styles used by this widget.
     */
    @CssResource.ImportedWithPrefix("fireworks-MainControlPanel")
    public interface ResourceCSS extends CssResource {
        /**
         * The path to the default CSS styles used by this resource.
         */
        String CSS = "org/reactome/web/fireworks/launcher/controls/MainControlPanel.css";

        String fitall();

        String diagram();
    }
}
