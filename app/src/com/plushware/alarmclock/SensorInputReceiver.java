package com.plushware.alarmclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class SensorInputReceiver extends BroadcastReceiver {
	public static final String TAG = "SensorInputReceiver";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		
		Log.d(TAG, "Received " + action);
		
		if (action == AlarmClock.INTENT_SENSOR_HIT_SHORT) {	
			context.sendBroadcast(new Intent(AlarmClock.INTENT_SCREEN_ON_OFF));
		} else if (action == AlarmClock.INTENT_SENSOR_HIT_LONG) {
			context.sendBroadcast(new Intent(AlarmClock.INTENT_ALARM_OFF));
		} else {
			assert false;
		}		
		
	}
}
