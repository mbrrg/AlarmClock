package com.plushware.alarmclock;

import com.plushware.hardware.SensorInput;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Time;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class ClockActivity extends Activity {
	public static final String TAG = "ClockActivity";
	private static final int AUTO_HIDE_DELAY_MILLIS = 3000;		
	
	private MediaPlayer mPlayer;
	private TextView mSensorValue;
	private TimeTextView mTimeView;
	private AlarmReceiver mAlarmReceiver;
	private Boolean mReceiverIsRegistered = false;
	
	private Runnable mRefreshSensorValueRunnable = new Runnable() {
		public void run() {
			if (mSensorValue != null && SensorInput.isEnabled()) {
				mSensorValue.setText(Integer.toString(SensorInput.getRawValue()));
			}
		}
	};
	
	private RepeatingRunner mRefreshSensorValue = new RepeatingRunner(mRefreshSensorValueRunnable, 500);	
	
	private class AlarmReceiver extends BroadcastReceiver {     
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			
			Log.d(TAG, "Received " + action);
			
			if (action == AlarmClock.INTENT_ALARM_ON) {
				alarmOn();
			} else if (action == AlarmClock.INTENT_ALARM_OFF) {
				alarmOff();			 
			}
		}
	}
	
	protected void alarmOn() {
		mTimeView.setTextColor(Color.RED);

		if (mPlayer == null) {
			mPlayer = MediaPlayer.create(this, R.raw.alarm);
			mPlayer.setLooping(true);
		}
		
        mPlayer.seekTo(0);
		mPlayer.start();
	}
	
	protected void alarmOff() {
		mTimeView.setTextColor(Color.WHITE);
		
		if (mPlayer != null) {
			mPlayer.stop();
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		if (mReceiverIsRegistered) {
		    unregisterReceiver(mAlarmReceiver);
		    mReceiverIsRegistered = false;
		}		
		
		mTimeView.pause();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		if (!mReceiverIsRegistered) {
			IntentFilter filter = new IntentFilter();
			
			filter.addAction(AlarmClock.INTENT_ALARM_ON);
			filter.addAction(AlarmClock.INTENT_ALARM_OFF);
			
		    registerReceiver(mAlarmReceiver, filter);
		    mReceiverIsRegistered = true;
		}
		
		Intent i = getIntent();
		
		if (i.getBooleanExtra("ALARM_IS_ON",  false)) {	
			alarmOn();
		}
		
		mTimeView.resume();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		if (mPlayer != null && mPlayer.isPlaying()) {
			mPlayer.stop();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_clock);

		final View controlsView = findViewById(R.id.fullscreen_content_controls);
		mSensorValue = (TextView)findViewById(R.id.sensor_value);
		mTimeView = (TimeTextView) findViewById(R.id.fullscreen_content);
		
		mTimeView.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				controlsView.setVisibility(View.VISIBLE);
				
				delayedHide(3000);

				return false;
			}
		});
		
		findViewById(R.id.set_alarm).setOnTouchListener(mDelayHideTouchListener);
		
		mAlarmReceiver = new AlarmReceiver();
		
		startService(new Intent(this, SensorInputService.class));
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		//delayedHide(100);
	}
	
	public void setAlarm(View view) {		
		AlarmTrigger.enable();
	}	
	
	public void showSettings(View view) {
		Intent intent = new Intent(this, SettingsActivity.class);
		startActivityForResult(intent, 0); 		
	}	
	
	public void alarmOff(View view) {		
		Intent intent = new Intent(AlarmClock.INTENT_ALARM_OFF);
		sendBroadcast(intent);
	}

	View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			delayedHide(AUTO_HIDE_DELAY_MILLIS);

			return false;
		}
	};

	Handler mHideHandler = new Handler();
	Runnable mHideRunnable = new Runnable() {
		@Override
		public void run() {
			findViewById(R.id.fullscreen_content_controls).setVisibility(View.GONE);
		}
	};

	private void delayedHide(int delayMillis) {
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, delayMillis);
	}
}
