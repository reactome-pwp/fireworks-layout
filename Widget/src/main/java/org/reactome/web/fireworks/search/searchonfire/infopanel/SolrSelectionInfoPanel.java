package org.reactome.web.fireworks.search.searchonfire.infopanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import org.reactome.web.fireworks.search.fallback.events.SuggestionSelectedEvent;
import org.reactome.web.fireworks.search.fallback.handlers.SuggestionSelectedHandler;
import org.reactome.web.fireworks.search.fallback.panels.AbstractAccordionPanel;


/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class SolrSelectionInfoPanel extends AbstractAccordionPanel implements SuggestionSelectedHandler {
    private EventBus eventBus;

    public SolrSelectionInfoPanel(EventBus eventBus) {
        this.eventBus = eventBus;
        this.setStyleName(RESOURCES.getCSS().container());
    }

    @Override
    public void onSuggestionSelected(SuggestionSelectedEvent event) {
        this.clear();

//        SearchResultObject obj = event.getSearchResultObject();

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
