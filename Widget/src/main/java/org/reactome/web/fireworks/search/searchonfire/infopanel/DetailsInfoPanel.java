package org.reactome.web.fireworks.search.searchonfire.infopanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.fireworks.events.GraphEntrySelectedEvent;
import org.reactome.web.fireworks.events.SearchFilterEvent;
import org.reactome.web.fireworks.search.searchonfire.graph.model.GraphEntry;
import org.reactome.web.pwp.model.client.classes.DatabaseObject;
import org.reactome.web.pwp.model.client.classes.Pathway;

import java.util.Arrays;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class DetailsInfoPanel extends Composite {
    private EventBus eventBus;

    private DatabaseObject selectedSuggestion;
    private GraphEntry[] result;
    private FlowPanel mainPanel;

    public DetailsInfoPanel(EventBus eventBus, DatabaseObject selectedSuggestion, GraphEntry[] result) {
        this.eventBus = eventBus;
        this.selectedSuggestion = selectedSuggestion;
        this.result = result;
        init();
    }

    private void init() {
        FlowPanel header = new FlowPanel();
        header.setStyleName(RESOURCES.getCSS().infoHeader());
        header.add(new InlineLabel(selectedSuggestion.getDisplayName()));

        Label typeLb = new Label("Type: " + selectedSuggestion.getSchemaClass().name);
        Label identifierLb = new Label("Identifier: ");
        identifierLb.setStyleName(RESOURCES.getCSS().identifierLabel());

        Anchor identifierLink = new Anchor(selectedSuggestion.getReactomeIdentifier());
        identifierLink.setStyleName(RESOURCES.getCSS().identifierLink());
        identifierLink.setTitle("Click to find out more");
        identifierLink.addClickHandler(event -> {
            String url = "http://www.reactome.org/content/detail/" + selectedSuggestion.getReactomeIdentifier();
            Window.open(url, "_blank", "");
        });

        mainPanel = new FlowPanel();
        mainPanel.setStyleName(RESOURCES.getCSS().objectInfoContent());
        mainPanel.add(header);
        mainPanel.add(typeLb);
        mainPanel.add(identifierLb);
        mainPanel.add(identifierLink);

        if(!(selectedSuggestion instanceof Pathway)) {
            String title = "Present in " + result.length + (result.length > 1 ? " pathways" : " pathway");
            mainPanel.add(new GraphEntryListPanel(title, Arrays.asList(result), eventBus));
        } else {
            eventBus.fireEventFromSource(new SearchFilterEvent(result), this);
            eventBus.fireEventFromSource(new GraphEntrySelectedEvent(result[0]), this); // In case of a pathway, besides highlighting, select it.
        }

        SimplePanel sp = new SimplePanel();
        sp.setStyleName(RESOURCES.getCSS().objectInfoPanel());
        sp.add(mainPanel);
        initWidget(sp);
    }


    public static Resources RESOURCES;
    static {
        RESOURCES = GWT.create(Resources.class);
        RESOURCES.getCSS().ensureInjected();
    }

    public interface Resources extends ClientBundle {
        @Source(ResourceCSS.CSS)
        ResourceCSS getCSS();

        @Source("../../images/open_diagram.png")
        ImageResource openDiagram();
    }

    @CssResource.ImportedWithPrefix("fireworks-DetailsInfoPanel")
    public interface ResourceCSS extends CssResource {

        String CSS = "org/reactome/web/fireworks/search/searchonfire/infopanel/DetailsInfoPanel.css";

        String objectInfoPanel();

        String objectInfoContent();

        String infoHeader();

        String identifierLabel();

        String identifierLink();

        String databaseObjectListPanel();

        String databaseObjectListTitle();

        String databaseObjectList();

        String listItem();

        String listItemIcon();

        String listItemLink();

        String listItemButton();
    }
}
