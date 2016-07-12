package org.reactome.web.fireworks.search.searchonfire.solr.model.factory;

import com.google.gwt.core.client.GWT;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;
import org.reactome.web.fireworks.search.searchonfire.solr.model.Entry;
import org.reactome.web.fireworks.search.searchonfire.solr.model.FacetContainer;
import org.reactome.web.fireworks.search.searchonfire.solr.model.SolrSearchResult;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class SolrSearchResultFactory {

    @SuppressWarnings("UnusedDeclaration")
    interface ModelAutoBeanFactory extends AutoBeanFactory {
        AutoBean<Entry> entries();
        AutoBean<FacetContainer> facets();
        AutoBean<SolrSearchResult> solrSearchResult();
    }

    @SuppressWarnings("Duplicates")
    public static <T> T getSolrSearchObject(Class<T> cls, String json) throws SolrSearchException {
        try{
            AutoBeanFactory factory = GWT.create(ModelAutoBeanFactory.class);
            AutoBean<T> bean = AutoBeanCodex.decode(factory, cls, json);
            return bean.as();
        }catch (Throwable e){
            throw new SolrSearchException("Error mapping json string for [" + cls + "]: " + json, e);
        }
    }
}
