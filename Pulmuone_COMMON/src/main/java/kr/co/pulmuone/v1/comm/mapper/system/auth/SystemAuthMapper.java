package kr.co.pulmuone.v1.comm.mapper.system.auth;

import java.util.List;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.system.auth.dto.*;
import kr.co.pulmuone.v1.system.auth.dto.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SystemAuthMapper {

    /**
     * @Desc 회원 별 역할권한 조회
     * @param userId
     * @return List<AuthUserRoleTypeVo>
     */
    List<AuthUserRoleTypeVo> getAuthUserRoleTypeList(@Param("userId") Long userId);

    /**
     * @Desc 사용자권한 변경이력 등록
     * @param authUserChangeHistVo
     * @
     * @return int
     */
    int addAuthUserChangeHist(AuthUserChangeHistVo authUserChangeHistVo);

    /**
     * @Desc 사용자권한 매핑 등록
     * @param authUserRoleTypeVo
     * @
     * @return int
     */
    int addAuthUserMapping(AuthUserRoleTypeVo authUserRoleTypeVo);

    /**
     * @Desc 사용자권한 매핑 삭제
     * @param authUserRoleTypeVo
     * @
     * @return int
     */
    int delAuthUserMapping(AuthUserRoleTypeVo authUserRoleTypeVo);

    /**
     * @Desc 역할관리 조회
     * @return List<AuthUserRoleTypeVo>
     */
    List<AuthUserRoleTypeVo> getRoleTypeList();

    int getRoleListCount(GetRoleListRequestDto dto);

    Page<GetRoleListResultVo> getRoleList(GetRoleListRequestDto dto);

    GetRoleResultVo getRole(GetRoleRequestDto dto);

    void addRole(RoleRequestDto dto);

    void putRole(RoleRequestDto dto);

    void delRole(Long inputStRoleTypeId);

    int duplicateRoleCount(RoleRequestDto dto);

    int existforeignKeyCount(Long inputStRoleTypeId);

    List<getRoleListWithoutPagingResultVo> getRoleListWithoutPaging();

    int GetAuthUserOutListCount(GetAuthUserOutListRequestDto dto);

    Page<GetAuthUserResultVo> GetAuthUserOutList(GetAuthUserOutListRequestDto dto);

    int getAuthUserInListCount(GetAuthUserInListRequestDto dto);

    Page<GetAuthUserResultVo> getAuthUserInList(GetAuthUserInListRequestDto dto);

    int addListcheckDelAuthUsers(SaveAuthUserRequestDto dto);

    int addListcheckAddAuthUsers(SaveAuthUserRequestDto dto);

    int addListDelAuthUsers(SaveAuthUserRequestDto dto);

    int addListAddAuthUsers(SaveAuthUserRequestDto dto);

    int addAuthUser(SaveAuthUserRequestDto dto);

    int delAuthUser(SaveAuthUserRequestDto dto);

    List<SaveAuthUserRequestSaveDto> getAuthUserList(@Param("stRoleTpId") Long stRoleTpId);

    List<GetAuthMenuListResultVo> getAuthMenuList(@Param("stRoleTypeId") Long stRoleTypeId,@Param("stMenuGroupId") Long stMenuGroupId);

    List<GetRoleMenuAuthListResultVo> getRoleMenuAuthList(@Param("stRoleTypeId") Long stRoleTypeId,@Param("stMenuId") Long stMenuId);

    int delAuthMenu(List<SaveRoleMenuAuthRequestDtoSaveDto> voList);

    int saveAuthMenu(@Param("stRoleTypeId") Long stRoleTypeId,@Param("stMenuId") Long stMenuId, @Param("voList") List<SaveRoleMenuAuthRequestDtoSaveDto> voList);

    int addRoleMenuAuthMapping(RoleRequestDto dto);

    List<RoleMenuAuthListExcelDto> getRoleMenuAuthExcelList(Long stRoleTypeId);
}
