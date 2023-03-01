package kr.co.pulmuone.v1.statics.sale.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.statics.sale.dto.SaleStaticsRequestDto;
import kr.co.pulmuone.v1.statics.sale.dto.SaleStaticsResponseDto;

public class SaleStaticsServiceTest extends CommonServiceTestBaseForJunit5 {

  @Autowired
  private SaleStaticsService saleStaticsSerivce;

  /**
   * 판매현황통계조회
   * @throws BaseException
   */
  @Test
  public void test_판매현황통계조회() throws BaseException {

    // ------------------------------------------------------------------------
    // # Param
    // ------------------------------------------------------------------------
    SaleStaticsRequestDto saleStaticsRequestDto = new SaleStaticsRequestDto();

    saleStaticsRequestDto.setStartDt("20210201");
    saleStaticsRequestDto.setEndDt("20210228");
    saleStaticsRequestDto.setContrastStartDt("20210301");
    saleStaticsRequestDto.setContrastEndDt("20210318");
    saleStaticsRequestDto.setSearchTp("ODR");

    // ------------------------------------------------------------------------
    // # Call
    // ------------------------------------------------------------------------
    SaleStaticsResponseDto result = saleStaticsSerivce.selectSaleStaticsList(saleStaticsRequestDto);

    // ------------------------------------------------------------------------
    // # Result
    // ------------------------------------------------------------------------
    assertTrue(result.getTotal() > 0);
  }

  /**
   * 상품별판매현황통계조회
   * @throws BaseException
   */
  @Test
  public void test_상품별판매현황통계조회() throws BaseException {

    // ------------------------------------------------------------------------
    // # Param
    // ------------------------------------------------------------------------
    SaleStaticsRequestDto saleStaticsRequestDto = new SaleStaticsRequestDto();

    saleStaticsRequestDto.setStartDe("20210201");
    saleStaticsRequestDto.setEndDe("20210228");
    //saleStaticsRequestDto.setContrastStartDt("20210301");
    //saleStaticsRequestDto.setContrastEndDt("20210318");
    saleStaticsRequestDto.setSearchTp("ODR");

    // ------------------------------------------------------------------------
    // # Call
    // ------------------------------------------------------------------------
    SaleStaticsResponseDto result = saleStaticsSerivce.selectSaleGoodsStaticsList(saleStaticsRequestDto);

    // ------------------------------------------------------------------------
    // # Result
    // ------------------------------------------------------------------------
    assertTrue(result.getTotal() > 0);
  }

}
