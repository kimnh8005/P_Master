package kr.co.pulmuone.v1.comm.mapper.policy.benefit;

import java.util.List;

import kr.co.pulmuone.v1.policy.benefit.dto.vo.*;
import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.Param;

@Mapper
public interface PolicyBenefitEmployeeMapper {

	List<PolicyBenefitEmployeeVo> getPolicyBenefitEmployeeMasterList();
	List<PolicyBenefitEmployeeGroupVo> getPolicyBenefitEmployeeGroupList(PolicyBenefitEmployeeVo vo);
	List<PolicyBenefitEmployeeGroupBrandGroupVo> getPolicyBenefitEmployeeGroupBrandGroupList(PolicyBenefitEmployeeGroupVo vo);
	List<PolicyBenefitEmployeeLegalVo> getPolicyBenefitEmployeeLegalList(PolicyBenefitEmployeeVo vo);
	PolicyBenefitEmployeeVo getLatestUpdatePolicyBenefitEmployee();

	int addPolicyBenefitEmployeeMaster(PolicyBenefitEmployeeVo vo);
	int putPolicyBenefitEmployeeMaster(List<PolicyBenefitEmployeeVo> voList);
	int delPolicyBenefitEmployeeMaster(String psEmplDiscMasterId);

	int addPolicyBenefitEmployeeGroup(PolicyBenefitEmployeeGroupVo vo);
	int putPolicyBenefitEmployeeGroup(PolicyBenefitEmployeeGroupVo vo);
	int delPolicyBenefitEmployeeGroup(String psEmplDiscMasterId);
	int delPolicyBenefitEmployeeGroupByPsEmplDiscGrpId(@Param("psEmplDiscGrpIds") List<String> psEmplDiscGrpIds);

	int addPolicyBenefitEmployeeGroupBrandGroup(PolicyBenefitEmployeeGroupVo vo);
	int delPolicyBenefitEmployeeGroupBrandGroup(List<PolicyBenefitEmployeeGroupVo> voList);

	int addPolicyBenefitEmployeeLegal(PolicyBenefitEmployeeVo vo);
	int delPolicyBenefitEmployeeLegal(String psEmplDiscMasterId);

	List<PolicyBenefitEmployeeBrandGroupVo> getPolicyBenefitEmployeeBrandGroupList();
	List<PolicyBenefitEmployeeBrandGroupBrandVo> getPolicyBenefitEmployeeBrandGroupBrandList(String psEmplDiscBrandGrpId);
	List<PolicyBenefitEmployeeBrandGroupBrandVo> getNonePolicyBenefitEmployeeBrandGroupBrandList();
	PolicyBenefitEmployeeBrandGroupVo getLatestUpdatePolicyBenefitEmployeeBrandGroup();
	PolicyBenefitEmployeeBrandGroupVo getRegistDiscMasterPolicyBenefitEmployeeBrandGroup(String psEmplDiscBrandGrpId);

	int addPolicyBenefitEmployeeBrandGroup(PolicyBenefitEmployeeBrandGroupVo vo);
	int putPolicyBenefitEmployeeBrandGroup(List<PolicyBenefitEmployeeBrandGroupVo> voList);
	int delPolicyBenefitEmployeeBrandGroup(String psEmplDiscBrandGrpId);
	int addPolicyBenefitEmployeeBrandGroupBrand(PolicyBenefitEmployeeBrandGroupVo vo);
	int delPolicyBenefitEmployeeBrandGroupBrand(PolicyBenefitEmployeeBrandGroupVo vo);

	PolicyBenefitEmployeeBrandGroupBrandVo getPolicyBenefitEmployeeDiscountRatio(String urBrandId);

	List<PolicyBenefitEmployeeByUserVo> getEmployeeDiscountByUser(@Param("urErpEmployeeCd") String urErpEmployeeCd) throws Exception;

	List<PolicyBenefitEmployeeBrandGroupByUserVo> getEmployeeDiscountBrandGroupByUser(Long psEmplDiscGrpId) throws Exception;

	List<PolicyBenefitEmployeeBrandByUserVo> getEmployeeDiscountBrandByUser(Long psEmplDiscBrandGrpId) throws Exception;

	List<PolicyBenefitEmployeePastInfoByUserVo> getEmployeeDiscountPastByUser(@Param("urErpEmployeeCd") String urErpEmployeeCd, @Param("startDate") String startDate, @Param("endDate") String endDate) throws Exception;

}
