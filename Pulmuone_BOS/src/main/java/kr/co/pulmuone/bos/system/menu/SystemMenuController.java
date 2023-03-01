package kr.co.pulmuone.bos.system.menu;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.system.menu.dto.AddMenuAssigUrlRequestDto;
import kr.co.pulmuone.v1.system.menu.dto.AddMenuAssigUrlRequestSaveDto;
import kr.co.pulmuone.v1.system.menu.dto.GetMenuAssignUrlListResponseDto;
import kr.co.pulmuone.v1.system.menu.dto.GetMenuGroupListRequestDto;
import kr.co.pulmuone.v1.system.menu.dto.GetMenuGroupListResponseDto;
import kr.co.pulmuone.v1.system.menu.dto.GetMenuGroupNameListResponseDto;
import kr.co.pulmuone.v1.system.menu.dto.GetMenuGroupRequestDto;
import kr.co.pulmuone.v1.system.menu.dto.GetMenuGroupResponseDto;
import kr.co.pulmuone.v1.system.menu.dto.GetMenuListRequestDto;
import kr.co.pulmuone.v1.system.menu.dto.GetMenuListResponseDto;
import kr.co.pulmuone.v1.system.menu.dto.GetMenuPopupListRequestDto;
import kr.co.pulmuone.v1.system.menu.dto.GetMenuPopupListResponseDto;
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
import kr.co.pulmuone.v1.system.menu.service.SystemMenuBiz;
import lombok.RequiredArgsConstructor;

/**
* <PRE>
* Forbiz Korea
* Class의 기능과 역할을 상세히 기술한다.
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2020.05.28                dora47            최초작성
* =======================================================================
* </PRE>
*/
@RequiredArgsConstructor
@Controller
public class SystemMenuController {
	@Autowired(required=true)
	private HttpServletRequest request;

	private final SystemMenuBiz systemMenuBiz;

	private static final Logger log = LoggerFactory.getLogger(SystemMenuController.class);

	/**
	 * 메뉴그룹 리스트 조회
	 * @param dto
	 * @return GetMenuGroupListResponseDto
	 * @
	 */
	@PostMapping(value = "/admin/st/menu/getMenuGroupList")
	@ResponseBody
	@ApiOperation(value = "메뉴그룹 리스트 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GetMenuGroupListResponseDto.class),
			@ApiResponse(code = 901, message = "" +
					"[MANDATORY_MISSING] 333333333 - 필수값을 입력해주세요. \n"
			)
	})
	public ApiResult<?> getMenuGroupList(GetMenuGroupListRequestDto dto) throws Exception{
		return ApiResult.success(systemMenuBiz.getMenuGroupList((GetMenuGroupListRequestDto) BindUtil.convertRequestToObject(request, GetMenuGroupListRequestDto.class)));

	}

	/**
	 * 메뉴그룹 상세 조회
	 * @param dto
	 * @return GetMenuGroupResponseDto
	 * @
	 */
	@PostMapping(value = "/admin/st/menu/getMenuGroup")
	@ResponseBody
	@ApiOperation(value = "메뉴그룹 상세 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GetMenuGroupResponseDto.class),
			@ApiResponse(code = 901, message = "" +
					"[MANDATORY_MISSING] 333333333 - 필수값을 입력해주세요. \n"
			)
	})
	public ApiResult<?> getMenuGroup(GetMenuGroupRequestDto dto){
		return ApiResult.success(systemMenuBiz.getMenuGroup(dto));
	}


	/**
	 * 메뉴그룹 등록
	 * @param dto
	 * @
	 */
	@PostMapping(value = "/admin/st/menu/addMenuGroup")
	@ResponseBody
	@ApiOperation(value = "메뉴그룹 등록")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data"),
			@ApiResponse(code = 901, message = "" +
					"[MANDATORY_MISSING] 333333333 - 필수값을 입력해주세요. \n"
			)
	})
	public ApiResult<?> addMenuGroup(MenuGroupRequestDto dto){
		return systemMenuBiz.addMenuGroup(dto);
	}

	/**
	 * 메뉴그룹 데이터 수정
	 * @param dto
	 * @
	 */
	@PostMapping(value = "/admin/st/menu/putMenuGroup")
	@ResponseBody
	@ApiOperation(value = "메뉴그룹 수정")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data"),
			@ApiResponse(code = 901, message = "" +
					"[MANDATORY_MISSING] 333333333 - 필수값을 입력해주세요. \n"
			)
	})
	public ApiResult<?> putMenuGroup(MenuGroupRequestDto dto){
		return systemMenuBiz.putMenuGroup(dto);
	}

	/**
	 * 메뉴그룹 데이터 삭제
	 * @param inputStMenuGroupId
	 * @
	 */
	@PostMapping(value = "/admin/st/menu/delMenuGroup")
	@ResponseBody
	@ApiOperation(value = "메뉴그룹 수정")
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "inputStMenuGroupId", value = "메뉴그룹 PK", dataType = "Long")
	})
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data"),
			@ApiResponse(code = 901, message = "" +
					"[MANDATORY_MISSING] 333333333 - 필수값을 입력해주세요. \n"
			)
	})
	public ApiResult<?> delMenuGroup(@RequestParam(value="inputStMenuGroupId") Long inputStMenuGroupId){
		return systemMenuBiz.delMenuGroup(inputStMenuGroupId);
	}


	/**
	 * 메뉴그룹명 조회
	 * @return GetMenuGroupNameListResponseDto
	 * @
	 */
	@PostMapping(value = "/admin/st/menu/getMenuGroupNameList")
	@ResponseBody
	@ApiOperation(value = "메뉴그룹명 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GetMenuGroupNameListResponseDto.class),
			@ApiResponse(code = 901, message = "" +
					"[MANDATORY_MISSING] 333333333 - 필수값을 입력해주세요. \n"
			)
	})
	public ApiResult<?> getMenuGroupNameList()  {
		return ApiResult.success(systemMenuBiz.getMenuGroupNameList());
	}

	/**
	 * 메뉴그룹명 조회 (역활별 권한 정보 포함)
	 * @return GetMenuGroupNameListResponseDto
	 * @
	 */
	@PostMapping(value = "/admin/st/menu/getRoleTypeMenuGroupNameList")
	@ResponseBody
	@ApiOperation(value = "메뉴그룹명 조회(역활별 권한 정보 포함)")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GetMenuGroupNameListResponseDto.class),
			@ApiResponse(code = 901, message = "" +
					"[MANDATORY_MISSING] 333333333 - 필수값을 입력해주세요. \n"
			)
	})
	public ApiResult<?> getRoleTypeMenuGroupNameList(@RequestParam(value="stRoleTypeId") Long stRoleTypeId)  {
		return ApiResult.success(systemMenuBiz.getRoleTypeMenuGroupNameList(stRoleTypeId));
	}

	/**
	 * 메뉴그룹명 팝업 리스트 조회
	 * @return GetMenuGroupNameListResponseDto
	 * @
	 */
	@GetMapping(value = "/admin/st/menu/getMenuGroupNamePopList")
	@ResponseBody
	@ApiOperation(value = "메뉴그룹명 팝업 리스트 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GetMenuGroupNameListResponseDto.class),
			@ApiResponse(code = 901, message = "" +
					"[MANDATORY_MISSING] 333333333 - 필수값을 입력해주세요. \n"
			)
	})
	public ApiResult<?> getMenuGroupNamePopList()  {
		return ApiResult.success(systemMenuBiz.getMenuGroupNameList());
	}



	/**
	 * 메뉴 리스트 조회
	 * @return GetMenuListRequestDto
	 * @
	 */
	@PostMapping(value = "/admin/st/menu/getMenuList")
	@ResponseBody
	@ApiOperation(value = "메뉴 리스트 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GetMenuListResponseDto.class),
			@ApiResponse(code = 901, message = "" +
					"[MANDATORY_MISSING] 333333333 - 필수값을 입력해주세요. \n"
			)
	})
	public ApiResult<?> getMenuList(GetMenuListRequestDto dto) throws Exception {
		return ApiResult.success(systemMenuBiz.getMenuList((GetMenuListRequestDto) BindUtil.convertRequestToObject(request, GetMenuListRequestDto.class)));
	}

	/**
	 * 메뉴 상세 조회
	 * @param dto
	 * @return GetMenuResponseDto
	 * @
	 */
	@PostMapping(value = "/admin/st/menu/getMenu")
	@ResponseBody
	@ApiOperation(value = "메뉴 상세 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GetMenuResponseDto.class),
			@ApiResponse(code = 901, message = "" +
					"[MANDATORY_MISSING] 333333333 - 필수값을 입력해주세요. \n"
			)
	})
	public ApiResult<?> getMenu(GetMenuRequestDto dto){
		return ApiResult.success(systemMenuBiz.getMenu(dto));
	}


	/**
	 * 메뉴 등록
	 * @param dto
	 * @
	 */
	@PostMapping(value = "/admin/st/menu/addMenu")
	@ResponseBody
	@ApiOperation(value = "메뉴 등록")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data"),
			@ApiResponse(code = 901, message = "" +
					"[MANDATORY_MISSING] 333333333 - 필수값을 입력해주세요. \n"
			)
	})
	public ApiResult<?> addMenu(MenuRequestDto dto){
		return ApiResult.success(systemMenuBiz.addMenu(dto));
	}

	/**
	 * 메뉴 수정
	 * @param dto
	 * @
	 */
	@PostMapping(value = "/admin/st/menu/putMenu")
	@ResponseBody
	@ApiOperation(value = "메뉴 수정")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data"),
			@ApiResponse(code = 901, message = "" +
					"[MANDATORY_MISSING] 333333333 - 필수값을 입력해주세요. \n"
			)
	})
	public ApiResult<?> putMenu(MenuRequestDto dto){
		return ApiResult.success(systemMenuBiz.putMenu(dto));
	}

	/**
	 * 메뉴 삭제
	 * @param stMenuId
	 * @
	 */
	@PostMapping(value = "/admin/st/menu/delMenu")
	@ResponseBody
	@ApiOperation(value = "메뉴 삭제")
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "stMenuId", value = "메뉴 PK", required = true, dataType = "Long")
	})
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data"),
			@ApiResponse(code = 901, message = "" +
					"[MANDATORY_MISSING] 333333333 - 필수값을 입력해주세요. \n"
			)
	})
	public ApiResult<?> delMenu(@RequestParam(value="stMenuId", required = true) Long stMenuId){
		return systemMenuBiz.delMenu(stMenuId);
	}

	/**
	 * 상위 메뉴 리스트 조회
	 * @param dto
	 * @return GetParentMenuListResponseDto
	 * @
	 */
	@PostMapping(value = "/admin/st/menu/getParentMenuList")
	@ResponseBody
	@ApiOperation(value = "상위 메뉴 리스트 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GetParentMenuListResponseDto.class),
			@ApiResponse(code = 901, message = "" +
					"[MANDATORY_MISSING] 333333333 - 필수값을 입력해주세요. \n"
			)
	})
	public ApiResult<?> getParentMenuList(GetParentMenuListRequestDto dto) throws Exception {
		return ApiResult.success(systemMenuBiz.getParentMenuList((GetParentMenuListRequestDto) BindUtil.convertRequestToObject(request, GetParentMenuListRequestDto.class)));

	}

	/**
	 * 시스템 URL 정보 리스트 조회
	 * @param dto
	 * @return GetSystemUrlListResponseDto
	 * @
	 */
	@PostMapping(value="/admin/st/menu/getSystemUrlList")
	@ResponseBody
	@ApiOperation(value = "시스템 URL 정보 리스트 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GetSystemUrlListResponseDto.class),
			@ApiResponse(code = 901, message = "" +
					"[MANDATORY_MISSING] 333333333 - 필수값을 입력해주세요. \n"
			)
	})
	public ApiResult<?> getSystemUrlList(GetSystemUrlListRequestDto dto) throws Exception  {
		return ApiResult.success(systemMenuBiz.getSystemUrlList((GetSystemUrlListRequestDto) BindUtil.convertRequestToObject(request, GetSystemUrlListRequestDto.class)));
	}

	/**
	 * 시스템 URL 정보 권한관리 조회
	 * @return getSystemAuthority
	 * @
	 */
	@ResponseBody
	@GetMapping(value="/admin/st/menu/getSystemAuthority")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = SystemAuthorityResponseDto.class)
	})
	@ApiOperation(value = "시스템 URL 권한관리 조회")
	public ApiResult<?> getSystemAuthority() {

		return systemMenuBiz.getSystemAuthority();
	}

	/**
	 * 시스템 URL 정보 상세 조회
	 * @param id
	 * @return GetSystemUrlListResponseDto
	 * @
	 */
	@PostMapping(value="/admin/st/menu/getSystemUrl")
	@ResponseBody
	@ApiOperation(value = "시스템 URL 정보 상세 조회")
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "id", value = "시스템 URL PK", required = true, dataType = "Long")
	})
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GetSystemUrlResponseDto.class),
			@ApiResponse(code = 901, message = "" +
					"[MANDATORY_MISSING] 333333333 - 필수값을 입력해주세요. \n"
			)
	})
	public ApiResult<?> getSystemUrl(@RequestParam(value="id") Long id)  {
		return ApiResult.success(systemMenuBiz.getSystemUrl(id));
	}

	/**
	 * 시스템 URL 정보 등록
	 * @param dto
	 * @
	 */
	@PostMapping(value="/admin/st/menu/addSystemUrl")
	@ResponseBody
	@ApiOperation(value = "시스템 URL 정보 등록")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data"),
			@ApiResponse(code = 901, message = "" +
					"[MANDATORY_MISSING] 333333333 - 필수값을 입력해주세요. \n"
			)
	})
	public ApiResult<?> addSystemUrl(SystemUrlRequestDto dto)  {
		return systemMenuBiz.addSystemUrl(dto);
	}

	/**
	 * 시스템 URL 정보 수정
	 * @param dto
	 * @
	 */
	@PostMapping(value="/admin/st/menu/putSystemUrl")
	@ResponseBody
	@ApiOperation(value = "시스템 URL 정보 수정")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data"),
			@ApiResponse(code = 901, message = "" +
					"[MANDATORY_MISSING] 333333333 - 필수값을 입력해주세요. \n"
			)
	})
	public ApiResult<?> putSystemUrl(SystemUrlRequestDto dto)  {
		return systemMenuBiz.putSystemUrl(dto);
	}

	/**
	 * 시스템 URL 정보 삭제
	 * @param id
	 * @
	 */
	@PostMapping(value="/admin/st/menu/delSystemUrl")
	@ResponseBody
	@ApiOperation(value = "시스템 URL 정보 삭제")
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "id", value = "시스템 URL PK", required = true, dataType = "Long")
	})
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data"),
			@ApiResponse(code = 901, message = "" +
					"[MANDATORY_MISSING] 333333333 - 필수값을 입력해주세요. \n"
			)
	})
	public ApiResult<?> delSystemUrl(@RequestParam(value="id", required = true) Long id)  {
		return systemMenuBiz.delSystemUrl(id);
	}


	/**
	 * 메뉴 URL 리스트 조회
	 * @param dto
	 * @return GetMenuUrlListResponseDto
	 * @
	 */
	@PostMapping(value="/admin/st/menu/getMenuUrlList")
	@ResponseBody
	@ApiOperation(value = "메뉴 URL 리스트 조회")
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "stProgramAuthId", value = "프로그램 권한 PK", dataType = "Long")
	})
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GetMenuUrlListResponseDto.class),
			@ApiResponse(code = 901, message = "" +
					"[MANDATORY_MISSING] 333333333 - 필수값을 입력해주세요. \n"
			)
	})
	public ApiResult<?> getMenuUrlList(@RequestParam(value="stProgramAuthId") Long stProgramAuthId)  {
		return ApiResult.success(systemMenuBiz.getMenuUrlList(stProgramAuthId));
	}



	/**
	 * 할당 메뉴 URL 리스트 조회
	 * @param stMenuId
	 * @return GetMenuAssignUrlListResponseDto
	 * @
	 */
	@PostMapping(value="/admin/st/menu/getMenuAssignUrlList")
	@ResponseBody
	@ApiOperation(value = "할당 메뉴 URL 리스트 조회")
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "stProgramAuthId", value = "프로그램 권한 PK", dataType = "Long")
	})
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GetMenuAssignUrlListResponseDto.class),
			@ApiResponse(code = 901, message = "" +
					"[MANDATORY_MISSING] 333333333 - 필수값을 입력해주세요. \n"
			)
	})
	public ApiResult<?> getMenuAssignUrlList(@RequestParam(value="stProgramAuthId") Long stProgramAuthId)  {
		return ApiResult.success(systemMenuBiz.getMenuAssignUrlList(stProgramAuthId));
	}

	/**
	 * 프로그램별 URL 할당 정보 저장
	 * @param dto
	 * @
	 */
	@PostMapping(value = "/admin/st/menu/saveMenuAssignUrl")
	@ResponseBody
	@ApiOperation(value = "프로그램별 URL 할당 정보 저장")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data"),
			@ApiResponse(code = 901, message = "" +
					"[MANDATORY_MISSING] 333333333 - 필수값을 입력해주세요. \n"
			)
	})
	public ApiResult<?> saveMenuAssignUrl(AddMenuAssigUrlRequestDto dto) throws Exception {
		dto.setInsertRequestDtoList((List<AddMenuAssigUrlRequestSaveDto>) BindUtil.convertJsonArrayToDtoList(dto.getInsertData(), AddMenuAssigUrlRequestSaveDto.class));
		dto.setDeleteRequestDtoList((List<AddMenuAssigUrlRequestSaveDto>) BindUtil.convertJsonArrayToDtoList(dto.getDeleteData(), AddMenuAssigUrlRequestSaveDto.class));

		return ApiResult.success(systemMenuBiz.saveMenuAssignUrl(dto));
	}

	/**
	 * 메뉴팝업 데이터 리스트 조회
	 * @param dto
	 * @return GetMenuPopupListResponseDto
	 * @
	 */
	@PostMapping(value = "/admin/st/menu/getMenuPopupList")
	@ResponseBody
	@ApiOperation(value = "메뉴팝업 데이터 리스트 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GetMenuPopupListResponseDto.class),
			@ApiResponse(code = 901, message = "" +
					"[MANDATORY_MISSING] 333333333 - 필수값을 입력해주세요. \n"
			)
	})
	public ApiResult<?> getMenuPopupList(GetMenuPopupListRequestDto dto) throws Exception {
		return systemMenuBiz.getMenuPopupList(BindUtil.bindDto(request, GetMenuPopupListRequestDto.class));
	}
}
