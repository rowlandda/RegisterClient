package edu.uark.uarkregisterapp.models.api.enums;

        import java.util.HashMap;
        import java.util.Map;

        import edu.uark.uarkregisterapp.models.api.interfaces.PathElementInterface;

public enum TransactionApiMethod implements PathElementInterface {
    NONE(""),
    BY_TRANSACTION_ID("byTransactionID");

    @Override
    public String getPathValue() {
        return value;
    }

    private String value;

    TransactionApiMethod(String value) {
        this.value = value;
    }
}
