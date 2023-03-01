package kr.co.pulmuone.v1.store.warehouse.service;

import java.time.LocalDate;
import java.util.List;

import kr.co.pulmuone.v1.policy.config.service.PolicyConfigBiz;
import kr.co.pulmuone.v1.policy.shiparea.dto.vo.NonDeliveryAreaInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.DeliveryEnums;
import kr.co.pulmuone.v1.comm.enums.WarehouseEnums.SetlYn;
import kr.co.pulmuone.v1.goods.goods.dto.ArrivalScheduledDateDto;
import kr.co.pulmuone.v1.policy.shiparea.service.PolicyShipareaBiz;
import kr.co.pulmuone.v1.store.delivery.dto.WarehouseUnDeliveryableInfoDto;
import kr.co.pulmuone.v1.store.warehouse.service.dto.vo.UrWarehouseVo;
import kr.co.pulmuone.v1.system.code.dto.vo.GetCodeListResultVo;
import kr.co.pulmuone.v1.system.code.service.SystemCodeBiz;

@Service
public class StoreWarehouseBizImpl implements StoreWarehouseBiz {

	@Autowired
	private StoreWarehouseService storeWarehouseService;

	@Autowired
	private SystemCodeBiz systemCodeBiz;

	@Autowired
	private PolicyShipareaBiz policyShipareaBiz;

	@Autowired
	private PolicyConfigBiz policyConfigBiz;

	/**
	 * 휴일 제거
	 */
	@Override
	public List<ArrivalScheduledDateDto> rmoveHoliday(Long urWarehouseId, List<ArrivalScheduledDateDto> scheduledDateList, boolean isDawnDelivery) throws Exception {
		return storeWarehouseService.rmoveHoliday(urWarehouseId, scheduledDateList, isDawnDelivery);
	}

	/**
	 * 휴일 배송불가 업데이트
	 */
	@Override
	public List<ArrivalScheduledDateDto> updateHolidayUnDelivery(Long urWarehouseId, List<ArrivalScheduledDateDto> scheduledDateList, boolean isDawnDelivery) throws Exception {
		return storeWarehouseService.updateHolidayUnDelivery(urWarehouseId, scheduledDateList, isDawnDelivery);
	}

	/**
	 * 출고처 정보 조회
	 */
	@Override
	public UrWarehouseVo getWarehouse(Long urWarehouseId) throws Exception {
		return storeWarehouseService.getWarehouse(urWarehouseId);
	}

	/**
	 * 도착예정일 변경시 출고처 마감시간 체크
	 */
	@Override
	public ApiResult<?> checkOverWarehouseCutoffTime(Long urWarehouseId, LocalDate forwardingScheduledDate, Boolean isDawnDelivery) throws Exception{
		boolean checkOverWarehouseCutOffTime = storeWarehouseService.checkOverWarehouseCutoffTime(urWarehouseId, forwardingScheduledDate, isDawnDelivery);
		if(!checkOverWarehouseCutOffTime){
			return ApiResult.result(DeliveryEnums.ChangeArriveDateValidation.OVER_WAREHOUSE_CUTOFF_TIME);
		}
		return ApiResult.success();
	}

	/**
	 * 출고처 정산 여부 로 주문 정산 구분 조회
	 */
	@Override
	public SetlYn getWarehouseSetlYn(Long urWarehouseId) throws Exception {
		return storeWarehouseService.getWarehouseSetlYn(urWarehouseId);
	}

	/**
	 * 출고처 주소 기반 배송 가능 정보 조회
	 */
	@Override
	public WarehouseUnDeliveryableInfoDto getUnDeliverableInfo(Long urWarehouseId, String zipCode, boolean isDawnDelivery) throws Exception {
		WarehouseUnDeliveryableInfoDto reqDto = storeWarehouseService.getUnDeliverableInfo(urWarehouseId, isDawnDelivery);

		// isApplyDeliveryAreaPolicy 정책키의 적용 날짜를 비교하여 배송권역정책(도서산간, 배송불가권역 신규 테이블 로직)을 적용
		if(policyConfigBiz.isApplyDeliveryAreaPolicy()){
			// 신규 배송권역정책 로직
			if(reqDto.getArrayUndeliverableType() == null){
				return reqDto;
			}

			// 배송불가 지역 여부
			reqDto.setShippingPossibility(!policyShipareaBiz.isNonDeliveryArea(reqDto.getArrayUndeliverableType(), zipCode));

			if(reqDto.isShippingPossibility()){
				return reqDto;
			}

			// 배송 불가 사유값 조회 : 한 우편번호에 배송불가권역유형이 두개 이상일때 최초 등록된 배송불가권역유형의 메시지로 안내
			NonDeliveryAreaInfoVo nonDeliveryAreaInfoVo = policyShipareaBiz.getNonDeliveryAreaInfo(reqDto.getArrayUndeliverableType(), zipCode);
			if(nonDeliveryAreaInfoVo != null){
				reqDto.setShippingImpossibilityMsg(nonDeliveryAreaInfoVo.getNonDeliveryAreaMessage());
			}

		} else {
			// 배송권역정책 기존 로직
			if (reqDto.getUndeliverableType() != null) {
				// 배송불가 지역 여부
				reqDto.setShippingPossibility(!policyShipareaBiz.isUndeliverableArea(reqDto.getUndeliverableType().getCode(), zipCode));

				if (!reqDto.isShippingPossibility()) {
					// 공통코드의 추가 1필드에 배송 불가 사유 입력
					GetCodeListResultVo codeVo = systemCodeBiz.getCode(reqDto.getUndeliverableType().getCode());
					reqDto.setShippingImpossibilityMsg(codeVo.getAttribute1());
				}
			}
		}

		return reqDto;
	}
}