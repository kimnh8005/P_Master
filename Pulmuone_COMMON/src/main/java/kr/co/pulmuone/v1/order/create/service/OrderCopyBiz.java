package kr.co.pulmuone.v1.order.create.service;


import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.order.create.dto.OrderCopyDetailInfoRequestDto;
import kr.co.pulmuone.v1.order.create.dto.OrderCopySaveRequestDto;
import kr.co.pulmuone.v1.order.create.dto.OrderCopyValidationDto;

import java.util.List;

/**
*
* <PRE>
* Forbiz Korea
* 주문 복사 관련 I/F
* </PRE>
*
* <PRE>
* <B>History:</B>
 * =======================================================================
 *  버전  :	작성일                		:	작성자      :	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 02. 24.		이규한	최초작성
 * =======================================================================
* </PRE>
*/

public interface OrderCopyBiz {
	/** 주문복사 주문 상세내역 조회 */
	ApiResult<?> getOrderCopyDetailInfo(OrderCopyDetailInfoRequestDto orderCopyDetailInfoRequestDto) throws Exception;
	/** 주문복사 주문 수량 변경 조회 */
	ApiResult<?> getOrderCopyCntChangeInfo(OrderCopyDetailInfoRequestDto orderCopyDetailInfoRequestDto) throws Exception;
	/** 주문복사 주문 상세내역 복사 */
	ApiResult<?> addOrderCopy(OrderCopySaveRequestDto reqDto, long userId) throws Exception;
	/** 주문복사 저장 */
	ApiResult<?> saveOrderCopyDetailInfo(OrderCopySaveRequestDto orderCopySaveRequestDto, long userId) throws Exception;
	/** 비인증 카드 결제 */
	ApiResult<?> addNonCardPayment(OrderCopySaveRequestDto orderCopySaveRequestDto, long userId) throws Exception;

	/**
	 *  @Desc 주문복사 할 주문상세 할인금액 건수가 있는지 조회.
	 * @param srchOdOrderId
	 * @param srchOdOrderDetlId
	 * @return
	 */
	int getOrderCopyDiscountCnt(long srchOdOrderId, long srchOdOrderDetlId);

	/**
	 * @Desc 주문복사 할 주문상세 패키지 건수가 있는지 조회.
	 * @param srchOdOrderDetlId
	 * @return
	 */
	int getOrderCopyPackageCnt(long srchOdOrderDetlId);

	/** 주문상품 정보 조회 */
	ApiResult<?> getOrderDetlGoodsSaleStatus(List<Long> odOrderDetlIdList);

	/** 주문복사 유효성체크 */
	ApiResult<?> checkOrderCopyValidation(List<OrderCopyValidationDto> orderCopyValidationDtoList) throws Exception;

	/** 주문상품 출고처 조회 */
	ApiResult<?> getOrderDetlGoodsWarehouseCode(List<Long> odOrderDetlIdList);
}