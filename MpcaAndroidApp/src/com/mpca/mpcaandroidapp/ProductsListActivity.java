package com.mpca.mpcaandroidapp;

import java.util.Map.Entry;
import java.util.SortedMap;

import com.mpca.utils.MpcaProduct;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ListView;

public class ProductsListActivity extends Activity {
	
	private ListView mProductsLv;
	public final static String PRODUCTS = "PRODUCTS";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_products_list);
		
		mProductsLv = (ListView)findViewById(R.id.productsLV);
		
		Bundle b = getIntent().getExtras();
		
		SortedMap<MpcaProduct, Boolean> products = (SortedMap<MpcaProduct, Boolean>) b.get(PRODUCTS);
		for (Entry<MpcaProduct, Boolean> entryProduct : products.entrySet()) {
			if(entryProduct.getValue()) {
				MpcaProduct p = entryProduct.getKey();
				
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.products_list, menu);
		return true;
	}

}
