package com.vivantor.vinstagramsdk;

import android.content.Context;
import android.util.Log;

import com.vivantor.vinstagramsdk.callbacks.InstagramDialogListener;
import com.vivantor.vinstagramsdk.callbacks.InstagramSDKListener;

/**
 * Created by AhmedNTS on 2016-09-20.
 */
@SuppressWarnings("all")
public class InstagramSDK
{
	private static final String TAG = "InstagramSDK";

	private static final String AUTH_URL = "https://api.instagram.com/oauth/authorize/";

	public static Context applicationContext;
	private static String clientId;
	public static String callbackUrl;

	public static void initializeSDK(Context applicationContext, String clientId, String callbackUrl)
	{
		Utils.notNull(applicationContext, "applicationContext");

		Utils.notNullOrEmpty(clientId, "clientId");
		Utils.notNullOrEmpty(callbackUrl, "callbackUrl");

		InstagramSDK.applicationContext = applicationContext.getApplicationContext();

		InstagramSDK.clientId = clientId;
		InstagramSDK.callbackUrl = callbackUrl;

		InstagramAccessToken session = new InstagramAccessToken();
	}

	public static void login(Context context, final InstagramSDKListener listener)
	{
		String url = AUTH_URL + "?client_id=" + clientId + "&redirect_uri=" + callbackUrl + "&response_type=token&scope=basic&follower_list";

		if (InstagramAccessToken.getAccessToken() != null)
		{
			if (listener != null)
				listener.onSuccess();
			return;
		}

		InstagramDialog instagramDialog = new InstagramDialog(context, url, new InstagramDialogListener()
		{
			@Override
			public void onComplete(String accessToken)
			{
				Log.i(TAG, accessToken);
				InstagramAccessToken.setAccessToken(accessToken);

				if (listener != null)
					listener.onSuccess();
			}

			@Override
			public void onError(String error)
			{
				if (listener != null)
					listener.onFail(error);
			}
		});

		if (!instagramDialog.isShowing())
			instagramDialog.show();
	}

	public static void logout()
	{
		InstagramAccessToken.clearAccessToken();
	}
}
