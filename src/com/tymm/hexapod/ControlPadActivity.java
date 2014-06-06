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

public class ControlPadActivity extends Activity {
	private TextView position_info;

	private Handler circle_show;
	private Handler circle_hide;

	private boolean pressed = false;

	private boolean circle_top_top_pressed = false;
	private boolean circle_top_right_pressed = false;
	private boolean circle_right_right_pressed = false;
	private boolean circle_bottom_right_pressed = false;
	private boolean circle_bottom_bottom_pressed = false;
	private boolean circle_bottom_left_pressed = false;
	private boolean circle_left_left_pressed = false;
	private boolean circle_top_left_pressed = false;

	private boolean circle_top_top_state = false;
	private boolean circle_top_right_state = false;
	private boolean circle_right_right_state = false;
	private boolean circle_bottom_right_state = false;
	private boolean circle_bottom_bottom_state = false;
	private boolean circle_bottom_left_state = false;
	private boolean circle_left_left_state = false;
	private boolean circle_top_left_state = false;

	private PolygonImageView circle;

	private ImageView circle_top_top;
	private ImageView circle_top_right;
	private ImageView circle_right_right;
	private ImageView circle_bottom_right;
	private ImageView circle_bottom_bottom;
	private ImageView circle_bottom_left;
	private ImageView circle_left_left;
	private ImageView circle_top_left;

	// Directions
	private final int TOP_TOP = 0; private final int TOP_RIGHT = 1; private final int RIGHT_RIGHT = 2; private final int BOTTOM_RIGHT = 3; private final int BOTTOM_BOTTOM = 4; private final int BOTTOM_LEFT = 5; private final int LEFT_LEFT = 6; private final int TOP_LEFT = 7;

	// Sensor Manager
	private SensorManager mSensorManager;
	private SensorInformation sensor;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.controlpad);

		// Sensors
		mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
		sensor = new SensorInformation(mSensorManager);

		// Will show the position of the touch event
		position_info = new TextView(this);
		position_info = (TextView) findViewById(R.id.position_info);

		// Main image which will be shown all the time
		circle = (PolygonImageView) findViewById(R.id.circle);

		// Images with highlighted sections
		circle_bottom_right = (ImageView) findViewById(R.id.circle_bottomright);
		circle_right_right = (ImageView) findViewById(R.id.circle_rightright);
		circle_top_right = (ImageView) findViewById(R.id.circle_topright);
		circle_top_top = (ImageView) findViewById(R.id.circle_toptop);
		circle_top_left = (ImageView) findViewById(R.id.circle_topleft);
		circle_left_left = (ImageView) findViewById(R.id.circle_leftleft);
		circle_bottom_left = (ImageView) findViewById(R.id.circle_bottomleft);
		circle_bottom_bottom = (ImageView) findViewById(R.id.circle_bottombottom);
		// Make it invisible
		circle_bottom_right.setAlpha(0);
		circle_right_right.setAlpha(0);
		circle_top_right.setAlpha(0);
		circle_top_top.setAlpha(0);
		circle_top_left.setAlpha(0);
		circle_left_left.setAlpha(0);
		circle_bottom_left.setAlpha(0);
		circle_bottom_bottom.setAlpha(0);

		// Handler to make the bottom right section visible
		circle_show = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				Bundle bundle = msg.getData();
				int direction = bundle.getInt("direction");

				// Show by setting alpha value to 100
				switch(direction) {
					case TOP_TOP:
						if (!circle_top_top_state) {
							circle_top_top.setAlpha(100);
							circle_top_top_state = true;
						}
						break;
					case TOP_RIGHT:
						if (!circle_top_right_state) {
							circle_top_right.setAlpha(100);
							circle_top_right_state = true;
						}
						break;
					case RIGHT_RIGHT:
						if (!circle_right_right_state) {
							circle_right_right.setAlpha(100);
							circle_right_right_state = true;
						}
						break;
					case BOTTOM_RIGHT:
						if (!circle_bottom_right_state) {
							circle_bottom_right.setAlpha(100);
							circle_bottom_right_state = true;
						}
						break;
					case BOTTOM_BOTTOM:
						if (!circle_bottom_bottom_state) {
							circle_bottom_bottom.setAlpha(100);
							circle_bottom_bottom_state = true;
						}
						break;
					case BOTTOM_LEFT:
						if (!circle_bottom_left_state) {
							circle_bottom_left.setAlpha(100);
							circle_bottom_left_state = true;
						}
						break;
					case LEFT_LEFT:
						if (!circle_left_left_state) {
							circle_left_left.setAlpha(100);
							circle_left_left_state = true;
						}
						break;
					case TOP_LEFT:
						if (!circle_top_left_state) {
							circle_top_left.setAlpha(100);
							circle_top_left_state = true;
						}
						break;
				}
			}
		};

		// Handler to make the bottom right section invisible
		circle_hide = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				Bundle bundle = msg.getData();
				int direction = bundle.getInt("direction");

				// Show by setting alpha value to 100
				switch(direction) {
					case TOP_TOP:
						if (circle_top_top_state) {
							circle_top_top.setAlpha(0);
							circle_top_top_state = false;
						}
						break;
					case TOP_RIGHT:
						if (circle_top_right_state) {
							circle_top_right.setAlpha(0);
							circle_top_right_state = false;
						}
						break;
					case RIGHT_RIGHT:
						if (circle_right_right_state) {
							circle_right_right.setAlpha(0);
							circle_right_right_state = false;
						}
						break;
					case BOTTOM_RIGHT:
						if (circle_bottom_right_state) {
							circle_bottom_right.setAlpha(0);
							circle_bottom_right_state = false;
						}
						break;
					case BOTTOM_BOTTOM:
						if (circle_bottom_bottom_state) {
							circle_bottom_bottom.setAlpha(0);
							circle_bottom_bottom_state = false;
						}
						break;
					case BOTTOM_LEFT:
						if (circle_bottom_left_state) {
							circle_bottom_left.setAlpha(0);
							circle_bottom_left_state = false;
						}
						break;
					case LEFT_LEFT:
						if (circle_left_left_state) {
							circle_left_left.setAlpha(0);
							circle_left_left_state = false;
						}
						break;
					case TOP_LEFT:
						if (circle_top_left_state) {
							circle_top_left.setAlpha(0);
							circle_top_left_state = false;
						}
						break;
					default:
						circle_top_top.setAlpha(0);
						circle_top_right.setAlpha(0);
						circle_right_right.setAlpha(0);
						circle_bottom_right.setAlpha(0);
						circle_bottom_bottom.setAlpha(0);
						circle_bottom_left.setAlpha(0);
						circle_left_left.setAlpha(0);
						circle_top_left.setAlpha(0);
				}
			}
		};

		// What happens when the circle image is being touched
		circle.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch(event.getAction()) {
					// User touches the image
					case MotionEvent.ACTION_DOWN:
						// User touches the screen
						pressed = true;

						if(circle.getAreaTopTop().contains((int)event.getX(), (int)event.getY())) {
							circle_top_top_pressed = true;
						}

						if(circle.getAreaTopRight().contains((int)event.getX(), (int)event.getY())) {
							circle_top_right_pressed = true;
						}

						if(circle.getAreaRightRight().contains((int)event.getX(), (int)event.getY())) {
							circle_right_right_pressed = true;
						}

						if(circle.getAreaBottomRight().contains((int)event.getX(), (int)event.getY())) {
							circle_bottom_right_pressed = true;
						}

						if(circle.getAreaBottomBottom().contains((int)event.getX(), (int)event.getY())) {
							circle_bottom_bottom_pressed = true;
						}

						if(circle.getAreaBottomLeft().contains((int)event.getX(), (int)event.getY())) {
							circle_bottom_left_pressed = true;
						}

						if(circle.getAreaLeftLeft().contains((int)event.getX(), (int)event.getY())) {
							circle_left_left_pressed = true;
						}

						// Start new thread which runs until ACTION_UP is performed
						new Thread(new Runnable() {
							public void run() {
								Message msg;
								// Runs until ACTION UP is performed
								while(pressed) {
									// Right right
									while(!circle_bottom_right_pressed && circle_right_right_pressed && !circle_top_right_pressed && !circle_top_top_pressed && !circle_top_left_pressed && !circle_left_left_pressed && !circle_bottom_left_pressed && !circle_bottom_bottom_pressed) {
										Log.i("ControlApp", "RIGHT RIGHT PRESSED");
										// Send message to UI
										msg = getMessage(RIGHT_RIGHT, 0);
										circle_show.sendMessage(msg);
									}
									// Done with right_right, so lets hide it
									msg = getMessage(RIGHT_RIGHT, 1);
									circle_hide.sendMessage(msg);

									// Bottom right
									while(circle_bottom_right_pressed && !circle_right_right_pressed && !circle_top_right_pressed && !circle_top_top_pressed && !circle_top_left_pressed && !circle_left_left_pressed && !circle_bottom_left_pressed && !circle_bottom_bottom_pressed) {
										// Image being touched in the bottom right section
										Log.i("ControlApp", "BOTTOM RIGHT PRESSED");

										// Send message to UI
										msg = getMessage(BOTTOM_RIGHT, 0);
										circle_show.sendMessage(msg);
									}
									// Done with bottom right, so lets hide it
									msg = getMessage(BOTTOM_RIGHT, 1);
									circle_hide.sendMessage(msg);

									// Bottom bottom
									while(!circle_bottom_right_pressed && !circle_right_right_pressed && !circle_top_right_pressed && !circle_top_top_pressed && !circle_top_left_pressed && !circle_left_left_pressed && !circle_bottom_left_pressed && circle_bottom_bottom_pressed) {
										// Image being touched in the bottom right section
										Log.i("ControlApp", "BOTTOM BOTTOM PRESSED");

										// Send message to UI
										msg = getMessage(BOTTOM_BOTTOM, 0);
										circle_show.sendMessage(msg);
									}
									// Done with bottom right, so lets hide it
									msg = getMessage(BOTTOM_BOTTOM, 1);
									circle_hide.sendMessage(msg);

									// Bottom left
									while(!circle_bottom_right_pressed && !circle_right_right_pressed && !circle_top_right_pressed && !circle_top_top_pressed && !circle_top_left_pressed && !circle_left_left_pressed && circle_bottom_left_pressed && !circle_bottom_bottom_pressed) {
										// Image being touched in the bottom right section
										Log.i("ControlApp", "BOTTOM LEFT PRESSED");

										// Send message to UI
										msg = getMessage(BOTTOM_LEFT, 0);
										circle_show.sendMessage(msg);
									}
									// Done with bottom right, so lets hide it
									msg = getMessage(BOTTOM_LEFT, 1);
									circle_hide.sendMessage(msg);

									// Left left
									while(!circle_bottom_right_pressed && !circle_right_right_pressed && !circle_top_right_pressed && !circle_top_top_pressed && !circle_top_left_pressed && circle_left_left_pressed && !circle_bottom_left_pressed && !circle_bottom_bottom_pressed) {
										// Image being touched in the bottom right section
										Log.i("ControlApp", "LEFT LEFT PRESSED");

										// Send message to UI
										msg = getMessage(LEFT_LEFT, 0);
										circle_show.sendMessage(msg);
									}
									// Done with bottom right, so lets hide it
									msg = getMessage(LEFT_LEFT, 1);
									circle_hide.sendMessage(msg);

									// Top left
									while(!circle_bottom_right_pressed && !circle_right_right_pressed && !circle_top_right_pressed && !circle_top_top_pressed && circle_top_left_pressed && !circle_left_left_pressed && !circle_bottom_left_pressed && !circle_bottom_bottom_pressed) {
										// Image being touched in the bottom right section
										Log.i("ControlApp", "TOP LEFT PRESSED");

										// Send message to UI
										msg = getMessage(TOP_LEFT, 0);
										circle_show.sendMessage(msg);
									}
									// Done with bottom right, so lets hide it
									msg = getMessage(TOP_LEFT, 1);
									circle_hide.sendMessage(msg);

									// Top top
									while(!circle_bottom_right_pressed && !circle_right_right_pressed && !circle_top_right_pressed && circle_top_top_pressed && !circle_top_left_pressed && !circle_left_left_pressed && !circle_bottom_left_pressed && !circle_bottom_bottom_pressed) {
										// Image being touched in the bottom right section
										Log.i("ControlApp", "TOP TOP PRESSED");

										// Send message to UI
										msg = getMessage(TOP_TOP, 0);
										circle_show.sendMessage(msg);
									}
									// Done with bottom right, so lets hide it
									msg = getMessage(TOP_TOP, 1);
									circle_hide.sendMessage(msg);

									// Top right
									while(!circle_bottom_right_pressed && !circle_right_right_pressed && circle_top_right_pressed && !circle_top_top_pressed && !circle_top_left_pressed && !circle_left_left_pressed && !circle_bottom_left_pressed && !circle_bottom_bottom_pressed) {
										// Image being touched in the bottom right section
										Log.i("ControlApp", "TOP RIGHT PRESSED");

										// Send message to UI
										msg = getMessage(TOP_RIGHT, 0);
										circle_show.sendMessage(msg);
									}
									// Done with bottom right, so lets hide it
									msg = getMessage(TOP_RIGHT, 1);
									circle_hide.sendMessage(msg);
								}
							}
						}).start();
						break;

					// User stops touching the image
					case MotionEvent.ACTION_UP:
						// Stop the thread which manages the ImageViews
						pressed = false;

						Log.i("ControlApp", "UP");
						// Hide all
						circle_top_top_pressed = false;
						circle_top_right_pressed = false;
						circle_right_right_pressed = false;
						circle_bottom_right_pressed = false;
						circle_bottom_bottom_pressed = false;
						circle_bottom_left_pressed = false;
						circle_left_left_pressed = false;
						circle_top_left_pressed = false;
						break;

					// User moves around on the image
					case MotionEvent.ACTION_MOVE:
						check_Area(TOP_TOP, event);
						check_Area(TOP_RIGHT, event);
						check_Area(RIGHT_RIGHT, event);
						check_Area(BOTTOM_RIGHT, event);
						check_Area(BOTTOM_BOTTOM, event);
						check_Area(BOTTOM_LEFT, event);
						check_Area(LEFT_LEFT, event);
						check_Area(TOP_LEFT, event);
						break;
				}

				// Update position information
				position_info.setText(event.getX() + " " + event.getY());
				return true;
			}
		});
	}

	private void check_Area(int area, MotionEvent event) {
		switch(area) {
			case RIGHT_RIGHT:
				if(!circle.getAreaRightRight().contains((int)event.getX(), (int)event.getY())) {
					circle_right_right_pressed = false;
				}
				if(circle.getAreaRightRight().contains((int)event.getX(), (int)event.getY())) {
					circle_right_right_pressed = true;
				}
				break;
			case BOTTOM_RIGHT:
				if(!circle.getAreaBottomRight().contains((int)event.getX(), (int)event.getY())) {
					circle_bottom_right_pressed = false;
				}
				if(circle.getAreaBottomRight().contains((int)event.getX(), (int)event.getY())) {
					circle_bottom_right_pressed = true;
				}
				break;
			case BOTTOM_BOTTOM:
				if(!circle.getAreaBottomBottom().contains((int)event.getX(), (int)event.getY())) {
					circle_bottom_bottom_pressed = false;
				}
				if(circle.getAreaBottomBottom().contains((int)event.getX(), (int)event.getY())) {
					circle_bottom_bottom_pressed = true;
				}
				break;
			case BOTTOM_LEFT:
				if(!circle.getAreaBottomLeft().contains((int)event.getX(), (int)event.getY())) {
					circle_bottom_left_pressed = false;
				}
				if(circle.getAreaBottomLeft().contains((int)event.getX(), (int)event.getY())) {
					circle_bottom_left_pressed = true;
				}
				break;
			case LEFT_LEFT:
				if(!circle.getAreaLeftLeft().contains((int)event.getX(), (int)event.getY())) {
					circle_left_left_pressed = false;
				}
				if(circle.getAreaLeftLeft().contains((int)event.getX(), (int)event.getY())) {
					circle_left_left_pressed = true;
				}
				break;
			case TOP_LEFT:
				if(!circle.getAreaTopLeft().contains((int)event.getX(), (int)event.getY())) {
					circle_top_left_pressed = false;
				}
				if(circle.getAreaTopLeft().contains((int)event.getX(), (int)event.getY())) {
					circle_top_left_pressed = true;
				}
				break;
			case TOP_TOP:
				if(!circle.getAreaTopTop().contains((int)event.getX(), (int)event.getY())) {
					circle_top_top_pressed = false;
				}
				if(circle.getAreaTopTop().contains((int)event.getX(), (int)event.getY())) {
					circle_top_top_pressed = true;
				}
				break;
			case TOP_RIGHT:
				if(!circle.getAreaTopRight().contains((int)event.getX(), (int)event.getY())) {
					circle_top_right_pressed = false;
				}
				if(circle.getAreaTopRight().contains((int)event.getX(), (int)event.getY())) {
					circle_top_right_pressed = true;
				}
				break;
		}
	}

	private Message getMessage(int direction, int type) {
		Message msg;
		if (type == 0) {
			msg = circle_show.obtainMessage();
		} else {
			msg = circle_hide.obtainMessage();
		}
		msg.what = 1;

		Bundle b = new Bundle(1);
		b.putInt("direction", direction);
		msg.setData(b);

		return msg;
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
