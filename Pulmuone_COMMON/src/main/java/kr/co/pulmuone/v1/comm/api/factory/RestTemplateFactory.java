package kr.co.pulmuone.v1.comm.api.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Configuration(value = "restTemplateFactory")
@DependsOn(value = "httpRequestFactory") // CustomRestTemplate 클래스는 HttpRequestFactory 클래스에 의존
public class RestTemplateFactory {

    @SuppressWarnings("unused") // 별도 log 이름 지정
    private Logger log = LoggerFactory.getLogger("RestTemplateFactory");

    @Autowired
    private HttpRequestFactory httpRequestFactory;

    private List<ClientHttpRequestInterceptor> interceptors = Collections.singletonList(new RequestResponseLoggingInterceptor());
    private RestTemplateResponseErrorHandler errorHandler = new RestTemplateResponseErrorHandler();

    /*
     * RestTemplate 반환 메서드 : 각 통신마다 restTemplate 를 생성하지 않고 변수로 지정된 객체를 재활용
     */
    public RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate(httpRequestFactory.getHttpRequestFactory());

        restTemplate.setInterceptors(interceptors);
        restTemplate.setErrorHandler(errorHandler);

        return restTemplate;
    }

    /*
     * RestTemplate 전용 Interceptor
     */
    class RequestResponseLoggingInterceptor implements ClientHttpRequestInterceptor {

        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

            ClientHttpResponse clientHttpResponse = null;

            // request 로그 영역

            clientHttpResponse = execution.execute(request, body);

            // response 로그 영역

            return clientHttpResponse;

        }

    }

    /*
     * RestTemplate 전용 ResponseErrorHandler
     */
    public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {

        @Override
        public boolean hasError(final ClientHttpResponse clientHttpResponse) throws IOException {

            // 200 OK 가 아닌 경우 예외로 판정
            return (clientHttpResponse.getStatusCode() != HttpStatus.OK);

        }

        @Override
        public void handleError(final ClientHttpResponse clientHttpResponse) throws IOException {

            // hasError 에서 true 를 return 하면 해당 메서드 실행.

        }

    }

}
