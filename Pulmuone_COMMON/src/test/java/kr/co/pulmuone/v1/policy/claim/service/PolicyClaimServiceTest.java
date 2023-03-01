package kr.co.pulmuone.v1.policy.claim.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.PolicyEnums;
import kr.co.pulmuone.v1.policy.claim.dto.*;
import kr.co.pulmuone.v1.policy.claim.dto.vo.PolicyClaimBosSupplyVo;
import kr.co.pulmuone.v1.policy.claim.dto.vo.PolicyClaimBosVo;
import kr.co.pulmuone.v1.policy.claim.dto.vo.PolicyClaimMallVo;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PolicyClaimServiceTest extends CommonServiceTestBaseForJunit5{

	@Autowired
	private PolicyClaimService policyClaimService;

	@BeforeEach
	void beforeEach() {preLogin();}

	@Test
	void getPsClaimCtgryList_성공() throws Exception{
		PolicyClaimCtgryRequestDto policyClaimCtgryRequestDto = new PolicyClaimCtgryRequestDto();
		policyClaimCtgryRequestDto.setCategoryCode("10");

		PolicyClaimCtgryResponseDto result = policyClaimService.getPsClaimCtgryList(policyClaimCtgryRequestDto);

		assertTrue(result.getRows().size() > 0);
	}

	@Test
	void getPsClaimCtgryList_조회결과없음() throws Exception{
		PolicyClaimCtgryRequestDto policyClaimCtgryRequestDto = new PolicyClaimCtgryRequestDto();
		policyClaimCtgryRequestDto.setCategoryCode("100");

		PolicyClaimCtgryResponseDto result = policyClaimService.getPsClaimCtgryList(policyClaimCtgryRequestDto);

		assertFalse(result.getRows().size() > 0);
	}

	@Test
	void searchPsClaimCtgryList_성공() throws Exception{
		PolicyClaimCtgryRequestDto policyClaimCtgryRequestDto = new PolicyClaimCtgryRequestDto();
		policyClaimCtgryRequestDto.setCategoryCode("10");

		PolicyClaimCtgryResponseDto result = policyClaimService.getPsClaimCtgryList(policyClaimCtgryRequestDto);

		assertTrue(result.getRows().size() > 0);
	}

	@Test
	void searchPsClaimCtgryList_조회결과없음() throws Exception{
		PolicyClaimCtgryRequestDto policyClaimCtgryRequestDto = new PolicyClaimCtgryRequestDto();
		policyClaimCtgryRequestDto.setCategoryCode("100");

		PolicyClaimCtgryResponseDto result = policyClaimService.getPsClaimCtgryList(policyClaimCtgryRequestDto);

		assertFalse(result.getRows().size() > 0);
	}

	@Test
	void savePsClaimCtgry_중복데이터_있음() throws Exception{
		SavePsClaimCtgryRequestDto requestDto = new SavePsClaimCtgryRequestDto();
		List<SavePolicyClaimCtgryRequestSaveDto> insertRequestDtoList = new ArrayList<>();
		SavePolicyClaimCtgryRequestSaveDto saveDto = new SavePolicyClaimCtgryRequestSaveDto();
		saveDto.setClaimName("고객");
		insertRequestDtoList.add(saveDto);

		requestDto.setCategoryCode("10");
		requestDto.setInsertRequestDtoList(insertRequestDtoList);

		ApiResult<?> result = policyClaimService.savePsClaimCtgry(requestDto);

		assertEquals(PolicyEnums.ClaimMessage.DUPLICATE_REASON.getCode(), result.getCode());
	}

	@Test
	void savePsClaimCtgry_등록() throws Exception{
		SavePsClaimCtgryRequestDto requestDto = new SavePsClaimCtgryRequestDto();
		List<SavePolicyClaimCtgryRequestSaveDto> insertRequestDtoList = new ArrayList<>();
		SavePolicyClaimCtgryRequestSaveDto saveDto = new SavePolicyClaimCtgryRequestSaveDto();
		saveDto.setClaimName("등록테스트데이터");
		insertRequestDtoList.add(saveDto);

		requestDto.setCategoryCode("10");
		requestDto.setInsertRequestDtoList(insertRequestDtoList);

		ApiResult<?> result = policyClaimService.savePsClaimCtgry(requestDto);

		assertEquals(result.getCode(), BaseEnums.Default.SUCCESS.getCode());
	}

	@Test
	void savePsClaimCtgry_수정() throws Exception{
		SavePsClaimCtgryRequestDto requestDto = new SavePsClaimCtgryRequestDto();
		List<SavePolicyClaimCtgryRequestSaveDto> updateRequestDtoList = new ArrayList<>();
		SavePolicyClaimCtgryRequestSaveDto saveDto = new SavePolicyClaimCtgryRequestSaveDto();
		saveDto.setClaimName("등록테스트데이터");
		updateRequestDtoList.add(saveDto);

		requestDto.setCategoryCode("10");
		requestDto.setUpdateRequestDtoList(updateRequestDtoList);

		ApiResult<?> result = policyClaimService.savePsClaimCtgry(requestDto);

		assertEquals(result.getCode(), BaseEnums.Default.SUCCESS.getCode());
	}

	@Test
	void savePsClaimCtgry_삭제() throws Exception{
		SavePsClaimCtgryRequestDto requestDto = new SavePsClaimCtgryRequestDto();
		List<SavePolicyClaimCtgryRequestSaveDto> deleteRequestDtoList = new ArrayList<>();
		SavePolicyClaimCtgryRequestSaveDto saveDto = new SavePolicyClaimCtgryRequestSaveDto();
		saveDto.setPsClaimCtgryId(4L);
		saveDto.setCategoryCode("10");
		deleteRequestDtoList.add(saveDto);

		requestDto.setCategoryCode("10");
		requestDto.setDeleteRequestDtoList(deleteRequestDtoList);

		ApiResult<?> result = policyClaimService.savePsClaimCtgry(requestDto);

		assertEquals(BaseEnums.Default.SUCCESS.getCode(), result.getCode());
	}

	@Test
	void savePsClaimCtgry_BOS에서_사용중인_항목_삭제불가() throws Exception{
		SavePsClaimCtgryRequestDto requestDto = new SavePsClaimCtgryRequestDto();
		List<SavePolicyClaimCtgryRequestSaveDto> deleteRequestDtoList = new ArrayList<>();
		SavePolicyClaimCtgryRequestSaveDto saveDto = new SavePolicyClaimCtgryRequestSaveDto();
		saveDto.setPsClaimCtgryId(2L);
		saveDto.setCategoryCode("10");
		deleteRequestDtoList.add(saveDto);

		requestDto.setCategoryCode("10");
		requestDto.setDeleteRequestDtoList(deleteRequestDtoList);

		ApiResult<?> result = policyClaimService.savePsClaimCtgry(requestDto);

		assertEquals(PolicyEnums.ClaimMessage.FOREIGN_KEY_DATA.getCode(), result.getCode());
	}

	@Test
	void addPsClaimMall_성공() throws Exception{
		SavePolicyClaimMallRequestDto reqDto = new SavePolicyClaimMallRequestDto();
		reqDto.setReasonMessage("테스트1234");
		reqDto.setReasonType("A");
		reqDto.setAttcRequiredYn("Y");
		reqDto.setPsClaimBosId(10L);
		reqDto.setUseYn("Y");
		reqDto.getUserVo().setUserId("1");

		ApiResult<?> result = policyClaimService.addPsClaimMall(reqDto);

		assertEquals(result.getCode(), BaseEnums.Default.SUCCESS.getCode());
	}

	@Test
	void addPsClaimMall_실패() throws Exception{
		SavePolicyClaimMallRequestDto reqDto = new SavePolicyClaimMallRequestDto();
		reqDto.setReasonMessage("테스트");
		reqDto.setReasonType("A");

	    assertThrows(Exception.class, () -> {
	    	policyClaimService.addPsClaimMall(reqDto);
	    });
	}

	@Test
	void addPsClaimMall_BOS클레임사유_중복() throws Exception{
		SavePolicyClaimMallRequestDto reqDto = new SavePolicyClaimMallRequestDto();
		reqDto.setReasonMessage("테스트2");
		reqDto.setReasonType("A");
		reqDto.setAttcRequiredYn("N");
		reqDto.setPsClaimBosId(1L);
		reqDto.setUseYn("Y");

		ApiResult<?> result = policyClaimService.addPsClaimMall(reqDto);

		assertEquals(PolicyEnums.ClaimMessage.DUPLICATE_REASON.getCode(), result.getCode());
	}

	@Test
	void putPsClaimMall_성공() throws Exception{
		SavePolicyClaimMallRequestDto reqDto = new SavePolicyClaimMallRequestDto();
		reqDto.setPsClaimMallId(new Long(3));
		reqDto.setReasonMessage("테스트1234");
		reqDto.setReasonType("A");
		reqDto.setAttcRequiredYn("N");
		reqDto.setPsClaimBosId(1L);
		reqDto.setUseYn("Y");

		ApiResult<?> result = policyClaimService.putPsClaimMall(reqDto);

		assertEquals(result.getCode(), BaseEnums.Default.SUCCESS.getCode());
	}

	@Test
	void putPsClaimMall_실패() throws Exception{
		SavePolicyClaimMallRequestDto reqDto = new SavePolicyClaimMallRequestDto();
		reqDto.setPsClaimMallId(new Long(3));
		reqDto.setReasonMessage("테스트");
		reqDto.setPsClaimBosId(1L);
		reqDto.setUseYn("Y");

		assertThrows(Exception.class, () -> {
			policyClaimService.putPsClaimMall(reqDto);
	    });
	}

	@Test
	void getPsClaimMallList_성공() throws Exception{
		PolicyClaimMallRequestDto reqDto = new PolicyClaimMallRequestDto();
		PolicyClaimMallResponseDto result = policyClaimService.getPsClaimMallList(reqDto);

		assertTrue(result.getRows().size() > 0);
	}

	@Test
	void getPsClaimMallList_조회결과없음() throws Exception{
		PolicyClaimMallRequestDto reqDto = new PolicyClaimMallRequestDto();
		reqDto.setSearchResaonType("12344566");
		PolicyClaimMallResponseDto result = policyClaimService.getPsClaimMallList(reqDto);

		assertFalse(result.getRows().size() > 0);
	}

	@Test
	void getPolicyClaimMall_성공() throws Exception{
		PolicyClaimMallVo vo = new PolicyClaimMallVo();
		vo.setPsClaimMallId(new Long(3));
		PolicyClaimMallDetailResponseDto result = policyClaimService.getPolicyClaimMall(vo);

		assertTrue(StringUtils.isNotEmpty(result.getRows().getReasonMessage()));
	}

	@Test
	void getPolicyClaimMall_조회결과없음() throws Exception{
		PolicyClaimMallVo vo = new PolicyClaimMallVo();
		vo.setPsClaimMallId(new Long(1000));
		PolicyClaimMallDetailResponseDto result = policyClaimService.getPolicyClaimMall(vo);

		assertNull(result.getRows());
	}

	@Test
	void addPsClaimBos_등록() throws Exception{
		SavePolicyClaimBosRequestDto reqDto = new SavePolicyClaimBosRequestDto();
		reqDto.setLClaimCtgryId(new Long(11));
		reqDto.setMClaimCtgryId(new Long(11));
		reqDto.setSClaimCtgryId(new Long(11));
		reqDto.setUseYn("Y");

		List<PolicyClaimBosSupplyVo> claimList = new ArrayList<>();
		List<PolicyClaimBosSupplyVo> nonClaimList = new ArrayList<>();
		PolicyClaimBosSupplyVo claimVo = new PolicyClaimBosSupplyVo();
		claimVo.setSupplierCode("PF");
		claimVo.setClaimCode("0001");
		claimList.add(claimVo);
		PolicyClaimBosSupplyVo nonClaimVo = new PolicyClaimBosSupplyVo();
		nonClaimVo.setSupplierCode("PF");
		nonClaimVo.setClaimCode("0001");
		nonClaimList.add(nonClaimVo);

		reqDto.setClaimSupplierList(claimList);
		reqDto.setNonClaimSupplierList(nonClaimList);

		ApiResult<?> result = policyClaimService.addPsClaimBos(reqDto);

		assertEquals(result.getCode(), BaseEnums.Default.SUCCESS.getCode());
	}

	@Test
	void addPsClaimBos_실패() throws Exception{
		SavePolicyClaimBosRequestDto reqDto = new SavePolicyClaimBosRequestDto();
		reqDto.setLClaimCtgryId(new Long(11));

		assertThrows(Exception.class, () -> {
			policyClaimService.addPsClaimBos(reqDto);
	    });
	}

	@Test
	void addPsClaimBos_BOS클레임사유_중복() throws Exception{
		SavePolicyClaimBosRequestDto reqDto = new SavePolicyClaimBosRequestDto();
		reqDto.setLClaimCtgryId(1L);
		reqDto.setMClaimCtgryId(8L);
		reqDto.setSClaimCtgryId(19L);
		reqDto.setUseYn("Y");

		List<PolicyClaimBosSupplyVo> claimList = new ArrayList<>();
		List<PolicyClaimBosSupplyVo> nonClaimList = new ArrayList<>();
		PolicyClaimBosSupplyVo claimVo = new PolicyClaimBosSupplyVo();
		claimVo.setSupplierCode("PF");
		claimVo.setClaimCode("0001");
		claimList.add(claimVo);
		PolicyClaimBosSupplyVo nonClaimVo = new PolicyClaimBosSupplyVo();
		nonClaimVo.setSupplierCode("PF");
		nonClaimVo.setClaimCode("0001");
		nonClaimList.add(nonClaimVo);

		reqDto.setClaimSupplierList(claimList);
		reqDto.setNonClaimSupplierList(nonClaimList);

		ApiResult<?> result = policyClaimService.addPsClaimBos(reqDto);

		assertEquals(PolicyEnums.ClaimMessage.DUPLICATE_REASON.getCode(), result.getCode());
	}

	@Test
	void getPolicyClaimBosList_성공() throws Exception{
		PolicyClaimBosRequestDto reqDto = new PolicyClaimBosRequestDto();
		PolicyClaimBosResponseDto result = policyClaimService.getPolicyClaimBosList(reqDto);

		assertTrue(result.getRows().size() > 0);
	}

	@Test
	void getPolicyClaimBosList_조회결과없음() throws Exception{
		PolicyClaimBosRequestDto reqDto = new PolicyClaimBosRequestDto();
		reqDto.setSearchLClaimCtgryId(new Long(1000));
		PolicyClaimBosResponseDto result = policyClaimService.getPolicyClaimBosList(reqDto);

		assertFalse(result.getRows().size() > 0);
	}

	@Test
	void getPolicyClaimBos_성공() throws Exception{
		PolicyClaimBosRequestDto reqDto = new PolicyClaimBosRequestDto();
		reqDto.setPsClaimBosId(new Long(5));
		PolicyClaimBosVo result = policyClaimService.getPolicyClaimBos(reqDto);

		assertTrue(StringUtils.isNotEmpty(result.getLclaimCtgryName()));
	}

	@Test
	void getPolicyClaimBos_조회결과없음() throws Exception{
		PolicyClaimBosRequestDto reqDto = new PolicyClaimBosRequestDto();
		reqDto.setPsClaimBosId(new Long(0));

		assertThrows(Exception.class, () -> {
			policyClaimService.getPolicyClaimBos(reqDto);
	    });

	}

	@Test
	void putPsClaimBos_성공() throws Exception{
		SavePolicyClaimBosRequestDto reqDto = new SavePolicyClaimBosRequestDto();
		reqDto.setPsClaimBosId(new Long(5));
		reqDto.setLClaimCtgryId(new Long(25));
		reqDto.setMClaimCtgryId(new Long(28));
		reqDto.setSClaimCtgryId(new Long(32));
		reqDto.setUseYn("Y");

		List<PolicyClaimBosSupplyVo> claimList = new ArrayList<>();
		List<PolicyClaimBosSupplyVo> nonClaimList = new ArrayList<>();
		PolicyClaimBosSupplyVo claimVo = new PolicyClaimBosSupplyVo();
		claimVo.setSupplierCode("PF");
		claimVo.setClaimCode("0001");
		claimList.add(claimVo);
		PolicyClaimBosSupplyVo nonClaimVo = new PolicyClaimBosSupplyVo();
		nonClaimVo.setSupplierCode("PF");
		nonClaimVo.setClaimCode("0001");
		nonClaimList.add(nonClaimVo);

		reqDto.setClaimSupplierList(claimList);
		reqDto.setNonClaimSupplierList(nonClaimList);

		ApiResult<?> result = policyClaimService.putPsClaimBos(reqDto);

		assertEquals(result.getCode(), BaseEnums.Default.SUCCESS.getCode());
	}

	@Test
	void putPsClaimBos_실패() throws Exception{
		SavePolicyClaimBosRequestDto reqDto = new SavePolicyClaimBosRequestDto();
		reqDto.setLClaimCtgryId(new Long(0));

		assertThrows(Exception.class, () -> {
			policyClaimService.putPsClaimBos(reqDto);
	    });
	}

}
