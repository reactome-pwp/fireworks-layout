package org.reactome.web.fireworks.search.fallback.launcher;

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
import org.reactome.web.fireworks.events.SearchKeyPressedEvent;
import org.reactome.web.fireworks.handlers.SearchKeyPressedHandler;
import org.reactome.web.fireworks.legends.ControlButton;
import org.reactome.web.fireworks.model.Graph;
import org.reactome.web.fireworks.model.Node;
import org.reactome.web.fireworks.search.fallback.events.PanelCollapsedEvent;
import org.reactome.web.fireworks.search.fallback.events.PanelExpandedEvent;
import org.reactome.web.fireworks.search.fallback.events.SuggestionHoveredEvent;
import org.reactome.web.fireworks.search.fallback.events.SuggestionSelectedEvent;
import org.reactome.web.fireworks.search.fallback.handlers.PanelCollapsedHandler;
import org.reactome.web.fireworks.search.fallback.handlers.PanelExpandedHandler;
import org.reactome.web.fireworks.search.fallback.handlers.SuggestionHoveredHandler;
import org.reactome.web.fireworks.search.fallback.handlers.SuggestionSelectedHandler;
import org.reactome.web.fireworks.search.fallback.provider.SuggestionsProvider;
import org.reactome.web.fireworks.search.fallback.provider.SuggestionsProviderImpl;
import org.reactome.web.fireworks.search.fallback.searchbox.*;

import java.util.List;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class SearchLauncher extends AbsolutePanel implements ClickHandler, SearchBoxUpdatedHandler,
        SuggestionHoveredHandler, SuggestionSelectedHandler, SearchBoxArrowKeysHandler,
        SearchKeyPressedHandler {

    @SuppressWarnings("FieldCanBeLocal")
    private static String OPENING_TEXT = "Search for a pathway ...";
    private static int FOCUS_IN_TEXTBOX_DELAY = 300;
    private static int FOCUS_IN_PATHWAY_DELAY = 500;

    private EventBus eventBus;
    private SuggestionsProvider<Node> suggestionsProvider;

    private SearchBox input = null;
    private ControlButton searchBtn = null;

    private Boolean isExpanded = false;

    private Timer focusTimer;
    private Timer focusOnPathwayTimer;

    public SearchLauncher(EventBus eventBus, Graph graph) {

        //Setting the search style
        setStyleName(RESOURCES.getCSS().launchPanel());

        this.eventBus = eventBus;
        this.suggestionsProvider = new SuggestionsProviderImpl(graph);

        this.searchBtn = new ControlButton("Search pathways", RESOURCES.getCSS().launch(), this);
        this.add(searchBtn);

        this.input = new SearchBox();
        this.input.setStyleName(RESOURCES.getCSS().input());
        this.input.getElement().setPropertyString("placeholder", OPENING_TEXT);
        this.add(input);

        this.initHandlers();
        this.searchBtn.setEnabled(true);

        focusTimer = new Timer() {
            @Override
            public void run() {
                SearchLauncher.this.input.setFocus(true);
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

    public HandlerRegistration addSearchPerformedHandler(SearchPerformedHandler handler){
        return addHandler(handler, SearchPerformedEvent.TYPE);
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
    public void onSearchUpdated(SearchBoxUpdatedEvent event) {
        if(suggestionsProvider!=null) {
            String term = input.getText().trim();
            List<Node> suggestions = suggestionsProvider.getSuggestions(term);
            fireEvent(new SearchPerformedEvent(term, suggestions));
        }
    }

    @Override
    public void onSuggestionHovered(final SuggestionHoveredEvent event) {
        eventBus.fireEventFromSource(event, this);

        if(focusOnPathwayTimer!=null && focusOnPathwayTimer.isRunning()) {
            focusOnPathwayTimer.cancel();
        }

        focusOnPathwayTimer = new Timer() {
            @Override
            public void run() {
                eventBus.fireEventFromSource(new SuggestionHoveredEvent(event.getHoveredObject(), Boolean.TRUE), this);
            }
        };
        focusOnPathwayTimer.schedule(FOCUS_IN_PATHWAY_DELAY);
    }

    @Override
    public void onSuggestionSelected(SuggestionSelectedEvent event) {
        eventBus.fireEventFromSource(event, this);
    }

    @Override
    public void onSearchKeyPressed(SearchKeyPressedEvent event) {
        if(!isExpanded){
            expandPanel();
        }else{
            collapsePanel();
        }
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

    public void setFocus(boolean focused){
        this.input.setFocus(focused);
    }

    @Override
    public void onKeysPressed(SearchBoxArrowKeysEvent event) {
        if(event.getValue() == KeyCodes.KEY_ESCAPE) {
            setFocus(false);
            this.collapsePanel();
        }
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
    }

    /**
     * Styles used by this widget.
     */
    @CssResource.ImportedWithPrefix("fireworks-SearchLauncher")
    public interface SearchLauncherCSS extends CssResource {
        /**
         * The path to the default CSS styles used by this resource.
         */
        String CSS = "org/reactome/web/fireworks/search/fallback/launcher/SearchLauncher.css";

        String launchPanel();

        String launchPanelExpanded();

        String launch();

        String input();

        String inputActive();
    }

}
