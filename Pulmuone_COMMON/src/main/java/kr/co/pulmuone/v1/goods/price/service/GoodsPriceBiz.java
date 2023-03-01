package kr.co.pulmuone.v1.goods.price.service;

import kr.co.pulmuone.v1.goods.price.dto.GoodsDiscountResponseDto;
import kr.co.pulmuone.v1.goods.price.dto.GoodsPackageEmployeeDiscountResponseDto;
import kr.co.pulmuone.v1.goods.price.dto.GoodsPriceRequestDto;
import kr.co.pulmuone.v1.goods.price.dto.GoodsPriceResponseDto;
//import kr.co.pulmuone.v1.goods.price.dto.GoodsPriceResponseDto;
//import kr.co.pulmuone.v1.goods.price.dto.vo.GoodsDiscountVo;
//import kr.co.pulmuone.v1.goods.price.dto.vo.GoodsPriceVo;
//import kr.co.pulmuone.v1.goods.price.dto.vo.ItemPriceVo;
//import kr.co.pulmuone.v1.goods.price.dto.vo.ItemVo;
import kr.co.pulmuone.v1.goods.price.dto.ItemPriceRequestDto;
import kr.co.pulmuone.v1.goods.price.dto.ItemPriceResponseDto;

/**
* <PRE>
* Forbiz Korea
* 상품가격스케줄생성 COMMON Interface
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

public interface GoodsPriceBiz {

  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // 상품가격로직
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  /**
   * 상품가격스케줄생성(API:대상일자-N건) : ERP배치에서 품목가격정보 변경 건 존재 시 호출
   * @param priceChngDe
   * @return
   * @throws Exception
   */
  GoodsPriceResponseDto genGoodsPriceScheduleForErpBatch (String baseDe) throws Exception;


  /**
   * 상품가격스케줄생성(API:대상일자-N건) : ERP배치에서 올가할인가 변경 건 존재 시 호출
   * @param baseDe
   * @return
   * @throws Exception
   */
  GoodsPriceResponseDto genGoodsPriceScheduleForOrgaBatch (String baseDe) throws Exception;


  /**
   * 상품가격스케줄생성(API:품목CD-단건)
   * @param ilItemCd
   * @return
   * @throws Exception
   */
  GoodsPriceResponseDto genGoodsPriceScheduleByItemCd (String ilItemCd) throws Exception;


  /**
   * 상품가격스케줄생성(API:상품ID-단건)
   * @param ilGoodsId
   * @return
   * @throws Exception
   */
  GoodsPriceResponseDto genGoodsPriceScheduleByGoodsId (String ilGoodsId) throws Exception;

  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // 상품정보조회(ETC)
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  /**
   * 상품가격페이징리스트조회
   * @param goodsPriceRequestDto
   * @return
   * @throws Exception
   */
  GoodsPriceResponseDto getGoodsPricePagingList (GoodsPriceRequestDto goodsPriceRequestDto) throws Exception;

  /**
   * 품목가격페이징리스트조회
   * @param itemPriceRequestDto
   * @return
   * @throws Exception
   */
  ItemPriceResponseDto getItemPricePagingList (ItemPriceRequestDto itemPriceRequestDto) throws Exception;

  /**
   * 상품할인정보리스트조회
   * @param goodsPriceRequestDto
   * @return
   * @throws Exception
   */
  GoodsDiscountResponseDto getGoodsDiscountPagingList (GoodsPriceRequestDto goodsPriceRequestDto) throws Exception;

  /**
   * 임직원 개별할인 정보 변경이력 보기
   * @param String
   * @return
   * @throws Exception
   */
  GoodsPackageEmployeeDiscountResponseDto goodsPackageEmployeeDiscountHistoryGridList(GoodsPriceRequestDto goodsPriceRequestDto) throws Exception;

  /**
   * 묶음상품 > 판매 가격정보 > 변경이력 보기 > 상세보기
   * @param String
   * @return
   * @throws Exception
   */
  GoodsPackageEmployeeDiscountResponseDto goodsPackageGoodsMappingPrice(String ilGoodsPriceId, String ilGoodsDiscountId) throws Exception;
}
