package kr.co.pulmuone.v1.display.manage.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.DisplayEnums;
import kr.co.pulmuone.v1.comm.enums.UserEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.util.JsonUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.display.manage.dto.DisplayContsResponseDto;
import kr.co.pulmuone.v1.display.manage.dto.DisplayInventoryResponseDto;
import kr.co.pulmuone.v1.display.manage.dto.DisplayManageRequestDto;
import kr.co.pulmuone.v1.display.manage.dto.DisplayPageResponseDto;
import kr.co.pulmuone.v1.display.manage.dto.vo.ContsVo;
import kr.co.pulmuone.v1.display.manage.dto.vo.PageVo;
import kr.co.pulmuone.v1.system.basic.dto.GetEnvironmentListRequestDto;
import kr.co.pulmuone.v1.system.basic.dto.vo.GetEnvironmentListResultVo;
import kr.co.pulmuone.v1.system.basic.service.SystemBasicEnvironmentBiz;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
* <PRE>
* Forbiz Korea
* 전시관리 COMMON Impl
*
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2020.09.23.    dgyoun   최초작성
* =======================================================================
* </PRE>
*/

@Slf4j
@Service
@RequiredArgsConstructor
public class DisplayManageBizImpl implements DisplayManageBiz {

  @Autowired
  private DisplayManageService displayManageService;

  @Autowired
  private SystemBasicEnvironmentBiz systemBasicEnvironmentBiz;

  //호스트
  @Value("${resource.server.url.mall}")
  private String hostUrl; // public 저장소 접근 url

  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // 전시페이지관리
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  /**
   * 전시페이지 리스트조회
   */
  @Override
  public ApiResult<?> selectDpPageList (DisplayManageRequestDto displayManageRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# DisplayManageBizImpl.selectDpPageList Start");
    log.debug("# ######################################");
    if (displayManageRequestDto != null) {
      log.debug("# In.displayManageRequestDto :: " + displayManageRequestDto.toString());
    }
    else {
      log.debug("# In.displayManageRequestDto is Null");
    }

    DisplayPageResponseDto result = displayManageService.selectDpPageList(displayManageRequestDto.getDpPageId(), displayManageRequestDto.getUseAllYn());

    return ApiResult.success(result);
  }

  /**
   * 전시카테고리 리스트조회
   */
  @Override
  public ApiResult<?> selectDpCategoryList (DisplayManageRequestDto displayManageRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# DisplayManageBizImpl.selectDpCategoryList Start");
    log.debug("# ######################################");
    if (displayManageRequestDto != null) {
      log.debug("# In.displayManageRequestDto :: " + displayManageRequestDto.toString());
    }
    else {
      log.debug("# In.displayManageRequestDto is Null");
    }

    DisplayPageResponseDto result = displayManageService.selectDpCategoryList(displayManageRequestDto);

    return ApiResult.success(result);
  }

  /**
   * 페이지 상세조회
   */
  @Override
  public ApiResult<?> selectPageInfo (DisplayManageRequestDto displayManageRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# DisplayManageBizImpl.selectPageInfo Start");
    log.debug("# ######################################");
    if (displayManageRequestDto != null) {
      log.debug("# In.displayManageRequestDto :: " + displayManageRequestDto.toString());
    }
    else {
      log.debug("# In.displayManageRequestDto is Null");
    }

    DisplayPageResponseDto result = displayManageService.selectPageInfo(displayManageRequestDto.getDpPageId());

    return ApiResult.success(result);
  }

  /**
   * 페이지 수정
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
  public ApiResult<?> putPage (DisplayManageRequestDto displayManageRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# DisplayManageBizImpl.selectPageInfo Start");
    log.debug("# ######################################");
    if (displayManageRequestDto != null) {
      log.debug("# In.displayManageRequestDto :: " + displayManageRequestDto.toString());
    }
    else {
      log.debug("# In.displayManageRequestDto is Null");
    }

    try {

      DisplayPageResponseDto result = displayManageService.putPage(displayManageRequestDto.getPageInfo());
      return ApiResult.success(result);
    }
    catch (BaseException be) {
      return ApiResult.result(be.getMessageEnum());
    }
//    catch (Exception e) {
//      log.debug("");
//      return ApiResult.fail();
//    }

  }

  /**
   * 페이지 순서변경
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
  public ApiResult<?> putPageSort (DisplayManageRequestDto displayManageRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# DisplayManageBizImpl.putPageSort Start");
    log.debug("# ######################################");
    if (displayManageRequestDto != null) {
      log.debug("# In.displayManageRequestDto :: " + displayManageRequestDto.toString());
    }
    else {
      log.debug("# In.displayManageRequestDto is Null");
    }

    DisplayPageResponseDto result = displayManageService.putPageSort(displayManageRequestDto.getPageList());

    return ApiResult.success(result);
  }

  /**
   * 페이지 삭제
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
  public ApiResult<?> delPage (DisplayManageRequestDto displayManageRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# DisplayManageBizImpl.delPage Start");
    log.debug("# ######################################");
    if (displayManageRequestDto != null) {
      log.debug("# In.displayManageRequestDto :: " + displayManageRequestDto.toString());
    }
    else {
      log.debug("# In.displayManageRequestDto is Null");
    }

    // 다건 구조에 맞춤 (현재 단건만 처리)
    List<PageVo> pageList = new ArrayList<PageVo>();
    pageList.add(displayManageRequestDto.getPageInfo());
    displayManageRequestDto.setPageList(pageList);

    DisplayPageResponseDto result = displayManageService.delPage(displayManageRequestDto.getPageList());

    return ApiResult.success(result);
  }

  /**
   * 페이지 등록
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
  public ApiResult<?> addPage (DisplayManageRequestDto displayManageRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# DisplayManageBizImpl.addPage Start");
    log.debug("# ######################################");
    if (displayManageRequestDto != null) {
      log.debug("# In.displayManageRequestDto :: " + displayManageRequestDto.toString());
    }
    else {
      log.debug("# In.displayManageRequestDto is Null");
    }
    DisplayPageResponseDto result = displayManageService.addPage(displayManageRequestDto.getPageInfo());

    return ApiResult.success(result);

  }

  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // 전시인벤토리관리
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  /**
   * 인벤토리 리스트조회
   */
  @Override
  public ApiResult<?> selectInventoryList (DisplayManageRequestDto displayManageRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# DisplayManageBizImpl.selectInventoryList Start");
    log.debug("# ######################################");
    if (displayManageRequestDto != null) {
      log.debug("# In.displayManageRequestDto :: " + displayManageRequestDto.toString());
    }
    else {
      log.debug("# In.displayManageRequestDto is Null");
    }

    DisplayInventoryResponseDto result = displayManageService.selectInventoryList(displayManageRequestDto);

    return ApiResult.success(result);
  }

  /**
   * 인벤토리 상세조회
   */
  @Override
  public ApiResult<?> selectInventoryInfo (DisplayManageRequestDto displayManageRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# DisplayManageBizImpl.selectInventoryInfo Start");
    log.debug("# ######################################");
    if (displayManageRequestDto != null) {
      log.debug("# In.displayManageRequestDto :: " + displayManageRequestDto.toString());
    }
    else {
      log.debug("# In.displayManageRequestDto is Null");
    }

    DisplayInventoryResponseDto result = displayManageService.selectInventoryInfo(displayManageRequestDto.getDpInventoryId());

    return ApiResult.success(result);
  }

  /**
   * 인벤토리 수정
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
  public ApiResult<?> putInventory (DisplayManageRequestDto displayManageRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# DisplayManageBizImpl.putInventory Start");
    log.debug("# ######################################");
    if (displayManageRequestDto != null) {
      log.debug("# In.displayManageRequestDto :: " + displayManageRequestDto.toString());
    }
    else {
      log.debug("# In.displayManageRequestDto is Null");
    }

    DisplayInventoryResponseDto result = displayManageService.putInventory(displayManageRequestDto.getInventoryInfo());

    return ApiResult.success(result);
  }

  /**
   * 인벤토리 순서변경
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
  public ApiResult<?> putInventorySort (DisplayManageRequestDto displayManageRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# DisplayManageBizImpl.putInventorySort Start");
    log.debug("# ######################################");
    if (displayManageRequestDto != null) {
      log.debug("# In.displayManageRequestDto :: " + displayManageRequestDto.toString());
    }
    else {
      log.debug("# In.displayManageRequestDto is Null");
    }

    DisplayInventoryResponseDto result = displayManageService.putInventorySort(displayManageRequestDto.getInventoryList());

    return ApiResult.success(result);
  }

  /**
   * 인벤토리 삭제
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
  public ApiResult<?> delInventory (DisplayManageRequestDto displayManageRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# DisplayManageBizImpl.delInventory Start");
    log.debug("# ######################################");
    if (displayManageRequestDto != null) {
      log.debug("# In.displayManageRequestDto :: " + displayManageRequestDto.toString());
    }
    else {
      log.debug("# In.displayManageRequestDto is Null");
    }

    DisplayInventoryResponseDto result = displayManageService.delInventory(displayManageRequestDto.getInventoryList());

    return ApiResult.success(result);
  }

  /**
   * 인벤토리 등록
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
  public ApiResult<?> addInventory (DisplayManageRequestDto displayManageRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# DisplayManageBizImpl.addInventory Start");
    log.debug("# ######################################");
    if (displayManageRequestDto != null) {
      log.debug("# In.displayManageRequestDto :: " + displayManageRequestDto.toString());
    }
    else {
      log.debug("# In.displayManageRequestDto is Null");
    }

    DisplayInventoryResponseDto result = displayManageService.addInventory(displayManageRequestDto.getInventoryInfo());

    return ApiResult.success(result);
  }

  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // 전시컨텐츠관리
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  /**
   * 전시컨텐츠 리스트조회
   */
  @Override
  public ApiResult<?> selectDpContsList (DisplayManageRequestDto displayManageRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# DisplayManageBizImpl.selectDpContsList Start");
    log.debug("# ######################################");
    if (displayManageRequestDto != null) {
      log.debug("# In.displayManageRequestDto :: " + displayManageRequestDto.toString());
    }
    else {
      log.debug("# In.displayManageRequestDto is Null");
    }

    // ------------------------------------------------------------------------
    // # 초기화
    // ------------------------------------------------------------------------
    String goodsDetailUrl = "";

    // ----------------------------------------------------------------------
    // 1.링크URL조회
    // ----------------------------------------------------------------------
    if (DisplayEnums.ContentsType.GOODS.getCode().equals(StringUtil.nvl(displayManageRequestDto.getContsTp()))) {
      // 컨텐츠유형이 상품인 경우

      try {

        // 호스트URL
        displayManageRequestDto.setHostUrl(hostUrl);;

        // 상품상세주소
        GetEnvironmentListRequestDto getEnvironmentListRequestDto = new GetEnvironmentListRequestDto();
        getEnvironmentListRequestDto.setSearchEnvironmentKey("URL_FRONT_GOODS_DETAIL");
        log.debug("# 상품상세주소 조회 Start");
        GetEnvironmentListResultVo resultEnv = systemBasicEnvironmentBiz.getEnvironment(getEnvironmentListRequestDto);

        if (resultEnv != null) {
          goodsDetailUrl = resultEnv.getEnvironmentValue();
          displayManageRequestDto.setGoodsDetailUrl(goodsDetailUrl);
        }
        else {
          log.debug("# resultEnv is Null");
        }
      }
      catch (Exception e) {
        throw new BaseException(e.toString());
      }
    } // End of f (DisplayEnums.ContentsType.GOODS.getCode().equals(StringUtil.nvl(displayManageRequestDto.getContsTp())))

log.info("# hostUrl         :: " + hostUrl);
log.info("# goodsDetailUrl  :: " + displayManageRequestDto.getGoodsDetailUrl());
    // ------------------------------------------------------------------------
    // 2.컨텐츠리스트조회
    // ------------------------------------------------------------------------
    //DisplayContsResponseDto result = displayManageService.selectDpContsList(displayManageRequestDto.getDpInventoryId(), displayManageRequestDto.getPrntsContsId(), displayManageRequestDto.getUseYn());
    DisplayContsResponseDto result = displayManageService.selectDpContsList(displayManageRequestDto);

    return ApiResult.success(result);
  }

  /**
   * 컨텐츠 상세조회
   */
  @Override
  public ApiResult<?> selectDpContsInfo (DisplayManageRequestDto displayManageRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# DisplayManageBizImpl.selectDpContsInfo Start");
    log.debug("# ######################################");
    if (displayManageRequestDto != null) {
      log.debug("# In.displayManageRequestDto :: " + displayManageRequestDto.toString());
    }
    else {
      log.debug("# In.displayManageRequestDto is Null");
    }

    // ------------------------------------------------------------------------
    // # 초기화
    // ------------------------------------------------------------------------
    String goodsDetailUrl = "";

    // ----------------------------------------------------------------------
    // 1.링크URL조회
    // ----------------------------------------------------------------------
    if (DisplayEnums.ContentsType.GOODS.getCode().equals(StringUtil.nvl(displayManageRequestDto.getContsTp()))) {
      // 컨텐츠유형이 상품인 경우

      try {

        // 호스트URL
        displayManageRequestDto.setHostUrl(hostUrl);;

        // 상품상세주소
        GetEnvironmentListRequestDto getEnvironmentListRequestDto = new GetEnvironmentListRequestDto();
        getEnvironmentListRequestDto.setSearchEnvironmentKey("URL_FRONT_GOODS_DETAIL");
        log.debug("# 상품상세주소 조회 Start");
        GetEnvironmentListResultVo resultEnv = systemBasicEnvironmentBiz.getEnvironment(getEnvironmentListRequestDto);

        if (resultEnv != null) {
          goodsDetailUrl = resultEnv.getEnvironmentValue();
          displayManageRequestDto.setGoodsDetailUrl(goodsDetailUrl);
        }
        else {
          log.debug("# resultEnv is Null");
        }
      }
      catch (Exception e) {
        throw new BaseException(e.toString());
      }
    } // End of f (DisplayEnums.ContentsType.GOODS.getCode().equals(StringUtil.nvl(displayManageRequestDto.getContsTp())))

    DisplayContsResponseDto result = displayManageService.selectDpContsInfo(displayManageRequestDto);

    return ApiResult.success(result);
  }

  /**
   * 컨텐츠 수정
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
  public ApiResult<?> putConts (DisplayManageRequestDto displayManageRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# DisplayManageBizImpl.putConts Start");
    log.debug("# ######################################");
    if (displayManageRequestDto != null) {
      log.debug("# In.displayManageRequestDto :: " + displayManageRequestDto.toString());
    }
    else {
      log.debug("# In.displayManageRequestDto is Null");
    }

    DisplayContsResponseDto result = displayManageService.putConts(displayManageRequestDto.getContsInfo());

    return ApiResult.success(result);
  }

  /**
   * 컨텐츠 순서변경
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
  public ApiResult<?> putContsSort (DisplayManageRequestDto displayManageRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# DisplayManageBizImpl.putContsSort Start");
    log.debug("# ######################################");
    if (displayManageRequestDto != null) {
      log.debug("# In.displayManageRequestDto :: " + displayManageRequestDto.toString());
    }
    else {
      log.debug("# In.displayManageRequestDto is Null");
    }

    DisplayContsResponseDto result = displayManageService.putContsSort(displayManageRequestDto.getContsList());

    return ApiResult.success(result);
  }

  /**
   * 컨텐츠 삭제
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
  public ApiResult<?> delConts (DisplayManageRequestDto displayManageRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# DisplayManageBizImpl.delConts Start");
    log.debug("# ######################################");
    if (displayManageRequestDto != null) {
      log.debug("# In.displayManageRequestDto :: " + displayManageRequestDto.toString());
    }
    else {
      log.debug("# In.displayManageRequestDto is Null");
    }

    DisplayContsResponseDto result = displayManageService.delConts(displayManageRequestDto.getContsList());

    return ApiResult.success(result);
  }

  /**
   * 컨텐츠 등록
   */
  @Override
  public ApiResult<?> addConts (DisplayManageRequestDto displayManageRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# DisplayManageBizImpl.addConts Start");
    log.debug("# ######################################");
    if (displayManageRequestDto != null) {
      log.debug("# In.displayManageRequestDto :: " + displayManageRequestDto.toString());
    }
    else {
      log.debug("# In.displayManageRequestDto is Null");
    }

    DisplayContsResponseDto result;

    if (DisplayEnums.ContentsType.GOODS.getCode().equals(StringUtil.nvl(displayManageRequestDto.getContsTp()))) {
      // 컨텐츠유형이 상품
      result = this.addContsMulti(displayManageRequestDto);
    }
    else {
      // 컨텐츠유형이 상품 이외
      result = this.addContsTr(displayManageRequestDto.getContsInfo());
    }

    return ApiResult.success(result);
  }

  /**
   * 컨텐츠 등록 - multi
   * @param displayManageRequestDto
   * @return
   * @throws BaseException
   */
  @SuppressWarnings("unused")
  private DisplayContsResponseDto addContsMulti (DisplayManageRequestDto displayManageRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# DisplayManageBizImpl.addContsMulti Start");
    log.debug("# ######################################");
    if (displayManageRequestDto != null) {
      log.debug("# In.displayManageRequestDto :: " + displayManageRequestDto.toString());
    }
    else {
      log.debug("# In.displayManageRequestDto is Null");
    }

    // ======================================================================
    // # 초기화
    // ======================================================================
    DisplayContsResponseDto resultResDto = new DisplayContsResponseDto();
    resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_SUCCESS.getCode());
    resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_SUCCESS.getMessage());

    DisplayContsResponseDto unitResDto;
    int resultTotalCnt = 0;
    int resultSuccCnt  = 0;
    int resultDupCnt   = 0;   // 중복건수
    int resultFailCnt  = 0;   // 실패건수

    List<ContsVo> dupContsList =  new ArrayList<ContsVo>();
    List<ContsVo> failContsList = new ArrayList<ContsVo>();
    ContsVo failContsVo = null;

    if (displayManageRequestDto.getContsList() != null && displayManageRequestDto.getContsList().size() > 0) {

      // 컨텐츠 수 만큼 loop
      for (ContsVo inContsVo : displayManageRequestDto.getContsList()) {

        try {

          // ------------------------------------------------------------------
          // 1.중복체크
          // ------------------------------------------------------------------
          List<ContsVo> dupList = displayManageService.selectDpGoodsContsDupList(inContsVo);

          if (dupList != null && dupList.size() > 0) {
            // 상품ID & 기간 중복이 있는 경우 목록작성 후 다음 건으로 continue;

            // 상품명Set
            //inContsVo.setGoodsNm(((ContsVo)dupList.get(0)).getGoodsNm());
            // 중복정보 Set
            resultDupCnt++;
            dupContsList.add(inContsVo);
            continue;
          }

          // ------------------------------------------------------------------
          // 2.등록처리
          // ------------------------------------------------------------------
          unitResDto = this.addContsTr(inContsVo);

          if (unitResDto.getTotal() > 0) {
            resultSuccCnt++;
          }
          else {
            resultFailCnt++;
            failContsList.add(inContsVo);
            continue;
          }
        }
        catch(Exception e) {
          resultFailCnt++;
          failContsList.add(inContsVo);
          continue;
        }

      } // End of for (ContsVo contsVo : displayManageRequestDto.getContsList())

      // ----------------------------------------------------------------------
      // 3.중복/실패 응답
      // ----------------------------------------------------------------------
      // 중복건수
      log.debug("# resultDupCnt :: " + resultDupCnt);
      if (resultDupCnt > 0 && dupContsList.size() > 0) {
        resultResDto.setDupRows(dupContsList);
        resultResDto.setDupTotal(resultDupCnt);

        for (ContsVo dupConts : dupContsList) {
          log.debug("# dupContsList :: " + JsonUtil.serializeWithPrettyPrinting(dupConts));
        }
      }

      // 실패건수
      log.debug("# resultFailCnt :: " + resultFailCnt);
      if (resultFailCnt > 0 && failContsList.size() > 0) {
        resultResDto.setFailRows(failContsList);
        resultResDto.setFailTotal(resultFailCnt);

        for (ContsVo failConts : failContsList) {
          log.debug("# failConts :: " + JsonUtil.serializeWithPrettyPrinting(failConts));
        }
      }

    } // End of if (displayManageRequestDto.getContsList() != null && displayManageRequestDto.getContsList().size() > 0)
    return resultResDto;
  }

  /**
   * 컨텐츠 등록 - 단건
   */
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
  private DisplayContsResponseDto addContsTr (ContsVo contsVo) throws BaseException {
    log.debug("# ######################################");
    log.debug("# DisplayManageBizImpl.addContsTr Start");
    log.debug("# ######################################");
    if (contsVo != null) {
      log.debug("# In.contsVo :: " + contsVo.toString());
    }
    else {
      log.debug("# In.ContsVo is Null");
    }

    return displayManageService.addConts(contsVo);

  }


  /**
   * 브랜드 리스트조회 (콤보용)
   */
  @Override
  public ApiResult<?> selectBrandList (DisplayManageRequestDto displayManageRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# DisplayManageBizImpl.selectBrandList Start");
    log.debug("# ######################################");
    if (displayManageRequestDto != null) {
      log.debug("# In.displayManageRequestDto :: " + displayManageRequestDto.toString());
    }
    else {
      log.debug("# In.displayManageRequestDto is Null");
    }

    return displayManageService.selectBrandList(displayManageRequestDto);
  }

  /**
   * 상품목록조회-키워드조회
   */
  @Override
  public ApiResult<?> selectGoodsListByKeyword (DisplayManageRequestDto displayManageRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# DisplayManageBizImpl.selectGoodsListByKeyword Start");
    log.debug("# ######################################");
    if (displayManageRequestDto != null) {
      log.debug("# In.displayManageRequestDto :: " + displayManageRequestDto.toString());
    }
    else {
      log.debug("# In.displayManageRequestDto is Null");
    }

    DisplayContsResponseDto result = displayManageService.selectGoodsListByKeyword(displayManageRequestDto);

    log.debug("# DisplayManageBizImpl.selectGoodsListByKeyword result :: " + result.toString());

    return ApiResult.success(result);
  }

  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // 인벤토리그룹관리
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  /**
   * 인벤토리그룹 리스트조회
   */
  @Override
  public ApiResult<?> selectDpInventoryGroupList (DisplayManageRequestDto displayManageRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# DisplayManageBizImpl.selectDpInventoryGroupList Start");
    log.debug("# ######################################");
    if (displayManageRequestDto != null) {
      log.debug("# In.displayManageRequestDto :: " + displayManageRequestDto.toString());
    }
    else {
      log.debug("# In.displayManageRequestDto is Null");
    }

    DisplayPageResponseDto result = displayManageService.selectDpInventoryGroupList(displayManageRequestDto);

    return ApiResult.success(result);
  }

  /**
   * 인벤토리그룹 인벤토리 리스트조회
   */
  @Override
  public ApiResult<?> selectDpGroupInventoryList (DisplayManageRequestDto displayManageRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# DisplayManageBizImpl.selectDpGroupInventoryList Start");
    log.debug("# ######################################");
    if (displayManageRequestDto != null) {
      log.debug("# In.displayManageRequestDto :: " + displayManageRequestDto.toString());
    }
    else {
      log.debug("# In.displayManageRequestDto is Null");
    }

    DisplayInventoryResponseDto result = displayManageService.selectDpGroupInventoryList(displayManageRequestDto);

    return ApiResult.success(result);
  }

  /**
   * 인벤토리그룹구성 리스트조회
   */
  @Override
  public ApiResult<?> selectDpGroupInventoryMappingList (DisplayManageRequestDto displayManageRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# DisplayManageBizImpl.selectDpGroupInventoryMappingList Start");
    log.debug("# ######################################");
    if (displayManageRequestDto != null) {
      log.debug("# In.displayManageRequestDto :: " + displayManageRequestDto.toString());
    }
    else {
      log.debug("# In.displayManageRequestDto is Null");
    }

    DisplayInventoryResponseDto result = displayManageService.selectDpGroupInventoryMappingList(displayManageRequestDto);

    return ApiResult.success(result);
  }

  /**
   * 인벤토리그룹 등록
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
  public ApiResult<?> addInventoryGroup (DisplayManageRequestDto displayManageRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# DisplayManageBizImpl.addInventoryGroup Start");
    log.debug("# ######################################");
    if (displayManageRequestDto != null) {
      log.debug("# In.displayManageRequestDto :: " + displayManageRequestDto.toString());
    }
    else {
      log.debug("# In.displayManageRequestDto is Null");
    }

    DisplayPageResponseDto result = displayManageService.addInventoryGroup(displayManageRequestDto.getInventoryInfo());

    return ApiResult.success(result);
  }

  /**
   * 인벤토리그룹 수정
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
  public ApiResult<?> putInventoryGroup (DisplayManageRequestDto displayManageRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# DisplayManageBizImpl.putInventoryGroup Start");
    log.debug("# ######################################");
    if (displayManageRequestDto != null) {
      log.debug("# In.displayManageRequestDto :: " + displayManageRequestDto.toString());
    }
    else {
      log.debug("# In.displayManageRequestDto is Null");
    }

    DisplayInventoryResponseDto result = displayManageService.putInventoryGroup(displayManageRequestDto.getInventoryInfo());

    return ApiResult.success(result);
  }

  /**
   * 인벤토리그룹 삭제
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
  public ApiResult<?> delInventoryGroup (DisplayManageRequestDto displayManageRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# DisplayManageBizImpl.delInventoryGroup Start");
    log.debug("# ######################################");
    if (displayManageRequestDto != null) {
      log.debug("# In.displayManageRequestDto :: " + displayManageRequestDto.toString());
    }
    else {
      log.debug("# In.displayManageRequestDto is Null");
    }

    DisplayInventoryResponseDto result = displayManageService.delInventoryGroup(displayManageRequestDto.getInventoryList());

    return ApiResult.success(result);
  }

  /**
   * 인벤토리그룹 순서변경
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
  public ApiResult<?> putInventoryGroupSort (DisplayManageRequestDto displayManageRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# DisplayManageBizImpl.putInventoryGroupSort Start");
    log.debug("# ######################################");
    if (displayManageRequestDto != null) {
      log.debug("# In.displayManageRequestDto :: " + displayManageRequestDto.toString());
    }
    else {
      log.debug("# In.displayManageRequestDto is Null");
    }

    DisplayInventoryResponseDto result = displayManageService.putInventoryGroupSort(displayManageRequestDto.getInventoryList());

    return ApiResult.success(result);
  }

}
