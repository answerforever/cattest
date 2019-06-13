package feign;

import com.answer.test.request.Service3RdContext;
import feign.Client.Default;
import feign.Request;
import feign.Request.Options;
import feign.Response;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;


public class MyFeignClient extends Default {
    /**
     * Null parameters imply platform defaults.
     *
     * @param sslContextFactory
     * @param hostnameVerifier
     */
    public MyFeignClient(SSLSocketFactory sslContextFactory,
                         HostnameVerifier hostnameVerifier) {
        super(sslContextFactory, hostnameVerifier);
    }

    @Override
    public Response execute(Request request, Options options) throws IOException {
        String url = request == null ? "" : request.url();
        Map<String, String> service3RdContext = Service3RdContext.getContext();
        if (service3RdContext == null) {
            service3RdContext = new HashMap<>();
        }
        service3RdContext.put(Service3RdContext.URL, url);
        Service3RdContext.setHeaderContext(service3RdContext);

        HttpURLConnection connection = convertAndSend(request, options);
        Response response = convertResponse(connection).toBuilder().request(request).build();
        return response;
    }
}