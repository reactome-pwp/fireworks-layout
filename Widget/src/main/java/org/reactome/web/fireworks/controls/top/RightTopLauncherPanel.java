package org.reactome.web.fireworks.controls.top;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.FlowPanel;
import org.reactome.web.fireworks.controls.common.ExpandibleContainer;
import org.reactome.web.fireworks.controls.top.illustrations.Illustrations;
import org.reactome.web.fireworks.controls.top.key.PathwayOverviewKey;
import org.reactome.web.fireworks.events.CanvasExportRequestedEvent;
import org.reactome.web.fireworks.legends.ControlButton;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class RightTopLauncherPanel extends FlowPanel implements ClickHandler {

    private EventBus eventBus;

    private Illustrations diagramIllustrations;
    private PathwayOverviewKey pathwayOverviewKey;

    private ControlButton illustrationsBtn;
    private ControlButton captureBtn;
    private ControlButton pathwayOverviewKeyBtn;

    private ExpandibleContainer expContainer;

    public RightTopLauncherPanel(EventBus eventBus) {
        this.setStyleName(RESOURCES.getCSS().launcherPanel());

        this.eventBus = eventBus;
        this.diagramIllustrations = new Illustrations(eventBus);
        this.pathwayOverviewKey = new PathwayOverviewKey(eventBus);

        this.illustrationsBtn = new ControlButton("Show illustrations", RESOURCES.getCSS().illustrations(), this);
        this.add(illustrationsBtn);

        this.captureBtn = new ControlButton("Export", RESOURCES.getCSS().camera(), this);

        expContainer = new ExpandibleContainer("Select one export option", RESOURCES.getCSS().export());
        expContainer.addButton(captureBtn);
        add(expContainer);

        this.pathwayOverviewKeyBtn = new ControlButton("Pathway overview key", RESOURCES.getCSS().key(), this);
        this.add(this.pathwayOverviewKeyBtn);

        this.setVisible(true);
    }

    @Override
    public void onClick(ClickEvent event) {
        ControlButton btn = (ControlButton) event.getSource();
        if (btn.equals(this.captureBtn)) {
            this.eventBus.fireEventFromSource(new CanvasExportRequestedEvent(), this);
        } else if (btn.equals(this.pathwayOverviewKeyBtn)) {
            if (this.pathwayOverviewKey.isShowing()) {
                this.pathwayOverviewKey.hide();
            } else {
                this.pathwayOverviewKey.showRelativeTo(this.pathwayOverviewKeyBtn);
            }
        } else if (btn.equals(this.illustrationsBtn)) {
            if (this.diagramIllustrations.isShowing()) {
                this.diagramIllustrations.hide();
            } else {
                this.diagramIllustrations.showRelativeTo(btn);
            }
        }
    }


    public static Resources RESOURCES;
    static {
        RESOURCES = GWT.create(Resources.class);
        RESOURCES.getCSS().ensureInjected();
    }

    public interface Resources extends ClientBundle {
        @Source(ResourceCSS.CSS)
        ResourceCSS getCSS();


        @Source("images/camera_clicked.png")
        ImageResource cameraClicked();

        @Source("images/camera_disabled.png")
        ImageResource cameraDisabled();

        @Source("images/camera_hovered.png")
        ImageResource cameraHovered();

        @Source("images/camera_normal.png")
        ImageResource cameraNormal();

        @Source("images/export_hovered.png")
        ImageResource exportHovered();

        @Source("images/export_normal.png")
        ImageResource exportNormal();

        @Source("images/illustrations_clicked.png")
        ImageResource illustrationsClicked();

        @Source("images/illustrations_disabled.png")
        ImageResource illustrationsDisabled();

        @Source("images/illustrations_hovered.png")
        ImageResource illustrationsHovered();

        @Source("images/illustrations_normal.png")
        ImageResource illustrationsNormal();

        @Source("images/key_clicked.png")
        ImageResource keyClicked();

        @Source("images/key_disabled.png")
        ImageResource keyDisabled();

        @Source("images/key_hovered.png")
        ImageResource keyHovered();

        @Source("images/key_normal.png")
        ImageResource keyNormal();
    }

    @CssResource.ImportedWithPrefix("fireworks-LeftTopLauncher")
    public interface ResourceCSS extends CssResource {
        String CSS = "org/reactome/web/fireworks/controls/top/RightTopLauncherPanel.css";

        String launcherPanel();

        String camera();

        String export();

        String illustrations();

        String key();
    }
}
