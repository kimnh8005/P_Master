package kr.co.pulmuone.v1.comm.mapper.goods.price;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsPriceInfoResultVo;
import kr.co.pulmuone.v1.goods.price.dto.GoodsPriceRequestDto;
import kr.co.pulmuone.v1.goods.price.dto.ItemPriceRequestDto;
import kr.co.pulmuone.v1.goods.price.dto.vo.GoodsDiscountVo;
import kr.co.pulmuone.v1.goods.price.dto.vo.GoodsPackageMappingVo;
import kr.co.pulmuone.v1.goods.price.dto.vo.GoodsPriceVo;
import kr.co.pulmuone.v1.goods.price.dto.vo.GoodsVo;
import kr.co.pulmuone.v1.goods.price.dto.vo.ItemPriceVo;
import kr.co.pulmuone.v1.goods.price.dto.vo.ItemVo;

@Mapper
public interface GoodsPriceMapper {

  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // 상품가격로직
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  /**
   * 대상품목리스트조회-대상일자
   * @param ilItemCd
   * @return
   * @throws Exception
   */
  List<ItemVo> getTargetItemListByDate(@Param("baseDe") String baseDe) throws Exception;

  /**
   * 대상상품리스트조회-품목CD
   * @param ilItemCd
   * @return
   * @throws Exception
   */
  //List<GoodsVo> getTargetGoodsList(@Param("ilItemCd") String ilItemCd, @Param("baseDe") String baseDe) throws Exception;
  List<GoodsVo> getTargetGoodsList(@Param("ilItemCdList") List<String> ilItemCdList, @Param("baseDe") String baseDe) throws Exception;

  /**
   * 대상상품리스트조회-가격변경처리여부
   * @param ilItemCd
   * @return
   * @throws Exception
   */
  List<GoodsVo> getTargetGoodsListForOrga(@Param("baseDe") String baseDe) throws Exception;

  /**
   * 상품정보조회
   * @param ilGoodsId
   * @return
   * @throws Exception
   */
  GoodsVo getGoodsInfo(@Param("ilGoodsId") String ilGoodsId) throws Exception;

  /**
   * 구성상품리스트조회-묶음상품의 구성상품
   * @param ilGoodsId
   * @return
   * @throws Exception
   */
  List<GoodsPackageMappingVo> getGoodsPackageMappingList(@Param("ilGoodsId") String ilGoodsId, @Param("baseDe") String baseDe) throws Exception;

  /**
   * 묶음상품리스트조회-입력상품을구성상품으로 사용하는 묶음상품
   * @param ilItemCd
   * @return
   * @throws Exception
   */
  List<GoodsVo> getPackageGoodsListByMappingGoods(@Param("ilGoodsIid") String ilGoodsIid) throws Exception;

  /**
   * 품목가격리스트조회
   * @param ilItemCd
   * @return
   * @throws Exception
   */
  List<ItemPriceVo> getItemPriceList(@Param("ilGoodsId") String ilGoodsId) throws Exception;

  /**
   * 상품가격시작일자리스트조회-일반상품
   * @param ilGoodsId
   * @return
   * @throws Exception
   */
  List<GoodsPriceVo> getGoodsPriceItemDiscountPeriodList(@Param("ilGoodsId") String ilGoodsId) throws Exception;

  /**
   * 품목가격리스트조회-묶음상품의구성상품전체
   * @param ilGoodsId
   * @return
   * @throws Exception
   */
  List<ItemPriceVo> getItemPriceListForPackage(@Param("ilGoodsId") String ilGoodsId) throws Exception;

  /**
   * 상품가격시작일자리스트조회-묶음상품의구성상품전체
   * @param ilGoodsId
   * @return
   * @throws Exception
   */
  List<GoodsPriceVo> getGoodsPriceItemDiscountPeriodListForPackage(@Param("ilGoodsId") String ilGoodsId) throws Exception;

  /**
   * 묶음상품의 구성상품 정상가 Sumw 조회
   * @param ilGoodsId
   * @param scheduleStartDt
   * @return
   * @throws Exception
   */
  ItemPriceVo getTargetGoodsPriceSum(@Param("ilGoodsId") String ilGoodsId, @Param("scheduleStartDt") String scheduleStartDt) throws Exception;

  /**
   * 상품할인리스트조회
   * @param ilGoodsId
   * @return
   * @throws Exception
   */
  List<GoodsDiscountVo> getGoodsDiscountList(@Param("ilGoodsId") String ilGoodsId, @Param("scheduleStartDt") String scheduleStartDt, @Param("recommendedPrice") long recommendedPrice) throws Exception;

  /**
   * 상품가격미사용수정
   * @param ilGoodsId
   * @return
   * @throws Exception
   */
  int putGoodsPriceUnUse(GoodsPriceVo goodsPriceVo) throws Exception;

  /**
   * 상품가격스케줄등록
   * @return
   */
  int addGoodsPrice(GoodsPriceVo goodsPriceVo) throws Exception;

  /**
   * 품목-상품가격변경처리여부수정
   * @param itemVo
   * @return
   * @throws Exception
   */
  int putItemPorcYn(ItemVo itemVo) throws Exception;

  /**
   * 상품-상품가격변경처리여부수정
   * @param goodsVo
   * @return
   * @throws Exception
   */
  int putGoodsPorcYn(GoodsVo goodsVo) throws Exception;

  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // 상품정보조회(ETC)
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  /**
   * 상품가격페이징리스트조회
   * @param goodsPriceRequestDto
   * @return
   * @throws Exception
   */
  List<GoodsPriceVo> getGoodsPricePagingList (GoodsPriceRequestDto goodsPriceRequestDto) throws Exception;

  /**
   * 임직원 할인 가격정보 리스트조회
   * @param goodsPriceRequestDto
   * @return
   * @throws Exception
   */
  List<GoodsPriceVo> getGoodsEmployeePricePagingList (GoodsPriceRequestDto goodsPriceRequestDto) throws Exception;

  /**
   * 품목가격페이징리스트조회
   * @param itemPriceRequestDto
   * @return
   * @throws Exception
   */
  List<ItemPriceVo> getItemPricePagingList (ItemPriceRequestDto itemPriceRequestDto) throws Exception;

  /**
   * 상품할인정보리스트조회
   * @param goodsPriceRequestDto
   * @return
   * @throws Exception
   */
  List<GoodsDiscountVo> getGoodsDiscountPagingList (GoodsPriceRequestDto goodsPriceRequestDto) throws Exception;

  /**
   * 임직원 할인 가격정보 변경이력 보기
   * @param String
   * @return
   * @throws Exception
   */
  List<GoodsPriceInfoResultVo> goodsPackageEmployeePriceHistroyList(GoodsPriceRequestDto goodsPriceRequestDto);

  /**
   * 임직원 개별할인 정보 변경이력 보기
   * @param String
   * @return
   * @throws Exception
   */
  List<GoodsPriceInfoResultVo> goodsPackageEmployeeDiscountHistoryGridList (GoodsPriceRequestDto goodsPriceRequestDto) throws Exception;

  /**
   * 묶음상품 > 판매 가격정보 > 변경이력 보기 > 상세보기
   * @param String
   * @return
   * @throws Exception
   */
  List<GoodsPriceInfoResultVo> goodsPackageGoodsMappingPrice (@Param("ilGoodsPriceId") String ilGoodsPriceId) throws Exception;

  /**
   * 묶음상품 > 행사/할인내역, 묶음상품 기본 판매가 > 즉시할인, 기본할인 상세내역 > 상세보기
   * @param String
   * @return
   * @throws Exception
   */
  List<GoodsPriceInfoResultVo> goodsPackageGoodsMappingDiscount (@Param("ilGoodsDiscountId") String ilGoodsDiscountId) throws Exception;
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // 전체건수
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  int selectTotalCount () throws Exception;
}
