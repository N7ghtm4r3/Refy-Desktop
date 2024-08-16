package com.tecknobit.refy;

import com.tecknobit.refycore.helpers.RefyLocalUser;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class DesktopRefyLocalUser extends RefyLocalUser {

    private final Preferences preferences;

    public DesktopRefyLocalUser() {
        preferences = Preferences.userRoot().node("tecknobit/refy/desktop");
        initLocalUser();
    }

    @Override
    protected void setPreference(String key, String value) {
        if (value == null)
            preferences.remove(key);
        else
            preferences.put(key, value);
    }

    @Override
    protected String getPreference(String key) {
        return preferences.get(key, null);
    }

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
