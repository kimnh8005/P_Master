package kr.co.pulmuone.v1.system.help.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.mapper.system.help.SystemHelpMapper;
import kr.co.pulmuone.v1.system.help.dto.GetHelpListRequestDto;
import kr.co.pulmuone.v1.system.help.dto.GetHelpResponseDto;
import kr.co.pulmuone.v1.system.help.dto.HelpRequestDto;
import kr.co.pulmuone.v1.system.help.dto.vo.GetHelpListResultVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;

import java.util.List;

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
 *  	   20201106    	  강윤경           현행화
 * =======================================================================
 * </PRE>
 */

@RequiredArgsConstructor
@Service
public class SystemHelpService {

	private final SystemHelpMapper mapper;

	/**
	 * 도움말 리스트조회

	protected GetHelpListResponseDto getHelpList(GetHelpListRequestDto dto) {
		GetHelpListResponseDto result = new GetHelpListResponseDto();

		int total = mapper.getHelpListCount(dto);	// total
		List<GetHelpListResultVo> rows = mapper.getHelpList(dto);	// rows

		if(rows != null) {
			for (int i = 0; i < rows.size(); i++) {
				String tmpContent = rows.get(i).getContent();
				tmpContent = tmpContent.replaceAll("&lt;br /&gt;", "\n");
				tmpContent = tmpContent.replaceAll("&lt(;)(.*?)&gt(;)", "");
				tmpContent = tmpContent.replaceAll("nbsp;", "");
				tmpContent = tmpContent.replaceAll("amp;", "");
				tmpContent = tmpContent.replaceAll("&", "");
				rows.get(i).setContent(tmpContent);
			}
		}

		result.setTotal(total);
		result.setRows(rows);

		return result;
	}
	 */

	/**
	 * @Desc 도움말 리스트조회
	 * @param dto
	 * @return
	 * @return Page<GetHelpListResultVo>
	 */
	protected Page<GetHelpListResultVo> getHelpList(GetHelpListRequestDto dto) {
		PageMethod.startPage(dto.getPage(), dto.getPageSize());
	    return mapper.getHelpList(dto);
	}


	/**
	 * 도움말 상세조회
	 */
	protected GetHelpResponseDto getHelp(Long id) {
		GetHelpResponseDto result = new GetHelpResponseDto();

		result.setRows(mapper.getHelp(id));

		return result;
	}


	/**
	 * @Desc 도움말 중복체크
	 * @param dto
	 * @return
	 * @return int
	 */
	protected int duplicateHelpCount(HelpRequestDto dto) {
		return mapper.duplicateHelpCount(dto);
	}



	/**
	 * 도움말 추가
	 */
	protected int addHelp(HelpRequestDto dto) {

		String contentPlain = dto.getContent();
		contentPlain = contentPlain.replaceAll("&lt;br /&gt;", "\n");
		contentPlain = contentPlain.replaceAll("&lt(;)(.*?)&gt(;)", "");
		contentPlain = contentPlain.replaceAll("nbsp;", "");
		contentPlain = contentPlain.replaceAll("amp;", "");
		contentPlain = contentPlain.replaceAll("&", "");
		dto.setContentPlain(contentPlain);

		return mapper.addHelp(dto);
	}



	/**
	 * 도움말 수정
	 */
	protected int putHelp(HelpRequestDto dto) {
		String contentPlain = dto.getContent();
		contentPlain = contentPlain.replaceAll("&lt;br /&gt;", "\n");
		contentPlain = contentPlain.replaceAll("&lt(;)(.*?)&gt(;)", "");
		contentPlain = contentPlain.replaceAll("nbsp;", "");
		contentPlain = contentPlain.replaceAll("amp;", "");
		contentPlain = contentPlain.replaceAll("&", "");
		dto.setContentPlain(contentPlain);

		return mapper.putHelp(dto);
	}


	/**
	 * 도움말 삭제
	 */
	protected ApiResult<?> delHelp(String id) {
		if(mapper.delHelp(id) > 0) {
			return ApiResult.success();
		} else {
			return ApiResult.result(BaseEnums.CommBase.VALID_ERROR);
		}
	}


	/**
	 * @Desc  해당 도움말 리스트조회
	 * @param dto
	 * @return
	 * @return List<GetHelpListResultVo>
	 */
	protected List<GetHelpListResultVo> getHelpListByArray(GetHelpListRequestDto dto) {
	    return mapper.getHelpListByArray(dto);
	}

}
