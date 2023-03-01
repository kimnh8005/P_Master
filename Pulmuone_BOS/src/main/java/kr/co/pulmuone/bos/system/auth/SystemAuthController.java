package kr.co.pulmuone.bos.system.auth;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.view.ExcelDownloadView;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsListRequestDto;
import kr.co.pulmuone.v1.system.auth.dto.AuthUserRoleTypeResponseDto;
import kr.co.pulmuone.v1.system.auth.dto.GetAuthMenuListResponseDto;
import kr.co.pulmuone.v1.system.auth.dto.GetRoleMenuAuthListResponseDto;
import kr.co.pulmuone.v1.system.auth.dto.GetAuthUserInListRequestDto;
import kr.co.pulmuone.v1.system.auth.dto.GetAuthUserInListResponseDto;
import kr.co.pulmuone.v1.system.auth.dto.GetAuthUserOutListRequestDto;
import kr.co.pulmuone.v1.system.auth.dto.GetAuthUserOutListResponseDto;
import kr.co.pulmuone.v1.system.auth.dto.GetRoleListRequestDto;
import kr.co.pulmuone.v1.system.auth.dto.GetRoleListResponseDto;
import kr.co.pulmuone.v1.system.auth.dto.GetRoleListWithoutPagingResponseDto;
import kr.co.pulmuone.v1.system.auth.dto.GetRoleRequestDto;
import kr.co.pulmuone.v1.system.auth.dto.GetRoleResponseDto;
import kr.co.pulmuone.v1.system.auth.dto.RoleRequestDto;
import kr.co.pulmuone.v1.system.auth.dto.SaveRoleMenuAuthRequestDto;
import kr.co.pulmuone.v1.system.auth.dto.SaveRoleMenuAuthRequestDtoSaveDto;
import kr.co.pulmuone.v1.system.auth.dto.SaveAuthUserRequestDto;
import kr.co.pulmuone.v1.system.auth.dto.SaveAuthUserRequestSaveDto;
import kr.co.pulmuone.v1.system.auth.dto.vo.AuthUserRoleTypeVo;
import kr.co.pulmuone.v1.system.auth.service.SystemAuthBiz;
import lombok.RequiredArgsConstructor;

/**
* <PRE>
* Forbiz Korea
* 권한관리 Controller
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2020. 9. 15.                손진구          최초작성
* =======================================================================
* </PRE>
*/
@RestController
@RequiredArgsConstructor
public class SystemAuthController {
    private final SystemAuthBiz systemAuthBiz;

    @Autowired
    private ExcelDownloadView excelDownloadView; // 엑셀 다운로드 뷰

    /**
     * @Desc 역할관리 조회
     */
    @GetMapping(value="/admin/system/auth/getRoleTypeList")
    @ApiOperation(value = "역할관리 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 0000, message = "성공", response = AuthUserRoleTypeResponseDto.class)
    })
    public ApiResult<?> getRoleTypeList() {
        AuthUserRoleTypeResponseDto authUserRoleTypeResponseDto = new AuthUserRoleTypeResponseDto();
        List<AuthUserRoleTypeVo> authUserRoleTypeList = systemAuthBiz.getRoleTypeList();
        authUserRoleTypeResponseDto.setRows(authUserRoleTypeList);

        return ApiResult.success(authUserRoleTypeResponseDto);
    }

    /**
     * 역할그룹관리 리스트 조회
     */
    @PostMapping(value = "/admin/st/auth/getRoleList")
    @ApiOperation(value = "역할그룹관리 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 0000, message = "성공", response = GetRoleListResponseDto.class)
    })
    public ApiResult<?> getRoleList(HttpServletRequest request, GetRoleListRequestDto dto) throws Exception {
        return ApiResult.success(systemAuthBiz.getRoleList((GetRoleListRequestDto) BindUtil.convertRequestToObject(request, GetRoleListRequestDto.class)));

    }

    /**
     * 역할그룹관리 상세 조회
     */
    @PostMapping(value = "/admin/st/auth/getRole")
    @ApiOperation(value = "역할그룹관리 상세 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 0000, message = "성공", response = GetRoleResponseDto.class)
    })
    public ApiResult<?> getRole(GetRoleRequestDto dto) throws Exception {
        return ApiResult.success(systemAuthBiz.getRole(dto));
    }

    /**
     * 역할그룹 등록
     */
    @PostMapping(value = "/admin/st/auth/addRole")
    @ApiOperation(value = "역할그룹관리 등록")
    @ApiResponses(value = {
            @ApiResponse(code = 0000, message = "성공"),
            @ApiResponse(code = 9999, message = "" +
                    "[DUPLICATE_DATA] 777777777 - 중복된 데이터가 존재합니다. \n"
            )
    })
    public ApiResult<?> addRole(RoleRequestDto dto) throws Exception {
        return systemAuthBiz.addRole(dto);
    }

    /**
     * 역할그룹 수정
     */
    @PostMapping(value = "/admin/st/auth/putRole")
    @ApiOperation(value = "역할그룹관리 수정")
    @ApiResponses(value = {
            @ApiResponse(code = 0000, message = "성공"),
            @ApiResponse(code = 9999, message = "" +
                    "[DUPLICATE_DATA] 777777777 - 중복된 데이터가 존재합니다. \n"
            )
    })
    public ApiResult<?> putRole(RoleRequestDto dto) throws Exception {
        return systemAuthBiz.putRole(dto);
    }

    /**
     * 역할그룹 삭제
     */
    @PostMapping(value = "/admin/st/auth/delRole")
    @ApiOperation(value = "역할그룹 삭제")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "inputStRoleTypeId", value = "역할그룹 PK", required = true, dataType = "Long")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 0000, message = "성공"),
            @ApiResponse(code = 9999, message = "" +
                    "[FOREIGN_KEY_DATA] 666666666 - 다른테이블에서 사용중입니다. \n"
            )
    })
    public ApiResult<?> delRole(@RequestParam(value="inputStRoleTypeId", required=true) Long inputStRoleTypeId) throws Exception {
        return systemAuthBiz.delRole(inputStRoleTypeId);
    }

    // 사용자권한관리

    /**
     * 사용자권한관리 - 사용자 리스트 조회  (페이징x)
     */
    @PostMapping(value = "/admin/st/auth/getRoleListWithoutPaging")
    @ApiOperation(value = "사용자권한관리 - 사용자 리스트 조회  (페이징x)")
    @ApiResponses(value = {
            @ApiResponse(code = 0000, message = "성공", response = GetRoleListWithoutPagingResponseDto.class)
    })
    public ApiResult<?> getRoleListWithoutPaging() throws Exception {
        return ApiResult.success(systemAuthBiz.getRoleListWithoutPaging());
    }

    /**
     * 사용자권한관리 - 사용자 리스트 조회  (페이징o)
     */
    @PostMapping(value = "/admin/st/auth/getAuthUserOutList")
    @ResponseBody
    @ApiOperation(value = "사용자권한관리 - 사용자 리스트 조회  (페이징o)")
    @ApiResponses(value = {
            @ApiResponse(code = 0000, message = "성공", response = GetAuthUserOutListResponseDto.class)
    })
    public ApiResult<?> GetAuthUserOutList(HttpServletRequest request, GetAuthUserOutListRequestDto dto) throws Exception  {
        return ApiResult.success(systemAuthBiz.getAuthUserOutList((GetAuthUserOutListRequestDto) BindUtil.convertRequestToObject(request, GetAuthUserOutListRequestDto.class)));
    }

    /**
     * 사용자권한관리 - 권한 설정된 사용자 조회
     */
    @PostMapping(value = "/admin/st/auth/getAuthUserInList")
    @ResponseBody
    @ApiOperation(value = "사용자권한관리 - 권한 설정된 사용자 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 0000, message = "성공", response = GetAuthUserInListResponseDto.class)
    })
    public ApiResult<?> getAuthUserInList(HttpServletRequest request, GetAuthUserInListRequestDto dto) throws Exception  {
        return ApiResult.success(systemAuthBiz.getAuthUserInList(dto));
    }

    /**
     * 사용자권한관리 - 입력처리 - 단일
     */
    @PostMapping(value = "/admin/st/auth/addAuthUser")
    @ApiOperation(value = "사용자권한관리 - 입력처리 - 단일")
    @ApiResponses(value = {
            @ApiResponse(code = 0000, message = "성공"),
            @ApiResponse(code = 9999, message = "" +
                    "[VALID_ERROR] 888888888 - 데이터가 유효하지 않습니다. \n"
            )
    })
    public ApiResult<?> addAuthUser(SaveAuthUserRequestDto dto) throws Exception {
        dto.setInsertSaveDataList((List<SaveAuthUserRequestSaveDto>) BindUtil.convertJsonArrayToDtoList(dto.getInsertData(), SaveAuthUserRequestSaveDto.class));

        return ApiResult.success(systemAuthBiz.addAuthUser(dto));
    }

    /**
     * 사용자권한관리 - 입력처리 -
     */
    @PostMapping(value = "/admin/st/auth/getAuthMenuList")
    @ApiOperation(value = "메뉴권한관리 메뉴관리 리스트 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "stRoleTypeId", value = "역할그룹 PK", dataType = "Long")
            , @ApiImplicitParam(name = "stMenuGroupId", value = "메뉴그룹 PK", dataType = "Long")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 0000, message = "성공", response = GetAuthMenuListResponseDto.class)
    })
    public ApiResult<?> getAuthMenuList (
            @RequestParam(value="stRoleTypeId") Long stRoleTypeId
            , @RequestParam(value="stMenuGroupId") Long stMenuGroupId
    ) throws Exception {
        return ApiResult.success(systemAuthBiz.getAuthMenuList(stRoleTypeId, stMenuGroupId));
    }

    /**
     * 할당 된 권한 리스트 조회
     */
    @PostMapping(value = "/admin/st/auth/getRoleMenuAuthList")
    @ApiOperation(value = "할당 된 권한 리스트 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "stRoleTypeId", value = "역할그룹 PK", dataType = "Long")
            , @ApiImplicitParam(name = "stMenuId", value = "메뉴 PK", dataType = "Long")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 0000, message = "성공", response = GetRoleMenuAuthListResponseDto.class)
    })
    public ApiResult<?> getRoleMenuAuthList(
            @RequestParam(value="stRoleTypeId") Long stRoleTypeId
            , @RequestParam(value="stMenuId") Long stMenuId
    ) throws Exception {
        return ApiResult.success(systemAuthBiz.getRoleMenuAuthList(stRoleTypeId, stMenuId));
    }

    /**
     * 할당 된 권한 저장
     */
    @PostMapping(value = "/admin/st/auth/saveRoleMenuAuth")
    @ApiOperation(value = "할당 된 권한 저장")
    @ApiResponses(value = {
            @ApiResponse(code = 0000, message = "성공"),
            @ApiResponse(code = 9999, message = "" +
                    "[VALID_ERROR] 888888888 - 데이터가 유효하지 않습니다. \n"
            )
    })
    public ApiResult<?> saveAuthMenu(SaveRoleMenuAuthRequestDto dto) throws Exception {
        dto.setDeleteRequestDtoList((List<SaveRoleMenuAuthRequestDtoSaveDto>) BindUtil.convertJsonArrayToDtoList(dto.getDeleteData(), SaveRoleMenuAuthRequestDtoSaveDto.class));
        dto.setInsertRequestDtoList((List<SaveRoleMenuAuthRequestDtoSaveDto>) BindUtil.convertJsonArrayToDtoList(dto.getInsertData(), SaveRoleMenuAuthRequestDtoSaveDto.class));

        return ApiResult.success(systemAuthBiz.saveAuthMenu(dto));
    }

    /**
     * 상품 엑셀 다운로드 목록 조회
     *
     * @param MasterItemListRequestDto
     * @return ModelAndView
     */
    @ApiOperation(value = "할당된 권한 목록 엑셀 다운로드")
    @PostMapping(value = "admin/st/auth/getRoleMenuAuthListExportExcel")
    public ModelAndView getRoleMenuAuthListExportExcel(@RequestBody Long stRoleTypeId) throws Exception {

        ExcelDownloadDto excelDownloadDto = systemAuthBiz.getRoleMenuAuthListExportExcel(stRoleTypeId);

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;

    }
}

