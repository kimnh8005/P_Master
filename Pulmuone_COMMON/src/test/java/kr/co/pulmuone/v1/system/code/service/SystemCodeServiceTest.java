package kr.co.pulmuone.v1.system.code.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.system.code.dto.*;
import kr.co.pulmuone.v1.system.code.dto.vo.GetCodeListResultVo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


import static org.junit.jupiter.api.Assertions.*;

class SystemCodeServiceTest extends CommonServiceTestBaseForJunit5 {
    @Autowired
    private SystemCodeService service;

    @BeforeEach
    void beforeEach() {
        preLogin();
    }

    @Test
    void 코드_마스터_리스트_조회() {
        GetCodeMasterListResponseDto result =  service.getCodeMasterList(new GetCodeMasterListRequestDto());
        boolean total = result.getTotal() > 0;
        assertTrue(total);
    }

    @Test
    void 코드_마스터_등록_수정_삭제() {
        CodeMasterRequestDto dto = CodeMasterRequestDto.builder()
                .commonMasterCode("TEST_MASTER_CODE")
                .stComnCodeMstId("TEST_MASTER_CODE")
                .commonMasterName("테스트 마스터 코드")
                .comment("코드 테스트")
                .useYn("N")
                .build();

        boolean duplFlag = service.isDuplicateCommonMasterCode(dto);
        assertFalse(duplFlag);

        int flag = service.addCodeMaster(dto);
        assertEquals(1, flag);

        flag = service.putCodeMaster(dto);
        assertEquals(1, flag);

        flag = service.delCodeMaster(dto.getCommonMasterCode());
        assertEquals(1, flag);

    }

    @Test
    void 코드_마스터_이름_리스트() {
        GetCodeMasterNameListRequestDto dto = GetCodeMasterNameListRequestDto.builder()
                .conditionType("COMMON_MASTER_CODE")
                .conditionValue("PO_TYPE")
                .build();
        GetCodeMasterNameListResponseDto result = service.getCodeMasterNameList(dto);

        boolean flag = result.getRows().stream().allMatch(i -> "PO_TYPE".equals(i.getCommonMasterCode()));

        assertTrue(flag);
    }

    @Test
    void 코드_리스트_조회() {
        GetCodeListRequestDto dto = new GetCodeListRequestDto("PO_TYPE", "Y");

        GetCodeListResponseDto result = service.getCodeList(dto);

        boolean flag = result.getRows().stream().allMatch(i -> "PO_TYPE".equals(i.getStCommonCodeMasterId()));

        assertTrue(flag);
    }

    @Test
    void 코드_등록_수정_삭제() {
        SaveCodeRequestSaveDto dto = SaveCodeRequestSaveDto.builder()
                .stCommonCodeId("PO_TYPE.TEST")
                .stCommonCodeMasterId("PO_TYPE")
                .commonMasterCode("PO_TYPE")
                .commonCode("TEST")
                .gbDictionaryMasterId("100001826")
                .useYn("N")
                .sort("3")
                .comment("")
                .attribute1("")
                .attribute2("")
                .attribute3("")
                .build();

        int flag = service.addCode(dto);
        assertEquals(1, flag);

        flag = service.putCode(dto);
        assertEquals(1, flag);

        flag = service.delCode(dto);
        assertEquals(1, flag);
    }

    @Test
    void getCode_성공(){
    	String stComnCodeId = "QNA_ONETOONE_TP.PRODUCT";

    	GetCodeListResultVo vo = service.getCode(stComnCodeId);

    	assertTrue(!vo.getCommonCode().isEmpty());
    }

    @Test
    void getCode_조회_결과_없음(){
    	String stComnCodeId = "QNA_ONETOONE_TP";

    	GetCodeListResultVo vo = service.getCode(stComnCodeId);

    	assertNull(vo);
    }
}