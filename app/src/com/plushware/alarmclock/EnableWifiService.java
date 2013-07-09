package com.plushware.alarmclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.SystemClock;
import android.util.Log;

import com.commonsware.cwac.wakeful.WakefulIntentService;

public class EnableWifiService extends WakefulIntentService {
	private static final int WIFI_ENABLED_MILLIS = 5 * 60 * 1000;
	
	public EnableWifiService() {
		super("EnableWifiService");
	}
	
	private static PendingIntent createTriggerIntent(Context context)
	{
		Intent updateTimeIntent = new Intent(context, BroadcastDispatcher.class);		
		updateTimeIntent.putExtra("Service", BroadcastDispatcher.SERVICE_WIFI);				
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, updateTimeIntent, 0);		
		
		return pendingIntent;
	}
	
	public static void disableTrigger(Context context)
	{
		AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);		
		
		manager.cancel(createTriggerIntent(context));		
	}
	
	public static void enableTrigger(Context context)
	{
		disableTrigger(context);
		
		AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);		
		
		manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 
				AlarmManager.INTERVAL_HALF_DAY, createTriggerIntent(context));								
	}

	@Override
	protected void doWakefulWork(Intent intent) {						
		Log.d("EnableWifiService", "About to enable wifi for " + WIFI_ENABLED_MILLIS + " ms.");
		
		WifiManager wifiManager = (WifiManager)this.getSystemService(Context.WIFI_SERVICE);		
		wifiManager.setWifiEnabled(true);
		
		Log.d("EnableWifiService", "Wifi enabled, waiting.");
		
		try {						
			Thread.sleep(WIFI_ENABLED_MILLIS); 
		} catch (InterruptedException e) { 			
		}
		finally {
			Log.d("EnableWifiService", "Wait complete, disabling wifi.");
			
			wifiManager.setWifiEnabled(false);
		}						
	}
}
