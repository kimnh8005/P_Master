package kr.co.pulmuone.v1.system.code.service;

import java.util.List;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.system.code.dto.GetCodeListRequestDto;
import kr.co.pulmuone.v1.system.code.dto.GetCodeListResponseDto;
import kr.co.pulmuone.v1.system.code.dto.GetCodeMasterListRequestDto;
import kr.co.pulmuone.v1.system.code.dto.GetCodeMasterListResponseDto;
import kr.co.pulmuone.v1.system.code.dto.GetCodeMasterNameListRequestDto;
import kr.co.pulmuone.v1.system.code.dto.GetCodeMasterNameListResponseDto;
import kr.co.pulmuone.v1.system.code.dto.CodeMasterRequestDto;
import kr.co.pulmuone.v1.system.code.dto.SaveCodeRequestSaveDto;
import kr.co.pulmuone.v1.system.code.dto.vo.GetCodeListResultVo;
import kr.co.pulmuone.v1.system.code.dto.vo.GetCodeMasterListResultVo;
import kr.co.pulmuone.v1.system.code.dto.vo.GetCodeMasterNameListResultVo;
import kr.co.pulmuone.v1.comm.mapper.system.code.SystemCodeMapper;

@RequiredArgsConstructor
@Service
public class SystemCodeService {

	private final SystemCodeMapper systemCodeMapper;


	protected GetCodeMasterListResponseDto getCodeMasterList(GetCodeMasterListRequestDto dto) {
		GetCodeMasterListResponseDto result = new GetCodeMasterListResponseDto();

		PageMethod.startPage(dto.getPage(), dto.getPageSize());
		Page<GetCodeMasterListResultVo> rows = systemCodeMapper.getCodeMasterList(dto);	// rows

		result.setTotal((int)rows.getTotal());
		result.setRows(rows.getResult());

		return result;
	}

	protected int addCodeMaster(CodeMasterRequestDto dto) {
		return systemCodeMapper.addCodeMaster(dto);
	}

	protected int putCodeMaster(CodeMasterRequestDto dto) {
		return systemCodeMapper.putCodeMaster(dto);
	}

	protected int delCodeMaster(String stComnCodeMstId) {
		return systemCodeMapper.delCodeMaster(stComnCodeMstId);
	}

	protected boolean isDuplicateCommonMasterCode(CodeMasterRequestDto dto) {
		return systemCodeMapper.duplicateCommonMasterCodeCount(dto) > 0 ? true : false;
	}

	protected GetCodeMasterNameListResponseDto getCodeMasterNameList(GetCodeMasterNameListRequestDto dto) {
		GetCodeMasterNameListResponseDto result = new GetCodeMasterNameListResponseDto();

		PageMethod.startPage(dto.getPage(), dto.getPageSize());
		Page<GetCodeMasterNameListResultVo> rows = systemCodeMapper.getCodeMasterNameList(dto);	// rows

		result.setTotal((int)rows.getTotal());
		result.setRows(rows.getResult());

		return result;
	}

	protected GetCodeListResponseDto getCodeList(GetCodeListRequestDto dto) {
		GetCodeListResponseDto result = new GetCodeListResponseDto();

		List<GetCodeListResultVo> rows = systemCodeMapper.getCodeList(dto);	// rows

		result.setRows(rows);

		return result;
	}

	protected int addCode(SaveCodeRequestSaveDto dto) {
		return systemCodeMapper.addCode(dto);
	}

	protected int putCode(SaveCodeRequestSaveDto dto) {
		return systemCodeMapper.putCode(dto);
	}

	protected int delCode(SaveCodeRequestSaveDto dto) {
		return systemCodeMapper.delCode(dto);
	}

	protected GetCodeListResultVo getCode(String stComnCodeId) {
		return systemCodeMapper.getCode(stComnCodeId);
	}

	protected List<String> decryptStr(List<String> encryptList) {
		return systemCodeMapper.decryptStr(encryptList);
	}

}
