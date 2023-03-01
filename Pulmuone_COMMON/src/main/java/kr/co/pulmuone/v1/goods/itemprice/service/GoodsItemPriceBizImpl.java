package kr.co.pulmuone.v1.goods.itemprice.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

@Service
public class GoodsItemPriceBizImpl  implements GoodsItemPriceBiz {

	private static final Logger log = LoggerFactory.getLogger(GoodsItemPriceBizImpl.class);

	@Autowired
	private GoodsItemPriceService itemPriceService;


	/**
	 * 품목 가격조회 ERP 연동
	 * @param
	 * @return
	 * @throws	Exception
	 */
//	@Override
//	public ApiResult<?> putItemPriceOrigWithErpIfPriceBatch() throws Exception
//	{
//		ItemPriceByBatchResponseDto itemPriceByBatchResponseDto = new ItemPriceByBatchResponseDto();
//
//        //배치 데이터 처리 get & insert
//		int addOrigCount = itemPriceService.addItemPriceOrigByErpIfPriceSrchApi();
//
//		itemPriceByBatchResponseDto.setAddOrigCount(addOrigCount);
//   		return ApiResult.success(itemPriceByBatchResponseDto);
//	}


	/**
	 * 품목 가격조회 ERP 연동 및 가격 로직 구현
	 * @param
	 * @return
	 * @throws	Exception
	 */
//	@Override
//	public ItemPriceByBatchResponseDto putItemPriceListWithErpIfPriceBatch() throws Exception
//	{
//		ItemPriceByBatchResponseDto itemPriceByBatchResponseDto = new ItemPriceByBatchResponseDto();
//
//        //배치 데이터 처리 get & insert
//		int addOrigCount = itemPriceService.addItemPriceOrigByErpIfPriceSrchApi();
//
//        //IL_ITEM_PRICE 테이블에 데이터 add
//		int addPriceCount = itemPriceService.addItemPriceByOrig();
//
//		itemPriceByBatchResponseDto.setAddOrigCount(addOrigCount);
//		itemPriceByBatchResponseDto.setAddPriceCount(addPriceCount);
//   		return itemPriceByBatchResponseDto;
//	}


	/**
	 * 배치 데이터로 가격 정보 저장
	 * @param
	 * @return
	 * @throws	Exception
	 */
	@Override
	public ItemPriceResponseDto putItemPriceListByErpIfPrice() throws Exception
	{
		ItemPriceResponseDto itemPriceResponseDto = new ItemPriceResponseDto();

        //IL_ITEM_PRICE 테이블에 데이터 add
		int addPriceCount =  itemPriceService.addItemPriceByOrig();
		itemPriceResponseDto.setAddPriceCount(addPriceCount);

   		return itemPriceResponseDto;
	}


	/**
	 * ERP 데이터 받기
	 * @param
	 * @return
	 * @throws	Exception
	 */
	@Override
	public Map<String,?> getErpIfPriceSrchApi(Map<String, String> parameterMap) throws Exception {
		return itemPriceService.getErpIfPriceSrchApi(parameterMap);
	}



}
