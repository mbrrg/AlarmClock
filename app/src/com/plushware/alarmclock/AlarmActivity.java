package com.plushware.alarmclock;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;

public class AlarmActivity extends Activity {

	private MediaPlayer mPlayer;		
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
		
		setContentView(R.layout.activity_alarm);

		mTimeView = (TimeTextView)findViewById(R.id.timeAlarm);
		
		mPlayer = MediaPlayer.create(this, R.raw.alarm);
		mPlayer.setLooping(true);
		
        mPlayer.start();		
	}

	@Override
	protected void onDestroy() {
		mPlayer.stop();
		
		super.onDestroy();
	}
}
