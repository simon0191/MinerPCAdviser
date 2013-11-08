package com.mpca.utils;

import java.util.Arrays;
import java.util.List;

import com.mpca.mpcaandroidapp.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MySimpleArrayAdapter extends ArrayAdapter<MpcaProduct> {
	
	private Context context;
	private List<MpcaProduct> values;
	
	public MySimpleArrayAdapter(Context context, MpcaProduct[] values) {
		super(context, R.layout.activity_item_fragment, values);
		this.context = context;
		this.values = Arrays.asList(values);
	}
	
	public MySimpleArrayAdapter(Context context, List<MpcaProduct> values) {
		super(context, R.layout.activity_item_fragment, values);
		this.context = context;
		this.values = values;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		LayoutInflater inflater = (LayoutInflater) context.
				getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.activity_item_fragment, parent, false);
		
		TextView itemModel = (TextView) rowView.findViewById(R.id.itemModel);
		TextView itemRecommendation = (TextView) rowView.findViewById(R.id.itemRecommendation);
		TextView itemBrand = (TextView) rowView.findViewById(R.id.itemBrand);
		ImageView itemIcon = (ImageView) rowView.findViewById(R.id.itemIcon);
		
		MpcaProduct product = values.get(position);
		
		itemModel.setText(product.getModel());
		
		itemRecommendation.setText(product.getRecommendation());
		itemBrand.setText(product.getBrand());
		int imageId = getContext().getResources().getIdentifier(product.getImageName(), 
				"drawable", getContext().getPackageName());
		itemIcon.setImageResource(imageId);
		
		return rowView;
	}
	
}
