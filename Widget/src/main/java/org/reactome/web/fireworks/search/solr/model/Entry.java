package org.reactome.web.fireworks.search.solr.model;

import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@JsType(isNative = true)
public interface Entry {

    @JsProperty
    String getStId();

    @JsProperty
    String getId();

    @JsProperty
    String getName();

    @JsProperty
    String getExactType();

}
