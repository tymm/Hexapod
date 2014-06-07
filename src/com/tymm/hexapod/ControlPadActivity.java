package com.tymm.hexapod;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.preference.PreferenceManager;
import android.content.SharedPreferences;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.widget.Button;
import android.view.MenuItem;
import android.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem.OnMenuItemClickListener;
import android.hardware.SensorManager;
import com.zerokol.views.JoystickView;
import com.zerokol.views.JoystickView.OnJoystickMoveListener;

public class ControlPadActivity extends Activity {
	private TextView position_info;
	// Sensor Manager
	private SensorManager mSensorManager;
	private SensorInformation sensor;

	// Bluetooth Communication
	private Communication comm;

	// Joystick
	private JoystickView joystick;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.controlpad);

		// Bluetooth Communication
		comm = (Communication)getApplication();
		comm.start();

		// Sensors
		mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
		sensor = new SensorInformation(mSensorManager);

		// Joystick
		joystick = (JoystickView) findViewById(R.id.joystickView);

		joystick.setOnJoystickMoveListener(new OnJoystickMoveListener() {

			@Override
			public void onValueChanged(int angle, int power, int direction) {
				// TODO Auto-generated method stub
				Log.i("Hexapod", " " + String.valueOf(angle) + "°");
				Log.i("Hexapod", " " + String.valueOf(power) + "%");
				switch (direction) {
					case JoystickView.FRONT:
						Log.i("Hexapod", "Front");
						break;
					case JoystickView.FRONT_RIGHT:
						Log.i("Hexapod", "FRONT RIGHT");
						break;
					case JoystickView.RIGHT:
						Log.i("Hexapod", "RIGHT");
						break;
					case JoystickView.RIGHT_BOTTOM:
						Log.i("Hexapod", "RIGHT BOTTOM");
						break;
					case JoystickView.BOTTOM:
						Log.i("Hexapod", "BOTTOM");
						break;
					case JoystickView.BOTTOM_LEFT:
						Log.i("Hexapod", "BOTTOM LEFT");
						break;
					case JoystickView.LEFT:
						Log.i("Hexapod", "LEFT");
						break;
					case JoystickView.LEFT_FRONT:
						Log.i("Hexapod", "LEFT FRONT");
						break;
					default:
						Log.i("Hexapod", "Center");
				}
			}
		}, JoystickView.DEFAULT_LOOP_INTERVAL);
	}

	@Override
	protected void onResume() {
		super.onResume();
		sensor.start();
	}

	@Override
	protected void onPause() {
		super.onPause();
		sensor.stop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.controlpad_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.settings:
				// Goto preference activity
				Intent intent = new Intent();
				intent.setClass(ControlPadActivity.this, SettingsActivity.class);
				startActivityForResult(intent, 0);

				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
