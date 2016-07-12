package org.reactome.web.fireworks.search.searchonfire.solr.model;

import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public interface SolrSearchResult {

    List<Entry> getEntries();

    void setEntries(List<Entry> entries);

    List<FacetContainer> getFacets();

    void setFacets(List<FacetContainer> facets);

    Integer getFound();

    void setFound(Integer found);

    void setStartRow(Integer page);

    Integer getStartRow();

    void setRows(Integer rows);

    Integer getRows();

    void setSpecies(String species);

    String getSpecies();

    void setTerm(String term);

    String getTerm();

    void setSelectedFacet(String facet);

    String getSelectedFacet();
}
