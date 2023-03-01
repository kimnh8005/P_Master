package kr.co.pulmuone.v1.statics.sale.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.statics.sale.dto.SaleStaticsRequestDto;
import kr.co.pulmuone.v1.statics.sale.dto.SaleStaticsResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
* <PRE>
* Forbiz Korea
* 통계관리 매출통계 COMMON Impl
*
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0       2021.03.19.              dgyoun         최초작성
* =======================================================================
* </PRE>
*/

@Slf4j
@Service
@RequiredArgsConstructor
public class SaleStaticsBizImpl implements SaleStaticsBiz {

  @Autowired
  private SaleStaticsService saleStaticsService;

  /**
   * 판매현황통계 리스트 조회
   */
  @Override
  public ApiResult<?> selectSaleStaticsList (SaleStaticsRequestDto saleStaticsRequestDto) throws BaseException {
    log.info("# ######################################");
    log.info("# SaleStaticsBizImpl.selectSaleStaticsList Start");
    log.info("# ######################################");
    if (saleStaticsRequestDto != null) {
      log.info("# In.saleStaticsRequestDto :: " + saleStaticsRequestDto.toString());
    }
    else {
      log.info("# In.saleStaticsRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    SaleStaticsResponseDto result = null;

    // ========================================================================
    // # 처리
    // ========================================================================
    result = saleStaticsService.selectSaleStaticsList(saleStaticsRequestDto);

    // ========================================================================
    // # 반환
    // ========================================================================
    return ApiResult.success(result);
  }

  /**
   * 상품별 판매현황통계 리스트 조회
   */
  @Override
  public ApiResult<?> selectSaleGoodsStaticsList (SaleStaticsRequestDto saleStaticsRequestDto) throws BaseException {
    log.info("# ######################################");
    log.info("# SaleStaticsBizImpl.selectSaleGoodsStaticsList Start");
    log.info("# ######################################");
    if (saleStaticsRequestDto != null) {
      log.info("# In.saleStaticsRequestDto :: " + saleStaticsRequestDto.toString());
    }
    else {
      log.info("# In.saleStaticsRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    SaleStaticsResponseDto result = null;

    // ========================================================================
    // # 처리
    // ========================================================================
    log.debug("# SaleStaticsController.reqDto[3] :: " + saleStaticsRequestDto.toString());
    result = saleStaticsService.selectSaleGoodsStaticsList(saleStaticsRequestDto);

    // ========================================================================
    // # 반환
    // ========================================================================
    return ApiResult.success(result);
  }

}
