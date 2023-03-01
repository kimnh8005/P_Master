package kr.co.pulmuone.v1.statics.sale.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.statics.sale.dto.SaleStaticsRequestDto;

/**
* <PRE>
* Forbiz Korea
* 통계관리 판매매출 COMMON Interface
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

public interface SaleStaticsBiz {

  /**
   * 판매현황통계 리스트 조회
   * @param saleStaticsRequestDt
   * @return
   * @throws BaseException
   */
  public ApiResult<?> selectSaleStaticsList (SaleStaticsRequestDto saleStaticsRequestDto) throws BaseException;

  /**
   * 상품별판매현황통계 리스트 조회
   * @param saleStaticsRequestDt
   * @return
   * @throws BaseException
   */
  public ApiResult<?> selectSaleGoodsStaticsList (SaleStaticsRequestDto saleStaticsRequestDto) throws BaseException;






}
