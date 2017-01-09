package com.vivantor.vinstagramsdk;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.vivantor.vinstagramsdk.callbacks.InstagramDialogListener;
import com.vivantor.vinstagramsdk.callbacks.InstagramSDKListener;
import com.vivantor.vinstagramsdk.callbacks.InstagramUserListener;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by AhmedNTS on 2016-09-20.
 */
@SuppressWarnings("all")
public class InstagramSDK
{
	private static final String TAG = "InstagramSDK";

	private Context mContext;

	private InstagramSession mSession;
	private InstagramDialog instagramDialog;

	private InstagramSDKListener mListener;

	private String mAuthUrl;

	private String mClientId;
	public static String mCallbackUrl = "";

	static final String AUTH_URL = "https://api.instagram.com/oauth/authorize/";
	static final String API_URL = "https://api.instagram.com/v1";

	private ProgressDialog mProgress;

	public InstagramSDK(Context context, String clientId, String callbackUrl)
	{
		mContext = context;

		mClientId = clientId;
		mCallbackUrl = callbackUrl;

		mAuthUrl = AUTH_URL + "?client_id=" + clientId + "&redirect_uri=" + mCallbackUrl + "&response_type=token&scope=basic&follower_list";

		mProgress = new ProgressDialog(mContext);
		mProgress.setCancelable(false);
	}

	public void authorize(InstagramSDKListener listener)
	{
		mListener = listener;

		mSession = new InstagramSession(mContext);

		if (getAccessToken() != null)
		{
			mListener.onSuccess();
			return;
		}

		instagramDialog = new InstagramDialog(mContext, mAuthUrl, new InstagramDialogListener()
		{
			@Override
			public void onComplete(String accessToken)
			{
				Log.i(TAG, accessToken);
				mSession.setAccessToken(accessToken);

				mListener.onSuccess();
			}

			@Override
			public void onError(String error)
			{
				mListener.onFail(error);
			}
		});

		if (!instagramDialog.isShowing())
			instagramDialog.show();
	}

	public String getAccessToken()
	{
		return mSession.getAccessToken();
	}

	public void resetInstagramSession()
	{
		if (mSession != null)
			mSession.resetInstagramSession();
	}
}
