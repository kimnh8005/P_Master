package kr.co.pulmuone.mall.goods.goods.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.customer.qna.dto.ProductQnaRequestDto;
import kr.co.pulmuone.v1.customer.qna.dto.ProductQnaListByGoodsRequestDto;
import kr.co.pulmuone.v1.customer.qna.dto.vo.ProductQnaVo;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsDailyMealScheduleRequestDto;
import kr.co.pulmuone.v1.promotion.coupon.dto.CouponByGoodsListRequestDto;
import kr.co.pulmuone.v1.customer.feedback.dto.FeedbackImageListByGoodsRequestDto;
import kr.co.pulmuone.v1.customer.feedback.dto.FeedbackListByGoodsRequestDto;
import kr.co.pulmuone.v1.customer.feedback.dto.FeedbackSetBestRequestDto;

/**
 * <PRE>
 * Forbiz Korea
 * Class의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    20200810		 	천혜현            최초작성
 * =======================================================================
 * </PRE>
 */

public interface GoodsGoodsMallService {

  /**
   * 상품 상세 조회
   *
   * @param ilGoodsId
   * @return GoodsPageInfoResponseDto
   * @throws Exception
   */
  ApiResult<?> getGoodsPageInfo(Long ilCategoryId, String mallDiv, String ilCtgryId, String urPcidCd) throws Exception;

  /**
   * 장바구니 옵션 항목 조회
   *
   * @param ilGoodsId
   * @return
   * @throws Exception
   */
  ApiResult<?> getOptionInfo(String cartType, Long ilGoodsId, String employeeYn) throws Exception;

  /**
   * 쿠폰 다운로드
   *
   * @param dto CouponByGoodsRequestDto
   * @return ApiResult<?>
   * @throws Exception Exception
   */
  ApiResult<?> addCouponByGoods(CouponByGoodsListRequestDto dto) throws Exception;

  /**
   * 상품 문의 목록 조회 - 상품상세
   *
   * @param dto ProductQnaListByGoodsRequestDto
   * @return ApiResult<?>
   * @throws Exception Exception
   */
  ApiResult<?> getProductQnaListByGoods(ProductQnaListByGoodsRequestDto dto) throws Exception;

  /**
   * 상품 문의 유형 목록 - 상품상세
   *
   * @return ApiResult<?>
   * @throws Exception Exception
   */
  ApiResult<?> getProductType() throws Exception;

  /**
   * 상품 문의 작성 - 상품상세
   *
   * @Param dto ProductQnaRequestDto
   * @return ApiResult<?>
   * @throws Exception Exception
   */
  ApiResult<?> addProductQna(ProductQnaRequestDto dto) throws Exception;

  /**
   * 상품 문의 상세 조회 - 상품상세
   *
   * @Param csQnaId Long
   * @return ApiResult<?>
   * @throws Exception Exception
   */
  ApiResult<?> getProductQnaByGoods(Long csQnaId) throws Exception;

  /**
   * 상품 문의 수정 - 상품상세
   *
   * @Param vo ProductQnaVo
   * @return ApiResult<?>
   * @throws Exception Exception
   */
  ApiResult<?> putProductQna(ProductQnaVo vo) throws Exception;

  /**
   * 상품 후기 이미지 목록 조회
   *
   * @param dto GetFeedbackImageListByGoodsRequestDto
   * @return ApiResult<?>
   * @throws Exception
   */
  ApiResult<?> getFeedbackImageListByGoods(FeedbackImageListByGoodsRequestDto dto) throws Exception;

  /**
   * 상품 후기 목록 조회
   *
   * @param GetFeedbackListByGoodsRequestDto
   * @return ApiResult<?>
   * @throws Exception
   */
  ApiResult<?> getFeedbackListByGoods(FeedbackListByGoodsRequestDto dto) throws Exception;

  /**
   * 상품 상세 후기 추천
   *
   * @param dto FeedbackSetBestRequestDto
   * @return ApiResult<?>
   * @throws Exception Exception
   */
  ApiResult<?> putFeedbackSetBestYes(FeedbackSetBestRequestDto dto) throws Exception;

  /**
   * 상품 상세 후기 추천 취소
   *
   * @param dto FeedbackSetBestRequestDto
   * @return ApiResult<?>
   * @throws Exception Exception
   */
  ApiResult<?> putFeedbackSetBestNo(FeedbackSetBestRequestDto dto) throws Exception;

  /**
   * 식단 스케줄 조회
   *
   * @param reqDto GoodsDailyMealScheduleRequestDto
   * @return
   * @throws Exception
   */
  ApiResult<?> getGoodsDailyMealSchedule(GoodsDailyMealScheduleRequestDto reqDto) throws Exception;

  /**
   * 식단 컨텐츠 상세 조회
   *
   * @param ilGoodsDailyMealContsCd
   * @return
   * @throws Exception
   */
  ApiResult<?> getGoodsDailyMealContents(String ilGoodsDailyMealContsCd) throws Exception;
}
