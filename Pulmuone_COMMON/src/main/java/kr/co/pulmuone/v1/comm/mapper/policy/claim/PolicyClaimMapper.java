package kr.co.pulmuone.v1.comm.mapper.policy.claim;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.policy.claim.dto.PolicyClaimBosRequestDto;
import kr.co.pulmuone.v1.policy.claim.dto.PolicyClaimCtgryRequestDto;
import kr.co.pulmuone.v1.policy.claim.dto.PolicyClaimMallRequestDto;
import kr.co.pulmuone.v1.policy.claim.dto.SavePolicyClaimBosRequestDto;
import kr.co.pulmuone.v1.policy.claim.dto.vo.PolicyClaimMallVo;
import kr.co.pulmuone.v1.policy.claim.dto.vo.PolicyClaimSupplyCtgryVo;
import kr.co.pulmuone.v1.policy.claim.dto.SavePolicyClaimCtgryRequestSaveDto;
import kr.co.pulmuone.v1.policy.claim.dto.SavePolicyClaimMallRequestDto;
import kr.co.pulmuone.v1.policy.claim.dto.vo.PolicyClaimBosSupplyVo;
import kr.co.pulmuone.v1.policy.claim.dto.vo.PolicyClaimBosVo;
import kr.co.pulmuone.v1.policy.claim.dto.vo.PolicyClaimCtgryVo;

@Mapper
public interface PolicyClaimMapper {

	Page<PolicyClaimCtgryVo> getPsClaimCtgryList(PolicyClaimCtgryRequestDto policyClaimCtgryRequestDto) throws Exception;

	int checkClaimNameDuplicate(@Param("claimList")List<SavePolicyClaimCtgryRequestSaveDto> list, @Param("categoryCode")String categoryCode, @Param("psClaimCtgryId")Long psClaimCtgryId);

	int addPsClaimCtgry(@Param("claimList")List<SavePolicyClaimCtgryRequestSaveDto> list, @Param("categoryCode")String categoryCode);

	int putPsClaimCtgry(@Param("claimList")List<SavePolicyClaimCtgryRequestSaveDto> list, @Param("categoryCode")String categoryCode);

	int delPsClaimCtgry(@Param("claimList")List<SavePolicyClaimCtgryRequestSaveDto> list, @Param("categoryCode")String categoryCode);

	int addPsClaimMall(SavePolicyClaimMallRequestDto savePolicyClaimMallRequestDto) throws Exception;

	int putPsClaimMall(SavePolicyClaimMallRequestDto savePolicyClaimMallRequestDto) throws Exception;

	int checkPsClaimMallDuplicate(PolicyClaimMallRequestDto dto);

	Page<PolicyClaimMallVo> getPsClaimMallList(PolicyClaimMallRequestDto dto) throws Exception;

	PolicyClaimMallVo getPolicyClaimMall(PolicyClaimMallVo dto) throws Exception;

	List<PolicyClaimSupplyCtgryVo> getPsClaimSupplyCtgryList(String supplierCode) throws Exception;

	int addPsClaimBos(SavePolicyClaimBosRequestDto savePolicyClaimBosRequestDto) throws Exception;

	int addPsClaimBosSupply(PolicyClaimBosSupplyVo vo) throws Exception;

	int checkPsClaimBosDuplicate(PolicyClaimBosRequestDto vo);

	Page<PolicyClaimBosVo> getPolicyClaimBosList(PolicyClaimBosRequestDto dto) throws Exception;

	List<PolicyClaimBosSupplyVo> getPolicyClaimSupplyList(Long psClaimBosId);

	PolicyClaimBosVo getPolicyClaimBos(PolicyClaimBosRequestDto dto) throws Exception;

	int putPsClaimBos(SavePolicyClaimBosRequestDto vo) throws Exception;

	int delPsClaimBosSupply(SavePolicyClaimBosRequestDto vo) throws Exception;

	int delPolicyClaimBos(PolicyClaimBosRequestDto dto) throws Exception;

	List<PolicyClaimCtgryVo> searchPsClaimBosLCtgryList(PolicyClaimCtgryRequestDto dto) throws Exception;

	List<PolicyClaimCtgryVo> searchPsClaimBosMCtgryList(PolicyClaimCtgryRequestDto dto) throws Exception;

	List<PolicyClaimCtgryVo> searchPsClaimBosSCtgryList(PolicyClaimCtgryRequestDto dto) throws Exception;

	List<PolicyClaimCtgryVo> getPolicyClaimCtgryListForClaimPopup() throws Exception;
}


