package kr.co.pulmuone.mall.display.contents.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.display.contents.dto.*;

/**
 * <PRE>
 * Forbiz Korea
 * Class의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 * 버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 * 1.0    20200928   	 	이원호            최초작성
 * =======================================================================
 * </PRE>
 */

public interface DisplayContentsMallService {

    ApiResult<?> getInventoryContentsInfoList(String pageCd, String deviceType, String userType) throws Exception;

    ApiResult<?> getCategoryInfo(String inventoryCd, String deviceType, String userType) throws Exception;

    ApiResult<?> getContentsLevel1ByInventoryCd(String inventoryCd, String deviceType) throws Exception;

    ApiResult<?> getContentsInfo(Long dpContsId, String deviceType, String userType, String goodsSortCode) throws Exception;

    ApiResult<?> getShippingAddress() throws Exception;

    ApiResult<?> getCoupon() throws Exception;

    ApiResult<?> getUserJoinGoodsList() throws Exception;

    ApiResult<?> getBestGoods(String searchType, String mallDiv, Long ilCtgryId) throws Exception;

    ApiResult<?> getBestGoodsList(String mallDiv, String deviceType, String userType) throws Exception;

    ApiResult<?> getDailyShippingGoods(DailyShippingGoodsRequestDto dto) throws Exception;

    ApiResult<?> getSaleGoodsPageInfo(String mallDiv, int discountRateStart, int discountRateEnd) throws Exception;

    ApiResult<?> getSaleGoods(SaleGoodsRequestDto dto) throws Exception;

    ApiResult<?> getNewGoods(NewGoodsRequestDto dto) throws Exception;

    ApiResult<?> getBrandGoods(BrandGoodsRequestDto dto) throws Exception;

    ApiResult<?> getLohasBanner(LohasBannerRequestDto dto) throws Exception;

}
