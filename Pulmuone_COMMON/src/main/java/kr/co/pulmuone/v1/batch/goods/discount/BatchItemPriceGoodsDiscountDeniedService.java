package kr.co.pulmuone.v1.batch.goods.discount;

import kr.co.pulmuone.v1.batch.goods.discount.dto.BatchItemPriceGoodsDiscountDeniedReqeustDto;
import kr.co.pulmuone.v1.comm.enums.ApprovalEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.mappers.batch.master.goods.discount.BatchItemPriceGoodsDiscountDeniedMapper;
import kr.co.pulmuone.v1.comm.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <PRE>
 * Forbiz Korea
 * 상품할인 승인 내역에서 할인승인 상태가 '승인요청'이고 시작일이 이미 도래한 내역에 대한 Batch 처리
 * </PRE>
 *
 * <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2021. 05. 14.               임상건         최초작성
* =======================================================================
 * </PRE>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BatchItemPriceGoodsDiscountDeniedService {

	@Autowired
	private BatchItemPriceGoodsDiscountDeniedMapper batchItemPriceGoodsDiscountDeniedMapper;

	//상품할인 승인 내역에서 할인승인 상태가 '승인요청'이고 시작일이 이미 도래한 내역에 대한 Batch 처리
	protected void runGoodsDiscountApprPastStartDateDenied() throws BaseException {
		String toDateTime = DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss");
		
		BatchItemPriceGoodsDiscountDeniedReqeustDto batchItemPriceGoodsDiscountDeniedReqeustDto = BatchItemPriceGoodsDiscountDeniedReqeustDto.builder()
				.apprStat(ApprovalEnums.ApprovalStatus.DENIED.getCode())
				.toDateTime(toDateTime)
				.build();

		//상품할인 승인 내역 폐기 히스토리 입력
		insertGoodsDiscountApprHistoryPastStartDateDenied(batchItemPriceGoodsDiscountDeniedReqeustDto);

		//상품할인 승인 내역 폐기 처리
		updateGoodsDiscountApprPastStartDateDenied(batchItemPriceGoodsDiscountDeniedReqeustDto);

		// 품목 가격 승인 내역 폐기 히스토리 입력
		insertItemPriceApprHistoryPastStartDateDenied(batchItemPriceGoodsDiscountDeniedReqeustDto);

		// 품목 가격 승인 폐기 처리
		updateItemPriceApprPastStartDateDenied(batchItemPriceGoodsDiscountDeniedReqeustDto);

	}

	/**
	 * 할인승인 상태가 '승인요청'상태이고 할인 시작일이 이미 도래한 상품 할인 승인 내역에 대해서 '폐기'처리
	 */
	protected int updateGoodsDiscountApprPastStartDateDenied(BatchItemPriceGoodsDiscountDeniedReqeustDto batchItemPriceGoodsDiscountDeniedReqeustDto) {
		return batchItemPriceGoodsDiscountDeniedMapper.updateGoodsDiscountApprPastStartDateDenied(batchItemPriceGoodsDiscountDeniedReqeustDto);
	}

	/**
	 * 할인승인 상태가 '승인요청'상태이고 할인 시작일이 이미 도래한 상품 할인 승인 내역에 대해서 '폐기' 히스토리 입력
	 */
	protected int insertGoodsDiscountApprHistoryPastStartDateDenied(BatchItemPriceGoodsDiscountDeniedReqeustDto batchItemPriceGoodsDiscountDeniedReqeustDto) {
		return batchItemPriceGoodsDiscountDeniedMapper.insertGoodsDiscountApprHistoryPastStartDateDenied(batchItemPriceGoodsDiscountDeniedReqeustDto);
	}

	/**
	 * 품목 가격 승인 상태가 '승인요청'상태이고 할인 시작일이 이미 도래한 상품 할인 승인 내역에 대해서 '폐기'처리
	 */
	protected int updateItemPriceApprPastStartDateDenied(BatchItemPriceGoodsDiscountDeniedReqeustDto batchItemPriceGoodsDiscountDeniedReqeustDto) {
		return batchItemPriceGoodsDiscountDeniedMapper.updateItemPriceApprPastStartDateDenied(batchItemPriceGoodsDiscountDeniedReqeustDto);
	}

	/**
	 * 품목 가격 승인 상태가 '승인요청'상태이고 할인 시작일이 이미 도래한 상품 할인 승인 내역에 대해서 '폐기' 히스토리 입력
	 */
	protected int insertItemPriceApprHistoryPastStartDateDenied(BatchItemPriceGoodsDiscountDeniedReqeustDto batchItemPriceGoodsDiscountDeniedReqeustDto) {
		return batchItemPriceGoodsDiscountDeniedMapper.insertItemPriceApprHistoryPastStartDateDenied(batchItemPriceGoodsDiscountDeniedReqeustDto);
	}
}
