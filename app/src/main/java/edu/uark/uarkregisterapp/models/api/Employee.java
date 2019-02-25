package edu.uark.uarkregisterapp.models.api;

        import org.apache.commons.lang3.StringUtils;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.text.ParseException;
        import java.text.SimpleDateFormat;
        import java.util.Date;
        import java.util.Locale;
        import java.util.UUID;

        import edu.uark.uarkregisterapp.models.api.fields.EmployeeFieldName;
        import edu.uark.uarkregisterapp.models.api.interfaces.ConvertToJsonInterface;
        import edu.uark.uarkregisterapp.models.api.interfaces.LoadFromJsonInterface;
        import edu.uark.uarkregisterapp.models.transition.EmployeeTransition;

public class Employee implements ConvertToJsonInterface, LoadFromJsonInterface<Employee> {

    //Getters and setters
    private UUID id;
    public UUID getId() {
        return this.id;
    }
    public Employee setId(UUID id) {
        this.id = id;
        return this;
    }

    private String f_name;
    public String getFname() {
        return this.f_name;
    }

    public Employee setFname(String Fname) {
        this.f_name = Fname;
        return this;
    }

    private String l_name;
    public String getLname() {
        return this.l_name;
    }

    public Employee setLname(String Lname) {
        this.l_name = Lname;
        return this;
    }

    private String employeeid;
    public String getEmployeeid() {
        return this.employeeid;
    }

    public Employee setEmployeeid(String employeeid) {
        this.employeeid = employeeid;
        return this;
    }

    private Boolean active;
    public Boolean getActive() {
        return this.active;
    }

    public Employee setActive(String active) {
        this.active = (active.equals("yes")) || (active.equals("Yes")) || (active.equals("true")) || (active.equals("True"));
        return this;
    }

    private String role;
    public String getRole() {
        return this.role;
    }

    public Employee setRole(String role) {
        this.role = role;
        return this;
    }

    //the fieldname in the database is actually "manage".  unsure if this will cause problems later
    private String manager;
    public String getManager() {
        return this.manager;
    }

    public Employee setManager(String manager) {
        this.manager = manager;
        return this;
    }

    private String password;
    public String getPassword() {
        return this.password;
    }

    public Employee setPassword(String password) {
        this.password = password;
        return this;
    }

    private Date createdOn;
    public Date getCreatedOn() {
        return this.createdOn;
    }
    public Employee setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    //this creates an employee from a json representation
    @Override
    public Employee loadFromJson(JSONObject rawJsonObject) {
        String value = rawJsonObject.optString(EmployeeFieldName.ID.getFieldName());
        if (!StringUtils.isBlank(value)) {
            this.id = UUID.fromString(value);
        }

        this.f_name = rawJsonObject.optString(EmployeeFieldName.F_NAME.getFieldName());
        this.l_name = rawJsonObject.optString(EmployeeFieldName.L_NAME.getFieldName());
        this.employeeid = rawJsonObject.optString(EmployeeFieldName.EMPLOYEEID.getFieldName());

        //the string in the active field must read "true" for this to register true
        String active = rawJsonObject.optString(EmployeeFieldName.ACTIVE.getFieldName());
        this.active = active.equals("true");

        this.role = rawJsonObject.optString(EmployeeFieldName.ROLE.getFieldName());
        this.manager = rawJsonObject.optString(EmployeeFieldName.MANAGER.getFieldName());
        this.password = rawJsonObject.optString(EmployeeFieldName.PASSWORD.getFieldName());

        value = rawJsonObject.optString(EmployeeFieldName.CREATED_ON.getFieldName());
        if (!StringUtils.isBlank(value)) {
            try {
                this.createdOn = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US).parse(value);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return this;
    }

    //convert an employee object to json to pass somewhere
    @Override
    public JSONObject convertToJson() {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put(EmployeeFieldName.ID.getFieldName(), this.id.toString());
            jsonObject.put(EmployeeFieldName.F_NAME.getFieldName(), this.f_name);
            jsonObject.put(EmployeeFieldName.L_NAME.getFieldName(), this.l_name);
            jsonObject.put(EmployeeFieldName.EMPLOYEEID.getFieldName(), this.employeeid);
            //remember active is a boolean
            jsonObject.put(EmployeeFieldName.ACTIVE.getFieldName(), this.active.toString());
            jsonObject.put(EmployeeFieldName.ROLE.getFieldName(), this.role);
            jsonObject.put(EmployeeFieldName.MANAGER.getFieldName(), this.manager);
            jsonObject.put(EmployeeFieldName.PASSWORD.getFieldName(), this.password);
            jsonObject.put(EmployeeFieldName.CREATED_ON.getFieldName(), (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US)).format(this.createdOn));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    //constructor with no parameters
    public Employee() {
        this.id = new UUID(0, 0);
        this.f_name = "";
        this.l_name = "";
        this.employeeid = "-1";
        this.active = false;
        this.role = "";
        this.manager = "";
        //default password is password
        this.password = "password";
        this.createdOn = new Date();
    }

    //create an employee object from an employeeTransition object
    public Employee(EmployeeTransition employeeTransition) {
        this.id = employeeTransition.getId();
        this.f_name = employeeTransition.getFname();
        this.l_name = employeeTransition.getLname();
        this.employeeid = employeeTransition.getEmployeeid();
        this.active = employeeTransition.getActive();
        this.role = employeeTransition.getRole();
        this.manager = employeeTransition.getManager();
        this.password = employeeTransition.getPassword();
        this.createdOn = employeeTransition.getCreatedOn();
    }
}
