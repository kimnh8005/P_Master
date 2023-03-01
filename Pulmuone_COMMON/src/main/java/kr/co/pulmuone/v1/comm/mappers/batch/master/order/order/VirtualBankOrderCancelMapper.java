package kr.co.pulmuone.v1.comm.mappers.batch.master.order.order;

import kr.co.pulmuone.v1.batch.order.order.dto.OrderClaimAddShippingPriceDetlListDto;
import kr.co.pulmuone.v1.batch.order.order.dto.OrderClaimAddShippingPriceListDto;
import kr.co.pulmuone.v1.batch.order.order.dto.VirtualBankOrderCancelDto;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimGoodsInfoDto;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimSearchGoodsDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 API 배치 Mapper
 * </PRE>
 */

@Mapper
public interface VirtualBankOrderCancelMapper {

	/**
	 * 입금기한 지난 가상계좌 주문건 조회
	 * @return List<VirtualBankOrderCancelDto>
	 * @throws BaseException
	 */
    List<VirtualBankOrderCancelDto> getTargetVirtualBankOrderCancel() throws BaseException;

	/**
	 * 입금기한 지난 추가배송비 가상계좌 / 직접 결제 대상 조회
	 * @return List<VirtualBankOrderCancelDto>
	 * @throws BaseException
	 */
    List<OrderClaimAddShippingPriceListDto> getTargetOrderClaimAddShippingPriceVirtualDirectPay() throws BaseException;

	/**
	 * 입금기한 지난 추가배송비 가상계좌 / 직접 결제 대상 상세 조회
	 * @return List<VirtualBankOrderCancelDto>
	 * @throws BaseException
	 */
	List<OrderClaimGoodsInfoDto> getTargetOrderClaimAddShippingPriceVirtualDirectPayDetl(@Param(value ="addShippingPriceTargetList") List<OrderClaimAddShippingPriceListDto> addShippingPriceTargetList) throws BaseException;

	List<HashMap> getOdOrderDetlId(long odOrderId) throws Exception;
}
