package com.tymm.hexapod;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.util.Log;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

public class PrefFragment extends PreferenceFragment implements FragmentLifecycle, OnSharedPreferenceChangeListener {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}

	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		Communication comm = (Communication)getActivity().getApplication();

		if (key.equals("pref_gait")) {
			int gait_id = Integer.parseInt(sharedPreferences.getString(key, ""));

			switch(gait_id) {
				case 1:
					Log.i("Hexapod", "Setting gait to Wave 1");
					comm.setGaitWaveOne();
					break;
				case 2:
					Log.i("Hexapod", "Setting gait to Wave 2");
					comm.setGaitWaveTwo();
					break;
				case 3:
					Log.i("Hexapod", "Setting gait to Wave 3");
					comm.setGaitWaveThree();
					break;
				case 4:
					Log.i("Hexapod", "Setting gait to Tripod");
					comm.setGaitTripod();
					break;
				default:
					Log.i("Hexapod", "Unable to determine gait");
				}
		} else if (key.equals("pref_road")) {
			int gait_id = Integer.parseInt(sharedPreferences.getString(key, ""));

			switch(gait_id) {
				case 1:
					Log.i("Hexapod", "Setting gait to Onroad");
					comm.setGaitOnRoad();
					break;
				case 2:
					Log.i("Hexapod", "Setting gait to Offroad");
					comm.setGaitOffRoad();
					break;
				default:
					Log.i("Hexapod", "Unable to determine gait");
			}
		} else if (key.equals("pref_speed")) {
			int speed = sharedPreferences.getInt(key, 0);
			Log.i("Hexapod", "Set speed to "+ speed);

			comm.sendSpeed(speed);
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

	@Override
	public void onPauseFragment() {
	}

	@Override
	public void onResumeFragment() {
	}
}
