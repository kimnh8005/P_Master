package kr.co.pulmuone.v1.customer.feedback.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.customer.feedback.dto.FeedbackBosRequestDto;

public interface FeedbackBiz {

	// 후기관리 목록조회
	ApiResult<?> getFeedbackList(FeedbackBosRequestDto feedbackBosRequestDto) throws Exception;

	//후기관리 목록조회 엑셀 다운로드
	ExcelDownloadDto feedbackExportExcel(FeedbackBosRequestDto feedbackBosRequestDto);

	//후기관리 상세조회
	ApiResult<?> getDetailFeedback(FeedbackBosRequestDto feedbackBosRequestDto) throws Exception;

	//후기관리 상세 첨부파일 이미지
	ApiResult<?> getImageList(String feedbackId) throws Exception;

	//후기관리정보 수정
	ApiResult<?> putFeedbackInfo(FeedbackBosRequestDto feedbackBosRequestDto) throws Exception;

}
