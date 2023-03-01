package kr.co.pulmuone.v1.policy.excel.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.mapper.policy.excel.PolicyExcelTmpltMapper;
import kr.co.pulmuone.v1.policy.excel.dto.PolicyExcelTmpltDto;
import kr.co.pulmuone.v1.policy.excel.dto.vo.PolicyExcelTmpltVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@Slf4j
class PolicyExcelTmpltServiceTest extends CommonServiceTestBaseForJunit5 {

	@Autowired
    private PolicyExcelTmpltService policyExcelTmpltService;

	@InjectMocks
	private PolicyExcelTmpltService mockPolicyExcelTmpltService;

	@Mock
	PolicyExcelTmpltMapper mockPolicyExcelTmpltMapper;

	@BeforeEach
	void setUp() {
		preLogin();
	}

	@Test
	public void test_등록된양식_결과있음()  {
		// given
    	String psExcelTemplateId = "81";

    	// when
    	PolicyExcelTmpltVo result = policyExcelTmpltService.getPolicyExcelTmpltInfo(psExcelTemplateId);

		// then
		assertNotNull(result);
	}

    @Test
    public void test_등록된양식목록_결과있음()  {

    	// given
    	String excelTemplateTp = "EXCEL_TEMPLATE_TP.MASTER_ITEM";

    	PolicyExcelTmpltVo vo = new PolicyExcelTmpltVo();
		vo.setExcelTemplateTp(excelTemplateTp);

        // when
    	PolicyExcelTmpltDto result = policyExcelTmpltService.getPolicyExcelTmpltList(vo);

    	// then
        assertNotNull(result.getRows());
    }

    @Test
    public void test_엑셀양식_신규등록() {
    	String excelTemplateTp = "EXCEL_TEMPLATE_TP.MASTER_ITEM";
    	String excelTemplateUseTp = "EXCEL_TEMPLATE_USE_TP.DOWNLOAD";
    	String templateNm = "test_엑셀양식신규등록";
    	String templateData = "[{\"EXCEL_COL\":\"A\",\"CODE\":\"EXCEL_TEMPLATE_ITEM.TEST_B\",\"NAME\":\"MDM 품목코드\"},{\"EXCEL_COL\":\"B\",\"CODE\":\"EXCEL_TEMPLATE_ITEM.TEST_A\",\"NAME\":\"MDM 품목명\"}]";
    	String urUserId = "1";
    	String urCompanyId = "1";
    	String personalUseYn = "N";

    	 // given
    	PolicyExcelTmpltDto dto = new PolicyExcelTmpltDto();
    	dto.setExcelTemplateTp(excelTemplateTp);
    	dto.setExcelTemplateUseTp(excelTemplateUseTp);
    	dto.setTemplateNm(templateNm);
    	dto.setTemplateData(templateData);
    	dto.setUrUserId(urUserId);
    	dto.setUrCompanyId(urCompanyId);
    	dto.setPersonalUseYn(personalUseYn);
        log.info("dto: {}", dto);

        // when
        int count = policyExcelTmpltService.addPolicyExcelTmplt(dto);

        // then
        assertEquals(1, count);
    }

    @Test
    public void test_엑셀양식_수정() {
    	// given
    	PolicyExcelTmpltDto dto = new PolicyExcelTmpltDto();
    	dto.setPsExcelTemplateId("81");
    	dto.setExcelTemplateTp("EXCEL_TEMPLATE_TP.MASTER_ITEM");
    	dto.setExcelTemplateUseTp("EXCEL_TEMPLATE_USE_TP.DOWNLOAD");
    	dto.setTemplateNm("test_엑셀양식신규등록");
    	dto.setTemplateData("[{\"EXCEL_COL\":\"A\",\"CODE\":\"EXCEL_TEMPLATE_ITEM.TEST_B\",\"NAME\":\"MDM 품목코드\"},{\"EXCEL_COL\":\"B\",\"CODE\":\"EXCEL_TEMPLATE_ITEM.TEST_A\",\"NAME\":\"MDM 품목명\"}]");
    	dto.setUrUserId("1");
    	dto.setUrCompanyId("1");
    	dto.setPersonalUseYn("N");
        log.info("dto: {}", dto);

        // when
        int count = policyExcelTmpltService.putPolicyExcelTmplt(dto);

        // then
        assertEquals(1, count);
    }

    @Test
    public void test_엑셀양식_삭제() {
    	// given
    	String psExcelTemplateId = "81";

    	// when
    	int count = policyExcelTmpltService.delPolicyExcelTmplt(psExcelTemplateId);

    	// then
    	assertEquals(1, count);
    }

    @Test
    void 엑셀양식관리_설정_신규_등록_addPolicyExcelTmplt() {
		PolicyExcelTmpltDto policyExcelTmpltDto = new PolicyExcelTmpltDto();
		policyExcelTmpltDto.setExcelTemplateTp("01");
		policyExcelTmpltDto.setExcelTemplateUseTp("01");
		policyExcelTmpltDto.setTemplateNm("common");
		policyExcelTmpltDto.setTemplateData("20201201");
		policyExcelTmpltDto.setUrCompanyId("1");
		policyExcelTmpltDto.setPersonalUseYn("Y");

		given(mockPolicyExcelTmpltService.addPolicyExcelTmplt(any())).willReturn(1);

		//when
		int result = mockPolicyExcelTmpltMapper.addPolicyExcelTmplt(policyExcelTmpltDto);

		//then
		assertEquals(1, result);
    }
}
