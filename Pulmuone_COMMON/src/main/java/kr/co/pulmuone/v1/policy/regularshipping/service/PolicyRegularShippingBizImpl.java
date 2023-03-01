package kr.co.pulmuone.v1.policy.regularshipping.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.policy.regularshipping.dto.PolicyRegularShippingDto;

@Service
public class PolicyRegularShippingBizImpl implements PolicyRegularShippingBiz {

    @Autowired
    private PolicyRegularShippingService policyRegularShippingService;

    /**
     * 정기배송 기본설정
     *
     * @param PolicyRegularShippingDto
     * @return PolicyRegularShippingDto
     * @throws	Exception
     */
    @Override
    public ApiResult<?> getPolicyRegularShipping(PolicyRegularShippingDto dto) {
    	PolicyRegularShippingDto rtnDto = policyRegularShippingService.getPolicyRegularShipping(dto);
    	if(rtnDto == null) {
    		rtnDto = new PolicyRegularShippingDto();
    		rtnDto.setRETURN_CODE("0000");
    		rtnDto.setRETURN_MSG("NOT_DEFINE_설정된 값이 없어 기본 세팅됩니다.");
    		rtnDto.setRegularShippingBasicDiscountRate(5);
    		rtnDto.setRegularShippingAdditionalDiscountApplicationTimes(3);
    		rtnDto.setRegularShippingAdditionalDiscountRate(5);
    		rtnDto.setRegularShippingMaxCustomerCycle(4);
    		rtnDto.setRegularShippingPaymentFailTerminate(1);
    	}
    	return ApiResult.success(rtnDto);
    }
    /**
     * 정기배송 기본설정 저장
     *
     * @param PolicyRegularShippingDto
     * @return int
     * @throws	Exception
     */
    @Override
    public ApiResult<?> putPolicyRegularShipping(PolicyRegularShippingDto dto) {
    	policyRegularShippingService.putPolicyRegularShipping(dto);
    	return ApiResult.success();
    }
}
