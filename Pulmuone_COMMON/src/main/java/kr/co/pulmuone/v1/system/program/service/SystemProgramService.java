package kr.co.pulmuone.v1.system.program.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import kr.co.pulmuone.v1.comm.mapper.system.program.SystemProgramMapper;
import kr.co.pulmuone.v1.system.program.dto.*;
import kr.co.pulmuone.v1.system.program.dto.vo.GetProgramListResultVo;
import kr.co.pulmuone.v1.system.program.dto.vo.GetProgramNameListResultVo;
import kr.co.pulmuone.v1.system.program.dto.vo.GetProgramResultVo;
import kr.co.pulmuone.v1.system.program.dto.vo.ProgramAuthVo;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SystemProgramService {

	private final SystemProgramMapper mapper;

	protected GetProgramListResponseDto findAllProgramList(GetProgramListRequestDto dto) {

		GetProgramListResponseDto result = new GetProgramListResponseDto();

		PageMethod.startPage(dto.getPage(), dto.getPageSize());
		Page<GetProgramListResultVo> rows = mapper.getProgramList(dto);

		result.setTotal((int) rows.getTotal());
		result.setRows(rows.getResult());

		return result;
	}

	protected GetProgramNameListResponseDto findAllProgramNameList(GetProgramNameListRequestDto dto) {
		GetProgramNameListResponseDto result = new GetProgramNameListResponseDto();

		PageMethod.startPage(dto.getPage(), dto.getPageSize());
		Page<GetProgramNameListResultVo> rows = mapper.getProgramNameList(dto);

		result.setTotal((int) rows.getTotal());
		result.setRows(rows.getResult());

		return result;
	}

	protected GetProgramResponseDto findProgram(ProgramRequestDto dto) {
		GetProgramResponseDto result = new GetProgramResponseDto();
		result.setRows(mapper.getProgram(dto));
		result.setAuthList(mapper.getProgramAuthList(dto.getStProgramId()));
		return result;
	}

	protected int existsProgram(ProgramRequestDto dto) {
		return mapper.duplicateProgramCount(dto);
	}

	protected int addProgram(ProgramRequestDto dto) {
		return mapper.addProgram(dto);
	}

	protected int putProgram(ProgramRequestDto dto) {
		return mapper.putProgram(dto);
	}

	protected int delProgram(Long id) {
		return mapper.delProgram(id);
	}

	protected int addProgramAuth(ProgramRequestDto dto) {
		return mapper.addProgramAuth(dto);
	}

	protected int putProgramAuth(ProgramRequestDto dto) {
		return mapper.putProgramAuth(dto);
	}

	protected int delProgramAuthByStProgramId(Long id) {
		return mapper.delProgramAuthByStProgramId(id);
	}

	protected GetProgramAuthListResponseDto getProgramAuthUseList(Long stProgramId) {
		GetProgramAuthListResponseDto result = new GetProgramAuthListResponseDto();
		result.setRows(mapper.getProgramAuthUseList(stProgramId));
		return result;
	}
}
