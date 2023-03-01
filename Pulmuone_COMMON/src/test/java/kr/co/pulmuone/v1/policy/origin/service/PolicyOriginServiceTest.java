package kr.co.pulmuone.v1.policy.origin.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.policy.origin.dto.basic.AddPolicyOriginRequestDto;
import kr.co.pulmuone.v1.policy.origin.dto.basic.DelPolicyOriginRequestDto;
import kr.co.pulmuone.v1.policy.origin.dto.basic.DuplicatePolicyOriginCountParamDto;
import kr.co.pulmuone.v1.policy.origin.dto.basic.GetPolicyOriginListRequestDto;
import kr.co.pulmuone.v1.policy.origin.dto.basic.GetPolicyOriginRequestDto;
import kr.co.pulmuone.v1.policy.origin.dto.basic.GetPolicyOriginTypeListRequestDto;
import kr.co.pulmuone.v1.policy.origin.dto.basic.PutPolicyOriginRequestDto;
import kr.co.pulmuone.v1.policy.origin.dto.vo.GetPolicyOriginListResultVo;
import kr.co.pulmuone.v1.policy.origin.dto.vo.GetPolicyOriginResultVo;
import kr.co.pulmuone.v1.policy.origin.dto.vo.GetPolicyOriginTypeListResultVo;

class PolicyOriginServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    private PolicyOriginService policyOriginService;

    @BeforeEach
    void beforeEach() {
        preLogin();
    }

    @Test
    void 원산지_목록_조회() {
        GetPolicyOriginListRequestDto getPolicyOriginListRequestDto = new GetPolicyOriginListRequestDto();
        List<GetPolicyOriginListResultVo> getPolicyOriginListResultVo = policyOriginService.getOriginList(getPolicyOriginListRequestDto);

        assertTrue(CollectionUtils.isNotEmpty(getPolicyOriginListResultVo));
    }

    @Test
    void 원산지_구분목록_조회() {
        GetPolicyOriginTypeListRequestDto getPolicyOriginTypeListRequestDto = new GetPolicyOriginTypeListRequestDto();
        List<GetPolicyOriginTypeListResultVo> getPolicyOriginTypeListResultVo = policyOriginService.getOriginTypeList(getPolicyOriginTypeListRequestDto);

        assertTrue(CollectionUtils.isNotEmpty(getPolicyOriginTypeListResultVo));
    }

    @Test
    void 원산지_상세_조회_성공() {
        GetPolicyOriginRequestDto getPolicyOriginRequestDto = new GetPolicyOriginRequestDto();
        getPolicyOriginRequestDto.setSystemCommonCodeId("10");
        GetPolicyOriginResultVo getPolicyOriginResultVo = policyOriginService.getOrigin(getPolicyOriginRequestDto);

        assertEquals("10", getPolicyOriginResultVo.getSystemCommonCodeId());
    }

    @Test
    void 원산지_상세_조회_실패() {
        GetPolicyOriginRequestDto getPolicyOriginRequestDto = new GetPolicyOriginRequestDto();
        getPolicyOriginRequestDto.setSystemCommonCodeId("99999999");
        GetPolicyOriginResultVo getPolicyOriginResultVo = policyOriginService.getOrigin(getPolicyOriginRequestDto);

        assertFalse(getPolicyOriginResultVo != null);
    }

    @Test
    void 원산지_등록() {
        AddPolicyOriginRequestDto addPolicyOriginRequestDto = new AddPolicyOriginRequestDto();
        addPolicyOriginRequestDto.setSystemCommonCodeId("1");
        addPolicyOriginRequestDto.setOriginType("국외");
        addPolicyOriginRequestDto.setOriginCode("US");
        addPolicyOriginRequestDto.setGbDictionaryMasterId("100");

        int count = policyOriginService.addOrigin(addPolicyOriginRequestDto);

        assertTrue(count > 0);

    }

    @Test
    void 원산지_수정() {
        PutPolicyOriginRequestDto putPolicyOriginRequestDto = new PutPolicyOriginRequestDto();
        putPolicyOriginRequestDto.setSystemCommonCodeId("국외.111");
        putPolicyOriginRequestDto.setOriginType("국외");
        putPolicyOriginRequestDto.setOriginCode("111");
        putPolicyOriginRequestDto.setGbDictionaryMasterId("100");

        int count = policyOriginService.putOrigin(putPolicyOriginRequestDto);

        assertTrue(count > 0);
    }

    @Test
    void 원산지_삭제() {
        DelPolicyOriginRequestDto delPolicyOriginRequestDto = new DelPolicyOriginRequestDto();
        delPolicyOriginRequestDto.setSystemCommonCodeId("국외.22");

        int count = policyOriginService.delOrigin(delPolicyOriginRequestDto);

        assertEquals(1, count);
    }

    @Test
    void 원산지_중복_체크() {
        DuplicatePolicyOriginCountParamDto duplicatePolicyOriginCountParamDto = new DuplicatePolicyOriginCountParamDto();
        duplicatePolicyOriginCountParamDto.setSystemCommonCodeId("10");

        int count = policyOriginService.duplicateOriginCount(duplicatePolicyOriginCountParamDto);

        assertTrue(count > 0);
    }

    @Test
    void 원산지_목록_조회_엑셀다운로드() {
    	GetPolicyOriginListRequestDto getPolicyOriginListRequestDto = new GetPolicyOriginListRequestDto();
    	ExcelDownloadDto result = policyOriginService.getOriginListExportExcel(getPolicyOriginListRequestDto);

        assertNotNull(result);
    }


}
