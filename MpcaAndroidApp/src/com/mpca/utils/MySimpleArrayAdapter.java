package com.mpca.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mpca.mpcaandroidapp.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MySimpleArrayAdapter extends ArrayAdapter<MpcaProduct> {

	private Context context;
	private List<MpcaProduct> values;
	private Map<String, Bitmap> images;

	public MySimpleArrayAdapter(Context context, MpcaProduct[] values) {
		super(context, R.layout.activity_item_fragment, values);
		this.context = context;
		this.values = Arrays.asList(values);
		images = new HashMap<String, Bitmap>();
	}

	public MySimpleArrayAdapter(Context context, List<MpcaProduct> values) {
		super(context, R.layout.activity_item_fragment, values);
		this.context = context;
		this.values = values;
		images = new HashMap<String, Bitmap>();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.activity_item_fragment,
				parent, false);

		TextView itemModel = (TextView) rowView.findViewById(R.id.itemModel);
		TextView itemRecommendation = (TextView) rowView
				.findViewById(R.id.itemRecommendation);
		TextView itemBrand = (TextView) rowView.findViewById(R.id.itemBrand);
		ImageView itemIcon = (ImageView) rowView.findViewById(R.id.itemIcon);

		MpcaProduct product = values.get(position);

		itemModel.setText(product.getModel());

		itemRecommendation.setText(product.getRecommendation());
		itemBrand.setText(product.getBrand());

		if (!images.containsKey(product.getImageUrl())) {
			new ImageWSConsumer(itemIcon).execute(product.getImageUrl());
		} else {
			itemIcon.setImageBitmap(images.get(product.getImageUrl()));
		}

		/*
		 * int imageId =
		 * getContext().getResources().getIdentifier(product.getImageName(),
		 * "drawable", getContext().getPackageName());
		 */
		// itemIcon.setImageResource(imageId);

		return rowView;
	}

	public Bitmap getBitmapValue(String key) {
		return images.get(key);
	}

	public Set<String> getKeys() {
		return images.keySet();
	}

	private class ImageWSConsumer extends AsyncTask<String, Void, Bitmap> {
		
		private ImageView itemIcon;
		private String imageUrl;

		public ImageWSConsumer(ImageView itemIcon) {
			super();
			this.itemIcon = itemIcon;
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			Bitmap img = null;
			imageUrl = params[0];
			try {
				img = getImageFromURL(imageUrl);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return img;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			if (result != null) {
				itemIcon.setImageBitmap(result);
				images.put(imageUrl, result);
			} else {
				String name = getContext().getResources().getString(
						R.string.default_image);
				int imageId = getContext().getResources().getIdentifier(name,
						"drawable", getContext().getPackageName());
				itemIcon.setImageResource(imageId);
			}
		}
	}

	public static Bitmap getImageFromURL(String imageUrl) throws MalformedURLException, IOException {
		Bitmap img;
		URL url = new URL(imageUrl);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		InputStream is;
		is = connection.getInputStream();
		img = BitmapFactory.decodeStream(is);
		return img;
	}

}
