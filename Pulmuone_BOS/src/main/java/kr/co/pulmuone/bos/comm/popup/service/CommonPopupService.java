package kr.co.pulmuone.bos.comm.popup.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.goods.goods.dto.GoodsChangeLogListRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsChangeLogListResponseDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsChangeLogListVo;
import kr.co.pulmuone.v1.goods.goods.service.GoodsChangeLogListBiz;
import kr.co.pulmuone.v1.goods.price.dto.GoodsDiscountResponseDto;
import kr.co.pulmuone.v1.goods.price.dto.GoodsPriceRequestDto;
import kr.co.pulmuone.v1.goods.price.dto.GoodsPriceResponseDto;
import kr.co.pulmuone.v1.goods.price.dto.ItemPriceRequestDto;
import kr.co.pulmuone.v1.goods.price.dto.ItemPriceResponseDto;
import kr.co.pulmuone.v1.goods.price.service.GoodsPriceBiz;
import lombok.extern.slf4j.Slf4j;

/**
* <PRE>
* Forbiz Korea
* 공통팝업 BOS Service
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
@Service
public class CommonPopupService {

  @Autowired
  private GoodsPriceBiz goodsPriceBiz;

  @Autowired
  private GoodsChangeLogListBiz goodsChangeLogListBiz;

  /**
   * 상품할인가격리스트조회
   * @param goodsDiscountPriceRequestDto
   * @return
   * @throws Exception
   */
  public GoodsPriceResponseDto getGoodsPricePagingList (GoodsPriceRequestDto goodsPriceRequestDto) throws Exception {
    log.info("# ######################################");
    log.info("# CommonPopupService.getGoodsPricePagingList Start");
    log.info("# ######################################");
    log.info("# In :: " + goodsPriceRequestDto.toString());

    return goodsPriceBiz.getGoodsPricePagingList(goodsPriceRequestDto);
  }


  /**
   * 품목가격리스트조회
   * @param itemPriceRequestDto
   * @return
   * @throws Exception
   */
  public ItemPriceResponseDto getItemPricePagingList (ItemPriceRequestDto itemPriceRequestDto) throws Exception {
    log.info("# ######################################");
    log.info("# CommonPopupService.getItemPricePagingList Start");
    log.info("# ######################################");
    log.info("# In :: " + itemPriceRequestDto.toString());

    return goodsPriceBiz.getItemPricePagingList(itemPriceRequestDto);
  }

  /**
   * 상품할인정보리스트조회
   * @param goodsPriceRequestDto
   * @return
   * @throws Exception
   */
  public GoodsDiscountResponseDto getGoodsDiscountPagingList (GoodsPriceRequestDto goodsPriceRequestDto) throws Exception {
    log.info("# ######################################");
    log.info("# CommonPopupService.getGoodsDiscountPagingList Start");
    log.info("# ######################################");
    log.info("# In :: " + goodsPriceRequestDto.toString());

    return goodsPriceBiz.getGoodsDiscountPagingList(goodsPriceRequestDto);
  }

  /**
   * 업데이트 상세 내역
   * @param goodsChangeLogListRequestDto
   * @return
   * @throws Exception
   */
  public GoodsChangeLogListResponseDto getGoodsChangeLogPopup (GoodsChangeLogListRequestDto goodsChangeLogListRequestDto) throws Exception {
    log.info("# ######################################");
    log.info("# CommonPopupService.getGoodsChangeLogPopup Start");
    log.info("# ######################################");
    log.info("# In :: " + goodsChangeLogListRequestDto.toString());

    return goodsChangeLogListBiz.getGoodsChangeLogPopup(goodsChangeLogListRequestDto);
  }
}
