package kr.co.pulmuone.v1.policy.bbs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.policy.bbs.dto.PolicyBbsAuthDto;

@Service
public class PolicyBbsAuthBizImpl implements PolicyBbsAuthBiz {

    @Autowired
    private PolicyBbsAuthService policyBbsAuthService;
    /**
     * 게시판권한설정 조회
     *
     * @param PolicyBbsAuthDto
     * @return ApiResult
     * @throws 	Exception
     */
    @Override
    public ApiResult<?> getPolicyBbsAuthInfo(Long csCategoryId) {
    	return ApiResult.success(policyBbsAuthService.getPolicyBbsAuthInfo(csCategoryId));
    }
    /**
     * 게시판권한설정 목록 조회
     *
     * @param PolicyBbsAuthDto
     * @return ApiResult
     * @throws 	Exception
     */
    @Override
    public ApiResult<?> getPolicyBbsAuthList(PolicyBbsAuthDto dto) {
    	return ApiResult.success(policyBbsAuthService.getPolicyBbsAuthList(dto));
    }
    /**
	 * 게시판권한 설정 분류 코드 조회
	 * @param bbsTp
	 * @return ApiResult
	 * @throws Exception
	 */
    @Override
    public ApiResult<?> getPolicyBbsAuthCategoryList(String bbsTp) {
    	return ApiResult.success(policyBbsAuthService.getPolicyBbsAuthCategoryList(bbsTp));
    }
    /**
     * 게시판권한설정 신규 등록
     *
     * @param PolicyBbsAuthDto
     * @return ApiResult
     * @throws 	Exception
     */
    @Override
    public ApiResult<?> addPolicyBbsAuth(PolicyBbsAuthDto dto) {
    	policyBbsAuthService.addPolicyBbsAuth(dto);

        return ApiResult.success();
    }
    /**
     * 게시판권한설정 수정
     *
     * @param PolicyBbsAuthDto
     * @return ApiResult
     * @throws 	Exception
     */
    @Override
    public ApiResult<?> putPolicyBbsAuth(PolicyBbsAuthDto dto) {
    	policyBbsAuthService.putPolicyBbsAuth(dto);
    	return ApiResult.success();
    }
    /**
     * 게시판권한설정 삭제
     *
     * @param PolicyBbsAuthDto
     * @return ApiResult
     * @throws 	Exception
     */
    @Override
    public ApiResult<?> delPolicyBbsAuth(Long csBbsConfigId) {
    	policyBbsAuthService.delPolicyBbsAuth(csBbsConfigId);
    	return ApiResult.success();
    }
}


