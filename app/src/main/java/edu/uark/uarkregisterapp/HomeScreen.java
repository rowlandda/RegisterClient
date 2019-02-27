package edu.uark.uarkregisterapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import edu.uark.uarkregisterapp.models.transition.EmployeeTransition;

public class HomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
    }

    public void createNewEmployeeButton(View view) {
        Intent intent = new Intent(getApplicationContext(), CreateEmployeeActivity.class);

        intent.putExtra(
                getString(R.string.intent_create_employee),
                new EmployeeTransition()
        );
        this.startActivity(intent);
    }
}
