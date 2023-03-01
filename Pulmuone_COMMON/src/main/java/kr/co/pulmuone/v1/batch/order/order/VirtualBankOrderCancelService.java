package kr.co.pulmuone.v1.batch.order.order;

import java.util.HashMap;
import java.util.List;

import kr.co.pulmuone.v1.batch.order.order.dto.OrderClaimAddShippingPriceDetlListDto;
import kr.co.pulmuone.v1.batch.order.order.dto.OrderClaimAddShippingPriceListDto;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimGoodsInfoDto;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimSearchGoodsDto;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.batch.order.order.dto.VirtualBankOrderCancelDto;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.mappers.batch.master.order.order.VirtualBankOrderCancelMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 API 배치 Service
 * </PRE>
 */

@Slf4j
@RequiredArgsConstructor
@Service
public class VirtualBankOrderCancelService {

    private final VirtualBankOrderCancelMapper virtualBankOrderCancelMapper;


	/**
	 * 입금기한 지난 가상계좌주문 대상 조회
	 * @param
	 * @return void
	 * @throws BaseException
	 */
    public List<VirtualBankOrderCancelDto> getTargetVirtualBankOrderCancel() throws Exception {
    	return virtualBankOrderCancelMapper.getTargetVirtualBankOrderCancel();
    }

	/**
	 * 입금기한 지난 추가배송비 가상계좌 / 직접 결제 대상 조회
	 * @param
	 * @return void
	 * @throws BaseException
	 */
	public List<OrderClaimAddShippingPriceListDto> getTargetOrderClaimAddShippingPriceVirtualDirectPay() throws Exception {
		return virtualBankOrderCancelMapper.getTargetOrderClaimAddShippingPriceVirtualDirectPay();
	}

	/**
	 * 입금기한 지난 추가배송비 가상계좌 / 직접 결제 대상 상세 조회
	 * @param
	 * @return void
	 * @throws BaseException
	 */
	public List<OrderClaimGoodsInfoDto> getTargetOrderClaimAddShippingPriceVirtualDirectPayDetl(List<OrderClaimAddShippingPriceListDto> addShippingPriceTargetList) throws Exception {
		return virtualBankOrderCancelMapper.getTargetOrderClaimAddShippingPriceVirtualDirectPayDetl(addShippingPriceTargetList);
	}

	/**
	 * 주문상세 PK 조회
	 * @param odOrderId
	 * @throws BaseException
	 */
	public List<HashMap> getOdOrderDetlId(long odOrderId) throws Exception {
		return virtualBankOrderCancelMapper.getOdOrderDetlId(odOrderId);
	}
}
