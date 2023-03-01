package kr.co.pulmuone.v1.policy.benefit.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.MessageCommEnum;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.order.front.dto.OrderInfoFromEmployeeDiscountRequestDto;
import kr.co.pulmuone.v1.order.front.service.OrderFrontBiz;
import kr.co.pulmuone.v1.policy.benefit.dto.PolicyBenefitEmployeeBrandGroupSaveDto;
import kr.co.pulmuone.v1.policy.benefit.dto.PolicyBenefitEmployeeGroupSaveDto;
import kr.co.pulmuone.v1.policy.benefit.dto.PolicyBenefitEmployeePastInfoByUserResponseDto;
import kr.co.pulmuone.v1.policy.benefit.dto.vo.PolicyBenefitEmployeeBrandGroupByUserVo;
import kr.co.pulmuone.v1.policy.benefit.dto.vo.PolicyBenefitEmployeeByUserVo;
import kr.co.pulmuone.v1.policy.benefit.dto.vo.PolicyBenefitEmployeePastInfoByUserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PolicyBenefitEmployeeBizImpl implements PolicyBenefitEmployeeBiz {

    @Autowired
    private PolicyBenefitEmployeeService policyBenefitEmployeeService;

    @Autowired
	private OrderFrontBiz orderFrontBiz;

    /**
     * 임직원 혜택 그룹 목록 최근 업데이트 일자
     *
     * @param
     * @return PolicyBenefitEmployeeVo
     */
    @Override
    public ApiResult<?> getLastModifyDatePolicyBenefitEmployeeGroup() {
    	return ApiResult.success(policyBenefitEmployeeService.getLastModifyDatePolicyBenefitEmployeeGroup());
    }
    /**
     * 임직원 혜택 그룹 목록 조회
     *
     * @param
     * @return PolicyBenefitEmployeeDto
     */
    @Override
    public ApiResult<?> getPolicyBenefitEmployeeGroupList() {
    	return ApiResult.success(policyBenefitEmployeeService.getPolicyBenefitEmployeeGroupList());
    }

    /**
     * 임직원 혜택 그룹 저장
     *
     * @param PolicyBenefitEmployeeGroupSaveDto
     * @return ApiResult
     * @throws 	Exception
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
    public ApiResult<?> putPolicyBenefitEmployeeGroup(PolicyBenefitEmployeeGroupSaveDto dto) throws Exception{
    	if (dto.hasInsertData()) {
    		MessageCommEnum enums = policyBenefitEmployeeService.addPolicyBenefitEmployeeGroup(dto.getInsertList());
    		if(!BaseEnums.Default.SUCCESS.getCode().equals(enums.getCode())){
    			//처리
    			return ApiResult.result(enums);
    		}
    	}
    	if (dto.hasUpdateData()) {
    		MessageCommEnum enums = policyBenefitEmployeeService.putPolicyBenefitEmployeeGroup(dto.getUpdateList());
    		if(!BaseEnums.Default.SUCCESS.getCode().equals(enums.getCode())){
    			//처리
    			return ApiResult.result(enums);
    		}
    	}
    	if (dto.hasDeleteData()) {
    		MessageCommEnum enums = policyBenefitEmployeeService.delPolicyBenefitEmployeeGroup(dto.getDeleteList().get(0));

    		if(!BaseEnums.Default.SUCCESS.getCode().equals(enums.getCode())){
    			//처리
    			return ApiResult.result(enums);
    		}
    	}
    	return ApiResult.success();
    }

    /**
     * 임직원 할인율 브랜드 그룹 목록 최근 업데이트 일자
     *
     * @param
     * @return PolicyBenefitEmployeeBrandGroupVo
     */
    @Override
    public ApiResult<?> getLastModifyDatePolicyBenefitEmployeeBrandGroup() {
    	return ApiResult.success(policyBenefitEmployeeService.getLastModifyDatePolicyBenefitEmployeeBrandGroup());
    }
    /**
     * 임직원 혜택그룹에 등록된 할인율 브랜드 그룹
     *
     * @param
     * @return PolicyBenefitEmployeeBrandGroupVo
     */
    @Override
    public ApiResult<?> getRegistDiscMasterPolicyBenefitEmployeeBrandGroup(String psEmplDiscBrandGrpId) {
    	return ApiResult.success(policyBenefitEmployeeService.getRegistDiscMasterPolicyBenefitEmployeeBrandGroup(psEmplDiscBrandGrpId));
    }

    /**
     * 임직원 할인율 브랜드 그룹 목록 조회
     *
     * @param
     * @return PolicyBenefitEmployeeDto
     */
    @Override
    public ApiResult<?> getPolicyBenefitEmployeeBrandGroupList(String searchType) {
    	return ApiResult.success(policyBenefitEmployeeService.getPolicyBenefitEmployeeBrandGroupList(searchType));
    }

    /**
     * 임직원 할인율 브랜드 그룹 저장
     *
     * @param PolicyBenefitEmployeeBrandGroupSaveDto
     * @return ApiResult
     * @throws 	Exception
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
    public ApiResult<?> putPolicyBenefitEmployeeBrandGroup(PolicyBenefitEmployeeBrandGroupSaveDto dto) throws Exception{
    	if (dto.hasInsertData()) {
    		MessageCommEnum enums = policyBenefitEmployeeService.addPolicyBenefitEmployeeBrandGroup(dto.getInsertList());
    		if(!BaseEnums.Default.SUCCESS.getCode().equals(enums.getCode())){
    			//처리
    			return ApiResult.result(enums);
    		}
    	}
    	if (dto.hasUpdateData()) {
    		MessageCommEnum enums = policyBenefitEmployeeService.putPolicyBenefitEmployeeBrandGroup(dto.getUpdateList());
    		if(!BaseEnums.Default.SUCCESS.getCode().equals(enums.getCode())){
    			//처리
    			return ApiResult.result(enums);
    		}
    	}
    	return ApiResult.success();
    }

    /**
     * 임직원 할인율 브랜드 그룹 삭제
     *
     * @param psEmplDiscBrandGrpId
     * @return ApiResult
     * @throws 	Exception
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
    public ApiResult<?> delPolicyBenefitEmployeeBrandGroup(String psEmplDiscBrandGrpId) throws Exception{
    	return ApiResult.result(policyBenefitEmployeeService.delPolicyBenefitEmployeeBrandGroup(psEmplDiscBrandGrpId));
    }

    /**
     * 임직원 혜택 현황 조회
     */
	@Override
	public List<PolicyBenefitEmployeeByUserVo> getEmployeeDiscountBrandByUser(String urErpEmployeeCd) throws Exception {
		if(StringUtil.isEmpty(urErpEmployeeCd)) return new ArrayList<>();
		List<PolicyBenefitEmployeeByUserVo> result = policyBenefitEmployeeService.getEmployeeDiscountByUser(urErpEmployeeCd);
		for (PolicyBenefitEmployeeByUserVo vo : result) {
			vo.setUseAmount(orderFrontBiz.getOrderInfoFromEmployeeDiscount(OrderInfoFromEmployeeDiscountRequestDto.builder()
							.urErpEmployeeCd(urErpEmployeeCd)
							.psEmplDiscGrpId(vo.getPsEmplDiscGrpId())
							.startDate(vo.getStartDate())
							.endDate(vo.getEndDate())
					.build()
			));

			int remain = vo.getLimitAmount() - vo.getUseAmount();
			if (remain < 0) remain = 0;
			vo.setRemainAmount(remain);

			List<PolicyBenefitEmployeeBrandGroupByUserVo> brandGroupList = policyBenefitEmployeeService.getEmployeeDiscountBrandGroupByUser(vo.getPsEmplDiscGrpId());
			for (PolicyBenefitEmployeeBrandGroupByUserVo brandGroup : brandGroupList) {
				brandGroup.setBrand(policyBenefitEmployeeService.getEmployeeDiscountBrandByUser(brandGroup.getPsEmplDiscBrandGrpId()));
			}
			vo.setList(brandGroupList);
		}
		return result;
	}

	/**
	 * 임직원 포인트 이력 조회
	 */
	@Override
	public PolicyBenefitEmployeePastInfoByUserResponseDto getEmployeeDiscountPastByUser(String urErpEmployeeCd, String startDate, String endDate) throws Exception {
		PolicyBenefitEmployeePastInfoByUserResponseDto result = new PolicyBenefitEmployeePastInfoByUserResponseDto();
		List<PolicyBenefitEmployeePastInfoByUserVo> responseVoList = policyBenefitEmployeeService.getEmployeeDiscountPastByUser(urErpEmployeeCd, startDate, endDate);
		result.setSumAmount(responseVoList.stream().mapToInt(PolicyBenefitEmployeePastInfoByUserVo::getUseAmount).sum());
		result.setRows(responseVoList);

		return result;
	}

	/**
	 * 표준 브랜드로  임직원 혜택  그룹 찾기
	 */
	@Override
	public PolicyBenefitEmployeeByUserVo findEmployeeDiscountBrandByUser(List<PolicyBenefitEmployeeByUserVo> list, Long urBrandId) {
		return policyBenefitEmployeeService.findEmployeeDiscountBrandByUser(list, urBrandId);
	}

	/**
	 * 표준브랜드로 할인정보 찾기
	 */
	@Override
	public PolicyBenefitEmployeeBrandGroupByUserVo getDiscountRatioEmployeeDiscountBrand(PolicyBenefitEmployeeByUserVo policyBenefitEmployeeByUserVo,
			Long urBrandId) {
		return policyBenefitEmployeeService.getDiscountRatioEmployeeDiscountBrand(policyBenefitEmployeeByUserVo, urBrandId);
	}
}