package com.plushware.alarmclock;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class ClockActivity extends Activity {
	public static final String TAG = "ClockActivity";
	private static final int AUTO_HIDE_DELAY_MILLIS = 3000;		
	
	private MediaPlayer mPlayer;
	private TimeTextView mTimeView;
	private AlarmReceiver mAlarmReceiver;
	private Boolean mReceiverIsRegistered = false;
	
	private class AlarmReceiver extends BroadcastReceiver {     
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d(TAG, "Received alarm on broadcast.");
			
			mTimeView.setTextColor(Color.RED);

			if (mPlayer == null) {
				mPlayer = MediaPlayer.create(context, R.raw.alarm);
				mPlayer.setLooping(true);
			}
			
	        mPlayer.seekTo(0);
			mPlayer.start();				
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
		    registerReceiver(mAlarmReceiver, new IntentFilter(AlarmClock.INTENT_ALARM_ON));
		    mReceiverIsRegistered = true;
		}
		
		Intent i = getIntent();
		
		if (i.getBooleanExtra("ALARM_IS_ON",  false)) {	
			mTimeView.setTextColor(Color.GREEN);
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
		mTimeView = (TimeTextView) findViewById(R.id.fullscreen_content);
		
		mTimeView.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				controlsView.setVisibility(View.VISIBLE);
				
				delayedHide(3000);

				return false;
			}
		});
		
		findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);
		
		mAlarmReceiver = new AlarmReceiver();
		
		startService(new Intent(this, SensorInputService.class));
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		//delayedHide(100);
	}
	
	public void openSettings(View view) {
		//Intent intent = new Intent(this, SettingsActivity.class);
		//startActivity(intent);
		
		AlarmTrigger.enable(this);
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
