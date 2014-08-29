package com.altinn.apps.fisher.ui.screen;
/**
 * This activity user can invoke from Overflow menu action.
 */
import android.os.Bundle;

import com.altinn.apps.fisher.R;

public class InformationActivity extends BaseActivity {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		mLayOutId = R.layout.information;
		super.onCreate(savedInstanceState);
		init();
	}
	
	private void init(){		
		setTitle(R.string.context_menu_information,R.drawable.navigation_back);
	}	
	
}
