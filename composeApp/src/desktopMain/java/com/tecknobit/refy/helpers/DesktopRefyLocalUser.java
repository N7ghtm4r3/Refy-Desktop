package com.tecknobit.refy.helpers;

import com.tecknobit.equinox.environment.records.EquinoxLocalUser;
import com.tecknobit.refycore.helpers.RefyLocalUser;
import com.tecknobit.refycore.records.RefyUser;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * The {@code DesktopRefyLocalUser} class is useful to represent a {@link RefyUser} in the desktop
 * applications
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see EquinoxLocalUser
 * @see RefyLocalUser
 */
public class DesktopRefyLocalUser extends RefyLocalUser {

    /**
     * {@code preferences} the manager of the local preferences
     */
    private final Preferences preferences;

    /**
     * Constructor to init {@link DesktopRefyLocalUser} class
     * <p>
     * No-any params required
     */
    public DesktopRefyLocalUser() {
        preferences = Preferences.userRoot().node("tecknobit/refy/desktop");
        initLocalUser();
    }

    /**
     * Method to store and set a preference
     *
     * @param key   :   the key of the preference
     * @param value : the value of the preference
     */
    @Override
    protected void setPreference(String key, String value) {
        if (value == null)
            preferences.remove(key);
        else
            preferences.put(key, value);
    }

    /**
     * Method to get a stored preference
     *
     * @param key : the key of the preference to get
     * @return the preference stored as {@link String}
     */
    @Override
    protected String getPreference(String key) {
        return preferences.get(key, null);
    }

    /**
     * Method to clear the current local user session <br>
     * No-any params required
     */
    @Override
    public void clear() {
        super.clear();
        try {
            preferences.clear();
            initLocalUser();
        } catch (BackingStoreException e) {
            throw new RuntimeException(e);
        }
    }

}
