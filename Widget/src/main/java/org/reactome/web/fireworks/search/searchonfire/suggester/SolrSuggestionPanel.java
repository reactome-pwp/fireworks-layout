package org.reactome.web.fireworks.search.searchonfire.suggester;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.HasKeyboardPagingPolicy;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import org.reactome.web.fireworks.controls.common.pager.PageChangedHandler;
import org.reactome.web.fireworks.controls.common.pager.Pager;
import org.reactome.web.fireworks.search.fallback.events.SuggestionHoveredEvent;
import org.reactome.web.fireworks.search.fallback.events.SuggestionSelectedEvent;
import org.reactome.web.fireworks.search.fallback.handlers.SuggestionHoveredHandler;
import org.reactome.web.fireworks.search.fallback.handlers.SuggestionSelectedHandler;
import org.reactome.web.fireworks.search.fallback.panels.AbstractAccordionPanel;
import org.reactome.web.fireworks.search.fallback.searchbox.SearchBoxArrowKeysEvent;
import org.reactome.web.fireworks.search.fallback.searchbox.SearchBoxArrowKeysHandler;
import org.reactome.web.fireworks.search.searchonfire.facets.FacetChangedHandler;
import org.reactome.web.fireworks.search.searchonfire.facets.FacetsPanel;
import org.reactome.web.fireworks.search.searchonfire.launcher.SolrSearchPerformedEvent;
import org.reactome.web.fireworks.search.searchonfire.launcher.SolrSearchPerformedHandler;
import org.reactome.web.fireworks.search.searchonfire.solr.model.Entry;
import org.reactome.web.fireworks.search.searchonfire.solr.model.FireworksResult;

import java.util.Arrays;
import java.util.List;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
@SuppressWarnings("Duplicates")
public class SolrSuggestionPanel extends AbstractAccordionPanel implements SolrSearchPerformedHandler, SearchBoxArrowKeysHandler,
        SelectionChangeEvent.Handler, DoubleClickHandler {

    private final SingleSelectionModel<Entry> selectionModel;
    private CellList<Entry> suggestions;
    private ListDataProvider<Entry> dataProvider;
    private Pager pager;
    private FacetsPanel facetsPanel;

    /**
     * The key provider that provides the unique ID of a DatabaseObject.
     */
    public static final ProvidesKey<Entry> KEY_PROVIDER = item -> item == null ? null : item.getStId();

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

    public HandlerRegistration addSuggestionHoveredHandler(SuggestionHoveredHandler handler) {
        return addHandler(handler, SuggestionHoveredEvent.TYPE);
    }

    public HandlerRegistration addSuggestionSelectedHandler(SuggestionSelectedHandler handler) {
        return addHandler(handler, SuggestionSelectedEvent.TYPE);
    }

    public HandlerRegistration addPageChangedHandler(PageChangedHandler handler){
        return pager.addPageChangedHandler(handler);
    }

    @Override
    public void onDoubleClick(DoubleClickEvent event) {
        event.stopPropagation(); event.preventDefault();
        if(clickTimer !=null) clickTimer.cancel();
        Entry selected = selectionModel.getSelectedObject();
        if (selected != null) {
//            fireEvent(new SuggestionSelectedEvent(selected, Boolean.TRUE));
        }
    }

    @Override
    public void onKeysPressed(SearchBoxArrowKeysEvent event) {
        if(suggestions.getRowCount()>0) {
            Entry current = selectionModel.getSelectedObject();
            int currentIndex = current == null ? -1 : dataProvider.getList().indexOf(current);
            int toIndex = currentIndex;
            if(event.getValue() == KeyCodes.KEY_ENTER){
//                fireEvent(new SuggestionSelectedEvent(current, Boolean.TRUE));
            }else if(event.getValue() == KeyCodes.KEY_DOWN) {
                toIndex = currentIndex + 1 < dataProvider.getList().size() ? currentIndex + 1 : dataProvider.getList().size() - 1;
            }else if(event.getValue() == KeyCodes.KEY_UP) {
                toIndex = currentIndex - 1 > 0 ? currentIndex - 1 : 0;
            }
            if(toIndex!=-1 && toIndex!=currentIndex) {
//                Node newSelection = dataProvider.getList().get(toIndex);
//                suggestions.getRowElement(toIndex).scrollIntoView();
//                selectionModel.setSelected(newSelection, true);
            }
        }
    }

    @Override
    public void onSolrSearchPerformed(SolrSearchPerformedEvent event) {
        Entry sel = selectionModel.getSelectedObject();
        FireworksResult searchResult = event.getSuggestions();
        String term = searchResult.getTerm();
        List<Entry> entries = searchResult.getEntries()!=null ? Arrays.asList(searchResult.getEntries()) : null;

        if (!entries.isEmpty() && !entries.contains(sel)) {
            selectionModel.clear();
        } else if (entries.isEmpty() && !term.isEmpty()){
            suggestions.setEmptyListWidget(new HTML("No results found for '" + term +"'"));
        } else {
            suggestions.setEmptyListWidget(null);
        }
        pager.setResults(searchResult);
        facetsPanel.setResults(searchResult);

        dataProvider.getList().clear();
        dataProvider.getList().addAll(entries);
        suggestions.setVisibleRange(0, entries.size()); //configure list paging
        suggestions.setRowCount(entries.size());

        if (dataProvider.getList().isEmpty()) {
            fireEvent(new SuggestionSelectedEvent(null));
        }
    }

    Timer clickTimer;

    @Override
    public void onSelectionChange(SelectionChangeEvent event) {
//        clickTimer = new Timer() {
//            @Override
//            public void run() {
//                fireEvent(new SuggestionHoveredEvent(selectionModel.getSelectedObject()));
//            }
//        };
//        clickTimer.schedule(500);
    }

    private void init(){
        //Setting the legend style
        setStyleName(RESOURCES.getCSS().mainPanel());

        SolrSuggestionCell suggestionCell = new SolrSuggestionCell();

        suggestions = new CellList(suggestionCell, KEY_PROVIDER);
        suggestions.sinkEvents(Event.FOCUSEVENTS);
        suggestions.setSelectionModel(selectionModel);
        suggestions.addStyleName(RESOURCES.getCSS().list());
        suggestions.addDomHandler(this, DoubleClickEvent.getType());
        suggestions.setKeyboardPagingPolicy(HasKeyboardPagingPolicy.KeyboardPagingPolicy.INCREASE_RANGE);
        suggestions.setKeyboardSelectionPolicy(HasKeyboardSelectionPolicy.KeyboardSelectionPolicy.DISABLED);

        dataProvider = new ListDataProvider<>();
        dataProvider.addDataDisplay(this.suggestions);

        pager = new Pager();
        pager.addStyleName(RESOURCES.getCSS().pager());

        facetsPanel = new FacetsPanel();
        facetsPanel.addStyleName(RESOURCES.getCSS().facetsPanel());

        add(suggestions);
        add(pager);
        add(facetsPanel);
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

        @Source("images/pathway.png")
        ImageResource pathway();
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
    }
}