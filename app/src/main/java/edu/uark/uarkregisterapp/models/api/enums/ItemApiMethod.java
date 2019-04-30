package edu.uark.uarkregisterapp.models.api.enums;

        import java.util.HashMap;
        import java.util.Map;

        import edu.uark.uarkregisterapp.models.api.interfaces.PathElementInterface;

public enum ItemApiMethod implements PathElementInterface {
    NONE("");

    @Override
    public String getPathValue() {
        return value;
    }

    private String value;

    ItemApiMethod(String value) {
        this.value = value;
    }
}
