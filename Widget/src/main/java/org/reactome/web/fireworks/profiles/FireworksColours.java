package org.reactome.web.fireworks.profiles;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;
import org.reactome.web.fireworks.profiles.factory.ProfileFactory;
import org.reactome.web.fireworks.profiles.factory.ProfileModelException;
import org.reactome.web.fireworks.profiles.model.Profile;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public abstract class FireworksColours {
    @SuppressWarnings("SpellCheckingInspection")
    public static FireworksProfile PROFILE;

    static {
        setProfile(ProfileSource.SOURCE.profile02());
    }

    public static void setProfile(TextResource resource){
        try {
            Profile profile = ProfileFactory.getModelObject(Profile.class, resource.getText());
            PROFILE = new FireworksProfile(profile);
        } catch (ProfileModelException e) {
            GWT.log(e.getMessage());
            PROFILE = null;
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    interface ProfileSource extends ClientBundle {

        static final ProfileSource SOURCE = GWT.create(ProfileSource.class);

        @Source("profile_01.json")
        TextResource profile01();

        @Source("profile_02.json")
        TextResource profile02();

        @Source("profile_03.json")
        TextResource profile03();
    }
}
