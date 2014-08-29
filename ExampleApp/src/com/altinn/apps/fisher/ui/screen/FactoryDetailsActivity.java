package com.altinn.apps.fisher.ui.screen;
/**
 * This screen allows user enter factory details
 * Eg Factory name and number
 * 
 * This screen can be traversed from UserProfileActivity
 * By selecting bottom button (to Add new factory details), or by selecting any list item to edit those.
 * 
 */
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.altinn.apps.fisher.R;
import com.altinn.apps.fisher.db.DataBaseHelper;
import com.altinn.apps.fisher.db.FactoryDBHelper;
import com.altinn.apps.fisher.settings.FactoryDetails;

public class FactoryDetailsActivity extends BaseActivity {

	private EditText mEdtFirmName,mEdtFirmNumber;
	private View mSaveBtn,mDeleteBtn;
	private int index = -1;
	private TextView mBtnLabelDelete;
	private String mfirmName = "", mFirmNumber = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		mLayOutId = R.layout.factory_details;
		super.onCreate(savedInstanceState);
		init();
	}
	
	private void init(){
		setTitle(R.string.lbl_change_receive_info,R.drawable.navigation_back);
		index = getIntent().getIntExtra("ACTION_INDEX", -1);
		
		mEdtFirmName = (EditText)findViewById(R.id.edtFirmName);
		mEdtFirmNumber = (EditText)findViewById(R.id.edtFirmNumber);
		
		mEdtFirmName.addTextChangedListener(new MyTextWatcher());
		mEdtFirmNumber.addTextChangedListener(new MyTextWatcher());
		mBtnLabelDelete = (TextView)findViewById(R.id.textDelete);
		
		mDeleteBtn = findViewById(R.id.btn_delete);		
		mDeleteBtn.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				mfirmName = mEdtFirmName.getText().toString().trim();
				mFirmNumber = mEdtFirmNumber.getText().toString().trim();
				if(mfirmName.length() == 0 && mFirmNumber.length()==0){
					if(index != -1){
						FactoryDBHelper helper = (FactoryDBHelper)DataBaseHelper.getInstance().getDBHelper(DataBaseHelper.TABLE_HEPLER_FACTORY);
						helper.deleteData(index);						
					}
					finish();
				}else{
					mBtnLabelDelete.setText(R.string.lbl_delete);
					mEdtFirmName.setText("");
					mEdtFirmNumber.setText("");
				}
			}
		});
	
		mSaveBtn = findViewById(R.id.btn_save);		
		mSaveBtn.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				FactoryDBHelper helper = (FactoryDBHelper)DataBaseHelper.getInstance().getDBHelper(DataBaseHelper.TABLE_HEPLER_FACTORY);
				FactoryDetails fd = new FactoryDetails();
				fd.setId(index);
				mfirmName = mEdtFirmName.getText().toString().trim();
				mFirmNumber = mEdtFirmNumber.getText().toString().trim();
				if(mfirmName.length() > 0 && mFirmNumber.length()>0){
					fd.setFactoryName(mfirmName);
					fd.setFactoryNumber(mFirmNumber);
					helper.updateData(fd);
					finish();
				}
			}
		});
		updateValues();
		mfirmName = mEdtFirmName.getText().toString().trim();
		mFirmNumber = mEdtFirmNumber.getText().toString().trim();
		if(mfirmName.length() == 0 && mFirmNumber.length()==0){
			mBtnLabelDelete.setText(R.string.lbl_delete);
		}else{
			mBtnLabelDelete.setText(R.string.lbl_clear);
		}
		
	}
	
	private void updateValues(){
		if(index != -1){
			FactoryDBHelper helper = (FactoryDBHelper)DataBaseHelper.getInstance().getDBHelper(DataBaseHelper.TABLE_HEPLER_FACTORY);
			FactoryDetails fd = (FactoryDetails)helper.getItem(index);
			mEdtFirmName.setText(fd.getFactoryName());
			mEdtFirmNumber.setText(fd.getFactoryNumber());
		}
	}
	
	
	
	private class MyTextWatcher implements TextWatcher{

		@Override
		public void afterTextChanged(Editable arg0) {
			
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			
		}

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			mfirmName = mEdtFirmName.getText().toString().trim();
			mFirmNumber = mEdtFirmNumber.getText().toString().trim();
			if(mfirmName.length() == 0 && mFirmNumber.length()==0){
				mBtnLabelDelete.setText(R.string.lbl_delete);
			}else{
				mBtnLabelDelete.setText(R.string.lbl_clear);
			}
			
		}
		
	};
	
}
