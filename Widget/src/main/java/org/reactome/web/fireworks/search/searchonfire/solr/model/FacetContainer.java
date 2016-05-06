package org.reactome.web.fireworks.search.searchonfire.solr.model;


import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@JsType(isNative = true)
public interface FacetContainer {

    @JsProperty
    Integer getCount();

    @JsProperty
    String getName();

}
