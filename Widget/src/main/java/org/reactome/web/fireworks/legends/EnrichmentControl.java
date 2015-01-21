package org.reactome.web.fireworks.legends;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.InlineLabel;
import org.reactome.web.fireworks.analysis.AnalysisType;
import org.reactome.web.fireworks.events.AnalysisPerformedEvent;
import org.reactome.web.fireworks.events.AnalysisResetEvent;
import org.reactome.web.fireworks.handlers.AnalysisPerformedEventHandler;
import org.reactome.web.fireworks.handlers.AnalysisResetEventHandler;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class EnrichmentControl extends LegendPanel implements ClickHandler,
        AnalysisPerformedEventHandler, AnalysisResetEventHandler {

    private InlineLabel message;
    private ControlButton closeBtn;

    public EnrichmentControl(final EventBus eventBus) {
        super(eventBus);

        LegendPanelCSS css = RESOURCES.getCSS();
        //Setting the legend style
        addStyleName(css.analysisControl());
        addStyleName(css.enrichmentControl());

        this.message = new InlineLabel();
        this.add(this.message);

        this.closeBtn = new ControlButton(css.close(), this);
        this.add(this.closeBtn );

        this.initHandlers();
        this.setVisible(false);
    }

    @Override
    public void onAnalysisPerformed(AnalysisPerformedEvent e){
        if(e.getAnalysisType().equals(AnalysisType.OVERREPRESENTATION)) {
            this.message.setText(e.getAnalysisType().name().toUpperCase());
            this.setVisible(true);
        }else{
            this.setVisible(false);
        }
    }

    @Override
    public void onAnalysisReset() {
        if(this.isVisible()) {
            this.message.setText("");
            this.setVisible(false);
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        if(event.getSource().equals(this.closeBtn)){
            eventBus.fireEventFromSource(new AnalysisResetEvent(), this);
        }
    }

    private void initHandlers() {
        this.eventBus.addHandler(AnalysisPerformedEvent.TYPE, this);
        this.eventBus.addHandler(AnalysisResetEvent.TYPE, this);
    }

}
