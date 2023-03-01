package kr.co.pulmuone.v1.comm.mapper.base;

import java.util.List;

import kr.co.pulmuone.v1.base.dto.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.pulmuone.v1.base.dto.GetAuthMenuParamDto;
import kr.co.pulmuone.v1.base.dto.GetCodeListRequestDto;
import kr.co.pulmuone.v1.base.dto.GetMenuListRequestDto;
import kr.co.pulmuone.v1.base.dto.GetProgramListRequestDto;

@Mapper
public interface StComnMapper {

	List<GetCodeListResultVo> getCodeList(GetCodeListRequestDto dto);

	List<GetProgramListResultVo> getProgramList(GetProgramListRequestDto dto);

	List<GetMenuGroupListResultVo> getMenuGroupList(GetMenuListRequestDto dto);

	List<GetMenuListResultVo> getMenuList(GetMenuListRequestDto dto);

	String[] getAuthMenu(GetAuthMenuParamDto dto);

	boolean isRoleMenuAuthUrl (@Param("stRoleTpId") String stRoleTpId, @Param("stMenuId") String stMenuId, @Param("url") String url);

	boolean isRoleMenuAuthUrl (@Param("listRoleId") List<String> listRoleId, @Param("stMenuId") String stMenuId, @Param("url") String url);

	GetMenuNameResultVo getMenuName(GetMenuNameParamVo dto);

	String getCodeName(@Param("code") String code);

}
