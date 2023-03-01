package kr.co.pulmuone.v1.system.menu.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.system.menu.dto.AddMenuAssigUrlRequestDto;
import kr.co.pulmuone.v1.system.menu.dto.GetMenuAssignUrlListResponseDto;
import kr.co.pulmuone.v1.system.menu.dto.GetMenuGroupListRequestDto;
import kr.co.pulmuone.v1.system.menu.dto.GetMenuGroupListResponseDto;
import kr.co.pulmuone.v1.system.menu.dto.GetMenuGroupNameListResponseDto;
import kr.co.pulmuone.v1.system.menu.dto.GetMenuGroupRequestDto;
import kr.co.pulmuone.v1.system.menu.dto.GetMenuGroupResponseDto;
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
import kr.co.pulmuone.v1.system.menu.dto.SystemUrlRequestDto;

public interface SystemMenuBiz {

	GetMenuGroupListResponseDto getMenuGroupList(GetMenuGroupListRequestDto dto);

	GetMenuGroupResponseDto getMenuGroup(GetMenuGroupRequestDto dto);

	ApiResult<?> addMenuGroup(MenuGroupRequestDto dto);

	ApiResult<?> putMenuGroup(MenuGroupRequestDto dto);

	ApiResult<?> delMenuGroup(Long inputStMenuGroupId);

	GetMenuGroupNameListResponseDto getMenuGroupNameList();

	GetMenuGroupNameListResponseDto getRoleTypeMenuGroupNameList(Long stRoleTypeId);

	GetMenuListResponseDto getMenuList(GetMenuListRequestDto dto);

	GetMenuResponseDto getMenu(GetMenuRequestDto dto);

	ApiResult<?> addMenu(MenuRequestDto dto);

	ApiResult<?> putMenu(MenuRequestDto dto);

	ApiResult<?> delMenu(Long stMenuId);

	GetParentMenuListResponseDto getParentMenuList(GetParentMenuListRequestDto dto);

	/** 시스템 URL 리스트 조회 */
	GetSystemUrlListResponseDto getSystemUrlList(GetSystemUrlListRequestDto getSystemUrlListRequestDto);

	/** 시스템 URL 상세 정보 조회 */
	GetSystemUrlResponseDto getSystemUrl(Long id);

	ApiResult<?> addSystemUrl(SystemUrlRequestDto addSystemUrlListRequestDto);

	ApiResult<?> addSystemUrlMenuInfo(SystemUrlRequestDto addSystemUrlListRequestDto);

	ApiResult<?> putSystemUrl(SystemUrlRequestDto dto);

	ApiResult<?> delSystemUrl(Long id);

	GetMenuUrlListResponseDto getMenuUrlList(Long stProgramAuthId);

	GetMenuAssignUrlListResponseDto getMenuAssignUrlList(Long stProgramAuthId);

	ApiResult<?> saveMenuAssignUrl(AddMenuAssigUrlRequestDto dto);

	ApiResult<?> getMenuPopupList(GetMenuPopupListRequestDto dto);

	ApiResult<?> getSystemAuthority();
}
