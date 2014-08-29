package com.altinn.apps.fisher.net.jsobj;
/**
 * A JSONObject  for "_links" 
 * Created into Java Object 'LinkObj' to use their fields directly
 */
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

public class LinkObj implements JsonObj {

	public ArrayList<LinkItemObj> mLinkList;

	public LinkObj(){
		mLinkList = new ArrayList<LinkItemObj>();
	}
	

	/*
	 * (non-Javadoc)
	 * @see com.altinn.apps.fisher.net.jsobj.JsonObj#parse(org.json.JSONObject)
	 */
	public LinkObj parse(JSONObject jsObj) {
		
		LinkObj obj = new LinkObj();
		try {
			if (jsObj.has("self")) {
				JSONObject self = jsObj.getJSONObject("self");
				LinkItemObj liObj = new LinkItemObj();
				liObj = liObj.parse(self);
				if (liObj != null) {
					liObj.mLinkType = LinkItemObj.LINK_TYPE_SELF;
					obj.mLinkList.add(liObj);
				}
			}

			if (jsObj.has("print")) {
				JSONObject print = jsObj.getJSONObject("print");
				LinkItemObj liObj = new LinkItemObj();
				liObj = liObj.parse(print);
				if (liObj != null) {
					liObj.mLinkType = LinkItemObj.LINK_TYPE_PRINT;
					obj.mLinkList.add(liObj);
				}
			}

			if (jsObj.has("attachment")) {
				JSONObject attachment = jsObj.getJSONObject("attachment");
				LinkItemObj liObj = new LinkItemObj();
				liObj = liObj.parse(attachment);
				if (liObj != null) {
					liObj.mLinkType = LinkItemObj.LINK_TYPE_ATTACHMENT;
					obj.mLinkList.add(liObj);
				}
			}

			if (jsObj.has("message")) {
				JSONObject message = jsObj.getJSONObject("message");
				LinkItemObj liObj = new LinkItemObj();
				liObj = liObj.parse(message);
				if (liObj != null) {
					liObj.mLinkType = LinkItemObj.LINK_TYPE_MESSAGE;
					obj.mLinkList.add(liObj);
				}
			}

		} catch (JSONException jse) {
			obj = null;
		} catch (Exception e) {
			obj = null;
		}
		return obj;
	}

	/**
	 * Its a warper function for, which will support String type input.
	 */
	public LinkObj parse(String jsStrings) throws JSONException {
		JSONObject jsObj = new JSONObject(jsStrings);
		return parse(jsObj);
	}

	/*
	 * (non-Javadoc)
	 * @see com.altinn.apps.fisher.net.jsobj.JsonObj#createJson()
	 */
	public JSONObject createJson(){
		JSONObject jsObj = null;
			if(mLinkList != null && mLinkList.size()>0){
				try {
				  jsObj = new JSONObject();
					for(LinkItemObj linkItem:mLinkList){						
							JSONObject jsItem  = null;
							String objType = "";
							if(linkItem.mLinkType == LinkItemObj.LINK_TYPE_SELF){
								objType = "self";														
							}else if(linkItem.mLinkType == LinkItemObj.LINK_TYPE_PRINT){
								objType = "print";
							}else if(linkItem.mLinkType == LinkItemObj.LINK_TYPE_ATTACHMENT){
								objType = "attachment";
							}else if(linkItem.mLinkType == LinkItemObj.LINK_TYPE_MESSAGE){
								objType = "message";
							}
							jsItem = linkItem.createJson();
							if(jsItem != null){
								jsObj.put(objType, jsItem);
							}
					}					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
		}
		return jsObj;
	}
	
}
