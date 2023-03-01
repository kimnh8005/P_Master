package kr.co.pulmuone.v1.comm.mapper.system.program;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.system.program.dto.GetProgramListRequestDto;
import kr.co.pulmuone.v1.system.program.dto.GetProgramNameListRequestDto;
import kr.co.pulmuone.v1.system.program.dto.ProgramRequestDto;
import kr.co.pulmuone.v1.system.program.dto.vo.GetProgramListResultVo;
import kr.co.pulmuone.v1.system.program.dto.vo.GetProgramNameListResultVo;
import kr.co.pulmuone.v1.system.program.dto.vo.GetProgramResultVo;
import kr.co.pulmuone.v1.system.program.dto.vo.ProgramAuthVo;

@Mapper
public interface SystemProgramMapper {

	int getProgramListCount(GetProgramListRequestDto dto);

	Page<GetProgramListResultVo> getProgramList(GetProgramListRequestDto dto);

	GetProgramResultVo getProgram(ProgramRequestDto dto);

	int duplicateProgramCount(ProgramRequestDto dto);

    int addProgram(ProgramRequestDto dto);

    int putProgram(ProgramRequestDto dto);

    int delProgram(Long stProgramId);

    List<ProgramAuthVo> getProgramAuthList(Long stProgramId);

    int addProgramAuth(ProgramRequestDto dto);

    int putProgramAuth(ProgramRequestDto dto);

    int delProgramAuthByStProgramId(Long stProgramId);

	int getProgramNameListCount(GetProgramNameListRequestDto dto);

	Page<GetProgramNameListResultVo> getProgramNameList(GetProgramNameListRequestDto dto);

	List<ProgramAuthVo> getProgramAuthUseList(Long stProgramId);
}
