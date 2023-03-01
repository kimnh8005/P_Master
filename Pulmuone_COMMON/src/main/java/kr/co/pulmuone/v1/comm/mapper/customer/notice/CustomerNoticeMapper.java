package kr.co.pulmuone.v1.comm.mapper.customer.notice;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.customer.notice.dto.GetNoticeByUserResponseDto;
import kr.co.pulmuone.v1.customer.notice.dto.GetNoticeListByUserRequsetDto;
import kr.co.pulmuone.v1.customer.notice.dto.NoticeBosRequestDto;
import kr.co.pulmuone.v1.customer.notice.dto.vo.GetNoticeListByUserResultVo;
import kr.co.pulmuone.v1.customer.notice.dto.vo.GetNoticePreNextListByUserResultVo;
import kr.co.pulmuone.v1.customer.notice.dto.vo.NoticeBosDetailVo;
import kr.co.pulmuone.v1.customer.notice.dto.vo.NoticeBosListVo;

@Mapper
public interface CustomerNoticeMapper {
	Page<GetNoticeListByUserResultVo> getNoticeListByUser(GetNoticeListByUserRequsetDto getNoticeListByUserRequsetDto);

	GetNoticeByUserResponseDto getNoticeByUser(Long csNoticeId);

	List<GetNoticePreNextListByUserResultVo> getNoticePreNextListByUser(Long csNoticeId);

	Page<NoticeBosListVo> getNoticeList(NoticeBosRequestDto noticeBosRequestDto) throws Exception;

	int addNoticeInfo(NoticeBosRequestDto noticeBosRequestDto);

	int putNoticeInfo(NoticeBosRequestDto noticeBosRequestDto);

	int deleteNoticeInfo(NoticeBosRequestDto noticeBosRequestDto);

	NoticeBosDetailVo getDetailNotice (NoticeBosRequestDto noticeBosRequestDto) throws Exception;

	int addNoticeViews(Long csNoticeId) throws Exception;

	Page<NoticeBosListVo> getNoticeHistoryList(NoticeBosRequestDto noticeBosRequestDto) throws Exception;

	int addNoticeHistoryInfo(NoticeBosRequestDto noticeBosRequestDto);

	int deleteNoticeHistoryInfo(NoticeBosRequestDto noticeBosRequestDto);
}


