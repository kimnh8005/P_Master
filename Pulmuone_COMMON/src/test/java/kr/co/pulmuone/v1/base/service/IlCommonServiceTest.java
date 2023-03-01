package kr.co.pulmuone.v1.base.service;

import kr.co.pulmuone.v1.base.dto.CategoryRequestDto;
import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class IlCommonServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    IlCommonService ilCommonService;
    @Test
    void 전시_카테고리_조회() {
        CategoryRequestDto dto = new CategoryRequestDto();
        dto.setCategoryId("4714");
        ApiResult<?> apiResult = ilCommonService.getDropDownCategoryList(dto);

        assertTrue(BaseEnums.Default.SUCCESS.getCode() == apiResult.getCode());
    }

    @Test
    void 표준_카테고리_조회() {
        CategoryRequestDto dto = new CategoryRequestDto();
        dto.setCategoryId("10098");
        ApiResult<?> apiResult = ilCommonService.getDropDownCategoryStandardList(dto);

        assertTrue(BaseEnums.Default.SUCCESS.getCode() == apiResult.getCode());
    }
}