package kr.co.pulmuone.v1.company.notice.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.company.notice.dto.*;

public interface CompanyNoticeBiz {

    ApiResult<?> addNotice(AddNoticeRequestDto dto) throws Exception;

    ApiResult<?> putNotice(PutNoticeRequestDto dto) throws Exception;

    GetNoticeResponseDto getNotice(GetNoticeRequestDto dto) throws Exception;

    GetNoticeListResponseDto getNoticeList(GetNoticeListRequestDto convertRequestToObject) throws Exception;

    ApiResult<?> delNotice(DelNoticeRequestDto dto) throws Exception;

    ApiResult<?> putNoticeSet(PutNoticeSetRequestDto dto) throws Exception;

    GetNoticePreNextListResponseDto getNoticePreNextList(GetNoticePreNextListRequestDto dto) throws Exception;

    ApiResult<?> delAttc(NoticeAttachParamDto noticeAttachParamDto) throws Exception;

    ApiResult<?> getNoticePopupList() throws Exception;

    ApiResult<?> getNoticePopup(int csCompanyBbsId);
}