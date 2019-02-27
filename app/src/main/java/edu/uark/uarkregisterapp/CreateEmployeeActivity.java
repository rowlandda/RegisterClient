package edu.uark.uarkregisterapp;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import org.apache.commons.lang3.StringUtils;

import java.util.Locale;
import java.util.UUID;

import edu.uark.uarkregisterapp.models.api.ApiResponse;
import edu.uark.uarkregisterapp.models.api.Employee;
import edu.uark.uarkregisterapp.models.api.services.EmployeeService;
import edu.uark.uarkregisterapp.models.transition.EmployeeTransition;

public class CreateEmployeeActivity extends AppCompatActivity {

    private EmployeeTransition employeeTransition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_employee);

        this.employeeTransition = this.getIntent().getParcelableExtra("intent_create_employee");
    }

    private EditText getEmployeeFNameEditText() {
        return (EditText) this.findViewById(R.id.first_name);
    }

    private EditText getEmployeeLNameEditText() {
        return (EditText) this.findViewById(R.id.last_name);
    }

    private EditText getEmployeePasswordEditText() {
        return (EditText) this.findViewById(R.id.employee_password);
    }

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

        this.getEmployeeFNameEditText().setText(this.employeeTransition.getFname());
        this.getEmployeeLNameEditText().setText(this.employeeTransition.getLname());
        this.getEmployeePasswordEditText().setText(this.employeeTransition.getPassword());
    }

    public void createEmployeeOnClick(View view) {
        if (!this.validateInput()) {
            return;
        }

        (new CreateEmployeeTask()).execute();
    }

    private class CreateEmployeeTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            this.savingEmployeeAlert.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Employee employee = (new Employee()).
                    setId(employeeTransition.getId()).
                    setFname(getEmployeeFNameEditText().getText().toString()).
                    setLname(getEmployeeLNameEditText().getText().toString()).
                    setPassword(getEmployeePasswordEditText().getText().toString());

            ApiResponse<Employee> apiResponse = (
                    (employee.getId().equals(new UUID(0, 0)))
                            ? (new EmployeeService()).createEmployee(employee)
                            : (new EmployeeService()).updateEmployee(employee)
            );

            if (apiResponse.isValidResponse()) {
                employeeTransition.setFname(apiResponse.getData().getFname());
                employeeTransition.setLname(apiResponse.getData().getLname());
            }

            return apiResponse.isValidResponse();
        }

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

        private AlertDialog savingEmployeeAlert;

        private CreateEmployeeTask() {
            this.savingEmployeeAlert = new AlertDialog.Builder(CreateEmployeeActivity.this).
                    setMessage(R.string.alert_dialog_employee_create).
                    create();
        }
    }



}