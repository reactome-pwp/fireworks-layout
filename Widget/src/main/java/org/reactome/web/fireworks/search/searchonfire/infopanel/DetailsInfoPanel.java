package org.reactome.web.fireworks.search.searchonfire.infopanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.fireworks.search.searchonfire.graph.model.GraphEntry;
import org.reactome.web.fireworks.search.searchonfire.solr.model.Entry;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class DetailsInfoPanel extends Composite {
    private Entry selectedSuggestion;
    private GraphEntry[] result;
    private FlowPanel mainPanel;

    public DetailsInfoPanel(Entry selectedSuggestion, GraphEntry[] result) {
        this.selectedSuggestion = selectedSuggestion;
        this.result = result;
        init();
    }

    private void init() {
        FlowPanel header = new FlowPanel();
        header.setStyleName(RESOURCES.getCSS().infoHeader());
        header.add(new InlineLabel(selectedSuggestion.getName()));

        Label typeLb = new Label("Type: " + selectedSuggestion.getExactType());
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

        FlowPanel d = new FlowPanel();
        for (GraphEntry entry : result) {
            d.add(new Label(entry.getDisplayName()));
        }
        mainPanel.add(d);

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
