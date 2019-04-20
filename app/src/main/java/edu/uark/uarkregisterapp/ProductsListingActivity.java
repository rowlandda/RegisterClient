package edu.uark.uarkregisterapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import edu.uark.uarkregisterapp.adapters.ProductListAdapter;
import edu.uark.uarkregisterapp.models.api.ApiResponse;
import edu.uark.uarkregisterapp.models.api.Product;
import edu.uark.uarkregisterapp.models.api.Transaction;
import edu.uark.uarkregisterapp.models.api.services.ProductService;
import edu.uark.uarkregisterapp.models.transition.EmployeeTransition;
import edu.uark.uarkregisterapp.models.transition.ProductTransition;
import edu.uark.uarkregisterapp.models.transition.TransactionTransition;

public class ProductsListingActivity extends AppCompatActivity {
    private EmployeeTransition currentEmployeeTransition;
    private String search_string = "";
	private List<Product> searchedProducts;
	private List<Product> allProducts;
	private ProductListAdapter productListAdapter;
	private ArrayList<ProductTransition> cartProducts;  //contains the contents of the cart

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_products_listing);
		setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

		ActionBar actionBar = this.getSupportActionBar();
		if (actionBar != null) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}

		this.allProducts = new ArrayList<>();
		this.searchedProducts = new ArrayList<>();
		this.cartProducts = this.getIntent().getParcelableArrayListExtra("shopping_list");
		this.productListAdapter = new ProductListAdapter(this, this.searchedProducts);
		this.currentEmployeeTransition = this.getIntent().getParcelableExtra("current_employee");

		this.getProductsListView().setAdapter(this.productListAdapter);
		this.getProductsListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(getApplicationContext(), ProductViewActivity.class);

				intent.putExtra(
					getString(R.string.intent_extra_product),
					new ProductTransition((Product) getProductsListView().getItemAtPosition(position))
				);
				intent.putExtra(
						getString(R.string.current_employee),
                        currentEmployeeTransition
				);

				startActivity(intent);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		this.cartProducts = this.getIntent().getParcelableArrayListExtra("shopping_list");
		(new RetrieveProductsTask()).execute();
	}

	private ListView getProductsListView() {
		return (ListView) this.findViewById(R.id.list_view_products);
	}

	private EditText getSearchString() {
		return (EditText) this.findViewById(R.id.search_product_field);
	}

	public void searchProducts(View view) {
		search_string = getSearchString().getText().toString();
		searchedProducts.clear();
		for (Product product : allProducts) {
			if (product.getLookupCode().contains(search_string)) {
				searchedProducts.add(product);
			}
		}
		productListAdapter.notifyDataSetChanged();
	}

	public void addProductTask(View view) {
		Product p = new Product();
		//finds the index of the product whos add to cart button we clicked
	    int position = getProductsListView().getPositionForView((LinearLayout)view.getParent());
	    if (position >= 0) {
	    	p = this.productListAdapter.getItem(position);
		}
		cartProducts.add(new ProductTransition(p));
	}

	public void viewCart(View view) {
		Intent intent = new Intent(getApplicationContext(), ShoppingCartActivity.class);
		intent.putExtra(
				"current_employee",
				currentEmployeeTransition
		);
		intent.putParcelableArrayListExtra(
				"current_transaction",
				cartProducts
		);
		this.startActivity(intent);
	}


	private class RetrieveProductsTask extends AsyncTask<Void, Void, ApiResponse<List<Product>>> {
		@Override
		protected void onPreExecute() {
			this.loadingProductsAlert.show();
		}

		@Override
		protected ApiResponse<List<Product>> doInBackground(Void... params) {
			ApiResponse<List<Product>> apiResponse = (new ProductService()).getProducts();

			if (apiResponse.isValidResponse()) {
				allProducts.clear();
				searchedProducts.clear();
				searchedProducts.addAll(apiResponse.getData());
				allProducts.addAll(apiResponse.getData());
			}

			return apiResponse;
		}

		@Override
		protected void onPostExecute(ApiResponse<List<Product>> apiResponse) {
			if (apiResponse.isValidResponse()) {
				productListAdapter.notifyDataSetChanged();
			}

			this.loadingProductsAlert.dismiss();

			if (!apiResponse.isValidResponse()) {
				new AlertDialog.Builder(ProductsListingActivity.this).
						setMessage(R.string.alert_dialog_products_load_failure).
						setPositiveButton(
								R.string.button_dismiss,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										dialog.dismiss();
									}
								}
						).
						create().
						show();
			}
		}

		private AlertDialog loadingProductsAlert;

		private RetrieveProductsTask() {
			this.loadingProductsAlert = new AlertDialog.Builder(ProductsListingActivity.this).
					setMessage(R.string.alert_dialog_products_loading).
					create();
		}
	}
}
