package kr.co.pulmuone.v1.policy.benefit.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.policy.benefit.dto.PolicyBenefitEmployeeBrandGroupSaveDto;
import kr.co.pulmuone.v1.policy.benefit.dto.PolicyBenefitEmployeeGroupSaveDto;
import kr.co.pulmuone.v1.policy.benefit.dto.PolicyBenefitEmployeePastInfoByUserResponseDto;
import kr.co.pulmuone.v1.policy.benefit.dto.vo.PolicyBenefitEmployeeBrandByUserVo;
import kr.co.pulmuone.v1.policy.benefit.dto.vo.PolicyBenefitEmployeeBrandGroupByUserVo;
import kr.co.pulmuone.v1.policy.benefit.dto.vo.PolicyBenefitEmployeeByUserVo;

import java.util.List;

public interface PolicyBenefitEmployeeBiz {

	ApiResult<?> getPolicyBenefitEmployeeGroupList();

	ApiResult<?> getLastModifyDatePolicyBenefitEmployeeGroup();

	ApiResult<?> putPolicyBenefitEmployeeGroup(PolicyBenefitEmployeeGroupSaveDto dto) throws Exception;

	ApiResult<?> getPolicyBenefitEmployeeBrandGroupList(String searchType);

	ApiResult<?> getLastModifyDatePolicyBenefitEmployeeBrandGroup();

	ApiResult<?> getRegistDiscMasterPolicyBenefitEmployeeBrandGroup(String psEmplDiscBrandGrpId);

	ApiResult<?> putPolicyBenefitEmployeeBrandGroup(PolicyBenefitEmployeeBrandGroupSaveDto dto) throws Exception;

	ApiResult<?> delPolicyBenefitEmployeeBrandGroup(String psEmplDiscBrandGrpId) throws Exception;

	List<PolicyBenefitEmployeeByUserVo> getEmployeeDiscountBrandByUser(String urErpEmployeeCd) throws Exception;

	PolicyBenefitEmployeePastInfoByUserResponseDto getEmployeeDiscountPastByUser(String urErpEmployeeCd, String startDate, String endDate) throws Exception;

	PolicyBenefitEmployeeByUserVo findEmployeeDiscountBrandByUser(List<PolicyBenefitEmployeeByUserVo> list, Long urBrandId);

	PolicyBenefitEmployeeBrandGroupByUserVo getDiscountRatioEmployeeDiscountBrand(PolicyBenefitEmployeeByUserVo policyBenefitEmployeeByUserVo, Long urBrandId);
}
