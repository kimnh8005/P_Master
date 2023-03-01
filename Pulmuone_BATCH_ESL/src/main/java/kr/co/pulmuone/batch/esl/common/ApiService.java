package kr.co.pulmuone.batch.esl.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class ApiService {
    @Autowired
    RestTemplate restTemplate;

    private HttpEntity<?> apiClientHttpEntity(String params) {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.set(ApiConstant.CONTENT_TYPE, new String(ApiConstant.JSON_CONTENT));

        if (StringUtils.isEmpty(params)) return new HttpEntity<Object>(requestHeaders);
        else return new HttpEntity<Object>(params, requestHeaders);
    }

    public String callRest(String resourceUri, String params, String methodType) {
        HttpEntity<?> requestEntity = apiClientHttpEntity(params);
        ResponseEntity<String> response;
        switch (methodType.toUpperCase()) {
            case "POST":
                response = restTemplate.exchange(resourceUri, HttpMethod.POST, requestEntity, String.class);
                break;
            case "PUT":
                response = restTemplate.exchange(resourceUri, HttpMethod.PUT, requestEntity, String.class);
                break;
            case "DELETE":
                response = restTemplate.exchange(resourceUri, HttpMethod.DELETE, requestEntity, String.class);
                break;
            default:
                response = restTemplate.exchange(resourceUri, HttpMethod.GET, requestEntity, String.class);
                break;
        }
        log.info("resourceUri={}, statusCode={}", resourceUri, response.getStatusCode());
        return StringUtils.defaultString(StringUtils.defaultIfEmpty(response.getBody().toString(), "{}"));
    }
}
