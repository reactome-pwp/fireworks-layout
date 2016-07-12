package org.reactome.web.fireworks.search.searchonfire.graph.model;

import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
@JsType(isNative = true)
public interface GraphEntry {
    @JsProperty
    String getDbId();

    @JsProperty
    String getStId();

    @JsProperty
    String getDisplayName();

    @JsProperty
    String getSchemaClass();
}
