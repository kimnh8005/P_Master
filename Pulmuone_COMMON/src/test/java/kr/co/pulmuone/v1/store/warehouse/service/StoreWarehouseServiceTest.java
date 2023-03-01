package kr.co.pulmuone.v1.store.warehouse.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.enums.WarehouseEnums.SetlYn;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StoreWarehouseServiceTest extends CommonServiceTestBaseForJunit5 {

	@Autowired
	StoreWarehouseService storeWarehouseService;

	@InjectMocks
	private StoreWarehouseService mockStoreWarehouseService;

	@Test
	void getWarehouseSetlYn_정상() throws Exception {

		SetlYn setlYnEnum = storeWarehouseService.getWarehouseSetlYn(0L);

		Assertions.assertTrue(setlYnEnum.NOT.equals(setlYnEnum));
	}
}
