package kr.co.pulmuone.bos.goods.price.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.pulmuone.v1.goods.price.dto.GoodsPriceResponseDto;
import kr.co.pulmuone.v1.goods.price.service.GoodsPriceBiz;
import lombok.extern.slf4j.Slf4j;

/**
* <PRE>
* Forbiz Korea
* 상품가격스케줄생성 BOS Service
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
@Service
public class GoodsPriceBosService {

  @Autowired
  private GoodsPriceBiz goodsPriceBiz;

  /**
   * 상품가격스케줄생성(API:대상일자-N건) : ERP배치에서 품목가격정보 변경 건 존재 시 호출
   * @param baseDe
   * @return
   * @throws Exception
   */
  public GoodsPriceResponseDto genGoodsPriceScheduleForErpBatch (@RequestParam(value = "baseDe") String baseDe) throws Exception {
    log.info("# ######################################");
    log.info("# GoodsPriceBosService.genGoodsPriceScheduleForErpBatch Start");
    log.info("# ######################################");
    log.info("# baseDe :: " + baseDe);

    return goodsPriceBiz.genGoodsPriceScheduleForErpBatch(baseDe);
  }

  /**
   * 상품가격스케줄생성(API:대상일자-N건) : ERP배치에서 올가할인가 변경 건 존재 시 호출
   * @param baseDe
   * @return
   * @throws Exception
   */
  public GoodsPriceResponseDto genGoodsPriceScheduleForOrgaBatch (@RequestParam(value = "baseDe") String baseDe) throws Exception {
    log.info("# ######################################");
    log.info("# GoodsPriceBosService.genGoodsPriceScheduleForOrgaBatch Start");
    log.info("# ######################################");
    log.info("# baseDe :: " + baseDe);

    return goodsPriceBiz.genGoodsPriceScheduleForOrgaBatch(baseDe);
  }

  // ##########################################################################
  // TODO 아래 두건은 필요한지 확인 필요
  // ##########################################################################
  /**
   * 상품가격스케줄생성(API:품목CD-단건)
   * @param ilItemCd
   * @return
   * @throws Exception
   */
  public GoodsPriceResponseDto genGoodsPriceScheduleByItemCd (@RequestParam(value = "ilItemCd") String ilItemCd) throws Exception {
    log.info("# ######################################");
    log.info("# GoodsPriceBosService.genGoodsPriceScheduleByItemCd Start");
    log.info("# ######################################");
    log.info("# ilItemCd :: " + ilItemCd);

    return goodsPriceBiz.genGoodsPriceScheduleByItemCd(ilItemCd);
  }

  /**
   * 상품가격스케줄생성(API:상품ID-단건)
   * @param ilGoodsId
   * @return
   * @throws Exception
   */
  public GoodsPriceResponseDto genGoodsPriceScheduleByGoodsId (@RequestParam(value = "ilGoodsId") String ilGoodsId) throws Exception {
    log.info("# ######################################");
    log.info("# GoodsPriceBosService.genGoodsPriceScheduleByGoodsId Start");
    log.info("# ######################################");
    log.info("# ilGoodsId :: " + ilGoodsId);

    return goodsPriceBiz.genGoodsPriceScheduleByGoodsId(ilGoodsId);
  }

}
