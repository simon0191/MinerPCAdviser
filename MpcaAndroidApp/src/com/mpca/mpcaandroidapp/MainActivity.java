package com.mpca.mpcaandroidapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import com.mpca.ui.RangeSeekBar;
import com.mpca.ui.RangeSeekBar.OnRangeSeekBarChangeListener;
import com.mpca.utils.MpcaFilter;
import com.mpca.utils.MpcaProduct;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private static SortedMap<MpcaProduct,Boolean> products;
	private List<MpcaFilter> filters;
	
	private LinearLayout mMainLinear;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mMainLinear = (LinearLayout) findViewById(R.id.mainLinearLayout);
		
		try {
			products = readProducts();
		} catch (IOException e) {
			e.printStackTrace();
		}
		filters = createFilters(products);
		for (MpcaFilter<Comparable> f : filters) {
			String name = f.getName();
			TextView nameTv = new TextView(this);
			nameTv.setText(name);
			nameTv.setTextSize(18);
			nameTv.setTypeface(Typeface.DEFAULT_BOLD);
			mMainLinear.addView(nameTv);
			
			final SortedMap<Comparable, Boolean> map = f.getValues();
			boolean isIntegerFilter = false; 
			for (final Comparable value : map.keySet()) {
				if(value instanceof String) {
					String v = value.toString();
					final CheckBox cb = new CheckBox(this);
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
				final RangeSeekBar<Integer> seekBar = new RangeSeekBar<Integer>(0,(Integer)map.lastKey() , this);
				final TextView min = new TextView(this);
				min.setText("Min: " + 0 + " GB");
				mMainLinear.addView(min);
				
				final TextView max = new TextView(this);
				max.setText("Max: " + (Integer)map.lastKey() + " GB");
				mMainLinear.addView(max);
				
				seekBar.setOnRangeSeekBarChangeListener(new OnRangeSeekBarChangeListener<Integer>() {

					@Override
					public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar,
							Integer minValue, Integer maxValue) {
						// TODO Auto-generated method stub
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
		
		final Button filterButton = new Button(this);
		filterButton.setText("Filter");
		filterButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				filter(products,filters);
				Intent i = new Intent(MainActivity.this, ProductsListActivity.class);
				startActivity(i);
				/*for(Map.Entry<MpcaProduct, Boolean> entry:products.entrySet()) {
					System.out.println(entry.getKey().getModel()+": "+entry.getValue());
				}*/
			}
		});
		mMainLinear.addView(filterButton);
		
	}
	
	public static SortedMap<MpcaProduct, Boolean> getProducts() {
		return products;
	}
	
	private SortedMap<MpcaProduct,Boolean> filter(SortedMap<MpcaProduct,Boolean> ps,List<MpcaFilter> fs) {
		Set<MpcaProduct> keys = ps.keySet();
		
		for(MpcaProduct p:keys) {
			ps.put(p, false);
			int nPassedFilters = 0;
			for(MpcaFilter f:fs) {
				Set<Map.Entry<Comparable,Boolean>> entrySet = f.getValues().entrySet();
				for (Map.Entry<Comparable, Boolean> entry : entrySet) {
					if(entry.getValue() && p.getProperty(f.getName()).equals(entry.getKey())) {
						nPassedFilters++;
						break;
					}
				}
			}
			if(nPassedFilters == fs.size()) {
				ps.put(p, true);
			}
		}
		return ps;
	}

	private List<MpcaFilter> createFilters(Map<MpcaProduct,Boolean> ps) {
		MpcaFilter<String> fBrand = new MpcaFilter<String>("Brand");
		MpcaFilter<Integer> fRAM = new MpcaFilter<Integer>("RAM");
		MpcaFilter<Integer> fHardDrive = new MpcaFilter<Integer>("Hard Drive");
		
		for (MpcaProduct p : ps.keySet()) {
			fBrand.addValue(p.getBrand());
			fRAM.addValue(p.getRam());
			fHardDrive.addValue(p.getHardDisk());
		}
		
		List<MpcaFilter> fs = new ArrayList<MpcaFilter>();
		fs.add(fBrand);
		fs.add(fRAM);
		fs.add(fHardDrive);
		return fs;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public SortedMap<MpcaProduct,Boolean> readProducts() throws IOException {
		SortedMap<MpcaProduct,Boolean> products = new TreeMap<MpcaProduct,Boolean>();
		BufferedReader bf = new BufferedReader(new InputStreamReader(
				getAssets().open("all_products.txt")));
		int size = Integer.parseInt(bf.readLine());
		for (int i = 0; i < size; i++) {
			String model = bf.readLine();
			String brand = bf.readLine();
			int ram = Integer.parseInt(bf.readLine());
			int hd = Integer.parseInt(bf.readLine());
			String recommendation = bf.readLine();
			int priority = Integer.parseInt(bf.readLine());
			String image = bf.readLine();
			if(image.equals("null")) {
				image = "ic_launcher";
			}
			MpcaProduct p = new MpcaProduct(i, model, brand, ram, hd, recommendation, priority, image);
			products.put(p,true);
		}
		return products;
	}

}
