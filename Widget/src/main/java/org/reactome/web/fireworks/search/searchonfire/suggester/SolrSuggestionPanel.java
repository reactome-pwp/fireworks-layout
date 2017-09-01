package org.reactome.web.fireworks.search.searchonfire.suggester;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.HasKeyboardPagingPolicy;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import org.reactome.web.fireworks.search.fallback.panels.AbstractAccordionPanel;
import org.reactome.web.fireworks.search.fallback.searchbox.SearchBoxArrowKeysEvent;
import org.reactome.web.fireworks.search.fallback.searchbox.SearchBoxArrowKeysHandler;
import org.reactome.web.fireworks.search.searchonfire.events.SolrSuggestionSelectedEvent;
import org.reactome.web.fireworks.search.searchonfire.facets.FacetChangedHandler;
import org.reactome.web.fireworks.search.searchonfire.facets.FacetsPanel;
import org.reactome.web.fireworks.search.searchonfire.handlers.IncludeAllFormsHandler;
import org.reactome.web.fireworks.search.searchonfire.handlers.SolrSuggestionSelectedHandler;
import org.reactome.web.fireworks.search.searchonfire.launcher.SolrSearchPerformedEvent;
import org.reactome.web.fireworks.search.searchonfire.launcher.SolrSearchPerformedHandler;
import org.reactome.web.fireworks.search.searchonfire.options.OptionsPanel;
import org.reactome.web.fireworks.search.searchonfire.pager.PageChangedHandler;
import org.reactome.web.fireworks.search.searchonfire.pager.Pager;
import org.reactome.web.fireworks.search.searchonfire.solr.model.Entry;
import org.reactome.web.fireworks.search.searchonfire.solr.model.SolrSearchResult;
import org.reactome.web.pwp.model.client.classes.DatabaseObject;
import org.reactome.web.pwp.model.client.common.ContentClientHandler;
import org.reactome.web.pwp.model.client.content.ContentClient;
import org.reactome.web.pwp.model.client.content.ContentClientError;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
@SuppressWarnings("Duplicates")
public class SolrSuggestionPanel extends AbstractAccordionPanel implements SolrSearchPerformedHandler, SearchBoxArrowKeysHandler,
        SelectionChangeEvent.Handler {

    private final SingleSelectionModel<Entry> selectionModel;
    private CellList<Entry> suggestions;
    private ListDataProvider<Entry> dataProvider;
    private Pager pager;
    private FacetsPanel facetsPanel;
    private OptionsPanel optionsPanel;

    private boolean selectFirstRow;
    private boolean selectLastRow;


    /**
     * The key provider that provides the unique ID of a DatabaseObject.
     */
    public static final ProvidesKey<Entry> KEY_PROVIDER = item -> item == null ? null : item.getId();

    public SolrSuggestionPanel() {
        this.sinkEvents(Event.ONCLICK);

        // Add a selection model so we can select cells.
        selectionModel = new SingleSelectionModel<>(KEY_PROVIDER);
        selectionModel.addSelectionChangeHandler(this);

        init();
    }

    public HandlerRegistration addClickHandler(ClickHandler handler){
        return addHandler(handler, ClickEvent.getType());
    }

    public HandlerRegistration addFacetChangedHandler(FacetChangedHandler handler) {
        return facetsPanel.addFacetChangedHandler(handler);
    }

    public HandlerRegistration addIncludeAllInstancesHandler(IncludeAllFormsHandler handler) {
        return optionsPanel.addIncludeAllInstancesHandler(handler);
    }

    public HandlerRegistration addSolrSuggestionSelectedHandler(SolrSuggestionSelectedHandler handler) {
        return addHandler(handler, SolrSuggestionSelectedEvent.TYPE);
    }

    public HandlerRegistration addPageChangedHandler(PageChangedHandler handler){
        return pager.addPageChangedHandler(handler);
    }

    @Override
    public void onKeysPressed(SearchBoxArrowKeysEvent event) {
        if(suggestions.getRowCount()>0) {
            Entry current = selectionModel.getSelectedObject();
            int currentIndex = current == null ? -1 : dataProvider.getList().indexOf(current);
            int toIndex = currentIndex;

            if(event.getValue() == KeyCodes.KEY_DOWN) {
                if(currentIndex + 1 < dataProvider.getList().size()) {
                    toIndex = currentIndex + 1;
                } else {
                    // Go to next page
                    selectFirstRow = true;
                    selectLastRow = false;
                    pager.loadNextPage();
                }
            }else if(event.getValue() == KeyCodes.KEY_UP) {
                if(currentIndex - 1 >= 0) {
                    toIndex = currentIndex - 1;
                } else {
                    // Go to previous page
                    selectFirstRow = false;
                    selectLastRow = true;
                    pager.loadPreviousPage();
                }
            }
            if(toIndex!=-1 && toIndex!=currentIndex) {
                Entry newSelection = dataProvider.getList().get(toIndex);
                selectionModel.setSelected(newSelection, true);
            }
        }
    }

    @Override
    public void onSolrSearchPerformed(SolrSearchPerformedEvent event) {
        Entry sel = selectionModel.getSelectedObject();
        SolrSearchResult searchResult = event.getSuggestions();
        String term = searchResult.getTerm();
        List<Entry> entries = searchResult.getEntries()!=null ? searchResult.getEntries() : new ArrayList<>();

        if (!entries.isEmpty() && !entries.contains(sel)) {
            selectionModel.clear();
        } else if (entries.isEmpty() && term.length()>0 && term.length()<3){
            suggestions.setEmptyListWidget(new HTML("Please type at least 3 characters"));
        } else if (entries.isEmpty() && term.length()>3){
            suggestions.setEmptyListWidget(new HTML("No results found for '" + term +"'"));
        } else {
            suggestions.setEmptyListWidget(null);
        }
        pager.setResults(searchResult);
        facetsPanel.setResults(searchResult);
        optionsPanel.setVisible(!entries.isEmpty());

        dataProvider.getList().clear();
        dataProvider.getList().addAll(entries);
        suggestions.setVisibleRange(0, entries.size()); //configure list paging
        suggestions.setRowCount(entries.size());

        if (dataProvider.getList().isEmpty()) {
            fireEvent(new SolrSuggestionSelectedEvent(null));
        }

        if(selectFirstRow) {
            Entry newSelection = dataProvider.getList().get(0);
            selectionModel.setSelected(newSelection, true);
            selectFirstRow = false;
        } else if (selectLastRow) {
            Entry newSelection = dataProvider.getList().get(dataProvider.getList().size()-1);
            selectionModel.setSelected(newSelection, true);
            selectLastRow = false;
        }
    }

    @Override
    public void onSelectionChange(SelectionChangeEvent event) {
        Entry selection = selectionModel.getSelectedObject();
        if(selection!=null && !selection.getExactType().equalsIgnoreCase("Interactor")) { //To be removed when interactors are addressed
            ContentClient.query(selection.getId(), new ContentClientHandler.ObjectLoaded<DatabaseObject>() {
                @Override
                public void onObjectLoaded(DatabaseObject databaseObject) {
                    optionsPanel.setEnable(!(databaseObject instanceof org.reactome.web.pwp.model.client.classes.Event));
                    fireEvent(new SolrSuggestionSelectedEvent(databaseObject));
                }

                @Override
                public void onContentClientException(Type type, String message) {
                    //TODO
                }

                @Override
                public void onContentClientError(ContentClientError error) {
                    //TODO
                }
            });
        } else {
            fireEvent(new SolrSuggestionSelectedEvent(null)); //To be removed when interactors are addressed
        }
    }

    private void init(){
        //Setting the legend style
        setStyleName(RESOURCES.getCSS().mainPanel());

        SolrSuggestionCell suggestionCell = new SolrSuggestionCell();

        suggestions = new CellList(suggestionCell, KEY_PROVIDER);
        suggestions.sinkEvents(Event.FOCUSEVENTS);
        suggestions.setSelectionModel(selectionModel);
        suggestions.addStyleName(RESOURCES.getCSS().list());
        suggestions.setKeyboardPagingPolicy(HasKeyboardPagingPolicy.KeyboardPagingPolicy.INCREASE_RANGE);
        suggestions.setKeyboardSelectionPolicy(HasKeyboardSelectionPolicy.KeyboardSelectionPolicy.DISABLED);

        dataProvider = new ListDataProvider<>();
        dataProvider.addDataDisplay(this.suggestions);

        pager = new Pager();
        pager.addStyleName(RESOURCES.getCSS().pager());

        facetsPanel = new FacetsPanel();
        facetsPanel.addStyleName(RESOURCES.getCSS().facetsPanel());

        optionsPanel = new OptionsPanel();
        optionsPanel.addStyleName(RESOURCES.getCSS().optionsPanel());

        add(suggestions);
        add(pager);
        add(facetsPanel);
        add(optionsPanel);
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
        @Source(SolrSuggestionPanelCSS.CSS)
        SolrSuggestionPanelCSS getCSS();

        @Source("images/Interactor.png")
        ImageResource interactor();
    }

    /**
     * Styles used by this widget.
     */
    @CssResource.ImportedWithPrefix("fireworks-SolrSuggestionPanel")
    public interface SolrSuggestionPanelCSS extends CssResource {
        /**
         * The path to the default CSS styles used by this resource.
         */
        String CSS = "org/reactome/web/fireworks/search/searchonfire/suggester/SolrSuggestionPanel.css";

        String mainPanel();

        String list();

        String pager();

        String facetsPanel();

        String optionsPanel();
    }
}