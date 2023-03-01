package kr.co.pulmuone.v1.policy.shiparea.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.mapper.policy.shiparea.PolicyShipareaMapper;
import kr.co.pulmuone.v1.policy.shiparea.dto.GetBackCountryListResponseDto;
import kr.co.pulmuone.v1.policy.shiparea.dto.GetBackCountryResponseDto;
import kr.co.pulmuone.v1.policy.shiparea.dto.PolicyShipareaDto;
import kr.co.pulmuone.v1.policy.shiparea.dto.vo.GetBackCountryResultVo;
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

public class PolicyShipareaServiceTest extends CommonServiceTestBaseForJunit5{

	@Autowired
	private PolicyShipareaService policyShipAreaService;

	@InjectMocks
	private PolicyShipareaService mockPolicyShipareaService;

	@Mock
	PolicyShipareaMapper mockPolicyShipareaMapper;

	@BeforeEach
	void setUp() {
		preLogin();
	}

	@Test
    public void test_도서산간목록조회()  {

		PolicyShipareaDto paramDto = new PolicyShipareaDto();

		GetBackCountryListResponseDto result = policyShipAreaService.getBackCountryList(paramDto);
		// then
        assertNotNull(result.getRows());
    }

	@Test
    public void test_도서산간입력()  {

		PolicyShipareaDto addParam = new PolicyShipareaDto();

		addParam.setZipCode("99978");

		String areaType = "JEJU";

		if ("JEJU".equals(areaType))
			addParam.setJejuYn("Y");
		else if ("ISLAND".equals(areaType))
			addParam.setIslandYn("Y");

		ApiResult<?> result = policyShipAreaService.addBackCountry(addParam);

		// then
		assertFalse(result.getCode().equals(ApiResult.fail().getCode()));
    }

	@Test
    public void test_도서산간수정()  {
		//given
		PolicyShipareaDto putParam = new PolicyShipareaDto();
		putParam.setZipCode("15654");
		putParam.setIslandYn("Y");

		//when
		ApiResult<?> result = policyShipAreaService.putBackCountry(putParam);

		// then
		assertEquals(BaseEnums.Default.SUCCESS.getCode(), result.getCode());
    }

	@Test
    public void test_도서산간삭제()  {
		//given
		PolicyShipareaDto delParam = new PolicyShipareaDto();
		delParam.setZipCodes("15654".split(","));

		//when
		ApiResult<?> result = policyShipAreaService.delBackCountry(delParam);

		// then
		assertEquals(BaseEnums.Default.SUCCESS.getCode(), result.getCode());
    }

	@Test
    public void test_도서산간조회()  {
		//given
		PolicyShipareaDto paramDto = new PolicyShipareaDto();
		paramDto.setZipCode("15654");

		//when
		GetBackCountryResponseDto result = policyShipAreaService.getBackCountry(paramDto);

		// then
		assertNotNull(result.getRows());
    }

    @Test
    void 도서산관_권역설정_중복_검사_duplicateBackCountryCount() {
		PolicyShipareaDto policyShipareaDto = new PolicyShipareaDto();
		policyShipareaDto.setZipCode("151550");

		given(mockPolicyShipareaService.duplicateBackCountryCount(any())).willReturn(1);

		//when
		int result = mockPolicyShipareaService.duplicateBackCountryCount(policyShipareaDto);

		//then
		assertEquals(1, result);
    }

    @Test
    void 도서산관_권역설정_추가_addBackCountry() {
		PolicyShipareaDto policyShipareaDto = new PolicyShipareaDto();
		policyShipareaDto.setZipCode("151550");
		policyShipareaDto.setJejuYn("N");
		policyShipareaDto.setIslandYn("Y");

		given(mockPolicyShipareaMapper.addBackCountry(any())).willReturn(1);

		//when
		ApiResult result = mockPolicyShipareaService.addBackCountry(policyShipareaDto);

		//then
		assertEquals(ApiResult.success().getCode(), result.getCode());
    }

    @Test
    void 도서산관_권역설정_상세_조회_getBackCountryExcelList() {
		String[] zipCodes = {"112333","444567"};
		String excelFileName = "back_country";
		GetBackCountryResultVo getBackCountryResultVo = new GetBackCountryResultVo();

		List<GetBackCountryResultVo> rows = new ArrayList<>();
		rows.add(getBackCountryResultVo);

		given(mockPolicyShipareaMapper.getBackCountryExcelList(any())).willReturn(rows);

		//when
		ExcelDownloadDto result = mockPolicyShipareaService.getBackCountryExcelList(zipCodes);

		//then
		assertNotNull(result);
    }
}
