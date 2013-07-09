package com.plushware.alarmclock;

import com.commonsware.cwac.wakeful.WakefulIntentService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class UpdateSystemTimeReceiver extends BroadcastReceiver {
	public static final int SERVICE_UNKNOWN = 0;
	public static final int SERVICE_WIFI = 1;
	public static final int SERVICE_ALARM = 2;
	
	public UpdateSystemTimeReceiver() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		int service = intent.getIntExtra("Service", SERVICE_UNKNOWN);
			
		switch (service) {
		case SERVICE_WIFI:
			Log.d("UpdateSystemTimeReceiver", "Invoking EnableWifiService.");
			WakefulIntentService.sendWakefulWork(context, EnableWifiService.class);
			break;
			
		case SERVICE_ALARM:
			Log.d("UpdateSystemTimeReceiver", "Invoking AlarmService.");
			//WakefulIntentService.sendWakefulWork(context, AlarmService.class);
			break;
		
		default:
			Log.w("UpdateSystemTimeReceiver", "Invalid service specified (" + Integer.toString(service) + "), ignoring.");
			break;
		}		
	}
}
