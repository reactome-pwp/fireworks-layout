package org.reactome.web.fireworks.search.searchonfire.infopanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import org.reactome.web.fireworks.search.fallback.panels.AbstractAccordionPanel;
import org.reactome.web.fireworks.search.searchonfire.events.SolrSuggestionSelectedEvent;
import org.reactome.web.fireworks.search.searchonfire.graph.GraphSearchResultFactory;
import org.reactome.web.fireworks.search.searchonfire.graph.model.GraphEntry;
import org.reactome.web.fireworks.search.searchonfire.handlers.SolrSuggestionSelectedHandler;
import org.reactome.web.fireworks.search.searchonfire.solr.model.Entry;


/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class SolrSelectionInfoPanel extends AbstractAccordionPanel implements SolrSuggestionSelectedHandler,
        GraphSearchResultFactory.GraphSearchResultHandler {
    private EventBus eventBus;
    private Entry selectedSuggestion;

    public SolrSelectionInfoPanel(EventBus eventBus) {
        this.eventBus = eventBus;
        this.setStyleName(RESOURCES.getCSS().container());
    }

    @Override
    public void onSuggestionSelected(SolrSuggestionSelectedEvent event) {
        this.clear();
        selectedSuggestion = event.getSelectedEntry();
        if(selectedSuggestion!=null) {
            performGraphSearch();
        }
    }

    private void performGraphSearch(){
        GraphSearchResultFactory.searchForPathways(selectedSuggestion.getStId(), 48887L, this);
    }

    @Override
    public void onGraphSearchResult(GraphEntry[] result) {
        if(result!=null) {
            this.add(new DetailsInfoPanel(selectedSuggestion, result));
        }
    }

    @Override
    public void onGraphSearchError() {
        //TODO to implement this
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

    @CssResource.ImportedWithPrefix("fireworks-SolrSelectionInfoPanel")
    public interface ResourceCSS extends CssResource {

        String CSS = "org/reactome/web/fireworks/search/searchonfire/infopanel/SolrSelectionInfoPanel.css";

        String container();
    }
}
