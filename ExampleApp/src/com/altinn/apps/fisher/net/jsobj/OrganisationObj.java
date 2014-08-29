package com.altinn.apps.fisher.net.jsobj;
/**
 * A JSONObject  for "organizations" 
 * Created into Java Object 'OrganisationObj' to use their fields directly
 * 
 */
import org.json.JSONException;
import org.json.JSONObject;

public class OrganisationObj implements JsonObj{
	
	public String mOrganizationNumber;
	public String mName;
	public String mMessageLink;
	public String mType;
	
	
	/*
	 * (non-Javadoc)
	 * @see com.altinn.apps.fisher.net.jsobj.JsonObj#parse(org.json.JSONObject)
	 */
	public OrganisationObj parse(JSONObject jsObj){
		OrganisationObj obj = new OrganisationObj();
		try{
			if(jsObj.has("OrganizationNumber"))
				obj.mOrganizationNumber = jsObj.getString("OrganizationNumber");
			if(jsObj.has("MessagesLink"))
				obj.mMessageLink = jsObj.getString("MessagesLink");
			if(jsObj.has("Name"))
				obj.mName = jsObj.getString("Name");
			if(jsObj.has("Type"))
				obj.mType = jsObj.getString("Type");
			
		}catch(JSONException jse){
			obj = null;
		}catch(Exception e){
			obj = null;
		}
		return obj;
	}
	
	/**
	 * Its a warper function for, which will support String type input.
	 */
	public OrganisationObj parse(String jsStrings) throws JSONException {
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
			if(mOrganizationNumber != null)
				jsObj.put("OrganizationNumber", mOrganizationNumber);
			if(mMessageLink != null)
				jsObj.put("MessagesLink", mMessageLink);
			if(mName != null)
				jsObj.put("Name", mName);
			if(mType != null)
				jsObj.put("Type", mType);
		} catch (JSONException e) {
			jsObj = null;
			e.printStackTrace();
		}
		
		return jsObj;
	}
	

}
