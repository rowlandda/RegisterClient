package edu.uark.uarkregisterapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import edu.uark.uarkregisterapp.models.api.ApiResponse;
import edu.uark.uarkregisterapp.models.api.Employee;
import edu.uark.uarkregisterapp.models.api.services.EmployeeService;
import edu.uark.uarkregisterapp.models.transition.EmployeeTransition;

//==========================================
//  Login Page, initial view when app loads
//==========================================

public class LoginScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_view);

        this.employeeTransition = this.getIntent().getParcelableExtra("intent_login_employee");
    }
    private EmployeeTransition employeeTransition;
    View view;
//==========================================
//	Button to proceed with Login process. if
//  successful, displays Home Screen
//==========================================

    public void Login_Attempt_Task(View view) {
        this.view = view;
        Login_Successful_Task(view);
        //i have these two lines commented out so you can still go to home page, they should work in theory but i cant seem to get it
        //new RetrieveEmployeesTask().execute();

        //new LoginCheckTask().execute();
    }

    public void Login_Successful_Task(View view) {
        this.startActivity(new Intent(getApplicationContext(), HomeScreen.class));
    }

    public void No_Employees_Task(View view) {
        this.startActivity(new Intent(getApplicationContext(), CreateEmployeeActivity.class));
    }

    private EditText getEmployee_ID_field() {
        return (EditText) this.findViewById(R.id.employee_id);
    }

    private EditText getEmployee_Password_field() {
        return (EditText) this.findViewById(R.id.employee_password);
    }

    private void Test() {

    }

    private List<Employee> employees;
    //this.employees = new ArrayList<>();

    private class RetrieveEmployeesTask extends AsyncTask<Void, Void, ApiResponse<List<Employee>>> {
        @Override
        protected void onPreExecute() {
            this.LoggingInAlert.show();
        }

        @Override
        protected ApiResponse<List<Employee>> doInBackground(Void... params) {
            ApiResponse<List<Employee>> apiResponse = (new EmployeeService()).getEmployees();

            if (apiResponse.isValidResponse()) {
                employees.clear();
                employees.addAll(apiResponse.getData());
            }



            return apiResponse;
        }

        @Override
        protected void onPostExecute(ApiResponse<List<Employee>> apiResponse) {
            if (apiResponse.isValidResponse()) {
                if (employees.isEmpty()) {
                    this.LoggingInAlert.dismiss();
                    new AlertDialog.Builder(LoginScreenActivity.this).
                            setTitle(R.string.alert_dialog_no_employees_title).
                            setMessage(R.string.alert_dialog_no_employees).
                            setPositiveButton(
                                    R.string.button_ok,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            No_Employees_Task(view);
                                        }
                                    }

                            ).
                            create().
                            show();
                }
                else { //this has been an attempt to fix if i cant use two private classes, as i think that is the issue but am not sure
                    Employee employee = (new Employee()).
                            setEmployeeid(getEmployee_ID_field().getText().toString()).
                            setPassword(getEmployee_Password_field().getText().toString());
                    this.LoggingInAlert.dismiss();
                   // this.startActivity(new Intent(getApplicationContext(), HomeScreen.class));

                }
            }
        }


        private AlertDialog LoggingInAlert;

        private RetrieveEmployeesTask() {
            this.LoggingInAlert = new AlertDialog.Builder(LoginScreenActivity.this).
                    setMessage(R.string.alert_dialog_logging_in).
                    create();
        }
    }

    private class LoginCheckTask extends AsyncTask<Void, Void, ApiResponse<Employee>> {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected ApiResponse<Employee> doInBackground(Void... params) {
            Employee employee = (new Employee()).
                    setEmployeeid(getEmployee_ID_field().getText().toString()).
                    setPassword(getEmployee_Password_field().getText().toString());

            ApiResponse<Employee> apiResponse = (
                    (employee.getEmployeeid().equals(new UUID(0, 0)))
                         ? (new EmployeeService()).loginEmployee(employee)
                         : (new EmployeeService()).getEmployee(employee.getId())
                    );

            if(apiResponse.isValidResponse()) {
                employeeTransition.setFname((apiResponse.getData().getFname()));
                employeeTransition.setLname(apiResponse.getData().getLname());
                employeeTransition.setEmployeeid(apiResponse.getData().getEmployeeid());
                employeeTransition.setPassword(apiResponse.getData().getPassword());

            }

            return apiResponse;
        }

        @Override
        protected  void onPostExecute(ApiResponse<Employee> apiResponse) {
            String message;
            if (apiResponse.isValidResponse()) {
                message = getString(R.string.alert_dialog_login_success);
            }
            else {
                message = getString(R.string.alert_dialog_login_failure);
            }

            AlertDialog loginAlert = new AlertDialog.Builder(LoginScreenActivity.this).
                    setMessage(message).create();

            loginAlert.show();

            if(apiResponse.isValidResponse()) {
                loginAlert.dismiss();
                Login_Successful_Task(view);
            }
            else {
                loginAlert.dismiss();
            }
        }
    }
}
