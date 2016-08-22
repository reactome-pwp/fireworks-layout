package org.reactome.web.fireworks.search.searchonfire.launcher;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AbsolutePanel;
import org.reactome.web.fireworks.controls.common.IconButton;
import org.reactome.web.fireworks.events.SearchKeyPressedEvent;
import org.reactome.web.fireworks.events.SearchResetEvent;
import org.reactome.web.fireworks.handlers.SearchKeyPressedHandler;
import org.reactome.web.fireworks.legends.ControlButton;
import org.reactome.web.fireworks.model.Graph;
import org.reactome.web.fireworks.search.fallback.events.PanelCollapsedEvent;
import org.reactome.web.fireworks.search.fallback.events.PanelExpandedEvent;
import org.reactome.web.fireworks.search.fallback.handlers.PanelCollapsedHandler;
import org.reactome.web.fireworks.search.fallback.handlers.PanelExpandedHandler;
import org.reactome.web.fireworks.search.fallback.searchbox.*;
import org.reactome.web.fireworks.search.searchonfire.facets.FacetChangedEvent;
import org.reactome.web.fireworks.search.searchonfire.facets.FacetChangedHandler;
import org.reactome.web.fireworks.search.searchonfire.pager.PageChangedEvent;
import org.reactome.web.fireworks.search.searchonfire.pager.PageChangedHandler;
import org.reactome.web.fireworks.search.searchonfire.solr.SearchResultFactory;
import org.reactome.web.fireworks.search.searchonfire.solr.model.SolrSearchResult;
import org.reactome.web.fireworks.util.Console;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
@SuppressWarnings("Duplicates")
public class SolrSearchLauncher extends AbsolutePanel implements ClickHandler, SearchBoxUpdatedHandler,
        SearchBoxArrowKeysHandler, SearchKeyPressedHandler, SearchResultFactory.SearchResultHandler,
        PageChangedHandler, FacetChangedHandler {

    @SuppressWarnings("FieldCanBeLocal")
    private static String OPENING_TEXT = "Search for any term ...";
    private static int FOCUS_IN_TEXTBOX_DELAY = 300;

    private EventBus eventBus;

    private SearchBox input = null;
    private ControlButton searchBtn = null;
    private IconButton clearBtn;

    private Boolean isExpanded = false;

    private Timer focusTimer;

    private SearchParameters searchParameters;

    public SolrSearchLauncher(EventBus eventBus, Graph graph) {
        //Setting the search style
        setStyleName(RESOURCES.getCSS().launchPanel());

        this.eventBus = eventBus;

        searchParameters = new SearchParameters(graph.getSpeciesName());

        this.searchBtn = new ControlButton("Search reactome", RESOURCES.getCSS().launch(), this);
        this.add(searchBtn);

        this.input = new SearchBox();
        this.input.setStyleName(RESOURCES.getCSS().input());
        this.input.getElement().setPropertyString("placeholder", OPENING_TEXT);
        this.add(input);

        clearBtn = new IconButton("", RESOURCES.clear());
        clearBtn.setStyleName(RESOURCES.getCSS().clearBtn());
        clearBtn.setVisible(false);
        clearBtn.setTitle("Clear search");
        clearBtn.addClickHandler(event -> clearSearch());
        this.add(clearBtn);

        this.initHandlers();
        this.searchBtn.setEnabled(true);

        focusTimer = new Timer() {
            @Override
            public void run() {
                SolrSearchLauncher.this.input.setFocus(true);
            }
        };
    }

    public HandlerRegistration addPanelCollapsedHandler(PanelCollapsedHandler handler){
        return addHandler(handler, PanelCollapsedEvent.TYPE);
    }

    public HandlerRegistration addPanelExpandedHandler(PanelExpandedHandler handler){
        return addHandler(handler, PanelExpandedEvent.TYPE);
    }

    public HandlerRegistration addSearchBoxArrowKeysHandler(SearchBoxArrowKeysHandler handler){
        return input.addSearchBoxArrowKeysHandler(handler);
    }

    public HandlerRegistration addSolrSearchPerformedHandler(SolrSearchPerformedHandler handler){
        return addHandler(handler, SolrSearchPerformedEvent.TYPE);
    }

    @Override
    public void onClick(ClickEvent event) {
        if(event.getSource().equals(this.searchBtn)){
            if(!isExpanded){
                expandPanel();
            }else{
                collapsePanel();
            }
        }
    }

    @Override
    public void onFacetChanged(FacetChangedEvent event) {
        searchParameters.setFacet(event.getResults().getSelectedFacet());
        searchParameters.setStartRow(0); //Go to the first page
        performSearch();
    }

    @Override
    public void onSearchUpdated(SearchBoxUpdatedEvent event) {
        searchParameters.setSearchTerm(event.getValue());
        searchParameters.setFacet(""); // By default search for all facets
        searchParameters.setStartRow(0); //Go to the first page
        performSearch();
        showHideClearBtn();

        //If the inputBox is left empty then reset the view
        if(input.getValue().isEmpty()) {
            clearSearch();
        }
    }

    @Override
    public void onSearchResult(SolrSearchResult result) {
        //TODO: Consider changing the behaviour of the server-side so that it does not return anything in this case
        String term = result.getTerm().trim();
        if(term.isEmpty() || term.length()<3){
            result.setEntries(null);
            result.setFound(0);
            result.setFacets(null);
        }
        fireEvent(new SolrSearchPerformedEvent(result));
    }

    @Override
    public void onSearchError() {
        //TODO show error
        Console.error("Search error");
    }

    @Override
    public void onSearchKeyPressed(SearchKeyPressedEvent event) {
        if(!isExpanded){
            expandPanel();
        }else{
            collapsePanel();
        }
    }

    @Override
    public void onKeysPressed(SearchBoxArrowKeysEvent event) {
        if(event.getValue() == KeyCodes.KEY_ESCAPE) {
            setFocus(false);
            this.collapsePanel();
            clearSearch();
        }
    }

    @Override
    public void onPageChanged(PageChangedEvent event) {
        searchParameters.setStartRow(event.getResults().getStartRow());
        performSearch();
    }

    public void setFocus(boolean focused){
        this.input.setFocus(focused);
    }

    private void clearSearch() {
        if (!input.getValue().isEmpty()) {
            input.setValue("");
            setFocus(true);
        }
        eventBus.fireEventFromSource(new SearchResetEvent(), this);
    }

    private void collapsePanel(){
        if(focusTimer.isRunning()){
            focusTimer.cancel();
        }
        removeStyleName(RESOURCES.getCSS().launchPanelExpanded());
        input.removeStyleName(RESOURCES.getCSS().inputActive());
        isExpanded = false;
        fireEvent(new PanelCollapsedEvent());
    }

    private void expandPanel(){
        addStyleName(RESOURCES.getCSS().launchPanelExpanded());
        input.addStyleName(RESOURCES.getCSS().inputActive());
        isExpanded = true;
        fireEvent(new PanelExpandedEvent());
        focusTimer.schedule(FOCUS_IN_TEXTBOX_DELAY);
    }

    private void initHandlers(){
        this.input.addSearchBoxUpdatedHandler(this);
        this.input.addSearchBoxArrowKeysHandler(this);
        eventBus.addHandler(SearchKeyPressedEvent.TYPE, this);
    }

    private void performSearch() {
        SearchResultFactory.searchForTerm(searchParameters, this);
    }

    private void showHideClearBtn() {
        clearBtn.setVisible(!input.getText().isEmpty());
    }


    public static Resources RESOURCES;
    static {
        RESOURCES = GWT.create(Resources.class);
        RESOURCES.getCSS().ensureInjected();
    }


    /**
     * A ClientBundle of resources used by this widget.
     */
    public interface Resources extends ClientBundle {
        /**
         * The styles used in this widget.
         */
        @Source(SearchLauncherCSS.CSS)
        SearchLauncherCSS getCSS();

        @Source("../../images/search_clicked.png")
        ImageResource launchClicked();

        @Source("../../images/search_disabled.png")
        ImageResource launchDisabled();

        @Source("../../images/search_hovered.png")
        ImageResource launchHovered();

        @Source("../../images/search_normal.png")
        ImageResource launchNormal();

        @Source("../../images/search_clicked.png")
        ImageResource clearClicked();

        @Source("../../images/search_disabled.png")
        ImageResource clearDisabled();

        @Source("../../images/search_hovered.png")
        ImageResource clearHovered();

        @Source("../../images/search_normal.png")
        ImageResource clearNormal();

        @Source("../../images/cancel.png")
        ImageResource clear();
    }

    /**
     * Styles used by this widget.
     */
    @CssResource.ImportedWithPrefix("fireworks-SolrSearchLauncher")
    public interface SearchLauncherCSS extends CssResource {
        /**
         * The path to the default CSS styles used by this resource.
         */
        String CSS = "org/reactome/web/fireworks/search/searchonfire/launcher/SolrSearchLauncher.css";

        String launchPanel();

        String launchPanelExpanded();

        String launch();

        String input();

        String inputActive();

        String clearBtn();
    }

}
