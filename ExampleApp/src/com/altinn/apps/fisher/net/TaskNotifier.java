package com.altinn.apps.fisher.net;
/**
 * This is a notifier interface, and result of task will be delegated through this interface.
 *
 */
public interface TaskNotifier {
	
	/**
	 * This method is delegated during execution of a task if get an error or unexpected result,
	 * So using this method we can communicate to the implementer object.
	 * @param message
	 */
	public void onError(int message);

	/**
	 * This method is delegated during execution of a task if get desired result,
	 * So using this method we can communicate to the implementer object.
	 */
	public void onSuccess();
	
	/**
	 * This method is delegated during execution of a task we can notify the implementer about the current progress of the task.
	 */
	public void onProgress();
	
	/**
	 * This method is delegated during execution of a task if get html-content which has login-html-content
	 * Or if we get PeerVerificationException.
	 * 
	 * This is handled in BaseActivity, When user is notified as login expire at that time login expired popup has been shown
	 * and any operation on the popup will  invoke BrowserActivity for login, And once login is done, the same task will be executed again
	 * in which login-expire received, to handle this case [same task execute again] individual screen should put their task execution stuff 
	 * inside 'callwebservice' method.
	 */
	public void onLoginExpire();
}
