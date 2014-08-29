package com.altinn.apps.fisher.net;
/**
 * This is parser to parse json strings, and make respective 'JsonObj' object or list of these objects.
 * 
 */
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.altinn.apps.fisher.net.jsobj.JsonObj;
import com.altinn.apps.fisher.net.jsobj.MessageObj;
import com.altinn.apps.fisher.net.jsobj.MessagesEmbedded;
import com.altinn.apps.fisher.net.jsobj.OrganisationObj;

public class JSParser implements IParser{
	
	public static final int PARSE_TYPE_OBJ_MSG = 110;
	public static final int PARSE_TYPE_OBJ_EMMSG = 111;
	public static final int PARSE_TYPE_OBJ_ORG = 112;
	
	
	protected JSParser(){
		
	}
	
	/**
	 * If it is known that response data is Json-array
	 * then use this method. 
	 * In current framework usually we get response 3 kind of 
	 * Json-objects PARSE_TYPE_OBJ_MSG,PARSE_TYPE_OBJ_EMMSG,PARSE_TYPE_OBJ_ORG
	 * 
	 * @param jsonStr
	 * @param typeObj
	 * @return
	 */
	public ArrayList<JsonObj> parseList(String jsonStr,  int typeObj) {
		ArrayList<JsonObj> mList = new ArrayList<JsonObj>();
			try {
				JSONArray jsArray = new JSONArray(jsonStr);
				if(jsArray != null && jsArray.length()>0){
					for(int i = 0 ;  i < jsArray.length();i++){
						JSONObject jsObj =  jsArray.getJSONObject(i);
						JsonObj jnObj = parse(jsObj,typeObj);
						if(jnObj != null){
							mList.add(jnObj);
						}
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		return mList;
	}
	
	/**
	 * This method is used to parse json text and converts into Java - JsonObj
	 * @param jsonStr
	 * @param typeObj
	 * @return
	 */
	public JsonObj parse(String jsonStr,  int typeObj) {
		JSONObject jsonObj = null;
		try {
			 jsonObj = new JSONObject(jsonStr);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if(jsonObj != null)
			return parse(jsonObj,typeObj);
		else
			return null;
		
	}
	
	/**
	 * This method is used to parse json text and converts into Java - JsonObj
	 * @param jsonObj
	 * @param typeObj
	 * @return
	 */
	public JsonObj parse(JSONObject jsonObj,  int typeObj) {
		JsonObj jnObj = null;
		switch(typeObj){
			case PARSE_TYPE_OBJ_MSG:
				jnObj = new MessageObj();			
				break;
			case PARSE_TYPE_OBJ_ORG:
				jnObj = new OrganisationObj();			
				break;
			case PARSE_TYPE_OBJ_EMMSG:
				jnObj = new MessagesEmbedded();			
				break;
		}
		jnObj = jnObj.parse(jsonObj);
		return jnObj;
	}

}
