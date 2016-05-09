package org.reactome.web.fireworks.search.searchonfire.infopanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.fireworks.search.searchonfire.graph.model.GraphEntry;
import org.reactome.web.fireworks.search.searchonfire.solr.model.Entry;
import org.reactome.web.pwp.model.factory.SchemaClass;

import java.util.Arrays;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class DetailsInfoPanel extends Composite {
    private EventBus eventBus;

    private Entry selectedSuggestion;
    private GraphEntry[] result;
    private FlowPanel mainPanel;

    public DetailsInfoPanel(EventBus eventBus, Entry selectedSuggestion, GraphEntry[] result) {
        this.eventBus = eventBus;
        this.selectedSuggestion = selectedSuggestion;
        this.result = result;
        init();
    }

    private void init() {
        FlowPanel header = new FlowPanel();
        header.setStyleName(RESOURCES.getCSS().infoHeader());
        header.add(new InlineLabel(selectedSuggestion.getName()));

        Label typeLb = new Label("Type: " + SchemaClass.getSchemaClass(selectedSuggestion.getExactType()));
        Label identifierLb = new Label("Identifier: ");
        identifierLb.setStyleName(RESOURCES.getCSS().identifierLabel());

        Anchor identifierLink = new Anchor(selectedSuggestion.getStId());
        identifierLink.setStyleName(RESOURCES.getCSS().identifierLink());

        mainPanel = new FlowPanel();
        mainPanel.setStyleName(RESOURCES.getCSS().objectInfoContent());
        mainPanel.add(header);
        mainPanel.add(typeLb);
        mainPanel.add(identifierLb);
        mainPanel.add(identifierLink);

        String title = "Present in " + result.length + (result.length > 1 ? " pathways" : " pathway");
        mainPanel.add( new GraphEntryListPanel(title, Arrays.asList(result), eventBus));

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

        String listItemLink();
    }
}
