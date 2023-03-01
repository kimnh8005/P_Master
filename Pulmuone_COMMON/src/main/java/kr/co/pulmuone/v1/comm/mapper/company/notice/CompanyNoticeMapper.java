package kr.co.pulmuone.v1.comm.mapper.company.notice;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.pulmuone.v1.company.notice.dto.AddNoticeAttachParamDto;
import kr.co.pulmuone.v1.company.notice.dto.AddNoticeRequestDto;
import kr.co.pulmuone.v1.company.notice.dto.DelNoticeRequestSaveDto;
import kr.co.pulmuone.v1.company.notice.dto.GetNoticeListRequestDto;
import kr.co.pulmuone.v1.company.notice.dto.GetNoticePreNextListRequestDto;
import kr.co.pulmuone.v1.company.notice.dto.GetNoticeRequestDto;
import kr.co.pulmuone.v1.company.notice.dto.NoticeAttachParamDto;
import kr.co.pulmuone.v1.company.notice.dto.NoticeParamDto;
import kr.co.pulmuone.v1.company.notice.dto.PutNoticeRequestDto;
import kr.co.pulmuone.v1.company.notice.dto.vo.GetNoticeAttachResultVo;
import kr.co.pulmuone.v1.company.notice.dto.vo.GetNoticeListResultVo;
import kr.co.pulmuone.v1.company.notice.dto.vo.GetNoticePopupListResultVo;
import kr.co.pulmuone.v1.company.notice.dto.vo.GetNoticePopupResultVo;
import kr.co.pulmuone.v1.company.notice.dto.vo.GetNoticePreNextListResultVo;
import kr.co.pulmuone.v1.company.notice.dto.vo.GetNoticeResultVo;
import kr.co.pulmuone.v1.user.login.dto.vo.RecentlyLoginResultVo;

@Mapper
public interface CompanyNoticeMapper {

	int addNotice(AddNoticeRequestDto dto);

	int addNoticeAttach(AddNoticeAttachParamDto dto);

	int putNotice(PutNoticeRequestDto dto);

	int delNoticeAttach(NoticeAttachParamDto dto);

	int addNoticeViewCount(GetNoticeRequestDto dto);

	GetNoticeResultVo getNotice(GetNoticeRequestDto dto);

	GetNoticeAttachResultVo getNoticeAttach(GetNoticeRequestDto dto);

	int getNoticeListCount(GetNoticeListRequestDto dto);

	List<GetNoticeListResultVo> getNoticeList(GetNoticeListRequestDto dto);

	List<GetNoticeListResultVo> getDashboardNoticeList(GetNoticeListRequestDto dto);

	int delNotice(DelNoticeRequestSaveDto dto);

	int putNoticeSet(NoticeParamDto noticeParamDto);

	List<GetNoticePreNextListResultVo> getNoticePreNextList(GetNoticePreNextListRequestDto dto);

	RecentlyLoginResultVo getNoticeLoginData(String userId);

	List<GetNoticePopupListResultVo> getNoticePopupList(RecentlyLoginResultVo recentlyLoginResultVo);

	GetNoticePopupResultVo getNoticePopup(int csCompanyBbsId);
}
