package kr.co.pulmuone.v1.policy.bbs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.policy.bbs.dto.PolicyBbsDivisionDto;

@Service
public class PolicyBbsDivisionBizImpl implements PolicyBbsDivisionBiz {

    @Autowired
    private PolicyBbsDivisionService policyBbsDivisionService;
    /**
     * 게시판분류설정 조회
     *
     * @param csCategoryId
     * @return ApiResult
     * @throws 	Exception
     */
    @Override
    public ApiResult<?> getPolicyBbsDivisionInfo(Long csCategoryId) {
    	return ApiResult.success(policyBbsDivisionService.getPolicyBbsDivisionInfo(csCategoryId));
    }
    /**
     * 게시판분류설정 목록 조회
     *
     * @param PolicyBbsDivisionDto
     * @return ApiResult
     * @throws 	Exception
     */
    @Override
    public ApiResult<?> getPolicyBbsDivisionList(PolicyBbsDivisionDto dto) {
    	return ApiResult.success(policyBbsDivisionService.getPolicyBbsDivisionList(dto));
    }
    /**
     * 게시판분류 설정 상위 분류 코드 조회
     *
     * @param PolicyBbsDivisionDto
     * @return ApiResult
     * @throws 	Exception
     */
    @Override
    public ApiResult<?> getPolicyBbsDivisionParentCategoryList(String bbsTp) {
    	return ApiResult.success(policyBbsDivisionService.getPolicyBbsDivisionParentCategoryList(bbsTp));
    }
    /**
     * 게시판분류설정 신규 등록
     *
     * @param PolicyBbsDivisionDto
     * @return ApiResult
     * @throws 	Exception
     */
    @Override
    public ApiResult<?> addPolicyBbsDivision(PolicyBbsDivisionDto dto) {
    	policyBbsDivisionService.addPolicyBbsDivision(dto);

        return ApiResult.success();
    }
    /**
     * 게시판분류설정 수정
     *
     * @param PolicyBbsDivisionDto
     * @return ApiResult
     * @throws 	Exception
     */
    @Override
    public ApiResult<?> putPolicyBbsDivision(PolicyBbsDivisionDto dto) {
    	policyBbsDivisionService.putPolicyBbsDivision(dto);
    	return ApiResult.success();
    }
    /**
     * 게시판분류설정 삭제
     *
     * @param PolicyBbsDivisionDto
     * @return ApiResult
     * @throws 	Exception
     */
    @Override
    public ApiResult<?> delPolicyBbsDivision(Long csCategoryId) {
    	policyBbsDivisionService.delPolicyBbsDivision(csCategoryId);
    	return ApiResult.success();
    }
}


