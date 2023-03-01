package kr.co.pulmuone.mall.customer.notice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.customer.notice.dto.GetNoticeListByUserRequsetDto;
import kr.co.pulmuone.v1.customer.notice.service.CustomerNoticeBiz;

/**
* <PRE>
* Forbiz Korea
* Class의 기능과 역할을 상세히 기술한다.
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2020. 11. 27.     최윤지         최초작성
* =======================================================================
* </PRE>
*/

@Service
public class CustomerNoticeMallServiceImpl implements CustomerNoticeMallService{

	@Autowired
	public CustomerNoticeBiz customerNoticeBiz;

	/**
	 * @Desc 공지사항 리스트 조회
	 * @param getNoticeListByUserRequsetDto
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> getNoticeListByUser(GetNoticeListByUserRequsetDto getNoticeListByUserRequsetDto) throws Exception {
		return ApiResult.success(customerNoticeBiz.getNoticeListByUser(getNoticeListByUserRequsetDto));
	}

	/**
	 * @Desc 공지사항 상세조회
	 * @param getNoticeByUserRequsetDto
	 * @throws Exception
	 * @return ApiResult<?>
	 */
	@Override
	public ApiResult<?> getNoticeByUser(Long csNoticeId) throws Exception {
		return ApiResult.success(customerNoticeBiz.getNoticeByUser(csNoticeId));
	}



}
