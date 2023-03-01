package kr.co.pulmuone.v1.comm.mapper.system.code;

import java.util.List;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;

import kr.co.pulmuone.v1.system.code.dto.GetCodeListRequestDto;
import kr.co.pulmuone.v1.system.code.dto.GetCodeMasterListRequestDto;
import kr.co.pulmuone.v1.system.code.dto.GetCodeMasterNameListRequestDto;
import kr.co.pulmuone.v1.system.code.dto.CodeMasterRequestDto;
import kr.co.pulmuone.v1.system.code.dto.SaveCodeRequestSaveDto;
import kr.co.pulmuone.v1.system.code.dto.vo.GetCodeListResultVo;
import kr.co.pulmuone.v1.system.code.dto.vo.GetCodeMasterListResultVo;
import kr.co.pulmuone.v1.system.code.dto.vo.GetCodeMasterNameListResultVo;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SystemCodeMapper {

	int getCodeMasterListCount(GetCodeMasterListRequestDto dto);

	Page<GetCodeMasterListResultVo> getCodeMasterList(GetCodeMasterListRequestDto dto);

	int duplicateCommonMasterCodeCount(CodeMasterRequestDto dto);

	int addCodeMaster(CodeMasterRequestDto dto);

	int putCodeMaster(CodeMasterRequestDto dto);

	int delCodeMaster(String stComnCodeMstId);

	int getCodeMasterNameListCount(GetCodeMasterNameListRequestDto dto);

	Page<GetCodeMasterNameListResultVo> getCodeMasterNameList(GetCodeMasterNameListRequestDto dto);

	List<GetCodeListResultVo> getCodeList(GetCodeListRequestDto dto);

	int addCode(SaveCodeRequestSaveDto dto);

	int putCode(SaveCodeRequestSaveDto dto);

	int delCode(SaveCodeRequestSaveDto dto);

	GetCodeListResultVo getCode(String stComnCodeId);

	List<String> decryptStr(@Param("encryptList") List<String> encryptList);

}
