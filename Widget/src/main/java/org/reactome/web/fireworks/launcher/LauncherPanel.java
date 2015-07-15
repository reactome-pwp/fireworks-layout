package org.reactome.web.fireworks.launcher;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.FlowPanel;
import org.reactome.web.fireworks.launcher.controls.MainControlPanel;
import org.reactome.web.fireworks.launcher.search.SearchPanel;


/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class LauncherPanel extends FlowPanel {

    public LauncherPanel(EventBus eventBus) {
        this.setStyleName(RESOURCES.getCSS().launcherPanel());

        //Search panel
        this.add(new SearchPanel(eventBus));
        //Main Control panel
        this.add(new MainControlPanel(eventBus));

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
    }
    /**
     * Styles used by this widget.
     */
    @CssResource.ImportedWithPrefix("fireworks-SearchLauncher")
    public interface ResourceCSS extends CssResource {
        /**
         * The path to the default CSS styles used by this resource.
         */
        String CSS = "org/reactome/web/fireworks/launcher/LauncherPanel.css";

        String launcherPanel();
    }
}
