package kr.co.pulmuone.v1.statics.sale.service;

import kr.co.pulmuone.v1.comm.enums.StaticsEnums.SaleSataticsMessage;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.mapper.statics.sale.SaleStaticsMapper;
import kr.co.pulmuone.v1.statics.sale.dto.SaleStaticsRequestDto;
import kr.co.pulmuone.v1.statics.sale.dto.SaleStaticsResponseDto;
import kr.co.pulmuone.v1.statics.sale.dto.vo.SaleStaticsVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
* <PRE>
* Forbiz Korea
* 통계관리 판매매출 COMMON Service
*
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0       2021.03.17.              dgyoun         최초작성
* =======================================================================
* </PRE>
*/

@Slf4j
@Service
@RequiredArgsConstructor
public class SaleStaticsService {

  final String YES  = "Y";
  final String NO   = "N";

  final String MODE_ADD = "ADD";
  final String MODE_PUT = "PUT";

  private final SaleStaticsMapper saleStaticsMapper;

  // ##########################################################################
  // protected
  // ##########################################################################
  /**
   * 판매현황통계 리스트 조회
   * - No Page
   * @param saleStaticsRequestDto
   * @return
   * @throws BaseException
   */
  @SuppressWarnings("unused")
  protected SaleStaticsResponseDto selectSaleStaticsList (SaleStaticsRequestDto saleStaticsRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# SaleStaticsService.selectSaleStaticsList Start");
    log.debug("# ######################################");
    //log.debug("# In.dpPageId :: " + dpPageId);
    //log.debug("# In.useAllYn :: " + useAllYn);

    // ========================================================================
    // # 초기화
    // ========================================================================
    SaleStaticsResponseDto resultResDto = new SaleStaticsResponseDto();
    resultResDto.setResultCode(SaleSataticsMessage.SALE_STATICS_MANAGE_SUCCESS.getCode());
    resultResDto.setResultMessage(SaleSataticsMessage.SALE_STATICS_MANAGE_SUCCESS.getMessage());
    List<SaleStaticsVo> resultList = null;

    // ========================================================================
    // # 처리
    // ========================================================================

    // ------------------------------------------------------------------------
    // # 조회
    // ------------------------------------------------------------------------
    resultList = saleStaticsMapper.selectSaleStaticsList (saleStaticsRequestDto);

    // ------------------------------------------------------------------------
    // 총계 row 추가
    // ------------------------------------------------------------------------
    if (resultList != null && resultList.size() > 0) {
      long sumStandardPaidPrice    = 0l;  // 총계row 기준기간 판매금액
      long sumStandardPaidPriceNonTax    = 0l;  // 총계row 기준기간 판매금액
      long sumStandardOrderCnt     = 0l;  // 총계row 기준기간 주문건수
      long sumStandardGoodsCnt     = 0l;  // 총계row 기준기간 주문상품수량
      long sumContrastPaidPrice    = 0l;  // 총계row 대비기간 판매금액
      long sumContrastPaidPriceNonTax    = 0l;  // 총계row 대비기간 판매금액
      long sumContrastOrderCnt     = 0l;  // 총계row 대비기간 주문건수
      long sumContrastGoodsCnt     = 0l;  // 총계row 대비기간 주문상품수량
      long sumStretchRate          = 0l;  // 총계row 판매금액신장율

      for (SaleStaticsVo unitVo : resultList) {
        sumStandardPaidPrice  += unitVo.getStandardPaidPrice();
        sumStandardPaidPriceNonTax  += unitVo.getStandardPaidPriceNonTax();
        sumStandardOrderCnt   += unitVo.getStandardOrderCnt();
        sumStandardGoodsCnt   += unitVo.getStandardGoodsCnt();
        sumContrastPaidPrice  += unitVo.getContrastPaidPrice();
        sumContrastPaidPriceNonTax  += unitVo.getContrastPaidPriceNonTax();
        sumContrastOrderCnt   += unitVo.getContrastOrderCnt();
        sumContrastGoodsCnt   += unitVo.getContrastGoodsCnt();
        sumStretchRate        += unitVo.getStretchRate();
      }

      // 총계 row의 판매신장률
      long calcSumStretchRate = 0;
      if (sumContrastPaidPrice > 0) {
        calcSumStretchRate = Math.round(((1F*sumStandardPaidPrice/sumContrastPaidPrice)*100) - 100);
      }

      SaleStaticsVo addRowVo = new SaleStaticsVo();
      addRowVo.setSupplierNm("총계");
      addRowVo.setSellersGroupCdNm("");
      addRowVo.setSellersNm("");
      addRowVo.setStandardPaidPrice(sumStandardPaidPrice);
      addRowVo.setStandardPaidPriceFm(numberFormat(sumStandardPaidPrice));  // 엑셀용
      addRowVo.setStandardPaidPriceNonTax(sumStandardPaidPriceNonTax);
      addRowVo.setStandardPaidPriceNonTaxFm(numberFormat(sumStandardPaidPriceNonTax));  // 엑셀용
      addRowVo.setStandardOrderCnt(sumStandardOrderCnt);
      addRowVo.setStandardOrderCntFm(numberFormat(sumStandardOrderCnt));    // 엑셀용
      addRowVo.setStandardGoodsCnt(sumStandardGoodsCnt);
      addRowVo.setStandardGoodsCntFm(numberFormat(sumStandardGoodsCnt));    // 엑셀용
      addRowVo.setContrastPaidPrice(sumContrastPaidPrice);
      addRowVo.setContrastPaidPriceFm(numberFormat(sumContrastPaidPrice));  // 엑셀용
      addRowVo.setContrastPaidPriceNonTax(sumContrastPaidPriceNonTax);
      addRowVo.setContrastPaidPriceNonTaxFm(numberFormat(sumContrastPaidPriceNonTax));  // 엑셀용
      addRowVo.setContrastOrderCnt(sumContrastOrderCnt);
      addRowVo.setContrastOrderCntFm(numberFormat(sumContrastOrderCnt));    // 엑셀용
      addRowVo.setContrastGoodsCnt(sumContrastGoodsCnt);
      addRowVo.setContrastGoodsCntFm(numberFormat(sumContrastGoodsCnt));    // 엑셀용
      addRowVo.setStretchRate(calcSumStretchRate);
      addRowVo.setStretchRateFm(numberFormat(calcSumStretchRate));          // 엑셀용

      resultList.add(addRowVo);
      resultResDto.setTotal(resultList.size());
    }
    else {
      resultResDto.setTotal(0);
    }

    resultResDto.setRows(resultList);

    // ========================================================================
    // # 반환
    // ========================================================================
    return resultResDto;
  }

  /**
   * 상품별 판매현황통계 리스트 조회
   * - No Page
   * @param saleStaticsRequestDto
   * @return
   * @throws BaseException
   */
  @SuppressWarnings("unused")
  protected SaleStaticsResponseDto selectSaleGoodsStaticsList (SaleStaticsRequestDto saleStaticsRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# SaleStaticsService.selectSaleGoodsStaticsList Start");
    log.debug("# ######################################");
    //log.debug("# In.dpPageId :: " + dpPageId);
    //log.debug("# In.useAllYn :: " + useAllYn);

    // ========================================================================
    // # 초기화
    // ========================================================================
    SaleStaticsResponseDto resultResDto = new SaleStaticsResponseDto();
    resultResDto.setResultCode(SaleSataticsMessage.SALE_STATICS_MANAGE_SUCCESS.getCode());
    resultResDto.setResultMessage(SaleSataticsMessage.SALE_STATICS_MANAGE_SUCCESS.getMessage());
    List<SaleStaticsVo> resultList = null;

    // ========================================================================
    // # 처리
    // ========================================================================

    List<String> keywordList = new ArrayList<String>();
    // 묶음상품 확인
    if(!StringUtils.isEmpty(saleStaticsRequestDto.getKeyword())){
      String[] packageId = saleStaticsRequestDto.getKeyword().split(",");
      for(int i=0;i< packageId.length;i++){
        if(saleStaticsMapper.confirmPackage(packageId[i]) >0 ){
          List<SaleStaticsVo> voList = new ArrayList<SaleStaticsVo>();
          voList = saleStaticsMapper.getPakageGoodsInfo(packageId[i]);
          for(int j=0;j<voList.size() ;j++){
            keywordList.add(voList.get(j).getIlGoodsId());
          }
        }else{
          keywordList.add(packageId[i]);
        }
      }
    }
    // 묶음상품 번호에 포함된 상품 번호 조회키에 추가
    saleStaticsRequestDto.setKeywordList(keywordList);
    // ------------------------------------------------------------------------
    // # 조회
    // ------------------------------------------------------------------------
    log.debug("# SaleStaticsController.reqDto[4] :: " + saleStaticsRequestDto.toString());
    resultList = saleStaticsMapper.selectSaleGoodsStaticsList (saleStaticsRequestDto);
    resultResDto.setRows(resultList);


    if (resultList != null && resultList.size() > 0) {
      resultResDto.setTotal(resultList.size());
    }
    else {
      resultResDto.setTotal(0);
    }

    // ========================================================================
    // # 반환
    // ========================================================================
    return resultResDto;
  }


  @SuppressWarnings("unused")
  private String numberFormat(long inValue) {
    DecimalFormat df = new DecimalFormat("#,##0");
    return df.format(inValue);
  }




}
