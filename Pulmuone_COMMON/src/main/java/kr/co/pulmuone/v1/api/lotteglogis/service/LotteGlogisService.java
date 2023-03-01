package kr.co.pulmuone.v1.api.lotteglogis.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.pulmuone.v1.api.lotteglogis.dto.LotteGlogisClientOrderRequestDto;
import kr.co.pulmuone.v1.api.lotteglogis.dto.LotteGlogisClientOrderResponseDto;
import kr.co.pulmuone.v1.api.lotteglogis.dto.LotteGlogisTrackingResponseDto;
import kr.co.pulmuone.v1.comm.constants.ApiConstants;
import kr.co.pulmuone.v1.comm.enums.ApiEnums;
import kr.co.pulmuone.v1.comm.enums.ErpEnums;
import kr.co.pulmuone.v1.comm.enums.OrderClaimEnums;
import kr.co.pulmuone.v1.comm.util.JsonUtil;
import kr.co.pulmuone.v1.comm.util.RestTemplateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
* <PRE>
* Forbiz Korea
* 롯데택배 Service
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2020. 9. 18.                손진구          최초작성
* =======================================================================
* </PRE>
*/
@Slf4j
@Service
@RequiredArgsConstructor
public class LotteGlogisService {

    @Autowired
    RestTemplateUtil restTemplateUtil;

    private static final ObjectMapper OBJECT_MAPPER = JsonUtil.OBJECT_MAPPER;

    @Value("${lotteglogis.tracking.scheme}")
    private String trackingScheme;

    @Value("${lotteglogis.tracking.host}")
    private String trackingHost;

    @Value("${lotteglogis.tracking.port}")
    private String trackingPort;

    @Value("${lotteglogis.tracking.path}")
    private String trackingPath;

    @Value("${lotteglogis.client-order.auth-key}")
    private String clientOrderAuthKey;

    @Value("${lotteglogis.client-order.scheme}")
    private String clientOrderScheme;

    @Value("${lotteglogis.client-order.host}")
    private String clientOrderHost;

    @Value("${lotteglogis.client-order.port}")
    private String clientOrderPort;

    @Value("${lotteglogis.client-order.path}")
    private String clientOrderPath;

    /**
     * @Desc 롯데 송장번호 트래킹 API
     * @param waybillNumber
     * @return LotteGlogisTrackingResponseDto
     */
    protected LotteGlogisTrackingResponseDto getLotteGlogisTrackingList(String waybillNumber, String returnsYn) {
        LotteGlogisTrackingResponseDto lotteGlogisTrackingDto = new LotteGlogisTrackingResponseDto();
        final String RESPONSE_CODE = "errorCd";
        final String RESPONSE_NAME = "errorNm";
        final String RESPONSE_BODY = "tracking";
        log.info("롯데 운송장번호::: {}", waybillNumber);
        // 운송장번호 체크
        // -- 반품 회수의 경우는 송장번호로 조회 하는게 아니기 때문에 제외한다
        if( (StringUtils.isEmpty( waybillNumber ) || waybillNumber.length() != ApiConstants.WAYBILL_NUMBER_LENGTH) && !OrderClaimEnums.AllTypeYn.ALL_TYPE_Y.getCode().equals(returnsYn) ) {
            lotteGlogisTrackingDto.setResponseCodeAndMessage(ApiEnums.LotteGlogisTrackingApiResponse.TRACKING_PARAM_ERROR);
            log.info("롯데 운송장번호 체크 에러 ::: {}", lotteGlogisTrackingDto);
            return lotteGlogisTrackingDto;
        }
        log.info("Lotte Tracking trackingScheme ::: {}", trackingScheme);
        log.info("Lotte Tracking trackingHost   ::: {}", trackingHost);
        log.info("Lotte Tracking trackingPort   ::: {}", trackingPort);
        log.info("Lotte Tracking trackingPath   ::: {}", trackingPath);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");
        HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
        UriComponentsBuilder urlBuilder = UriComponentsBuilder.newInstance()
                                                              .scheme(trackingScheme)
                                                              .host(trackingHost)
                                                              .port(trackingPort)
                                                              .path(trackingPath)
                                                              .queryParam("param1", waybillNumber);

        // 반품 배송 추적
        if(OrderClaimEnums.AllTypeYn.ALL_TYPE_Y.getCode().equals(returnsYn)) {
            urlBuilder = UriComponentsBuilder.newInstance()
                                                .scheme(trackingScheme)
                                                .host(trackingHost)
                                                .port(trackingPort)
                                                .path(trackingPath)
                                                .queryParam("param1", ErpEnums.ParcelServiceCustId.LOTTE_CUST_CD.getCode())
                                                .queryParam("param2", waybillNumber);
        }

        ResponseEntity<String> responseEntity = restTemplateUtil.get(urlBuilder, requestEntity, String.class);

        // 통신 결과 체크
        if( responseEntity.getStatusCode() != HttpStatus.OK ) {
            lotteGlogisTrackingDto.setResponseCodeAndMessage(ApiEnums.LotteGlogisTrackingApiResponse.TRACKING_PARAM_ERROR);
            return lotteGlogisTrackingDto;
        }

        String responseBody = responseEntity.getBody();

        // responseBody null 체크
        if( StringUtils.isEmpty(responseBody) ) {
            lotteGlogisTrackingDto.setResponseCodeAndMessage(ApiEnums.LotteGlogisTrackingApiResponse.RESPONSEBODY_NO_DATA);
            return lotteGlogisTrackingDto;
        }

        JsonNode jsonNode;

        try {
            jsonNode = OBJECT_MAPPER.readTree(responseBody);
            log.info("Lotte Tracking jsonNode   :::\n <{}>", jsonNode);
        } catch (JsonProcessingException e) {
            lotteGlogisTrackingDto.setResponseCodeAndMessage(ApiEnums.LotteGlogisTrackingApiResponse.JSON_PARSING_ERROR);
            return lotteGlogisTrackingDto;
        }

        // API 결과 체크
        if( ObjectUtils.isEmpty( jsonNode.get(RESPONSE_CODE)) ) {
            lotteGlogisTrackingDto.setResponseCodeAndMessage(ApiEnums.LotteGlogisTrackingApiResponse.NO_DATA);
            return lotteGlogisTrackingDto;
        }else if( !"0".equals( jsonNode.get(RESPONSE_CODE).asText()) ) {

            lotteGlogisTrackingDto.setResponseCodeAndMessage( jsonNode.get(RESPONSE_CODE).asText(), jsonNode.get(RESPONSE_NAME).asText() );
            return lotteGlogisTrackingDto;
        }

        String trackingJsonData = jsonNode.get(RESPONSE_BODY).toString();
        log.info("Lotte Tracking trackingJsonData   :::\n <{}>", trackingJsonData);
        // tracking DATA 유무 체크
        if( StringUtils.isEmpty(trackingJsonData) ){
            lotteGlogisTrackingDto.setResponseCodeAndMessage(ApiEnums.LotteGlogisTrackingApiResponse.TRACKING_NO_DATA);
            return lotteGlogisTrackingDto;
        }

        // tracking DATA 셋팅
        lotteGlogisTrackingDto.setTrackingData(trackingJsonData);
        return lotteGlogisTrackingDto;
    }

    /**
     * @Desc 롯데 거래처 주문 API
     * @param lotteGlogisClientOrderList
     * @return LotteGlogisClientOrderResponseDto
     */
    protected LotteGlogisClientOrderResponseDto convertLotteGlogisClientOrderList(List<LotteGlogisClientOrderRequestDto> lotteGlogisClientOrderList) {
    	log.info("롯데택배  거래처 주문 API Start!!!! ::: <{}>", clientOrderAuthKey);
        LotteGlogisClientOrderResponseDto lotteGlogisClientOrderDto = new LotteGlogisClientOrderResponseDto();
        final String RESPONSE_CODE = "status";
        final String RESPONSE_NAME = "message";
        final String RESPONSE_BODY = "rtn_list";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Accept", "application/json");
        headers.add("Authorization", clientOrderAuthKey);

        Map<String, List<LotteGlogisClientOrderRequestDto>> param = new HashMap<String, List<LotteGlogisClientOrderRequestDto>>();
        param.put("snd_list", lotteGlogisClientOrderList);

        HttpEntity<Object> requestEntity = new HttpEntity<>(JsonUtil.serializeWithPrettyPrinting(param), headers);

        log.info("Lotte Return Goods clientOrderAuthKey ::: <{}>", clientOrderAuthKey);
        log.info("Lotte Return Goods clientOrderScheme ::: <{}>", clientOrderScheme);
        log.info("Lotte Return Goods clientOrderHost   ::: <{}>", clientOrderHost);
        log.info("Lotte Return Goods clientOrderPort   ::: <{}>", clientOrderPort);
        log.info("Lotte Return Goods clientOrderPath   ::: <{}>", clientOrderPath);

        UriComponentsBuilder urlBuilder = UriComponentsBuilder.newInstance()
                                                              .scheme(clientOrderScheme)
                                                              .host(clientOrderHost)
                                                              .port(clientOrderPort)
                                                              .path(clientOrderPath);

        ResponseEntity<String> responseEntity = restTemplateUtil.post(urlBuilder, requestEntity, String.class);
        log.info("Lotte Return Goods responseEntity :: <{}>", responseEntity);
        // 통신 결과 체크
        if( responseEntity.getStatusCode() != HttpStatus.OK ) {
            lotteGlogisClientOrderDto.setResponseCodeAndMessage(ApiEnums.LotteGlogisClientOrderApiResponse.HTTP_STATUS_FAIL);
            return lotteGlogisClientOrderDto;
        }

        String responseBody = responseEntity.getBody();
        log.info("Lotte Return Goods responseBody :: <{}>", responseBody);
        // responseBody null 체크
        if( StringUtils.isEmpty(responseBody) ) {
            lotteGlogisClientOrderDto.setResponseCodeAndMessage(ApiEnums.LotteGlogisClientOrderApiResponse.RESPONSEBODY_NO_DATA);
            return lotteGlogisClientOrderDto;
        }

        JsonNode jsonNode;

        try {
            jsonNode = OBJECT_MAPPER.readTree(responseBody);
            log.info("Lotte Return Goods jsonNode :: \n<{}>", jsonNode);
        } catch (JsonProcessingException e) {
            lotteGlogisClientOrderDto.setResponseCodeAndMessage(ApiEnums.LotteGlogisClientOrderApiResponse.JSON_PARSING_ERROR);
            return lotteGlogisClientOrderDto;
        }

        // API 결과 체크
        if( ObjectUtils.isEmpty( jsonNode.get(RESPONSE_CODE)) ) {
            lotteGlogisClientOrderDto.setResponseCodeAndMessage(ApiEnums.LotteGlogisClientOrderApiResponse.NO_DATA);
            return lotteGlogisClientOrderDto;
        }else if( !ApiEnums.LotteGlogisClientOrderApiResponse.SUCCESS.name().equalsIgnoreCase(jsonNode.get(RESPONSE_CODE).asText()) ) {
            lotteGlogisClientOrderDto.setResponseCodeAndMessage( jsonNode.get(RESPONSE_CODE).asText(), jsonNode.get(RESPONSE_NAME).asText() );
            return lotteGlogisClientOrderDto;
        }

        String clientOrderReturnJsonData = jsonNode.get(RESPONSE_BODY).toString();
        log.info("Lotte Return Goods clientOrderReturnJsonData :: \n<{}>", clientOrderReturnJsonData);
        //  거래처주문 결과 DATA 유무 체크
        if( StringUtils.isEmpty(clientOrderReturnJsonData) ){
            lotteGlogisClientOrderDto.setResponseCodeAndMessage(ApiEnums.LotteGlogisClientOrderApiResponse.RETURN_NO_DATA);
            return lotteGlogisClientOrderDto;
        }

        // 거래처주문 결과 DATA 셋팅
        lotteGlogisClientOrderDto.setClientOrderReturnData(clientOrderReturnJsonData);

        return lotteGlogisClientOrderDto;
    }
}
