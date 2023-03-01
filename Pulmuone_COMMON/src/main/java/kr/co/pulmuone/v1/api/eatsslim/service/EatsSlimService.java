package kr.co.pulmuone.v1.api.eatsslim.service;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import kr.co.pulmuone.v1.comm.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.pulmuone.v1.api.eatsslim.dto.EatsslimOrderDeliveryListDto;
import kr.co.pulmuone.v1.api.eatsslim.dto.EatsslimOrderDeliveryListRequestDto;
import kr.co.pulmuone.v1.api.eatsslim.dto.EatsslimOrderInfoDto;
import kr.co.pulmuone.v1.api.eatsslim.dto.EatsslimOrderInfoRequestDto;
import kr.co.pulmuone.v1.api.eatsslim.dto.EatsslimOrderUpdateInfoRequestDto;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.ApiEnums;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.OrderScheduleEnums;
import kr.co.pulmuone.v1.comm.util.JsonUtil;
import kr.co.pulmuone.v1.comm.util.RestTemplateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * <PRE>
* Forbiz Korea
* Eatsslim Service
 * </PRE>
 *
 * <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                	:  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2021. 01. 28.    이규한        	 최초작성
* =======================================================================
 * </PRE>
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class EatsSlimService {

	@Autowired
	private RestTemplateUtil restTemplateUtil;

	private static final ObjectMapper OBJECT_MAPPER = JsonUtil.OBJECT_MAPPER;

	@Value("${udms.url}")
	private String url;

	private static final String SEARCH_ES_ORDER_LIST 			= "/api/searchEsOrder/searchEsOrderList";			// 잇슬림 주문조회 목록 I/F ID

	private static final String SEARCH_ES_ORDER_DELIVERY_LIST 	= "/api/searchEsOrder/searchEsOrderDeliveryList";	// 잇슬림 배송 스케줄정보 목록 I/F ID

	private static final String UPDATE_ES_ORDER_DELIVERY_INFO 	= "/api/searchEsOrder/updateOrder";					// 잇슬림배송 스케줄정보 변경 I/F ID

	private static final String RESPONSE_CODE					= "info";		// 결과 데이터 코드 JSON Key

	private static final String RESPONSE_DATA					= "items";		// 결과 데이터 JSON Key

	private static final String GOODS_STATUS					= "01";			// 정상주문

	/**
     * @Desc 잇슬림 주문정보 조회
     * @param eatsslimOrderListRequestDto
     * @return ApiResult
     */
	protected ApiResult<?> getOrderList(EatsslimOrderInfoRequestDto eatsslimOrderInfoRequestDto) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");

        EatsslimOrderInfoDto eatsslimOrderInfoDto = null;

		try {
			HttpEntity<JSONObject> requestEntity = new HttpEntity<JSONObject>(JSONObject.fromObject(OBJECT_MAPPER.writeValueAsString(eatsslimOrderInfoRequestDto)), headers);
			UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromHttpUrl(url + SEARCH_ES_ORDER_LIST);
			ResponseEntity<String> responseEntity = restTemplateUtil.post(urlBuilder, requestEntity, String.class);
			String responseBody = URLDecoder.decode(responseEntity.getBody(), "UTF-8");
			log.info("responseBody={}", responseBody);

			// 통신 결과 체크
	        if (responseEntity.getStatusCode() != HttpStatus.OK ) return ApiResult.result(OrderScheduleEnums.ScheduleErrMsg.API_COMMUNICATION_FAILED);
	        // responseBody null 체크
	        if (StringUtils.isEmpty(responseBody)) return ApiResult.result(ApiEnums.Default.RESPONSEBODY_NO_DATA);

	        // 잇슬림 주문정보 결과 코드 값 체크
	        JSONArray orderInfoArr = (JSONArray)JSONObject.fromObject(responseBody).get(RESPONSE_CODE);
	        if (orderInfoArr == null || orderInfoArr.isEmpty() || orderInfoArr.toString().isEmpty() || orderInfoArr.size() < 1) return ApiResult.result(ApiEnums.Default.RESPONSE_FAIL);
	        JSONObject orderInfoCode = (JSONObject)orderInfoArr.get(0);
	        if (Integer.parseInt((String)orderInfoCode.get("resultCode")) < 0) return ApiResult.result(ApiEnums.Default.RESPONSE_FAIL);

	        JSONArray orderArr = (JSONArray)JSONObject.fromObject(responseBody).get(RESPONSE_DATA);
	        if (orderArr == null || orderArr.isEmpty() || orderArr.toString().isEmpty() || orderArr.size() < 1) return ApiResult.result(OrderScheduleEnums.ScheduleErrMsg.API_SCHEDULE_NO_DATA);
	        JSONObject orderInfo = (JSONObject)orderArr.get(0);
	        if (orderInfo == null || orderInfo.isEmpty() || orderInfo.toString().isEmpty()) return ApiResult.result(OrderScheduleEnums.ScheduleErrMsg.API_SCHEDULE_NO_DATA);

	        eatsslimOrderInfoDto = OBJECT_MAPPER.convertValue(orderInfo, EatsslimOrderInfoDto.class);

		} catch (Exception e) {
			e.printStackTrace();
            return ApiResult.result(ApiEnums.Default.HTTP_STATUS_FAIL);
		}
        return ApiResult.result(eatsslimOrderInfoDto, BaseEnums.Default.SUCCESS);
	}

	/**
     * @Desc 잇슬림 배송 스케줄정보 조회
     * @param eatsslimOrderListRequestDto
     * @return ApiResult
     */
	@SuppressWarnings("unchecked")
	protected ApiResult<?> getOrderDeliveryList(EatsslimOrderDeliveryListRequestDto eatsslimOrderDeliveryListRequestDto) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");

        List<EatsslimOrderDeliveryListDto> eatsslimOrderDeliveryList = new ArrayList<EatsslimOrderDeliveryListDto>();

		try {
			HttpEntity<JSONObject> requestEntity = new HttpEntity<JSONObject>(JSONObject.fromObject(OBJECT_MAPPER.writeValueAsString(eatsslimOrderDeliveryListRequestDto)), headers);
			UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromHttpUrl(url + SEARCH_ES_ORDER_DELIVERY_LIST);
			ResponseEntity<String> responseEntity = restTemplateUtil.post(urlBuilder, requestEntity, String.class);
			String responseBody = URLDecoder.decode(responseEntity.getBody(), "UTF-8");
			log.info("responseBody={}", responseBody);

			// 통신 결과 체크
	        if (responseEntity.getStatusCode() != HttpStatus.OK) return ApiResult.result(OrderScheduleEnums.ScheduleErrMsg.API_COMMUNICATION_FAILED);
	        // responseBody null 체크
	        if (StringUtils.isEmpty (responseBody)) return ApiResult.result(ApiEnums.Default.RESPONSEBODY_NO_DATA);

	        // 잇슬림  배송 스케줄정보 결과 코드 값 체크
	        JSONArray orderInfoArr = (JSONArray)JSONObject.fromObject(responseBody).get(RESPONSE_CODE);
	        if (orderInfoArr == null || orderInfoArr.isEmpty() || orderInfoArr.toString().isEmpty() || orderInfoArr.size() < 1) return ApiResult.result(ApiEnums.Default.RESPONSE_FAIL);
	        JSONObject orderInfoCode = (JSONObject)orderInfoArr.get(0);
	        if (Integer.parseInt((String)orderInfoCode.get("resultCode")) < 0) return ApiResult.result(ApiEnums.Default.RESPONSE_FAIL);

	        JSONArray orderDeliveryArr = (JSONArray) JSONObject.fromObject(responseBody).get(RESPONSE_DATA);
	        if (!orderDeliveryArr.isEmpty()) {
	        	orderDeliveryArr.forEach(orderDeliveryInfo -> {
					JSONObject app = (JSONObject) orderDeliveryInfo;
					String state = StringUtil.nvl(app.get("state"), "");
					if(GOODS_STATUS.equals(state))
	        			eatsslimOrderDeliveryList.add(OBJECT_MAPPER.convertValue((JSONObject)orderDeliveryInfo, EatsslimOrderDeliveryListDto.class));
	        	});
	        }
		} catch (Exception e) {
			e.printStackTrace();
            return ApiResult.result(ApiEnums.Default.HTTP_STATUS_FAIL);
		}
        return ApiResult.result(eatsslimOrderDeliveryList, BaseEnums.Default.SUCCESS);
	}

	/**
     * @Desc 잇슬림 배송 스케줄정보 변경
     * @param eatsslimOrderUpdateInfoRequestDto
     * @return ApiResult
     */
	protected ApiResult<?> putOrderDeliveryInfo(EatsslimOrderUpdateInfoRequestDto eatsslimOrderUpdateInfoRequestDto) {
		HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");

		try {
			HttpEntity<JSONObject> requestEntity = new HttpEntity<JSONObject>(JSONObject.fromObject(OBJECT_MAPPER.writeValueAsString(eatsslimOrderUpdateInfoRequestDto)), headers);
			UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromHttpUrl(url + UPDATE_ES_ORDER_DELIVERY_INFO);
			ResponseEntity<String> responseEntity = restTemplateUtil.post(urlBuilder, requestEntity, String.class);
			String responseBody = URLDecoder.decode(responseEntity.getBody(), "UTF-8");
			log.info("responseBody={}", responseBody);

			// 통신 결과 체크
	        if (responseEntity.getStatusCode() != HttpStatus.OK ) return ApiResult.result(OrderScheduleEnums.ScheduleErrMsg.API_COMMUNICATION_FAILED);
	        // responseBody null 체크
	        if (StringUtils.isEmpty(responseBody)) return ApiResult.result(ApiEnums.Default.RESPONSEBODY_NO_DATA);

	        // 잇슬림  배송 스케줄정보 변경 결과 코드 값 체크
	        JSONArray orderInfoArr = (JSONArray)JSONObject.fromObject(responseBody).get(RESPONSE_CODE);
	        if (orderInfoArr == null || orderInfoArr.isEmpty() || orderInfoArr.toString().isEmpty() || orderInfoArr.size() < 1) return ApiResult.result(ApiEnums.Default.RESPONSE_FAIL);
	        JSONObject orderInfoCode = (JSONObject)orderInfoArr.get(0);
	        if (Integer.parseInt((String)orderInfoCode.get("resultCode")) < 0) return ApiResult.result(ApiEnums.Default.RESPONSE_FAIL);
		} catch (Exception e) {
			e.printStackTrace();
            return ApiResult.result(ApiEnums.Default.HTTP_STATUS_FAIL);
		}
        return ApiResult.success();
	}
}