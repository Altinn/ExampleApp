package com.altinn.apps.fisher.net.jsobj;
/**
 * A JSONObject  for "Forms" 
 * Created into Java Object 'FormObj' to use their fields directly
 * 
 */
import org.json.JSONException;
import org.json.JSONObject;

public class FormObj implements JsonObj{	
	
	public String mDataFormatId;
	public String mDataFormatVersion;
	public String mValidationStatus;
	public String mType;
	public String mName;
	public String mFormData;
	
	/*
	 * (non-Javadoc)
	 * @see com.altinn.apps.fisher.net.jsobj.JsonObj#parse(org.json.JSONObject)
	 */
	public FormObj parse(JSONObject jsObj){
		FormObj obj = new FormObj();
		try{
			if(jsObj.has("DataFormatVersion"))
				obj.mDataFormatVersion = jsObj.getString("DataFormatVersion");
			if(jsObj.has("DataFormatId"))
				obj.mDataFormatId = jsObj.getString("DataFormatId");
			if(jsObj.has("ValidationStatus"))
				obj.mValidationStatus = jsObj.getString("ValidationStatus");
			if(jsObj.has("Type"))
				obj.mType = jsObj.getString("Type");
			if(jsObj.has("Name"))
				obj.mName = jsObj.getString("Name");
			if(jsObj.has("FormData"))
				obj.mFormData = jsObj.getString("FormData");
			
		}catch(JSONException jse){
			obj = null;
		}catch(Exception e){
			obj = null;
		}
		return obj;
	}
	
	/**
	 * its a warper function for, which will support String type input.
	 */
	public FormObj parse(String jsStrings) throws JSONException {
		JSONObject jsObj = new JSONObject(jsStrings);
		return parse(jsObj);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.altinn.apps.fisher.net.jsobj.JsonObj#createJson()
	 */
	public JSONObject createJson(){		
		JSONObject  jsObj = null;		
		try {
			jsObj = new JSONObject();
			if(mDataFormatVersion != null)
				jsObj.put("DataFormatVersion", mDataFormatVersion);
			if(mDataFormatId != null)
				jsObj.put("DataFormatId", mDataFormatId);
			if(mValidationStatus != null)
				jsObj.put("ValidationStatus", mValidationStatus);
			if(mType != null)
				jsObj.put("Type", mType);
			if(mName != null)
				jsObj.put("Name", mName);
			if(mFormData != null)
				jsObj.put("FormData", mFormData);
		} catch (JSONException e) {
			jsObj = null;
			e.printStackTrace();
		}
		
		return jsObj;
	}

}
