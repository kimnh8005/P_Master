package kr.co.pulmuone.v1.promotion.advertising.service;

import kr.co.pulmuone.v1.base.dto.GetCodeListResponseDto;
import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.promotion.advertising.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class PromotionAdvertisingServiceTest extends CommonServiceTestBaseForJunit5 {
    @Autowired
    PromotionAdvertisingService promotionAdvertisingService;

    @Test
    void getAdvertisingExternalList_조회_성공() throws Exception {
        //given
        AdvertisingExternalListRequestDto dto = new AdvertisingExternalListRequestDto();
        dto.setSource("");
        dto.setMedium("");
        dto.setCampaign("");
        dto.setContent("");
        dto.setUseYnFilter("ALL");
        dto.setCreateStartDate("20210101");
        dto.setCreateEndDate("20210830");
        dto.setModifyStartDate("20210101");
        dto.setModifyEndDate("20210830");

        //when
        AdvertisingExternalListResponseDto result = promotionAdvertisingService.getAdvertisingExternalList(dto);

        //then
        assertTrue(result.getTotal() > 0);
    }

    @Test
    void getAdvertisingExternal_조회_성공_단건() throws Exception {
        //given
        AdvertisingExternalRequestDto dto = new AdvertisingExternalRequestDto();
        dto.setPmAdExternalCd("linkprice");

        //when
        AdvertisingExternalResponseDto result = promotionAdvertisingService.getAdvertisingExternal(dto);

        //then
        assertTrue(result.getAdvertisingExternalList().size() > 0);
    }

    @Test
    void getAdvertisingExternal_조회_성공_다건() throws Exception {
        //given
        AdvertisingExternalRequestDto dto = new AdvertisingExternalRequestDto();
        dto.setPmAdExternalCdList(Collections.singletonList("linkprice"));

        //when
        AdvertisingExternalResponseDto result = promotionAdvertisingService.getAdvertisingExternal(dto);

        //then
        assertTrue(result.getAdvertisingExternalList().size() > 0);
    }

    @Test
    void addAdvertisingExternalList_등록() throws Exception {
        //given
        List<AddAdvertisingExternalRequestDto> dtoList = new ArrayList<>();
        AddAdvertisingExternalRequestDto dto = AddAdvertisingExternalRequestDto.builder()
                .pmAdExternalCd("test")
                .advertisingName("광고명")
                .source("source")
                .medium("medium")
                .campaign("campaign")
                .content("content")
                .redirectUrl("redirect")
                .useYn("Y")
                .userId("1")
                .build();
        dtoList.add(dto);

        //when, then
        promotionAdvertisingService.addAdvertisingExternalList(dtoList);
    }

    @Test
    void addAdvertisingExternal_추가() throws Exception {
        //given
        AddAdvertisingExternalRequestDto dto = AddAdvertisingExternalRequestDto.builder()
                .pmAdExternalCd("testtesttest")
                .advertisingName("광고명")
                .source("source")
                .medium("source" + Constants.ARRAY_SEPARATORS + "medium")
                .campaign("source" + Constants.ARRAY_SEPARATORS + "medium" + Constants.ARRAY_SEPARATORS + "campaign")
                .content("source" + Constants.ARRAY_SEPARATORS + "medium" + Constants.ARRAY_SEPARATORS + "campaign" + Constants.ARRAY_SEPARATORS + "content")
                .redirectUrl("redirect")
                .useYn("Y")
                .userId("1")
                .build();

        //when, then
        promotionAdvertisingService.addAdvertisingExternal(dto);
    }

    @Test
    void putAdvertisingExternal_수정() throws Exception {
        //given
        AddAdvertisingExternalRequestDto dto = AddAdvertisingExternalRequestDto.builder()
                .pmAdExternalCd("test")
                .advertisingName("광고명")
                .source("source")
                .medium("medium")
                .campaign("campaign")
                .content("content")
                .redirectUrl("redirect")
                .useYn("Y")
                .userId("1")
                .build();

        //when, then
        promotionAdvertisingService.putAdvertisingExternal(dto);
    }

    @Test
    void getAdvertisingType_유형조회() throws Exception {
        //given
        AdvertisingTypeRequestDto dto = new AdvertisingTypeRequestDto();
        dto.setSearchType("SOURCE");

        //when
        GetCodeListResponseDto result = promotionAdvertisingService.getAdvertisingType(dto);

        //then
        assertTrue(result.getRows().size() > 0);
    }

    @Test
    void isExistPmAdExternalCd() throws Exception {
        //given
        String pmAdExternalCd = "testtsetsetset";

        //when
        boolean result = promotionAdvertisingService.isExistPmAdExternalCd(pmAdExternalCd);

        //then
        assertFalse(result);
    }

}