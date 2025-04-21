

import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.IOException;
import java.util.UUID;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import pocketgov.presenter.message.DeviceRegisterContract;


/**
 * Model layer implementation class for device registration module, responsible for generating
 * device identifiers and registering devices with the server via HTTP.
 * </p>
 * @author Zhongwei Zhang
 */

public class DeviceRegisterModel implements DeviceRegisterContract.Model {

//    private final OkHttpClient httpClient = new OkHttpClient();
    private OkHttpClient httpClient = new OkHttpClient();    //for testing
//    protected final String serverUrl = "localhost";
    public String serverUrl = "http://localhost:8080/register";    //for testing

    @Override
    public void registerDevice(OnRegisterListener listener) {

        String deviceId = UUID.randomUUID().toString();


        RequestBody body = new FormBody.Builder()
                .add("device_id", deviceId)
                .build();

        Request request = new Request.Builder()
                .url(serverUrl)
                .post(body)
                .build();


        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                listener.onFailure("Network error: " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.isSuccessful()) {
                    listener.onSuccess(deviceId);
                } else {
                    listener.onFailure("Server error: " + response.code());
                }
            }
        });
    }
    //for testing
    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }
    public String getServerUrl() {
        return serverUrl;
    }
    public void setHttpClient(OkHttpClient httpClient) {
        this.httpClient = httpClient;
    }
    public OkHttpClient getHttpClient() {
        return httpClient;
    }
}