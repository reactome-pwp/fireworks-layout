package org.reactome.web.fireworks.legends;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.InlineLabel;
import org.reactome.web.fireworks.analysis.AnalysisType;
import org.reactome.web.fireworks.events.AnalysisPerformedEvent;
import org.reactome.web.fireworks.events.AnalysisResetEvent;
import org.reactome.web.fireworks.handlers.AnalysisPerformedEventHandler;
import org.reactome.web.fireworks.handlers.AnalysisResetEventHandler;
import org.reactome.web.fireworks.legends.slider.Slider;
import org.reactome.web.fireworks.legends.slider.SliderValueChangedEvent;
import org.reactome.web.fireworks.legends.slider.SliderValueChangedHandler;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class ExpressionControl extends LegendPanel implements ClickHandler, SliderValueChangedHandler,
        AnalysisPerformedEventHandler, AnalysisResetEventHandler, ValueChangeHandler {

    private InlineLabel message;

    private ControlButton rewindBtn;
    private ControlButton playBtn;
    private ControlButton pauseBtn;
    private ControlButton forwardBtn;
    private SpeedButton speedBtn;
    private ControlButton closeBtn;
    private Slider slider;

    public ExpressionControl(EventBus eventBus) {
        super(eventBus);

        LegendPanelCSS css = RESOURCES.getCSS();
        //Setting the legend style
        addStyleName(css.analysisControl());
        addStyleName(css.expressionControl());

        this.rewindBtn = new ControlButton(css.rewind(), this);
        this.add(this.rewindBtn);

        this.playBtn = new ControlButton(css.play(), this);
        this.add(this.playBtn);

        this.pauseBtn = new ControlButton(css.pause(), this);
        this.pauseBtn.setVisible(false);
        this.add(this.pauseBtn);

        this.forwardBtn = new ControlButton(css.forward(), this);
        this.add(this.forwardBtn);

        this.speedBtn = new SpeedButton(RESOURCES);
        this.speedBtn.setStyleName(css.speed());
        //noinspection unchecked
        this.speedBtn.addValueChangeHandler(this);
        this.speedBtn.disable();
        this.add(this.speedBtn);

        this.slider = new Slider(100, 24);
        this.slider.addSliderValueChangedHandler(this);
        this.slider.setVisible(false);
        this.slider.setStyleName(css.slide());
        this.add(this.slider);

        this.message = new InlineLabel();
        this.add(this.message);

        this.closeBtn = new ControlButton(css.close(), this);
        this.add(this.closeBtn);

        this.initHandlers();
        this.setVisible(false);
    }

    @Override
    public void onAnalysisPerformed(AnalysisPerformedEvent e) {
        if(e.getAnalysisType().equals(AnalysisType.EXPRESSION)) {
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
        Object source = event.getSource();
        if(source.equals(this.closeBtn)){
            eventBus.fireEventFromSource(new AnalysisResetEvent(), this);
        }else if(source.equals(this.rewindBtn)){
            System.out.println("Rewind");
        }else if(source.equals(this.playBtn)){
            this.rewindBtn.setEnabled(false);
            this.forwardBtn.setEnabled(false);
            this.speedBtn.enable();

            this.playBtn.setVisible(false);
            this.pauseBtn.setVisible(true);
            System.out.println("Play");
        }else if(source.equals(this.pauseBtn)){
            this.rewindBtn.setEnabled(true);
            this.forwardBtn.setEnabled(true);
            this.speedBtn.setDown(false);
            this.speedBtn.disable();
            this.slider.setVisible(false);

            this.pauseBtn.setVisible(false);
            this.playBtn.setVisible(true);
            System.out.println("Pause");
        }else if(source.equals(this.forwardBtn)){
            System.out.println("Forward");
        }else if(source.equals(this.speedBtn)){
            this.slider.setVisible(true);
        }
    }

    private void initHandlers() {
        this.eventBus.addHandler(AnalysisPerformedEvent.TYPE, this);
        this.eventBus.addHandler(AnalysisResetEvent.TYPE, this);
    }

    @Override
    public void onValueChange(ValueChangeEvent event) {
        SpeedButton source = (SpeedButton) event.getSource();
        if(source.equals(this.speedBtn)){
            this.slider.setVisible(source.isDown());
        }
    }

    @Override
    public void onSliderValueChanged(SliderValueChangedEvent event) {
        System.out.println(event.getPercentage());
    }
}
