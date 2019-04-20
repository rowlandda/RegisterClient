package edu.uark.uarkregisterapp.models.api;

        import org.apache.commons.lang3.ObjectUtils;
        import org.apache.commons.lang3.StringUtils;
        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.text.ParseException;
        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Date;
        import java.util.List;
        import java.util.Locale;
        import java.util.UUID;

        import edu.uark.uarkregisterapp.models.api.fields.TransactionFieldName;
        import edu.uark.uarkregisterapp.models.api.interfaces.ConvertToJsonInterface;
        import edu.uark.uarkregisterapp.models.api.interfaces.LoadFromJsonInterface;
        import edu.uark.uarkregisterapp.models.transition.TransactionTransition;
//todo trouble with serializing
public class Transaction implements ConvertToJsonInterface, LoadFromJsonInterface<Transaction> {

    private UUID id;
    //==================================================
    //===Id Getter and Setter
    //==================================================
    public UUID getId() {
        return this.id;
    }
    public Transaction setId(UUID id) {
        this.id = id;
        return this;
    }

    private ArrayList<Product> shoppingCart;
    //==================================================
    //===Shopping cart getter
    //==================================================
    public ArrayList<Product> getShoppingCart() {
        return this.shoppingCart;
    }

    //==================================================
    //===Add item to cart
    //==================================================
    private boolean addProduct(Product product) {
        try{
            this.shoppingCart.add(product);
            return true;
        }
        catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }

    //==================================================
    //===Clear cart contents
    //==================================================
    private boolean clear() {
        try{
            this.shoppingCart.clear();
            return true;
        }
        catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Date createdOn;
    //==================================================
    //===CreatedOn Getter and Setter
    //==================================================
    public Date getCreatedOn() {
        return this.createdOn;
    }
    public Transaction setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    @Override
    public Transaction loadFromJson(JSONObject rawJsonObject) {
        String value = rawJsonObject.optString(TransactionFieldName.ID.getFieldName());
        if (!StringUtils.isBlank(value)) {
            this.id = UUID.fromString(value);
        }

        this.shoppingCart = new ArrayList<>();
        JSONArray cartArray = rawJsonObject.optJSONArray(TransactionFieldName.SHOPPING_CART.getFieldName());
        try {
            if (cartArray != null) {
                for (int i = 0; i < cartArray.length(); i++) {
                    Product product = new Product();
                    product.loadFromJson(cartArray.getJSONObject(i));
                    this.shoppingCart.add(product);
                }
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        value = rawJsonObject.optString(TransactionFieldName.CREATED_ON.getFieldName());
        if (!StringUtils.isBlank(value)) {
            try {
                this.createdOn = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US).parse(value);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return this;
    }

    @Override
    public JSONObject convertToJson() {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put(TransactionFieldName.ID.getFieldName(), this.id.toString());
            jsonObject.put(TransactionFieldName.SHOPPING_CART.getFieldName(), this.shoppingCart.toArray());
            jsonObject.put(TransactionFieldName.CREATED_ON.getFieldName(),
                    (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US)).format(this.createdOn));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public Transaction() {
        this.id = new UUID(0, 0);
        this.createdOn = new Date();
        this.shoppingCart = new ArrayList<>();
    }

    public Transaction(List<Product> productList){
        this.id = new UUID(0,0);
        this.createdOn = new Date();
        this.shoppingCart = new ArrayList<>();
        for (int i = 0; i < productList.size(); i++) {
            this.shoppingCart.add(productList.get(i));
        }
    }

    public Transaction(TransactionTransition transactionTransition) {
        this.id = transactionTransition.getId();
        this.createdOn = transactionTransition.getCreatedOn();
        this.shoppingCart = new ArrayList<>();
        for (int i = 0; i < transactionTransition.getShoppingCart().size(); i++){
            Product p = new Product(transactionTransition.getShoppingCart().get(i));
            this.shoppingCart.add(p);
        }
    }
}
