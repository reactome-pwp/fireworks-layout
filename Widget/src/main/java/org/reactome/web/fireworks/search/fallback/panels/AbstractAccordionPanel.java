package org.reactome.web.fireworks.search.fallback.panels;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.SimplePanel;
import org.reactome.web.fireworks.search.fallback.events.PanelCollapsedEvent;
import org.reactome.web.fireworks.search.fallback.events.PanelExpandedEvent;


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
