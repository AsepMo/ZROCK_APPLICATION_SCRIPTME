package zrock.application.scriptme.create;

import zrock.application.scriptme.R;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;

import zrock.application.engine.Engine;
import zrock.application.scriptme.BootReceiver;

public class SettingsActivity extends PreferenceActivity {

	private CheckBoxPreference mStartOnBootPreference;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.pref_root_adb);

		mStartOnBootPreference = (CheckBoxPreference) getPreferenceScreen()
				.findPreference("start_onboot");

		// toggle BootReceiver on/off
		mStartOnBootPreference
				.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

					@Override
					public boolean onPreferenceChange(Preference preference,
							Object newValue) {

						if (newValue instanceof Boolean) {
							if ((Boolean) newValue) {
								Engine.enableReceiver(getApplicationContext(),
										BootReceiver.class);
							} else {
								Engine.disableReceiver(getApplicationContext(),
										BootReceiver.class);
							}
							return true;
						}
						return false;
					}
				});

	}

}
