package kr.co.pulmuone.v1.batch.order.order;

import kr.co.pulmuone.v1.batch.order.order.dto.OrderClaimAddShippingPriceListDto;
import kr.co.pulmuone.v1.batch.order.order.dto.VirtualBankOrderCancelDto;
import kr.co.pulmuone.v1.comm.api.dto.vo.BaseApiResponseVo;
import kr.co.pulmuone.v1.comm.enums.OrderScheduleEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.mappers.batch.master.order.order.GreenJuiceSyncMapper;
import kr.co.pulmuone.v1.comm.mappers.batch.master.order.order.VirtualBankOrderCancelMapper;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimGoodsInfoDto;
import kr.co.pulmuone.v1.order.schedule.dto.ErpIfCustordConditionRequestDto;
import kr.co.pulmuone.v1.order.schedule.dto.ErpIfCustordRequestDto;
import kr.co.pulmuone.v1.order.schedule.dto.ErpIfCustordSearchResponseDto;
import kr.co.pulmuone.v1.order.schedule.dto.ErpIfCustordSerachLineResponseDto;
import kr.co.pulmuone.v1.order.schedule.dto.vo.OrderDetlScheduleVo;
import kr.co.pulmuone.v1.order.schedule.service.OrderScheduleBiz;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 녹즙 동기화 배치 Service
 * </PRE>
 */

@Slf4j
@RequiredArgsConstructor
@Service
public class GreenJuiceSyncService {

	private final GreenJuiceSyncMapper greenJuiceSyncMapper;

	@Autowired
	private OrderScheduleBiz orderScheduleBiz;

	/**
	 * 녹즙 ERP 주문 / 취소 변경 내역 조회
	 * @return
	 */
	protected List<ErpIfCustordSearchResponseDto> getErpCustordApiList() {

		BaseApiResponseVo baseApiResponseVo = null;
		List<ErpIfCustordSearchResponseDto> erpCustordApiList = new ArrayList<>();

		// 녹즙 취소 내역 조회
		baseApiResponseVo = orderScheduleBiz.getErpCustordApiAllList(OrderScheduleEnums.ScheduleOrderType.SCHEDULE_ORDER_TYPE_CANCEL.getCode());

		if (baseApiResponseVo.isSuccess()) {
			log.debug("-------- 녹즙 취소 내역 조회 성공 :: <{}> --------", baseApiResponseVo.getResponseMessage());
			erpCustordApiList.addAll(baseApiResponseVo.deserialize(ErpIfCustordSearchResponseDto.class));
		}

		// 녹즙 주문 내역 조회
		baseApiResponseVo = orderScheduleBiz.getErpCustordApiAllList(OrderScheduleEnums.ScheduleOrderType.SCHEDULE_ORDER_TYPE_ORDER.getCode());

		if (baseApiResponseVo.isSuccess()) {
			log.debug("-------- 녹즙 주문 내역 조회 성공 :: <{}> --------", baseApiResponseVo.getResponseMessage());
			erpCustordApiList.addAll(baseApiResponseVo.deserialize(ErpIfCustordSearchResponseDto.class));
		}

		return erpCustordApiList;
	}

	/**
	 * 녹즙 ERP 주문 / 취소 변경 내역 조회 완료 처리
	 * @param erpCustordApiList
	 */
	protected BaseApiResponseVo putErpCustordApiComplete(List<ErpIfCustordSearchResponseDto> erpCustordApiList) {

		List<ErpIfCustordRequestDto> erpCustordFlagApiList = new ArrayList<>();

		for(ErpIfCustordSearchResponseDto erpIfCustordSearchResponseDto : erpCustordApiList) {
			ErpIfCustordConditionRequestDto erpIfCustordConditionRequestDto = new ErpIfCustordConditionRequestDto();
			erpIfCustordConditionRequestDto.setHrdSeq(erpIfCustordSearchResponseDto.getHrdSeq());
			erpIfCustordConditionRequestDto.setOrdNum(erpIfCustordSearchResponseDto.getOrderNumber());

			List<ErpIfCustordConditionRequestDto> scheduleList = new ArrayList<>();
			scheduleList.add(erpIfCustordConditionRequestDto);

			ErpIfCustordRequestDto erpIfCustordRequestDto = new ErpIfCustordRequestDto();
			erpIfCustordRequestDto.setHrdSeq(erpIfCustordSearchResponseDto.getHrdSeq());
			erpIfCustordRequestDto.setOrdNum(erpIfCustordSearchResponseDto.getOrderNumber());
			erpIfCustordRequestDto.setLine(scheduleList);

			erpCustordFlagApiList.add(erpIfCustordRequestDto);
		}

		return orderScheduleBiz.putErpCustordApiComplete(erpCustordFlagApiList);
	}

	/**
	 * 녹즙 ERP 주문 / 취소 동기화 처리
	 * @param erpCustordApiList
	 */
	protected void putGreenJuiceSync(List<ErpIfCustordSearchResponseDto> erpCustordApiList) {

		List<OrderDetlScheduleVo> insertScheduleList = new ArrayList<>();
		List<OrderDetlScheduleVo> updateScheduleList = new ArrayList<>();
		String deliveryDate = "";
		for(ErpIfCustordSearchResponseDto erpIfCustordSearchResponseDto : erpCustordApiList) {
			if(erpIfCustordSearchResponseDto.getLine() != null) {
				for( ErpIfCustordSerachLineResponseDto erpIfCustordSerachLineResponseDto : erpIfCustordSearchResponseDto.getLine()){
					OrderDetlScheduleVo orderDetlScheduleVo = new OrderDetlScheduleVo();

					orderDetlScheduleVo.setOdid(Long.parseLong(erpIfCustordSearchResponseDto.getOrderNumber()));
					orderDetlScheduleVo.setOdOrderDetlSeq(erpIfCustordSerachLineResponseDto.getOrdNoDtl());
					orderDetlScheduleVo.setOdOrderDetlDailySchSeq(erpIfCustordSerachLineResponseDto.getSchLinNo());

					// 주문 상세번호 조회
					long odOrderDetlId = greenJuiceSyncMapper.getOdOrderDetlByOrderDetlScheduleVo(orderDetlScheduleVo);
					orderDetlScheduleVo.setOdOrderDetlId(odOrderDetlId);

					deliveryDate = 	erpIfCustordSerachLineResponseDto.getDlvReqDat().substring(0, 4) + "-"
									+ erpIfCustordSerachLineResponseDto.getDlvReqDat().substring(4, 6) + "-"
									+ erpIfCustordSerachLineResponseDto.getDlvReqDat().substring(6, 8);
					orderDetlScheduleVo.setDeliveryDt(deliveryDate);
					orderDetlScheduleVo.setOrderSchStatus(erpIfCustordSearchResponseDto.getHrdType());
					orderDetlScheduleVo.setOrderCnt(Integer.parseInt(erpIfCustordSerachLineResponseDto.getOrdCnt()));

					if (OrderScheduleEnums.ScheduleOrderType.SCHEDULE_ORDER_TYPE_CANCEL.getCode().equals(erpIfCustordSearchResponseDto.getHrdType())) {
						orderDetlScheduleVo.setUseYn(OrderScheduleEnums.ScheduleUseType.SCHEDULE_USE_N.getCode());
						updateScheduleList.add(orderDetlScheduleVo);
					}
					else {
						orderDetlScheduleVo.setUseYn(OrderScheduleEnums.ScheduleUseType.SCHEDULE_USE_Y.getCode());
						insertScheduleList.add(orderDetlScheduleVo);
					}
				}
			}
		}

		int result = 0;

		if (!updateScheduleList.isEmpty())
			result = orderScheduleBiz.putErpOrderDetlSchedule(updateScheduleList);
		log.debug("-------- 녹즙 동기화 수정 [<{}>]건 --------", result);

		result = 0;
		if (!insertScheduleList.isEmpty())
			result = orderScheduleBiz.addOrderDetlSchedule(insertScheduleList);
		log.debug("-------- 녹즙 동기화 등록 [<{}>]건 --------", result);
	}
}
