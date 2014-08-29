package com.altinn.apps.fisher.net.jsobj;
/**
 * A JSONObject  for "_embedded" 
 * Created into Java Object 'MessagesEmbedded' to use their fields directly
 */
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MessagesEmbedded implements JsonObj{	
	private LinkObj mLink;
	private ArrayList<MessageObj> mEmbededList;	
	
	public MessagesEmbedded(){
		mEmbededList = new ArrayList<MessageObj>();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.altinn.apps.fisher.net.jsobj.JsonObj#parse(org.json.JSONObject)
	 */
	public MessagesEmbedded parse(JSONObject jsObj){
		
		MessagesEmbedded obj = new MessagesEmbedded();
		try{
			if(jsObj.has("_links")){
				JSONObject  jsSubItem = jsObj.getJSONObject("_links");
				if(jsSubItem != null){
					LinkObj linkObj = new LinkObj();
					obj.mLink = linkObj.parse(jsSubItem);
				}
			}
			if(jsObj.has("_embedded")){
				JSONObject  jsSubItem = jsObj.getJSONObject("_embedded");
				JSONArray jsMsgArray = jsSubItem.getJSONArray("messages");
				if(jsMsgArray != null && jsMsgArray.length()>0){
					
					for(int i = 0 ; i <jsMsgArray.length();i++){
						JSONObject jsMsgObj = jsMsgArray.getJSONObject(i);
						if(jsMsgObj != null){
							MessageObj msgObj = new MessageObj();
							msgObj = msgObj.parse(jsMsgObj);
							if(msgObj != null){
								obj.mEmbededList.add(msgObj);
							}
						}
						
					}
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
	 * Its a warper function for, which will support String type input.
	 */
	public MessagesEmbedded parse(String jsStrings) throws JSONException {
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
			if(mLink != null)
				jsObj.put("_links", mLink.createJson());
			if(mEmbededList != null && mEmbededList.size()>0){
				JSONArray jsMessageArray = new JSONArray();
				for(int i = 0 ; i < mEmbededList.size(); i++){
					MessageObj msgObj = mEmbededList.get(i);
					if(msgObj != null){
						JSONObject jsMsgObj = msgObj.createJson();
						if(jsMsgObj != null){
							jsMessageArray.put(jsMsgObj);
						}
					}					
				}
				JSONObject tempJson= new JSONObject();
				tempJson.put("messages",jsMessageArray);
				jsObj.put("_embedded",tempJson);				
			}
			
		} catch (JSONException e) {
			jsObj = null;
			e.printStackTrace();
		}
		
		return jsObj;
	}

}
