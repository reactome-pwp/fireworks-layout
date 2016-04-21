package org.reactome.web.fireworks.search;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.FlowPanel;
import org.reactome.web.fireworks.controls.top.search.SearchLauncher;
import org.reactome.web.fireworks.model.Graph;
import org.reactome.web.fireworks.search.fallback.suggester.SuggestionPanel;


/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class SearchPanel extends FlowPanel {

    public static SearchPanelResources RESOURCES;
    static {
        RESOURCES = GWT.create(SearchPanelResources.class);
        RESOURCES.getCSS().ensureInjected();
    }

    public SearchPanel(EventBus eventBus, Graph graph) {
        //Setting the legend style
        setStyleName(RESOURCES.getCSS().searchPanel());

        final SearchLauncher launcher = new SearchLauncher(eventBus, graph);
        this.add(launcher);

        SuggestionPanel suggestions = new SuggestionPanel();
        suggestions.addSuggestionSelectedHandler(launcher);
        suggestions.addSuggestionHoveredHandler(launcher);
        // Listen to click events on suggestions and return focus on SearchBox
        suggestions.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                launcher.setFocus(true);
            }
        });

        launcher.addSearchPerformedHandler(suggestions);
        launcher.addPanelCollapsedHandler(suggestions);
        launcher.addPanelExpandedHandler(suggestions);
        launcher.addSearchBoxArrowKeysHandler(suggestions);
        this.add(suggestions);



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
    @CssResource.ImportedWithPrefix("fireworks-SearchPanel")
    public interface SearchPanelCSS extends CssResource {
        /**
         * The path to the default CSS styles used by this resource.
         */
        String CSS = "org/reactome/web/fireworks/search/fallback/SearchPanel.css";

        String searchPanel();
    }
}
