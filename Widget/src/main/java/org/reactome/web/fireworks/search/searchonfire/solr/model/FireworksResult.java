package org.reactome.web.fireworks.search.searchonfire.solr.model;

import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@JsType(isNative = true)
public interface FireworksResult {

    @JsProperty
    Entry[] getEntries();

    @JsProperty
    FacetContainer[] getFacets();

    @JsProperty
    int getFound();

    @JsProperty
    void setPage(Integer page);

    @JsProperty
    Integer getPage();

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
