package kr.co.pulmuone.v1.system.auth.service;

import java.util.List;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.system.auth.dto.GetAuthMenuListResponseDto;
import kr.co.pulmuone.v1.system.auth.dto.GetAuthUserInListRequestDto;
import kr.co.pulmuone.v1.system.auth.dto.GetAuthUserInListResponseDto;
import kr.co.pulmuone.v1.system.auth.dto.GetAuthUserOutListRequestDto;
import kr.co.pulmuone.v1.system.auth.dto.GetAuthUserOutListResponseDto;
import kr.co.pulmuone.v1.system.auth.dto.GetRoleListRequestDto;
import kr.co.pulmuone.v1.system.auth.dto.GetRoleListResponseDto;
import kr.co.pulmuone.v1.system.auth.dto.GetRoleListWithoutPagingResponseDto;
import kr.co.pulmuone.v1.system.auth.dto.GetRoleMenuAuthListResponseDto;
import kr.co.pulmuone.v1.system.auth.dto.GetRoleRequestDto;
import kr.co.pulmuone.v1.system.auth.dto.GetRoleResponseDto;
import kr.co.pulmuone.v1.system.auth.dto.RoleRequestDto;
import kr.co.pulmuone.v1.system.auth.dto.SaveAuthUserRequestDto;
import kr.co.pulmuone.v1.system.auth.dto.SaveRoleMenuAuthRequestDto;
import kr.co.pulmuone.v1.system.auth.dto.vo.AuthUserRoleTypeVo;

public interface SystemAuthBiz {

    List<AuthUserRoleTypeVo> getAuthUserRoleTypeList(Long userId);
    void addByRoleTypeIdAuthUserMapping(AuthUserRoleTypeVo authUserRoleTypeVo) throws Exception;
    void delByRoleTypeIdAuthUserMapping(AuthUserRoleTypeVo authUserRoleTypeVo) throws Exception;
    List<AuthUserRoleTypeVo> getRoleTypeList();
    GetRoleListResponseDto getRoleList(GetRoleListRequestDto dto) throws Exception;
    GetRoleResponseDto getRole(GetRoleRequestDto dto) throws Exception;
    ApiResult<?> addRole(RoleRequestDto dto) throws Exception;
    ApiResult<?> putRole(RoleRequestDto dto) throws Exception;
    ApiResult<?> delRole(Long inputStRoleTypeId) throws Exception;
    GetRoleListWithoutPagingResponseDto getRoleListWithoutPaging() throws Exception;
    GetAuthUserOutListResponseDto getAuthUserOutList(GetAuthUserOutListRequestDto dto) throws Exception;
    GetAuthUserInListResponseDto getAuthUserInList(GetAuthUserInListRequestDto dto) throws Exception;
    ApiResult<?> addAuthUser(SaveAuthUserRequestDto dto) throws Exception;
    GetAuthMenuListResponseDto getAuthMenuList(Long stRoleTypeId, Long stMenuGroupId) throws Exception;
    GetRoleMenuAuthListResponseDto getRoleMenuAuthList(Long stRoleTypeId, Long stMenuId) throws Exception;
    ApiResult<?> saveAuthMenu(SaveRoleMenuAuthRequestDto dto) throws Exception;
    ExcelDownloadDto getRoleMenuAuthListExportExcel(Long stRoleTypeId) throws Exception;
}
