package kr.co.pulmuone.v1.comm.mapper.system.help;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.system.help.dto.HelpRequestDto;
import kr.co.pulmuone.v1.system.help.dto.GetHelpListRequestDto;
import kr.co.pulmuone.v1.system.help.dto.vo.GetHelpListResultVo;
import kr.co.pulmuone.v1.system.help.dto.vo.GetHelpResultVo;

@Mapper
public interface SystemHelpMapper {

	int getHelpListCount(GetHelpListRequestDto dto);

	Page<GetHelpListResultVo> getHelpList(GetHelpListRequestDto dto);

	GetHelpResultVo getHelp(Long id);

	int addHelp(HelpRequestDto dto);

	int putHelp(HelpRequestDto dto);

	int delHelp(String id);

	int duplicateHelpCount(HelpRequestDto dto);

	List<GetHelpListResultVo> getHelpListByArray(GetHelpListRequestDto dto);

}
