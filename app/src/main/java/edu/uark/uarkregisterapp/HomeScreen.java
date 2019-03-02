package edu.uark.uarkregisterapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import org.apache.commons.lang3.StringUtils;

import edu.uark.uarkregisterapp.models.transition.EmployeeTransition;

public class HomeScreen extends AppCompatActivity {

    String errMessage = "Functionality Not Available";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
    }

    public void startTransactionOnClick(View view) {
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


    public void createNewEmployeeButton(View view) {
        Intent intent = new Intent(getApplicationContext(), CreateEmployeeActivity.class);

        intent.putExtra(
                getString(R.string.intent_create_employee),
                new EmployeeTransition()
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
