package edu.uark.uarkregisterapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

import edu.uark.uarkregisterapp.models.transition.EmployeeTransition;
import edu.uark.uarkregisterapp.models.transition.ProductTransition;

public class HomeScreen extends AppCompatActivity {

    String errMessage = "Functionality Not Available";
    private EmployeeTransition currentEmployeeTransition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        this.currentEmployeeTransition = this.getIntent().getParcelableExtra("current_employee");
    }

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
                this.startActivity(new Intent(getApplicationContext(), HomeScreen.class));
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
    protected void onStart() {
        super.onStart();
        String welcome_msg = "Welcome "
                            + this.currentEmployeeTransition.getFname()
                            + "Make Some Sales!!!" ;
        this.getEmployeeTextView().setText(welcome_msg);
    }

    private TextView getEmployeeTextView() {
        return (TextView)this.findViewById(R.id.welcome_message_HomeScreen);
    }

    public void startTransactionOnClick(View view) {
        Intent intent = new Intent(getApplicationContext(), ProductsListingActivity.class);
        intent.putParcelableArrayListExtra(
                "shopping_list",
                new ArrayList<ProductTransition>()
        );
        intent.putExtra(
                "current_employee",
                this.currentEmployeeTransition
        );
        this.startActivity(intent);
    }

    public void createNewEmployeeButton(View view) {
        Intent intent = new Intent(getApplicationContext(), CreateEmployeeActivity.class);

        intent.putExtra(
                getString(R.string.current_employee),
                currentEmployeeTransition
        );
        this.startActivity(intent);
    }

    public void salesReportProductOnClick(View view) {
        new AlertDialog.Builder(this).
                setMessage(errMessage).
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

    public void salesReportCashierOnClick(View view) {
        new AlertDialog.Builder(this).
                setMessage(errMessage).
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

    public void logOutOnClick(View view) {
        this.startActivity(new Intent(getApplicationContext(), LoginScreenActivity.class));
    }
}
