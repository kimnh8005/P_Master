package kr.co.pulmuone.v1.user.buyer.service;


public interface UserBuyerShippingAddrBiz {

	Long getOrderLatelyShippingAddressCount(long urUserId, long odOrderId) throws Exception;

	int addLatelyShippingAddress(long urUserId, long odOrderId) throws Exception;

	int putLatelyShippingAddress(long urLatelyShippingAddrId) throws Exception;

}
