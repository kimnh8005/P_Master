package kr.co.pulmuone.v1.comm.mappers.batch.master.goods.discount;

import kr.co.pulmuone.v1.batch.goods.discount.dto.BatchItemPriceGoodsDiscountDeniedReqeustDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BatchItemPriceGoodsDiscountDeniedMapper {

	/**
	 * 할인승인 상태가 '승인요청'상태이고 할인 시작일이 이미 도래한 상품 할인 승인 내역에 대해서 '폐기'처리
	 */
	int updateGoodsDiscountApprPastStartDateDenied(BatchItemPriceGoodsDiscountDeniedReqeustDto batchItemPriceGoodsDiscountDeniedReqeustDto);
	/**
	 * 할인승인 상태가 '승인요청'상태이고 할인 시작일이 이미 도래한 상품 할인 승인 내역에 대해서 '폐기' 히스토리 입력
	 */
	int insertGoodsDiscountApprHistoryPastStartDateDenied(BatchItemPriceGoodsDiscountDeniedReqeustDto batchItemPriceGoodsDiscountDeniedReqeustDto);
	/**
	 * 품목 가격 승인 상태가 '승인요청'상태이고 할인 시작일이 이미 도래한 상품 할인 승인 내역에 대해서 '폐기'처리
	 */
	int updateItemPriceApprPastStartDateDenied(BatchItemPriceGoodsDiscountDeniedReqeustDto batchItemPriceGoodsDiscountDeniedReqeustDto);
	/**
	 * 품목 가격 승인 상태가 '승인요청'상태이고 할인 시작일이 이미 도래한 상품 할인 승인 내역에 대해서 '폐기' 히스토리 입력
	 */
	int insertItemPriceApprHistoryPastStartDateDenied(BatchItemPriceGoodsDiscountDeniedReqeustDto batchItemPriceGoodsDiscountDeniedReqeustDto);
}
