package kr.co.pulmuone.v1.customer.notice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;

import kr.co.pulmuone.v1.comm.aop.service.UserMaskingRun;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.mapper.customer.notice.CustomerNoticeMapper;
import kr.co.pulmuone.v1.customer.notice.dto.GetNoticeByUserResponseDto;
import kr.co.pulmuone.v1.customer.notice.dto.GetNoticeListByUserRequsetDto;
import kr.co.pulmuone.v1.customer.notice.dto.NoticeBosRequestDto;
import kr.co.pulmuone.v1.customer.notice.dto.NoticeBosResponseDto;
import kr.co.pulmuone.v1.customer.notice.dto.vo.GetNoticeListByUserResultVo;
import kr.co.pulmuone.v1.customer.notice.dto.vo.GetNoticePreNextListByUserResultVo;
import kr.co.pulmuone.v1.customer.notice.dto.vo.NoticeBosDetailVo;
import kr.co.pulmuone.v1.customer.notice.dto.vo.NoticeBosListVo;

@Service
public class CustomerNoticeService {

	@Autowired
	private CustomerNoticeMapper customerNoticeMapper;

	/**
	 * @Desc 공지사항 리스트 조회
	 * @param getNoticeListByUserRequsetDto
	 * @throws Exception
	 * @return Page<GetNoticeListByUserResultVo>
	 */
	protected Page<GetNoticeListByUserResultVo> getNoticeListByUser(GetNoticeListByUserRequsetDto getNoticeListByUserRequsetDto) throws Exception{

		PageMethod.startPage(getNoticeListByUserRequsetDto.getPage(), getNoticeListByUserRequsetDto.getLimit());
		return customerNoticeMapper.getNoticeListByUser(getNoticeListByUserRequsetDto);
	}

	/**
	 * @Desc 공지사항 상세 조회
	 * @param csNoticeId
	 * @throws Exception
	 * @return GetNoticeByUserResponseDto
	 */
	protected GetNoticeByUserResponseDto getNoticeByUser(Long csNoticeId) throws Exception {
		GetNoticeByUserResponseDto getNoticeByUserResponseDto = customerNoticeMapper.getNoticeByUser(csNoticeId);

		// 공지사항 조회수 증가
		customerNoticeMapper.addNoticeViews(csNoticeId);

		// 공지사항 pk 상세 조회
		getNoticeByUserResponseDto.setCsNoticeId(getNoticeByUserResponseDto.getCsNoticeId());
		getNoticeByUserResponseDto.setNoticeTypeName(getNoticeByUserResponseDto.getNoticeTypeName());
		getNoticeByUserResponseDto.setNoticeType(getNoticeByUserResponseDto.getNoticeType());
		getNoticeByUserResponseDto.setTitle(getNoticeByUserResponseDto.getTitle());
		getNoticeByUserResponseDto.setViews(getNoticeByUserResponseDto.getViews());
		getNoticeByUserResponseDto.setContent(getNoticeByUserResponseDto.getContent());
		getNoticeByUserResponseDto.setCreateDate(getNoticeByUserResponseDto.getCreateDate());

		//이전글-다음글 조회
		List<GetNoticePreNextListByUserResultVo> preNextList = customerNoticeMapper.getNoticePreNextListByUser(csNoticeId);
		getNoticeByUserResponseDto.setPreNextList(preNextList);

		return getNoticeByUserResponseDto;
	}

    /**
     * 공지사항 관리 목록조회
     *
     * @param NoticeBosRequestDto
     * @return Page<NoticeBosListVo>
     * @throws Exception Exception
     */
 	@UserMaskingRun(system="BOS")
    protected Page<NoticeBosListVo> getNoticeList(NoticeBosRequestDto noticeBosRequestDto) throws Exception {
    	PageMethod.startPage(noticeBosRequestDto.getPage(), noticeBosRequestDto.getPageSize());
        return customerNoticeMapper.getNoticeList(noticeBosRequestDto);
    }

 	/**
 	 * 공지사항 신규 등록
 	 * @param dto
 	 * @return
 	 * @throws Exception
 	 */
 	protected ApiResult<?> addNoticeInfo(NoticeBosRequestDto noticeBosRequestDto) throws Exception {
 		NoticeBosResponseDto result = new NoticeBosResponseDto();

 		customerNoticeMapper.addNoticeInfo(noticeBosRequestDto);
 		customerNoticeMapper.addNoticeHistoryInfo(noticeBosRequestDto);

        return ApiResult.success(result);
    }


 	/**
 	 * 공지사항 수정
 	 * @param dto
 	 * @return
 	 * @throws Exception
 	 */
 	protected ApiResult<?> putNoticeInfo(NoticeBosRequestDto noticeBosRequestDto) throws Exception {
 		NoticeBosResponseDto result = new NoticeBosResponseDto();

 		customerNoticeMapper.putNoticeInfo(noticeBosRequestDto);
        customerNoticeMapper.addNoticeHistoryInfo(noticeBosRequestDto);

        return ApiResult.success(result);
    }


 	/**
 	 * 공지사항 삭제
 	 * @param dto
 	 * @return
 	 * @throws Exception
 	 */
 	protected ApiResult<?> deleteNoticeInfo(NoticeBosRequestDto noticeBosRequestDto) throws Exception {
 		NoticeBosResponseDto result = new NoticeBosResponseDto();

 		customerNoticeMapper.deleteNoticeInfo(noticeBosRequestDto);
 		customerNoticeMapper.deleteNoticeHistoryInfo(noticeBosRequestDto);

        return ApiResult.success(result);
    }



    /**
     * 공지사항 관리 상세조회
     * @param NoticeBosRequestDto
     * @return NoticeBosDetailVo
     * @throws Exception
     */
    protected NoticeBosDetailVo getDetailNotice(NoticeBosRequestDto noticeBosRequestDto)throws Exception {
    	return customerNoticeMapper.getDetailNotice(noticeBosRequestDto);
    }


    /**
     * 공지사항 관리 수정내역 목록조회
     *
     * @param NoticeBosRequestDto
     * @return Page<NoticeBosListVo>
     * @throws Exception Exception
     */
    @UserMaskingRun(system="BOS")
    protected Page<NoticeBosListVo> getNoticeHistoryList(NoticeBosRequestDto noticeBosRequestDto) throws Exception {
        PageMethod.startPage(noticeBosRequestDto.getPage(), noticeBosRequestDto.getPageSize());
        return customerNoticeMapper.getNoticeHistoryList(noticeBosRequestDto);
    }
}
