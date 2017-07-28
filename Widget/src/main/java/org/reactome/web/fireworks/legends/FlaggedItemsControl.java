package org.reactome.web.fireworks.legends;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import org.reactome.web.fireworks.controls.common.PwpButton;
import org.reactome.web.fireworks.events.NodeFlagRequestedEvent;
import org.reactome.web.fireworks.events.NodeFlaggedEvent;
import org.reactome.web.fireworks.events.NodeFlaggedResetEvent;
import org.reactome.web.fireworks.handlers.NodeFlagRequestedHandler;
import org.reactome.web.fireworks.handlers.NodeFlaggedHandler;
import org.reactome.web.fireworks.handlers.NodeFlaggedResetHandler;
import org.reactome.web.fireworks.model.Node;

import java.util.Collection;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class FlaggedItemsControl extends LegendPanel implements ClickHandler,
        NodeFlaggedHandler, NodeFlaggedResetHandler, NodeFlagRequestedHandler {

    private InlineLabel msgLabel;
    private PwpButton closeBtn;
    private Image loadingIcon;

    public FlaggedItemsControl(final EventBus eventBus) {
        super(eventBus);

        LegendPanelCSS css = RESOURCES.getCSS();
        //Setting the legend style
        addStyleName(css.analysisControl());
        addStyleName(css.flaggedItemsControl());

        loadingIcon = new Image(RESOURCES.loader());
        loadingIcon.setStyleName(css.flaggedItemsLoadingIcon());
        this.add(loadingIcon);

        this.msgLabel = new InlineLabel();
        this.msgLabel.setStyleName(RESOURCES.getCSS().flaggedItemsLabel());
        this.add(this.msgLabel);

        this.closeBtn = new PwpButton("Close and un-flag pathways", css.close(), this);
        this.add(this.closeBtn);

        this.initHandlers();
        this.setVisible(false);
    }

    @Override
    public void onClick(ClickEvent event) {
        if(event.getSource().equals(this.closeBtn)){
            eventBus.fireEventFromSource(new NodeFlaggedResetEvent(), this);
        }
    }

    @Override
    public void onNodeFlagged(NodeFlaggedEvent event) {
        String term =  event.getTerm();
        Collection<Node> flaggedItems =  event.getFlagged();
        String msg = " - " + flaggedItems.size() + (flaggedItems.size() == 1 ? " pathway" : " pathways") + " flagged";
        this.msgLabel.setText(term + msg);
        loadingIcon.setVisible(false);
        this.setVisible(true);
    }

    @Override
    public void onNodeFlagRequested(NodeFlagRequestedEvent event) {
        String term = event.getTerm();
        loadingIcon.setVisible(true);
        this.msgLabel.setText("Flagging entities for " + term + "...");
        this.setVisible(true);
    }

    @Override
    public void onNodeFlaggedReset() {
        this.setVisible(false);
    }

    private void initHandlers() {
        this.eventBus.addHandler(NodeFlaggedEvent.TYPE, this);
        this.eventBus.addHandler(NodeFlaggedResetEvent.TYPE, this);
        this.eventBus.addHandler(NodeFlagRequestedEvent.TYPE, this);
    }
}
