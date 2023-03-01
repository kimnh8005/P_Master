package kr.co.pulmuone.v1.comm.api.service.impl;

import java.net.SocketTimeoutException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.conn.ConnectTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import kr.co.pulmuone.v1.comm.api.constant.ErpApiInfo;
import kr.co.pulmuone.v1.comm.api.dto.vo.BaseApiResponseVo;
import kr.co.pulmuone.v1.comm.api.factory.RestTemplateFactory;
import kr.co.pulmuone.v1.comm.api.service.ErpApiExchangeService;
import kr.co.pulmuone.v1.comm.util.JsonUtil;

@Component
public class ErpApiExchangeServiceImpl implements InitializingBean, ErpApiExchangeService {

    // 별도 로그명 지정
    protected static final Logger log = LoggerFactory.getLogger("ErpApiExchangeService");

    // put, post 방식의 데이터 송신시 request body 내 최상위 key 값
    // { "header" : [ data object, data object, ... ] } 형식으로 request body 가 세팅됨
    private static final String HEADER_KEY = "header";

    @Value("${erp-api-exchange.scheme}")
    private String scheme;

    @Value("${erp-api-exchange.api-server-url}")
    private String apiServerUrl;

    @Value("${erp-api-exchange.auth-key}")
    private String authkey;

    @Autowired
    private RestTemplateFactory restTemplateFactory;

    private RestTemplate restTemplate;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.restTemplate = restTemplateFactory.getRestTemplate();
    }

    /*
     * ERP API 대상 GET 방식의 데이터 조회 ( restTemplate 활용 )
     *
     * @Param paramMap : GET 방식으로 전송할 parameter Map
     *
     * @Param interfaceId : 호출할 ERP API 의 인터페이스 ID
     *
     */
    public BaseApiResponseVo exchange(Map<String, String> parameterMap, String interfaceId) {

        /*
         * 헤더 정보 세팅
         */
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        headers.add("Accept", "application/json");
        headers.add("authkey", authkey); // ERP API 인증키
        headers.add("interfaceId", interfaceId); // 인터페이스ID

        /*
         * 호출할 ERP API 대상 uri 생성
         */
        URI uri = generateApiUri(parameterMap, interfaceId);

        /*
         * restTemplate 으로 GET 방식 ERP API 호출
         */
        ResponseEntity<String> responseEntity = sendGetRequestToErpApi(uri, headers);

        return BaseApiResponseVo.builder() //
                .statusCode(responseEntity.getStatusCode()) // HTTP 상태코드
                .responseHeaders(responseEntity.getHeaders()) // Response 헤더 정보
                .responseBody(responseEntity.getBody()) // Response Body : 실제 Json 데이터
                .build();

    }

    /*
     * 상단 exchange 메서드를 추후 get 으로 변경하고 기존 exchange 메서드는 삭제 예정
     *
     * 현재 기존 exchange 메서드를 사용하는 곳이 많아 동일한 내용으로 get 메서드 우선 등록 / 다른 로직에서 exchange => get 수정 예정
     */

    /*
     * ERP API 대상 GET 방식의 데이터 조회 ( restTemplate 활용 )
     *
     * @Param paramMap : GET 방식으로 전송할 parameter Map
     *
     * @Param interfaceId : 호출할 ERP API 의 인터페이스 ID
     *
     */
    public BaseApiResponseVo get(Map<String, String> parameterMap, String interfaceId) {

        /*
         * 헤더 정보 세팅
         */
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        headers.add("Accept", "application/json");
        headers.add("authkey", authkey); // ERP API 인증키
        headers.add("interfaceId", interfaceId); // 인터페이스ID

        /*
         * 호출할 ERP API 대상 uri 생성
         */
        URI uri = generateApiUri(parameterMap, interfaceId);

        /*
         * restTemplate 으로 GET 방식 ERP API 호출
         */
        ResponseEntity<String> responseEntity = sendGetRequestToErpApi(uri, headers);

        return BaseApiResponseVo.builder() //
                .statusCode(responseEntity.getStatusCode()) // HTTP 상태코드
                .responseHeaders(responseEntity.getHeaders()) // Response 헤더 정보
                .responseBody(responseEntity.getBody()) // Response Body : 실제 Json 데이터
                .build();

    }

    /*
     * ERP API 대상 POST 방식의 데이터 송신 ( restTemplate 활용 )
     *
     * @Param paramList : POST 방식으로 호출시 request body 에 추가할 Data 목록
     *
     * @Param interfaceId : 호출할 ERP API 의 인터페이스 ID
     *
     */
    public BaseApiResponseVo post(List<?> paramList, String interfaceId) {

        /*
         * 헤더 정보 세팅
         */
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        headers.add("Accept", "application/json");
        headers.add("authkey", authkey); // ERP API 인증키
        headers.add("interfaceId", interfaceId); // 인터페이스ID

        /*
         * 호출할 ERP API 대상 uri 생성
         */
        URI uri = generateApiUri(null, interfaceId);

        /*
         * restTemplate 으로 POST 방식 ERP API 호출
         */
        ResponseEntity<String> responseEntity = sendPostRequestToErpApi(uri, headers, paramList);

        return BaseApiResponseVo.builder() //
                .statusCode(responseEntity.getStatusCode()) // HTTP 상태코드
                .responseHeaders(responseEntity.getHeaders()) // Response 헤더 정보
                .responseBody(responseEntity.getBody()) // Response Body : 실제 Json 데이터
                .build();

    }

    /*
     * ERP API 대상 POST 방식의 데이터 송신 ( restTemplate 활용 )
     *
     * @Param obj : POST 방식으로 호출시 request body 에 추가할 Data 목록
     *
     * @Param interfaceId : 호출할 ERP API 의 인터페이스 ID
     *
     */
    public BaseApiResponseVo post(Object obj, String interfaceId) {

        /*
         * 헤더 정보 세팅
         */
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        headers.add("Accept", "application/json");
        headers.add("authkey", authkey); // ERP API 인증키
        headers.add("interfaceId", interfaceId); // 인터페이스ID

        /*
         * 호출할 ERP API 대상 uri 생성
         */
        URI uri = generateApiUri(null, interfaceId);

        /*
         * restTemplate 으로 POST 방식 ERP API 호출
         */
        ResponseEntity<String> responseEntity = sendPostRequestToErpApi(uri, headers, obj);

        return BaseApiResponseVo.builder() //
                .statusCode(responseEntity.getStatusCode()) // HTTP 상태코드
                .responseHeaders(responseEntity.getHeaders()) // Response 헤더 정보
                .responseBody(responseEntity.getBody()) // Response Body : 실제 Json 데이터
                .build();

    }

    /*
     * ERP API 대상 PUT 방식의 데이터 송신 ( restTemplate 활용 )
     *
     * @Param paramList : PUT 방식으로 호출시 request body 에 추가할 Data 목록
     *
     * @Param interfaceId : 호출할 ERP API 의 인터페이스 ID
     *
     */
    public BaseApiResponseVo put(List<?> paramList, String interfaceId) {

        /*
         * 헤더 정보 세팅
         */
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        headers.add("Accept", "application/json");
        headers.add("authkey", authkey); // ERP API 인증키
        headers.add("interfaceId", interfaceId); // 인터페이스ID

        /*
         * 호출할 ERP API 대상 uri 생성
         */
        URI uri = generateApiUri(null, interfaceId);

        /*
         * restTemplate 으로 PUT 방식 ERP API 호출
         */
        ResponseEntity<String> responseEntity = sendPutRequestToErpApi(uri, headers, paramList);

        return BaseApiResponseVo.builder() //
                .statusCode(responseEntity.getStatusCode()) // HTTP 상태코드
                .responseHeaders(responseEntity.getHeaders()) // Response 헤더 정보
                .responseBody(responseEntity.getBody()) // Response Body : 실제 Json 데이터
                .build();

    }

    /*
     * 호출할 ERP API 대상 uri 생성
     */
    private URI generateApiUri(Map<String, String> parameterMap, String interfaceId) {

        ErpApiInfo erpApiInfo = null;

        try {

            erpApiInfo = ErpApiInfo.valueOf(interfaceId);

        } catch (IllegalArgumentException e) { // 등록되지 않은 interfaceId 인 경우 IllegalArgumentException 발생
            log.error("Not Registered interfaceId", e);
            throw e;
        }

        /*
         * Parameter 세팅
         */

        // UriComponentsBuilder 의 queryParams 전용 MultiValueMap 생성
        MultiValueMap<String, String> queryParamMap = new LinkedMultiValueMap<>();

        // MultiValueMap 에 모든 parameter 등록
        // => parameterMap의 모든 Key에 해당하는 Value들을 1개의 값을 가진 LinkedList로 변환하여 set
        if (parameterMap != null && !parameterMap.keySet().isEmpty()) {
            queryParamMap.setAll(parameterMap);
        }

        // 해당 API 호출시 필수로 포함해야 할 고정값들을 등록
        queryParamMap.setAll(erpApiInfo.getFixedValueMap());

        /*
         * URL 세팅
         */
        return UriComponentsBuilder.newInstance() //
                .scheme(scheme) //
                .host(apiServerUrl) //
                .path(erpApiInfo.getErpApiUri()) // 해당 API 의 uri
                .queryParams(queryParamMap) //
                .build().encode().toUri();

    }

    /*
     * restTemplate 으로 GET 방식 ERP API 호출
     */
    private ResponseEntity<String> sendGetRequestToErpApi(URI url, HttpHeaders headers) {

        HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity; // API 호출 결과 responseEntity

        try {

            responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);

        } catch (HttpClientErrorException e) {
            log.error("HttpClientErrorException 발생 : EPR API 연결실패1-4xx");
            throw e;

        } catch (HttpServerErrorException e) {
            log.error("HttpServerErrorException 발생 : EPR API 연결실패2-5xx");
            throw e;

        } catch (ResourceAccessException e) {

            if (e.getRootCause() instanceof ConnectTimeoutException) {
                log.error("ConnectTimeoutException 발생 : ERP API 연결실패");

            } else if (e.getRootCause() instanceof SocketTimeoutException) {
                log.error("SocketTimeoutException 발생 : ERP API 연결실패");

            } else {
                log.error("ResourceAccessException 발생 : ERP API 연결실패3-IO");

            }

            throw e;

        }

        return responseEntity;

    }

    /*
     * restTemplate 으로 POST 방식 ERP API 호출
     */
    private ResponseEntity<String> sendPostRequestToErpApi(URI url, HttpHeaders headers, Object obj) {

        HttpEntity<Object> requestEntity = new HttpEntity<>(obj, headers);
        ResponseEntity<String> responseEntity; // API 호출 결과 responseEntity

        try {

            responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        } catch (HttpClientErrorException e) {
            log.error("HttpClientErrorException 발생 : EPR API 연결실패1-4xx");
            throw e;

        } catch (HttpServerErrorException e) {
            log.error("HttpServerErrorException 발생 : EPR API 연결실패2-5xx");
            throw e;

        } catch (ResourceAccessException e) {

            if (e.getRootCause() instanceof ConnectTimeoutException) {
                log.error("ConnectTimeoutException 발생 : ERP API 연결실패");

            } else if (e.getRootCause() instanceof SocketTimeoutException) {
                log.error("SocketTimeoutException 발생 : ERP API 연결실패");

            } else {
                log.error("ResourceAccessException 발생 : ERP API 연결실패3-IO");

            }

            throw e;

        }

        return responseEntity;

    }

    /*
     * restTemplate 으로 POST 방식 ERP API 호출
     */
    private ResponseEntity<String> sendPostRequestToErpApi(URI url, HttpHeaders headers, List<?> paramList) {

        // PUT 방식으로 데이터 송신할 request body map 생성
        Map<String, Object> requestBodyMap = new HashMap<>();

        // { "header" : [ data object, data object, ... ] } 형식으로 request body 가 세팅됨
        requestBodyMap.put(HEADER_KEY, paramList);

        HttpEntity<Object> requestEntity = new HttpEntity<>(requestBodyMap, headers);
        ResponseEntity<String> responseEntity; // API 호출 결과 responseEntity

        try {

            responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        } catch (HttpClientErrorException e) {
            log.error("HttpClientErrorException 발생 : EPR API 연결실패1-4xx");
            throw e;

        } catch (HttpServerErrorException e) {
            log.error("HttpServerErrorException 발생 : EPR API 연결실패2-5xx");
            throw e;

        } catch (ResourceAccessException e) {

            if (e.getRootCause() instanceof ConnectTimeoutException) {
                log.error("ConnectTimeoutException 발생 : ERP API 연결실패");

            } else if (e.getRootCause() instanceof SocketTimeoutException) {
                log.error("SocketTimeoutException 발생 : ERP API 연결실패");

            } else {
                log.error("ResourceAccessException 발생 : ERP API 연결실패3-IO");

            }

            throw e;

        }

        return responseEntity;

    }

    /*
     * restTemplate 으로 PUT 방식 ERP API 호출
     */
    private ResponseEntity<String> sendPutRequestToErpApi(URI url, HttpHeaders headers, List<?> paramList) {

        // PUT 방식으로 데이터 송신할 request body map 생성
        Map<String, Object> requestBodyMap = new HashMap<>();

        // { "header" : [ data object, data object, ... ] } 형식으로 request body 가 세팅됨
        requestBodyMap.put(HEADER_KEY, paramList);

        HttpEntity<Object> requestEntity = new HttpEntity<>(requestBodyMap, headers);
        ResponseEntity<String> responseEntity; // API 호출 결과 responseEntity

        try {

            responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, String.class);

        } catch (HttpClientErrorException e) {
            log.error("HttpClientErrorException 발생 : EPR API 연결실패1-4xx");
            throw e;

        } catch (HttpServerErrorException e) {
            log.error("HttpServerErrorException 발생 : EPR API 연결실패2-5xx");
            throw e;

        } catch (ResourceAccessException e) {

            if (e.getRootCause() instanceof ConnectTimeoutException) {
                log.error("ConnectTimeoutException 발생 : ERP API 연결실패");

            } else if (e.getRootCause() instanceof SocketTimeoutException) {
                log.error("SocketTimeoutException 발생 : ERP API 연결실패");

            } else {
                log.error("ResourceAccessException 발생 : ERP API 연결실패3-IO");

            }

            throw e;

        }

        return responseEntity;

    }

	@Override
	public BaseApiResponseVo put(Object obj, String interfaceId) {
		// TODO Auto-generated method stub
		/*
         * 헤더 정보 세팅
         */
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        headers.add("Accept", "application/json");
        headers.add("authkey", authkey); // ERP API 인증키
        headers.add("interfaceId", interfaceId); // 인터페이스ID

        /*
         * 호출할 ERP API 대상 uri 생성
         */
        URI uri = generateApiUri(null, interfaceId);

        /*
         * restTemplate 으로 POST 방식 ERP API 호출
         */
        ResponseEntity<String> responseEntity = sendPutRequestToErpApi(uri, headers, obj);

        return BaseApiResponseVo.builder() //
                .statusCode(responseEntity.getStatusCode()) // HTTP 상태코드
                .responseHeaders(responseEntity.getHeaders()) // Response 헤더 정보
                .responseBody(responseEntity.getBody()) // Response Body : 실제 Json 데이터
                .build();
	}

	/*
    * restTemplate 으로 POST 방식 ERP API 호출
    */
   private ResponseEntity<String> sendPutRequestToErpApi(URI url, HttpHeaders headers, Object obj) {

       HttpEntity<Object> requestEntity = new HttpEntity<>(obj, headers);
       ResponseEntity<String> responseEntity; // API 호출 결과 responseEntity

       try {

           responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, String.class);

       } catch (HttpClientErrorException e) {
           log.error("HttpClientErrorException 발생 : EPR API 연결실패1-4xx");
           throw e;

       } catch (HttpServerErrorException e) {
           log.error("HttpServerErrorException 발생 : EPR API 연결실패2-5xx");
           throw e;

       } catch (ResourceAccessException e) {

           if (e.getRootCause() instanceof ConnectTimeoutException) {
               log.error("ConnectTimeoutException 발생 : ERP API 연결실패");

           } else if (e.getRootCause() instanceof SocketTimeoutException) {
               log.error("SocketTimeoutException 발생 : ERP API 연결실패");

           } else {
               log.error("ResourceAccessException 발생 : ERP API 연결실패3-IO");

           }

           throw e;

       }

       return responseEntity;

   }


}
