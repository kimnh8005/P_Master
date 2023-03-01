package kr.co.pulmuone.v1.api.babymeal.service;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import kr.co.pulmuone.v1.comm.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.pulmuone.v1.api.babymeal.dto.BabymealOrderDeliveryListDto;
import kr.co.pulmuone.v1.api.babymeal.dto.BabymealOrderDeliveryListRequestDto;
import kr.co.pulmuone.v1.api.babymeal.dto.BabymealOrderDeliveryListResponseDto;
import kr.co.pulmuone.v1.api.babymeal.dto.BabymealOrderInfoDto;
import kr.co.pulmuone.v1.api.babymeal.dto.BabymealOrderUpdateInfoRequestDto;
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
 * 베이비밀  API Service
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 01. 26.	이규한		최초작성
 * =======================================================================
 * </PRE>
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class BabymealService {

	@Autowired
	private RestTemplateUtil restTemplateUtil;

	private static final ObjectMapper OBJECT_MAPPER = JsonUtil.OBJECT_MAPPER;

	@Value("${udms.url}")
	private String url;

	private static final String SEARCH_BM_ORDER_DELIVERY_LIST		= "/api/babymeal/searchOrderInfoDetail";			// 베이비밀 주문정보, 배송 스케줄 정보 조회 I/F ID

	private static final String UPDATE_BM_ORDER_DELIVERY_INFO 		= "/api/babymeal/updateOrder";						// 베이비밀 배송 스케줄정보 변경 I/F ID

	private static final String RESPONSE_CODE						= "info";			// 결과 데이터 코드 JSON Key

	private static final String RESPONSE_DATA						= "items";			// 결과 데이터 JSON Key

	private static final String GOODS_DATA							= "detailInfo";		// 결과 데이터 주문정보 JSON Key

	private static final String DELIVERY_DATA						= "goodsInfo";		// 결과 데이터 배송 스케줄 정보 JSON Key

	private static final String GOODS_STATUS						= "0001";			// 정상주문
	/**
	 * @Dec 베이비밀 주문정보, 배송 스케줄 정보 조회
	 * @param babymealOrderDeliveryListRequestDto
	 * @return ApiResult
	 */
	@SuppressWarnings("unchecked")
	public ApiResult<?> getOrderScheduleList(BabymealOrderDeliveryListRequestDto babymealOrderDeliveryListRequestDto) {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Accept", "application/json");

		BabymealOrderDeliveryListResponseDto babymealOrderDeliveryListResponseDto = null;
		List<BabymealOrderDeliveryListDto> babymealOrderDeliveryList = new ArrayList<BabymealOrderDeliveryListDto>();

		try {
			HttpEntity<JSONObject> requestEntity = new HttpEntity<JSONObject>(JSONObject.fromObject(OBJECT_MAPPER.writeValueAsString(babymealOrderDeliveryListRequestDto)), headers);
			UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromHttpUrl(url + SEARCH_BM_ORDER_DELIVERY_LIST);
			ResponseEntity<String> responseEntity = restTemplateUtil.post(urlBuilder, requestEntity, String.class);
			String responseBody = URLDecoder.decode(responseEntity.getBody(), "UTF-8").replaceAll("null", ".");
			log.info("responseBody={}", responseBody);

			// 통신 결과 체크
			if (responseEntity.getStatusCode() != HttpStatus.OK) return ApiResult.result(OrderScheduleEnums.ScheduleErrMsg.API_COMMUNICATION_FAILED);
			// responseBody null 체크
			if (StringUtils.isEmpty(responseBody)) return ApiResult.result(ApiEnums.Default.RESPONSEBODY_NO_DATA);

			// 베이비밀 주문, 배송정보 결과 코드 값 체크
	        JSONArray orderInfoArr = (JSONArray)JSONObject.fromObject(responseBody).get(RESPONSE_CODE);
	        if (orderInfoArr == null || orderInfoArr.isEmpty() || orderInfoArr.toString().isEmpty() || orderInfoArr.size() < 1) return ApiResult.result(ApiEnums.Default.RESPONSE_FAIL);
	        JSONObject orderInfoCode = (JSONObject)orderInfoArr.get(0);
	        if (Integer.parseInt((String)orderInfoCode.get("resultCode")) < 0) return ApiResult.result(ApiEnums.Default.RESPONSE_FAIL);

	        // 베이비밀 주문, 배송 스케줄 정보조회 결과 데이터 null 체크
	        JSONArray orderScheduleArr = (JSONArray)JSONObject.fromObject(responseBody).get(RESPONSE_DATA);
	        if (orderScheduleArr == null || orderScheduleArr.isEmpty() || orderScheduleArr.toString().isEmpty() || orderScheduleArr.size() < 1) return ApiResult.result(OrderScheduleEnums.ScheduleErrMsg.API_SCHEDULE_NO_DATA);
	        // 주문정보 null 체크
	        JSONArray orderArr = (JSONArray)((JSONObject)orderScheduleArr.get(0)).get(GOODS_DATA);
	        if (orderArr == null || orderArr.isEmpty() || orderArr.toString().isEmpty() || orderScheduleArr.size() < 1) return ApiResult.result(OrderScheduleEnums.ScheduleErrMsg.API_SCHEDULE_NO_DATA);
	        // 배송 스케줄 정보 null 체크
	        JSONArray scheduleArr = (JSONArray)((JSONObject)orderScheduleArr.get(0)).get(DELIVERY_DATA);

	        log.info("orderInfo={}", orderArr.get(0));
	        log.info("scheduleArr={}", scheduleArr);

	        // 주문정보 Dto 셋팅
	        BabymealOrderInfoDto babymealOrderInfoDto = OBJECT_MAPPER.convertValue(orderArr.get(0), BabymealOrderInfoDto.class);

	        // 배송 스케줄정보 Dto 셋팅
	        if (!scheduleArr.isEmpty()) {
	        	scheduleArr.forEach(orderDeliveryInfo -> {
					JSONObject app = (JSONObject) orderDeliveryInfo;
					String goodsStatus = StringUtil.nvl(app.get("goodsStatus"), "");
	        		BabymealOrderDeliveryListDto babymealOrderDeliveryListDto = OBJECT_MAPPER.convertValue((JSONObject)orderDeliveryInfo , BabymealOrderDeliveryListDto.class);
	        		if (!"NO".equals(babymealOrderDeliveryListDto.getGoodsAllergyType())) {
	        			babymealOrderDeliveryListDto.setGoodsNm(babymealOrderDeliveryListDto.getGoodsNm() + ApiEnums.BabymealGetOrderInfoDateType.SEARCH_BM_ORDER_DELIVERY_LIST.getAllergyTypeNm());
	        		}
					if(GOODS_STATUS.equals(goodsStatus))
	        			babymealOrderDeliveryList.add(babymealOrderDeliveryListDto);
	        	});
	        }

	        babymealOrderDeliveryListResponseDto = BabymealOrderDeliveryListResponseDto.builder()
	        		.goodsInfo(babymealOrderInfoDto)
	        		.rows(babymealOrderDeliveryList)
	        		.build();

		} catch (Exception e) {
			e.printStackTrace();
            return ApiResult.result(ApiEnums.Default.HTTP_STATUS_FAIL);
		}
		return ApiResult.result(babymealOrderDeliveryListResponseDto, BaseEnums.Default.SUCCESS);
	}

	/**
     * @Desc 베이비밀 배송 스케줄정보 변경
     * @param babymealOrderUpdateInfoRequestDto
     * @return ApiResult
     */
	public ApiResult<?> putOrderDeliveryInfo(BabymealOrderUpdateInfoRequestDto babymealOrderUpdateInfoRequestDto) {
		HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");

		try {
			HttpEntity<JSONObject> requestEntity = new HttpEntity<JSONObject>(JSONObject.fromObject(OBJECT_MAPPER.writeValueAsString(babymealOrderUpdateInfoRequestDto)), headers);
			UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromHttpUrl(url + UPDATE_BM_ORDER_DELIVERY_INFO);
			ResponseEntity<String> responseEntity = restTemplateUtil.post(urlBuilder, requestEntity, String.class);
			String responseBody = URLDecoder.decode(responseEntity.getBody(), "UTF-8");
			log.info("responseBody={}", responseBody);

			// 통신 결과 체크
	        if (responseEntity.getStatusCode() != HttpStatus.OK ) return ApiResult.result(OrderScheduleEnums.ScheduleErrMsg.API_COMMUNICATION_FAILED);
	        // responseBody null 체크
	        if (StringUtils.isEmpty(responseBody)) return ApiResult.result(ApiEnums.Default.RESPONSEBODY_NO_DATA);

	        // 통신 결과 체크
	        if (responseEntity.getStatusCode() != HttpStatus.OK ) return ApiResult.result(OrderScheduleEnums.ScheduleErrMsg.API_COMMUNICATION_FAILED);
	        // responseBody null 체크
	        if (StringUtils.isEmpty(responseBody)) return ApiResult.result(ApiEnums.Default.RESPONSEBODY_NO_DATA);

	        // 베이비밀  배송 스케줄정보 변경 결과 코드 값 체크
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