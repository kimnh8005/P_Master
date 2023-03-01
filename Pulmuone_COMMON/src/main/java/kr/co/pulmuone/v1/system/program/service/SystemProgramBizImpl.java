package kr.co.pulmuone.v1.system.program.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.system.program.dto.GetProgramAuthListResponseDto;
import kr.co.pulmuone.v1.system.program.dto.GetProgramListRequestDto;
import kr.co.pulmuone.v1.system.program.dto.GetProgramListResponseDto;
import kr.co.pulmuone.v1.system.program.dto.GetProgramNameListRequestDto;
import kr.co.pulmuone.v1.system.program.dto.GetProgramNameListResponseDto;
import kr.co.pulmuone.v1.system.program.dto.GetProgramResponseDto;
import kr.co.pulmuone.v1.system.program.dto.ProgramRequestDto;
import kr.co.pulmuone.v1.system.program.dto.vo.ProgramAuthVo;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SystemProgramBizImpl implements SystemProgramBiz {

	private final SystemProgramService service;

	/**
	 * 프로그램관리 리스트
	 * @param dto
	 * @return
	 * @
	 */
	@Override
	public GetProgramListResponseDto getProgramList(GetProgramListRequestDto dto) {
		return service.findAllProgramList(dto);
	}

	@Override
	public GetProgramNameListResponseDto getProgramNameList(GetProgramNameListRequestDto dto)  {
		return service.findAllProgramNameList(dto);
	}

	/**
	 * 프로그램 관리 상세 조회
	 * @param dto
	 * @return
	 * @
	 */
	@Override
	public GetProgramResponseDto getProgram(ProgramRequestDto dto)  {
		return service.findProgram(dto);
	}

	/**
	 * 프로그램관리 신규저장
	 * @param dto
	 * @return
	 * @
	 */
	@Override
	public ApiResult<?> addProgram(ProgramRequestDto dto) {

		dto.setHtmlPath(DomainIni(dto.getHtmlPath()));
		dto.setUrl(DomainIni(dto.getUrl()));
		 if(service.existsProgram(dto) > 0) {
			return ApiResult.result(BaseEnums.CommBase.DUPLICATE_DATA);
		} else if(dto.getProgramId().replaceAll("/", "").length() < 1 || dto.getHtmlPath().replaceAll("/", "").length() < 1){
			return ApiResult.result(BaseEnums.CommBase.VALID_ERROR);
		} else {
			try {
				dto.setAuthInsertVoList((List<ProgramAuthVo>) BindUtil.convertJsonArrayToDtoList(dto.getAuthInsertData(), ProgramAuthVo.class));
			} catch (Exception e) {
				e.printStackTrace();
				return ApiResult.result(BaseEnums.CommBase.CONVERT_JSON_ERROR);
			}
			service.addProgram(dto);
			service.addProgramAuth(dto);
		}

		return ApiResult.success();
	}


	/**
	 * 프로그램관리 수정
	 * @param dto
	 * @return
	 * @
	 */
	@Override
	public ApiResult<?> putProgram(ProgramRequestDto dto) {

			dto.setHtmlPath(DomainIni(dto.getHtmlPath()));
			dto.setUrl(DomainIni(dto.getUrl()));

		if(service.existsProgram(dto)  > 0) {
			return ApiResult.result(BaseEnums.CommBase.DUPLICATE_DATA);
		} else if(dto.getProgramId().replaceAll("/", "").length() < 1 || dto.getHtmlPath().replaceAll("/", "").length() < 1){
			return ApiResult.result(BaseEnums.CommBase.VALID_ERROR);
		} else {
			try {
				dto.setAuthInsertVoList((List<ProgramAuthVo>) BindUtil.convertJsonArrayToDtoList(dto.getAuthInsertData(), ProgramAuthVo.class));
				dto.setAuthUpdateVoList((List<ProgramAuthVo>) BindUtil.convertJsonArrayToDtoList(dto.getAuthUpdateData(), ProgramAuthVo.class));
			} catch (Exception e) {
				e.printStackTrace();
				return ApiResult.result(BaseEnums.CommBase.CONVERT_JSON_ERROR);
			}
			service.putProgram(dto);
			service.addProgramAuth(dto);
			service.putProgramAuth(dto);
		}

		return ApiResult.success();
	}


	/**
	 * 프로그램관리 삭제
	 * @return
	 * @
	 */
	@Override
	public ApiResult<?> delProgram(Long id)  {
		if(service.delProgram(id) > 0) {
			service.delProgramAuthByStProgramId(id);
			return ApiResult.success();
		} else {
			return ApiResult.result(BaseEnums.CommBase.VALID_ERROR);
		}
	}

	@Override
	public GetProgramAuthListResponseDto getProgramAuthUseList(Long stProgramId) throws Exception {
		return service.getProgramAuthUseList(stProgramId);
	}

	public String DomainIni(String result) {
			result = "/" + StringUtils.stripStart(result, "/");
		return result;
	}
}
