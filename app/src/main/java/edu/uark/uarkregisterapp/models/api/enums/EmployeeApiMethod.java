package edu.uark.uarkregisterapp.models.api.enums;

        import java.util.HashMap;
        import java.util.Map;

        import edu.uark.uarkregisterapp.models.api.interfaces.PathElementInterface;

public enum EmployeeApiMethod implements PathElementInterface {
    NONE(""),
    BY_EMPLOYEE_ID("byemployeeid"),
    BY_LAST_NAME("bylastname");

    @Override
    public String getPathValue() {
        return value;
    }

    private String value;

    EmployeeApiMethod(String value) {
        this.value = value;
    }
}
