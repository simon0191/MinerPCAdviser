package com.mpca.mpcaandroidapp;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.mpca.utils.MpcaFilter;
import com.mpca.utils.MpcaIConstants;
import com.mpca.utils.MpcaJObjectsReader;
import com.mpca.utils.MpcaProduct;
import com.mpca.utils.MySimpleArrayAdapter;

public class ProductsListActivity extends Activity {

	private ListView mProductsLv;
	private List<MpcaProduct> finalProducts;
	private static SortedMap<MpcaProduct, Boolean> products;
	@SuppressWarnings("rawtypes")
	private List<MpcaFilter> filters;

	public static final String PRODUCT_TAG = "PRODUCT_TAG";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_products_list);

		setTitle(getResources().getString(R.string.items_list_title));

		mProductsLv = (ListView) findViewById(R.id.productsLV);

		filters = new ArrayList<MpcaFilter>();
		
		Bundle b = getIntent().getExtras();
		Object[] fs = (Object[]) b.getSerializable(MpcaIConstants.FILTERS_TAG);
		for (Object o : fs) {
			MpcaFilter filter = (MpcaFilter) o;
			filters.add(filter);
		}

		try {
			filter(filters);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}

	private void showProducts() {
		finalProducts = new ArrayList<MpcaProduct>();
		for (Entry<MpcaProduct, Boolean> entryProduct : products.entrySet()) {
			if (entryProduct.getValue()) {
				MpcaProduct p = entryProduct.getKey();
				finalProducts.add(p);
			}
		}
		final MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(this,
				finalProducts);
		mProductsLv.setAdapter(adapter);
		mProductsLv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos,
					long id) {
				// Toast.makeText(ProductsListActivity.this, "Product " + pos +
				// " was selected", Toast.LENGTH_SHORT).show();
				Bundle b = new Bundle();
				b.putSerializable(PRODUCT_TAG, finalProducts.get(pos));
				Intent i = new Intent(ProductsListActivity.this,
						ItemDetailActivity.class);
				i.putExtras(b);
				startActivity(i);
			}
		});

		if (finalProducts.isEmpty()) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(getResources().getString(
					R.string.no_products_title));
			builder.setInverseBackgroundForced(true);
			builder.setPositiveButton(getResources()
					.getString(R.string.ok_text), new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					ProductsListActivity.this.finish();
				}
			});
			AlertDialog alert = builder.create();
			alert.show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.products_list, menu);
		return true;
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
			for (Map.Entry<Comparable, Boolean> ele : elements.entrySet()) {
				if (ele.getValue()) {
					elems.put(ele.getKey());
				}
			}
			jsonValues.put("elems", elems);
			array.put(new JSONObject(jsonValues));
		}

		JSONObject finalFilters = new JSONObject();
		finalFilters.put("filters", array);

		new WSPoster("http://mpca-api.herokuapp.com/products")
				.execute(finalFilters);
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
			progress = ProgressDialog.show(ProductsListActivity.this, "",
					"Loading...");
		}

		@Override
		protected Void doInBackground(JSONObject... params) {
			JSONObject jObject = params[0];

			DefaultHttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(url);

			// setting json object to post request.
			AbstractHttpEntity entity;
			try {
				entity = new ByteArrayEntity(jObject.toString()
						.getBytes("UTF8"));
				entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
						"application/json"));
				post.setEntity(entity);

				// this is your response:
				HttpResponse response = client.execute(post);
				HttpEntity e = response.getEntity();
				InputStream inputStream = e.getContent();
				BufferedReader bf = new BufferedReader(new InputStreamReader(
						inputStream, "UTF-8"), 8);
				StringBuilder sb = new StringBuilder();

				String line;
				while ((line = bf.readLine()) != null) {
					sb.append(line + "\n");
				}
				System.out.println(sb.toString());
				jObject = new JSONObject(sb.toString());
				products = MpcaJObjectsReader.readJProducts(jObject);
			} catch (Exception e1) {
				// e1.printStackTrace();
				success = false;
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			progress.dismiss();
			if (success) {
				showProducts();
			} else {
				Builder alert = new Builder(ProductsListActivity.this);
				alert.setTitle(R.string.connection_fail_title);
				alert.setMessage(R.string.connection_fail_message)
						.setCancelable(false)
						.setPositiveButton(R.string.ok_text,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
										ProductsListActivity.this.finish();
									}
								}).show();
			}
		}
	}

}
