package kr.co.pulmuone.v1.order.status.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.ErpApiEnums;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.comm.enums.OrderEnums;
import kr.co.pulmuone.v1.comm.enums.SystemEnums;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimDetlDtInfoDto;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimGoodsInfoDto;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimRegisterRequestDto;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimViewRequestDto;
import kr.co.pulmuone.v1.order.claim.dto.vo.ClaimDetlHistVo;
import kr.co.pulmuone.v1.order.claim.dto.vo.ClaimDetlVo;
import kr.co.pulmuone.v1.order.claim.dto.vo.ClaimDetlVo.ClaimDetlVoBuilder;
import kr.co.pulmuone.v1.order.date.service.OrderDateBiz;
import kr.co.pulmuone.v1.order.delivery.dto.vo.ClaimNumberSearchVo;
import kr.co.pulmuone.v1.order.delivery.dto.vo.OrderTrackingNumberVo;
import kr.co.pulmuone.v1.order.delivery.service.TrackingNumberBiz;
import kr.co.pulmuone.v1.order.email.dto.OrderDetailGoodsDto;
import kr.co.pulmuone.v1.order.email.dto.OrderInfoForEmailResultDto;
import kr.co.pulmuone.v1.order.email.service.OrderEmailBiz;
import kr.co.pulmuone.v1.order.email.service.OrderEmailSendBiz;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetlHistVo;
import kr.co.pulmuone.v1.order.status.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

/**
 * <PRE>
 * Forbiz Korea
 * 주문상태 관련 Implements
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 29.     이명수         최초작성
 *  1.1    2021. 01. 11.     김명진         주문상세상태 변경, 클레임상세상태 변경 이력 등록 추가
 * =======================================================================
 * </PRE>
 */
@Slf4j
@Service
public class OrderStatusBizImpl implements OrderStatusBiz {

	@Autowired
    private OrderStatusService orderStatusService;

	@Autowired
	private TrackingNumberBiz trackingNumberBiz;

	@Autowired
	private OrderDateBiz orderDateBiz;

	@Autowired
	public OrderEmailBiz orderEmailBiz;

	@Autowired
	public OrderEmailSendBiz orderEmailSendBiz;

    /**
     * 주문상태 변경 전에 현재 주문상태 조회
     * @param orderStatusSelectRequestDto
     * @return
     */
	public ApiResult<?> getOrderDetailStatusInfo(OrderStatusSelectRequestDto orderStatusSelectRequestDto) {
		List<OrderStatusSelectResponseDto> orderStatusSelectResponseDto =  orderStatusService.getOrderDetailStatusInfo(orderStatusSelectRequestDto);
		return ApiResult.success(orderStatusSelectResponseDto);
	}

	/**
	 * 주문상태 수정
	 * @param orderStatusUpdateRequestDto
	 * @return
	 * @throws Exception
	 */
	@Override
	//@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false, rollbackFor = { BaseException.class, Exception.class })
	public ApiResult<?> putOrderDetailListStatus(OrderStatusUpdateRequestDto orderStatusUpdateRequestDto) throws Exception {


		int cnt = 0;

		List<OrderStatusUpdateDto> orderStatusUpdateList = new ArrayList<>();
		List<Long> odOrderDetlIdList	= orderStatusUpdateRequestDto.getDetlIdList();
		String orderStatusCd			= orderStatusUpdateRequestDto.getStatusCd();
		long userId						= orderStatusUpdateRequestDto.getUserId();
		String loginName				= orderStatusUpdateRequestDto.getLoginName();
		OrderEnums.OrderDetailStatusHistMsg orderDetailStatusHistMsg = OrderEnums.OrderDetailStatusHistMsg.findByCode(orderStatusCd);

		int totalCount		= odOrderDetlIdList.size();
		int successCount	= 0;
		int failCount		= 0;

		if(OrderEnums.OrderStatus.DELIVERY_READY.getCode().equals(orderStatusCd)){				// 배송준비중

			int checkCnt = orderStatusService.selectOrderInterfaceTargetCheck(odOrderDetlIdList, null);

			if(checkCnt > 0){
				OrderEnums.OrderErrMsg resultMsg = totalCount > 1 ? OrderEnums.OrderErrMsg.MANY_ORDER_STATE_CHANGE : OrderEnums.OrderErrMsg.ONE_ORDER_STATE_CHANGE;
				return ApiResult.result(resultMsg);
			}

			// 기존 상태값 체크
			List<String> checkOrderDetlIdList = orderStatusService.selectTargetOverlapOrderStatusList(orderStatusCd, odOrderDetlIdList);
			failCount		= checkOrderDetlIdList.size();
			odOrderDetlIdList = odOrderDetlIdList.stream()
					.filter(x -> checkOrderDetlIdList.contains(x.toString()) != true)
					.collect(Collectors.toList());
			successCount	= odOrderDetlIdList.size();
		}

		// 배송중
		if(OrderEnums.OrderStatus.DELIVERY_ING.getCode().equals(orderStatusCd)) {

			String[] urWarehouseIds = { ErpApiEnums.UrWarehouseId.EATSSLIM_D3_FRANCHISEE.getCode()
					, ErpApiEnums.UrWarehouseId.EATSSLIM_D2_3PL.getCode()
					, ErpApiEnums.UrWarehouseId.EATSSLIM_D3_DELIVERY.getCode()
					, ErpApiEnums.UrWarehouseId.ORGA_STORE.getCode() };
			int checkCnt = orderStatusService.selectOrderInterfaceTargetCheck(odOrderDetlIdList, urWarehouseIds);

			if(checkCnt > 0){
				OrderEnums.OrderErrMsg resultMsg = totalCount > 1 ? OrderEnums.OrderErrMsg.MANY_ORDER_STATE_CHANGE : OrderEnums.OrderErrMsg.ONE_ORDER_STATE_CHANGE;
				return ApiResult.result(resultMsg);
			}

			List<Long> shippingCompIdList	= orderStatusUpdateRequestDto.getShippingCompIdList();
			List<String> trackingNoList		= orderStatusUpdateRequestDto.getTrackingNoList();

			for(int i=0;i<shippingCompIdList.size();i++) {


				OrderTrackingNumberVo orderTrackingNumberVo = OrderTrackingNumberVo.builder()
						.odOrderDetlId(odOrderDetlIdList.get(i))
						.psShippingCompId(shippingCompIdList.get(i))
						.trackingNo(trackingNoList.get(i))
						.sort(1)
						.createId(userId)
						.build();
				trackingNumberBiz.delTrackingNumber(orderTrackingNumberVo);
				trackingNumberBiz.addTrackingNumber(orderTrackingNumberVo);

				// 주문상세번호에 따른 클레임 번호 조회
				ClaimNumberSearchVo claimNumberSearchVo = trackingNumberBiz.getOdClaimId(odOrderDetlIdList.get(i));
				// 취소요청 존재시 취소거부 처리
				long odClaimId = StringUtil.nvlLong(claimNumberSearchVo.getOdClaimId());
				long odClaimDetlId = StringUtil.nvlLong(claimNumberSearchVo.getOdClaimDetlId());
				if (odClaimId > 0 && odClaimDetlId > 0) {
					try {
						trackingNumberBiz.putCancelRequestClaimDenial(claimNumberSearchVo.getOdid(), odClaimId);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				OrderStatusUpdateDto orderStatusUpdateDto = new OrderStatusUpdateDto();
				orderStatusUpdateDto.setUserId(userId);
				orderStatusUpdateDto.setOdOrderDetlId(odOrderDetlIdList.get(i));
				orderStatusUpdateDto.setOrderStatusCd(orderStatusCd);
				orderStatusUpdateDto.setHistMsg(MessageFormat.format(orderDetailStatusHistMsg.getMessage(), loginName, trackingNoList.get(i)));
				orderStatusUpdateDto.setDiId(userId);
				orderStatusUpdateList.add(orderStatusUpdateDto);
			}

		} else {
			for(long odOrderDetlId : odOrderDetlIdList) {
				OrderStatusUpdateDto orderStatusUpdateDto = new OrderStatusUpdateDto();

				orderStatusUpdateDto.setOdOrderDetlId(odOrderDetlId);
				orderStatusUpdateDto.setOrderStatusCd(orderStatusCd);
				orderStatusUpdateDto.setHistMsg(orderDetailStatusHistMsg.getMessage());
				orderStatusUpdateDto.setUserId(userId);
				if(OrderEnums.OrderStatus.DELIVERY_READY.getCode().equals(orderStatusCd)){				// 배송준비중
					orderStatusUpdateDto.setDrId(userId);
				} else if(OrderEnums.OrderStatus.DELIVERY_COMPLETE.getCode().equals(orderStatusCd)) {	// 배송완료
					orderStatusUpdateDto.setDcId(userId);
				} else if(OrderEnums.OrderStatus.BUY_FINALIZED.getCode().equals(orderStatusCd)) {			// 구매확정
					orderStatusUpdateDto.setBfId(userId);
				}
				orderStatusUpdateList.add(orderStatusUpdateDto);

			}

		}



		/**
		 * 상태값 업데이트
		 */
		cnt = putOrderDetailStatus(orderStatusUpdateList);


		// 상품발송 자동메일
		if(OrderEnums.OrderStatus.DELIVERY_ING.getCode().equals(orderStatusCd)) {
			sendOrderGoodsDelivery(odOrderDetlIdList, false);
		}

		OrderStatusUpdateResponseDto orderStatusUpdateResponseDto = OrderStatusUpdateResponseDto.builder()
				.totalCount(totalCount)
				.successCount(successCount)
				.failCount(failCount)
				.build();

		return ApiResult.success(orderStatusUpdateResponseDto);
	}

	/**
	 * 주문상태 수정
	 * @param orderStatusUpdateList
	 * @return
	 * @throws Exception
	 */
    @Override
    public int putOrderDetailStatus(List<OrderStatusUpdateDto> orderStatusUpdateList) {
		int cnt = 0;
		// 주문상세상태일자 수정
		//orderDateBiz.putOrderDetailDt(orderDetlVo);
		for(OrderStatusUpdateDto orderStatusUpdateDto : orderStatusUpdateList) {
			OrderDetlHistVo orderDetlHistVo = OrderDetlHistVo.builder()
					.odOrderDetlId(orderStatusUpdateDto.getOdOrderDetlId())    // 주문상세PK
					.statusCd(orderStatusUpdateDto.getOrderStatusCd())        // 변경상태값
					.histMsg(orderStatusUpdateDto.getHistMsg())        // 처리이력내용
					.createId(orderStatusUpdateDto.getUserId())    // 등록자
					.build();
			// 주문상세상태 이력 등록 (테이블 : OD_ORDER_DETL_HIST)
			// 일일배송상품 스케쥴 변경 시 "원스케쥴정보" >> "변경스케쥴정보" 확인
			orderStatusService.putOrderDetailStatusHist(orderDetlHistVo);

			cnt += orderStatusService.putOrderDetailStatus(orderStatusUpdateDto);

		}

    	return cnt;
    }

	@Override
	public int putClaimDetailStatus(ClaimDetlVo claimDetlVo, long userId) {

        //UserVo userVo = SessionUtil.getBosUserVO();

        // 클레임상세상태일자 변경
		//orderDateBiz.putClaimDetailDt(claimDetlDtVo);

		ClaimDetlHistVo claimDetlHistVo = ClaimDetlHistVo.builder()
															.odClaimDetlId(claimDetlVo.getOdClaimDetlId())	// 클레임상세PK
															.statusCd(claimDetlVo.getClaimStatusCd())		// 변경상태값
															.histMsg(OrderEnums.OrderDetailStatusHistMsg.findByCode(claimDetlVo.getClaimStatusCd()).getMessage())		// 처리이력내용
															.createId(userId)	// 등록자
															.build();

		// 주문상세상태 이력 등록 (테이블 : OD_CLAIM_DETL_HIST)
		// 일일배송상품 스케쥴 변경 시 "원스케쥴정보" >> "변경스케쥴정보" 확인
		orderStatusService.putClaimDetailStatusHist(claimDetlHistVo);

		return orderStatusService.putClaimDetailStatus(claimDetlVo);
	}

	/**
	 * 상품발송 자동메일 발송
	 * @param odOrderDetlIdList
	 * @param isSend
	 */
	@Override
	public void sendOrderGoodsDelivery(List<Long> odOrderDetlIdList, boolean isSend) throws Exception{

		// odOrderDetlIdList로 주문PK, 송장정보 목록 조회
		List<OrderDetailGoodsDto> orderIdList = orderEmailBiz.getOrderIdList(odOrderDetlIdList);

		// 외부몰 주문건인 경우 BOS에서 상품발송시 자동메일 발송 X
		boolean isOutmallOrder = orderIdList.stream().anyMatch(a-> SystemEnums.AgentType.OUTMALL.getCode().equals(a.getAgentTypeCd()));
		if(isOutmallOrder){
			return;
		}

		// 일일상품 리스트
		List<OrderDetailGoodsDto> dailyGoodsList = orderIdList.stream()
													.filter(f-> GoodsEnums.GoodsType.DAILY.getCode().equals(f.getGoodsTpCd()))
													.collect(Collectors.toList());
		// 일일상품이외 상품 리스트(렌탈상품은 자동메일 발송x)
		List<OrderDetailGoodsDto> goodsList = orderIdList.stream()
													.filter(f-> !GoodsEnums.GoodsType.DAILY.getCode().equals(f.getGoodsTpCd()) && !OrderEnums.OrderStatusDetailType.RENTAL.getCode().equals(f.getOrderStatusDeliTp()))
													.collect(Collectors.toList());
		// 매장픽업 상품 리스트 (일반+매장 OR 매장전용 && 매장픽업)
		List<OrderDetailGoodsDto> shopPickupGoodsList = orderIdList.stream()
													.filter(f-> (GoodsEnums.GoodsType.NORMAL.getCode().equals(f.getGoodsTpCd()) || GoodsEnums.GoodsType.SHOP_ONLY.getCode().equals(f.getGoodsTpCd()))
															&& OrderEnums.OrderStatusDetailType.SHOP_PICKUP.getCode().equals(f.getOrderStatusDeliTp()))
													.collect(Collectors.toList());

		// 1. 일일상품 이외 상품리스트
		if(CollectionUtils.isNotEmpty(goodsList)) {

			// odOrderId, 송장번호 같은 상품끼리 자동메일 발송
			Map<Long,Map<String,List<OrderDetailGoodsDto>>> resultMap = goodsList.stream()
									.collect(groupingBy(OrderDetailGoodsDto::getOdOrderId, LinkedHashMap::new,
											groupingBy(OrderDetailGoodsDto::getTrackingNo, LinkedHashMap::new, toList())));

			// odOrderId 기준
			resultMap.entrySet().forEach(entry -> {
				Map<String,List<OrderDetailGoodsDto>> trackingNoList = entry.getValue();

				// 송장번호 기준
				trackingNoList.entrySet().forEach(trackigNo ->{
					List<Long> orderDetailGoodsList = trackigNo.getValue().stream()
																		.map(m -> m.getOdOrderDetlId())
																		.collect(Collectors.toList());

					// 상품발송 자동메일
					OrderInfoForEmailResultDto orderInfoForEmailResultDto;
					try {
						if(CollectionUtils.isNotEmpty(shopPickupGoodsList)) { //매장픽업 상품 준비 자동메일
							orderInfoForEmailResultDto = orderEmailBiz.getOrderShopPickupGoodsDeliveryInfoForEmail(orderDetailGoodsList);
							orderEmailSendBiz.orderShopPickupGoodsDelivery(orderInfoForEmailResultDto);
						} else {
							orderInfoForEmailResultDto = orderEmailBiz.getOrderGoodsDeliveryInfoForEmail(orderDetailGoodsList);
							orderEmailSendBiz.orderGoodsDelivery(orderInfoForEmailResultDto);
						}

					} catch (Exception e) {
						log.error("ERROR ====== 상품발송 자동메일 발송 오류 odOrderId ::{}" , entry.getKey());
						log.error(e.getMessage());
					}

				});

			});
		}

		// 2. 일일상품 리스트
		// 일일상품 -> 베이비밀 일괄배송만 자동메일발송(HGRM-9715)
		if(CollectionUtils.isNotEmpty(dailyGoodsList)) {

			List<OrderDetailGoodsDto> babymealBulkGoodsList = dailyGoodsList.stream().filter(f-> "Y".equals(f.getDailyBulkYn())).collect(Collectors.toList());

			if(CollectionUtils.isNotEmpty(babymealBulkGoodsList)){
				for(OrderDetailGoodsDto dailyGoodsDto : babymealBulkGoodsList) {

					List<Long> orderDetailGoodsList = new ArrayList<>();
					orderDetailGoodsList.add(dailyGoodsDto.getOdOrderDetlId());

					// 상품발송 자동메일
					OrderInfoForEmailResultDto orderInfoForEmailResultDto;
					try {
						orderInfoForEmailResultDto = orderEmailBiz.getOrderGoodsDeliveryInfoForEmail(orderDetailGoodsList);
						orderEmailSendBiz.orderGoodsDelivery(orderInfoForEmailResultDto);
					} catch (Exception e) {
						log.error("ERROR ====== 상품발송 자동메일 발송 오류 odOrderId ::" + orderDetailGoodsList);
						log.error(e.getMessage());
					}

				}
			}
		}

	}

	/**
	 * 배송중 업데이트를 위한 주문상세 조회
	 * @param odOrderId
	 * @return
	 */
	/*@Override
	public List<String> selectOrderDetailDcList(long odOrderId) {
		return orderStatusService.selectOrderDetailDcList(odOrderId);
	}*/

	/**
	 * 취소요청 -> 취소완료 일괄 변경
	 * @param orderClaimStatusRequestDto
	 * @return
	 */
	@Override
	public OrderStatusUpdateResponseDto putClaimCancelReqeustToCancelComplete(OrderClaimStatusRequestDto orderClaimStatusRequestDto) throws Exception {
		return orderStatusService.putClaimCancelReqeustToCancelComplete(orderClaimStatusRequestDto);
	}
}