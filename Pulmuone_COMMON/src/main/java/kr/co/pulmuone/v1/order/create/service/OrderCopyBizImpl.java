package kr.co.pulmuone.v1.order.create.service;

import kr.co.pulmuone.v1.order.create.dto.OrderCopyValidationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.order.create.dto.OrderCopyDetailInfoRequestDto;
import kr.co.pulmuone.v1.order.create.dto.OrderCopySaveRequestDto;

import java.util.List;

/**
*
* <PRE>
* Forbiz Korea
* 주문 복사 관련 I/F Impl
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

@Service
public class OrderCopyBizImpl implements OrderCopyBiz {

    @Autowired
    private OrderCopyService orderCopyService;	// 주문복사 Service

    /**
     * 주문복사 주문 상세내역 조회
     *
     * @param orderCopyDetailInfoRequestDto
     * @return ApiResult<?>
     */
	@Override
	public ApiResult<?> getOrderCopyDetailInfo(OrderCopyDetailInfoRequestDto reqDto) throws Exception {
		return orderCopyService.getOrderCopyDetailInfo(reqDto);
	}

    /**
     * 주문복사 주문 수량 변경 결제상세정보 조회
     *
     * @param orderCopyDetailInfoRequestDto
     * @return ApiResult<?>
     */
	@Override
	public ApiResult<?> getOrderCopyCntChangeInfo(OrderCopyDetailInfoRequestDto reqDto) throws Exception {
		return orderCopyService.getOrderCopyCntChangeInfo(reqDto);
	}

	/**
	 * 주문복사 저장 후 비인증 카드 결제
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false, rollbackFor = {BaseException.class,Exception.class})
	public ApiResult<?> addNonCardPayment(OrderCopySaveRequestDto reqDto, long userId) throws Exception {
		return orderCopyService.addNonCardPayment(reqDto, userId);
	}

	/**
	 * 주문복사 저장
	 * @param reqDto
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false, rollbackFor = {BaseException.class,Exception.class})
	public ApiResult<?> addOrderCopy(OrderCopySaveRequestDto reqDto, long userId) throws Exception {
		return orderCopyService.addOrderCopy(reqDto, userId);
	}

	/**
	 * 주문복사 저장
	 * @param reqDto
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> saveOrderCopyDetailInfo(OrderCopySaveRequestDto reqDto, long userId) throws Exception {
		return orderCopyService.saveOrderCopyDetailInfo(reqDto, userId);
	}

	/**
	 *  @Desc 주문복사 할 주문상세 할인금액 건수가 있는지 조회.
	 * @param srchOdOrderId
	 * @param srchOdOrderDetlId
	 * @return
	 */
	public int getOrderCopyDiscountCnt(long srchOdOrderId, long srchOdOrderDetlId) {
		return orderCopyService.getOrderCopyDiscountCnt(srchOdOrderId, srchOdOrderDetlId);
	}

	/**
	 * @Desc 주문복사 할 주문상세 패키지 건수가 있는지 조회.
	 * @param srchOdOrderDetlId
	 * @return
	 */
	public int getOrderCopyPackageCnt(long srchOdOrderDetlId) {
		return orderCopyService.getOrderCopyPackageCnt(srchOdOrderDetlId);
	}

	/**
	 * @Desc 주문상품 정보 조회
	 * @param odOrderDetlIdList
	 * @return
	 */
	public ApiResult<?> getOrderDetlGoodsSaleStatus(List<Long> odOrderDetlIdList){
		return orderCopyService.getOrderDetlGoodsSaleStatus(odOrderDetlIdList);
	}

	/**
	 * @Desc 주문복사 유효성체크
	 * @param orderCopyValidationDtoList
	 * @return
	 */
	public ApiResult<?> checkOrderCopyValidation(List<OrderCopyValidationDto> orderCopyValidationDtoList) throws Exception {
		return orderCopyService.checkOrderCopyValidation(orderCopyValidationDtoList);
	}

	/**
	 * @Desc 주문상품 출고처 조회
	 * @param odOrderDetlIdList
	 * @return
	 */
	public ApiResult<?> getOrderDetlGoodsWarehouseCode(List<Long> odOrderDetlIdList){
		return orderCopyService.getOrderDetlGoodsWarehouseCode(odOrderDetlIdList);
	}
}