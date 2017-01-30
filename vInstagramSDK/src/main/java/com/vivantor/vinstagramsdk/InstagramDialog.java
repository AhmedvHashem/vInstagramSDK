package com.vivantor.vinstagramsdk;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vivantor.vinstagramsdk.callbacks.InstagramDialogListener;

/**
 * Created by AhmedNTS on 2016-09-20.
 */
@SuppressWarnings("all")
class InstagramDialog extends Dialog
{
	private static final String TAG = "InstagramDialog";
	private Context mContext;

	static final FrameLayout.LayoutParams MATCH_PARENT = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
	static final int PADDING = 10;

	private String mUrl;
	private InstagramDialogListener mListener;

	private LinearLayout mContent;
	private TextView mTitle;
	private WebView mWebView;
	private ProgressDialog progressDialog;

	public InstagramDialog(Context context, String url, InstagramDialogListener listener)
	{
		super(context);

		mContext = context;

		mUrl = url;
		mListener = listener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

//		setOnShowListener(new OnShowListener()
//		{
//			@Override
//			public void onShow(DialogInterface dialog)
//			{
//				((Activity)mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
//			}
//		});
//
		setOnDismissListener(new OnDismissListener()
		{
			@Override
			public void onDismiss(DialogInterface dialog)
			{
//				((Activity)mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
				mListener.onError("Canceled");
			}
		});

		progressDialog = new ProgressDialog(getContext());
		progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		progressDialog.setMessage("Loading...");

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		mContent = new LinearLayout(getContext());
		mContent.setOrientation(LinearLayout.VERTICAL);
		setUpTitle();
		setUpWebView();

		Display display = getWindow().getWindowManager().getDefaultDisplay();
		Point outSize = new Point();

		int width = 0;
		int height = 0;

		double[] dimensions = new double[2];

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2)
		{
			display.getSize(outSize);

			width = outSize.x;
			height = outSize.y;
		}
		else
		{
			width = display.getWidth();
			height = display.getHeight();
		}

		if (width < height)
		{
			dimensions[0] = 0.88 * width;
			dimensions[1] = 0.60 * height;
		}
		else
		{
			dimensions[0] = 0.75 * width;
			dimensions[1] = 0.75 * height;
		}

		addContentView(mContent, new FrameLayout.LayoutParams((int) dimensions[0], (int) dimensions[1]));

		CookieSyncManager.createInstance(getContext());
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.removeAllCookie();
	}

	private void setUpTitle()
	{
//		Drawable icon = getContext().getResources().getDrawable(R.drawable.instagram_icon);
		mTitle = new TextView(getContext());

		mTitle.setText("Instagram");
		mTitle.setTextColor(Color.WHITE);
		mTitle.setTypeface(Typeface.DEFAULT_BOLD);
		mTitle.setBackgroundColor(0xFF163753);
		mTitle.setPadding(PADDING * 2, PADDING, PADDING, PADDING);
		mTitle.setGravity(Gravity.CENTER);
//		mTitle.setCompoundDrawablePadding(PADDING);
//		mTitle.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);

		mContent.addView(mTitle);
	}

	private void setUpWebView()
	{
		mWebView = new WebView(getContext());
		mWebView.setVerticalScrollBarEnabled(false);
		mWebView.setHorizontalScrollBarEnabled(false);
		mWebView.setWebViewClient(new OAuthWebViewClient());
		mWebView.loadUrl(mUrl);
		mWebView.setLayoutParams(MATCH_PARENT);

		WebSettings webSettings = mWebView.getSettings();
//		webSettings.setJavaScriptEnabled(true);
		webSettings.setSavePassword(false);
		webSettings.setSaveFormData(false);

		mContent.addView(mWebView);
		mContent.requestFocus();
	}

	private class OAuthWebViewClient extends WebViewClient
	{
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url)
		{
			Log.d(TAG, "Redirecting URL " + url);
			if (url.startsWith(InstagramSDK.callbackUrl))
			{
				String urls[] = url.split("=");
				mListener.onComplete(urls[1]);
				InstagramDialog.this.dismiss();
				return true;
			}
			return false;
		}

		@Override
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
		{
			Log.d(TAG, "Page error: " + description);
			super.onReceivedError(view, errorCode, description, failingUrl);
			mListener.onError(description);
			InstagramDialog.this.dismiss();
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon)
		{
			Log.d(TAG, "Loading URL: " + url);
			super.onPageStarted(view, url, favicon);
			progressDialog.show();
		}

		@Override
		public void onPageFinished(WebView view, String url)
		{
			super.onPageFinished(view, url);
			Log.d(TAG, "onPageFinished URL: " + url);
			progressDialog.dismiss();
		}
	}
}
