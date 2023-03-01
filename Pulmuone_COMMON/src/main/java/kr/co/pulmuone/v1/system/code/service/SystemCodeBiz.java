package kr.co.pulmuone.v1.system.code.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.system.code.dto.GetCodeListRequestDto;
import kr.co.pulmuone.v1.system.code.dto.GetCodeListResponseDto;
import kr.co.pulmuone.v1.system.code.dto.GetCodeMasterListRequestDto;
import kr.co.pulmuone.v1.system.code.dto.GetCodeMasterListResponseDto;
import kr.co.pulmuone.v1.system.code.dto.GetCodeMasterNameListRequestDto;
import kr.co.pulmuone.v1.system.code.dto.GetCodeMasterNameListResponseDto;
import kr.co.pulmuone.v1.system.code.dto.CodeMasterRequestDto;
import kr.co.pulmuone.v1.system.code.dto.SaveCodeRequestDto;
import kr.co.pulmuone.v1.system.code.dto.vo.GetCodeListResultVo;

import java.util.List;

public interface SystemCodeBiz {

	GetCodeMasterListResponseDto getCodeMasterList(GetCodeMasterListRequestDto dto) throws Exception;

	ApiResult<?> addCodeMaster(CodeMasterRequestDto dto) throws Exception;

	ApiResult<?> putCodeMaster(CodeMasterRequestDto dto) throws Exception;

	ApiResult<?> delCodeMaster(String stComnCodeMstId) throws Exception;

	GetCodeMasterNameListResponseDto getCodeMasterNameList(GetCodeMasterNameListRequestDto convertRequestToObject) throws Exception;

	GetCodeListResponseDto getCodeList(GetCodeListRequestDto convertRequestToObject) throws Exception;

	ApiResult<?> saveCode(SaveCodeRequestDto dto) throws Exception;

	GetCodeListResultVo getCode(String stComnCodeId) throws Exception;

	List<String> decryptStr(List<String> encryptList);

}
