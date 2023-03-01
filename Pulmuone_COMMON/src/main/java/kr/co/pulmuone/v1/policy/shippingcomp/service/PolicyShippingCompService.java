package kr.co.pulmuone.v1.policy.shippingcomp.service;

import java.util.List;

import kr.co.pulmuone.v1.policy.shippingcomp.dto.PolicyShippingCompUseAllDto;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import kr.co.pulmuone.v1.comm.enums.PolicyEnums;
import kr.co.pulmuone.v1.comm.mapper.policy.shippingcomp.PolicyShippingCompMapper;
import kr.co.pulmuone.v1.policy.shippingcomp.dto.PolicyShippingCompDto;
import kr.co.pulmuone.v1.policy.shippingcomp.dto.PolicyShippingCompRequestDto;
import kr.co.pulmuone.v1.policy.shippingcomp.vo.PolicyShippingCompOutmallVo;
import kr.co.pulmuone.v1.policy.shippingcomp.vo.PolicyShippingCompVo;
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
 *  버전	:	작성일	:  작성자		:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		20200908	박승현		최초작성
 *  1.1		20200908	박승현		shipping -> shippingComp 로 변경(테이블명 PS_SHIPPING > PS_SHIPPING_COMP 변경)
 *  1.2		20200914	박승현		코드 신규 개발 가이드 적용
 * =======================================================================
 * </PRE>
 */

@Service
@RequiredArgsConstructor
public class PolicyShippingCompService {

	@Autowired
    private final PolicyShippingCompMapper policyShippingCompMapper;

    /**
     * 택배사 설정 조회
     *
     * @param PolicyShippingCompRequestDto
     * @return PolicyShippingCompDto
     */
    protected PolicyShippingCompDto getPolicyShippingCompInfo(Long psShippingCompId) {
    	return policyShippingCompMapper.getPolicyShippingCompInfo(psShippingCompId);
    }

    /**
     * 택배사 설정 목록 조회
     *
     * @param PolicyShippingCompRequestDto
     * @return PolicyShippingCompDto
     */
    protected PolicyShippingCompDto getPolicyShippingCompList(PolicyShippingCompRequestDto dto) {
    	PolicyShippingCompDto result = new PolicyShippingCompDto();
    	PageHelper.startPage(dto.getPage(), dto.getPageSize());
	    Page<PolicyShippingCompVo> rows = policyShippingCompMapper.getPolicyShippingCompList(dto);
        result.setTotal((int)rows.getTotal());
        result.setRows(rows.getResult());
        return result;
    }

    /**
     * 택배사 설정 신규 등록
     *
     * @param PolicyShippingCompRequestDto
     * @return int
     */
    protected int addPolicyShippingComp(PolicyShippingCompRequestDto dto) {
		return policyShippingCompMapper.addPolicyShippingComp(dto);
    }

    /**
     * 택배사 설정 수정
     *
     * @param PolicyShippingCompRequestDto
     * @return int
     */
    protected int putPolicyShippingComp(PolicyShippingCompRequestDto dto) {
		return policyShippingCompMapper.putPolicyShippingComp(dto);
    }

    /**
     * 택배사 설정 삭제
     *
     * @param PolicyShippingCompRequestDto
     * @return int
     */
    protected int delPolicyShippingComp(PolicyShippingCompRequestDto dto) {
    	return policyShippingCompMapper.delPolicyShippingComp(dto);
    }

    /**
     * 택배사 코드 삭제
     *
     * @param PolicyShippingCompRequestDto
     * @return int
     */
    protected int delPolicyShippingCompCode(PolicyShippingCompRequestDto dto) {
    	return policyShippingCompMapper.delPolicyShippingCompCode(dto);
    }

    /**
     * 외부몰 택배사 코드 삭제
     *
     * @param PolicyShippingCompRequestDto
     * @return int
     */
    protected int delPolicyShippingCompOutmall(PolicyShippingCompRequestDto dto) {
    	return policyShippingCompMapper.delPolicyShippingCompOutmall(dto);
    }



    /**
     * 사용중인 택배사 리스트 조회
     *
     * @return PolicyShippingCompDto
     */
    protected PolicyShippingCompUseAllDto getPolicyShippingCompUseAllList() {
        PolicyShippingCompUseAllDto result = new PolicyShippingCompUseAllDto();
    	List<PolicyShippingCompUseAllDto> compVoList = policyShippingCompMapper.getPolicyShippingCompUseAllList();
    	result.setRows(compVoList);
        return result;
    }

    /**
     * 사용중인 택배사 리스트 조회
     *
     * @return PolicyShippingCompDto
     */
    protected PolicyShippingCompUseAllDto getDropDownPolicyShippingCompList() {
        PolicyShippingCompUseAllDto result = new PolicyShippingCompUseAllDto();
        List<PolicyShippingCompUseAllDto> compVoList = policyShippingCompMapper.getDropDownPolicyShippingCompList();
        result.setRows(compVoList);
        return result;
    }

    /**
     * 택배사 설정. 택배사 코드 등록
     *
     * @param PolicyShippingCompRequestDto
     * @return int
     */
    protected int addPolicyShippingCompCode(PolicyShippingCompRequestDto dto) {
    	return policyShippingCompMapper.addPolicyShippingCompCode(dto);
    }

    /**
     * 택배사 설정. 외부몰 코드 등록
     *
     * @param PolicyShippingCompRequestDto
     * @return int
     */
    protected int addPolicyShippingCompOutmall(PolicyShippingCompRequestDto dto) {

    	// 외부몰 이지어드민 택배사 정보 입력
    	if(StringUtils.isNotEmpty(dto.getEzadminShippingCompCd()) && StringUtils.isNotEmpty(dto.getEzadminShippingCompNm())) {
    		PolicyShippingCompOutmallVo ezadminVo = new PolicyShippingCompOutmallVo();
    		ezadminVo.setPsShippingCompId(dto.getPsShippingCompId());
    		ezadminVo.setOutmallCode(PolicyEnums.Outmallcode.E.getCode());
    		ezadminVo.setOutmallShippingCompCode(dto.getEzadminShippingCompCd());
    		ezadminVo.setOutmallShippingCompName(dto.getEzadminShippingCompNm());
    		policyShippingCompMapper.addPolicyShippingCompOutmall(ezadminVo);
    	}

    	// 외부몰 사방넷 택배사 정보 입력
    	if(StringUtils.isNotEmpty(dto.getSabangnetShippingCompCd())) {
    		PolicyShippingCompOutmallVo sabangnetVo = new PolicyShippingCompOutmallVo();
    		sabangnetVo.setPsShippingCompId(dto.getPsShippingCompId());
    		sabangnetVo.setOutmallCode(PolicyEnums.Outmallcode.S.getCode());
    		sabangnetVo.setOutmallShippingCompCode(dto.getSabangnetShippingCompCd());
    		policyShippingCompMapper.addPolicyShippingCompOutmall(sabangnetVo);
    	}

    	return 1;
    }
}
