package kr.co.pulmuone.v1.store.delivery.service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums.StoreDeliverableItem;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums.StoreDeliveryInterval;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums.StoreDeliveryType;
import kr.co.pulmuone.v1.comm.mapper.store.delivery.StoreDeliveryMapper;
import kr.co.pulmuone.v1.shopping.cart.dto.CartStoreShippingDateScheduleDto;
import kr.co.pulmuone.v1.store.delivery.dto.ShippingAddressPossibleDeliveryInfoDto;
import kr.co.pulmuone.v1.store.delivery.dto.ShippingPossibilityStoreDeliveryAreaDto;
import kr.co.pulmuone.v1.store.delivery.dto.StoreDeliveryScheduleDto;
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
 * 버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 * 1.0    20200901   	 천혜현            최초작성
 * =======================================================================
 * </PRE>
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreDeliveryService {

    private final StoreDeliveryMapper storeDeliveryMapper;

    /**
     * 스토어 배송권역 정보 조회
     *
     * @param storeType, storeDeliveralbeItemType,
     *                    receiverZipCode, buildingCode
     * @return ShippingPossibilityStoreDeliveryAreaDto
     * @throws Exception
     */
	protected ShippingPossibilityStoreDeliveryAreaDto getShippingPossibilityStoreDeliveryAreaInfo(
			String storeType, List<String> storeDeliveralbeItemTypeBySupplierIdList,
			String receiverZipCode, String buildingCode) throws Exception {

		return storeDeliveryMapper.getShippingPossibilityStoreDeliveryAreaInfo(storeType,
				storeDeliveralbeItemTypeBySupplierIdList, receiverZipCode, buildingCode, null);
	}

	protected ShippingPossibilityStoreDeliveryAreaDto getShippingPossibilityStoreDeliveryAreaInfo(
			String storeType, List<String> storeDeliveralbeItemTypeBySupplierIdList,
			String receiverZipCode, String buildingCode, String storeDeliveryType) throws Exception {

		return storeDeliveryMapper.getShippingPossibilityStoreDeliveryAreaInfo(storeType,
				storeDeliveralbeItemTypeBySupplierIdList, receiverZipCode, buildingCode, storeDeliveryType);
	}

    /**
     * 배송 권역 여부
     *
     * @param zipCode String
     * @param buildingCode String
     * @return ShippingAddressPossibleDeliveryInfoDto
     * @throws Exception Exception
     */
    protected ShippingAddressPossibleDeliveryInfoDto getShippingPossibilityDeliveryArea(String zipCode, String buildingCode) throws Exception {
        ShippingAddressPossibleDeliveryInfoDto result = new ShippingAddressPossibleDeliveryInfoDto();

        // 새벽배송, 택배배송
        result.setDawnDelivery(isDawnDeliveryArea(zipCode, buildingCode));
        result.setShippingCompDelivery(true);

        // 매장배송
        List<String> storeNameList = storeDeliveryMapper.getStoreDeliveryArea(zipCode, buildingCode);
        if(storeNameList.size() > 0){
            result.setStoreName(storeNameList);
            result.setStoreDelivery(true);
        }else{
            result.setStoreDelivery(false);
        }

        // 일일배송
        final List<String> DELIVERY_TYPE = Arrays.asList(GoodsEnums.StoreDeliverableItem.FD.getCode(), GoodsEnums.StoreDeliverableItem.DM.getCode());
        List<String> dailyDeliveryTypeList = storeDeliveryMapper.getDailyDeliveryArea(zipCode, buildingCode);

        if(dailyDeliveryTypeList.size() > 0){
            // 일일배송유형 ALL 이 포함 되어 있으면 ALL
            if(dailyDeliveryTypeList.contains(GoodsEnums.StoreDeliverableItem.ALL.getCode())){
                result.setDailyDeliveryType(GoodsEnums.StoreDeliverableItem.ALL.getCode());
            }else{
                // 일일배송유형 FDD, PDM 포함시 ALL
                if(dailyDeliveryTypeList.containsAll(DELIVERY_TYPE)){
                    result.setDailyDeliveryType(GoodsEnums.StoreDeliverableItem.ALL.getCode());
                }else{
                    result.setDailyDeliveryType(dailyDeliveryTypeList.get(0));
                }
            }
            result.setDailyDelivery(true);
        }else{
            result.setDailyDelivery(false);
        }

        return result;
    }

	/**
	 * 새벽 배송 여부
	 *
	 * @param zipCode
	 * @param buildingCode
	 * @return
	 * @throws Exception
	 */
	protected boolean isDawnDeliveryArea(String zipCode, String buildingCode) throws Exception {
		return storeDeliveryMapper.isDawnDeliveryArea(zipCode, buildingCode);
	}

	protected List<CartStoreShippingDateScheduleDto> getStoreSchedule(String urStoreId, String urDeliveryAreaId, LocalDate arrivalScheduledDate) throws Exception {
		return storeDeliveryMapper.getStoreSchedule(urStoreId, urDeliveryAreaId, arrivalScheduledDate);
	}

	protected StoreDeliveryScheduleDto getStoreScheduleByUrStoreScheduleId(Long urStoreScheduleId, LocalDate arrivalScheduledDate) throws Exception {
		return storeDeliveryMapper.getStoreScheduleByUrStoreScheduleId(urStoreScheduleId, arrivalScheduledDate);
	}

	protected ShippingPossibilityStoreDeliveryAreaDto getUrStoreDeliveryAreaId(String urStoreId, String storeDeliveryType, String storeDeliverableItemType) throws Exception{
        return storeDeliveryMapper.getUrStoreDeliveryAreaId(urStoreId, storeDeliveryType, storeDeliverableItemType);
    }

	protected boolean isPossibleSelectStoreSchedule(List<CartStoreShippingDateScheduleDto> list) throws Exception {
		for (CartStoreShippingDateScheduleDto dto : list) {
			if (dto.isPossibleSelect()) {
				return true;
			}
		}
		return false;
	}

	protected ShippingPossibilityStoreDeliveryAreaDto makeStoreDeliveryIntervalParcelInfo() throws Exception {
		ShippingPossibilityStoreDeliveryAreaDto result = new ShippingPossibilityStoreDeliveryAreaDto();
		result.setStoreDeliveryIntervalType(StoreDeliveryInterval.PARCEL.getCode());
		result.setStoreDeliveryType(StoreDeliveryType.HOME.getCode());
		result.setStoreDeliverableItemType(StoreDeliverableItem.DM.getCode());
		return result;
	}
}