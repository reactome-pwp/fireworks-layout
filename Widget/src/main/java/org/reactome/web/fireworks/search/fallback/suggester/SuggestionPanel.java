package org.reactome.web.fireworks.search.fallback.suggester;

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
import org.reactome.web.fireworks.model.Node;
import org.reactome.web.fireworks.search.fallback.events.SuggestionHoveredEvent;
import org.reactome.web.fireworks.search.fallback.events.SuggestionSelectedEvent;
import org.reactome.web.fireworks.search.fallback.handlers.SuggestionHoveredHandler;
import org.reactome.web.fireworks.search.fallback.handlers.SuggestionSelectedHandler;
import org.reactome.web.fireworks.search.fallback.launcher.SearchPerformedEvent;
import org.reactome.web.fireworks.search.fallback.launcher.SearchPerformedHandler;
import org.reactome.web.fireworks.search.fallback.panels.AbstractAccordionPanel;
import org.reactome.web.fireworks.search.fallback.searchbox.SearchBoxArrowKeysEvent;
import org.reactome.web.fireworks.search.fallback.searchbox.SearchBoxArrowKeysHandler;

import java.util.List;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@SuppressWarnings("Duplicates")
public class SuggestionPanel extends AbstractAccordionPanel implements SearchPerformedHandler, SearchBoxArrowKeysHandler,
        SelectionChangeEvent.Handler, DoubleClickHandler {
    private final SingleSelectionModel<Node> selectionModel;
    private CellList<Node> suggestions;
    private ListDataProvider<Node> dataProvider;

    public static SuggestionResources RESOURCES;

    static {
        RESOURCES = GWT.create(SuggestionResources.class);
        RESOURCES.getCSS().ensureInjected();
    }

    /**
     * The key provider that provides the unique ID of a DatabaseObject.
     */
    public static final ProvidesKey<Node> KEY_PROVIDER = new ProvidesKey<Node>() {
        @Override
        public Object getKey(Node item) {
            return item == null ? null : item.getDbId();
        }
    };

    public SuggestionPanel() {
        this.sinkEvents(Event.ONCLICK);

        // Add a selection model so we can select cells.
        selectionModel = new SingleSelectionModel<>(KEY_PROVIDER);
        selectionModel.addSelectionChangeHandler(this);

        SuggestionCell suggestionCell = new SuggestionCell();

        suggestions = new CellList<>(suggestionCell, KEY_PROVIDER);
        suggestions.sinkEvents(Event.FOCUSEVENTS);
        suggestions.setSelectionModel(selectionModel);

        suggestions.addDomHandler(this, DoubleClickEvent.getType());

        suggestions.setKeyboardPagingPolicy(HasKeyboardPagingPolicy.KeyboardPagingPolicy.INCREASE_RANGE);
        suggestions.setKeyboardSelectionPolicy(HasKeyboardSelectionPolicy.KeyboardSelectionPolicy.DISABLED);

        dataProvider = new ListDataProvider<>();
        dataProvider.addDataDisplay(this.suggestions);

        this.add(suggestions);
        //Setting the legend style
        setStyleName(RESOURCES.getCSS().suggestionPanel());
    }

    public HandlerRegistration addClickHandler(ClickHandler handler){
        return addHandler(handler, ClickEvent.getType());
    }

    public HandlerRegistration addSuggestionHoveredHandler(SuggestionHoveredHandler handler) {
        return addHandler(handler, SuggestionHoveredEvent.TYPE);
    }

    public HandlerRegistration addSuggestionSelectedHandler(SuggestionSelectedHandler handler) {
        return addHandler(handler, SuggestionSelectedEvent.TYPE);
    }

    @Override
    public void onDoubleClick(DoubleClickEvent event) {
        event.stopPropagation(); event.preventDefault();
        if(clickTimer !=null) clickTimer.cancel();
        Node selected = selectionModel.getSelectedObject();
        if (selected != null) {
            fireEvent(new SuggestionSelectedEvent(selected, Boolean.TRUE));
        }
    }

    @Override
    public void onKeysPressed(SearchBoxArrowKeysEvent event) {
        if(suggestions.getRowCount()>0) {
            Node current = selectionModel.getSelectedObject();
            int currentIndex = current == null ? -1 : dataProvider.getList().indexOf(current);
            int toIndex = currentIndex;
            if(event.getValue() == KeyCodes.KEY_ENTER){
                fireEvent(new SuggestionSelectedEvent(current, Boolean.TRUE));
            }else if(event.getValue() == KeyCodes.KEY_DOWN) {
                toIndex = currentIndex + 1 < dataProvider.getList().size() ? currentIndex + 1 : dataProvider.getList().size() - 1;
            }else if(event.getValue() == KeyCodes.KEY_UP) {
                toIndex = currentIndex - 1 > 0 ? currentIndex - 1 : 0;
            }
            if(toIndex!=-1 && toIndex!=currentIndex) {
                Node newSelection = dataProvider.getList().get(toIndex);
                suggestions.getRowElement(toIndex).scrollIntoView();
                selectionModel.setSelected(newSelection, true);
            }
        }
    }

    @Override
    public void onSearchPerformed(SearchPerformedEvent event) {
        Node sel = selectionModel.getSelectedObject();
        List<Node> searchResult = event.getSuggestions();
        if(!searchResult.isEmpty() && !searchResult.contains(sel)) {
            selectionModel.clear();
        } else if (searchResult.isEmpty() && !event.getTerm().isEmpty()){
            suggestions.setEmptyListWidget(new HTML("No results found for '" + event.getTerm() +"'"));
        } else {
            suggestions.setEmptyListWidget(null);
        }

        dataProvider.getList().clear();
        dataProvider.getList().addAll(searchResult);
        suggestions.setVisibleRange(0, searchResult.size()); //configure list paging
        suggestions.setRowCount(searchResult.size());

        if (dataProvider.getList().isEmpty()) {
            fireEvent(new SuggestionSelectedEvent(null));
        }
    }

    Timer clickTimer;

    @Override
    public void onSelectionChange(SelectionChangeEvent event) {
        clickTimer = new Timer() {
            @Override
            public void run() {
                fireEvent(new SuggestionHoveredEvent(selectionModel.getSelectedObject()));
            }
        };
        clickTimer.schedule(500);
    }

    /**
     * A ClientBundle of resources used by this widget.
     */
    public interface SuggestionResources extends ClientBundle {
        /**
         * The styles used in this widget.
         */
        @Source(SuggestionPanelCSS.CSS)
        SuggestionPanelCSS getCSS();

        @Source("images/pathway.png")
        ImageResource pathway();
    }

    /**
     * Styles used by this widget.
     */
    @CssResource.ImportedWithPrefix("fireworks-SuggestionPanel")
    public interface SuggestionPanelCSS extends CssResource {
        /**
         * The path to the default CSS styles used by this resource.
         */
        String CSS = "org/reactome/web/fireworks/search/fallback/suggester/SuggestionPanel.css";

        String suggestionPanel();
    }

}
