package com.altinn.apps.fisher.net.jsobj;
/**
 * A JSONObject  for items of "_links" type 
 * Created into Java Object 'LinkItemObj' to use their fields directly
 * 
 */

import org.json.JSONException;
import org.json.JSONObject;

public class LinkItemObj implements JsonObj{

	public static final int LINK_TYPE_SELF = 2000;
	public static final int LINK_TYPE_PRINT = 2001;
	public static final int LINK_TYPE_ATTACHMENT = 2002;
	public static final int LINK_TYPE_MESSAGE = 2002;
	
	public int mLinkType;
	public String mLinkHref;
	public String mLinkName;
	public boolean mTemplated;
	
	/*
	 * (non-Javadoc)
	 * @see com.altinn.apps.fisher.net.jsobj.JsonObj#parse(org.json.JSONObject)
	 */
	public LinkItemObj parse(JSONObject jsObj){
		LinkItemObj obj = new LinkItemObj();	
		try{
				if(jsObj != null){
					
					if(jsObj.has("href")){
						String href = jsObj.getString("href");
						obj.mLinkHref = href;						
					}
					if(jsObj.has("name")){
						String name = jsObj.getString("name");
						obj.mLinkName = name;
					}
					if(jsObj.has("isTemplated")){
						boolean isTemplated = jsObj.getBoolean("isTemplated");					
							obj.mTemplated = isTemplated;
					}
					
					
				}
			
			
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
	public LinkItemObj parse(String jsStrings) throws JSONException {
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
			
			if(mLinkHref == null)
				return jsObj;
			jsObj = new JSONObject();
			jsObj.put("href", mLinkHref);
			if(mLinkName != null)
				jsObj.put("name", mLinkName);
			if(mTemplated)
				jsObj.put("isTemplated", mTemplated);			
			
		} catch (JSONException e) {
			jsObj = null;
			e.printStackTrace();
		}
		
		return jsObj;
	}
	
}
