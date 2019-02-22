package edu.uark.uarkregisterapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import edu.uark.uarkregisterapp.models.transition.ProductTransition;

//==========================================
//  Landing Page when the app first opens
//==========================================

public class LandingActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_landing);
	}

//==========================================
//  Button that leads to the Product Page
//==========================================
	public void displayAllProductsButtonOnClick(View view) {
		this.startActivity(new Intent(getApplicationContext(), ProductsListingActivity.class));
	}


//==========================================
//  Button To create a product from the app
//==========================================
	public void createProductButtonOnClick(View view) {
		Intent intent = new Intent(getApplicationContext(), ProductViewActivity.class);

		intent.putExtra(
			getString(R.string.intent_extra_product),
			new ProductTransition()
		);
		//note to self to look at this code as it provides implementation of buttons/product page
		this.startActivity(intent);
	}
}
