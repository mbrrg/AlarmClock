package com.plushware.alarmclock;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;

import com.plushware.hardware.SensorInput;

public class SensorInputService extends Service {
	static final String TAG = "SensorInputService";
	static final String PRESENCE_ACTION = "com.plushware.alarmclock.SensorInputService.PRESENCE_ACTION";
	
	Thread mPollThread;
	WakeLock mPartialWakeLock;
	
	public class PollSensorRunnable implements Runnable {
		Context mContext;
        
		public PollSensorRunnable(Context context) {
            mContext = context;
        }

        public void run() {
			Log.d(TAG, "Starting polling thread.");
			
			SensorInput test = new SensorInput();
		
			while (!Thread.currentThread().isInterrupted()) {
				int result = test.poll("1_pb4");
				
				if (result != -1) {					
					Log.d(TAG, "Got interrupt.");
					
					Intent intent = new Intent();
					
					intent.setAction(PRESENCE_ACTION);
									
					mContext.sendBroadcast(intent);
				}
				
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					break;
				}
			}
				
			Log.d(TAG, "Exiting polling thread.");
        }
    }	
	
	public SensorInputService() {
	}
	
	private void createPartialWakeLock() {
		Log.d(TAG, "About to create partial wake lock.");
		
		PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
		mPartialWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
		
		if (mPartialWakeLock.isHeld()) {
			Log.d(TAG, "Created partial wake lock and it is being held.");
		} else {
			Log.w(TAG, "Created partial wake lock but for some reason it is not held.");
		}		
	}
	
	private void releasePartialWakeLock() {
		if (mPartialWakeLock != null) {
			Log.d(TAG, "Releasing partial wake lock.");
			
			mPartialWakeLock.release();
		}
	}
	
	@Override
	public void onCreate() {
		Log.d(TAG, "onCreate");
		
		createPartialWakeLock();
		
		mPollThread = new Thread(new PollSensorRunnable(this));
		mPollThread.start();
	}

	@Override
	public int onStartCommand(android.content.Intent intent, int flags, int startId) {
		Log.d(TAG, "onStartCommand");
		
		return Service.START_STICKY;	
	}
	
	@Override
	public void onDestroy() {
		Log.d(TAG, "onDestroy, sending interrupt to poll thread");
		
		releasePartialWakeLock();
		
		mPollThread.interrupt();
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}	
}
