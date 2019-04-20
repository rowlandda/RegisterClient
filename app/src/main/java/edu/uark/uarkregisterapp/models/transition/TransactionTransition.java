package edu.uark.uarkregisterapp.models.transition;

        import android.os.Parcel;
        import android.os.Parcelable;

        import org.apache.commons.lang3.StringUtils;

        import java.util.ArrayList;
        import java.util.Date;
        import java.util.UUID;

        import edu.uark.uarkregisterapp.commands.converters.ByteToUUIDConverterCommand;
        import edu.uark.uarkregisterapp.commands.converters.UUIDToByteConverterCommand;
        import edu.uark.uarkregisterapp.models.api.Product;
        import edu.uark.uarkregisterapp.models.api.Transaction;

public class TransactionTransition implements Parcelable {
    private UUID id;
    public UUID getId() {
        return this.id;
    }
    public TransactionTransition setId(UUID id) {
        this.id = id;
        return this;
    }

    private Date createdOn;
    public Date getCreatedOn() {
        return this.createdOn;
    }

    public TransactionTransition setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    private ArrayList<ProductTransition> shoppingCart;
    public ArrayList<ProductTransition> getShoppingCart() {
        return this.shoppingCart;
    }

    @Override
    public void writeToParcel(Parcel destination, int flags) {
        destination.writeByteArray((new UUIDToByteConverterCommand()).setValueToConvert(this.id).execute());
        destination.writeLong(this.createdOn.getTime());
        destination.writeTypedList(this.shoppingCart);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<TransactionTransition> CREATOR = new Parcelable.Creator<TransactionTransition>() {
        public TransactionTransition createFromParcel(Parcel transactionTransitionParcel) {
            return new TransactionTransition(transactionTransitionParcel);
        }

        public TransactionTransition[] newArray(int size) {
            return new TransactionTransition[size];
        }
    };

    public TransactionTransition() {
        this.id = new UUID(0, 0);
        this.createdOn = new Date();
        this.shoppingCart = new ArrayList<>();
    }

    public TransactionTransition(Transaction transaction) {
        this.id = transaction.getId();
        this.createdOn = transaction.getCreatedOn();
        this.shoppingCart = new ArrayList<>();
        for (int i = 0; i < transaction.getShoppingCart().size(); i++){
            ProductTransition p = new ProductTransition(transaction.getShoppingCart().get(i));
            this.shoppingCart.add(p);
        }
    }

    private TransactionTransition(Parcel transactionTransitionParcel) {
        this.id = (new ByteToUUIDConverterCommand()).setValueToConvert(transactionTransitionParcel.createByteArray()).execute();
        this.createdOn = new Date();
        this.createdOn.setTime(transactionTransitionParcel.readLong());
        this.shoppingCart = new ArrayList<>();
    }
}
