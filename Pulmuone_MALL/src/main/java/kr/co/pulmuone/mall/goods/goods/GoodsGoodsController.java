package kr.co.pulmuone.mall.goods.goods;

import io.swagger.annotations.*;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.constants.BuyerConstants;
import kr.co.pulmuone.v1.comm.util.CookieUtil;
import kr.co.pulmuone.v1.customer.qna.dto.ProductQnaByGoodsResponseDto;
import kr.co.pulmuone.v1.customer.qna.dto.ProductQnaListByGoodsRequestDto;
import kr.co.pulmuone.v1.customer.qna.dto.ProductQnaListByGoodsResponseDto;
import kr.co.pulmuone.v1.customer.qna.dto.ProductQnaRequestDto;
import kr.co.pulmuone.v1.customer.qna.dto.vo.ProductQnaVo;
import kr.co.pulmuone.v1.customer.feedback.dto.FeedbackImageListByGoodsRequestDto;
import kr.co.pulmuone.v1.customer.feedback.dto.FeedbackListByGoodsRequestDto;
import kr.co.pulmuone.v1.customer.feedback.dto.FeedbackSetBestRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.GetOptionInfoResponseDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsDailyMealScheduleRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsPageInfoResponseDto;
import kr.co.pulmuone.v1.promotion.coupon.dto.CouponByGoodsListRequestDto;
import kr.co.pulmuone.v1.user.buyer.dto.CommonGetCodeListResponseDto;
import kr.co.pulmuone.mall.goods.goods.service.GoodsGoodsMallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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
 *  1.0    20200824   	 	천혜현            최초작성
 *  1.1    20200928   	 	이원호            쿠폰 다운로드 API 추가
 * =======================================================================
 * </PRE>
 */
@RestController
public class GoodsGoodsController {

  @Autowired
  public GoodsGoodsMallService goodsGoodsMallService;

  /**
   * 상품 상세 조회
   *
   * @param ilGoodsId
   * @return GoodsPageInfoResponseDto
   * @throws Exception
   */
  @PostMapping(value = "/goods/goods/getGoodsPageInfo")
  @ApiOperation(value = "상품 상세 조회")
  @ApiImplicitParams(
            {
              @ApiImplicitParam(name = "ilGoodsId", value = "상품 PK", required = true, dataType = "Long")
              , @ApiImplicitParam(name = "mallDiv", value = "몰구분", required = false, dataType = "String")
              , @ApiImplicitParam(name = "categoryId", value = "선택카테고리", required = false, dataType = "String")
            }
          )
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = GoodsPageInfoResponseDto.class) })
  public ApiResult<?> getGoodsPageInfo(Long ilGoodsId, String mallDiv, String categoryId, HttpServletRequest request) throws Exception {
    String urPcidCd = CookieUtil.getCookie(request, BuyerConstants.COOKIE_PCID_CODE_KEY);
    return goodsGoodsMallService.getGoodsPageInfo(ilGoodsId, mallDiv, categoryId, urPcidCd);
  }

  /**
   * 장바구니 옵션 항목 조회
   *
   * @param ilGoodsId
   * @return
   * @throws Exception
   */
  @PostMapping(value = "/goods/goods/getOptionInfo")
  @ApiOperation(value = "장바구니 옵션 항목 조회")
	@ApiImplicitParams({ @ApiImplicitParam(name = "ilGoodsId", value = "상품 PK", required = true, dataType = "Long"),
			@ApiImplicitParam(name = "employeeYn", value = "임직원 구매 여부", required = false, dataType = "String") })
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = GetOptionInfoResponseDto.class) })
	public ApiResult<?> getOptionInfo(@RequestParam(value = "cartType", required = true) String cartType,
			@RequestParam(value = "ilGoodsId", required = true) Long ilGoodsId,
			@RequestParam(value = "employeeYn", required = false) String employeeYn) throws Exception {
    return goodsGoodsMallService.getOptionInfo(cartType, ilGoodsId, employeeYn);
  }

  /**
   * 상품 후기 이미지 목록 조회
   *
   * @param dto GetFeedbackImageListByGoodsRequestDto
   * @throws Exception
   */
  @ApiOperation(value = "상품 후기 이미지 목록 조회")
  @PostMapping(value = "/goods/goods/getFeedbackImageListByGoods")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data : null") })
  public ApiResult<?> getFeedbackImageListByGoods(FeedbackImageListByGoodsRequestDto dto) throws Exception {
    return goodsGoodsMallService.getFeedbackImageListByGoods(dto);
  }

  /**
   * 상품 후기 목록 조회
   *
   * @param dto
   * @throws Exception
   */
  @ApiOperation(value = "상품 후기  목록 조회")
  @PostMapping(value = "/goods/goods/getFeedbackListByGoods")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data : null") })
  public ApiResult<?> getFeedbackListByGoods(FeedbackListByGoodsRequestDto dto) throws Exception {
    return goodsGoodsMallService.getFeedbackListByGoods(dto);
  }

  /**
   * 쿠폰다운로드 - 상품상세
   *
   * @param dto CouponByGoodsRequestDto
   * @return ApiResult<?>
   * @throws Exception Exception
   */
  @PostMapping(value = "/goods/goods/addCouponByGoods")
  @ApiOperation(value = "쿠폰 다운로드 - 상품상세")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data : null"),
      @ApiResponse(code = 901, message = "" + "[NEED_LOGIN] NEED_LOGIN - 로그인 필요 \n" + "[ETC] ETC - 기타 오류 \n"
          + "[NOT_ACCEPT_APPROVAL] NOT_ACCEPT_APPROVAL - 승인 상태 아님 \n" + "[NOT_ISSUE_DATE] NOT_ISSUE_DATE - 발급기간 아님 \n"
          + "[OVER_ISSUE_DATE] OVER_ISSUE_DATE - 발급기간 지남 \n" + "[OVER_ISSUE_QTY] OVER_ISSUE_QTY - 발급수량 제한 \n"
          + "[OVER_ISSUE_QTY_LIMIT] OVER_ISSUE_QTY_LIMIT - 1인발급수량 제한 \n" + "[PASS_VALIDATION] PASS_VALIDATION - 검증 통과") })
  public ApiResult<?> addCouponByGoods(CouponByGoodsListRequestDto dto) throws Exception {
    return goodsGoodsMallService.addCouponByGoods(dto);
  }

  /**
   * 상품 문의 목록 조회 - 상품상세
   *
   * @param dto ProductQnaListByGoodsRequestDto
   * @return ApiResult<?>
   * @throws Exception Exception
   */
  @PostMapping(value = "/goods/goods/getProductQnaListByGoods")
  @ApiOperation(value = "상품 문의 목록 조회 - 상품상세")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = ProductQnaListByGoodsResponseDto.class),
      @ApiResponse(code = 901, message = "" + "[NEED_LOGIN] NEED_LOGIN - 로그인 필요 ") })
  public ApiResult<?> getProductQnaListByGoods(ProductQnaListByGoodsRequestDto dto) throws Exception {
    return goodsGoodsMallService.getProductQnaListByGoods(dto);
  }

  /**
   * 상품 문의 유형 목록 - 상품상세
   *
   * @return ApiResult<?>
   * @throws Exception Exception
   */
  @GetMapping(value = "/goods/goods/getProductType")
  @ApiOperation(value = "상품 문의 유형 목록 - 상품상세")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = CommonGetCodeListResponseDto.class) })
  public ApiResult<?> getProductType() throws Exception {
    return goodsGoodsMallService.getProductType();
  }

  /**
   * 상품 문의 작성 - 상품상세
   *
   * @param dto ProductQnaRequestDto
   * @return ApiResult<?>
   * @throws Exception Exception
   */
  @PostMapping(value = "/goods/goods/addProductQna")
  @ApiOperation(value = "상품 문의 작성 - 상품상세")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data : null"),
      @ApiResponse(code = 901, message = "" + "[NEED_LOGIN] NEED_LOGIN - 로그인 필요 ") })
  public ApiResult<?> addProductQna(ProductQnaRequestDto dto) throws Exception {
    return goodsGoodsMallService.addProductQna(dto);
  }

  /**
   * 상품 문의 상세 조회 - 상품상세
   *
   * @param csQnaId Long
   * @return ApiResult<?>
   * @throws Exception Exception
   */
  @PostMapping(value = "/goods/goods/getProductQnaByGoods")
  @ApiOperation(value = "상품 문의 상세 조회 - 상품상세")
  @ApiImplicitParams({ @ApiImplicitParam(name = "csQnaId", value = "문의 PK", required = true, dataType = "Long") })
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = ProductQnaByGoodsResponseDto.class) })
  public ApiResult<?> getProductQnaByGoods(Long csQnaId) throws Exception {
    return goodsGoodsMallService.getProductQnaByGoods(csQnaId);
  }

  /**
   * 상품 문의 수정 - 상품상세
   *
   * @param vo ProductQnaVo
   * @return ApiResult<?>
   * @throws Exception Exception
   */
  @PostMapping(value = "/goods/goods/putProductQna")
  @ApiOperation(value = "상품 문의 수정 - 상품상세")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data : null"),
      @ApiResponse(code = 901, message = "" + "[NEED_LOGIN] NEED_LOGIN - 로그인필요 \n" + "[NOT_QNA] NOT_QNA - 해당하는 문의가 없음 \n"
          + "[NOT_STATUS] NOT_STATUS - 수정 가능 상태가 아님 \n" + "[USER_CECHK_FAIL] USER_CECHK_FAIL - 입력한 사용자가 아님") })
  public ApiResult<?> putProductQna(ProductQnaVo vo) throws Exception {
    return goodsGoodsMallService.putProductQna(vo);
  }

  /**
   * 상품 상세 후기 추천
   *
   * @param dto FeedbackSetBestRequestDto
   * @return ApiResult<?>
   * @throws Exception Exception
   */
  @PostMapping(value = "/goods/goods/putFeedbackSetBestYes")
  @ApiOperation(value = "상품 상세    후기 추천")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data : null"),
          @ApiResponse(code = 901, message = "" + "[NEED_LOGIN] NEED_LOGIN - 로그인필요 \n" + "[ALREADY_BEST] ALREADY_BEST- 기존에 추천한 건 \n") })
  public ApiResult<?> putFeedbackSetBestYes(FeedbackSetBestRequestDto dto) throws Exception {
    return goodsGoodsMallService.putFeedbackSetBestYes(dto);
  }

  /**
   * 상품 상세 후기 추천 취소
   *
   * @param dto FeedbackSetBestRequestDto
   * @return ApiResult<?>
   * @throws Exception Exception
   */
  @PostMapping(value = "/goods/goods/putFeedbackSetBestNo")
  @ApiOperation(value = "상품 상세    후기 추천 취소")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data : null"), @ApiResponse(code = 901, message = ""
      + "[NEED_LOGIN] NEED_LOGIN - 로그인필요 \n" + "[ALREADY_BEST] ALREADY_BEST- 기존에 추천한 건 \n") })

  public ApiResult<?> putFeedbackSetBestNo(FeedbackSetBestRequestDto dto) throws Exception {
    return goodsGoodsMallService.putFeedbackSetBestNo(dto);
  }

  /**
   * 식단 스케줄 조회
   *
   * @param reqDto
   * @return List<GoodsDailyMealScheduleDto>
   * @throws Exception
   */
  @PostMapping(value = "/goods/goods/getGoodsDailyMealSchedule")
  @ApiOperation(value = "식단 스케줄 조회")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = GoodsPageInfoResponseDto.class) })
  public ApiResult<?> getGoodsDailyMealSchedule(GoodsDailyMealScheduleRequestDto reqDto) throws Exception {
    return goodsGoodsMallService.getGoodsDailyMealSchedule(reqDto);
  }

  /**
   * 식단 컨텐츠 상세 조회
   *
   * @param ilGoodsDailyMealContsCd
   * @return GoodsDailyMealContentsDto
   * @throws Exception
   */
  @PostMapping(value = "/goods/goods/getGoodsDailyMealContents")
  @ApiOperation(value = "식단 컨텐츠 상세 조회")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = GoodsPageInfoResponseDto.class) })
  public ApiResult<?> getGoodsDailyMealSchedule(String ilGoodsDailyMealContsCd) throws Exception {
    return goodsGoodsMallService.getGoodsDailyMealContents(ilGoodsDailyMealContsCd);
  }
}
