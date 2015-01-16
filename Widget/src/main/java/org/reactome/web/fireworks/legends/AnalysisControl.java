package org.reactome.web.fireworks.legends;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusWidget;
import org.reactome.web.fireworks.events.AnalysisPerformedEvent;
import org.reactome.web.fireworks.events.AnalysisResetEvent;
import org.reactome.web.fireworks.handlers.AnalysisPerformedEventHandler;
import org.reactome.web.fireworks.handlers.AnalysisResetEventHandler;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class AnalysisControl extends LegendPanel implements AnalysisPerformedEventHandler, AnalysisResetEventHandler {

    FlowPanel container;

    public AnalysisControl(final EventBus eventBus) {
        super(eventBus);

        LegendPanelCSS css = RESOURCES.getCSS();
        //Setting the legend style
        addStyleName(css.analysisControl());

        final AnalysisControl that = this;
//        final Image close = new Image(LegendImages.INSTANCE.closeNormal());
//        close.setStyleName(getDefaultResources().getCSS().close());
//        close.addMouseOverHandler(new MouseOverHandler() {
//            @Override
//            public void onMouseOver(MouseOverEvent event) {
//                close.setResource(LegendImages.INSTANCE.closeHovered());
//            }
//        });
//        close.addMouseDownHandler(new MouseDownHandler() {
//            @Override
//            public void onMouseDown(MouseDownEvent event) {
//                close.setResource(LegendImages.INSTANCE.closeClicked());
//            }
//        });
//        close.addMouseOutHandler(new MouseOutHandler() {
//            @Override
//            public void onMouseOut(MouseOutEvent event) {
//                close.setResource(LegendImages.INSTANCE.closeNormal());
//            }
//        });
//        close.addClickHandler(new ClickHandler() {
//            @Override
//            public void onClick(ClickEvent event) {
//                eventBus.fireEventFromSource(new AnalysisResetEvent(), that);
//            }
//        });

        ControlButton close = new ControlButton(css.close());
        close.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.fireEventFromSource(new AnalysisResetEvent(), that);
            }
        });
        this.add(close);

        this.initHandlers();
        this.setVisible(false);
    }

    @Override
    public void onAnalysisPerformed(AnalysisPerformedEvent e) {
        this.setVisible(true);
    }

    @Override
    public void onAnalysisReset() {
        this.setVisible(false);
    }

    private void initHandlers() {
        this.eventBus.addHandler(AnalysisPerformedEvent.TYPE, this);
        this.eventBus.addHandler(AnalysisResetEvent.TYPE, this);
    }
}
