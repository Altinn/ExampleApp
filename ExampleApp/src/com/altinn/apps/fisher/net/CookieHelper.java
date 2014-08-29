package com.altinn.apps.fisher.net;
/**
 * Since we do not have any web-service which will do login for us and respond back some auth-code to get response from web-service
 * So CookieHelper class is used to retrieve cookies (Auth-Token) when we perform login operation through web-view
 * also it will update the cookies if we received (refresh-token) it while accessing web service.
 * 
 * This class stores the cookies for future references. 
 * It is a singleton class accessible throughout the application.
 * 
 */
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.message.BasicHeader;

import android.util.Log;

import com.altinn.apps.fisher.utils.PreferenceUtils;
import com.altinn.apps.fisher.utils.Utils;

public class CookieHelper {
	private static final String TAG = "com.altinn.apps.fisher.net.CookieHelper";
	private static CookieHelper mInstance;
	private  Hashtable<String, String> cookieMap;
	
	private CookieHelper(){
		cookieMap = new Hashtable<String, String>();
		initCookie();
	}
	
	public static CookieHelper getInstance(){
		if(mInstance == null)
			mInstance = new CookieHelper();
		return mInstance;
	}
	
	public void updateCookies(Header[] headers){
		
		if(headers != null){
			for(Header header:headers){
				String key = header.getName();
				String value = header.getValue();
				if(cookieMap.containsKey(key))
					cookieMap.put(key, value);
			}
		}
	}
	private void initCookie(){
		cookieMap.put("BIGipServerai2v2-tt02-intweb-80", "2652430787.20480.0000");
		cookieMap.put("ASP.NET_SessionId", "vddjtq55ca2veemsx1wzwn55");
		cookieMap.put("BIGipServerai2v2-tt02-portal-80", "2920866243.20480.0000");
		cookieMap.put(".ASPXAUTH", "D6D09DD6CC07A3B973F2CD84EACFAAF316BCFF8BAC47756E0BF787C069F5B078DD5E1B190993F4FF2E2F19C87D6E43780B37860ABA2DFF8372037939A9A3118B4A582ECBB126CACC19488B68E1BC5EB5199F43D4CB6451C5C531A818BB17221B35CBDD9DECE0BBA4EEDED8B28FC95A6110192DC42262C363CA587A5E285CBBA3219D4348586DA42B895E8D51E11696F241FDF28F91E76D8EEDD57F91C5305112D5F56C63615088DA5814EC66599D918A766AB049");
		cookieMap.put("Environment", "Identified");
		cookieMap.put("altinnContext", "lFGpvr0ZkFwGCRa+BnmNCNt7WAfYs8jMMWyj8jHhmfY=");
		cookieMap.put("altinnPersistentContext", "UL=1044&LM=10");
		updateCookie(PreferenceUtils.getInstance().getCookies(), false);
	}
	
	public void updateCookie(String cookieStr, boolean fromNetwork){
		if(Utils.isNullorEmpty(cookieStr))
			return;
		StringTokenizer tokenizer = new StringTokenizer(cookieStr, ";");
	    while( tokenizer.hasMoreElements()){//Parsing of the cookies element
	    	String token = tokenizer.nextToken().trim();	    	
	    	int index = -1;
	    	index = token.indexOf('=');
	    	if(index>0){
	    		String key = token.substring(0, index);
	    		String value = token.substring(index+1);
	    		if(cookieMap.containsKey(key)){
	    			if(!cookieMap.get(key).startsWith(value)){
		    			cookieMap.put(key, value);
		    			//cookie associated for this (.ASPXAUTH) is primarily responsible for accessing web service
		    			if(key.equalsIgnoreCase(".ASPXAUTH") && fromNetwork){	    			
		    				PreferenceUtils.getInstance().setCookieUpdateTime(System.currentTimeMillis());
		    				Log.d(TAG, "TIME-Cokkie-Uapdate: "+(new Date(System.currentTimeMillis())).toString());
		    			}
	    			}
	    		}
	    	}
	    }
	    PreferenceUtils.getInstance().setCookies(cookieStr);
	}
	/**
	 * A cookie header created from preferences
	 * While making any call to access web-service we have to set this header
	 * @return header
	 */
	public Header getCookieHeader(){
		Enumeration<String> keys = cookieMap.keys();
		String cookieStr = "";
		while(keys.hasMoreElements()){
			String key = keys.nextElement();
			String value = cookieMap.get(key);
			cookieStr += key+"="+value+"; ";
		}
		
		Header header = new BasicHeader("Cookie",cookieStr.trim());
		return header;
	}
	
	/**
	 * Update the cookie if we get refresh token while accessing web-service
	 * if in HttpResponse there is any 'Location' header and task execution is kind of 
	 * post-message in that case here we are parsing message-id from location header and returned
	 * @param response
	 * @return
	 */
	public String updateCokkieFromHeaders(HttpResponse response){
		String msgReturn = null; //here location header get returned
		//expected like "Location: https://tt02.altinn.basefarm.net/api/910463152/messages/b1110209"
		//Here we need to return meesage-id b1110209
		if(response != null){
			System.out.println("****** HEADER START *********");
			Header[] headers = response.getAllHeaders();
			for(Header header:headers){
				System.out.println( header.getName() + ":"+header.getValue());
				if(header.getName().equalsIgnoreCase("Set-Cookie")){
					updateCookie(header.getValue(),true);
				}
				if(header.getName().equalsIgnoreCase("Location")){
					int index = header.getValue().lastIndexOf('/');
					msgReturn = header.getValue().substring(index+1);
				}
			}
			System.out.println("****** HEADER END *********");
		}
		return msgReturn;
	}
	

}
