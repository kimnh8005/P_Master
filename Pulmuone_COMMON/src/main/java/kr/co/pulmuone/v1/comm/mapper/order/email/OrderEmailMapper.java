package kr.co.pulmuone.v1.comm.mapper.order.email;

import kr.co.pulmuone.v1.order.email.dto.*;
import kr.co.pulmuone.v1.order.email.dto.vo.BosOrderInfoForEmailVo;
import kr.co.pulmuone.v1.order.email.dto.vo.OrderRegularReqInfoVo;
import kr.co.pulmuone.v1.order.email.dto.vo.OrderInfoForEmailVo;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface OrderEmailMapper {

	List<UnregistetedInvoiceDto> getUnregistetedInvoiceList() throws Exception;

	OrderInfoForEmailVo getOrderInfoForEmail(@Param("odOrderId")Long odOrderId, @Param("goodsTpCd") String goodsTpCd) throws Exception;

	List<OrderDetailGoodsDto> getOrderDetailGoodsListForEmail(@Param("odOrderId")Long odOrderId, @Param("odOrderDetlIdList")List<Long> odOrderDetlIdList) throws Exception;

	List<OrderDetailGoodsDto> getOrderIdList(@Param("odOrderDetlIdList")List<Long> odOrderDetlIdList) throws Exception;

	OrderInfoForEmailVo getOrderClaimInfoForEmail(@Param("odClaimId")Long odClaimId) throws Exception;

	List<Long> getClaimDetlIdList(@Param("odClaimId")Long odClaimId) throws Exception;

	OrderRegularReqInfoVo getOrderRegularReqInfoForEmail(@Param("odRegularReqId")Long odRegularReqId) throws Exception;

	List<OrderDetailGoodsDto> getOrderRegularGoodsListForEmail(@Param("odRegularReqId")Long odRegularReqId) throws Exception;

	OrderRegularResultDto getRegularOrderResultCreateGoodsListForEmail(@Param("odRegularResultId")Long odRegularResultId) throws Exception;

	OrderRegularResultDto getRegularOrderResultDetlInfoForEmail(@Param("odRegularResultDetlId")Long odRegularResultDetlId) throws Exception;

	List<OrderRegularResultDto> getOrderRegularResultList(@Param("odRegularResultId")Long odRegularResultId) throws Exception;

	List<Long> getTargetOrderRegularExpired() throws Exception;

	int putSmsSendStatusByOrderRegularExpired(@Param("odRegularResultId")Long odRegularResultId) throws Exception;

	List<Long> getTargetOrderDailyGreenJuiceEnd() throws Exception;

	OrderInfoForEmailVo getOrderDailyGreenJuiceEndInfoForEmail(@Param("odOrderDetlId")Long odOrderDetlId) throws Exception;

	List<HashMap<String,BigInteger>> getTargetOrderRegularGoodsPriceChangeList() throws Exception;

	OrderRegularGoodsPriceChangeDto getOrderRegularGoodsPriceChangeInfo(@Param("odRegularReqId")Long odRegularReqId, @Param("ilGoodsId") Long ilGoodsId) throws Exception;

	List<BosOrderStatusNotiDto> getTargetBosOrderStatusNotification() throws Exception;

	BosOrderInfoForEmailVo getOrderCountForOrderStatusNotification(long urClientId) throws Exception;

	OrderInfoForEmailVo getPayAdditionalShippingPriceInfo(Long odPaymentMasterId) throws Exception;

	OrderInfoForEmailVo getOrderShopPickupInfoForEmail(@Param("odOrderId") Long odOrderId, @Param("goodsTpCd")String goodsTpCd) throws Exception;

	OrderRegularResultDto getRegularOrderResultForPaymentFail(Long odRegularResultId) throws Exception;

	int putOdRegularInfoSendHist(OrderRegularGoodsPriceChangeDto orderRegularGoodsPriceChangeDto);
}


