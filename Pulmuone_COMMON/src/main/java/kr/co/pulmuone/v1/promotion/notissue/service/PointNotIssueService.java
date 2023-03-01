package kr.co.pulmuone.v1.promotion.notissue.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;

import kr.co.pulmuone.v1.comm.aop.service.UserMaskingRun;
import kr.co.pulmuone.v1.comm.mapper.promotion.notissue.PointNotIssueMapper;
import kr.co.pulmuone.v1.promotion.notissue.dto.PointNotIssueListRequestDto;
import kr.co.pulmuone.v1.promotion.notissue.dto.vo.PointNotIssueListVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class PointNotIssueService {

	private final PointNotIssueMapper pointNotIssueMapper;

	 /**
     * 적립금 미지급내역 조회
     *
     * @param pointNotIssueListRequestDtov
     * @return pointNotIssueListResponseDto
     * @throws Exception
     */
	@UserMaskingRun(system = "MUST_MASKING")
    protected Page<PointNotIssueListVo> getPointNotIssueList(PointNotIssueListRequestDto pointNotIssueListRequestDto) throws Exception {
    	PageMethod.startPage(pointNotIssueListRequestDto.getPage(), pointNotIssueListRequestDto.getPageSize());
        return pointNotIssueMapper.getPointNotIssueList(pointNotIssueListRequestDto);
    }

	/**
	 * 적립금 미지급내역 엑셀 다운로드
	 *
	 * @param PointNotIssueListRequestDto
	 * @return List<PointNotIssueListVo>
	 * @throws Exception
	 */
	@UserMaskingRun(system = "MUST_MASKING")
	protected List<PointNotIssueListVo> getPointNotIssueListExportExcel(PointNotIssueListRequestDto pointNotIssueListRequestDto) throws Exception
	{

		List<PointNotIssueListVo> result = pointNotIssueMapper.getPointNotIssueListExportExcel(pointNotIssueListRequestDto);

		return result;
	}

}
