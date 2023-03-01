package kr.co.pulmuone.v1.policy.shippingcomp.service;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.policy.shippingcomp.dto.PolicyShippingCompRequestDto;

@Service
public class PolicyShippingCompBizImpl implements PolicyShippingCompBiz {

    @Autowired
    private PolicyShippingCompService policyShippingCompService;
    /**
     * 택배사 설정 조회
     *
     * @param psShippingCompId
     * @throws 	Exception
     */
    @Override
    public ApiResult<?> getPolicyShippingCompInfo(Long psShippingCompId) {
    	return ApiResult.success(policyShippingCompService.getPolicyShippingCompInfo(psShippingCompId));
    }
    /**
     * 택배사 설정 목록 조회
     *
     * @param dto
     * @throws 	Exception
     */
    @Override
    public ApiResult<?> getPolicyShippingCompList(PolicyShippingCompRequestDto dto) {
    	return ApiResult.success(policyShippingCompService.getPolicyShippingCompList(dto));
    }
    /**
     * 택배사 설정 신규 등록
     *
     * @param dto
     * @throws 	Exception
     */
    @Override
    public ApiResult<?> addPolicyShippingComp(PolicyShippingCompRequestDto dto) {
    	policyShippingCompService.addPolicyShippingComp(dto);

    	// 택배사 코드 추가
    	if(CollectionUtils.isNotEmpty(dto.getShippingCompCodeList())) {
    		policyShippingCompService.addPolicyShippingCompCode(dto);
    	}

    	// 외부몰  택배사 코드 추가
    	policyShippingCompService.addPolicyShippingCompOutmall(dto);

        return ApiResult.success();
    }
    /**
     * 택배사 설정 수정
     *
     * @param dto
     * @throws 	Exception
     */
    @Override
    public ApiResult<?> putPolicyShippingComp(PolicyShippingCompRequestDto dto) {
    	policyShippingCompService.putPolicyShippingComp(dto);
    	policyShippingCompService.delPolicyShippingCompCode(dto);
    	policyShippingCompService.delPolicyShippingCompOutmall(dto);

    	if(CollectionUtils.isNotEmpty(dto.getShippingCompCodeList())) {
    		policyShippingCompService.addPolicyShippingCompCode(dto);
    	}

    	// 외부몰  택배사 코드 추가
    	policyShippingCompService.addPolicyShippingCompOutmall(dto);
    	return ApiResult.success();
    }
    /**
     * 택배사 설정 삭제
     *
     * @param dto
     * @throws 	Exception
     */
    @Override
    public ApiResult<?> delPolicyShippingComp(PolicyShippingCompRequestDto dto) {
    	policyShippingCompService.delPolicyShippingComp(dto);
    	policyShippingCompService.delPolicyShippingCompCode(dto);
    	policyShippingCompService.delPolicyShippingCompOutmall(dto);
    	return ApiResult.success();
    }

    @Override
    public ApiResult<?> getPolicyShippingCompUseAllList() {
        return ApiResult.success(policyShippingCompService.getPolicyShippingCompUseAllList());
    }

    /**
     * 택배사 목록 조회
     * @return ApiResult<?>
     * @throws
     */
    @Override
    public ApiResult<?> getDropDownPolicyShippingCompList() {
        return ApiResult.success(policyShippingCompService.getDropDownPolicyShippingCompList());
    }
}


