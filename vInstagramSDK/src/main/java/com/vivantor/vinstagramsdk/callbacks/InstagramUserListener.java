package com.vivantor.vinstagramsdk.callbacks;

import com.vivantor.vinstagramsdk.InstagramUser;

/**
 * Created by AhmedNTS on 2016-09-21.
 */

public interface InstagramUserListener
{
	public abstract void onSuccess(InstagramUser instagramUser);

	public abstract void onFail(String error);
}
