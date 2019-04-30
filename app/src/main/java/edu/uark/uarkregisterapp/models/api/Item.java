package edu.uark.uarkregisterapp.models.api;

        import org.apache.commons.lang3.StringUtils;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.text.ParseException;
        import java.text.SimpleDateFormat;
        import java.util.Date;
        import java.util.Locale;
        import java.util.UUID;

        import edu.uark.uarkregisterapp.models.api.fields.ItemFieldName;
        import edu.uark.uarkregisterapp.models.api.interfaces.ConvertToJsonInterface;
        import edu.uark.uarkregisterapp.models.api.interfaces.LoadFromJsonInterface;
        import edu.uark.uarkregisterapp.models.transition.ItemTransition;

public class Item implements ConvertToJsonInterface, LoadFromJsonInterface<Item> {

    private UUID id;
    public UUID getId() {
        return this.id;
    }
    public Item setId(UUID id) {
        this.id = id;
        return this;
    }

    private String transactionId;
    public String gettransactionId() {
        return this.transactionId;
    }
    public Item setTransactionId(String transactionId) {
        this.transactionId = transactionId;
        return this;
    }

    private String productName;
    public String getproductName() {
        return this.productName;
    }
    public Item setProductName(String[] names) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < names.length; i++) {
            sb.append(names[i]);
            if (i < names.length-1){
                sb.append(",");
            }
        }
        productName = sb.toString();
        return this;
    }

    private String quantity;
    public String getQuantity() {
        return this.productName;
    }
    public Item setQuantity(String[] quantities) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < quantities.length; i++) {
            sb.append(quantities[i]);
            if (i < quantities.length-1){
                sb.append(",");
            }
        }
        quantity = sb.toString();
        return this;
    }
    private double total;
    //==================================================
    //===Cost Getter and Setter
    //==================================================
    public double getTotal() { return this.total;}
    public Item setTotal(double total) {
        this.total = total;
        return this;
    }

    @Override
    public Item loadFromJson(JSONObject rawJsonObject) {
        String value = rawJsonObject.optString(ItemFieldName.ID.getFieldName());
        if (!StringUtils.isBlank(value)) {
            this.id = UUID.fromString(value);
        }

        this.transactionId = rawJsonObject.optString(ItemFieldName.TRANSACTION_ID.getFieldName());
        this.productName = rawJsonObject.optString(ItemFieldName.PRODUCT_NAMES.getFieldName());
        this.quantity = rawJsonObject.optString(ItemFieldName.QUANTITY.getFieldName());
        this.total = rawJsonObject.optDouble(ItemFieldName.TOTAL.getFieldName());
        return this;
    }

    @Override
    public JSONObject convertToJson() {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put(ItemFieldName.TRANSACTION_ID.getFieldName(), this.transactionId);
            jsonObject.put(ItemFieldName.PRODUCT_NAMES.getFieldName(), this.productName);
            jsonObject.put(ItemFieldName.QUANTITY.getFieldName(), this.quantity);
            jsonObject.put(ItemFieldName.TOTAL.getFieldName(), this.total);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public Item() {
        this.id = new UUID(0, 0);
        this.transactionId = "-1";
        this.productName = "";
        this.quantity = "";
        this.total = -1;
    }

    public Item(ItemTransition itemTransition) {
        this.id = itemTransition.getId();
        this.transactionId = itemTransition.getTransactionID();
        this.productName = itemTransition.getProductName();
        this.quantity = itemTransition.getQuantity();
        this.total = itemTransition.getTotal();
    }
}
