package com.ghostwan.dtake;


import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

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

    public final static String COUNT_PREF = "com.ghostwan.dtake.COUNT_PREF";
    public final static String LAST_TAKE_PREF = "com.ghostwan.dtake.LAST_TAKE_PREF";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
    }

    public static class MyPreferenceFragment extends PreferenceFragment
    {
        private Preference updateTimePreference;
        private Preference timeLimitPreference;

        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
            updateTimePreference = getPreferenceScreen().findPreference("update_time");
            updateTimePreference.setOnPreferenceChangeListener(numberCheckListener);

            timeLimitPreference = getPreferenceScreen().findPreference("time_limit");
            timeLimitPreference.setOnPreferenceChangeListener(numberCheckListener);
        }

        private Preference.OnPreferenceChangeListener numberCheckListener = new Preference.OnPreferenceChangeListener() {

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
                return value;
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
