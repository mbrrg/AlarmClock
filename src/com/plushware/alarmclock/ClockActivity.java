package com.plushware.alarmclock;

import com.plushware.alarmclock.util.SystemUiHider;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Time;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class ClockActivity extends Activity {
	private static final boolean AUTO_HIDE = true;
	private static final int AUTO_HIDE_DELAY_MILLIS = 3000;
	private static final boolean TOGGLE_ON_CLICK = true;
	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;
	
	private SystemUiHider mSystemUiHider;
	private RepeatingRunner mRefreshTimeRunner;
	private TextView mTimeView;
	
	private Runnable mRefreshTimeRunnable = new Runnable() {
		public void run() {
			Time currentTime = new Time();        
			currentTime.setToNow();
			
			mTimeView.setText(currentTime.format("%H:%M"));    	    	    	   
		}
	};
	
	@Override
	protected void onPause() {
		super.onPause();
		
		mRefreshTimeRunner.pause();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		mRefreshTimeRunner.resume();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_clock);

		final View controlsView = findViewById(R.id.fullscreen_content_controls);
		mTimeView = (TextView) findViewById(R.id.fullscreen_content);
		
		mRefreshTimeRunner = new RepeatingRunner(mRefreshTimeRunnable, 5000);
		mRefreshTimeRunnable.run();

		mSystemUiHider = SystemUiHider.getInstance(this, mTimeView, HIDER_FLAGS);
		mSystemUiHider.setup();
		mSystemUiHider
				.setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
					// Cached values.
					int mControlsHeight;
					int mShortAnimTime;

					@Override
					public void onVisibilityChange(boolean visible) {
						if (mControlsHeight == 0) {
							mControlsHeight = controlsView.getHeight();
						}
						if (mShortAnimTime == 0) {
							mShortAnimTime = getResources().getInteger(
									android.R.integer.config_shortAnimTime);
						}
						controlsView
								.animate()
								.translationY(visible ? 0 : mControlsHeight)
								.setDuration(mShortAnimTime);

						if (visible && AUTO_HIDE) {
							delayedHide(AUTO_HIDE_DELAY_MILLIS);
						}
					}
				});

		mTimeView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (TOGGLE_ON_CLICK) {
					mSystemUiHider.toggle();
				} else {
					mSystemUiHider.show();
				}
			}
		});

		findViewById(R.id.dummy_button).setOnTouchListener(
				mDelayHideTouchListener);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		delayedHide(100);
	}

	View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			if (AUTO_HIDE) {
				delayedHide(AUTO_HIDE_DELAY_MILLIS);
			}
			return false;
		}
	};

	Handler mHideHandler = new Handler();
	Runnable mHideRunnable = new Runnable() {
		@Override
		public void run() {
			mSystemUiHider.hide();
		}
	};

	private void delayedHide(int delayMillis) {
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, delayMillis);
	}
}
