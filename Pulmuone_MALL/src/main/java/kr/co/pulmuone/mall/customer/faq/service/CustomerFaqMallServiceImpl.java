package kr.co.pulmuone.mall.customer.faq.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.customer.faq.dto.GetFaqListByUserRequsetDto;
import kr.co.pulmuone.v1.customer.faq.service.CustomerFaqBiz;
import kr.co.pulmuone.v1.user.buyer.service.UserBuyerBiz;

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
public class CustomerFaqMallServiceImpl implements CustomerFaqMallService {

	@Autowired
	public CustomerFaqBiz customerFaqBiz;
	@Autowired
	public UserBuyerBiz userBuyerBiz;

	/**
	 * @Desc FAQ 게시판 리스트 조회
	 * @param getFaqListByUserRequsetDto
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> getFaqListByUser(GetFaqListByUserRequsetDto getFaqListByUserRequsetDto) throws Exception {

		return ApiResult.success(customerFaqBiz.getFaqListByUser(getFaqListByUserRequsetDto));

	}

	/**
	 * @Desc FAQ 게시판 정보
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> getFaqInfoByUser() throws Exception{

		return ApiResult.success(userBuyerBiz.getCommonCode("FAQ_TP"));

	}

	/**
	 * @Desc FAQ 게시글 조회수 증가
	 * @return
	 */
	@Override
	public ApiResult<?> addFaqViews(Long csFaqId) throws Exception{

		return ApiResult.success(customerFaqBiz.addFaqViews(csFaqId));
	}


}