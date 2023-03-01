package kr.co.pulmuone.v1.policy.benefit.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import kr.co.pulmuone.v1.policy.benefit.dto.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.MessageCommEnum;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.mapper.policy.benefit.PolicyBenefitEmployeeMapper;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.policy.benefit.dto.PolicyBenefitEmployeeBrandGroupDto;
import kr.co.pulmuone.v1.policy.benefit.dto.PolicyBenefitEmployeeGroupDto;
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
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0		20201030		박승현              최초작성
 *
 * =======================================================================
 * </PRE>
 *
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class PolicyBenefitEmployeeService {

	@Autowired
	private final PolicyBenefitEmployeeMapper policyBenefitEmployeeMapper;

	/**
     * 임직원 혜택관리그룹 목록 조회
     *
     * @param
     * @return PolicyBenefitEmployeeDto
     */
    protected PolicyBenefitEmployeeGroupDto getPolicyBenefitEmployeeGroupList() {
    	PolicyBenefitEmployeeGroupDto result = new PolicyBenefitEmployeeGroupDto();
    	List<PolicyBenefitEmployeeVo> rows = policyBenefitEmployeeMapper.getPolicyBenefitEmployeeMasterList();

    	for(PolicyBenefitEmployeeVo vo : rows) {
    		if(StringUtil.isNotEmpty(vo.getPsEmplDiscMasterId())) {
    			List<PolicyBenefitEmployeeGroupVo> groupList = policyBenefitEmployeeMapper.getPolicyBenefitEmployeeGroupList(vo);
    			for(PolicyBenefitEmployeeGroupVo grpVo : groupList) {
    				List<PolicyBenefitEmployeeGroupBrandGroupVo> brandGroupList = policyBenefitEmployeeMapper.getPolicyBenefitEmployeeGroupBrandGroupList(grpVo);
    				grpVo.setBrandGroupList(brandGroupList);
    			}
    			vo.setGroupList(groupList);
    			List<PolicyBenefitEmployeeLegalVo> companyList = policyBenefitEmployeeMapper.getPolicyBenefitEmployeeLegalList(vo);
    			vo.setCompanyList(companyList);
    		}
        }
    	result.setRows(rows);
    	return result;
    }
    /**
     * 임직원 혜택관리그룹 목록 최근 업데이트 일자
     *
     * @param
     * @return PolicyBenefitEmployeeVo
     */
    protected PolicyBenefitEmployeeVo getLastModifyDatePolicyBenefitEmployeeGroup() {
    	return policyBenefitEmployeeMapper.getLatestUpdatePolicyBenefitEmployee();
    }

	/**
     * 임직원 혜택 그룹 등록
     *
     * @param List<PolicyBenefitEmployeeVo>
     * @return MessageCommEnum
     */
    protected MessageCommEnum addPolicyBenefitEmployeeGroup(List<PolicyBenefitEmployeeVo> voList) throws Exception {
    	int addCnt = 0;

    	for(PolicyBenefitEmployeeVo vo : voList) {

    		if(policyBenefitEmployeeMapper.addPolicyBenefitEmployeeMaster(vo) < 1) throw new BaseException(BaseEnums.CommBase.VALID_ERROR);
    		int addGroupCnt = 0;
    		for(PolicyBenefitEmployeeGroupVo grpVo : vo.getGroupList()) {
    			grpVo.setPsEmplDiscMasterId(vo.getPsEmplDiscMasterId());
    			if(policyBenefitEmployeeMapper.addPolicyBenefitEmployeeGroup(grpVo) < 1) throw new BaseException(BaseEnums.CommBase.VALID_ERROR);
    			if(policyBenefitEmployeeMapper.addPolicyBenefitEmployeeGroupBrandGroup(grpVo) != grpVo.getBrandGroupList().size()) throw new BaseException(BaseEnums.CommBase.VALID_ERROR);
    			addGroupCnt++;
    		}
    		if(addGroupCnt != vo.getGroupList().size()) throw new BaseException(BaseEnums.CommBase.VALID_ERROR);

    		if(policyBenefitEmployeeMapper.addPolicyBenefitEmployeeLegal(vo) != vo.getCompanyList().size()) throw new BaseException(BaseEnums.CommBase.VALID_ERROR);

    		addCnt++;
    	}
    	if(addCnt < 1 || addCnt != voList.size()) {
    		//에러
    		return BaseEnums.Default.FAIL;
//    		throw new BaseException(BaseEnums.CommBase.VALID_ERROR);
    	}
    	return BaseEnums.Default.SUCCESS;
    }

    /**
     * 임직원 혜택 그룹 수정
     *
     * @param List<PolicyBenefitEmployeeVo>
     * @return MessageCommEnum
     */
    protected MessageCommEnum putPolicyBenefitEmployeeGroup(List<PolicyBenefitEmployeeVo> voList) throws Exception {
    	if(policyBenefitEmployeeMapper.putPolicyBenefitEmployeeMaster(voList) > 0) {

    		// PS_EMPL_DISC_GRP.PS_EMPL_DISC_GRP_ID 은 혜택 사용금액에 사용되는 중요한 값이여서 수정할때마다 변경되면 안됨
    		for(PolicyBenefitEmployeeVo vo : voList) {
    			// 기존 group 조회
    			List<PolicyBenefitEmployeeGroupVo> orgGroupList = policyBenefitEmployeeMapper.getPolicyBenefitEmployeeGroupList(vo);

    			// 업데이트 하려는 psEmplDiscGrpId list 추출
				List<String> putPsEmplDiscGrpIds = vo.getGroupList().stream()
						.filter(dto -> StringUtil.isNotEmpty(dto.getPsEmplDiscGrpId()))
						.map(PolicyBenefitEmployeeGroupVo::getPsEmplDiscGrpId).collect(Collectors.toList());

    			if(orgGroupList!= null && !orgGroupList.isEmpty()) {
    				// 업데이트 psEmplDiscGrpId list 에 기존 데이터 없으면 삭제
					List<String> delPsEmplDiscGrpIds = orgGroupList.stream().map(PolicyBenefitEmployeeGroupVo::getPsEmplDiscGrpId)
							.filter(psEmplDiscGrpId -> !putPsEmplDiscGrpIds.contains(psEmplDiscGrpId)).collect(Collectors.toList());
					if (!delPsEmplDiscGrpIds.isEmpty()) {
						if(policyBenefitEmployeeMapper.delPolicyBenefitEmployeeGroupByPsEmplDiscGrpId(delPsEmplDiscGrpIds) != delPsEmplDiscGrpIds.size()) throw new BaseException(BaseEnums.CommBase.VALID_ERROR);
	    			}
					if(policyBenefitEmployeeMapper.delPolicyBenefitEmployeeGroupBrandGroup(orgGroupList) < 1) throw new BaseException(BaseEnums.CommBase.VALID_ERROR);
    			}
    			for(PolicyBenefitEmployeeGroupVo grpVo : vo.getGroupList()) {
    				grpVo.setPsEmplDiscMasterId(vo.getPsEmplDiscMasterId());
					if (StringUtil.isNotEmpty(grpVo.getPsEmplDiscGrpId())) {
						policyBenefitEmployeeMapper.putPolicyBenefitEmployeeGroup(grpVo);
    				} else {
    					if(policyBenefitEmployeeMapper.addPolicyBenefitEmployeeGroup(grpVo) < 1) throw new BaseException(BaseEnums.CommBase.VALID_ERROR);
    				}
    				if(policyBenefitEmployeeMapper.addPolicyBenefitEmployeeGroupBrandGroup(grpVo) != grpVo.getBrandGroupList().size()) throw new BaseException(BaseEnums.CommBase.VALID_ERROR);
    			}

    			if(policyBenefitEmployeeMapper.delPolicyBenefitEmployeeLegal(vo.getPsEmplDiscMasterId()) < 1) throw new BaseException(BaseEnums.CommBase.VALID_ERROR);
    			if(policyBenefitEmployeeMapper.addPolicyBenefitEmployeeLegal(vo) != vo.getCompanyList().size()) throw new BaseException(BaseEnums.CommBase.VALID_ERROR);
    		}
    	}else {
    		//에러
    		return BaseEnums.Default.FAIL;
    	}
    	return BaseEnums.Default.SUCCESS;
    }

    /**
     * 임직원 혜택 그룹 삭제
     *
     * @param psEmplDiscBrandGrpId
     * @return MessageCommEnum
     */
    protected MessageCommEnum delPolicyBenefitEmployeeGroup(PolicyBenefitEmployeeVo vo) throws Exception {
    	List<PolicyBenefitEmployeeGroupVo> orgGroupList = policyBenefitEmployeeMapper.getPolicyBenefitEmployeeGroupList(vo);
    	if(policyBenefitEmployeeMapper.delPolicyBenefitEmployeeGroupBrandGroup(orgGroupList) > 0
    		&& policyBenefitEmployeeMapper.delPolicyBenefitEmployeeGroup(vo.getPsEmplDiscMasterId()) > 0
    		&& policyBenefitEmployeeMapper.delPolicyBenefitEmployeeLegal(vo.getPsEmplDiscMasterId()) > 0
    		&& policyBenefitEmployeeMapper.delPolicyBenefitEmployeeMaster(vo.getPsEmplDiscMasterId()) > 0) {
    		return BaseEnums.Default.SUCCESS;
    	}else {
    		//에러
    		throw new BaseException(BaseEnums.CommBase.VALID_ERROR);
    	}
    }

    /**
     * 임직원 할인율 브랜드 그룹 목록 최근 업데이트 일자
     *
     * @param
     * @return PolicyBenefitEmployeeBrandGroupVo
     */
    protected PolicyBenefitEmployeeBrandGroupVo getLastModifyDatePolicyBenefitEmployeeBrandGroup() {
    	return policyBenefitEmployeeMapper.getLatestUpdatePolicyBenefitEmployeeBrandGroup();
    }

    /**
     * 임직원 혜택그룹에 등록된 할인율 브랜드 그룹
     *
     * @param
     * @return PolicyBenefitEmployeeBrandGroupVo
     */
    protected PolicyBenefitEmployeeBrandGroupVo getRegistDiscMasterPolicyBenefitEmployeeBrandGroup(String psEmplDiscBrandGrpId) {
    	return policyBenefitEmployeeMapper.getRegistDiscMasterPolicyBenefitEmployeeBrandGroup(psEmplDiscBrandGrpId);
    }
    /**
     * 임직원 할인율 브랜드 그룹 목록 조회
     *
     * @param
     * @return PolicyBenefitEmployeeBrandGroupDto
     */
    protected PolicyBenefitEmployeeBrandGroupDto getPolicyBenefitEmployeeBrandGroupList(String searchType) {
    	PolicyBenefitEmployeeBrandGroupDto result = new PolicyBenefitEmployeeBrandGroupDto();
    	List<PolicyBenefitEmployeeBrandGroupVo> rows = policyBenefitEmployeeMapper.getPolicyBenefitEmployeeBrandGroupList();
    	if(!("BRANDGROUP").equals(searchType)) {
	    	for(PolicyBenefitEmployeeBrandGroupVo vo : rows) {
	    		if(StringUtil.isNotEmpty(vo.getPsEmplDiscBrandGrpId())) {
	    			List<PolicyBenefitEmployeeBrandGroupBrandVo> brandList = policyBenefitEmployeeMapper.getPolicyBenefitEmployeeBrandGroupBrandList(vo.getPsEmplDiscBrandGrpId());
	    			vo.setBrandList(brandList);
	    		}
	        }
	    	List<PolicyBenefitEmployeeBrandGroupBrandVo> noneBrandList = policyBenefitEmployeeMapper.getNonePolicyBenefitEmployeeBrandGroupBrandList();
	    	if(rows == null) {
	    		rows = new ArrayList<PolicyBenefitEmployeeBrandGroupVo>();
	    	}
	    	if(noneBrandList != null && noneBrandList.size() > 0) {
	    		PolicyBenefitEmployeeBrandGroupVo newVo = new PolicyBenefitEmployeeBrandGroupVo();
	    		newVo.setBrandList(noneBrandList);
	    		rows.add(newVo);
	    	}
    	}
    	result.setRows(rows);
    	return result;
    }

    /**
     * 임직원 할인율 브랜드 그룹 등록
     *
     * @param List<PolicyBenefitEmployeeBrandGroupVo>
     * @return MessageCommEnum
     */
    protected MessageCommEnum addPolicyBenefitEmployeeBrandGroup(List<PolicyBenefitEmployeeBrandGroupVo> voList) throws Exception {

    	int addGroupBrandCnt = 0;
    	int addCnt = 0;
    	for(PolicyBenefitEmployeeBrandGroupVo vo : voList) {
    		if(policyBenefitEmployeeMapper.addPolicyBenefitEmployeeBrandGroup(vo) < 1) throw new BaseException(BaseEnums.CommBase.VALID_ERROR);
//    		vo.setPsEmplDiscBrandGrpId(Integer.toString(PsEmplDiscBrandGrpId));
    		addGroupBrandCnt = policyBenefitEmployeeMapper.addPolicyBenefitEmployeeBrandGroupBrand(vo);
    		if(addGroupBrandCnt < 1 || addGroupBrandCnt != vo.getBrandList().size()) {
    			//에러. 롤백
    			throw new BaseException(BaseEnums.CommBase.VALID_ERROR);
    		}
    		addCnt++;
    	}
    	if(addCnt < 1 || addCnt != voList.size()) {
    		//에러
    		return BaseEnums.Default.FAIL;
//    		throw new BaseException(BaseEnums.CommBase.VALID_ERROR);
    	}
    	return BaseEnums.Default.SUCCESS;
    }

    /**
     * 임직원 할인율 브랜드 그룹 수정
     *
     * @param List<PolicyBenefitEmployeeBrandGroupVo>
     * @return MessageCommEnum
     */
    protected MessageCommEnum putPolicyBenefitEmployeeBrandGroup(List<PolicyBenefitEmployeeBrandGroupVo> voList) throws Exception {
    	if(policyBenefitEmployeeMapper.putPolicyBenefitEmployeeBrandGroup(voList) > 0) {
    		for(PolicyBenefitEmployeeBrandGroupVo vo : voList) {
    			int addGroupBrandCnt = 0;
    			if(policyBenefitEmployeeMapper.delPolicyBenefitEmployeeBrandGroupBrand(vo) > 0) {
    				addGroupBrandCnt = policyBenefitEmployeeMapper.addPolicyBenefitEmployeeBrandGroupBrand(vo);
    				if(addGroupBrandCnt < 1 || addGroupBrandCnt != vo.getBrandList().size()) {
    					//에러. 롤백
    					throw new BaseException(BaseEnums.CommBase.VALID_ERROR);
    				}
    			}else return BaseEnums.Default.FAIL;
    		}
    	}else {
    		//에러
    		return BaseEnums.Default.FAIL;
    	}
    	return BaseEnums.Default.SUCCESS;
    }
    /**
     * 임직원 할인율 브랜드 그룹 삭제
     *
     * @param psEmplDiscBrandGrpId
     * @return MessageCommEnum
     */
    protected MessageCommEnum delPolicyBenefitEmployeeBrandGroup(String psEmplDiscBrandGrpId) throws Exception {
    	PolicyBenefitEmployeeBrandGroupVo vo = new PolicyBenefitEmployeeBrandGroupVo();
    	vo.setPsEmplDiscBrandGrpId(psEmplDiscBrandGrpId);
    	if(policyBenefitEmployeeMapper.delPolicyBenefitEmployeeBrandGroupBrand(vo) > 0
    			&& policyBenefitEmployeeMapper.delPolicyBenefitEmployeeBrandGroup(psEmplDiscBrandGrpId) > 0) {
    		return BaseEnums.Default.SUCCESS;
    	}else {
    		//에러
    		throw new BaseException(BaseEnums.CommBase.VALID_ERROR);
    	}
    }

    /**
     * 브랜드별 임직원 할인율 구하기
     *
     * @param String
     * @return int
     */
    protected PolicyBenefitEmployeeBrandGroupBrandVo getPolicyBenefitEmployeeDiscountRatio(String urBrandId) throws Exception {
    	return policyBenefitEmployeeMapper.getPolicyBenefitEmployeeDiscountRatio(urBrandId);
    }

	/**
	 * 임직원 할인정보 조회 by user
	 *
	 * @param urErpEmployeeCd String
	 * @return List<PolicyBenefitEmployeeByUserVo>
	 * @throws Exception exception
	 */
	protected List<PolicyBenefitEmployeeByUserVo> getEmployeeDiscountByUser(String urErpEmployeeCd) throws Exception {
		return policyBenefitEmployeeMapper.getEmployeeDiscountByUser(urErpEmployeeCd);
	}

	/**
	 * 임직원 할인 브랜드 그룹 정보 조회 by user
	 *
	 * @param psEmplDiscGrpId Long
	 * @return List<PolicyBenefitEmployeeBrandGroupByUserVo>
	 * @throws Exception exception
	 */
	protected List<PolicyBenefitEmployeeBrandGroupByUserVo> getEmployeeDiscountBrandGroupByUser(Long psEmplDiscGrpId) throws Exception {
		return policyBenefitEmployeeMapper.getEmployeeDiscountBrandGroupByUser(psEmplDiscGrpId);
	}

	/**
	 * 임직원 할인 브랜드 정보 조회 by user
	 *
	 * @param psEmplDiscBrandGrpId Long
	 * @return List<PolicyBenefitEmployeeBrandByUserVo>
	 * @throws Exception exception
	 */
	protected List<PolicyBenefitEmployeeBrandByUserVo> getEmployeeDiscountBrandByUser(Long psEmplDiscBrandGrpId) throws Exception {
		return policyBenefitEmployeeMapper.getEmployeeDiscountBrandByUser(psEmplDiscBrandGrpId);
	}

	/**
	 * 임직원 할인 과거 내역 조회 by user
	 *
	 * @param urErpEmployeeCd String
	 * @param startDate String
	 * @param endDate String
	 * @return List<PolicyBenefitEmployeePastInfoByUserVo>
	 * @throws Exception exception
	 */
	protected List<PolicyBenefitEmployeePastInfoByUserVo> getEmployeeDiscountPastByUser(String urErpEmployeeCd, String startDate, String endDate) throws Exception {
		return policyBenefitEmployeeMapper.getEmployeeDiscountPastByUser(urErpEmployeeCd, startDate, endDate);
	}

	/**
	 * 표준 브랜드로  임직원 혜택  그룹 찾기
	 *
	 * @param list
	 * @param urBrandId
	 * @return
	 */
	protected PolicyBenefitEmployeeByUserVo findEmployeeDiscountBrandByUser(List<PolicyBenefitEmployeeByUserVo> list, Long urBrandId) {
		if(list != null) {
			for (PolicyBenefitEmployeeByUserVo vo : list) {
				for (PolicyBenefitEmployeeBrandGroupByUserVo brandGroupVo : vo.getList()){
					if (brandGroupVo.getBrand().stream().anyMatch(brand -> brand.getUrBrandId().equals(urBrandId))){
						return vo;
					}
				}
			}
		}
		return null;
	}

	/**
	 * 표준브랜드로 할인정보 찾기
	 *
	 * @param policyBenefitEmployeeByUserVo
	 * @param urBrandId
	 * @return PolicyBenefitEmployeeBrandGroupByUserVo
	 */
	protected PolicyBenefitEmployeeBrandGroupByUserVo getDiscountRatioEmployeeDiscountBrand(PolicyBenefitEmployeeByUserVo policyBenefitEmployeeByUserVo, Long urBrandId) {
		if (policyBenefitEmployeeByUserVo != null && policyBenefitEmployeeByUserVo.getList() != null) {
			for (PolicyBenefitEmployeeBrandGroupByUserVo brandGroupVo : policyBenefitEmployeeByUserVo.getList()){
				if (brandGroupVo.getBrand().stream().anyMatch(brand -> brand.getUrBrandId().equals(urBrandId))){
					return brandGroupVo;
				}
			}
		}

		return null;
	}
}
