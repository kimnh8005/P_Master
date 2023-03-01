package kr.co.pulmuone.v1.order.order.service;


import kr.co.pulmuone.v1.comm.aop.service.UserMaskingRun;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.*;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.util.MaskingUtil;
import kr.co.pulmuone.v1.goods.goods.dto.vo.ShippingDataResultVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.ShippingInfoByOdOrderIdResultVo;
import kr.co.pulmuone.v1.goods.goods.service.GoodsShippingTemplateBiz;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimAttcListDto;
import kr.co.pulmuone.v1.order.delivery.service.ShippingZoneBiz;
import kr.co.pulmuone.v1.order.order.dto.*;
import kr.co.pulmuone.v1.order.order.dto.mall.*;
import kr.co.pulmuone.v1.order.order.dto.vo.*;
import kr.co.pulmuone.v1.order.present.service.OrderPresentBiz;
import kr.co.pulmuone.v1.order.schedule.dto.OrderDetailScheduleListDto;
import kr.co.pulmuone.v1.order.schedule.dto.OrderDetailScheduleListRequestDto;
import kr.co.pulmuone.v1.order.schedule.service.OrderScheduleBiz;
import kr.co.pulmuone.v1.order.schedule.service.mall.MallOrderScheduleBiz;
import kr.co.pulmuone.v1.store.delivery.dto.ShippingAddressPossibleDeliveryInfoDto;
import kr.co.pulmuone.v1.store.delivery.dto.ShippingPossibilityStoreDeliveryAreaDto;
import kr.co.pulmuone.v1.store.delivery.service.StoreDeliveryBiz;
import kr.co.pulmuone.v1.user.buyer.dto.CommonSaveShippingAddressRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 상세 관련 Implements
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 28.            이명수         최초작성
 * =======================================================================
 * </PRE>
 */
@Slf4j
@Service
public class OrderDetailBizImpl implements OrderDetailBiz {
    @Autowired
    private OrderDetailService orderDetailService;

	@Autowired
	private MallOrderDetailService mallOrderDetailService;

    @Autowired
    private StoreDeliveryBiz     storeDeliveryBiz;

	@Autowired
	private MallOrderDetailBiz mallOrderDetailBiz;

	@Autowired
	private GoodsShippingTemplateBiz goodsShippingTemplateBiz;

	@Autowired
	private OrderOrderBiz orderOrderBiz;

	@Autowired
	private MallOrderDailyDetailBiz mallOrderDailyDetailBiz;

	@Autowired
	private MallOrderScheduleBiz mallOrderScheduleBiz;

	@Autowired
	private OrderDetailBiz orderDetailBiz;

	@Autowired
	private OrderPresentBiz orderPresentBiz;

	@Autowired
	private ShippingZoneBiz shippingZoneBiz;

	@Autowired
	OrderScheduleBiz orderScheduleBiz;


	/**
     * 주문상세 상품 리스트 조회
     * @param odOrderId
     * @return
     */
    @Override
    public ApiResult<?> getOrderDetailGoodsList(String odOrderId) {
        OrderDetailGoodsListResponseDto orderDetailGoodsListResponseDto = OrderDetailGoodsListResponseDto.builder()
                .rows(orderDetailService.getOrderDetailGoodsList(odOrderId))
                .build();

        return ApiResult.success(orderDetailGoodsListResponseDto);
    }

    /**
     * 주문상세 클레임 상품 리스트 조회
     * @param odOrderId
     * @return
     */
    @Override
	@UserMaskingRun(system = "BOS")
    public ApiResult<?> getOrderDetailClaimGoodsList(String odOrderId) {
        OrderDetailGoodsListResponseDto orderDetailGoodsListResponseDto = OrderDetailGoodsListResponseDto.builder()
                .rows(orderDetailService.getOrderDetailClaimGoodsList(odOrderId))
                .build();

        return ApiResult.success(orderDetailGoodsListResponseDto);
    }

    /**
     * @Desc 주문 상세 클레임 상품정보 조회 > 첨부파일보기 조회
     * @param odClaimId
     * @return
     */
    @Override
	public ApiResult<?> getOrderClaimAttc(long odClaimId) {
    	OrderClaimAttcListDto orderClaimAttcListDto = OrderClaimAttcListDto.builder()
    			.rows(orderDetailService.getOrderClaimAttc(odClaimId))
    			.build();

		return ApiResult.success(orderClaimAttcListDto);
	}

    /**
     * 주문상세 결제 리스트 조회
     * @param odOrderId
     * @return
     */
    @Override
    public ApiResult<?> getOrderDetailPayList(String odOrderId) {

    	log.debug("-------------------------------------------------- IN getOrderDetailPayList");


		// 결제 정보
		List<OrderDetailPayDetlListDto> payDetailList =  orderDetailService.getOrderDetailPayDetlList(odOrderId);

		for(OrderDetailPayDetlListDto resultDto : payDetailList) {
			MallOrderDetailPayResultDto payDto = new MallOrderDetailPayResultDto();

			payDto.setPgService(resultDto.getPgServiceCd());
			payDto.setPayType(resultDto.getPayTypeCd());
			payDto.setTid(resultDto.getTid());
			payDto.setCashReceiptNo(resultDto.getCashReceiptNo());
			payDto.setOdid(resultDto.getOdid());
			payDto.setPaymentPrice(Integer.parseInt(resultDto.getPaymentPrice()));
			payDto.setCashReceiptAuthNo(resultDto.getCashReceiptAuthNo());

			resultDto.setSalesSlipUrl(mallOrderDetailService.getOrderBillUrl(payDto));

		}

		//환불정보(추가결제, 추가환불 포함)
		List<OrderDetailRefundListDto> refundList =  orderDetailService.getOrderDetailRefundList(odOrderId);

		for(OrderDetailRefundListDto resultDto : refundList) {
			MallOrderDetailPayResultDto payDto = new MallOrderDetailPayResultDto();

			payDto.setPgService(resultDto.getPgServiceCd());
			payDto.setPayType(resultDto.getPayTypeCd());
			payDto.setTid(resultDto.getTid());
			payDto.setCashReceiptNo(resultDto.getCashReceiptNo());
			payDto.setOdid(resultDto.getOdid());
			payDto.setPaymentPrice(resultDto.getPaymentPrice());
			payDto.setCashReceiptAuthNo(resultDto.getCashReceiptAuthNo());

			// 이름 마스킹처리
			resultDto.setAccountHolder(MaskingUtil.name(resultDto.getAccountHolder()));
			// 계좌번호 마스킹처리
			resultDto.setAccountNumber(MaskingUtil.accountNumber(resultDto.getAccountNumber()));

			resultDto.setSalesSlipUrl(mallOrderDetailService.getOrderBillUrl(payDto));

		}

		OrderDetailPayListResponseDto orderDetailPayListResponseDto = OrderDetailPayListResponseDto.builder()
				.payDetailList(payDetailList)
				.payList(orderDetailService.getOrderDetailPayList(odOrderId)) /*추후삭제예정*/
				.refundList(refundList)
				.build();

        return ApiResult.success(orderDetailPayListResponseDto);
    }

    /**
     * 주문상세 쿠폰할인 리스트 조회
     * @param odOrderId
     * @return
     */
    @Override
    public ApiResult<?> getOrderDetailDiscountList(String odOrderId) {
        OrderDetailDiscountListResponseDto orderDetailDiscountListResponseDto = OrderDetailDiscountListResponseDto.builder()
                .rows(orderDetailService.getOrderDetailDiscountList(odOrderId))
                .build();

        return ApiResult.success(orderDetailDiscountListResponseDto);
    }

	/**
	 * 주문 상세 클레임 회수 정보 조회
	 * @param odOrderId
	 * @return
	 */
	@Override
	public ApiResult<?> getOrderDetailClaimCollectionList(String odOrderId) {
		OrderDetailClaimCollectionListResponseDto orderDetailClaimCollectionListResponseDto = OrderDetailClaimCollectionListResponseDto.builder()
				.rows(orderDetailService.getOrderDetailClaimCollectionList(odOrderId))
				.build();

		return ApiResult.success(orderDetailClaimCollectionListResponseDto);
	}

    /**
     * 주문상세 상담 리스트 조회
     * @param odOrderId
     * @return
     */
    @Override
    public ApiResult<?> getOrderDetailConsultList(String odOrderId) {
    	OrderDetailConsultListResponseDto orderDetailConsultListResponseDto = OrderDetailConsultListResponseDto.builder()
                .rows(orderDetailService.getOrderDetailConsultList(odOrderId, Constants.BATCH_CREATE_USER_ID))
                .build();

        return ApiResult.success(orderDetailConsultListResponseDto);
    }

    /**
     * 주문 상세 상담 등록
     * @param orderDetailConsultRequestDto
     * @return
     */
    @Override
    public ApiResult<?> addOrderDetailConsult(OrderDetailConsultRequestDto orderDetailConsultRequestDto) {
    	int result = orderDetailService.addOrderDetailConsult(orderDetailConsultRequestDto);
		if(result > 0) return ApiResult.success();
		else return ApiResult.fail();
    }

    /**
     * 주문 상세 상담 수정
     * @param orderDetailConsultRequestDto
     * @return
     */
    @Override
    public ApiResult<?> putOrderDetailConsult(OrderDetailConsultRequestDto orderDetailConsultRequestDto) {
    	int result = orderDetailService.putOrderDetailConsult(orderDetailConsultRequestDto);
		if(result > 0) return ApiResult.success();
		else return ApiResult.fail();
    }

    /**
     * 주문 상세 상담 삭제
     * @param odConsultId
     * @return
     */
    @Override
    public ApiResult<?> delOrderDetailConsult(String odConsultId) {
    	int result = orderDetailService.delOrderDetailConsult(odConsultId);
		if(result > 0) return ApiResult.success();
		else return ApiResult.fail();
    }

    /**
     * 주문상세 처리이력 리스트 조회
     * @param odOrderId
     * @return
     */
    @Override
	@UserMaskingRun(system = "BOS")
    public ApiResult<?> getOrderDetailHistoryList(String odOrderId) {
		OrderDetailHistoryRequestDto orderDetailHistoryRequestDto = OrderDetailHistoryRequestDto.builder()
																								.odOrderId(odOrderId)
																								.batchId(Constants.BATCH_CREATE_USER_ID)
																								.guestId(Constants.GUEST_CREATE_USER_ID)
																								.virtualId(Constants.VIRTUAL_ACCOUNT_USER_ID)
																								.build();
        OrderDetailHistoryListResponseDto orderDetailHistoryListResponseDto = OrderDetailHistoryListResponseDto.builder()
                .rows(orderDetailService.getOrderDetailHistoryList(orderDetailHistoryRequestDto))
                .build();
        return ApiResult.success(orderDetailHistoryListResponseDto);
    }



	/** 주문상세 수취정보 변경
	 * @param orderDetailShippingZoneRequestDto
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {BaseException.class,Exception.class})
	public ApiResult<?> putOrderDetailShippingZone(OrderDetailShippingZoneRequestDto orderDetailShippingZoneRequestDto) throws Exception {
		boolean isChangeDawnToNormal = false;

		boolean isHitokSwitch = orderDetailService.isHitokSwitch();

		//배송지 변경 가능 여부 체크
		CommonSaveShippingAddressRequestDto commonSaveShippingAddressRequestDto = new CommonSaveShippingAddressRequestDto();
		commonSaveShippingAddressRequestDto.setOdOrderId(orderDetailShippingZoneRequestDto.getOdOrderId());
		commonSaveShippingAddressRequestDto.setOdShippingZoneId(orderDetailShippingZoneRequestDto.getOdShippingZoneId());
		commonSaveShippingAddressRequestDto.setDeliveryType(orderDetailShippingZoneRequestDto.getDeliveryType());
		commonSaveShippingAddressRequestDto.setReceiverZipCode(orderDetailShippingZoneRequestDto.getRecvZipCd());
		commonSaveShippingAddressRequestDto.setBuildingCode(orderDetailShippingZoneRequestDto.getRecvBldNo());

		ApiResult result = this.isPossibleChangeDeliveryAddress(commonSaveShippingAddressRequestDto);

		if(UserEnums.ChangeDeliveryAddress.CHANGE_DAWN_TO_NORMAL.getCode().equals(result.getCode())){
			// 택배배송지로 변경
			isChangeDawnToNormal = true;
		}else if(!BaseEnums.Default.SUCCESS.getCode().equals(result.getCode())){
			// 배송지 변경 불가능
			return result;
		}


		// 1. 하이톡 스위치여부가 false이고 녹즙인 경우 -> 배송지 새로 등록 & 일일상품 스케쥴 테이블의 배송지PK 변경 & ERP 스케쥴 입력
		if(!isHitokSwitch && GoodsEnums.GoodsDailyType.GREENJUICE.getCode().equals(orderDetailShippingZoneRequestDto.getGoodsDailyType())){

			OrderDetailScheduleListRequestDto orderDetailScheduleListRequestDto = new OrderDetailScheduleListRequestDto();

			// 배송적용일자 존재하지 않을 경우 오늘 날짜로 Set
			if(StringUtils.isEmpty(orderDetailShippingZoneRequestDto.getDeliveryDt())) {
				orderDetailShippingZoneRequestDto.setDeliveryDt(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
			}

			// 1. 주문 상품별 도착일 정보 리스트 조회
			MallOrderDetailGoodsDto goodsDto = new MallOrderDetailGoodsDto();
			goodsDto.setPromotionTp(orderDetailShippingZoneRequestDto.getPromotionTp());
			goodsDto.setOdOrderId(orderDetailShippingZoneRequestDto.getOdOrderId());
			goodsDto.setGoodsDailyTp(orderDetailShippingZoneRequestDto.getGoodsDailyType());

			List<OrderDetailScheduleListDto> orderDetailScheduleArrivalDateList = mallOrderDailyDetailBiz.getOrderDetailScheduleArrivalDateList(orderDetailShippingZoneRequestDto.getDeliveryDt(), goodsDto);
			if(CollectionUtils.isEmpty(orderDetailScheduleArrivalDateList)){
				return ApiResult.result(BaseEnums.Default.FAIL);
			}

			//2. OD_SHIPPING_ZONE -> 새로운 주소지 등록
			long odShippingZoneId = mallOrderDailyDetailBiz.getOrderShippingZoneSeq();

			OrderShippingZoneVo orderShippingZoneVo = OrderShippingZoneVo.builder()
					.odOrderId(orderDetailShippingZoneRequestDto.getOdOrderId())
					.deliveryType(ShoppingEnums.DeliveryType.DAILY.getCode())
					.shippingType(1)
					.odShippingZoneId(odShippingZoneId)
					.recvNm(orderDetailShippingZoneRequestDto.getRecvNm())
					.recvHp(orderDetailShippingZoneRequestDto.getRecvHp())
					.recvZipCd(orderDetailShippingZoneRequestDto.getRecvZipCd())
					.recvAddr1(orderDetailShippingZoneRequestDto.getRecvAddr1())
					.recvAddr2(orderDetailShippingZoneRequestDto.getRecvAddr2())
					.recvBldNo(orderDetailShippingZoneRequestDto.getRecvBldNo())
					.deliveryMsg(orderDetailShippingZoneRequestDto.getDeliveryMsg())
					.doorMsgCd(orderDetailShippingZoneRequestDto.getDoorMsgCd())
					.doorMsg(orderDetailShippingZoneRequestDto.getDoorMsg())
					.build();
			// 권역정보가 존재할 경우 권역정보 Set
			if(!StringUtils.isEmpty(commonSaveShippingAddressRequestDto.getUrStoreId())) {
				orderShippingZoneVo.setUrStoreId(Long.parseLong(commonSaveShippingAddressRequestDto.getUrStoreId()));
			}
			orderDetailBiz.addShippingZone(orderShippingZoneVo);

			// 주문상세PK로 일일상품 정보 조회
			MallOrderDetailGoodsDto orderDetailGoodsDto =  mallOrderDailyDetailBiz.getOrderDailyDetailByOdOrderDetlId(orderDetailShippingZoneRequestDto.getOdOrderDetlId());

			// 3. OD_ORDER_DETL_DAILY_SCH 스케쥴 수정, ERP 스케줄 입력
			if(ShoppingEnums.CartPromotionType.GREENJUICE_SELECT.getCode().equals(orderDetailShippingZoneRequestDto.getPromotionTp())){
				orderDetailGoodsDto.setDeliveryDt(orderDetailShippingZoneRequestDto.getDeliveryDt());
				ApiResult<?> apiResult = mallOrderDailyDetailBiz.putOrderDailyGreenjuiceShippingZone(orderDetailGoodsDto,orderDetailScheduleArrivalDateList, odShippingZoneId, orderDetailShippingZoneRequestDto.getDeliveryDt());
				if(!apiResult.getCode().equals(BaseEnums.Default.SUCCESS.getCode())){
					return apiResult;
				}
			}else{

				List<MallOrderDetailGoodsDto>  orderDetailGoodsList = mallOrderDailyDetailBiz.getOrderDailyDetailByOdOrderId(orderDetailGoodsDto.getOdOrderId(), orderDetailGoodsDto.getGoodsDailyTp());

				// 녹즙인 경우 - > 같은 주문건에서 녹즙주문건 모두 변경
				for(MallOrderDetailGoodsDto mallOrderDetailGoodsDto : orderDetailGoodsList){

					orderDetailScheduleListRequestDto.setOdOrderDetlId(mallOrderDetailGoodsDto.getOdOrderDetlId());
					orderDetailScheduleListRequestDto.setChangeDate(orderDetailShippingZoneRequestDto.getDeliveryDt());
					List<OrderDetailScheduleListDto>  orderDetailList = mallOrderScheduleBiz.getOrderDetailScheduleList(orderDetailScheduleListRequestDto);
					mallOrderDetailGoodsDto.setDeliveryDt(orderDetailShippingZoneRequestDto.getDeliveryDt());
					ApiResult<?> apiResult = mallOrderDailyDetailBiz.putOrderDailyGreenjuiceShippingZone(mallOrderDetailGoodsDto, orderDetailList, odShippingZoneId, orderDetailShippingZoneRequestDto.getDeliveryDt());
					if(!apiResult.getCode().equals(BaseEnums.Default.SUCCESS.getCode())){
						return apiResult;
					}
				}
			}

			// 주문 배송 정보 이력 등록
			OrderShippingZoneHistVo orderShippingZoneHistVo = orderDetailService.setAddShippingZoneHistRequestParam(odShippingZoneId, orderDetailShippingZoneRequestDto);
			orderDetailService.addShippingZoneHist(orderShippingZoneHistVo);

		}else if(GoodsEnums.GoodsDailyType.BABYMEAL.getCode().equals(orderDetailShippingZoneRequestDto.getGoodsDailyType())
				||GoodsEnums.GoodsDailyType.EATSSLIM.getCode().equals(orderDetailShippingZoneRequestDto.getGoodsDailyType())
				||(isHitokSwitch && GoodsEnums.GoodsDailyType.GREENJUICE.getCode().equals(orderDetailShippingZoneRequestDto.getGoodsDailyType()))
		){
			// 2. 일일상품(베이비밀,잇슬림인 경우) 또는 하이톡 스위치여부가 true이고 녹즙인경우

			// 같은 주문건에서 동일 브랜드 주소 모두 변경(베이비밀 일괄배송 제외)
			List<MallOrderDetailGoodsDto> orderDetailGoodsList = mallOrderDailyDetailBiz.getOrderDailyDetailByOdOrderId(orderDetailShippingZoneRequestDto.getOdOrderId(), orderDetailShippingZoneRequestDto.getGoodsDailyType());
			for(MallOrderDetailGoodsDto dto : orderDetailGoodsList){
				// 주문배송지 업데이트
				OrderShippingZoneVo orderShippingZoneVo = orderDetailService.setPutShippingZoneRequestParam(dto.getOdShippingZoneId(), orderDetailShippingZoneRequestDto);
				// 권역정보가 존재할 경우 권역정보 Set
				if(!StringUtils.isEmpty(commonSaveShippingAddressRequestDto.getUrStoreId())) {
					orderShippingZoneVo.setUrStoreId(Long.parseLong(commonSaveShippingAddressRequestDto.getUrStoreId()));
				}
				orderDetailService.putShippingZone(orderShippingZoneVo);

				// OD_ORDER_DETL_DAILY 테이블의 주문상세번호로 스토어PK 업데이트 처리
				orderScheduleBiz.putOrderDetlDailyUrStoreId(dto.getOdOrderId(), 0, dto.getOdShippingZoneId());

				// 주문 배송 정보 이력 등록
				OrderShippingZoneHistVo orderShippingZoneHistVo = orderDetailService.setAddShippingZoneHistRequestParam(dto.getOdShippingZoneId(), orderDetailShippingZoneRequestDto);
				orderDetailService.addShippingZoneHist(orderShippingZoneHistVo);
			}

		}else{
			// 3. 일반상품 주문배송지 업데이트
			// 주문배송지 업데이트
			OrderShippingZoneVo orderShippingZoneVo = orderDetailService.setPutShippingZoneRequestParam(orderDetailShippingZoneRequestDto.getOdShippingZoneId(), orderDetailShippingZoneRequestDto);
			orderDetailService.putShippingZone(orderShippingZoneVo);

			// 주문 배송 정보 이력 등록
			OrderShippingZoneHistVo orderShippingZoneHistVo = orderDetailService.setAddShippingZoneHistRequestParam(orderDetailShippingZoneRequestDto.getOdShippingZoneId(), orderDetailShippingZoneRequestDto);
			orderDetailService.addShippingZoneHist(orderShippingZoneHistVo);
		}

		// 새벽배송 -> 택배배송 변경인 경우
		if(isChangeDawnToNormal) {

			// 주문건의 배송유형 새벽-> 택배로 변경
			String goodsDeliveryType = GoodsEnums.GoodsDeliveryType.NORMAL.getCode();
			String orderStatusDetailType = OrderEnums.OrderStatusDetailType.NORMAL.getCode();

			orderOrderBiz.putOrderDetailGoodsDeliveryType(orderDetailShippingZoneRequestDto.getOdShippingZoneId(),goodsDeliveryType, orderStatusDetailType);
			return ApiResult.success(UserEnums.ChangeDeliveryAddress.CHANGE_DAWN_TO_NORMAL);
		}

		return ApiResult.success(UserEnums.ChangeDeliveryAddress.SUCCESS_CHANGE_DELIVERY_ADDRESS);
	}
	/**
	 * @Desc 주문상세 주문자정보 리스트 조회
	 * @param odid
	 */
	@Override
	@UserMaskingRun(system = "BOS")
	public ApiResult<?> getOrderBuyer(String odid) {

		OrderBuyerResponseDto orderBuyerResponseDto = OrderBuyerResponseDto.builder()
			.rows(orderDetailService.getOrderBuyer(odid))
			.build();

		return ApiResult.success(orderBuyerResponseDto);
	}

	/**
	 * @Desc 주문상세 배송정보 리스트 조회
	 * @param odOrderId
	 */
	@Override
	@UserMaskingRun(system = "BOS")
	public ApiResult<?> getOrderDetailShippingZoneList(String odOrderId) {
		OrderDetailShippingZoneListResponseDto orderDetailShippingZoneListResponseDto = OrderDetailShippingZoneListResponseDto.builder()
				.rows(orderDetailService.getOrderDetailShippingZoneList(odOrderId))
				.build();
		return ApiResult.success(orderDetailShippingZoneListResponseDto);
	}

	@Override
	@UserMaskingRun(system = "BOS")
	public ApiResult<?> getOrderDetailShippingZoneList(String odOrderId, String odShippingZoneId) {
		List<OrderDetlShippingZoneVo> responseOrderDetlShippingZoneList = new ArrayList<>();

		boolean isHitokSwitch = orderDetailService.isHitokSwitch();

		// 1. 주문배송지 리스트 조회
		List<OrderDetlShippingZoneVo> orderDetlShippingZoneList = orderDetailService.getOrderDetailShippingZoneList(odOrderId,odShippingZoneId);


		// 일일상품 제외 배송지 리스트(해당주문 : 일일상품x, 일일상품 베이비밀 일괄배송)
		List<OrderDetlShippingZoneVo> normalOrderShippingZoneList = orderDetlShippingZoneList.stream()
				.filter(f-> (!f.getDeliveryTypeCode().equals(ShoppingEnums.DeliveryType.DAILY.getCode()) ||
						(f.getDeliveryTypeCode().equals(ShoppingEnums.DeliveryType.DAILY.getCode()) && "Y".equals(f.getDailyBulkYn()))))
				.collect(Collectors.toList());
		if(CollectionUtils.isNotEmpty(normalOrderShippingZoneList)){
			responseOrderDetlShippingZoneList.addAll(normalOrderShippingZoneList);
		}

		// 일일상품 배송지 리스트(일일상품은 동일브랜드별로 주소 그룹핑, 일일상품 베이비밀 일괄배송 제외)
		List<OrderDetlShippingZoneVo> dailyOrderShippingZoneList = orderDetlShippingZoneList.stream()
				.filter(f-> (f.getDeliveryTypeCode().equals(ShoppingEnums.DeliveryType.DAILY.getCode())
						&& "N".equals(f.getDailyBulkYn())))
				.collect(Collectors.toList());

		if(CollectionUtils.isNotEmpty(dailyOrderShippingZoneList)) {

			// 베이비밀
			List<OrderDetlShippingZoneVo> babymealOrderShippingZoneList = dailyOrderShippingZoneList.stream()
					.filter(f -> f.getGoodsDailyType().equals(GoodsEnums.GoodsDailyType.BABYMEAL.getCode()))
					.collect(Collectors.toList());

			if (CollectionUtils.isNotEmpty(babymealOrderShippingZoneList)) {
				OrderDetlShippingZoneVo babymealShippingZoneVo = babymealOrderShippingZoneList.get(0);
				String goodsNameInfo = babymealShippingZoneVo.getGoodsNmInfo();
				if (babymealOrderShippingZoneList.size() > 1) {
					goodsNameInfo += " 외 " + (babymealOrderShippingZoneList.size() - 1) + "건";
				}
				babymealShippingZoneVo.setGoodsNmInfo(goodsNameInfo);
				responseOrderDetlShippingZoneList.add(babymealShippingZoneVo);
			}

			// 잇슬림
			List<OrderDetlShippingZoneVo> eatsslimOrderShippingZoneList = dailyOrderShippingZoneList.stream()
					.filter(f -> f.getGoodsDailyType().equals(GoodsEnums.GoodsDailyType.EATSSLIM.getCode()))
					.collect(Collectors.toList());

			if (CollectionUtils.isNotEmpty(eatsslimOrderShippingZoneList)) {
				OrderDetlShippingZoneVo eatsslimShippingZoneVo = eatsslimOrderShippingZoneList.get(0);
				String goodsNameInfo = eatsslimShippingZoneVo.getGoodsNmInfo();
				if (eatsslimOrderShippingZoneList.size() > 1) {
					goodsNameInfo += " 외 " + (eatsslimOrderShippingZoneList.size() - 1) + "건";
				}
				eatsslimShippingZoneVo.setGoodsNmInfo(goodsNameInfo);
				responseOrderDetlShippingZoneList.add(eatsslimShippingZoneVo);
			}

			// 녹즙
			List<OrderDetlShippingZoneVo> greenjuiceOrderShippingZoneList = dailyOrderShippingZoneList.stream()
					.filter(f -> f.getGoodsDailyType().equals(GoodsEnums.GoodsDailyType.GREENJUICE.getCode()))
					.collect(Collectors.toList());

			if (!isHitokSwitch && CollectionUtils.isNotEmpty(greenjuiceOrderShippingZoneList)) {
				OrderDetlShippingZoneVo greenjuiceShippingZoneVo = greenjuiceOrderShippingZoneList.get(0);
				OrderDetailScheduleListRequestDto orderDetailScheduleListRequestDto = new OrderDetailScheduleListRequestDto();

				// 녹즙-내맘대로일 경우  -> OD_ORDER_ID 세팅
				if (ShoppingEnums.CartPromotionType.GREENJUICE_SELECT.getCode().equals(greenjuiceOrderShippingZoneList.get(0).getPromotionTp())) {
					orderDetailScheduleListRequestDto.setOdOrderId(String.valueOf(greenjuiceShippingZoneVo.getOdOrderId()));
					orderDetailScheduleListRequestDto.setPromotionYn("Y");
				} else {
					// 녹즙일 경우 - > OD_ORDER_DETL_ID 세팅
					orderDetailScheduleListRequestDto.setOdOrderDetlId(greenjuiceShippingZoneVo.getOdOrderDetlId());
				}

				// 녹즙 - OD_ORDER_DETL_ID & 일일배송 스케줄별 주문배송지 리스트 조회
				List<MallOrderDailyShippingZoneHistListDto> orderDailyShippingZoneList = mallOrderDailyDetailBiz.getOrderDailyShippingZoneList(orderDetailScheduleListRequestDto);

				if (CollectionUtils.isNotEmpty(orderDailyShippingZoneList)) {
					OrderDetlShippingZoneVo nowOrderDailyShippingZoneVo = new OrderDetlShippingZoneVo();

					// 현재 적용중인 배송지이거나 가장 빠른 적용일 주소지 1건만 리스트에 노출
					boolean isNowDailyShippingZone = orderDailyShippingZoneList.stream().anyMatch(f -> f.getNowYn().equals("Y"));

					// 현재 적용중인 배송지가 있는 경우
					if (isNowDailyShippingZone) {
						for (MallOrderDailyShippingZoneHistListDto dto : orderDailyShippingZoneList) {
							if ("Y".equals(dto.getNowYn())) {
								nowOrderDailyShippingZoneVo = orderDetailService.getOrderDetailDailySchShippingZone(dto.getOdShippingZoneId());
								nowOrderDailyShippingZoneVo.setDeliveryDt(dto.getApplyDtFrom());
							}
						}

						// 현재 적용중인 배송지가 없는 경우 -> 가장 빠른 적용일 주소지 1건만 추출
					} else {
						long nowOdShippingZoneId = orderDailyShippingZoneList.get(0).getOdShippingZoneId();
						nowOrderDailyShippingZoneVo = orderDetailService.getOrderDetailDailySchShippingZone(nowOdShippingZoneId);
						nowOrderDailyShippingZoneVo.setDeliveryDt(orderDailyShippingZoneList.get(0).getApplyDtFrom());
					}

					// 주문배송지 변경이력 count 수정
					long reqParamOdOrderDetlId = greenjuiceShippingZoneVo.getOdOrderDetlId();
					if (ShoppingEnums.CartPromotionType.GREENJUICE_SELECT.getCode().equals(nowOrderDailyShippingZoneVo.getPromotionTp())) {
						// 녹즙 내맘대로인 경우 OD_ORDER_DETL_PARENT_ID를 넘김
						reqParamOdOrderDetlId = nowOrderDailyShippingZoneVo.getOdOrderDetlParentId();
					} else {
						// 녹즙인 경우 -> 상품명 수정
						String goodsNameInfo = greenjuiceShippingZoneVo.getGoodsNmInfo();
						if (greenjuiceOrderShippingZoneList.size() > 1) {
							goodsNameInfo += " 외 " + (greenjuiceOrderShippingZoneList.size() - 1) + "건";
							nowOrderDailyShippingZoneVo.setGoodsNmInfo(goodsNameInfo);
						}
					}
					int recvChgHist = mallOrderDailyDetailBiz.getOrderDailyShippingZoneChangeCount(nowOrderDailyShippingZoneVo.getPromotionTp(), reqParamOdOrderDetlId);
					nowOrderDailyShippingZoneVo.setHistCnt(String.valueOf(recvChgHist));

					responseOrderDetlShippingZoneList.add(nowOrderDailyShippingZoneVo);
				}
			} else if(isHitokSwitch && CollectionUtils.isNotEmpty(greenjuiceOrderShippingZoneList)) {
				OrderDetlShippingZoneVo greenjuiceShippingZoneVo = greenjuiceOrderShippingZoneList.get(0);
				String goodsNameInfo = greenjuiceShippingZoneVo.getGoodsNmInfo();
				if (babymealOrderShippingZoneList.size() > 1) {
					goodsNameInfo += " 외 " + (babymealOrderShippingZoneList.size() - 1) + "건";
				}
				greenjuiceShippingZoneVo.setGoodsNmInfo(goodsNameInfo);
				responseOrderDetlShippingZoneList.add(greenjuiceShippingZoneVo);
			}
		}

		OrderDetailShippingZoneListResponseDto orderDetailShippingZoneListResponseDto = OrderDetailShippingZoneListResponseDto.builder()
				.rows(responseOrderDetlShippingZoneList)
				.build();

		return ApiResult.success(orderDetailShippingZoneListResponseDto);
	}

	/**
	 * @Desc 주문복사에서 배송정보 리스트 조회
	 * @param odOrderId
	 * @return
	 */
	public List<OrderDetlCopyShippingZoneDto> getOrderDetailCopyShippingZoneList(String odOrderId) {
		return orderDetailService.getOrderDetailCopyShippingZoneList(odOrderId);
	}

	/**
	 * @Desc 주문 상세 변경내역 조회 (변경 정보)
	 * @param odShippingZoneId
	 */
	@Override
	public ApiResult<?> getOrderShippingZoneHistList(String odShippingZoneId, String paramOdOrderDetlId) {
		long odOrderDetlId = Long.parseLong(paramOdOrderDetlId);
		List<OrderDetlShippingZoneHistVo> orderDetlShippingZoneHistList = new ArrayList<>();

		// 1. 녹즙일 경우 - 일일배송 스케줄별 주문배송지 리스트 조회
		MallOrderDetailGoodsDto orderDetailGoodsDto =  mallOrderDailyDetailBiz.getOrderDailyDetailByOdOrderDetlId(odOrderDetlId);

		if(GoodsEnums.GoodsDailyType.GREENJUICE.getCode().equals(orderDetailGoodsDto.getGoodsDailyTp())){
			OrderDetailScheduleListRequestDto orderDetailScheduleListRequestDto = new OrderDetailScheduleListRequestDto();

			if(GoodsEnums.GoodsType.PACKAGE.getCode().equals(orderDetailGoodsDto.getGoodsTpCd())
					&& ShoppingEnums.CartPromotionType.GREENJUICE_SELECT.getCode().equals(orderDetailGoodsDto.getPromotionTp())){
				// 1.2 - 녹즙-내맘대로 인 경우 -> OD_ORDER_ID 세팅
				orderDetailScheduleListRequestDto.setOdOrderId(String.valueOf(orderDetailGoodsDto.getOdOrderId()));
				orderDetailScheduleListRequestDto.setPromotionYn("Y");
			}else{
				// 1.3 - 녹즙인 경우 - > OD_ORDER_DETL_ID 세팅
				orderDetailScheduleListRequestDto.setOdOrderDetlId(odOrderDetlId);
			}

			List<MallOrderDailyShippingZoneHistListDto> orderDailyShippingZoneList = mallOrderDailyDetailBiz.getOrderDailyShippingZoneList(orderDetailScheduleListRequestDto);

			if(CollectionUtils.isNotEmpty(orderDailyShippingZoneList)){
				for(MallOrderDailyShippingZoneHistListDto dto : orderDailyShippingZoneList){
					OrderDetlShippingZoneHistVo orderDetlShippingZoneHistVo = orderDetailService.getOrderShippingZoneHistList(String.valueOf(dto.getOdShippingZoneId())).get(0);
					orderDetlShippingZoneHistVo.setDeliveryDt(dto.getApplyDtFrom());
					orderDetlShippingZoneHistList.add(orderDetlShippingZoneHistVo);
				}

			}

		}else{
			// 2. 녹즙 아닌경우 -> 주문배송지 변경내역 조회
			orderDetlShippingZoneHistList = orderDetailService.getOrderShippingZoneHistList(odShippingZoneId);
		}


		OrderDetailShippingZoneHistListResponseDto orderDetailShippingZoneHistListResponseDto = OrderDetailShippingZoneHistListResponseDto.builder()
				.rows(orderDetlShippingZoneHistList)
				.build();

		return ApiResult.success(orderDetailShippingZoneHistListResponseDto);
	}

	/**
	 * @Desc 주문 상세 변경내역 조회 (수취 정보)
	 * @param odShippingZoneId
	 * @return ApiResult<?>
	 */
	@Override
	@UserMaskingRun(system = "BOS")
	public ApiResult<?> getOrderShippingZoneByOdShippingZoneId(String odShippingZoneId) {
		OrderDetailShippingZoneListResponseDto orderDetailShippingZoneListResponseDto = OrderDetailShippingZoneListResponseDto.builder()
				.rows(orderDetailService.getOrderShippingZoneByOdShippingZoneId(odShippingZoneId))
				.build();
		return ApiResult.success(orderDetailShippingZoneListResponseDto);
	}


	/**
	 * @Desc 주문 상세 결제 정보 > 즉시할인내역 조회
	 * @param odOrderId
	 */
	@Override
	public ApiResult<?> getOrderDirectDiscountList(String odOrderId) {
		OrderDetailDirectDiscountListResponseDto orderDetailDirectDiscountListResponseDto = OrderDetailDirectDiscountListResponseDto.builder()
				.rows(orderDetailService.getOrderDirectDiscountList(odOrderId))
				.build();
		return ApiResult.success(orderDetailDirectDiscountListResponseDto);
	}

	/**
	 * @Desc 주문PK, 쿠폰발급PK로 배송비쿠폰재발급가능한지여부 확인
	 * @param odOrderId
	 * @param pmCouponIssueId
	 * @return int
	 */
	@Override
	public int isPossibilityReissueCouponInOdshippingPrice(Long odOrderId, Long pmCouponIssueId) {
		return orderDetailService.isPossibilityReissueCouponInOdshippingPrice(odOrderId, pmCouponIssueId);
	}

	/**
	 * @Desc 주문PK, 쿠폰발급PK로 쿠폰재발급가능한지여부 확인
	 * @param odOrderId
	 * @param pmCouponIssueId
	 * @return int
	 */
	@Override
	public int isPossibilityReissueCouponInOdOrderDetl(Long odOrderId, Long pmCouponIssueId) {
		return orderDetailService.isPossibilityReissueCouponInOdOrderDetl(odOrderId, pmCouponIssueId);
	}

	@Override
	public int isPossibilityReissueCartCouponInOdOrderDetl(Long odOrderId, Long pmCouponIssueId, List<Long> odClaimDetlIds) {
		return orderDetailService.isPossibilityReissueCartCouponInOdOrderDetl(odOrderId, pmCouponIssueId, odClaimDetlIds);
	}


    /**
     * @Desc 주문 배송지 수정
     * @param orderShippingZoneVo
     * @return int
     */
	@Override
    public int putShippingZone(OrderShippingZoneVo orderShippingZoneVo) {
        return orderDetailService.putShippingZone(orderShippingZoneVo);
    }

	/**
	 * @Desc 주문 배송지 등록
	 * @param orderShippingZoneVo
	 * @return int
	 */
	@Override
	public int addShippingZone(OrderShippingZoneVo orderShippingZoneVo) {
		return orderDetailService.addShippingZone(orderShippingZoneVo);
	}

    /**
     * @Desc 주문 배송지 이력 등록
     * @param orderShippingZoneHistVo
     * @return int
     */
	@Override
    public int addShippingZoneHist(OrderShippingZoneHistVo orderShippingZoneHistVo) {
        return orderDetailService.addShippingZoneHist(orderShippingZoneHistVo);
    }

	/**
	 * @Desc 주문 상세 결제 정보 > 증정품 내역 조회
	 * @param odOrderId
	 */
	@Override
	public ApiResult<?> getOrderGiftList(Long odOrderId) {
		OrderGiftListResponseDto orderGiftListResponseDto = OrderGiftListResponseDto.builder()
				.rows(orderDetailService.getOrderGiftList(odOrderId))
				.build();
		return ApiResult.success(orderGiftListResponseDto);
	}

	/**
	 * @Desc 배송지 변경 가능 여부 체크
	 * @return ApiResult<?>
	 */
	@Override
	public ApiResult<?> isPossibleChangeDeliveryAddress(CommonSaveShippingAddressRequestDto dto) throws Exception{

		// 배송지 변경 가능 여부 체크
		// 변경 배송지 배송 가능 타입정보 조회
		ShippingAddressPossibleDeliveryInfoDto shippingPossibleInfo
				= storeDeliveryBiz.getShippingAddressPossibleDeliveryInfo(dto.getReceiverZipCode(), dto.getBuildingCode());

		// 1.일일배송, 매장배송
		// TODO :: 매장배송 케이스 추가
		if(ShoppingEnums.DeliveryType.DAILY.getCode().equals(dto.getDeliveryType())) {

			// 주문배송지 PK 기준 상품PK,스토어PK별 정보 조회
			List<OrderDetailByOdShippingZondIdResultDto> orderDetailDtoList =  mallOrderDetailBiz.getOrderDetailInfoByOdShippingZoneId(dto.getOdShippingZoneId());

			for(OrderDetailByOdShippingZondIdResultDto resultDto : orderDetailDtoList) {

				// 변경 배송지의 스토어 배송권역정보 조회
				ShippingPossibilityStoreDeliveryAreaDto shippingPossibilityDto = storeDeliveryBiz.getShippingPossibilityStoreDeliveryAreaInfo(resultDto.getIlGoodsId(), dto.getReceiverZipCode(), dto.getBuildingCode());

				// 기존 주문 배송권역의 배송방식이 매일이고 변경 배송지 배송권역이 격일일 경우(매일권역 -> 격일권역으로 변경불가)
				if(shippingPossibilityDto == null || (StoreEnums.StoreDeliveryIntervalTpye.EVERY.getCode().equals(resultDto.getStoreDeliveryIntervalTp())
						&& StoreEnums.StoreDeliveryIntervalTpye.TWO_DAYS.getCode().equals(shippingPossibilityDto.getStoreDeliveryIntervalType()))) {
					// 배송지 변경 불가 -> 배송불가 상품 존재
					return ApiResult.result(UserEnums.ChangeDeliveryAddress.EXIST_DELIVERY_NOT_POSSIBLE_GOODS_BOS);
				}
				// 권역정보 Set
				dto.setUrStoreId(shippingPossibilityDto.getUrStoreId());
			}

			// 2.일반배송, 예약배송, 정기배송
		}else {

			// 2-1. 주문 PK기준 상품별 주문정보 조회
			List<ShippingInfoByOdOrderIdResultVo> orderInfoList = mallOrderDetailBiz.getOrderDetailInfoByOdOrderId(dto.getOdOrderId());
			for(ShippingInfoByOdOrderIdResultVo resultVo : orderInfoList) {

				// 상품 배송정책 PK별 배송정책 정보
				ShippingDataResultVo goodsShippingData = goodsShippingTemplateBiz.getShippingInfoByShippingTmplId(resultVo.getIlGoodsShippingTemplateId());

				//2-1-1. 배송정책의 배송불가지역 체크
				String undeliverableAreaTypeByShippingTemplate = goodsShippingData.getUndeliverableAreaType();
				boolean isUnDeliverableAreaByShippingTemplate = goodsShippingTemplateBiz.isUnDeliverableArea(undeliverableAreaTypeByShippingTemplate, dto.getReceiverZipCode());
				if(isUnDeliverableAreaByShippingTemplate) {
					// 배송지 변경 불가 -> 배송불가 상품 존재
					return ApiResult.result(UserEnums.ChangeDeliveryAddress.EXIST_DELIVERY_NOT_POSSIBLE_GOODS_BOS);
				}

				// 2-1-2. 상품의 배송불가지역 체크
				String undeliverableAreaTypeByItem = resultVo.getUndeliverableAreaTp();
				boolean isUnDeliverableAreaByItem = goodsShippingTemplateBiz.isUnDeliverableArea(undeliverableAreaTypeByItem, dto.getReceiverZipCode());
				if(isUnDeliverableAreaByItem) {
					// 배송지 변경 불가 -> 배송불가 상품 존재
					return ApiResult.result(UserEnums.ChangeDeliveryAddress.EXIST_DELIVERY_NOT_POSSIBLE_GOODS_BOS);
				}

				// 2-1-3. 새벽배송이고 변경 배송지가 택배배송만 가능한 경우
				if(GoodsEnums.GoodsDeliveryType.DAWN.getCode().equals(resultVo.getGoodsDeliveryType())
						&& !shippingPossibleInfo.isDawnDelivery() && shippingPossibleInfo.isShippingCompDelivery()) {

					// 택배배송지로 변경
					return ApiResult.result(UserEnums.ChangeDeliveryAddress.CHANGE_DAWN_TO_NORMAL);
				}
			}

		}

		return ApiResult.success();
	}

	/**
	 * @Desc 주문상세 PK로 일일배송 스케쥴정보 조회
	 * @param odOrderDetlId
	 * @return OrderDetlDailyVo
	 */
	@Override
	public OrderDetlDailyVo getOrderDetlDailySchByOdOrderDetlId(Long odOrderDetlId){
		return orderDetailService.getOrderDetlDailySchByOdOrderDetlId(odOrderDetlId);
	}

	/**
	 * @Desc 주문상세 PK로 일일배송 스케쥴정보 조회
	 * @param odOrderId
	 * @return OrderShopStoreVo
	 */
	@Override
	public ApiResult<?> getOrderShopStoreInfo(String odOrderId) throws Exception {

		OrderShopStoreResponseDto orderShopStoreResponseDto = OrderShopStoreResponseDto.builder()
				.rows(orderDetailService.getOrderShopStoreInfo(odOrderId))
				.build();

		return  ApiResult.success(orderShopStoreResponseDto);
	}

	/**
	 * 하이톡 <--> FD-PHI 스위치 설정값 조회(하이톡 스위치)
	 */
	@Override
	public ApiResult<?> getHitokSwitch() {
		return ApiResult.success(orderDetailService.isHitokSwitch());
	}


	public int getOrderEmployeeDiscountCnt(String odOrderid){
		return orderDetailService.getOrderEmployeeDiscountCnt(odOrderid);
	}

	/**
	 * @Desc 주문 상세 선물정보 조회
	 * @param odOrderId
	 * @return OrderPresentVo
	 */
	@Override
	public ApiResult<?> getOrderPresentInfo(String odOrderId) throws Exception {
		return  ApiResult.success(orderDetailService.getOrderPresentInfo(odOrderId));
	}

	/**
	 * @Desc 주문 상세 선물정보 수정
	 */
	@Override
	public ApiResult<?> putOrderPresentInfo(OrderDetailPresentVo orderDetailPresentVo) throws Exception {
		int result = orderDetailService.putOrderPresentInfo(orderDetailPresentVo);
		if(result > 0) return ApiResult.success();
		else return ApiResult.fail();
	}

	/**
	 * 선물하기 메세지 재발송
	 */
	@Override
	public ApiResult<?> reSendMessage(String odid) throws Exception {

		PgApprovalOrderDataDto orderData = orderOrderBiz.getPgApprovalOrderDataByOdid(odid);

		// 주문정보 조회
		OrderBuyerDto orderBuyerDto = orderDetailService.getOrderBuyer(odid);
		if(orderBuyerDto != null) {
			MallOrderDto order = mallOrderDetailBiz.getOrder(orderData.getOdOrderId(), orderBuyerDto.getUrUserId(), orderBuyerDto.getGuestCi());
			if (order == null){
				return ApiResult.result(OrderEnums.OrderErrMsg.VALUE_EMPTY);
			}
		}

		// 선물하기 메세지 재발송
		OrderEnums.OrderPresentErrorCode result = orderPresentBiz.reSendMessage(odid, BaseEnums.EnumSiteType.BOS.getCode());
		return OrderEnums.OrderPresentErrorCode.SUCCESS.equals(result) ? ApiResult.success() : ApiResult.result(result);
	}
}
