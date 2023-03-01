package kr.co.pulmuone.v1.system.auth.service;

import java.util.List;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.system.auth.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.pulmuone.v1.comm.enums.UserEnums;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.system.auth.dto.vo.AuthUserChangeHistVo;
import kr.co.pulmuone.v1.system.auth.dto.vo.AuthUserRoleTypeVo;


/**
* <PRE>
* Forbiz Korea
* 권한관리 BizImpl
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2020. 9. 14.                손진구          최초작성
* =======================================================================
* </PRE>
*/
@RequiredArgsConstructor
@Service
public class SystemAuthBizImpl  implements SystemAuthBiz {

    private final SystemAuthService service;

    /**
     * @Desc 회원 별 역할권한 조회
     * @param userId
     * @return List<AuthUserRoleTypeVo>
     */
    @Override
    public List<AuthUserRoleTypeVo> getAuthUserRoleTypeList(Long userId){
        return service.getAuthUserRoleTypeList(userId);
    }

    /**
     * @Desc 역할관리 별 사용자권한 매핑 등록
     * @param authUserRoleTypeVo
     * @
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void addByRoleTypeIdAuthUserMapping(AuthUserRoleTypeVo authUserRoleTypeVo) {
        AuthUserChangeHistVo authUserChangeHistVo = new AuthUserChangeHistVo();
        authUserChangeHistVo.setTargetUserId(authUserRoleTypeVo.getUserId());
        authUserChangeHistVo.setRoleTypeId(authUserRoleTypeVo.getRoleTypeId());
        authUserChangeHistVo.setAction(UserEnums.DatabaseAction.INSERT.getCode());
        authUserChangeHistVo.setHandleUserId(authUserRoleTypeVo.getCreateId());
        authUserChangeHistVo.setCreateId(authUserRoleTypeVo.getCreateId());

        service.addAuthUserChangeHist(authUserChangeHistVo);
        service.addAuthUserMapping(authUserRoleTypeVo);
    }

    /**
     * @Desc 역할관리 별 사용자권한 매핑 삭제
     * @param authUserRoleTypeVo
     * @
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void delByRoleTypeIdAuthUserMapping(AuthUserRoleTypeVo authUserRoleTypeVo) {
        AuthUserChangeHistVo authUserChangeHistVo = new AuthUserChangeHistVo();
        authUserChangeHistVo.setTargetUserId(authUserRoleTypeVo.getUserId());
        authUserChangeHistVo.setRoleTypeId(authUserRoleTypeVo.getRoleTypeId());
        authUserChangeHistVo.setAction(UserEnums.DatabaseAction.DELETE.getCode());
        authUserChangeHistVo.setHandleUserId(authUserRoleTypeVo.getCreateId());
        authUserChangeHistVo.setCreateId(authUserRoleTypeVo.getCreateId());

        service.addAuthUserChangeHist(authUserChangeHistVo);
        service.delAuthUserMapping(authUserRoleTypeVo);
    }

    /**
     * @Desc 역할관리 조회
     */
    @Override
    public List<AuthUserRoleTypeVo> getRoleTypeList(){
        return service.getRoleTypeList();
    }

    @Override
    public GetRoleListResponseDto getRoleList(GetRoleListRequestDto dto) {
        return service.getRoleList(dto);
    }

    /**
     * 역할그룹관리 팝업조회
     */
    @Override
    public GetRoleResponseDto getRole(GetRoleRequestDto dto) {
        return service.getRole(dto);
    }

    /**
     * 역할그룹관리 신규저장
     */
    @Override
    public ApiResult<?> addRole(RoleRequestDto dto) {
        return service.addRole(dto);
    }

    /**
     * 역할그룹관리 데이터 수정
     */
    @Override
    public ApiResult<?> putRole(RoleRequestDto dto) {
        return service.putRole(dto);
    }

    /**
     * 역할그룹관리 데이터 삭제
     */
    @Override
    public ApiResult<?> delRole(Long inputStRoleTypeId) {
        return service.delRole(inputStRoleTypeId);
    }

    /**
     * 사용자권한관리 - 역할명 조회
     */
    @Override
    public GetRoleListWithoutPagingResponseDto getRoleListWithoutPaging() {
        return service.getRoleListWithoutPaging();
    }

    /**
     * 사용자권한관리 -
     */
    @Override
    public GetAuthUserOutListResponseDto getAuthUserOutList(GetAuthUserOutListRequestDto dto) {
        return service.GetAuthUserOutList(dto);
    }

    /**
     * 사용자권한관리 -
     */
    @Override
    public GetAuthUserInListResponseDto getAuthUserInList(GetAuthUserInListRequestDto dto) {
        return service.getAuthUserInList(dto);
    }

    /**
     * 사용자권한관 신규저장
     */
    @Override
    public ApiResult<?> addAuthUser(SaveAuthUserRequestDto dto) {
        return service.addAuthUser(dto);
    }

    /**
     * 메뉴권한관리 메뉴관리 리스트
     */
    @Override
    public GetAuthMenuListResponseDto getAuthMenuList(Long stRoleTypeId, Long stMenuGroupId) {
        return service.getAuthMenuList(stRoleTypeId, stMenuGroupId);
    }

    /**
     * 할당된 URL 리스트
     */
    @Override
    public GetRoleMenuAuthListResponseDto getRoleMenuAuthList(Long stRoleTypeId, Long stMenuId) {
        return service.getRoleMenuAuthList(stRoleTypeId, stMenuId);
    }

    /**
     * 할당된 URL 리스트 저장
     */
    @Transactional
    @Override
    public ApiResult<?> saveAuthMenu(SaveRoleMenuAuthRequestDto dto) {
        return service.saveAuthMenu(dto);
    }

    @Override
    public ExcelDownloadDto getRoleMenuAuthListExportExcel(Long stRoleTypeId) throws Exception {
    	return service.getRoleMenuAuthListExportExcel(stRoleTypeId);
    }
}
