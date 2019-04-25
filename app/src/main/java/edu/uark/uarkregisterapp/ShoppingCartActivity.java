package edu.uark.uarkregisterapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.uark.uarkregisterapp.adapters.CartListAdapter;
import edu.uark.uarkregisterapp.adapters.ProductListAdapter;
import edu.uark.uarkregisterapp.models.api.ApiResponse;
import edu.uark.uarkregisterapp.models.api.Product;
import edu.uark.uarkregisterapp.models.api.services.ProductService;
import edu.uark.uarkregisterapp.models.transition.EmployeeTransition;
import edu.uark.uarkregisterapp.models.transition.ProductTransition;

public class ShoppingCartActivity extends AppCompatActivity {
    private EmployeeTransition currentEmployeeTransition;
    private CartListAdapter productListAdapter;
    private List<ProductTransition> cartTransition; //contains the contents of the cart
    private List<Product> cartProducts;


    //===========================================================
    //Adds Menu at the top
    //===========================================================
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
                intent.putExtra("current_employee",
                        this.currentEmployeeTransition
                );
                this.startActivity(intent);
                return true;
            case R.id.item1:
                Toast.makeText(this, "Fruit selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.item2:
                Toast.makeText(this, "Protein selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.item3:
                Toast.makeText(this, "Gear selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.item4:
                Toast.makeText(this, "Gift Card selected", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //End Menu
    //===========================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        this.cartTransition = this.getIntent().getParcelableArrayListExtra("current_transaction");
        this.cartProducts = new ArrayList<>();
        //convert product transition object to actual products
        for (int i = 0; i < cartTransition.size(); i++) {
            Product p = new Product(cartTransition.get(i));
            cartProducts.add(p);
        }
        this.productListAdapter = new CartListAdapter(this, this.cartProducts);
        this.currentEmployeeTransition = this.getIntent().getParcelableExtra("current_employee");
        this.getProductsListView().setAdapter(this.productListAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        (new RetrieveProductsTask()).execute();
    }

    private ListView getProductsListView() {
        return (ListView) this.findViewById(R.id.list_view_shopping_cart);
    }

    public void checkout(View view) {
    }

    public void clearCart(View view) {
        this.cartTransition.clear();
        this.cartProducts.clear();
        this.productListAdapter.notifyDataSetChanged();
    }

    public void keepShopping(View view) {
        Intent intent = new Intent(getApplicationContext(), ProductsListingActivity.class);
        intent.putParcelableArrayListExtra(
                "shopping_list",
                (ArrayList<? extends Parcelable>) this.cartTransition
        );
        intent.putExtra(
                "current_employee",
                this.currentEmployeeTransition
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
            //todo read from transaction table instead of internal

            if (apiResponse.isValidResponse()) {
//                cartProducts.clear();
//                cartProducts.addAll(apiResponse.getData());
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
                new AlertDialog.Builder(ShoppingCartActivity.this).
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
            this.loadingProductsAlert = new AlertDialog.Builder(ShoppingCartActivity.this).
                    setMessage(R.string.alert_dialog_products_loading).
                    create();
        }
    }
}
