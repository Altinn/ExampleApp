package com.altinn.apps.fisher.ui.screen;
/**
 * This is a browser activity used to initiate a login process
 * On success full login, A wait screen will be displayed to the user indicating he will be 
 * redirected to the application soon and notified to the caller activity by invoking onActivityResult.
 */
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.altinn.apps.fisher.R;
import com.altinn.apps.fisher.common.AppConstants;
import com.altinn.apps.fisher.net.CookieHelper;


public class BrowserActivity extends Activity {

	private WebView webView;
	private CWebViewClient webviewClient;
	private int resultFoundCount=0;
	private TextView mTextView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//mExcludeMenuNavigation = true;
		//mLayOutId = R.layout.browser;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.browser);
		init();
	}
	
	private void init(){		
		//setTitle(R.string.context_menu_information,R.drawable.navigation_back);
		webView = (WebView)findViewById(R.id.myWebView);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setPluginsEnabled(true);
		webviewClient = new CWebViewClient();
		webView.setWebViewClient(webviewClient);
		webView.loadUrl(AppConstants.LOGIN_WEBSERVICE_URL);
		
		mTextView =  (TextView)findViewById(R.id.redirectText);
		mTextView.setVisibility(View.GONE);
		webView.setVisibility(View.VISIBLE);
		
		
	}
	
	private class CWebViewClient extends WebViewClient {
		@Override
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			Toast.makeText(BrowserActivity.this, "Oh no! " + description, Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {	
			 System.out.println("------------- on shouldOverrideUrlLoading---"+url);
			 if( !url.startsWith(AppConstants.LOGIN_WEBSERVICE_URL)){
					resultFoundCount = 0;
					webView.setVisibility(View.VISIBLE);
					mTextView.setVisibility(View.GONE);
				}
			 super.onPageStarted(view, url, favicon);
		}
		
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {	
			view.loadUrl(url);			
			return true;
		}
		public void onPageFinished(WebView view, String url) {	
			System.out.println("------------- on new onPageFinished:URL---"+url);
			String cookieStr = null;
			if( url.startsWith(AppConstants.LOGIN_WEBSERVICE_URL)){
				resultFoundCount++;
				cookieStr = CookieManager.getInstance().getCookie(url);
				CookieHelper.getInstance().updateCookie(cookieStr,true);
				System.out.println("------------- on new onPageFinished:Cookie---"+cookieStr);
			}
			if(cookieStr != null && cookieStr.contains(".ASPXAUTH") && resultFoundCount==1){
				mTextView.setVisibility(View.VISIBLE);
				webView.setVisibility(View.GONE);
				resultHandler.sendEmptyMessageDelayed(1, 5*1000);		
				//Here delayed send opted because some time, onpageFinished called and with login callback url then and
				//After few seconds onPageStart will be called, in that case we are waiting for 5 seconds 
			}
		}
	}
	
	@Override
	public void onBackPressed() {
		// if web view has history, then
		if(webView != null && webView.getVisibility() == View.VISIBLE && webView.canGoBack()){
			webView.goBack();
		} else{
			confirmBack();
		}
	}
	
	private void confirmBack(){
		Dialog d = new AlertDialog.Builder(this)
				.setMessage((R.string.dialog_skip_login))
				.setPositiveButton(R.string.dialog_yes,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								dialog.dismiss();								
								finish();
							}
						})
				.setNegativeButton(R.string.dialog_no,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								dialog.dismiss();
								resultFoundCount = 0;
								webView.loadUrl(AppConstants.LOGIN_WEBSERVICE_URL);
							}
						}).create();
		d.show();
	}
	
	private Handler resultHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if(resultFoundCount > 0){
				Intent returnIntent = new Intent();
				setResult(RESULT_OK, returnIntent); 
				finish();
			}
		}
	};

}
