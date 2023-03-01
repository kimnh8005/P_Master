package kr.co.pulmuone.bos.goods.itemprice;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.goods.itemprice.service.GoodsItemPriceBiz;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


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
 *  1.0    20200902   	ykk     최초작성
 * =======================================================================
 * </PRE>
 */

@RestController
@RequiredArgsConstructor
public class GoodsItemPriceController {

	private final GoodsItemPriceBiz itemPriceBiz;

//	@ApiOperation(value = "품목 가격조회 ERP 연동")
//	@PostMapping(value = "/admin/goods/itemprice/putItemPriceOrigWithErpIfPriceBatch")
//	public ApiResult<?> putItemPriceOrigWithErpIfPriceBatch() throws Exception{
//		return itemPriceBiz.putItemPriceOrigWithErpIfPriceBatch();
//	}



	/**
	 * 품목 가격조회 ERP 연동 및 가격 로직 구현
	 * @param
	 * @return ApiResult<?>
	 * @throws Exception

	@PostMapping(value = "/admin/goods/itemprice/putItemPriceListWithErpIfPriceBatch")
	public ApiResult<?> putItemPriceListWithErpIfPriceBatch() throws Exception{
		return ApiResult.success(itemPriceBosService.putItemPriceListWithErpIfPriceBatch());
	}*/



	/**
	 * 품목 가격 수정 (품목가격원본 정보로)
	 * @param
	 * @return ApiResult<?>
	 * @throws Exception

	@PostMapping(value = "/admin/goods/itemprice/putItemPriceListByErpIfPrice")
	public ApiResult<?> putItemPriceListByErpIfPrice() throws Exception{
		return ApiResult.success(itemPriceBosService.putItemPriceListByErpIfPrice());
	}
	 */
}

