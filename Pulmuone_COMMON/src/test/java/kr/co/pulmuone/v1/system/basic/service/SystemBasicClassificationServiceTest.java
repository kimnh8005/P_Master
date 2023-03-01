package kr.co.pulmuone.v1.system.basic.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.system.basic.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class SystemBasicClassificationServiceTest extends CommonServiceTestBaseForJunit5 {
    @Autowired
    private SystemBasicClassificationService service;

    @BeforeEach
    void beforeEach() {
        preLogin();
    }

    @Test
    void 분류_등록_검색_수정_삭제() {
        SaveClassificationRequestDto insDto = SaveClassificationRequestDto.builder()
                .classificationTp("CLASSIFICATION_CODE.COUPON")
                .depth("3")
                .parentsClassificationId("128")
                .useYn("Y")
                .sort("2")
                .gbDictionaryMasterId(1)
                .build();

        // 유효성체크
        boolean booleanFlag = service.checkClassificationDuplicate(insDto);
        assertTrue(booleanFlag);

        // 등록
        int flag = service.addClassification(insDto);
        assertEquals(1, flag);


        GetClassificationListParamDto searchDto = GetClassificationListParamDto.builder()
                .useYn("Y")
                .build();

        GetClassificationListResponseDto searchVo = service.getClassificationList(searchDto);
        booleanFlag = searchVo.getRows().stream()
                .anyMatch(m -> "Y".equals(m.getUseYn()));
        assertTrue(booleanFlag);


        GetClassificationResponseDto dtlDto = service.getClassification(insDto.getId());
        if("CLASSIFICATION_CODE.COUPON".equals(dtlDto.getRows().getClassificationTp())) {
            booleanFlag = true;
        } else {
            booleanFlag = false;
        }
        assertTrue(booleanFlag);

        flag = service.putClassification(insDto);
        assertEquals(1, flag);

        flag = service.delClassification(insDto.getId());
        assertEquals(1, flag);
    }

    @Test
    void 분류코드_리스트() {
        GetTypeListResponseDto typeList = service.getTypeList();
        boolean booleanFlag = typeList.getRows().size() > 0;

        assertTrue(booleanFlag);
    }
}