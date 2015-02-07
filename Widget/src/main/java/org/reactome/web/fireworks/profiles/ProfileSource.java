package org.reactome.web.fireworks.profiles;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@SuppressWarnings("UnusedDeclaration")
public interface ProfileSource extends ClientBundle {

    public static final ProfileSource SOURCE = GWT.create(ProfileSource.class);

    @Source("profile_01.json")
    TextResource profile01();

    @Source("profile_02.json")
    TextResource profile02();

    @Source("profile_03.json")
    TextResource profile03();
}
