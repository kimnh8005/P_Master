package kr.co.pulmuone.bos.policy.claim;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.policy.claim.dto.PolicyClaimBosRequestDto;
import kr.co.pulmuone.v1.policy.claim.dto.PolicyClaimBosResponseDto;
import kr.co.pulmuone.v1.policy.claim.dto.PolicyClaimCtgryRequestDto;
import kr.co.pulmuone.v1.policy.claim.dto.PolicyClaimCtgryResponseDto;
import kr.co.pulmuone.v1.policy.claim.dto.PolicyClaimMallRequestDto;
import kr.co.pulmuone.v1.policy.claim.dto.SavePolicyClaimBosRequestDto;
import kr.co.pulmuone.v1.policy.claim.dto.vo.PolicyClaimMallVo;
import kr.co.pulmuone.v1.policy.claim.dto.SavePsClaimCtgryRequestDto;
import kr.co.pulmuone.v1.policy.claim.dto.SavePolicyClaimCtgryRequestSaveDto;
import kr.co.pulmuone.v1.policy.claim.dto.SavePolicyClaimMallRequestDto;
import kr.co.pulmuone.v1.policy.claim.service.PolicyClaimBiz;
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
 *  1.0		20210120		천혜현              최초작성
 *
 * =======================================================================
 * </PRE>
 *
 */
@RestController
@RequiredArgsConstructor
public class PolicyClaimController {

	@Autowired
	private PolicyClaimBiz policyClaimBiz;

    @Autowired(required = true)
    private HttpServletRequest request;


	/**
     * BOS 클레임 사유 목록
     *
     * @param PolicyClaimCtgryRequestDto
     * @return PolicyClaimCtgryResponseDto
     * @throws Exception
     */
	@RequestMapping(value = "/admin/policy/claim/getPsClaimCtgryList")
	@ApiOperation(value = "BOS 클레임 사유 목록 ")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = PolicyClaimCtgryResponseDto.class)
	})
	@ResponseBody
	public ApiResult<?> getPsClaimCtgryList(PolicyClaimCtgryRequestDto policyClaimCtgryRequestDto) throws Exception{
		return policyClaimBiz.getPsClaimCtgryList((PolicyClaimCtgryRequestDto) BindUtil.convertRequestToObject(request, PolicyClaimCtgryRequestDto.class));
	}

	/**
     * BOS 클레임 사유 목록(리스트박스 조회용)
     *
     * @param PolicyClaimCtgryRequestDto
     * @return PolicyClaimCtgryResponseDto
     * @throws Exception
     */
	@RequestMapping(value = "/admin/policy/claim/searchPsClaimCtgryList")
	@ApiOperation(value = "BOS 클레임 사유 목록(리스트박스 조회용)")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = PolicyClaimCtgryResponseDto.class)
	})
	@ResponseBody
	public ApiResult<?> searchPsClaimCtgryList(PolicyClaimCtgryRequestDto policyClaimCtgryRequestDto) throws Exception{
		return policyClaimBiz.searchPsClaimCtgryList(policyClaimCtgryRequestDto);
	}


	/**
	 * BOS 클레임 사유 저장
	 *
	 * @param SavePsClaimCtgryRequestDto
     * @throws Exception
	 */
	@PostMapping(value = "/admin/policy/claim/savePsClaimCtgry")
	@ApiOperation(value = "BOS 클레임 사유 저장")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data : null"),
			@ApiResponse(code = 9999, message = "" + "[DUPLICATE_DATA] 777777777 - 중복된 데이터가 존재합니다. \n" )
	})
	public ApiResult<?> savePsClaimCtgry(SavePsClaimCtgryRequestDto dto)throws Exception{

		//binding data
		dto.setInsertRequestDtoList((List<SavePolicyClaimCtgryRequestSaveDto>) BindUtil.convertJsonArrayToDtoList(dto.getInsertData(), SavePolicyClaimCtgryRequestSaveDto.class));
		dto.setUpdateRequestDtoList((List<SavePolicyClaimCtgryRequestSaveDto>) BindUtil.convertJsonArrayToDtoList(dto.getUpdateData(), SavePolicyClaimCtgryRequestSaveDto.class));
		dto.setDeleteRequestDtoList((List<SavePolicyClaimCtgryRequestSaveDto>) BindUtil.convertJsonArrayToDtoList(dto.getDeleteData(), SavePolicyClaimCtgryRequestSaveDto.class));

		return policyClaimBiz.savePsClaimCtgry(dto);
	}


	/**
     * 쇼핑몰 클레임 사유 등록
     *
     * @param SavePolicyClaimMallRequestDto
     * @throws Exception
     */
	@PostMapping(value = "/admin/policy/claim/addPsClaimMall")
	@ApiOperation(value = "쇼핑몰 클레임 사유 등록")
	public ApiResult<?> addPsClaimMall(SavePolicyClaimMallRequestDto savePolicyClaimMallRequestDto) throws Exception{
		return policyClaimBiz.addPsClaimMall(savePolicyClaimMallRequestDto);
	}


	/**
     * 쇼핑몰 클레임 사유 수정
     *
     * @param SavePolicyClaimMallRequestDto
     * @throws Exception
     */
	@PostMapping(value = "/admin/policy/claim/putPsClaimMall")
	@ApiOperation(value = "쇼핑몰 클레임 사유 수정")
	public ApiResult<?> putPsClaimMall(SavePolicyClaimMallRequestDto savePolicyClaimMallRequestDto) throws Exception{
		return policyClaimBiz.putPsClaimMall(savePolicyClaimMallRequestDto);
	}



	/**
     * 쇼핑몰 클레임 사유 목록
     *
     * @param PolicyClaimMallRequestDto
     * @throws Exception
     */
	@PostMapping(value = "/admin/policy/claim/getPolicyClaimMallList")
	@ApiOperation(value = "쇼핑몰 클레임 사유 목록")
	public ApiResult<?> getPolicyClaimMallList(PolicyClaimMallRequestDto dto) throws Exception{
		return policyClaimBiz.getPolicyClaimMallList((PolicyClaimMallRequestDto) BindUtil.convertRequestToObject(request,PolicyClaimMallRequestDto.class));
	}


	/**
     * 쇼핑몰 클레임 사유 상세
     *
     * @param PolicyClaimMallVo
     * @throws Exception
     */
	@PostMapping(value = "/admin/policy/claim/getPolicyClaimMall")
	@ApiOperation(value = "쇼핑몰 클레임 사유 상세")
	public ApiResult<?> getPolicyClaimMall(PolicyClaimMallVo dto) throws Exception{
		return policyClaimBiz.getPolicyClaimMall(dto);
	}


	/**
     * BOS 클레임 공급업체별 사유 목록
     *
     * @param PolicyClaimCtgryRequestDto
     * @return PolicyClaimCtgryResponseDto
     * @throws Exception
     */
	@RequestMapping(value = "/admin/policy/claim/getPsClaimSupplyCtgryList")
	@ApiOperation(value = "BOS 클레임 공급업체별 사유 목록")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = PolicyClaimCtgryResponseDto.class)
	})
	@ResponseBody
	public ApiResult<?> getPsClaimSupplyCtgryList(String supplierCode) throws Exception{
		return policyClaimBiz.getPsClaimSupplyCtgryList(supplierCode);
	}


	/**
     * BOS 클레임 사유 등록
     *
     * @param SavePolicyClaimBosRequestDto
     * @throws Exception
     */
	@PostMapping(value = "/admin/policy/claim/addPsClaimBos")
	@ApiOperation(value = "BOS 클레임 사유 등록")
	public ApiResult<?> addPsClaimBos(@RequestBody SavePolicyClaimBosRequestDto savePolicyClaimBosRequestDto) throws Exception{
		return policyClaimBiz.addPsClaimBos(savePolicyClaimBosRequestDto);
	}


	/**
     * BOS 클레임 사유 수정
     *
     * @param PolicyClaimBosVo
     * @throws Exception
     */
	@PostMapping(value = "/admin/policy/claim/putPsClaimBos")
	@ApiOperation(value = "BOS 클레임 사유 수정")
	public ApiResult<?> putPsClaimBos(@RequestBody SavePolicyClaimBosRequestDto savePolicyClaimBosRequestDto) throws Exception{
		return policyClaimBiz.putPsClaimBos(savePolicyClaimBosRequestDto);
	}

	/**
     * BOS 클레임 사유 목록
     *
     * @param PolicyClaimBosRequestDto
     * @throws Exception
     */
	@PostMapping(value = "/admin/policy/claim/getPolicyClaimBosList")
	@ApiOperation(value = "BOS 클레임 사유 목록")
	public ApiResult<?> getPolicyClaimBosList(PolicyClaimBosRequestDto dto) throws Exception{
		return policyClaimBiz.getPolicyClaimBosList((PolicyClaimBosRequestDto) BindUtil.convertRequestToObject(request,PolicyClaimBosRequestDto.class));
	}


	/**
     * BOS 클레임 사유 상세
     *
     * @param PolicyClaimBosRequestDto
     * @throws Exception
     */
	@PostMapping(value = "/admin/policy/claim/getPolicyClaimBos")
	@ApiOperation(value = "BOS 클레임 사유 목록")
	public ApiResult<?> getPolicyClaimBos(PolicyClaimBosRequestDto dto) throws Exception{
		return policyClaimBiz.getPolicyClaimBos(dto);
	}

	/**
     * BOS 클레임 사유 삭제
     *
     * @param PolicyClaimBosRequestDto
     * @throws Exception
     */
	@PostMapping(value = "/admin/policy/claim/delPolicyClaimBos")
	@ApiOperation(value = "BOS 클레임 사유 삭제")
	public ApiResult<?> delPolicyClaimBos(PolicyClaimBosRequestDto dto) throws Exception{
		return policyClaimBiz.delPolicyClaimBos(dto);
	}

	/**
     * BOS 클레임 등록 사유 목록(리스트박스 조회용)
     *
     * @param PolicyClaimCtgryRequestDto
     * @return PolicyClaimCtgryResponseDto
     * @throws Exception
     */
	@RequestMapping(value = "/admin/policy/claim/searchPsClaimBosList")
	@ApiOperation(value = "BOS 클레임 등록 사유 목록(리스트박스 조회용)")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = PolicyClaimCtgryResponseDto.class)
	})
	@ResponseBody
	public ApiResult<?> searchPsClaimBosList(PolicyClaimCtgryRequestDto policyClaimCtgryRequestDto) throws Exception{
		return policyClaimBiz.searchPsClaimBosList(policyClaimCtgryRequestDto);
	}

	/**
     * BOS 클레임 사유 카테고리 조회 (주문 > 미출 주문상세리스트 > 일괄 취소완료 팝업에서 사용)
     *
     * @throws Exception
     */
	@PostMapping(value = "/admin/policy/claim/getPolicyClaimCtgryListForClaimPopup")
	@ApiOperation(value = "BOS 클레임 사유 목록")
	public ApiResult<?> getPolicyClaimCtgryListForClaimPopup() throws Exception{
		return policyClaimBiz.getPolicyClaimCtgryListForClaimPopup();
	}

}
