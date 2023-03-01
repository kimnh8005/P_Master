package kr.co.pulmuone.v1.policy.payment.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.mapper.policy.payment.PolicyPaymentMapper;
import kr.co.pulmuone.v1.policy.payment.dto.PolicyPaymentCardBenefitDto;
import kr.co.pulmuone.v1.policy.payment.dto.PolicyPaymentPromotionDto;
import kr.co.pulmuone.v1.policy.payment.dto.PolicyPaymentPromotionRequestDto;
import kr.co.pulmuone.v1.policy.payment.dto.vo.PolicyPaymentPromotionVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <PRE>
 * Forbiz Korea
 * Class의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    20200915   	 홍진영            최초작성
 * =======================================================================
 * </PRE>
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class PolicyPaymentService {

	private final PolicyPaymentMapper policyPaymentMapper;

	/**
	 * 결제방법 리스트 조회
	 *
	 * @throws Exception
	 */
	protected List<HashMap<String,String>> getPayUseList() throws Exception {
		return policyPaymentMapper.getPayUseList();
	}


	/**
	 * 카드정보 리스트 조회
	 *
	 * @throws Exception
	 */
	protected List<HashMap<String,String>> getPayCardUseList() throws Exception {
		return policyPaymentMapper.getPayCardUseList();
	}

	/**
	 * 신용카드 혜택 리스트 조회
	 *
	 * @throws Exception
	 */
	protected List<HashMap<String,String>> getCartBenefitList() throws Exception {
		return policyPaymentMapper.getCartBenefitList();
	}

	/**
	 * @Desc 제휴구분 PG 조회 DropDown
	 * @param policyPaymentCardBenefitDto
	 * @return PolicyPaymentPromotionDto
	 * @throws Exception
	 */
	protected ApiResult<?> getPaymentList(PolicyPaymentCardBenefitDto policyPaymentCardBenefitDto) throws Exception{
		PolicyPaymentPromotionDto result = new PolicyPaymentPromotionDto();

		List<PolicyPaymentPromotionVo> rows = policyPaymentMapper.getPaymentList(policyPaymentCardBenefitDto);

		result.setRows(rows);

		return ApiResult.success(result);
	}


	/**
	 * @Desc 제휴구분 결제수단 조회 DropDown
	 * @param policyPaymentCardBenefitDto
	 * @return PolicyPaymentPromotionDto
	 * @throws Exception
	 */
	protected ApiResult<?> getPaymentUseList(PolicyPaymentCardBenefitDto policyPaymentCardBenefitDto) throws Exception{
		PolicyPaymentPromotionDto result = new PolicyPaymentPromotionDto();

		List<PolicyPaymentPromotionVo> rows = policyPaymentMapper.getPaymentUseList(policyPaymentCardBenefitDto);

		result.setRows(rows);

		return ApiResult.success(result);
	}


	/**
	 * @Desc 제휴구분 결제수단 상세 조회 DropDown
	 * @param PolicyPaymentPromotionRequestDto
	 * @return PolicyPaymentPromotionDto
	 * @throws Exception
	 */
	protected ApiResult<?> getPayCardList(PolicyPaymentPromotionRequestDto policyPaymentPromotionRequestDto) throws Exception{
		PolicyPaymentPromotionDto result = new PolicyPaymentPromotionDto();

		List<PolicyPaymentPromotionVo> rows = policyPaymentMapper.getPayCardList(policyPaymentPromotionRequestDto);

		result.setRows(rows);

		return ApiResult.success(result);
	}


}
