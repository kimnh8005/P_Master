package kr.co.pulmuone.v1.order.email.service;

import kr.co.pulmuone.v1.base.dto.GetCodeListRequestDto;
import kr.co.pulmuone.v1.base.dto.GetCodeListResponseDto;
import kr.co.pulmuone.v1.base.dto.vo.GetCodeListResultVo;
import kr.co.pulmuone.v1.base.service.StComnBiz;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.DeliveryEnums;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.comm.enums.OrderEnums;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimGoodsInfoDto;
import kr.co.pulmuone.v1.order.email.dto.*;
import kr.co.pulmuone.v1.order.email.dto.vo.BosOrderInfoForEmailVo;
import kr.co.pulmuone.v1.order.email.dto.vo.OrderInfoForEmailVo;
import kr.co.pulmuone.v1.order.email.dto.vo.OrderRegularReqInfoVo;
import kr.co.pulmuone.v1.order.regular.dto.RegularReqGoodsSkipCancelResponseDto;
import kr.co.pulmuone.v1.order.regular.service.OrderRegularDetailBiz;
import net.sf.json.JSONObject;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

//@SuppressWarnings("static-access")
@Service
public class OrderEmailBizImpl implements OrderEmailBiz {

    @Value("${app.storage.public.public-storage-url}")
    private String publicStorageUrlValue; // public 저장소 접근 url

    @Autowired
    private OrderEmailService orderEmailService;

    @Autowired
    private OrderRegularDetailBiz orderRegularDetailBiz;

	@Autowired
	private StComnBiz stComnBiz;

	/**
	 * @Desc 주문PK기준 주문 상품 정보 조회
	 * @param odOrderId
	 * @return List<OrderDetailGoodsDto>
	 */
    @Override
    public List<OrderDetailGoodsDto> getOrderDetailGoodsListForEmail(Long odOrderId, List<Long> odOrderDetlIdList) throws Exception{
    	return orderEmailService.getOrderDetailGoodsListForEmail(odOrderId, odOrderDetlIdList);
    }

	/**
	 * @Desc 주문상세 PK로 주문PK, 송장번호 조회
	 * @param odOrderDetlIdList
	 * @return List<OrderDetailGoodsDto>
	 */
    @Override
    public List<OrderDetailGoodsDto> getOrderIdList(List<Long> odOrderDetlIdList) throws Exception{
    	return orderEmailService.getOrderIdList(odOrderDetlIdList);
    }

	/**
	 * 직접배송 미등록 송장 리스트
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<UnregistetedInvoiceDto> getUnregistetedInvoiceList() throws Exception {
    	return orderEmailService.getUnregistetedInvoiceList();
	}

	/**
	 * @Desc 주문 접수 완료, 주문 결제 완료 이메일 템플릿
	 * @param odOrderId
	 * @return OrderInfoForEmailDto
	 */
    @Override
    public OrderInfoForEmailResultDto getOrderInfoForEmail(Long odOrderId) throws Exception {
    	OrderInfoForEmailResultDto orderInfoForEmailResultDto = new OrderInfoForEmailResultDto();

    	// 1. 주문정보, 결제정보, 배송지정보
    	OrderInfoForEmailVo orderInfoVo = orderEmailService.getOrderInfoForEmail(odOrderId, null);
    	orderInfoForEmailResultDto.setOrderInfoVo(orderInfoVo);

    	// 2. 주문상품
    	List<OrderDetailGoodsDto> orderDetailGoodsList = getOrderDetailGoodsListForEmail(odOrderId, null);

    	if(CollectionUtils.isNotEmpty(orderDetailGoodsList)) {

			// 2-1. 증정품 상품목록
			List<OrderDetailGoodsDto> giftList = orderDetailGoodsList.stream()
    													.filter(f-> GoodsEnums.GoodsType.GIFT.getCode().equals(f.getGoodsTpCd()) || GoodsEnums.GoodsType.GIFT_FOOD_MARKETING.getCode().equals(f.getGoodsTpCd()))
    													.collect(Collectors.toList());
			orderInfoForEmailResultDto.setOrderGiftList(giftList);

			// 2-2. 증정품 제외한 상품목록
			List<OrderDetailGoodsDto> goodsList = orderDetailGoodsList.stream()
    													.filter(f-> !GoodsEnums.GoodsType.GIFT.getCode().equals(f.getGoodsTpCd()) && !GoodsEnums.GoodsType.GIFT_FOOD_MARKETING.getCode().equals(f.getGoodsTpCd()))
    													.collect(Collectors.toList());

			// 2-2. 증정품 제외한 상품만 배송정책별 상품리스트 세팅
    		List<OrderDetailShippingTypeDto> orderDetailList = setOrderDetailList(goodsList);
    		orderInfoForEmailResultDto.setOrderDetailList(orderDetailList);

    		// 3. SMS 발송위한 주문 정보 DTO 세팅
    		OrderInfoForSmsDto orderInfoForSmsDto = new OrderInfoForSmsDto();
    		if(CollectionUtils.isNotEmpty(orderDetailList)) {
    			orderInfoForSmsDto.setBuyerName(orderInfoVo.getBuyerName());
    			orderInfoForSmsDto.setGoodsNm(orderInfoVo.getGoodsNm());
    			orderInfoForSmsDto.setOdid(orderInfoVo.getOdid());
    			// 결제대기
    			String virtualAccountInfo = orderInfoVo.getBankName()+"(계좌번호 : "+orderInfoVo.getVirtualAccountNumber()+" / 예금주 : " + orderInfoVo.getPaidHolder() +")";
    			orderInfoForSmsDto.setVirtualAccountInfo(virtualAccountInfo);
    			orderInfoForSmsDto.setPaymentPrice(orderInfoVo.getPaymentPrice());
    			orderInfoForSmsDto.setPaidDueDate(orderInfoVo.getPaidDueDate());
    			// 결제완료
    			String payInfo = orderInfoVo.getPayTypeName();
    			if(OrderEnums.PaymentType.CARD.getCode().equals(orderInfoVo.getPayType())) {
    				payInfo = orderInfoVo.getInfo() +"("+ orderInfoVo.getPayTypeName() +") / "+ orderInfoVo.getCardQuota();
    			}
    			orderInfoForSmsDto.setPayInfo(payInfo);
    			orderInfoForSmsDto.setRecvAddr(orderInfoVo.getRecvAddr());
    		}
    		orderInfoForEmailResultDto.setOrderInfoForSmsDto(orderInfoForSmsDto);

    	}

        return orderInfoForEmailResultDto;
    }


    /**
	 * @Desc 상품발송 이메일 템플릿
	 * @param odOrderDetlIdList
	 * @return OrderInfoForEmailDto
	 */
    @Override
    public OrderInfoForEmailResultDto getOrderGoodsDeliveryInfoForEmail(List<Long> odOrderDetlIdList) throws Exception {
    	OrderInfoForEmailResultDto orderInfoForEmailResultDto = new OrderInfoForEmailResultDto();

    	// 1. 주문정보, 배송정보
    	// odOrderDetilIdList로 odOrderId 조회
    	List<OrderDetailGoodsDto> orderDetailGoodsDtoList = orderEmailService.getOrderIdList(odOrderDetlIdList);

    	if(CollectionUtils.isNotEmpty(orderDetailGoodsDtoList)){
			// 같은 OD_ORDER_ID의 송장번호가 같은 OD_ORDER_DETL_ID들만  들어왔다는 조건!!!!
			OrderDetailGoodsDto orderDetailDto = orderDetailGoodsDtoList.get(0);

			OrderInfoForEmailVo orderInfoVo = orderEmailService.getOrderInfoForEmail(orderDetailDto.getOdOrderId(),orderDetailDto.getGoodsTpCd());
			// 송장정보 세팅
			orderInfoVo.setShippingCompName(orderDetailDto.getShippingCompName());
			orderInfoVo.setTrackingNo(orderDetailDto.getTrackingNo());
			orderInfoVo.setTrackingUrl(orderDetailDto.getTrackingUrl());
			orderInfoVo.setDiDate(orderDetailDto.getDiDate());
			orderInfoVo.setOrderDetailGoodsList(odOrderDetlIdList);
			orderInfoForEmailResultDto.setOrderInfoVo(orderInfoVo);

			// 2. 주문상품
			List<OrderDetailGoodsDto> orderDetailGoodsList = getOrderDetailGoodsListForEmail(null, odOrderDetlIdList);

			if(CollectionUtils.isNotEmpty(orderDetailGoodsList)) {

				// 2-1. 증정품 제외한 배송정책별 상품리스트 세팅
				List<OrderDetailGoodsDto> goodsList = orderDetailGoodsList.stream()
						.filter(f-> !GoodsEnums.GoodsType.GIFT.getCode().equals(f.getGoodsTpCd()) && !GoodsEnums.GoodsType.GIFT_FOOD_MARKETING.getCode().equals(f.getGoodsTpCd()))
						.collect(Collectors.toList());
				List<OrderDetailShippingTypeDto> orderDetailList = setOrderDetailList(goodsList);
				orderInfoForEmailResultDto.setOrderDetailList(orderDetailList);

				// 3. SMS 발송위한 주문 정보 DTO 세팅
				OrderInfoForSmsDto orderInfoForSmsDto = new OrderInfoForSmsDto();
				if(CollectionUtils.isNotEmpty(orderDetailList)) {
					orderInfoForSmsDto.setBuyerName(orderInfoVo.getBuyerName());

					String goodsNm = goodsList.get(0).getGoodsNm();
					if(goodsList.size() > 1) {
						goodsNm = goodsList.get(0).getGoodsNm() + "외 " + (goodsList.size()-1) + "건";
					}
					orderInfoForSmsDto.setGoodsNm(goodsNm);
					orderInfoForSmsDto.setOdid(orderInfoVo.getOdid());
					orderInfoForSmsDto.setRecvAddr(orderInfoVo.getRecvAddr());
					orderInfoForSmsDto.setShippingCompName(orderInfoVo.getShippingCompName());
					orderInfoForSmsDto.setTrackingNo(orderInfoVo.getTrackingNo());
				}
				orderInfoForEmailResultDto.setOrderInfoForSmsDto(orderInfoForSmsDto);

			}
		}


        return orderInfoForEmailResultDto;
    }


	/**
	 * @Desc 배송정책별 주문상세 리스트 세팅
	 * @param orderDetailGoodsList
	 * @return List<OrderDetailShippingTypeDto>
	 */
    public List<OrderDetailShippingTypeDto> setOrderDetailList(List<OrderDetailGoodsDto> orderDetailGoodsList){
    	List<OrderDetailShippingTypeDto> orderDetailList = new ArrayList<>();

    	// 배송정책별로 그룹
		Map<Long, List<OrderDetailGoodsDto>> resultMap = orderDetailGoodsList.stream()
				.collect(groupingBy(OrderDetailGoodsDto::getIlShippingTmplId, LinkedHashMap::new, toList()));

		resultMap.entrySet().forEach(entry -> {
			OrderDetailShippingTypeDto orderDetailShippingTypeDto = new OrderDetailShippingTypeDto();

			//Long ilShippingTmplId = entry.getKey();
			List<OrderDetailGoodsDto> goodsList = entry.getValue();

			orderDetailShippingTypeDto.setOrderDetailGoodsDto(goodsList);

			// 배송정책별 배송비
			orderDetailShippingTypeDto.setShippingPrice(goodsList.get(0).getShippingPrice());

			// 배송정책별 결제예정금액(배송비 + 총 상품금액)
			int paidPrice = goodsList.get(0).getShippingPrice();
			for(OrderDetailGoodsDto goodsDto : goodsList) {
				paidPrice += goodsDto.getPaidPrice();

				// 일일상품일 경우 배송요일 세팅
				if(goodsDto.getGoodsTpCd().equals(GoodsEnums.GoodsType.DAILY.getCode())) {
			 		String goodsDailyCycleTermDays = (goodsDto.getMonCnt() > 0 ? DeliveryEnums.WeekType.MON.getCodeName() + "/" : "") +
			 				(goodsDto.getTueCnt() > 0 ? DeliveryEnums.WeekType.TUE.getCodeName() + "/" : "") +
			 				(goodsDto.getWedCnt() > 0 ? DeliveryEnums.WeekType.WED.getCodeName() + "/" : "") +
			 				(goodsDto.getThuCnt() > 0 ? DeliveryEnums.WeekType.THU.getCodeName() + "/" : "") +
			 				(goodsDto.getFriCnt() > 0 ? DeliveryEnums.WeekType.FRI.getCodeName(): "");
			 		goodsDailyCycleTermDays = goodsDailyCycleTermDays.endsWith("/") ? goodsDailyCycleTermDays.substring(0, goodsDailyCycleTermDays.length()-1) : goodsDailyCycleTermDays;
			 		goodsDto.setGoodsDailyCycleTermDays(goodsDailyCycleTermDays);
				}

				// 상품이미지 도메인 연결
				String goodsImg = publicStorageUrlValue + goodsDto.getGoodsImgNm();
				goodsDto.setGoodsImgNm(goodsImg);

			}
			orderDetailShippingTypeDto.setPaymentPrice(paidPrice);

			orderDetailList.add(orderDetailShippingTypeDto);
		});

    	return orderDetailList;
    }

	/**
	 * @Desc 정기배송 전용 배송정책별 주문상세 리스트 세팅
	 * @param orderDetailGoodsList
	 * @param regularBasicDiscountRate
	 * @return List<OrderDetailShippingTypeDto>
	 */
    public List<OrderDetailShippingTypeDto> setOrderDetailListForRegular(List<OrderDetailGoodsDto> orderDetailGoodsList, int regularBasicDiscountRate){
    	List<OrderDetailShippingTypeDto> orderDetailList = new ArrayList<>();

    	// 배송정책별로 그룹
		Map<Long, List<OrderDetailGoodsDto>> resultMap = orderDetailGoodsList.stream()
				.collect(groupingBy(OrderDetailGoodsDto::getIlShippingTmplId, LinkedHashMap::new, toList()));

		resultMap.entrySet().forEach(entry -> {
			OrderDetailShippingTypeDto orderDetailShippingTypeDto = new OrderDetailShippingTypeDto();

			//Long ilShippingTmplId = entry.getKey();
			List<OrderDetailGoodsDto> goodsList = entry.getValue();

			orderDetailShippingTypeDto.setOrderDetailGoodsDto(goodsList);

			// 배송정책별 배송비
			int shippingPrice = goodsList.get(0).getShippingPrice();
			orderDetailShippingTypeDto.setRegularShippingPrice(shippingPrice);

			int totalGoodsPrice = 0; // 총 상품금액
			for(OrderDetailGoodsDto goodsDto : goodsList) {
				totalGoodsPrice += goodsDto.getSalePriceByRegular();

				// 상품이미지 도메인 연결
				String goodsImg = publicStorageUrlValue + goodsDto.getGoodsImgNm();
				goodsDto.setGoodsImgNm(goodsImg);
			}

			// 배송정책별 결제예정금액(배송비 + 총 상품금액 * (100 - 정기배송 기본 할인율))
			int paidPrice = totalGoodsPrice * (100-regularBasicDiscountRate)/100;
			orderDetailShippingTypeDto.setRegularPaymentPrice(paidPrice);
			orderDetailList.add(orderDetailShippingTypeDto);
		});

    	return orderDetailList;
    }

    /**
	 * @Desc 주문 취소,반품 완료 이메일 템플릿
	 * @param odClaimId
	 * @param odOrderDetlIdList
	 * @return OrderInfoForEmailDto
	 */
    public OrderInfoForEmailResultDto getOrderClaimCompleteInfoForEmail(Long odClaimId, List<Long> odOrderDetlIdList) throws Exception{
    	OrderInfoForEmailResultDto orderInfoForEmailResultDto = new OrderInfoForEmailResultDto();

    	// 1. 환불정보
    	OrderInfoForEmailVo orderInfoVo = orderEmailService.getOrderClaimInfoForEmail(odClaimId);
    	orderInfoForEmailResultDto.setOrderInfoVo(orderInfoVo);

    	// 2. 환불상품
    	// 주문PK로 주문상세PK 리스트 조회
    	//List<Long> odOrderDetlIdList = orderEmailService.getClaimDetlIdList(odClaimId);
    	List<OrderDetailGoodsDto> orderDetailGoodsList = getOrderDetailGoodsListForEmail(null, odOrderDetlIdList);

    	if(CollectionUtils.isNotEmpty(orderDetailGoodsList)) {

			// 2-1. 증정품 제외한 배송정책별 상품리스트 세팅
    		List<OrderDetailGoodsDto> goodsList = orderDetailGoodsList.stream()
									                    .filter(f-> !f.getGoodsTpCd().equals(GoodsEnums.GoodsType.GIFT.getCode()) && !f.getGoodsTpCd().equals(GoodsEnums.GoodsType.GIFT_FOOD_MARKETING.getCode()))
									                    .collect(Collectors.toList());
    		List<OrderDetailShippingTypeDto> orderDetailList = setOrderDetailList(goodsList);
    		orderInfoForEmailResultDto.setOrderDetailList(orderDetailList);


    		// 3. SMS 발송위한 주문 정보 DTO 세팅
    		OrderInfoForSmsDto orderInfoForSmsDto = new OrderInfoForSmsDto();
    		if(CollectionUtils.isNotEmpty(orderDetailList)) {
    			orderInfoForSmsDto.setBuyerName(orderInfoVo.getBuyerName());

    			String goodsNm = goodsList.get(0).getGoodsNm();
    			if(goodsList.size() > 1) {
    				goodsNm = goodsList.get(0).getGoodsNm() + "외 " + (goodsList.size()-1) + "건";
    			}
    			orderInfoForSmsDto.setGoodsNm(goodsNm);
    			orderInfoForSmsDto.setOdid(orderInfoVo.getOdid());
    			orderInfoForSmsDto.setRefundPrice(orderInfoVo.getRefundPrice());
    		}
    		orderInfoForEmailResultDto.setOrderInfoForSmsDto(orderInfoForSmsDto);

    	}

        return orderInfoForEmailResultDto;
    }

    /**
	 * @Desc 주문 취소(입금 전 취소) SMS DTO
	 * @param odOrderId
	 * @return OrderInfoForEmailDto
	 */
    public OrderInfoForEmailResultDto getOrderCancelBeforeDepositInfoForEmail(Long odOrderId) throws Exception{
    	OrderInfoForEmailResultDto orderInfoForEmailResultDto = new OrderInfoForEmailResultDto();

    	// 1. 주문정보
    	OrderInfoForEmailVo orderInfoVo = orderEmailService.getOrderInfoForEmail(odOrderId,null);
    	orderInfoForEmailResultDto.setOrderInfoVo(orderInfoVo);

    	// 2. 주문상품
    	List<Long> odOrderDetlIdList = new ArrayList<>();
    	List<OrderDetailGoodsDto> orderDetailGoodsList = getOrderDetailGoodsListForEmail(odOrderId, odOrderDetlIdList);

    	if(CollectionUtils.isNotEmpty(orderDetailGoodsList)) {

			// 2-1. 증정품 상품목록
			List<OrderDetailGoodsDto> giftList = orderDetailGoodsList.stream()
    													.filter(f-> GoodsEnums.GoodsType.GIFT.getCode().equals(f.getGoodsTpCd()) || GoodsEnums.GoodsType.GIFT_FOOD_MARKETING.getCode().equals(f.getGoodsTpCd()))
    													.collect(Collectors.toList());
			orderInfoForEmailResultDto.setOrderGiftList(giftList);

			// 2-2. 증정품 제외한 배송정책별 상품리스트 세팅
			List<OrderDetailGoodsDto> goodsList = orderDetailGoodsList.stream()
									                    .filter(f-> !f.getGoodsTpCd().equals(GoodsEnums.GoodsType.GIFT.getCode()) && !f.getGoodsTpCd().equals(GoodsEnums.GoodsType.GIFT_FOOD_MARKETING.getCode()))
									                    .collect(Collectors.toList());
    		List<OrderDetailShippingTypeDto> orderDetailList = setOrderDetailList(goodsList);
    		orderInfoForEmailResultDto.setOrderDetailList(orderDetailList);

    		// 3. SMS 발송위한 주문 정보 DTO 세팅
    		OrderInfoForSmsDto orderInfoForSmsDto = new OrderInfoForSmsDto();
    		if(CollectionUtils.isNotEmpty(orderDetailList)) {
    			orderInfoForSmsDto.setBuyerName(orderInfoVo.getBuyerName());
    			orderInfoForSmsDto.setGoodsNm(orderInfoVo.getGoodsNm());
    			orderInfoForSmsDto.setOdid(orderInfoVo.getOdid());
    		}
    		orderInfoForEmailResultDto.setOrderInfoForSmsDto(orderInfoForSmsDto);

    	}

        return orderInfoForEmailResultDto;
    }

    /**
	 * @Desc 정기배송 신청 완료 이메일 템플릿
	 * @param odRegularReqId
	 * @return OrderInfoForEmailDto
	 */
    @Override
    public OrderInfoForEmailResultDto getOrderRegularApplyCompletedInfoForEmail(Long odRegularReqId, String firstOrderYn) throws Exception{
    	OrderInfoForEmailResultDto orderInfoForEmailResultDto = new OrderInfoForEmailResultDto();
    	// 결제 예정 금액
    	int paymentPrice = 0;

    	// 1. 주문정보, 결제정보, 배송지정보
    	OrderRegularReqInfoVo orderRegularReqInfoVo = orderEmailService.getOrderRegularReqInfoForEmail(odRegularReqId);

    	// 2. 주문상품
    	List<OrderDetailGoodsDto> orderDetailGoodsList = orderEmailService.getOrderRegularGoodsListForEmail(odRegularReqId);

    	if(CollectionUtils.isNotEmpty(orderDetailGoodsList)) {

			// 2-1. 증정품 상품목록
			List<OrderDetailGoodsDto> giftList = orderDetailGoodsList.stream()
    													.filter(f-> GoodsEnums.GoodsType.GIFT.getCode().equals(f.getGoodsTpCd()) || GoodsEnums.GoodsType.GIFT_FOOD_MARKETING.getCode().equals(f.getGoodsTpCd()))
    													.collect(Collectors.toList());
			orderInfoForEmailResultDto.setOrderGiftList(giftList);

			// 2-2. 증정품 제외한 배송정책별 상품리스트 세팅
			List<OrderDetailGoodsDto> goodsList = orderDetailGoodsList.stream()
									                    .filter(f-> !GoodsEnums.GoodsType.GIFT.getCode().equals(f.getGoodsTpCd()) && !GoodsEnums.GoodsType.GIFT_FOOD_MARKETING.getCode().equals(f.getGoodsTpCd()))
									                    .collect(Collectors.toList());
    		List<OrderDetailShippingTypeDto> orderDetailList = setOrderDetailListForRegular(goodsList, orderRegularReqInfoVo.getRegularBasicDiscountRate());
    		orderInfoForEmailResultDto.setOrderDetailList(orderDetailList);

    		// 3. SMS 발송위한 주문 정보 DTO 세팅
    		OrderInfoForSmsDto orderInfoForSmsDto = new OrderInfoForSmsDto();
    		if(CollectionUtils.isNotEmpty(orderDetailList)) {
    			orderInfoForSmsDto.setBuyerName(orderRegularReqInfoVo.getBuyerName());
    			String goodsNm = goodsList.get(0).getGoodsNm();
    			if(goodsList.size() > 1) {
    				goodsNm = goodsList.get(0).getGoodsNm() + "외 " + (goodsList.size()-1) + "건";
    			}
    			orderInfoForSmsDto.setGoodsNm(goodsNm);
    			orderInfoForSmsDto.setRecvAddr(orderRegularReqInfoVo.getRecvAddr());
    			String goodsCycle = "";
    			goodsCycle = orderRegularReqInfoVo.getGoodsCycleTpNm() + " (첫 도착예정일: " +orderRegularReqInfoVo.getStartArriveDt()+")";
    			// 첫주문일때
    			if("Y".equals(firstOrderYn)) {
					goodsCycle = orderRegularReqInfoVo.getGoodsCycleTpNm() + " (첫 도착예정일: " +orderRegularReqInfoVo.getStartArriveDt()+")";
    			}else {
					goodsCycle = orderRegularReqInfoVo.getGoodsCycleTpNm() + " (도착예정일: " +orderRegularReqInfoVo.getStartArriveDt()+")";
    			}

    			orderInfoForSmsDto.setGoodsCycle(goodsCycle);

    			// 결제예정금액 (배송비 + 결제예정금액)
    			for(OrderDetailShippingTypeDto shippingTypeDto : orderDetailList) {
    				paymentPrice += shippingTypeDto.getRegularPaymentPrice() + shippingTypeDto.getRegularShippingPrice();
    			}
    			orderInfoForSmsDto.setPaymentPriceByRegular(paymentPrice);
    			orderInfoForSmsDto.setPayType(orderRegularReqInfoVo.getCardName()+"(신용카드) / 일시불");

    		}
    		orderInfoForEmailResultDto.setOrderInfoForSmsDto(orderInfoForSmsDto);

        	orderRegularReqInfoVo.setFirstOrderYn(firstOrderYn);
        	orderRegularReqInfoVo.setPaymentPrice(paymentPrice);
        	orderInfoForEmailResultDto.setOrderRegularReqInfoVo(orderRegularReqInfoVo);


    	}

        return orderInfoForEmailResultDto;
    }


    /**
	 * @Desc 정기배송 주문신청 정보 이메일 템플릿
	 * @param odRegularResultId
	 * @return OrderInfoForEmailDto
	 */
	@Override
    public OrderInfoForEmailResultDto getOrderRegularInfoForEmail(Long odRegularResultId) throws Exception{
    	OrderInfoForEmailResultDto orderInfoForEmailResultDto = new OrderInfoForEmailResultDto();
    	List<OrderDetailGoodsDto> orderDetailGoodsList = new ArrayList<>();
    	// 1. 정기배송 주문신청 결과 정보
    	OrderRegularResultDto orderRegularResultDto = orderEmailService.getRegularOrderResultCreateGoodsListForEmail(odRegularResultId);

    	// 2. 상품정보 조회
    	// 2-1. OD_ORDER_ID 있을 경우 주문 상품 정보 조회
    	if(orderRegularResultDto.getOdOrderId() > 0) {

    		orderDetailGoodsList = getOrderDetailGoodsListForEmail(orderRegularResultDto.getOdOrderId(), null);
    		if(CollectionUtils.isNotEmpty(orderDetailGoodsList)) {

    			// 2-1. 증정품 상품목록
    			List<OrderDetailGoodsDto> giftList = orderDetailGoodsList.stream()
        													.filter(f-> GoodsEnums.GoodsType.GIFT.getCode().equals(f.getGoodsTpCd()) || GoodsEnums.GoodsType.GIFT_FOOD_MARKETING.getCode().equals(f.getGoodsTpCd()))
        													.collect(Collectors.toList());
    			orderInfoForEmailResultDto.setOrderGiftList(giftList);

    			// 2-2. 증정품 제외한 상품목록
    			List<OrderDetailGoodsDto> goodsList = orderDetailGoodsList.stream()
        													.filter(f-> !GoodsEnums.GoodsType.GIFT.getCode().equals(f.getGoodsTpCd()) && !GoodsEnums.GoodsType.GIFT_FOOD_MARKETING.getCode().equals(f.getGoodsTpCd()))
        													.collect(Collectors.toList());

    			// 2-2. 증정품 제외한 상품만 배송정책별 상품리스트 세팅
        		List<OrderDetailShippingTypeDto> orderDetailList = setOrderDetailList(goodsList);
        		orderInfoForEmailResultDto.setOrderDetailList(orderDetailList);
    		}
    	}
    	// 2-2. 정기배송 회차 건너뛰기일 경우 -> 회차별 상품정보 목록 조회
    	List<OrderRegularResultDto> orderRegularResultList = orderEmailService.getOrderRegularResultList(odRegularResultId);
    	orderInfoForEmailResultDto.setOrderRegularResultList(orderRegularResultList);

    	// 3. SMS 발송위한 주문 정보 DTO 세팅
    	OrderInfoForSmsDto orderInfoForSmsDto = new OrderInfoForSmsDto();
		orderInfoForSmsDto.setOdid(orderRegularResultDto.getOdid());
		orderInfoForSmsDto.setBuyerName(orderRegularResultDto.getBuyerNm());
		orderInfoForSmsDto.setPaymentPrice(orderRegularResultDto.getPaymentPrice());
		orderInfoForSmsDto.setReqRound(orderRegularResultDto.getReqRound());
		orderInfoForSmsDto.setTotalRound(orderRegularResultDto.getTotalRound());
		orderInfoForSmsDto.setGoodsNm(orderRegularResultDto.getGoodsNm());
		orderInfoForSmsDto.setCreateDate(orderRegularResultDto.getCreateDate());
		orderInfoForSmsDto.setArriveDt(orderRegularResultDto.getArriveDt());
		// 결제완료
		String payInfo = orderRegularResultDto.getPayTypeName();
		if(StringUtils.isNotEmpty(payInfo) && OrderEnums.PaymentType.CARD.getCode().equals(orderRegularResultDto.getPayType())) {
			payInfo = orderRegularResultDto.getInfo() +"("+ orderRegularResultDto.getPayTypeName() +") / "+ orderRegularResultDto.getCardQuota();
		}else {
			payInfo = orderRegularResultDto.getCardNm() + "(신용카드) / 일시불";
		}
		orderInfoForSmsDto.setPayInfo(payInfo);
		orderInfoForSmsDto.setRecvAddr(orderRegularResultDto.getRecvAddr());

		// 결제 응답 데이터 JSON 파싱
    	String jsonString = orderRegularResultDto.getResponseData();
    	String responseDate = "";
    	String res_msg = "";
    	if(StringUtils.isNotEmpty(jsonString)) {
    		JSONObject jObject = new JSONObject().fromObject(jsonString);
    		String app_time = jObject.getString("app_time");
    		res_msg = jObject.getString("res_msg");
    		responseDate = orderRegularResultDto.getCreateDate();
    		if(!app_time.equals("null") && StringUtils.isNotEmpty(app_time)) {
    			DateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
    			Date tempDate = dateFormat.parse(app_time);
    			DateFormat responseDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    			responseDate = responseDateFormat.format(tempDate);
    		}
    	}
    	responseDate = StringUtil.nvl(responseDate, LocalDate.now().toString());
    	res_msg = StringUtil.nvl(res_msg, "-");
		// EMAIL DTO 세팅
		orderRegularResultDto.setPaymentFailDate(responseDate);
		orderRegularResultDto.setPaymentFailResponseMsg(res_msg);
		// SMS DTO 세팅
		orderInfoForSmsDto.setPaymentFailDate(responseDate);
		orderInfoForSmsDto.setPaymentFailResponseMsg(res_msg);

    	orderInfoForEmailResultDto.setOrderRegularResultDto(orderRegularResultDto);
		orderInfoForEmailResultDto.setOrderInfoForSmsDto(orderInfoForSmsDto);

        return orderInfoForEmailResultDto;
    }


    /**
	 * @Desc 정기배송 주문신청결과 정보 이메일 템플릿
	 * @param odRegularResultDetlId
	 * @return OrderInfoForEmailDto
	 */
	@Override
    public OrderInfoForEmailResultDto getOrderRegularResultDetlInfoForEmail(Long odRegularResultDetlId) throws Exception{
    	OrderInfoForEmailResultDto orderInfoForEmailResultDto = new OrderInfoForEmailResultDto();

    	// 1. 정기배송 주문신청 결과 정보
    	OrderRegularResultDto orderRegularResultDto = orderEmailService.getRegularOrderResultDetlInfoForEmail(odRegularResultDetlId);

    	// 2. 잔여 결제예정금액 조회
    	RegularReqGoodsSkipCancelResponseDto regualrReqGoodsCancelDto = orderRegularDetailBiz.getOrderRegularGoodsSkipCancelInfo(odRegularResultDetlId);
    	orderRegularResultDto.setRemainPaymentPrice(regualrReqGoodsCancelDto.getPaidPrice());

    	// 3. SMS 발송위한 주문 정보 DTO 세팅
    	OrderInfoForSmsDto orderInfoForSmsDto = new OrderInfoForSmsDto();
		orderInfoForSmsDto.setBuyerName(orderRegularResultDto.getBuyerNm());
		orderInfoForSmsDto.setGoodsNm(orderRegularResultDto.getGoodsNm());
		orderInfoForSmsDto.setCreateDate(orderRegularResultDto.getCreateDate());
		orderInfoForSmsDto.setArriveDt(orderRegularResultDto.getArriveDt());

    	orderInfoForEmailResultDto.setOrderRegularResultDto(orderRegularResultDto);
		orderInfoForEmailResultDto.setOrderInfoForSmsDto(orderInfoForSmsDto);

        return orderInfoForEmailResultDto;
    }

	/**
	 * @Desc 녹즙 일일배송 종료 정보 이메일 템플릿
	 * @param odOrderDetlId
	 * @return OrderInfoForEmailDto
	 */
	@Override
    public OrderInfoForEmailResultDto getOrderDailyGreenJuiceEndInfoForEmail(Long odOrderDetlId) throws Exception{
    	OrderInfoForEmailResultDto orderInfoForEmailResultDto = new OrderInfoForEmailResultDto();

    	// 1. 주문신청 결과 정보
    	OrderInfoForEmailVo orderInfoForEmailVo = orderEmailService.getOrderDailyGreenJuiceEndInfoForEmail(odOrderDetlId);

    	// 2. SMS 발송위한 주문 정보 DTO 세팅
		if(orderInfoForEmailVo != null){
			OrderInfoForSmsDto orderInfoForSmsDto = new OrderInfoForSmsDto();
			orderInfoForSmsDto.setBuyerName(orderInfoForEmailVo.getBuyerName());
			orderInfoForSmsDto.setGoodsNm(orderInfoForEmailVo.getGoodsNm());
			orderInfoForSmsDto.setRecvAddr(orderInfoForEmailVo.getRecvAddr());

			orderInfoForEmailResultDto.setOrderInfoVo(orderInfoForEmailVo);
			orderInfoForEmailResultDto.setOrderInfoForSmsDto(orderInfoForSmsDto);
		}

        return orderInfoForEmailResultDto;
    }


    /**
	 * @Desc 정기배송 만료 SMS 대상 조회(BATCH)
	 * @Desc 전일 9시~금일9시 사이 배송완료 된 주문건 중 정기배송 마지막 회차만 조회
	 * @return List<Long>
	 */
	@Override
	public List<Long> getTargetOrderRegularExpired() throws Exception{
		return orderEmailService.getTargetOrderRegularExpired();
	}

    /**
	 * @Desc 정기배송 만료 SMS 발송 상태 업데이트 (BATCH)
	 * @Param odRegularResultId
	 * @return
	 */
	@Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
	public int putSmsSendStatusByOrderRegularExpired(Long odRegularResultId) throws Exception{
		return orderEmailService.putSmsSendStatusByOrderRegularExpired(odRegularResultId);
	}

    /**
	 * @Desc 녹즙 일일배송 종료 SMS 대상 조회(BATCH)
	 * @Desc 마지막 도착예정일 D-4인 녹즙 주문건 조회
	 * @return List<Long>
	 */
	@Override
	public List<Long> getTargetOrderDailyGreenJuiceEnd() throws Exception{
		return orderEmailService.getTargetOrderDailyGreenJuiceEnd();
	}

    /**
	 * @Desc 정기배송 상품금액변동 자동메일 발송 위한 금액변동 상품목록 조회(BATCH)
	 * @Desc 금일 상품가격 변동 된 상품 목록 조회
	 * @return List<HashMap<String,Long>>
	 */
	@Override
	public List<HashMap<String,BigInteger>> getTargetOrderRegularGoodsPriceChangeList() throws Exception{
		return orderEmailService.getTargetOrderRegularGoodsPriceChangeList();
	}

    /**
	 * @Desc 정기배송 상품금액 변동안내 자동메일  발송
	 * @param odRegularReqId
	 * @param ilGoodsId
	 * @return OrderInfoForEmailDto
	 */
    @Override
    public OrderInfoForEmailResultDto getOrderRegularGoodsPriceChangeInfoForEmail(Long odRegularReqId, Long ilGoodsId) throws Exception{
    	OrderInfoForEmailResultDto orderInfoForEmailResultDto = new OrderInfoForEmailResultDto();

    	// 1. 주문정보, 결제정보, 배송지정보
    	OrderRegularReqInfoVo orderRegularReqInfoVo = orderEmailService.getOrderRegularReqInfoForEmail(odRegularReqId);


    	// 2. 상품 정보
    	OrderRegularGoodsPriceChangeDto goodsPriceChangeDto = orderEmailService.getOrderRegularGoodsPriceChangeInfo(odRegularReqId, ilGoodsId);

    	// 상품이미지 도메인 연결
    	String goodsImg = publicStorageUrlValue + goodsPriceChangeDto.getGoodsImgNm();
    	goodsPriceChangeDto.setGoodsImgNm(goodsImg);

		// 3. SMS 발송위한 주문 정보 DTO 세팅
		OrderInfoForSmsDto orderInfoForSmsDto = new OrderInfoForSmsDto();
		orderInfoForSmsDto.setBuyerName(orderRegularReqInfoVo.getBuyerName());
		orderInfoForSmsDto.setGoodsNm(goodsPriceChangeDto.getGoodsNm());
		orderInfoForSmsDto.setBeforeSalePrice(goodsPriceChangeDto.getBeforeSalePrice());
		orderInfoForSmsDto.setAfterSalePrice(goodsPriceChangeDto.getAfterSalePrice());
		orderInfoForSmsDto.setArriveDtMonth(orderRegularReqInfoVo.getArriveDtMonth());
		orderInfoForSmsDto.setArriveDtDay(orderRegularReqInfoVo.getArriveDtDay());

		orderInfoForEmailResultDto.setOrderInfoForSmsDto(orderInfoForSmsDto);
    	orderInfoForEmailResultDto.setOrderRegularReqInfoVo(orderRegularReqInfoVo);
    	orderInfoForEmailResultDto.setOrderRegularGoodsPriceChangeDto(goodsPriceChangeDto);

        return orderInfoForEmailResultDto;
    }

	/**
	 * @Desc BOS 주문 상태 알림(BATCH) - 발송 대상 거래처 조회
	 * @return List<BosOrderStatusNotiDto>
	 */
	@Override
	public List<BosOrderStatusNotiDto> getTargetBosOrderStatusNotification() throws Exception{
		return orderEmailService.getTargetBosOrderStatusNotification();
	}

	/**
	 * @Desc BOS 주문 상태 알림 자동메일 발송(BATCH)
	 * @param urClientId
	 * @return OrderInfoForEmailDto
	 */
	@Override
	public OrderInfoForEmailResultDto getBosOrderStatusNotificationForEmail(Long urClientId) throws Exception{
		OrderInfoForEmailResultDto orderInfoForEmailResultDto = new OrderInfoForEmailResultDto();

		// 거래처별 주문 상태별 건수 조회
		BosOrderInfoForEmailVo bosOrderInfoForEmailVo = orderEmailService.getOrderCountForOrderStatusNotification(urClientId);
		orderInfoForEmailResultDto.setBosOrderInfoForEmailVo(bosOrderInfoForEmailVo);

		return orderInfoForEmailResultDto;
	}

	/**
	 * @Desc BOS 올가 식품안전팀 주의 주문 발생 확인
	 * @param odOrderId
	 * @throws Exception
	 */
	@Override
	public boolean checkBosOrgaCautionOrderNotification(Long odOrderId) throws Exception{
		return orderEmailService.checkBosOrgaCautionOrderNotification(odOrderId);
	}

	/**
	 * @Desc  부분취소 배송비 추가결제 가상계좌 발급 SMS 발송
	 * @param odPaymentMasterId
	 * @return OrderInfoForEmailDto
	 */
	@Override
	public OrderInfoForEmailResultDto getPayAdditionalShippingPriceInfoForEmail(Long odPaymentMasterId) throws Exception{
		OrderInfoForEmailResultDto orderInfoForEmailResultDto = new OrderInfoForEmailResultDto();

		// 1. 주문정보
		OrderInfoForEmailVo orderInfoVo = orderEmailService.getPayAdditionalShippingPriceInfo(odPaymentMasterId);
		orderInfoForEmailResultDto.setOrderInfoVo(orderInfoVo);

		String virtualAccountInfo = orderInfoVo.getBankName()+"(계좌번호 : "+orderInfoVo.getVirtualAccountNumber()+" / 예금주 : " + orderInfoVo.getPaidHolder() +")";

		// 2. SMS 발송위한 주문 정보 DTO 세팅
		OrderInfoForSmsDto orderInfoForSmsDto = new OrderInfoForSmsDto();
		orderInfoForSmsDto.setBuyerName(orderInfoVo.getBuyerName());
		orderInfoForSmsDto.setVirtualAccountInfo(virtualAccountInfo);
		orderInfoForSmsDto.setPaymentPrice(orderInfoVo.getPaymentPrice());
		orderInfoForSmsDto.setPaidDueDate(orderInfoVo.getPaidDueDate());
		orderInfoForEmailResultDto.setOrderInfoForSmsDto(orderInfoForSmsDto);

		return orderInfoForEmailResultDto;
	}

	/**
	 * @Desc 매장픽업 상품 준비 이메일 템플릿
	 * @param odOrderDetlIdList
	 * @return OrderInfoForEmailDto
	 */
	@Override
    public OrderInfoForEmailResultDto getOrderShopPickupGoodsDeliveryInfoForEmail(List<Long> odOrderDetlIdList) throws Exception {
    	OrderInfoForEmailResultDto orderInfoForEmailResultDto = new OrderInfoForEmailResultDto();

    	// 1. 주문정보, 배송정보
    	// odOrderDetilIdList로 odOrderId 조회
    	List<OrderDetailGoodsDto> orderDetailGoodsDtoList = orderEmailService.getOrderIdList(odOrderDetlIdList);
    	// 같은 OD_ORDER_ID의 송장번호가 같은 OD_ORDER_DETL_ID들만  들어왔다는 조건!!!!
    	OrderDetailGoodsDto orderDetailDto = orderDetailGoodsDtoList.get(0);

    	OrderInfoForEmailVo orderInfoVo = orderEmailService.getOrderShopPickupInfoForEmail(orderDetailDto.getOdOrderId(),orderDetailDto.getGoodsTpCd());
    	// 송장정보 세팅
    	orderInfoVo.setShippingCompName(orderDetailDto.getShippingCompName());
    	orderInfoVo.setTrackingNo(orderDetailDto.getTrackingNo());
    	orderInfoVo.setTrackingUrl(orderDetailDto.getTrackingUrl());
    	orderInfoVo.setDiDate(orderDetailDto.getDiDate());
    	orderInfoVo.setOrderDetailGoodsList(odOrderDetlIdList);
    	orderInfoForEmailResultDto.setOrderInfoVo(orderInfoVo);

    	// 2. 주문상품
    	List<OrderDetailGoodsDto> orderDetailGoodsList = getOrderDetailGoodsListForEmail(null, odOrderDetlIdList);

    	if(CollectionUtils.isNotEmpty(orderDetailGoodsList)) {

			// 2-1. 증정품 제외한 배송정책별 상품리스트 세팅
			List<OrderDetailGoodsDto> goodsList = orderDetailGoodsList.stream()
															.filter(f-> !GoodsEnums.GoodsType.GIFT.getCode().equals(f.getGoodsTpCd()) && !GoodsEnums.GoodsType.GIFT_FOOD_MARKETING.getCode().equals(f.getGoodsTpCd()))
															.collect(Collectors.toList());
    		List<OrderDetailShippingTypeDto> orderDetailList = setOrderDetailList(goodsList);
    		orderInfoForEmailResultDto.setOrderDetailList(orderDetailList);

    		// 3. SMS 발송위한 주문 정보 DTO 세팅
    		OrderInfoForSmsDto orderInfoForSmsDto = new OrderInfoForSmsDto();
    		if(CollectionUtils.isNotEmpty(orderDetailList)) {
    			orderInfoForSmsDto.setBuyerName(orderInfoVo.getBuyerName());

    			String goodsNm = goodsList.get(0).getGoodsNm();
    			if(goodsList.size() > 1) {
    				goodsNm = goodsList.get(0).getGoodsNm() + "외 " + (goodsList.size()-1) + "건";
    			}
    			orderInfoForSmsDto.setGoodsNm(goodsNm);
    			orderInfoForSmsDto.setOdid(orderInfoVo.getOdid());
    			orderInfoForSmsDto.setUrStoreNm(orderInfoVo.getUrStoreNm());
    			orderInfoForSmsDto.setStoreStartTime(orderInfoVo.getStoreStartTime());
    			orderInfoForSmsDto.setStoreEndTime(orderInfoVo.getStoreEndTime());
    		}
    		orderInfoForEmailResultDto.setOrderInfoForSmsDto(orderInfoForSmsDto);

    	}

        return orderInfoForEmailResultDto;
    }

	/**
	 * @Desc 정기배송 결제실패(4차) 정보 조회
	 * @param odRegularResultId
	 * @return OrderInfoForEmailDto
	 */
	@Override
	public OrderInfoForEmailResultDto getOrderRegularInfoForEmailForPaymentFail(Long odRegularResultId) throws Exception{
		OrderInfoForEmailResultDto orderInfoForEmailResultDto = new OrderInfoForEmailResultDto();

		// 1. 정기배송 정보 조회
		OrderRegularResultDto orderRegularResultDto = orderEmailService.getOrderRegularInfoForEmailForPaymentFail(odRegularResultId);

		// 2. SMS 발송위한 주문 정보 DTO 세팅
		OrderInfoForSmsDto orderInfoForSmsDto = new OrderInfoForSmsDto();
		orderInfoForSmsDto.setBuyerName(orderRegularResultDto.getBuyerNm());
		orderInfoForSmsDto.setCreateDate(orderRegularResultDto.getCreateDate());


		orderInfoForEmailResultDto.setOrderRegularResultDto(orderRegularResultDto);
		orderInfoForEmailResultDto.setOrderInfoForSmsDto(orderInfoForSmsDto);

		return orderInfoForEmailResultDto;
	}

}
