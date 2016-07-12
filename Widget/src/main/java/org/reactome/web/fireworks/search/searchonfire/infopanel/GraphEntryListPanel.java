package org.reactome.web.fireworks.search.searchonfire.infopanel;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import org.reactome.web.fireworks.search.searchonfire.graph.model.GraphEntry;
import org.reactome.web.pwp.model.images.DatabaseObjectImages;

import java.util.Collection;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class GraphEntryListPanel extends FlowPanel {

    public GraphEntryListPanel(String title, Collection<? extends GraphEntry> objects, EventBus eventBus) {
        DetailsInfoPanel.ResourceCSS css = DetailsInfoPanel.RESOURCES.getCSS();

        this.setStyleName(css.databaseObjectListPanel());

        Label titleLabel = new Label(title);
        titleLabel.setStyleName(css.databaseObjectListTitle());
        this.add(titleLabel);

        FlowPanel listPanel = new FlowPanel();

        listPanel.setStyleName(css.databaseObjectList());
        for (GraphEntry object : objects) {
            FlowPanel listItem = new FlowPanel();
            listItem.setStyleName(css.listItem());

            Image icon = new Image(DatabaseObjectImages.INSTANCE.pathway());
            icon.setHeight("12px");
            icon.setWidth("12px");
            listItem.add(icon);

            Anchor listItemLink = new Anchor(object.getDisplayName());
            listItemLink.setStyleName(css.listItemLink());
            listItemLink.setTitle(object.getDisplayName());
            listItemLink.addClickHandler(InfoActionsHelper.getLinkClickHandler(object, eventBus, this));
            listItemLink.addMouseOverHandler(InfoActionsHelper.getLinkMouseOver(object, eventBus, this));
            listItemLink.addMouseOutHandler(InfoActionsHelper.getLinkMouseOut(eventBus, this));
            listItem.add(listItemLink);

            listPanel.add(listItem);
        }
        this.add(listPanel);
    }
}
