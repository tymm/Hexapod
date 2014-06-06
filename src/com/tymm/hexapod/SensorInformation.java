package com.tymm.hexapod;

import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.hardware.Sensor;

class SensorInformation implements SensorEventListener {
	private Sensor mRotationVectorSensor;
	private final float[] mRotationMatrix = new float[16];
	private SensorManager mSensorManager;

	public SensorInformation(SensorManager SensorManager) {
		mSensorManager = SensorManager;

		mRotationVectorSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
	}

	public void start() {
		// enable our sensor when the activity is resumed, ask for
		// 10 ms updates.
		mSensorManager.registerListener(this, mRotationVectorSensor, 10000);
	}

	public void stop() {
		// make sure to turn our sensor off when the activity is paused
		mSensorManager.unregisterListener(this);
	}

	public void onSensorChanged(SensorEvent event) {
		// we received a sensor event. it is a good practice to check
		// that we received the proper event
		if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
			Log.i("Hexapod", "Rotation changed:");

			Float x = event.values[0];
			Float y = event.values[1];
			Float z = event.values[2];

			Log.i("Hexapod", x.toString());
			Log.i("Hexapod", y.toString());
			Log.i("Hexapod", z.toString());
		}
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}
}
