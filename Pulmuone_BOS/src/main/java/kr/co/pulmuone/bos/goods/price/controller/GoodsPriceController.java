package kr.co.pulmuone.bos.goods.price.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import kr.co.pulmuone.bos.goods.price.service.GoodsPriceBosService;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.goods.price.dto.GoodsPriceResponseDto;
import lombok.extern.slf4j.Slf4j;

/**
* <PRE>
* Forbiz Korea
* 상품가격스케줄생성 BOS Controller
*  1. ERP 배치에서 품목가격정보 변경건 존재 시 호출 - 파람 : 기준일자 (baseDe (yyyyMMdd))
*  2. ERP 배치에서 올가할인가정보 변경건 존재 시 호출 - 파람 : 기준일자 (baseDe (yyyyMMdd))
*  3. 미연동상품의 품목가격변동 승인 완료 시 - 파람 : 품목CD (ilItemCd)
*  4. (즉시, 우선) 상품할인가격변동 승인 완료 시 - 파람 : 상품ID (ilGoodsId)
*  5. 묶음상품가격변동 승인 완료 시 - 파람 : 상품ID (ilGoodsId)
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2020.09.02.    dgyoun   최초작성
* =======================================================================
* </PRE>
*/

@Slf4j
@RestController
public class GoodsPriceController {

  @Resource
  private GoodsPriceBosService goodsPriceBosService;

  //@Autowired(required=true)
  //private HttpServletRequest request;

  //@Autowired(required=true)
  //private HttpServletResponse response;

  // ##########################################################################
  // TODO 2020.09.07 기준,
  //      GoodsPriceController.java, GoodsPriceBosService.java 불필요 할 듯
  //      COMMON에서 GoodsPriceBiz~~~  를 바로 호출 가능할 듯
  // ##########################################################################

  /**
   * 상품가격스케줄생성(API:대상일자-N건) : ERP배치에서 품목가격정보 변경 건 존재 시 호출
   * @param baseDe
   * @return
   * @throws Exception
   */
  @ApiOperation(value = "상품가격생성")
  @PostMapping(value = "/admin/goods/price/genGoodsPriceScheduleForErpBatch")
  public ApiResult<?> genGoodsPriceScheduleForErpBatch (@RequestParam(value = "baseDe") String baseDe) throws Exception {
    log.info("# ######################################");
    log.info("# GoodsPriceController.genGoodsPriceScheduleForErpBatch Start");
    log.info("# ######################################");
    log.info("# baseDe :: " + baseDe);

    // ------------------------------------------------------------------------
    // # 초기화
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // # 처리
    // ------------------------------------------------------------------------
    // # BOS 서비스 호출
    GoodsPriceResponseDto result = goodsPriceBosService.genGoodsPriceScheduleForErpBatch(baseDe);
    log.info("# result                :: " + result.toString());
    log.info("# result.resultCode     :: " + result.getResultCode());
    log.info("# result.resultMessage  :: " + result.getResultMessage());

    // ------------------------------------------------------------------------
    // # 반환
    // ------------------------------------------------------------------------
    return ApiResult.success(result);

  }

  /**
   * 상품가격스케줄생성(API:대상일자-N건) : ERP배치에서 올가할인가 변경 건 존재 시 호출
   * @param baseDe
   * @return
   * @throws Exception
   */
  @ApiOperation(value = "상품가격생성")
  @PostMapping(value = "/admin/goods/price/genGoodsPriceScheduleForOrgaBatch")
  public ApiResult<?> genGoodsPriceScheduleForOrgaBatch (@RequestParam(value = "baseDe") String baseDe) throws Exception {
    log.info("# ######################################");
    log.info("# GoodsPriceController.genGoodsPriceScheduleForOrgaBatch Start");
    log.info("# ######################################");
    log.info("# baseDe :: " + baseDe);

    // ------------------------------------------------------------------------
    // # 초기화
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // # 처리
    // ------------------------------------------------------------------------
    // # BOS 서비스 호출
    GoodsPriceResponseDto result = goodsPriceBosService.genGoodsPriceScheduleForOrgaBatch(baseDe);
    log.info("# result                :: " + result.toString());
    log.info("# result.resultCode     :: " + result.getResultCode());
    log.info("# result.resultMessage  :: " + result.getResultMessage());

    // ------------------------------------------------------------------------
    // # 반환
    // ------------------------------------------------------------------------
    return ApiResult.success(result);
  }

  /**
   * 상품가격스케줄생성(API:품목CD-단건)
   * @param ilItemCd
   * @return
   * @throws Exception
   */
  @ApiOperation(value = "상품가격생성")
  @PostMapping(value = "/admin/goods/price/genGoodsPriceScheduleByItemCd")
  public ApiResult<?> genGoodsPriceScheduleByItemCd (@RequestParam(value = "ilItemCd") String ilItemCd) throws Exception {
    log.info("# ######################################");
    log.info("# GoodsPriceController.genGoodsPriceScheduleByItemCd Start");
    log.info("# ######################################");
    log.info("# ilItemCd :: " + ilItemCd);

    // ------------------------------------------------------------------------
    // # 초기화
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // # 처리
    // ------------------------------------------------------------------------
    // # BOS 서비스 호출
    GoodsPriceResponseDto result = goodsPriceBosService.genGoodsPriceScheduleByItemCd(ilItemCd);
    log.info("# result                :: " + result.toString());
    log.info("# result.resultCode     :: " + result.getResultCode());
    log.info("# result.resultMessage  :: " + result.getResultMessage());

    // ------------------------------------------------------------------------
    // # 반환
    // ------------------------------------------------------------------------
    return ApiResult.success(result);
  }

  /**
   * 상품가격스케줄생성(API:상품ID-단건)
   * @param ilGoodsId
   * @return
   * @throws Exception
   */
  @ApiOperation(value = "상품가격생성")
  @PostMapping(value = "/admin/goods/price/genGoodsPriceScheduleByGoodsId")
  public ApiResult<?> genGoodsPriceScheduleByGoodsId (@RequestParam(value = "ilGoodsId") String ilGoodsId) throws Exception {
    log.info("# ######################################");
    log.info("# GoodsPriceController.genGoodsPriceScheduleByGoodsId Start");
    log.info("# ######################################");
    log.info("# ilGoodsId :: " + ilGoodsId);

    // ------------------------------------------------------------------------
    // # 초기화
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // # 처리
    // ------------------------------------------------------------------------
    // # BOS 서비스 호출
    GoodsPriceResponseDto result = goodsPriceBosService.genGoodsPriceScheduleByGoodsId(ilGoodsId);
    log.info("# result                :: " + result.toString());
    log.info("# result.resultCode     :: " + result.getResultCode());
    log.info("# result.resultMessage  :: " + result.getResultMessage());

    // ------------------------------------------------------------------------
    // # 반환
    // ------------------------------------------------------------------------
    return ApiResult.success(result);
  }



}
