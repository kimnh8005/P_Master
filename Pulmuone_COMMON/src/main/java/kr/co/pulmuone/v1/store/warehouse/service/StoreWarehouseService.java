package kr.co.pulmuone.v1.store.warehouse.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.enums.WarehouseEnums.SetlYn;
import kr.co.pulmuone.v1.comm.enums.WarehouseEnums.UndeliverableType;
import kr.co.pulmuone.v1.comm.mapper.store.warehouse.StoreWarehouseMapper;
import kr.co.pulmuone.v1.goods.goods.dto.ArrivalScheduledDateDto;
import kr.co.pulmuone.v1.store.delivery.dto.WarehouseUnDeliveryableInfoDto;
import kr.co.pulmuone.v1.store.warehouse.service.dto.vo.UrWarehouseVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <PRE>
 * Forbiz Korea
 * Class의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    20200901   	 천혜현            최초작성
 * =======================================================================
 * </PRE>
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreWarehouseService {

	private final StoreWarehouseMapper storeWarehouseMapper;

	/**
	 * 휴일 제거
	 *
	 * @param forwardingDateList
	 * @return
	 * @throws Exception
	 */
	protected List<ArrivalScheduledDateDto> rmoveHoliday(Long urWarehouseId,
			List<ArrivalScheduledDateDto> scheduledDateList, boolean isDawnDelivery) throws Exception {

		if (scheduledDateList != null && !scheduledDateList.isEmpty()) {
			List<LocalDate> holidayList = storeWarehouseMapper.getCheckWarehouseHolidayList(urWarehouseId,
					scheduledDateList, isDawnDelivery);

			if (holidayList != null && holidayList.size() > 0) {
				// 2020-09-16
				scheduledDateList = scheduledDateList.stream()
						.filter(dto -> (holidayList.indexOf(dto.getForwardingScheduledDate()) < 0 ? true : false))
						.collect(Collectors.toList());
			}
		}

		return scheduledDateList;
	}

	/**
	 * 휴일 배송불가 업데이트
	 *
	 * @param urWarehouseId
	 * @param scheduledDateList
	 * @return
	 * @throws Exception
	 */
	protected List<ArrivalScheduledDateDto> updateHolidayUnDelivery(Long urWarehouseId, List<ArrivalScheduledDateDto> scheduledDateList, boolean isDawnDelivery) throws Exception {
		if (scheduledDateList != null && !scheduledDateList.isEmpty()) {
			List<LocalDate> holidayList = storeWarehouseMapper.getCheckWarehouseHolidayList(urWarehouseId, scheduledDateList, isDawnDelivery);

			if (holidayList != null && holidayList.size() > 0) {
				for (ArrivalScheduledDateDto dto : scheduledDateList) {
					if (holidayList.indexOf(dto.getForwardingScheduledDate()) >= 0) {
						dto.setUnDelivery(true);
					}
				}
			}
		}

		return scheduledDateList;
	}

	/**
	 * get 출고처 정보
	 *
	 * @param urWarehouseId
	 * @return
	 * @throws Exception
	 */
	protected UrWarehouseVo getWarehouse(Long urWarehouseId) throws Exception {
		return storeWarehouseMapper.getWarehouse(urWarehouseId);
	}

	/**
	 * 도착예정일 변경시 출고처 마감시간 체크
	 *
	 * @param urWarehouseId
	 * @param forwardingScheduledDate
	 * @param isDawnDelivery
	 * @throws Exception
	 */
	protected boolean checkOverWarehouseCutoffTime(Long urWarehouseId, LocalDate forwardingScheduledDate, Boolean isDawnDelivery) throws Exception{
		UrWarehouseVo warehouseVo = this.getWarehouse(urWarehouseId);
		LocalDate nowDate = LocalDate.now();
		LocalTime nowTime = LocalTime.now();

		if(nowDate.isEqual(forwardingScheduledDate)){
			if (isDawnDelivery) {
				// 새벽 배송일때
				if (nowTime.isAfter(warehouseVo.getDawnDeliveryCutoffTime())) {
					return false;
				}
			} else {
				// 일반 배송일때
				if (nowTime.isAfter(warehouseVo.getCutoffTime())) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 출고처 정산 여부 로 주문 정산 구분 조회
	 *
	 * @param urWarehouseId
	 * @return
	 * @throws Exception
	 */
	protected SetlYn getWarehouseSetlYn(Long urWarehouseId) throws Exception {
		UrWarehouseVo vo = storeWarehouseMapper.getWarehouse(urWarehouseId);
		if (vo != null) {
			if ("Y".equals(vo.getStlmnYn())) {
				return SetlYn.NO;
			} else {
				return SetlYn.NOT;
			}
		} else {
			return SetlYn.NOT;
		}
	}

	/**
	 * 출고처 주소 기반 배송 가능 정보 조회
	 *
	 * @param urWarehouseId
	 * @param isDawnDelivery
	 * @return
	 * @throws Exception
	 */
	protected WarehouseUnDeliveryableInfoDto getUnDeliverableInfo(Long urWarehouseId, boolean isDawnDelivery) throws Exception {
		WarehouseUnDeliveryableInfoDto reqDto = new WarehouseUnDeliveryableInfoDto();

	    UndeliverableType undeliverableAreaType = null;
	    String[] arrayUndeliverableAreaTp;

	    UrWarehouseVo warehouseVo = getWarehouse(urWarehouseId);

		if (isDawnDelivery) {
			undeliverableAreaType = UndeliverableType.findByCode(warehouseVo.getDawnUndeliverableAreaTp()); // 배송권역정책 신규로직 교체 시 미사용 예정
			arrayUndeliverableAreaTp = getArrayUndeliverableAreaTp(warehouseVo.getDawnUndeliverableAreaTpGrp()); // 배송권역정책 신규로직
		} else {
			undeliverableAreaType = UndeliverableType.findByCode(warehouseVo.getUndeliverableAreaTp()); // 배송권역정책 신규로직 교체 시 미사용 예정
			arrayUndeliverableAreaTp = getArrayUndeliverableAreaTp(warehouseVo.getUndeliverableAreaTpGrp()); // 배송권역정책 신규로직
		}

		reqDto.setShippingPossibility(true);
		reqDto.setUndeliverableType(undeliverableAreaType); // 배송권역정책 신규로직 교체 시 미사용 예정
		reqDto.setArrayUndeliverableType(arrayUndeliverableAreaTp); // 배송권역정책 신규로직
		return reqDto;
	}

	/**
	 * 출고처 다중 배송불가권역유형 array
	 * @param undeliverableAreaTpGrp
	 * @return
	 */
	protected String[] getArrayUndeliverableAreaTp(String undeliverableAreaTpGrp) {
		if(StringUtil.isEmpty(undeliverableAreaTpGrp)){
			return null;
		}

		return undeliverableAreaTpGrp.split(Constants.ARRAY_SEPARATORS);
	}
}