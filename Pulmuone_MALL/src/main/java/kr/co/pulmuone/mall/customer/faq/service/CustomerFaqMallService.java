package kr.co.pulmuone.mall.customer.faq.service;

import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.customer.faq.dto.GetFaqListByUserRequsetDto;

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
*  1.0    2020. 11. 25.     최윤지          최초작성
* =======================================================================
* </PRE>
*/
@Service
public interface CustomerFaqMallService {

	/**
	 * @Desc FAQ 게시판 리스트 조회
	 * @param getFaqListByUserRequsetDto
	 * @return ApiResult<?>
	 */
	ApiResult<?> getFaqListByUser(GetFaqListByUserRequsetDto getFaqListByUserRequsetDto) throws Exception;

	/**
	 * @Desc FAQ 게시판 정보
	 * @throws Exception
	 */
	ApiResult<?> getFaqInfoByUser() throws Exception;

	/**
	 * @param csFaqId
	 * @Desc FAQ 게시글 조회수 증가
	 * @return ApiResult<?>
	 */
	ApiResult<?> addFaqViews(Long csFaqId) throws Exception;

}
