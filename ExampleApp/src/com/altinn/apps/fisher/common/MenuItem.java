package com.altinn.apps.fisher.common;
/**
 * This is a model class which will hold menu of HomeActivity
 *
 */
public class MenuItem {
	public static final byte MENU_TYPE_CONTEXT = 1;
	public static final byte MENU_TYPE_SCREEN = 2;
	public static final byte MENU_TYPE_CONTEXT_OPTION = 3;
	
	private String mMenuTitle;
	private String mMenuSubTitle;
	private byte mMenuType;
	private int mMenuId;
	private int mImgResId;
	
	public MenuItem(byte menuType,String title, String subTitle){
		mMenuType = menuType;
		mMenuTitle = title;
		mMenuSubTitle = subTitle;
	}
	
	public MenuItem(int menuId, byte menuType,String title, String subTitle){
		this(menuType,title,subTitle);
		this.mMenuId = menuId;
		
	}
	public MenuItem(){		
	}
	
	public String getMenuTitle() {
		return mMenuTitle;
	}
	public void setMenuTitle(String mMenuTitle) {
		this.mMenuTitle = mMenuTitle;
	}
	public String getMenuSubTitle() {
		return mMenuSubTitle;
	}
	public void setMenuSubTitle(String mMenuSubTitle) {
		this.mMenuSubTitle = mMenuSubTitle;
	}
	public byte getMenuType() {
		return mMenuType;
	}
	public void setMenuType(byte mMenuType) {
		this.mMenuType = mMenuType;
	}
	public int getMenuId() {
		return mMenuId;
	}
	public void setMenuId(int mMenuId) {
		this.mMenuId = mMenuId;
	}

	public int getImgResId() {
		return mImgResId;
	}

	public void setImgResId(int mImgResId) {
		this.mImgResId = mImgResId;
	}

}
