package org.reactome.web.fireworks.search.searchonfire.solr.model;

import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@JsType(isNative = true)
public interface SolrSearchResult {

    @JsProperty
    Entry[] getEntries();

    @JsProperty
    void setEntries(Entry[] entries);

    @JsProperty
    FacetContainer[] getFacets();

    @JsProperty
    void setFacets(FacetContainer[] facets);

    @JsProperty
    int getFound();

    @JsProperty
    void setFound(int found);

    @JsProperty
    void setStartRow(Integer page);

    @JsProperty
    Integer getStartRow();

    @JsProperty
    void setRows(Integer rows);

    @JsProperty
    Integer getRows();

    @JsProperty
    void setSpecies(String species);

    @JsProperty
    String getSpecies();

    @JsProperty
    void setTerm(String term);

    @JsProperty
    String getTerm();

    @JsProperty
    void setSelectedFacet(String facet);

    @JsProperty
    String getSelectedFacet();
}
