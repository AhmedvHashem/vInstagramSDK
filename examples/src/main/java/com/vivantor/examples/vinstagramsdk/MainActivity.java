package com.vivantor.examples.vinstagramsdk;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.vivantor.vinstagramsdk.InstagramAPI;
import com.vivantor.vinstagramsdk.InstagramAccessToken;
import com.vivantor.vinstagramsdk.InstagramSDK;
import com.vivantor.vinstagramsdk.InstagramUser;
import com.vivantor.vinstagramsdk.callbacks.InstagramSDKListener;
import com.vivantor.vinstagramsdk.callbacks.InstagramUserListener;

@SuppressWarnings("all")
public class MainActivity extends AppCompatActivity
{
	private static final String TAG = "MainActivity";

	public static final String CLIENT_ID = "2b87d4030f47425b9230fe7d538c9f49";
	public static final String CALLBACK_URL = "http://cms.mobileznation.com/";

	InstagramSDK instagramSDK;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		InstagramSDK.initializeSDK(getApplicationContext(), CLIENT_ID, CALLBACK_URL);

		InstagramSDK.login(this, new InstagramSDKListener()
		{
			@Override
			public void onSuccess()
			{
				Log.d(TAG, "onSuccess");

				if (InstagramAccessToken.getAccessToken() != null)
					getUserInfo();
			}

			@Override
			public void onFail(String error)
			{
				Log.e(TAG, "onFail" + error);

				Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
			}
		});
	}

	void getUserInfo()
	{
		InstagramAPI.getUserByID(null, new InstagramUserListener()
		{
			@Override
			public void onSuccess(InstagramUser instagramUser)
			{
				Log.d(TAG, "onSuccess " + instagramUser.toString());

				Toast.makeText(MainActivity.this, "InstagramUser ID=" + instagramUser.getId(), Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onFail(String error)
			{
				Log.e(TAG, "onFail " + error);

				Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
			}
		});
	}
}