package kr.co.pulmuone.v1.api.storedelivery.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class StoreDeliveryApiServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    private StoreDeliveryApiService storeDeliveryApiService;

    @Test
    void 매장배송_취소_API_송신() throws BaseException {

        long odOrderId = 0;
        long odClaimId = 0;
        boolean result = storeDeliveryApiService.addStoreDeliveryCancelApiSend(odOrderId, odClaimId);

        assertTrue(result);

    }

    @Test
    void 매장배송_반품_API_송신() throws BaseException {

        long odOrderId = 0;
        long odClaimId = 0;
        boolean result = storeDeliveryApiService.addStoreDeliveryReturnApiSend(odOrderId, odClaimId);

        assertTrue(result);

    }

    @Test
    void 매장배송_재배송_API_송신() throws BaseException {

        long odOrderId = 0;
        long odClaimId = 0;
        boolean result = storeDeliveryApiService.addStoreDeliveryRedeliveryApiSend(odOrderId, odClaimId);

        assertTrue(result);

    }
}
