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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import edu.uark.uarkregisterapp.adapters.CartListAdapter;
import edu.uark.uarkregisterapp.adapters.ProductListAdapter;
import edu.uark.uarkregisterapp.models.api.ApiResponse;
import edu.uark.uarkregisterapp.models.api.Product;
import edu.uark.uarkregisterapp.models.api.Transaction;
import edu.uark.uarkregisterapp.models.api.services.ProductService;
import edu.uark.uarkregisterapp.models.api.services.TransactionService;
import edu.uark.uarkregisterapp.models.transition.EmployeeTransition;
import edu.uark.uarkregisterapp.models.transition.ProductTransition;
import edu.uark.uarkregisterapp.models.transition.TransactionTransition;

public class ShoppingCartActivity extends AppCompatActivity {
    private EmployeeTransition currentEmployeeTransition;
    private TransactionTransition transactionTransition = new TransactionTransition();
    private CartListAdapter productListAdapter;
    private List<ProductTransition> cartTransition; //contains the contents of the cart
    private List<Product> cartProducts;
    private List<Transaction> transactions;
    private double totalSales;
    private String total_msg;


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

    public void updateTotalSales() {
        this.totalSales = 0;
        for (int i = 0; i < cartTransition.size(); i++) {
            this.totalSales += (cartTransition.get(i).getCount() * cartTransition.get(i).getCost());
        }
        this.total_msg = "$" + this.totalSales;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        this.transactions = new ArrayList<>();
        this.cartTransition = this.getIntent().getParcelableArrayListExtra("current_transaction");
        this.cartProducts = new ArrayList<>();
        updateTotalSales();
        for (int i = 0; i < cartTransition.size(); i++) {
            Product p = new Product(cartTransition.get(i));
            cartProducts.add(p);
        }
        this.productListAdapter = new CartListAdapter(this, this.cartProducts);
        this.currentEmployeeTransition = this.getIntent().getParcelableExtra("current_employee");
        this.getProductsListView().setAdapter(this.productListAdapter);
        this.getCartTotalView().setText(this.total_msg);
    }

    @Override
    protected void onResume() {
        super.onResume();
        productListAdapter.notifyDataSetChanged();
        updateTotalSales();
    }

    private ListView getProductsListView() {
        return (ListView) this.findViewById(R.id.list_view_shopping_cart);
    }

    private TextView getCartTotalView() {
        return (TextView) this.findViewById(R.id.total_view);
    }

    public void checkout(View view) {
        new RetreiveTransactionList().execute();
        new CreateTransactionTask().execute();
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

    private class RetreiveTransactionList extends AsyncTask<Void, ApiResponse<Transaction>, ApiResponse<List<Transaction>>> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected ApiResponse<List<Transaction>> doInBackground(Void... params) {
            ApiResponse<List<Transaction>> apiResponse = (new TransactionService()).getTransactions();

            if (apiResponse.isValidResponse()) {
                transactions.clear();
                transactions.addAll(apiResponse.getData());
            }

            return apiResponse;
        }

        @Override
        protected void onPostExecute(ApiResponse<List<Transaction>> apiResponse) {
        }
    }

    private class CreateTransactionTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            this.creatingTransactionAlert.show();
        }

        //create employee object from the values in the text fields
        @Override
        protected Boolean doInBackground(Void... params) {
            Transaction transaction = (new Transaction()).
                    setTransactionID(transactions.size()).
                    setCashierID(currentEmployeeTransition.getEmployeeid()).
                    setTotalSales(totalSales);

            ApiResponse<Transaction> apiResponse = (
                    (new TransactionService()).startTransaction(transaction.convertStartTransaction()));

            if (apiResponse.isValidResponse()) {
                transactionTransition.setTransactionID(apiResponse.getData().getTransactionID());
                transactionTransition.setCashierID(apiResponse.getData().getCashierID());
                transactionTransition.setTotalSales(apiResponse.getData().getTotalSales());
            }

            return apiResponse.isValidResponse();
        }

        //alert notification information
        @Override
        protected void onPostExecute(Boolean successfulSave) {
            String message;

            creatingTransactionAlert.dismiss();

            if (successfulSave) {
                message = getString(R.string.alert_transaction_complete);
            } else {
                message = getString(R.string.alert_checkout_failed);
            }

            new AlertDialog.Builder(ShoppingCartActivity.this).
                    setMessage(message).
                    setPositiveButton(
                            R.string.home,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                    Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
                                    intent.putExtra(
                                            "current_employee",
                                            ShoppingCartActivity.this.currentEmployeeTransition
                                    );
                                    ShoppingCartActivity.this.startActivity(intent);
                                }

                            }
                    ).
                    create().
                    show();
        }

        private AlertDialog creatingTransactionAlert;

        private CreateTransactionTask() {
            this.creatingTransactionAlert = new AlertDialog.Builder(ShoppingCartActivity.this).
                    setMessage(R.string.alert_dialog_checking_out).
                    create();
        }
    }
}
