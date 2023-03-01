package kr.co.pulmuone.v1.policy.claim.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.policy.claim.dto.PolicyClaimBosRequestDto;
import kr.co.pulmuone.v1.policy.claim.dto.PolicyClaimCtgryRequestDto;
import kr.co.pulmuone.v1.policy.claim.dto.PolicyClaimMallRequestDto;
import kr.co.pulmuone.v1.policy.claim.dto.SavePolicyClaimBosRequestDto;
import kr.co.pulmuone.v1.policy.claim.dto.SavePolicyClaimMallRequestDto;
import kr.co.pulmuone.v1.policy.claim.dto.vo.PolicyClaimMallVo;
import kr.co.pulmuone.v1.policy.claim.dto.SavePsClaimCtgryRequestDto;

public interface PolicyClaimBiz {

    ApiResult<?> getPsClaimCtgryList(PolicyClaimCtgryRequestDto policyClaimCtgryRequestDto) throws Exception;

    ApiResult<?> searchPsClaimCtgryList(PolicyClaimCtgryRequestDto policyClaimCtgryRequestDto) throws Exception;

    ApiResult<?> savePsClaimCtgry(SavePsClaimCtgryRequestDto dto) throws Exception;

    ApiResult<?> addPsClaimMall(SavePolicyClaimMallRequestDto savePolicyClaimMallRequestDto) throws Exception;

    ApiResult<?> putPsClaimMall(SavePolicyClaimMallRequestDto savePolicyClaimMallRequestDto) throws Exception;

    ApiResult<?> getPolicyClaimMallList(PolicyClaimMallRequestDto dto) throws Exception;

    ApiResult<?> getPolicyClaimMall(PolicyClaimMallVo dto) throws Exception;

    ApiResult<?> getFrontPolicyClaimMall(PolicyClaimMallVo dto) throws Exception;

    ApiResult<?> getPsClaimSupplyCtgryList(String supplierCode) throws Exception;

    ApiResult<?> addPsClaimBos(SavePolicyClaimBosRequestDto savePolicyClaimBosRequestDto) throws Exception;

    ApiResult<?> getPolicyClaimBosList(PolicyClaimBosRequestDto dto) throws Exception;

    ApiResult<?> getPolicyClaimBos(PolicyClaimBosRequestDto dto) throws Exception;

    ApiResult<?> putPsClaimBos(SavePolicyClaimBosRequestDto savePolicyClaimBosRequestDto) throws Exception;

    ApiResult<?> delPolicyClaimBos(PolicyClaimBosRequestDto dto) throws Exception;

    ApiResult<?> searchPsClaimBosList(PolicyClaimCtgryRequestDto policyClaimCtgryRequestDto) throws Exception;

    ApiResult<?> getPolicyClaimCtgryListForClaimPopup() throws Exception;
}




