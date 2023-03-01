package kr.co.pulmuone.v1.order.email.service;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;

import kr.co.pulmuone.v1.base.dto.vo.GetCodeListResultVo;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimGoodsInfoDto;
import kr.co.pulmuone.v1.order.email.dto.*;

public interface OrderEmailBiz {

	List<UnregistetedInvoiceDto> getUnregistetedInvoiceList() throws Exception;

	OrderInfoForEmailResultDto getOrderInfoForEmail(Long odOrderId) throws Exception;

	List<OrderDetailGoodsDto> getOrderDetailGoodsListForEmail(Long odOrderId, List<Long> odOrderDetlIdList) throws Exception;

	OrderInfoForEmailResultDto getOrderGoodsDeliveryInfoForEmail(List<Long> odOrderDetlIdList) throws Exception;

	List<OrderDetailGoodsDto> getOrderIdList(List<Long> odOrderDetlIdList) throws Exception;

	OrderInfoForEmailResultDto getOrderClaimCompleteInfoForEmail(Long odClaimId, List<Long> odOrderDetlIdList) throws Exception;

	OrderInfoForEmailResultDto getOrderCancelBeforeDepositInfoForEmail(Long odOrderId) throws Exception;

	OrderInfoForEmailResultDto getOrderRegularApplyCompletedInfoForEmail(Long odRegularReqId, String firstOrderYn) throws Exception;

	OrderInfoForEmailResultDto getOrderRegularInfoForEmail(Long odRegularResultId) throws Exception;

	OrderInfoForEmailResultDto getOrderRegularResultDetlInfoForEmail(Long odRegularResultDetlId) throws Exception;

	OrderInfoForEmailResultDto getOrderDailyGreenJuiceEndInfoForEmail(Long odOrderDetlId) throws Exception;

	List<Long> getTargetOrderRegularExpired() throws Exception;

	int putSmsSendStatusByOrderRegularExpired(Long odRegularResultId) throws Exception;

	List<Long> getTargetOrderDailyGreenJuiceEnd() throws Exception;

	List<HashMap<String,BigInteger>> getTargetOrderRegularGoodsPriceChangeList() throws Exception;

	OrderInfoForEmailResultDto getOrderRegularGoodsPriceChangeInfoForEmail(Long odRegularReqId, Long ilGoodsId) throws Exception;

	List<BosOrderStatusNotiDto> getTargetBosOrderStatusNotification() throws Exception;

	OrderInfoForEmailResultDto getBosOrderStatusNotificationForEmail(Long urClientId) throws Exception;

	boolean checkBosOrgaCautionOrderNotification(Long odOrderId) throws Exception;

	OrderInfoForEmailResultDto getPayAdditionalShippingPriceInfoForEmail(Long odPaymentMasterId) throws Exception;

	OrderInfoForEmailResultDto getOrderShopPickupGoodsDeliveryInfoForEmail(List<Long> odOrderDetlIdList) throws Exception;

	OrderInfoForEmailResultDto getOrderRegularInfoForEmailForPaymentFail(Long odRegularResultId) throws Exception;
}
