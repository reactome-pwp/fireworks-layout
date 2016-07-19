package org.reactome.web.fireworks.search.fallback.provider;


import org.reactome.web.fireworks.model.Graph;
import org.reactome.web.fireworks.model.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class SuggestionsProviderImpl implements SuggestionsProvider<Node> {

    private Graph content;

    public SuggestionsProviderImpl(Graph content) {
        this.content = content;
    }

    @Override
    public List<Node> getSuggestions(String input) {
        List<Node> rtn = new ArrayList<>();
        if (content == null || input == null || input.isEmpty()) return rtn;

        String[] inputs = input.split("  *");
        if(inputs.length==0) return rtn;

        String term = inputs[0].toLowerCase();
        for (Node obj : content.getNodes()) {
            obj.clearSearchDisplayValue(); //clears the result of previous searches
            if (obj.getName().toLowerCase().contains(term) || obj.getIdentifier().toLowerCase().contains(term)) {
                rtn.add(obj);
            }
        }

        // improvement for extra terms filtering
        if (inputs.length > 1) {
            for (int i = 1; i < inputs.length; i++) {
                term = inputs[i].toLowerCase();
                List<Node> aux = new ArrayList<>();
                for (Node obj : rtn) {
                    if (obj.getName().toLowerCase().contains(term) || obj.getIdentifier().toLowerCase().contains(term)) {
                        aux.add(obj);
                    }
                }
                rtn = aux;
            }
        }

        Collections.sort(rtn);
        for (Node object : rtn) {
            object.setSearchDisplay(inputs);
        }
        return rtn;
    }
}
