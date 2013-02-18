package com.plushware.alarmclock;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.commonsware.cwac.wakeful.WakefulIntentService;

public class EnableWifiService extends WakefulIntentService {
	private static final int WIFI_ENABLED_MILLIS = 5 * 60 * 1000;
	
	public EnableWifiService() {
		super("EnableWifiService");
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
