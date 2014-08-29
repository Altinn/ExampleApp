package com.altinn.apps.fisher.net.jsobj;
/**
 * A JSONObject  for "Attachments" 
 * Created into Java Object 'AttachmentObj' to use their fields directly
 */

import org.json.JSONException;
import org.json.JSONObject;

public class AttachmentObj implements JsonObj{

	public String mAttachmentLink;
	public String mFileName;
	public String mData;
	
	/*
	 * (non-Javadoc)
	 * @see com.altinn.apps.fisher.net.jsobj.JsonObj#parse(org.json.JSONObject)
	 */
	public AttachmentObj parse(JSONObject jsObj){
		AttachmentObj obj = new AttachmentObj();
		try{
			if(jsObj.has("FileName"))
				obj.mFileName = jsObj.getString("FileName");
			if(jsObj.has("AttachmentLink"))
				obj.mAttachmentLink = jsObj.getString("AttachmentLink");
			if(jsObj.has("Data"))
				obj.mData = jsObj.getString("Data");
			
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
	public AttachmentObj parse(String jsStrings) throws JSONException {
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
			if(mFileName != null)
				jsObj.put("FileName", mFileName);
			if(mAttachmentLink != null)
				jsObj.put("AttachmentLink", mAttachmentLink);
			if(mData != null)
				jsObj.put("Data", mData);
		} catch (JSONException e) {
			jsObj = null;
			e.printStackTrace();
		}
		
		return jsObj;
	}
	
	
}
