package kr.co.pulmuone.v1.batch.order.order;

import kr.co.pulmuone.v1.batch.order.order.dto.OrderClaimAddShippingPriceListDto;
import kr.co.pulmuone.v1.batch.order.order.dto.VirtualBankOrderCancelDto;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.ClaimEnums;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.comm.enums.OrderClaimEnums;
import kr.co.pulmuone.v1.comm.enums.OrderEnums;
import kr.co.pulmuone.v1.comm.enums.OrderEnums.OrderStatus;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.order.claim.dto.*;
import kr.co.pulmuone.v1.order.claim.service.ClaimProcessBiz;
import kr.co.pulmuone.v1.order.claim.service.ClaimRequestBiz;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 API 배치 BizImpl
 * </PRE>
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class VirtualBankOrderCancelBizImpl implements VirtualBankOrderCancelBiz {

	@Autowired
	private final VirtualBankOrderCancelService virtualBankOrderCancelService;

	@Autowired
	private final ClaimRequestBiz claimRequestBiz;

	@Autowired
	private final ClaimProcessBiz claimProcessBiz;

	/**
	 * 입금기한 지난 가상계좌주문  취소
	 * @param
	 * @return void
	 * @throws BaseException
	 */
	@Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class })
	public void runVirtualBankOrderCancel() throws Exception {

		// 1. 취소 대상 주문 조회
		List<VirtualBankOrderCancelDto> targetList = virtualBankOrderCancelService.getTargetVirtualBankOrderCancel();

		if(CollectionUtils.isNotEmpty(targetList)) {

			for(VirtualBankOrderCancelDto dto : targetList) {
				Long odOrderId = dto.getOdOrderId();

				try {
					// 2. 주문  취소
					// 2-1. 주문PK별 주문취소 대상 상품 목록 조회
					OrderClaimViewRequestDto orderClaimViewRequestDto = new OrderClaimViewRequestDto();
					orderClaimViewRequestDto.setOdOrderId(odOrderId);
					orderClaimViewRequestDto.setGoodsChange(Integer.parseInt(ClaimEnums.ClaimGoodsChangeType.ALL_CANCEL.getCode()));  // 조회구분 (전체취소수량 : 0, 수량변경취소 : 1)
					orderClaimViewRequestDto.setClaimStatusTp(OrderClaimEnums.ClaimStatusTp.CANCEL.getCode());
					orderClaimViewRequestDto.setFrontTp(Integer.parseInt(ClaimEnums.ClaimFrontTp.BATCH.getCode())); // 프론트 구분 (0 : Bos, 1:Front 2:Batch)
					// 상품리스트
					List<OrderClaimSearchGoodsDto> goodSearchList = new ArrayList<>();
					List<HashMap> odOrderDetlIdList = virtualBankOrderCancelService.getOdOrderDetlId(odOrderId);
					for(HashMap goodsDto : odOrderDetlIdList){
						OrderClaimSearchGoodsDto searchGoodsDto = new OrderClaimSearchGoodsDto();
						searchGoodsDto.setOdOrderDetlId(Long.parseLong(String.valueOf(goodsDto.get("OD_ORDER_DETL_ID"))));
						searchGoodsDto.setOrderCnt(Integer.parseInt(String.valueOf(goodsDto.get("ORDER_CNT"))));
						searchGoodsDto.setClaimCnt(Integer.parseInt(String.valueOf(goodsDto.get("ORDER_CNT"))));
						searchGoodsDto.setClaimGoodsYn("N"); 	// 클레임수량 변경 상품 유무 N : 아니오
						goodSearchList.add(searchGoodsDto);
					}
					orderClaimViewRequestDto.setGoodSearchList(goodSearchList);

					ApiResult<?> result = claimRequestBiz.getOrderClaimInfo(orderClaimViewRequestDto);
					OrderClaimViewResponseDto orderClaimResDto = (OrderClaimViewResponseDto)result.getData();

					// 상품리스트 체크
					checkGoodsInfoList(orderClaimResDto);

					// 2-2. 주문 취소
					OrderClaimRegisterRequestDto orderClaimRegisterRequestDto = new OrderClaimRegisterRequestDto();
					orderClaimRegisterRequestDto.setOdOrderId(odOrderId);
					orderClaimRegisterRequestDto.setOdid(dto.getOdid());
					orderClaimRegisterRequestDto.setClaimStatusTp(OrderClaimEnums.ClaimStatusTp.CANCEL.getCode());
					orderClaimRegisterRequestDto.setClaimStatusCd(OrderEnums.OrderStatus.INCOM_BEFORE_CANCEL_COMPLETE.getCode());
					orderClaimRegisterRequestDto.setStatus(OrderEnums.OrderStatus.INCOM_READY.getCode());
					orderClaimRegisterRequestDto.setPsClaimMallId(0); // 가상계좌 입금전 취소일 경우 MALL 클레임 PK 0 고정
					orderClaimRegisterRequestDto.setTargetTp(ClaimEnums.ReasonAttributableType.BUYER.getType());
					orderClaimRegisterRequestDto.setRefundType(orderClaimResDto.getPriceInfo().getRefundType());
					orderClaimRegisterRequestDto.setGoodsNm(dto.getGoodsName());
					orderClaimRegisterRequestDto.setGoodsPrice(orderClaimResDto.getPriceInfo().getGoodsPrice());
					orderClaimRegisterRequestDto.setGoodsCouponPrice(orderClaimResDto.getPriceInfo().getGoodsCouponPrice());
					orderClaimRegisterRequestDto.setCartCouponPrice(orderClaimResDto.getPriceInfo().getCartCouponPrice());
					orderClaimRegisterRequestDto.setShippingPrice(orderClaimResDto.getPriceInfo().getShippingPrice());
					orderClaimRegisterRequestDto.setRefundPrice(orderClaimResDto.getPriceInfo().getRefundPrice());
					orderClaimRegisterRequestDto.setRefundPointPrice(orderClaimResDto.getPriceInfo().getRefundPointPrice());
					orderClaimRegisterRequestDto.setPartial(true); // 부분취소여부 (true : 전체취소, false : 부분취소)
					orderClaimRegisterRequestDto.setHistMsg("입금 전 취소");
					orderClaimRegisterRequestDto.setGoodsInfoList(orderClaimResDto.getOrderGoodList());
					orderClaimRegisterRequestDto.setGoodsCouponInfoList(orderClaimResDto.getGoodsCouponList());
					orderClaimRegisterRequestDto.setCartCouponInfoList(orderClaimResDto.getCartCouponList());
					orderClaimRegisterRequestDto.setFrontTp(Integer.parseInt(ClaimEnums.ClaimFrontTp.BATCH.getCode()));
					orderClaimRegisterRequestDto.setUrUserId(String.valueOf(Constants.BATCH_CREATE_USER_ID));
					orderClaimRegisterRequestDto.setCustomUrUserId(String.valueOf(dto.getUrUserId()));


					ApiResult<?> orderClaim = claimProcessBiz.addOrderClaim(orderClaimRegisterRequestDto);

				} catch (Exception e) {
					log.debug("입금기한 지난 가상계좌주문 취소 실패 :: <{}>", e.getMessage());
				}

			}
		}

		// 2. 클레임 거부 대상 목록 조회 (가상계좌 입금 기한 만료 / 직접 결제 입금 기한 만료 대상 목록)
		log.debug("======== 클레임 거부 대상 목록 조회");
		List<OrderClaimAddShippingPriceListDto> addShippingPriceTargetList = virtualBankOrderCancelService.getTargetOrderClaimAddShippingPriceVirtualDirectPay();
		if(ObjectUtils.isNotEmpty(addShippingPriceTargetList)) {
			// 3. 클레임 거부 대상 상세 목록 조회
			log.debug("======== 클레임 거부 대상 상세 목록 조회");
			List<OrderClaimGoodsInfoDto> addShippingPriceTargetDetlList = virtualBankOrderCancelService.getTargetOrderClaimAddShippingPriceVirtualDirectPayDetl(addShippingPriceTargetList);
			if(ObjectUtils.isNotEmpty(addShippingPriceTargetDetlList)) {
				// 4. 클레임 거부 대상 목록 클레임 거부 처리
				log.debug("======== 클레임 거부 대상 클레임 처리");
				for(OrderClaimAddShippingPriceListDto addShippingPriceTargetItem : addShippingPriceTargetList) {
					List<OrderClaimGoodsInfoDto> goodsInfoList = addShippingPriceTargetDetlList.stream().filter(x -> x.getOdClaimId() == addShippingPriceTargetItem.getOdClaimId()).collect(Collectors.toList());
					if(ObjectUtils.isEmpty(goodsInfoList)) {
						continue;
					}
					String claimStatusCd = OrderEnums.OrderStatus.CANCEL_DENY_DEFE.getCode();
					if(OrderClaimEnums.ClaimStatusTp.RETURN.getCode().equals(addShippingPriceTargetItem.getClaimStatusTp())) {
						claimStatusCd = OrderStatus.RETURN_DENY_DEFER.getCode();
					}
					try {
						OrderClaimRegisterRequestDto orderClaimRegisterRequestDto = new OrderClaimRegisterRequestDto();
						orderClaimRegisterRequestDto.setOdClaimId(addShippingPriceTargetItem.getOdClaimId());
						orderClaimRegisterRequestDto.setOdOrderId(addShippingPriceTargetItem.getOdOrderId());
						orderClaimRegisterRequestDto.setOdid(addShippingPriceTargetItem.getOdid());
						orderClaimRegisterRequestDto.setClaimStatusTp(addShippingPriceTargetItem.getClaimStatusTp());
						orderClaimRegisterRequestDto.setClaimStatusCd(claimStatusCd);
						orderClaimRegisterRequestDto.setPsClaimMallId(0);
						orderClaimRegisterRequestDto.setRejectReasonMsg("입금 기한 초과");
						orderClaimRegisterRequestDto.setTargetTp(addShippingPriceTargetItem.getTargetTp());
						orderClaimRegisterRequestDto.setReturnsYn(addShippingPriceTargetItem.getReturnsYn());
						orderClaimRegisterRequestDto.setRecvZipCd(addShippingPriceTargetItem.getRecvZipCd());
						orderClaimRegisterRequestDto.setGoodsInfoList(goodsInfoList);
						orderClaimRegisterRequestDto.setFrontTp(Integer.parseInt(ClaimEnums.ClaimFrontTp.BATCH.getCode()));
						orderClaimRegisterRequestDto.setUrUserId(String.valueOf(Constants.BATCH_CREATE_USER_ID));

						ApiResult<?> orderClaim = claimProcessBiz.addOrderClaim(orderClaimRegisterRequestDto);
					}
					catch(Exception e) {
						log.debug("클레임 거부 처리 실패 :: <{}>", e.getMessage());
					}
				}
			}
		}
	}

	/** 상품리스트 체크 */
	public void checkGoodsInfoList(OrderClaimViewResponseDto orderClaimResDto){

		// 묶음상품 대표상품 제외
 		Iterator<OrderClaimGoodsInfoDto> orderGoodsListItr = orderClaimResDto.getOrderGoodList().iterator();
		while(orderGoodsListItr.hasNext()){
			OrderClaimGoodsInfoDto orderClaimGoodsInfoDto = orderGoodsListItr.next();
			if(GoodsEnums.GoodsType.PACKAGE.getCode().equals(orderClaimGoodsInfoDto.getGoodsTpCd()) && orderClaimGoodsInfoDto.getOdOrderDetlSeq() == 0){
				orderGoodsListItr.remove();
			}
		}
	}

}