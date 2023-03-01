package kr.co.pulmuone.v1.comm.mapper.system.menu;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.system.menu.dto.*;
import kr.co.pulmuone.v1.system.menu.dto.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SystemMenuMapper {

	int getMenuGroupListCount(GetMenuGroupListRequestDto dto);

	Page<GetMenuGroupListResultVo> getMenuGroupList(GetMenuGroupListRequestDto dto);

	int getMenuGroupNameListCount();

	List<GetMenuGroupNameListResultVo> getMenuGroupNameList();

	List<GetMenuGroupNameListResultVo> getRoleTypeMenuGroupNameList(Long stRoleTypeId);

	GetMenuGroupResultVo getMenuGroup(GetMenuGroupRequestDto dto);

	int duplicateMenuGroupCount(MenuGroupRequestDto dto);

    int addMenuGroup(MenuGroupRequestDto dto);

    int putMenuGroup(MenuGroupRequestDto dto);

    int delMenuGroup(Long inputStMenuGroupId);

	int getMenuListCount(GetMenuListRequestDto dto);

	Page<GetMenuListResultVo> getMenuList(GetMenuListRequestDto dto);

	GetMenuResultVo getMenu(GetMenuRequestDto dto);

	int duplicateMenuCount(MenuRequestDto dto);

    int addMenu(MenuRequestDto dto);

    int putMenu(MenuRequestDto dto);

    int delMenu(Long stMenuId);

    int getParentMenuListCount(GetParentMenuListRequestDto dto);

    Page<GetParentMenuListResultVo> getParentMenuList(GetParentMenuListRequestDto dto);

	Page<GetSystemUrlListResultVo> getSystemUrlList(GetSystemUrlListRequestDto dto);

	int getSystemUrlListCount(GetSystemUrlListRequestDto dto);

	GetSystemUrlResultVo getSystemUrl(Long id);

	int addSystemUrl(SystemUrlRequestDto dto);

	int getSystemUrlMenuInfo(SystemUrlRequestDto dto);

	int addSystemUrlMenuInfo(SystemUrlRequestDto dto);

	int putSystemUrl(SystemUrlRequestDto dto);

	int delSystemUrl(Long id);

	int duplicateSystemUrlCount(SystemUrlRequestDto dto);

	List<GetMenuUrlListResultVo> getMenuUrlList(Long stProgramAuthId);

	int getMenuUrlListCount(Long stProgramAuthId);

	List<GetMenuAssignUrlListResultVo> getMenuAssignUrlList(Long stProgramAuthId);

	int getMenuAssignUrlListCount(Long stProgramAuthId);

	int delMenuAssigUrl(List<AddMenuAssigUrlRequestSaveDto> list);

	int saveMenuAssignUrl(@Param("stProgramAuthId") Long stProgramAuthId, @Param("voList") List<AddMenuAssigUrlRequestSaveDto> voList);

	Page<GetMenuPopupListResultVo> getMenuPopupList(GetMenuPopupListRequestDto dto);

	int getMenuPopupListCount(GetMenuPopupListRequestDto dto);

    List<GetSystemAuthorityResultVo> getSystemAuthority();
}
