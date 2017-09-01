package org.reactome.web.fireworks.search.searchonfire.graph;

import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.http.client.*;
import org.reactome.web.fireworks.client.FireworksFactory;
import org.reactome.web.fireworks.search.searchonfire.graph.model.GraphEntry;
import org.reactome.web.pwp.model.client.classes.DatabaseObject;
import org.reactome.web.pwp.model.client.classes.Event;


/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public abstract class GraphSearchResultFactory {

    private static final String SEARCH_ENTITY    = "/ContentService/data/pathways/low/entity/##id##?speciesId=##speciesId##";
    private static final String SEARCH_ALL_FORMS = "/ContentService/data/pathways/low/entity/##id##/allForms?speciesId=##speciesId##";
    private static Request request;

    public interface GraphSearchResultHandler {
        void onGraphSearchResult(GraphEntry[] result);
        void onGraphSearchError();
    }

    public static void searchForPathways(DatabaseObject selection, Long speciesId, boolean includeAllForms, final GraphSearchResultFactory.GraphSearchResultHandler handler) {

        String url = (!(selection instanceof Event) && includeAllForms) ? SEARCH_ALL_FORMS : SEARCH_ENTITY;
        url = FireworksFactory.SERVER + url.replace("##id##", selection.getReactomeIdentifier()).replace("##speciesId##", speciesId.toString());

        if (request != null && request.isPending()) request.cancel();

        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
        requestBuilder.setHeader("Accept", "application/json");
        try {
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
