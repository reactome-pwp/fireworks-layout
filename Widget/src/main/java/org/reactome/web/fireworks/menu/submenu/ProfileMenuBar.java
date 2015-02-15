package org.reactome.web.fireworks.menu.submenu;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.MenuItem;
import org.reactome.web.fireworks.profiles.FireworksColours;
import org.reactome.web.fireworks.profiles.model.Profile;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class ProfileMenuBar extends SubMenuBar{

    public interface ProfileColourChangedHandler {
        void onProfileColourChanged(Profile profile);
    }

    public ProfileMenuBar(final ProfileColourChangedHandler handler) {
        super(true);
        setAnimationEnabled(true);

        String selected = FireworksColours.getSelectedProfileName();
        for (final String name : FireworksColours.ColourProfile.getProfiles()) {
            MenuItem item = new MenuItem(new SafeHtmlBuilder().appendEscaped(name).toSafeHtml());
            final Style itemStyle = item.getElement().getStyle();

            itemStyle.setFontWeight(name.equals(selected) ? Style.FontWeight.BOLD : Style.FontWeight.LIGHTER );

            item.setScheduledCommand(new Scheduler.ScheduledCommand() {
                @Override
                public void execute() {
                    if (handler != null) {
                        for (MenuItem menuItem : getItems()) {
                            menuItem.getElement().getStyle().setFontWeight(Style.FontWeight.LIGHTER);
                        }
                        itemStyle.setFontWeight(Style.FontWeight.BOLD);

                        Profile p = FireworksColours.ColourProfile.getColourProfile(name).getProfile();
                        handler.onProfileColourChanged(p);
                    }
                }
            });
            addItem(item);
        }
    }
}