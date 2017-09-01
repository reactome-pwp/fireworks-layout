package org.reactome.web.fireworks.search.searchonfire.infopanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import org.reactome.web.fireworks.events.SearchFilterEvent;
import org.reactome.web.fireworks.model.Graph;
import org.reactome.web.fireworks.search.fallback.panels.AbstractAccordionPanel;
import org.reactome.web.fireworks.search.searchonfire.events.IncludeAllFormsEvent;
import org.reactome.web.fireworks.search.searchonfire.events.SolrSuggestionSelectedEvent;
import org.reactome.web.fireworks.search.searchonfire.graph.GraphSearchResultFactory;
import org.reactome.web.fireworks.search.searchonfire.graph.model.GraphEntry;
import org.reactome.web.fireworks.search.searchonfire.handlers.IncludeAllFormsHandler;
import org.reactome.web.fireworks.search.searchonfire.handlers.SolrSuggestionSelectedHandler;
import org.reactome.web.pwp.model.client.classes.DatabaseObject;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class SolrSelectionInfoPanel extends AbstractAccordionPanel implements SolrSuggestionSelectedHandler,
        GraphSearchResultFactory.GraphSearchResultHandler, IncludeAllFormsHandler {
    private EventBus eventBus;
    private Graph graph;
    private DatabaseObject selectedSuggestion;
    private boolean includeAllForms;

    public SolrSelectionInfoPanel(EventBus eventBus, Graph graph) {
        this.eventBus = eventBus;
        this.graph = graph;
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
        GraphSearchResultFactory.searchForPathways(selectedSuggestion, graph.getSpeciesId(), includeAllForms, this);
    }

    @Override
    public void onGraphSearchResult(GraphEntry[] result) {
        if(result!=null) {
            this.add(new DetailsInfoPanel(eventBus, selectedSuggestion, result));
        }
        eventBus.fireEventFromSource(new SearchFilterEvent(result), this);
    }

    @Override
    public void onGraphSearchError() {
        //TODO to implement this
    }

    @Override
    public void onIncludeAllForms(IncludeAllFormsEvent event) {
        this.clear();
        includeAllForms = event.getIncludeAllForms();
        if(selectedSuggestion!=null) {
            performGraphSearch();
        }
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
