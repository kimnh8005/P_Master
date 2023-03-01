package kr.co.pulmuone.v1.policy.bbs.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;

import kr.co.pulmuone.v1.comm.mapper.policy.bbs.PolicyBbsMapper;
import kr.co.pulmuone.v1.policy.bbs.dto.PolicyBbsAuthDto;
import kr.co.pulmuone.v1.policy.bbs.dto.PolicyBbsDivisionDto;
import kr.co.pulmuone.v1.policy.bbs.dto.vo.PolicyBbsAuthVo;
import kr.co.pulmuone.v1.policy.bbs.dto.vo.PolicyBbsDivisionVo;
import lombok.RequiredArgsConstructor;

/**
 * <PRE>
 * Forbiz Korea
 * Class의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0		20200922		박승현              최초작성
 *
 * =======================================================================
 * </PRE>
 *
 */

@Service
@RequiredArgsConstructor
public class PolicyBbsAuthService {

	@Autowired
	private final PolicyBbsMapper policyBbsMapper;

    /**
     * 게시판권한설정 조회
     *
     * @param csCategoryId
     * @return PolicyBbsAuthVo
     */
    protected PolicyBbsAuthVo getPolicyBbsAuthInfo(Long csBbsConfigId) {
    	return policyBbsMapper.getPolicyBbsAuthInfo(csBbsConfigId);
    }
    /**
     * 게시판권한설정 목록 조회
     *
     * @param PolicyBbsAuthDto
     * @return PolicyBbsAuthDto
     */
    protected PolicyBbsAuthDto getPolicyBbsAuthList(PolicyBbsAuthDto dto) {
    	PolicyBbsAuthDto result = new PolicyBbsAuthDto();
    	PageMethod.startPage(dto.getPage(), dto.getPageSize());
	    Page<PolicyBbsAuthVo> rows = policyBbsMapper.getPolicyBbsAuthList(dto);
        result.setTotal((int)rows.getTotal());
        result.setRows(rows.getResult());
        return result;
    }
    /**
	 * 게시판권한 설정 분류 코드 조회
	 * @param bbsTp
	 * @return ApiResult
	 * @throws Exception
	 */
    protected PolicyBbsDivisionDto getPolicyBbsAuthCategoryList(String bbsTp) {
    	PolicyBbsDivisionDto result = new PolicyBbsDivisionDto();
		List<PolicyBbsDivisionVo> rows = policyBbsMapper.getPolicyBbsAuthCategoryList(bbsTp);
		result.setRows(rows);
		return result;
    }
    /**
     * 게시판권한설정 신규 등록
     *
     * @param PolicyBbsAuthDto
     * @return int
     */
    protected int addPolicyBbsAuth(PolicyBbsAuthDto dto) {
		return policyBbsMapper.addPolicyBbsAuth(dto);
    }
    /**
     * 게시판권한설정 수정
     *
     * @param PolicyBbsAuthDto
     * @return int
     */
    protected int putPolicyBbsAuth(PolicyBbsAuthDto dto) {
		return policyBbsMapper.putPolicyBbsAuth(dto);
    }
    /**
     * 게시판권한설정 삭제
     *
     * @param PolicyBbsAuthDto
     * @return int
     */
    protected int delPolicyBbsAuth(Long csBbsConfigId) {
    	return policyBbsMapper.delPolicyBbsAuth(csBbsConfigId);
    }
}
