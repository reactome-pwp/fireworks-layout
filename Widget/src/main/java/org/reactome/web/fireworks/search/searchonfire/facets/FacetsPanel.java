package org.reactome.web.fireworks.search.searchonfire.facets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import org.reactome.web.fireworks.search.searchonfire.solr.model.FacetContainer;
import org.reactome.web.fireworks.search.searchonfire.solr.model.SolrSearchResult;

import java.util.*;

/**
 * A panel that contains all the available facets
 *
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class FacetsPanel extends FlowPanel implements ClickHandler {
    private SolrSearchResult searchResult;
    private Map<String, FacetTag> facetsMap;

    private Label titleLabel;
    private FlowPanel tagsContainer;

    public FacetsPanel() {
        setVisible(false);
        facetsMap = new HashMap<>();
        init();
    }

    public HandlerRegistration addFacetChangedHandler(FacetChangedHandler handler) {
        return addHandler(handler, FacetChangedEvent.TYPE);
    }

    public void setResults(SolrSearchResult searchResult) {
        if(searchResult!=null && searchResult.getFacets()!=null) {
            this.searchResult = searchResult;

            List<String> selectedFacets = new ArrayList<>();
            String aux = searchResult.getSelectedFacet();
            if (aux != null && !aux.isEmpty()) {
                selectedFacets = Arrays.asList(aux.split(","));
            }

            List<FacetContainer> facets = searchResult.getFacets();
            facetsMap.clear();
            if (facets != null) {
                for (final FacetContainer fac : facets) {
                    FacetTag facetTag = new FacetTag(fac.getName(), fac.getCount());
                    facetTag.setSelected(selectedFacets.isEmpty() || selectedFacets.contains(facetTag.getName()));
                    facetTag.addClickHandler(this);
                    facetsMap.put(facetTag.getName(), facetTag);
                }
            }
        } else {
            facetsMap.clear();  // Results are null or no facets are provided
        }
        updateView();
    }

    @Override
    public void onClick(ClickEvent event) {
        if(facetsMap.size() == 1)  return;

        FacetTag clickedFacet = (FacetTag) event.getSource();
        Set<FacetTag> selectedFacets = getSelectedFacets();
        if(selectedFacets.size()>1 || !clickedFacet.isSelected()) {
            // Select that one
            setSelectedForAll(false);
            facetsMap.get(clickedFacet.getName()).setSelected(true);
        } else {
            // select all facets
            setSelectedForAll(true);
        }
        updateView();

        if(getSelectedFacets().size()>1) {
            searchResult.setSelectedFacet("");
        } else {
            searchResult.setSelectedFacet(clickedFacet.getName());
        }
        fireEvent(new FacetChangedEvent(searchResult));
    }

    private Set<FacetTag> getSelectedFacets() {
        Set<FacetTag> rtn = new HashSet<>();
        if(facetsMap!=null) {
            for (FacetTag facet : facetsMap.values()) {
                if(facet.isSelected()) {
                    rtn.add(facet);
                }
            }
        }
        return rtn;
    }

    private void init() {
        titleLabel = new Label("Filter by type:");
        titleLabel.setStyleName(RESOURCES.getCSS().title());

        tagsContainer = new FlowPanel();
        tagsContainer.setStyleName(RESOURCES.getCSS().tagContainer());


        SimplePanel sp = new SimplePanel();
        sp.setStyleName(RESOURCES.getCSS().outerContainer());
        sp.add(tagsContainer);

        add(titleLabel);
        add(sp);
    }

    private void setSelectedForAll(boolean value) {
        if(facetsMap!=null) {
            for (FacetTag facet : facetsMap.values()) {
                facet.setSelected(value);
            }
        }
    }

    private void updateView(){
        tagsContainer.clear();
        for(FacetTag facet : facetsMap.values()) {
            tagsContainer.add(facet);
        }
        setVisible(!facetsMap.isEmpty());
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
    @CssResource.ImportedWithPrefix("fireworks-FacetPanel")
    public interface ResourceCSS extends CssResource {
        /**
         * The path to the default CSS styles used by this resource.
         */
        String CSS = "org/reactome/web/fireworks/search/searchonfire/facets/FacetPanel.css";

        String title();

        String outerContainer();

        String tagContainer();
    }
}
