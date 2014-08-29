package com.altinn.apps.fisher.utils;
/**
 * This class includes some static functions those are required through out the application 
 * very frequently ad generic in nature.
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Environment;

public class Utils {
	
	/**
	 * A custom logger which offers 
	 * Which can be used for logging or debugging and if we don't want log messages we can comment inside the 
	 * respective method body, that will remove your screen specific un-commenting effort.
	 *
	 */
	public static class Log{
		public static void d(String tag, String message){
			android.util.Log.d(tag, message);
		}
		
		public static void e(String tag, String message){
			android.util.Log.e(tag, message);
		}
		
		public static void e(String tag, String message, Throwable th){
			android.util.Log.e(tag, message,th);
		}
		
		public static void i(String tag, String message){
			android.util.Log.i(tag, message);
		}
	}
	
	/**
	 * A utility function to check null/empty behavior of a String
	 * @param text
	 * @return
	 */
	public static boolean isNullorEmpty(String text){
		if(text == null || text.trim().length() == 0 || text.trim().equalsIgnoreCase("null")){
			return true;
		}
		return false;
	}
	
	/**
	 * This method checks network availability
	 * @param context
	 * @return
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if(cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable()
				&& cm.getActiveNetworkInfo().isConnected()){
			Log.d("NETWORK-IFO ---", "TRUE");
			return true;
		} else{
			Log.d("NETWORK-IFO ---", "FALSE");
			return false;
			
		}
	}
	
	/**
	 * This method copies the file into external memory
	 * @param fnameOut
	 * @param fnameIn
	 */
	public static void copyFileInExternalMemory(String fnameOut, String fnameIn){
		 try {
			File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+ fnameOut);
			 if(!file.exists()){
				 FileOutputStream fos  = new FileOutputStream(file);
				 FileInputStream fis = new FileInputStream(new File(fnameIn));
				 byte[] buffer = new byte[1024];
				 int ch = -1;
				 while((ch =  fis.read(buffer)) != -1){
					 fos.write(buffer,0,ch);
				 }
				 fis.close();
				 fos.flush();
				 fos.close();
			 }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * This method checks API support weather application is greater than FROYO or not
	 * @return
	 */
	public static boolean isAPIsupported(){
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		return currentapiVersion > android.os.Build.VERSION_CODES.FROYO;
	}
	
	
	

}
