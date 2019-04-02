package edu.uark.uarkregisterapp;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import edu.uark.uarkregisterapp.models.api.ApiResponse;
import edu.uark.uarkregisterapp.models.api.Employee;
import edu.uark.uarkregisterapp.models.api.services.EmployeeService;
import edu.uark.uarkregisterapp.models.transition.EmployeeTransition;

public class CreateEmployeeActivity extends AppCompatActivity {

    private EmployeeTransition createdEmployeeTransition;
    private EmployeeTransition currentEmployeeTransition;
    private ArrayList<Employee> employees = new ArrayList<>();

    //this is done before everything
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_employee);

        this.currentEmployeeTransition = this.getIntent().getParcelableExtra("current_employee");
        this.createdEmployeeTransition = new EmployeeTransition();
        //get list of all employees from server.
        new RetrieveEmployeesTask().execute();

    }

    //get a list of all employees from server.
    private class RetrieveEmployeesTask extends AsyncTask<Void, ApiResponse<Employee>, ApiResponse<List<Employee>>> {
        @Override
        protected ApiResponse<List<Employee>> doInBackground(Void... params) {
            ApiResponse<List<Employee>> apiResponse = (new EmployeeService()).getEmployees();

            if (apiResponse.isValidResponse()) {
                employees.clear();
                employees.addAll(apiResponse.getData());
            }

            return apiResponse;
        }
    }

    //grab text from the text fields in the view
    private EditText getEmployeeFNameEditText() {
        return (EditText) this.findViewById(R.id.first_name);
    }

    private EditText getEmployeeLNameEditText() {
        return (EditText) this.findViewById(R.id.last_name);
    }

    private EditText getEmployeePasswordEditText() {
        return (EditText) this.findViewById(R.id.employee_password);
    }

    //check for invalid values for name, password
    private boolean validateInput() {
        boolean inputIsValid = true;
        String validationMessage = StringUtils.EMPTY;

        if (StringUtils.isBlank(this.getEmployeeFNameEditText().getText().toString())) {
            validationMessage = this.getString(R.string.validation_first_name);
            inputIsValid = false;
        }

        if (StringUtils.isBlank(this.getEmployeeLNameEditText().getText().toString())) {
            validationMessage = this.getString(R.string.validation_last_name);
            inputIsValid = false;
        }
        if (StringUtils.isBlank(this.getEmployeePasswordEditText().getText().toString())) {
            validationMessage = this.getString(R.string.validation_password);
            inputIsValid = false;
        }

        if (!inputIsValid) {
            new AlertDialog.Builder(this).
                    setMessage(validationMessage).
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

        return inputIsValid;
    }

    @Override
    protected void onResume() {
        super.onResume();

        this.getEmployeeFNameEditText().setText(this.createdEmployeeTransition.getFname());
        this.getEmployeeLNameEditText().setText(this.createdEmployeeTransition.getLname());
        this.getEmployeePasswordEditText().setText(this.createdEmployeeTransition.getPassword());
    }

    public void createEmployeeOnClick(View view) {
        if (!this.validateInput()) {
            return;
        }

        (new CreateEmployeeTask()).execute();
    }

    //this creates an employee object then converts it to json for the server
    private class CreateEmployeeTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            this.savingEmployeeAlert.show();
        }

        //create employee object from the values in the text fields
        @Override
        protected Boolean doInBackground(Void... params) {
            Employee employee = (new Employee()).
                    setEmployeeid(Integer.toString(employees.size())).
                    setFname(getEmployeeFNameEditText().getText().toString()).
                    setLname(getEmployeeLNameEditText().getText().toString()).
                    setPassword(getEmployeePasswordEditText().getText().toString());

            //build the string to send to server.  if uuid is 0's then make new
            //if not then update the the employee tuple
            ApiResponse<Employee> apiResponse = (
                    (employee.getId().equals(new UUID(0, 0)))
                            ? (new EmployeeService()).createEmployee(employee)
                            : (new EmployeeService()).updateEmployee(employee)
            );

            //if successful then make an employeeTransition object to pass to the next
            //view
            if (apiResponse.isValidResponse()) {
                createdEmployeeTransition.setEmployeeid(apiResponse.getData().getEmployeeid());
                createdEmployeeTransition.setFname(apiResponse.getData().getFname());
                createdEmployeeTransition.setLname(apiResponse.getData().getLname());
                createdEmployeeTransition.setPassword(apiResponse.getData().getPassword());
            }

            return apiResponse.isValidResponse();
        }

        //alert notification information
        @Override
        protected void onPostExecute(Boolean successfulSave) {
            String message;

            savingEmployeeAlert.dismiss();

            if (successfulSave) {
                message = getString(R.string.alert_dialog_employee_create_success);
            } else {
                message = getString(R.string.alert_dialog_employee_create_failure);
            }

            new AlertDialog.Builder(CreateEmployeeActivity.this).
                    setMessage(message).
                    setPositiveButton(
                            R.string.home,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //needs to go to homepage but not sure how right now
                                    dialog.dismiss();
                                }

                            }
                    ).
                    create().
                    show();
            //todo
            //go back to transaction page.  this comment is my week one commit(David)
            //refresh employee list
            new RetrieveEmployeesTask().execute();
        }

        private AlertDialog savingEmployeeAlert;

        private CreateEmployeeTask() {
            this.savingEmployeeAlert = new AlertDialog.Builder(CreateEmployeeActivity.this).
                    setMessage(R.string.alert_dialog_employee_create).
                    create();
        }
    }



}
