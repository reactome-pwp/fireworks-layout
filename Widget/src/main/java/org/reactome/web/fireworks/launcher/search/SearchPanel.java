package org.reactome.web.fireworks.launcher.search;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.FlowPanel;


/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class SearchPanel extends FlowPanel {

    public static SearchPanelResources RESOURCES;
    static {
        RESOURCES = GWT.create(SearchPanelResources.class);
        RESOURCES.getCSS().ensureInjected();
    }

    public SearchPanel(EventBus eventBus) {
        //Setting the legend style
        setStyleName(RESOURCES.getCSS().searchPanel());

        final SearchLauncher launcher = new SearchLauncher(eventBus);
        this.add(launcher);

    }

    /**
     * A ClientBundle of resources used by this widget.
     */
    public interface SearchPanelResources extends ClientBundle {
        /**
         * The styles used in this widget.
         */
        @Source(SearchPanelCSS.CSS)
        SearchPanelCSS getCSS();
    }

    /**
     * Styles used by this widget.
     */
    @CssResource.ImportedWithPrefix("diagram-SearchPanel")
    public interface SearchPanelCSS extends CssResource {
        /**
         * The path to the default CSS styles used by this resource.
         */
        String CSS = "org/reactome/web/fireworks/launcher/search/SearchPanel.css";

        String searchPanel();
    }
}
