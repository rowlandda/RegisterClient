package edu.uark.uarkregisterapp.models.api.fields;

import edu.uark.uarkregisterapp.models.api.interfaces.FieldNameInterface;

public enum TransactionFieldName implements FieldNameInterface {
    ID("id"),
    SHOPPING_CART("shoppingCart"),
    API_REQUEST_STATUS("apiRequestStatus"),
    API_REQUEST_MESSAGE("apiRequestMessage"),
    CREATED_ON("createdOn");

    private String fieldName;
    public String getFieldName() {
        return this.fieldName;
    }

    TransactionFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}

