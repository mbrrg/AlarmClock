package com.plushware.alarmclock;

import android.content.Context;
import android.text.format.Time;
import android.util.AttributeSet;
import android.widget.TextView;

public class TimeTextView extends TextView {
	private static final int TIME_REFRESH_MILLIS = 30 * 1000;
	
	private RepeatingRunner mRefreshTimeRunner;
	
	private Runnable mRefreshTimeRunnable = new Runnable() {
		public void run() {						
			Time currentTime = new Time();        
			currentTime.setToNow();
			
			setText(currentTime.format("%H.%M"));    	    	    	   
		}
	};
	
	private void Initialize() {
		mRefreshTimeRunner = new RepeatingRunner(mRefreshTimeRunnable, TIME_REFRESH_MILLIS);
	}
	
	public TimeTextView(Context context) {
		super(context);
		Initialize();
	}
	
	public TimeTextView(Context context, AttributeSet set) {
		super(context, set);
		Initialize();
	}
	
	public TimeTextView(Context context, AttributeSet set, int x) {
		super(context, set, x);
		Initialize();
	}
		
	@Override
	public void onAttachedToWindow() {		
		mRefreshTimeRunnable.run();	
	}
	
	public void pause() {
		mRefreshTimeRunner.pause();
	}
	
	public void resume() {
		mRefreshTimeRunner.resume();
	}
}
