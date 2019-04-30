package edu.uark.uarkregisterapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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
            //todo
            //get rid of this try catch block once we fix api
            try {
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
            catch (Exception e)
            {
                this.savingEmployeeAlert.dismiss();
                e.printStackTrace();
                return false;
            }
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
                                    dialog.dismiss();
                                    new RetrieveEmployeesTask().execute();
                                    Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
                                    intent.putExtra(
                                            "current_employee",
                                            CreateEmployeeActivity.this.currentEmployeeTransition
                                    );
                                    CreateEmployeeActivity.this.startActivity(intent);
                                }

                            }
                    ).
                    create().
                    show();
        }

        private AlertDialog savingEmployeeAlert;

        private CreateEmployeeTask() {
            this.savingEmployeeAlert = new AlertDialog.Builder(CreateEmployeeActivity.this).
                    setMessage(R.string.alert_dialog_employee_create).
                    create();
        }
    }



}
