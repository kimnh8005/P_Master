package kr.co.pulmuone.v1.customer.notice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.CustomerEnums;
import kr.co.pulmuone.v1.comm.util.DeviceUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.customer.notice.dto.GetNoticeByUserResponseDto;
import kr.co.pulmuone.v1.customer.notice.dto.GetNoticeListByUserRequsetDto;
import kr.co.pulmuone.v1.customer.notice.dto.GetNoticeListByUserResponseDto;
import kr.co.pulmuone.v1.customer.notice.dto.NoticeBosDetailResponseDto;
import kr.co.pulmuone.v1.customer.notice.dto.NoticeBosRequestDto;
import kr.co.pulmuone.v1.customer.notice.dto.NoticeBosResponseDto;
import kr.co.pulmuone.v1.customer.notice.dto.vo.GetNoticeListByUserResultVo;
import kr.co.pulmuone.v1.customer.notice.dto.vo.NoticeBosDetailVo;
import kr.co.pulmuone.v1.customer.notice.dto.vo.NoticeBosListVo;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomerNoticeBizImpl implements CustomerNoticeBiz {

	@Autowired
	private CustomerNoticeService customerNoticeService;

	/**
	 * @Desc 공지사항 리스트 조회
	 * @param getNoticeListByUserRequsetDto
	 * @throws Exception
	 */
	@Override
	public GetNoticeListByUserResponseDto getNoticeListByUser(GetNoticeListByUserRequsetDto getNoticeListByUserRequsetDto) throws Exception{

		//디바이스 PC-MOBILE 구분
		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
	    HttpServletRequest req = servletRequestAttributes.getRequest();

	    getNoticeListByUserRequsetDto.setDeviceType(DeviceUtil.getDeviceInfo( req.getHeader("User-Agent") ));

		GetNoticeListByUserResponseDto getNoticeListByUserResponseDto = new GetNoticeListByUserResponseDto();
		Page<GetNoticeListByUserResultVo> notice = customerNoticeService.getNoticeListByUser(getNoticeListByUserRequsetDto);
		getNoticeListByUserResponseDto.setNotice(notice.getResult());
		getNoticeListByUserResponseDto.setTotal(notice.getTotal());

		return getNoticeListByUserResponseDto;

	}

	/**
	 * @Desc 공지사항 상세 조회
	 * @param getNoticeByUserRequsetDto
	 * @throws Exception
	 */
	@Override
	public GetNoticeByUserResponseDto getNoticeByUser(Long csNoticeId) throws Exception {

		return customerNoticeService.getNoticeByUser(csNoticeId);

	}


	 /**
     * 공지사항 관리 목록조회
     *
     * @throws Exception
     */
    @Override
    public  ApiResult<?>  getNoticeList(NoticeBosRequestDto noticeBosRequestDto) throws Exception {

    	NoticeBosResponseDto result = new NoticeBosResponseDto();

    	if (!noticeBosRequestDto.getFindKeyword().isEmpty()) {
			ArrayList<String> array = new ArrayList<>();
			StringTokenizer st = new StringTokenizer(noticeBosRequestDto.getFindKeyword(), "\n|,");
			while (st.hasMoreElements()) {
				String object = (String) st.nextElement();
				array.add(object);
			}
			noticeBosRequestDto.setFindKeywordArray(array);
		}


        Page<NoticeBosListVo> voList = customerNoticeService.getNoticeList(noticeBosRequestDto);

        result.setTotal(voList.getTotal());
        result.setRows(voList.getResult());

        return ApiResult.success(result);
    }

    /**
     * 공지사항 신규 등록
     */
    @Override
    public ApiResult<?> addNoticeInfo(NoticeBosRequestDto noticeBosRequestDto) throws Exception {

    	List<String> channelList = StringUtil.getArrayList(noticeBosRequestDto.getChannelYn());

    	for(int i=0; i < channelList.size() ;i++) {

    		String channenYn = channelList.get(i);

    		if(channenYn.equals(CustomerEnums.NoticeChannelType.PC.getCode())) {
    			noticeBosRequestDto.setChannelPcYn(channenYn);
    		}else if(channenYn.equals(CustomerEnums.NoticeChannelType.MOBILE.getCode())) {
    			noticeBosRequestDto.setChannelMobileYn(channenYn);
    		}

    	}

        return customerNoticeService.addNoticeInfo(noticeBosRequestDto);
    }

    /**
     * 공지사항 수정
     */
    @Override
    public ApiResult<?> putNoticeInfo(NoticeBosRequestDto noticeBosRequestDto) throws Exception {

    	List<String> channelList = StringUtil.getArrayList(noticeBosRequestDto.getChannelYn());

    	for(int i=0; i < channelList.size() ;i++) {

    		String channenYn = channelList.get(i);

    		if(channenYn.equals(CustomerEnums.NoticeChannelType.PC.getCode())) {
    			noticeBosRequestDto.setChannelPcYn(channenYn);
    		}else if(channenYn.equals(CustomerEnums.NoticeChannelType.MOBILE.getCode())) {
    			noticeBosRequestDto.setChannelMobileYn(channenYn);
    		}

    	}

        return customerNoticeService.putNoticeInfo(noticeBosRequestDto);
    }

    /**
     * 공지사항 삭제
     */
    @Override
    public ApiResult<?> deleteNoticeInfo(NoticeBosRequestDto noticeBosRequestDto) throws Exception {
        return customerNoticeService.deleteNoticeInfo(noticeBosRequestDto);
    }

    /**
     * 공지사항 관리 상세조회
     *
     * @throws Exception
     */
	@Override
    public ApiResult<?> getDetailNotice(NoticeBosRequestDto noticeBosRequestDto) throws Exception{
		NoticeBosDetailResponseDto result = new NoticeBosDetailResponseDto();
		NoticeBosDetailVo vo = new NoticeBosDetailVo();
		vo = customerNoticeService.getDetailNotice(noticeBosRequestDto);

		result.setRow(vo);

    	return ApiResult.success(result);
    }

    /**
     * 공지사항 수정내역 목록 조회
     * @param noticeBosRequestDto
     * @return
     * @throws Exception
     */
    @Override
    public ApiResult<?> getNoticeHistoryList(NoticeBosRequestDto noticeBosRequestDto) throws Exception {
        NoticeBosResponseDto result = new NoticeBosResponseDto();

        if(noticeBosRequestDto.getNoticeId().isEmpty()) {
            return null;
        }
        Page<NoticeBosListVo> voList = customerNoticeService.getNoticeHistoryList(noticeBosRequestDto);

        result.setTotal(voList.getTotal());
        result.setRows(voList.getResult());

        return ApiResult.success(result);
    }
}
