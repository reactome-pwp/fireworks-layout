package org.reactome.web.fireworks.search.searchonfire.infopanel;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import org.reactome.web.fireworks.controls.common.IconButton;
import org.reactome.web.fireworks.search.searchonfire.graph.model.GraphEntry;
import org.reactome.web.pwp.model.client.factory.DatabaseObjectImages;

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
            icon.setStyleName(css.listItemIcon());
            listItem.add(icon);

            Anchor listItemLink = new Anchor(object.getDisplayName());
            listItemLink.setStyleName(css.listItemLink());
            listItemLink.setTitle(object.getDisplayName());
            listItemLink.addClickHandler(InfoActionsHelper.getLinkClickHandler(object, eventBus, this));
            listItemLink.addDoubleClickHandler(InfoActionsHelper.getLinkDoubleClickHandler(object, eventBus, this));
            listItemLink.addMouseOverHandler(InfoActionsHelper.getLinkMouseOver(object, eventBus, this));
            listItemLink.addMouseOutHandler(InfoActionsHelper.getLinkMouseOut(eventBus, this));
            listItem.add(listItemLink);

            IconButton listItemButton = new IconButton("", DetailsInfoPanel.RESOURCES.openDiagram());
            listItemButton.setStyleName(css.listItemButton());
            listItemButton.setTitle("Go to " + object.getDisplayName());
            listItemButton.addClickHandler(InfoActionsHelper.getIconClickHandler(object, eventBus,this));
            listItem.add(listItemButton);

            listPanel.add(listItem);
        }
        this.add(listPanel);
    }
}
