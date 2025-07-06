package org.apache.cordova.plugin;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.engine.SystemWebView;
import org.json.JSONArray;
import org.json.JSONException;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.util.Log;

/**
 * * This class provides methods for AndroidTV.
 */
public class AndroidTV extends CordovaPlugin {

	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		if (action.equals("getStringExtra")) {
			String message = args.getString(0);
			this.getActivityStringExtra(message, callbackContext);
			return true;
		} else if (action.equals("fitToScreen")) {
			this.fitToScreen(callbackContext);
			return true;
		} else if (action.equals("setSize")) {
			int width = args.getInt(0);
			int height = args.getInt(1);
			this.setSize(width, height, callbackContext);
			return true;
		}
		return false;
	}

	private void setSize(int width, int height, CallbackContext callbackContext) {
		final Double val = 1080.0 / height * 100;
		final SystemWebView webView = (SystemWebView) super.webView.getEngine().getView();

		webView.post(new Runnable() {
			@Override
			public void run() {
				webView.setPadding(0, 0, 0, 0);
				webView.setInitialScale(val.intValue());
			}
		});
		callbackContext.success();
	}

	private void fitToScreen(CallbackContext callbackContext) {
		final SystemWebView webView = (SystemWebView) super.webView.getEngine().getView();

		webView.post(new Runnable() {
			@Override
			public void run() {
				webView.getSettings().setLoadWithOverviewMode(true);
				webView.getSettings().setUseWideViewPort(true);
			}
		});
		callbackContext.success();
	}

	private void getActivityStringExtra(String extraString, CallbackContext callbackContext) {
		Intent intent = this.cordova.getActivity().getIntent();
		Log.d("AndroidTVPlugin", "try to get string extra: " + extraString);
		try {
			callbackContext.success(intent.getStringExtra(extraString));
		} catch (ActivityNotFoundException e) {
			callbackContext.error("Failed to get stringExtra");
		}
	}

}
