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
	
	static final String PRESENCE_SHORT = "com.plushware.alarmclock.SensorInputService.PRESENCE_SHORT";
	static final String PRESENCE_LONG = "com.plushware.alarmclock.SensorInputService.PRESENCE_LONG";
	static final int PRESENCE_LONG_COOLDOWN = 3000;	
	
	Thread mPollThread;
	WakeLock mPartialWakeLock;
	
	public class PollSensorRunnable implements Runnable {
		Context mContext;
        
		public PollSensorRunnable(Context context) {
            mContext = context;
        }

        public void run() {
			Log.d(TAG, "Starting polling thread.");
		
			while (!Thread.currentThread().isInterrupted()) {
				int result = SensorInput.poll();
				
				if (result != -1) {					
					Log.d(TAG, "Got interrupt, broadcasting intent.");
					
					mContext.sendBroadcast(new Intent(PRESENCE_SHORT));
					
					Boolean longPresence = true;
					
					for (int i = 0; i < 30; i++) {
						if (SensorInput.getValue() == 0) {
							Log.d(TAG, "Long presence aborted after " + Integer.toString(i) + " samples.");							
							longPresence = false;
							
							break;
						}
						
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							Thread.currentThread().interrupt();
							break;
						}											
					}
					
					if (longPresence) {
						Log.d(TAG, "Got long presence, broadcasting intent.");
						mContext.sendBroadcast(new Intent(PRESENCE_LONG));
						
						try {
							Thread.sleep(PRESENCE_LONG_COOLDOWN);
						} catch (InterruptedException e) {
							Thread.currentThread().interrupt();
							break;
						}											
					}
				} else {
					Log.d(TAG, "Polling failed, will delay and try again.");
					
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
						break;
					}				
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
	
	private void initializeSensor() {
		SensorInput.init();
		
		Settings settings = new Settings(this);
		
		if (SensorInput.setThreshold(settings.sensorThreshold) >= 0) {
			Log.d(TAG, "Succeeded in setting sensor threshold.");			
		} else {
			Log.e(TAG, "Failed to set sensor threshold.");
		}	
	}
	
	@Override
	public void onCreate() {
		Log.d(TAG, "onCreate");
		
		if (SensorInput.isEnabled()) {		
			createPartialWakeLock();
			initializeSensor();
		
			mPollThread = new Thread(new PollSensorRunnable(this));
			mPollThread.start();
		}
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
