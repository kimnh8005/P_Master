package kr.co.pulmuone.v1.order.delivery.service;

import kr.co.pulmuone.v1.api.cjlogistics.dto.CJLogisticsTrackingResponseDto;
import kr.co.pulmuone.v1.api.cjlogistics.dto.vo.CJLogisticsTrackingVo;
import kr.co.pulmuone.v1.api.cjlogistics.service.CJLogisticsBizImpl;
import kr.co.pulmuone.v1.api.lotteglogis.service.LotteGlogisBizImpl;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.OrderClaimEnums;
import kr.co.pulmuone.v1.comm.enums.OrderEnums.LogisticsCd;
import kr.co.pulmuone.v1.api.lotteglogis.dto.LotteGlogisTrackingResponseDto;
import kr.co.pulmuone.v1.api.lotteglogis.dto.vo.LotteGlogisTrackingVo;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.order.delivery.dto.OrderDeliveryTrackingDto;
import kr.co.pulmuone.v1.order.delivery.dto.OrderDeliveryTrackingRequestDto;
import kr.co.pulmuone.v1.order.delivery.dto.OrderDeliveryTrackingResponseDto;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 생성 관련 Implements
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 15.            이명수         최초작성
 * =======================================================================
 * </PRE>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderDeliveryTrackingBizImpl implements OrderDeliveryTrackingBiz {

    @Autowired
    private CJLogisticsBizImpl cJLogisticsBizImpl;

    @Autowired
    private LotteGlogisBizImpl lotteGlogisBizImpl;

    @Override
    public ApiResult<?> getDeliveryTrackingList(OrderDeliveryTrackingRequestDto orderDeliveryTrackingRequestDto) throws Exception {
    	OrderDeliveryTrackingResponseDto orderDeliveryTrackingResponseDto = null;
    	String logisticCd = orderDeliveryTrackingRequestDto.getLogisticsCd();
    	log.debug("logisticCd 1 ::: <{}>", logisticCd);
    	if (!StringUtil.isNotEmpty(logisticCd)) {
    		logisticCd = "CJ";
    	}
    	log.debug("orderDeliveryTrackingRequestDto 2 ::: <{}>", orderDeliveryTrackingRequestDto);
    	if (LogisticsCd.CJ.getLogisticsCode().equals(logisticCd)) {
    		CJLogisticsTrackingResponseDto cJLogisticsTrackingDto = cJLogisticsBizImpl.getCJLogisticsTrackingList(orderDeliveryTrackingRequestDto.getTrackingNo());
    		orderDeliveryTrackingResponseDto = getDeliveryResponseList(cJLogisticsTrackingDto);
    	}
    	else {
    		LotteGlogisTrackingResponseDto lotteGlogisTrackingDto = lotteGlogisBizImpl.getLotteGlogisTrackingList(orderDeliveryTrackingRequestDto.getTrackingNo(), StringUtil.nvl(orderDeliveryTrackingRequestDto.getReturnsYn(), OrderClaimEnums.AllTypeYn.ALL_TYPE_N.getCode()));
    		log.debug("롯데 getResponseCode <{}>", lotteGlogisTrackingDto.getResponseCode());
    		log.debug("롯데 getResponseMessage <{}>", lotteGlogisTrackingDto.getResponseMessage());
    		log.debug("롯데 getTracking <{}>", lotteGlogisTrackingDto.getTracking());
    		orderDeliveryTrackingResponseDto = getDeliveryResponseList(lotteGlogisTrackingDto);
    	}

    	if(CollectionUtils.isNotEmpty(orderDeliveryTrackingResponseDto.getTracking())){
			Collections.reverse(orderDeliveryTrackingResponseDto.getTracking());
		}

        return ApiResult.success(orderDeliveryTrackingResponseDto);
    }

    public OrderDeliveryTrackingResponseDto getDeliveryResponseList(Object obj) throws Exception {
    	OrderDeliveryTrackingResponseDto orderDeliveryTrackingResponseDto = null;

    	String className = obj.getClass().getName();

    	List<OrderDeliveryTrackingDto> trackingList = new ArrayList<>();

    	if (className.contains("LotteGlogisTrackingResponseDto")) {
    		LotteGlogisTrackingResponseDto lotteGlogisTrackingDto = (LotteGlogisTrackingResponseDto)obj;
    		if (lotteGlogisTrackingDto.getTracking() != null) {
	    		for (LotteGlogisTrackingVo lotteVo : lotteGlogisTrackingDto.getTracking()) {
	    			OrderDeliveryTrackingDto orderDelivery = OrderDeliveryTrackingDto.builder()
	    				    .trackingStatusCode(lotteVo.getTrackingStatusCode())	//화물상태코드
	    				    .trackingStatusName(lotteVo.getTrackingStatusName())	//화물상태명
	    					.scanDate(lotteVo.getScanDate())	//처리일자
	    				    .scanTime(lotteVo.getScanTime())	//처리시간
	    				    .processingShopCode(lotteVo.getProcessingShopCode())	//처리점소코드
	    				    .processingShopName(lotteVo.getProcessingShopName())	//처리점소명
	    				    .processingShopTelephone(lotteVo.getProcessingEmployeeTelephone())	//처리점소전화번호
	    				    .partnerShopCode(lotteVo.getPartnerShopCode())	//상대점소코드
	    				    .partnerShopName(lotteVo.getPartnerShopName())	//상대점소명
	    				    .partnerShopTelephone(lotteVo.getPartnerShopTelephone())	//상대점소전화번호
	    				    .processingEmployeeNumber(lotteVo.getProcessingEmployeeNumber())	//처리사원번호
	    				    .processingEmployeeName(lotteVo.getProcessingEmployeeName())	//처리사원명
	    				    .processingEmployeeTelephone(lotteVo.getProcessingEmployeeTelephone())	// 처리사원전화번호
	    				    .build();
	    			trackingList.add(orderDelivery);
	    		}
    		}
    		orderDeliveryTrackingResponseDto = OrderDeliveryTrackingResponseDto.builder()
    				.responseCode(lotteGlogisTrackingDto.getResponseCode())
    				.responseMessage(lotteGlogisTrackingDto.getResponseMessage())
    				.tracking(trackingList)
    				.build();
    	} else if (className.contains("CJLogisticsTrackingResponseDto")) {
	    	CJLogisticsTrackingResponseDto cJLogisticsTrackingDto = (CJLogisticsTrackingResponseDto)obj;
	    	if (cJLogisticsTrackingDto.getMaster() != null && !cJLogisticsTrackingDto.getMaster().getTracking().isEmpty()) {
	    		for (CJLogisticsTrackingVo cjVo : cJLogisticsTrackingDto.getMaster().getTracking()) {
	    			OrderDeliveryTrackingDto orderDelivery = OrderDeliveryTrackingDto.builder()
	    					.shopName(cjVo.getShopName())				//점소명
	    					.scanTypeName(cjVo.getScanTypeName())		//스캔구분값
	    					.trackingStatusName(cjVo.getScanTypeName())	//화물상태명
	    					.scanDate(cjVo.getScanDate())				//스캔일자
	    					.scanTime(cjVo.getScanTime())				//스캔시간
	    					.telephone(cjVo.getTelephone())				//연락처
	    				    .build();
	    			trackingList.add(orderDelivery);
	    		}
	    	}
    		orderDeliveryTrackingResponseDto = OrderDeliveryTrackingResponseDto.builder()
    				.responseCode(cJLogisticsTrackingDto.getResponseCode())
    				.responseMessage(cJLogisticsTrackingDto.getResponseMessage())
    				.tracking(trackingList)
    				.build();
    	}
    	log.debug("result <{}>", orderDeliveryTrackingResponseDto);
    	return orderDeliveryTrackingResponseDto;

    }
}
