package kr.co.pulmuone.v1.goods.price.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hsqldb.lib.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.comm.enums.UserEnums;
import kr.co.pulmuone.v1.comm.mapper.goods.price.GoodsPriceMapper;
import kr.co.pulmuone.v1.comm.util.DateUtil;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsPriceInfoResultVo;
import kr.co.pulmuone.v1.goods.price.dto.GoodsDiscountResponseDto;
import kr.co.pulmuone.v1.goods.price.dto.GoodsPackageEmployeeDiscountResponseDto;
import kr.co.pulmuone.v1.goods.price.dto.GoodsPriceRequestDto;
import kr.co.pulmuone.v1.goods.price.dto.GoodsPriceResponseDto;
import kr.co.pulmuone.v1.goods.price.dto.ItemPriceRequestDto;
import kr.co.pulmuone.v1.goods.price.dto.ItemPriceResponseDto;
import kr.co.pulmuone.v1.goods.price.dto.vo.GoodsDiscountVo;
import kr.co.pulmuone.v1.goods.price.dto.vo.GoodsPackageMappingVo;
import kr.co.pulmuone.v1.goods.price.dto.vo.GoodsPriceVo;
import kr.co.pulmuone.v1.goods.price.dto.vo.GoodsVo;
import kr.co.pulmuone.v1.goods.price.dto.vo.ItemPriceVo;
import kr.co.pulmuone.v1.goods.price.dto.vo.ItemVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
* <PRE>
* Forbiz Korea
* 상품가격스케줄생성 COMMON Service
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
@RequiredArgsConstructor
public class GoodsPriceService {

    private final GoodsPriceMapper goodsPriceMapper;

    // ########################################################################
    // protected (@Transaction의 경우 반듯이 public
    // ########################################################################

    // ########################################################################
    // 상품가격로직
    // ########################################################################
    /**
     * 대상상품리스트조회 (묶음상품리스트, 일반상품리스트로 구분 구성) - 품목CD
     * @param inItemCd
     * @param targetGoodsListUnPackage
     * @param targetGoodsListPackage
     * @throws Exception
     */
    protected void getTargetGoodsListByItemCd (List<String> ilItemCdList, String baseDe, List<Map<String, String>> targetGoodsListUnPackage, List<Map<String, String>> targetGoodsListPackage) throws Exception {
      log.info("# ######################################");
      log.info("# GoodsPriceService.getTargetGoodsListByItemCd Start");
      log.info("# ######################################");
      log.info("# In.ilItemCdList :: " + ilItemCdList.toString());

      // ======================================================================
      // # 초기화
      // ======================================================================
      List<GoodsVo> goodsList = null;

      // ======================================================================
      // # 처리
      // ======================================================================

      // ----------------------------------------------------------------------
      // 1.품목CD를 사용하는 상품리스트조회
      // ----------------------------------------------------------------------
      /* 2020.09.22 - dgyoun : 널체크 추가 */
      if(ilItemCdList != null && ilItemCdList.size() > 0) {
        goodsList = goodsPriceMapper.getTargetGoodsList(ilItemCdList, null);
      }

      if (goodsList != null && goodsList.size() > 0) {

        for (GoodsVo unitGoodsVo : goodsList) {

          this.getTargetGoodsListByGoodsId(unitGoodsVo, baseDe, targetGoodsListUnPackage, targetGoodsListPackage);

        } // End of for (GoodsVo unitGoodsVo : goodsList)

      } // End of if (goodsList != null && goodsList.size() > 0)

      log.info("# 일반상품개수 :: " + targetGoodsListUnPackage.size());
      log.info("# 묶음상품개수 :: " + targetGoodsListPackage.size());


      // ======================================================================
      // # 반환
      // ======================================================================

    }


    /**
     * 대상상품리스트조회 (묶음상품리스트, 일반상품리스트로 구분 구성) - 상품ID
     * @param inIlGoodsId
     * @param targetGoodsListUnPackage
     * @param packageGoodsList
     * @throws Exception
     */
    protected void getTargetGoodsListByGoodsId (GoodsVo inGoodsVo, String baseDe, List<Map<String, String>> targetGoodsListUnPackage, List<Map<String, String>> targetGoodsListPackage) throws Exception {
      log.info("# ######################################");
      log.info("# GoodsPriceService.getTargetGoodsListByGoodsId Start");
      log.info("# ######################################");
      log.info("# In.inGoodsVo :: " + inGoodsVo.toString());

      // ======================================================================
      // # 초기화
      // ======================================================================
      Map<String, String> unitMapTargetGoods = null;

      boolean bIsPackage = false;
      String inIlGoodsId = "";
      String inIlItemCd  = "";
      String inGoodsType = "";
      List<GoodsPackageMappingVo> resultListPackageMapping = null;
      List<GoodsVo> resultListPackageGoods = null;

      // ======================================================================
      // # 처리
      // ======================================================================
      // ----------------------------------------------------------------------
      // 1.상품정보조회-묶음상품여부확인용
      // ----------------------------------------------------------------------
      if (inGoodsVo != null) {

        inIlGoodsId = inGoodsVo.getIlGoodsId();
        inIlItemCd  = inGoodsVo.getIlItemCd();
        inGoodsType = inGoodsVo.getGoodsTp();

        if((GoodsEnums.GoodsType.PACKAGE.getCode()).equals(inGoodsType)) {
          // 묶음상품이면
          bIsPackage = true;
        }

        // --------------------------------------------------------------------
        // 2.묶음상품/일반상품 구분처리
        // --------------------------------------------------------------------
        if (bIsPackage) {
          // ------------------------------------------------------------------
          // 2.1.입력상품이 묶음상품인 경우 -> @[대상상품리스트]묶음상품리스트에 추가
          // ------------------------------------------------------------------
          unitMapTargetGoods = new HashMap<String, String>();
          unitMapTargetGoods.put("ilGoodsId", inIlGoodsId);
          unitMapTargetGoods.put("ilItemCd" , inIlItemCd);
          unitMapTargetGoods.put("goodsTp"  , inGoodsType);
          targetGoodsListPackage.add(unitMapTargetGoods);

          // ------------------------------------------------------------------
          // 2.1.1.입력상품(묶음상품)의 구성상품리스트조회 : 변경일자 조건 조회
          // ------------------------------------------------------------------
          if (StringUtil.isEmpty(baseDe)) {
            // 기준일자가 없으면 현재일자 Set
            baseDe = DateUtil.getCurrentDate();
          }

          resultListPackageMapping = goodsPriceMapper.getGoodsPackageMappingList(inIlGoodsId, baseDe);

          if (resultListPackageMapping != null && resultListPackageMapping.size() > 0) {

            // ----------------------------------------------------------------
            // 2.1.2.가격변동된구성상품 선별 -> @[대상상품리스트]일반상품리스트에 추가
            // ----------------------------------------------------------------
            for (GoodsPackageMappingVo unitMappingVo : resultListPackageMapping) {

              unitMapTargetGoods = new HashMap<String, String>();
              unitMapTargetGoods.put("ilGoodsId", unitMappingVo.getTargetGoodsId());
              unitMapTargetGoods.put("ilItemCd" , unitMappingVo.getIlItemCd());
              unitMapTargetGoods.put("goodsTp"  , unitMappingVo.getGoodsTp());
              targetGoodsListUnPackage.add(unitMapTargetGoods);

              // --------------------------------------------------------------
              // 2.1.3.가격변동된구성상품을 구성상품으로 하는 묶음상품조회 -> @[대상상품리스트]묶음상품리스트에 추가
              // --------------------------------------------------------------
              resultListPackageGoods = goodsPriceMapper.getPackageGoodsListByMappingGoods(unitMappingVo.getTargetGoodsId());

              if (resultListPackageGoods != null && resultListPackageGoods.size() > 0) {

                for (GoodsVo unitPackageVo : resultListPackageGoods) {

                  if (!inIlGoodsId.equals(unitPackageVo.getIlGoodsId())) {
                    // 입력 묶음상품이 아닌 경우만(앞에서 이미 add 되었음)
                    unitMapTargetGoods = new HashMap<String, String>();
                    unitMapTargetGoods.put("ilGoodsId", unitPackageVo.getIlGoodsId());
                    unitMapTargetGoods.put("ilItemCd" , unitPackageVo.getIlItemCd());
                    unitMapTargetGoods.put("goodsTp"  , unitPackageVo.getGoodsTp());
                    targetGoodsListPackage.add(unitMapTargetGoods);

                  } // End of if (!inIlGoodsId.equals(unitPackageVo.getIlGoodsId()))

                } // End of for (GoodsResultVo unitPackageVo : resultListPackageGoods)

              } // End of for (GoodsPackageMappingVo unitMappingVo : resultListPackageMapping)

            } // End of if (resultListPackageGoods != null && resultListPackageGoods.size() > 0)

          } // End of if (resultListPackageMapping != null && resultListPackageMapping.size() > 0)

        } // End of if (bIsPackage)
        else {
          // ------------------------------------------------------------------
          // 2.2.입력상품이 묶음상품이 아닌 경우 -> @[대상상품리스트]일반상품리스트에 추가
          // ------------------------------------------------------------------
          unitMapTargetGoods = new HashMap<String, String>();
          unitMapTargetGoods.put("ilGoodsId", inIlGoodsId);
          unitMapTargetGoods.put("ilItemCd" , inIlItemCd);
          unitMapTargetGoods.put("goodsTp"  , inGoodsType);
          targetGoodsListUnPackage.add(unitMapTargetGoods);

          // ------------------------------------------------------------------
          // 2.2.1.입력상품을 구성상품으로하는 묶음상품리스트 -> @[대상상품리스트]묶음상품리스트에 추가
          // ------------------------------------------------------------------
          resultListPackageGoods = goodsPriceMapper.getPackageGoodsListByMappingGoods(inIlGoodsId);

          if (resultListPackageGoods != null && resultListPackageGoods.size() > 0) {

            for (GoodsVo unitPackageVo : resultListPackageGoods) {

              unitMapTargetGoods = new HashMap<String, String>();
              unitMapTargetGoods.put("ilGoodsId", unitPackageVo.getIlGoodsId());
              unitMapTargetGoods.put("ilItemCd" , unitPackageVo.getIlItemCd());
              unitMapTargetGoods.put("goodsTp"  , unitPackageVo.getGoodsTp());
              targetGoodsListPackage.add(unitMapTargetGoods);

            } // End of for (GoodsResultVo unitPackageVo : resultListPackageGoods)

          } // End of if (resultListPackageGoods != null && resultListPackageGoods.size() > 0)

        } // End of if (bIsPackage) else

      } // End of if (resultGoodsInfo != null)

      // ======================================================================
      // # 반환
      // ======================================================================
    }

    /**
     * 상품가격정보 (재)생성
     *  - @Transactional 처리를 위해 반듯이 public 으로 선언되어야 함
     * @param ilGoodsId
     * @param listScheduleFinal
     * @throws RuntimeException
     * @throws Exception
     */
    @SuppressWarnings("unused")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void genGoodsPriceInfo (Map<String, String> targetGoodsMap, List<GoodsPriceVo> listScheduleFinal, String goodsPriceCallType) throws RuntimeException, Exception {
      log.info("# ######################################");
      log.info("# GoodsPriceService.genGoodsPriceInfo Start");
      log.info("# ######################################");
      log.info("# In.ilGoodsId :: ["+listScheduleFinal.size()+"] :: " + listScheduleFinal.toString());

      // ======================================================================
      // # 초기화
      // ======================================================================
      String ilGoodsId = "";


      // ======================================================================
      // # 처리
      // ======================================================================

      try {

        if (targetGoodsMap != null) {
          ilGoodsId = targetGoodsMap.get("ilGoodsId");
        }
        log.info("# genGoodsPriceInfo.ilGoodsId :: " + ilGoodsId);

        // --------------------------------------------------------------------
        // 8.1.기존상품가격 미사용처리
        // --------------------------------------------------------------------
        log.info("# ======================================");
        log.info("# 8.1.기존상품가격 미사용처리");
        log.info("# ======================================");
        GoodsPriceVo updtGoodsPriceVo = new GoodsPriceVo(SessionUtil.getBosUserVO());
        updtGoodsPriceVo.setIlGoodsId(ilGoodsId);
        int iResultUpd = goodsPriceMapper.putGoodsPriceUnUse(updtGoodsPriceVo);

        // --------------------------------------------------------------------
        // 8.2.신규 상품가격스케줄 등록
        // --------------------------------------------------------------------
        log.info("# ======================================");
        log.info("# 8.2.신규 상품가격스케줄 등록");
        log.info("# ======================================");
        int insCnt = 0;
        for (GoodsPriceVo unitGoodsPriceVo : listScheduleFinal) {

          unitGoodsPriceVo.setModifyId((SessionUtil.getBosUserVO()).getUserId());
          unitGoodsPriceVo.setCreateId((SessionUtil.getBosUserVO()).getUserId());

          // ############################################################################
          //// TODO 강제오류 테스트 Start
          //if ("10000037".equals(ilGoodsId) && insCnt == 2) {
          //  // TR 테스트 강제 오류
          //  unitGoodsPriceVo.setCreateId("12345678901234567890123456789");
          //  log.info("@ TR 테스트 강제 오류 ["+ilGoodsId+"]");
          //}
          //// TODO 강제오류 테스트 Start
          // ############################################################################

          int iResultIns = goodsPriceMapper.addGoodsPrice(unitGoodsPriceVo);
          insCnt += iResultIns;
        } // End of for (GoodsPriceVo unitGoodsPriceVo : listScheduleFinal)
        log.info("# insCnt :: " + insCnt);

        // --------------------------------------------------------------------
        // 8.3.상품가격변경처리여부 업데이트
        // --------------------------------------------------------------------
        log.info("# goodsPriceCallType :: " + goodsPriceCallType);

        if ((GoodsEnums.GoodsPriceCallType.BATCH.getCode()).equals(goodsPriceCallType)) {
          // ------------------------------------------------------------------
          // 배치호출 인 경우만 결과 처리
          // ------------------------------------------------------------------

          if ((GoodsEnums.GoodsPriceProcType.ITEM.getCode()).equals(targetGoodsMap.get("goodsPriceProcType"))) {
            // ----------------------------------------------------------------
            // 8.3.1.품목-상품가격변경처리결과 업데이트
            // ----------------------------------------------------------------
            log.info("# ======================================");
            log.info("# 8.3.1.품목-상품가격변경처리결과 업데이트");
            log.info("# ======================================");
            ItemVo updtProcYnItemVo = new ItemVo(SessionUtil.getBosUserVO());
            updtProcYnItemVo.setIlItemCd(targetGoodsMap.get("ilItemCd"));
            int iResultUpdtItemProcYn = goodsPriceMapper.putItemPorcYn(updtProcYnItemVo);
            log.info("# 처리결과업데이트-품목 :: " + iResultUpdtItemProcYn);
          }
          else {
            // ----------------------------------------------------------------
            // 8.3.2.상품-상품가격변경처리결과 업데이트
            // ----------------------------------------------------------------
            log.info("# ======================================");
            log.info("# 8.3.2.상품-상품가격변경처리결과 업데이트");
            log.info("# ======================================");
            GoodsVo updtProcYnGoodsVo = new GoodsVo(SessionUtil.getBosUserVO());
            updtProcYnGoodsVo.setIlGoodsId(ilGoodsId);
            int iResultUpdtGoodsProcYn = goodsPriceMapper.putGoodsPorcYn(updtProcYnGoodsVo);
            log.info("# 처리결과업데이트-상품 :: " + iResultUpdtGoodsProcYn);
          }

        } // End of if ((GoodsEnums.GoodsPriceCallType.BATCH.getCode()).equals(goodsPriceCallType))

      }
      catch (Exception e) {
        log.info("# genGoodsPriceInfo Exception e :: " + e.toString());
        throw new Exception();
      }

      // ======================================================================
      // # 반환
      // ======================================================================

    }

    /**
     * 할인가 적용 판매가 스케줄 생성
     * @param inListScheduleFinal
     * @return
     * @throws Exception
     */
    protected void setSalePriceByDiscountInfo (List<GoodsPriceVo> inListScheduleFinal) throws Exception {
      log.info("# ######################################");
      log.info("# GoodsPriceService.setSalePriceByDiscountInfo Start");
      log.info("# ######################################");
      log.info("# In.inListScheduleFinal :: ["+inListScheduleFinal.size()+"]" + inListScheduleFinal.toString());

      // ======================================================================
      // # 초기화
      // ======================================================================
      List<GoodsDiscountVo> resultListDiscount = null;

      // ======================================================================
      // # 처리
      // ======================================================================
      if (inListScheduleFinal != null && inListScheduleFinal.size() > 0) {

        // --------------------------------------------------------------------
        // 신규 스케줄 만큼 Loop
        // --------------------------------------------------------------------
        for (GoodsPriceVo unitSchedule : inListScheduleFinal) {

          // ------------------------------------------------------------------
          // 1.시작일기준으로 할인정보 조회
          // ------------------------------------------------------------------
          //if ("2403".equals(unitSchedule.getIlGoodsId())){
          //log.info("# unitSchedule["+unitSchedule.getPriceStartDt()+"] :: " + unitSchedule.toString());
          //}

          resultListDiscount = goodsPriceMapper.getGoodsDiscountList(unitSchedule.getIlGoodsId(), unitSchedule.getPriceStartDt(), unitSchedule.getRecommendedPrice());
          log.info("@ @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
          log.info("@ resultListDiscount.size :: " + resultListDiscount.size() + "["+unitSchedule.getIlGoodsId()+"]["+unitSchedule.getPriceStartDt()+"]["+unitSchedule.getRecommendedPrice()+"]");
          log.info("@ @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

          if (resultListDiscount != null && resultListDiscount.size() > 0) {
            // ----------------------------------------------------------------
            // 할인정보 존재 =>
            // ----------------------------------------------------------------

            // 조회쿼리에서 우선>올가>즉시 순서로 정렬되어 조회되므로 첫번째 것을 적용함
            GoodsDiscountVo firstVo = (GoodsDiscountVo)resultListDiscount.get(0);

            int discountApplySalePrice = firstVo.getDiscountApplySalePrice();

            log.info("@ 할인적용가 :: " + discountApplySalePrice);
            log.info("@ 정상가       :: " + unitSchedule.getRecommendedPrice());
            if (discountApplySalePrice >= unitSchedule.getRecommendedPrice()) {
              // --------------------------------------------------------------
              // @[상품가격정보] 할인적용가 >= 정상가 적용 => 정상가적용
              // --------------------------------------------------------------
              log.info("@ 할인적용가 >= 정상가 적용 => 정상가적용 :: " + GoodsEnums.GoodsDiscountType.NOT_APPLICABLE.getCode());
              unitSchedule.setDiscountTp(GoodsEnums.GoodsDiscountType.NOT_APPLICABLE.getCode()); // @할인유형 : GOODS_DISCOUNT_TP.NOT_APPLICABLE
              unitSchedule.setSalePrice(unitSchedule.getRecommendedPrice());                // @판매가 <- 정상가
            }
            else {
              // --------------------------------------------------------------
              // @[상품가격정보] 할인적용가 < 정상가 => 할인적용가 적용
              // --------------------------------------------------------------
              log.info("@ 할인적용가 < 정상가 => 할인적용가 적용 :: " + firstVo.getDiscountTp());
              unitSchedule.setIlGoodsDiscountId(firstVo.getIlGoodsDiscountId());  // @상품할인PK
              unitSchedule.setDiscountTp(firstVo.getDiscountTp());                // @할인유형
              unitSchedule.setSalePrice(firstVo.getDiscountApplySalePrice());     // @판매가<-할인적용가
            }

          } // End of if (resultListDiscount != null && resultListDiscount.size() > 0)
          else {
            // ----------------------------------------------------------------
            // @[상품가격정보] 할인정보 미존재 => 품목.정상가를 판매가격으로  Set
            // ----------------------------------------------------------------
            log.info("@ 할인정보 미존재 => 품목.정상가를 판매가격으로  Set :: " + GoodsEnums.GoodsDiscountType.NONE.getCode());
            unitSchedule.setDiscountTp(GoodsEnums.GoodsDiscountType.NONE.getCode()); // @할인유형 : GOODS_DISCOUNT_TP.NONE
            unitSchedule.setSalePrice(unitSchedule.getRecommendedPrice());      // @판매가

          } // End of if (resultListDiscount != null && resultListDiscount.size() > 0) else

        } // End of for (GoodsPriceVo unitSchedule : inListScheduleFinal)
        log.info("@ @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        log.info("@ inListScheduleFinal :: " + inListScheduleFinal.toString());
        log.info("@ @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
      } // End of if (inListScheduleFinal != null && inListScheduleFinal.size() > 0)

      // ======================================================================
      // # 반환
      // ======================================================================

    }


//  /**
//  * 상품별 상품가격스케줄 생성 - 최초방식, 디버깅 후 제거 검토
//  * @param targetGoodsMap
//  * @param goodsTp
//  * @param goodsPriceProcType
//  * @param resultResDto
//  * @throws Exception
//  */
// @SuppressWarnings("unused")
// private void procGoodsPriceScheduleGenStyleOne (Map<String, String> targetGoodsMap, String goodsTp, String goodsPriceProcType, String goodsPriceCallType, GoodsPriceResponseDto resultResDto)  throws Exception {
//   log.info("# ######################################");
//   log.info("# GoodsPriceService.procGoodsPriceScheduleGenStyleOne Start");
//   log.info("# ######################################");
//   log.info("# In.targetGoodsMap :: ["+goodsTp+"]["+goodsPriceProcType+"]["+targetGoodsMap.toString()+"]");
//
//   // ======================================================================
//   // # 초기화
//   // ======================================================================
//   List<GoodsPriceVo> listSchedule = null;
//   List<ItemPriceVo> resultListItemPrice = null;
//   GoodsPriceVo goodsPriceVo = null;
//   List<GoodsDiscountVo> resultListDiscount = null;
//   List<GoodsPriceVo> listScheduleFinal = null;
//   String ilGoodsId  = "";
//   String ilItemCd   = "";
//
//   // ======================================================================
//   // # 처리
//   // ======================================================================
//
//   listSchedule      = new ArrayList<GoodsPriceVo>();
//
//   // ------------------------------------------------------------------
//   // 1.품목가격구간설정 : 품목정보조회 > 시작일/종료일정보
//   // ------------------------------------------------------------------
//   log.info("# ======================================");
//   log.info("# 1.품목가격구간설정 - ilGoodsId :: ["+targetGoodsMap.get("ilGoodsId")+"]["+goodsTp+"]");
//   log.info("# ======================================");
//   ilGoodsId = targetGoodsMap.get("ilGoodsId");
//   ilItemCd  = targetGoodsMap.get("ilItemCd");
//
//   // ----------------------------------------------------------------------
//   // @ 이전로직  Start
//   if (GoodsEnums.GoodsType.PACKAGE.getCode().equals(goodsTp)) {
//     // ----------------------------------------------------------------
//     // 묶음상품이면 -> 묶음상품의 구성상품 전체의 가격구간리스트를 가져옴
//     // ----------------------------------------------------------------
//     // 구성상품 전체의 구간들의 합집합
//     resultListItemPrice = goodsPriceMapper.getItemPriceListForPackage(ilGoodsId);
//   }
//   else {
//     // ----------------------------------------------------------------
//     // 묶음상품이 아니면 -> 입력상품의 가격리스트를 가져옴
//     // ----------------------------------------------------------------
//     resultListItemPrice = goodsPriceMapper.getItemPriceList(ilGoodsId);
//   }
//
//   log.info("# resultListItemPrice.size :: " + resultListItemPrice.size());
//   log.info("# resultListItemPrice :: " + resultListItemPrice.toString());
//
//   if (resultListItemPrice != null && resultListItemPrice.size() > 0) {
//
//     // 품목가격구간 loop
//     for (ItemPriceVo unitItemPriceVo : resultListItemPrice) {
//
//       // --------------------------------------------------------------
//       // @[상품가격정보] 시작일자 -> 시작일자 Set
//       // --------------------------------------------------------------
//       goodsPriceVo = new GoodsPriceVo();
//       goodsPriceVo.setPriceStartDt(unitItemPriceVo.getStartDt());
//       listSchedule.add(goodsPriceVo);
//       // --------------------------------------------------------------
//       // @[상품가격정보] 종료일자 -> 시작일자 Set
//       // --------------------------------------------------------------
//       String endDtAfter1Sec = "";
//       Date date = null;
//       SimpleDateFormat sdFormat = new SimpleDateFormat("yyyyMMddHHmmss");
//       date = sdFormat.parse(unitItemPriceVo.getEndDt());
//       Calendar cal = Calendar.getInstance();
//       cal.setTime(date);
//       cal.add(Calendar.SECOND, 1);  // 1초후
//       endDtAfter1Sec = sdFormat.format(cal.getTime());
//       goodsPriceVo = new GoodsPriceVo();
//       goodsPriceVo.setPriceStartDt(endDtAfter1Sec);
//       listSchedule.add(goodsPriceVo);
//
//     } // End of for (ItemPriceVo unitItemPriceVo : resultListItemPrice)
//
//   } // End of if (resultListItemPrice != null && resultListItemPrice.size() > 0)
//
//   // ------------------------------------------------------------------
//   // 2.할인구간설정
//   // ------------------------------------------------------------------
//   log.info("# ======================================");
//   log.info("# 2.할인구간설정");
//   log.info("# ======================================");
//   // 할인 구간 정보만 사용함
//   resultListDiscount = goodsPriceMapper.getGoodsDiscountList(ilGoodsId, null, 0);
//
//   if (resultListDiscount != null && resultListDiscount.size() > 0) {
//
//     for (GoodsDiscountVo unitGoodsDiscountVo : resultListDiscount) {
//
//       // --------------------------------------------------------------
//       // @[상품가격정보] 시작일자 -> 시작일자 Set
//       // --------------------------------------------------------------
//       goodsPriceVo = new GoodsPriceVo();
//       goodsPriceVo.setPriceStartDt(unitGoodsDiscountVo.getDiscountStartDt());
//       listSchedule.add(goodsPriceVo);
//       // --------------------------------------------------------------
//       // @[상품가격정보] 종료일자 -> 시작일자 Set
//       // --------------------------------------------------------------
//       String endDtAfter1Sec = "";
//       Date date = null;
//       SimpleDateFormat sdFormat = new SimpleDateFormat("yyyyMMddHHmmss");
//       date = sdFormat.parse(unitGoodsDiscountVo.getDiscountEndDt());
//       Calendar cal = Calendar.getInstance();
//       cal.setTime(date);
//       cal.add(Calendar.SECOND, 1);  // 1초후
//       endDtAfter1Sec = sdFormat.format(cal.getTime());
//       goodsPriceVo = new GoodsPriceVo();
//       goodsPriceVo.setPriceStartDt(endDtAfter1Sec);
//       listSchedule.add(goodsPriceVo);
//
//     } // End of for (GoodsDiscountVo unitGoodsDiscountVo : resultListDiscount)
//
//   } // End of if (resultListDiscount != null && resultListDiscount.size() > 0)
//
//   if (listSchedule == null || listSchedule.size() <= 0) {
//
//     log.info("# 대상구간 없음[910000002]");
//     resultResDto.setResultCode(UserEnums.GoodsPrice.GOODS_PRICE_PERIOD_NO_EXIST.getCode());
//     resultResDto.setResultMessage(UserEnums.GoodsPrice.GOODS_PRICE_PERIOD_NO_EXIST.getMessage());
//     //return resultResDto;
//     return;
//   }
//
//   // ------------------------------------------------------------------
//   // 3.시작일자 순으로 정렬
//   // ------------------------------------------------------------------
//   log.info("# ======================================");
//   log.info("# 3.시작일자 순으로 정렬");
//   log.info("# ======================================");
//   Collections.sort(listSchedule, new Comparator<GoodsPriceVo>() {
//
//     public int compare(GoodsPriceVo o1, GoodsPriceVo o2) {
//       String sort1 = o1.getPriceStartDt();
//       String sort2 = o2.getPriceStartDt();
//       //String sort1 = String.format("%010d", o1.getPriceStartDt());
//       //String sort2 = String.format("%010d", o2.getPriceStartDt());
//       return sort1.compareTo(sort2);
//     }
//   });
//
//   // ------------------------------------------------------------------
//   // 4.종료일자 Set
//   // ------------------------------------------------------------------
//   log.info("# ======================================");
//   log.info("# 4.종료일자 Set");
//   log.info("# ======================================");
//   String endDtBefore1Sec = "";
//   Date date = null;
//   SimpleDateFormat sdFormat = new SimpleDateFormat("yyyyMMddHHmmss");
//   Calendar cal = Calendar.getInstance();
//   log.info("# listSchedule.size() :: " + listSchedule.size());
//
//   for (int i = 0; i < listSchedule.size(); i++) {
//
//     if (i > 0) {
//       // (시작일자-1초)를 앞 loop의 종료일자로 Set
//       date = sdFormat.parse(listSchedule.get(i).getPriceStartDt());
//       cal.setTime(date);
//       cal.add(Calendar.SECOND, -1); // 1초전
//       endDtBefore1Sec = sdFormat.format(cal.getTime());
//       // --------------------------------------------------------------
//       // @[상품가격정보] 종종료일자 Set : 이전 loop에 Set
//       // --------------------------------------------------------------
//       listSchedule.get(i-1).setPriceEndDt(endDtBefore1Sec);
//
//     } // End of if (i > 0)
//
//   } // End of for (int i = 0; i < listSchedule.size(); i++)
//
//   // --------------------------------------------------------------
//   // @[상품가격정보] 마지막 종료일자  Set
//   // --------------------------------------------------------------
//   log.info("# listSchedule.size() :: " + listSchedule.size());
//   //date = sdFormat.parse(listSchedule.get(listSchedule.size()-1).getPriceStartDt());
//   //cal.setTime(date);
//   //cal.add(Calendar.SECOND, -1); // 1초전
//   //endDtBefore1Sec = sdFormat.format(cal.getTime());
//   //listSchedule.get(listSchedule.size()-1).setPriceEndDt(endDtBefore1Sec);
//   listSchedule.get(listSchedule.size()-1).setPriceEndDt("29991231235959");
//
//   // ------------------------------------------------------------------
//   // 5.시작/종료 비교 및 중복 제거(시작,종료 역전 제거 포함)
//   // ------------------------------------------------------------------
//   log.info("# ======================================");
//   log.info("# 5.시작/종료 비교 및 중복 제거(시작,종료 역전 제거 포함)");
//   log.info("# ======================================");
//   listScheduleFinal = new ArrayList<GoodsPriceVo>();
//   long longStartDt  = 0l;
//   long longEndDt    = 0l;
//
//   for (GoodsPriceVo unitVo : listSchedule) {
//
//     longStartDt = Long.parseLong(unitVo.getPriceStartDt());
//     longEndDt   = Long.parseLong(unitVo.getPriceEndDt());
//
//     if (longStartDt < longEndDt) {
//       // --------------------------------------------------------------
//       // @[상품가격정보] 종료일자가 시작일자보다 큰 경우만 add
//       // --------------------------------------------------------------
//       unitVo.setIlGoodsId(ilGoodsId);   // @상품PK
//       listScheduleFinal.add(unitVo);
//     }
//
//   } // End of for (Map<String, String> unitMap : listSchedule)
//   log.info("# listSchedule.size      :: " + listSchedule.size());
//   log.info("# listScheduleFinal.size :: " + listScheduleFinal.size());
//
//   // ------------------------------------------------------------------
//   // 6.구간별 원가/정상가 조회 및 Set
//   // ------------------------------------------------------------------
//   log.info("# ======================================");
//   log.info("# 6.구간별 원가/정상가 조회 및 Set");
//   log.info("# ======================================");
//   // (정상가구간 + 할인구간) loop
//   if (listScheduleFinal != null && listScheduleFinal.size() > 0) {
//
//     String scheduleStartDt    = "";
//     String scheduleEndDt      = "";
//     long   lnScheduleStartDt  = 0l;
//     long   lnScheduleEndDt    = 0l;
//     String itemStartDt      = "";
//     String itemEndDt        = "";
//     long   lnItemStartDt    = 0l;
//     long   lnItemEndDt      = 0l;
//
//     // ----------------------------------------------------------------
//     // 6.1.품목가격스케줄 loop
//     // ----------------------------------------------------------------
//     // 확정된 구간별 loop
//     log.info("# ======================================");
//     log.info("# 6.1.가격스케줄 loop");
//     log.info("# ======================================");
//     for (GoodsPriceVo unitFinalVo : listScheduleFinal) {
//
//       scheduleStartDt   = unitFinalVo.getPriceStartDt();
//       scheduleEndDt     = unitFinalVo.getPriceEndDt();
//       lnScheduleStartDt = Long.parseLong(scheduleStartDt);
//       lnScheduleEndDt   = Long.parseLong(scheduleEndDt);
//
//       // --------------------------------------------------------------
//       // 6.2.정상가 loop
//       // --------------------------------------------------------------
//       log.info("# ======================================");
//       log.info("# 6.2.정상가 loop");
//       log.info("# ======================================");
//       if (GoodsEnums.GoodsType.PACKAGE.getCode().equals(goodsTp)) {
//         // ------------------------------------------------------------
//         // 묶음상품인 경우 -> 해당 묶음상품의 구성상품 기본가의 Sum 조회
//         // ------------------------------------------------------------
//         // 입력 : ilGoodsId
//         ItemPriceVo resultTargetPriceSum = goodsPriceMapper.getTargetGoodsPriceSum(ilGoodsId, scheduleStartDt);
//         log.info("# 묶음상품 관련 처리 준비 중...");
//
//         if (resultTargetPriceSum != null) {
//           // ############################################################################
//           // TODO 묶음상품 관련 처리 Start
//           //      묶음상품 생성에 의한 할인된 가격을 정상가로 봐야 하는데,
//           //      현재 관련 테이블이나 컬럼이 없음...... 일단  작업 대기
//           //unitFinalVo.setIlItemPriceId(unitItemPriceVo.getIlItemPriceId());           // @품목가격PK
//           // 여러개의 구성상품의 합이므로 품목가격PK는 n개여서 Set 안함
//           //unitFinalVo.setStandardPrice(resultTargetPriceSum.getStandardPrice());        // @원가
//           //unitFinalVo.setRecommendedPrice(resultTargetPriceSum.getRecommendedPrice());  // @정상가
//           // TODO 묶음상품 관련 처리 End
//           // ############################################################################
//         }
//
//       } // End of if (GoodsEnums.GoodsType.PACKAGE.getCode().equals(goodsTp))
//       else {
//         // ------------------------------------------------------------
//         // 일반상품인 경우 -> 상단에서 조회했던 정상가리스트조회 결과 활용
//         // ------------------------------------------------------------
//         // 품목가격 스케줄 loop
//         for (ItemPriceVo unitItemPriceVo : resultListItemPrice) {
//
//           itemStartDt   = unitItemPriceVo.getStartDt();
//           itemEndDt     = unitItemPriceVo.getEndDt();
//           lnItemStartDt = Long.parseLong(itemStartDt);
//           lnItemEndDt   = Long.parseLong(itemEndDt);
//
//           // ------------------------------------------------------------
//           // @[상품가격정보] 스케줄일자가 해당하는 원가/정상가 Set
//           // ------------------------------------------------------------
//           if ((lnScheduleStartDt >= lnItemStartDt) && (lnScheduleEndDt <= lnItemEndDt)) {
//             // 스케줄시작일자 >= 품품시작일자 && 스케줄종료일자 <= 품목종료일자
//
//             unitFinalVo.setIlItemPriceId(unitItemPriceVo.getIlItemPriceId());       // @품목가격PK
//             unitFinalVo.setStandardPrice(unitItemPriceVo.getStandardPrice());       // @원가
//             unitFinalVo.setRecommendedPrice(unitItemPriceVo.getRecommendedPrice()); // @정상가
//           } // End of if (lnScheduleStartDt >= lnItemStartDt)
//
//         } // End of for (ItemPriceVo unitItemPriceVo : resultListItemPrice)
//
//       } // End of if (GoodsEnums.GoodsType.PACKAGE.getCode().equals(goodsTp)) else
//
//     } // End of for (Map<String, String> unitFinal : listScheduleFinal)
//
//   } // End of if (listScheduleFinal != null && listScheduleFinal.size() > 0)
//
//   // ------------------------------------------------------------------
//   // 7.할인가 적용 판매가 스케줄 생성
//   // ------------------------------------------------------------------
//   log.info("# ======================================");
//   log.info("# 7.할인가 적용 판매가 스케줄 생성");
//   log.info("# ======================================");
//   this.setSalePriceByDiscountInfo(listScheduleFinal);
//
//   // ------------------------------------------------------------------
//   // 8.상품가격정보 (재)생성
//   // ------------------------------------------------------------------
//   log.info("# ======================================");
//   log.info("# 8.상품가격정보 (재)생성");
//   log.info("# ======================================");
//   java.sql.Timestamp startTmStamp = new Timestamp(System.currentTimeMillis());
//
//   try {
//     targetGoodsMap.put("goodsPriceProcType", goodsPriceProcType);
//     this.genGoodsPriceInfo(targetGoodsMap, listScheduleFinal, goodsPriceCallType);
//     java.sql.Timestamp endTmStamp = new Timestamp(System.currentTimeMillis());
//     log.info("# TimeStamp[S] ["+ilGoodsId+"]:: " + "["+startTmStamp+"]["+endTmStamp+"]");
//   }
//   catch (Exception e) {
//     log.info("# 가격생성실패 ["+ilGoodsId+"] :: " + e.toString());
//     // 응답오류결과Set
//     resultResDto.setProcErrorCount(resultResDto.getProcErrorCount()+1);
//     GoodsVo errorGoodsVo = new GoodsVo();
//     errorGoodsVo.setIlGoodsId(ilGoodsId);
//     errorGoodsVo.setIlItemCd(ilItemCd);
//     resultResDto.getProcErrorList().add(errorGoodsVo);
//
//     java.sql.Timestamp endTmStamp = new Timestamp(System.currentTimeMillis());
//     log.info("# TimeStamp[F] ["+ilGoodsId+"]:: " + "["+startTmStamp+"]["+endTmStamp+"]");
//     //continue;
//     throw new Exception();
//   }
//
//   // ======================================================================
//   // # 반환
//   // ======================================================================
//
// }

    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // 상품정보조회(ETC)
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    /**
     * 상품가격페이징리스트조회
     * @param goodsPriceRequestDto
     * @return
     * @throws Exception
     */
    protected GoodsPriceResponseDto getGoodsPricePagingList (GoodsPriceRequestDto goodsPriceRequestDto) throws Exception {
      log.info("# ######################################");
      log.info("# GoodsPriceService.getGoodsPricePagingList Start");
      log.info("# ######################################");
      log.info("# In :: " + goodsPriceRequestDto.toString());

      // ========================================================================
      // # 초기화
      // ========================================================================
      GoodsPriceResponseDto resultResDto = new GoodsPriceResponseDto();
      resultResDto.setResultCode(UserEnums.GoodsPrice.GOODS_PRICE_SUCCESS.getCode());
      resultResDto.setResultMessage(UserEnums.GoodsPrice.GOODS_PRICE_SUCCESS.getMessage());
      List<GoodsPriceVo> resultList = null;

      // ========================================================================
      // # 처리
      // ========================================================================
      if("price".equals(goodsPriceRequestDto.getPriceKind()) || "packagePrice".equals(goodsPriceRequestDto.getPriceKind())) {
    	  resultList = goodsPriceMapper.getGoodsPricePagingList(goodsPriceRequestDto);
      }
      else if("employeePrice".equals(goodsPriceRequestDto.getPriceKind())) {
    	  resultList = goodsPriceMapper.getGoodsEmployeePricePagingList(goodsPriceRequestDto);
      }
      resultResDto.setRows(resultList);
      // 전체건수
      resultResDto.setTotal(goodsPriceMapper.selectTotalCount());

      // ========================================================================
      // # 반환
      // ========================================================================
      return resultResDto;

    }

    /**
     * 품목가격페이징리스트조회
     * @param itemPriceRequestDto
     * @return
     * @throws Exception
     */
    protected ItemPriceResponseDto getItemPricePagingList (ItemPriceRequestDto itemPriceRequestDto) throws Exception {
      log.info("# ######################################");
      log.info("# GoodsRegPriceService.getItemPricePagingList Start");
      log.info("# ######################################");
      log.info("# In :: " + itemPriceRequestDto.toString());

      // ========================================================================
      // # 초기화
      // ========================================================================
      ItemPriceResponseDto resultResDto = new ItemPriceResponseDto();
      List<ItemPriceVo> resultList = null;

      // ========================================================================
      // # 처리
      // ========================================================================
      resultList = goodsPriceMapper.getItemPricePagingList(itemPriceRequestDto);
      resultResDto.setRows(resultList);
      // 전체건수
      resultResDto.setTotal(goodsPriceMapper.selectTotalCount());

      // ========================================================================
      // # 반환
      // ========================================================================
      return resultResDto;

    }

    /**
     * 상품할인정보리스트조회
     * @param goodsPriceRequestDto
     * @return
     * @throws Exception
     */
    protected GoodsDiscountResponseDto getGoodsDiscountPagingList (GoodsPriceRequestDto goodsPriceRequestDto) throws Exception {
      log.info("# ######################################");
      log.info("# GoodsPriceService.getGoodsDiscountPagingList Start");
      log.info("# ######################################");
      log.info("# In :: " + goodsPriceRequestDto.toString());

      // ========================================================================
      // # 초기화
      // ========================================================================
      GoodsDiscountResponseDto resultResDto = new GoodsDiscountResponseDto();
      resultResDto.setResultCode(UserEnums.GoodsPrice.GOODS_PRICE_SUCCESS.getCode());
      resultResDto.setResultMessage(UserEnums.GoodsPrice.GOODS_PRICE_SUCCESS.getMessage());
      List<GoodsDiscountVo> resultList = null;

      // ========================================================================
      // # 처리
      // ========================================================================
      resultList = goodsPriceMapper.getGoodsDiscountPagingList(goodsPriceRequestDto);
      resultResDto.setRows(resultList);
      // 전체건수
      resultResDto.setTotal(goodsPriceMapper.selectTotalCount());
      log.info("# result count :: " + resultResDto.toString());
      // ========================================================================
      // # 반환
      // ========================================================================
      return resultResDto;

    }

    /**
     * 임직원 개별할인 정보 변경이력 보기
     * @param String
     * @return
     * @throws Exception
     */
    protected GoodsPackageEmployeeDiscountResponseDto goodsPackageEmployeeDiscountHistoryGridList (GoodsPriceRequestDto goodsPriceRequestDto) throws Exception {
      log.info("# ######################################");
      log.info("# GoodsPriceService.goodsPackageEmployeeDiscountHistoryGridList Start");
      log.info("# ######################################");
      log.info("# In :: " + goodsPriceRequestDto.toString());

      // ========================================================================
      // # 초기화
      // ========================================================================
      GoodsPackageEmployeeDiscountResponseDto resultResDto = new GoodsPackageEmployeeDiscountResponseDto();
      resultResDto.setResultCode(UserEnums.GoodsPrice.GOODS_PRICE_SUCCESS.getCode());
      resultResDto.setResultMessage(UserEnums.GoodsPrice.GOODS_PRICE_SUCCESS.getMessage());
      List<GoodsPriceInfoResultVo> resultList = null;

      // ========================================================================
      // # 처리
      // ========================================================================

      if("price".equals(goodsPriceRequestDto.getHistoryKind())) {
    	  resultList = goodsPriceMapper.goodsPackageEmployeePriceHistroyList(goodsPriceRequestDto);
      }
      else if("discount".equals(goodsPriceRequestDto.getHistoryKind())) {
    	  resultList = goodsPriceMapper.goodsPackageEmployeeDiscountHistoryGridList(goodsPriceRequestDto);
      }
      resultResDto.setRows(resultList);
      // 전체건수
      resultResDto.setTotal(goodsPriceMapper.selectTotalCount() / goodsPriceRequestDto.getBaseRowCount());

      log.info("#### TotalCount : " + resultResDto.getTotal());
      // ========================================================================
      // # 반환
      // ========================================================================
      return resultResDto;

    }

    /**
     * 묶음상품 > 판매 가격정보 > 변경이력 보기 > 상세보기
     * @param String
     * @return
     * @throws Exception
     */
    protected GoodsPackageEmployeeDiscountResponseDto goodsPackageGoodsMappingPrice(String ilGoodsPriceId, String ilGoodsDiscountId) throws Exception {
      log.info("# ######################################");
      log.info("# GoodsPriceService.goodsPackageGoodsMappingPricePopup Start");
      log.info("# ######################################");
      log.info("# In :: " + ilGoodsPriceId);

      // ========================================================================
      // # 초기화
      // ========================================================================
      GoodsPackageEmployeeDiscountResponseDto resultResDto = new GoodsPackageEmployeeDiscountResponseDto();
      resultResDto.setResultCode(UserEnums.GoodsPrice.GOODS_PRICE_SUCCESS.getCode());
      resultResDto.setResultMessage(UserEnums.GoodsPrice.GOODS_PRICE_SUCCESS.getMessage());
      List<GoodsPriceInfoResultVo> resultList = null;

      // ========================================================================
      // # 처리
      // ========================================================================\
      if(!"".equals(ilGoodsPriceId) && "".equals(ilGoodsDiscountId)) {
    	  resultList = goodsPriceMapper.goodsPackageGoodsMappingPrice(ilGoodsPriceId);			//판매 가격정보 > 변경이력 보기 > 상세보기
      }
      else if("".equals(ilGoodsPriceId) && !"".equals(ilGoodsDiscountId)) {
    	  resultList = goodsPriceMapper.goodsPackageGoodsMappingDiscount(ilGoodsDiscountId);	//즉시할인, 기본할인 > 상세내역 > 상세보기
      }
      resultResDto.setRows(resultList);
      // ========================================================================
      // # 반환
      // ========================================================================
      return resultResDto;

    }
}
