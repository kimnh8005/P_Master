package kr.co.pulmuone.v1.user.buyer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserBuyerShippingAddrBizImpl implements UserBuyerShippingAddrBiz {

    @Autowired
    private UserBuyerShippingAddrService userBuyerShippingAddrService;


	/**
	 * @Desc 주문시 최근배송지 존재 유무
	 * @param urUserId
	 * @param odOrderid
	 * @return long
	 */
    @Override
	public Long getOrderLatelyShippingAddressCount(long urUserId, long odOrderId) throws Exception{
		return userBuyerShippingAddrService.getOrderLatelyShippingAddressCount(urUserId, odOrderId);
	}

	/**
	 * @Desc 배송지 추가
	 * @param urUserId
	 * @param odOrderid
	 * @return int
	 */
    @Override
	public int addLatelyShippingAddress(long urUserId, long odOrderId) throws Exception{
		return userBuyerShippingAddrService.addLatelyShippingAddress(urUserId, odOrderId);
	}

	/**
	 * @Desc 배송지 날짜 업데이트
	 * @param urLatelyShippingAddrId
	 * @return int
	 */
    @Override
	public int putLatelyShippingAddress(long urLatelyShippingAddrId) throws Exception{
		return userBuyerShippingAddrService.putLatelyShippingAddress(urLatelyShippingAddrId);
	}


}
