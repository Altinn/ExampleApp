package com.altinn.apps.fisher.net.jsobj;
/**
 * This is parent interface for all kind of supported JsonObj (eg FormObj, MessageObj etc)
 * This class can create JsonObj from JSONObject
 * JsonObj has fields and list of JsonObj's which can be directly used there is no need to iterate JSONObject again and again
 * JsonObj class has capability to create JSONObject from their existing fields.
 * 
 */
import org.json.JSONObject;

public interface JsonObj {

	/**
	 * From JSONObject this method construct JsonObj
	 * @param JSONObject
	 * @return JsonObj
	 */
	JsonObj parse(JSONObject jsonObj);
	
	/**
	 * From this method fields of this JsonObj(Implementer's) can convert into JSONObject,
	 * which can be used in network transaction.
	 * @return
	 */
	JSONObject createJson();
	
}
