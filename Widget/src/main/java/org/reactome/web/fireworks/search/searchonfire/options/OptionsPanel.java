package org.reactome.web.fireworks.search.searchonfire.options;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import org.reactome.web.fireworks.controls.common.IconButton;
import org.reactome.web.fireworks.search.searchonfire.events.IncludeAllFormsEvent;
import org.reactome.web.fireworks.search.searchonfire.handlers.IncludeAllFormsHandler;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class OptionsPanel extends FlowPanel implements ClickHandler{

    private boolean includeAllForms;
    private IconButton showAllBtn;
    private InlineLabel showAllLabel;

    public OptionsPanel() {
        setVisible(false);
        init();
    }

    public HandlerRegistration addIncludeAllInstancesHandler(IncludeAllFormsHandler handler) {
        return addHandler(handler, IncludeAllFormsEvent.TYPE);
    }

    public void setEnable(boolean enable) {
        showAllBtn.setEnabled(enable);
    }

    @Override
    public void onClick(ClickEvent event) {
        if(showAllBtn.isEnabled()) {
            includeAllForms = !includeAllForms;
            updateView();
            fireEvent(new IncludeAllFormsEvent(includeAllForms));
        }
    }

    private void init() {
        setStyleName(RESOURCES.getCSS().mainPanel());

        showAllBtn = new IconButton("", RESOURCES.uncheckedIcon(), this);
        showAllBtn.setStyleName(RESOURCES.getCSS().showAllBtn());

        showAllLabel = new InlineLabel("Show pathways for all forms of this molecule");
        showAllLabel.setStyleName(RESOURCES.getCSS().showAllLabel());
        showAllLabel.addClickHandler(this);

        updateView();

        add(showAllBtn);
        add(showAllLabel);
    }

    private void updateView() {
        if(includeAllForms) {
            showAllBtn.setImage(RESOURCES.checkedIcon());
            showAllBtn.addStyleName(RESOURCES.getCSS().showAllEnabledBtn());
            showAllBtn.setTitle("Click to show pathways for only this instance");
            showAllLabel.setTitle("Click to show pathways for only this instance");
        } else {
            showAllBtn.setImage(RESOURCES.uncheckedIcon());
            showAllBtn.removeStyleName(RESOURCES.getCSS().showAllEnabledBtn());
            showAllBtn.setTitle("Click to show pathways for all forms of this molecule");
            showAllLabel.setTitle("Click to show pathways for all forms of this molecule");
        }
    }


    public static Resources RESOURCES;
    static {
        RESOURCES = GWT.create(Resources.class);
        RESOURCES.getCSS().ensureInjected();
    }

    /**
     * A ClientBundle of resources used by this widget.
     */
    public interface Resources extends ClientBundle {
        /**
         * The styles used in this widget.
         */
        @Source(ResourceCSS.CSS)
        ResourceCSS getCSS();

        @Source("../../images/checked.png")
        ImageResource checkedIcon();

        @Source("../../images/unchecked.png")
        ImageResource uncheckedIcon();
    }

    /**
     * Styles used by this widget.
     */
    @CssResource.ImportedWithPrefix("fireworks-OptionsPanel")
    public interface ResourceCSS extends CssResource {
        /**
         * The path to the default CSS styles used by this resource.
         */
        String CSS = "org/reactome/web/fireworks/search/searchonfire/options/OptionsPanel.css";

        String mainPanel();

        String showAllBtn();

        String showAllEnabledBtn();

        String showAllLabel();
    }
}
