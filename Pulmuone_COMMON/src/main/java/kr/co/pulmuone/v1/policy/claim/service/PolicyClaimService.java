package kr.co.pulmuone.v1.policy.claim.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.PolicyEnums;
import kr.co.pulmuone.v1.comm.mapper.policy.claim.PolicyClaimMapper;
import kr.co.pulmuone.v1.policy.claim.dto.PolicyClaimBosRequestDto;
import kr.co.pulmuone.v1.policy.claim.dto.PolicyClaimBosResponseDto;
import kr.co.pulmuone.v1.policy.claim.dto.PolicyClaimCtgryRequestDto;
import kr.co.pulmuone.v1.policy.claim.dto.PolicyClaimCtgryResponseDto;
import kr.co.pulmuone.v1.policy.claim.dto.PolicyClaimMallDetailResponseDto;
import kr.co.pulmuone.v1.policy.claim.dto.PolicyClaimMallRequestDto;
import kr.co.pulmuone.v1.policy.claim.dto.PolicyClaimMallResponseDto;
import kr.co.pulmuone.v1.policy.claim.dto.PolicyClaimSupplyCtgryResponseDto;
import kr.co.pulmuone.v1.policy.claim.dto.SavePolicyClaimBosRequestDto;
import kr.co.pulmuone.v1.policy.claim.dto.vo.PolicyClaimMallVo;
import kr.co.pulmuone.v1.policy.claim.dto.vo.PolicyClaimSupplyCtgryVo;
import kr.co.pulmuone.v1.policy.claim.dto.SavePsClaimCtgryRequestDto;
import kr.co.pulmuone.v1.policy.claim.dto.SavePolicyClaimCtgryRequestSaveDto;
import kr.co.pulmuone.v1.policy.claim.dto.SavePolicyClaimMallRequestDto;
import kr.co.pulmuone.v1.policy.claim.dto.vo.PolicyClaimBosSupplyVo;
import kr.co.pulmuone.v1.policy.claim.dto.vo.PolicyClaimBosVo;
import kr.co.pulmuone.v1.policy.claim.dto.vo.PolicyClaimCtgryVo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PolicyClaimService {

    @Autowired
    PolicyClaimMapper policyClaimMapper;

    /**
     * BOS 클레임 사유 목록
     *
     * @param PolicyClaimCtgryRequestDto
     * @return PolicyClaimCtgryResponseDto
     * @throws Exception
     */
    protected PolicyClaimCtgryResponseDto getPsClaimCtgryList(PolicyClaimCtgryRequestDto policyClaimCtgryRequestDto) throws Exception {

    	PageMethod.startPage(policyClaimCtgryRequestDto.getPage(), policyClaimCtgryRequestDto.getPageSize());
        Page<PolicyClaimCtgryVo> rows = policyClaimMapper.getPsClaimCtgryList(policyClaimCtgryRequestDto);

    	return PolicyClaimCtgryResponseDto.builder().total((int) rows.getTotal()).rows(rows.getResult()).build();
    }


    /**
     * BOS 클레임 사유 목록(리스트박스 조회용)
     *
     * @param PolicyClaimCtgryRequestDto
     * @return PolicyClaimCtgryResponseDto
     * @throws Exception
     */
    protected PolicyClaimCtgryResponseDto searchPsClaimCtgryList(PolicyClaimCtgryRequestDto policyClaimCtgryRequestDto) throws Exception {
        List<PolicyClaimCtgryVo> rows = policyClaimMapper.getPsClaimCtgryList(policyClaimCtgryRequestDto);
    	return PolicyClaimCtgryResponseDto.builder().rows(rows).build();
    }

    /**
     * BOS 클레임 사유 저장
     *
     * @param SavePsClaimCtgryRequestDto
     * @return
     * @throws Exception
     */
	protected ApiResult<?> savePsClaimCtgry(SavePsClaimCtgryRequestDto dto) throws Exception{
		List<SavePolicyClaimCtgryRequestSaveDto> insertRequestDtoList = dto.getInsertRequestDtoList();
		List<SavePolicyClaimCtgryRequestSaveDto> updateRequestDtoList = dto.getUpdateRequestDtoList();
		List<SavePolicyClaimCtgryRequestSaveDto> deleteRequestDtoList = dto.getDeleteRequestDtoList();

		boolean insertDuplicated = this.checkClaimNameDuplicate(insertRequestDtoList, dto.getCategoryCode());
		boolean updateDuplicated = this.checkClaimNameDuplicate(updateRequestDtoList, dto.getCategoryCode());
		if(!insertDuplicated && !updateDuplicated) {

			//데이터 저장
			if(CollectionUtils.isNotEmpty(insertRequestDtoList)){
				policyClaimMapper.addPsClaimCtgry(insertRequestDtoList, dto.getCategoryCode());
			}
			//데이터 수정
			if(CollectionUtils.isNotEmpty(updateRequestDtoList)){
				policyClaimMapper.putPsClaimCtgry(updateRequestDtoList, dto.getCategoryCode());
			}
			//데이터 삭제
			if(CollectionUtils.isNotEmpty(deleteRequestDtoList)){

				// 사용중인 BOS 클레임 사유 확인
				for(SavePolicyClaimCtgryRequestSaveDto saveDto : deleteRequestDtoList) {

					PolicyClaimBosRequestDto policyClaimBosReqDto = new PolicyClaimBosRequestDto();
					if(saveDto.getCategoryCode().equals(PolicyEnums.ClaimCategoryCode.L_CLAIM.getCode())) {
						policyClaimBosReqDto.setSearchLClaimCtgryId(saveDto.getPsClaimCtgryId());
					}else if(saveDto.getCategoryCode().equals(PolicyEnums.ClaimCategoryCode.M_CLAIM.getCode())) {
						policyClaimBosReqDto.setSearchMClaimCtgryId(saveDto.getPsClaimCtgryId());
					}else {
						policyClaimBosReqDto.setSearchSClaimCtgryId(saveDto.getPsClaimCtgryId());
					}

					int count = policyClaimMapper.checkPsClaimBosDuplicate(policyClaimBosReqDto);

					// BOS 클레임사유에서 사용중인 항목은 삭제 불가
					if(count > 0) {
						return ApiResult.result(PolicyEnums.ClaimMessage.FOREIGN_KEY_DATA);
					}

				}
				policyClaimMapper.delPsClaimCtgry(deleteRequestDtoList, dto.getCategoryCode());
			}
		}else {
			return ApiResult.result(PolicyEnums.ClaimMessage.DUPLICATE_REASON);
		}

		return ApiResult.success();
	}

	/**
	 * 중복 데이터 체크
	 * @param List<SavePolicyClaimCtgryRequestSaveDto>
	 * @param categoryCode
	 * @throws Exception
	 */
	protected boolean checkClaimNameDuplicate(List<SavePolicyClaimCtgryRequestSaveDto> dtoList, String categoryCode){

		if(dtoList.stream().map(m -> m.getClaimName()).distinct().count() != dtoList.size()) {
			return true;
		}

		int count = 0 ;
		if(CollectionUtils.isNotEmpty(dtoList)) {
			if(PolicyEnums.ClaimCategoryCode.S_CLAIM.getCode().equals(categoryCode)){
				for(SavePolicyClaimCtgryRequestSaveDto dto : dtoList){
					List<SavePolicyClaimCtgryRequestSaveDto> reqList = new ArrayList<>();
					reqList.add(dto);
					count += policyClaimMapper.checkClaimNameDuplicate(reqList, categoryCode,dto.getPsClaimCtgryId());
				}
			}else{
				Long psClaimCtgryId = dtoList.get(0).getPsClaimCtgryId();
				count = policyClaimMapper.checkClaimNameDuplicate(dtoList, categoryCode,psClaimCtgryId);
			}

			if(count > 0) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 쇼핑몰 클레임 사유 등록
	 * @param SavePolicyClaimMallRequestDto
	 * @throws Exception
	 */
	protected ApiResult<?> addPsClaimMall(SavePolicyClaimMallRequestDto savePolicyClaimMallRequestDto) throws Exception{

    	// BOS 클레임 사유 중복체크
    	if(checkPsClaimMallDuplicate(savePolicyClaimMallRequestDto)) {
    		return ApiResult.result(PolicyEnums.ClaimMessage.DUPLICATE_REASON);
    	}
    	policyClaimMapper.addPsClaimMall(savePolicyClaimMallRequestDto);

		return ApiResult.success();
	}


	/**
	 * 쇼핑몰 클레임 사유 수정
	 * @param SavePolicyClaimMallRequestDto
	 * @throws Exception
	 */
	protected ApiResult<?> putPsClaimMall(SavePolicyClaimMallRequestDto savePolicyClaimMallRequestDto) throws Exception{

		// BOS 클레임 사유 중복체크
    	if(checkPsClaimMallDuplicate(savePolicyClaimMallRequestDto)) {
    		return ApiResult.result(PolicyEnums.ClaimMessage.DUPLICATE_REASON);
    	}
    	policyClaimMapper.putPsClaimMall(savePolicyClaimMallRequestDto);

		return ApiResult.success();
	}


	/**
	 * MALL 클레임 사유 중복 체크
	 * @param PolicyClaimMallVo
	 * @throws Exception
	 */
	protected boolean checkPsClaimMallDuplicate(SavePolicyClaimMallRequestDto savePolicyClaimMallRequestDto){
		boolean reseult = false;

    	PolicyClaimMallRequestDto policyClaimMallReqDto = new PolicyClaimMallRequestDto();
    	policyClaimMallReqDto.setPsClaimCtgryId(savePolicyClaimMallRequestDto.getPsClaimMallId());
    	policyClaimMallReqDto.setSearchKeyword(savePolicyClaimMallRequestDto.getReasonMessage());

		int count = policyClaimMapper.checkPsClaimMallDuplicate(policyClaimMallReqDto);
		if(count > 0) {
			reseult = true;
		}

		return reseult;
	}


	/**
	 * 쇼핑몰 클레임 사유 목록
	 * @param PolicyClaimMallRequestDto
	 * @return PolicyClaimMallResponseDto
	 * @throws Exception
	 */
	protected PolicyClaimMallResponseDto getPsClaimMallList(PolicyClaimMallRequestDto dto) throws Exception{
		PageMethod.startPage(dto.getPage(), dto.getPageSize());
        Page<PolicyClaimMallVo> rows = policyClaimMapper.getPsClaimMallList(dto);

    	return PolicyClaimMallResponseDto.builder().total((int) rows.getTotal()).rows(rows.getResult()).build();
	}

	/**
	 * 쇼핑몰 클레임 사유 상세
	 * @param PolicyClaimMallVo
	 * @return PolicyClaimMallDetailResponseDto
	 * @throws Exception
	 */
	protected PolicyClaimMallDetailResponseDto getPolicyClaimMall(PolicyClaimMallVo dto) throws Exception{
		PolicyClaimMallDetailResponseDto responseDto = new PolicyClaimMallDetailResponseDto();
		PolicyClaimMallVo vo = policyClaimMapper.getPolicyClaimMall(dto);
		responseDto.setRows(vo);
		return responseDto;
	}


	/**
	 * 쇼핑몰 클레임 사유 상세
	 * @param
	 * @return PolicyClaimSupplyCtgryResponseDto
	 * @throws Exception
	 */
	protected PolicyClaimSupplyCtgryResponseDto getPsClaimSupplyCtgryList(String supplierCode) throws Exception{
		PolicyClaimSupplyCtgryResponseDto responseDto = new PolicyClaimSupplyCtgryResponseDto();
		List<PolicyClaimSupplyCtgryVo> vo = policyClaimMapper.getPsClaimSupplyCtgryList(supplierCode);
		responseDto.setRows(vo);
		return responseDto;
	}

	/**
     * BOS 클레임 사유 등록
     *
     * @param SavePolicyClaimBosRequestDto
     * @throws Exception
     */
	protected ApiResult<?> addPsClaimBos(SavePolicyClaimBosRequestDto savePolicyClaimBosRequestDto) throws Exception{

		// BOS 클레임 사유 중복체크
    	if(checkPsClaimBosDuplicate(savePolicyClaimBosRequestDto)) {
    		return ApiResult.result(PolicyEnums.ClaimMessage.DUPLICATE_REASON);
    	}
    	policyClaimMapper.addPsClaimBos(savePolicyClaimBosRequestDto);

    	// 공급업체 사유 등록
    	addPsClaimBosSupply(savePolicyClaimBosRequestDto);

		return ApiResult.success();
	}

	/**
	 * BOS 클레임 사유 중복 체크
	 * @param SavePolicyClaimBosRequestDto
	 * @throws Exception
	 */
	protected boolean checkPsClaimBosDuplicate(SavePolicyClaimBosRequestDto savePolicyClaimBosRequestDto){
		boolean reseult = false;

		PolicyClaimBosRequestDto policyClaimBosReqDto = new PolicyClaimBosRequestDto();
		policyClaimBosReqDto.setPsClaimBosId(savePolicyClaimBosRequestDto.getPsClaimBosId());
		policyClaimBosReqDto.setSearchLClaimCtgryId(savePolicyClaimBosRequestDto.getLClaimCtgryId());
		policyClaimBosReqDto.setSearchMClaimCtgryId(savePolicyClaimBosRequestDto.getMClaimCtgryId());
		policyClaimBosReqDto.setSearchSClaimCtgryId(savePolicyClaimBosRequestDto.getSClaimCtgryId());

		int count = policyClaimMapper.checkPsClaimBosDuplicate(policyClaimBosReqDto);
		if(count > 0) {
			reseult = true;
		}

		return reseult;
	}

    /**
     * BOS 클레임 사유 목록
     *
     * @param PolicyClaimBosRequestDto
     * @return PolicyClaimBosResponseDto
     * @throws Exception
     */
	protected PolicyClaimBosResponseDto getPolicyClaimBosList(PolicyClaimBosRequestDto dto) throws Exception{
		PageMethod.startPage(dto.getPage(), dto.getPageSize());
        Page<PolicyClaimBosVo> rows = policyClaimMapper.getPolicyClaimBosList(dto);

        //공급업체 사유(반품회수), 공급업체 사유(반품 미회수) vo, 쇼핑몰 클레임 사유 조회
        for(PolicyClaimBosVo policyClaimBosVo : rows) {

        	List<PolicyClaimBosSupplyVo> supplyList = policyClaimMapper.getPolicyClaimSupplyList(policyClaimBosVo.getPsClaimBosId());
        	policyClaimBosVo.setClaimSupplierList(supplyList);
        	policyClaimBosVo.setNonClaimSupplierList(supplyList);

        	//쇼핑몰 클레임 사유 조회
        	PolicyClaimMallRequestDto mallReqDto = new PolicyClaimMallRequestDto();
        	mallReqDto.setSearchLClaimCtgryId(policyClaimBosVo.getLClaimCtgryId());
        	mallReqDto.setSearchMClaimCtgryId(policyClaimBosVo.getMClaimCtgryId());
        	mallReqDto.setSearchSClaimCtgryId(policyClaimBosVo.getSClaimCtgryId());
        	List<PolicyClaimMallVo> mallClaimReasonList = policyClaimMapper.getPsClaimMallList(mallReqDto);
        	policyClaimBosVo.setMallClaimReasonList(mallClaimReasonList);
        }

    	return PolicyClaimBosResponseDto.builder().total((int) rows.getTotal()).rows(rows.getResult()).build();
	}


	protected PolicyClaimBosVo getPolicyClaimBos(PolicyClaimBosRequestDto dto) throws Exception{
		PolicyClaimBosVo resultVo  = policyClaimMapper.getPolicyClaimBos(dto);

		//공급업체 사유(반품회수), 공급업체 사유(반품 미회수) vo 조회
    	List<PolicyClaimBosSupplyVo> supplyList = policyClaimMapper.getPolicyClaimSupplyList(dto.getPsClaimBosId());
    	resultVo.setClaimSupplierList(supplyList);
    	resultVo.setNonClaimSupplierList(supplyList);

    	//쇼핑몰 클레임 사유 조회
    	PolicyClaimMallRequestDto mallReqDto = new PolicyClaimMallRequestDto();
    	mallReqDto.setSearchLClaimCtgryId(resultVo.getLClaimCtgryId());
    	mallReqDto.setSearchMClaimCtgryId(resultVo.getMClaimCtgryId());
    	mallReqDto.setSearchSClaimCtgryId(resultVo.getSClaimCtgryId());
    	List<PolicyClaimMallVo> mallClaimReasonList = policyClaimMapper.getPsClaimMallList(mallReqDto);
    	resultVo.setMallClaimReasonList(mallClaimReasonList);

		return resultVo;
	}

	/**
     * BOS 클레임 사유 수정
     *
     * @param SavePolicyClaimBosRequestDto
     * @throws Exception
     */
	protected ApiResult<?> putPsClaimBos(SavePolicyClaimBosRequestDto savePolicyClaimBosRequestDto) throws Exception{

		// BOS 클레임 사유 중복체크
    	if(checkPsClaimBosDuplicate(savePolicyClaimBosRequestDto)) {
    		return ApiResult.result(PolicyEnums.ClaimMessage.DUPLICATE_REASON);
    	}
    	policyClaimMapper.putPsClaimBos(savePolicyClaimBosRequestDto);

    	// 공급업체 사유 삭제
    	policyClaimMapper.delPsClaimBosSupply(savePolicyClaimBosRequestDto);

    	// 공급업체 사유 등록
    	addPsClaimBosSupply(savePolicyClaimBosRequestDto);

		return ApiResult.success();
	}

	/**
     * BOS 클레임 사유 공급업체 등록
     *
     * @param SavePolicyClaimBosRequestDto
     * @throws Exception
     */
	protected void addPsClaimBosSupply(SavePolicyClaimBosRequestDto savePolicyClaimBosRequestDto) throws Exception{

		for(PolicyClaimBosSupplyVo claimVo : savePolicyClaimBosRequestDto.getClaimSupplierList()) {
    		for(PolicyClaimBosSupplyVo nonClaimVo : savePolicyClaimBosRequestDto.getNonClaimSupplierList() ) {

    			if(claimVo.getSupplierCode().equals(nonClaimVo.getNonSupplierCode())
    					&& StringUtils.isNotEmpty(claimVo.getClaimCode()) && StringUtils.isNotEmpty(nonClaimVo.getNonClaimCode())) {

    				PolicyClaimBosSupplyVo vo = new PolicyClaimBosSupplyVo();
    				vo.setPsClaimBosId(savePolicyClaimBosRequestDto.getPsClaimBosId());
    				vo.setSupplierCode(claimVo.getSupplierCode());
    				vo.setClaimCode(claimVo.getClaimCode());
    				vo.setNonSupplierCode(nonClaimVo.getNonSupplierCode());
    				vo.setNonClaimCode(nonClaimVo.getNonClaimCode());

    				policyClaimMapper.addPsClaimBosSupply(vo);
    			}
    		}
    	}
	}


	/**
	 * BOS 클레임 사유 삭제
	 * @param PolicyClaimBosRequestDto
	 * @throws Exception
	 */
	protected int delPolicyClaimBos(PolicyClaimBosRequestDto dto)throws Exception{
		return policyClaimMapper.delPolicyClaimBos(dto);
	}


	/**
     * BOS 클레임에 등록된 사유 목록(리스트박스 조회용)
     *
     * @param PolicyClaimCtgryRequestDto
     * @return PolicyClaimCtgryResponseDto
     * @throws Exception
     */
	protected PolicyClaimCtgryResponseDto searchPsClaimBosList(PolicyClaimCtgryRequestDto reqDto) throws Exception{
		List<PolicyClaimCtgryVo> rows = new ArrayList<>();

		if(StringUtils.isNotEmpty(reqDto.getCategoryCode())) {

			//클레임 사유(대)
			if(PolicyEnums.ClaimCategoryCode.L_CLAIM.getCode().equals(reqDto.getCategoryCode())) {
				rows = policyClaimMapper.searchPsClaimBosLCtgryList(reqDto);
			}
			//클레임 사유(중)
			if(PolicyEnums.ClaimCategoryCode.M_CLAIM.getCode().equals(reqDto.getCategoryCode())) {
				rows = policyClaimMapper.searchPsClaimBosMCtgryList(reqDto);
			}
			//귀책처
			if(PolicyEnums.ClaimCategoryCode.S_CLAIM.getCode().equals(reqDto.getCategoryCode())) {
				rows = policyClaimMapper.searchPsClaimBosSCtgryList(reqDto);
			}
		}

		return PolicyClaimCtgryResponseDto.builder().rows(rows).build();

	}

	/**
     * BOS 클레임 사유 카테고리 조회 (주문 > 미출 주문상세리스트 > 일괄 취소완료 팝업에서 사용)
     *
     * @return List<PolicyClaimCtgryVo>
     * @throws Exception
     */
	protected List<PolicyClaimCtgryVo> getPolicyClaimCtgryListForClaimPopup() throws Exception{
    	return policyClaimMapper.getPolicyClaimCtgryListForClaimPopup();
	}
}
