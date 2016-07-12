package org.reactome.web.fireworks.search.searchonfire.launcher;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class SearchParameters {
    private String searchTerm = "";
    private String facet = "";
    private String species;
    private int startRow = 0;
    private final int rows = 4;

    public SearchParameters(String species) {
        this.species = species;
    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public String getFacet() {
        return facet;
    }

    public void setFacet(String facet) {
        this.facet = facet;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public String getSpecies() {
        return species;
    }

    public int getRows() {
        return rows;
    }
}
