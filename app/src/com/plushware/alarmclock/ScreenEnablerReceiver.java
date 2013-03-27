package com.plushware.alarmclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.util.Log;

public class ScreenEnablerReceiver extends BroadcastReceiver {
	private class EnableScreenTask extends AsyncTask<Context, Void, Void> {
		final static String TAG = "EnableScreenTask";
		
		@Override
		protected Void doInBackground(Context... context) {
			PowerManager pm = (PowerManager)context[0].getSystemService(Context.POWER_SERVICE);
			PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "EnableScreenTask");
			
			Log.d(TAG, "About to acquire wake lock.");
			
			wl.acquire();
			
			if (wl.isHeld()) {
				Log.d(TAG, "Full wake lock aquired.");
			}
			else {
				Log.w(TAG, "Failed to acquire full wake lock.");
			}
			
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				;
			}
			finally {						
				wl.release();
			}
			
			Log.d(TAG, "Full wake lock released.");
			
			return null;
		}
	
	}
	
	
	public ScreenEnablerReceiver() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("ScreenEnablerReceiver", "onReceive");
		
		new EnableScreenTask().execute(context);
	}
}
