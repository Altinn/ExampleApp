package com.altinn.apps.fisher.models;
/**
 * This is model class which holds the fields associated in [ReportReceivedFishActivity]
 * 
 */
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.altinn.apps.fisher.AppContext;
import com.altinn.apps.fisher.net.jsobj.FormObj;
import com.altinn.apps.fisher.net.jsobj.MessageObj;
import com.altinn.apps.fisher.ui.screen.HomeActivity;



public class ReportInfoData extends InfoData {
	
	public final byte DEFAULT_BYTE=-1;
	public String mUnitName = "";
	public String mFirmName = "";
	public int mFirmNameIndex = 0;
	public String mRegsNumber = "";
	//public int mRegsNumberIndex = 0;
	public String mShipName = "";
	public String mFishName = "";
	public int mFishTypeIndex = 0;
	public String mDateStr = "";
	public String mTimeStr = "";
	public ReportInfoData(){
		init();
	}
	public ReportInfoData(byte[] data){
		init(data);
	}
	
	private void init(){		
		mFormType = HomeActivity.MENU_REPORT_RECEIVED;
	}
		
	public byte[] getBytes(){
		ByteArrayOutputStream bos = null;
		DataOutputStream dos = null;
		try {
			bos = new ByteArrayOutputStream();
			dos = new DataOutputStream(bos);
			
			dos.writeInt(mId);
			dos.writeInt(mFormType);
			dos.writeLong(mCreateId);
			dos.writeLong(mSendId);
			dos.writeUTF(mUnitName);
			dos.writeUTF(mFirmName);
			dos.writeInt(mFirmNameIndex);
			dos.writeUTF(mRegsNumber);
			//dos.writeInt(mRegsNumberIndex);
			dos.writeUTF(mShipName);
			dos.writeUTF(mFishName);
			dos.writeInt(mFishTypeIndex);
			dos.writeUTF(mDateStr);
			dos.writeUTF(mTimeStr);
			
			dos.flush();
			bos.flush();
			
			return bos.toByteArray();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			if(bos != null){
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(dos != null){
				try {
					dos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return null;
	}
	
	private void init(byte[] data){
		init();
		ByteArrayInputStream bis = null;
		DataInputStream dis = null;
		try {
			bis = new ByteArrayInputStream(data);
			dis = new DataInputStream(bis);
			mId = dis.readInt();
			mFormType = dis.readInt();			
			mCreateId = dis.readLong();
			mSendId = dis.readLong();
			mUnitName = dis.readUTF();
			mFirmName = dis.readUTF();
			mFirmNameIndex = dis.readInt();		
			mRegsNumber =dis.readUTF();
			//mRegsNumberIndex = dis.readInt();		
			mShipName =dis.readUTF();
			mFishName =dis.readUTF();
			mFishTypeIndex = dis.readInt();		
			mDateStr =dis.readUTF();
			mTimeStr =dis.readUTF();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			if(bis != null){
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(dis != null){
				try {
					dis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	public String createJSONPost(){
		String result = "";
		
		MessageObj jsMsgPost = new MessageObj();
		jsMsgPost.mType = "FormTask";
		jsMsgPost.mServiceCode="3689";//3682
		jsMsgPost.mServiceEdition=131113;//1
		
		FormObj formObj1 = new FormObj();
		formObj1.mType = "MainForm";
		formObj1.mDataFormatId="4302";
		formObj1.mDataFormatVersion="36777";//36562
		formObj1.mFormData="%1$s";// Here I can not directly put xml content, because at the time of conversion of 
		//Json-object to string, it inserts un-necessary SLASH's which will make json into unacceptable format.
		//So we replacing xml content at the end
		jsMsgPost.mFormList.add(formObj1);
		
		result = jsMsgPost.createJson().toString();
		result = String.format(result, getXMLData(formObj1));
		
		return result;
	}
	
	private String getXMLData(FormObj formObj){
		String result = null;
		try {
			InputStream is = AppContext.getInstance().getAssets().open("xml/postmsg.xml");
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int ch = -1;
			while( (ch = is.read(buffer)) != -1){
				bos.write(buffer,0,ch);
			}
			bos.flush();
			result = new String(bos.toByteArray(),"UTF-8");
			result = String.format(result, formObj.mDataFormatId,formObj.mDataFormatVersion,mFirmName,mRegsNumber,mShipName,
									mFishName.toLowerCase()	/*similar to xsd-schema, we can not send user friendly text*/
									,dateFormatedField(FORMAT_TYPE_DATE_TIME)/*,dateFormatedField(SYSTEM_FORMAT_TYPE_DATE)*/
									);
			bos.close();
			is.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
		
	}
	
	private final int SYSTEM_FORMAT_TYPE_DATE = 4;//2013-11-23
	private final int FORMAT_TYPE_DATE = 0;//2013-11-23
	private final int FORMAT_TYPE_TIME = 1;//21:56:45
	private final int FORMAT_TYPE_DATE_TIME = 2;//2013-11-23T21:56:45
	private  String dateFormatedField(int type){		
		String result = "";
		switch(type){
		case SYSTEM_FORMAT_TYPE_DATE:
			SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd");
			Date date = Calendar.getInstance().getTime();
			if(date != null)
				result = sdf.format(date);
			break;
			case FORMAT_TYPE_DATE:
				sdf =  new SimpleDateFormat("yyyy-MM-dd");
				date = getDate();
				if(date != null)
					result = sdf.format(date);
				break;
			case FORMAT_TYPE_TIME:
				sdf =  new SimpleDateFormat("HH:mm:ss");
				date = getDate();
				if(date != null)
					result = sdf.format(date);
				break;
			case FORMAT_TYPE_DATE_TIME:
				result = dateFormatedField(FORMAT_TYPE_DATE) +"T"+dateFormatedField(FORMAT_TYPE_TIME);
				break;
		}
		return result;
	}
	
	private Date getDate(){
		Date date = null;		
		if(mDateStr.length()>0 && mTimeStr.length()>0){
			SimpleDateFormat sdf =  new SimpleDateFormat("dd.MM.yyyy HH.mm");
			try {
				 date = sdf.parse(mDateStr + " "+mTimeStr);			
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		return date;
	}

}
