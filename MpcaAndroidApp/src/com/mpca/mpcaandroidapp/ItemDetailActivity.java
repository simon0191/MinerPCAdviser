package com.mpca.mpcaandroidapp;

import java.io.IOException;
import java.util.Set;

import com.mpca.utils.MpcaProduct;
import com.mpca.utils.MySimpleArrayAdapter;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class ItemDetailActivity extends Activity {
	
	private ImageView mProductIcon;
	private TextView mRecommendationTv;
	private TextView mBrandTv;
	private TextView mModelTv;
	private TableLayout mPolaritiesTable;
	private Button mWordCloudButton;
	
	public static final String WORD_CLOUD = "WORD_CLOUD";
	public static final String PERFECT_WORD_CLOUD = "cloud_perfect";
	public static final String BAD_WORD_CLOUD = "cloud_bad";
	
	public static final String WOW_RECOMMENDATION = "WOW!";
	public static final String BUY_IT_RECOMMENDATION = "BUIT IT!";
	public static final String GOOD_OPTION_RECOMMENDATION = "IT'S A GOOD OPTION";
	public static final String THINK_TWICE_RECOMMENDATION = "THINK IT TWICE!";
	public static final String NEVER_MIND_RECOMMENDATION = "NEVER MIND!";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_detail);
		Bundle b = getIntent().getExtras();
		final MpcaProduct p = (MpcaProduct)
				b.getSerializable(ProductsListActivity.PRODUCT_TAG);
		/*int imageId = getResources().getIdentifier(p.getImageName(), 
				"drawable", getPackageName());*/
		
		setTitle(getResources().getString(R.string.item_detail_text));
		
		// Instanciación
		mProductIcon = (ImageView)findViewById(R.id.productIcon);
		mRecommendationTv = (TextView) findViewById(R.id.recommendationTv);
		mBrandTv = (TextView) findViewById(R.id.brandTv);
		mModelTv = (TextView) findViewById(R.id.modelTv);
		mPolaritiesTable = (TableLayout) findViewById(R.id.commentsTable);
		mWordCloudButton = (Button) findViewById(R.id.wordCloudButton);
		
		mWordCloudButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Bundle b = new Bundle();
				b.putSerializable(ProductsListActivity.PRODUCT_TAG, p);
				/*String recommendation = p.getRecommendation();
				if(!recommendation.equals(THINK_TWICE_RECOMMENDATION) &&
						!recommendation.equals(NEVER_MIND_RECOMMENDATION)) {
					b.putString(WORD_CLOUD, PERFECT_WORD_CLOUD);
				} else {
					b.putString(WORD_CLOUD, BAD_WORD_CLOUD);
				}
				*/
				Intent i = new Intent(ItemDetailActivity.this, 
						WordCloudActivity.class);
				i.putExtras(b);
				startActivity(i);
			}
		});
		
		String imageUrl = p.getImageUrl();
		new ImageWSConsumer().execute(imageUrl);
		
		mPolaritiesTable.setStretchAllColumns(true);
		mPolaritiesTable.setShrinkAllColumns(true);
		
		//mProductIcon.setImageResource(imageId);
		mRecommendationTv.setText("Advise:\n" + p.getRecommendation());
		mBrandTv.setText(p.getBrand());
		mModelTv.setText(p.getModel());
		
		mRecommendationTv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);  
		mRecommendationTv.setGravity(Gravity.CENTER);  
		mRecommendationTv.setTypeface(Typeface.SERIF, Typeface.BOLD);
		
		Set<String> polarities = p.getPolarities();
		
		TableRow rowTitle = new TableRow(this);  
	    rowTitle.setGravity(Gravity.CENTER_HORIZONTAL);
	    TextView bigTitle = new TextView(this);  
	    bigTitle.setText("Score");  
	    bigTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);  
	    bigTitle.setGravity(Gravity.CENTER);  
	    bigTitle.setTypeface(Typeface.SERIF, Typeface.BOLD);
	    TableRow.LayoutParams params = new TableRow.LayoutParams();  
	    params.span = 6;  
	    rowTitle.addView(bigTitle, params);
	    mPolaritiesTable.addView(rowTitle);
	    
		TableRow titles = new TableRow(this);
		titles.setGravity(Gravity.CENTER);
		TextView polarityTitle = new TextView(this);  
		polarityTitle.setText("How is it seen by people?");  
		polarityTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);  
		polarityTitle.setGravity(Gravity.CENTER);  
		polarityTitle.setTypeface(Typeface.SERIF, Typeface.BOLD);
		
		TextView indexTitle = new TextView(this);  
		indexTitle.setText("Index");  
		indexTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);  
		indexTitle.setGravity(Gravity.CENTER);  
		indexTitle.setTypeface(Typeface.SERIF, Typeface.BOLD);
		titles.addView(polarityTitle);
		titles.addView(indexTitle);
		mPolaritiesTable.addView(titles);
		
		for (String polarity : polarities) {
			TableRow row = new TableRow(this);
			row.setGravity(Gravity.CENTER);
			TextView polarityTv = new TextView(this);
			polarityTv.setText(polarity + " comments");
			polarityTv.setGravity(Gravity.CENTER_HORIZONTAL);
			polarityTv.setTypeface(Typeface.SERIF, Typeface.BOLD);
			TextView indexTv = new TextView(this);
			indexTv.setText(new String((p.getPolarityIndex(polarity)*100) + "").subSequence(0, 4) + "%");
			indexTv.setGravity(Gravity.CENTER_HORIZONTAL);
			row.addView(polarityTv);
			row.addView(indexTv);
			
			mPolaritiesTable.addView(row);
		}
		/*for (int i = 0; i < min; i++) {
			TableRow row = new TableRow(this);
			row.setGravity(Gravity.CENTER);
			for (String polarity : polarities) {
				String comment = p.getPolarityIndex(polarity).get(i);
				TextView comm = new TextView(this);
				comm.setText(comment);
				comm.setGravity(Gravity.CENTER_HORIZONTAL);
				row.addView(comm);
			}
			mPolaritiesTable.addView(row);
		}*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.item_detail, menu);
		return true;
	}
	
	private class ImageWSConsumer extends AsyncTask<String, Void, Bitmap> {

		@Override
		protected Bitmap doInBackground(String... params) {
			Bitmap img = null;
			String imageUrl = params[0];
			try {
				img = MySimpleArrayAdapter.getImageFromURL(imageUrl);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return img;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			if(result != null) {
				mProductIcon.setImageBitmap(result);
			}
		}
	}

}
