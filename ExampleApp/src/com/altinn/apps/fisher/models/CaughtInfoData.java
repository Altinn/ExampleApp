package com.altinn.apps.fisher.models;
/**
 * Currently not in Use.
 * A class for future use
 * 
 */
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class CaughtInfoData extends InfoData {
	
	
	public CaughtInfoData(){
		init();
	}
	public CaughtInfoData(byte[] data){
		init(data);
	}
	
	private void init(){
	
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
	
	public  void setTempVlaues(){	
		mCreateId = System.currentTimeMillis();			
	}
	
	

}
