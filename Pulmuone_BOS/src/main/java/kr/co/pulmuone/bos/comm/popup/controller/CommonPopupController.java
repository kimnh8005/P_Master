package kr.co.pulmuone.bos.comm.popup.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.bos.comm.popup.service.CommonPopupService;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.company.notice.dto.GetNoticePopupListResponseDto;
import kr.co.pulmuone.v1.company.notice.dto.GetNoticePopupResponseDto;
import kr.co.pulmuone.v1.company.notice.service.CompanyNoticeBiz;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsChangeLogListRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsChangeLogListResponseDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsChangeLogListVo;
import kr.co.pulmuone.v1.goods.price.dto.GoodsDiscountResponseDto;
import kr.co.pulmuone.v1.goods.price.dto.GoodsPriceRequestDto;
import kr.co.pulmuone.v1.goods.price.dto.GoodsPriceResponseDto;
import kr.co.pulmuone.v1.goods.price.dto.ItemPriceRequestDto;
import kr.co.pulmuone.v1.goods.price.dto.ItemPriceResponseDto;
import kr.co.pulmuone.v1.user.login.service.UserLoginBosBiz;
import lombok.extern.slf4j.Slf4j;

/**
* <PRE>
* Forbiz Korea
* 공통팝업 BOS Controller
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2020.09.09.    dgyoun   최초작성
* =======================================================================
* </PRE>
*/

@Slf4j
@RestController
public class CommonPopupController {

  @Resource
  private CommonPopupService commonPopupService;

  @Autowired
  private UserLoginBosBiz userLoginBosBiz;

  @Autowired
  private CompanyNoticeBiz companyNoticeBiz;

  //@Autowired(required=true)
  //private HttpServletRequest request;

  /**
   * 상품할인가격리스트조회
   * @param ilGoodsId
   * @return
   * @throws Exception
   */
  @ApiOperation(value = "상품할인가격리스트조회")
  @PostMapping(value = "/admin/comn/popup/getGoodsPriceList")
  public ApiResult<?> getGoodsPricePagingList (GoodsPriceRequestDto goodsPriceRequestDto) throws Exception {
    log.info("# ######################################");
    log.info("# CommonPopupController.goodsPriceRequestDto Start");
    log.info("# ######################################");
    log.info("# In :: " + goodsPriceRequestDto.toString());

    // ------------------------------------------------------------------------
    // # 초기화
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // # 처리
    // ------------------------------------------------------------------------
    // # BOS 서비스 호출
    GoodsPriceResponseDto result = commonPopupService.getGoodsPricePagingList(goodsPriceRequestDto);
    log.info("# result                :: " + result.toString());
    log.info("# result.resultCode     :: " + result.getResultCode());
    log.info("# result.resultMessage  :: " + result.getResultMessage());

    // ------------------------------------------------------------------------
    // # 반환
    // ------------------------------------------------------------------------
    return ApiResult.success(result);

  }

  /**
   * 품목가격리스트조회
   * @param itemPriceRequestDto
   * @return
   * @throws Exception
   */
  @ApiOperation(value = "품목가격리스트조회")
  @PostMapping(value = "/admin/comn/popup/getItemPriceList")
  public ApiResult<?> getItemPricePagingList (ItemPriceRequestDto itemPriceRequestDto) throws Exception {
    log.info("# ######################################");
    log.info("# CommonPopupController.getItemPricePagingList Start");
    log.info("# ######################################");
    log.info("# In :: " + itemPriceRequestDto.toString());

    // ------------------------------------------------------------------------
    // # 초기화
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // # 처리
    // ------------------------------------------------------------------------
    // # BOS 서비스 호출
    ItemPriceResponseDto result = commonPopupService.getItemPricePagingList(itemPriceRequestDto);
    log.info("# result                :: " + result.toString());
    log.info("# result.resultCode     :: " + result.getResultCode());
    log.info("# result.resultMessage  :: " + result.getResultMessage());

    // ------------------------------------------------------------------------
    // # 반환
    // ------------------------------------------------------------------------
    return ApiResult.success(result);

  }

  /**상품할인정보리스트조회
   *
   * @param goodsPriceRequestDto
   * @return
   * @throws Exception
   */
  @ApiOperation(value = "상품할인정보리스트조회")
  @PostMapping(value = "/admin/comn/popup/getGoodsDiscountList")
  public ApiResult<?> getGoodsDiscountPagingList (GoodsPriceRequestDto goodsPriceRequestDto) throws Exception {
    log.info("# ######################################");
    log.info("# CommonPopupController.getGoodsDiscountPagingList Start");
    log.info("# ######################################");
    log.info("# In :: " + goodsPriceRequestDto.toString());

    // ------------------------------------------------------------------------
    // # 초기화
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // # 처리
    // ------------------------------------------------------------------------
    // # BOS 서비스 호출
    GoodsDiscountResponseDto result = commonPopupService.getGoodsDiscountPagingList(goodsPriceRequestDto);
    log.info("# result                :: " + result.toString());
    log.info("# result.resultCode     :: " + result.getResultCode());
    log.info("# result.resultMessage  :: " + result.getResultMessage());

    // ------------------------------------------------------------------------
    // # 반환
    // ------------------------------------------------------------------------
    return ApiResult.success(result);

  }

  /**
   * 관리자 최근 접속 정보
   * @return
   * @throws Exception
   */
  @ApiOperation(value = "관리자 최근 접속 정보")
  @PostMapping(value = "/admin/comn/popup/getRecentlyLoginData")
  @ApiResponses(value = {
          @ApiResponse(code = 901, message = "LOGIN_NO_DATA - 로그인 정보가 없습니다. \n")
  })
  public ApiResult<?> getRecentlyLoginData() throws Exception{
      return userLoginBosBiz.getRecentlyLoginData();
  }

  /**
   * 로그인 시 공지사항 팝업 리스트
   * @return
   * @throws Exception
   */
  @ApiOperation(value = "로그인 시 공지사항 팝업 리스트")
  @PostMapping(value = "/admin/comn/popup/getNoticePopupList")
  @ApiResponses(value = {
          @ApiResponse(code = 900, message = "response data", response  = GetNoticePopupListResponseDto.class)})
  public ApiResult<?> getNoticePopupList() throws Exception{
      return companyNoticeBiz.getNoticePopupList();
  }

  /**
   * 로그인 시 공지사항 상세내용 팝업
   * @return
   * @throws Exception
   */
  @ApiOperation(value = "로그인 시 공지사항 상세내용 팝업")
  @PostMapping(value = "/admin/comn/popup/getNoticePopup")
  @ApiResponses(value = {
          @ApiResponse(code = 900, message = "response data", response  = GetNoticePopupResponseDto.class)})
  public ApiResult<?> getNoticePopup(@RequestParam(value="csCompanyBbsId", required=false) int csCompanyBbsId){
      return companyNoticeBiz.getNoticePopup(csCompanyBbsId);
  }

  /**업데이트 상세 내역
  *
  * @param goodsPriceRequestDto
  * @return
  * @throws Exception
  */
 @ApiOperation(value = "업데이트 상세 내역")
 @PostMapping(value = "/admin/comn/popup/getGoodsChangeLogPopup")
 public ApiResult<?> getGoodsChangeLogPopup (GoodsChangeLogListRequestDto goodsChangeLogListRequestDto) throws Exception {
   log.info("# ######################################");
   log.info("# CommonPopupController.getGoodsChangeLogPopup Start");
   log.info("# ######################################");
   log.info("# In :: " + goodsChangeLogListRequestDto.toString());

   // ------------------------------------------------------------------------
   // # 초기화
   // ------------------------------------------------------------------------

   // ------------------------------------------------------------------------
   // # 처리
   // ------------------------------------------------------------------------
   // # BOS 서비스 호출
   GoodsChangeLogListResponseDto result = commonPopupService.getGoodsChangeLogPopup(goodsChangeLogListRequestDto);
   log.info("# result                :: " + result.toString());

   // ------------------------------------------------------------------------
   // # 반환
   // ------------------------------------------------------------------------
   return ApiResult.success(result);

 }
}
