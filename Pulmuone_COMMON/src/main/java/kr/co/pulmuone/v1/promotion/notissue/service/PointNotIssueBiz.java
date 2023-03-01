package kr.co.pulmuone.v1.promotion.notissue.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.promotion.notissue.dto.PointNotIssueListRequestDto;

public interface PointNotIssueBiz {

	/**
	 * 적립금 미지급 내역 리스트 조회
	 * @param pointHistoryListRequestDto
	 * @return
	 * @throws Exception
	 */
	ApiResult<?> getPointNotIssueList(PointNotIssueListRequestDto pointNotIssueListRequestDto) throws Exception;

	ExcelDownloadDto getPointNotIssueListExportExcel(PointNotIssueListRequestDto pointNotIssueListRequestDto) throws Exception;
}
