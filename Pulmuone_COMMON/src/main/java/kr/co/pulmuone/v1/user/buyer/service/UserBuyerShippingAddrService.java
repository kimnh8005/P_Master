package kr.co.pulmuone.v1.user.buyer.service;

import kr.co.pulmuone.v1.comm.mapper.user.buyer.UserBuyerShippingAddrMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
 * 버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 * 1.0    20210216    	 천혜현           최초작성
 * =======================================================================
 * </PRE>
 */
@Service
public class UserBuyerShippingAddrService {

    @Autowired
    private UserBuyerShippingAddrMapper	userBuyerShippingAddrMapper;


	/**
	 * @Desc 주문시 최근배송지 존재 유무
	 * @param urUserId
	 * @param odOrderid
	 * @return long
	 */
	protected Long getOrderLatelyShippingAddressCount(long urUserId, long odOrderId) throws Exception{
		return userBuyerShippingAddrMapper.getOrderLatelyShippingAddressCount(urUserId, odOrderId);
	}

	/**
	 * @Desc 배송지 추가
	 * @param urUserId
	 * @param odOrderid
	 * @return int
	 */
	protected int addLatelyShippingAddress(long urUserId, long odOrderId) throws Exception{
		return userBuyerShippingAddrMapper.addLatelyShippingAddress(urUserId, odOrderId);
	}

	/**
	 * @Desc 배송지 날짜 업데이트
	 * @param urLatelyShippingAddrId
	 * @return int
	 */
	protected int putLatelyShippingAddress(long urLatelyShippingAddrId) throws Exception{
		return userBuyerShippingAddrMapper.putLatelyShippingAddress(urLatelyShippingAddrId);
	}

}