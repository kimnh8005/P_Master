package kr.co.pulmuone.v1.base.service;

import kr.co.pulmuone.v1.base.dto.*;
import kr.co.pulmuone.v1.base.dto.vo.GetMenuNameResultVo;
import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class StComnServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    StComnService stComnService;

    @BeforeEach
    void setUp() {
        preLogin();
    }

    @Test
    void 공통코드_리스트_조회() {
        GetCodeListRequestDto dto = new GetCodeListRequestDto();
        dto.setStCommonCodeMasterCode("BANK_CODE");
        dto.setUseYn("Y");

        ApiResult<?> apiResult = stComnService.getCodeList(dto);

        assertTrue(BaseEnums.Default.SUCCESS.getCode() == apiResult.getCode());
    }

    @Test
    void 프로그램_리스트_조회() {
        GetProgramListRequestDto dto = new GetProgramListRequestDto();

        ApiResult<?> apiResult = stComnService.getProgramList(dto);

        assertTrue(BaseEnums.Default.SUCCESS.getCode() == apiResult.getCode());
    }

    @Test
    void 메뉴_리스트_조회() {
        GetMenuListRequestDto dto = new GetMenuListRequestDto();

        ApiResult<?> apiResult = stComnService.getMenuList(dto);

        assertTrue(BaseEnums.Default.SUCCESS.getCode() == apiResult.getCode());
    }

    @Test
    void 샵정보_조회() {
        GetShopInfoRequestDto dto = new GetShopInfoRequestDto();

        ApiResult<?> apiResult = stComnService.getShopInfo(dto);

        assertTrue(BaseEnums.Default.SUCCESS.getCode() == apiResult.getCode());
    }

    @Test
    void 페이지_정보_조회() throws Exception {
        GetPageInfoRequestDto dto = new GetPageInfoRequestDto();
        dto.setStMenuId("41");
        ApiResult<?> apiResult = stComnService.getPageInfo(dto);

        assertSame(BaseEnums.Default.SUCCESS.getCode(), apiResult.getCode());
    }

    @Test
    void 메뉴권한_조회_권한정보_존재()  throws Exception {
        GetAuthMenuParamDto dto = new GetAuthMenuParamDto();
        dto.setStMenuId("41");
    	String[] result = stComnService.getAuthMenu(dto);

    	assertTrue(result.length > 0);
    }

    @Test
    void 메뉴권한_조회_메뉴url_존재() throws Exception{
  		assertTrue(stComnService.isRoleMenuAuthUrl(null, "41", "/admin/system/shopconfig/getShopConfigList"));
    }

    @Test
    void 메뉴권한_조회_메뉴url_없음() throws Exception{
    	assertFalse(stComnService.isRoleMenuAuthUrl(null, "41", "/admin/ur/warehouse/getWarehouseList"));
    }

    @Test
    void 최상위_네비게이션_메뉴명_조회() throws Exception {
        GetMenuNameResultVo menuInfo = new GetMenuNameResultVo();
        menuInfo.setMenuId("38");
        menuInfo.setMenuName("메뉴관리");
        menuInfo.setParentMenuId("27");
        ConvertNavigationParamDto dto = new ConvertNavigationParamDto();
        dto.setStMenuId("27");
        dto.setTitle(menuInfo.getMenuName());
        dto.setPageNavi(menuInfo.getMenuName());

        ConvertNavigationResultDto result = stComnService.convertNavigation(dto, menuInfo);

        assertEquals("메뉴관리", (result.getTitle()));
        assertEquals("시스템 설정 &gt; 메뉴관리",(result.getPageNavi()));
    }

    @Test
    void 메뉴명_조회() throws Exception {
        GetNavigationMenuNameParamDto dto = new GetNavigationMenuNameParamDto();
        dto.setStMenuId("1256");

        GetNavigationMenuNameResultDto result = stComnService.getNavigationMenuName(dto);

        assertEquals("BOS 접근 IP 관리", (result.getTitle()));
        assertEquals("시스템 설정 &gt; 기초설정 &gt; BOS 접근 IP 관리",(result.getPageNavi()));
    }
    @Test
    void 코드명_조회() throws Exception {
    	assertNotNull(stComnService.getCodeName("APPR_KIND_TP.ITEM_REGIST"));
    }

}