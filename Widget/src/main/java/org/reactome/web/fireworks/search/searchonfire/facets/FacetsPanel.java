package org.reactome.web.fireworks.search.searchonfire.facets;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;

import java.util.List;

/**
 * A panel that contains all the available facets
 *
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class FacetsPanel extends FlowPanel {

    public interface Handler {
        void onFacetSelected(String facet);
    }

    private Handler handler;

    public FacetsPanel(Handler handler) {
        this.handler = handler;
    }

    public void addFacets(List<String> facets) {
        clear();
        if(facets != null && !facets.isEmpty()) {
            for (final String facet : facets) {
                add(new Button(facet, new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                        handler.onFacetSelected(facet);
                    }
                }));
            }
        }
    }
}
