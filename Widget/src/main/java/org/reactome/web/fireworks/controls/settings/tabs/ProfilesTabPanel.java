package org.reactome.web.fireworks.controls.settings.tabs;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.*;
import org.reactome.web.fireworks.events.ProfileChangedEvent;
import org.reactome.web.fireworks.profiles.FireworksColours;
import org.reactome.web.fireworks.profiles.model.Profile;

import java.util.List;

/**
 * @author Kostas Sidiropoulos <ksidiro@ebi.ac.uk>
 */
public class ProfilesTabPanel extends Composite implements ChangeHandler {
    private EventBus eventBus;
    private ListBox colourProfiles;

    public ProfilesTabPanel(EventBus eventBus) {
        this.eventBus = eventBus;
        FlowPanel main = new FlowPanel();
        main.setStyleName(RESOURCES.getCSS().profilesPanel());

        colourProfiles = new ListBox();

        Label tabHeader = new Label("Colour Profiles");
        tabHeader.setStyleName(RESOURCES.getCSS().tabHeader());
        main.add(tabHeader);
        main.add(getProfilesWidget("Pathway Overview Colour Profile:", colourProfiles, FireworksColours.ProfileType.getProfiles()));

        setSelection(colourProfiles, FireworksColours.getSelectedProfileName());

        initHandlers();

        initWidget(main);
    }

    @Override
    public void onChange(ChangeEvent event) {
        ListBox lb = (ListBox) event.getSource();
        String aux = lb.getSelectedValue();
        if(lb.equals(colourProfiles)){
            Profile profile = FireworksColours.ProfileType.getByName(aux).getProfile();
            FireworksColours.setProfile(profile);
            eventBus.fireEventFromSource(new ProfileChangedEvent(profile), this);
        }
    }


    private Widget getProfilesWidget(String title, ListBox profileListBox, List<String> profileNames){
        profileListBox.setMultipleSelect(false);
        for(String name : profileNames){
            profileListBox.addItem(name);
        }

        Label lb = new Label(title);
        lb.setStyleName(RESOURCES.getCSS().profileLabel());

        FlowPanel fp = new FlowPanel();
        fp.add(lb);
        fp.add(profileListBox);
        return fp;
    }

    private void setSelection(ListBox profileListBox, String selection){
        if(selection==null){
            return;
        }
        for(int i=0; i<profileListBox.getItemCount(); i++){
            if(profileListBox.getValue(i).equals(selection)){
                profileListBox.setSelectedIndex(i);
            }
        }
    }

    private void initHandlers(){
        colourProfiles.addChangeHandler(this);
    }


    public static Resources RESOURCES;

    static {
        RESOURCES = GWT.create(Resources.class);
        RESOURCES.getCSS().ensureInjected();
    }

    public interface Resources extends ClientBundle {
        @Source(ResourceCSS.CSS)
        ResourceCSS getCSS();
    }

    @CssResource.ImportedWithPrefix("fireworks-ProfilesTabPanel")
    public interface ResourceCSS extends CssResource {
        String CSS = "org/reactome/web/fireworks/controls/settings/tabs/ProfilesTabPanel.css";

        String profilesPanel();

        String profileLabel();

        String tabHeader();
    }
}
