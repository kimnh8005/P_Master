package kr.co.pulmuone.v1.api.cjlogistics.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.pulmuone.v1.api.cjlogistics.dto.CJLogisticsOrderAcceptDto;
import kr.co.pulmuone.v1.api.cjlogistics.dto.CJLogisticsTrackingResponseDto;
import kr.co.pulmuone.v1.comm.constants.ApiConstants;
import kr.co.pulmuone.v1.comm.enums.ApiEnums;
import kr.co.pulmuone.v1.comm.mappers.slaveCjFront.CJLogisticsMapper;
import kr.co.pulmuone.v1.comm.util.JsonUtil;
import kr.co.pulmuone.v1.comm.util.RestTemplateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;



/**
* <PRE>
* Forbiz Korea
* CJ택배 Service
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2020. 9. 22.                손진구          최초작성
* =======================================================================
* </PRE>
*/
@Slf4j
@Service
@RequiredArgsConstructor
public class CJLogisticsService {

    @Autowired
    private RestTemplateUtil restTemplateUtil;

    private static final ObjectMapper OBJECT_MAPPER = JsonUtil.OBJECT_MAPPER;

    @Value("${cjlogistics.tracking.scheme}")
    private String trackingScheme;

    @Value("${cjlogistics.tracking.host}")
    private String trackingHost;

    @Value("${cjlogistics.tracking.path}")
    private String trackingPath;

    private final CJLogisticsMapper cJLogisticsMapper;

    /**
     * @Desc CJ 송장번호 트래킹 API
     * @param waybillNumber
     * @return CJLogisticsTrackingResponseDto
     */
    protected CJLogisticsTrackingResponseDto getCJLogisticsTrackingList(String waybillNumber) {
        CJLogisticsTrackingResponseDto cJLogisticsTrackingDto = new CJLogisticsTrackingResponseDto();
        final String RECIPIENT_NAME = "rcvernm";
        final String RESPONSE_BODY = "tracking";

        log.debug("CJ 택배 트래킹 1 waybillNumber :: <{}>", waybillNumber);
        // 운송장번호 체크
        if( StringUtils.isEmpty( waybillNumber ) || waybillNumber.length() != ApiConstants.WAYBILL_NUMBER_LENGTH ) {
            cJLogisticsTrackingDto.setResponseCodeAndMessage(ApiEnums.CJLogisticsTrackingApiResponse.TRACKING_PARAM_ERROR);
            log.debug("CJ 운송장 번호 유효성 에러  :: <{}>", cJLogisticsTrackingDto);
            return cJLogisticsTrackingDto;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");

        HttpEntity<Object> requestEntity = new HttpEntity<>(headers);

        UriComponentsBuilder urlBuilder = UriComponentsBuilder.newInstance()
                                                              .scheme(trackingScheme)
                                                              .host(trackingHost)
                                                              .path(trackingPath)
                                                              .queryParam("invoice", waybillNumber);

        ResponseEntity<String> responseEntity = restTemplateUtil.get(urlBuilder, requestEntity, String.class);

        // 통신 결과 체크
        if( responseEntity.getStatusCode() != HttpStatus.OK ) {
            cJLogisticsTrackingDto.setResponseCodeAndMessage(ApiEnums.CJLogisticsTrackingApiResponse.TRACKING_PARAM_ERROR);
            return cJLogisticsTrackingDto;
        }

        String responseBody = responseEntity.getBody();

        // responseBody null 체크
        if( StringUtils.isEmpty(responseBody) ) {
            cJLogisticsTrackingDto.setResponseCodeAndMessage(ApiEnums.CJLogisticsTrackingApiResponse.RESPONSEBODY_NO_DATA);
            return cJLogisticsTrackingDto;
        }

        JsonNode jsonNode;

        try {
            jsonNode = OBJECT_MAPPER.readTree(responseBody);
        } catch (JsonProcessingException e) {
            cJLogisticsTrackingDto.setResponseCodeAndMessage(ApiEnums.CJLogisticsTrackingApiResponse.JSON_PARSING_ERROR);
            return cJLogisticsTrackingDto;
        }

        log.info("jsonNode 6 :::: <{}>", jsonNode);
        log.info("StringUtils.isEmpty(jsonNode.get(RECIPIENT_NAME).asText()) :: <{}>", StringUtils.isEmpty( jsonNode.get(RECIPIENT_NAME).asText() ));

        // API 결과 체크
        if( StringUtils.isEmpty( jsonNode.get(RECIPIENT_NAME).asText() ) ) {
            cJLogisticsTrackingDto.setResponseCodeAndMessage(ApiEnums.CJLogisticsTrackingApiResponse.NO_DATA);
            return cJLogisticsTrackingDto;
        }

        String trackingJsonData = jsonNode.get(RESPONSE_BODY).toString();

        log.info("trackingJsonData :::: \n <{}>", trackingJsonData);

        // tracking DATA 유무 체크
        if( StringUtils.isEmpty(trackingJsonData) ){
            cJLogisticsTrackingDto.setResponseCodeAndMessage(ApiEnums.CJLogisticsTrackingApiResponse.TRACKING_NO_DATA);
            return cJLogisticsTrackingDto;
        }

        // tracking DATA 셋팅
        cJLogisticsTrackingDto.setTrackingData(trackingJsonData, jsonNode);
        return cJLogisticsTrackingDto;
    }

    /**
     * @Desc CJ 주문접수 API
     * @param dto
     * @return
     * @throws Exception
     */
    protected int addCJLogisticsOrderAccept(CJLogisticsOrderAcceptDto dto) {
    	log.debug("CJ 주문접수 API START!!!!");
    	log.debug("CJ 주문접수 API param :: <{}>", dto);
    	return cJLogisticsMapper.addCJLogisticsOrderAccept(dto);
    }
}
