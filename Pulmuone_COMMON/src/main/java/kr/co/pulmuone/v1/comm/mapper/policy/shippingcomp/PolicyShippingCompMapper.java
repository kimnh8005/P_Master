package kr.co.pulmuone.v1.comm.mapper.policy.shippingcomp;

import kr.co.pulmuone.v1.policy.shippingcomp.dto.PolicyShippingCompUseAllDto;
import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.policy.shippingcomp.dto.PolicyShippingCompDto;
import kr.co.pulmuone.v1.policy.shippingcomp.dto.PolicyShippingCompRequestDto;
import kr.co.pulmuone.v1.policy.shippingcomp.vo.PolicyShippingCompOutmallVo;
import kr.co.pulmuone.v1.policy.shippingcomp.vo.PolicyShippingCompVo;

import java.util.List;

@Mapper
public interface PolicyShippingCompMapper {

    PolicyShippingCompDto getPolicyShippingCompInfo(Long psShippingCompId);

    Page<PolicyShippingCompVo> getPolicyShippingCompList(PolicyShippingCompRequestDto dto);

	int addPolicyShippingComp(PolicyShippingCompRequestDto dto);

	int addPolicyShippingCompCode(PolicyShippingCompRequestDto dto);

	int addPolicyShippingCompOutmall(PolicyShippingCompOutmallVo dto);

	int putPolicyShippingComp(PolicyShippingCompRequestDto dto);

	int delPolicyShippingComp(PolicyShippingCompRequestDto dto);

	int delPolicyShippingCompCode(PolicyShippingCompRequestDto dto);

	int delPolicyShippingCompOutmall(PolicyShippingCompRequestDto dto);

	List<PolicyShippingCompUseAllDto> getPolicyShippingCompUseAllList();

	List<PolicyShippingCompUseAllDto> getDropDownPolicyShippingCompList();
}
