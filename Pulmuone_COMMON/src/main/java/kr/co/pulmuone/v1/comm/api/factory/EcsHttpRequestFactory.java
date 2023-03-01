package kr.co.pulmuone.v1.comm.api.factory;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

@Configuration(value = "ecsHttpRequestFactory")
public class EcsHttpRequestFactory implements InitializingBean {

    /*
     * Request Timeout 설정
     */
    private int connectTimeout = 5; // 서버에 소켓 연결을 맺을 때의 타임아웃 ( 단위 : 초 )

    private int connectionRequestTimeout = 5; // Connection Pool 로부터 꺼내올 때의 타임아웃 ( 단위 : 초 )

    private int readTimeout = 5; // 읽기시간초과 ( 단위 : 초 )

    /*
     * Connection Pooling 설정
     */
    @Value("${erp-api-exchange.connection-pooling.max-conn-total}")
    private int maxConnTotal; // 최대 커넥션 갯수

    @Value("${erp-api-exchange.connection-pooling.max-conn-per-route}")
    private int maxConnPerRoute; // IP/domain port 하나 당 최대 커넥션 갯수

    private HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory;

    // 객체 생성 / @Value 값 주입 완료 후 connection 정보 세팅
    @Override
    public void afterPropertiesSet() throws Exception {

        this.httpComponentsClientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();

        httpComponentsClientHttpRequestFactory.setConnectTimeout(connectTimeout * 1000);
        httpComponentsClientHttpRequestFactory.setConnectionRequestTimeout(connectionRequestTimeout * 1000);
        httpComponentsClientHttpRequestFactory.setReadTimeout(readTimeout * 1000);

        CloseableHttpClient httpClient = HttpClientBuilder.create() //
                .setMaxConnTotal(maxConnTotal) //
                .setMaxConnPerRoute(maxConnPerRoute) //
                .build();

        httpComponentsClientHttpRequestFactory.setHttpClient(httpClient);
    }

    public HttpComponentsClientHttpRequestFactory getHttpRequestFactory() {
        return this.httpComponentsClientHttpRequestFactory;
    }

}
