package org.reactome.web.fireworks.controls.top.search;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.FlowPanel;
import org.reactome.web.fireworks.client.FireworksFactory;
import org.reactome.web.fireworks.model.Graph;
import org.reactome.web.fireworks.search.fallback.launcher.SearchLauncher;
import org.reactome.web.fireworks.search.fallback.suggester.SuggestionPanel;
import org.reactome.web.fireworks.search.searchonfire.infopanel.SolrSelectionInfoPanel;
import org.reactome.web.fireworks.search.searchonfire.launcher.SolrSearchLauncher;
import org.reactome.web.fireworks.search.searchonfire.suggester.SolrSuggestionPanel;


/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class SearchPanel extends FlowPanel {

    public SearchPanel(EventBus eventBus, Graph graph) {
        //Setting the legend style
        setStyleName(RESOURCES.getCSS().searchPanel());

        if(!FireworksFactory.SOLR_SEARCH) {
            final SearchLauncher launcher = new SearchLauncher(eventBus, graph);
            this.add(launcher);
            SuggestionPanel suggestions = new SuggestionPanel();
            suggestions.addSuggestionSelectedHandler(launcher);
            suggestions.addSuggestionHoveredHandler(launcher);
            // Listen to click events on suggestions and return focus on SearchBox
            suggestions.addClickHandler(event -> launcher.setFocus(true));

            launcher.addSearchPerformedHandler(suggestions);
            launcher.addPanelCollapsedHandler(suggestions);
            launcher.addPanelExpandedHandler(suggestions);
            launcher.addSearchBoxArrowKeysHandler(suggestions);
            this.add(suggestions);
        } else {
            final SolrSearchLauncher launcher = new SolrSearchLauncher(eventBus, graph);
            this.add(launcher);

            SolrSuggestionPanel suggestions = new SolrSuggestionPanel();
            suggestions.addPageChangedHandler(launcher);
            suggestions.addFacetChangedHandler(launcher);
            // Listen to click events on suggestions and return focus on SearchBox
            suggestions.addClickHandler(event -> launcher.setFocus(true));

            launcher.addSolrSearchPerformedHandler(suggestions);
            launcher.addPanelCollapsedHandler(suggestions);
            launcher.addPanelExpandedHandler(suggestions);
            launcher.addSearchBoxArrowKeysHandler(suggestions);
            this.add(suggestions);

            SolrSelectionInfoPanel infoPanel = new SolrSelectionInfoPanel(eventBus, graph);
            suggestions.addSolrSuggestionSelectedHandler(infoPanel);
            suggestions.addIncludeAllInstancesHandler(infoPanel);
            launcher.addPanelCollapsedHandler(infoPanel);
            launcher.addPanelExpandedHandler(infoPanel);
            this.add(infoPanel);
        }
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
        String CSS = "org/reactome/web/fireworks/controls/top/search/SearchPanel.css";

        String searchPanel();
    }
}
