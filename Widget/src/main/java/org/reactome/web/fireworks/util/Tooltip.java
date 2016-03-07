package org.reactome.web.fireworks.util;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class Tooltip extends PopupPanel {
    private static Tooltip tooltip;

    private Tooltip() {
//        this.setStyleName(FireworksStyleFactory.getFireworksStyle().bubble());
        this.setStyleName(RESOURCES.getCSS().popup());
    }

    public static Tooltip getTooltip() {
        if (tooltip == null) {
            tooltip = new Tooltip();
        }
        return tooltip;
    }

    public void setText(String text){
        tooltip.clear();
        tooltip.add(new Label(text != null ? text : ""));
    }

    public void hide() {
        setVisible(false);
    }

    @Override
    public void add(Widget w) {
        this.clear();
        super.add(w);
    }

    public void setPositionAndShow(ToolTipContainer container, double offsetX, double offsetY, double distance) {
        container.add(this, -1000, -1000); //Adding it where is not visible
        container.getElement().appendChild(this.getElement());

        this.setVisible(true);
        this.setPosition((int) offsetX, (int) (offsetY + distance));
    }

    private void setPosition(int left, int top) {
        Element elem = getElement();
        elem.getStyle().setPropertyPx("left", left);
        elem.getStyle().setPropertyPx("top", top);
    }


    public static Resources RESOURCES;
    static {
        RESOURCES = GWT.create(Resources.class);
        RESOURCES.getCSS().ensureInjected();
    }

    public interface Resources extends ClientBundle {
        @Source(ResourceCSS.CSS)
        ResourceCSS getCSS();
    }

    @CssResource.ImportedWithPrefix("fireworks-ToolTips")
    public interface ResourceCSS extends CssResource {
        String CSS = "org/reactome/web/fireworks/util/ToolTips.css";

        String popup();
    }
}