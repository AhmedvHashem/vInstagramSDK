package com.vivantor.vinstagramsdk;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by AhmedNTS on 2016-09-20.
 */
public class InstagramAccessToken
{
	private static final String SHARED = "Instagram_Preferences";
	private static final String API_ACCESS_TOKEN = "access_token";

	private static SharedPreferences sharedPref;

	InstagramAccessToken()
	{
		Utils.notNull(InstagramSDK.applicationContext, "applicationContext");

		sharedPref = InstagramSDK.applicationContext.getSharedPreferences(SHARED, Context.MODE_PRIVATE);
	}

	public static void setAccessToken(String accessToken)
	{
		if (sharedPref != null)
			sharedPref.edit().putString(API_ACCESS_TOKEN, accessToken).apply();
	}

	public static String getAccessToken()
	{
		if (sharedPref != null)
			return sharedPref.getString(API_ACCESS_TOKEN, null);

		return null;
	}

	public static void clearAccessToken()
	{
		if (sharedPref != null)
			sharedPref.edit().clear().apply();
	}
}
