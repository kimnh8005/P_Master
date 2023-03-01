package kr.co.pulmuone.v1.comm.mapper.policy.regularshipping;

import org.apache.ibatis.annotations.Mapper;

import kr.co.pulmuone.v1.policy.regularshipping.dto.PolicyRegularShippingDto;

@Mapper
public interface PolicyRegularShippingMapper {

	PolicyRegularShippingDto getPolicyRegularShipping(PolicyRegularShippingDto dto);
	int addPolicyRegularShipping(PolicyRegularShippingDto dto);
	int putPolicyRegularShipping(PolicyRegularShippingDto dto);

}
