package kr.co.pulmuone.v1.system.help.service;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.SystemEnums;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.system.help.dto.HelpRequestDto;
import kr.co.pulmuone.v1.system.help.dto.vo.GetHelpListResultVo;
import kr.co.pulmuone.v1.system.help.dto.GetHelpListRequestDto;
import kr.co.pulmuone.v1.system.help.dto.GetHelpListResponseDto;
import kr.co.pulmuone.v1.system.help.dto.GetHelpResponseDto;

/**
 * <PRE>
 * Forbiz Korea
 * Class의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    20200602    	  천혜현           최초작성
 * =======================================================================
 * </PRE>
 */

@RequiredArgsConstructor
@Service
public class SystemHelpBizImpl implements SystemHelpBiz {

	private final SystemHelpService service;

	/**
	 * 도움말 리스트조회
	 */
	@Override
	public ApiResult<?> getHelpList(GetHelpListRequestDto dto) {
		GetHelpListResponseDto getHelpListResponseDto = new GetHelpListResponseDto();
		Page<GetHelpListResultVo> helpListResult = service.getHelpList(dto);

		getHelpListResponseDto.setTotal(helpListResult.getTotal());
		getHelpListResponseDto.setRows(helpListResult.getResult());

	    return ApiResult.success(getHelpListResponseDto);
	}


	/**
	 * 도움말 상세조회
	 */
	@Override
	public GetHelpResponseDto getHelp(Long id) {
		return service.getHelp(id);
	}


	/**
	 * 도움말 추가
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ApiResult<?> addHelp(HelpRequestDto dto) {
		if(service.duplicateHelpCount(dto) > 0){
			return ApiResult.result(SystemEnums.HelpData.HELP_DUPLICATE_DATA);
		}
		service.addHelp(dto);
		return ApiResult.success();
	}



	/**
	 * 도움말 수정
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ApiResult<?> putHelp(HelpRequestDto dto) {
		if(service.duplicateHelpCount(dto) > 0){
			return ApiResult.result(SystemEnums.HelpData.HELP_DUPLICATE_DATA);
		}
		service.putHelp(dto);
		return ApiResult.success();
	}


	/**
	 * 도움말 삭제
	 */
	@Override
	public ApiResult<?> delHelp(String id) {
		return service.delHelp(id);
	}



	/**
	 * 해당 도움말 리스트조회
	 */
	@Override
	public ApiResult<?> getHelpListByArray(String systemHelpId) {
		GetHelpListRequestDto getHelpListRequestDto = new GetHelpListRequestDto();

		if( StringUtils.isNotEmpty(systemHelpId)) {
			getHelpListRequestDto.setSystemHelpIdList(Stream.of(systemHelpId.split(","))
				.map(String::trim)
				.filter(StringUtils::isNotEmpty)
				.collect(Collectors.toList()));
		}

	    GetHelpListResponseDto getHelpListResponseDto = new GetHelpListResponseDto();
	    List<GetHelpListResultVo> helpListResult = service.getHelpListByArray(getHelpListRequestDto);

		getHelpListResponseDto.setRows(helpListResult);

	    return ApiResult.success(getHelpListResponseDto);

	}


}
