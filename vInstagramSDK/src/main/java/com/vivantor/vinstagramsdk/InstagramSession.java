package com.vivantor.vinstagramsdk;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by AhmedNTS on 2016-09-20.
 */
class InstagramSession
{
	private SharedPreferences sharedPref;
	private SharedPreferences.Editor editor;

	private static final String SHARED = "Instagram_Preferences";

	private static final String API_ACCESS_TOKEN = "access_token";

	private String accessToken;

	InstagramSession(Context context)
	{
		sharedPref = context.getSharedPreferences(SHARED, Context.MODE_PRIVATE);
		editor = sharedPref.edit();
	}

	void setAccessToken(String accessToken)
	{
		this.accessToken = accessToken;

		editor.putString(API_ACCESS_TOKEN, accessToken);
		editor.commit();
	}

	String getAccessToken()
	{
		if (accessToken == null)
			accessToken = sharedPref.getString(API_ACCESS_TOKEN, null);
		return accessToken;
	}

	void resetInstagramSession()
	{
		editor.putString(API_ACCESS_TOKEN, null);
		editor.commit();
	}
}
