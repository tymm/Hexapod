package com.tymm.hexapod;

import android.content.Context;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ControlFragment extends Fragment {
	// Bluetooth Communication
	private Communication comm;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_control, container, false);

		// Bluetooth Communication
		comm = (Communication)getActivity().getApplication();

		final Button wake_up = (Button) rootView.findViewById(R.id.button_wakeup);
		final Button sleep = (Button) rootView.findViewById(R.id.button_sleep);
		final Button shutdown = (Button) rootView.findViewById(R.id.button_shutdown);

		wake_up.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				comm.sendWakeUp();
			}
		});

		sleep.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				comm.sendSleep();
			}
		});

		shutdown.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				comm.sendShutdown();
			}
		});


		return rootView;
	}
}
