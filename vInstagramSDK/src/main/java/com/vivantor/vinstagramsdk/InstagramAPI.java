package com.vivantor.vinstagramsdk;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.vivantor.vinstagramsdk.callbacks.InstagramUserListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by AhmedNTS on 2016-09-21.
 */

public class InstagramAPI
{
	private static final String TAG = "InstagramAPI";

	private static boolean isAccessTokenExpired(JSONObject meta)
	{
//		int code;
		String error_type = "";
		try
		{
//			code = meta.getInt("code");
			error_type = meta.getString("error_type");
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}

		if (error_type.equals("OAuthAccessTokenException"))
			return true;

		return false;
	}

	public static void getUserByID(final InstagramSDK sdk, final String id, final InstagramUserListener listener)
	{
		final String userInfos = InstagramSDK.API_URL + "/users/" + (id == null ? "self" : id) + "/?access_token=" + sdk.getAccessToken();

		AsyncTask asyncTask = new AsyncTask()
		{
			private boolean errorHappened;
			private String error;

			InstagramUser instagramUser;

			@Override
			protected Object doInBackground(Object[] params)
			{
				Log.i(TAG, "Fetching user info");
				try
				{
					Log.d(TAG, "Opening URL " + userInfos);

					URL url = new URL(userInfos);
					HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
					urlConnection.setRequestMethod("GET");
					urlConnection.setDoInput(true);
					urlConnection.connect();

					int responseCode = urlConnection.getResponseCode();
					if (responseCode == HttpURLConnection.HTTP_OK)
					{
						String responseMessage = Utils.streamToString(urlConnection.getInputStream());
						Log.i(TAG, responseMessage);

						JSONObject jsonObj = (JSONObject) new JSONTokener(responseMessage).nextValue();
						if (isAccessTokenExpired(jsonObj.getJSONObject("meta")))
						{
							errorHappened = true;
							error = "AccessTokenExpired";
							sdk.resetInstagramSession();
							return null;
						}

						instagramUser = new InstagramUser();
						instagramUser.setId(jsonObj.getJSONObject("data").getString("id"));
						instagramUser.setProfile_picture(jsonObj.getJSONObject("data").getString("profile_picture"));
						instagramUser.setUsername(jsonObj.getJSONObject("data").getString("username"));
						instagramUser.setFull_name(jsonObj.getJSONObject("data").getString("full_name"));
					}
					else
					{
						errorHappened = true;
						error = "Request Error";
					}
				}
				catch (Exception ex)
				{
					errorHappened = true;
					error = "Exception Error";
					ex.printStackTrace();
				}

				return instagramUser;
			}

			@Override
			protected void onPostExecute(Object o)
			{
				if (!errorHappened)
				{
					if (listener != null)
						listener.onSuccess((InstagramUser) o);
				}
				else
				{
					if (listener != null)
						listener.onFail(error);
				}
			}
		};
		asyncTask.execute();
	}
}
