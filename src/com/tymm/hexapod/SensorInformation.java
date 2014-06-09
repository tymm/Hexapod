package com.tymm.hexapod;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.util.Log;

class SensorInformation implements GLSurfaceView.Renderer, SensorEventListener {
	private Sensor mRotationVectorSensor;
	private final float[] mRotationMatrix = new float[16];
	private SensorManager mSensorManager;
	private float[] orientation = new float[3];
	private float[] prev_orientation = new float[3];
	private final int diff_degree = 5;
	private Communication comm;

	private Cube mCube;

	public SensorInformation(SensorManager SensorManager, Context context) {
		mSensorManager = SensorManager;

		// find the rotation-vector sensor
		mRotationVectorSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

		mCube = new Cube();

		mRotationMatrix[ 0] = 1;
		mRotationMatrix[ 4] = 1;
		mRotationMatrix[ 8] = 1;
		mRotationMatrix[12] = 1;

		// Get bluetooth communication object
		comm = ((Communication)context.getApplicationContext());

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
			SensorManager.getRotationMatrixFromVector(mRotationMatrix, event.values);
			SensorManager.getOrientation(mRotationMatrix, orientation);

			if (isDiffBigEnoughChange(orientation)) {
				double Z = Math.toDegrees(orientation[0]);
				double X = Math.toDegrees(orientation[1]);
				double Y = Math.toDegrees(orientation[2]);

				Log.i("Hexapod", "Z: " + (int)(-1*Z+135) + " X: " + (int)-1*X + " Y: " + (int)Y);
				Log.i("Hexapod", "scaled Z: " + scale(-1*Z+135) + " scaled X: " + scale(-1*X) + " scaled Y: " + scale(Y));
				// Sending sensor information here
				comm.sendRotation(scale(-1*X), scale(Y), scale(-1*Z+135));
			}
		}
	}

	private int scale(double degree) {
		double factor = 2.8;
		if ((int)(degree*factor) > 127) {
			return 127;
		} else if ((int)(degree*factor) < -128) {
			return -128;
		} else {
			return (int)(degree*factor);
		}
	}

	public boolean isDiffBigEnoughChange(float[] orientation) {
		double Z_prev = Math.toDegrees(prev_orientation[0]);
		double X_prev = Math.toDegrees(prev_orientation[1]);
		double Y_prev = Math.toDegrees(prev_orientation[2]);

		double Z = Math.toDegrees(orientation[0]);
		double X = Math.toDegrees(orientation[1]);
		double Y = Math.toDegrees(orientation[2]);

		if (Math.abs(Z-Z_prev) >= diff_degree || Math.abs(X-X_prev) >= diff_degree || Math.abs(Y-Y_prev) >= diff_degree) {
			// Copy by value
			System.arraycopy(orientation, 0, this.prev_orientation, 0, orientation.length);
			return true;
		} else {
			return false;
		}
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	public void onDrawFrame(GL10 gl) {
		// clear screen
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		// set-up modelview matrix
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glTranslatef(0, 0, -3.0f);
		gl.glMultMatrixf(mRotationMatrix, 0);
		// draw our object
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		mCube.draw(gl);
	}

	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// set view-port
		gl.glViewport(0, 0, width, height);
		// set projection matrix
		float ratio = (float) width / height;
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10);
	}

	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// dither is enabled by default, we don't need it
		gl.glDisable(GL10.GL_DITHER);
		// clear screen in white
		gl.glClearColor(1,1,1,1);
	}
}
