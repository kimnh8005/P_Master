package kr.co.pulmuone.v1.comm.api.factory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

@Configuration(value = "httpRequestFactory")
public class HttpRequestFactory {

    /*
     * Request Timeout 설정
     */
    @Value("${erp-api-exchange.request-timeout.connect-timeout}")
    private int connectTimeout; // 서버에 소켓 연결을 맺을 때의 타임아웃 ( 단위 : 초 )

    @Value("${erp-api-exchange.request-timeout.connection-request-timeout}")
    private int connectionRequestTimeout; // Connection Pool 로부터 꺼내올 때의 타임아웃 ( 단위 : 초 )

    @Value("${erp-api-exchange.request-timeout.read-timeout}")
    private int readTimeout; // 읽기시간초과 ( 단위 : 초 )

    public HttpComponentsClientHttpRequestFactory getHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();

        httpComponentsClientHttpRequestFactory.setConnectTimeout(connectTimeout * 1000);
        httpComponentsClientHttpRequestFactory.setConnectionRequestTimeout(connectionRequestTimeout * 1000);
        httpComponentsClientHttpRequestFactory.setReadTimeout(readTimeout * 1000);

        return httpComponentsClientHttpRequestFactory;
    }

}
