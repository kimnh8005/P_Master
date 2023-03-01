package kr.co.pulmuone.v1.store.delivery.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import kr.co.pulmuone.v1.store.delivery.dto.ShippingAddressPossibleDeliveryInfoDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.enums.StoreEnums;
import kr.co.pulmuone.v1.store.delivery.dto.ShippingPossibilityStoreDeliveryAreaDto;

class StoreDeliveryServiceTest extends CommonServiceTestBaseForJunit5{

	@Autowired
	private StoreDeliveryService storeDeliveryService;

	@Test
	void getShippingPossibilityStoreDeliveryAreaInfo_성공() throws Exception{
		Long ilGoodsId = 175L;
		String storeType = StoreEnums.StoreType.BRANCH.getCode();
		List<String> storeDeliveralbeItemType = new ArrayList<>();
		storeDeliveralbeItemType.add("STORE_DELIVERABLE_ITEM.FD");
		String receiverZipCode = "61015";
		String buildingCode = "2917012600106700000000001";

		ShippingPossibilityStoreDeliveryAreaDto shippingPossibilityStoreDeliveryAreaInfo = storeDeliveryService.getShippingPossibilityStoreDeliveryAreaInfo(storeType, storeDeliveralbeItemType,receiverZipCode,buildingCode);

		assertNotNull(shippingPossibilityStoreDeliveryAreaInfo);
	}

	@Test
	void getShippingPossibilityStoreDeliveryAreaInfo_조회결과없음() throws Exception{
		Long ilGoodsId = 0L;
		String storeType = StoreEnums.StoreType.BRANCH.getCode();
		List<String> storeDeliveralbeItemType = new ArrayList<>();
		storeDeliveralbeItemType.add("STORE_DELIVERABLE_ITEM.ALL");
		String receiverZipCode = "06302";
		String buildingCode = "88888888";

		ShippingPossibilityStoreDeliveryAreaDto shippingPossibilityStoreDeliveryAreaInfo = storeDeliveryService.getShippingPossibilityStoreDeliveryAreaInfo(storeType, storeDeliveralbeItemType,receiverZipCode,buildingCode);

		assertNull(shippingPossibilityStoreDeliveryAreaInfo);
	}


	@Test
	void getShippingPossibilityDeliveryArea_조회_성공() throws Exception {
		//given
		String zipCode = "06367";
		String buildingCode = "1168011500107240000001486";

		//when
		ShippingAddressPossibleDeliveryInfoDto result = storeDeliveryService.getShippingPossibilityDeliveryArea(zipCode, buildingCode);

		//then
		assertTrue(result.isStoreDelivery());
	}
}
