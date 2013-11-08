package com.mpca.mpcaandroidapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;

import com.mpca.utils.MpcaFilter;
import com.mpca.utils.MpcaProduct;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private List<MpcaProduct> products;
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
			mMainLinear.addView(nameTv);
			
			final SortedMap<Comparable, Boolean> map = f.getValues();
			
			for (final Comparable value : map.keySet()) {
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
			//https://code.google.com/p/range-seek-bar/
			final SeekBar sb = new SeekBar(this);
		
		}
	}

	private List<MpcaFilter> createFilters(List<MpcaProduct> ps) {
		MpcaFilter<String> fBrand = new MpcaFilter<String>("Brand");
		MpcaFilter<Integer> fRAM = new MpcaFilter<Integer>("RAM");
		MpcaFilter<Integer> fHardDrive = new MpcaFilter<Integer>("Hard Drive");
		
		for (MpcaProduct p : ps) {
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
	
	public List<MpcaProduct> readProducts() throws IOException {
		List<MpcaProduct> products = new ArrayList<MpcaProduct>();
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
			products.add(p);
		}
		return products;
	}

}
