package com.mpca.mpcaandroidapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.mpca.mpcaandroidapp.util.SystemUiHider;
import com.mpca.utils.MpcaProduct;
import com.mpca.utils.MySimpleArrayAdapter;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */

public class WordCloudActivity extends Activity {
	/**
	 * Whether or not the system UI should be auto-hidden after
	 * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
	 */
	private static final boolean AUTO_HIDE = true;
	
	/**
	 * The flags to pass to {@link SystemUiHider#getInstance}.
	 */
	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

	/**
	 * The instance of the {@link SystemUiHider} for this activity.
	 */
	private SystemUiHider mSystemUiHider;
	
	private ImageView mWordCloudImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_word_cloud);

		final View contentView = findViewById(R.id.fullscreen_content);

		// Set up an instance of SystemUiHider to control the system UI for
		// this activity.
		mSystemUiHider = SystemUiHider.getInstance(this, contentView,
				HIDER_FLAGS);
		mSystemUiHider.setup();
		setTitle(getResources().getString(R.string.word_cloud_text));
		
		mWordCloudImage = (ImageView) findViewById(R.id.wordCloudImage);

		// Upon interacting with UI controls, delay any scheduled hide()
		// operations to prevent the jarring behavior of controls going away
		// while interacting with the UI.
		Bundle b = getIntent().getExtras();
		MpcaProduct p = (MpcaProduct) b.getSerializable(ProductsListActivity.PRODUCT_TAG);
		new ImageWSConsumer(p.getId()).execute(p.getWordcloudUrl());
		
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		// Trigger the initial hide() shortly after the activity has been
		// created, to briefly hint to the user that UI controls
		// are available.
		//delayedHide(100);
	}
	
	private class ImageWSConsumer extends AsyncTask<String, Void, Bitmap> {
		
		private int productId;
		private ProgressDialog progress;
		private boolean success = true;
		
		public ImageWSConsumer(int productId) {
			this.productId = productId;
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progress = ProgressDialog.show(WordCloudActivity.this, "", "Loading...");
		}
		
		@Override
		protected Bitmap doInBackground(String... params) {
			Bitmap img = null;
			String imageUrl = params[0];
			try {
				if(!imageUrl.endsWith("?")) {
					imageUrl += "?";
				}
				
				List<NameValuePair> parameters = new LinkedList<NameValuePair>();
				
				parameters.add(new BasicNameValuePair("id", productId+""));
				
				String paramsStr = URLEncodedUtils.format(parameters, "UTF-8");
				
				imageUrl += paramsStr;
				
				img = MySimpleArrayAdapter.getImageFromURL(imageUrl);
			} catch (IOException e) {
				//e.printStackTrace();
				success = false;
			}
			return img;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			progress.dismiss();
			if(result != null) {
				mWordCloudImage.setImageBitmap(result);
			}
			if(!success) {
				Builder alert = new Builder(WordCloudActivity.this);
				alert.setTitle(R.string.connection_fail_title);
				alert.setMessage(R.string.connection_fail_message)
				.setCancelable(false)
				.setPositiveButton(R.string.ok_text, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						WordCloudActivity.this.finish();
					}
				}).show();
			}
		}
	}

	/**
	 * Touch listener to use for in-layout UI controls to delay hiding the
	 * system UI. This is to prevent the jarring behavior of controls going away
	 * while interacting with activity UI.
	 */
	View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			if (AUTO_HIDE) {
				//delayedHide(AUTO_HIDE_DELAY_MILLIS);
			}
			return false;
		}
	};

	Handler mHideHandler = new Handler();
	Runnable mHideRunnable = new Runnable() {
		@Override
		public void run() {
			mSystemUiHider.hide();
		}
	};
	
}
