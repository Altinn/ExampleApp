package com.altinn.apps.fisher.ui.screen;
/**
 * This Screen represents list of saved/sent report items
 * If items are saved and user selects such item in that case ReportReceivedFishActivity screen brings in-front of user for editing and sending again.
 * If Sent item is selected in that send related information were shown by 'ReportSendDetialsActivity'
 * 
 */
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.altinn.apps.fisher.R;
import com.altinn.apps.fisher.db.DataBaseHelper;
import com.altinn.apps.fisher.db.FormDBHelper;
import com.altinn.apps.fisher.models.InfoData;
import com.altinn.apps.fisher.models.ReportInfoData;
import com.altinn.apps.fisher.utils.Utils;
import com.altinn.apps.fisher.utils.Utils.Log;

public class ReportActivity extends BaseActivity  {
	private final String TAG = "ReportActivity";
	private ListView mListViewReports;
	private ReportAdapter mReportsAdapter;
	private RrefreshListTask mRefreshListTask;
	private ArrayList<InfoData> mReceivedListDraft;
	private ArrayList<InfoData> mReceivedListSent;
	private EditText mSearchInputView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		mLayOutId = R.layout.reports;
		super.onCreate(savedInstanceState);
		init();
	}
	
	private void init(){
		setTitle(R.string.context_menu_myreports, R.drawable.navigation_back);
		mSearchInputView = (EditText)findViewById(R.id.search_input);
		mListViewReports = (ListView)findViewById(R.id.list_reports);
		mListViewReports.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
				ReportItem reportItem = mReportsAdapter.getItem(position);
				Log.d(TAG, "VIEW@@@@@@@@@@@: "+reportItem.id);
				Bundle bundle = new Bundle();
				//bundle.putBoolean("VIEW_MODE", mViewMode);
				bundle.putInt("FORM_ID", (int)reportItem.id);
				if(reportItem.isDraft)
					startActivity(ReportReceivedFishActivity.class,bundle);
				else{
					Intent intent = new Intent(ReportActivity.this,ReportSendDetailActivity.class);
					intent.putExtras(bundle);
					startActivity(intent);
				}
			}			
		});	
		
		mSearchInputView.addTextChangedListener(new TextWatcher() {			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				startFilterSearch();
			}
		});		
		startFilterSearch();		
	}
		
	private class ReportItem{
		String dateStr;
		String timeStr;
		int itemType;
		boolean isDraft;
		long id;
		String titleStr,subTitleLbl,subTitleValue;
		public ReportItem(InfoData infoData ){
			id = infoData.mId;
			itemType = infoData.mFormType;
			Calendar cal = Calendar.getInstance();			
			if(infoData.mSendId > 0){
				isDraft = false;
				cal.setTimeInMillis(infoData.mSendId);
			}else{
				isDraft = true;
				cal.setTimeInMillis(infoData.mCreateId);
			}
			SimpleDateFormat sdfDate = new SimpleDateFormat("MM.dd.yy");
			SimpleDateFormat sdfTime = new SimpleDateFormat("HH.mm");
			dateStr = sdfDate.format(cal.getTime());
			timeStr = sdfTime.format(cal.getTime());
			String timePreAppend = getString(R.string.lbl_time_short);
			timeStr = timePreAppend +" " +timeStr;
			if(itemType ==  HomeActivity.MENU_REPORT_RECEIVED){
				ReportInfoData rInfo = (ReportInfoData)infoData;
				titleStr = rInfo.mFirmName;
				//subTitleLbl = "MOTTAK AV";
				subTitleValue = rInfo.mFishName;
			}
		}
	}
	
	private class ReportAdapter extends BaseAdapter{
		private ArrayList<ReportItem> mReportList;
		private LayoutInflater mInflater;
		
		public ReportAdapter(ArrayList<ReportItem> menuList){
			mInflater = LayoutInflater.from(ReportActivity.this);
			if(menuList == null)
				mReportList = new ArrayList<ReportItem> ();
			else
				this.mReportList = menuList;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mReportList.size();
		}

		@Override
		public ReportItem getItem(int index) {
			
			return mReportList.get(index);
		}

		@Override
		public long getItemId(int index) {
			// TODO Auto-generated method stub
			return index;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {			
			final ReportItem reportItem = mReportList.get(position);
			ViewHolder holder = null;
			if(convertView == null){
				convertView = mInflater.inflate(R.layout.report_item_new, null);
				holder = new ViewHolder();
				holder.lbl_time = (TextView)convertView.findViewById(R.id.time_item);
				holder.lbl_date = (TextView)convertView.findViewById(R.id.date_item);
				holder.lbl_subtitle = (TextView)convertView.findViewById(R.id.label_item);
				holder.lbl_subtitle_value = (TextView)convertView.findViewById(R.id.label_value);
				holder.lbl_title = (TextView)convertView.findViewById(R.id.title_item);
				holder.indicator = (ImageView)convertView.findViewById(R.id.send_icon);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder)convertView.getTag();
			}
			
			holder.lbl_date.setText(reportItem.dateStr);
			holder.lbl_time.setText(reportItem.timeStr);
			holder.lbl_title.setText(reportItem.titleStr);
			//holder.lbl_subtitle.setText(reportItem.subTitleLbl);
			holder.lbl_subtitle_value.setText(reportItem.subTitleValue);
			if(reportItem.isDraft){
				holder.indicator.setImageResource(R.drawable.content_save_b);
			}else{
				holder.indicator.setImageResource(R.drawable.social_send_now_b);
			}			
			return convertView;
		}		
	}
	
	private static class ViewHolder{
		TextView lbl_date, lbl_time, lbl_title, lbl_subtitle,lbl_subtitle_value;
		ImageView indicator;
	}
	private void startFilterSearch(){	
		if(mRefreshListTask != null){
			mRefreshListTask.cancel(true);
		}		
		mRefreshListTask = new RrefreshListTask();
		mRefreshListTask.execute(mSearchInputView.getText().toString());		
	}
	
	private class RrefreshListTask extends AsyncTask{

		@Override
		protected Object doInBackground(Object... arg0) {
			String searchText = (String)arg0[0];
			FormDBHelper formDbHelper = (FormDBHelper)DataBaseHelper.getInstance().getDBHelper(DataBaseHelper.TABLE_HEPLER_FORM);
			if(mReceivedListDraft == null)
				mReceivedListDraft =  formDbHelper.getFormList(HomeActivity.MENU_REPORT_RECEIVED,true);
			if(mReceivedListSent == null)
				mReceivedListSent =  formDbHelper.getFormList(HomeActivity.MENU_REPORT_RECEIVED,false);
			
			
			ArrayList<ReportItem> listAll = new ArrayList<ReportItem>();
			
			boolean isFilterEnabled = false;
			if(!Utils.isNullorEmpty(searchText) && searchText.trim().length() > 0){
				isFilterEnabled = true;
				searchText = searchText.toLowerCase();
			}
			
			if(mReceivedListDraft != null){
				for(InfoData infodata:mReceivedListDraft){
					ReportItem reportItem = new ReportItem(infodata);
					if(!isFilterEnabled)
						listAll.add(reportItem);
					else{
						String searchInText = (reportItem.titleStr + " "+reportItem.subTitleValue ).toLowerCase();
						if(searchInText.indexOf(searchText) != -1){
							listAll.add(reportItem);
						}
					}
				}
			}
			if(mReceivedListSent != null){
				for(InfoData infodata:mReceivedListSent){
					ReportItem reportItem = new ReportItem(infodata);
					if(!isFilterEnabled)
						listAll.add(reportItem);
					else{
						String searchInText = (reportItem.titleStr + " "+reportItem.subTitleValue).toLowerCase();
						if(searchInText.indexOf(searchText) != -1){
							listAll.add(reportItem);
						}
					}
				}
			}
			
			mReportsAdapter = new ReportAdapter(listAll);
			return null;
		}
		
		@Override
		protected void onPostExecute(Object obj) {
			mListViewReports.setAdapter(mReportsAdapter);
		}
		
	}

	

	
}
