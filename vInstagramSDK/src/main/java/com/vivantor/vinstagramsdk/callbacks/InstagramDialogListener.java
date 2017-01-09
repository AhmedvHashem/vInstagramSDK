package com.vivantor.vinstagramsdk.callbacks;

/**
 * Created by AhmedNTS on 2016-09-21.
 */

public interface InstagramDialogListener
{
	public abstract void onComplete(String accessToken);

	public abstract void onError(String error);
}
