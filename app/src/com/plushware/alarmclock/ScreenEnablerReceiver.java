package com.plushware.alarmclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.util.Log;

public class ScreenEnablerReceiver extends BroadcastReceiver {
	private static Thread mCancelThread;
	private static Object mSync = new Object();
	
	private class TaskParams {
		public Context context;
		public Boolean sleepIndefinitely;
		
		public TaskParams(Context c, Boolean s) {
			context = c;
			sleepIndefinitely = s;
		}
	}
	
	private class EnableScreenTask extends AsyncTask<TaskParams, Void, Void> {		
		@Override
		protected Void doInBackground(TaskParams... params) {
			synchronized (mSync) {
				mCancelThread = Thread.currentThread();
			}
			
			PowerManager pm = (PowerManager)params[0].context.getSystemService(Context.POWER_SERVICE);
			PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "EnableScreenTask");
			
			Log.d("EnableScreenTask", "Acquiring full wake lock.");
					
			wl.acquire();
			
			try {
				if (params[0].sleepIndefinitely) {
					while (true) {
						Thread.sleep(500);
					}
				}
				else {
					Settings s = new Settings(params[0].context);
					
					Thread.sleep(s.screenWakeTime * 1000);			
				}
			} catch (InterruptedException e) {
				;
			}
			finally {						
				wl.release();
			}
			
			synchronized (mSync) {
				mCancelThread = null;
			}
			
			Log.d("EnableScreenTask", "Full wake lock released.");		
			
			return null;
		}
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("ScreenEnablerReceiver", "onReceive");

		String action = intent.getAction();
		
		if (action == AlarmClock.INTENT_SCREEN_ON) {
			new EnableScreenTask().execute(new TaskParams(context, true));
		} else if (action == AlarmClock.INTENT_SCREEN_OFF) {
			synchronized (mSync) {			
				if (mCancelThread != null) {
					mCancelThread.interrupt();
				}
			}
		} else if (action == AlarmClock.INTENT_SCREEN_ON_OFF) {	
			new EnableScreenTask().execute(new TaskParams(context, false));
		} else {
			assert false;
		}
	}
}
