package org.reactome.web.fireworks.legends;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.FlowPanel;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class BottomContainerPanel extends FlowPanel {

    public BottomContainerPanel() {
        setStyleName(RESOURCES.getCSS().container());
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

    @CssResource.ImportedWithPrefix("fireworks-BottomContainerPanel")
    public interface ResourceCSS extends CssResource {
        String CSS = "org/reactome/web/fireworks/legends/BottomContainerPanel.css";

        String container();
    }
}
