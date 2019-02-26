package edu.uark.uarkregisterapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.support.v7.app.AppCompatActivity;

//==========================================
//  Login Page, initial view when app loads
//==========================================

public class LoginScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_view);
    }
//==========================================
//	Button to proceed with Login process. if
//  successful, displays landing page
//==========================================

    public void displayLandingPageOnClick(View view) {
        this.startActivity(new Intent(getApplicationContext(), LandingActivity.class));
    }
}
