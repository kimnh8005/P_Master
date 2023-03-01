package kr.co.pulmuone.v1.policy.claim.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingRun;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.policy.claim.dto.PolicyClaimBosRequestDto;
import kr.co.pulmuone.v1.policy.claim.dto.PolicyClaimBosResponseDto;
import kr.co.pulmuone.v1.policy.claim.dto.PolicyClaimCtgryRequestDto;
import kr.co.pulmuone.v1.policy.claim.dto.PolicyClaimMallRequestDto;
import kr.co.pulmuone.v1.policy.claim.dto.SavePolicyClaimBosRequestDto;
import kr.co.pulmuone.v1.policy.claim.dto.SavePolicyClaimMallRequestDto;
import kr.co.pulmuone.v1.policy.claim.dto.vo.PolicyClaimMallVo;
import kr.co.pulmuone.v1.policy.claim.dto.SavePsClaimCtgryRequestDto;


@Service
public class PolicyClaimBizImpl implements PolicyClaimBiz {

    @Autowired
    PolicyClaimService policyClaimService;

    /**
     * BOS 클레임 사유 목록
     *
     * @param PolicyClaimCtgryRequestDto
     * @return PolicyClaimCtgryResponseDto
     * @throws Exception
     */
    public ApiResult<?> getPsClaimCtgryList(PolicyClaimCtgryRequestDto policyClaimCtgryRequestDto) throws Exception {
    	return ApiResult.success(policyClaimService.getPsClaimCtgryList(policyClaimCtgryRequestDto));
    }

    /**
     * BOS 클레임 사유 목록(리스트박스 조회용)
     *
     * @param PolicyClaimCtgryRequestDto
     * @return PolicyClaimCtgryResponseDto
     * @throws Exception
     */
    public ApiResult<?> searchPsClaimCtgryList(PolicyClaimCtgryRequestDto policyClaimCtgryRequestDto) throws Exception {
    	return ApiResult.success(policyClaimService.searchPsClaimCtgryList(policyClaimCtgryRequestDto));
    }

    /**
     * BOS 클레임 사유 저장
     *
     * @param SavePsClaimCtgryRequestDto
     * @throws Exception
     */
    @Override
    public ApiResult<?> savePsClaimCtgry(SavePsClaimCtgryRequestDto dto) throws Exception {
        return policyClaimService.savePsClaimCtgry(dto);
    }

    /**
     * 쇼핑몰 클레임 사유 등록
     *
     * @param SavePolicyClaimMallRequestDto
     * @throws Exception
     */
    @Override
    public ApiResult<?> addPsClaimMall(SavePolicyClaimMallRequestDto savePolicyClaimMallRequestDto) throws Exception{
    	return policyClaimService.addPsClaimMall(savePolicyClaimMallRequestDto);
    }


    /**
     * 쇼핑몰 클레임 사유 수정
     *
     * @param SavePolicyClaimMallRequestDto
     * @throws Exception
     */
    @Override
    public ApiResult<?> putPsClaimMall(SavePolicyClaimMallRequestDto savePolicyClaimMallRequestDto) throws Exception{
    	return policyClaimService.putPsClaimMall(savePolicyClaimMallRequestDto);
    }

    /**
     * 쇼핑몰 클레임 사유 목록
     *
     * @param PolicyClaimMallVo
     * @throws Exception
     */
    @Override
    public ApiResult<?> getPolicyClaimMallList(PolicyClaimMallRequestDto dto) throws Exception{
    	return ApiResult.success(policyClaimService.getPsClaimMallList(dto));
    }


    /**
     * 쇼핑몰 클레임 사유 상세
     *
     * @param PolicyClaimMallVo
     * @throws Exception
     */
    @Override
    @UserMaskingRun(system = "BOS")
    public ApiResult<?> getPolicyClaimMall(PolicyClaimMallVo dto) throws Exception{
    	return ApiResult.success(policyClaimService.getPolicyClaimMall(dto));
    }

    /**
     * 쇼핑몰 클레임 사유 상세
     *
     * @param PolicyClaimMallVo
     * @throws Exception
     */
    @Override
    @UserMaskingRun(system = "MALL")
    public ApiResult<?> getFrontPolicyClaimMall(PolicyClaimMallVo dto) throws Exception{
        return ApiResult.success(policyClaimService.getPolicyClaimMall(dto));
    }


    /**
     * BOS 클레임 공급업체별 사유 목록
     *
     * @param urSupplierId
     * @Return
     * @throws Exception
     */
    @Override
    public ApiResult<?> getPsClaimSupplyCtgryList(String supplierCode) throws Exception{
    	return ApiResult.success(policyClaimService.getPsClaimSupplyCtgryList(supplierCode));
    }

	/**
     * BOS 클레임 사유 등록
     *
     * @param SavePolicyClaimBosRequestDto
     * @throws Exception
     */
	public ApiResult<?> addPsClaimBos(SavePolicyClaimBosRequestDto savePolicyClaimBosRequestDto) throws Exception{
		return policyClaimService.addPsClaimBos(savePolicyClaimBosRequestDto);
	}

	/**
     * BOS 클레임 사유 수정
     *
     * @param SavePolicyClaimBosRequestDto
     * @throws Exception
     */
    @Override
	public ApiResult<?> putPsClaimBos(SavePolicyClaimBosRequestDto savePolicyClaimBosRequestDto) throws Exception{
		return policyClaimService.putPsClaimBos(savePolicyClaimBosRequestDto);
	}

    /**
     * BOS 클레임 사유 목록
     *
     * @param PolicyClaimBosRequestDto
     * @throws Exception
     */
    @Override
    public ApiResult<?> getPolicyClaimBosList(PolicyClaimBosRequestDto dto) throws Exception{
    	return ApiResult.success(policyClaimService.getPolicyClaimBosList(dto));
    }

	/**
     * BOS 클레임 사유 상세
     *
     * @param PolicyClaimBosRequestDto
     * @throws Exception
     */
    @Override
	public ApiResult<?> getPolicyClaimBos(PolicyClaimBosRequestDto dto) throws Exception{
		return ApiResult.success(policyClaimService.getPolicyClaimBos(dto));
	}

	/**
     * BOS 클레임 사유 삭제
     *
     * @param PolicyClaimBosRequestDto
     * @throws Exception
     */
    @Override
	public ApiResult<?> delPolicyClaimBos(PolicyClaimBosRequestDto dto) throws Exception{
		return ApiResult.success(policyClaimService.delPolicyClaimBos(dto));
	}

	/**
     * BOS 클레임 등록 사유 목록(리스트박스 조회용)
     *
     * @param PolicyClaimCtgryRequestDto
     * @return PolicyClaimCtgryResponseDto
     * @throws Exception
     */
    @Override
	public ApiResult<?> searchPsClaimBosList(PolicyClaimCtgryRequestDto policyClaimCtgryRequestDto) throws Exception{
		return ApiResult.success(policyClaimService.searchPsClaimBosList(policyClaimCtgryRequestDto));
	}

	/**
     * BOS 클레임 사유 카테고리 조회 (주문 > 미출 주문상세리스트 > 일괄 취소완료 팝업에서 사용)
     *
     * @throws Exception
     */
    @Override
	public ApiResult<?> getPolicyClaimCtgryListForClaimPopup() throws Exception{
		return ApiResult.success(policyClaimService.getPolicyClaimCtgryListForClaimPopup());
	}

}
