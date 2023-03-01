package kr.co.pulmuone.v1.display.contents.service;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.constants.DisplayConstants;
import kr.co.pulmuone.v1.comm.enums.DisplayEnums;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.display.contents.dto.*;
import kr.co.pulmuone.v1.display.contents.dto.vo.ContentsDetailVo;
import kr.co.pulmuone.v1.goods.search.GoodsSearchBiz;
import kr.co.pulmuone.v1.search.searcher.dto.GoodsSearchResultDto;
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

class DisplayContentsServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    DisplayContentsService displayContentsService;

    @InjectMocks
    private DisplayContentsService mockDisplayContentsService;

    @Mock
    GoodsSearchBiz mockGoodsSearchBiz;

    @BeforeEach
    void setUp() {
        buyerLogin();
        mockDisplayContentsService = new DisplayContentsService(mockGoodsSearchBiz);
    }

    @Test
    void getInventoryContentsInfoList_조회_정상() throws Exception {
        //given
        String pageCd = "21-Main";
        String deviceType = DisplayEnums.DeviceType.PC.getCode();
        String userType = DisplayEnums.UserType.NORMAL.getCode();

        //when
        buyerLogin();
        List<InventoryContentsInfoResponseDto> getContentsInfoResponseDto = displayContentsService.getInventoryContentsInfoList(pageCd, deviceType, userType);

        //then
        assertTrue(getContentsInfoResponseDto.size() > 0);
    }

    @Test
    void getInventoryContentsInfoList_조회_실패() throws Exception {
        //given
        String pageCd = "none";
        String deviceType = DisplayEnums.DeviceType.PC.getCode();
        String userType = DisplayEnums.UserType.NORMAL.getCode();

        //when
        List<InventoryContentsInfoResponseDto> getContentsInfoResponseDto = displayContentsService.getInventoryContentsInfoList(pageCd, deviceType, userType);

        //then
        assertFalse(getContentsInfoResponseDto.size() > 0);
    }


    @Test
    void getContentsInfo_조회_정상() throws Exception {
        //given
        Long dpContsId = 5951L;
        String deviceType = DisplayEnums.DeviceType.PC.getCode();
        String userType = DisplayEnums.UserType.NORMAL.getCode();
        String goodsSortCode = DisplayEnums.GoodsSortCode.DEFAULT.getCode();

        //when
        ContentsInfoResponseDto contentsInfoResponseDto = displayContentsService.getContentsInfo(dpContsId, deviceType, userType, goodsSortCode);

        //then
        assertTrue(contentsInfoResponseDto.getLevel2TotalCount() > 0);
    }

    @Test
    void getContentsInfo_조회_실패() throws Exception {
        //given
        Long dpContsId = null;
        String deviceType = DisplayEnums.DeviceType.PC.getCode();
        String userType = DisplayEnums.UserType.NORMAL.getCode();
        String goodsSortCode = DisplayEnums.GoodsSortCode.DEFAULT.getCode();

        //when
        ContentsInfoResponseDto contentsInfoResponseDto = displayContentsService.getContentsInfo(dpContsId, deviceType, userType, goodsSortCode);

        //then
        assertFalse(contentsInfoResponseDto.getLevel2TotalCount() > 0);
    }

    @Test
    void getContentsDetail_조회_정상() throws Exception {
        //given
        Long dpContsId = 2L;
        String deviceType = DisplayEnums.DeviceType.PC.getCode();
        String userType = DisplayEnums.UserType.NORMAL.getCode();

        //when
        Page<ContentsDetailVo> result = displayContentsService.getContentsDetail(dpContsId, deviceType, userType);

        //then
        assertTrue(result.getTotal() > 0);
    }

    @Test
    void contentsDetailVoToResponseDto_정상() throws Exception {
        //given
        List<ContentsDetailVo> list = new ArrayList<>();
        ContentsDetailVo vo1 = new ContentsDetailVo();
        vo1.setContentsType(DisplayEnums.ContentsType.HTML.getCode());
        vo1.setDpContsId(1L);
        vo1.setDpStartDate("20201001");
        vo1.setDpEndDate("20201030");
        vo1.setDpRangeTypeName("test");
        vo1.setTitleName("testTitle");
        vo1.setSort(1);
        vo1.setHtmlPc("Y");
        vo1.setHtmlMobile("Y");
        list.add(vo1);

        ContentsDetailVo vo2 = new ContentsDetailVo();
        vo2.setContentsType(DisplayEnums.ContentsType.TEXT.getCode());
        list.add(vo2);

        ContentsDetailVo vo3 = new ContentsDetailVo();
        vo3.setContentsType(DisplayEnums.ContentsType.BANNER.getCode());
        list.add(vo3);

        ContentsDetailVo vo4 = new ContentsDetailVo();
        vo4.setContentsType(DisplayEnums.ContentsType.CATEGORY.getCode());
        list.add(vo4);

        ContentsDetailVo vo5 = new ContentsDetailVo();
        vo5.setContentsType(DisplayEnums.ContentsType.BRAND.getCode());
        list.add(vo5);

        ContentsDetailVo vo6 = new ContentsDetailVo();
        vo6.setContentsType(DisplayEnums.ContentsType.GOODS.getCode());
        vo6.setContentsId(175L);
        list.add(vo6);

        String deviceType = DisplayEnums.DeviceType.PC.getCode();

        //when
        List<?> result = displayContentsService.contentsDetailVoToResponseDto(list, deviceType);

        //then
        assertTrue(result.size() > 0);
    }

    @Test
    void contentsDetailVoToResponseDto_오류_값없음() throws Exception {
        //given
        String deviceType = DisplayEnums.DeviceType.PC.getCode();

        //when
        List<?> result = displayContentsService.contentsDetailVoToResponseDto(null, deviceType);

        //then
        assertNull(result);
    }

    @Test
    void contentsDetailVoToResponseDto_상품() throws Exception {
        //given
        List<ContentsDetailVo> list = new ArrayList<>();

        ContentsDetailVo vo6 = new ContentsDetailVo();
        vo6.setInventoryCode("test");
        vo6.setContentsType(DisplayEnums.ContentsType.GOODS.getCode());
        vo6.setContentsId(90018032L);
        list.add(vo6);

        String deviceType = DisplayEnums.DeviceType.PC.getCode();
        String userType = DisplayEnums.UserType.NORMAL.getCode();
        String goodsSortCode = DisplayEnums.GoodsSortCode.HIGH_PRICE.getCode();

        //when
        List<ContentsDetailVo> result = displayContentsService.searchGoodsInfo(list, deviceType, userType, goodsSortCode, "");

        //then
        assertTrue(result.size() > 0);
    }

    @Test
    void contentsDetailVoToResponseDto_상품_조회실패구분_PC() throws Exception {
        //given
        List<ContentsDetailVo> list = new ArrayList<>();

        ContentsDetailVo vo6 = new ContentsDetailVo();
        vo6.setInventoryCode("test");
        vo6.setContentsType(DisplayEnums.ContentsType.GOODS.getCode());
        vo6.setContentsId(175L);
        list.add(vo6);

        String deviceType = DisplayEnums.DeviceType.PC.getCode();
        String userType = DisplayEnums.UserType.NORMAL.getCode();
        String goodsSortCode = DisplayEnums.GoodsSortCode.HIGH_PRICE.getCode();

        List<GoodsSearchResultDto> goodsList = new ArrayList<>();
        GoodsSearchResultDto dto = new GoodsSearchResultDto();
        dto.setGoodsId(175L);
        dto.setDisplayPcYn("N");
        goodsList.add(dto);
        given(mockGoodsSearchBiz.searchGoodsByGoodsIdList(any())).willReturn(goodsList);

        //when
        List<ContentsDetailVo> result = mockDisplayContentsService.searchGoodsInfo(list, deviceType, userType, goodsSortCode, "");

        //then
        assertEquals(result.size(), 0);
    }

    @Test
    void contentsDetailVoToResponseDto_상품_조회실패구분_MOBILE() throws Exception {
        //given
        List<ContentsDetailVo> list = new ArrayList<>();

        ContentsDetailVo vo6 = new ContentsDetailVo();
        vo6.setInventoryCode("test");
        vo6.setContentsType(DisplayEnums.ContentsType.GOODS.getCode());
        vo6.setContentsId(175L);
        list.add(vo6);

        String deviceType = DisplayEnums.DeviceType.MOBILE.getCode();
        String userType = DisplayEnums.UserType.NORMAL.getCode();
        String goodsSortCode = DisplayEnums.GoodsSortCode.HIGH_PRICE.getCode();

        List<GoodsSearchResultDto> goodsList = new ArrayList<>();
        GoodsSearchResultDto dto = new GoodsSearchResultDto();
        dto.setGoodsId(175L);
        dto.setDisplayMobileYn("N");
        goodsList.add(dto);
        given(mockGoodsSearchBiz.searchGoodsByGoodsIdList(any())).willReturn(goodsList);

        //when
        List<ContentsDetailVo> result = mockDisplayContentsService.searchGoodsInfo(list, deviceType, userType, goodsSortCode, "");

        //then
        assertEquals(result.size(), 0);
    }

    @Test
    void searchGoodsInfo_상품_조회실패구분_APP() throws Exception {
        //given
        List<ContentsDetailVo> list = new ArrayList<>();

        ContentsDetailVo vo6 = new ContentsDetailVo();
        vo6.setInventoryCode("test");
        vo6.setContentsType(DisplayEnums.ContentsType.GOODS.getCode());
        vo6.setContentsId(175L);
        list.add(vo6);

        String deviceType = DisplayEnums.DeviceType.APP.getCode();
        String userType = DisplayEnums.UserType.NORMAL.getCode();
        String goodsSortCode = DisplayEnums.GoodsSortCode.HIGH_PRICE.getCode();

        List<GoodsSearchResultDto> goodsList = new ArrayList<>();
        GoodsSearchResultDto dto = new GoodsSearchResultDto();
        dto.setGoodsId(175L);
        dto.setDisplayAppYn("N");
        goodsList.add(dto);
        given(mockGoodsSearchBiz.searchGoodsByGoodsIdList(any())).willReturn(goodsList);

        //when
        List<ContentsDetailVo> result = mockDisplayContentsService.searchGoodsInfo(list, deviceType, userType, goodsSortCode, "");

        //then
        assertEquals(result.size(), 0);
    }

    @Test
    void searchGoodsInfo_상품_조회실패구분_비회원() throws Exception {
//        //given 비회원 SPEC OUT - 2021-04-06
//        List<ContentsDetailVo> list = new ArrayList<>();
//
//        ContentsDetailVo vo6 = new ContentsDetailVo();
//        vo6.setInventoryCode("test");
//        vo6.setContentsType(DisplayEnums.ContentsType.GOODS.getCode());
//        vo6.setContentsId(175L);
//        list.add(vo6);
//
//        String deviceType = DisplayEnums.DeviceType.APP.getCode();
//        String userType = DisplayEnums.UserType.NONE.getCode();
//        String goodsSortCode = DisplayEnums.GoodsSortCode.HIGH_PRICE.getCode();
//
//        List<GoodsSearchResultDto> goodsList = new ArrayList<>();
//        GoodsSearchResultDto dto = new GoodsSearchResultDto();
//        dto.setGoodsId(175L);
//        dto.setDisplayAppYn("Y");
//        dto.setPurchaseNonMemberYn("N");
//        goodsList.add(dto);
//        given(mockGoodsSearchBiz.searchGoodsByGoodsIdList(any())).willReturn(goodsList);
//
//        //when
//        List<ContentsDetailVo> result = mockDisplayContentsService.searchGoodsInfo(list, deviceType, userType, goodsSortCode);
//
//        //then
//        assertEquals(result.size(), 0);
    }

    @Test
    void searchGoodsInfo_상품_조회실패구분_회원() throws Exception {
        //given
        List<ContentsDetailVo> list = new ArrayList<>();

        ContentsDetailVo vo6 = new ContentsDetailVo();
        vo6.setInventoryCode("test");
        vo6.setContentsType(DisplayEnums.ContentsType.GOODS.getCode());
        vo6.setContentsId(175L);
        list.add(vo6);

        String deviceType = DisplayEnums.DeviceType.APP.getCode();
        String userType = DisplayEnums.UserType.NORMAL.getCode();
        String goodsSortCode = DisplayEnums.GoodsSortCode.HIGH_PRICE.getCode();

        List<GoodsSearchResultDto> goodsList = new ArrayList<>();
        GoodsSearchResultDto dto = new GoodsSearchResultDto();
        dto.setGoodsId(175L);
        dto.setDisplayAppYn("Y");
        dto.setPurchaseMemberYn("N");
        goodsList.add(dto);
        given(mockGoodsSearchBiz.searchGoodsByGoodsIdList(any())).willReturn(goodsList);

        //when
        List<ContentsDetailVo> result = mockDisplayContentsService.searchGoodsInfo(list, deviceType, userType, goodsSortCode, "");

        //then
        assertEquals(result.size(), 0);
    }

    @Test
    void searchGoodsInfo_상품_조회실패구분_임직원() throws Exception {
        //given
        List<ContentsDetailVo> list = new ArrayList<>();

        ContentsDetailVo vo6 = new ContentsDetailVo();
        vo6.setInventoryCode("test");
        vo6.setContentsType(DisplayEnums.ContentsType.GOODS.getCode());
        vo6.setContentsId(175L);
        list.add(vo6);

        String deviceType = DisplayEnums.DeviceType.APP.getCode();
        String userType = DisplayEnums.UserType.EMPLOYEE.getCode();
        String goodsSortCode = DisplayEnums.GoodsSortCode.HIGH_PRICE.getCode();

        List<GoodsSearchResultDto> goodsList = new ArrayList<>();
        GoodsSearchResultDto dto = new GoodsSearchResultDto();
        dto.setGoodsId(175L);
        dto.setDisplayAppYn("Y");
        dto.setPurchaseEmployeeYn("N");
        goodsList.add(dto);
        given(mockGoodsSearchBiz.searchGoodsByGoodsIdList(any())).willReturn(goodsList);

        //when
        List<ContentsDetailVo> result = mockDisplayContentsService.searchGoodsInfo(list, deviceType, userType, goodsSortCode, "");

        //then
        assertEquals(result.size(), 0);
    }

    @Test
    void getCategoryInfo_조회_정상() throws Exception {
        //given
        String inventoryCd = "1depth-mainbanner";
        String deviceType = DisplayEnums.DeviceType.PC.getCode();
        String userType = DisplayEnums.UserType.NORMAL.getCode();

        //when
        List<InventoryCategoryResponseDto> result = displayContentsService.getCategoryInfo(inventoryCd, deviceType, userType);

        //then
        assertNotNull(result);
    }

    @Test
    void getLohasBanner_조회_정상() throws Exception {
        //given
        LohasBannerRequestDto dto = new LohasBannerRequestDto();
        dto.setInventoryCd(DisplayConstants.LOHAS_BANNER_CODE);
        dto.setDeviceType("PC");
        dto.setPage(1);
        dto.setLimit(20);

        //when
        LohasBannerResponseDto result = displayContentsService.getLohasBanner(dto);

        //then
        assertTrue(result.getTotal() > 0);
    }

    @Test
    void getContentsLevel1ByInventoryCd_조회_정상() throws Exception {
        //given
        String inventoryCd = DisplayConstants.KEYWORDS_GOODS;
        String deviceType = GoodsEnums.DeviceType.PC.getCode();

        //when
        List<ContentsDetailVo> result = displayContentsService.getContentsLevel1ByInventoryCd(inventoryCd, deviceType);

        //then
        assertTrue(result.size() > 0);
    }
}