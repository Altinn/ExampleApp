package com.altinn.apps.fisher.common;
/**
 * This class contains common constants throughout the Application
 *
 */

public class AppConstants {
	
	public static final int TIME_OUT = 30000;
	
	public static final int ERROR_TYPE_NONE = 0;
	public static final int ERROR_TYPE_UNKNOWN = 1;
	public static final int ERROR_TYPE_WRONG_CREDENTIALS = 2;
	public static final int ERROR_TYPE_CONN_TIME_OUT = 3;
	public static final int ERROR_TYPE_LOGIN_EXPIRE = 302;
	
	public static final long MILLIES_ONE_MINUTE = 60*1000;
	public static final long TOKEN_EXPIRE_TIME_MINS = 30;//Configured at server end
	public static final long TOKEN_EXPIRE_TIME_MILLIES = TOKEN_EXPIRE_TIME_MINS*MILLIES_ONE_MINUTE;
	public static final long REFRESH_AVAILABLE_TIME = TOKEN_EXPIRE_TIME_MILLIES>>1;
	public static final long REFRESH_TIMER = REFRESH_AVAILABLE_TIME>>1;
	public static final String API_KEY_HEADER = "ApiKey";
	public static final String API_KEY_HEADER_VALUE = "Go to https://www.altinn.no/api/help to request a key";
		


	public static final String LOGIN_WEBSERVICE_URL =  "https://www.altinn.no/API";
	public static final String ORGLIST_WEBSERVICE_URL =  "https://www.altinn.no/api/organizations";

	public static final String POST_MESSAGE_WEBSERVICE_URL =  "https://www.altinn.no/api/%s/messages";
}
