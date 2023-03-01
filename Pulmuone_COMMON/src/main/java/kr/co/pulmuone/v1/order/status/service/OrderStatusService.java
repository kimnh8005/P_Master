package kr.co.pulmuone.v1.order.status.service;


import kr.co.pulmuone.v1.comm.enums.ClaimEnums;
import kr.co.pulmuone.v1.comm.enums.OrderClaimEnums;
import kr.co.pulmuone.v1.comm.enums.OrderEnums;
import kr.co.pulmuone.v1.comm.mapper.order.status.OrderStatusMapper;
import kr.co.pulmuone.v1.order.claim.dto.*;
import kr.co.pulmuone.v1.order.claim.dto.vo.ClaimDetlHistVo;
import kr.co.pulmuone.v1.order.claim.dto.vo.ClaimDetlVo;
import kr.co.pulmuone.v1.order.claim.service.ClaimProcessBiz;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetlHistVo;
import kr.co.pulmuone.v1.order.status.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <PRE>
 * Forbiz Korea
 * 주문상태 관련 Service
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 29.     이명수         최초작성
 *  1.1    2021. 01. 11.     김명진         주문상세상태 변경 이력, 클레임상세상태 변경 이력 등록 추가
 * =======================================================================
 * </PRE>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderStatusService {

    private final OrderStatusMapper orderStatusMapper;

    @Autowired
    private ClaimProcessBiz claimProcessBiz;

    /**
     * 주문상태 변경 전에 현재 주문상태 조회
     * @param orderStatusSelectRequestDto
     * @return
     */
    public List<OrderStatusSelectResponseDto> getOrderDetailStatusInfo(OrderStatusSelectRequestDto orderStatusSelectRequestDto) {
    	return orderStatusMapper.getOrderDetailStatusInfo(orderStatusSelectRequestDto);
    }

    /**
     * 주문상세상태 변경 이력 등록
     * @param OrderDetlHistVo
     * @return
     */
    public int putOrderDetailStatusHist(OrderDetlHistVo orderDetlHistVo) {
    	return orderStatusMapper.putOrderDetailStatusHist(orderDetlHistVo);
    }

	/**
	 * 주문상태 수정
	 * @param OrderDetlVo
	 * @return
	 */
    protected int putOrderDetailStatus(OrderStatusUpdateDto orderStatusUpdateDto) {
		return orderStatusMapper.putOrderDetailStatus(orderStatusUpdateDto);
	}

    /**
     * 주문클레임상세상태 변경 이력 등록
     * @param ClaimDetlHistVo
     * @return
     */
    protected int putClaimDetailStatusHist(ClaimDetlHistVo claimDetlHistVo) {
    	return orderStatusMapper.putClaimDetailStatusHist(claimDetlHistVo);
    }

    /**
     * 주문클레임상태 수정
     * @param ClaimDetlVo
     * @return
     */
    protected int putClaimDetailStatus(ClaimDetlVo claimDetlVo) {
    	return orderStatusMapper.putClaimDetailStatus(claimDetlVo);
    }

    /**
     * 주문상세 상태 값 조회
     * @param orderStatusCd
     * @param orderDetlIdList
     * @return
     */
    protected List<String> selectTargetOverlapOrderStatusList(String orderStatusCd, List<Long> orderDetlIdList) {
        return orderStatusMapper.selectTargetOverlapOrderStatusList(orderStatusCd, orderDetlIdList);
    }

    /**
     * 클레임 상세 상태 값 조회
     * @param claimStatusCd
     * @param orderDetlIdList
     * @return
     */
    protected List<String> selectTargetOverlapClaimStatusList(String claimStatusCd, List<Long> orderDetlIdList) {
        return orderStatusMapper.selectTargetOverlapClaimStatusList(claimStatusCd, orderDetlIdList);
    }

    /**
     * 주문IF대상 체크
     * @param orderDetlIdList
     * @return
     */
    protected int selectOrderInterfaceTargetCheck(List<Long> orderDetlIdList, String[] urWarehouseIds) {
        return orderStatusMapper.selectOrderInterfaceTargetCheck(orderDetlIdList, urWarehouseIds);
    }

    /**
     * 취소 요청 -> 취소 완료 일괄 변경
     * @param orderClaimStatusRequestDto
     * @return
     */
    protected OrderStatusUpdateResponseDto putClaimCancelReqeustToCancelComplete(OrderClaimStatusRequestDto orderClaimStatusRequestDto) {

        int totalCount = 0;
        int successCount = 0;
        int failCount = 0;

        // 1. odClaimId로 Group
        Map<Long, List<OrderClaimInfoListDto>> groupClaimId = orderClaimStatusRequestDto.getOdClaimInfoList().stream()
                                                                                                             .collect(Collectors.groupingBy(OrderClaimInfoListDto::getOdClaimId, LinkedHashMap::new, Collectors.toList()));

        // 2. loop
        for(long odClaimId : groupClaimId.keySet()) {

            List<OrderClaimInfoListDto> goodsList = groupClaimId.get(odClaimId);

            OrderClaimRegisterRequestDto param = new OrderClaimRegisterRequestDto();
            param.setClaimStatusCd(OrderEnums.OrderStatus.CANCEL_COMPLETE.getCode());   // 주문상태 : 취소완료
            param.setClaimStatusTp(OrderClaimEnums.ClaimStatusTp.CANCEL.getCode());     // 클레임타입 : 취소
            param.setOdClaimId(odClaimId);                                              // 클레임PK
            param.setOdOrderId(goodsList.get(0).getOdOrderId());                        // 주문PK
            param.setOdid(goodsList.get(0).getOdid());                                  // 주문번호
            param.setTargetTp(goodsList.get(0).getTargetTp());                          // 귀책구분
            param.setReturnsYn(goodsList.get(0).getReturnsYn());                        // 회수여부

            List<OrderClaimGoodsInfoDto> goodsInfoList = new ArrayList<>();
            List<OrderClaimSearchGoodsDto> goodsSchList = new ArrayList<>();
            for(OrderClaimInfoListDto orderClaimInfo : goodsList) {
                OrderClaimGoodsInfoDto orderClaimGoodsInfo = new OrderClaimGoodsInfoDto();
                orderClaimGoodsInfo.setOdOrderId(orderClaimInfo.getOdOrderId());
                orderClaimGoodsInfo.setOdOrderDetlId(orderClaimInfo.getOdOrderDetlId());
                orderClaimGoodsInfo.setOdClaimId(orderClaimInfo.getOdClaimId());
                orderClaimGoodsInfo.setOdClaimDetlId(orderClaimInfo.getOdClaimDetlId());
                orderClaimGoodsInfo.setOrderCnt(orderClaimInfo.getOrderCnt());
                orderClaimGoodsInfo.setClaimCnt(orderClaimInfo.getClaimCnt());
                orderClaimGoodsInfo.setUrWarehouseId(orderClaimInfo.getUrWarehouseId());
                orderClaimGoodsInfo.setIlGoodsId(orderClaimInfo.getIlGoodsId());
                goodsInfoList.add(orderClaimGoodsInfo);

                OrderClaimSearchGoodsDto searchGoodsDto = new OrderClaimSearchGoodsDto();
                searchGoodsDto.setOdOrderDetlId(orderClaimInfo.getOdOrderDetlId());
                goodsSchList.add(searchGoodsDto);
            }
            param.setGoodsInfoList(goodsInfoList);
            try {

                //상품쿠폰
                OrderClaimViewRequestDto goodsCouponDto = new OrderClaimViewRequestDto();
                BeanUtils.copyProperties(param, goodsCouponDto);
                goodsCouponDto.setGoodSearchList(goodsSchList);
                goodsCouponDto.setCouponTp(OrderClaimEnums.ClaimCouponTp.COUPON_GOODS.getCode());
                goodsCouponDto.setGoodsChange(Integer.parseInt(ClaimEnums.ClaimGoodsChangeType.ALL_CANCEL.getCode()));
                List<OrderClaimCouponInfoDto> goodsCouponList = claimProcessBiz.getCouponInfoList(goodsCouponDto);
                if(CollectionUtils.isNotEmpty(goodsCouponList)) {
                    param.setGoodsCouponInfoList(goodsCouponList);
                }

                //장바구니쿠폰
                OrderClaimViewRequestDto cartCouponDto = new OrderClaimViewRequestDto();
                BeanUtils.copyProperties(param, cartCouponDto);
                cartCouponDto.setGoodSearchList(goodsSchList);
                cartCouponDto.setCouponTp(OrderClaimEnums.ClaimCouponTp.COUPON_CART.getCode());
                cartCouponDto.setGoodsChange(Integer.parseInt(ClaimEnums.ClaimGoodsChangeType.ALL_CANCEL.getCode()));
                List<OrderClaimCouponInfoDto> cartCouponList = claimProcessBiz.getCouponInfoList(cartCouponDto);
                if(CollectionUtils.isNotEmpty(cartCouponList)) {
                    param.setCartCouponInfoList(cartCouponList);
                }

                claimProcessBiz.addOrderClaim(param);
                successCount += goodsInfoList.size();
            }
            catch(Exception e) {
                log.error("-------------------- 클레임 등록 실패 :: 클레임번호 <{}>", odClaimId);
                log.error("-------------------- 오류 메시지 :: <{}>", e.getMessage());
                e.printStackTrace();
                failCount += goodsInfoList.size();
            }
            totalCount += goodsInfoList.size();
        }

        return OrderStatusUpdateResponseDto.builder()
                                            .totalCount(totalCount)
                                            .successCount(successCount)
                                            .failCount(failCount)
                                            .build();
    }

    /**
     * 배송중 업데이트를 위한 주문상세 조회
     * @param odOrderId
     * @return
     */
    /*public List<String> selectOrderDetailDcList(long odOrderId) {
        return orderStatusMapper.selectOrderDetailDcList(odOrderId);
    }*/
}