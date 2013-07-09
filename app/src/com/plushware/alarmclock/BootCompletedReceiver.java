package com.plushware.alarmclock;

import java.util.Date;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

public class BootCompletedReceiver extends BroadcastReceiver {
	public BootCompletedReceiver() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i("BootCompletedReceiver", "Boot completed.");
		
		AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		
		Intent updateTimeIntent = new Intent(context, UpdateSystemTimeReceiver.class);		
		updateTimeIntent.putExtra("Service", UpdateSystemTimeReceiver.SERVICE_WIFI);				
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, updateTimeIntent, 0);
		
		manager.cancel(pendingIntent);
		manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 
				AlarmManager.INTERVAL_HALF_DAY, pendingIntent);		

		AlarmTrigger.enable(context);
	}
}
