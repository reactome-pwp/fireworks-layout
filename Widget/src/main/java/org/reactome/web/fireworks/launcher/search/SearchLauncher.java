package org.reactome.web.fireworks.launcher.search;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.TextBox;
import org.reactome.web.fireworks.legends.ControlButton;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class SearchLauncher extends AbsolutePanel implements BlurHandler, ClickHandler, FocusHandler,
        MouseOutHandler, MouseOverHandler, HasMouseOutHandlers, HasMouseOverHandlers {

    @SuppressWarnings("FieldCanBeLocal")
    private static String OPENING_TEXT = "Search for any term ...";

    private EventBus eventBus;
//    private SuggestionsProvider<DatabaseObject> suggestionsProvider;

//    private SearchBox input = null;
    private ControlButton searchBtn = null;

    private Boolean isExpanded = false;
    private Boolean mustStayExpanded = false;

    public SearchLauncher(EventBus eventBus) {
        //Setting the search style
        setStyleName(RESOURCES.getCSS().launchPanel());

        this.eventBus = eventBus;

        this.searchBtn = new ControlButton("Search", RESOURCES.getCSS().launch(), this);
        this.add(searchBtn);

//        this.input = new SearchBox();
//        this.input.setStyleName(RESOURCES.getCSS().input());
//        this.input.getElement().setPropertyString("placeholder", OPENING_TEXT);
//        this.add(input);

//        this.setVisible(false);
        this.searchBtn.setEnabled(false);
    }

    @Override
    public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
        sinkEvents(Event.ONMOUSEOVER);
        return addHandler(handler, MouseOverEvent.getType());
    }

    @Override
    public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
        sinkEvents(Event.ONMOUSEOUT);
        return addHandler(handler, MouseOutEvent.getType());
    }

    @Override
    public void onBlur(BlurEvent event) {
        TextBox textBox = (TextBox) event.getSource();
        if(textBox.getValue().isEmpty()){
            //This is used to avoid the expansion of the panel by the following onclick event
            Timer delayTimer = new Timer() {
                @Override
                public void run() {
                    collapsePanel();
                }
            };
            delayTimer.schedule(100);
        }
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
    public void onFocus(FocusEvent event) {
        mustStayExpanded = true;
    }

    @Override
    public void onMouseOut(MouseOutEvent event) {
        if(isExpanded && !mustStayExpanded) {
            collapsePanel();
        }
    }

    @Override
    public void onMouseOver(MouseOverEvent event) {
        if(!isExpanded) {
            expandPanel();
        }
    }

    private void collapsePanel(){
        removeStyleName(RESOURCES.getCSS().launchPanelExpanded());
//        input.removeStyleName(RESOURCES.getCSS().inputActive());
        isExpanded = false;
        mustStayExpanded = false;
//        fireEvent(new PanelCollapsedEvent());
    }

    private void expandPanel(){
        addStyleName(RESOURCES.getCSS().launchPanelExpanded());
//        input.addStyleName(RESOURCES.getCSS().inputActive());
        isExpanded = true;
//        fireEvent(new PanelExpandedEvent());
    }

    public static SearchLauncherResources RESOURCES;
    static {
        RESOURCES = GWT.create(SearchLauncherResources.class);
        RESOURCES.getCSS().ensureInjected();
    }

    /**
     * A ClientBundle of resources used by this widget.
     */
    public interface SearchLauncherResources extends ClientBundle {
        /**
         * The styles used in this widget.
         */
        @Source(SearchLauncherCSS.CSS)
        SearchLauncherCSS getCSS();

        @Source("images/search_clicked.png")
        ImageResource launchClicked();

        @Source("images/search_disabled.png")
        ImageResource launchDisabled();

        @Source("images/search_hovered.png")
        ImageResource launchHovered();

        @Source("images/search_normal.png")
        ImageResource launchNormal();
    }

    /**
     * Styles used by this widget.
     */
    @CssResource.ImportedWithPrefix("diagram-SearchLauncher")
    public interface SearchLauncherCSS extends CssResource {
        /**
         * The path to the default CSS styles used by this resource.
         */
        String CSS = "org/reactome/web/fireworks/launcher/search/SearchLauncher.css";

        String launchPanel();

        String launchPanelExpanded();

        String launch();

        String input();

        String inputActive();
    }

}
