package edu.uark.uarkregisterapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import edu.uark.uarkregisterapp.models.transition.ProductTransition;

//==========================================
//  Landing Page after logging in
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

	//Button that leads to the Create Employee page

	public void createEmployeeOnClick(View view) {
		this.startActivity(new Intent(getApplicationContext(), CreateEmployeeActivity.class));
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
		this.startActivity(intent);
	}
}


