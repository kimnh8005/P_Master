package kr.co.pulmuone.v1.system.menu.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import kr.co.pulmuone.v1.comm.mapper.system.menu.SystemMenuMapper;
import org.apache.commons.collections.CollectionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.system.menu.dto.AddMenuAssigUrlRequestSaveDto;
import kr.co.pulmuone.v1.system.menu.dto.GetMenuAssignUrlListResponseDto;
import kr.co.pulmuone.v1.system.menu.dto.GetMenuGroupListRequestDto;
import kr.co.pulmuone.v1.system.menu.dto.GetMenuGroupListResponseDto;
import kr.co.pulmuone.v1.system.menu.dto.GetMenuGroupNameListResponseDto;
import kr.co.pulmuone.v1.system.menu.dto.GetMenuGroupRequestDto;
import kr.co.pulmuone.v1.system.menu.dto.GetMenuListRequestDto;
import kr.co.pulmuone.v1.system.menu.dto.GetMenuListResponseDto;
import kr.co.pulmuone.v1.system.menu.dto.GetMenuPopupListRequestDto;
import kr.co.pulmuone.v1.system.menu.dto.GetMenuRequestDto;
import kr.co.pulmuone.v1.system.menu.dto.GetMenuResponseDto;
import kr.co.pulmuone.v1.system.menu.dto.GetMenuUrlListResponseDto;
import kr.co.pulmuone.v1.system.menu.dto.GetParentMenuListRequestDto;
import kr.co.pulmuone.v1.system.menu.dto.GetParentMenuListResponseDto;
import kr.co.pulmuone.v1.system.menu.dto.GetSystemUrlListRequestDto;
import kr.co.pulmuone.v1.system.menu.dto.GetSystemUrlListResponseDto;
import kr.co.pulmuone.v1.system.menu.dto.GetSystemUrlResponseDto;
import kr.co.pulmuone.v1.system.menu.dto.MenuGroupRequestDto;
import kr.co.pulmuone.v1.system.menu.dto.MenuRequestDto;
import kr.co.pulmuone.v1.system.menu.dto.SystemAuthorityResponseDto;
import kr.co.pulmuone.v1.system.menu.dto.SystemUrlRequestDto;
import kr.co.pulmuone.v1.system.menu.dto.vo.GetMenuAssignUrlListResultVo;
import kr.co.pulmuone.v1.system.menu.dto.vo.GetMenuGroupResultVo;
import kr.co.pulmuone.v1.system.menu.dto.vo.GetMenuPopupListResultVo;
import kr.co.pulmuone.v1.system.menu.dto.vo.GetMenuResultVo;
import kr.co.pulmuone.v1.system.menu.dto.vo.GetSystemAuthorityResultVo;
import kr.co.pulmuone.v1.system.menu.dto.vo.GetSystemUrlResultVo;
import lombok.extern.slf4j.Slf4j;


@Slf4j
class SystemMenuServiceTest extends CommonServiceTestBaseForJunit5 {
    @Autowired
    private SystemMenuService service;

    @InjectMocks
    private SystemMenuService mockSystemMenuService;

    @Mock
    SystemMenuMapper mockMapper;

    @BeforeEach
    void beforeEach() {
        preLogin();
        mockSystemMenuService = new SystemMenuService(mockMapper);
    }


    @Test
    void 메뉴_그룹_리스트_조회_정상() {
        GetMenuGroupListRequestDto dto = GetMenuGroupListRequestDto.builder()
                .menuGroupName("")
                .id("")
                .useYn("Y")
                .build();

        GetMenuGroupListResponseDto menuList = service.getMenuGroupList(dto);

        assertTrue(CollectionUtils.isNotEmpty(menuList.getRows()));
    }

    @Test
    void 메뉴_그룹_리스트_조회_실패() {
        GetMenuGroupListRequestDto dto = GetMenuGroupListRequestDto.builder()
                .menuGroupName("")
                .id("")
                .useYn("E")
                .build();

        GetMenuGroupListResponseDto menuList = service.getMenuGroupList(dto);

        assertFalse(CollectionUtils.isNotEmpty(menuList.getRows()));
    }

    @Test
    void 메뉴_그룹_상세_조회_성공() {
        GetMenuGroupRequestDto dto = new GetMenuGroupRequestDto("1");
        GetMenuGroupResultVo menuGroup = service.getMenuGroup(dto);

        assertEquals("1", menuGroup.getInputStMenuGroupId());
    }

    @Test
    void 메뉴_그룹_상세_조회_실패() {
        GetMenuGroupRequestDto dto = new GetMenuGroupRequestDto("0");
        GetMenuGroupResultVo menuGroup = service.getMenuGroup(dto);

        assertTrue(Objects.isNull(menuGroup));
    }

    @Test
    void 메뉴_그룹_중복_체크() {
        MenuGroupRequestDto dto = MenuGroupRequestDto.builder()
                .inputStMenuGroupId("1")
                .inputMenuGroupName("통합몰 설정")
                .inputStProgramId("91")
                .build();

        int valid = service.existsMenuGroup(dto);

        assertEquals(0, valid);
    }

    @Test
    void 메뉴_그룹_신규_생성_성공() {
        MenuGroupRequestDto dto = MenuGroupRequestDto.builder()
                .inputMenuGroupName("재고/발주/테스트")
                .inputStProgramId("9999")
                .inputSort("9999")
                .inputUseYn("N")
                .build();

        int addFlag = service.addMenuGroup(dto);

        assertEquals(1, addFlag);
    }

    @Test
    void 메뉴_그룹_수정_성공() {
        MenuGroupRequestDto dto = MenuGroupRequestDto.builder()
                .inputStMenuGroupId("1")
                .inputMenuGroupName("재고/발주/테스트")
                .inputStProgramId("9999")
                .inputSort("9999")
                .inputUseYn("N")
                .build();

        int saveFlag = service.putMenuGroup(dto);

        assertEquals(1, saveFlag);
    }

//    @Test
//    void 메뉴_그룹_삭제_FK_실패() {
//        long id = 1L;
//
//        assertThrows(DataIntegrityViolationException.class, () -> {
//            service.delMenuGroup(id);
//        });
//    }


    @Test
    void 메뉴_그룹_등록_리스트검색_상세검색_수정_삭제_성공() {
        //등록
        MenuGroupRequestDto addDto = MenuGroupRequestDto.builder()
                .inputMenuGroupName("재고/발주/테스트")
                .inputStProgramId("9999")
                .inputSort("9999")
                .inputUseYn("N")
                .build();

        int addFlag = service.addMenuGroup(addDto);

        assertEquals(1, addFlag);

        //리스트 조회
        GetMenuGroupListRequestDto listDto = GetMenuGroupListRequestDto.builder()
                .menuGroupName("재고/발주/테스트")
                .id("")
                .useYn("N")
                .build();

        GetMenuGroupListResponseDto menuList = service.getMenuGroupList(listDto);

        boolean matched = menuList.getRows().stream()
                .anyMatch(vo -> "재고/발주/테스트".equals(vo.getMenuGroupName()));

        assertTrue(matched);

        //상세조회
        GetMenuGroupRequestDto dtlDto = new GetMenuGroupRequestDto(menuList.getRows().get(0).getStMenuGroupId());
        GetMenuGroupResultVo menuGroup = service.getMenuGroup(dtlDto);

        assertFalse(Objects.isNull(menuGroup));

        // 수정
        MenuGroupRequestDto dto = MenuGroupRequestDto.builder()
                .inputStMenuGroupId(dtlDto.getStMenuGroupId())
                .inputMenuGroupName("재고/발주/테스트")
                .inputStProgramId("9999")
                .inputSort("9999")
                .inputUseYn("Y")
                .build();

        int saveFlag = service.putMenuGroup(dto);

        assertEquals(1, saveFlag);

        //삭제
        int delFlag = service.delMenuGroup(Long.parseLong(dtlDto.getStMenuGroupId()));

        assertEquals(1, delFlag);
    }

    @Test
    void 메뉴_네임_검색() {
        GetMenuGroupNameListResponseDto result =  service.getMenuGroupNameList();

        boolean total = result.getTotal() > 0;

        assertTrue(total);
    }

    @Test
    void 메뉴_검색() {
        GetMenuListResponseDto  result =  service.getMenuList(new GetMenuListRequestDto());

        boolean total = result.getTotal() > 0;

        assertTrue(total);
    }

    @Test
    void 메뉴_등록_상세_수정_삭제() {
        //등록
		MenuRequestDto addDto = new MenuRequestDto();
		addDto.setStProgramId("386");
		addDto.setGbDictionaryMasterId("6918");
		addDto.setMenuName("테스트 메뉴");
		addDto.setMenuType("MENU_TYPE.PAGE");
		addDto.setParentMenuId("38");
		addDto.setBusinessType("PS");
		addDto.setStMenuGroupId("27");
		addDto.setSort("27000207");
		addDto.setPopYn("N");
		addDto.setUseYn("N");
		addDto.setComment("");
		addDto.setAddAuthorizationCheckData("");

        int addFlag = service.addMenu(addDto);
        assertEquals(1, addFlag);

        //상세
        GetMenuRequestDto dtlDto = new GetMenuRequestDto(addDto.getStMenuId());
        GetMenuResponseDto dtlResult = service.getMenu(dtlDto);

        String stMenuId = Optional.ofNullable(dtlResult)
                .map(m -> m.getRows())
                .map(GetMenuResultVo::getStMenuId)
                .orElse("");

        assertEquals(addDto.getStMenuId(), stMenuId);

        //중복체크
        GetMenuResultVo menuResultVo = dtlResult.getRows();

		MenuRequestDto existsDto = new MenuRequestDto();
		existsDto.setStMenuGroupId(menuResultVo.getStMenuGroupId());
		existsDto.setMenuName(menuResultVo.getMenuName());
		existsDto.setMenuType(menuResultVo.getMenuType());
		existsDto.setUrl(menuResultVo.getUrl());
		existsDto.setParentMenuId(menuResultVo.getParentMenuId());
		existsDto.setBusinessType(menuResultVo.getBusinessType());
		existsDto.setStProgramId(menuResultVo.getStProgramId());
		existsDto.setGbDictionaryMasterId(menuResultVo.getGbDictionaryMasterId());
		existsDto.setSort(menuResultVo.getSort());
		existsDto.setPopYn(menuResultVo.getPopYn());
		existsDto.setUseYn(menuResultVo.getUseYn());
		existsDto.setComment(menuResultVo.getComment());
		existsDto.setAddAuthorizationCheckData(menuResultVo.getAddAuthorizationCheckData());
		existsDto.setStMenuId(menuResultVo.getStMenuId());

        boolean checkFlag = service.existsMenu(existsDto) == 0;
        assertTrue(checkFlag);


        //수정
        int saveFlag = service.putMenu(existsDto);
        assertEquals(1, saveFlag);

        //삭제
        int delFlag = service.delMenu(Long.parseLong(existsDto.getStMenuId()));
        assertEquals(1, delFlag);
    }

    @Test
    void 상위_메뉴_리스트_성공() {
        GetParentMenuListResponseDto  result =  service.getParentMenuList(new GetParentMenuListRequestDto());
        boolean total = result.getTotal() > 0;
        assertTrue(total);
    }

    @Test
    void 시스템_URL_리스트_성공() {
        GetSystemUrlListResponseDto  result =  service.getSystemUrlList(new GetSystemUrlListRequestDto());
        boolean total = result.getTotal() > 0;
        assertTrue(total);
    }

    @Test
    void 시스템_URL_등록_상세조회_수정_삭제() {
        //등록
        SystemUrlRequestDto addDto = SystemUrlRequestDto.builder()
                .inputUrl("/aaa/bbb")
                .inputUrlName("테스트메뉴")
                .inputContent("테스트URL 등록 테스트")
                .inputAuthority("CRUD_TP.UNDEFINED")
                .inputUrlUsageYn("N")
                .build();

        int addFlag = service.addSystemUrl(addDto);
        assertEquals(1, addFlag);

        //상세
        long id = Long.parseLong(addDto.getId());
        GetSystemUrlResponseDto dtlResult = service.getSystemUrl(id);

        String urlId = Optional.ofNullable(dtlResult)
                .map(m -> m.getRows())
                .map(GetSystemUrlResultVo::getId)
                .orElse("");

        assertEquals(addDto.getId(), urlId);

        //중복체크
        GetSystemUrlResultVo checkDto = dtlResult.getRows();
        SystemUrlRequestDto sysUrlDto = SystemUrlRequestDto.builder()
                .id(checkDto.getId())
                .inputUrl(checkDto.getInputUrl())
                .inputUrlName(checkDto.getInputUrlName())
                .inputContent(checkDto.getInputContent())
                .inputUrlUsageYn("Y")
                .inputAuthority("CRUD_TP.R")
                .build();

        boolean checkFlag = service.existsSystemUrl(sysUrlDto) == 0 ? true : false;
        assertTrue(checkFlag);

        //할당 메뉴 URL 등록
        AddMenuAssigUrlRequestSaveDto assDto = AddMenuAssigUrlRequestSaveDto.builder()
                .stMenuUrlId(id)
                .build();
        List<AddMenuAssigUrlRequestSaveDto> assList = new ArrayList<>();
        assList.add(assDto);
        int assInsFlag = service.saveMenuAssignUrl(0L, assList);
        assertEquals(1, assInsFlag);

        //할당메뉴 삭제
        GetMenuAssignUrlListResponseDto assignUrlListResDto = service.getMenuAssignUrlList(0L);
        List<AddMenuAssigUrlRequestSaveDto> deleteRequestDtoList = new ArrayList<AddMenuAssigUrlRequestSaveDto>();
		for (GetMenuAssignUrlListResultVo assignUrlDto : assignUrlListResDto.getRows()) {
			AddMenuAssigUrlRequestSaveDto dto = new AddMenuAssigUrlRequestSaveDto();
			dto.setStProgramAuthUrlMappingId(assignUrlDto.getStProgramAuthUrlMappingId());
			deleteRequestDtoList.add(dto);
		}
        boolean assDelFlag = service.delMenuAssigUrl(deleteRequestDtoList) > 0;
        assertTrue(assDelFlag);

        //수정
        int saveFlag = service.putSystemUrl(sysUrlDto);
        assertEquals(1, saveFlag);

        //삭제
        int delFlag = service.delSystemUrl(id);
        assertEquals(1, delFlag);

    }

    @Test
    void 메뉴_URL_리스트() {
        GetMenuUrlListResponseDto  result =  service.getMenuUrlList(0L);
        boolean total = result.getTotal() > 0;
        assertTrue(total);
    }

    @Test
    void 메뉴_팝업_리스트() {
        GetMenuPopupListRequestDto dto = new GetMenuPopupListRequestDto();
        dto.setMenuType("MENU_TYPE.PAGE");

        Page<GetMenuPopupListResultVo>  result =  service.getMenuPopupList(dto);
        boolean total = result.getTotal() > 0;
        assertTrue(total);
    }

    @Test
    void 할당_메뉴_URL_조회() {
        long menuId = 94L;
        GetMenuAssignUrlListResponseDto  result =  service.getMenuAssignUrlList(menuId);
        boolean total = result.getTotal() > 0;
        assertTrue(total);
    }

    @Test
    void 할당_메뉴_URL_맵핑_삭제() {
		List<AddMenuAssigUrlRequestSaveDto> deleteRequestDtoList = new ArrayList<AddMenuAssigUrlRequestSaveDto>();
		AddMenuAssigUrlRequestSaveDto dto = new AddMenuAssigUrlRequestSaveDto();
		dto.setStProgramAuthUrlMappingId(94L);
		deleteRequestDtoList.add(dto);
		boolean delFlag = service.delMenuAssigUrl(deleteRequestDtoList) > 0;
		assertTrue(delFlag);
    }

    @Test
    void 권한_권리_조회() {
        SystemAuthorityResponseDto systemAuthorityResponseDto = new SystemAuthorityResponseDto();

        List<GetSystemAuthorityResultVo> systemAuthorityResultVo = service.getSystemAuthority();

        systemAuthorityResponseDto.setRows(systemAuthorityResultVo);

        assertTrue(CollectionUtils.isNotEmpty(systemAuthorityResponseDto.getRows()));
    }

    @Test
    void putSystemUrl() {
        mockSystemMenuService.putSystemUrl(null);
    }

    @Test
    void delSystemUrl() {
        mockSystemMenuService.delSystemUrl(null);
    }
}