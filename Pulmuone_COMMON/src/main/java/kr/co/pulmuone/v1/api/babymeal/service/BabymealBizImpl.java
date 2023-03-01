package kr.co.pulmuone.v1.api.babymeal.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.api.babymeal.dto.BabymealOrderDeliveryListDto;
import kr.co.pulmuone.v1.api.babymeal.dto.BabymealOrderDeliveryListRequestDto;
import kr.co.pulmuone.v1.api.babymeal.dto.BabymealOrderDeliveryListResponseDto;
import kr.co.pulmuone.v1.api.babymeal.dto.BabymealOrderInfoDto;
import kr.co.pulmuone.v1.api.babymeal.dto.BabymealOrderUpdateInfoRequestDto;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.ApiEnums;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.util.DateUtil;
import kr.co.pulmuone.v1.comm.util.DeviceUtil;
import kr.co.pulmuone.v1.order.schedule.dto.OrderDetailScheduleListRequestDto;

/**
 * <PRE>
 * Forbiz Korea
 * 베이비밀  API I/F Impl
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

@Service
public class BabymealBizImpl implements BabymealBiz {

    @Autowired
    BabymealService babyMealService;

	@Value("${udms.userID}")
	private String userID;

	@Value("${udms.authKey}")
	private String authKey;

	@Value("${udms.apiId}")
	private String apiId;

	/**
	 * @Dec 베이비밀 주문정보, 배송 스케줄정보 조회
	 * @param orderDetailScheduleListRequestDto
	 * @return ApriResult<?>
	 */
	@Override
	public ApiResult<?> getOrderScheduleList(OrderDetailScheduleListRequestDto orderDetailScheduleListRequestDto) throws Exception {
		Map<String, Object> apiResultMap = new HashMap<String, Object>();

		// 베이비밀 주문정보, 배송 스케줄 정보 조회
		BabymealOrderDeliveryListRequestDto babymealOrderDeliveryListRequestDto = BabymealOrderDeliveryListRequestDto.builder()
				.currentIp(DeviceUtil.getServerIp())
				.authKey(authKey)
				.userID(userID)
				.apiId(apiId)
				.companyID(ApiEnums.BabymealGetOrderInfoDateType.SEARCH_BM_ORDER_DELIVERY_LIST.getCompanyID())
				.siteCd(ApiEnums.BabymealGetOrderInfoDateType.SEARCH_BM_ORDER_DELIVERY_LIST.getSiteCd())
				.serviceCd(ApiEnums.BabymealGetOrderInfoDateType.SEARCH_BM_ORDER_DELIVERY_LIST.getServiceCd())
				.shopOrderNo(orderDetailScheduleListRequestDto.getOdid())
				.podSeq(Integer.toString(orderDetailScheduleListRequestDto.getOdOrderDetlSeq()))
				.build();

		ApiResult<?> resultOrderListApi = babyMealService.getOrderScheduleList(babymealOrderDeliveryListRequestDto);
		if (Integer.parseInt(resultOrderListApi.getCode()) < 0) return resultOrderListApi;

		int saleSeq = 0;
 		String toDayYmd = DateUtil.getCurrentDate();
 		for (BabymealOrderDeliveryListDto babymealOrderDeliveryListDto : (List<BabymealOrderDeliveryListDto>)((BabymealOrderDeliveryListResponseDto)resultOrderListApi.getData()).getRows()) {
 			if (Integer.parseInt(babymealOrderDeliveryListDto.getDelvDate().replaceAll("-", "")) > Integer.parseInt(toDayYmd)) saleSeq++;
 		}
 		((BabymealOrderDeliveryListResponseDto)resultOrderListApi.getData()).getGoodsInfo().setSaleSeq(saleSeq);

 		apiResultMap.put("orderInfo", ((BabymealOrderDeliveryListResponseDto)resultOrderListApi.getData()).getGoodsInfo());
 		apiResultMap.put("orderScheduleList", (List<BabymealOrderDeliveryListDto>)((BabymealOrderDeliveryListResponseDto)resultOrderListApi.getData()).getRows());
		return ApiResult.result(apiResultMap, BaseEnums.Default.SUCCESS);
	}

	/**
     * @Desc 베이비밀 스케줄 배송일자 변경
     * @param eatsslimOrderInfoDto(베이비밀 주문정보), babymealOrderDeliveryListDtoList(베이비밀 변경 스케줄 정보)
     * @return ApriResult<?>
     */
	@Override
	public ApiResult<?> putScheduleArrivalDate(BabymealOrderInfoDto babymealOrderInfoDto, List<BabymealOrderDeliveryListDto> babymealOrderDeliveryListDtoList) throws Exception {

		Map<String, String> paramMap = new HashMap<String, String>();

		babymealOrderDeliveryListDtoList.forEach(babymealOrderDeliveryListDto -> {
			paramMap.put("kindCd", paramMap.get("kindCd") == null ? babymealOrderDeliveryListDto.getGoodsKindCD() : paramMap.get("kindCd") + "|" + babymealOrderDeliveryListDto.getGoodsKindCD());
			paramMap.put("ordAddr1Ji", paramMap.get("ordAddr1Ji") == null ? babymealOrderDeliveryListDto.getRecvAddr1() : paramMap.get("ordAddr1Ji") + "|" + babymealOrderDeliveryListDto.getRecvAddr1());
			paramMap.put("ordAddr2", paramMap.get("ordAddr2") == null ? babymealOrderDeliveryListDto.getRecvAddr2() : paramMap.get("ordAddr2") + "|" + babymealOrderDeliveryListDto.getRecvAddr2());
			paramMap.put("deliverySeq", paramMap.get("deliverySeq") == null ? babymealOrderDeliveryListDto.getId() : paramMap.get("deliverySeq") + "|" + babymealOrderDeliveryListDto.getId());
			paramMap.put("jisaCd", paramMap.get("jisaCd") == null ? babymealOrderDeliveryListDto.getUrStoreId() : paramMap.get("jisaCd") + "|" + babymealOrderDeliveryListDto.getUrStoreId());
			paramMap.put("activeFlg", paramMap.get("activeFlg") == null ? babymealOrderDeliveryListDto.getActiveFlg() : paramMap.get("activeFlg") + "|" + babymealOrderDeliveryListDto.getActiveFlg());
			paramMap.put("addDt", paramMap.get("addDt") == null ? babymealOrderDeliveryListDto.getDeliveryDate() : paramMap.get("addDt") + "|" + babymealOrderDeliveryListDto.getDeliveryDate());
			paramMap.put("giftTypeCd", paramMap.get("giftTypeCd") == null ? ApiEnums.BabymealPutOrderInfoDateType.UPDATE_BM_ORDER_DELIVERY_LIST.getGiftTypeCd() : paramMap.get("giftTypeCd") + "|" + ApiEnums.BabymealPutOrderInfoDateType.UPDATE_BM_ORDER_DELIVERY_LIST.getGiftTypeCd());
			paramMap.put("goodsGroupId", paramMap.get("goodsGroupId") == null ? babymealOrderDeliveryListDto.getGoodsGoodsGroupID() : paramMap.get("goodsGroupId") + "|" + babymealOrderDeliveryListDto.getGoodsGoodsGroupID());
			paramMap.put("deliveryDate", paramMap.get("deliveryDate") == null ? babymealOrderDeliveryListDto.getDeliveryDate() : paramMap.get("deliveryDate") + "|" + babymealOrderDeliveryListDto.getDeliveryDate());
			paramMap.put("originDate", paramMap.get("originDate") == null ? babymealOrderDeliveryListDto.getDelvDate() : paramMap.get("originDate") + "|" + babymealOrderDeliveryListDto.getDelvDate());
			paramMap.put("giftChange", paramMap.get("giftChange") == null ? "N" : paramMap.get("giftChange") + "|" + "N");
			paramMap.put("kind", paramMap.get("kind") == null ? babymealOrderDeliveryListDto.getGoodsKind() : paramMap.get("kind") + "|" + babymealOrderDeliveryListDto.getGoodsKind());
			paramMap.put("giftType", paramMap.get("giftType") == null ? ApiEnums.BabymealPutOrderInfoKindCd.NORMAL.getCodeName() : paramMap.get("giftType") + "|" + ApiEnums.BabymealPutOrderInfoKindCd.NORMAL.getCodeName());

			String giftKey = babymealOrderDeliveryListDto.getGoodsOrderNo() + "_" +
					babymealOrderDeliveryListDto.getDelvDate() + "_" +
					babymealOrderDeliveryListDto.getGoodsStatus() + "_" +
					babymealOrderDeliveryListDto.getGoodsKind() + "_" +
					babymealOrderDeliveryListDto.getGoodsKindCD() + "_" +
					babymealOrderDeliveryListDto.getIlGoodsId();
			paramMap.put("giftKey", paramMap.get("giftKey") == null ? giftKey : paramMap.get("giftKey") + "|" + giftKey);
			paramMap.put("ordZipCd", paramMap.get("ordZipCd") == null ? babymealOrderDeliveryListDto.getRecvZipCd() : paramMap.get("ordZipCd") + "|" + babymealOrderDeliveryListDto.getRecvZipCd());
			paramMap.put("ordAddr1Do", paramMap.get("ordAddr1Do") == null ? babymealOrderDeliveryListDto.getRecvAddr1Do() : paramMap.get("ordAddr1Do") + "|" + babymealOrderDeliveryListDto.getRecvAddr1Do());
			paramMap.put("addCnt", paramMap.get("addCnt") == null ? babymealOrderDeliveryListDto.getOrderCnt() : paramMap.get("addCnt") + "|" + babymealOrderDeliveryListDto.getOrderCnt());
		});

		// 베이비밀 스케줄 배송일 변경
		BabymealOrderUpdateInfoRequestDto babymealOrderUpdateInfoRequestDto = BabymealOrderUpdateInfoRequestDto.builder()
				.currentIp(ApiEnums.BabymealPutOrderInfoDateType.UPDATE_BM_ORDER_DELIVERY_LIST.getCurrentIp())
				.authKey(authKey)
				.userID(userID)
				.userName(ApiEnums.BabymealPutOrderInfoDateType.UPDATE_BM_ORDER_DELIVERY_LIST.getUserName())
				.apiId(apiId)
				.siteCd(ApiEnums.BabymealPutOrderInfoDateType.UPDATE_BM_ORDER_DELIVERY_LIST.getSiteCd())
				.serviceCd(ApiEnums.BabymealPutOrderInfoDateType.UPDATE_BM_ORDER_DELIVERY_LIST.getServiceCd())
				.destsiteCD(ApiEnums.BabymealPutOrderInfoDateType.UPDATE_BM_ORDER_DELIVERY_LIST.getDestsiteCD())
				.languageCD(ApiEnums.BabymealPutOrderInfoDateType.UPDATE_BM_ORDER_DELIVERY_LIST.getLanguageCD())
				.localeCD(ApiEnums.BabymealPutOrderInfoDateType.UPDATE_BM_ORDER_DELIVERY_LIST.getLocaleCD())
				.clientDate(DateUtil.getCurrentDate())
				.timezoneCD(ApiEnums.BabymealPutOrderInfoDateType.UPDATE_BM_ORDER_DELIVERY_LIST.getTimezoneCD())
				.currencyCD(ApiEnums.BabymealPutOrderInfoDateType.UPDATE_BM_ORDER_DELIVERY_LIST.getCurrencyCD())
				.dateFormatCD("")
				.amtFormatCD("")
				.orderNo(babymealOrderInfoDto.getOrderNum())
				.deliveryType(ApiEnums.BabymealPutOrderInfoDateType.UPDATE_BM_ORDER_DELIVERY_LIST.getDeliveryType())
				.spartnerDivCD(ApiEnums.BabymealPutOrderInfoDateType.UPDATE_BM_ORDER_DELIVERY_LIST.getSpartnerDivCD())
				.kindCd(paramMap.get("kindCd"))
				.ordAddr1Ji(paramMap.get("ordAddr1Ji"))
				.ordAddr2(paramMap.get("ordAddr2"))
				.deliverySeq(paramMap.get("deliverySeq"))
				.jisaCd(paramMap.get("jisaCd"))
				.activeFlg(paramMap.get("activeFlg"))
				.addDt(paramMap.get("addDt"))
				.giftTypeCd(paramMap.get("giftTypeCd"))
				.goodsGroupId(paramMap.get("goodsGroupId"))
				.deliveryDate(paramMap.get("deliveryDate"))
				.originDate(paramMap.get("originDate"))
				.giftChange(paramMap.get("giftChange"))
				.kind(paramMap.get("kind"))
				.giftType(paramMap.get("giftType"))
				.giftKey(paramMap.get("giftKey"))
				.ordZipCd(paramMap.get("ordZipCd"))
				.ordAddr1Do(paramMap.get("ordAddr1Do"))
				.addCnt(paramMap.get("addCnt"))
				.build();

		// 베이비밀 배송 스케줄정보 변경
		return babyMealService.putOrderDeliveryInfo(babymealOrderUpdateInfoRequestDto);
	}
}