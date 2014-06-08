package com.tymm.hexapod;

import android.content.Context;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.opengl.GLSurfaceView;

public class RotationFragment extends Fragment {
	// Sensor Manager
	private SensorManager mSensorManager;
	private SensorInformation sensor;
	private GLSurfaceView mGLSurfaceView;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Context context = getActivity().getApplicationContext();
		// Sensors
		mSensorManager = (SensorManager)context.getSystemService(context.SENSOR_SERVICE);
		sensor = new SensorInformation(mSensorManager, getActivity().getApplicationContext());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mGLSurfaceView = new GLSurfaceView(this.getActivity());
		mGLSurfaceView.setRenderer(sensor);

		return mGLSurfaceView;
	}

	@Override
	public void onResume() {
		super.onResume();

		if (isVisible()) {
			// start sensor
			sensor.start();
			mGLSurfaceView.onResume();
		} else {
			// stop sensor
			try {
				sensor.stop();
				mGLSurfaceView.onPause();
			} catch (NullPointerException e) {}
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		sensor.stop();
		mGLSurfaceView.onPause();
	}
}
