package com.altinn.apps.fisher.net;
/**
 * This is a subclass of AsyncTask,
 * Usually this class is inherited by all the tasks, which perform network operation.
 * 
 * 
 */
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;

public abstract class AbstractWorkerTask extends AsyncTask<Object, Integer, JSONObject>{
	
	private TaskNotifier notifier;
	private Context appContext;

	
	public AbstractWorkerTask(Context context, TaskNotifier notifier) {
		this.notifier = notifier;
		this.appContext = context;
	}
	
	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	public TaskNotifier getNotifier() {
		return notifier;
	}

	public void setNotifier(TaskNotifier notifier) {
		this.notifier = notifier;
	}

	public Context getAppContext() {
		return appContext;
	}

	public void setAppContext(Context appContext) {
		this.appContext = appContext;
	}
	
	

}
