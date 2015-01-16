package org.reactome.web.fireworks.legends;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.AbsolutePanel;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public abstract class LegendPanel extends AbsolutePanel {

    public static LegendResources RESOURCES;
    static {
        RESOURCES = GWT.create(LegendResources.class);
        RESOURCES.getCSS().ensureInjected();
    }

    /**
     * A ClientBundle of resources used by this widget.
     */
    public interface LegendResources extends ClientBundle {
        /**
         * The styles used in this widget.
         */
        @Source(LegendPanelCSS.CSS)
        LegendPanelCSS getCSS();

        @Source("images/close_clicked.png")
        ImageResource closeClicked();

        @Source("images/close_disabled.png")
        ImageResource closeDisabled();

        @Source("images/close_hovered.png")
        ImageResource closeHovered();

        @Source("images/close_normal.png")
        ImageResource closeNormal();
    }

    /**
     * Styles used by this widget.
     */
    @CssResource.ImportedWithPrefix("fireworks-LegendPanel")
    public interface LegendPanelCSS extends CssResource {
        /**
         * The path to the default CSS styles used by this resource.
         */
        String CSS = "org/reactome/web/fireworks/legends/LegendPanel.css";

        String legendPanel();

        String analysisControl();

        String close();

        String enrichmentLegend();

    }

    protected EventBus eventBus;

    public LegendPanel(EventBus eventBus) {
        this.eventBus = eventBus;
        //Setting the legend style
        getElement().getStyle().setPosition(com.google.gwt.dom.client.Style.Position.ABSOLUTE);
        setStyleName(RESOURCES.getCSS().legendPanel());
    }
}
