package kr.co.pulmuone.v1.policy.benefit.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.MessageCommEnum;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.mapper.policy.benefit.PolicyBenefitEmployeeMapper;
import kr.co.pulmuone.v1.policy.benefit.dto.PolicyBenefitEmployeeBrandGroupDto;
import kr.co.pulmuone.v1.policy.benefit.dto.PolicyBenefitEmployeeGroupDto;
import kr.co.pulmuone.v1.policy.benefit.dto.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@Slf4j
class PolicyBenefitEmployeeServiceTest extends CommonServiceTestBaseForJunit5 {

	@Autowired
    private PolicyBenefitEmployeeService policyBenefitEmployeeService;

	@InjectMocks
	private PolicyBenefitEmployeeService mockPolicyBenefitEmployeeService;

	@Mock
	private PolicyBenefitEmployeeMapper mockPolicyBenefitEmployeeMapper;

	@BeforeEach
	void setUp() {
		mockPolicyBenefitEmployeeService = new PolicyBenefitEmployeeService(mockPolicyBenefitEmployeeMapper);
	}

	@Test
	public void test_혜택그룹_결과있음()  {

		// given

    	// when
		PolicyBenefitEmployeeGroupDto result = policyBenefitEmployeeService.getPolicyBenefitEmployeeGroupList();

		// then
		assertNotNull(result);
	}
	@Test
	public void test_할인율브랜드그룹_결과있음()  {

		// given
		String searchType = "";

		// when
		PolicyBenefitEmployeeBrandGroupDto result = policyBenefitEmployeeService.getPolicyBenefitEmployeeBrandGroupList(searchType);

		// then
		assertNotNull(result);
	}
	@Test
	public void test_할인율브랜드그룹_그룹명목록_결과있음()  {

		// given
		String searchType = "BRANDGROUP";

        log.info("searchType: {}", searchType);
		// when
		PolicyBenefitEmployeeBrandGroupDto result = policyBenefitEmployeeService.getPolicyBenefitEmployeeBrandGroupList(searchType);

		// then
		assertNotNull(result);
	}
	@Test
	public void test_혜택그룹_최종수정일자조회()  {

		// given

    	// when
		PolicyBenefitEmployeeVo result = policyBenefitEmployeeService.getLastModifyDatePolicyBenefitEmployeeGroup();

		// then
		assertNotNull(result);
	}
	@Test
	public void test_할인율브랜드그룹_최종수정일자조회()  {

		// given

		// when
		PolicyBenefitEmployeeBrandGroupVo result = policyBenefitEmployeeService.getLastModifyDatePolicyBenefitEmployeeBrandGroup();

		// then
		assertNotNull(result);
	}
	@Test
	public void test_임직원_혜택그룹에등록된_할인율브랜드그룹()  {
		// given
		String psEmplDiscBrandGrpId = "43";

		// when
		PolicyBenefitEmployeeBrandGroupVo result = policyBenefitEmployeeService.getRegistDiscMasterPolicyBenefitEmployeeBrandGroup(psEmplDiscBrandGrpId);

		// then
		assertNotNull(result);
	}

    @Test
	public void test_혜택그룹_등록() throws Exception {

    	List<PolicyBenefitEmployeeVo> voList = new ArrayList<PolicyBenefitEmployeeVo>();
    	PolicyBenefitEmployeeVo vo = new PolicyBenefitEmployeeVo();
    	List<PolicyBenefitEmployeeGroupVo> groupList = new ArrayList<PolicyBenefitEmployeeGroupVo>();
    	List<PolicyBenefitEmployeeGroupBrandGroupVo> brandGroupList = new ArrayList<PolicyBenefitEmployeeGroupBrandGroupVo>();
    	List<PolicyBenefitEmployeeLegalVo> companyList = new ArrayList<PolicyBenefitEmployeeLegalVo>();

    	PolicyBenefitEmployeeGroupVo groupVo = new PolicyBenefitEmployeeGroupVo();
    	PolicyBenefitEmployeeGroupBrandGroupVo brandGroupVo = new PolicyBenefitEmployeeGroupBrandGroupVo();
    	PolicyBenefitEmployeeLegalVo legalVo = new PolicyBenefitEmployeeLegalVo();

    	brandGroupList.add(brandGroupVo);

    	groupVo.setBrandGroupList(brandGroupList);
    	groupList.add(groupVo);

    	companyList.add(legalVo);

    	vo.setCompanyList(companyList);
    	vo.setGroupList(groupList);

    	voList.add(vo);

		given(mockPolicyBenefitEmployeeMapper.addPolicyBenefitEmployeeMaster(any())).willReturn(1);
		given(mockPolicyBenefitEmployeeMapper.addPolicyBenefitEmployeeGroup(any())).willReturn(1);
		given(mockPolicyBenefitEmployeeMapper.addPolicyBenefitEmployeeGroupBrandGroup(any())).willReturn(1);
		given(mockPolicyBenefitEmployeeMapper.addPolicyBenefitEmployeeLegal(any())).willReturn(1);

        // when
		MessageCommEnum msgEnum = mockPolicyBenefitEmployeeService.addPolicyBenefitEmployeeGroup(voList);

        // then
        assertTrue(msgEnum.getCode().equals(ApiResult.success().getCode()));

	}
    @Test
    public void test_혜택그룹_등록_실패() throws Exception {

    	List<PolicyBenefitEmployeeVo> voList = new ArrayList<PolicyBenefitEmployeeVo>();

    	given(mockPolicyBenefitEmployeeMapper.addPolicyBenefitEmployeeMaster(any())).willReturn(1);
    	given(mockPolicyBenefitEmployeeMapper.addPolicyBenefitEmployeeGroup(any())).willReturn(1);
    	given(mockPolicyBenefitEmployeeMapper.addPolicyBenefitEmployeeGroupBrandGroup(any())).willReturn(1);
    	given(mockPolicyBenefitEmployeeMapper.addPolicyBenefitEmployeeLegal(any())).willReturn(1);

    	// when
    	MessageCommEnum msgEnum = mockPolicyBenefitEmployeeService.addPolicyBenefitEmployeeGroup(voList);

    	// then
    	assertTrue(msgEnum.getCode().equals(ApiResult.fail().getCode()));

    }

    @Test
	public void test_혜택그룹_실패_하위데이터오류1() throws Exception {

    	List<PolicyBenefitEmployeeVo> voList = new ArrayList<PolicyBenefitEmployeeVo>();
    	PolicyBenefitEmployeeVo vo = new PolicyBenefitEmployeeVo();
    	List<PolicyBenefitEmployeeGroupVo> groupList = new ArrayList<PolicyBenefitEmployeeGroupVo>();
    	List<PolicyBenefitEmployeeGroupBrandGroupVo> brandGroupList = new ArrayList<PolicyBenefitEmployeeGroupBrandGroupVo>();
    	List<PolicyBenefitEmployeeLegalVo> companyList = new ArrayList<PolicyBenefitEmployeeLegalVo>();

    	PolicyBenefitEmployeeGroupVo groupVo = new PolicyBenefitEmployeeGroupVo();
    	PolicyBenefitEmployeeGroupBrandGroupVo brandGroupVo = new PolicyBenefitEmployeeGroupBrandGroupVo();
    	PolicyBenefitEmployeeLegalVo legalVo = new PolicyBenefitEmployeeLegalVo();

    	brandGroupList.add(brandGroupVo);

    	groupVo.setBrandGroupList(brandGroupList);
    	groupList.add(groupVo);

    	companyList.add(legalVo);

    	vo.setCompanyList(companyList);
    	vo.setGroupList(groupList);

    	voList.add(vo);

		given(mockPolicyBenefitEmployeeMapper.addPolicyBenefitEmployeeMaster(any())).willReturn(0);

		// when
    	BaseException myException = assertThrows(BaseException.class, () -> {
    		mockPolicyBenefitEmployeeService.addPolicyBenefitEmployeeGroup(voList);
    	});

    	// then
    	assertEquals(BaseEnums.CommBase.VALID_ERROR, myException.getMessageEnum());
	}
    @Test
    public void test_혜택그룹_실패_하위데이터오류2() throws Exception {

    	List<PolicyBenefitEmployeeVo> voList = new ArrayList<PolicyBenefitEmployeeVo>();
    	PolicyBenefitEmployeeVo vo = new PolicyBenefitEmployeeVo();
    	List<PolicyBenefitEmployeeGroupVo> groupList = new ArrayList<PolicyBenefitEmployeeGroupVo>();
    	List<PolicyBenefitEmployeeGroupBrandGroupVo> brandGroupList = new ArrayList<PolicyBenefitEmployeeGroupBrandGroupVo>();
    	List<PolicyBenefitEmployeeLegalVo> companyList = new ArrayList<PolicyBenefitEmployeeLegalVo>();

    	PolicyBenefitEmployeeGroupVo groupVo = new PolicyBenefitEmployeeGroupVo();
    	PolicyBenefitEmployeeGroupBrandGroupVo brandGroupVo = new PolicyBenefitEmployeeGroupBrandGroupVo();
    	PolicyBenefitEmployeeLegalVo legalVo = new PolicyBenefitEmployeeLegalVo();

    	brandGroupList.add(brandGroupVo);

    	groupVo.setBrandGroupList(brandGroupList);
    	groupList.add(groupVo);

    	companyList.add(legalVo);

    	vo.setCompanyList(companyList);
    	vo.setGroupList(groupList);

    	voList.add(vo);

    	given(mockPolicyBenefitEmployeeMapper.addPolicyBenefitEmployeeMaster(any())).willReturn(1);
    	given(mockPolicyBenefitEmployeeMapper.addPolicyBenefitEmployeeGroup(any())).willReturn(0);

    	// when
    	BaseException myException = assertThrows(BaseException.class, () -> {
    		mockPolicyBenefitEmployeeService.addPolicyBenefitEmployeeGroup(voList);
    	});

    	// then
    	assertEquals(BaseEnums.CommBase.VALID_ERROR, myException.getMessageEnum());
    }
    @Test
    public void test_혜택그룹_실패_하위데이터오류3() throws Exception {

    	List<PolicyBenefitEmployeeVo> voList = new ArrayList<PolicyBenefitEmployeeVo>();
    	PolicyBenefitEmployeeVo vo = new PolicyBenefitEmployeeVo();
    	List<PolicyBenefitEmployeeGroupVo> groupList = new ArrayList<PolicyBenefitEmployeeGroupVo>();
    	List<PolicyBenefitEmployeeGroupBrandGroupVo> brandGroupList = new ArrayList<PolicyBenefitEmployeeGroupBrandGroupVo>();
    	List<PolicyBenefitEmployeeLegalVo> companyList = new ArrayList<PolicyBenefitEmployeeLegalVo>();

    	PolicyBenefitEmployeeGroupVo groupVo = new PolicyBenefitEmployeeGroupVo();
    	PolicyBenefitEmployeeGroupBrandGroupVo brandGroupVo = new PolicyBenefitEmployeeGroupBrandGroupVo();
    	PolicyBenefitEmployeeLegalVo legalVo = new PolicyBenefitEmployeeLegalVo();

    	brandGroupList.add(brandGroupVo);

    	groupVo.setBrandGroupList(brandGroupList);
    	groupList.add(groupVo);

    	companyList.add(legalVo);

    	vo.setCompanyList(companyList);
    	vo.setGroupList(groupList);

    	voList.add(vo);

    	given(mockPolicyBenefitEmployeeMapper.addPolicyBenefitEmployeeMaster(any())).willReturn(1);
    	given(mockPolicyBenefitEmployeeMapper.addPolicyBenefitEmployeeGroup(any())).willReturn(1);
    	given(mockPolicyBenefitEmployeeMapper.addPolicyBenefitEmployeeGroupBrandGroup(any())).willReturn(0);

    	// when
    	BaseException myException = assertThrows(BaseException.class, () -> {
    		mockPolicyBenefitEmployeeService.addPolicyBenefitEmployeeGroup(voList);
    	});

    	// then
    	assertEquals(BaseEnums.CommBase.VALID_ERROR, myException.getMessageEnum());
    }
    @Test
    public void test_혜택그룹_실패_데이터오류() throws Exception {

    	List<PolicyBenefitEmployeeVo> voList = new ArrayList<PolicyBenefitEmployeeVo>();
    	PolicyBenefitEmployeeVo vo = new PolicyBenefitEmployeeVo();
    	List<PolicyBenefitEmployeeGroupVo> groupList = new ArrayList<PolicyBenefitEmployeeGroupVo>();
    	List<PolicyBenefitEmployeeGroupBrandGroupVo> brandGroupList = new ArrayList<PolicyBenefitEmployeeGroupBrandGroupVo>();
    	List<PolicyBenefitEmployeeLegalVo> companyList = new ArrayList<PolicyBenefitEmployeeLegalVo>();

    	PolicyBenefitEmployeeGroupVo groupVo = new PolicyBenefitEmployeeGroupVo();
    	PolicyBenefitEmployeeGroupBrandGroupVo brandGroupVo = new PolicyBenefitEmployeeGroupBrandGroupVo();
    	PolicyBenefitEmployeeLegalVo legalVo = new PolicyBenefitEmployeeLegalVo();

    	brandGroupList.add(brandGroupVo);

    	groupVo.setBrandGroupList(brandGroupList);
    	groupList.add(groupVo);

    	companyList.add(legalVo);

    	vo.setCompanyList(companyList);
    	vo.setGroupList(groupList);

    	voList.add(vo);

    	given(mockPolicyBenefitEmployeeMapper.addPolicyBenefitEmployeeMaster(any())).willReturn(1);
    	given(mockPolicyBenefitEmployeeMapper.addPolicyBenefitEmployeeGroup(any())).willReturn(1);
    	given(mockPolicyBenefitEmployeeMapper.addPolicyBenefitEmployeeGroupBrandGroup(any())).willReturn(1);
    	given(mockPolicyBenefitEmployeeMapper.addPolicyBenefitEmployeeLegal(any())).willReturn(0);

    	// when
    	BaseException myException = assertThrows(BaseException.class, () -> {
    		mockPolicyBenefitEmployeeService.addPolicyBenefitEmployeeGroup(voList);
    	});

    	// then
    	assertEquals(BaseEnums.CommBase.VALID_ERROR, myException.getMessageEnum());
    }

	@Test
	public void test_혜택그룹_수정() throws Exception {

    	List<PolicyBenefitEmployeeVo> voList = new ArrayList<PolicyBenefitEmployeeVo>();
    	PolicyBenefitEmployeeVo vo = new PolicyBenefitEmployeeVo();
    	List<PolicyBenefitEmployeeGroupVo> groupList = new ArrayList<PolicyBenefitEmployeeGroupVo>();
    	List<PolicyBenefitEmployeeGroupBrandGroupVo> brandGroupList = new ArrayList<PolicyBenefitEmployeeGroupBrandGroupVo>();
    	List<PolicyBenefitEmployeeLegalVo> companyList = new ArrayList<PolicyBenefitEmployeeLegalVo>();

    	PolicyBenefitEmployeeGroupVo groupVo = new PolicyBenefitEmployeeGroupVo();
    	PolicyBenefitEmployeeGroupBrandGroupVo brandGroupVo = new PolicyBenefitEmployeeGroupBrandGroupVo();
    	PolicyBenefitEmployeeLegalVo legalVo = new PolicyBenefitEmployeeLegalVo();

    	brandGroupList.add(brandGroupVo);

    	groupVo.setBrandGroupList(brandGroupList);
    	groupList.add(groupVo);

    	companyList.add(legalVo);

    	vo.setCompanyList(companyList);
    	vo.setGroupList(groupList);

    	voList.add(vo);


		given(mockPolicyBenefitEmployeeMapper.putPolicyBenefitEmployeeMaster(any())).willReturn(1);
		given(mockPolicyBenefitEmployeeMapper.delPolicyBenefitEmployeeGroupBrandGroup(any())).willReturn(1);
		given(mockPolicyBenefitEmployeeMapper.delPolicyBenefitEmployeeGroup(any())).willReturn(1);
		given(mockPolicyBenefitEmployeeMapper.addPolicyBenefitEmployeeGroup(any())).willReturn(1);
		given(mockPolicyBenefitEmployeeMapper.addPolicyBenefitEmployeeGroupBrandGroup(any())).willReturn(1);
		given(mockPolicyBenefitEmployeeMapper.delPolicyBenefitEmployeeLegal(any())).willReturn(1);
		given(mockPolicyBenefitEmployeeMapper.addPolicyBenefitEmployeeLegal(any())).willReturn(1);

        // when
		MessageCommEnum msgEnum = mockPolicyBenefitEmployeeService.putPolicyBenefitEmployeeGroup(voList);

        // then
        assertTrue(msgEnum.getCode().equals(ApiResult.success().getCode()));

	}
	@Test
	public void test_혜택그룹_수정_실패() throws Exception {

		List<PolicyBenefitEmployeeVo> voList = new ArrayList<PolicyBenefitEmployeeVo>();
		PolicyBenefitEmployeeVo vo = new PolicyBenefitEmployeeVo();
		List<PolicyBenefitEmployeeGroupVo> groupList = new ArrayList<PolicyBenefitEmployeeGroupVo>();
		List<PolicyBenefitEmployeeGroupBrandGroupVo> brandGroupList = new ArrayList<PolicyBenefitEmployeeGroupBrandGroupVo>();
		List<PolicyBenefitEmployeeLegalVo> companyList = new ArrayList<PolicyBenefitEmployeeLegalVo>();

		PolicyBenefitEmployeeGroupVo groupVo = new PolicyBenefitEmployeeGroupVo();
		PolicyBenefitEmployeeGroupBrandGroupVo brandGroupVo = new PolicyBenefitEmployeeGroupBrandGroupVo();
		PolicyBenefitEmployeeLegalVo legalVo = new PolicyBenefitEmployeeLegalVo();

		brandGroupList.add(brandGroupVo);

		groupVo.setBrandGroupList(brandGroupList);
		groupList.add(groupVo);

		companyList.add(legalVo);

		vo.setCompanyList(companyList);
		vo.setGroupList(groupList);

		voList.add(vo);


		given(mockPolicyBenefitEmployeeMapper.putPolicyBenefitEmployeeMaster(any())).willReturn(0);

		// when
		MessageCommEnum msgEnum = mockPolicyBenefitEmployeeService.putPolicyBenefitEmployeeGroup(voList);

		// then
    	assertTrue(msgEnum.getCode().equals(ApiResult.fail().getCode()));
	}
	@Test
	public void test_혜택그룹_수정_실패_기존데이터_삭제오류1() throws Exception {

		List<PolicyBenefitEmployeeVo> voList = new ArrayList<PolicyBenefitEmployeeVo>();
		PolicyBenefitEmployeeVo vo = new PolicyBenefitEmployeeVo();
		List<PolicyBenefitEmployeeGroupVo> groupList = new ArrayList<PolicyBenefitEmployeeGroupVo>();
		List<PolicyBenefitEmployeeGroupBrandGroupVo> brandGroupList = new ArrayList<PolicyBenefitEmployeeGroupBrandGroupVo>();
		List<PolicyBenefitEmployeeLegalVo> companyList = new ArrayList<PolicyBenefitEmployeeLegalVo>();

		PolicyBenefitEmployeeGroupVo groupVo = new PolicyBenefitEmployeeGroupVo();
		PolicyBenefitEmployeeGroupBrandGroupVo brandGroupVo = new PolicyBenefitEmployeeGroupBrandGroupVo();
		PolicyBenefitEmployeeLegalVo legalVo = new PolicyBenefitEmployeeLegalVo();

		brandGroupList.add(brandGroupVo);

		groupVo.setBrandGroupList(brandGroupList);
		groupList.add(groupVo);

		companyList.add(legalVo);

		vo.setCompanyList(companyList);
		vo.setGroupList(groupList);

		voList.add(vo);


		given(mockPolicyBenefitEmployeeMapper.putPolicyBenefitEmployeeMaster(any())).willReturn(1);
		given(mockPolicyBenefitEmployeeMapper.delPolicyBenefitEmployeeGroupBrandGroup(any())).willReturn(0);

		// when
    	BaseException myException = assertThrows(BaseException.class, () -> {
    		mockPolicyBenefitEmployeeService.putPolicyBenefitEmployeeGroup(voList);
    	});

    	// then
    	assertEquals(BaseEnums.CommBase.VALID_ERROR, myException.getMessageEnum());
	}
	@Test
	public void test_혜택그룹_수정_실패_기존데이터_삭제오류2() throws Exception {

		List<PolicyBenefitEmployeeVo> voList = new ArrayList<PolicyBenefitEmployeeVo>();
		PolicyBenefitEmployeeVo vo = new PolicyBenefitEmployeeVo();
		List<PolicyBenefitEmployeeGroupVo> groupList = new ArrayList<PolicyBenefitEmployeeGroupVo>();
		List<PolicyBenefitEmployeeGroupBrandGroupVo> brandGroupList = new ArrayList<PolicyBenefitEmployeeGroupBrandGroupVo>();
		List<PolicyBenefitEmployeeLegalVo> companyList = new ArrayList<PolicyBenefitEmployeeLegalVo>();

		PolicyBenefitEmployeeGroupVo groupVo = new PolicyBenefitEmployeeGroupVo();
		PolicyBenefitEmployeeGroupBrandGroupVo brandGroupVo = new PolicyBenefitEmployeeGroupBrandGroupVo();
		PolicyBenefitEmployeeLegalVo legalVo = new PolicyBenefitEmployeeLegalVo();

		brandGroupList.add(brandGroupVo);

		groupVo.setBrandGroupList(brandGroupList);
		groupList.add(groupVo);

		companyList.add(legalVo);

		vo.setCompanyList(companyList);
		vo.setGroupList(groupList);

		voList.add(vo);


		given(mockPolicyBenefitEmployeeMapper.putPolicyBenefitEmployeeMaster(any())).willReturn(1);
		given(mockPolicyBenefitEmployeeMapper.delPolicyBenefitEmployeeGroupBrandGroup(any())).willReturn(1);
		given(mockPolicyBenefitEmployeeMapper.delPolicyBenefitEmployeeGroup(any())).willReturn(0);

		// when
		BaseException myException = assertThrows(BaseException.class, () -> {
			mockPolicyBenefitEmployeeService.putPolicyBenefitEmployeeGroup(voList);
		});

		// then
		assertEquals(BaseEnums.CommBase.VALID_ERROR, myException.getMessageEnum());
	}
	@Test
	public void test_혜택그룹_삭제() throws Exception {

		PolicyBenefitEmployeeVo vo = new PolicyBenefitEmployeeVo();
		List<PolicyBenefitEmployeeGroupVo> groupList = new ArrayList<PolicyBenefitEmployeeGroupVo>();
		List<PolicyBenefitEmployeeGroupBrandGroupVo> brandGroupList = new ArrayList<PolicyBenefitEmployeeGroupBrandGroupVo>();
		List<PolicyBenefitEmployeeLegalVo> companyList = new ArrayList<PolicyBenefitEmployeeLegalVo>();

		PolicyBenefitEmployeeGroupVo groupVo = new PolicyBenefitEmployeeGroupVo();
		PolicyBenefitEmployeeGroupBrandGroupVo brandGroupVo = new PolicyBenefitEmployeeGroupBrandGroupVo();
		PolicyBenefitEmployeeLegalVo legalVo = new PolicyBenefitEmployeeLegalVo();

		brandGroupList.add(brandGroupVo);

		groupVo.setBrandGroupList(brandGroupList);
		groupList.add(groupVo);

		companyList.add(legalVo);

		vo.setCompanyList(companyList);
		vo.setGroupList(groupList);

		given(mockPolicyBenefitEmployeeMapper.delPolicyBenefitEmployeeGroupBrandGroup(any())).willReturn(1);
		given(mockPolicyBenefitEmployeeMapper.delPolicyBenefitEmployeeGroup(any())).willReturn(1);
		given(mockPolicyBenefitEmployeeMapper.delPolicyBenefitEmployeeLegal(any())).willReturn(1);
		given(mockPolicyBenefitEmployeeMapper.delPolicyBenefitEmployeeMaster(any())).willReturn(1);

		// when
		MessageCommEnum msgEnum = mockPolicyBenefitEmployeeService.delPolicyBenefitEmployeeGroup(vo);

		// then
		assertTrue(msgEnum.getCode().equals(ApiResult.success().getCode()));
	}
	@Test
	public void test_혜택그룹_삭제_실패_하위데이터삭제오류1() throws Exception {

		PolicyBenefitEmployeeVo vo = new PolicyBenefitEmployeeVo();
		List<PolicyBenefitEmployeeGroupVo> groupList = new ArrayList<PolicyBenefitEmployeeGroupVo>();
		List<PolicyBenefitEmployeeGroupBrandGroupVo> brandGroupList = new ArrayList<PolicyBenefitEmployeeGroupBrandGroupVo>();
		List<PolicyBenefitEmployeeLegalVo> companyList = new ArrayList<PolicyBenefitEmployeeLegalVo>();

		PolicyBenefitEmployeeGroupVo groupVo = new PolicyBenefitEmployeeGroupVo();
		PolicyBenefitEmployeeGroupBrandGroupVo brandGroupVo = new PolicyBenefitEmployeeGroupBrandGroupVo();
		PolicyBenefitEmployeeLegalVo legalVo = new PolicyBenefitEmployeeLegalVo();

		brandGroupList.add(brandGroupVo);

		groupVo.setBrandGroupList(brandGroupList);
		groupList.add(groupVo);

		companyList.add(legalVo);

		vo.setCompanyList(companyList);
		vo.setGroupList(groupList);

		given(mockPolicyBenefitEmployeeMapper.delPolicyBenefitEmployeeGroupBrandGroup(any())).willReturn(0);

		// when
    	BaseException myException = assertThrows(BaseException.class, () -> {
    		mockPolicyBenefitEmployeeService.delPolicyBenefitEmployeeGroup(vo);
    	});

    	// then
    	assertEquals(BaseEnums.CommBase.VALID_ERROR, myException.getMessageEnum());
	}
	@Test
	public void test_혜택그룹_삭제_실패_하위데이터삭제오류2() throws Exception {

		PolicyBenefitEmployeeVo vo = new PolicyBenefitEmployeeVo();
		List<PolicyBenefitEmployeeGroupVo> groupList = new ArrayList<PolicyBenefitEmployeeGroupVo>();
		List<PolicyBenefitEmployeeGroupBrandGroupVo> brandGroupList = new ArrayList<PolicyBenefitEmployeeGroupBrandGroupVo>();
		List<PolicyBenefitEmployeeLegalVo> companyList = new ArrayList<PolicyBenefitEmployeeLegalVo>();

		PolicyBenefitEmployeeGroupVo groupVo = new PolicyBenefitEmployeeGroupVo();
		PolicyBenefitEmployeeGroupBrandGroupVo brandGroupVo = new PolicyBenefitEmployeeGroupBrandGroupVo();
		PolicyBenefitEmployeeLegalVo legalVo = new PolicyBenefitEmployeeLegalVo();

		brandGroupList.add(brandGroupVo);

		groupVo.setBrandGroupList(brandGroupList);
		groupList.add(groupVo);

		companyList.add(legalVo);

		vo.setCompanyList(companyList);
		vo.setGroupList(groupList);

		given(mockPolicyBenefitEmployeeMapper.delPolicyBenefitEmployeeGroupBrandGroup(any())).willReturn(1);
		given(mockPolicyBenefitEmployeeMapper.delPolicyBenefitEmployeeGroup(any())).willReturn(0);

		// when
		BaseException myException = assertThrows(BaseException.class, () -> {
			mockPolicyBenefitEmployeeService.delPolicyBenefitEmployeeGroup(vo);
		});

		// then
		assertEquals(BaseEnums.CommBase.VALID_ERROR, myException.getMessageEnum());
	}
	@Test
	public void test_혜택그룹_삭제_실패_하위데이터삭제오류3() throws Exception {

		PolicyBenefitEmployeeVo vo = new PolicyBenefitEmployeeVo();
		List<PolicyBenefitEmployeeGroupVo> groupList = new ArrayList<PolicyBenefitEmployeeGroupVo>();
		List<PolicyBenefitEmployeeGroupBrandGroupVo> brandGroupList = new ArrayList<PolicyBenefitEmployeeGroupBrandGroupVo>();
		List<PolicyBenefitEmployeeLegalVo> companyList = new ArrayList<PolicyBenefitEmployeeLegalVo>();

		PolicyBenefitEmployeeGroupVo groupVo = new PolicyBenefitEmployeeGroupVo();
		PolicyBenefitEmployeeGroupBrandGroupVo brandGroupVo = new PolicyBenefitEmployeeGroupBrandGroupVo();
		PolicyBenefitEmployeeLegalVo legalVo = new PolicyBenefitEmployeeLegalVo();

		brandGroupList.add(brandGroupVo);

		groupVo.setBrandGroupList(brandGroupList);
		groupList.add(groupVo);

		companyList.add(legalVo);

		vo.setCompanyList(companyList);
		vo.setGroupList(groupList);

		given(mockPolicyBenefitEmployeeMapper.delPolicyBenefitEmployeeGroupBrandGroup(any())).willReturn(1);
		given(mockPolicyBenefitEmployeeMapper.delPolicyBenefitEmployeeGroup(any())).willReturn(1);
		given(mockPolicyBenefitEmployeeMapper.delPolicyBenefitEmployeeLegal(any())).willReturn(0);

		// when
		BaseException myException = assertThrows(BaseException.class, () -> {
			mockPolicyBenefitEmployeeService.delPolicyBenefitEmployeeGroup(vo);
		});

		// then
		assertEquals(BaseEnums.CommBase.VALID_ERROR, myException.getMessageEnum());
	}
	@Test
	public void test_혜택그룹_삭제_실패_데이터삭제오류() throws Exception {

		PolicyBenefitEmployeeVo vo = new PolicyBenefitEmployeeVo();
		List<PolicyBenefitEmployeeGroupVo> groupList = new ArrayList<PolicyBenefitEmployeeGroupVo>();
		List<PolicyBenefitEmployeeGroupBrandGroupVo> brandGroupList = new ArrayList<PolicyBenefitEmployeeGroupBrandGroupVo>();
		List<PolicyBenefitEmployeeLegalVo> companyList = new ArrayList<PolicyBenefitEmployeeLegalVo>();

		PolicyBenefitEmployeeGroupVo groupVo = new PolicyBenefitEmployeeGroupVo();
		PolicyBenefitEmployeeGroupBrandGroupVo brandGroupVo = new PolicyBenefitEmployeeGroupBrandGroupVo();
		PolicyBenefitEmployeeLegalVo legalVo = new PolicyBenefitEmployeeLegalVo();

		brandGroupList.add(brandGroupVo);

		groupVo.setBrandGroupList(brandGroupList);
		groupList.add(groupVo);

		companyList.add(legalVo);

		vo.setCompanyList(companyList);
		vo.setGroupList(groupList);

		given(mockPolicyBenefitEmployeeMapper.delPolicyBenefitEmployeeGroupBrandGroup(any())).willReturn(1);
		given(mockPolicyBenefitEmployeeMapper.delPolicyBenefitEmployeeGroup(any())).willReturn(1);
		given(mockPolicyBenefitEmployeeMapper.delPolicyBenefitEmployeeLegal(any())).willReturn(1);
		given(mockPolicyBenefitEmployeeMapper.delPolicyBenefitEmployeeMaster(any())).willReturn(0);

		// when
		BaseException myException = assertThrows(BaseException.class, () -> {
			mockPolicyBenefitEmployeeService.delPolicyBenefitEmployeeGroup(vo);
		});

		// then
		assertEquals(BaseEnums.CommBase.VALID_ERROR, myException.getMessageEnum());
	}

	@Test
	public void test_할인율브랜드그룹_등록() throws Exception {

		List<PolicyBenefitEmployeeBrandGroupVo> voList = new ArrayList<PolicyBenefitEmployeeBrandGroupVo>();
		PolicyBenefitEmployeeBrandGroupVo vo = new PolicyBenefitEmployeeBrandGroupVo();
		List<PolicyBenefitEmployeeBrandGroupBrandVo> brandList = new ArrayList<PolicyBenefitEmployeeBrandGroupBrandVo>();

    	PolicyBenefitEmployeeBrandGroupBrandVo brand = new PolicyBenefitEmployeeBrandGroupBrandVo();

    	brandList.add(brand);
    	vo.setBrandList(brandList);
    	voList.add(vo);

		given(mockPolicyBenefitEmployeeMapper.addPolicyBenefitEmployeeBrandGroup(any())).willReturn(1);
		given(mockPolicyBenefitEmployeeMapper.addPolicyBenefitEmployeeBrandGroupBrand(any())).willReturn(1);

        // when
		MessageCommEnum msgEnum = mockPolicyBenefitEmployeeService.addPolicyBenefitEmployeeBrandGroup(voList);

        // then
        assertTrue(msgEnum.getCode().equals(ApiResult.success().getCode()));

	}
	@Test
	public void test_할인율브랜드그룹_등록실패() throws Exception {

		List<PolicyBenefitEmployeeBrandGroupVo> voList = new ArrayList<PolicyBenefitEmployeeBrandGroupVo>();
		PolicyBenefitEmployeeBrandGroupVo vo = new PolicyBenefitEmployeeBrandGroupVo();
		List<PolicyBenefitEmployeeBrandGroupBrandVo> brandList = new ArrayList<PolicyBenefitEmployeeBrandGroupBrandVo>();

		PolicyBenefitEmployeeBrandGroupBrandVo brand = new PolicyBenefitEmployeeBrandGroupBrandVo();

		brandList.add(brand);
		vo.setBrandList(brandList);
		voList.add(vo);

		given(mockPolicyBenefitEmployeeMapper.addPolicyBenefitEmployeeBrandGroup(any())).willReturn(0);

		// when
//		MessageCommEnum msgEnum = mockPolicyBenefitEmployeeService.addPolicyBenefitEmployeeBrandGroup(voList);
		// then
//		assertTrue(msgEnum.getCode().equals(ApiResult.fail().getCode()));

		// when
    	BaseException myException = assertThrows(BaseException.class, () -> {
    		mockPolicyBenefitEmployeeService.addPolicyBenefitEmployeeBrandGroup(voList);
    	});

    	// then
    	assertEquals(BaseEnums.CommBase.VALID_ERROR, myException.getMessageEnum());


	}
	@Test
	public void test_할인율브랜드그룹_등록실패_하위데이터오류() throws Exception {

		List<PolicyBenefitEmployeeBrandGroupVo> voList = new ArrayList<PolicyBenefitEmployeeBrandGroupVo>();
		PolicyBenefitEmployeeBrandGroupVo vo = new PolicyBenefitEmployeeBrandGroupVo();
		List<PolicyBenefitEmployeeBrandGroupBrandVo> brandList = new ArrayList<PolicyBenefitEmployeeBrandGroupBrandVo>();

		PolicyBenefitEmployeeBrandGroupBrandVo brand = new PolicyBenefitEmployeeBrandGroupBrandVo();

		brandList.add(brand);
		vo.setBrandList(brandList);
		voList.add(vo);

		given(mockPolicyBenefitEmployeeMapper.addPolicyBenefitEmployeeBrandGroup(any())).willReturn(1);
		given(mockPolicyBenefitEmployeeMapper.addPolicyBenefitEmployeeBrandGroupBrand(any())).willReturn(0);

		// when
		BaseException myException = assertThrows(BaseException.class, () -> {
			mockPolicyBenefitEmployeeService.addPolicyBenefitEmployeeBrandGroup(voList);
		});

		// then
		assertEquals(BaseEnums.CommBase.VALID_ERROR, myException.getMessageEnum());


	}
	@Test
	public void test_할인율브랜드그룹_등록실패_데이터없음() throws Exception {

		List<PolicyBenefitEmployeeBrandGroupVo> voList = new ArrayList<PolicyBenefitEmployeeBrandGroupVo>();
		// when
		MessageCommEnum msgEnum = mockPolicyBenefitEmployeeService.addPolicyBenefitEmployeeBrandGroup(voList);
		// then
		assertTrue(msgEnum.getCode().equals(ApiResult.fail().getCode()));
	}
	@Test
	public void test_할인율브랜드그룹_수정() throws Exception {

		List<PolicyBenefitEmployeeBrandGroupVo> voList = new ArrayList<PolicyBenefitEmployeeBrandGroupVo>();
		PolicyBenefitEmployeeBrandGroupVo vo = new PolicyBenefitEmployeeBrandGroupVo();
		List<PolicyBenefitEmployeeBrandGroupBrandVo> brandList = new ArrayList<PolicyBenefitEmployeeBrandGroupBrandVo>();

		PolicyBenefitEmployeeBrandGroupBrandVo brand = new PolicyBenefitEmployeeBrandGroupBrandVo();

		brandList.add(brand);
		vo.setBrandList(brandList);
		voList.add(vo);

		given(mockPolicyBenefitEmployeeMapper.putPolicyBenefitEmployeeBrandGroup(any())).willReturn(1);
		given(mockPolicyBenefitEmployeeMapper.delPolicyBenefitEmployeeBrandGroupBrand(any())).willReturn(1);
		given(mockPolicyBenefitEmployeeMapper.addPolicyBenefitEmployeeBrandGroupBrand(any())).willReturn(1);

		// when
		MessageCommEnum msgEnum = mockPolicyBenefitEmployeeService.putPolicyBenefitEmployeeBrandGroup(voList);

		// then
		assertTrue(msgEnum.getCode().equals(ApiResult.success().getCode()));

	}
	@Test
	public void test_할인율브랜드그룹_수정실패() throws Exception {

		List<PolicyBenefitEmployeeBrandGroupVo> voList = new ArrayList<PolicyBenefitEmployeeBrandGroupVo>();
		PolicyBenefitEmployeeBrandGroupVo vo = new PolicyBenefitEmployeeBrandGroupVo();
		List<PolicyBenefitEmployeeBrandGroupBrandVo> brandList = new ArrayList<PolicyBenefitEmployeeBrandGroupBrandVo>();

		PolicyBenefitEmployeeBrandGroupBrandVo brand = new PolicyBenefitEmployeeBrandGroupBrandVo();

		brandList.add(brand);
		vo.setBrandList(brandList);
		voList.add(vo);

		given(mockPolicyBenefitEmployeeMapper.putPolicyBenefitEmployeeBrandGroup(any())).willReturn(0);
		given(mockPolicyBenefitEmployeeMapper.delPolicyBenefitEmployeeBrandGroupBrand(any())).willReturn(0);
		given(mockPolicyBenefitEmployeeMapper.addPolicyBenefitEmployeeBrandGroupBrand(any())).willReturn(0);

		// when
		MessageCommEnum msgEnum = mockPolicyBenefitEmployeeService.putPolicyBenefitEmployeeBrandGroup(voList);

		// then
		assertTrue(msgEnum.getCode().equals(ApiResult.fail().getCode()));

	}

	@Test
	public void test_할인율브랜드그룹_삭제() throws Exception {

		String psEmplDiscBrandGrpId = "";

		given(mockPolicyBenefitEmployeeMapper.delPolicyBenefitEmployeeBrandGroupBrand(any())).willReturn(1);
		given(mockPolicyBenefitEmployeeMapper.delPolicyBenefitEmployeeBrandGroup(any())).willReturn(1);

		// when
		MessageCommEnum msgEnum = mockPolicyBenefitEmployeeService.delPolicyBenefitEmployeeBrandGroup(psEmplDiscBrandGrpId);

		// then
		assertTrue(msgEnum.getCode().equals(ApiResult.success().getCode()));
	}
	@Test
	public void test_할인율브랜드그룹_삭제_실패() throws Exception {

		String psEmplDiscBrandGrpId = "";

		given(mockPolicyBenefitEmployeeMapper.delPolicyBenefitEmployeeBrandGroupBrand(any())).willReturn(0);
		given(mockPolicyBenefitEmployeeMapper.delPolicyBenefitEmployeeBrandGroup(any())).willReturn(0);

		// when
    	BaseException myException = assertThrows(BaseException.class, () -> {
    		mockPolicyBenefitEmployeeService.delPolicyBenefitEmployeeBrandGroup(psEmplDiscBrandGrpId);
    	});

    	// then
    	assertEquals(BaseEnums.CommBase.VALID_ERROR, myException.getMessageEnum());
	}

    @Test
    void getEmployeeDiscountByUser_조회_성공() throws Exception {
		//given
		String urErpEmployeeCd = "00001";

		//when
		List<PolicyBenefitEmployeeByUserVo> result = policyBenefitEmployeeService.getEmployeeDiscountByUser(urErpEmployeeCd);

		//then
		assertTrue(result.size() > 0);
    }

    @Test
    void getEmployeeDiscountBrandByUser_조회_성공() throws Exception {
		//given
		Long psEmplDiscMasterId = 43L;

		//when
		List<PolicyBenefitEmployeeBrandByUserVo> result = policyBenefitEmployeeService.getEmployeeDiscountBrandByUser(psEmplDiscMasterId);

		//then
		assertTrue(result.size() > 0);
    }

	@Test
	void getEmployeeDiscountPastByUser_조회_성공() throws Exception {
		//given
		String urErpEmployeeCd = "forbiz04";
		String startDate = "2021-04-01";
		String endDate = "2021-08-31";

		//when
		List<PolicyBenefitEmployeePastInfoByUserVo> result = policyBenefitEmployeeService.getEmployeeDiscountPastByUser(urErpEmployeeCd, startDate, endDate);

		//then
		assertTrue(result.size() > 0);
	}

	@Test
	void findEmployeeDiscountBrandByUser_성공() {
		//given
		List<PolicyBenefitEmployeeByUserVo> result = new ArrayList<>();
		PolicyBenefitEmployeeByUserVo vo1 = new PolicyBenefitEmployeeByUserVo();
		vo1.setPsEmplDiscMasterId(1L);

		List<PolicyBenefitEmployeeBrandByUserVo> blist = new ArrayList<>();
		PolicyBenefitEmployeeBrandByUserVo bvo1 = new PolicyBenefitEmployeeBrandByUserVo();
		bvo1.setUrBrandId(1L);
		blist.add(bvo1);

		List<PolicyBenefitEmployeeBrandGroupByUserVo> groupList = new ArrayList<>();
		PolicyBenefitEmployeeBrandGroupByUserVo groupVo1 = new PolicyBenefitEmployeeBrandGroupByUserVo();
		groupVo1.setBrand(blist);
		groupList.add(groupVo1);

		vo1.setList(groupList);
		result.add(vo1);

		//when
		PolicyBenefitEmployeeByUserVo vo = policyBenefitEmployeeService.findEmployeeDiscountBrandByUser(result, 1L);

		//then
		assertEquals(1L, vo.getPsEmplDiscMasterId());
	}

	@Test
	void findEmployeeDiscountBrandByUser_실패() throws Exception {
		//given
		List<PolicyBenefitEmployeeByUserVo> result = new ArrayList<PolicyBenefitEmployeeByUserVo>();
		PolicyBenefitEmployeeByUserVo vo1 = new PolicyBenefitEmployeeByUserVo();
		vo1.setPsEmplDiscMasterId(1L);
		List<PolicyBenefitEmployeeBrandByUserVo> blist = new ArrayList<PolicyBenefitEmployeeBrandByUserVo>();
		PolicyBenefitEmployeeBrandByUserVo bvo1 = new PolicyBenefitEmployeeBrandByUserVo();
		bvo1.setUrBrandId(1L);
		blist.add(bvo1);

		List<PolicyBenefitEmployeeBrandGroupByUserVo> groupList = new ArrayList<>();
		PolicyBenefitEmployeeBrandGroupByUserVo groupVo1 = new PolicyBenefitEmployeeBrandGroupByUserVo();
		groupVo1.setBrand(blist);
		groupList.add(groupVo1);

		vo1.setList(groupList);
		result.add(vo1);

		//when
		PolicyBenefitEmployeeByUserVo vo = policyBenefitEmployeeService.findEmployeeDiscountBrandByUser(result, 2L);

		//then
		assertNull(vo);
	}
}
