package kr.co.pulmuone.v1.system.code.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.system.code.dto.*;
import kr.co.pulmuone.v1.system.code.dto.vo.GetCodeListResultVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SystemCodeBizImpl implements SystemCodeBiz  {
	private final SystemCodeService service;

	@Override
	public GetCodeMasterListResponseDto getCodeMasterList(GetCodeMasterListRequestDto dto) {
		return service.getCodeMasterList(dto);
	}

	@Override
	public ApiResult<?> addCodeMaster(CodeMasterRequestDto dto) {
		if(service.isDuplicateCommonMasterCode(dto)) {
			return ApiResult.result(BaseEnums.CommBase.DUPLICATE_DATA);
		} else {
			service.addCodeMaster(dto);
		}

		return ApiResult.success();
	}

	@Override
	public ApiResult<?> putCodeMaster(CodeMasterRequestDto dto) {
		if(service.isDuplicateCommonMasterCode(dto)) {
			return ApiResult.result(BaseEnums.CommBase.DUPLICATE_DATA);
		} else {
			service.putCodeMaster(dto);
		}

		return ApiResult.success();
	}

	@Override
	public ApiResult<?> delCodeMaster(String stComnCodeMstId) {
		if(service.delCodeMaster(stComnCodeMstId) > 0) {
			return ApiResult.success();
		} else {
			return ApiResult.result(BaseEnums.CommBase.VALID_ERROR);
		}
	}

	@Override
	public GetCodeMasterNameListResponseDto getCodeMasterNameList(GetCodeMasterNameListRequestDto dto) {
		return service.getCodeMasterNameList(dto);
	}

	@Override
	public GetCodeListResponseDto getCodeList(GetCodeListRequestDto dto) {
		return service.getCodeList(dto);
	}

	@Override
	public ApiResult<?> saveCode(SaveCodeRequestDto dto) {
		List<SaveCodeRequestSaveDto> insertRequestDtoList = dto.getInsertRequestDtoList();
		List<SaveCodeRequestSaveDto> updateRequestDtoList = dto.getUpdateRequestDtoList();
		List<SaveCodeRequestSaveDto> deleteRequestDtoList = dto.getDeleteRequestDtoList();


		//데이터 저장
		if(!insertRequestDtoList.isEmpty()){
			for(SaveCodeRequestSaveDto vo : insertRequestDtoList) {
				service.addCode(vo);
			}

		}
		//데이터 수정
		if(!updateRequestDtoList.isEmpty()){
			for(SaveCodeRequestSaveDto vo : updateRequestDtoList) {
				service.putCode(vo);
			}
		}
		//데이터 삭제
		if(!deleteRequestDtoList.isEmpty()){
			for(SaveCodeRequestSaveDto vo : deleteRequestDtoList) {
				service.delCode(vo);
			}
		}

		boolean existsList = false;
		if(insertRequestDtoList.size() > 0) existsList = true;
		if(updateRequestDtoList.size() > 0) existsList = true;
		if(deleteRequestDtoList.size() > 0) existsList = true;

		return existsList ? ApiResult.success() : ApiResult.result(BaseEnums.CommBase.VALID_ERROR);
	}

	@Override
	public GetCodeListResultVo getCode(String stComnCodeId) {
		return service.getCode(stComnCodeId);
	}

	@Override
	public List<String> decryptStr(List<String> encryptList) {
		return service.decryptStr(encryptList);
	}
}
