package kr.co.pulmuone.v1.system.menu.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.mapper.system.menu.SystemMenuMapper;
import kr.co.pulmuone.v1.system.menu.dto.*;
import kr.co.pulmuone.v1.system.menu.dto.vo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SystemMenuService {
	private final SystemMenuMapper mapper;


	protected GetMenuGroupListResponseDto getMenuGroupList(GetMenuGroupListRequestDto dto) {
		GetMenuGroupListResponseDto result = new GetMenuGroupListResponseDto();

		PageMethod.startPage(dto.getPage(), dto.getPageSize());
		Page<GetMenuGroupListResultVo> rows = mapper.getMenuGroupList(dto);	// rows

		result.setTotal((int)rows.getTotal());
		result.setRows(rows.getResult());

		return result;
	}


	protected GetMenuGroupResultVo getMenuGroup(GetMenuGroupRequestDto dto) {
		return mapper.getMenuGroup(dto);
	}

	protected int existsMenuGroup(MenuGroupRequestDto dto) {
		return mapper.duplicateMenuGroupCount(dto);
	}

	/**
	 * 메뉴그룹 신규저장
	 * @param dto
	 * @return
	 * @
	 */

	protected int addMenuGroup(MenuGroupRequestDto dto) {
		return mapper.addMenuGroup(dto);
	}


	/**
	 * 메뉴그룹 데이터 수정
	 * @param dto
	 * @return
	 * @
	 */

	protected int putMenuGroup(MenuGroupRequestDto dto) {
		return mapper.putMenuGroup(dto);
	}


    /**
     * 메뉴그룹 데이터 삭제
	 * @return
	 * @
     */

    protected int delMenuGroup(Long inputStMenuGroupId) {
		return mapper.delMenuGroup(inputStMenuGroupId);
    }

	protected GetMenuGroupNameListResponseDto getMenuGroupNameList() {
		GetMenuGroupNameListResponseDto result = new GetMenuGroupNameListResponseDto();

		int total = mapper.getMenuGroupNameListCount();	// total
		List<GetMenuGroupNameListResultVo> rows = mapper.getMenuGroupNameList();	// rows

		result.setTotal(total);
		result.setRows(rows);

		return result;
	}

	protected GetMenuGroupNameListResponseDto getRoleTypeMenuGroupNameList(Long stRoleTypeId) {
		GetMenuGroupNameListResponseDto result = new GetMenuGroupNameListResponseDto();

		int total = mapper.getMenuGroupNameListCount();	// total
		List<GetMenuGroupNameListResultVo> rows = mapper.getRoleTypeMenuGroupNameList(stRoleTypeId);	// rows

		result.setTotal(total);
		result.setRows(rows);

		return result;
	}

	protected GetMenuListResponseDto getMenuList(GetMenuListRequestDto dto) {
		GetMenuListResponseDto result = new GetMenuListResponseDto();

		PageMethod.startPage(dto.getPage(), dto.getPageSize());
		Page<GetMenuListResultVo> rows = mapper.getMenuList(dto);	// rows

		result.setTotal((int)rows.getTotal());
		result.setRows(rows.getResult());

		return result;
	}

	protected GetMenuResponseDto getMenu(GetMenuRequestDto dto) {
		GetMenuResponseDto result = new GetMenuResponseDto();
		GetMenuResultVo vo = mapper.getMenu(dto);

		result.setRows(vo);

		return result;
	}

	protected int existsMenu(MenuRequestDto dto) {
    	return mapper.duplicateMenuCount(dto);
	}

	protected int addMenu(MenuRequestDto dto) {
		return mapper.addMenu(dto);
	}

	protected int putMenu(MenuRequestDto dto) {
		return mapper.putMenu(dto);
	}

    protected int delMenu(Long stMenuId) {
    	return mapper.delMenu(stMenuId);
    }

	protected GetParentMenuListResponseDto getParentMenuList(GetParentMenuListRequestDto dto) {
		GetParentMenuListResponseDto result = new GetParentMenuListResponseDto();

		PageMethod.startPage(dto.getPage(), dto.getPageSize());
		Page<GetParentMenuListResultVo> rows = mapper.getParentMenuList(dto);	// rows

		result.setTotal((int)rows.getTotal());
		result.setRows(rows.getResult());

		return result;
	}

	protected GetSystemUrlListResponseDto getSystemUrlList(GetSystemUrlListRequestDto dto) {
		GetSystemUrlListResponseDto result = new GetSystemUrlListResponseDto();

		PageMethod.startPage(dto.getPage(), dto.getPageSize());
		Page<GetSystemUrlListResultVo> rows = mapper.getSystemUrlList(dto);

		result.setTotal((int)rows.getTotal());
		result.setRows(rows.getResult());

		return result;
	}


	protected GetSystemUrlResponseDto getSystemUrl(Long id) {
		GetSystemUrlResponseDto result = new GetSystemUrlResponseDto();

        GetSystemUrlResultVo rows = mapper.getSystemUrl(id);

        result.setRows(rows);

		return result;
	}

	protected int existsSystemUrl(SystemUrlRequestDto dto) {
    	return mapper.duplicateSystemUrlCount(dto);
	}

	protected int addSystemUrl(SystemUrlRequestDto dto) {
		return mapper.addSystemUrl(dto);
	}

	protected int getSystemUrlMenuInfo(SystemUrlRequestDto dto) {
    return mapper.getSystemUrlMenuInfo(dto);
	}

	protected int addSystemUrlMenuInfo(SystemUrlRequestDto dto) {
		return mapper.addSystemUrlMenuInfo(dto);
	}

	protected int putSystemUrl(SystemUrlRequestDto dto) {
		return mapper.putSystemUrl(dto);
	}

	protected int delSystemUrl(Long id) {
		return mapper.delSystemUrl(id);
	}

	protected GetMenuUrlListResponseDto getMenuUrlList(Long stProgramAuthId) {
		GetMenuUrlListResponseDto result = new GetMenuUrlListResponseDto();

		int total = mapper.getMenuUrlListCount(stProgramAuthId);	// total
		List<GetMenuUrlListResultVo> rows = mapper.getMenuUrlList(stProgramAuthId);	// rows

		result.setTotal(total);
		result.setRows(rows);

		return result;
	}

	protected GetMenuAssignUrlListResponseDto getMenuAssignUrlList(Long stProgramAuthId) {
		GetMenuAssignUrlListResponseDto result = new GetMenuAssignUrlListResponseDto();

		int total = mapper.getMenuAssignUrlListCount(stProgramAuthId);	// total
		List<GetMenuAssignUrlListResultVo> rows = mapper.getMenuAssignUrlList(stProgramAuthId);	// rows

		result.setTotal(total);
		result.setRows(rows);

		return result;
	}

	protected int delMenuAssigUrl(List<AddMenuAssigUrlRequestSaveDto> list) {
    	return mapper.delMenuAssigUrl(list);
	}

	protected int saveMenuAssignUrl(Long stProgramAuthId, List<AddMenuAssigUrlRequestSaveDto> dto) {
		return mapper.saveMenuAssignUrl(stProgramAuthId, dto);
	}

	protected Page<GetMenuPopupListResultVo> getMenuPopupList(GetMenuPopupListRequestDto dto) {
		PageMethod.startPage(dto.getPage(), dto.getPageSize());
		return mapper.getMenuPopupList(dto);
	}

    public List<GetSystemAuthorityResultVo> getSystemAuthority() {
    	return mapper.getSystemAuthority();
    }
}
