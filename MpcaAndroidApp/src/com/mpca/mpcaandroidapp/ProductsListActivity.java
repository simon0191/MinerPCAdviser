package com.mpca.mpcaandroidapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;

import com.mpca.utils.MpcaProduct;
import com.mpca.utils.MySimpleArrayAdapter;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.support.v4.app.DialogFragment;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ProductsListActivity extends Activity {
	
	private ListView mProductsLv;
	private List<MpcaProduct> finalProducts;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_products_list);
		
		mProductsLv = (ListView)findViewById(R.id.productsLV);
		
		Bundle b = getIntent().getExtras();
		
		finalProducts = new ArrayList<MpcaProduct>();
		SortedMap<MpcaProduct, Boolean> products = MainActivity.getProducts();
		for (Entry<MpcaProduct, Boolean> entryProduct : products.entrySet()) {
			if(entryProduct.getValue()) {
				MpcaProduct p = entryProduct.getKey();
				finalProducts.add(p);
			}
		}
		final MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(this, finalProducts);
		mProductsLv.setAdapter(adapter);
		mProductsLv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int pos, long id) {
				Toast.makeText(ProductsListActivity.this, "Product " + pos + " was selected", Toast.LENGTH_SHORT).show();
			}
		});
		
		if(finalProducts.isEmpty()) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(getResources().getString(R.string.no_products_title));
			builder.setInverseBackgroundForced(true);
			builder.setPositiveButton(getResources().getString(R.string.ok_text),
					new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							onBackPressed();
						}
					});
			AlertDialog alert = builder.create();
			alert.show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.products_list, menu);
		return true;
	}

}
