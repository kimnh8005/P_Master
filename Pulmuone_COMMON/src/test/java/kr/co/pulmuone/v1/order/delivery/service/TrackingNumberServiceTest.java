package kr.co.pulmuone.v1.order.delivery.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class TrackingNumberServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    private TrackingNumberService trackingNumberService;

    @Test
    void 취소요청에_대한_클레임_정보_있으면_취소거부로_업데이트() throws Exception {

        String odid = "21011600001016";
        long odClaimId = 1;

        trackingNumberService.putCancelRequestClaimDenial(odid, odClaimId);

    }
}
