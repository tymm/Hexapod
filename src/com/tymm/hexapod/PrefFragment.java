package com.tymm.hexapod;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.util.Log;
import android.content.SharedPreferences;
import android.preference.Preference;

public class PrefFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}

	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		if (key.equals("pref_gait")) {
			int gait_id = Integer.parseInt(sharedPreferences.getString(key, ""));

			switch(gait_id) {
				case 1:
					Log.i("Hexapod", "Setting gait to Wave 1");
					break;
				case 2:
					Log.i("Hexapod", "Setting gait to Wave 2");
					break;
				case 3:
					Log.i("Hexapod", "Setting gait to Wave 3");
					break;
				default:
					Log.i("Hexapod", "Unable to determine gait");
				}
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
	}

}
