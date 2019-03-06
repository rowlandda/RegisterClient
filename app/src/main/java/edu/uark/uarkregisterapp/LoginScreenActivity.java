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

import edu.uark.uarkregisterapp.models.api.ApiResponse;
import edu.uark.uarkregisterapp.models.api.Employee;
import edu.uark.uarkregisterapp.models.api.services.EmployeeService;
import edu.uark.uarkregisterapp.models.transition.EmployeeTransition;

public class LoginScreenActivity extends AppCompatActivity {
    private EmployeeTransition employeeTransition = new EmployeeTransition();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_view);

        this.employees = new ArrayList<>();
    }

    View view;

    public void Login_Attempt_Task(View view) {
        this.view = view;

        new RetrieveEmployeesTask().execute();
        new LoginCheckTask().execute();
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

    private class RetrieveEmployeesTask extends AsyncTask<Void, ApiResponse<Employee>, ApiResponse<List<Employee>>> {
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
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.dismiss();
                                            No_Employees_Task(view);
                                        }
                                    }
                            ).
                            create().
                            show();
                }
                else {
                    this.LoggingInAlert.dismiss();
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
        protected ApiResponse<Employee> doInBackground(Void... params) {
            Employee employee = (new Employee()).
                    setEmployeeid(getEmployee_ID_field().getText().toString()).
                    setPassword(getEmployee_Password_field().getText().toString());

            ApiResponse<Employee> apiResponse = (
                          (new EmployeeService()).loginEmployee(employee.convertToLoginJson()));

             if(apiResponse.isValidResponse()) {
                 employeeTransition.setFname(apiResponse.getData().getFname());
                 employeeTransition.setLname(apiResponse.getData().getLname());
                 employeeTransition.setActive(apiResponse.getData().getActive());
                 employeeTransition.setManager(apiResponse.getData().getManager());
                 employeeTransition.setEmployeeid(apiResponse.getData().getEmployeeid());
             }

            return apiResponse;
        }

        @Override
        protected  void onPostExecute(ApiResponse<Employee> apiResponse) {
            if (apiResponse.isValidResponse()) {
                Intent intent = new Intent(getApplicationContext(), HomeScreen.class);

                intent.putExtra(
                        getString(R.string.intent_login_employee),
                        employeeTransition
                );

                    LoginSuccessAlert.show();
                    LoginSuccessAlert.dismiss();
                    Login_Successful_Task(view);
            }
            else {
               LoginFailureAlert.show();
            }
        }

        private AlertDialog LoginSuccessAlert, LoginFailureAlert;

        private LoginCheckTask() {
            this.LoginSuccessAlert = new AlertDialog.Builder(LoginScreenActivity.this).
                    setMessage(R.string.alert_dialog_login_success).
                    create();

            this.LoginFailureAlert = new AlertDialog.Builder(LoginScreenActivity.this).
                    setMessage(R.string.alert_dialog_login_failure).
                    setPositiveButton(
                        R.string.button_ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        }
                    ).
                    create();
        }
    }
    private List<Employee> employees;
}
