package kr.co.pulmuone.v1.customer.notice.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.customer.notice.dto.GetNoticeByUserResponseDto;
import kr.co.pulmuone.v1.customer.notice.dto.GetNoticeListByUserRequsetDto;
import kr.co.pulmuone.v1.customer.notice.dto.GetNoticeListByUserResponseDto;
import kr.co.pulmuone.v1.customer.notice.dto.NoticeBosRequestDto;

public interface CustomerNoticeBiz {

	GetNoticeListByUserResponseDto getNoticeListByUser(GetNoticeListByUserRequsetDto getNoticeListByUserRequsetDto) throws Exception;

	GetNoticeByUserResponseDto getNoticeByUser(Long csNoticeId) throws Exception;

	// 공지사항 목록조회
	ApiResult<?> getNoticeList(NoticeBosRequestDto noticeBosRequestDto) throws Exception;

	// 공지사항 신규 등록
    ApiResult<?> addNoticeInfo(NoticeBosRequestDto noticeBosRequestDto) throws Exception;

	// 공지사항 수정
    ApiResult<?> putNoticeInfo(NoticeBosRequestDto noticeBosRequestDto) throws Exception;

	// 공지사항 삭제
    ApiResult<?> deleteNoticeInfo(NoticeBosRequestDto noticeBosRequestDto) throws Exception;

	// 공지사항 관리 상세조회
	ApiResult<?> getDetailNotice(NoticeBosRequestDto noticeBosRequestDto) throws Exception;

	// 공지사항 수정내역 조회
	ApiResult<?> getNoticeHistoryList(NoticeBosRequestDto noticeBosRequestDto) throws Exception;
}
