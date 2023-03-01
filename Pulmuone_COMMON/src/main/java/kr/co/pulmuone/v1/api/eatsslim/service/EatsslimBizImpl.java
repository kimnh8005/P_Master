package kr.co.pulmuone.v1.api.eatsslim.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.api.eatsslim.dto.EatsslimOrderDeliveryListDto;
import kr.co.pulmuone.v1.api.eatsslim.dto.EatsslimOrderDeliveryListRequestDto;
import kr.co.pulmuone.v1.api.eatsslim.dto.EatsslimOrderInfoDto;
import kr.co.pulmuone.v1.api.eatsslim.dto.EatsslimOrderInfoRequestDto;
import kr.co.pulmuone.v1.api.eatsslim.dto.EatsslimOrderUpdateInfoRequestDto;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.ApiEnums;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.util.DateUtil;
import kr.co.pulmuone.v1.order.schedule.dto.OrderDetailScheduleListRequestDto;

/**
* <PRE>
* Forbiz Korea
* 잇슬림 I/F BizImpl
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                	:  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2021. 01. 28.		이규한          최초작성
* =======================================================================
* </PRE>
*/

@Service
public class EatsslimBizImpl implements EatsslimBiz {

    @Autowired
    EatsSlimService eatsSlimService;

	@Value("${udms.userID}")
	private String userID;

	@Value("${udms.authKey}")
	private String authKey;

	@Value("${udms.apiId}")
	private String apiId;

    /**
     * @Desc 잇슬림 주문정보, 배송 스케쥴정보 조회
     * @param orderDetailScheduleListRequestDto
     * @return ApriResult<?>
     */
	@SuppressWarnings("unchecked")
	@Override
	public ApiResult<?> getOrderScheduleList(OrderDetailScheduleListRequestDto orderDetailScheduleListRequestDto) throws Exception {
		Map<String, Object> apiResultMap = new HashMap<String, Object>();

		// 잇슬림 주문정보 조회
		EatsslimOrderInfoRequestDto eatsslimOrderInfoRequestDto = EatsslimOrderInfoRequestDto.builder()
				.currentIp(ApiEnums.EatsSlimOrderInfoDateType.SEARCH_ES_ORDER_LIST.getCurrentIp())
				.authKey(authKey)
				.userID(userID)
				.apiId(apiId)
				.siteCd(ApiEnums.EatsSlimOrderInfoDateType.SEARCH_ES_ORDER_LIST.getSiteCd())
				.mode(ApiEnums.EatsSlimOrderInfoDateType.SEARCH_ES_ORDER_LIST.getMode())
				.modeSeq(ApiEnums.EatsSlimOrderInfoDateType.SEARCH_ES_ORDER_LIST.getModeSeq())
				.serviceCd(ApiEnums.EatsSlimOrderInfoDateType.SEARCH_ES_ORDER_LIST.getServiceCd())
				.outOrderNum(orderDetailScheduleListRequestDto.getOdid())
				.gooodsSetId(orderDetailScheduleListRequestDto.getIlGoodsId())
				.build();

		ApiResult<?> resultOrderListApi = eatsSlimService.getOrderList(eatsslimOrderInfoRequestDto);
		if (Integer.parseInt(resultOrderListApi.getCode()) < 0) return resultOrderListApi;

 		// 잇슬림 배송 스케줄 정보 조회
 		EatsslimOrderDeliveryListRequestDto eatsslimOrderDeliveryListRequestDto = EatsslimOrderDeliveryListRequestDto.builder()
				.currentIp(ApiEnums.EatsSlimOrderInfoDateType.SEARCH_ES_ORDER_DELIVERY_LIST.getCurrentIp())
				.authKey(authKey)
				.userID(userID)
				.apiId(apiId)
				.siteCd(ApiEnums.EatsSlimOrderInfoDateType.SEARCH_ES_ORDER_DELIVERY_LIST.getSiteCd())
				.mode(ApiEnums.EatsSlimOrderInfoDateType.SEARCH_ES_ORDER_DELIVERY_LIST.getMode())
				.modeSeq(ApiEnums.EatsSlimOrderInfoDateType.SEARCH_ES_ORDER_DELIVERY_LIST.getModeSeq())
				.serviceCd(ApiEnums.EatsSlimOrderInfoDateType.SEARCH_ES_ORDER_DELIVERY_LIST.getServiceCd())
				.outOrderNum(orderDetailScheduleListRequestDto.getOdid())
				.goodsSetId(orderDetailScheduleListRequestDto.getIlGoodsId())
				.destsiteCD(ApiEnums.EatsSlimOrderInfoDateType.SEARCH_ES_ORDER_DELIVERY_LIST.getDestsiteCD())
				.build();
 		ApiResult<?> resulOrderDeliveryListApi = eatsSlimService.getOrderDeliveryList(eatsslimOrderDeliveryListRequestDto);
 		if (Integer.parseInt(resulOrderDeliveryListApi.getCode()) < 0) return resulOrderDeliveryListApi;

 		EatsslimOrderInfoDto eatsslimOrderInfoDto = (EatsslimOrderInfoDto) resultOrderListApi.getData();

 		int saleSeq = 0;
 		String toDayYmd = DateUtil.getCurrentDate();
 		for (EatsslimOrderDeliveryListDto eatsslimOrderDeliveryListDto : ((List<EatsslimOrderDeliveryListDto>)resulOrderDeliveryListApi.getData())) {
 			if (Integer.parseInt(eatsslimOrderDeliveryListDto.getDelvDate().replaceAll("-", "")) > Integer.parseInt(toDayYmd)) saleSeq++;
 		}
 		eatsslimOrderInfoDto.setSaleSeq(saleSeq);

 		apiResultMap.put("orderInfo", eatsslimOrderInfoDto);
 		apiResultMap.put("orderScheduleList", resulOrderDeliveryListApi.getData());

		return ApiResult.result(apiResultMap, BaseEnums.Default.SUCCESS);
	}

	/**
     * @Desc 잇슬림 스케줄 배송일자 변경
     * @param eatsslimOrderInfoDto(잇슬림 주문정보), eatsslimOrderDeliveryListDto(잇슬림 변경 스케줄 정보)
     * @return ApriResult<?>
     */
	@Override
	public ApiResult<?> putScheduleArrivalDate(EatsslimOrderInfoDto eatsslimOrderInfoDto, EatsslimOrderDeliveryListDto eatsslimOrderDeliveryListDto) throws Exception {

		// 잇슬림 스케줄 배송일 변경
		EatsslimOrderUpdateInfoRequestDto eatsslimOrderUpdateInfoRequestDto = EatsslimOrderUpdateInfoRequestDto.builder()
				.currentIp(ApiEnums.EatsSlimOrderInfoDateType.UPDATE_ES_ORDER_INFO.getCurrentIp())
				.authKey(authKey)
				.userID(userID)
				.userId(userID)
				.userNm(ApiEnums.EatsSlimOrderInfoDateType.UPDATE_ES_ORDER_INFO.getUserNm())
				.apiId(apiId)
				.siteCd(ApiEnums.EatsSlimOrderInfoDateType.UPDATE_ES_ORDER_INFO.getSiteCd())
				.mode(ApiEnums.EatsSlimOrderInfoDateType.UPDATE_ES_ORDER_INFO.getMode())
				.modeSeq(ApiEnums.EatsSlimOrderInfoDateType.UPDATE_ES_ORDER_INFO.getModeSeq())
				.serviceCd(ApiEnums.EatsSlimOrderInfoDateType.UPDATE_ES_ORDER_INFO.getServiceCd())
				.destsiteCD(ApiEnums.EatsSlimOrderInfoDateType.UPDATE_ES_ORDER_INFO.getDestsiteCD())
				.insertTypeTxt(ApiEnums.EatsSlimOrderInfoDateType.UPDATE_ES_ORDER_INFO.getInsertType())
				.activeFlgTxt(ApiEnums.EatsSlimOrderInfoDateType.UPDATE_ES_ORDER_INFO.getActiveFlag())
				.giftTypeTxt(eatsslimOrderDeliveryListDto.getStateDetail())		// 증정타입
				.orderNumTxt(eatsslimOrderInfoDto.getOrderNum())				// I/F 주문번호 (잇슬림주문번호)
				.groupCodeTxt(eatsslimOrderDeliveryListDto.getIlGoodsId())		// I/F 상품 코드 (상품그룹코드)
				.zipCodeTxt(eatsslimOrderDeliveryListDto.getRecvZipCd())		// 수령인 우편번호
				.addressTxt(eatsslimOrderDeliveryListDto.getRecvAddr1())		// 수령인 주소1
				.addressDetailTxt(eatsslimOrderDeliveryListDto.getRecvAddr2())	// 수령인 주소2(상세주소)
				.idTxt(eatsslimOrderDeliveryListDto.getId())					// 잇슬림 주문상품 고유ID(PK)
				.oSubNumTxt(eatsslimOrderInfoDto.getOSubNum())					// I/F 서브주문번호 (잇슬림 서브주문번호)
				.stdateTxt(eatsslimOrderDeliveryListDto.getDelvDate())			// 배송일자 (고객이 변경한 날짜)
				.gubunCodeTxt(eatsslimOrderDeliveryListDto.getIlGoodsId())		// I/F 상품 코드 (상품그룹코드)
				.orderCntTxt(eatsslimOrderDeliveryListDto.getOrderCnt())		// 주문수량
				.agencyIdTxt(eatsslimOrderDeliveryListDto.getUrStoreId())		// 가맹점코드
				.build();

		// 잇슬림 배송 스케줄정보 변경
		return eatsSlimService.putOrderDeliveryInfo(eatsslimOrderUpdateInfoRequestDto);
	}
}