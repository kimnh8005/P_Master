package kr.co.pulmuone.v1.order.order.service;


import kr.co.pulmuone.v1.comm.enums.PayEnums;
import kr.co.pulmuone.v1.comm.enums.PgEnums;
import kr.co.pulmuone.v1.comm.mapper.order.order.MallOrderDetailMapper;
import kr.co.pulmuone.v1.goods.goods.dto.vo.ShippingInfoByOdOrderIdResultVo;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimAttcInfoDto;
import kr.co.pulmuone.v1.order.order.dto.mall.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <PRE>
 * Forbiz Korea
 * Mall 주문상세 리스트 관련 Service
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 01. 20.	이규한		최초작성
 * =======================================================================
 * </PRE>
 */

@Service
@RequiredArgsConstructor
public class MallOrderDetailService {

	@Value("${inicis.cardBillUrl}")
	private String inicisCardBillUrl;

	@Value("${inicis.cashBillUrl}")
	private String inicisCashBillUrl;

	@Value("${kcp.cardBillUrl}")
	private String kcpCardBillUrl;

	@Value("${kcp.cashBillUrl}")
	private String kcpCashBillUrl;

    private final MallOrderDetailMapper mallOrderDetailMapper;


	/**
	 * @Desc 주문 조회
	 * @param odOrderId(주문 PK)
	 * @return OrderVo
	 * @throws
	 */
	protected MallOrderDto getOrder(long odOrderId, String urUserId, String guestCi) {
		return mallOrderDetailMapper.getOrder(odOrderId, urUserId, guestCi);
	}

	/**
	 * 주문 클레임 조회
	 * @param odClaimId
	 * @return
	 */
	protected Map<String, Object> getOrderClaim(long odClaimId, String urUserId, String guestCi) {
		return mallOrderDetailMapper.getOrderClaim(odClaimId, urUserId, guestCi);
	}

	/**
	 * @Desc 주문 정기배송 정보 조회
	 * @param odOrderId(주문 PK)
	 * @return MallOrderRegularDto
	 * @throws
	 */
	protected MallOrderRegularDto getRegular(long odOrderId) {
		return mallOrderDetailMapper.getRegular(odOrderId);
	}


    /**
     * @Desc 주문상세 리스트  조회
     * @param odOrderId(주문 PK)
     * @return List<MallOrderDetailListDto>
     * @throws
     */
   	protected List<MallOrderDetailGoodsDto> getOrderDetailGoodsList(long odOrderId) {
        return mallOrderDetailMapper.getOrderDetailGoodsList(odOrderId);
	}

	/**
	 * 주문 클레임 상세 리스트 조회
	 * @param odClaimId
	 * @return
	 */
	protected List<MallOrderDetailGoodsDto> getClaimDetailGoodsList(long odOrderId, long odClaimId) {
		return mallOrderDetailMapper.getClaimDetailGoodsList(odOrderId, odClaimId);
	}


   	/**
   	 * @Desc 주문상세 배송지  조회
   	 * @param odOrderId(주문 PK)
   	 * @return MallOrderDetailShippingZoneDto
   	 * @throws
   	 */
   	protected MallOrderDetailShippingZoneDto getOrderDetailShippingInfo(long odOrderId) {
   		return mallOrderDetailMapper.getOrderDetailShippingInfo(odOrderId);
   	}

	/**
	 * @Desc 주문 상세 결제 정보
	 * @param odOrderId
	 * @return MallOrderDetailPayDto
	 */
	protected MallOrderDetailPayResultDto getOrderDetailPayInfo(long odOrderId) {
   		return mallOrderDetailMapper.getOrderDetailPayInfo(odOrderId);
	}

	/**
	 * @Desc 주문 상세 결제 정보
	 * @param odOrderId
	 * @return MallOrderDetailPayDto
	 */
	protected List<MallOrderDetailPayDiscountDto> getOrderDetailDiscountList(long odOrderId) {
		return mallOrderDetailMapper.getOrderDetailDiscountList(odOrderId);
	}


	/**
	 * @Desc 주문 상세 주문 취소 / 반품 신청 내역
	 * @param odOrderId
	 * @return List<MallOrderDetailClaimListDto>
	 */
	protected List<MallOrderDetailClaimListDto> getOrderDetailClaimList(long odOrderId) {
		return mallOrderDetailMapper.getOrderDetailClaimList(odOrderId);
	}

	/**
	 * 주문 클레임 상세 반품 수거지
	 * @param odClaimId
	 * @return
	 */
	protected MallClaimSendShippingZoneDto getClaimShippingZone(long odClaimId) {
		return mallOrderDetailMapper.getClaimShippingZone(odClaimId);
	}

	/**
	 * 주문 클레임 정보
	 * @param odClaimId
	 * @return
	 */
	protected MallClaimInfoDto getClaimInfo(long odClaimId) {
		return mallOrderDetailMapper.getClaimInfo(odClaimId);
	}

	/**
	 * 클레임 첨부파일 목록
	 * @param odClaimId
	 * @return
	 */
	protected List<OrderClaimAttcInfoDto> getClaimAttcList(long odClaimId) {
		return mallOrderDetailMapper.getClaimAttcList(odClaimId);
	}

	/**
	 * 클레임 결제 정보
	 * @param odClaimId
	 * @return
	 */
	protected MallOrderDetailPayResultDto getClaimDetailPayInfo(long odOrderId, long odClaimId) {
		return mallOrderDetailMapper.getClaimDetailPayInfo(odOrderId, odClaimId);
	}

	/**
	 * 주문배송지PK로 주문정보 조회
	 * @param odShippingZoneId
	 * @return List<OrderDetailByOdShippingZondIdResultDto>
	 */
	protected List<OrderDetailByOdShippingZondIdResultDto> getOrderDetailInfoByOdShippingZoneId(Long odShippingZoneId) {
		return mallOrderDetailMapper.getOrderDetailInfoByOdShippingZoneId(odShippingZoneId);
	}

	/**
	 * 주문PK로 배송정책별 주문정보 조회
	 * @param odOrderId
	 * @return List<ShippingInfoByOdOrderIdResultVo>
	 */
	protected List<ShippingInfoByOdOrderIdResultVo> getShippingInfoByOdOrderId(Long odOrderId){
		return mallOrderDetailMapper.getShippingInfoByOdOrderId(odOrderId);
	}

	/**
	 * 주문PK로 상품PK별 주문정보 조회
	 * @param odOrderId
	 * @return List<ShippingInfoByOdOrderIdResultVo>
	 */
	protected List<ShippingInfoByOdOrderIdResultVo> getOrderDetailInfoByOdOrderId(Long odOrderId){
		return mallOrderDetailMapper.getOrderDetailInfoByOdOrderId(odOrderId);
	}

	/**
	 * PG별 매출전표 url 조회
	 */
	protected String getOrderBillUrl(MallOrderDetailPayResultDto resultDto){
		String billUrl = "";
		PgEnums.PgAccountType pgAccountType = PgEnums.PgAccountType.findByCode(resultDto.getPgService());

		// 이니시스
		if(pgAccountType != null && PgEnums.PgServiceType.INICIS.getCode().equals(pgAccountType.getPgServiceType())){
			// 카드결제
			if(PayEnums.PsPay.CARD.getCode().equals(resultDto.getPayType())){
				billUrl = inicisCardBillUrl + resultDto.getTid() + "&noMethod=1";

			}else{
				// 현금영수증
				if(StringUtils.isNotEmpty(resultDto.getCashReceiptNo())){
					billUrl = inicisCashBillUrl + resultDto.getCashReceiptAuthNo() + "&clpaymethod=22";
				}
			}

		// KCP
		}else if(pgAccountType != null && PgEnums.PgServiceType.KCP.getCode().equals(pgAccountType.getPgServiceType())){

			// 카드결제, 간편결제(카카오페이, 페이코, 네이버페이, 삼성페이)
			if(PayEnums.PsPay.CARD.getCode().equals(resultDto.getPayType()) || PayEnums.PsPay.KAKAOPAY.getCode().equals(resultDto.getPayType())
				|| PayEnums.PsPay.PAYCO.getCode().equals(resultDto.getPayType()) || PayEnums.PsPay.NAVERPAY.getCode().equals(resultDto.getPayType())
				|| PayEnums.PsPay.SSPAY.getCode().equals(resultDto.getPayType())){
				billUrl = kcpCardBillUrl+resultDto.getTid()+"&order_no="+resultDto.getOdid()+"&trade_mony="+resultDto.getPaymentPrice();
			}else{
				// 현금영수증
				if(StringUtils.isNotEmpty(resultDto.getCashReceiptNo())){
					billUrl = kcpCashBillUrl + resultDto.getCashReceiptAuthNo() + "&order_no=" + resultDto.getOdid() + "&trade_mony=" + resultDto.getPaymentPrice();
				}
			}
		}

		return billUrl;
	}

	/**
	 * @Desc 주문상세PK로 주문정보 조회
	 * @param odOrderDetlId
	 * @return MallOrderDto
	 */
	protected MallOrderDto getOrderInfoByOdOrderDetlId(Long odOrderDetlId){
		return mallOrderDetailMapper.getOrderInfoByOdOrderDetlId(odOrderDetlId);
	}

	/**
	 * @Desc 배송가능여부 체크 위한 주문정보 조회
	 * @param odOrderId
	 * @param odOrderDetlId
	 * @return List<HashMap>
	 */
	protected List<HashMap> getOrderInfoForShippingPossibility(Long odOrderId, Long odOrderDetlId){
		return mallOrderDetailMapper.getOrderInfoForShippingPossibility(odOrderId, odOrderDetlId);
	}
}