package kr.co.pulmuone.v1.display.manage.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.base.dto.GetCodeListResponseDto;
import kr.co.pulmuone.v1.base.dto.vo.GetCodeListResultVo;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.DisplayEnums;
import kr.co.pulmuone.v1.comm.enums.UserEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.mapper.display.manage.DisplayManageMapper;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.display.manage.dto.DisplayContsResponseDto;
import kr.co.pulmuone.v1.display.manage.dto.DisplayInventoryResponseDto;
import kr.co.pulmuone.v1.display.manage.dto.DisplayManageRequestDto;
import kr.co.pulmuone.v1.display.manage.dto.DisplayPageResponseDto;
import kr.co.pulmuone.v1.display.manage.dto.vo.ContsVo;
import kr.co.pulmuone.v1.display.manage.dto.vo.InventoryVo;
import kr.co.pulmuone.v1.display.manage.dto.vo.PageVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
* <PRE>
* Forbiz Korea
* 전시관리 COMMON Service
*  - 2020.11.23 기준 묶음상품 적용 안됨( 프로시저로 대체될수도 있다하여 묶음상품 작업 중단)
*
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2020.09.16.    dgyoun   최초작성
* =======================================================================
* </PRE>
*/

@Slf4j
@Service
@RequiredArgsConstructor
public class DisplayManageService {

    private final DisplayManageMapper displayManageMapper;

    // ########################################################################
    // protected
    // ########################################################################

    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // 전시페이지관리
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    /**
     * 전시페이지 리스트조회
     * @param dpPageId
     * @param useAllYn
     * @return
     * @throws Exception
     */
    protected DisplayPageResponseDto selectDpPageList (String dpPageId, String useAllYn) throws BaseException {
      log.debug("# ######################################");
      log.debug("# DisplayManageService.selectPageList Start");
      log.debug("# ######################################");
      log.debug("# In.dpPageId :: " + dpPageId);
      log.debug("# In.useAllYn :: " + useAllYn);

      // ======================================================================
      // # 초기화
      // ======================================================================
      DisplayPageResponseDto resultResDto = new DisplayPageResponseDto();
      resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_SUCCESS.getCode());
      resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_SUCCESS.getMessage());
      List<PageVo> resultList = null;

      // ======================================================================
      // # 처리
      // ======================================================================

      // ----------------------------------------------------------------------
      // # 조회
      // ----------------------------------------------------------------------
      resultList = displayManageMapper.selectDpPageList(dpPageId, useAllYn, "", "");

      if (resultList != null && resultList.size() > 0) {
        resultResDto.setRows(resultList);
        resultResDto.setTotal(resultList.size());
      }
      else {
        // 조회건 없음
        log.debug("# 조회건 없음");
        resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_PAGE_LIST_NO_EXIST.getCode());
        resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_PAGE_LIST_NO_EXIST.getMessage());
      }

      // ======================================================================
      // # 반환
      // ======================================================================
      return resultResDto;
    }

    /**
     * 전시카테고리 리스트조회
     * @param displayManageRequestDto
     * @return
     * @throws Exception
     */
    //protected DisplayPageResponseDto selectDpCategoryList (String dpPageId, String useAllYn, String mallDiv) throws Exception {
    @SuppressWarnings("unused")
    protected DisplayPageResponseDto selectDpCategoryList (DisplayManageRequestDto displayManageRequestDto) throws BaseException {
      log.debug("# ######################################");
      log.debug("# DisplayManageService.selectDpCategoryList Start");
      log.debug("# ######################################");
      log.debug("# In.pageTp      :: " + displayManageRequestDto.getPageTp());
      log.debug("# In.dpPageId    :: " + displayManageRequestDto.getDpPageId());
      log.debug("# In.useAllYn    :: " + displayManageRequestDto.getUseAllYn());
      log.debug("# In.mallDiv     :: " + displayManageRequestDto.getMallDiv());
      log.debug("# In.contsListYn :: " + displayManageRequestDto.getContsListYn());

      // ======================================================================
      // # 초기화
      // ======================================================================
      DisplayPageResponseDto resultResDto = new DisplayPageResponseDto();
      resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_SUCCESS.getCode());
      resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_SUCCESS.getMessage());
      List<PageVo> resultList = null;
      int depth = 0;

      // ======================================================================
      // # 처리
      // ======================================================================

      // ----------------------------------------------------------------------
      // # 조회
      // ----------------------------------------------------------------------

      // * 페이지 DEPTH 조회
      //log.debug("# selectDpPageDepth  :: " + displayManageMapper.selectDpPageDepth(dpPageId));
      //depth = displayManageMapper.selectDpPageDepth(dpPageId);

      if (StringUtil.isNotEmpty(displayManageRequestDto.getContsListYn()) && StringUtil.isEquals(displayManageRequestDto.getContsListYn(), "Y")) {
        // --------------------------------------------------------------------
        // 전시콘텐츠관리 화면에서 조회 (전시카테고리 Tree와 동일 쿼리)
        // --------------------------------------------------------------------
        resultList = displayManageMapper.selectDpCategoryList(displayManageRequestDto.getDpPageId(), displayManageRequestDto.getUseAllYn(), displayManageRequestDto.getMallDiv(), "");
      }
      else {
        // --------------------------------------------------------------------
        // 전시페이지관리 화면에서 조회
        // --------------------------------------------------------------------

        if (StringUtil.isEmpty(displayManageRequestDto.getDpPageId()) || StringUtil.isEquals(displayManageRequestDto.getDpPageId(), "0")) {
          // 최상위조회
          resultList = displayManageMapper.selectDpCategoryList(displayManageRequestDto.getDpPageId(), displayManageRequestDto.getUseAllYn(), displayManageRequestDto.getMallDiv(), "0");
        }
        else {
          // 최상위 이외 조회
          resultList = displayManageMapper.selectDpCategorySubList(displayManageRequestDto.getDpPageId(), displayManageRequestDto.getUseAllYn(), displayManageRequestDto.getMallDiv());
        }

      }

      if (resultList != null && resultList.size() > 0) {
        resultResDto.setRows(resultList);
        resultResDto.setTotal(resultList.size());
      }
      else {
        // 조회건 없음
        log.debug("# 조회건 없음");
        resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_PAGE_LIST_NO_EXIST.getCode());
        resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_PAGE_LIST_NO_EXIST.getMessage());
        //throw new BaseException(UserEnums.DisplayManage.DISPLAY_MANAGE_PAGE_LIST_NO_EXIST);
      }

      // ======================================================================
      // # 반환
      // ======================================================================
      return resultResDto;
    }

    /**
     * 페이지 상세조회
     * @param dpPageId
     * @param useAllYn
     * @return
     * @throws Exception
     */
    protected DisplayPageResponseDto selectPageInfo (String dpPageId) throws BaseException {
      log.debug("# ######################################");
      log.debug("# DisplayManageService.selectPageInfo Start");
      log.debug("# ######################################");
      log.debug("# In.dpPageId :: " + dpPageId);

      // ======================================================================
      // # 초기화
      // ======================================================================
      DisplayPageResponseDto resultResDto = new DisplayPageResponseDto();
      resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_SUCCESS.getCode());
      resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_SUCCESS.getMessage());
      PageVo resultVo = null;

      // ======================================================================
      // # 처리
      // ======================================================================

      // ----------------------------------------------------------------------
      // # 조회
      // ----------------------------------------------------------------------
      resultVo = displayManageMapper.selectPageInfo(dpPageId);

      if (resultVo != null) {
        // 등록내용 응답 Set
        resultResDto.setDetail(resultVo);
      }
      else {
        // 조회건 없음
        log.debug("# 조회건 없음");
        resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_PAGE_NO_EXIST.getCode());
        resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_PAGE_NO_EXIST.getMessage());
        //throw new BaseException(UserEnums.DisplayManage.DISPLAY_MANAGE_PAGE_NO_EXIST);
      }

      // ======================================================================
      // # 반환
      // ======================================================================
      return resultResDto;
    }

    /**
     * 페이지 수정
     * @param dpPageId
     * @param useAllYn
     * @return
     * @throws Exception
     */
    protected DisplayPageResponseDto putPage (PageVo pageVo) throws BaseException {
      log.debug("# ######################################");
      log.debug("# DisplayManageService.putPage Start");
      log.debug("# ######################################");
      if (pageVo != null) {
        log.debug("# In.pageVo :: " + pageVo.toString());
      }
      else {
        log.debug("# In.pageVo is Null...");
      }

      // ======================================================================
      // # 초기화
      // ======================================================================
      DisplayPageResponseDto resultResDto = new DisplayPageResponseDto();
      resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_SUCCESS.getCode());
      resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_SUCCESS.getMessage());
      int resultInt = 0;

      // ======================================================================
      // # 처리
      // ======================================================================

      try {

        // --------------------------------------------------------------------
        // # 세션정보 Set
        // --------------------------------------------------------------------
        if (SessionUtil.getBosUserVO() != null) {
          pageVo.setCreateId((SessionUtil.getBosUserVO()).getUserId());
          pageVo.setModifyId((SessionUtil.getBosUserVO()).getUserId());
        }
        else {
          pageVo.setCreateId("0");
          pageVo.setModifyId("0");
        }

        // --------------------------------------------------------------------
        // # 수정
        // --------------------------------------------------------------------
        resultInt = displayManageMapper.putPage(pageVo);

        if (resultInt > 0) {
          resultResDto.setTotal(resultInt);

          // ------------------------------------------------------------------
          // 상세조회
          // ------------------------------------------------------------------
          resultResDto.setDetail(displayManageMapper.selectPageInfo(pageVo.getDpPageId()));
        }
        else {
          // 수정건 없음
          log.debug("# 수정건 없음");
          resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_PAGE_FAIL_PUT_NO_PROC.getCode());
          resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_PAGE_FAIL_PUT_NO_PROC.getMessage());
          throw new BaseException(UserEnums.DisplayManage.DISPLAY_MANAGE_PAGE_FAIL_PUT_NO_PROC);
        }
      }
      catch (BaseException be) {
        log.info("# putPage BaseException e :: " + be.toString());
        throw be;
      }
      catch (Exception e) {
        log.info("# putPage Exception e :: " + e.toString());
        throw new BaseException(UserEnums.DisplayManage.DISPLAY_MANAGE_PAGE_FAIL_PUT);
      }

      // ======================================================================
      // # 반환
      // ======================================================================
      return resultResDto;
    }

    /**
     * 페이지 순서변경
     * @param dpPageId
     * @param useAllYn
     * @return
     * @throws Exception
     */
    @SuppressWarnings({ "unused", "null" })
    protected DisplayPageResponseDto putPageSort (List<PageVo> pageVoList) throws BaseException {
      log.debug("# ######################################");
      log.debug("# DisplayManageService.putPageSort Start");
      log.debug("# ######################################");
      if (pageVoList != null) {
        log.debug("# In.pageVoList.size :: " + pageVoList.size());
        log.debug("# In.pageVoList :: " + pageVoList.toString());
      }
      else {
        log.debug("# In.pageVoList is Null or size 0");
      }

      // ======================================================================
      // # 초기화
      // ======================================================================
      DisplayPageResponseDto resultResDto = new DisplayPageResponseDto();
      resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_SUCCESS.getCode());
      resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_SUCCESS.getMessage());
      int resultInt = 0;
      int resultTotalInt = 0;

      // ======================================================================
      // # 처리
      // ======================================================================

      try {

        if (pageVoList != null || pageVoList.size() <= 0) {
          // 순번변경 대상 없음
          resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_PAGE_FAIL_PUT_SORT_NO_TARGET.getCode());
          resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_PAGE_FAIL_PUT_SORT_NO_TARGET.getMessage());
          resultResDto.setTotal(0);
          return resultResDto;
        }

        for (PageVo unitPageVo : pageVoList) {

          // ------------------------------------------------------------------
          // # 세션정보 Set
          // ------------------------------------------------------------------
          if (SessionUtil.getBosUserVO() != null) {
            unitPageVo.setCreateId((SessionUtil.getBosUserVO()).getUserId());
            unitPageVo.setModifyId((SessionUtil.getBosUserVO()).getUserId());
          }
          else {
            unitPageVo.setCreateId("0");
            unitPageVo.setModifyId("0");
          }

          // ------------------------------------------------------------------
          // # 수정
          // ------------------------------------------------------------------
          resultInt = displayManageMapper.putPageSort(unitPageVo);

          if (resultInt <= 0) {
            // # 한건이라도 실패할 경우 모두 롤백
            log.debug("# 수정건 없음");
            resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_PAGE_FAIL_PUT_SORT_NO_PROC.getCode());
            resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_PAGE_FAIL_PUT_SORT_NO_PROC.getMessage());
            throw new BaseException(UserEnums.DisplayManage.DISPLAY_MANAGE_PAGE_FAIL_PUT_SORT_NO_PROC);
          }
          resultTotalInt++;
        } // End of for (PageVo unitPageVo : pageVoList)

        // --------------------------------------------------------------------
        // # 처리건 Set
        // --------------------------------------------------------------------
        resultResDto.setTotal(resultTotalInt);

      }
      catch (BaseException be) {
        log.info("# putPageSort BaseException e :: " + be.toString());
        throw be;
      }
      catch (Exception e) {
        log.info("# putPageSort Exception e :: " + e.toString());
        throw new BaseException(UserEnums.DisplayManage.DISPLAY_MANAGE_PAGE_FAIL_PUT_SORT);
      }

      // ======================================================================
      // # 반환
      // ======================================================================
      return resultResDto;
    }

    /**
     * 페이지 삭제
     * @param dpPageId
     * @param useAllYn
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unused")
    protected DisplayPageResponseDto delPage (List<PageVo> pageVoList) throws BaseException {
      log.debug("# ######################################");
      log.debug("# DisplayManageService.delPage Start");
      log.debug("# ######################################");
      if (pageVoList != null) {
        log.debug("# In.pageVoList.size :: " + pageVoList.size());
        log.debug("# In.pageVoList :: " + pageVoList.toString());
      }
      else {
        log.debug("# In.pageVoList is Null or size 0");
      }

      // ======================================================================
      // # 초기화
      // ======================================================================
      DisplayPageResponseDto resultResDto = new DisplayPageResponseDto();
      resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_SUCCESS.getCode());
      resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_SUCCESS.getMessage());
      int resultInt = 0;
      int resultTotalInt = 0;

      // ======================================================================
      // # 처리
      // ======================================================================

      try {

        if (pageVoList == null || pageVoList.size() <= 0) {
          // 순번변경 대상 없음
          resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_PAGE_FAIL_DEL_NO_TARGET.getCode());
          resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_PAGE_FAIL_DEL_NO_TARGET.getMessage());
          resultResDto.setTotal(0);
          return resultResDto;
        }

        for (PageVo unitPageVo : pageVoList) {

          // ------------------------------------------------------------------
          // # 세션정보 Set
          // ------------------------------------------------------------------
          if (SessionUtil.getBosUserVO() != null) {
            unitPageVo.setCreateId((SessionUtil.getBosUserVO()).getUserId());
            unitPageVo.setModifyId((SessionUtil.getBosUserVO()).getUserId());
          }
          else {
            unitPageVo.setCreateId("0");
            unitPageVo.setModifyId("0");
          }

          // ------------------------------------------------------------------
          // # 삭제
          // ------------------------------------------------------------------
          unitPageVo.setDelYn("Y");
          resultInt = displayManageMapper.delPage(unitPageVo);

          if (resultInt <= 0) {
            // # 한건이라도 실패할 경우 모두 롤백
            log.debug("# 삭제건 없음");
            resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_PAGE_FAIL_DEL_NO_PROC.getCode());
            resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_PAGE_FAIL_DEL_NO_PROC.getMessage());
            throw new BaseException(UserEnums.DisplayManage.DISPLAY_MANAGE_PAGE_FAIL_DEL_NO_PROC);
          }
          resultTotalInt++;
          // 현재 단건임, 다건인 경우 List로 반환 필요
          resultResDto.setDetail(unitPageVo);
        } // End of for (PageVo unitPageVo : pageVoList)

        // --------------------------------------------------------------------
        // # 처리건 Set
        // --------------------------------------------------------------------
        resultResDto.setTotal(resultTotalInt);

      }
      catch (BaseException be) {
        log.info("# delPage BaseException e :: " + be.toString());
        throw be;
      }
      catch (Exception e) {
        log.info("# delPage Exception e :: " + e.toString());
        throw new BaseException(UserEnums.DisplayManage.DISPLAY_MANAGE_PAGE_FAIL_DEL);
      }

      // ======================================================================
      // # 반환
      // ======================================================================
      return resultResDto;
    }

    /**
     * 페이지 등록
     * @param dpPageId
     * @param useAllYn
     * @return
     * @throws Exception
     */
    protected DisplayPageResponseDto addPage (PageVo pageVo) throws BaseException {
      log.debug("# ######################################");
      log.debug("# DisplayManageService.addPage Start");
      log.debug("# ######################################");
      if (pageVo != null) {
        log.debug("# In.pageVo :: " + pageVo.toString());
      }
      else {
        log.debug("# In.pageVo is Null...");
      }

      // ======================================================================
      // # 초기화
      // ======================================================================
      DisplayPageResponseDto resultResDto = new DisplayPageResponseDto();
      resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_SUCCESS.getCode());
      resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_SUCCESS.getMessage());
      int resultInt = 0;
      int resultUpdInt = 0;

      // ======================================================================
      // # 처리
      // ======================================================================

      try {

        // --------------------------------------------------------------------
        // # 세션정보 Set
        // --------------------------------------------------------------------
        if (SessionUtil.getBosUserVO() != null) {
          pageVo.setCreateId((SessionUtil.getBosUserVO()).getUserId());
          pageVo.setModifyId((SessionUtil.getBosUserVO()).getUserId());
        }
        else {
          pageVo.setCreateId("0");
          pageVo.setModifyId("0");
        }

        // --------------------------------------------------------------------
        // # 1.페이지코드 중복조회
        // --------------------------------------------------------------------
        // 파람 : 부모페이지ID(), 사용여부(미입력), 페이지CD(입력), 중복체크여부(Y)
        // 조건 : 페이지CD & USE_YN = 모두 & DEL_YN = 'N'


        List<PageVo> resultList = displayManageMapper.selectDpPageList(pageVo.getPrntsPageId(), "Y", pageVo.getPageCd(), "Y");

        if (resultList != null && resultList.size() > 0) {
          log.debug("# 페이지코드 중복");
          resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_PAGE_FAIL_DUP_PAGE_CD.getCode());
          resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_PAGE_FAIL_DUP_PAGE_CD.getMessage());
          throw new BaseException(UserEnums.DisplayManage.DISPLAY_MANAGE_PAGE_FAIL_DUP_PAGE_CD);
        }

        // --------------------------------------------------------------------
        // # 2.등록
        // --------------------------------------------------------------------
        resultInt = displayManageMapper.addPage(pageVo);
        log.debug("# New pageVo :: " + pageVo.toString());

        if (resultInt > 0) {

          // ------------------------------------------------------------------
          // # 3.후처리
          // ------------------------------------------------------------------
          resultUpdInt = displayManageMapper.putPageAfterAdd(pageVo);

          if (resultUpdInt > 0) {
            // 처기결과건수
            resultResDto.setTotal(resultInt);

            // 등록정보 Set (등록된 페이지 정보 조회)
            //resultResDto.setDetail(displayManageMapper.selectPageInfo(pageVo.getDpPageId()));
            resultResDto.setDetail(pageVo);
          }
          else {
            log.debug("# 후처리 실패");
            resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_PAGE_FAIL_ADD_AFTER_NO_PROC.getCode());
            resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_PAGE_FAIL_ADD_AFTER_NO_PROC.getMessage());
            throw new BaseException(UserEnums.DisplayManage.DISPLAY_MANAGE_PAGE_FAIL_ADD_AFTER_NO_PROC);
          }

        }
        else {
          // 등록건 없음
          log.debug("# 등록건 없음");
          resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_PAGE_FAIL_ADD_NO_PROC.getCode());
          resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_PAGE_FAIL_ADD_NO_PROC.getMessage());
          throw new BaseException(UserEnums.DisplayManage.DISPLAY_MANAGE_PAGE_FAIL_ADD_NO_PROC);

        }
      }
      catch (BaseException be) {
        log.info("# addPage BaseException :: " + be.toString());
        throw be;
      }
      catch (Exception e) {
        log.info("# addPage Exception :: " + e.toString());
        throw new BaseException(UserEnums.DisplayManage.DISPLAY_MANAGE_PAGE_FAIL_ADD);
      }

      // ======================================================================
      // # 반환
      // ======================================================================
      return resultResDto;
    }

    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // 전시인벤토리관리
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    /**
     * 인벤토리 리스트조회
     * @param displayManageRequestDto
     * @return
     * @throws Exception
     */
    protected DisplayInventoryResponseDto selectInventoryList (DisplayManageRequestDto displayManageRequestDto) throws BaseException {
      log.debug("# ######################################");
      log.debug("# DisplayManageService.selectInventoryList Start");
      log.debug("# ######################################");
      log.debug("# In.pageTp    :: " + displayManageRequestDto.getPageTp());
      log.debug("# In.dpPageId  :: " + displayManageRequestDto.getDpPageId());
      log.debug("# In.mallDiv   :: " + displayManageRequestDto.getMallDiv());
      log.debug("# In.depth     :: " + displayManageRequestDto.getDepth());
      log.debug("# In.useYn     :: " + displayManageRequestDto.getUseYn());

      // ======================================================================
      // # 초기화
      // ======================================================================
      DisplayInventoryResponseDto resultResDto = new DisplayInventoryResponseDto();
      resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_SUCCESS.getCode());
      resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_SUCCESS.getMessage());
      List<InventoryVo> resultList = null;

      // ======================================================================
      // # 처리
      // ======================================================================

      // ----------------------------------------------------------------------
      // # 조회
      // ----------------------------------------------------------------------
      InventoryVo inInventoryVo = new InventoryVo();

      inInventoryVo.setPageTp(displayManageRequestDto.getPageTp());
      inInventoryVo.setDpPageId(displayManageRequestDto.getDpPageId());
      inInventoryVo.setMallDiv(displayManageRequestDto.getMallDiv());
      inInventoryVo.setDepth(displayManageRequestDto.getDepth());
      inInventoryVo.setUseYn(displayManageRequestDto.getUseYn());
      inInventoryVo.setPageNm(displayManageRequestDto.getPageNm());

      resultList = displayManageMapper.selectInventoryList(inInventoryVo);

      if (resultList != null && resultList.size() > 0) {

        //// 응답 결과에 화면으로부터 입력받은 페이지명, 페이지경로 Set
        //for (InventoryVo unitInventoryVo : resultList) {
        //
        //  unitInventoryVo.setPageNm(displayManageRequestDto.getPageNm());
        //  unitInventoryVo.setPageFullPath(displayManageRequestDto.getPageFullPath());
        //}

        resultResDto.setRows(resultList);
        resultResDto.setTotal(resultList.size());
      }
      else {
        // 조회건 없음
        log.debug("# 조회건 없음");
        resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_INVENTORY_LIST_NO_EXIST.getCode());
        resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_INVENTORY_LIST_NO_EXIST.getMessage());
        //throw new BaseException(UserEnums.DisplayManage.DISPLAY_MANAGE_INVENTORY_LIST_NO_EXIST);
      }

      // ======================================================================
      // # 반환
      // ======================================================================
      return resultResDto;
    }

    /**
     * 인벤토리 상세조회
     * @param dpInventoryId
     * @return
     * @throws Exception
     */
    protected DisplayInventoryResponseDto selectInventoryInfo (String dpInventoryId) throws BaseException {
      log.debug("# ######################################");
      log.debug("# DisplayManageService.selectInventoryInfo Start");
      log.debug("# ######################################");
      log.debug("# In.dpInventoryId :: " + dpInventoryId);

      // ======================================================================
      // # 초기화
      // ======================================================================
      DisplayInventoryResponseDto resultResDto = new DisplayInventoryResponseDto();
      resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_SUCCESS.getCode());
      resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_SUCCESS.getMessage());
      InventoryVo resultVo = null;

      // ======================================================================
      // # 처리
      // ======================================================================

      // ----------------------------------------------------------------------
      // # 조회
      // ----------------------------------------------------------------------
      resultVo = displayManageMapper.selectInventoryInfo(dpInventoryId);

      if (resultVo != null) {
        resultResDto.setDetail(resultVo);
      }
      else {
        // 조회건 없음
        log.debug("# 조회건 없음");
        resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_INVENTORY_NO_EXIST.getCode());
        resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_INVENTORY_NO_EXIST.getMessage());
        //throw new BaseException(UserEnums.DisplayManage.DISPLAY_MANAGE_INVENTORY_NO_EXIST);
      }

      // ======================================================================
      // # 반환
      // ======================================================================
      return resultResDto;
    }

    /**
     * 인벤토리 수정
     * @param inventoryVo
     * @return
     * @throws Exception
     */
    protected DisplayInventoryResponseDto putInventory (InventoryVo inventoryVo) throws BaseException {
      log.debug("# ######################################");
      log.debug("# DisplayManageService.putInventory Start");
      log.debug("# ######################################");
      if (inventoryVo != null) {
        log.debug("# In.inventoryVo :: " + inventoryVo.toString());
      }
      else {
        log.debug("# In.inventoryVo is Null...");
      }

      // ======================================================================
      // # 초기화
      // ======================================================================
      DisplayInventoryResponseDto resultResDto = new DisplayInventoryResponseDto();
      resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_SUCCESS.getCode());
      resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_SUCCESS.getMessage());
      int resultInt = 0;

      // ======================================================================
      // # 처리
      // ======================================================================

      try {

        // --------------------------------------------------------------------
        // # 세션정보 Set
        // --------------------------------------------------------------------
        if (SessionUtil.getBosUserVO() != null) {
          inventoryVo.setCreateId((SessionUtil.getBosUserVO()).getUserId());
          inventoryVo.setModifyId((SessionUtil.getBosUserVO()).getUserId());
        }
        else {
          inventoryVo.setCreateId("0");
          inventoryVo.setModifyId("0");
        }

        // --------------------------------------------------------------------
        // # 수정
        // --------------------------------------------------------------------
        resultInt = displayManageMapper.putInventory(inventoryVo);

        if (resultInt > 0) {
          resultResDto.setTotal(resultInt);
        }
        else {
          // 수정건 없음
          log.debug("# 수정건 없음");
          resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_INVENTORY_FAIL_PUT_NO_PROC.getCode());
          resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_INVENTORY_FAIL_PUT_NO_PROC.getMessage());
          throw new BaseException(UserEnums.DisplayManage.DISPLAY_MANAGE_INVENTORY_FAIL_PUT_NO_PROC);
        }
      }
      catch (BaseException be) {
        log.info("# putInventory BaseException e :: " + be.toString());
        throw be;
      }
      catch (Exception e) {
        log.info("# putInventory Exception e :: " + e.toString());
        throw new BaseException(UserEnums.DisplayManage.DISPLAY_MANAGE_INVENTORY_FAIL_PUT);
      }

      // ======================================================================
      // # 반환
      // ======================================================================
      return resultResDto;
    }

    /**
     * 인벤토리 순서변경
     * @param inventoryVoList
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unused")
    protected DisplayInventoryResponseDto putInventorySort (List<InventoryVo> inventoryVoList) throws BaseException {
      log.debug("# ######################################");
      log.debug("# DisplayManageService.putInventorySort Start");
      log.debug("# ######################################");
      if (inventoryVoList != null) {
        log.debug("# In.inventoryVoList.size :: " + inventoryVoList.size());
        log.debug("# In.inventoryVoList :: " + inventoryVoList.toString());
      }
      else {
        log.debug("# In.inventoryVoList is Null or size 0");
      }

      // ======================================================================
      // # 초기화
      // ======================================================================
      DisplayInventoryResponseDto resultResDto = new DisplayInventoryResponseDto();
      resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_SUCCESS.getCode());
      resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_SUCCESS.getMessage());
      int resultInt = 0;
      int resultTotalInt = 0;

      // ======================================================================
      // # 처리
      // ======================================================================

      try {

        if (inventoryVoList == null || inventoryVoList.size() <= 0) {
          // 순번변경 대상 없음
          resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_INVENTORY_FAIL_PUT_SORT_NO_TARGET.getCode());
          resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_INVENTORY_FAIL_PUT_SORT_NO_TARGET.getMessage());
          resultResDto.setTotal(0);
          return resultResDto;
        }

        for (InventoryVo unitInventoryVo : inventoryVoList) {

          // ------------------------------------------------------------------
          // # 세션정보 Set
          // ------------------------------------------------------------------
          if (SessionUtil.getBosUserVO() != null) {
            unitInventoryVo.setCreateId((SessionUtil.getBosUserVO()).getUserId());
            unitInventoryVo.setModifyId((SessionUtil.getBosUserVO()).getUserId());
          }
          else {
            unitInventoryVo.setCreateId("0");
            unitInventoryVo.setModifyId("0");
          }

          // ------------------------------------------------------------------
          // # 수정
          // ------------------------------------------------------------------
          resultInt = displayManageMapper.putInventorySort(unitInventoryVo);

          if (resultInt <= 0) {
            // # 한건이라도 실패할 경우 모두 롤백
            log.debug("# 수정건 없음");
            resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_INVENTORY_FAIL_PUT_SORT_NO_PROC.getCode());
            resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_INVENTORY_FAIL_PUT_SORT_NO_PROC.getMessage());
            throw new BaseException(UserEnums.DisplayManage.DISPLAY_MANAGE_INVENTORY_FAIL_PUT_SORT_NO_PROC);
          }
          resultTotalInt++;
        } // End of for (InventoryVo unitInventoryVo : inventoryVoList)

        // --------------------------------------------------------------------
        // # 처리건 Set
        // --------------------------------------------------------------------
        resultResDto.setTotal(resultTotalInt);

      }
      catch (BaseException be) {
        log.info("# putInventorySort BaseException e :: " + be.toString());
        throw be;
      }
      catch (Exception e) {
        log.info("# putInventorySort Exception e :: " + e.toString());
        throw new BaseException(UserEnums.DisplayManage.DISPLAY_MANAGE_INVENTORY_FAIL_PUT_SORT);
      }

      // ======================================================================
      // # 반환
      // ======================================================================
      return resultResDto;
    }

    /**
     * 인벤토리 삭제
     * @param inventoryVoList
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unused")
    protected DisplayInventoryResponseDto delInventory (List<InventoryVo> inventoryVoList) throws BaseException {
      log.debug("# ######################################");
      log.debug("# DisplayManageService.delInventory Start");
      log.debug("# ######################################");
      if (inventoryVoList != null) {
        log.debug("# In.inventoryVoList.size :: " + inventoryVoList.size());
        log.debug("# In.inventoryVoList :: " + inventoryVoList.toString());
      }
      else {
        log.debug("# In.inventoryVoList is Null or size 0");
      }

      // ======================================================================
      // # 초기화
      // ======================================================================
      DisplayInventoryResponseDto resultResDto = new DisplayInventoryResponseDto();
      resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_SUCCESS.getCode());
      resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_SUCCESS.getMessage());
      int resultInt = 0;
      int resultTotalInt = 0;

      // ======================================================================
      // # 처리
      // ======================================================================

      try {

        if (inventoryVoList == null || inventoryVoList.size() <= 0) {
          // 삭제 대상 없음
          resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_INVENTORY_FAIL_DEL_NO_TARGET.getCode());
          resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_INVENTORY_FAIL_DEL_NO_TARGET.getMessage());
          resultResDto.setTotal(0);
          return resultResDto;
        }

        for (InventoryVo unitInventoryVo : inventoryVoList) {

          // ------------------------------------------------------------------
          // # 세션정보 Set
          // ------------------------------------------------------------------
          if (SessionUtil.getBosUserVO() != null) {
            unitInventoryVo.setCreateId((SessionUtil.getBosUserVO()).getUserId());
            unitInventoryVo.setModifyId((SessionUtil.getBosUserVO()).getUserId());
          }
          else {
            unitInventoryVo.setCreateId("0");
            unitInventoryVo.setModifyId("0");
          }

          // ------------------------------------------------------------------
          // # 삭제
          // ------------------------------------------------------------------
          resultInt = displayManageMapper.delInventory(unitInventoryVo);

          if (resultInt <= 0) {
            // # 한건이라도 실패할 경우 모두 롤백
            log.debug("# 삭제건 없음");
            resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_INVENTORY_FAIL_DEL_NO_PROC.getCode());
            resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_INVENTORY_FAIL_DEL_NO_PROC.getMessage());
            throw new BaseException(UserEnums.DisplayManage.DISPLAY_MANAGE_INVENTORY_FAIL_DEL_NO_PROC);
          }
          resultTotalInt++;
        } // End of for (InventoryVo unitInventoryVo : inventoryVoList)

        // --------------------------------------------------------------------
        // # 처리건 Set
        // --------------------------------------------------------------------
        resultResDto.setTotal(resultTotalInt);

      }
      catch (BaseException be) {
        log.info("# delInventory BaseException e :: " + be.toString());
        throw be;
      }
      catch (Exception e) {
        log.info("# delInventory Exception e :: " + e.toString());
        throw new BaseException(UserEnums.DisplayManage.DISPLAY_MANAGE_INVENTORY_FAIL_DEL);
      }

      // ======================================================================
      // # 반환
      // ======================================================================
      return resultResDto;
    }

    /**
     * 인벤토리 등록
     * @param inventoryVo
     * @return
     * @throws Exception
     */
    protected DisplayInventoryResponseDto addInventory (InventoryVo inventoryVo) throws BaseException {
      log.debug("# ######################################");
      log.debug("# DisplayManageService.addInventory Start");
      log.debug("# ######################################");
      if (inventoryVo != null) {
        log.debug("# In.inventoryVo :: " + inventoryVo.toString());
      }
      else {
        log.debug("# In.inventoryVo is Null...");
      }

      // ======================================================================
      // # 초기화
      // ======================================================================
      DisplayInventoryResponseDto resultResDto = new DisplayInventoryResponseDto();
      resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_SUCCESS.getCode());
      resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_SUCCESS.getMessage());
      int resultInt = 0;

      // ======================================================================
      // # 처리
      // ======================================================================

      try {

        // --------------------------------------------------------------------
        // # 세션정보 Set
        // --------------------------------------------------------------------
        if (SessionUtil.getBosUserVO() != null) {
          inventoryVo.setCreateId((SessionUtil.getBosUserVO()).getUserId());
          inventoryVo.setModifyId((SessionUtil.getBosUserVO()).getUserId());
        }
        else {
          inventoryVo.setCreateId("0");
          inventoryVo.setModifyId("0");
        }

        // --------------------------------------------------------------------
        // # 1.인벤토리코드 중복조회
        // --------------------------------------------------------------------
        // 파람 : 인벤토리코드(O), 인벤토리코드리스트(X), 페이지유형(X)
        // 조건 : 인벤토리코드(단건) & USE_YN = 모두 & DEL_YN = 'N'
        List<InventoryVo> resultList = displayManageMapper.selectInventoryCdList(inventoryVo.getInventoryCd(), null, null);

        if (resultList != null && resultList.size() > 0) {
          log.debug("# 인벤토리코드 중복");
          resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_INVENTORY_FAIL_DUP_INVENTORY_CD.getCode());
          resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_INVENTORY_FAIL_DUP_INVENTORY_CD.getMessage());
          throw new BaseException(UserEnums.DisplayManage.DISPLAY_MANAGE_INVENTORY_FAIL_DUP_INVENTORY_CD);
        }

        // --------------------------------------------------------------------
        // # 2.등록
        // --------------------------------------------------------------------
        log.debug("# inventoryVo :: " + inventoryVo.toString());
        resultInt = displayManageMapper.addInventory(inventoryVo);

        if (resultInt > 0) {
          resultResDto.setTotal(resultInt);
        }
        else {
          // 수정건 없음
          log.debug("# 등록건 없음");
          resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_INVENTORY_FAIL_ADD_NO_PROC.getCode());
          resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_INVENTORY_FAIL_ADD_NO_PROC.getMessage());
          throw new BaseException(UserEnums.DisplayManage.DISPLAY_MANAGE_INVENTORY_FAIL_ADD_NO_PROC);
        }
      }
      catch (BaseException be) {
        log.info("# addInventory BaseException be :: " + be.toString());
        throw be;
      }
      catch (Exception e) {
        log.info("# addInventory Exception e :: " + e.toString());
        throw new BaseException(UserEnums.DisplayManage.DISPLAY_MANAGE_INVENTORY_FAIL_ADD);
      }

      // ======================================================================
      // # 반환
      // ======================================================================
      return resultResDto;
    }


    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // 전시콘텐츠관리
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    /**
     * 전시콘텐츠 리스트조회
     * @param dpInventoryId
     * @param prntsContsId
     * @param useYn
     * @return
     * @throws Exception
     */
    protected DisplayContsResponseDto selectDpContsList (DisplayManageRequestDto displayManageRequestDto) throws BaseException {
      log.debug("# ######################################");
      log.debug("# DisplayManageService.selectDpContsList Start");
      log.debug("# ######################################");
      log.debug("# In.dpInventoryId :: " + displayManageRequestDto.getDpInventoryId());
      log.debug("# In.prntsContsId  :: " + displayManageRequestDto.getPrntsContsId());
      //log.debug("# In.useYn         :: " + displayManageRequestDto.getUseYn());

      // ======================================================================
      // # 초기화
      // ======================================================================
      DisplayContsResponseDto resultResDto = new DisplayContsResponseDto();
      resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_SUCCESS.getCode());
      resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_SUCCESS.getMessage());
      List<ContsVo> resultList = null;

      // ======================================================================
      // # 처리
      // ======================================================================

      // ----------------------------------------------------------------------
      // # 조회
      // ----------------------------------------------------------------------
      ContsVo inContsVo = new ContsVo();
      inContsVo.setDpInventoryId(displayManageRequestDto.getDpInventoryId());
      inContsVo.setPrntsContsId(displayManageRequestDto.getPrntsContsId());
      inContsVo.setContsLevel(displayManageRequestDto.getContsLevel());
      inContsVo.setDpRangeTp(displayManageRequestDto.getDpRangeTp());
      inContsVo.setStatus(displayManageRequestDto.getStatus());
      inContsVo.setDpStartDt((displayManageRequestDto.getDpStartDt()).replaceAll("-", ""));
      inContsVo.setDpEndDt((displayManageRequestDto.getDpEndDt()).replaceAll("-", ""));
      inContsVo.setDpCondTp(displayManageRequestDto.getDpCondTp());
      inContsVo.setDpSortTp(displayManageRequestDto.getDpSortTp());
      inContsVo.setIlCtgryId(displayManageRequestDto.getIlCtgryId());
      inContsVo.setUseYn("Y");
      inContsVo.setHostUrl(displayManageRequestDto.getHostUrl());
      inContsVo.setGoodsDetailUrl(displayManageRequestDto.getGoodsDetailUrl());

      log.debug("# selectDpContsList Param :: " + inContsVo.toString());

      resultList = displayManageMapper.selectDpContsList(inContsVo);

      if (resultList != null && resultList.size() > 0) {
        resultResDto.setRows(resultList);
        resultResDto.setTotal(resultList.size());
      }
      else {
        // 조회건 없음
        log.debug("# 조회건 없음");
        resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_CONTS_LIST_NO_EXIST.getCode());
        resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_CONTS_LIST_NO_EXIST.getMessage());
        //throw new BaseException(UserEnums.DisplayManage.DISPLAY_MANAGE_CONTS_LIST_NO_EXIST);
      }

      // ======================================================================
      // # 반환
      // ======================================================================
      return resultResDto;
    }

    /**
     * 콘텐츠 상세조회
     * @param dpcontsId
     * @return
     * @throws Exception
     */
    protected DisplayContsResponseDto selectDpContsInfo (DisplayManageRequestDto displayManageRequestDto) throws BaseException {
      log.debug("# ######################################");
      log.debug("# DisplayManageService.selectDpContsInfo Start");
      log.debug("# ######################################");
      log.debug("# In.dpContsId       :: " + displayManageRequestDto.getDpContsId());
      log.debug("# In.hostUrl         :: " + displayManageRequestDto.getHostUrl());
      log.debug("# In.goodsDetailUrl  :: " + displayManageRequestDto.getGoodsDetailUrl());

      // ======================================================================
      // # 초기화
      // ======================================================================
      DisplayContsResponseDto resultResDto = new DisplayContsResponseDto();
      resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_SUCCESS.getCode());
      resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_SUCCESS.getMessage());
      ContsVo resultVo = null;

      // ======================================================================
      // # 처리
      // ======================================================================

      // ----------------------------------------------------------------------
      // # 조회
      // ----------------------------------------------------------------------
      ContsVo inContsVo = new ContsVo();
      inContsVo.setDpContsId(displayManageRequestDto.getDpContsId());
      inContsVo.setHostUrl(displayManageRequestDto.getHostUrl());
      inContsVo.setGoodsDetailUrl(displayManageRequestDto.getGoodsDetailUrl());
      resultVo = displayManageMapper.selectDpContsInfo(inContsVo);

      if (resultVo != null) {
        resultResDto.setDetail(resultVo);
        resultResDto.setTotal(1);
      }
      else {
        // 조회건 없음
        log.debug("# 조회건 없음");
        resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_CONTS_NO_EXIST.getCode());
        resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_CONTS_NO_EXIST.getMessage());
        //throw new BaseException(UserEnums.DisplayManage.DISPLAY_MANAGE_CONTS_NO_EXIST);
      }

      // ======================================================================
      // # 반환
      // ======================================================================
      return resultResDto;
    }

    /**
     * 콘텐츠 수정
     * @param contsVo
     * @return
     * @throws Exception
     */
    protected DisplayContsResponseDto putConts (ContsVo contsVo) throws BaseException {
      log.debug("# ######################################");
      log.debug("# DisplayManageService.putConts Start");
      log.debug("# ######################################");
      if (contsVo != null) {
        log.debug("# In.contsVo :: " + contsVo.toString());
      }
      else {
        log.debug("# In.contsVo is Null...");
      }

      // ======================================================================
      // # 초기화
      // ======================================================================
      DisplayContsResponseDto resultResDto = new DisplayContsResponseDto();
      resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_SUCCESS.getCode());
      resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_SUCCESS.getMessage());
      int resultInt = 0;

      // ======================================================================
      // # 처리
      // ======================================================================

      try {

        // --------------------------------------------------------------------
        // # 세션정보 Set
        // --------------------------------------------------------------------
        if (SessionUtil.getBosUserVO() != null) {
          contsVo.setCreateId((SessionUtil.getBosUserVO()).getUserId());
          contsVo.setModifyId((SessionUtil.getBosUserVO()).getUserId());
        }
        else {
          contsVo.setCreateId("0");
          contsVo.setModifyId("0");
        }

        // --------------------------------------------------------------------
        // 노출텍스트 색상 기본값 Set
        //    - text 값 입력 & textColor 미입력 시 기본값 #000000 처리
        // --------------------------------------------------------------------
        if (StringUtil.isNotEmpty(contsVo.getText1()) && StringUtil.isEmpty(contsVo.getText1Color())) {
          contsVo.setText1Color("#000000");
        }
        if (StringUtil.isNotEmpty(contsVo.getText2()) && StringUtil.isEmpty(contsVo.getText2Color())) {
          contsVo.setText2Color("#000000");
        }
        if (StringUtil.isNotEmpty(contsVo.getText3()) && StringUtil.isEmpty(contsVo.getText3Color())) {
          contsVo.setText3Color("#000000");
        }

        // --------------------------------------------------------------------
        // # 수정
        // --------------------------------------------------------------------
        if (DisplayEnums.ContentsType.GOODS.getCode().equals(StringUtil.nvl(contsVo.getContsTp()))) {
          // 콘텐츠유형이 상품인 경우
          resultInt = displayManageMapper.putContsForGoods(contsVo);
        }
        else {
          // 상품 이외인 경우
          resultInt = displayManageMapper.putConts(contsVo);
        }



        if (resultInt > 0) {
          resultResDto.setTotal(resultInt);
        }
        else {
          // 수정건 없음
          log.debug("# 수정건 없음");
          resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_CONTS_FAIL_PUT_NO_PROC.getCode());
          resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_CONTS_FAIL_PUT_NO_PROC.getMessage());
          throw new BaseException(UserEnums.DisplayManage.DISPLAY_MANAGE_CONTS_FAIL_PUT_NO_PROC);
        }
      }
      catch (BaseException be) {
        log.info("# putConts BaseException e :: " + be.toString());
        throw be;
      }
      catch (Exception e) {
        log.info("# putConts Exception e :: " + e.toString());
        throw new BaseException(UserEnums.DisplayManage.DISPLAY_MANAGE_CONTS_FAIL_PUT);
      }

      // ======================================================================
      // # 반환
      // ======================================================================
      return resultResDto;
    }

    /**
     * 콘텐츠 순서변경
     * @param contsVoList
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unused")
    protected DisplayContsResponseDto putContsSort (List<ContsVo> contsVoList) throws BaseException {
      log.debug("# ######################################");
      log.debug("# DisplayManageService.putContsSort Start");
      log.debug("# ######################################");
      if (contsVoList != null) {
        log.debug("# In.contsVoList.size :: " + contsVoList.size());
        log.debug("# In.contsVoList :: " + contsVoList.toString());
      }
      else {
        log.debug("# In.contsVoList is Null or size 0");
      }

      // ======================================================================
      // # 초기화
      // ======================================================================
      DisplayContsResponseDto resultResDto = new DisplayContsResponseDto();
      resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_SUCCESS.getCode());
      resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_SUCCESS.getMessage());
      int resultInt = 0;
      int resultTotalInt = 0;

      // ======================================================================
      // # 처리
      // ======================================================================

      try {

        if (contsVoList == null || contsVoList.size() <= 0) {
          // 순번변경 대상 없음
          resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_CONTS_FAIL_PUT_SORT_NO_TARGET.getCode());
          resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_CONTS_FAIL_PUT_SORT_NO_TARGET.getMessage());
          resultResDto.setTotal(0);
          return resultResDto;
        }

        for (ContsVo unitContsVo : contsVoList) {

          // ------------------------------------------------------------------
          // # 세션정보 Set
          // ------------------------------------------------------------------
          if (SessionUtil.getBosUserVO() != null) {
            unitContsVo.setCreateId((SessionUtil.getBosUserVO()).getUserId());
            unitContsVo.setModifyId((SessionUtil.getBosUserVO()).getUserId());
          }
          else {
            unitContsVo.setCreateId("0");
            unitContsVo.setModifyId("0");
          }

          // ------------------------------------------------------------------
          // # 수정
          // ------------------------------------------------------------------
          resultInt = displayManageMapper.putContsSort(unitContsVo);

          if (resultInt <= 0) {
            // # 한건이라도 실패할 경우 모두 롤백
            log.debug("# 수정건 없음");
            resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_CONTS_FAIL_PUT_SORT_NO_PROC.getCode());
            resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_CONTS_FAIL_PUT_SORT_NO_PROC.getMessage());
            throw new BaseException(UserEnums.DisplayManage.DISPLAY_MANAGE_CONTS_FAIL_PUT_SORT_NO_PROC);
          }
          resultTotalInt++;
        } // End of for (ContsVo unitContsVo : contsVoList)

        // --------------------------------------------------------------------
        // # 처리건 Set
        // --------------------------------------------------------------------
        resultResDto.setTotal(resultTotalInt);

      }
      catch (BaseException be) {
        log.info("# putContsSort BaseException e :: " + be.toString());
        throw be;
      }
      catch (Exception e) {
        log.info("# putContsSort Exception e :: " + e.toString());
        throw new BaseException(UserEnums.DisplayManage.DISPLAY_MANAGE_CONTS_FAIL_PUT_SORT);
      }

      // ======================================================================
      // # 반환
      // ======================================================================
      return resultResDto;
    }

    /**
     * 콘텐츠 삭제
     * @param contsVoList
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unused")
    protected DisplayContsResponseDto delConts (List<ContsVo> contsVoList) throws BaseException {
      log.debug("# ######################################");
      log.debug("# DisplayManageService.delConts Start");
      log.debug("# ######################################");
      if (contsVoList != null) {
        log.debug("# In.contsVoList.size :: " + contsVoList.size());
        log.debug("# In.contsVoList :: " + contsVoList.toString());
      }
      else {
        log.debug("# In.contsVoList is Null or size 0");
      }

      // ======================================================================
      // # 초기화
      // ======================================================================
      DisplayContsResponseDto resultResDto = new DisplayContsResponseDto();
      resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_SUCCESS.getCode());
      resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_SUCCESS.getMessage());
      int resultInt = 0;
      int resultTotalInt = 0;

      // ======================================================================
      // # 처리
      // ======================================================================

      try {

        if (contsVoList == null || contsVoList.size() <= 0) {
          // 순번변경 대상 없음
          resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_CONTS_FAIL_DEL_NO_TARGET.getCode());
          resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_CONTS_FAIL_DEL_NO_TARGET.getMessage());
          resultResDto.setTotal(0);
          throw new BaseException(UserEnums.DisplayManage.DISPLAY_MANAGE_CONTS_FAIL_DEL_NO_TARGET);
          //return resultResDto;
        }

        for (ContsVo unitContsVo : contsVoList) {

          // ------------------------------------------------------------------
          // # 세션정보 Set
          // ------------------------------------------------------------------
          if (SessionUtil.getBosUserVO() != null) {
            unitContsVo.setCreateId((SessionUtil.getBosUserVO()).getUserId());
            unitContsVo.setModifyId((SessionUtil.getBosUserVO()).getUserId());
          }
          else {
            unitContsVo.setCreateId("0");
            unitContsVo.setModifyId("0");
          }

          // ------------------------------------------------------------------
          // # 삭제
          // ------------------------------------------------------------------
          resultInt = displayManageMapper.delConts(unitContsVo);

          if (resultInt <= 0) {
            // # 한건이라도 실패할 경우 모두 롤백
            log.debug("# 삭제건 없음");
            resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_CONTS_FAIL_DEL_NO_PROC.getCode());
            resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_CONTS_FAIL_DEL_NO_PROC.getMessage());
            throw new BaseException(UserEnums.DisplayManage.DISPLAY_MANAGE_CONTS_FAIL_DEL_NO_PROC);
          }
          resultTotalInt++;
        } // End of for (ContsVo unitContsVo : contsVoList)

        // --------------------------------------------------------------------
        // # 처리건 Set
        // --------------------------------------------------------------------
        resultResDto.setTotal(resultTotalInt);

      }
      catch (BaseException be) {
        log.info("# delConts BaseException e :: " + be.toString());
        throw be;
      }
      catch (Exception e) {
        log.info("# delConts Exception e :: " + e.toString());
        throw new BaseException(UserEnums.DisplayManage.DISPLAY_MANAGE_CONTS_FAIL_DEL);
      }

      // ======================================================================
      // # 반환
      // ======================================================================
      return resultResDto;
    }

    /**
     * 상품컨텑츠 중복조회
     * @param contsVo
     * @return
     * @throws BaseException
     */
    protected List<ContsVo> selectDpGoodsContsDupList (ContsVo contsVo) throws BaseException {
      log.debug("# ######################################");
      log.debug("# DisplayManageService.selectDpGoodsContsDupList Start");
      log.debug("# ######################################");
      log.debug("# In.contsVo         :: " + contsVo.toString());

      // ======================================================================
      // # 초기화
      // ======================================================================
      DisplayContsResponseDto resultResDto = new DisplayContsResponseDto();
      resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_SUCCESS.getCode());
      resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_SUCCESS.getMessage());
      List<ContsVo> resultList = null;

      // ======================================================================
      // # 처리
      // ======================================================================

      // ----------------------------------------------------------------------
      // # 조회
      // ----------------------------------------------------------------------
      resultList = displayManageMapper.selectDpGoodsContsDupList(contsVo);

      if (resultList != null && resultList.size() > 0) {
        resultResDto.setRows(resultList);
        resultResDto.setTotal(resultList.size());
      }
      else {
        // 조회건 없음
        log.debug("# 조회건 없음");
        //resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_CONTS_LIST_NO_EXIST.getCode());
        //resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_CONTS_LIST_NO_EXIST.getMessage());
        //throw new BaseException(UserEnums.DisplayManage.DISPLAY_MANAGE_CONTS_LIST_NO_EXIST);
      }

      // ======================================================================
      // # 반환
      // ======================================================================
      return resultList;
    }


    /**
     * 콘텐츠 등록
     * @param contsVo
     * @return
     * @throws Exception
     */
    protected DisplayContsResponseDto addConts (ContsVo contsVo) throws BaseException {
      log.debug("# ######################################");
      log.debug("# DisplayManageService.addConts Start");
      log.debug("# ######################################");
      if (contsVo != null) {
        log.debug("# In.contsVo :: " + contsVo.toString());
      }
      else {
        log.debug("# In.contsVo is Null...");
      }

      // ======================================================================
      // # 초기화
      // ======================================================================
      DisplayContsResponseDto resultResDto = new DisplayContsResponseDto();
      resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_SUCCESS.getCode());
      resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_SUCCESS.getMessage());
      int resultInt = 0;
      int resultUpdInt = 0;

      // ======================================================================
      // # 처리
      // ======================================================================

      try {

        // --------------------------------------------------------------------
        // # 세션정보 Set
        // --------------------------------------------------------------------
        if (SessionUtil.getBosUserVO() != null) {
          contsVo.setCreateId((SessionUtil.getBosUserVO()).getUserId());
          contsVo.setModifyId((SessionUtil.getBosUserVO()).getUserId());
        }
        else {
          contsVo.setCreateId("0");
          contsVo.setModifyId("0");
        }

        // --------------------------------------------------------------------
        // 노출텍스트 색상 기본값 Set
        //    - text 값 입력 & textColor 미입력 시 기본값 #000000 처리
        // --------------------------------------------------------------------
        if (StringUtil.isNotEmpty(contsVo.getText1()) && StringUtil.isEmpty(contsVo.getText1Color())) {
          contsVo.setText1Color("#000000");
        }
        if (StringUtil.isNotEmpty(contsVo.getText2()) && StringUtil.isEmpty(contsVo.getText2Color())) {
          contsVo.setText2Color("#000000");
        }
        if (StringUtil.isNotEmpty(contsVo.getText3()) && StringUtil.isEmpty(contsVo.getText3Color())) {
          contsVo.setText3Color("#000000");
        }

        // --------------------------------------------------------------------
        // # 1.등록
        // --------------------------------------------------------------------
        resultInt = displayManageMapper.addConts(contsVo);

        if (resultInt > 0) {

          resultResDto.setTotal(resultInt);

          // ------------------------------------------------------------------
          // # 2.후처리
          // ------------------------------------------------------------------
          // 콘텐츠1/2/3ID Set
          if (contsVo.getContsLevel() == 1) {
            contsVo.setLevel1ContsId(contsVo.getDpContsId());
          }
          else if (contsVo.getContsLevel() == 2) {
            contsVo.setLevel2ContsId(contsVo.getDpContsId());
          }
          else if (contsVo.getContsLevel() == 3) {
            contsVo.setLevel3ContsId(contsVo.getDpContsId());
          }

          // 콘텐츠1/2/3ID 업데이트
          resultUpdInt = displayManageMapper.putContsAfterAdd(contsVo);

          if (resultUpdInt > 0) {
            resultResDto.setTotal(resultInt);
          }
          else {
            log.debug("# 후처리 실패");
            resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_CONTS_FAIL_ADD_AFTER_NO_PROC.getCode());
            resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_CONTS_FAIL_ADD_AFTER_NO_PROC.getMessage());
            throw new BaseException(UserEnums.DisplayManage.DISPLAY_MANAGE_CONTS_FAIL_ADD_AFTER_NO_PROC);
          }
        }
        else {
          // 등록건 없음
          log.debug("# 등록건 없음");
          resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_CONTS_FAIL_ADD_NO_PROC.getCode());
          resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_CONTS_FAIL_ADD_NO_PROC.getMessage());
          throw new BaseException(UserEnums.DisplayManage.DISPLAY_MANAGE_CONTS_FAIL_ADD_NO_PROC);
        }
      }
      catch (BaseException be) {
        log.info("# addConts BaseException e :: " + be.toString());
        throw be;
      }
      catch (Exception e) {
        log.info("# addConts Exception e :: " + e.toString());
        throw new BaseException(UserEnums.DisplayManage.DISPLAY_MANAGE_CONTS_FAIL_ADD);
      }

      // ======================================================================
      // # 반환
      // ======================================================================
      return resultResDto;
    }

    /**
     * 브랜드 리스트조회(콤보용)
     * @param dpInventoryId
     * @param prntsContsId
     * @param useYn
     * @return
     * @throws Exception
     */
    protected ApiResult<?> selectBrandList (DisplayManageRequestDto displayManageRequestDto) throws BaseException {
      log.debug("# ######################################");
      log.debug("# DisplayManageService.selectBrandList Start");
      log.debug("# ######################################");
      log.debug("# In.dpInventoryId :: " + displayManageRequestDto.getDpInventoryId());
      log.debug("# In.prntsContsId  :: " + displayManageRequestDto.getPrntsContsId());
      //log.debug("# In.useYn         :: " + displayManageRequestDto.getUseYn());

      GetCodeListResponseDto result = new GetCodeListResponseDto();
      ContsVo inContsVo = new ContsVo();
      inContsVo.setUseYn("Y");
      List<GetCodeListResultVo> rows = displayManageMapper.selectBrandList(inContsVo);
      result.setRows(rows);

      return ApiResult.success(result);
    }

    /**
     * 상품목록조회-키워드조회
     * @param dpInventoryId
     * @param prntsContsId
     * @param useYn
     * @return
     * @throws Exception
     */
    protected DisplayContsResponseDto selectGoodsListByKeyword (DisplayManageRequestDto displayManageRequestDto) throws BaseException {
      log.debug("# ######################################");
      log.debug("# DisplayManageService.selectGoodsListByKeyword Start");
      log.debug("# ######################################");
      //log.debug("# In.dpInventoryId :: " + displayManageRequestDto.getDpInventoryId());
      //log.debug("# In.prntsContsId  :: " + displayManageRequestDto.getPrntsContsId());
      //log.debug("# In.useYn         :: " + displayManageRequestDto.getUseYn());

      // ======================================================================
      // # 초기화
      // ======================================================================
      DisplayContsResponseDto resultResDto = new DisplayContsResponseDto();
      resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_SUCCESS.getCode());
      resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_SUCCESS.getMessage());
      List<ContsVo> resultList = null;

      // ======================================================================
      // # 처리
      // ======================================================================

      // ----------------------------------------------------------------------
      // # 조회
      // ----------------------------------------------------------------------

      // 키워드String을 List<String> 으로 변환
      String findKeywordListString = displayManageRequestDto.getFindKeywordListString();
      String splitKey = Constants.ARRAY_LINE_BREAK_OR_COMMA_SEPARATORS;

      List<String> searchKeyList = new ArrayList<String>();

      if( StringUtils.isNotEmpty(findKeywordListString) && findKeywordListString.indexOf("ALL") < 0 ) {
          searchKeyList.addAll(Stream.of(findKeywordListString.split(splitKey))
                                     .map(String::trim)
                                     .filter( x -> StringUtils.isNotEmpty(x) )
                                     .collect(Collectors.toList()));
      }

      DisplayManageRequestDto reqDto = new DisplayManageRequestDto();
      reqDto.setFindKeywordList(searchKeyList);
      reqDto.setSearchCondition(displayManageRequestDto.getSearchCondition());

      //try {
      //}
      //catch (Exception e) {
      //  log.debug("# DisplayManageService.selectGoodsListByKeyword 33333");
      //  e.printStackTrace();
      //}
      // Mapper 호출 시 없는 항목 Exception 던지지 않음
      resultList = displayManageMapper.selectGoodsListByKeyword(reqDto);

      if (resultList != null && resultList.size() > 0) {
        resultResDto.setRows(resultList);
        resultResDto.setTotal(resultList.size());
      }
      else {
        // 조회건 없음
        log.debug("# 조회건 없음");
        resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_CONTS_GOODS_LIST_NO_EXIST.getCode());
        resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_CONTS_GOODS_LIST_NO_EXIST.getMessage());
        //throw new BaseException(UserEnums.DisplayManage.DISPLAY_MANAGE_CONTS_LIST_NO_EXIST);
      }

      // ======================================================================
      // # 반환
      // ======================================================================
      return resultResDto;
    }

    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // 인벤토리그룹관리
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    /**
     * 그룹 리스트조회
     * @param dpPageId
     * @param useAllYn
     * @return
     * @throws Exception
     */
    protected DisplayPageResponseDto selectDpInventoryGroupList (DisplayManageRequestDto displayManageRequestDto) throws BaseException {
      log.info("# ######################################");
      log.info("# DisplayManageService.selectDpInventoryGroupList Start");
      log.info("# ######################################");

      // ======================================================================
      // # 초기화
      // ======================================================================
      DisplayPageResponseDto resultResDto = new DisplayPageResponseDto();
      resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_SUCCESS.getCode());
      resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_SUCCESS.getMessage());
      List<PageVo> resultList = null;

      String userId = "";

      // ======================================================================
      // # 처리
      // ======================================================================

      // --------------------------------------------------------------------
      // # 세션정보 Set
      // --------------------------------------------------------------------
      if (SessionUtil.getBosUserVO() != null) {
        userId = (SessionUtil.getBosUserVO()).getUserId();
      }
      else {
        // 사용자정보 없음
        log.debug("# 사용자정보 없음");
        resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_GRP_NO_USER_INFO.getCode());
        resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_GRP_NO_USER_INFO.getMessage());
        return resultResDto;
      }

      // ----------------------------------------------------------------------
      // # 조회
      // ----------------------------------------------------------------------
      resultList = displayManageMapper.selectDpInventoryGroupList(userId, displayManageRequestDto.getUseYn());

      if (resultList != null && resultList.size() > 0) {
        resultResDto.setRows(resultList);
        resultResDto.setTotal(resultList.size());
      }
      else {
        // 조회건 없음
        log.debug("# 조회건 없음");
        resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_GRP_LIST_NO_EXIST.getCode());
        resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_GRP_LIST_NO_EXIST.getMessage());
      }

      // ======================================================================
      // # 반환
      // ======================================================================
      return resultResDto;
    }

    /**
     * 인벤토리그룹 인벤토리 리스트조회
     * @param displayManageRequestDto
     * @return
     * @throws Exception
     */
    protected DisplayInventoryResponseDto selectDpGroupInventoryList (DisplayManageRequestDto displayManageRequestDto) throws BaseException {
      log.debug("# ######################################");
      log.debug("# DisplayManageService.selectDpGroupInventoryList Start");
      log.debug("# ######################################");
      log.debug("# In.dpInventoryGrpId  :: " + displayManageRequestDto.getDpInventoryGrpId());
      log.debug("# In.useYn             :: " + displayManageRequestDto.getUseYn());

      // ======================================================================
      // # 초기화
      // ======================================================================
      DisplayInventoryResponseDto resultResDto = new DisplayInventoryResponseDto();
      resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_SUCCESS.getCode());
      resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_SUCCESS.getMessage());
      List<InventoryVo> resultList = null;

      // ======================================================================
      // # 처리
      // ======================================================================

      // ----------------------------------------------------------------------
      // # 조회
      // ----------------------------------------------------------------------
      resultList = displayManageMapper.selectDpGroupInventoryList(displayManageRequestDto.getDpInventoryGrpId(), displayManageRequestDto.getUseYn());

      if (resultList != null && resultList.size() > 0) {
        resultResDto.setRows(resultList);
        resultResDto.setTotal(resultList.size());
      }
      else {
        // 조회건 없음
        log.debug("# 조회건 없음");
        resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_GRP_LIST_NO_INVENTORY_EXIST.getCode());
        resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_GRP_LIST_NO_INVENTORY_EXIST.getMessage());
        //throw new BaseException(UserEnums.DisplayManage.DISPLAY_MANAGE_INVENTORY_LIST_NO_EXIST);
      }

      // ======================================================================
      // # 반환
      // ======================================================================
      return resultResDto;
    }

    /**
     * 인벤토리그룹구성 리스트조회
     * @param displayManageRequestDto
     * @return
     * @throws Exception
     */
    protected DisplayInventoryResponseDto selectDpGroupInventoryMappingList (DisplayManageRequestDto displayManageRequestDto) throws BaseException {
      log.debug("# ######################################");
      log.debug("# DisplayManageService.selectDpGroupInventoryMappingList Start");
      log.debug("# ######################################");
      log.debug("# In.dpInventoryGrpId  :: " + displayManageRequestDto.getDpInventoryGrpId());


      // ======================================================================
      // # 초기화
      // ======================================================================
      DisplayInventoryResponseDto resultResDto = new DisplayInventoryResponseDto();
      resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_SUCCESS.getCode());
      resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_SUCCESS.getMessage());
      List<InventoryVo> resultList = null;
      String inventoryCdsString = "";    // 인벤토리코드 목록 문자

      // ======================================================================
      // # 처리
      // ======================================================================

      // ----------------------------------------------------------------------
      // # 조회
      // ----------------------------------------------------------------------
      resultList = displayManageMapper.selectDpGroupInventoryMappingList(displayManageRequestDto.getDpInventoryGrpId());

      if (resultList != null && resultList.size() > 0) {
        resultResDto.setRows(resultList);
        resultResDto.setTotal(resultList.size());

        int i = 0;

        for (InventoryVo inventory : resultList) {

          if (i >0) {
            inventoryCdsString += ", ";
          }

          if (StringUtil.isEquals(inventory.getPageTp(), "PAGE_TP.CATEGORY")) {
            // 페이지유형:카테고리
            inventoryCdsString += (inventory.getInventoryCd() + ":" + StringUtil.nvl(inventory.getIlCtgryId()));
          }
          else {
            // 페이지유형:페이지
            inventoryCdsString += inventory.getInventoryCd();
          }

          i++;
        }
        resultResDto.setInventoryCdsString(inventoryCdsString);

      }
      else {
        // 조회건 없음
        log.debug("# 조회건 없음");
        resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_GRP_LIST_NO_INVENTORY_EXIST.getCode());
        resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_GRP_LIST_NO_INVENTORY_EXIST.getMessage());
        //throw new BaseException(UserEnums.DisplayManage.DISPLAY_MANAGE_INVENTORY_LIST_NO_EXIST);
      }

      // ======================================================================
      // # 반환
      // ======================================================================
      return resultResDto;
    }

    /**
     * 인벤토리그룹 등록
     * @param inventoryVo
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unused")
    protected DisplayPageResponseDto addInventoryGroup (InventoryVo inventoryVo) throws BaseException {
      log.debug("# ######################################");
      log.debug("# DisplayManageService.addInventoryGroup Start");
      log.debug("# ######################################");
      if (inventoryVo != null) {
        log.debug("# In.inventoryVo :: " + inventoryVo.toString());
      }
      else {
        log.debug("# In.inventoryVo is Null...");
      }

      // ======================================================================
      // # 초기화
      // ======================================================================
      DisplayPageResponseDto resultResDto = new DisplayPageResponseDto();
      resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_SUCCESS.getCode());
      resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_SUCCESS.getMessage());
      int resultInt = 0;
      int resultIntMapping = 0;
      List<InventoryVo> resultPageList      = null;
      List<InventoryVo> resultCategoryList  = null;
      List<InventoryVo> inventoryGrpMappingList = new ArrayList<InventoryVo>();

      String inventoryCdsString = "";
      List<String> inventoryCdList         = new ArrayList<String>();
      List<String> inventoryCdPageList     = new ArrayList<String>();
      List<Map<String, String>> inventoryCdCategoryMapList = new ArrayList<Map<String, String>>();

      // ======================================================================
      // # 처리
      // ======================================================================

      try {

        // --------------------------------------------------------------------
        // # 세션정보 Set
        // --------------------------------------------------------------------
        if (SessionUtil.getBosUserVO() != null) {
          inventoryVo.setCreateId((SessionUtil.getBosUserVO()).getUserId());
          inventoryVo.setModifyId((SessionUtil.getBosUserVO()).getUserId());
        }
        else {
          inventoryVo.setCreateId("0");
          inventoryVo.setModifyId("0");
        }

        //  --------------------------------------------------------------------
        // 1.인벤토리코드 페이지유형별로 분리
        //   - 페이지유형:페이지   => 인벤토리코드
        //   - 페이지유형.카테고리 => 인벤토리코드:카테고리ID
        // --------------------------------------------------------------------
        inventoryCdsString = StringUtil.nvl(inventoryVo.getInventoryCdsString());
        String splitKey = Constants.ARRAY_LINE_BREAK_OR_COMMA_SEPARATORS;

        inventoryCdList.addAll(Stream.of(inventoryCdsString.split(splitKey))
                       .map(String::trim)
                       .filter( x -> StringUtils.isNotEmpty(x) )
                       .collect(Collectors.toList()));

        if (inventoryCdList != null && inventoryCdList.size() > 0) {

          for (String inventoryCd : inventoryCdList) {

            if (inventoryCd.contains(":")) {
              // --------------------------------------------------------------
              // 카테고리
              // --------------------------------------------------------------
              // 인벤토리코드카테고리 리스트
              String[] tmpArr = inventoryCd.split(":");
              // 인벤토리코드/카테고리ID 리스트
              Map<String, String> tmpMap = new HashMap<String, String>();
              tmpMap.put("inventoryCd", tmpArr[0]); // 인벤토리코드
              tmpMap.put("ilCtgryId"  , tmpArr[1]); // 카테고리ID
              inventoryCdCategoryMapList.add(tmpMap);
            }
            else {
              // --------------------------------------------------------------
              // 페이지
              // --------------------------------------------------------------
              // 인벤토리코드페이지 리스트
              inventoryCdPageList.add(inventoryCd);
            }
          }
        }

        // --------------------------------------------------------------------
        // 2.인벤토리코드 체크
        // --------------------------------------------------------------------
        // 2.1.페이지유형:페이지
        // --------------------------------------------------------------------
        if (inventoryCdPageList.size() > 0) {

          resultPageList = displayManageMapper.selectInventoryCdList(null, inventoryCdPageList, "PAGE_TP.PAGE");

          if (resultPageList == null || resultPageList.size() != inventoryCdPageList.size()) {
            // 페이지유형:페이지 인벤토리코너 비정상
            log.debug("# 페이지유형:페이지 인벤토리코너 유효성 실패");
            resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_GRP_INVENTORY_CD_PAGE_ERROR.getCode());
            resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_GRP_INVENTORY_CD_PAGE_ERROR.getMessage());
            throw new BaseException(UserEnums.DisplayManage.DISPLAY_MANAGE_GRP_INVENTORY_CD_PAGE_ERROR);
          }
          // 저장리스트에 Add
          inventoryGrpMappingList.addAll(resultPageList);
        }

        // --------------------------------------------------------------------
        // 2.2.페이지유형:카테고리
        // --------------------------------------------------------------------
        int checkCategorySucCnt = 0;
        if (inventoryCdCategoryMapList.size() > 0) {

          for (Map<String, String> unitMap : inventoryCdCategoryMapList) {

            resultCategoryList = displayManageMapper.selectInventoryCdListForCategory((String)unitMap.get("inventoryCd"), "PAGE_TP.CATEGORY", (String)unitMap.get("ilCtgryId"));

            if (resultCategoryList != null && resultCategoryList.size() == 1) {
              // 저장리스트에 Add
              inventoryGrpMappingList.add(resultCategoryList.get(0));
              // 한건 존재해야 정상임
              checkCategorySucCnt++;
            }
          } // End of for (Map<String, String> unitMap : inventoryCdCategoryMapList)

          if (inventoryCdCategoryMapList.size() != checkCategorySucCnt) {
            // 페이지유형:카테고리 인벤토리코너 비정상
            log.debug("# 페이지유형:카테고리 인벤토리코너:카테고리코드 유효성 실패");
            resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_GRP_INVENTORY_CD_CATEGORY_ERROR.getCode());
            resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_GRP_INVENTORY_CD_CATEGORY_ERROR.getMessage());
            throw new BaseException(UserEnums.DisplayManage.DISPLAY_MANAGE_GRP_INVENTORY_CD_CATEGORY_ERROR);
          }
        } // End of if (inventoryCdCategoryMapList.size() > 0)

        // --------------------------------------------------------------------
        // 2.3.모든 페이지유형의 인벤토리코드 체크
        // --------------------------------------------------------------------
        int checkPageSucCnt      = 0;

        if (resultPageList != null) {
          checkPageSucCnt = resultPageList.size();
        }

        if (checkPageSucCnt + checkCategorySucCnt == 0) {
          log.debug("# 인벤토리코너 미존재");
          resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_GRP_INVENTORY_CD_ERROR.getCode());
          resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_GRP_INVENTORY_CD_ERROR.getMessage());
          throw new BaseException(UserEnums.DisplayManage.DISPLAY_MANAGE_GRP_INVENTORY_CD_ERROR);
        }

        // --------------------------------------------------------------------
        // 3.인벤토리그룹 등록
        // --------------------------------------------------------------------
        inventoryVo.setDelYn("N");  // 삭제여부
        resultInt = displayManageMapper.addInventoryGroup(inventoryVo);

        if (resultInt <= 0) {
          log.debug("# 인벤토르그룹 등록건 없음");
          resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_GRP_FAIL_ADD_NO_PROC.getCode());
          resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_GRP_FAIL_ADD_NO_PROC.getMessage());
          throw new BaseException(UserEnums.DisplayManage.DISPLAY_MANAGE_GRP_FAIL_ADD_NO_PROC);
        }
        log.debug("# dpInventoryGrpId :: " + inventoryVo.getDpInventoryGrpId());

        //// 등록된 객체 응답에 Set
        //resultResDto.setDetail(inventoryVo);

        // --------------------------------------------------------------------
        // 4.인벤토리그룹구성 등록
        // --------------------------------------------------------------------
        for (InventoryVo unitVo : inventoryGrpMappingList) {

          unitVo.setDpInventoryGrpId(inventoryVo.getDpInventoryGrpId());
          unitVo.setUseYn("Y");   // 사용여부
          unitVo.setDelYn("N");   // 삭제여부
          unitVo.setCreateId(inventoryVo.getCreateId());  // 등록자ID
          unitVo.setModifyId(inventoryVo.getModifyId());  // 수정자ID
          resultInt = displayManageMapper.addInventoryGroupMapping(unitVo);
          resultIntMapping += resultInt;
        }

        // --------------------------------------------------------------------
        // 5. 인벤토리그룹 상세정보 조회
        // --------------------------------------------------------------------
        PageVo inventoryGrpInfo = displayManageMapper.selectDpInventoryGroupInfo(inventoryVo.getDpInventoryGrpId());

        if (inventoryGrpInfo != null) {
          resultResDto.setDetail(inventoryGrpInfo);
          resultResDto.setTotal(1);
        }

      }
      catch (BaseException be) {
        log.info("# addInventory BaseException be :: " + be.toString());
        throw be;
      }
      catch (Exception e) {
        log.info("# addInventory Exception e :: " + e.toString());
        //e.printStackTrace();
        throw new BaseException(UserEnums.DisplayManage.DISPLAY_MANAGE_GRP_FAIL_ADD);
      }

      // ======================================================================
      // # 반환
      // ======================================================================
      return resultResDto;
    }

    /**
     * 인벤토리그룹 수정
     * @param inventoryVo
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unused")
    protected DisplayInventoryResponseDto putInventoryGroup (InventoryVo inventoryVo) throws BaseException {
      log.debug("# ######################################");
      log.debug("# DisplayManageService.putInventoryGroup Start");
      log.debug("# ######################################");
      if (inventoryVo != null) {
        log.debug("# In.inventoryVo :: " + inventoryVo.toString());
      }
      else {
        log.debug("# In.inventoryVo is Null...");
      }

      // ======================================================================
      // # 초기화
      // ======================================================================
      DisplayInventoryResponseDto resultResDto = new DisplayInventoryResponseDto();
      resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_SUCCESS.getCode());
      resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_SUCCESS.getMessage());
      int resultInt = 0;
      int resultIntMapping = 0;
      List<InventoryVo> resultPageList      = null;
      List<InventoryVo> resultCategoryList  = null;
      List<InventoryVo> inventoryGrpMappingList = new ArrayList<InventoryVo>();

      String inventoryCdsString = "";
      List<String> inventoryCdList         = new ArrayList<String>();
      List<String> inventoryCdPageList     = new ArrayList<String>();
      List<Map<String, String>> inventoryCdCategoryMapList = new ArrayList<Map<String, String>>();

      // ======================================================================
      // # 처리
      // ======================================================================

      try {

        // --------------------------------------------------------------------
        // # 세션정보 Set
        // --------------------------------------------------------------------
        if (SessionUtil.getBosUserVO() != null) {
          inventoryVo.setCreateId((SessionUtil.getBosUserVO()).getUserId());
          inventoryVo.setModifyId((SessionUtil.getBosUserVO()).getUserId());
        }
        else {
          inventoryVo.setCreateId("0");
          inventoryVo.setModifyId("0");
        }

        //  --------------------------------------------------------------------
        // 1.인벤토리코드 페이지유형별로 분리
        //   - 페이지유형:페이지   => 인벤토리코드
        //   - 페이지유형.카테고리 => 인벤토리코드:카테고리ID
        // --------------------------------------------------------------------
        inventoryCdsString = StringUtil.nvl(inventoryVo.getInventoryCdsString());
        String splitKey = Constants.ARRAY_LINE_BREAK_OR_COMMA_SEPARATORS;

        inventoryCdList.addAll(Stream.of(inventoryCdsString.split(splitKey))
                       .map(String::trim)
                       .filter( x -> StringUtils.isNotEmpty(x) )
                       .collect(Collectors.toList()));

        if (inventoryCdList != null && inventoryCdList.size() > 0) {

          for (String inventoryCd : inventoryCdList) {

            if (inventoryCd.contains(":")) {
              // --------------------------------------------------------------
              // 카테고리
              // --------------------------------------------------------------
              // 인벤토리코드카테고리 리스트
              String[] tmpArr = inventoryCd.split(":");
              // 인벤토리코드/카테고리ID 리스트
              Map<String, String> tmpMap = new HashMap<String, String>();
              tmpMap.put("inventoryCd", tmpArr[0]); // 인벤토리코드
              tmpMap.put("ilCtgryId"  , tmpArr[1]); // 카테고리ID
              inventoryCdCategoryMapList.add(tmpMap);
            }
            else {
              // --------------------------------------------------------------
              // 페이지
              // --------------------------------------------------------------
              // 인벤토리코드페이지 리스트
              inventoryCdPageList.add(inventoryCd);
            }
          }
        }

        // --------------------------------------------------------------------
        // 2.인벤토리코드 체크
        // --------------------------------------------------------------------
        // 2.1.페이지유형:페이지
        // --------------------------------------------------------------------
        if (inventoryCdPageList.size() > 0) {

          resultPageList = displayManageMapper.selectInventoryCdList(null, inventoryCdPageList, "PAGE_TP.PAGE");

          if (resultPageList == null || resultPageList.size() != inventoryCdPageList.size()) {
            // 페이지유형:페이지 인벤토리코너 비정상
            log.debug("# 페이지유형:페이지 인벤토리코너 비정상");
            resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_GRP_INVENTORY_CD_PAGE_ERROR.getCode());
            resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_GRP_INVENTORY_CD_PAGE_ERROR.getMessage());
            throw new BaseException(UserEnums.DisplayManage.DISPLAY_MANAGE_GRP_INVENTORY_CD_PAGE_ERROR);
          }
          // 저장리스트에 Add
          inventoryGrpMappingList.addAll(resultPageList);
        }

        // --------------------------------------------------------------------
        // 2.2.페이지유형:카테고리
        // --------------------------------------------------------------------
        int checkCategorySucCnt = 0;
        if (inventoryCdCategoryMapList.size() > 0) {

          for (Map<String, String> unitMap : inventoryCdCategoryMapList) {

            resultCategoryList = displayManageMapper.selectInventoryCdListForCategory((String)unitMap.get("inventoryCd"), "PAGE_TP.CATEGORY", (String)unitMap.get("ilCtgryId"));

            if (resultCategoryList != null && resultCategoryList.size() == 1) {
              // 저장리스트에 Add
              inventoryGrpMappingList.add(resultCategoryList.get(0));
              // 한건 존재해야 정상임
              checkCategorySucCnt++;
            }
          } // End of for (Map<String, String> unitMap : inventoryCdCategoryMapList)

          if (inventoryCdCategoryMapList.size() != checkCategorySucCnt) {
            // 페이지유형:카테고리 인벤토리코너 비정상
            log.debug("# 페이지유형:페이지 인벤토리코너 비정상");
            resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_GRP_INVENTORY_CD_CATEGORY_ERROR.getCode());
            resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_GRP_INVENTORY_CD_CATEGORY_ERROR.getMessage());
            throw new BaseException(UserEnums.DisplayManage.DISPLAY_MANAGE_GRP_INVENTORY_CD_CATEGORY_ERROR);
          }
        } // End of if (inventoryCdCategoryMapList.size() > 0)

        // --------------------------------------------------------------------
        // 2.3.모든 페이지유형의 인벤토리코드 체크
        // --------------------------------------------------------------------
        int checkPageSucCnt      = 0;

        if (resultPageList != null) {
          checkPageSucCnt = resultPageList.size();
        }

        if (checkPageSucCnt + checkCategorySucCnt == 0) {
          log.debug("# 인벤토리코너 미존재");
          resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_GRP_INVENTORY_CD_ERROR.getCode());
          resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_GRP_INVENTORY_CD_ERROR.getMessage());
          throw new BaseException(UserEnums.DisplayManage.DISPLAY_MANAGE_GRP_INVENTORY_CD_ERROR);
        }

        // --------------------------------------------------------------------
        // 2.4.이전 인벤토리그룹구성 삭제처리
        // --------------------------------------------------------------------
        InventoryVo inventoryGrpMappingVo = new InventoryVo();
        inventoryGrpMappingVo.setDpInventoryGrpId(inventoryVo.getDpInventoryGrpId()); // 인벤토리그룹ID
        inventoryGrpMappingVo.setModifyId(inventoryVo.getModifyId());                 // 수정자ID
        inventoryGrpMappingVo.setDelYn("Y");                                          // 삭제여부
        resultInt = displayManageMapper.putInventoryGroupInventoryUseDelYn(inventoryGrpMappingVo);

        if (resultInt <= 0) {
          log.debug("# 인벤토르그룹구성 기존데이터 삭제건 없음");
          //resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_GRP_FAIL_DEL_NO_PROC.getCode());
          //resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_GRP_FAIL_DEL_NO_PROC.getMessage());
          //throw new BaseException(UserEnums.DisplayManage.DISPLAY_MANAGE_GRP_FAIL_DEL_NO_PROC);
        }

        // --------------------------------------------------------------------
        // 3.인벤토리그룹 수정
        // --------------------------------------------------------------------
        inventoryVo.setDelYn("N");  // 삭제여부
        resultInt = displayManageMapper.putInventoryGroup(inventoryVo);

        if (resultInt <= 0) {
          log.debug("# 인벤토르그룹 등록건 없음");
          resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_GRP_FAIL_PUT_NO_PROC.getCode());
          resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_GRP_FAIL_PUT_NO_PROC.getMessage());
          throw new BaseException(UserEnums.DisplayManage.DISPLAY_MANAGE_GRP_FAIL_PUT_NO_PROC);
        }
        log.debug("# dpInventoryGrpId :: " + inventoryVo.getDpInventoryGrpId());

        // --------------------------------------------------------------------
        // 4.인벤토리그룹구성 등록
        // --------------------------------------------------------------------
        for (InventoryVo unitVo : inventoryGrpMappingList) {

          unitVo.setDpInventoryGrpId(inventoryVo.getDpInventoryGrpId());
          unitVo.setUseYn("Y");   // 사용여부
          unitVo.setDelYn("N");   // 삭제여부
          unitVo.setCreateId(inventoryVo.getCreateId());  // 등록자ID
          unitVo.setModifyId(inventoryVo.getModifyId());  // 수정자ID
          resultInt = displayManageMapper.addInventoryGroupMapping(unitVo);
          resultIntMapping += resultInt;
        }
        resultResDto.setTotal(1);

      }
      catch (BaseException be) {
        log.info("# addInventory BaseException be :: " + be.toString());
        throw be;
      }
      catch (Exception e) {
        log.info("# addInventory Exception e :: " + e.toString());
        throw new BaseException(UserEnums.DisplayManage.DISPLAY_MANAGE_GRP_FAIL_PUT);
      }

      // ======================================================================
      // # 반환
      // ======================================================================
      return resultResDto;
    }

    /**
     * 인벤토리그룹 삭제
     * @param inventoryVoList
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unused")
    protected DisplayInventoryResponseDto delInventoryGroup (List<InventoryVo> inventoryVoList) throws BaseException {
      log.debug("# ######################################");
      log.debug("# DisplayManageService.delInventoryGroup Start");
      log.debug("# ######################################");
      if (inventoryVoList != null) {
        log.debug("# In.inventoryVoList.size :: " + inventoryVoList.size());
        log.debug("# In.inventoryVoList :: " + inventoryVoList.toString());
      }
      else {
        log.debug("# In.inventoryVoList is Null or size 0");
      }

      // ======================================================================
      // # 초기화
      // ======================================================================
      DisplayInventoryResponseDto resultResDto = new DisplayInventoryResponseDto();
      resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_SUCCESS.getCode());
      resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_SUCCESS.getMessage());
      int resultInt = 0;
      int resultTotalInt = 0;

      // ======================================================================
      // # 처리
      // ======================================================================

      try {

        if (inventoryVoList == null || inventoryVoList.size() <= 0) {
          // 삭제 대상 없음
          resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_GRP_FAIL_DEL_NO_TARGET.getCode());
          resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_GRP_FAIL_DEL_NO_TARGET.getMessage());
          resultResDto.setTotal(0);
          return resultResDto;
        }

        for (InventoryVo unitInventoryVo : inventoryVoList) {

          // ------------------------------------------------------------------
          // # 세션정보 Set
          // ------------------------------------------------------------------
          if (SessionUtil.getBosUserVO() != null) {
            unitInventoryVo.setCreateId((SessionUtil.getBosUserVO()).getUserId());
            unitInventoryVo.setModifyId((SessionUtil.getBosUserVO()).getUserId());
          }
          else {
            unitInventoryVo.setCreateId("0");
            unitInventoryVo.setModifyId("0");
          }

          // ------------------------------------------------------------------
          // # 삭제
          // ------------------------------------------------------------------
          unitInventoryVo.setUseYn(""); // USE_YN 변경하지 않음
          resultInt = displayManageMapper.putInventoryGroupUseDelYn(unitInventoryVo);

          if (resultInt <= 0) {
            // # 한건이라도 실패할 경우 모두 롤백
            log.debug("# 삭제건 없음");
            resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_GRP_FAIL_DEL_NO_PROC.getCode());
            resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_GRP_FAIL_DEL_NO_PROC.getMessage());
            throw new BaseException(UserEnums.DisplayManage.DISPLAY_MANAGE_GRP_FAIL_DEL_NO_PROC);
          }
          resultTotalInt++;
        } // End of for (InventoryVo unitInventoryVo : inventoryVoList)

        // --------------------------------------------------------------------
        // # 처리건 Set
        // --------------------------------------------------------------------
        resultResDto.setTotal(resultTotalInt);

      }
      catch (BaseException be) {
        log.info("# delInventory BaseException e :: " + be.toString());
        throw be;
      }
      catch (Exception e) {
        log.info("# delInventory Exception e :: " + e.toString());
        throw new BaseException(UserEnums.DisplayManage.DISPLAY_MANAGE_GRP_FAIL_DEL);
      }

      // ======================================================================
      // # 반환
      // ======================================================================
      return resultResDto;
    }

    /**
     * 인벤토리그룹 순서변경
     * @param inventoryVoList
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unused")
    protected DisplayInventoryResponseDto putInventoryGroupSort (List<InventoryVo> inventoryVoList) throws BaseException {
      log.debug("# ######################################");
      log.debug("# DisplayManageService.putInventoryGroupSort Start");
      log.debug("# ######################################");
      if (inventoryVoList != null) {
        log.debug("# In.inventoryVoList.size :: " + inventoryVoList.size());
        log.debug("# In.inventoryVoList :: " + inventoryVoList.toString());
      }
      else {
        log.debug("# In.inventoryVoList is Null or size 0");
      }

      // ======================================================================
      // # 초기화
      // ======================================================================
      DisplayInventoryResponseDto resultResDto = new DisplayInventoryResponseDto();
      resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_SUCCESS.getCode());
      resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_SUCCESS.getMessage());
      int resultInt = 0;
      int resultTotalInt = 0;

      // ======================================================================
      // # 처리
      // ======================================================================

      try {

        if (inventoryVoList == null || inventoryVoList.size() <= 0) {
          // 순번변경 대상 없음
          resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_GRP_PARAM_NO_SORT_TARGET.getCode());
          resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_GRP_PARAM_NO_SORT_TARGET.getMessage());
          resultResDto.setTotal(0);
          return resultResDto;
        }

        for (InventoryVo unitInventoryVo : inventoryVoList) {

          // ------------------------------------------------------------------
          // # 세션정보 Set
          // ------------------------------------------------------------------
          if (SessionUtil.getBosUserVO() != null) {
            unitInventoryVo.setCreateId((SessionUtil.getBosUserVO()).getUserId());
            unitInventoryVo.setModifyId((SessionUtil.getBosUserVO()).getUserId());
          }
          else {
            unitInventoryVo.setCreateId("0");
            unitInventoryVo.setModifyId("0");
          }

          // ------------------------------------------------------------------
          // # 수정
          // ------------------------------------------------------------------
          resultInt = displayManageMapper.putInventoryGroupSort(unitInventoryVo);

          if (resultInt <= 0) {
            // # 한건이라도 실패할 경우 모두 롤백
            log.debug("# 수정건 없음");
            resultResDto.setResultCode(UserEnums.DisplayManage.DISPLAY_MANAGE_GRP_FAIL_PUT_SORT_NO_PROC.getCode());
            resultResDto.setResultMessage(UserEnums.DisplayManage.DISPLAY_MANAGE_GRP_FAIL_PUT_SORT_NO_PROC.getMessage());
            throw new BaseException(UserEnums.DisplayManage.DISPLAY_MANAGE_GRP_FAIL_PUT_SORT_NO_PROC);
          }
          resultTotalInt++;
        } // End of for (InventoryVo unitInventoryVo : inventoryVoList)

        // --------------------------------------------------------------------
        // # 처리건 Set
        // --------------------------------------------------------------------
        resultResDto.setTotal(resultTotalInt);

      }
      catch (BaseException be) {
        log.info("# putInventorySort BaseException e :: " + be.toString());
        throw be;
      }
      catch (Exception e) {
        log.info("# putInventorySort Exception e :: " + e.toString());
        throw new BaseException(UserEnums.DisplayManage.DISPLAY_MANAGE_GRP_FAIL_PUT_SORT);
      }

      // ======================================================================
      // # 반환
      // ======================================================================
      return resultResDto;
    }



    // ########################################################################
    // private
    // ########################################################################


}
