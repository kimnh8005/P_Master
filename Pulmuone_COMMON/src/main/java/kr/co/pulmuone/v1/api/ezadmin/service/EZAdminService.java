package kr.co.pulmuone.v1.api.ezadmin.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.pulmuone.v1.api.ezadmin.dto.EZAdminResponseDefaultDto;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.ApiEnums;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.util.JsonUtil;
import kr.co.pulmuone.v1.comm.util.RestTemplateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
* <PRE>
* Forbiz Korea
* EZAdmin Api
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0		20201218		박승현              최초작성
*
* =======================================================================
* </PRE>
*/
@Slf4j
@Service
@RequiredArgsConstructor
public class EZAdminService {

    @Autowired
    RestTemplateUtil restTemplateUtil;

    private static final ObjectMapper OBJECT_MAPPER = JsonUtil.OBJECT_MAPPER;

    @Value("${ezadmin.tracking.scheme}")
    private String trackingScheme;

    @Value("${ezadmin.tracking.host}")
    private String trackingHost;

    @Value("${ezadmin.tracking.path}")
    private String trackingPath;

    @Value("${ezadmin.partner-key}")
    private String partnerKey;

    @Value("${ezadmin.domain-key}")
    private String domainKey;

    /**
     * @Desc EZ Admin 조회
     * @param MultiValueMap
     * @param Class<T>
     *
     * @return ApiResult
     */
    protected <T> ApiResult<?> get(MultiValueMap<String, String> paramMap, Class<T> dataType) {
    	EZAdminResponseDefaultDto resDto = null;
        final String RESPONSE_CODE = "error";
        final String RESPONSE_MSG = "msg";
        final String RESPONSE_DATA = "data";

        paramMap.add("partner_key", partnerKey);
        paramMap.add("domain_key", domainKey);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");
        HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
        UriComponentsBuilder urlBuilder = UriComponentsBuilder.newInstance()
                                                              .scheme(trackingScheme)
                                                              .host(trackingHost)
                                                              .path(trackingPath)
                                                              .queryParams(paramMap);

        ResponseEntity<String> responseEntity = restTemplateUtil.get(urlBuilder, requestEntity, String.class);

        // 통신 결과 체크
        if( responseEntity.getStatusCode() != HttpStatus.OK ) {
            return ApiResult.result(ApiEnums.Default.HTTP_STATUS_FAIL);
        }

        String responseBody = responseEntity.getBody();
        log.info("responseBody={}", responseBody);

        // responseBody null 체크
        if( StringUtils.isEmpty(responseBody) ) {
            return ApiResult.result(ApiEnums.Default.RESPONSEBODY_NO_DATA);
        }

        JsonNode jsonNode;

        try {
            jsonNode = OBJECT_MAPPER.readTree(responseBody);
        } catch (JsonProcessingException e) {
        	e.printStackTrace();
            return ApiResult.result(ApiEnums.Default.JSON_PARSING_ERROR);
        }

        // API 결과 체크
        if(jsonNode.get(RESPONSE_CODE) == null
         		|| !jsonNode.get(RESPONSE_CODE).toString().equals("0")) {
        	return ApiResult.result(ApiEnums.Default.RESPONSE_FAIL);
        }

//        if( StringUtils.isEmpty( jsonNode.get(RESPONSE_CODE).asText() ) ) {
//        	return ApiResult.result(ApiEnums.Default.NO_DATA);
//        }

        if(StringUtils.isEmpty( jsonNode.get(RESPONSE_MSG).asText() ) ) {
        	return ApiResult.result(ApiEnums.Default.RESPONSE_FAIL);
        }
        if(jsonNode.get(RESPONSE_DATA) == null
        		|| jsonNode.get(RESPONSE_DATA).isEmpty()
        		|| jsonNode.get(RESPONSE_DATA).toString().isEmpty()
        		) {
        	return ApiResult.result(ApiEnums.Default.NO_DATA);
        }

        String jsonData = jsonNode.get(RESPONSE_DATA).toString();

        log.info("jsonData={}", jsonData);
        // DATA 유무 체크
        if( StringUtils.isEmpty(jsonData) ){
            return ApiResult.result(ApiEnums.Default.NO_DATA);
        }

        resDto = OBJECT_MAPPER.convertValue(jsonNode, EZAdminResponseDefaultDto.class);

        resDto.setResponseData(responseBody);

        List<T> dataList = null;
        JavaType listType = OBJECT_MAPPER.getTypeFactory().constructCollectionType(ArrayList.class, dataType);
        try {
			dataList = OBJECT_MAPPER.readValue(jsonData, listType);
			resDto.setData(dataList);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
            return ApiResult.result(ApiEnums.Default.JSON_PARSING_ERROR);
		}
        log.info("resDto={}", resDto);
        return ApiResult.result(resDto, BaseEnums.Default.SUCCESS);
    }

    /**
     * @Desc EZ Admin 처리
     * @param MultiValueMap
     * @return
     * @return ApiResult
     */
    protected ApiResult<?> set(MultiValueMap<String, String> paramMap) {
    	EZAdminResponseDefaultDto resDto = null;
        final String RESPONSE_CODE = "error";
        final String RESPONSE_MSG = "msg";

        paramMap.add("partner_key", partnerKey);
        paramMap.add("domain_key", domainKey);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");
        HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
        UriComponentsBuilder urlBuilder = UriComponentsBuilder.newInstance()
                                                              .scheme(trackingScheme)
                                                              .host(trackingHost)
                                                              .path(trackingPath)
                                                              .queryParams(paramMap);

        ResponseEntity<String> responseEntity = restTemplateUtil.get(urlBuilder, requestEntity, String.class);

        // 통신 결과 체크
        if( responseEntity.getStatusCode() != HttpStatus.OK ) {
            return ApiResult.result(ApiEnums.Default.HTTP_STATUS_FAIL);
        }

        String responseBody = responseEntity.getBody();
        log.info("responseBody={}", responseBody);

        // responseBody null 체크
        if( StringUtils.isEmpty(responseBody) ) {
            return ApiResult.result(ApiEnums.Default.RESPONSEBODY_NO_DATA);
        }

        JsonNode jsonNode;

        try {
            jsonNode = OBJECT_MAPPER.readTree(responseBody);
        } catch (JsonProcessingException e) {
        	e.printStackTrace();
            return ApiResult.result(ApiEnums.Default.JSON_PARSING_ERROR);
        }

        // API 결과 체크
        if(jsonNode.get(RESPONSE_CODE) == null || !jsonNode.get(RESPONSE_CODE).toString().equals("0")) {
        	return ApiResult.result(ApiEnums.Default.RESPONSE_FAIL);
        }

//        if( StringUtils.isEmpty( jsonNode.get(RESPONSE_CODE).asText() ) ) {
//        	return ApiResult.result(ApiEnums.Default.NO_DATA);
//        }

        if(StringUtils.isEmpty( jsonNode.get(RESPONSE_MSG).asText() ) ) {
        	return ApiResult.result(ApiEnums.Default.RESPONSE_FAIL);
        }

        resDto = OBJECT_MAPPER.convertValue(jsonNode, EZAdminResponseDefaultDto.class);
        log.info("resDto={}", resDto);

        return ApiResult.result(resDto, BaseEnums.Default.SUCCESS);
    }
}
