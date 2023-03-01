package kr.co.pulmuone.v1.company.notice.service;

import java.util.List;

import kr.co.pulmuone.v1.user.noti.dto.UserNotiRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.UserEnums.UserNotiType;
import kr.co.pulmuone.v1.company.notice.dto.AddNoticeRequestDto;
import kr.co.pulmuone.v1.company.notice.dto.DelNoticeRequestDto;
import kr.co.pulmuone.v1.company.notice.dto.GetNoticeListRequestDto;
import kr.co.pulmuone.v1.company.notice.dto.GetNoticeListResponseDto;
import kr.co.pulmuone.v1.company.notice.dto.GetNoticePreNextListRequestDto;
import kr.co.pulmuone.v1.company.notice.dto.GetNoticePreNextListResponseDto;
import kr.co.pulmuone.v1.company.notice.dto.GetNoticeRequestDto;
import kr.co.pulmuone.v1.company.notice.dto.GetNoticeResponseDto;
import kr.co.pulmuone.v1.company.notice.dto.NoticeAttachParamDto;
import kr.co.pulmuone.v1.company.notice.dto.PutNoticeRequestDto;
import kr.co.pulmuone.v1.company.notice.dto.PutNoticeSetRequestDto;
import kr.co.pulmuone.v1.user.noti.service.UserNotiBiz;

@Service
public class CompanyNoticeBizImpl implements CompanyNoticeBiz {

	@Autowired
    private UserNotiBiz userNotiBiz;

    @Autowired
    private CompanyNoticeService companyNoticeService;

    @Override
    public ApiResult<?> addNotice(AddNoticeRequestDto dto) throws Exception {
		ApiResult result = companyNoticeService.addNotice(dto);
		if (result.getCode().equals(BaseEnums.Default.SUCCESS.getCode())) {
			// 공지사항 알림 등록
			List<Long> urUserIds = userNotiBiz.getUserListByBBS(dto.getCompanyBbsType());
			if(urUserIds != null && !urUserIds.isEmpty()) {
				UserNotiType userNotiType = UserNotiType.BOS_NOTI;
				userNotiBiz.addNoti(UserNotiRequestDto.builder()
                        .urUserIdList(urUserIds)
                        .userNotiType(userNotiType.getCode())
                        .notiMessage(userNotiType.getMsg())
                        .build());
			}
		}
		return result;
    }

    @Override
    public ApiResult<?> putNotice(PutNoticeRequestDto dto) throws Exception {
        return companyNoticeService.putNotice(dto);
    }

    @Override
    public GetNoticeResponseDto getNotice(GetNoticeRequestDto dto) throws Exception {
        return companyNoticeService.getNotice(dto);
    }

    @Override
    public GetNoticeListResponseDto getNoticeList(GetNoticeListRequestDto convertRequestToObject) throws Exception {
        return companyNoticeService.getNoticeList(convertRequestToObject);
    }

    @Override
    public ApiResult<?> delNotice(DelNoticeRequestDto dto) throws Exception {

        int cnt = companyNoticeService.delNotice(dto);
        if(cnt > 0) {
            return ApiResult.success();
        } else {
            return ApiResult.fail();
        }
    }

    @Override
    public ApiResult<?> putNoticeSet(PutNoticeSetRequestDto dto) throws Exception {

        int cnt = companyNoticeService.putNoticeSet(dto);
        if(cnt > 0) {
            return ApiResult.success();
        } else {
            return ApiResult.fail();
        }
    }

    @Override
    public GetNoticePreNextListResponseDto getNoticePreNextList(GetNoticePreNextListRequestDto dto) throws Exception {
        return companyNoticeService.getNoticePreNextList(dto);
    }

    @Override
    public ApiResult<?> delAttc(NoticeAttachParamDto noticeAttachParamDto) throws Exception {

        int cnt = companyNoticeService.delAttc(noticeAttachParamDto);
        if(cnt > 0) {
            return ApiResult.success();
        } else {
            return ApiResult.fail();
        }
    }

    @Override
    public ApiResult<?> getNoticePopupList() throws Exception{
        return companyNoticeService.getNoticePopupList();
    }

    @Override
    public ApiResult<?> getNoticePopup(int csCompanyBbsId){
        return companyNoticeService.getNoticePopup(csCompanyBbsId);
    }
}
