package kr.co.pulmuone.v1.order.order.service;

import kr.co.pulmuone.v1.comm.api.dto.vo.BaseApiResponseVo;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.*;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.order.delivery.service.ShippingZoneBiz;
import kr.co.pulmuone.v1.order.order.dto.mall.MallOrderDailyShippingZoneHistListDto;
import kr.co.pulmuone.v1.order.order.dto.mall.MallOrderDailyShippingZoneHistListResponseDto;
import kr.co.pulmuone.v1.order.order.dto.mall.MallOrderDailyShippingZoneRequestDto;
import kr.co.pulmuone.v1.order.order.dto.mall.MallOrderDetailGoodsDto;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderShippingZoneHistVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderShippingZoneVo;
import kr.co.pulmuone.v1.order.schedule.dto.OrderDetailScheduleListDto;
import kr.co.pulmuone.v1.order.schedule.dto.OrderDetailScheduleListRequestDto;
import kr.co.pulmuone.v1.order.schedule.service.OrderScheduleBiz;
import kr.co.pulmuone.v1.order.schedule.service.mall.MallOrderScheduleBiz;
import kr.co.pulmuone.v1.store.delivery.dto.WarehouseUnDeliveryableInfoDto;
import kr.co.pulmuone.v1.store.warehouse.service.StoreWarehouseBiz;
import kr.co.pulmuone.v1.user.buyer.dto.CommonSaveShippingAddressRequestDto;
import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <PRE>
 * Forbiz Korea
 * Mall 일일배송 주문상세 관련 BizImpl
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 02. 16.	김명진		최초작성
 * =======================================================================
 * </PRE>
 */

@Service
public class MallOrderDailyDetailBizImpl implements MallOrderDailyDetailBiz {

	@Autowired
	MallOrderDailyDetailService mallOrderDailyDetailService;

	@Autowired
	OrderDetailBiz orderDetailBiz;

	@Autowired
	ShippingZoneBiz shippingZoneBiz;

	@Autowired
	OrderScheduleBiz orderScheduleBiz;

	@Autowired
	MallOrderScheduleBiz mallOrderScheduleBiz;

	@Autowired
	private MallOrderDailyDetailBiz mallOrderDailyDetailBiz;

	@Autowired
	private MallOrderDetailBiz mallOrderDetailBiz;

	@Autowired
	private StoreWarehouseBiz storeWarehouseBiz;


	@Override
	public ApiResult<?> getOrderDailyShippingZoneHist(long odOrderId, long odShippingZoneId, long odOrderDetlId) {
		MallOrderDailyShippingZoneHistListDto result = new MallOrderDailyShippingZoneHistListDto();

		// 1. 일일배송 스케쥴 정보 조회
		MallOrderDetailGoodsDto orderDetailGoodsDto =  mallOrderDailyDetailBiz.getOrderDailyDetailByOdOrderDetlId(odOrderDetlId);

		// 1.1 베이비밀, 잇슬림인 경우
		if(GoodsEnums.GoodsDailyType.BABYMEAL.getCode().equals(orderDetailGoodsDto.getGoodsDailyTp())
				|| GoodsEnums.GoodsDailyType.EATSSLIM.getCode().equals(orderDetailGoodsDto.getGoodsDailyTp())){

			// 현재 배송지 정보
			result = mallOrderDailyDetailService.getOrderDailyShippingZone(odOrderId, odShippingZoneId);

			// 배송지 변경 이력 정보
			List<MallOrderDailyShippingZoneHistListDto> orderDailyShippingZoneHistList = mallOrderDailyDetailService.getOrderDailyShippingZoneHistList(odOrderId, odShippingZoneId);
			result.setHistList(orderDailyShippingZoneHistList);


			// 1.2 녹즙인 경우
		}else{
			OrderDetailScheduleListRequestDto orderDetailScheduleListRequestDto = new OrderDetailScheduleListRequestDto();

			if(GoodsEnums.GoodsType.PACKAGE.getCode().equals(orderDetailGoodsDto.getGoodsTpCd())
					&& ShoppingEnums.CartPromotionType.GREENJUICE_SELECT.getCode().equals(orderDetailGoodsDto.getPromotionTp())){
				// 1.2.1 - 녹즙-내맘대로 인 경우 -> OD_ORDER_ID 세팅
				orderDetailScheduleListRequestDto.setOdOrderId(String.valueOf(orderDetailGoodsDto.getOdOrderId()));
				orderDetailScheduleListRequestDto.setPromotionYn("Y");
			}else{
				// 1.2.2 - 녹즙인 경우 - > OD_ORDER_DETL_ID 세팅
				orderDetailScheduleListRequestDto.setOdOrderDetlId(odOrderDetlId);
			}

			List<MallOrderDailyShippingZoneHistListDto> orderDailyShippingZoneList = mallOrderDailyDetailService.getOrderDailyShippingZoneList(orderDetailScheduleListRequestDto);
			if(CollectionUtils.isNotEmpty(orderDailyShippingZoneList)){
				// 현재 적용중인 주소가 있는지 여부
				boolean isNowAddress = orderDailyShippingZoneList.stream().anyMatch(f->"Y".equals(f.getNowYn()));
				if(isNowAddress){
					for(MallOrderDailyShippingZoneHistListDto dto : orderDailyShippingZoneList){
						if("Y".equals(dto.getNowYn())){
							result.setApplyDtFrom(dto.getApplyDtFrom());
							result.setApplyDtTo(dto.getApplyDtTo());
							result.setRecvAddr1(dto.getRecvAddr1());
							result.setRecvAddr2(dto.getRecvAddr2());
							result.setRecvNm(dto.getRecvNm());
							result.setRecvZipCd(dto.getRecvZipCd());
							result.setRecvBldNo(dto.getRecvBldNo());
						}
					}

				}else{
					// 리스트의 첫번째 주소가 현재 적용중인 주소
					result.setApplyDtFrom(orderDailyShippingZoneList.get(0).getApplyDtFrom());
					result.setApplyDtTo(orderDailyShippingZoneList.get(0).getApplyDtTo());
					result.setRecvAddr1(orderDailyShippingZoneList.get(0).getRecvAddr1());
					result.setRecvAddr2(orderDailyShippingZoneList.get(0).getRecvAddr2());
					result.setRecvZipCd(orderDailyShippingZoneList.get(0).getRecvZipCd());
					result.setRecvNm(orderDailyShippingZoneList.get(0).getRecvNm());
					result.setRecvBldNo(orderDailyShippingZoneList.get(0).getRecvBldNo());
				}

				result.setHistList(orderDailyShippingZoneList);

			}
		}


		MallOrderDailyShippingZoneHistListResponseDto mallDailyShippingZoneHistResponse = MallOrderDailyShippingZoneHistListResponseDto.builder()
																																		.shippingZoneInfo(result)
																																		.build();
		return ApiResult.success(mallDailyShippingZoneHistResponse);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false, rollbackFor = {BaseException.class,Exception.class})
	public ApiResult<?> putOrderDailyShippingZone(MallOrderDailyShippingZoneRequestDto mallOrderDailyShippingZoneRequestDto) throws Exception{

		OrderDetailScheduleListRequestDto orderDetailScheduleListRequestDto = new OrderDetailScheduleListRequestDto();

		// 주문상세PK로 일일상품 정보 조회
		MallOrderDetailGoodsDto orderDetailGoodsDto =  mallOrderDailyDetailBiz.getOrderDailyDetailByOdOrderDetlId(mallOrderDailyShippingZoneRequestDto.getOdOrderDetlId());
		String deliveryType = this.getOrderDailyDetailDeliveryTypeByOdShippingZoneId(mallOrderDailyShippingZoneRequestDto.getOdShippingZoneId());

		//배송지 변경 가능 여부 체크
		CommonSaveShippingAddressRequestDto commonSaveShippingAddressRequestDto = new CommonSaveShippingAddressRequestDto();
		commonSaveShippingAddressRequestDto.setOdOrderId(orderDetailGoodsDto.getOdOrderId());
		commonSaveShippingAddressRequestDto.setOdShippingZoneId(mallOrderDailyShippingZoneRequestDto.getOdShippingZoneId());
		commonSaveShippingAddressRequestDto.setDeliveryType(deliveryType);
		commonSaveShippingAddressRequestDto.setReceiverZipCode(mallOrderDailyShippingZoneRequestDto.getRecvZipCd());
		commonSaveShippingAddressRequestDto.setBuildingCode(mallOrderDailyShippingZoneRequestDto.getRecvBldNo());
		ApiResult result = orderDetailBiz.isPossibleChangeDeliveryAddress(commonSaveShippingAddressRequestDto);

		if(!BaseEnums.Default.SUCCESS.getCode().equals(result.getCode())){
			// 배송지 변경 불가능
			return result;
		}

		// 출고처별 배송 불가 지역 체크
		List<HashMap> orderInfoMap = mallOrderDetailBiz.getOrderInfoForShippingPossibility(orderDetailGoodsDto.getOdOrderId(), mallOrderDailyShippingZoneRequestDto.getOdOrderDetlId());
		if(CollectionUtils.isNotEmpty(orderInfoMap)){
			for(HashMap map : orderInfoMap){
				boolean isDawnDelivery = Integer.parseInt(String.valueOf(map.get("IS_DAWN_DELIVERY"))) > 0 ? true : false;
				// 출고처 주소 기반 배송 가능 정보 조회
				WarehouseUnDeliveryableInfoDto warehouseUnDeliveryableInfoDto = storeWarehouseBiz.getUnDeliverableInfo(Long.parseLong(String.valueOf(map.get("UR_WAREHOUSE_ID"))), mallOrderDailyShippingZoneRequestDto.getRecvZipCd(), isDawnDelivery);

				boolean isShippingPossibility = warehouseUnDeliveryableInfoDto.isShippingPossibility();
				String shippingImpossibilityMsg = warehouseUnDeliveryableInfoDto.getShippingImpossibilityMsg();

				if(!isShippingPossibility){
					return ApiResult.result(shippingImpossibilityMsg, UserEnums.ChangeDeliveryAddress.WAREHOUSE_UNDELIVERABLE_AREA);
				}
			}
		}

		// 베이비밀, 잇슬림인 경우 -> 배송지 업데이트
		if(GoodsEnums.GoodsDailyType.BABYMEAL.getCode().equals(orderDetailGoodsDto.getGoodsDailyTp())
			|| GoodsEnums.GoodsDailyType.EATSSLIM.getCode().equals(orderDetailGoodsDto.getGoodsDailyTp())){

			// 권역정보가 존재할 경우 권역정보 Set
			if(!StringUtils.isEmpty(commonSaveShippingAddressRequestDto.getUrStoreId())) {
				mallOrderDailyShippingZoneRequestDto.setUrStoreId(commonSaveShippingAddressRequestDto.getUrStoreId());
			}

			mallOrderDailyDetailService.putOrderDailyShippingZoneForBabymealAndEatsslim(mallOrderDailyShippingZoneRequestDto,orderDetailGoodsDto);

		// 녹즙 & 녹즙-내맘대로 인 경우 -> 배송지 등록, 스케쥴 수정, ERP 스케줄 입력
		}else{

			// 배송 적용 일자 존재하지 않을 경우 관리자 문의
			if(StringUtils.isEmpty(mallOrderDailyShippingZoneRequestDto.getDeliveryDt())) {
				// mallOrderDailyShippingZoneRequestDto.setDeliveryDt(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
				return ApiResult.result(BaseEnums.Default.FAIL);
			}

			// 1. 주문 상품별 도착일 정보 리스트 조회
			List<OrderDetailScheduleListDto> orderDetailScheduleArrivalDateList = mallOrderDailyDetailService.getOrderDetailScheduleArrivalDateList(mallOrderDailyShippingZoneRequestDto.getDeliveryDt(), orderDetailGoodsDto);
			if(CollectionUtils.isEmpty(orderDetailScheduleArrivalDateList)){
				return ApiResult.result(BaseEnums.Default.FAIL);
			}

			// 2. OD_SHIPPING_ZONE -> 새로운 주소지 등록
			long odShippingZoneId = mallOrderDailyDetailService.getOrderShippingZoneSeq();

			OrderShippingZoneVo orderShippingZoneVo = OrderShippingZoneVo.builder()
					.odOrderId(orderDetailGoodsDto.getOdOrderId())
					.deliveryType(ShoppingEnums.DeliveryType.DAILY.getCode())
					.shippingType(1)
					.odShippingZoneId(odShippingZoneId)
					.recvNm(mallOrderDailyShippingZoneRequestDto.getRecvNm())
					.recvHp(mallOrderDailyShippingZoneRequestDto.getRecvHp())
					.recvZipCd(mallOrderDailyShippingZoneRequestDto.getRecvZipCd())
					.recvAddr1(mallOrderDailyShippingZoneRequestDto.getRecvAddr1())
					.recvAddr2(mallOrderDailyShippingZoneRequestDto.getRecvAddr2())
					.recvBldNo(mallOrderDailyShippingZoneRequestDto.getRecvBldNo())
					.deliveryMsg(mallOrderDailyShippingZoneRequestDto.getDeliveryMsg())
					.doorMsgCd(mallOrderDailyShippingZoneRequestDto.getDoorMsgCd())
					.doorMsg(mallOrderDailyShippingZoneRequestDto.getDoorMsg())
					.build();
			// 권역정보가 존재할 경우 권역정보 Set
			if(!StringUtils.isEmpty(commonSaveShippingAddressRequestDto.getUrStoreId())) {
				orderShippingZoneVo.setUrStoreId(Long.parseLong(commonSaveShippingAddressRequestDto.getUrStoreId()));
			}
			orderDetailBiz.addShippingZone(orderShippingZoneVo);

			// 3. OD_ORDER_DETL_DAILY_SCH 스케쥴 수정, ERP 스케줄 입력
			if(GoodsEnums.GoodsType.PACKAGE.getCode().equals(orderDetailGoodsDto.getGoodsTpCd())
					&& ShoppingEnums.CartPromotionType.GREENJUICE_SELECT.getCode().equals(orderDetailGoodsDto.getPromotionTp())){
				// 3-1. 녹즙-내맘대로 인 경우

				ApiResult<?> apiResult = putOrderDailyGreenjuiceShippingZone(orderDetailGoodsDto,orderDetailScheduleArrivalDateList, odShippingZoneId, mallOrderDailyShippingZoneRequestDto.getDeliveryDt());
				if(!apiResult.getCode().equals(BaseEnums.Default.SUCCESS.getCode())){
					return apiResult;
				}
			}else{

				List<MallOrderDetailGoodsDto>  orderDetailGoodsList = mallOrderDailyDetailBiz.getOrderDailyDetailByOdOrderId(orderDetailGoodsDto.getOdOrderId(), orderDetailGoodsDto.getGoodsDailyTp());

				// 3-2. 녹즙인 경우 - > 같은 주문건에서 녹즙주문건 모두 변경
				for(MallOrderDetailGoodsDto mallOrderDetailGoodsDto : orderDetailGoodsList){

					orderDetailScheduleListRequestDto.setOdOrderDetlId(mallOrderDetailGoodsDto.getOdOrderDetlId());
					orderDetailScheduleListRequestDto.setChangeDate(mallOrderDailyShippingZoneRequestDto.getDeliveryDt());
					List<OrderDetailScheduleListDto>  orderDetailList = mallOrderScheduleBiz.getOrderDetailScheduleList(orderDetailScheduleListRequestDto);

					ApiResult<?> apiResult = putOrderDailyGreenjuiceShippingZone(mallOrderDetailGoodsDto,orderDetailList, odShippingZoneId, mallOrderDailyShippingZoneRequestDto.getDeliveryDt());
					if(!apiResult.getCode().equals(BaseEnums.Default.SUCCESS.getCode())){
						return apiResult;
					}
				}
			}

			// 주문 배송 정보 조회
			OrderShippingZoneVo orderShipingZoneVo = shippingZoneBiz.getOrderShippingZone(odShippingZoneId);

			// 주문 배송 정보 이력 등록
			OrderShippingZoneHistVo orderShippingZoneHistVo = OrderShippingZoneHistVo.builder()
					.odOrderId(orderShipingZoneVo.getOdOrderId())
					.odShippingZoneId(odShippingZoneId)
					.deliveryType(orderShipingZoneVo.getDeliveryType())
					.shippingType(orderShipingZoneVo.getShippingType())
					.recvNm(mallOrderDailyShippingZoneRequestDto.getRecvNm())
					.recvHp(mallOrderDailyShippingZoneRequestDto.getRecvHp())
					.recvZipCd(mallOrderDailyShippingZoneRequestDto.getRecvZipCd())
					.recvAddr1(mallOrderDailyShippingZoneRequestDto.getRecvAddr1())
					.recvAddr2(mallOrderDailyShippingZoneRequestDto.getRecvAddr2())
					.recvBldNo(mallOrderDailyShippingZoneRequestDto.getRecvBldNo())
					.deliveryMsg(mallOrderDailyShippingZoneRequestDto.getDeliveryMsg())
					.doorMsgCd(mallOrderDailyShippingZoneRequestDto.getDoorMsgCd())
					.doorMsg(mallOrderDailyShippingZoneRequestDto.getDoorMsg())
					.build();
			orderDetailBiz.addShippingZoneHist(orderShippingZoneHistVo);
		}

		return ApiResult.success();
	}

	@Override
	public MallOrderDetailGoodsDto getOrderDailyDetailByOdOrderDetlId(Long odOrderDetlId){
		return mallOrderDailyDetailService.getOrderDailyDetailByOdOrderDetlId(odOrderDetlId);
	}

	/**
	 * 주문배송지PK 정보로 배송타입 조회
	 * @param odShippingZoneId
	 * @return
	 */
	private String getOrderDailyDetailDeliveryTypeByOdShippingZoneId(long odShippingZoneId) {
		return mallOrderDailyDetailService.getOrderDailyDetailDeliveryTypeByOdShippingZoneId(odShippingZoneId);
	}

	@Override
	public List<MallOrderDailyShippingZoneHistListDto> getOrderDailyShippingZoneList(OrderDetailScheduleListRequestDto orderDetailScheduleListRequestDto){
		return mallOrderDailyDetailService.getOrderDailyShippingZoneList(orderDetailScheduleListRequestDto);
	}

	@Override
	public long getOrderShippingZoneSeq(){
		return mallOrderDailyDetailService.getOrderShippingZoneSeq();
	}

	@Override
	public int getOrderDailyShippingZoneChangeCount(String promotionType, Long odOrderDetlId){
		return mallOrderDailyDetailService.getOrderDailyShippingZoneChangeCount(promotionType, odOrderDetlId);
	}

	@Override
	public ApiResult<?> putOrderDailyGreenjuiceShippingZone(MallOrderDetailGoodsDto orderDetailGoodsDto, List<OrderDetailScheduleListDto>  orderDetailScheduleArrivalDateList, Long odShippingZoneId, String deliveryDt){
		String scheduleBatchYn;
		// 녹즙 내맘대로
		if(ShoppingEnums.CartPromotionType.GREENJUICE_SELECT.getCode().equals(orderDetailGoodsDto.getPromotionTp())){

			// 스캐줄 배치 여부 조회 -> 내맘대로일 경우 param값으로 OD_ORDER_DETL_PARENT_ID가 들어와서 해당 아이디의 구성상품 OD_ORDER_DETL_ID로 스케쥴 배치여부 조회
			scheduleBatchYn = orderScheduleBiz.getOrderDetailBatchInfo(orderDetailGoodsDto.getOdOrderDetlChildId());

			// 3. OD_ORDER_DETL_DAILY_SCH -> 적용일자이후 일일배송 스케쥴의 주문 배송지PK 수정
			// 스케쥴 배치 Y인 경우 -> 스케쥴 취소후 다시 생성
			if(scheduleBatchYn.equals(OrderScheduleEnums.ScheduleBatchType.SCHEDULE_BATCH_Y.getCode())) {
				orderScheduleBiz.addChangeOrderDetailSchedule(orderDetailScheduleArrivalDateList,odShippingZoneId);

				// 스케쥴 업데이트
			}else{
				List<Long> odOrderDetlDailyIdList = orderDetailScheduleArrivalDateList.stream().map(m->m.getOdOrderDetlDailyId()).collect(Collectors.toList())
						.stream().distinct().collect(Collectors.toList());
				for(Long odOrderDetlDailyId : odOrderDetlDailyIdList){
					mallOrderScheduleBiz.putOrderDetlScheduleOdShippingZoneId(odShippingZoneId,odOrderDetlDailyId,deliveryDt);
				}
			}

			// 4. OD_ORDER_DETL_DAILY_ZONE -> 일일배송 배송지정보 수정
			orderScheduleBiz.saveOrderDetlDailyZone(orderDetailGoodsDto.getOdOrderDetlChildId(),orderDetailGoodsDto.getPromotionTp(), orderDetailGoodsDto.getOdOrderId());

			// 4-1. OD_ORDER_DETL_DAILY 테이블의 주문상세번호로 스토어PK 업데이트 처리
			orderScheduleBiz.putOrderDetlDailyUrStoreId(orderDetailGoodsDto.getOdOrderId(), 0, odShippingZoneId);
		}
		// 녹즙
		else{
			// 스캐줄 배치 여부 조회
			scheduleBatchYn = orderScheduleBiz.getOrderDetailBatchInfo(orderDetailGoodsDto.getOdOrderDetlId());

			// 3. OD_ORDER_DETL_DAILY_SCH -> 적용일자이후 일일배송 스케쥴의 주문 배송지PK 수정
			// 스케쥴 배치 Y인 경우 -> 스케쥴 취소후 다시 생성
			if(scheduleBatchYn.equals(OrderScheduleEnums.ScheduleBatchType.SCHEDULE_BATCH_Y.getCode())) {
				orderScheduleBiz.addChangeOrderDetailSchedule(orderDetailScheduleArrivalDateList, odShippingZoneId);

				// 스케쥴 업데이트
			}else{
				long odOrderDetlDailyId = orderDetailScheduleArrivalDateList.get(0).getOdOrderDetlDailyId();
				mallOrderScheduleBiz.putOrderDetlScheduleOdShippingZoneId(odShippingZoneId,odOrderDetlDailyId,deliveryDt);
			}

			// 4. OD_ORDER_DETL_DAILY_ZONE -> 일일배송 배송지정보 수정
			orderScheduleBiz.saveOrderDetlDailyZone(orderDetailGoodsDto.getOdOrderDetlId(),orderDetailGoodsDto.getPromotionTp(), null);

			// 4-1. OD_ORDER_DETL_DAILY 테이블의 주문상세번호로 스토어PK 업데이트 처리
			orderScheduleBiz.putOrderDetlDailyUrStoreId(orderDetailGoodsDto.getOdOrderId(), orderDetailGoodsDto.getOdOrderDetlId(), odShippingZoneId);
		}

		// 5. 스케쥴 배치 Y인 경우 -> ERP 스케쥴 수정
		if(OrderScheduleEnums.ScheduleBatchType.SCHEDULE_BATCH_Y.getCode().equals(scheduleBatchYn)) {

			//스캐줄 변경 내역 확인
			BaseApiResponseVo baseApiResponseVo = null;
			baseApiResponseVo = orderScheduleBiz.getErpCustordApiList(orderDetailGoodsDto.getOdid(), OrderScheduleEnums.ScheduleOrderType.SCHEDULE_ORDER_TYPE_CANCEL.getCode());

			if (!baseApiResponseVo.isSuccess()) {
				return ApiResult.result(OrderScheduleEnums.ScheduleErrMsg.API_COMMUNICATION_FAILED);
			}else if(baseApiResponseVo.getResponseCode().equals("000")){
				return ApiResult.result(OrderScheduleEnums.ScheduleErrMsg.API_SCHEDULE_INSERT_FAILED);
			}

			// 녹즙-내맘대로인 경우
			if(ShoppingEnums.CartPromotionType.GREENJUICE_SELECT.getCode().equals(orderDetailGoodsDto.getPromotionTp())){

				for(OrderDetailScheduleListDto dto : orderDetailScheduleArrivalDateList){
					// 녹즙 주문 스케쥴 seq 조회
					//int orderDetailDailySchSeq = orderScheduleBiz.getOrderDetailDailySchSeq(dto.getOdOrderDetlId());
					// ERP 스케쥴 수정
					//ApiResult<?> apiResult = orderScheduleBiz.putIfDlvFlagByErp(dto.getOdOrderDetlId(), orderDetailDailySchSeq);
					ApiResult<?> apiResult = orderScheduleBiz.putIfDlvFlagByErp(dto.getOdOrderDetlId(), deliveryDt);
					if(!apiResult.getCode().equals(BaseEnums.Default.SUCCESS.getCode())){
						return apiResult;
					}
				}

				// 녹즙인 경우
			}else{
				// 녹즙 주문 스케쥴 seq 조회
				//int orderDetailDailySchSeq = orderScheduleBiz.getOrderDetailDailySchSeq(orderDetailGoodsDto.getOdOrderDetlId());
				// ERP 스케쥴 수정
				//ApiResult<?> apiResult = orderScheduleBiz.putIfDlvFlagByErp(orderDetailGoodsDto.getOdOrderDetlId(), orderDetailDailySchSeq);
				ApiResult<?> apiResult = orderScheduleBiz.putIfDlvFlagByErp(orderDetailGoodsDto.getOdOrderDetlId(), deliveryDt);
				if(!apiResult.getCode().equals(BaseEnums.Default.SUCCESS.getCode())){
					return apiResult;
				}

			}
		}

		return ApiResult.success();
	}

	@Override
	public List<MallOrderDetailGoodsDto> getOrderDailyDetailByOdOrderId(Long odOrderId, String goodsDailyTp){
		return mallOrderDailyDetailService.getOrderDailyDetailByOdOrderId(odOrderId, goodsDailyTp);
	}

	@Override
	public List<Long> getOrderGoodsIdListByOdOrderDetlId(@Param("odOrderDetlId")Long odOrderDetlId){
		return mallOrderDailyDetailService.getOrderGoodsIdListByOdOrderDetlId(odOrderDetlId);
	}

	@Override
	public List<OrderDetailScheduleListDto> getOrderDetailScheduleArrivalDateList(String deliveryDt, MallOrderDetailGoodsDto orderDetailGoodsDto) throws Exception{
		return mallOrderDailyDetailService.getOrderDetailScheduleArrivalDateList(deliveryDt, orderDetailGoodsDto);
	}
}