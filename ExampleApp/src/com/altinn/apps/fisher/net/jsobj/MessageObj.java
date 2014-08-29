package com.altinn.apps.fisher.net.jsobj;
/**
 * A JSONObject  for "messages" 
 * Created into Java Object 'MessageObj' to use their fields directly
 */

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MessageObj implements JsonObj {
	
	
	public String mMessageLink;
	public String mSubject;
	public String mStatus;
	public String mLastChangedDateTime;
	public String mLastChangedBy;
	public String mServiceOwner;
	public String mServiceCode;
	public int mServiceEdition;
	public String mSummary;
	public String mBody;
	public String mType;
	public String mMessageSender;
	private LinkObj mLink;
	public ArrayList<AttachmentObj> mAttachmentList;
	public ArrayList<FormObj> mFormList;	
	
	public MessageObj(){
		mFormList = new ArrayList<FormObj>();
		mAttachmentList = new ArrayList<AttachmentObj>();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.altinn.apps.fisher.net.jsobj.JsonObj#parse(org.json.JSONObject)
	 */
	public MessageObj parse(JSONObject jsObj){
		MessageObj obj = new MessageObj();
		try{
			if(jsObj.has("MessageLink"))
				obj.mMessageLink = jsObj.getString("MessageLink");
			if(jsObj.has("Subject"))
				obj.mSubject = jsObj.getString("Subject");
			if(jsObj.has("Status"))
				obj.mStatus = jsObj.getString("Status");
			if(jsObj.has("LastChangedDateTime"))
				obj.mLastChangedDateTime = jsObj.getString("LastChangedDateTime");
			if(jsObj.has("LastChangedBy"))
				obj.mLastChangedBy = jsObj.getString("LastChangedBy");
			if(jsObj.has("ServiceOwner"))
				obj.mServiceOwner = jsObj.getString("ServiceOwner");
			if(jsObj.has("ServiceCode"))
				obj.mServiceCode = jsObj.getString("ServiceCode");
			if(jsObj.has("ServiceEdition"))
				obj.mServiceEdition = jsObj.getInt("ServiceEdition");
			if(jsObj.has("Summary"))
				obj.mSummary = jsObj.getString("Summary");
			if(jsObj.has("Body"))
				obj.mBody = jsObj.getString("Body");
			if(jsObj.has("Type"))
				obj.mType = jsObj.getString("Type");
			if(jsObj.has("MessageSender"))
				obj.mMessageSender = jsObj.getString("MessageSender");
			if(jsObj.has("_links")){
				JSONObject jsLinkObj = jsObj.getJSONObject("_links");
				LinkObj linkObj = new LinkObj();
				linkObj = linkObj.parse(jsLinkObj);
				if(linkObj != null)
					obj.mLink = linkObj;
			}
			if(jsObj.has("Attachments")){				
				JSONArray jsAttachmentArray = jsObj.getJSONArray("Attachments");
				for(int i = 0 ; i <jsAttachmentArray.length();i++){
					JSONObject jsAttachment = jsAttachmentArray.getJSONObject(i);
					if(jsAttachment != null){
						AttachmentObj attachmentObj = new AttachmentObj();
						attachmentObj = attachmentObj.parse(jsAttachment);
						if(attachmentObj != null){
							obj.mAttachmentList.add(attachmentObj);
						}
					}					
				}				
			}
			if(jsObj.has("_embedded")){//Added on 5/5/2014
				JSONObject jsEmbObj =  (JSONObject)jsObj.get("_embedded");//Added on 5/5/2014
				if(jsEmbObj.has("Forms")){//edited	on 5/5/2014		jsObj.has("Forms")	
					JSONArray jsFormArray = jsEmbObj.getJSONArray("Forms");//edited	on 5/5/2014		jsObj.getJSONArray("Forms")	
					for(int i = 0 ; i <jsFormArray.length();i++){
						JSONObject jsForm = jsFormArray.getJSONObject(i);
						if(jsForm != null){
							FormObj formObj = new FormObj();
							formObj = formObj.parse(jsForm);
							if(formObj != null){
								obj.mFormList.add(formObj);
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
	 * its a warper function for, which will support String type input.
	 */
	public MessageObj parse(String jsStrings) throws JSONException {
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
			if(mMessageLink != null)
				jsObj.put("MessageLink", mMessageLink);
			if(mSubject != null)
				jsObj.put("Subject", mSubject);
			if(mStatus != null)
				jsObj.put("Status", mStatus);
			if(mLastChangedDateTime != null)
				jsObj.put("LastChangedDateTime", mLastChangedDateTime);
			if(mServiceOwner != null)
				jsObj.put("ServiceOwner", mServiceOwner);
			if(mServiceCode != null)
				jsObj.put("ServiceCode", mServiceCode);
			if(mServiceEdition != 0)
				jsObj.put("ServiceEdition", mServiceEdition);
			if(mSummary != null)
				jsObj.put("Summary", mSummary);
			if(mBody != null)
				jsObj.put("Body", mBody);
			if(mType != null)
				jsObj.put("Type", mType);
			if(mMessageSender != null)
				jsObj.put("MessageSender", mMessageSender);
			if(mLastChangedBy != null)
				jsObj.put("LastChangedBy", mLastChangedBy);
			if(mLink != null && mLink.mLinkList != null && mLink.mLinkList.size()>0){
				JSONObject jsLinkObj = new JSONObject();
				for(int i = 0 ; i <mLink.mLinkList.size();i++ ){
					LinkItemObj lItemObj = mLink.mLinkList.get(i);
					String itemType = null;
					if(lItemObj.mLinkType == LinkItemObj.LINK_TYPE_SELF){
						itemType = "self";
					}else if(lItemObj.mLinkType == LinkItemObj.LINK_TYPE_PRINT){
						itemType = "print";
					}else if(lItemObj.mLinkType == LinkItemObj.LINK_TYPE_ATTACHMENT){
						itemType = "attachment";
					}else if(lItemObj.mLinkType == LinkItemObj.LINK_TYPE_MESSAGE){
						itemType = "message";
					}					
					if(itemType != null){
						JSONObject jsLinkItemObj = lItemObj.createJson();
						if(jsLinkItemObj != null){
							jsLinkObj.put(itemType, jsLinkItemObj);
						}
					}
				}				
				jsObj.put("_links", jsLinkObj);
			}
			if(mAttachmentList != null && mAttachmentList.size()>0){//Edited on 5/5/2014 : AND condition added
				JSONArray jsAttachmentArray = new JSONArray();
				for(int i = 0 ; i <mAttachmentList.size();i++ ){
					AttachmentObj attachmentObj = mAttachmentList.get(i);				
					JSONObject jsAttachmentObj = attachmentObj.createJson();
					if(jsAttachmentObj != null){
						jsAttachmentArray.put(jsAttachmentObj);
					}
				}
				jsObj.put("Attachments", jsAttachmentArray);
			}
			
			JSONArray jsFormArray = null;
			if(mFormList != null && mFormList.size()>0){
				jsFormArray = new JSONArray();
				for(int i = 0 ; i <mFormList.size();i++ ){
					FormObj formObj = mFormList.get(i);				
					JSONObject jsFormObj = formObj.createJson();
					if(jsFormObj != null){
						jsFormArray.put(jsFormObj);
					}
				}
				JSONObject jsEmbedded = new JSONObject();//Added on 5/5/2014
				jsObj.put("_embedded", jsEmbedded);//Added on 5/5/2014
				jsEmbedded.put("Forms", jsFormArray);//Added on 5/5/2014				
				//jsObj.put("Forms", jsFormArray);//commented on 5/5/2014
			}
			
		} catch (JSONException e) {
			jsObj = null;
			e.printStackTrace();
		}
		
		return jsObj;
	}

}
