package org.reactome.web.fireworks.search.searchonfire.solr;


import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.http.client.*;
import org.reactome.web.fireworks.search.searchonfire.solr.model.SolrSearchResult;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public abstract class SearchResultFactory {

    private static final String SEARCH = "/ContentService/search/fireworks?query=##term##&species=##species##&types=##facet##&page=##page##&rows=##rows##";

    public interface SearchResultHandler {
        void onSearchResult(SolrSearchResult result);
        void onSearchError();
    }

    public static void searchForTerm(String term, String facet, String species, int page, int rows, final SearchResultHandler handler) {

        String url = SEARCH.replace("##term##", term).replace("##facet##", facet).replace("##species##", species).replace("##page##", page + "").replace("##rows##", rows + "");

        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
        requestBuilder.setHeader("Accept", "application/json");
        try {
            requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    switch (response.getStatusCode()){
                        case Response.SC_OK:
                            SolrSearchResult result = JsonUtils.safeEval(response.getText());
                            result.setTerm(term);
                            result.setSelectedFacet(facet);
                            result.setSpecies(species);
                            result.setStartRow(page);
                            result.setRows(rows);
                            handler.onSearchResult(result);
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
