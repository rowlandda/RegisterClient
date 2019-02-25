package edu.uark.uarkregisterapp.models.api.fields;

        import edu.uark.uarkregisterapp.models.api.interfaces.FieldNameInterface;

public enum EmployeeFieldName implements FieldNameInterface {
    ID("id"),
    F_NAME("firstName"),
    L_NAME("lastName"),
    EMPLOYEEID("employeeid"),
    ACTIVE("active"),
    ROLE("role"),
    MANAGER("manager"),
    PASSWORD("password"),
    API_REQUEST_STATUS("apiRequestStatus"),
    API_REQUEST_MESSAGE("apiRequestMessage"),
    CREATED_ON("createdOn");

    private String fieldName;
    public String getFieldName() {
        return this.fieldName;
    }

    EmployeeFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}
