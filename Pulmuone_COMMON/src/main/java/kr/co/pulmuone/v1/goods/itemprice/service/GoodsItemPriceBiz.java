package kr.co.pulmuone.v1.goods.itemprice.service;

import java.util.Map;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.goods.itemprice.dto.ItemPriceByBatchResponseDto;
import kr.co.pulmuone.v1.goods.itemprice.dto.ItemPriceResponseDto;

/**
 * <PRE>
 * Forbiz Korea
 * 품목 가격조회 ERP 연동 및 가격 로직 구현
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    20200902   	ykk      최초작성
 * =======================================================================
 * </PRE>
 */

public interface GoodsItemPriceBiz {

//	public ApiResult<?> putItemPriceOrigWithErpIfPriceBatch() throws Exception;

//	public ItemPriceByBatchResponseDto putItemPriceListWithErpIfPriceBatch() throws Exception;

	public ItemPriceResponseDto putItemPriceListByErpIfPrice() throws Exception;

	public Map<String,?> getErpIfPriceSrchApi(Map<String, String> parameterMap) throws Exception;

}
