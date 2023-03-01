package kr.co.pulmuone.v1.system.help.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.system.help.dto.HelpRequestDto;
import kr.co.pulmuone.v1.system.help.dto.GetHelpListRequestDto;
import kr.co.pulmuone.v1.system.help.dto.GetHelpResponseDto;

public interface SystemHelpBiz {

	ApiResult<?> getHelpList(GetHelpListRequestDto dto);

	GetHelpResponseDto getHelp(Long id);

	ApiResult<?> addHelp(HelpRequestDto dto);

	ApiResult<?> putHelp(HelpRequestDto dto);

	ApiResult<?> delHelp(String id);

	ApiResult<?> getHelpListByArray(String systemHelpId);

}
