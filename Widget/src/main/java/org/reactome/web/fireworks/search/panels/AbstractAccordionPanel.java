package org.reactome.web.fireworks.search.panels;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.SimplePanel;
import org.reactome.web.fireworks.search.events.PanelCollapsedEvent;
import org.reactome.web.fireworks.search.events.PanelExpandedEvent;


/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class AbstractAccordionPanel extends SimplePanel implements SearchAccordionPanel {

    @Override
    public void onPanelCollapsed(PanelCollapsedEvent event) {
        getElement().getStyle().setDisplay(Style.Display.NONE);
    }

    @Override
    public void onPanelExpanded(PanelExpandedEvent event) {
        getElement().getStyle().setDisplay(Style.Display.INLINE);
    }
}
