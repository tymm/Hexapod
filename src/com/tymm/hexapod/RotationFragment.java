package com.tymm.hexapod;

import android.content.Context;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.opengl.GLSurfaceView;
import android.util.Log;

public class RotationFragment extends Fragment implements FragmentLifecycle {
	// Sensor Manager
	private SensorManager mSensorManager;
	private SensorInformation sensor;
	private GLSurfaceView mGLSurfaceView;

	// Bluetooth Communication
	private Communication comm;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Context context = getActivity().getApplicationContext();
		// Sensors
		mSensorManager = (SensorManager)context.getSystemService(context.SENSOR_SERVICE);
		sensor = new SensorInformation(mSensorManager, getActivity().getApplicationContext());

		// Bluetooth Communication
		comm = (Communication)getActivity().getApplication();
		comm.start();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mGLSurfaceView = new GLSurfaceView(this.getActivity());
		mGLSurfaceView.setRenderer(sensor);

		return mGLSurfaceView;
	}

	@Override
	public void onPauseFragment() {
		// stop sensor
		sensor.stop();
		mGLSurfaceView.onPause();
		comm.sendRotationReset();
	}

	@Override
	public void onResumeFragment() {
		// start sensor
		sensor.start();
		mGLSurfaceView.onResume();
	}
}
