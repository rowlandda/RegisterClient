package edu.uark.uarkregisterapp.models.api.fields;

        import edu.uark.uarkregisterapp.models.api.interfaces.FieldNameInterface;

public enum EmployeeFieldName implements FieldNameInterface {
    EMPLOYEEID("employeeID"),
    F_NAME("firstName"),
    L_NAME("lastName"),
    ACTIVE("active"),
    ROLE("role"),
    MANAGER("manage"),
    PASSWORD("password"),
    API_REQUEST_STATUS("apiRequestStatus"),
    API_REQUEST_MESSAGE("apiRequestMessage"),
    ID("id"),
    CREATED_ON("createdOn");

    private String fieldName;
    public String getFieldName() {
        return this.fieldName;
    }

    EmployeeFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}
