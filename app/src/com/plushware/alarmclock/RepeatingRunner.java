package com.plushware.alarmclock;

import android.os.Handler;

public class RepeatingRunner {
	protected final Handler mHandler = new Handler();
	private final long mIntervalMillisecs;
	private Runnable mExternalRunnable;
	private boolean mPaused; 
	
	private Runnable mRunnable = new Runnable() {
	   public void run() {
		   mHandler.removeCallbacks(this);
		   
		   if (!mPaused) {
			   mExternalRunnable.run();
			   mHandler.postDelayed(this, mIntervalMillisecs);
		   }
	   }
	};

	public RepeatingRunner(Runnable runnable, int intervalMillisecs) {
		mIntervalMillisecs = intervalMillisecs;
		mExternalRunnable = runnable;
		
		mRunnable.run();
	}
	
	public void pause() {
		mPaused = true;
	}
	
	public void resume() {
		mPaused = false;
		mRunnable.run();
	}
}
