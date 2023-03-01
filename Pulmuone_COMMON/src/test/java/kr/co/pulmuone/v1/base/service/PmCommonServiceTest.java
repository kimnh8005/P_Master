package kr.co.pulmuone.v1.base.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class PmCommonServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    PmCommonService pmCommonService;

    @Test
    void 포인트_리스트_조회() {
        ApiResult<?> apiResult = pmCommonService.getPmPointList();

        assertTrue(BaseEnums.Default.SUCCESS.getCode() == apiResult.getCode());
    }

    @Test
    void 쿠폰_리스트_조회() {
        ApiResult<?> apiResult = pmCommonService.getPmCpnList();

        assertTrue(BaseEnums.Default.SUCCESS.getCode() == apiResult.getCode());
    }
}