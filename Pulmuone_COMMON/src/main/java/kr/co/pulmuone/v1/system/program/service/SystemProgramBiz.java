package kr.co.pulmuone.v1.system.program.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.system.program.dto.GetProgramAuthListResponseDto;
import kr.co.pulmuone.v1.system.program.dto.GetProgramListRequestDto;
import kr.co.pulmuone.v1.system.program.dto.GetProgramListResponseDto;
import kr.co.pulmuone.v1.system.program.dto.GetProgramNameListRequestDto;
import kr.co.pulmuone.v1.system.program.dto.GetProgramNameListResponseDto;
import kr.co.pulmuone.v1.system.program.dto.GetProgramResponseDto;
import kr.co.pulmuone.v1.system.program.dto.ProgramRequestDto;

public interface SystemProgramBiz {

	GetProgramListResponseDto getProgramList(GetProgramListRequestDto dto) throws Exception;

	GetProgramResponseDto getProgram(ProgramRequestDto dto) throws Exception;

	ApiResult<?> addProgram(ProgramRequestDto dto) throws Exception;

	ApiResult<?> putProgram(ProgramRequestDto dto) throws Exception;

	ApiResult<?>  delProgram(Long id) throws Exception;

	GetProgramNameListResponseDto getProgramNameList(GetProgramNameListRequestDto dto) throws Exception;

	GetProgramAuthListResponseDto getProgramAuthUseList(Long stProgramId) throws Exception;
}
