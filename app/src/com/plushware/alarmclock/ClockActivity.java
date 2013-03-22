package com.plushware.alarmclock;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

public class ClockActivity extends Activity {
	private static final int AUTO_HIDE_DELAY_MILLIS = 3000;	
		
	private TimeTextView mTimeView;
	
	@Override
	protected void onPause() {
		super.onPause();
		
		mTimeView.pause();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		mTimeView.resume();
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
		
		startService(new Intent(this, SensorInputService.class));
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		//delayedHide(100);
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
