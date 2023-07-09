package util.http;

import okhttp3.*;

import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;


public class HttpClientUtil {

    //Logger.getLogger(OkHttpClient.class.getName()).setLevel(Level.FINE);

    private static final Logger LOGGER = Logger.getLogger(HttpClientUtil.class.getName());
    private final static SimpleCookieManager simpleCookieManager = new SimpleCookieManager();

    private final static OkHttpClient HTTP_CLIENT;

    static {
        LOGGER.setLevel(Level.FINE);
        HTTP_CLIENT =
                new OkHttpClient.Builder()
                        .cookieJar(simpleCookieManager)
                        .followRedirects(false)
                        .build();
    }
    
    public static void setCookieManagerLoggingFacility(Consumer<String> logConsumer) {
        simpleCookieManager.setLogData(logConsumer);
    }

    public static void removeCookiesOf(String domain) {
        simpleCookieManager.removeCookiesOf(domain);
    }

    public static void runAsync(String finalUrl, Callback callback) {
        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        Call call = HttpClientUtil.HTTP_CLIENT.newCall(request);

        call.enqueue(callback);
    }

    public static void runAsyncPost(String finalUrl, RequestBody body, Callback callback) {
        Request request = (new Request.Builder()).url(finalUrl).post(body).build();
        Call call = HTTP_CLIENT.newCall(request);
        call.enqueue(callback);
    }

    public static void shutdown() {
        System.out.println("Shutting down HTTP CLIENT");
        HTTP_CLIENT.dispatcher().executorService().shutdown();
        HTTP_CLIENT.connectionPool().evictAll();
    }
}
