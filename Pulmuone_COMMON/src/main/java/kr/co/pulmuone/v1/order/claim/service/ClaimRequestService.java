package kr.co.pulmuone.v1.order.claim.service;

import kr.co.pulmuone.v1.comm.enums.ClaimEnums;
import kr.co.pulmuone.v1.comm.enums.OrderClaimEnums;
import kr.co.pulmuone.v1.comm.enums.OrderEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.mapper.order.claim.ClaimRequestMapper;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.goods.goods.service.GoodsGoodsBiz;
import kr.co.pulmuone.v1.order.claim.dto.*;
import kr.co.pulmuone.v1.order.schedule.dto.OrderDetailScheduleDeliverableListResponseDto;
import kr.co.pulmuone.v1.order.schedule.service.mall.MallOrderScheduleBiz;
import kr.co.pulmuone.v1.policy.claim.dto.PolicyClaimMallRequestDto;
import kr.co.pulmuone.v1.policy.claim.dto.vo.PolicyClaimMallVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 클레임 상세정보 관련 Service
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 15.  강상국         최초작성
 * =======================================================================
 * </PRE>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ClaimRequestService {

    @Autowired
    private final ClaimRequestMapper claimRequestMapper;

    @Autowired
    private MallOrderScheduleBiz mallOrderScheduleBiz;

    @Autowired
    private ClaimUtilPriceService claimUtilPriceService;

    /**
     * @Desc 프론트 화면에서 클레임 상품 목록 조회
     *
     * @param orderClaimViewRequestDto
     * @return
     */
    protected List<OrderClaimGoodsInfoDto> getOrderClaimReqGoodsInfoList(OrderClaimViewRequestDto orderClaimViewRequestDto) {

        log.debug("클레임 대상 상품 전체 조회 ----- > <{}>", orderClaimViewRequestDto.getOdClaimId());
        log.debug("상품 정보 ----- > <{}>", orderClaimViewRequestDto.getGoodSearchList());
        if(orderClaimViewRequestDto.getOdClaimId() > 0 && !ClaimEnums.ClaimFrontTp.FRONT.getCode().equals(StringUtil.nvl(String.valueOf(orderClaimViewRequestDto.getFrontTp())))) {
            // 클레임 신청 상품 목록 조회
            return claimRequestMapper.getClaimReqGoodsInfoList(orderClaimViewRequestDto);
        }
        else {
            // 주문 상세 상품 목록 조회
            return claimRequestMapper.getOrderDetlClaimGoodsInfoList(orderClaimViewRequestDto);
        }
    }

    /**
     * 미출정보 목록조회
     * @param orderClaimViewRequestDto
     * @return
     */
    protected List<OrderClaimGoodsInfoDto> getUndeliveredInfoList(OrderClaimViewRequestDto orderClaimViewRequestDto) {
        return claimRequestMapper.getUndeliveredInfoList(orderClaimViewRequestDto);
    }

    /**
     * @Desc 녹즙 클레임 상품 정보 조회
     *
     * @param orderClaimViewRequestDto
     * @return
     */
    protected List<OrderClaimGoodsInfoDto> getClaimGreenJuiceGoodsInfoList(OrderClaimViewRequestDto orderClaimViewRequestDto) {
        return claimRequestMapper.getClaimGreenJuiceGoodsInfoList(orderClaimViewRequestDto);
    }



    /**
     * 상품상세 할인 정보 또는 주문클레임 상세 할인정보에서 상품 또는 장바구니 쿠폰 정보 조회
     * @param reqDto
     * @return
     */
    public List<OrderClaimCouponInfoDto> getCouponInfoList(OrderClaimViewRequestDto reqDto) {
        List<OrderClaimCouponInfoDto> couponList = null;

        log.debug("=== 주문클레임 신청에서 쿠폰 조회 !!!=== reqDto.getGoodSearchList() :: <{}>", CollectionUtils.isNotEmpty(reqDto.getGoodSearchList()));

        if (reqDto.getOdClaimId() == 0) {
            if (CollectionUtils.isNotEmpty(reqDto.getGoodSearchList())) {
                log.debug("=== 신규 주문클레임 신청에서 쿠폰 조회 !!!=== reqDto.getGoodSearchList() :: <{}>, reqDto.getGoodsChange() :: <{}>", reqDto.getGoodSearchList().size(), reqDto.getGoodsChange());
                // goodsChange - 조회구분 전체 : 0, 상품갯수변경 : 1
                if (reqDto.getGoodsChange() == 0)
                    couponList = claimRequestMapper.getOrderClaimCouponInfoList(reqDto);
                else
                    couponList = claimRequestMapper.getOrderClaimGoodsChangeCouponInfoList(reqDto);
            }
        }
        else {
            if (CollectionUtils.isNotEmpty(reqDto.getGoodSearchList())) {
                log.debug("=== 클레임 데이터 신청에서 쿠폰 조회 !!!=== reqDto.getGoodSearchList() :: <{}>, reqDto.getGoodsChange() :: <{}>", reqDto.getGoodSearchList().size(), reqDto.getGoodsChange());
                // goodsChange - 조회구분 전체 : 0, 상품갯수변경 : 1
                if (reqDto.getGoodsChange() == 0)
                    couponList = claimRequestMapper.getClaimCouponInfoList(reqDto);
                else
                    couponList = claimRequestMapper.getClaimGoodsChangeCouponInfoList(reqDto);
            }
        }
        return couponList;
    }

    /**
     * 받는 배송지 조회
     * @param reqDto
     * @return
     */
    public List<OrderClaimShippingInfoDto> getReceShippingInfoList(OrderClaimViewRequestDto reqDto, List<OrderClaimGoodsInfoDto> goodsList) {
        List<OrderClaimShippingInfoDto> resultList = null;
        log.debug("=== 테스트 - 받는 배송지 조회 !!!=== claimId :: <{}>, orderId<{}> getReceiveShippingInfoList<{}>", reqDto.getOdClaimId(), reqDto.getOdOrderId(), goodsList);
        // 매장픽업여부
        boolean isShopPickup = goodsList.stream().filter(vo -> OrderEnums.OrderStatusDetailType.SHOP_DELIVERY.getCode().equals(vo.getOrderStatusDeliTp()) ||
                                                                OrderEnums.OrderStatusDetailType.SHOP_PICKUP.getCode().equals(vo.getOrderStatusDeliTp()))
                                                .count() > 0 ? true : false;
        if(!isShopPickup) {
            if (reqDto.getOdClaimId() == 0) {
                Map<Long, Long> orderDetlMap = goodsList.stream().collect(Collectors.toMap(OrderClaimGoodsInfoDto::getOdOrderDetlId, OrderClaimGoodsInfoDto::getUrWarehouseId));
                if (CollectionUtils.isNotEmpty(reqDto.getGoodSearchList())) {
                    for (OrderClaimSearchGoodsDto srchDto : reqDto.getGoodSearchList()) {
                        if (orderDetlMap.containsKey(srchDto.getOdOrderDetlId()))
                            srchDto.setUrWarehouseId(orderDetlMap.get(srchDto.getOdOrderDetlId()));
                        log.debug("=== 테스트 if- 받는 배송지 조회 !!!=== claimId :: <{}>, orderId<{}> srchDto<{}>", reqDto.getOdClaimId(), reqDto.getOdOrderId(), srchDto);
                    }
                }
                resultList = claimRequestMapper.getReceiveShippingInfoList(reqDto);
            } else {
                resultList = claimRequestMapper.getOrderClaimShippingInfoList(reqDto);
                log.debug("받는 배송지 조회 결과 값 ::: <{}>", CollectionUtils.isNotEmpty(resultList));
                if (!CollectionUtils.isNotEmpty(resultList)) {
                    resultList = claimRequestMapper.getReceiveShippingInfoList(reqDto);
                }
            }
        }
        else {
            resultList = claimRequestMapper.getOdClaimShopPickupInfo(reqDto);
        }

        return resultList;
    }

    /**
     * @Desc 주문클레임 신청 화면에서 사유 목록 조회
     *
     * @return
     */
    protected OrderClaimReasonResponseDto getOrderClaimReasonList(PolicyClaimMallRequestDto dto) {
        OrderClaimReasonResponseDto orderClaimReasonResponseDto = new OrderClaimReasonResponseDto();
        List<PolicyClaimMallVo> rows = claimRequestMapper.getOrderClaimReasonList(dto);
        orderClaimReasonResponseDto.setRows(rows);
        return orderClaimReasonResponseDto;
    }

























































    /**
     * @Desc 주문클레임 신청 화면에서 상품 마스터 정보 조회
     *
     * @param odOrderId
     * @return
     */
    protected OrderMasterInfoDto getOrderMasterInfo(long odOrderId) {
        return claimRequestMapper.getOrderMasterInfo(odOrderId);
    }

    /**
     * @Desc 주문 회원 PK 조회
     *
     * @param odOrderId
     * @return
     */
    protected long getOrderUrUserId(long odOrderId) {
    	return claimRequestMapper.getOrderUrUserId(odOrderId);
    }

    /**
     * @Desc 주문클레임 신청 화면에서 결제 정보 조회
     *
     * @param odOrderId
     * @return
     */
    protected OrderClaimPaymentInfoDto getOrderClaimPaymentInfo(long odOrderId) {
    	return claimRequestMapper.getOrderClaimPaymentInfo(odOrderId);
    }

    /**
     * @Desc 주문클레임 신청 화면에서 전체 상품 금액 환불 정보 조회
     *
     * @param orderClaimViewRequestDto
     * @return
     */
    protected OrderClaimPriceInfoDto getOrderClaimPriceInfo(OrderClaimViewRequestDto orderClaimViewRequestDto) {
    	return claimRequestMapper.getOrderClaimPriceInfo(orderClaimViewRequestDto);
    }

    /**
     * @Desc 주문클레임 신청 화면에서 주문클레임 마스터 상품 금액 환불 정보 조회
     *
     * @param odClaimId
     * @return
     */
    protected OrderClaimPriceInfoDto getClaimPriceInfo(long odClaimId) {
    	return claimRequestMapper.getClaimPriceInfo(odClaimId);
    }

    /**
     * 상품 수량 변경시 환불정보 조회
     *
     * @param orderClaimViewRequestDto
     * @return
     */
    protected OrderClaimPriceInfoDto getOrderClaimGoodsChangePriceInfo(OrderClaimViewRequestDto orderClaimViewRequestDto) {
    	return claimRequestMapper.getOrderClaimGoodsChangePriceInfo(orderClaimViewRequestDto);
    }

    /**
     * @Desc 환불계좌 조회
     * @param orderClaimViewRequestDto
     * @return
     */
    protected OrderClaimAccountInfoDto getOrderClaimAccountInfo(OrderClaimViewRequestDto orderClaimViewRequestDto) {
    	return claimRequestMapper.getOrderClaimAccountInfo(orderClaimViewRequestDto);
    }

    /**
     * @Desc 첨부파일 조회
     *
     * @param orderClaimViewRequestDto
     * @return
     */
    protected List<OrderClaimAttcInfoDto> getOrderClaimAttcInfoList(OrderClaimViewRequestDto orderClaimViewRequestDto) {
    	return claimRequestMapper.getOrderClaimAttcInfoList(orderClaimViewRequestDto);
    }

    /**
     * @Desc 반품을 신청을 할 때 등록된 보내는 배송지 조회
     *
     * @param orderClaimViewRequestDto
     * @return
     */
    protected OrderClaimSendShippingInfoDto getOrderClaimSendShippingInfo(OrderClaimViewRequestDto orderClaimViewRequestDto) {
    	return claimRequestMapper.getOrderClaimSendShippingInfo(orderClaimViewRequestDto);
    }

    /**
     * @Desc 반품을 신청을 할 때 보내는 배송지 조회
     *
     * @param orderClaimViewRequestDto
     * @return
     */
    protected OrderClaimSendShippingInfoDto getSendShippingInfo(OrderClaimViewRequestDto orderClaimViewRequestDto) {
    	return claimRequestMapper.getSendShippingInfo(orderClaimViewRequestDto);
    }

    /**
     * 주문 클레임 주문 상품 결제 정보 조회
     * @param odOrderId
     * @param odOrderDetlId
     * @return
     */
    protected OrderClaimReShippingPaymentInfoDto getOrderClaimGoodsPaymentInfo(long odOrderId, long odOrderDetlId, int claimCnt) {
    	return claimRequestMapper.getOrderClaimGoodsPaymentInfo(odOrderId, odOrderDetlId, claimCnt);
    }

    /**
     * 주문 클레임 대상 상품 전체 조회
     * @param claimViewReqDto
     * @return
     */
    protected List<OrderClaimTargetGoodsListDto> getOrderClaimTargetGoodsList(OrderClaimViewRequestDto claimViewReqDto) {

        log.debug("클레임 대상 상품 전체 조회 ----- > <{}>", claimViewReqDto.getOdClaimId());
        if(claimViewReqDto.getOdClaimId() > 0 && !ClaimEnums.ClaimFrontTp.FRONT.getCode().equals(StringUtil.nvl(String.valueOf(claimViewReqDto.getFrontTp())))) {
            return claimRequestMapper.getOrderClaimTargetClaimGoodsList(claimViewReqDto);
        }
        else {
    	    return claimRequestMapper.getOrderClaimTargetGoodsList(claimViewReqDto);
        }
    }

    /**
     * 주문 클레임 녹즙 대상 상품 전체 조회
     * @param claimViewReqDto
     * @return
     */
    protected List<OrderClaimTargetGoodsListDto> getOrderClaimTargetGreenJuiceGoodsList(OrderClaimViewRequestDto claimViewReqDto) {

        log.debug("주문 클레임 녹즙 대상 상품 전체 조회 ----- > <{}>", claimViewReqDto.getOdClaimId());
        return claimRequestMapper.getOrderClaimTargetGreenJuiceGoodsList(claimViewReqDto);
    }

    /**
     * 녹즙 클레임 반품 스케쥴 배송정보 조회
     * @param claimViewReqDto
     * @return
     */
    protected List<OrderClaimGoodsInfoDto> getOrderGreenJuiceClaimReturnsScheduleView(OrderClaimViewRequestDto claimViewReqDto) {

        log.debug("녹즙 클레임 반품 스케쥴 배송정보 조회 ----- > <{}>", claimViewReqDto.getOdClaimId());
        return claimRequestMapper.getOrderGreenJuiceClaimReturnsScheduleView(claimViewReqDto);
    }

    /**
     * 주문 녹즙 재배송 상품 도착예정일 조회
     * @param claimViewReqDto
     * @return
     */
    protected OrderClaimGoodsExScheduleListDto getOrderGreenJuiceClaimExchangeScheduleView(OrderClaimViewRequestDto claimViewReqDto) throws Exception {

        log.debug("주문 녹즙 재배송 상품 도착예정일 조회 ----- > <{}>", claimViewReqDto.getOdClaimId());
        List<OrderClaimSearchGoodsDto> goodSearchList = claimViewReqDto.getGoodSearchList();

        // 도착 예정일 응답 정보 초기화
        OrderClaimGoodsExScheduleListDto orderClaimGoodsExScheduleListDto = new OrderClaimGoodsExScheduleListDto();
        List<OrderClaimGoodsExScheduleInfoDto> arrivalDtList = new ArrayList<>();

        if(!ObjectUtils.isEmpty(goodSearchList)) {

            for(OrderClaimSearchGoodsDto orderClaimSearchGoodsInfo : goodSearchList) {

                OrderDetailScheduleDeliverableListResponseDto orderDetailScheduleDeliverableListResponseDto =
                        (OrderDetailScheduleDeliverableListResponseDto) mallOrderScheduleBiz.getOrderDeliverableScheduleList(orderClaimSearchGoodsInfo.getOdOrderDetlId()).getData();

                List<String> scheduleDelvDateList = orderDetailScheduleDeliverableListResponseDto.getScheduleDelvDateList();

                List<LocalDate> resultList = new ArrayList<>();

                for(String scheduleDelvDate : scheduleDelvDateList) {

                    LocalDate orderDate = LocalDate.parse(scheduleDelvDate);

                    resultList.add(orderDate);

                }

                /*
                // 개별 상품별(주문상세 상품별) 도착예정일 리스트 조회
                List<ArrivalScheduledDateDto> arrivalScheduledDatelist = goodsGoodsBiz.getArrivalScheduledDateDtoList(
                                                                                                                        orderClaimSearchGoodsInfo.getUrWarehouseId(),     // 출고처ID(출고처PK)
                                                                                                                        orderClaimSearchGoodsInfo.getIlGoodsId(),         // 상품ID(상품PK)
                                                                                                                        GoodsEnums.GoodsDeliveryType.DAWN.getCode().equals(orderClaimSearchGoodsInfo.getGoodsDeliveryType()), // 새벽배송여부 (true/false)
                                                                                                                        orderClaimSearchGoodsInfo.getClaimCnt(),          // 주문수량
                                                                                                                        orderClaimSearchGoodsInfo.getGoodsCycleTp()       // 일일 배송주기코드
                                                                                                                    );

                // 출고 지시일 리스트 요일별로 처리
//                if (StringUtil.isNotEmpty(mallArriveDateRequestDto.getGoodsDailyCycleType())) {
//                    String weekCode = "";
//                    if (StringUtil.isNotEmpty(mallArriveDateRequestDto.getWeekCode())) {
//                        weekCode = DeliveryEnums.WeekType.findByCodeName(mallArriveDateRequestDto.getWeekCode()).getCode();
//                    }
                arrivalScheduledDatelist = goodsGoodsBiz.getArrivalScheduledDateDtoListByWeekCode(arrivalScheduledDatelist, orderClaimSearchGoodsInfo.getUrWarehouseId(), orderClaimSearchGoodsInfo.getGoodsCycleTp(), orderClaimSearchGoodsInfo.getWeekDayCd());
//                }

                // 도착 예정일 정보는 5개 까지만 노출
                List<LocalDate> resultList = arrivalScheduledDatelist.stream().map(x -> x.getArrivalScheduledDate()).collect(Collectors.toList());
                //List<ArrivalScheduledDateDto> resultList = new ArrayList<>();
//                for (int i = 0; i < arrivalScheduledDatelist.size(); i++) {
//                    ArrivalScheduledDateDto dto = arrivalScheduledDatelist.get(i);
//                    resultList.add(dto.getArrivalScheduledDate());
                    //resultList.add(dto);
                    //if (i == 4) break;
//                }

                //List<List<ArrivalScheduledDateDto>> arrivalScheduledDateDtoList= new ArrayList<>();
                //arrivalScheduledDateDtoList.add(resultList);
                //List<LocalDate> dateList = goodsGoodsBiz.intersectionArrivalScheduledDateListByDto(arrivalScheduledDateDtoList);
                 */

                OrderClaimGoodsExScheduleInfoDto orderClaimGoodsExScheduleInfoDto = new OrderClaimGoodsExScheduleInfoDto();
                orderClaimGoodsExScheduleInfoDto.setOdOrderDetlId(orderClaimSearchGoodsInfo.getOdOrderDetlId());
                orderClaimGoodsExScheduleInfoDto.setDateList(resultList);
                arrivalDtList.add(orderClaimGoodsExScheduleInfoDto);
            }
        }

        orderClaimGoodsExScheduleListDto.setArrivalDtList(arrivalDtList);
        return orderClaimGoodsExScheduleListDto;
    }

    /**
     * 주문클레임 마스터 조회
     * @param orderClaimViewRequestDto
     * @return OrderClaimMasterInfoDto
     */
    protected OrderClaimMasterInfoDto getOrderClaimMasterInfo(OrderClaimViewRequestDto orderClaimViewRequestDto) {
        return claimRequestMapper.getOrderClaimMasterInfo(orderClaimViewRequestDto);
    }


    /**
     * 클레임 환불 정보 조회
     * @param odClaimId
     * @return
     */
    protected OrderClaimPriceInfoDto getClaimRefundInfo(long odClaimId) {
        return claimRequestMapper.getClaimRefundInfo(odClaimId);
    }

    /**
     * 자기 주문 건 확인
     * @param orderClaimViewRequestDto
     * @return
     */
    protected int getSelfOrderCnt(OrderClaimViewRequestDto orderClaimViewRequestDto) {
        return claimRequestMapper.getSelfOrderCnt(orderClaimViewRequestDto);
    }

    /**
     * 클레임정보 클레임번호에 의한 BOS클레임사유 조회
     * @param orderClaimViewRequestDto
     * @return
     */
    protected List<OrderClaimGoodsInfoDto> getBosCalimReasonGoodsInfoList(OrderClaimViewRequestDto orderClaimViewRequestDto) throws Exception {
        return claimRequestMapper.getBosCalimReasonGoodsInfoList(orderClaimViewRequestDto);
    }

    /**
     * orderClaimRegisterRequestDto 정보에서 주문 관련 사용자 정보 Set
     * @param orderClaimRegisterRequestDto
     * @return
     */
    protected OrderClaimViewRequestDto setUserInfo(OrderClaimRegisterRequestDto orderClaimRegisterRequestDto) {
        OrderClaimViewRequestDto orderClaimViewRequestDto = new OrderClaimViewRequestDto();
        orderClaimViewRequestDto.setOdOrderId(orderClaimRegisterRequestDto.getOdOrderId());	// 주문번호
        orderClaimViewRequestDto.setUrUserId(orderClaimRegisterRequestDto.getUrUserId());	// 사용자PK
        orderClaimViewRequestDto.setGuestCi(orderClaimRegisterRequestDto.getGuestCi());		// 비회원CI
        return orderClaimViewRequestDto;
    }

    /**
     * 클레임 등록 파라미터 Set
     * @param mallOrderClaimAddPaymentRequest
     * @return
     */
    protected OrderClaimRegisterRequestDto setOrderClaimRegisterRequest(MallOrderClaimAddPaymentRequest mallOrderClaimAddPaymentRequest) {

        OrderClaimRegisterRequestDto orderClaimRegisterRequestDto = new OrderClaimRegisterRequestDto();
        orderClaimRegisterRequestDto.setOdOrderId(mallOrderClaimAddPaymentRequest.getOdOrderId());
        orderClaimRegisterRequestDto.setOdClaimId(mallOrderClaimAddPaymentRequest.getOdClaimId());
        orderClaimRegisterRequestDto.setLoginId(mallOrderClaimAddPaymentRequest.getLoginId());
        orderClaimRegisterRequestDto.setUrUserId(mallOrderClaimAddPaymentRequest.getUrUserId());
        orderClaimRegisterRequestDto.setCustomUrUserId(mallOrderClaimAddPaymentRequest.getCustomUrUserId());
        orderClaimRegisterRequestDto.setGuestCi(mallOrderClaimAddPaymentRequest.getGuestCi());

        // 결제정보 Set
        OrderClaimAddPaymentDto addPaymentInfo = new OrderClaimAddPaymentDto();
        addPaymentInfo.setPsPayCd(mallOrderClaimAddPaymentRequest.getPsPayCd());
        addPaymentInfo.setCardCode(mallOrderClaimAddPaymentRequest.getCardCode());
        addPaymentInfo.setInstallmentPeriod(mallOrderClaimAddPaymentRequest.getInstallmentPeriod());
        orderClaimRegisterRequestDto.setAddPaymentInfo(addPaymentInfo);

        return orderClaimRegisterRequestDto;
    }

    /**
     * 유효성체크 및 추가결제 정보 조회
     * @param orderClaimRegisterRequestDto
     * @return
     */
    protected MallOrderClaimAddPaymentValidResult validOrderClaimShippingPrice(OrderClaimRegisterRequestDto orderClaimRegisterRequestDto) {
        MallOrderClaimAddPaymentValidResult result = new MallOrderClaimAddPaymentValidResult();
        result.setValidResult(OrderClaimEnums.AddPaymentShippingPriceError.SUCCESS);

        OrderClaimViewRequestDto orderClaimViewRequestDto = this.setUserInfo(orderClaimRegisterRequestDto);

        log.debug("----------------- 1. 본인 주문 확인 -----------------");
        // 1. 자기 주문건 확인
        int selfOrderCnt = this.getSelfOrderCnt(orderClaimViewRequestDto);
        // 본인 주문이 아닐경우 오류 return
        if(selfOrderCnt < 1) {
            result.setValidResult(OrderClaimEnums.AddPaymentShippingPriceError.VALUE_EMPTY);
            return result;
        }

        log.debug("----------------- 2. 클레임 정보 및 추가 결제 금액 대상 조회 -----------------");
        // 2. 클레임PK로 직접 결제 여부 및 추가 결제 대상 배송비 조회
        MallOrderClaimAddPaymentResult mallOrderClaimAddPaymentResult = this.getOrderClaimAddShippingPrice(orderClaimRegisterRequestDto);
        // 직접 결제 여부가 Y가 아닐 경우 오류
        if(!OrderClaimEnums.AllTypeYn.ALL_TYPE_Y.getCode().equals(mallOrderClaimAddPaymentResult.getDirectPaymentYn())) {
            result.setValidResult(OrderClaimEnums.AddPaymentShippingPriceError.DIRECT_PAYMENT_YN_FAIL);
            return result;
        }
        result.setAddShippingInfo(mallOrderClaimAddPaymentResult);

        return result;
    }

    /**
     * 추가 배송비 결제 금액 조회
     * @param orderClaimRegisterRequestDto
     * @return
     */
    protected MallOrderClaimAddPaymentResult getOrderClaimAddShippingPrice(OrderClaimRegisterRequestDto orderClaimRegisterRequestDto) {
        return claimRequestMapper.getOrderClaimAddShippingPrice(orderClaimRegisterRequestDto);
    }

    /**
     * 취소 / 반품 추가 배송비 정보 조회
     * @param odClaimId
     * @return
     */
    protected List<OrderClaimAddShippingPaymentInfoDto> getOrderReturnsShippingPrice(long odClaimId) {
        return claimRequestMapper.getOrderReturnsShippingPrice(odClaimId);
    }

    /**
     * 클레임 신청 상품 정보로 클레임 귀책 구분 조회
     * @param orderClaimGoodsInfoDto
     * @return
     */
    protected String getOdClaimTargetTpByOrderClaimGoodsInfo(OrderClaimGoodsInfoDto orderClaimGoodsInfoDto) {
        return claimRequestMapper.getOdClaimTargetTpByOrderClaimGoodsInfo(orderClaimGoodsInfoDto);
    }


    /**
     * 녹즙 상품정보 조회
     * @param claimViewReqDto
     * @return
     * @throws Exception
     */
    protected List<OrderClaimGoodsInfoDto> getGreenJuiceGoodInfoList(OrderClaimViewRequestDto claimViewReqDto) throws Exception {

        log.debug("---------------------------- 녹즙 상품정보 조회 -----------------------------");

        // 녹즙 배송 완료 여부는 기본 N
        String deliveryYn = OrderClaimEnums.AllTypeYn.ALL_TYPE_N.getCode();

        // 녹즙 클레임 구분이 반품일 경우 배송완료 여부는 Y
        if(OrderClaimEnums.GreenJuiceClaimType.CLAIM_TYPE_RETURN.getCode().equals(claimViewReqDto.getClaimType())) {
            deliveryYn = OrderClaimEnums.AllTypeYn.ALL_TYPE_Y.getCode();
        }
        claimViewReqDto.setDeliveryYn(deliveryYn);
        claimViewReqDto.setDbStatusCheckYn(OrderClaimEnums.AllTypeYn.ALL_TYPE_N.getCode());

        //상품정보
        List<OrderClaimGoodsInfoDto> goodsList = claimRequestMapper.getClaimGreenJuiceGoodsInfoList(claimViewReqDto);

        return goodsList;
    }

    /**
     * 녹즙 환불 정보 조회
     * @param claimViewReqDto
     * @param goodsList
     * @return
     * @throws Exception
     */
    protected OrderClaimPriceInfoDto getGreenJuiceRefundPriceInfo(OrderClaimViewRequestDto claimViewReqDto, List<OrderClaimGoodsInfoDto> goodsList) throws Exception {

        log.debug("---------------------------- 녹즙 환불정보 조회 -----------------------------");
        //결제정보 구하기
        OrderClaimPaymentInfoDto paymentInfoDto = claimRequestMapper.getOrderClaimPaymentInfo(claimViewReqDto.getOdOrderId());

        //녹즙 주문건 전체 상품 조회
        List<OrderClaimTargetGoodsListDto> targetGreenJuiceGoodsList = claimRequestMapper.getOrderClaimTargetGreenJuiceGoodsList(claimViewReqDto);
        OrderClaimPriceInfoDto refundInfoDto = claimUtilPriceService.getRefundInfo(claimViewReqDto, goodsList, paymentInfoDto, targetGreenJuiceGoodsList);
        log.debug("환불금액 :: <{}>, 배송비 :: <{}>, 나머지 잔액 :: <{}>", refundInfoDto.getRefundPrice(), refundInfoDto.getShippingPrice(), refundInfoDto.getRemainPaymentPrice());

        return refundInfoDto;
    }

    /**
     * 녹즙 스케쥴 정보 Set
     * @param requestDto
     * @throws Exception
     */
    protected void setOrderClaimGreenJuiceScheduleList(OrderClaimRegisterRequestDto requestDto, OrderClaimViewRequestDto claimViewReqDto) throws Exception {
        List<OrderClaimGoodsScheduleInfoDto> goodSchList = null;
        // 녹즙 클레임 구분이 반품일 경우 스케쥴 정보 Set
        if(OrderClaimEnums.GreenJuiceClaimType.CLAIM_TYPE_RETURN.getCode().equals(requestDto.getClaimType())) {
            goodSchList = requestDto.getGoodSchList();
            // 스케쥴 정보가 비어있을 경우 오류 처리
            if(org.apache.commons.lang3.ObjectUtils.isEmpty(goodSchList)) {
                throw new BaseException(OrderClaimEnums.GreenJuiceRegistError.GOODS_SCHEDULE_NONE);	// 취소 가능 스케쥴 정보 미존재
            }
        }
        // 녹즙 클레임 구분이 취소 일 경우 스케쥴 정보 조회하여 Set
        else {
            goodSchList = new ArrayList<>();
            claimViewReqDto.setDeliveryYn(OrderClaimEnums.AllTypeYn.ALL_TYPE_N.getCode());
            List<OrderClaimGoodsInfoDto> greenjuiceScheduleInfoList = claimRequestMapper.getOrderGreenJuiceClaimReturnsScheduleView(claimViewReqDto);
            if(org.apache.commons.lang3.ObjectUtils.isEmpty(greenjuiceScheduleInfoList)) {
                throw new BaseException(OrderClaimEnums.GreenJuiceRegistError.GOODS_SCHEDULE_NONE);	// 취소 가능 스케쥴 정보 미존재
            }
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            // 녹즙 스케쥴 정보 얻어와서 스케쥴 리스트로 Set
            for(OrderClaimGoodsInfoDto greenjuiceScheduleInfo : greenjuiceScheduleInfoList) {
                OrderClaimGoodsScheduleInfoDto orderClaimGoodsScheduleInfo = new OrderClaimGoodsScheduleInfoDto();
                orderClaimGoodsScheduleInfo.setOdOrderDetlDailySchId(greenjuiceScheduleInfo.getOdOrderDetlDailySchId());    // 주문 상세 일일배송 패턴 PK
                orderClaimGoodsScheduleInfo.setOdOrderDetlId(greenjuiceScheduleInfo.getOdOrderDetlId());                    // 주문 상세 PK
                orderClaimGoodsScheduleInfo.setClaimCnt(greenjuiceScheduleInfo.getClaimCnt());                              // 클레임 수량
                orderClaimGoodsScheduleInfo.setDeliveryDt(LocalDate.parse(greenjuiceScheduleInfo.getDeliveryDt(), dateFormatter));// 배송예정일
                goodSchList.add(orderClaimGoodsScheduleInfo);
            }
        }
        requestDto.setGoodSchList(goodSchList);
    }

    protected OrderClaimGoodsInfoDto getOdOrderDetlGoodsDeliveryType(long odOrderDetlId){
        return claimRequestMapper.getOdOrderDetlGoodsDeliveryType(odOrderDetlId);
    }
}


