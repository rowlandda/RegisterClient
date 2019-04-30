package edu.uark.uarkregisterapp.models.api.fields;

        import edu.uark.uarkregisterapp.models.api.interfaces.FieldNameInterface;

public enum ItemFieldName implements FieldNameInterface {
    ID("id"),
    TRANSACTION_ID("transactionId"),
    PRODUCT_NAMES("productName"),
    QUANTITY("quantity"),
    TOTAL("total"),
    API_REQUEST_STATUS("apiRequestStatus"),
    API_REQUEST_MESSAGE("apiRequestMessage");

    private String fieldName;
    public String getFieldName() {
        return this.fieldName;
    }

    ItemFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}
