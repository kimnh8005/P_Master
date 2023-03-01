package kr.co.pulmuone.v1.policy.bbs.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;

import kr.co.pulmuone.v1.comm.mapper.policy.bbs.PolicyBbsMapper;
import kr.co.pulmuone.v1.policy.bbs.dto.PolicyBbsDivisionDto;
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
 *  1.0		20200916		박승현              최초작성
 *
 * =======================================================================
 * </PRE>
 *
 */

@Service
@RequiredArgsConstructor
public class PolicyBbsDivisionService {

	@Autowired
	private final PolicyBbsMapper policyBbsMapper;

    /**
     * 게시판분류설정 조회
     *
     * @param PolicyBbsDivisionVo
     * @return csCategoryId
     */
    protected PolicyBbsDivisionVo getPolicyBbsDivisionInfo(Long csCategoryId) {
    	return policyBbsMapper.getPolicyBbsDivisionInfo(csCategoryId);
    }
    /**
     * 게시판분류설정 목록 조회
     *
     * @param PolicyBbsDivisionDto
     * @return PolicyBbsDivisionDto
     */
    protected PolicyBbsDivisionDto getPolicyBbsDivisionList(PolicyBbsDivisionDto dto) {
    	PolicyBbsDivisionDto result = new PolicyBbsDivisionDto();
    	PageMethod.startPage(dto.getPage(), dto.getPageSize());
	    Page<PolicyBbsDivisionVo> rows = policyBbsMapper.getPolicyBbsDivisionList(dto);
        result.setTotal((int)rows.getTotal());
        result.setRows(rows.getResult());
        return result;
    }
    /**
     * 게시판분류 설정 상위 분류 코드 조회
     *
     * @param bbsTp
     * @return PolicyBbsDivisionDto
     */
    protected PolicyBbsDivisionDto getPolicyBbsDivisionParentCategoryList(String bbsTp) {
    	PolicyBbsDivisionDto result = new PolicyBbsDivisionDto();
		List<PolicyBbsDivisionVo> rows = policyBbsMapper.getPolicyBbsDivisionParentCategoryList(bbsTp);
		result.setRows(rows);
		return result;
    }
    /**
     * 게시판분류설정 신규 등록
     *
     * @param PolicyBbsDivisionDto
     * @return int
     */
    protected int addPolicyBbsDivision(PolicyBbsDivisionDto dto) {
		return policyBbsMapper.addPolicyBbsDivision(dto);
    }
    /**
     * 게시판분류설정 수정
     *
     * @param PolicyBbsDivisionDto
     * @return int
     */
    protected int putPolicyBbsDivision(PolicyBbsDivisionDto dto) {
		return policyBbsMapper.putPolicyBbsDivision(dto);
    }
    /**
     * 게시판분류설정 삭제
     *
     * @param PolicyBbsDivisionDto
     * @return int
     */
    protected int delPolicyBbsDivision(Long csCategoryId) {
    	return policyBbsMapper.delPolicyBbsDivision(csCategoryId);
    }
}
