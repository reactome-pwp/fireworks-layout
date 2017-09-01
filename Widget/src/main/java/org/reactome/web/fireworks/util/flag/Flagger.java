package org.reactome.web.fireworks.util.flag;

import com.google.gwt.http.client.*;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import org.reactome.web.fireworks.client.FireworksFactory;
import org.reactome.web.pwp.model.client.classes.Pathway;
import org.reactome.web.pwp.model.client.factory.DatabaseObjectFactory;

import java.util.LinkedList;
import java.util.List;

/**
 * Fun with flags!
 *
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class Flagger {

    private static final String SEARCH = "/ContentService/data/pathways/low/diagram/identifier/##term##/allForms?speciesId=##species##";
    private static Request request;

    public interface PathwaysToFlagHandler {
        void onPathwaysToFlag(List<Pathway> result);
        void onPathwaysToFlagError();
    }

    public static void findPathwaysToFlag(String term, Long speciesId, final PathwaysToFlagHandler handler) {
        String url = FireworksFactory.SERVER + SEARCH.replace("##term##", term).replace("##species##", speciesId.toString());

        if (request != null && request.isPending()) request.cancel();

        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);

        try {
            request = requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    List<Pathway> pathways = new LinkedList<>();
                    switch (response.getStatusCode()){
                        case Response.SC_OK:
                            JSONArray list = JSONParser.parseStrict(response.getText()).isArray();
                            for(int i=0; i<list.size(); ++i){
                                JSONObject object = list.get(i).isObject();
                                pathways.add((Pathway) DatabaseObjectFactory.create(object));
                            }
                            handler.onPathwaysToFlag(pathways);
                            break;
                        case Response.SC_NOT_FOUND:
                            handler.onPathwaysToFlag(pathways);
                        default:
                            handler.onPathwaysToFlagError();
                    }
                }
                @Override
                public void onError(Request request, Throwable exception) {
                    handler.onPathwaysToFlagError();
                }
            });
        }catch (RequestException ex) {
            handler.onPathwaysToFlagError();
        }
    }
}
