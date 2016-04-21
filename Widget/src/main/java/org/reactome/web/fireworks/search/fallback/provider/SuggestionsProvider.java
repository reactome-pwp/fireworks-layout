package org.reactome.web.fireworks.search.fallback.provider;

import java.util.List;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public interface SuggestionsProvider<T> {

    List<T> getSuggestions(String input);

}
