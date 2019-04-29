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

    private int transactionID;
    public int getTransactionID() {
        return this.transactionID;
    }
    public TransactionTransition setTransactionID(int transactionID) {
        this.transactionID = transactionID;
        return this;
    }

    private String cashierID;
    public String getCashierID() {
        return this.cashierID;
    }
    public TransactionTransition setCashierID(String cashierID) {
        this.cashierID = cashierID;
        return this;
    }

    private double totalSales;
    public double getTotalSales() {
        return this.totalSales;
    }
    public TransactionTransition setTotalSales(double totalSales) {
        this.totalSales = totalSales;
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
        destination.writeInt(this.transactionID);
        destination.writeString(this.cashierID);
        destination.writeDouble(this.totalSales);
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
        this.transactionID = -1;
        this.cashierID = "-1";
        //todo change all these to double
        this.totalSales = 0.00;
        this.createdOn = new Date();
        this.shoppingCart = new ArrayList<>();
    }

    public TransactionTransition(Transaction transaction) {
        this.id = transaction.getId();
        this.transactionID = transaction.getTransactionID();
        this.cashierID = transaction.getCashierID();
        this.totalSales = transaction.getTotalSales();
        this.createdOn = transaction.getCreatedOn();
        this.shoppingCart = new ArrayList<>();
        for (int i = 0; i < transaction.getShoppingCart().size(); i++){
            ProductTransition p = new ProductTransition(transaction.getShoppingCart().get(i));
            this.shoppingCart.add(p);
        }
    }

    private TransactionTransition(Parcel transactionTransitionParcel) {
        this.id = (new ByteToUUIDConverterCommand()).setValueToConvert(transactionTransitionParcel.createByteArray()).execute();
        this.transactionID = transactionTransitionParcel.readInt();
        this.cashierID = transactionTransitionParcel.readString();
        this.transactionID = transactionTransitionParcel.readInt();
        this.createdOn = new Date();
        this.createdOn.setTime(transactionTransitionParcel.readLong());
        this.shoppingCart = new ArrayList<>();
    }
}
