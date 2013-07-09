package com.plushware.alarmclock;

import com.commonsware.cwac.wakeful.WakefulIntentService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BroadcastDispatcher extends BroadcastReceiver {
	public static final String TAG = "BroadcastDispatcher";
	public static final int SERVICE_UNKNOWN = 0;
	public static final int SERVICE_WIFI = 1;
	public static final int SERVICE_ALARM = 2;
	
	public BroadcastDispatcher() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		int service = intent.getIntExtra("Service", SERVICE_UNKNOWN);
			
		switch (service) {
		case SERVICE_WIFI:
			Log.d(TAG, "Invoking EnableWifiService.");
			WakefulIntentService.sendWakefulWork(context, EnableWifiService.class);
			break;
			
		case SERVICE_ALARM:
			Log.d(TAG, "Invoking AlarmService.");
			//WakefulIntentService.sendWakefulWork(context, AlarmService.class);
			break;
		
		default:
			Log.w(TAG, "Invalid service specified (" + Integer.toString(service) + "), ignoring.");
			break;
		}		
	}
}
