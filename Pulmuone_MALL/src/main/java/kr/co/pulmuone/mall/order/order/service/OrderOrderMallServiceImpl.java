package kr.co.pulmuone.mall.order.order.service;

import kr.co.pulmuone.v1.order.order.dto.PaymentInfoResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.BuyerVo;
import kr.co.pulmuone.v1.comm.enums.OrderEnums;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.order.order.dto.VirtualAccountResponseDto;
import kr.co.pulmuone.v1.order.order.service.OrderOrderBiz;
import lombok.extern.slf4j.Slf4j;

/**
 * <PRE>
 * Forbiz Korea
 * Class의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    20201224   	 천혜현            최초작성
 * =======================================================================
 * </PRE>
 */

@Slf4j
@Service
public class OrderOrderMallServiceImpl implements OrderOrderMallService {

	@Autowired
	public OrderOrderBiz orderOrderBiz;


	/**
	 * 주문 완료 가상계좌 정보 조회
	 *
	 * @param	odid
	 * @return	VirtualAccountResponseDto
	 * @throws	Exception
	 */
	@Override
	public ApiResult<?> getVirtualAccount(String odid) throws Exception{
		BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
		String urUserId = StringUtil.nvl(buyerVo.getUrUserId());
		String guestCi = StringUtil.nvl(buyerVo.getPersonalCertificationCiCd());

		//odid와 세션 정보 체크
		int orderOdidCount = orderOrderBiz.getOrderOdidCount(odid, urUserId, guestCi);
		//본인 주문 아닐 경우
		if(orderOdidCount == 0 ) {
			return ApiResult.result(OrderEnums.virtualAccount.NOT_SAME_USER);
		}

		VirtualAccountResponseDto resDto = orderOrderBiz.getVirtualAccount(odid, urUserId, guestCi);

		return ApiResult.success(resDto);
	}

	/**
	 * 주문 완료 결제 정보 조회
	 *
	 * @param	odid
	 * @return	PaymentInfoResponseDto
	 * @throws	Exception
	 */
	@Override
	public ApiResult<?> getPaymentInfo(String odid) throws Exception{
		BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
		String urUserId = StringUtil.nvl(buyerVo.getUrUserId());
		String guestCi = StringUtil.nvl(buyerVo.getPersonalCertificationCiCd());

		//odid와 세션 정보 체크
		int orderOdidCount = orderOrderBiz.getOrderOdidCount(odid, urUserId, guestCi);
		//본인 주문 아닐 경우
		if(orderOdidCount == 0 ) {
			return ApiResult.result(OrderEnums.virtualAccount.NOT_SAME_USER);
		}

		PaymentInfoResponseDto resDto = orderOrderBiz.getPaymentInfo(odid, urUserId, guestCi);

		return ApiResult.success(resDto);
	}

}
