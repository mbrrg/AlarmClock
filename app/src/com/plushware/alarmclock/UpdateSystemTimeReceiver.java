package com.plushware.alarmclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;

public class UpdateSystemTimeReceiver extends BroadcastReceiver {
	public UpdateSystemTimeReceiver() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {
        PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");

        try
        {
        	wl.acquire();        	

        	Log.i("UpdateSystemTimeReceiver", "Received broadcast.");
        	
        	
        } finally {
        	wl.release();
        }
	}
}
