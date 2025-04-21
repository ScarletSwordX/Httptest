

import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import static org.junit.Assert.*;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import pocketgov.model.message.DeviceRegisterModel;
import pocketgov.presenter.message.DeviceRegisterContract;
import pocketgov.presenter.message.DeviceRegisterPresenter;
import pocketgov.view.message.DeviceRegisterView;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


/**
 * Unit tests for the DeviceRegisterModel class
 *
 * @author Zhongwei Zhang
 */

public class DeviceRegisterTest {
    private MockWebServer mockWebServer;
    private DeviceRegisterModel model;
    private DeviceRegisterPresenter presenter;
    private DeviceRegisterView mockView;

    @Before
    public void setup() throws Exception {
        // 1. 创建模拟服务器
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        // 2. 配置Model使用模拟服务器地址
        String serverUrl = mockWebServer.url("/register").toString();
        model = new DeviceRegisterModel() ;
        model.serverUrl = serverUrl;
        mockView = new DeviceRegisterView() {
        };

        presenter = new DeviceRegisterPresenter(model, mockView);
    }

    private String lastSuccessId;
    private String lastError;
    private CountDownLatch latch;
    @Test
    public void testDeviceRegister() throws Exception{

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"status\":\"success\"}"));
        latch = new CountDownLatch(1);
        presenter.registerDevice();

        // 等待异步回调完成（最多2秒）
        assertTrue(latch.await(2, TimeUnit.SECONDS));

        // 验证结果
        assertNotNull(lastSuccessId);
        assertNull(lastError);

        // 验证请求参数
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("POST", request.getMethod());
        assertTrue(request.getBody().readUtf8().contains("device_id"));

    }

    @Test
    public void testRegisterDeviceTimeout() throws Exception {
        // 1. 模拟请求超时
        mockWebServer.enqueue(new MockResponse()
                .setBodyDelay(3, TimeUnit.SECONDS)  // 延迟3秒响应
                .setResponseCode(200));

        latch = new CountDownLatch(1);
        presenter.registerDevice();

        // 设置更短的超时时间（客户端配置）
        model.setHttpClient(model.getHttpClient().newBuilder()
                .readTimeout(1, TimeUnit.SECONDS)
                .build());

        assertTrue(latch.await(2, TimeUnit.SECONDS));
        assertTrue(lastError.contains("Timeout"));
        assertNull(lastSuccessId);
    }
    @After
    public void cleanup() throws Exception {
        mockWebServer.shutdown();
    }


}