package com.altinn.apps.fisher.ui.screen;
/**
 * This is parent class where slider menu is needed.
 * To achieve slider menu functionality a library project(menudrawer) is used
 * This class offers any action/UI on title-bar/menu-view and delegate the action to the child class.
 */
import java.util.ArrayList;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;
import android.app.ActionBar;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.altinn.apps.fisher.AppContext;
import com.altinn.apps.fisher.R;
import com.altinn.apps.fisher.common.MenuItem;
import com.altinn.apps.fisher.utils.Utils.Log;

public class MenuNavigationActivity extends Activity {
	private static final String STATE_MENUDRAWER = "net.simonvt.menudrawer.samples.WindowSample.menuDrawer";
	private static final String STATE_ACTIVE_VIEW_ID = "net.simonvt.menudrawer.samples.WindowSample.activeViewId";
	protected int mLayOutId;
	private MenuDrawer mMenuDrawer;
	protected boolean mExcludeMenuNavigation;//Making this flag true slider menu wont come
	// private TextView mContentTextView;

	private final String TAG = "MenuNavigationActivity";
	public static final int CONTEXT_MENU_MY_REPORT = 200;
	public static final int CONTEXT_MENU_MY_PROFILE= 201;
	public static final int CONTEXT_MENU_INFORMATION = 202;
	//public static final int CONTEXT_MENU_ID_ITEM4 = 203;
	private ListView mMenuList;
	private ContextMenuAdapter mMenuAdapter;

	@Override
	protected void onCreate(Bundle inState) {
		super.onCreate(inState);
		if (!mExcludeMenuNavigation) {
			mMenuDrawer = MenuDrawer.attach(this, MenuDrawer.Type.BEHIND,
					Position.RIGHT, MenuDrawer.MENU_DRAG_WINDOW);			
			mMenuDrawer.setContentView(mLayOutId);
			mMenuDrawer.setMenuView(R.layout.menu_scrollview);
			
			mMenuDrawer.setMenuSize(AppContext.getInstance().getMenuWidth());
			initContextMenu();
		} else {
			setContentView(mLayOutId);
		}
	}

	@Override
	public boolean onOptionsItemSelected(android.view.MenuItem item) {
		if (mExcludeMenuNavigation) {
			return super.onOptionsItemSelected(item);
		} else {
			switch (item.getItemId()) {
			case android.R.id.home:
				mMenuDrawer.toggleMenu();
				return true;
			}
			return super.onOptionsItemSelected(item);
		}
	}
/**
 * on back if menu is open then it will be closed and user remains in the same screen
 * if menu is not opened in that case on-back press user will be switched to the previous screen
 */
	@Override
	public void onBackPressed() {
		if (!mExcludeMenuNavigation) {
			final int drawerState = mMenuDrawer.getDrawerState();
			if (drawerState == MenuDrawer.STATE_OPEN
					|| drawerState == MenuDrawer.STATE_OPENING) {
				mMenuDrawer.closeMenu();
				return;
			}else {
				super.onBackPressed();
			}
		} else {
			super.onBackPressed();
		}
	}

	
	/**
	 * Screen title (left icon, heading, right icon) has been changed by this method
	 * @param title
	 */
	public void setTitle(String title) {
		setTitle(title,-1);
	}
	
	
	/**
	 * Screen title (left icon, heading, right icon) has been changed by this method
	 * @param titleid
	 */
	public void setTitle(int titleid) {
		setTitle(getResources().getString(titleid),-1);
	}
	
	/**
	 * Screen title (left icon, heading, right icon) has been changed by this method
	 * @param titleid
	 * @param leftDrawable
	 */
	public void setTitle(int titleid, int leftDrawable) {
		setTitle(getResources().getString(titleid),leftDrawable);
	}

	/**
	 * Screen title (left icon, heading, right icon) has been changed by this method
	 * @param title
	 * @param leftDrawable
	 */
	public void setTitle(String title,  int leftDrawable) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) 		
		{
			View parentView = LayoutInflater.from(this).inflate(R.layout.title, null);
			ColorDrawable cd = new ColorDrawable(getResources().getColor(R.color.color_light_green));
			getActionBar().setBackgroundDrawable(cd);
			getActionBar().setCustomView(parentView);
			getActionBar().setDisplayOptions(ActionBar.DISPLAY_USE_LOGO|ActionBar.DISPLAY_SHOW_CUSTOM);			
			ImageView infoIcon = (ImageView)parentView.findViewById(R.id.info);			
			ImageView menuIcon = (ImageView)parentView.findViewById(R.id.menu);
			TextView titleText = (TextView)parentView.findViewById(R.id.title_txt);
			if(leftDrawable <=0){
				infoIcon.setVisibility(View.GONE);
			}else{
				infoIcon.setImageResource(leftDrawable);
			}
			titleText.setText(title);
			infoIcon.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Log.d("@@@@@@@@@", "INFO ITEM CLICKED");
					onInfoClicked();
				}
			});
			
			menuIcon.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Log.d("@@@@@@@@@", "Menu ITEM CLICKED");
					mMenuDrawer.toggleMenu();
				}
			});
		}
	}

	/**
	 * Action invoked when user select LHS icon
	 * over-ridden by child
	 */
	protected void onInfoClicked() {

	}
	
	/**
	 * Action initiated when user selects any of the slider menu 
	 * over-ridden by child
	 */
	protected void onContextExecute(MenuItem menuItem){
		
	}

	public void initContextMenu() {
		setTitle(R.string.title_home);
		mMenuList = (ListView) findViewById(R.id.menuContainer);
		mMenuAdapter = new ContextMenuAdapter(createMenuItems());
		mMenuList.setAdapter(mMenuAdapter);
		mMenuList.setOnItemClickListener(contextMenuListner);
	}

	private OnItemClickListener contextMenuListner = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> adapter, View v, int position,
				long id) {
			MenuItem menuItem = mMenuAdapter.getItem(position);	
			if (!mExcludeMenuNavigation) {
				final int drawerState = mMenuDrawer.getDrawerState();
				if (drawerState == MenuDrawer.STATE_OPEN
						|| drawerState == MenuDrawer.STATE_OPENING) {
					mMenuDrawer.closeMenu(false);					
				}
			}
			onContextExecute(menuItem);
		}		

	};
	
	


	private ArrayList<MenuItem> createMenuItems() {
		ArrayList<MenuItem> menuList = new ArrayList<MenuItem>();
		Resources res = getResources();
		MenuItem item1 = new MenuItem(CONTEXT_MENU_MY_REPORT,
				MenuItem.MENU_TYPE_CONTEXT_OPTION,
				res.getString(R.string.context_menu_myreports),
				res.getString(R.string.context_submenu_myreports));
		item1.setImgResId(R.drawable.content_save);
		MenuItem item2 = new MenuItem(CONTEXT_MENU_MY_PROFILE,
				MenuItem.MENU_TYPE_CONTEXT_OPTION,
				res.getString(R.string.context_menu_myproflie),
				res.getString(R.string.context_submenu_myprofile));
		item2.setImgResId(R.drawable.social_person);
		MenuItem item3 = new MenuItem(CONTEXT_MENU_INFORMATION,
				MenuItem.MENU_TYPE_CONTEXT_OPTION,
				res.getString(R.string.context_menu_information),
				res.getString(R.string.context_submenu_information));
		item3.setImgResId(R.drawable.action_about);
//		MenuItem item4 = new MenuItem(CONTEXT_MENU_ID_ITEM4,
//				MenuItem.MENU_TYPE_CONTEXT_OPTION,
//				res.getString(R.string.context_menu4),
//				res.getString(R.string.context_submenu4));
//		item4.setImgResId(R.drawable.bw_info);

		menuList.add(item1);
		menuList.add(item2);
		menuList.add(item3);
		//menuList.add(item4);
		return menuList;
	}

	private class ContextMenuAdapter extends BaseAdapter {
		private ArrayList<MenuItem> mMenuList;
		private LayoutInflater mInflater;

		public ContextMenuAdapter(ArrayList<MenuItem> menuList) {
			mInflater = LayoutInflater.from(MenuNavigationActivity.this);
			if (menuList == null)
				mMenuList = new ArrayList<MenuItem>();
			else
				this.mMenuList = menuList;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mMenuList.size();
		}

		@Override
		public MenuItem getItem(int index) {

			return mMenuList.get(index);
		}

		@Override
		public long getItemId(int index) {
			// TODO Auto-generated method stub
			return index;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.context_menu_list_item, null);
			}
			MenuItem menuItem = mMenuList.get(position);

			TextView subTitle = (TextView) convertView
					.findViewById(R.id.subMenuTitle);
			subTitle.setText(menuItem.getMenuSubTitle());

			TextView title = (TextView) convertView
					.findViewById(R.id.menuTitle);
			title.setText(menuItem.getMenuTitle());

			int resId = menuItem.getImgResId();
			if (resId > 0) {
				ImageView lIcon = (ImageView) convertView
						.findViewById(R.id.lIcon);
				lIcon.setImageResource(menuItem.getImgResId());
			}

			return convertView;
		}

	}

}
