package org.reactome.web.fireworks.search.searchonfire.facets;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import org.reactome.web.fireworks.search.searchonfire.solr.model.FacetContainer;
import org.reactome.web.fireworks.search.searchonfire.solr.model.FireworksResult;

import java.util.*;

/**
 * A panel that contains all the available facets
 *
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class FacetsPanel extends FlowPanel implements ClickHandler {
    private FireworksResult searchResult;
    private Map<String, FacetTag> facetsMap;

    private Label titleLabel;
    private FlowPanel tagsContainer;

    public FacetsPanel() {
        setVisible(false);
        init();
    }

    public HandlerRegistration addFacetChangedHandler(FacetChangedHandler handler) {
        return addHandler(handler, FacetChangedEvent.TYPE);
    }

    public void setResults(FireworksResult searchResult) {
        this.searchResult = searchResult;

        List<String> selectedFacets = new ArrayList<>();
        String aux = searchResult.getSelectedFacet();
        if(aux!=null && !aux.isEmpty()) {
            selectedFacets = Arrays.asList(aux.split(","));
        }

        FacetContainer[] facets = searchResult.getFacets();
        facetsMap = new HashMap<>();
        if(facets != null) {
            for (final FacetContainer fac : facets) {
                FacetTag facetTag = new FacetTag(fac.getName(), fac.getCount());
                facetTag.setSelected(selectedFacets.isEmpty() || selectedFacets.contains(facetTag.getName()));
                facetTag.addClickHandler(this);
                facetsMap.put(facetTag.getName(), facetTag);
            }
        }
        updateView();
    }

    @Override
    public void onClick(ClickEvent event) {
        FacetTag clickedFacet = (FacetTag) event.getSource();
        Set<FacetTag> selectedFacets = getSelectedFacets();
        if(selectedFacets.size()>1 || !clickedFacet.isSelected()) {
            // Select that one
            setSelectedForAll(false);
            facetsMap.get(clickedFacet.getName()).setSelected(true);
        } else {
            // select all
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
        titleLabel = new Label("Filter by facet:");
        Style style = titleLabel.getElement().getStyle();
        style.setFontSize(1.20, Style.Unit.EM);
        style.setFontWeight(Style.FontWeight.BOLD);
        style.setColor("rgb(30, 148, 208)");
        style.setMarginLeft(5, Style.Unit.PX);
        tagsContainer = new FlowPanel();
        add(titleLabel);
        add(tagsContainer);
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
}
