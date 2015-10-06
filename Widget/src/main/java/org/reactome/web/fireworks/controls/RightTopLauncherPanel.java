package org.reactome.web.fireworks.controls;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.FlowPanel;
import org.reactome.web.fireworks.controls.top.illustrations.Illustrations;
import org.reactome.web.fireworks.controls.top.menu.SettingsMenuPanel;
import org.reactome.web.fireworks.events.CanvasExportRequestedEvent;
import org.reactome.web.fireworks.legends.ControlButton;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class RightTopLauncherPanel extends FlowPanel implements ClickHandler {

    private EventBus eventBus;

    private Illustrations diagramIllustrations;
//    private DiagramKey diagramKey;
    private SettingsMenuPanel settings;

    private ControlButton illustrationsBtn;
    private ControlButton captureBtn;
    private ControlButton diagramKeyBtn;
    private ControlButton settingBtn;

    public RightTopLauncherPanel(EventBus eventBus) {
        this.setStyleName(RESOURCES.getCSS().launcherPanel());

        this.eventBus = eventBus;
        this.diagramIllustrations = new Illustrations(eventBus);
//        this.diagramKey = new DiagramKey(eventBus);
        this.settings = new SettingsMenuPanel(eventBus);

//        this.illustrationsBtn = new ControlButton("Show illustrations", RESOURCES.getCSS().illustrations(), this);
//        this.add(illustrationsBtn);

        this.captureBtn = new ControlButton("Diagram export", RESOURCES.getCSS().camera(), this);
        this.add(this.captureBtn);

//        this.diagramKeyBtn = new ControlButton("Diagram key", RESOURCES.getCSS().key(), this);
//        this.add(this.diagramKeyBtn);

        this.settingBtn = new ControlButton("Settings", RESOURCES.getCSS().settings(), this);
        this.add(this.settingBtn);

        this.setVisible(true);
    }

    @Override
    public void onClick(ClickEvent event) {
        ControlButton btn = (ControlButton) event.getSource();
        if (btn.equals(this.captureBtn)) {
            this.eventBus.fireEventFromSource(new CanvasExportRequestedEvent(), this);
//        } else if (btn.equals(this.diagramKeyBtn)) {
//            if (this.diagramKey.isShowing()) {
//                this.diagramKey.hide();
//            } else {
//                this.diagramKey.showRelativeTo(this.diagramKeyBtn);
//            }
        } else if (btn.equals(this.settingBtn)) {
            this.settings.showRelativeTo(btn);
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


        @Source("top/images/camera_clicked.png")
        ImageResource cameraClicked();

        @Source("top/images/camera_disabled.png")
        ImageResource cameraDisabled();

        @Source("top/images/camera_hovered.png")
        ImageResource cameraHovered();

        @Source("top/images/camera_normal.png")
        ImageResource cameraNormal();

        @Source("top/images/illustrations_clicked.png")
        ImageResource illustrationsClicked();

        @Source("top/images/illustrations_disabled.png")
        ImageResource illustrationsDisabled();

        @Source("top/images/illustrations_hovered.png")
        ImageResource illustrationsHovered();

        @Source("top/images/illustrations_normal.png")
        ImageResource illustrationsNormal();

        @Source("top/images/key_clicked.png")
        ImageResource keyClicked();

        @Source("top/images/key_disabled.png")
        ImageResource keyDisabled();

        @Source("top/images/key_hovered.png")
        ImageResource keyHovered();

        @Source("top/images/key_normal.png")
        ImageResource keyNormal();

        @Source("top/images/settings_clicked.png")
        ImageResource settingsClicked();

        @Source("top/images/settings_disabled.png")
        ImageResource settingsDisabled();

        @Source("top/images/settings_hovered.png")
        ImageResource settingsHovered();

        @Source("top/images/settings_normal.png")
        ImageResource settingsNormal();
    }

    @CssResource.ImportedWithPrefix("fireworks-LeftTopLauncher")
    public interface ResourceCSS extends CssResource {
        String CSS = "org/reactome/web/fireworks/controls/top/RightTopLauncherPanel.css";

        String launcherPanel();

        String camera();

        String illustrations();

        String key();

        String settings();
    }
}
