package com.mpca.utils;

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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MpcaJObjectsReader {
	public static SortedMap<MpcaProduct,Boolean> readJProducts(JSONObject jProducts) {
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
	
	public static JSONObject getJsonFromWS(String url) throws IOException, JSONException {
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
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<MpcaFilter> readJsonFilters(JSONObject jFilters) {
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
}
