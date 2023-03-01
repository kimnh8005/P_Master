package kr.co.pulmuone.v1.goods.price.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.comm.enums.UserEnums;
import kr.co.pulmuone.v1.comm.mapper.goods.price.GoodsPriceMapper;
import kr.co.pulmuone.v1.goods.price.dto.GoodsDiscountResponseDto;
import kr.co.pulmuone.v1.goods.price.dto.GoodsPackageEmployeeDiscountResponseDto;
import kr.co.pulmuone.v1.goods.price.dto.GoodsPriceRequestDto;
import kr.co.pulmuone.v1.goods.price.dto.GoodsPriceResponseDto;
import kr.co.pulmuone.v1.goods.price.dto.ItemPriceRequestDto;
import kr.co.pulmuone.v1.goods.price.dto.ItemPriceResponseDto;
import kr.co.pulmuone.v1.goods.price.dto.vo.GoodsDiscountVo;
import kr.co.pulmuone.v1.goods.price.dto.vo.GoodsPriceVo;
import kr.co.pulmuone.v1.goods.price.dto.vo.GoodsVo;
import kr.co.pulmuone.v1.goods.price.dto.vo.ItemPriceVo;
import kr.co.pulmuone.v1.goods.price.dto.vo.ItemVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
* <PRE>
* Forbiz Korea
* 상품가격스케줄생성 COMMON Impl
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
public class GoodsPriceBizImpl implements GoodsPriceBiz {

  @Autowired
  private GoodsPriceService goodsPriceService;

  private final GoodsPriceMapper goodsPriceMapper;

  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // 상품가격로직
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  /**
   * 상품가격스케줄생성(API:대상일자-N건) : ERP배치에서 품목가격정보 변경 건 존재 시 호출
   */
  @Override
  public GoodsPriceResponseDto genGoodsPriceScheduleForErpBatch (String baseDe) throws Exception {
    log.info("# ######################################");
    log.info("# GoodsPriceBizImpl.genGoodsPriceScheduleForErpBatch Start");
    log.info("# ######################################");
    log.info("# In.baseDe :: " + baseDe);

    // ========================================================================
    // # 초기화
    // ========================================================================
    GoodsPriceResponseDto resultResDto = new GoodsPriceResponseDto();
    resultResDto.setResultCode(UserEnums.GoodsPrice.GOODS_PRICE_SUCCESS.getCode());
    resultResDto.setResultMessage(UserEnums.GoodsPrice.GOODS_PRICE_SUCCESS.getMessage());
    List<ItemVo> itemList = null;

    // ========================================================================
    // # 처리
    // ========================================================================

    // ------------------------------------------------------------------------
    // 1.대상품목리스트조회
    // ------------------------------------------------------------------------
    log.info("# [ERP]1.대상품목리스트조회-대상일자 ");
    itemList = goodsPriceMapper.getTargetItemListByDate(baseDe);

    // ------------------------------------------------------------------------
    // 2.품목리스트로 상품가격생성
    // ------------------------------------------------------------------------
    this.procGoodsPriceScheduleByItem(resultResDto, itemList, baseDe, GoodsEnums.GoodsPriceCallType.BATCH.getCode());

    // ========================================================================
    // # 반환
    // ========================================================================
    return resultResDto;
  }


  /**
   * 상품가격스케줄생성(API:대상일자-N건) : ERP배치에서 올가할인가 변경 건 존재 시 호출
   */
  @Override
  public GoodsPriceResponseDto genGoodsPriceScheduleForOrgaBatch (String baseDe) throws Exception {
    log.info("# ######################################");
    log.info("# GoodsPriceBizImpl.genGoodsPriceScheduleForOrgaBatch Start");
    log.info("# ######################################");
    log.info("# In.baseDe :: " + baseDe);

    // ========================================================================
    // # 초기화
    // ========================================================================
    GoodsPriceResponseDto resultResDto = new GoodsPriceResponseDto();
    resultResDto.setResultCode(UserEnums.GoodsPrice.GOODS_PRICE_SUCCESS.getCode());
    resultResDto.setResultMessage(UserEnums.GoodsPrice.GOODS_PRICE_SUCCESS.getMessage());
    List<GoodsVo> goodsList = null;

    // ========================================================================
    // # 처리
    // ========================================================================

    // ------------------------------------------------------------------------
    // 1.배치에의한 상품가격/할인정보 변경상품리스트 조회 (상품.가격변경처리여부(N))
    // ------------------------------------------------------------------------
    goodsList = goodsPriceMapper.getTargetGoodsListForOrga(baseDe);

    // ------------------------------------------------------------------------
    // 2.상품리스트기준 상품가격생성
    // ------------------------------------------------------------------------
    this.procGoodsPriceScheduleByGoods(resultResDto, goodsList, baseDe, GoodsEnums.GoodsPriceCallType.BATCH.getCode());

    // ========================================================================
    // # 반환
    // ========================================================================
    return resultResDto;
  }


  /**
   * 상품가격스케줄생성(API:품목CD-단건)
   */
  @Override
  public GoodsPriceResponseDto genGoodsPriceScheduleByItemCd (String ilItemCd) throws Exception {
    log.info("# ######################################");
    log.info("# GoodsPriceBizImpl.genGoodsPriceScheduleByItemCd Start");
    log.info("# ######################################");
    log.info("# In.ilItemCd :: " + ilItemCd);

    // ========================================================================
    // # 초기화
    // ========================================================================
    GoodsPriceResponseDto resultResDto = new GoodsPriceResponseDto();
    resultResDto.setResultCode(UserEnums.GoodsPrice.GOODS_PRICE_SUCCESS.getCode());
    resultResDto.setResultMessage(UserEnums.GoodsPrice.GOODS_PRICE_SUCCESS.getMessage());

    // ========================================================================
    // # 처리
    // ========================================================================

    // ------------------------------------------------------------------------
    // 1.대상품목리스트조회
    // ------------------------------------------------------------------------
    List<ItemVo> itemList = new ArrayList<ItemVo>();

    ItemVo itemVo = new ItemVo();
    itemVo.setIlItemCd(ilItemCd);
    itemList.add(itemVo);

    // ----------------------------------------------------------------------
    // 2.품목리스트로 상품가격생성
    // ----------------------------------------------------------------------
    this.procGoodsPriceScheduleByItem(resultResDto, itemList, "", GoodsEnums.GoodsPriceCallType.ONLINE.getCode());

    // ======================================================================
    // # 반환
    // ======================================================================
    return resultResDto;
  }


  /**
   * 상품가격스케줄생성(API:상품ID-단건)
   */
  @Override
  public GoodsPriceResponseDto genGoodsPriceScheduleByGoodsId (String ilGoodsId) throws Exception {
    log.info("# ######################################");
    log.info("# GoodsPriceBizImpl.genGoodsPriceScheduleByGoodsId Start");
    log.info("# ######################################");
    log.info("# In.ilGoodsId :: " + ilGoodsId);

    // ========================================================================
    // # 초기화
    // ========================================================================
    GoodsPriceResponseDto resultResDto = new GoodsPriceResponseDto();
    resultResDto.setResultCode(UserEnums.GoodsPrice.GOODS_PRICE_SUCCESS.getCode());
    resultResDto.setResultMessage(UserEnums.GoodsPrice.GOODS_PRICE_SUCCESS.getMessage());

    // ========================================================================
    // # 처리
    // ========================================================================

    // ------------------------------------------------------------------------
    // 1.대상품목리스트조회
    // ------------------------------------------------------------------------
    List<GoodsVo> goodsList = new ArrayList<GoodsVo>();

    GoodsVo goodsVo = new GoodsVo();
    goodsVo.setIlGoodsId(ilGoodsId);
    goodsList.add(goodsVo);

    // ------------------------------------------------------------------------
    // 2.품목리스트로 상품가격생성
    // ------------------------------------------------------------------------
    this.procGoodsPriceScheduleByGoods(resultResDto, goodsList, "", GoodsEnums.GoodsPriceCallType.ONLINE.getCode());

    // ========================================================================
    // # 반환
    // ========================================================================
    return resultResDto;
  }

  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // 가격생성
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  /**
   * 상품ID리스트(단건포함)로 상품가격생성
   * @param resultResDto
   * @param goodsList           : 처리대상상품리스트
   * @param baseDe              : 처리대상일자
   * @param goodsPriceCallType  : 상품가격호출구분(배치/온라인)
   * @throws Exception
   */
  @SuppressWarnings("unused")
  private void procGoodsPriceScheduleByGoods (GoodsPriceResponseDto resultResDto, List<GoodsVo> goodsList, String baseDe, String goodsPriceCallType) throws Exception {
    log.info("# ######################################");
    log.info("# GoodsPriceBizImpl.procGoodsPriceScheduleByGoods Start");
    log.info("# ######################################");
    log.info("# In.baseDe :: " + baseDe);

    // ========================================================================
    // # 초기화
    // ========================================================================
    GoodsPriceResponseDto resultUnPackageResDto = null;
    GoodsPriceResponseDto resultPackageResDto = null;

    List<Map<String, String>> targetGoodsListUnPackage  = null;
    List<Map<String, String>> targetGoodsListPackage    = null;

    // ========================================================================
    // # 처리
    // ========================================================================
    if (goodsList != null && goodsList.size() > 0) {

      // ----------------------------------------------------------------------
      // 2.대상상품리스트조회  (묶음상품리스트, 일반상품리스트로 구분 구성)
      //   * targetGoodsListUnPackage, targetGoodsListPackage
      //     - map : "ilGoodsId", "ilItemCd", "goodsTp"
      // ----------------------------------------------------------------------
      targetGoodsListUnPackage  = new ArrayList<Map<String, String>>();
      targetGoodsListPackage    = new ArrayList<Map<String, String>>();

      for (GoodsVo unitGoodsVo : goodsList) {

        goodsPriceService.getTargetGoodsListByGoodsId(unitGoodsVo, baseDe, targetGoodsListUnPackage, targetGoodsListPackage);
      }

      // 오류처리
      if (targetGoodsListUnPackage.size() + targetGoodsListPackage.size() <= 0) {
        log.info("# GoodsList.size is 0");
        resultResDto.setResultCode(UserEnums.GoodsPrice.GOODS_PRICE_GOODS_LIST_NO_EXIST.getCode());
        resultResDto.setResultMessage(UserEnums.GoodsPrice.GOODS_PRICE_GOODS_LIST_NO_EXIST.getMessage());
        return;
      }

      // ----------------------------------------------------------------------
      // 3.상품가격생성처리
      // ----------------------------------------------------------------------
      // ----------------------------------------------------------------------
      // 3.1.상품가격생성처리 - 일반상품처리
      // ----------------------------------------------------------------------
      if (targetGoodsListUnPackage != null && targetGoodsListUnPackage.size() > 0) {
        // 상품가격처리구분(품목, *상품), 상품구분(*일반상품, 묶음상품), 상품리스트(*일반, 묶음)
        resultUnPackageResDto = this.procGoodsPriceScheduleCall(resultResDto, "", GoodsEnums.GoodsPriceProcType.GOODS.getCode(), goodsPriceCallType, targetGoodsListUnPackage);
      }

      // ----------------------------------------------------------------------
      // 3.2.상품가격생성처리 - 묶음상품처리
      // ----------------------------------------------------------------------
      // TODO 묶음상품 정의되면 주석 풀로 genGoodsPriceScheduleProc 로직 마저 구현할 것 Start
      // 묶음상품의 시작은 여기서 시작할 것, targetGoodsListPackage 이넘이 묶음상품 리스트임
      //if (targetGoodsListPackage != null && targetGoodsListPackage.size() > 0) {
      //  // 상품가격처리구분(품목, *상품), 상품구분(일반상품, *묶음상품), 상품리스트(일반, *묶음)
      //  resultPackageResDto = this.procGoodsPriceScheduleCall(resultResDto, GoodsEnums.GoodsType.PACKAGE.getCode(), GoodsEnums.GoodsPriceProcType.GOODS.getCode(), goodsPriceCallType, targetGoodsListPackage);
      //  // 작업전 버전
      //  //resultUnPackageResDto = this.genGoodsPriceScheduleProc(GoodsEnums.GoodsType.PACKAGE.getCode(), GoodsEnums.GoodsPriceProcType.GOODS.getCode(), goodsPriceCallType, targetGoodsListPackage);
      //}
      // TODO 묶음상품 정의되면 주석 풀로 genGoodsPriceScheduleProc 로직 마저 구현할 것 End

      // ----------------------------------------------------------------------
      // 4.오류결과 응답 Set
      // ----------------------------------------------------------------------
      if (resultUnPackageResDto != null) {

        resultResDto.setProcErrorCount(resultUnPackageResDto.getProcErrorCount());

        if (resultUnPackageResDto.getProcErrorList().size() > 0) {
          resultResDto.getProcErrorList().addAll(resultUnPackageResDto.getProcErrorList());
        }
      }
      if (resultPackageResDto != null) {

        resultResDto.setProcErrorCount(resultResDto.getProcErrorCount() + resultPackageResDto.getProcErrorCount());

        if (resultPackageResDto.getProcErrorList().size() > 0) {
          resultResDto.getProcErrorList().addAll(resultPackageResDto.getProcErrorList());
        }
      }

    } // End of if (goodsList != null && goodsList.size() > 0)
    else {
      log.info("# goodsList.size is 0");
      resultResDto.setResultCode(UserEnums.GoodsPrice.GOODS_PRICE_GOODS_NO_EXIST.getCode());
      resultResDto.setResultMessage(UserEnums.GoodsPrice.GOODS_PRICE_GOODS_NO_EXIST.getMessage());
      return;
    }

    // ======================================================================
    // # 반환
    // ======================================================================
    return;

  }

  /**
   * 품목CD리스트(단건포함)로 상품가격생성
   * @param itemList
   * @param baseDe
   * @return
   * @throws Exception
   */
  @SuppressWarnings("unused")
  private void procGoodsPriceScheduleByItem (GoodsPriceResponseDto resultResDto, List<ItemVo> itemList, String baseDe, String goodsPriceCallType) throws Exception {
    log.info("# ######################################");
    log.info("# GoodsPriceBizImpl.procGoodsPriceScheduleByItem Start");
    log.info("# ######################################");
    log.info("# In.itemList :: " + itemList.toString());

    // ========================================================================
    // # 초기화
    // ========================================================================
    GoodsPriceResponseDto resultUnPackageResDto = null;
    GoodsPriceResponseDto resultPackageResDto = null;
    List<Map<String, String>> targetGoodsListUnPackage  = null;
    List<Map<String, String>> targetGoodsListPackage    = null;

    // ========================================================================
    // # 처리
    // ========================================================================
    if (itemList != null && itemList.size() > 0) {

      // ----------------------------------------------------------------------
      // 2.대상상품리스트조회-품목CD : 상품리스트를 모두 조회 후 일반상품, 묶음상품 순으로 처리한다.
      //   * targetGoodsListUnPackage, targetGoodsListPackage
      //     - map : "ilGoodsId", "ilItemCd", "goodsTp"
      // ----------------------------------------------------------------------
      log.info("# [ERP]2.대상상품리스트조회-품목CD");
      targetGoodsListUnPackage  = new ArrayList<Map<String, String>>();
      targetGoodsListPackage    = new ArrayList<Map<String, String>>();
      List<String> ilItemCdList = new ArrayList<String>();

      for (ItemVo unitItemVo : itemList) {
        ilItemCdList.add(unitItemVo.getIlItemCd());
      }
      goodsPriceService.getTargetGoodsListByItemCd(ilItemCdList, baseDe, targetGoodsListUnPackage, targetGoodsListPackage);

      // 오류처리
      if (targetGoodsListUnPackage.size() + targetGoodsListPackage.size() <= 0) {
        log.info("# GoodsList.size is 0");
        resultResDto.setResultCode(UserEnums.GoodsPrice.GOODS_PRICE_GOODS_LIST_NO_EXIST.getCode());
        resultResDto.setResultMessage(UserEnums.GoodsPrice.GOODS_PRICE_GOODS_LIST_NO_EXIST.getMessage());
        return;
      }

      // ----------------------------------------------------------------------
      // 3.상품가격생성처리
      // ----------------------------------------------------------------------
      // ----------------------------------------------------------------------
      // 3.1.상품가격생성처리 - 일반상품처리
      // ----------------------------------------------------------------------
      log.info("# [ERP]3.1.상품가격생성처리 - 일반상품처리");
      if (targetGoodsListUnPackage != null && targetGoodsListUnPackage.size() > 0) {
        // 상품가격처리구분(*품목, 상품), 상품구분(*일반상품, 묶음상품), 상품리스트(*일반, 묶음)
        resultUnPackageResDto = this.procGoodsPriceScheduleCall(resultResDto, "", GoodsEnums.GoodsPriceProcType.ITEM.getCode(), goodsPriceCallType, targetGoodsListUnPackage);
      }

      // ----------------------------------------------------------------------
      // 3.2.상품가격생성처리 - 묶음상품처리
      // ----------------------------------------------------------------------
      log.info("# [ERP]3.2.상품가격생성처리 - 묶음상품처리");
      // TODO 묶음상품 정의되면 주석 풀로 genGoodsPriceScheduleProc 로직 마저 구현할 것 Start
      //if (targetGoodsListPackage != null && targetGoodsListPackage.size() > 0) {
      //  // 상품가격처리구분(*품목, 상품), 상품구분(일반상품, *묶음상품), 상품리스트(일반, *묶음)
      //  resultPackageResDto = this.procGoodsPriceScheduleCall(resultResDto, GoodsEnums.GoodsType.PACKAGE.getCode(), GoodsEnums.GoodsPriceProcType.GOODS.getCode(), goodsPriceCallType, targetGoodsListPackage);
      //  // 작업전 버전
      //  //resultPackageResDto = this.genGoodsPriceScheduleProc(GoodsEnums.GoodsType.PACKAGE.getCode(), GoodsEnums.GoodsPriceProcType.ITEM.getCode(), goodsPriceCallType, targetGoodsListPackage);
      //}
      // TODO 묶음상품 정의되면 주석 풀로 genGoodsPriceScheduleProc 로직 마저 구현할 것 End

      // ----------------------------------------------------------------------
      // 4.오류결과 응답 Set
      // ----------------------------------------------------------------------
      if (resultUnPackageResDto != null) {

        resultResDto.setProcErrorCount(resultUnPackageResDto.getProcErrorCount());

        if (resultUnPackageResDto.getProcErrorList().size() > 0) {
          resultResDto.getProcErrorList().addAll(resultUnPackageResDto.getProcErrorList());
        }
      }
      if (resultPackageResDto != null) {

        resultResDto.setProcErrorCount(resultResDto.getProcErrorCount() + resultPackageResDto.getProcErrorCount());

        if (resultPackageResDto.getProcErrorList().size() > 0) {
          resultResDto.getProcErrorList().addAll(resultPackageResDto.getProcErrorList());
        }
      }

    } // End of if (itemList != null && itemList.size() > 0)
    else {
      log.info("# itemList.size is 0");
      resultResDto.setResultCode(UserEnums.GoodsPrice.GOODS_PRICE_ITEM_NO_EXIST.getCode());
      resultResDto.setResultMessage(UserEnums.GoodsPrice.GOODS_PRICE_ITEM_NO_EXIST.getMessage());
      return;
    }

    // ========================================================================
    // # 반환
    // ========================================================================
    return;
  }

  /**
   * 상품가격생성 처리
   * @param resultResDto
   * @param goodsTp
   * @param goodsPriceProcType  : 상품가격처리구분 (품목/상품)
   * @param goodsPriceCallType  : 상품가격호출구분 (배치/온라인)
   * @param inGoodsList
   * @return
   * @throws Exception
   */
  @SuppressWarnings("unused")
  private GoodsPriceResponseDto procGoodsPriceScheduleCall (GoodsPriceResponseDto resultResDto, String goodsTp, String goodsPriceProcType, String goodsPriceCallType, List<Map<String, String>> inGoodsList) throws Exception {
    log.info("# ######################################");
    log.info("# GoodsPriceBizImpl.procGoodsPriceScheduleCall Start");
    log.info("# ######################################");
    log.info("# In.inGoodsList.size :: " + inGoodsList.size());
    log.info("# In.inGoodsList :: " + inGoodsList);

    // ========================================================================
    // # 초기화
    // ========================================================================
    List<ItemPriceVo> resultListItemPrice = null;
    List<GoodsDiscountVo> resultListDiscount = null;


    //List<Map<String, String>> listSchedule = new ArrayList<Map<String, String>>();
    List<GoodsPriceVo> listSchedule = null;
    GoodsPriceVo goodsPriceVo = null;
    List<GoodsPriceVo> listScheduleFinal = null;

    // ========================================================================
    // # 처리
    // ========================================================================
    if (inGoodsList != null && inGoodsList.size() > 0) {

      String ilGoodsId = "";
      String ilItemCd  = "";
      // ----------------------------------------------------------------------
      // 0.상품별 Loop
      // ----------------------------------------------------------------------
      //log.info("# inGoods :: " + inGoodsList.toString());

      // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      for (Map<String, String> targetGoodsMap : inGoodsList) {

        try {
          // ------------------------------------------------------------------
          // 상품별 상품가격스케줄 생성
          // ------------------------------------------------------------------
          this.procGoodsPriceScheduleGen (targetGoodsMap, goodsTp, goodsPriceProcType, goodsPriceCallType, resultResDto);
        }
        catch (Exception e) {
          // 오류가 나면 다음 상품 loop 진행
          log.info("# porcGoodsPriceSchedule Exception [ilGoodsId] :: " + targetGoodsMap.get("ilGoodsId") + " : " + e.toString());
          //e.printStackTrace();
          continue;
        }

      } // End of for (String ilGoodsId : inGoodsList)
      // End of 0.상품별 Loop
      // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    } // End of if (inGoodsList != null && inGoodsList.size() > 0)

    // ========================================================================
    // # 반환
    // ========================================================================
    return resultResDto;
  }

  /**
   * 상품별 상품가격스케줄 생성
   * @param targetGoodsMap
   * @param goodsTp
   * @param goodsPriceProcType  : 상품가격처리구분 (품목/상품)
   * @param goodsPriceCallType  : 상품가격호출구분 (배치/온라인)
   * @param resultResDto
   * @throws Exception
   */
  private void procGoodsPriceScheduleGen (Map<String, String> targetGoodsMap, String goodsTp, String goodsPriceProcType, String goodsPriceCallType, GoodsPriceResponseDto resultResDto)  throws Exception {
    log.info("# ######################################");
    log.info("# GoodsPriceBizImpl.procGoodsPriceScheduleGen Start");
    log.info("# ######################################");
    log.info("# In.targetGoodsMap :: ["+goodsTp+"]["+goodsPriceProcType+"]["+targetGoodsMap.toString()+"]");

    // ========================================================================
    // # 초기화
    // ========================================================================
    List<GoodsPriceVo> listSchedule = null;
    String ilGoodsId  = "";
    String ilItemCd   = "";

    // ========================================================================
    // # 처리
    // ========================================================================

    listSchedule      = new ArrayList<GoodsPriceVo>();

    // ------------------------------------------------------------------------
    // 1.품목가격구간설정 : 품목정보조회 > 시작일/종료일정보
    // ------------------------------------------------------------------------
    log.info("# ======================================");
    log.info("# Step1.품목가격구간설정 - ilGoodsId :: ["+targetGoodsMap.get("ilGoodsId")+"]["+goodsTp+"]");
    log.info("# ======================================");
    ilGoodsId = targetGoodsMap.get("ilGoodsId");
    ilItemCd  = targetGoodsMap.get("ilItemCd");

    // ------------------------------------------------------------------------
    // 1~3.간소화 : 품목구간+할인구간 의 시작일자 리스트 조회
    // ------------------------------------------------------------------------
    if (GoodsEnums.GoodsType.PACKAGE.getCode().equals(goodsTp)) {
      // ----------------------------------------------------------------------
      // 묶음상품이면 -> 묶음상품의 구성상품 전체의 가격구간리스트를 가져옴
      // ----------------------------------------------------------------------
      // 구성상품 전체의 구간들의 합집합
// TODO 묶음상품의 쿼리 수정 및 추가 로직 필요
      listSchedule = goodsPriceMapper.getGoodsPriceItemDiscountPeriodListForPackage(ilGoodsId);
    }
    else {
      // ----------------------------------------------------------------------
      // 묶음상품이 아니면 -> 입력상품의 가격리스트를 가져옴
      // ----------------------------------------------------------------------
      listSchedule = goodsPriceMapper.getGoodsPriceItemDiscountPeriodList(ilGoodsId);
    }

    // ------------------------------------------------------------------------
    // 2.종료일자 Set
    // ------------------------------------------------------------------------
    log.info("# ======================================");
    log.info("# Step2.종료일자 Set");
    log.info("# ======================================");
    String endDtBefore1Sec = "";
    Date date = null;
    SimpleDateFormat sdFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    Calendar cal = Calendar.getInstance();
    log.info("# listSchedule.size() :: " + listSchedule.size());

    for (int i = 0; i < listSchedule.size(); i++) {

      if (i > 0) {
        // (시작일자-1초)를 앞 loop의 종료일자로 Set
        date = sdFormat.parse(listSchedule.get(i).getPriceStartDt());
        cal.setTime(date);
        cal.add(Calendar.SECOND, -1); // 1초전
        endDtBefore1Sec = sdFormat.format(cal.getTime());
        // --------------------------------------------------------------------
        // @[상품가격정보] 종종료일자 Set : 이전 loop에 Set
        // --------------------------------------------------------------------
        listSchedule.get(i-1).setPriceEndDt(endDtBefore1Sec);

      } // End of if (i > 0)

    } // End of for (int i = 0; i < listSchedule.size(); i++)

    // ------------------------------------------------------------------------
    // @[상품가격정보] 마지막 종료일자  Set
    // ------------------------------------------------------------------------
    log.info("# listSchedule.size() :: " + listSchedule.size());
    listSchedule.get(listSchedule.size()-1).setPriceEndDt("29991231235959");

    // ------------------------------------------------------------------------
    // 3.할인가 적용 판매가 스케줄 생성
    // ------------------------------------------------------------------------
    log.info("# ======================================");
    log.info("# Step3.할인가 적용 판매가 스케줄 생성");
    log.info("# ======================================");
    goodsPriceService.setSalePriceByDiscountInfo(listSchedule);

    // ------------------------------------------------------------------------
    // 4.상품가격정보 (재)생성
    // ------------------------------------------------------------------------
    log.info("# ======================================");
    log.info("# Step4.상품가격정보 (재)생성");
    log.info("# ======================================");
    java.sql.Timestamp startTmStamp = new Timestamp(System.currentTimeMillis());

    try {
      targetGoodsMap.put("goodsPriceProcType", goodsPriceProcType);
      // @Transactional Call
      goodsPriceService.genGoodsPriceInfo(targetGoodsMap, listSchedule, goodsPriceCallType);
      java.sql.Timestamp endTmStamp = new Timestamp(System.currentTimeMillis());
      log.info("# TimeStamp[S] ["+ilGoodsId+"]:: " + "["+startTmStamp+"]["+endTmStamp+"]");
    }
    catch (Exception e) {
      log.info("# 가격생성실패 ["+ilGoodsId+"] :: " + e.toString());
      // 응답오류결과Set
      resultResDto.setProcErrorCount(resultResDto.getProcErrorCount()+1);
      GoodsVo errorGoodsVo = new GoodsVo();
      errorGoodsVo.setIlGoodsId(ilGoodsId);
      errorGoodsVo.setIlItemCd(ilItemCd);
      resultResDto.getProcErrorList().add(errorGoodsVo);

      java.sql.Timestamp endTmStamp = new Timestamp(System.currentTimeMillis());
      log.info("# TimeStamp[F] ["+ilGoodsId+"]:: " + "["+startTmStamp+"]["+endTmStamp+"]");
      //continue;
      throw new Exception();
    }

    // ========================================================================
    // # 반환
    // ========================================================================

  }


  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // 상품정보조회(ETC)
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  /**
   * 상품가격페이징리스트조회
   */
  @Override
  public GoodsPriceResponseDto getGoodsPricePagingList (GoodsPriceRequestDto goodsPriceRequestDto) throws Exception {
    log.info("# ######################################");
    log.info("# GoodsPriceBizImpl.getGoodsPricePagingList Start");
    log.info("# ######################################");
    log.info("# In :: " + goodsPriceRequestDto.toString());

    return goodsPriceService.getGoodsPricePagingList(goodsPriceRequestDto);
  }

  /**
   * 품목가격페이징리스트조회
   */
  @Override
  public ItemPriceResponseDto getItemPricePagingList (ItemPriceRequestDto itemPriceRequestDto)  throws Exception {
    log.info("# ######################################");
    log.info("# GoodsPriceBizImpl.getItemPriceList Start");
    log.info("# ######################################");
    log.info("# In :: " + itemPriceRequestDto);

    return goodsPriceService.getItemPricePagingList(itemPriceRequestDto);
  }

  /**
   * 상품할인정보리스트조회
   */
  @Override
  public GoodsDiscountResponseDto getGoodsDiscountPagingList (GoodsPriceRequestDto goodsPriceRequestDto) throws Exception {
    log.info("# ######################################");
    log.info("# GoodsPriceBizImpl.getGoodsDiscountPagingList Start");
    log.info("# ######################################");
    log.info("# In :: " + goodsPriceRequestDto.toString());

    return goodsPriceService.getGoodsDiscountPagingList(goodsPriceRequestDto);
  }

  /**
   * 임직원 개별할인 정보 변경이력 보기
   */
  @Override
  public GoodsPackageEmployeeDiscountResponseDto goodsPackageEmployeeDiscountHistoryGridList (GoodsPriceRequestDto goodsPriceRequestDto) throws Exception {
    log.info("# ######################################");
    log.info("# GoodsPriceBizImpl.goodsPackageEmployeeDiscountHistoryGridList Start");
    log.info("# ######################################");
    log.info("# In :: " + goodsPriceRequestDto.toString());

    return goodsPriceService.goodsPackageEmployeeDiscountHistoryGridList(goodsPriceRequestDto);
  }

  /**
   * 묶음상품 > 판매 가격정보 > 변경이력 보기 > 상세보기
   */
  @Override
  public GoodsPackageEmployeeDiscountResponseDto goodsPackageGoodsMappingPrice(String ilGoodsPriceId, String ilGoodsDiscountId) throws Exception {
    log.info("# ######################################");
    log.info("# GoodsPriceBizImpl.goodsPackageGoodsMappingPricePopup Start");
    log.info("# ######################################");
    log.info("# In :: " + ilGoodsPriceId);

    return goodsPriceService.goodsPackageGoodsMappingPrice(ilGoodsPriceId, ilGoodsDiscountId);
  }


}
