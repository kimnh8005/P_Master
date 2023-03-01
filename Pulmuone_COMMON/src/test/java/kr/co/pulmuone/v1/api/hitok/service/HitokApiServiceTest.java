package kr.co.pulmuone.v1.api.hitok.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class HitokApiServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    private HitokApiService hitokApiService;

    @Test
    void 하이톡_일일배송_반품_API_송신_성공() throws BaseException {

        long odClaimId = 0; // 하이톡 일일배송 반품 클레임 아이디
        boolean result = hitokApiService.sendApiHitokDailyReturnOrder(odClaimId);

        assertTrue(result);

    }

    @Test
    void 하이톡_일일배송_반품_API_송신_실패() throws BaseException {

        long odClaimId = 0;
        boolean result = hitokApiService.sendApiHitokDailyReturnOrder(odClaimId);

        assertFalse(!result);

    }

    @Test
    void 하이톡_일일배송_재배송_API_송신_성공() throws BaseException {

        long odClaimId = 0; // 하이톡 일일배송 재배송 클레임 아이디
        boolean result = hitokApiService.sendApiHitokDailyRedeliveryOrder(odClaimId);

        assertTrue(result);

    }

    @Test
    void 하이톡_일일배송_재배송_API_송신_실패() throws BaseException {

        long odClaimId = 0; // 하이톡 일일배송 재배송 클레임 아이디
        boolean result = hitokApiService.sendApiHitokDailyRedeliveryOrder(odClaimId);

        assertFalse(!result);

    }

}
