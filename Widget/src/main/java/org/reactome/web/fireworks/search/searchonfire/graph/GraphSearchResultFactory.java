package org.reactome.web.fireworks.search.searchonfire.graph;

import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.http.client.*;
import org.reactome.web.fireworks.search.searchonfire.graph.model.GraphEntry;


/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public abstract class GraphSearchResultFactory {

    private static final String SEARCH = "/ContentService/data/pathwaysForInstance/##stableId##?speciesId=##speciesId##";

    public interface GraphSearchResultHandler {
        void onGraphSearchResult(GraphEntry[] result);
        void onGraphSearchError();
    }

    public static void searchForPathways(String stableId, Long speciesId, final GraphSearchResultFactory.GraphSearchResultHandler handler) {

        String url = SEARCH.replace("##stableId##", stableId).replace("##speciesId##", speciesId.toString());

        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
        requestBuilder.setHeader("Accept", "application/json");
        try {
            requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    switch (response.getStatusCode()){
                        case Response.SC_OK:
                            GraphEntry[] result =  (GraphEntry[])(Object) JsonUtils.safeEval(response.getText());
                            handler.onGraphSearchResult(result);
                            break;
                        default:
                            handler.onGraphSearchError();
                    }
                }
                @Override
                public void onError(Request request, Throwable exception) {
                    handler.onGraphSearchError();
                }
            });
        }catch (RequestException ex) {
            handler.onGraphSearchError();
        }
    }
}
