package com.vivantor.examples.vinstagramsdk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.vivantor.vinstagramsdk.InstagramAPI;
import com.vivantor.vinstagramsdk.InstagramSDK;
import com.vivantor.vinstagramsdk.InstagramUser;
import com.vivantor.vinstagramsdk.callbacks.InstagramSDKListener;
import com.vivantor.vinstagramsdk.callbacks.InstagramUserListener;

@SuppressWarnings("all")
public class MainActivity extends AppCompatActivity
{
	public static final String CLIENT_ID = "074399c189b94f1184f9b88715d3b6c5";
	public static final String CALLBACK_URL = "http://www.mccarabia.com/";

	InstagramSDK instagramSDK;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		instagramSDK = new InstagramSDK(MainActivity.this, CLIENT_ID, CALLBACK_URL);
		instagramSDK.authorize(new InstagramSDKListener()
		{
			@Override
			public void onSuccess()
			{
				getUserInfo();
			}

			@Override
			public void onFail(String error)
			{
				Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
			}
		});
	}

	void getUserInfo()
	{
		InstagramAPI.getUserByID(instagramSDK, null, new InstagramUserListener()
		{
			@Override
			public void onSuccess(InstagramUser instagramUser)
			{
				Toast.makeText(MainActivity.this, "InstagramUser ID=" + instagramUser.getId(), Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onFail(String error)
			{
				Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
			}
		});
	}
}