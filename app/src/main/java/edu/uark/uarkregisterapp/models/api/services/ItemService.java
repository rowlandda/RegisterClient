package edu.uark.uarkregisterapp.models.api.services;

        import android.util.Log;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.ArrayList;
        import java.util.List;
        import java.util.UUID;

        import edu.uark.uarkregisterapp.models.api.ApiResponse;
        import edu.uark.uarkregisterapp.models.api.Item;
        import edu.uark.uarkregisterapp.models.api.enums.ApiObject;
        import edu.uark.uarkregisterapp.models.api.enums.ItemApiMethod;
        import edu.uark.uarkregisterapp.models.api.interfaces.PathElementInterface;

public class ItemService extends BaseRemoteService {
    public ApiResponse<Item> createItem(Item item) {
        return this.readItemDetailsFromResponse(
                this.<Item>performPostRequest(
                        this.buildPath()
                        , item.convertToJson()
                )
        );
    }

    private ApiResponse<Item> readItemDetailsFromResponse(ApiResponse<Item> apiResponse) {
        JSONObject rawJsonObject = this.rawResponseToJSONObject(
                apiResponse.getRawResponse()
        );

        if (rawJsonObject != null) {
            apiResponse.setData(
                    (new Item()).loadFromJson(rawJsonObject)
            );
        }

        return apiResponse;
    }

    public ItemService() { super(ApiObject.ITEM); }
}
