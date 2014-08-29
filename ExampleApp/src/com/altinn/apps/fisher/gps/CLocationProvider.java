package com.altinn.apps.fisher.gps;
/**
 * Currently not in Use
 */

import android.content.Context;
import android.location.Criteria;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import com.altinn.apps.fisher.utils.Utils;

public class CLocationProvider implements LocationListener {
	private static CLocationProvider instance;
	private ILocationUpdateListner listner;
	private Context mContext;
	private LocationManager mLocationManager;
	private LocationProvider mLocationProvider;

	private final long UPDATE_FREQUENCY = 30 * 1000;// 10*60*1000;//10 Mins
	private final long UPDATE_ACCURACY = 10;// 100 meters
	private HandlerThread mThread;// = new HandlerThread("UIHandler");
	private CHandler mHandler;
	private final int GPS_FIX_TYPE_CELLID = 100;
	private final int GPS_FIX_TYPE_INBUILD = 101;
	private int mGPSFixType = GPS_FIX_TYPE_CELLID;
	private final long GPS_FIX_TIMEOUT_CELLID = 1000 * 30; // 10sec

	private final int START_INIT = 0;
	private final int START_GPS_FIX = 1;
	private final int NOTIFY_INIT_COMPLETE = 2;
	private final int NOTIFY_LOCATION_UPDATE = 3;

	private CLocationProvider(Context context) {
		mContext = context;
		mThread = new HandlerThread("UIHandler");
		mThread.start();
		mHandler = new CHandler(mThread.getLooper());
	}

	public static CLocationProvider getInstance(Context context) {
		if (instance == null) {
			instance = new CLocationProvider(context);
		}
		return instance;
	}

	private void init() {

		System.out.println("---init+++  mContext :" + mContext);
		mLocationManager = (LocationManager) mContext
				.getSystemService(Context.LOCATION_SERVICE);
		System.out.println("--- mLocationManager :" + mLocationManager);
		mLocationProvider = mLocationManager
				.getProvider(LocationManager.GPS_PROVIDER);
		System.out.println("---mLocationProvider----" + mLocationProvider);

		// This code work for tablet/ API-Level 9 needed
		if (Utils.isNetworkAvailable(mContext) && Utils.isAPIsupported() && iSGPSEnabled()) {// get
																			// CELL_ID
																			// Based
																			// FIX
																			// [FOR
																			// fist
																			// FIX]
			Criteria criteria = new Criteria();
			criteria.setAccuracy(Criteria.ACCURACY_COARSE);
			criteria.setCostAllowed(true);
			criteria.setBearingAccuracy(Criteria.ACCURACY_COARSE);
			mGPSFixType = GPS_FIX_TYPE_CELLID;
			mLocationManager.requestSingleUpdate(criteria, this,
					mThread.getLooper());
			mHandler.sendEmptyMessageDelayed(START_GPS_FIX,
					GPS_FIX_TIMEOUT_CELLID);
		} else {
			mHandler.sendEmptyMessageDelayed(START_GPS_FIX, 0);
		}
		System.out.println("---init----");

		mLocationManager.addGpsStatusListener(new GpsStatus.Listener() {

			public void onGpsStatusChanged(int event) {
				switch (event) {
				case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
					System.out.println("GPS_EVENT_SATELLITE_STATUS :" + event);
					break;
				case GpsStatus.GPS_EVENT_STARTED:
					System.out.println("GPS_EVENT_STARTED :" + event);
					break;
				case GpsStatus.GPS_EVENT_FIRST_FIX:
					System.out.println("GPS_EVENT_FIRST_FIX :" + event);
					break;
				case GpsStatus.GPS_EVENT_STOPPED:
					System.out.println("GPS_EVENT_STOPPED :" + event);
					break;
				}

			}
		});

		mHandler.sendEmptyMessage(NOTIFY_INIT_COMPLETE);

		// List<String> prividesList = mLocationManager.getAllProviders();
		// for (String providerNameStr :prividesList){
		// System.out.println("Provider  :" + providerNameStr);
		// if(providerNameStr != null){
		// onLocationChanged(mLocationManager.getLastKnownLocation(providerNameStr));
		// }
		// }

	}

	public void registerListner(ILocationUpdateListner listner) {
		this.listner = listner;
	}

	public boolean iSGPSEnabled() {
		boolean result = false;
		if (mLocationProvider != null) {
			result = mLocationManager.isProviderEnabled(mLocationProvider
					.getName());
		}
		return result;
	}

	public void onLocationChanged(Location location) {
		System.out.println("onLocationChanged+++" + location);

		if (location != null) {
			// listner.onLocationChange(location);
			Message msg = Message.obtain(mHandler);
			msg.what = NOTIFY_LOCATION_UPDATE;
			msg.obj = location;
			//mHandler.sendMessageDelayed(msg, 5*1000);
			mHandler.sendMessage(msg);

		}
		if (mGPSFixType != GPS_FIX_TYPE_INBUILD) {
			mHandler.sendEmptyMessageDelayed(START_GPS_FIX, 0);
		}
		System.out.println("onLocationChanged---");

	}

	public void onProviderDisabled(String provider) {
		System.out.println("onProviderDisabled---" + provider);

	}

	public void onProviderEnabled(String provider) {
		System.out.println("onProviderEnabled---" + provider);

	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	public void stop() {
		try {
			if (mLocationManager != null)
				mLocationManager.removeUpdates(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void start() {
		stop();
		mHandler.sendEmptyMessage(START_INIT);
	}

	private class CHandler extends Handler {

		public CHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case START_INIT:
				init();
				break;
			case START_GPS_FIX:
				if (mLocationProvider != null) {
					System.out.println("---mLocationProvider.getName----"
							+ mLocationProvider.getName());
					mGPSFixType = GPS_FIX_TYPE_INBUILD;
					mLocationManager.removeUpdates(CLocationProvider.this);
					mLocationManager.requestLocationUpdates(
							mLocationProvider.getName(), UPDATE_FREQUENCY,
							UPDATE_ACCURACY, CLocationProvider.this);
				}
				break;
			case NOTIFY_INIT_COMPLETE:
				if (listner != null)
					listner.onInitializationComplete();
				break;
			case NOTIFY_LOCATION_UPDATE:
				if (listner != null) {
					Location lObj = (Location)msg.obj;
					listner.onLocationChange(lObj);
				}
				break;

			}

		}

	}

}
