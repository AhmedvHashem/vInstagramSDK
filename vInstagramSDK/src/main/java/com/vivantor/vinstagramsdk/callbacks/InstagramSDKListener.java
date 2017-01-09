package com.vivantor.vinstagramsdk.callbacks;

/**
 * Created by AhmedNTS on 2016-09-21.
 */

public interface InstagramSDKListener
{
	public abstract void onSuccess();

	public abstract void onFail(String error);
}
