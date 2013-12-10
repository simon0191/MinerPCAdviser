package com.mpca.mpcaandroidapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mpca.ui.RangeSeekBar;
import com.mpca.ui.RangeSeekBar.OnRangeSeekBarChangeListener;
import com.mpca.utils.MpcaFilter;
import com.mpca.utils.MpcaProduct;

public class MainActivity extends Activity {
	
	private static SortedMap<MpcaProduct,Boolean> products;
	@SuppressWarnings("rawtypes")
	private List<MpcaFilter> filters;
	
	private LinearLayout mMainLinear;
	
	private ProgressBar mProgressBar = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		setTitle(getResources().getString(R.string.main_activity_title));
		
		mMainLinear = (LinearLayout) findViewById(R.id.mainLinearLayout);
		
		try {
			readProducts();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//filters = createFilters(products);
		//createViewFilters();
		
		//addFilterButton();
		
	}
	
	private void addFilterButton() {
		final Button filterButton = new Button(MainActivity.this);
		filterButton.setText("Filter");
		filterButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					filter(filters);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
		mMainLinear.addView(filterButton);
	}
	
	private void goToProductsList() {
		Intent i = new Intent(MainActivity.this, ProductsListActivity.class);
		startActivity(i);
	}

	private void createViewFilters() {
		for (MpcaFilter<Comparable> f : filters) {
			String name = f.getName();
			TextView nameTv = new TextView(MainActivity.this);
			nameTv.setText(name.toUpperCase());
			nameTv.setTextSize(18);
			nameTv.setTypeface(Typeface.DEFAULT_BOLD);
			mMainLinear.addView(nameTv);
			
			final SortedMap<Comparable, Boolean> map = f.getValues();
			boolean isIntegerFilter = false; 
			for (final Comparable value : map.keySet()) {
				if(value instanceof String) {
					String v = value.toString();
					final CheckBox cb = new CheckBox(MainActivity.this);
					cb.setText(v);
					cb.setChecked(true);
					cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						
						@Override
						public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
							map.put(value, !map.get(value));
						}
					});
					mMainLinear.addView(cb);
				}
				else if(value instanceof Integer) {
					isIntegerFilter = true;
					break;
				}
			}
			if(isIntegerFilter) {
				final RangeSeekBar<Integer> seekBar = new RangeSeekBar<Integer>(0,(Integer)map.lastKey() , MainActivity.this);
				final TextView min = new TextView(MainActivity.this);
				min.setText("Min: " + 0 + " GB");
				mMainLinear.addView(min);
				
				final TextView max = new TextView(this);
				max.setText("Max: " + (Integer)map.lastKey() + " GB");
				mMainLinear.addView(max);
				
				seekBar.setOnRangeSeekBarChangeListener(new OnRangeSeekBarChangeListener<Integer>() {

					@Override
					public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar,
							Integer minValue, Integer maxValue) {
						setMapRange(map.headMap(minValue), false);
						setMapRange(map.subMap(minValue, maxValue), true);
						setMapRange(map.tailMap(maxValue), false);
						
						min.setText("Min: " + minValue + " GB");
						max.setText("Max: " + maxValue + " GB");
					}

					private void setMapRange(
							SortedMap<Comparable, Boolean> headMap, boolean val) {
						for(Comparable c:headMap.keySet()) {
							headMap.put(c, val);
						}
					}
					
				});
				
				mMainLinear.addView(seekBar);
			}
		}
	}
	
	public static SortedMap<MpcaProduct, Boolean> getProducts() {
		return products;
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void filter(List<MpcaFilter> fs) throws JSONException {
		
		JSONArray array = new JSONArray();
		for (MpcaFilter filter : fs) {
			Map<String, Object> jsonValues = new HashMap<String, Object>();
			
			String name = filter.getName();
			jsonValues.put("name", name);
			SortedMap<Comparable, Boolean> elements = filter.getValues();
			JSONArray elems = new JSONArray();
			for (Map.Entry<Comparable, Boolean> ele: elements.entrySet()) {
				if(ele.getValue()) {
					elems.put(ele.getKey());
				}
			}
			jsonValues.put("elems", elems);
			array.put(new JSONObject(jsonValues));
		}
		
		JSONObject finalFilters = new JSONObject();
		finalFilters.put("filters", array);
		
		new WSPoster("http://mpca-api.herokuapp.com/products").execute(finalFilters);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<MpcaFilter> readJsonFilters(JSONObject jFilters) {
		List<MpcaFilter> fs = new ArrayList<MpcaFilter>();
		try {
			JSONArray jArray = jFilters.getJSONArray("filters");
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject jf = jArray.getJSONObject(i);
				
				String name = jf.getString("name");
				String type = jf.getString("type");
				
				MpcaFilter filter = null;
				if(type.equalsIgnoreCase("String")) {
					filter = new MpcaFilter<String>(name);
					JSONArray arr = jf.getJSONArray("elems");
					for (int j = 0; j < arr.length(); j++) {
						String ele = arr.getString(j);
						filter.addValue(ele);
					}
				} else if(type.equalsIgnoreCase("Number")) {
					filter = new MpcaFilter<Integer>(name);
					JSONArray arr = jf.getJSONArray("elems");
					for (int j = 0; j < arr.length(); j++) {
						int ele = arr.getInt(j);
						filter.addValue(ele);
					}
				}
				fs.add(filter);
			}
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
		return fs;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void readProducts() throws IOException {
		String productsUrl = "http://mpca-api.herokuapp.com/products";
		String filtersUrl = "http://mpca-api.herokuapp.com/filters";
		try {
			getProductsWebServiceJson(productsUrl, filtersUrl);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			e1.printStackTrace();
		}
	}

	private void getProductsWebServiceJson(String ...url) throws InterruptedException, ExecutionException {
		new ConsumeWS().execute(url);
	}
	
	private SortedMap<MpcaProduct,Boolean> readJProducts(JSONObject jProducts) {
		SortedMap<MpcaProduct,Boolean> products = new TreeMap<MpcaProduct,Boolean>();
		try {
			JSONArray jArray = jProducts.getJSONArray("products");
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject pc = jArray.getJSONObject(i);
				
				int id = pc.getInt("id");
				String model = pc.getString("model");
				String brand = pc.getString("brand");
				
				int ram = pc.getInt("ram");
				int hd = pc.getInt("hd");
				String recommendation = pc.getString("rec");
				int priority = pc.getInt("priority");
				
				Map<String, Double> polaritiesIndex = new HashMap<String, Double>();
				polaritiesIndex.put("positive", pc.getDouble("positive"));
				polaritiesIndex.put("negative", pc.getDouble("negative"));
				
				String imageUrl = pc.getString("image");
				String wordcloudUrl = pc.getString("wordcloud");
				
				MpcaProduct p = new MpcaProduct(id, model, brand, ram, hd, recommendation, priority, imageUrl, polaritiesIndex, wordcloudUrl);
				products.put(p,true);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return products;
	}
	
	private void createProgressBar() {
		removeProgressBar();
		mProgressBar = new ProgressBar(MainActivity.this);
		mMainLinear.addView(mProgressBar);
	}
	
	private void removeProgressBar() {
		if(mProgressBar != null) {
			mMainLinear.removeView(mProgressBar);
		}
	}
	
	private class ConsumeWS extends AsyncTask<String, Void, Void> {
		
		private boolean success = true;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			createProgressBar();
		}
		
		@Override
		protected Void doInBackground(String... params) {
			JSONObject jProducts;
			try {
				jProducts = getJsonFromWS(params[0]);
				products = readJProducts(jProducts);
				JSONObject jFilters = getJsonFromWS(params[1]);
				filters = readJsonFilters(jFilters);
			} catch (Exception e) {
				//e.printStackTrace();
				success = false;
				System.out.println("NOT SUCCESS");
			}
			//createViewFilters();
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if(success) {
				removeProgressBar();
				createViewFilters();
				addFilterButton();
			} else {
				createExitAlertDialog();
			}
		}
		
	}
	
	private void createExitAlertDialog() {
		Builder alert = new Builder(MainActivity.this);
		alert.setTitle(R.string.connection_fail_title);
		alert.setMessage(R.string.connection_fail_message)
		.setCancelable(false)
		.setPositiveButton(R.string.ok_text, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				MainActivity.this.finish();
			}
		}).show();
	}
	
	private class WSPoster extends AsyncTask<JSONObject, Void, Void> {
		
		private String url;
		private ProgressDialog progress;
		private boolean success = true;
		
		public WSPoster(String url) {
			this.url = url;
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progress = ProgressDialog.show(MainActivity.this, "", "Loading...");
		}
		
		@Override
		protected Void doInBackground(JSONObject... params) {
			JSONObject jObject = params[0];
			
			DefaultHttpClient client = new DefaultHttpClient();
		    HttpPost post = new HttpPost(url);

		    //setting json object to post request.
		    AbstractHttpEntity entity;
			try {
				entity = new ByteArrayEntity(jObject.toString().getBytes("UTF8"));
				entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			    post.setEntity(entity);

			    //this is your response:
			    HttpResponse response = client.execute(post);
			    HttpEntity e = response.getEntity();
			    InputStream inputStream = e.getContent();
			    BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
				StringBuilder sb = new StringBuilder();
				
				String line;
				while((line = bf.readLine()) != null) {
					sb.append(line + "\n");
				}
				System.out.println(sb.toString());
				jObject = new JSONObject(sb.toString());
				products = readJProducts(jObject);
			} catch (Exception e1) {
				//e1.printStackTrace();
				success = false;
			}
		    
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			progress.dismiss();
			if(success) {
				goToProductsList();
			} else {
				Builder alert = new Builder(MainActivity.this);
				alert.setTitle(R.string.connection_fail_title);
				alert.setMessage(R.string.connection_fail_message)
				.setCancelable(false)
				.setPositiveButton(R.string.ok_text, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();
			}
		}
		
	}
	
	private JSONObject getJsonFromWS(String url) throws IOException, JSONException {
		DefaultHttpClient httpClient = new DefaultHttpClient(new BasicHttpParams());
		HttpGet httpGet = new HttpGet(url);
		
		httpGet.setHeader("Content-type", "application/json");
		
		JSONObject jObject = null;
		
		HttpResponse response = httpClient.execute(httpGet);
		HttpEntity entity = response.getEntity();
		//response.getEntity().
		
		InputStream inputStream = entity.getContent();
		
		
		BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
		StringBuilder sb = new StringBuilder();
		
		String line;
		while((line = bf.readLine()) != null) {
			sb.append(line + "\n");
		}
		jObject = new JSONObject(sb.toString());
		
		return jObject;
	}

}
