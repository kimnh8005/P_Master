package kr.co.pulmuone.v1.system.menu.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
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
import kr.co.pulmuone.v1.system.menu.dto.vo.GetMenuPopupListResultVo;
import kr.co.pulmuone.v1.system.menu.dto.vo.GetSystemAuthorityResultVo;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SystemMenuBizImpl implements SystemMenuBiz {
	private final SystemMenuService service;


	public GetMenuGroupListResponseDto getMenuGroupList(GetMenuGroupListRequestDto dto){
		return service.getMenuGroupList(dto);
	}

	@Override
	public GetMenuGroupResponseDto getMenuGroup(GetMenuGroupRequestDto dto) {
		GetMenuGroupResponseDto result = new GetMenuGroupResponseDto();
		result.setRows(service.getMenuGroup(dto));

		return result;
	}


	/**
	 * 메뉴그룹 신규저장
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> addMenuGroup(MenuGroupRequestDto dto){
		if(service.existsMenuGroup(dto) > 0) return ApiResult.result(BaseEnums.CommBase.DUPLICATE_DATA);
		if(service.addMenuGroup(dto) <= 0) return ApiResult.result(BaseEnums.CommBase.VALID_ERROR);

		return ApiResult.success();
	}


	/**
	 * 메뉴그룹 데이터 수정
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> putMenuGroup(MenuGroupRequestDto dto){
		if(service.existsMenuGroup(dto) > 0) return ApiResult.result(BaseEnums.CommBase.DUPLICATE_DATA);
		if(service.putMenuGroup(dto) <= 0) return ApiResult.result(BaseEnums.CommBase.VALID_ERROR);

		return ApiResult.success();
	}


	/**
	 * 메뉴그룹 데이터 삭제
	 * @return
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> delMenuGroup(Long inputStMenuGroupId) {
		if(service.delMenuGroup(inputStMenuGroupId) <= 0) {
			return ApiResult.result(BaseEnums.CommBase.VALID_ERROR);
		}

		return ApiResult.success();
	}

	@Override
	public GetMenuGroupNameListResponseDto getMenuGroupNameList(){
		return service.getMenuGroupNameList();
	}

	@Override
	public GetMenuGroupNameListResponseDto getRoleTypeMenuGroupNameList(Long stRoleTypeId) {
		return service.getRoleTypeMenuGroupNameList(stRoleTypeId);
	}

	@Override
	public GetMenuListResponseDto getMenuList(GetMenuListRequestDto dto){
		return service.getMenuList(dto);
	}


	@Override
	public GetMenuResponseDto getMenu(GetMenuRequestDto dto) {
		return service.getMenu(dto);
	}


	@Override
	public ApiResult<?> addMenu(MenuRequestDto dto){
		if(service.existsMenu(dto) > 0) return ApiResult.result(BaseEnums.CommBase.DUPLICATE_DATA);
		if(service.addMenu(dto) <= 0) return ApiResult.result(BaseEnums.CommBase.VALID_ERROR);

		return ApiResult.success();
	}


	@Override
	public ApiResult<?> putMenu(MenuRequestDto dto){
		if(service.existsMenu(dto) > 0) return ApiResult.result(BaseEnums.CommBase.DUPLICATE_DATA);
		if(service.putMenu(dto) <= 0) return ApiResult.result(BaseEnums.CommBase.VALID_ERROR);

		return ApiResult.success();
	}


	@Override
	public ApiResult<?> delMenu(Long stMenuId) {
		if(service.delMenu(stMenuId) <= 0) {
			return ApiResult.result(BaseEnums.CommBase.VALID_ERROR);
		}

		return ApiResult.success();
	}


	@Override
	public GetParentMenuListResponseDto getParentMenuList(GetParentMenuListRequestDto dto){
		return service.getParentMenuList(dto);
	}


	@Override
	public GetSystemUrlListResponseDto getSystemUrlList(GetSystemUrlListRequestDto getSystemUrlListRequestDto) {
		return service.getSystemUrlList(getSystemUrlListRequestDto);
	}

	@Override
	public GetSystemUrlResponseDto getSystemUrl(Long id) {
		return service.getSystemUrl(id);
	}

	@Override
	public ApiResult<?> addSystemUrl(SystemUrlRequestDto dto) {
		if(service.existsSystemUrl(dto) > 0) return ApiResult.result(BaseEnums.CommBase.DUPLICATE_DATA);
		if(service.addSystemUrl(dto) <= 0) return ApiResult.result(BaseEnums.CommBase.VALID_ERROR);

		return ApiResult.success();
	}

	@Override
	public ApiResult<?> addSystemUrlMenuInfo(SystemUrlRequestDto dto) {
	  int existCnt = service.getSystemUrlMenuInfo(dto);
	  if (existCnt > 0) {
      return ApiResult.result(BaseEnums.CommBase.DUPLICATE_DATA);
    }
	  else {
      int resultInt = service.addSystemUrlMenuInfo(dto);
      if (resultInt <= 0) {
        return ApiResult.result(BaseEnums.CommBase.VALID_ERROR);
      }
      else {
        return ApiResult.success();
      }
    }
	}

	@Override
	public ApiResult<?> putSystemUrl(SystemUrlRequestDto dto) {
		if(service.existsSystemUrl(dto) > 0) return ApiResult.result(BaseEnums.CommBase.DUPLICATE_DATA);
		if(service.putSystemUrl(dto) <= 0) return ApiResult.result(BaseEnums.CommBase.VALID_ERROR);

		return ApiResult.success();
	}

	@Override
	public ApiResult<?> delSystemUrl(Long id) {
		if(service.delSystemUrl(id) <= 0) {
			return ApiResult.result(BaseEnums.CommBase.VALID_ERROR);
		}

		return ApiResult.success();
	}

	@Override
	public GetMenuUrlListResponseDto getMenuUrlList(Long stProgramAuthId){
		return service.getMenuUrlList(stProgramAuthId);
	}

	@Override
	public GetMenuAssignUrlListResponseDto getMenuAssignUrlList(Long stProgramAuthId){
		return service.getMenuAssignUrlList(stProgramAuthId);
	}


	@Override
	public ApiResult<?> saveMenuAssignUrl(AddMenuAssigUrlRequestDto dto){
		List<AddMenuAssigUrlRequestSaveDto> insertRequestDtoList = dto.getInsertRequestDtoList();
		List<AddMenuAssigUrlRequestSaveDto> deleteRequestDtoList = dto.getDeleteRequestDtoList();

		if(!insertRequestDtoList.isEmpty()){
			service.saveMenuAssignUrl(dto.getStProgramAuthId(), insertRequestDtoList);
		}

		if(!deleteRequestDtoList.isEmpty()){
			service.delMenuAssigUrl(deleteRequestDtoList);
		}

		return ApiResult.success();
	}

	@Override
	public ApiResult<?> getMenuPopupList(GetMenuPopupListRequestDto dto){
		GetMenuPopupListResponseDto result = new GetMenuPopupListResponseDto();
		Page<GetMenuPopupListResultVo> helpListResult = service.getMenuPopupList(dto);

		result.setTotal(helpListResult.getTotal());
		result.setRows(helpListResult.getResult());

	    return ApiResult.success(result);

	}

	@Override
	public ApiResult<?> getSystemAuthority() {
		SystemAuthorityResponseDto systemAuthorityResponseDto = new SystemAuthorityResponseDto();

		List<GetSystemAuthorityResultVo> systemAuthorityResultVo = service.getSystemAuthority();
		systemAuthorityResponseDto.setRows(systemAuthorityResultVo);

		return ApiResult.success(systemAuthorityResponseDto);
	}
}
