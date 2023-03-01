package kr.co.pulmuone.v1.policy.config.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.enums.PolicyEnums;
import kr.co.pulmuone.v1.comm.mapper.policy.config.PolicyConfigMapper;
import kr.co.pulmuone.v1.goods.goods.dto.RegularShippingConfigDto;
import kr.co.pulmuone.v1.policy.config.dto.vo.MetaConfigVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.booleanThat;
import static org.mockito.BDDMockito.given;

@Slf4j
class PolicyConfigServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    PolicyConfigService policyConfigService;

    @InjectMocks
    PolicyConfigService mockPolicyConfigService;

    @Mock
    PolicyConfigMapper mockPolicyConfigMapper;

    @BeforeEach
    void setup() {
        mockPolicyConfigService = new PolicyConfigService(mockPolicyConfigMapper);
    }

    @Test
    void getRegularShippingConfig_성공() throws Exception {

        RegularShippingConfigDto regularShippingConfig = policyConfigService.getRegularShippingConfig();

        assertNotNull(regularShippingConfig);
    }

    @Test
    void getConfigValue_조회_성공() throws Exception {
        //given
        String psKey = PolicyEnums.PolicyKey.OD_CART_MAINTENANCE_PERIOD.getCode();

        //when
        String result = policyConfigService.getConfigValue(psKey);

        //then
        assertTrue(result.length() > 0);
    }

    @Test
    void getMetaConfig_조회_성공() throws Exception {
        //given, when
        List<MetaConfigVo> result = policyConfigService.getMetaConfig();

        //then
        assertTrue(result.size() > 0);
    }

    @Test
    void 배송권역_정책키_날짜가_오늘날짜를_지났을때() throws Exception {

        given(mockPolicyConfigMapper.getConfigValue(any())).willReturn("20211013114900");

        // when
        boolean isApplyDeliveryAreaPolicy = mockPolicyConfigService.isApplyDeliveryAreaPolicy();

        // then
        assertTrue(isApplyDeliveryAreaPolicy);
    }

    @Test
    void 배송권역_정책키_날짜가_오늘날짜를_지나지않을때() throws Exception {

        given(mockPolicyConfigMapper.getConfigValue(any())).willReturn("90211012235959");

        // when
        boolean isApplyDeliveryAreaPolicy = mockPolicyConfigService.isApplyDeliveryAreaPolicy();

        // then
        assertFalse(isApplyDeliveryAreaPolicy);
    }
}
