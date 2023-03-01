package kr.co.pulmuone.v1.user.warehouse.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.user.warehouse.dto.DeliveryPatternRequestDto;

public interface DeliveryPatternBiz {

	/**
	 * 배송패턴 리스트 조회
	 */
	ApiResult<?> getDeliveryPatternList(DeliveryPatternRequestDto deliveryPatternRequestDto) throws Exception;

	/**
	 * 배송패턴 상세조회
	 */
	ApiResult<?> getShippingPattern(DeliveryPatternRequestDto deliveryPatternRequestDto) throws Exception;

	/**
	 * 배송패턴 등록
	 */
	ApiResult<?> addDeliveryPattern(DeliveryPatternRequestDto deliveryPatternRequestDto) throws Exception;

	/**
	 * 배송패턴 수정
	 */
	ApiResult<?> putDeliveryPattern(DeliveryPatternRequestDto deliveryPatternRequestDto) throws Exception;



}
