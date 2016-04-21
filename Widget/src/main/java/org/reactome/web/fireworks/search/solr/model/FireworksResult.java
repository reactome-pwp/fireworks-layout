package org.reactome.web.fireworks.search.solr.model;

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
}
