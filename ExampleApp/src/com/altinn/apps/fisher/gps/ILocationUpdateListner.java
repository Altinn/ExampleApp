package com.altinn.apps.fisher.gps;
/**
 * Currently not in use
 * 
 */
import android.location.Location;

public interface ILocationUpdateListner {

	public void onLocationChange(Location location);
	
	public void onInitializationComplete();
}
