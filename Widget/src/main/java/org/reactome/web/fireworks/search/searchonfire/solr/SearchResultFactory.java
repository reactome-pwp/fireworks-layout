package org.reactome.web.fireworks.search.searchonfire.solr;


import com.google.gwt.http.client.*;
import org.reactome.web.fireworks.client.FireworksFactory;
import org.reactome.web.fireworks.search.searchonfire.launcher.SearchParameters;
import org.reactome.web.fireworks.search.searchonfire.solr.model.SolrSearchResult;
import org.reactome.web.fireworks.search.searchonfire.solr.model.factory.SolrSearchException;
import org.reactome.web.fireworks.search.searchonfire.solr.model.factory.SolrSearchResultFactory;
import org.reactome.web.fireworks.util.Console;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public abstract class SearchResultFactory {

    private static final String SEARCH = "/ContentService/search/fireworks?query=##term##&species=##species##&types=##facet##&start=##start##&rows=##rows##";
    private static Request request;

    public interface SearchResultHandler {
        void onSearchResult(SolrSearchResult result);
        void onSearchError();
    }

    public static void searchForTerm(SearchParameters params, SearchResultHandler handler) {

        String url = SEARCH.replace("##term##", params.getSearchTerm())
                           .replace("##facet##", params.getFacet())
                           .replace("##species##", params.getSpecies())
                           .replace("##start##", params.getStartRow() + "")
                           .replace("##rows##", params.getRows() + "");

        if (request != null && request.isPending()) request.cancel();

        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, FireworksFactory.SERVER + url);
        requestBuilder.setHeader("Accept", "application/json");
        try {
            request = requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    switch (response.getStatusCode()){
                        case Response.SC_OK:
                            try {
                                SolrSearchResult result = SolrSearchResultFactory.getSolrSearchObject(SolrSearchResult.class, response.getText());
                                result.setTerm(params.getSearchTerm());
                                result.setSelectedFacet(params.getFacet());
                                result.setSpecies(params.getSpecies());
                                result.setStartRow(params.getStartRow());
                                result.setRows(params.getRows());
                                handler.onSearchResult(result);
                            } catch (SolrSearchException e) {
                                Console.error(e.getCause());
                                handler.onSearchError();
                            }
                            break;
                        default:
                            handler.onSearchError();
                    }
                }
                @Override
                public void onError(Request request, Throwable exception) {
                    handler.onSearchError();
                }
            });
        }catch (RequestException ex) {
            handler.onSearchError();
        }
    }
}
