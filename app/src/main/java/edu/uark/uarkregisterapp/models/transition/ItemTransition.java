package edu.uark.uarkregisterapp.models.transition;

        import android.os.Parcel;
        import android.os.Parcelable;

        import org.apache.commons.lang3.StringUtils;

        import java.util.UUID;

        import edu.uark.uarkregisterapp.commands.converters.ByteToUUIDConverterCommand;
        import edu.uark.uarkregisterapp.commands.converters.UUIDToByteConverterCommand;
        import edu.uark.uarkregisterapp.models.api.Item;

//this class is the form we send employee objects between screens/views
public class ItemTransition implements Parcelable {
    private UUID id;
    public UUID getId() {
        return this.id;
    }
    public ItemTransition setId(UUID id) {
        this.id = id;
        return this;
    }

    private String transactionID;
    public String getTransactionID() {
        return this.transactionID;
    }

    public ItemTransition setTransactionID(String transactionID) {
        this.transactionID = transactionID;
        return this;
    }

    private String productName;
    public String getProductName() {
        return this.productName;
    }
    public ItemTransition setProductName(String[] names) {
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
    public ItemTransition setQuantity(String[] quantities) {
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
    public double getTotal() {
        return this.total;
    }

    public ItemTransition setTotal(Double total) {
        this.total = total;
        return this;
    }

    @Override
    public void writeToParcel(Parcel destination, int flags) {
        destination.writeByteArray((new UUIDToByteConverterCommand()).setValueToConvert(this.id).execute());
        destination.writeString(this.transactionID);
        destination.writeString(this.productName);
        destination.writeString(this.quantity);
        destination.writeDouble(this.total);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<ItemTransition> CREATOR = new Parcelable.Creator<ItemTransition>() {
        public ItemTransition createFromParcel(Parcel itemTransitionParcel) {
            return new ItemTransition(itemTransitionParcel);
        }

        public ItemTransition[] newArray(int size) {
            return new ItemTransition[size];
        }
    };

    //create an employee without parameters
    public ItemTransition() {
        this.id = new UUID(0, 0);
        this.transactionID = StringUtils.EMPTY;
        this.productName = StringUtils.EMPTY;
        this.quantity = StringUtils.EMPTY;
        this.total = 0;
    }

    //make an employee transition object from a regular employee object
    public ItemTransition(Item item) {
        this.id = item.getId();
        this.transactionID = item.gettransactionId();
        this.productName = item.getproductName();
        this.quantity = item.getQuantity();
        this.total = item.getTotal();
    }

    //copy constructor
    private ItemTransition(Parcel itemTransitionParcel) {
        this.id = (new ByteToUUIDConverterCommand()).setValueToConvert(itemTransitionParcel.createByteArray()).execute();
        this.transactionID = itemTransitionParcel.readString();
        this.productName = itemTransitionParcel.readString();
        this.quantity = itemTransitionParcel.readString();
        this.total = itemTransitionParcel.readDouble();
    }

}
