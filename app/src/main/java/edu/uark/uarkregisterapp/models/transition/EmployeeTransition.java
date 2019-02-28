package edu.uark.uarkregisterapp.models.transition;

import android.os.Parcel;
import android.os.Parcelable;

import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.UUID;

import edu.uark.uarkregisterapp.commands.converters.ByteToUUIDConverterCommand;
import edu.uark.uarkregisterapp.commands.converters.UUIDToByteConverterCommand;
import edu.uark.uarkregisterapp.models.api.Employee;

public class EmployeeTransition implements Parcelable {
    private UUID id;
    public UUID getId() {
        return this.id;
    }
    public EmployeeTransition setId(UUID id) {
        this.id = id;
        return this;
    }

    private String f_name;
    public String getFname() {
        return this.f_name;
    }

    public EmployeeTransition setFname(String Fname) {
        this.f_name = Fname;
        return this;
    }

    private String l_name;
    public String getLname() {
        return this.l_name;
    }

    public EmployeeTransition setLname(String Lname) {
        this.l_name = Lname;
        return this;
    }

    private String employeeid;
    public String getEmployeeid() {
        return this.employeeid;
    }

    public EmployeeTransition setEmployeeid(String employeeid) {
        this.employeeid = employeeid;
        return this;
    }

    private Boolean active;
    public Boolean getActive() {
        return this.active;
    }

    public EmployeeTransition setActive(String active) {
        this.active = (active.equals("yes")) || (active.equals("Yes")) ||
                      (active.equals("true")) || (active.equals("True"));
        return this;
    }

    private String role;
    public String getRole() {
        return this.role;
    }

    public EmployeeTransition setRole(String role) {
        this.role = role;
        return this;
    }

    //the fieldname in the database is actually manage.  unsure if this will cause problems later
    private String manager;
    public String getManager() {
        return this.manager;
    }

    public EmployeeTransition setManager(String manager) {
        this.manager = manager;
        return this;
    }

    private String password;
    public String getPassword() {
        return this.password;
    }

    public EmployeeTransition setPassword(String password) {
        this.password = password;
        return this;
    }

    private Date createdOn;
    public Date getCreatedOn() {
        return this.createdOn;
    }
    public EmployeeTransition setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    @Override
    public void writeToParcel(Parcel destination, int flags) {
        destination.writeByteArray((new UUIDToByteConverterCommand()).setValueToConvert(this.id).execute());
        destination.writeString(this.f_name);
        destination.writeString(this.l_name);
        destination.writeString(this.employeeid);

        //convert boolean variable 'active' to string
        String activeString;
        if (this.active)
            activeString = "true";
        else
            activeString = "false";
        destination.writeString(activeString);

        destination.writeString(this.role);
        destination.writeString(this.manager);
        destination.writeString(this.password);
        destination.writeLong(this.createdOn.getTime());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<EmployeeTransition> CREATOR = new Parcelable.Creator<EmployeeTransition>() {
        public EmployeeTransition createFromParcel(Parcel employeeTransitionParcel) {
            return new EmployeeTransition(employeeTransitionParcel);
        }

        public EmployeeTransition[] newArray(int size) {
            return new EmployeeTransition[size];
        }
    };

    //create an employee without parameters
    public EmployeeTransition() {
        this.id = new UUID(0, 0);
        this.f_name = StringUtils.EMPTY;
        this.l_name = StringUtils.EMPTY;
        this.employeeid = "-1";
        this.active = false;
        this.role = StringUtils.EMPTY;
        this.manager = StringUtils.EMPTY;
        this.password = StringUtils.EMPTY;
        this.createdOn = new Date();
    }

    //make an employee transition object from a regular employee object
    public EmployeeTransition(Employee employee) {
        this.id = employee.getId();
        this.f_name = employee.getFname();
        this.l_name = employee.getLname();
        this.employeeid = employee.getEmployeeid();
        this.active = employee.getActive();
        this.role = employee.getRole();
        this.manager = employee.getManager();
        this.password = employee.getPassword();
        this.createdOn = employee.getCreatedOn();
    }

    //copy constructor???
    private EmployeeTransition(Parcel employeeTransitionParcel) {
        this.id = (new ByteToUUIDConverterCommand()).setValueToConvert(employeeTransitionParcel.createByteArray()).execute();
        this.f_name = employeeTransitionParcel.readString();
        this.l_name = employeeTransitionParcel.readString();
        this.employeeid = employeeTransitionParcel.readString();

        //the choice of active as a boolean seems like its more trouble than its worth at this point
        String activeString = employeeTransitionParcel.readString();
        if (activeString != null)
            this.active = activeString.equals("true");

        this.role = employeeTransitionParcel.readString();
        this.manager = employeeTransitionParcel.readString();
        this.password = employeeTransitionParcel.readString();

        this.createdOn = new Date();
        this.createdOn.setTime(employeeTransitionParcel.readLong());
    }
}
