package org.reactome.web.fireworks.search.searchonfire.graph;

import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.http.client.*;
import org.reactome.web.fireworks.search.searchonfire.graph.model.GraphEntry;
import org.reactome.web.pwp.model.classes.DatabaseObject;
import org.reactome.web.pwp.model.classes.Event;


/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public abstract class GraphSearchResultFactory {

    private static final String SEARCH = "/ContentService/data/##method##/##stableId##?speciesId=##speciesId##";
    private static Request request;

    public interface GraphSearchResultHandler {
        void onGraphSearchResult(GraphEntry[] result);
        void onGraphSearchError();
    }

    public static void searchForPathways(DatabaseObject selection, Long speciesId, boolean includeAllForms, final GraphSearchResultFactory.GraphSearchResultHandler handler) {

        String url = SEARCH.replace("##stableId##", selection.getIdentifier()).replace("##speciesId##", speciesId.toString());
        url = (!(selection instanceof Event) && includeAllForms) ? url.replace("##method##", "pathwaysForAllFormsOf") : url.replace("##method##", "pathwaysForInstance");

        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
        requestBuilder.setHeader("Accept", "application/json");
        try {
            if (request != null && request.isPending()) request.cancel();
            request = requestBuilder.sendRequest(null, new RequestCallback() {
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
