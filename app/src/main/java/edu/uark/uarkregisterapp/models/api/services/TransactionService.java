package edu.uark.uarkregisterapp.models.api.services;

        import android.util.Log;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.ArrayList;
        import java.util.List;
        import java.util.UUID;

        import edu.uark.uarkregisterapp.models.api.ApiResponse;
        import edu.uark.uarkregisterapp.models.api.Transaction;
        import edu.uark.uarkregisterapp.models.api.enums.ApiObject;
        import edu.uark.uarkregisterapp.models.api.enums.TransactionApiMethod;
        import edu.uark.uarkregisterapp.models.api.interfaces.PathElementInterface;

public class TransactionService extends BaseRemoteService {
    public ApiResponse<Transaction> getTransaction(UUID productId) {
        return this.readTransactionDetailsFromResponse(
                this.<Transaction>performGetRequest(
                        this.buildPath(productId)
                )
        );
    }

    public ApiResponse<Transaction> getTransactionByID(String transactionID) {
        return this.readTransactionDetailsFromResponse(
                this.<Transaction>performGetRequest(
                        this.buildPath(
                                (new PathElementInterface[] { TransactionApiMethod.BY_TRANSACTION_ID })
                                , transactionID
                        )
                )
        );
    }

    public ApiResponse<List<Transaction>> getTransactions() {
        ApiResponse<List<Transaction>> apiResponse = this.performGetRequest(
                this.buildPath()
        );

        JSONArray rawJsonArray = this.rawResponseToJSONArray(apiResponse.getRawResponse());
        if (rawJsonArray != null) {
            ArrayList<Transaction> transactions = new ArrayList<>(rawJsonArray.length());
            for (int i = 0; i < rawJsonArray.length(); i++) {
                try {
                    transactions.add((new Transaction()).loadFromJson(rawJsonArray.getJSONObject(i)));
                } catch (JSONException e) {
                    Log.d("GET TRANSACTIONS", e.getMessage());
                }
            }

            apiResponse.setData(transactions);
        } else {
            apiResponse.setData(new ArrayList<Transaction>(0));
        }

        return apiResponse;
    }

    public ApiResponse<Transaction> updateTransaction(Transaction transaction) {
        return this.readTransactionDetailsFromResponse(
                this.<Transaction>performPutRequest(
                        this.buildPath(transaction.getId())
                        , transaction.convertToJson()
                )
        );
    }

    public ApiResponse<Transaction> createTransaction(Transaction transaction) {
        return this.readTransactionDetailsFromResponse(
                this.<Transaction>performPostRequest(
                        this.buildPath()
                        , transaction.convertToJson()
                )
        );
    }

    public ApiResponse<Transaction> startTransaction(JSONObject transaction) {
        return this.readTransactionDetailsFromResponse(
                this.<Transaction>performPostRequest(
                        this.buildPath()
                        , transaction
                        )
                );
    }

    public ApiResponse<String> deleteTransaction(UUID transactionId) {
        return this.<String>performDeleteRequest(
                this.buildPath(transactionId)
        );
    }

    private ApiResponse<Transaction> readTransactionDetailsFromResponse(ApiResponse<Transaction> apiResponse) {
        JSONObject rawJsonObject = this.rawResponseToJSONObject(
                apiResponse.getRawResponse()
        );

        if (rawJsonObject != null) {
            apiResponse.setData(
                    (new Transaction()).loadFromJson(rawJsonObject)
            );
        }

        return apiResponse;
    }

    public TransactionService() { super(ApiObject.TRANSACTION); }
}
