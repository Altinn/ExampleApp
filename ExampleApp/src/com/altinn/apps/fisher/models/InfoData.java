package com.altinn.apps.fisher.models;

import com.altinn.apps.fisher.utils.Utils;

/**
 * This class is parent class for all the model classes in this package
 * To support abstraction, and has some basic methods which need be overridden by all the child classes
 * for Marshaling and De-Marshaling 
 *
 */
public class InfoData {	
	public long mCreateId;
	public long mSendId;
	public int mFormType;
	public int mId;
	public String mResponseId="";
	
	/**
	 * Entire data and states of this class can be converted into byte-straem
	 * @return
	 */
	public byte[] getBytes(){
		return null;
	}
	
	
	/**
	 * This method returns the data, which is in Json compatible text which is needed at the time of 
	 * posting the content at server.
	 * @return json-string-data
	 */
	public String createJSONPost(){
		return "";
	}
	
	/**
	 * This method can be used when it is required to get message-id without prefix or with prefix
	 * specify it input parameter.
	 * @return responseId : String
	 */
	public String getResponsId(boolean withPrefix){
		String responseId = mResponseId;
		if(!withPrefix && !Utils.isNullorEmpty(responseId) ){
			responseId = responseId.trim().toLowerCase();
			//The message ids are in the format {prefix}{id} where the prefix is 
			//“a” when it’s an active element, 
			//“b” when archived and 
			//“c” when Altinn1 archived.
			
			if(responseId.length()>1 && ( responseId.startsWith("a") ||  responseId.startsWith("b") ||  responseId.startsWith("c"))){
				responseId = responseId.substring(1);
			}
		}
		return responseId;
	}
	
	
	
}
