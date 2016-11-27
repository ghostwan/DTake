package com.ghostwan.dtake;


import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends AppCompatPreferenceActivity {

    public final static String LAST_TAKE_PREF = "com.ghostwan.dtake.LAST_TAKE_PREF";
    public final static String THING_TAKEN = "thing_taken";
    public final static String UPDATE_TIME = "update_time";
    public final static String TIME_LIMIT = "time_limit";

    public static final String DEFAULT_VALUE_THING_TAKE = "pill";
    public static final String DEFAULT_VALUE_UPDATE_TIME = "1";
    public static final String DEFAULT_VALUE_TIME_LIMIT = "60";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
    }

    public static class MyPreferenceFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
            initPreference(UPDATE_TIME, minuteCheckListener);
            initPreference(TIME_LIMIT, minuteCheckListener);
            initPreference(THING_TAKEN, normalCheckListener);
        }

        private void initPreference(String preferenceName, Preference.OnPreferenceChangeListener listener) {
            Preference preference = getPreferenceScreen().findPreference(preferenceName);
            preference.setOnPreferenceChangeListener(listener);
            String value = PreferenceManager.getDefaultSharedPreferences(preference.getContext()).getString(preferenceName, "");
            listener.onPreferenceChange(preference, value);
        }


        private Preference.OnPreferenceChangeListener minuteCheckListener = new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                //Check that the string is an integer.

                String stringValue = newValue.toString();
                boolean value = numberCheck(stringValue);
                if (value) {
                    if(stringValue.equals("1"))
                        preference.setSummary(stringValue+" minute");
                    else
                        preference.setSummary(stringValue+" minutes");
                }
                else {
                    preference.setSummary(stringValue);
                }
                return value;
            }
        };

        private Preference.OnPreferenceChangeListener normalCheckListener = new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String stringValue = newValue.toString();
                preference.setSummary(stringValue);
                return true;
            }
        };

        private boolean numberCheck(String newValue) {
            if( !newValue.equals("")  &&  newValue.matches("\\d*") ) {
                return true;
            }
            else {
                return false;
            }
        }

    }
}
