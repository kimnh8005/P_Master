package kr.co.pulmuone.v1.system.auth.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.enums.UserEnums;
import kr.co.pulmuone.v1.comm.mapper.system.auth.SystemAuthMapper;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.system.auth.dto.*;
import kr.co.pulmuone.v1.system.auth.dto.vo.AuthUserChangeHistVo;
import kr.co.pulmuone.v1.system.auth.dto.vo.AuthUserRoleTypeVo;
import kr.co.pulmuone.v1.system.itgc.service.SystemItgcBiz;
import org.apache.commons.collections.CollectionUtils;
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

class SystemAuthServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    private SystemAuthService systemAuthService;

    @InjectMocks
    private SystemAuthService mockSystemAuthService;

    @Mock
    private SystemAuthMapper mockSystemAuthMapper;

    @Mock
    private SystemItgcBiz mockSystemItgcBiz;

    @BeforeEach
    void setUp() {
        mockSystemAuthService = new SystemAuthService(mockSystemAuthMapper,mockSystemItgcBiz);
    }

    @Test
    void 회원_별_역할권한_조회_성공() {
        Long userId = 1646799L;

        List<AuthUserRoleTypeVo> authUserRoleTypeList = systemAuthService.getAuthUserRoleTypeList(userId);

        assertTrue( CollectionUtils.isNotEmpty(authUserRoleTypeList) );
    }

    @Test
    void 회원_별_역할권한_조회_실패() {
        Long userId = 0L;

        List<AuthUserRoleTypeVo> authUserRoleTypeList = systemAuthService.getAuthUserRoleTypeList(userId);

        assertFalse( CollectionUtils.isNotEmpty(authUserRoleTypeList) );
    }

    @Test
    void 사용자권한_변경이력_등록_성공(){
        int count = 0;

        AuthUserChangeHistVo authUserChangeHistVo = new AuthUserChangeHistVo();
        authUserChangeHistVo.setTargetUserId(1646799L);
        authUserChangeHistVo.setRoleTypeId(11L);
        authUserChangeHistVo.setAction(UserEnums.DatabaseAction.INSERT.getCode());
        authUserChangeHistVo.setHandleUserId(1L);
        authUserChangeHistVo.setCreateId(1L);

        try {
            count = systemAuthService.addAuthUserChangeHist(authUserChangeHistVo);
        } catch (Exception e) {

        }

        assertEquals(1, count);
    }

    @Test
    void 사용자권한_변경이력_등록_실패() {
        int count = 0;

        AuthUserChangeHistVo authUserChangeHistVo = new AuthUserChangeHistVo();
        authUserChangeHistVo.setAction(UserEnums.DatabaseAction.INSERT.getCode());
        authUserChangeHistVo.setHandleUserId(1L);
        authUserChangeHistVo.setCreateId(1L);

        try {
            count = systemAuthService.addAuthUserChangeHist(authUserChangeHistVo);
        } catch (Exception e) {

        }

        assertNotEquals(1, count);
    }

    @Test
    void 사용자권한_매핑_등록_성공() {
        int count = 0;

        AuthUserRoleTypeVo authUserRoleTypeVo = new AuthUserRoleTypeVo();
        authUserRoleTypeVo.setUserId(1646799L);
        authUserRoleTypeVo.setRoleTypeId(112L);
        authUserRoleTypeVo.setCreateId(1L);

        try {
            count = systemAuthService.addAuthUserMapping(authUserRoleTypeVo);
        } catch (Exception e) {

        }

        assertEquals(1, count);
    }

    @Test
    void 사용자권한_매핑_등록_실패() {
        int count = 0;

        AuthUserRoleTypeVo authUserRoleTypeVo = new AuthUserRoleTypeVo();
        authUserRoleTypeVo.setRoleTypeId(112L);
        authUserRoleTypeVo.setCreateId(1L);

        try {
            count = systemAuthService.addAuthUserMapping(authUserRoleTypeVo);
        } catch (Exception e) {

        }

        assertNotEquals(1, count);
    }

    @Test
    void 사용자권한_매핑_삭제_성공() {
        int count = 0;

        AuthUserRoleTypeVo authUserRoleTypeVo = new AuthUserRoleTypeVo();
        authUserRoleTypeVo.setUserId(1646799L);
        authUserRoleTypeVo.setRoleTypeId(140L);

        try {
            count = systemAuthService.delAuthUserMapping(authUserRoleTypeVo);
        } catch (Exception e) {

        }

        assertEquals(1, count);
    }

    @Test
    void 사용자권한_매핑_삭제_실패() {
        int count = 0;

        AuthUserRoleTypeVo authUserRoleTypeVo = new AuthUserRoleTypeVo();
        authUserRoleTypeVo.setUserId(1646799L);
        authUserRoleTypeVo.setRoleTypeId(0L);

        try {
            count = systemAuthService.delAuthUserMapping(authUserRoleTypeVo);
        } catch (Exception e) {

        }

        assertNotEquals(1, count);
    }

    @Test
    void 역할관리_조회_성공() {
        List<AuthUserRoleTypeVo> authUserRoleTypeList = systemAuthService.getRoleTypeList();

        assertTrue( CollectionUtils.isNotEmpty(authUserRoleTypeList) );
    }

    @Test
    void 역할관리_조회_실패() {
        List<AuthUserRoleTypeVo> authUserRoleTypeList = systemAuthService.getRoleTypeList();

        assertFalse( CollectionUtils.isEmpty(authUserRoleTypeList) );
    }

    @Test
    void addAuthUserMapping() {
        given(mockSystemAuthMapper.addAuthUserMapping(any())).willReturn(1);
        int n = mockSystemAuthService.addAuthUserMapping(null);
        assertTrue(n > 0);
    }

    @Test
    void getRoleList() {
        GetRoleListRequestDto getRoleListRequestDto = new GetRoleListRequestDto();
        getRoleListRequestDto.setPage(1);
        getRoleListRequestDto.setPageSize(10);
        UserVo userVO = new UserVo();
        userVO.setUserId("1");
        userVO.setLoginId("forbiz");
        userVO.setLangCode("1");
        List<String> listRoleId = new ArrayList<>();
        listRoleId.add("1");
        userVO.setListRoleId(listRoleId);
        SessionUtil.setUserVO(userVO);

        GetRoleListResponseDto result = systemAuthService.getRoleList(getRoleListRequestDto);
        int n = result.getRows().size();
        assertTrue(n > 0);
    }

    @Test
    void getRole() {
        GetRoleRequestDto getRoleRequestDto = new GetRoleRequestDto();
        getRoleRequestDto.setStRoleTypeId("112");
        GetRoleResponseDto result = systemAuthService.getRole(getRoleRequestDto);
        assertTrue(result.getRows() != null);
    }

    @Test
    void addRole() {
        given(mockSystemAuthMapper.duplicateRoleCount(any())).willReturn(0);
        RoleRequestDto dto = new RoleRequestDto();
        ApiResult result = mockSystemAuthService.addRole(dto);
        assertEquals(ApiResult.success().getMessageEnum(), result.getMessageEnum());
    }

    @Test
    void putRole() {
        given(mockSystemAuthMapper.duplicateRoleCount(any())).willReturn(0);
        ApiResult result = mockSystemAuthService.putRole(null);
        assertEquals(ApiResult.success().getMessageEnum(), result.getMessageEnum());
    }

    @Test
    void delRole() {
        given(mockSystemAuthMapper.existforeignKeyCount(any())).willReturn(0);
        ApiResult result = mockSystemAuthService.delRole(null);
        assertEquals(ApiResult.success().getMessageEnum(), result.getMessageEnum());
    }

    @Test
    void getRoleListWithoutPaging() {
        GetRoleListWithoutPagingResponseDto result = systemAuthService.getRoleListWithoutPaging();
        assertTrue(result.getRows().size() > 0);
    }

    @Test
    void getAuthUserOutList() {
        GetAuthUserOutListRequestDto getAuthUserOutListRequestDto = new GetAuthUserOutListRequestDto();
        getAuthUserOutListRequestDto.setPage(1);
        getAuthUserOutListRequestDto.setPageSize(10);
        getAuthUserOutListRequestDto.setNotUrUserIds("1");
        UserVo userVO = new UserVo();
        userVO.setUserId("1");
        userVO.setLoginId("forbiz");
        userVO.setLangCode("1");
        List<String> listRoleId = new ArrayList<>();
        listRoleId.add("1");
        userVO.setListRoleId(listRoleId);
        SessionUtil.setUserVO(userVO);
        GetAuthUserOutListResponseDto result = systemAuthService.GetAuthUserOutList(getAuthUserOutListRequestDto);
        assertTrue(result.getRows().size() > 0);
    }

    @Test
    void getAuthUserInList() {
        GetAuthUserInListRequestDto getAuthUserInListRequestDto = new GetAuthUserInListRequestDto();
        getAuthUserInListRequestDto.setStRoleTypeId("112");
        GetAuthUserInListResponseDto result = systemAuthService.getAuthUserInList(getAuthUserInListRequestDto);
        assertTrue(result.getRows().size() > 0);
    }

    @Test
    void addAuthUser() {
        SaveAuthUserRequestDto saveAuthUserRequestDto = new SaveAuthUserRequestDto();
        saveAuthUserRequestDto.setInsertSaveDataList(new ArrayList<>());
        given(mockSystemAuthMapper.addListcheckDelAuthUsers(any())).willReturn(1);
        given(mockSystemAuthMapper.delAuthUser(any())).willReturn(1);
        given(mockSystemAuthMapper.addListAddAuthUsers(any())).willReturn(1);
        given(mockSystemAuthMapper.addListcheckAddAuthUsers(any())).willReturn(1);
        ApiResult result = mockSystemAuthService.addAuthUser(saveAuthUserRequestDto);
        assertEquals(ApiResult.success().getMessageEnum(), result.getMessageEnum());
    }

    @Test
    void getAuthMenuList() {
        GetAuthMenuListResponseDto result = systemAuthService.getAuthMenuList(1L, 1L);
        assertTrue(result.getRows().size() > 0);
    }

    @Test
    void getRoleMenuAuthList() {
        GetRoleMenuAuthListResponseDto result = systemAuthService.getRoleMenuAuthList(1L, 1L);
        assertTrue(result.getRows().size() > 0);
    }

    @Test
    void saveAuthMenu() {
        List<SaveRoleMenuAuthRequestDtoSaveDto> list = new ArrayList<>();
        SaveRoleMenuAuthRequestDtoSaveDto item = new SaveRoleMenuAuthRequestDtoSaveDto();
        list.add(item);
        SaveRoleMenuAuthRequestDto saveRoleMenuAuthRequestDto = new SaveRoleMenuAuthRequestDto();
        saveRoleMenuAuthRequestDto.setInsertRequestDtoList(list);
        saveRoleMenuAuthRequestDto.setDeleteRequestDtoList(list);
        given(mockSystemAuthMapper.delAuthMenu(any())).willReturn(1);
        given(mockSystemAuthMapper.saveAuthMenu(any(), any(), any())).willReturn(1);

        UserVo userVO = new UserVo();
        userVO.setUserId("1");
        userVO.setLoginId("forbiz");
        userVO.setLangCode("1");
        SessionUtil.setUserVO(userVO);

        ApiResult result = mockSystemAuthService.saveAuthMenu(saveRoleMenuAuthRequestDto);
        assertEquals(ApiResult.success().getMessageEnum(), result.getMessageEnum());
    }
}
