package kr.co.pulmuone.v1.policy.payment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.MessageCommEnum;
import kr.co.pulmuone.v1.comm.enums.PolicyEnums;
import kr.co.pulmuone.v1.comm.mapper.policy.payment.PolicyPaymentCardBenefitMapper;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.policy.payment.dto.PolicyPaymentCardBenefitDto;
import kr.co.pulmuone.v1.policy.payment.dto.vo.PolicyPaymentCardBenefitVo;
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
 *  1.0		20201007		박승현              최초작성
 *
 * =======================================================================
 * </PRE>
 *
 */

@Service
@RequiredArgsConstructor
public class PolicyPaymentCardBenefitService {

	@Autowired
	private final PolicyPaymentCardBenefitMapper policyPaymentCardBenefitMapper;

    /**
     * 신용카드혜택안내 조회
     *
     * @param psCardBenefitId
     * @return PolicyPaymentCardBenefitVo
     */
    protected PolicyPaymentCardBenefitVo getPolicyPaymentCardBenefitInfo(Long psCardBenefitId) {
    	return policyPaymentCardBenefitMapper.getPolicyPaymentCardBenefitInfo(psCardBenefitId);
    }
    /**
     * 신용카드혜택안내 목록 조회
     *
     * @param PolicyPaymentCardBenefitDto
     * @return PolicyPaymentCardBenefitDto
     */
    protected PolicyPaymentCardBenefitDto getPolicyPaymentCardBenefitList(PolicyPaymentCardBenefitDto dto) {
    	PolicyPaymentCardBenefitDto result = new PolicyPaymentCardBenefitDto();
    	PageMethod.startPage(dto.getPage(), dto.getPageSize());
    	Page<PolicyPaymentCardBenefitVo> rows = policyPaymentCardBenefitMapper.getPolicyPaymentCardBenefitList(dto);
		result.setTotal((int)rows.getTotal());
		result.setRows(rows.getResult());
		return result;
    }
    /**
     * 신용카드혜택안내 신규 등록
     *
     * @param PolicyPaymentCardBenefitDto
     * @return ApiResult<?>
     */
    protected ApiResult<?> addPolicyPaymentCardBenefit(PolicyPaymentCardBenefitDto dto) {
    	/**
    	 * to-do:
    	 * 저장시(등록/수정) 현재 진행중인 동일기간의 게시물이 있을 경우 저장불가 처리(v1.23)
    	 * 동일기간의 범주가 모호함 - PG사가 다르면서 기간이 동일할수도 있으며, 각 유형이 다를수도 있고, 기간 또한 시작일 , 종료일이 완벽히 일치 할 경우 만인건지 일부 겹치는 기간인건지에 대한 요건이 없음.
    	 * >> 2020.11.17 > 중복의 범위는
    	 * 1.모든 PG사
    	 * 2.안내유형별
    	 * 3.기간은 하루라도 중복될 경우
    	 */
    	PolicyPaymentCardBenefitVo duplicate = policyPaymentCardBenefitMapper.checkDuplicateTermPolicyPaymentCardBenefit(dto);
    	if(duplicate != null && StringUtil.isNotEmpty(duplicate.getPsCardBenefitId())) {
    		return ApiResult.result(PolicyEnums.Payment.DUPLICATE_TERM);
    	}else {
    		policyPaymentCardBenefitMapper.addPolicyPaymentCardBenefit(dto);
    	}
    	return ApiResult.success();
    }
    /**
     * 신용카드혜택안내 수정
     *
     * @param PolicyPaymentCardBenefitDto
     * @return MessageCommEnum
     */
    protected MessageCommEnum putPolicyPaymentCardBenefit(PolicyPaymentCardBenefitDto dto) {
    	/**
    	 * to-do:
    	 * 저장시(등록/수정) 현재 진행중인 동일기간의 게시물이 있을 경우 저장불가 처리(v1.23)
    	 * 동일기간의 범주가 모호함 - PG사가 다르면서 기간이 동일할수도 있으며, 각 유형이 다를수도 있고, 기간 또한 시작일 , 종료일이 완벽히 일치 할 경우 만인건지 일부 겹치는 기간인건지에 대한 요건이 없음.
    	 * >> 2020.11.17 > 중복의 범위는
    	 * 1.모든 PG사
    	 * 2.안내유형별
    	 * 3.기간은 하루라도 중복될 경우
    	 */
    	PolicyPaymentCardBenefitVo duplicate = policyPaymentCardBenefitMapper.checkDuplicateTermPolicyPaymentCardBenefit(dto);
    	if(duplicate != null && !dto.getPsCardBenefitId().equals(duplicate.getPsCardBenefitId())) {
    		return PolicyEnums.Payment.DUPLICATE_TERM;
    	}else {
    		int cnt = policyPaymentCardBenefitMapper.putPolicyPaymentCardBenefit(dto);
    		if(cnt < 1) return PolicyEnums.Payment.UPDATE_FAIL;
    	}
    	return BaseEnums.Default.SUCCESS;
    }
    /**
     * 신용카드혜택안내 삭제
     *
     * @param psCardBenefitId
     * @return int
     */
    protected int delPolicyPaymentCardBenefit(Long psCardBenefitId) {
    	return policyPaymentCardBenefitMapper.delPolicyPaymentCardBenefit(psCardBenefitId);
    }
}
