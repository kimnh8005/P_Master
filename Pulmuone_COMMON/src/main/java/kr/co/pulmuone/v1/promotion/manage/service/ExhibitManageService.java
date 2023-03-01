package kr.co.pulmuone.v1.promotion.manage.service;

import java.util.ArrayList;
import java.util.List;

import kr.co.pulmuone.v1.comm.aop.service.UserMaskingRun;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;

import kr.co.pulmuone.v1.approval.auth.dto.ApprovalExhibitRequestDto;
import kr.co.pulmuone.v1.approval.auth.dto.vo.ApprovalStatusVo;
import kr.co.pulmuone.v1.base.dto.vo.GoodsSearchVo;
import kr.co.pulmuone.v1.comm.enums.ApprovalEnums;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.ExhibitEnums;
import kr.co.pulmuone.v1.comm.enums.ExhibitEnums.ExhibitImgTp;
import kr.co.pulmuone.v1.comm.enums.ExhibitEnums.ExhibitMessage;
import kr.co.pulmuone.v1.comm.enums.ExhibitEnums.ExhibitTp;
import kr.co.pulmuone.v1.comm.enums.ExhibitEnums.GiftTargetTp;
import kr.co.pulmuone.v1.comm.enums.MessageCommEnum;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.mapper.promotion.manage.ExhibitManageMapper;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.promotion.manage.dto.ExhibitApprovalResponseDto;
import kr.co.pulmuone.v1.promotion.manage.dto.ExhibitManageRequestDto;
import kr.co.pulmuone.v1.promotion.manage.dto.ExhibitManageResponseDto;
import kr.co.pulmuone.v1.promotion.manage.dto.vo.EvUserGroupVo;
import kr.co.pulmuone.v1.promotion.manage.dto.vo.ExhibitApprovalResultVo;
import kr.co.pulmuone.v1.promotion.manage.dto.vo.ExhibitGiftGoodsVo;
import kr.co.pulmuone.v1.promotion.manage.dto.vo.ExhibitGiftVo;
import kr.co.pulmuone.v1.promotion.manage.dto.vo.ExhibitGroupDetlVo;
import kr.co.pulmuone.v1.promotion.manage.dto.vo.ExhibitGroupVo;
import kr.co.pulmuone.v1.promotion.manage.dto.vo.ExhibitSelectGoodsVo;
import kr.co.pulmuone.v1.promotion.manage.dto.vo.ExhibitSelectVo;
import kr.co.pulmuone.v1.promotion.manage.dto.vo.ExhibitVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
* <PRE>
* Forbiz Korea
* 프로모션 기획전관리 COMMON Service
*
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0       2020.12.01.              dgyoun         최초작성
* =======================================================================
* </PRE>
*/

@Slf4j
@Service
@RequiredArgsConstructor
public class ExhibitManageService {

    final String YES = "Y";

    private final ExhibitManageMapper exhibitManageMapper;

    // ########################################################################
    // protected
    // ########################################################################

    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // @ 조회
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    /**
     * 기획전 리스트조회
     * @param dpPageId
     * @param useAllYn
     * @return
     * @throws Exception
     */
    protected Page<ExhibitVo> selectExhibitList (ExhibitManageRequestDto exhibitManageRequestDto) throws BaseException {
      log.debug("# ######################################");
      log.debug("# ExhibitManageService.selectExhibitList Start");
      log.debug("# ######################################");
      //log.debug("# In.dpPageId :: " + dpPageId);
      //log.debug("# In.useAllYn :: " + useAllYn);

      // ======================================================================
      // # 초기화
      // ======================================================================
      //DisplayPageResponseDto resultResDto = new DisplayPageResponseDto();
      //resultResDto.setResultCode(ExhibitMessage.EXHIBIT_MANAGE_SUCCESS.getCode());
      //resultResDto.setResultMessage(ExhibitMessage.EXHIBIT_MANAGE_SUCCESS.getMessage());
      Page<ExhibitVo> resultList = null;

      // ======================================================================
      // # 처리
      // ======================================================================

      // --------------------------------------------------------------------
      // 1.1. 세션정보 Set
      // --------------------------------------------------------------------
      if (SessionUtil.getBosUserVO() != null) {
        exhibitManageRequestDto.setCreateId((SessionUtil.getBosUserVO()).getUserId());
        exhibitManageRequestDto.setModifyId((SessionUtil.getBosUserVO()).getUserId());
      }
      else {
        exhibitManageRequestDto.setCreateId("0");
        exhibitManageRequestDto.setModifyId("0");
      }

      // ----------------------------------------------------------------------
      // # 조회
      // ----------------------------------------------------------------------
      PageMethod.startPage(exhibitManageRequestDto.getPage(), exhibitManageRequestDto.getPageSize());
      resultList = exhibitManageMapper.selectExhibitList(exhibitManageRequestDto);

      // ======================================================================
      // # 반환
      // ======================================================================
      return resultList;
    }

    /**
     * 기획전 담당자리스트(조회조건 콤보용)
     * @param exhibitManageRequestDto
     * @return
     * @throws BaseException
     */
    protected ExhibitManageResponseDto selectExhibitManagerList (ExhibitManageRequestDto exhibitManageRequestDto) throws BaseException {
      log.debug("# ######################################");
      log.debug("# ExhibitManageService.selectExhibitManagerList Start");
      log.debug("# ######################################");
      log.debug("# In.evExhibitId :: " + exhibitManageRequestDto.getEvExhibitId());

      // ======================================================================
      // # 초기화
      // ======================================================================
      ExhibitManageResponseDto resultResDto = new ExhibitManageResponseDto();
      resultResDto.setResultCode(ExhibitMessage.EXHIBIT_MANAGE_SUCCESS.getCode());
      resultResDto.setResultMessage(ExhibitMessage.EXHIBIT_MANAGE_SUCCESS.getMessage());
      List<ExhibitVo>        resultManagerList             = null;

      // ======================================================================
      // # 처리
      // ======================================================================

      // ----------------------------------------------------------------------
      // 그룹 리스트조회
      // ----------------------------------------------------------------------
      resultManagerList = exhibitManageMapper.selectExhibitManagerList(exhibitManageRequestDto);
      resultResDto.setRows(resultManagerList);

      if (resultManagerList != null && resultManagerList.size() > 0) {
        resultResDto.setTotal(resultManagerList.size());
      }
      else {
        resultResDto.setTotal(0);
      }

      // ======================================================================
      // # 반환
      // ======================================================================
      return resultResDto;
    }

    /**
     * 기획전 상세조회 - 기본
     * @param exhibitManageRequestDto
     * @return
     * @throws BaseException
     */
    @UserMaskingRun(system = "BOS")
    protected ExhibitManageResponseDto selectExhibitInfo (ExhibitManageRequestDto exhibitManageRequestDto) throws BaseException {
      log.debug("# ######################################");
      log.debug("# ExhibitManageService.selectExhibitInfo Start");
      log.debug("# ######################################");
      log.debug("# In.evExhibitId :: " + exhibitManageRequestDto.getEvExhibitId());

      // ======================================================================
      // # 초기화
      // ======================================================================
      ExhibitManageResponseDto resultResDto = new ExhibitManageResponseDto();
      resultResDto.setResultCode(ExhibitMessage.EXHIBIT_MANAGE_SUCCESS.getCode());
      resultResDto.setResultMessage(ExhibitMessage.EXHIBIT_MANAGE_SUCCESS.getMessage());
      // 기획전-기본정보
      ExhibitVo resultDetailInfo = null;

      // ======================================================================
      // # 처리
      // ======================================================================

      // ----------------------------------------------------------------------
      // 1.기본정보조회
      // ----------------------------------------------------------------------
      resultDetailInfo = exhibitManageMapper.selectExhibitInfo(exhibitManageRequestDto.getEvExhibitId());

      resultResDto.setDetail(resultDetailInfo);
      if (resultDetailInfo == null || StringUtil.isEmpty(resultDetailInfo.getEvExhibitId()) || StringUtil.isEquals(resultDetailInfo.getEvExhibitId(), "0")) {
        log.debug("# 기획전 기본정보가 없습니다.");
        resultResDto.setResultCode(ExhibitMessage.EXHIBIT_MANAGE_DETAIL_NO_DATA.getCode());
        resultResDto.setResultMessage(ExhibitMessage.EXHIBIT_MANAGE_DETAIL_NO_DATA.getMessage());
        return resultResDto;
      }
      resultResDto.setTotal(1);

      List<EvUserGroupVo> userGroupList = exhibitManageMapper.getExhibitUserGroup(exhibitManageRequestDto.getEvExhibitId());
      resultResDto.getDetail().setUserGroupList(userGroupList);

      // 승인 정보 조회 - 1차담당자
      if(!StringUtil.isEmpty(resultDetailInfo.getApprovalSubUserId())){
        resultDetailInfo.setSubUser(exhibitManageMapper.getApprovalUserInfo(resultDetailInfo.getApprovalSubUserId()));
      }
      // 승인 정보 조회 - 2차담당자
      if(!StringUtil.isEmpty(resultDetailInfo.getApprovalUserId())){
        resultDetailInfo.setUser(exhibitManageMapper.getApprovalUserInfo(resultDetailInfo.getApprovalUserId()));
      }

      //// ----------------------------------------------------------------------
      //// 2.기획전유형별 상세조회
      //// ----------------------------------------------------------------------
      //if (StringUtil.isEquals(resultDetailInfo.getExhibitTp(), ExhibitTp.NORMAL.getCode())) {
      //  // 일반기획전조회
      //  this.selectExhibitInfoNormal(resultResDto);
      //
      //}
      //else if (StringUtil.isEquals(resultDetailInfo.getExhibitTp(), ExhibitTp.SELECT.getCode())) {
      //  // 골라담기(균일가)
      //  this.selectExhibitInfoSelect(resultResDto);
      //
      //}
      //else if (StringUtil.isEquals(resultDetailInfo.getExhibitTp(), ExhibitTp.GIFT.getCode())) {
      //  // 증정행사
      //  this.selectExhibitInfoGift(resultResDto);
      //}

      // ======================================================================
      // # 반환
      // ======================================================================
      return resultResDto;
    }

    /**
     * 기획전 상세조회 - 일반(그룹) - 그룹리스트
     * @param exhibitManageRequestDto
     * @return
     * @throws BaseException
     */
    protected ExhibitManageResponseDto selectfExhibitGroupList (ExhibitManageRequestDto exhibitManageRequestDto) throws BaseException {
      log.debug("# ######################################");
      log.debug("# ExhibitManageService.selectfExhibitGroupList Start");
      log.debug("# ######################################");
      log.debug("# In.evExhibitId :: " + exhibitManageRequestDto.getEvExhibitId());

      // ======================================================================
      // # 초기화
      // ======================================================================
      ExhibitManageResponseDto resultResDto = new ExhibitManageResponseDto();
      resultResDto.setResultCode(ExhibitMessage.EXHIBIT_MANAGE_SUCCESS.getCode());
      resultResDto.setResultMessage(ExhibitMessage.EXHIBIT_MANAGE_SUCCESS.getMessage());
      List<ExhibitGroupVo>        resultGroupList             = null;

      // ======================================================================
      // # 처리
      // ======================================================================

      // ----------------------------------------------------------------------
      // 그룹 리스트조회
      // ----------------------------------------------------------------------
      resultGroupList = exhibitManageMapper.selectfExhibitGroupList(exhibitManageRequestDto.getEvExhibitId());
      resultResDto.setRows(resultGroupList);

      if (resultGroupList != null && resultGroupList.size() > 0) {
        resultResDto.setTotal(resultGroupList.size());
      }
      else {
        resultResDto.setTotal(0);
      }

      // ======================================================================
      // # 반환
      // ======================================================================
      return resultResDto;
    }

    /**
     * 기획전 상세조회 - 일반(그룹) - 그룹상품리스트
     * @param exhibitManageRequestDto
     * @return
     * @throws BaseException
     */
    protected ExhibitManageResponseDto selectfExhibitGroupGoodsList (ExhibitManageRequestDto exhibitManageRequestDto) throws BaseException {
      log.debug("# ######################################");
      log.debug("# ExhibitManageService.selectfExhibitGroupGoodsList Start");
      log.debug("# ######################################");
      log.debug("# In.evExhibitId :: " + exhibitManageRequestDto.getEvExhibitId());

      // ======================================================================
      // # 초기화
      // ======================================================================
      ExhibitManageResponseDto resultResDto = new ExhibitManageResponseDto();
      resultResDto.setResultCode(ExhibitMessage.EXHIBIT_MANAGE_SUCCESS.getCode());
      resultResDto.setResultMessage(ExhibitMessage.EXHIBIT_MANAGE_SUCCESS.getMessage());
      List<ExhibitGroupDetlVo>    resultGroupGoodsList        = null;

      // ======================================================================
      // # 처리
      // ======================================================================

      // ----------------------------------------------------------------------
      // 그룹상품 리스트조회
      // ----------------------------------------------------------------------
      resultGroupGoodsList = exhibitManageMapper.selectfExhibitGroupGoodsList(exhibitManageRequestDto.getEvExhibitGroupId());
      resultResDto.setRows(resultGroupGoodsList);

      if (resultGroupGoodsList != null && resultGroupGoodsList.size() > 0) {
        resultResDto.setTotal(resultGroupGoodsList.size());
      }
      else {
        resultResDto.setTotal(0);
      }

      // ======================================================================
      // # 반환
      // ======================================================================
      return resultResDto;
    }

    /**
     * 기획전 상세조회 - 골라담기 - 기본정보
     * @param exhibitManageRequestDto
     * @return
     * @throws BaseException
     */
    protected ExhibitManageResponseDto selectfExhibitSelectInfo (ExhibitManageRequestDto exhibitManageRequestDto) throws BaseException {
      log.debug("# ######################################");
      log.debug("# ExhibitManageService.selectfExhibitSelectInfo Start");
      log.debug("# ######################################");
      log.debug("# In.evExhibitId :: " + exhibitManageRequestDto.getEvExhibitId());

      // ======================================================================
      // # 초기화
      // ======================================================================
      ExhibitManageResponseDto resultResDto = new ExhibitManageResponseDto();
      resultResDto.setResultCode(ExhibitMessage.EXHIBIT_MANAGE_SUCCESS.getCode());
      resultResDto.setResultMessage(ExhibitMessage.EXHIBIT_MANAGE_SUCCESS.getMessage());
      ExhibitSelectVo             resultSelectInfo            = null;

      // ======================================================================
      // # 처리
      // ======================================================================

      // ----------------------------------------------------------------------
      // 골라담기 - 기본정보
      // ----------------------------------------------------------------------
      resultSelectInfo = exhibitManageMapper.selectfExhibitSelectInfo(exhibitManageRequestDto.getEvExhibitId());
      resultResDto.setSelectDetail(resultSelectInfo);

      // ----------------------------------------------------------------------
      // 화면에서 그리드 사용을 위해 List도 함께 반환
      // ----------------------------------------------------------------------
      List<ExhibitSelectVo> resultSelectList = new ArrayList<ExhibitSelectVo>();
      resultSelectList.add(resultSelectInfo);

      resultResDto.setRows(resultSelectList);
      resultResDto.setTotal(1);

      // ======================================================================
      // # 반환
      // ======================================================================
      return resultResDto;
    }

    /**
     * 기획전 상세조회 - 골라담기 - 상품리스트
     * @param exhibitManageRequestDto
     * @return
     * @throws BaseException
     */
    protected ExhibitManageResponseDto selectExhibitSelectGoodsList (ExhibitManageRequestDto exhibitManageRequestDto) throws BaseException {
      log.debug("# ######################################");
      log.debug("# ExhibitManageService.selectExhibitSelectGoodsList Start");
      log.debug("# ######################################");
      log.debug("# In.evExhibitId :: " + exhibitManageRequestDto.getEvExhibitId());

      // ======================================================================
      // # 초기화
      // ======================================================================
      ExhibitManageResponseDto resultResDto = new ExhibitManageResponseDto();
      resultResDto.setResultCode(ExhibitMessage.EXHIBIT_MANAGE_SUCCESS.getCode());
      resultResDto.setResultMessage(ExhibitMessage.EXHIBIT_MANAGE_SUCCESS.getMessage());
      List<ExhibitSelectGoodsVo>  resultSelectGoodsList       = null;

      // ======================================================================
      // # 처리
      // ======================================================================

      // ----------------------------------------------------------------------
      // 골라담기 - 상품리스트
      // ----------------------------------------------------------------------
      resultSelectGoodsList = exhibitManageMapper.selectExhibitSelectGoodsList(exhibitManageRequestDto.getEvExhibitId());
      resultResDto.setRows(resultSelectGoodsList);

      if (resultSelectGoodsList != null && resultSelectGoodsList.size() > 0) {
        resultResDto.setTotal(resultSelectGoodsList.size());
      }
      else {
        resultResDto.setTotal(0);
      }

      // ======================================================================
      // # 반환
      // ======================================================================
      return resultResDto;
    }

    /**
     * 기획전 상세조회 - 골라담기 - 추가상품리스트
     * @param exhibitManageRequestDto
     * @return
     * @throws BaseException
     */
    protected ExhibitManageResponseDto selectExhibitSelectAddGoodsList (ExhibitManageRequestDto exhibitManageRequestDto) throws BaseException {
      log.debug("# ######################################");
      log.debug("# ExhibitManageService.selectExhibitSelectAddGoodsList Start");
      log.debug("# ######################################");
      log.debug("# In.evExhibitId :: " + exhibitManageRequestDto.getEvExhibitId());

      // ======================================================================
      // # 초기화
      // ======================================================================
      ExhibitManageResponseDto resultResDto = new ExhibitManageResponseDto();
      resultResDto.setResultCode(ExhibitMessage.EXHIBIT_MANAGE_SUCCESS.getCode());
      resultResDto.setResultMessage(ExhibitMessage.EXHIBIT_MANAGE_SUCCESS.getMessage());
      List<ExhibitSelectGoodsVo>  resultSelectAddGoodsList    = null;

      // ======================================================================
      // # 처리
      // ======================================================================

      // ----------------------------------------------------------------------
      // 골라담기 - 추가상품리스트
      // ----------------------------------------------------------------------
      resultSelectAddGoodsList = exhibitManageMapper.selectExhibitSelectAddGoodsList(exhibitManageRequestDto.getEvExhibitId());
      resultResDto.setRows(resultSelectAddGoodsList);

      if (resultSelectAddGoodsList != null && resultSelectAddGoodsList.size() > 0) {
        resultResDto.setTotal(resultSelectAddGoodsList.size());
      }
      else {
        resultResDto.setTotal(0);
      }

      // ======================================================================
      // # 반환
      // ======================================================================
      return resultResDto;
    }

    /**
     * 기획전 상세조회 - 증정행사 - 기본정보
     * @param exhibitManageRequestDto
     * @return
     * @throws BaseException
     */
    protected ExhibitManageResponseDto selectExhibitGiftInfo (ExhibitManageRequestDto exhibitManageRequestDto) throws BaseException {
      log.debug("# ######################################");
      log.debug("# ExhibitManageService.selectExhibitGiftInfo Start");
      log.debug("# ######################################");
      log.debug("# In.evExhibitId :: " + exhibitManageRequestDto.getEvExhibitId());

      // ======================================================================
      // # 초기화
      // ======================================================================
      ExhibitManageResponseDto resultResDto = new ExhibitManageResponseDto();
      resultResDto.setResultCode(ExhibitMessage.EXHIBIT_MANAGE_SUCCESS.getCode());
      resultResDto.setResultMessage(ExhibitMessage.EXHIBIT_MANAGE_SUCCESS.getMessage());
      ExhibitGiftVo               resultGiftInfo              = null;

      // ======================================================================
      // # 처리
      // ======================================================================

      // ----------------------------------------------------------------------
      // 증정행사 - 기본정보
      // ----------------------------------------------------------------------
      resultGiftInfo = exhibitManageMapper.selectExhibitGiftInfo(exhibitManageRequestDto.getEvExhibitId());
      resultResDto.setGiftDetail(resultGiftInfo);

      // ======================================================================
      // # 반환
      // ======================================================================
      return resultResDto;
    }

    /**
     * 기획전 상세조회 - 증정행사 - 증정상품리스트
     * @param exhibitManageRequestDto
     * @return
     * @throws BaseException
     */
    protected ExhibitManageResponseDto selectExhibitGiftGoodsList (ExhibitManageRequestDto exhibitManageRequestDto) throws BaseException {
      log.debug("# ######################################");
      log.debug("# ExhibitManageService.selectExhibitGiftGoodsList Start");
      log.debug("# ######################################");
      log.debug("# In.evExhibitId :: " + exhibitManageRequestDto.getEvExhibitId());

      // ======================================================================
      // # 초기화
      // ======================================================================
      ExhibitManageResponseDto resultResDto = new ExhibitManageResponseDto();
      resultResDto.setResultCode(ExhibitMessage.EXHIBIT_MANAGE_SUCCESS.getCode());
      resultResDto.setResultMessage(ExhibitMessage.EXHIBIT_MANAGE_SUCCESS.getMessage());
      List<ExhibitGiftGoodsVo>    resultGiftGoodsList         = null;

      // ======================================================================
      // # 처리
      // ======================================================================

      // ----------------------------------------------------------------------
      // 증정행사 - 증정상품리스트
      // ----------------------------------------------------------------------
      resultGiftGoodsList = exhibitManageMapper.selectExhibitGiftGoodsList(exhibitManageRequestDto.getEvExhibitId());
      resultResDto.setRows(resultGiftGoodsList);

      if (resultGiftGoodsList != null && resultGiftGoodsList.size() > 0) {
        resultResDto.setTotal(resultGiftGoodsList.size());
      }
      else {
        resultResDto.setTotal(0);
      }

      // ======================================================================
      // # 반환
      // ======================================================================
      return resultResDto;
    }

    /**
     * 기획전 상세조회 - 증정행사 - 적용상품리스트
     * @param exhibitManageRequestDto
     * @return
     * @throws BaseException
     */
    protected ExhibitManageResponseDto selectExhibitGiftTargetGoodsList (ExhibitManageRequestDto exhibitManageRequestDto) throws BaseException {
      log.debug("# ######################################");
      log.debug("# ExhibitManageService.selectExhibitGiftTargetGoodsList Start");
      log.debug("# ######################################");
      log.debug("# In.evExhibitId :: " + exhibitManageRequestDto.getEvExhibitId());

      // ======================================================================
      // # 초기화
      // ======================================================================
      ExhibitManageResponseDto resultResDto = new ExhibitManageResponseDto();
      resultResDto.setResultCode(ExhibitMessage.EXHIBIT_MANAGE_SUCCESS.getCode());
      resultResDto.setResultMessage(ExhibitMessage.EXHIBIT_MANAGE_SUCCESS.getMessage());
      List<ExhibitGiftGoodsVo>    resultGgiftTargetGoodsList  = null;

      // ======================================================================
      // # 처리
      // ======================================================================

      // ----------------------------------------------------------------------
      // 증정행사 - 적용상품리스트
      // ----------------------------------------------------------------------
      resultGgiftTargetGoodsList = exhibitManageMapper.selectExhibitGiftTargetGoodsList(exhibitManageRequestDto.getEvExhibitId());
      resultResDto.setRows(resultGgiftTargetGoodsList);

      if (resultGgiftTargetGoodsList != null && resultGgiftTargetGoodsList.size() > 0) {
        resultResDto.setTotal(resultGgiftTargetGoodsList.size());
      }
      else {
        resultResDto.setTotal(0);
      }

      // ======================================================================
      // # 반환
      // ======================================================================
      return resultResDto;
    }

    /**
     * 기획전 상세조회 - 증정행사 - 적용브랜드리스트
     * @param exhibitManageRequestDto
     * @return
     * @throws BaseException
     */
    protected ExhibitManageResponseDto selectExhibitGiftTargetBrandList (ExhibitManageRequestDto exhibitManageRequestDto) throws BaseException {
      log.debug("# ######################################");
      log.debug("# ExhibitManageService.selectExhibitGiftTargetBrandList Start");
      log.debug("# ######################################");
      log.debug("# In.evExhibitId :: " + exhibitManageRequestDto.getEvExhibitId());

      // ======================================================================
      // # 초기화
      // ======================================================================
      ExhibitManageResponseDto resultResDto = new ExhibitManageResponseDto();
      resultResDto.setResultCode(ExhibitMessage.EXHIBIT_MANAGE_SUCCESS.getCode());
      resultResDto.setResultMessage(ExhibitMessage.EXHIBIT_MANAGE_SUCCESS.getMessage());
      List<ExhibitGiftGoodsVo>    resultGiftTargetBrandList   = null;

      // ======================================================================
      // # 처리
      // ======================================================================

      // ----------------------------------------------------------------------
      // 증정행사 - 적용브랜드리스트
      // ----------------------------------------------------------------------
      resultGiftTargetBrandList =  exhibitManageMapper.selectExhibitGiftTargetBrandList(exhibitManageRequestDto.getEvExhibitId());
      resultResDto.setRows(resultGiftTargetBrandList);

      if (resultGiftTargetBrandList != null && resultGiftTargetBrandList.size() > 0) {
        resultResDto.setTotal(resultGiftTargetBrandList.size());
      }
      else {
        resultResDto.setTotal(0);
      }

      // ======================================================================
      // # 반환
      // ======================================================================
      return resultResDto;
    }

    /**
     * 상품정보리스트(엑셀용)
     * @param exhibitManageRequestDto
     * @return
     * @throws BaseException
     */
    protected ExhibitManageResponseDto selectGoodsInfoList (ExhibitManageRequestDto exhibitManageRequestDto) throws BaseException {
      log.debug("# ######################################");
      log.debug("# ExhibitManageService.selectGoodsInfoList Start");
      log.debug("# ######################################");
      log.debug("# In.ilGoodsList :: " + exhibitManageRequestDto.getIlGoodsIdList().toString());

      // ======================================================================
      // # 초기화
      // ======================================================================
      ExhibitManageResponseDto resultResDto = new ExhibitManageResponseDto();
      resultResDto.setResultCode(ExhibitMessage.EXHIBIT_MANAGE_SUCCESS.getCode());
      resultResDto.setResultMessage(ExhibitMessage.EXHIBIT_MANAGE_SUCCESS.getMessage());
      List<GoodsSearchVo> resultGoodsList        = null;

      // ======================================================================
      // # 처리
      // ======================================================================

      // ----------------------------------------------------------------------
      // 상품 리스트조회
      // ----------------------------------------------------------------------
      resultGoodsList = exhibitManageMapper.selectGoodsInfoList(exhibitManageRequestDto);
      resultResDto.setRows(resultGoodsList);

      if (resultGoodsList != null && resultGoodsList.size() > 0) {
        resultResDto.setTotal(resultGoodsList.size());
      }
      else {
        resultResDto.setTotal(0);
      }

      // ======================================================================
      // # 반환
      // ======================================================================
      return resultResDto;
    }

    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // @ 삭제
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    /**
     * 기획전 삭제
     * @param exhibitList
     * @return
     * @throws BaseException
     */
    @SuppressWarnings("unused")
    protected ExhibitManageResponseDto delExhibit (List<String> exhibitList) throws BaseException {
      log.debug("# ######################################");
      log.debug("# ExhibitManageService.delExhibit Start");
      log.debug("# ######################################");
      if (exhibitList != null) {
        log.debug("# In.exhibitList.size :: " + exhibitList.size());
        log.debug("# In.exhibitList      :: " + exhibitList.toString());
      }
      else {
        log.debug("# In.exhibitList is Null or size 0");
      }

      // ======================================================================
      // # 초기화
      // ======================================================================
      ExhibitManageResponseDto resultResDto = new ExhibitManageResponseDto();
      resultResDto.setResultCode(ExhibitMessage.EXHIBIT_MANAGE_SUCCESS.getCode());
      resultResDto.setResultMessage(ExhibitMessage.EXHIBIT_MANAGE_SUCCESS.getMessage());
      int resultInt = 0;
      int resultTotalInt = 0;

      ExhibitVo unitExhibitVo = null;

      // ======================================================================
      // # 처리
      // ======================================================================

      try {

        if (exhibitList == null || exhibitList.size() <= 0) {
          // 삭제대상기획전 없음
          resultResDto.setResultCode(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_DEL_FAIL_INPUT_TARGET.getCode());
          resultResDto.setResultMessage(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_DEL_FAIL_INPUT_TARGET.getMessage());
          resultResDto.setTotal(0);
          return resultResDto;
        }

        //for (ExhibitVo unitExhibitVo : exhibitList) {
        for (String evExhibitId : exhibitList) {

          unitExhibitVo = new ExhibitVo();

          // ------------------------------------------------------------------
          // # 세션정보 Set
          // ------------------------------------------------------------------
          if (SessionUtil.getBosUserVO() != null) {
            unitExhibitVo.setCreateId((SessionUtil.getBosUserVO()).getUserId());
            unitExhibitVo.setModifyId((SessionUtil.getBosUserVO()).getUserId());
          }
          else {
            unitExhibitVo.setCreateId("0");
            unitExhibitVo.setModifyId("0");
          }

          // ------------------------------------------------------------------
          // 삭제
          // ------------------------------------------------------------------
          unitExhibitVo.setEvExhibitId(evExhibitId);
          unitExhibitVo.setDelYn("Y");
          resultInt = exhibitManageMapper.delExhibit(unitExhibitVo);

          if (resultInt <= 0) {
            // # 한건이라도 실패할 경우 모두 롤백
            log.debug("# 삭제건 없음");
            resultResDto.setResultCode(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_DEL_FAIL_PROC.getCode());
            resultResDto.setResultMessage(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_DEL_FAIL_PROC.getMessage());
            throw new BaseException(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_DEL_FAIL_PROC);
          }
          resultTotalInt++;
          // 현재 단건임, 다건인 경우 List로 반환 필요
          resultResDto.setDetail(unitExhibitVo);

        } // End of for (ExhibitVo unitExhibitVo : exhibitList)

      }
      catch (BaseException be) {
        log.info("# delExhibit BaseException e :: " + be.toString());
        throw be;
      }
      catch (Exception e) {
        log.info("# delExhibit Exception e :: " + e.toString());
        throw new BaseException(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_DEL_FAIL);
      }

      //  ======================================================================
      // # 반환
      // ======================================================================
      return resultResDto;
    }

    /**
     * 그룹상세 삭제(그룹상품)
     * @param exhibitGroupDetlList
     * @return
     * @throws BaseException
     */
    @SuppressWarnings("unused")
    protected ExhibitManageResponseDto delExhibitGroupDetl (List<String> exhibitGroupDetlList) throws BaseException {
      log.debug("# ######################################");
      log.debug("# ExhibitManageService.delExhibitGroupDetl Start");
      log.debug("# ######################################");
      if (exhibitGroupDetlList != null) {
        log.debug("# In.exhibitGroupDetlList.size :: " + exhibitGroupDetlList.size());
        log.debug("# In.exhibitGroupDetlList      :: " + exhibitGroupDetlList.toString());
      }
      else {
        log.debug("# In.exhibitGroupDetlList is Null or size 0");
      }

      // ======================================================================
      // # 초기화
      // ======================================================================
      ExhibitManageResponseDto resultResDto = new ExhibitManageResponseDto();
      resultResDto.setResultCode(ExhibitMessage.EXHIBIT_MANAGE_SUCCESS.getCode());
      resultResDto.setResultMessage(ExhibitMessage.EXHIBIT_MANAGE_SUCCESS.getMessage());
      int resultInt = 0;
      int resultTotalInt = 0;

      // ======================================================================
      // # 처리
      // ======================================================================

      try {

        if (exhibitGroupDetlList == null || exhibitGroupDetlList.size() <= 0) {
          // 삭제대상그룹상세 없음
          resultResDto.setResultCode(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GROUP_DETL_DEL_FAIL_INPUT_TARGET.getCode());
          resultResDto.setResultMessage(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GROUP_DETL_DEL_FAIL_INPUT_TARGET.getMessage());
          resultResDto.setTotal(0);
          return resultResDto;
        }

        for (String evExhibitGroupDetlId : exhibitGroupDetlList) {

          // ------------------------------------------------------------------
          // 삭제 - 개별삭제
          // ------------------------------------------------------------------
          resultInt = exhibitManageMapper.delExhibitGroupDetl(evExhibitGroupDetlId);

          if (resultInt <= 0) {
            // # 한건이라도 실패할 경우 모두 롤백
            log.debug("# 삭제건 없음");
            resultResDto.setResultCode(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GROUP_DETL_DEL_FAIL_PROC.getCode());
            resultResDto.setResultMessage(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GROUP_DETL_DEL_FAIL_PROC.getMessage());
            throw new BaseException(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GROUP_DETL_DEL_FAIL_PROC);
          }
          resultTotalInt++;
          // 현재 단건임, 다건인 경우 List로 반환 필요
          //resultResDto.setDetail(unitExhibitVo);

        } // End of for (ExhibitVo unitExhibitVo : exhibitList)

      }
      catch (BaseException be) {
        log.info("# delExhibitGroupDetl BaseException e :: " + be.toString());
        throw be;
      }
      catch (Exception e) {
        log.info("# delExhibitGroupDetl Exception e :: " + e.toString());
        throw new BaseException(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GROUP_DETL_DEL_FAIL);
      }

      //  ======================================================================
      // # 반환
      // ======================================================================
      return resultResDto;
    }

    /**
     * 골라담기 상품 삭제
     * @param exhibitGroupDetlList
     * @return
     * @throws BaseException
     */
    @SuppressWarnings("unused")
    protected ExhibitManageResponseDto delExhibitSelectGoods (List<String> exhibitSelectGoodsList) throws BaseException {
      log.debug("# ######################################");
      log.debug("# ExhibitManageService.delExhibitSelectGoods Start");
      log.debug("# ######################################");
      if (exhibitSelectGoodsList != null) {
        log.debug("# In.exhibitSelectGoodsList.size :: " + exhibitSelectGoodsList.size());
        log.debug("# In.exhibitSelectGoodsList      :: " + exhibitSelectGoodsList.toString());
      }
      else {
        log.debug("# In.exhibitSelectGoodsList is Null or size 0");
      }

      // ======================================================================
      // # 초기화
      // ======================================================================
      ExhibitManageResponseDto resultResDto = new ExhibitManageResponseDto();
      resultResDto.setResultCode(ExhibitMessage.EXHIBIT_MANAGE_SUCCESS.getCode());
      resultResDto.setResultMessage(ExhibitMessage.EXHIBIT_MANAGE_SUCCESS.getMessage());
      int resultInt = 0;
      int resultTotalInt = 0;

      // ======================================================================
      // # 처리
      // ======================================================================

      try {

        if (exhibitSelectGoodsList == null || exhibitSelectGoodsList.size() <= 0) {
          // 삭제대상골라담기상품 없음
          resultResDto.setResultCode(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_SELECT_GOODS_DEL_FAIL_INPUT_TARGET.getCode());
          resultResDto.setResultMessage(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_SELECT_GOODS_DEL_FAIL_INPUT_TARGET.getMessage());
          resultResDto.setTotal(0);
          return resultResDto;
        }

        for (String exhibitSelectGoodsId : exhibitSelectGoodsList) {

          // ------------------------------------------------------------------
          // 삭제 - 개별삭제
          // ------------------------------------------------------------------
          resultInt = exhibitManageMapper.delExhibitSelectGoods(exhibitSelectGoodsId);

          if (resultInt <= 0) {
            // # 한건이라도 실패할 경우 모두 롤백
            log.debug("# 삭제건 없음");
            resultResDto.setResultCode(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_SELECT_GOODS_DEL_FAIL_PROC.getCode());
            resultResDto.setResultMessage(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_SELECT_GOODS_DEL_FAIL_PROC.getMessage());
            throw new BaseException(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_SELECT_GOODS_DEL_FAIL_PROC);
          }
          resultTotalInt++;
          // 현재 단건임, 다건인 경우 List로 반환 필요
          //resultResDto.setDetail(unitExhibitVo);

        } // End of for (ExhibitVo unitExhibitVo : exhibitList)

      }
      catch (BaseException be) {
        log.info("# delExhibitSelectGoods BaseException e :: " + be.toString());
        throw be;
      }
      catch (Exception e) {
        log.info("# delExhibitSelectGoods Exception e :: " + e.toString());
        throw new BaseException(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_SELECT_GOODS_DEL_FAIL);
      }

      //  ======================================================================
      // # 반환
      // ======================================================================
      return resultResDto;
    }

    /**
     * 골라담기 추가상품 삭제
     * @param exhibitGroupDetlList
     * @return
     * @throws BaseException
     */
    @SuppressWarnings("unused")
    protected ExhibitManageResponseDto delExhibitSelectAddGoods (List<String> exhibitSelectAddGoodsList) throws BaseException {
      log.debug("# ######################################");
      log.debug("# ExhibitManageService.delExhibitSelectAddGoods Start");
      log.debug("# ######################################");
      if (exhibitSelectAddGoodsList != null) {
        log.debug("# In.exhibitSelectAddGoodsList.size :: " + exhibitSelectAddGoodsList.size());
        log.debug("# In.exhibitSelectAddGoodsList      :: " + exhibitSelectAddGoodsList.toString());
      }
      else {
        log.debug("# In.exhibitSelectAddGoodsList is Null or size 0");
      }

      // ======================================================================
      // # 초기화
      // ======================================================================
      ExhibitManageResponseDto resultResDto = new ExhibitManageResponseDto();
      resultResDto.setResultCode(ExhibitMessage.EXHIBIT_MANAGE_SUCCESS.getCode());
      resultResDto.setResultMessage(ExhibitMessage.EXHIBIT_MANAGE_SUCCESS.getMessage());
      int resultInt = 0;
      int resultTotalInt = 0;

      // ======================================================================
      // # 처리
      // ======================================================================

      try {

        if (exhibitSelectAddGoodsList == null || exhibitSelectAddGoodsList.size() <= 0) {
          // 삭제대상골라담기추가상품 없음
          resultResDto.setResultCode(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_SELECT_ADD_GOODS_DEL_FAIL_INPUT_TARGET.getCode());
          resultResDto.setResultMessage(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_SELECT_ADD_GOODS_DEL_FAIL_INPUT_TARGET.getMessage());
          resultResDto.setTotal(0);
          return resultResDto;
        }

        for (String exhibitSelectAddGoodsId : exhibitSelectAddGoodsList) {

          // ------------------------------------------------------------------
          // 삭제 - 개별삭제
          // ------------------------------------------------------------------
          resultInt = exhibitManageMapper.delExhibitSelectAddGoods(exhibitSelectAddGoodsId);

          if (resultInt <= 0) {
            // # 한건이라도 실패할 경우 모두 롤백
            log.debug("# 삭제건 없음");
            resultResDto.setResultCode(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_SELECT_ADD_GOODS_DEL_FAIL_PROC.getCode());
            resultResDto.setResultMessage(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_SELECT_ADD_GOODS_DEL_FAIL_PROC.getMessage());
            throw new BaseException(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_SELECT_ADD_GOODS_DEL_FAIL_PROC);
          }
          resultTotalInt++;
          // 현재 단건임, 다건인 경우 List로 반환 필요
          //resultResDto.setDetail(unitExhibitVo);

        } // End of for (ExhibitVo unitExhibitVo : exhibitList)

      }
      catch (BaseException be) {
        log.info("# delExhibitSelectAddGoods BaseException e :: " + be.toString());
        throw be;
      }
      catch (Exception e) {
        log.info("# delExhibitSelectAddGoods Exception e :: " + e.toString());
        throw new BaseException(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_SELECT_ADD_GOODS_DEL_FAIL);
      }

      //  ======================================================================
      // # 반환
      // ======================================================================
      return resultResDto;
    }

    /**
     * 증정행사 상품 삭제
     * @param exhibitGroupDetlList
     * @return
     * @throws BaseException
     */
    @SuppressWarnings("unused")
    protected ExhibitManageResponseDto delExhibitGiftGoods (List<String> exhibitGiftGoodsList) throws BaseException {
      log.debug("# ######################################");
      log.debug("# ExhibitManageService.delExhibitGiftGoods Start");
      log.debug("# ######################################");
      if (exhibitGiftGoodsList != null) {
        log.debug("# In.exhibitGiftGoodsList.size :: " + exhibitGiftGoodsList.size());
        log.debug("# In.exhibitGiftGoodsList      :: " + exhibitGiftGoodsList.toString());
      }
      else {
        log.debug("# In.exhibitGiftGoodsList is Null or size 0");
      }

      // ======================================================================
      // # 초기화
      // ======================================================================
      ExhibitManageResponseDto resultResDto = new ExhibitManageResponseDto();
      resultResDto.setResultCode(ExhibitMessage.EXHIBIT_MANAGE_SUCCESS.getCode());
      resultResDto.setResultMessage(ExhibitMessage.EXHIBIT_MANAGE_SUCCESS.getMessage());
      int resultInt = 0;
      int resultTotalInt = 0;

      // ======================================================================
      // # 처리
      // ======================================================================

      try {

        if (exhibitGiftGoodsList == null || exhibitGiftGoodsList.size() <= 0) {
          // 삭제대상골라담기추가상품 없음
          resultResDto.setResultCode(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GIFT_GOODS_DEL_FAIL_INPUT_TARGET.getCode());
          resultResDto.setResultMessage(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GIFT_GOODS_DEL_FAIL_INPUT_TARGET.getMessage());
          resultResDto.setTotal(0);
          return resultResDto;
        }

        for (String exhibitGiftGoodsId : exhibitGiftGoodsList) {

          // ------------------------------------------------------------------
          // 삭제 - 개별삭제
          // ------------------------------------------------------------------
          resultInt = exhibitManageMapper.delExhibitGiftGoods(exhibitGiftGoodsId);

          if (resultInt <= 0) {
            // # 한건이라도 실패할 경우 모두 롤백
            log.debug("# 삭제건 없음");
            resultResDto.setResultCode(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GIFT_GOODS_DEL_FAIL_PROC.getCode());
            resultResDto.setResultMessage(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GIFT_GOODS_DEL_FAIL_PROC.getMessage());
            throw new BaseException(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GIFT_GOODS_DEL_FAIL_PROC);
          }
          resultTotalInt++;
          // 현재 단건임, 다건인 경우 List로 반환 필요
          //resultResDto.setDetail(unitExhibitVo);

        } // End of for (ExhibitVo unitExhibitVo : exhibitList)

      }
      catch (BaseException be) {
        log.info("# delExhibitGiftGoods BaseException e :: " + be.toString());
        throw be;
      }
      catch (Exception e) {
        log.info("# delExhibitGiftGoods Exception e :: " + e.toString());
        throw new BaseException(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GIFT_GOODS_DEL_FAIL);
      }

      //  ======================================================================
      // # 반환
      // ======================================================================
      return resultResDto;
    }

    /**
     * 증정행사 대상상품 삭제
     * @param exhibitGroupDetlList
     * @return
     * @throws BaseException
     */
    @SuppressWarnings("unused")
    protected ExhibitManageResponseDto delExhibitGiftTargetGoods (List<String> exhibitGiftTargetGoodsList) throws BaseException {
      log.debug("# ######################################");
      log.debug("# ExhibitManageService.delExhibitGiftTargetGoods Start");
      log.debug("# ######################################");
      if (exhibitGiftTargetGoodsList != null) {
        log.debug("# In.exhibitGiftTargetGoodsList.size :: " + exhibitGiftTargetGoodsList.size());
        log.debug("# In.exhibitGiftTargetGoodsList      :: " + exhibitGiftTargetGoodsList.toString());
      }
      else {
        log.debug("# In.exhibitGiftTargetGoodsList is Null or size 0");
      }

      // ======================================================================
      // # 초기화
      // ======================================================================
      ExhibitManageResponseDto resultResDto = new ExhibitManageResponseDto();
      resultResDto.setResultCode(ExhibitMessage.EXHIBIT_MANAGE_SUCCESS.getCode());
      resultResDto.setResultMessage(ExhibitMessage.EXHIBIT_MANAGE_SUCCESS.getMessage());
      int resultInt = 0;
      int resultTotalInt = 0;

      // ======================================================================
      // # 처리
      // ======================================================================

      try {

        if (exhibitGiftTargetGoodsList == null || exhibitGiftTargetGoodsList.size() <= 0) {
          // 삭제대상골라담기추가상품 없음
          resultResDto.setResultCode(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GIFT_TARGET_GOODS_DEL_FAIL_INPUT_TARGET.getCode());
          resultResDto.setResultMessage(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GIFT_TARGET_GOODS_DEL_FAIL_INPUT_TARGET.getMessage());
          resultResDto.setTotal(0);
          return resultResDto;
        }

        for (String exhibitGiftTargetGoodsId : exhibitGiftTargetGoodsList) {

          // ------------------------------------------------------------------
          // 삭제 - 개별삭제
          // ------------------------------------------------------------------
          resultInt = exhibitManageMapper.delExhibitGiftTargetGoods(exhibitGiftTargetGoodsId);

          if (resultInt <= 0) {
            // # 한건이라도 실패할 경우 모두 롤백
            log.debug("# 삭제건 없음");
            resultResDto.setResultCode(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GIFT_TARGET_GOODS_DEL_FAIL_PROC.getCode());
            resultResDto.setResultMessage(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GIFT_TARGET_GOODS_DEL_FAIL_PROC.getMessage());
            throw new BaseException(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GIFT_TARGET_GOODS_DEL_FAIL_PROC);
          }
          resultTotalInt++;
          // 현재 단건임, 다건인 경우 List로 반환 필요
          //resultResDto.setDetail(unitExhibitVo);

        } // End of for (ExhibitVo unitExhibitVo : exhibitList)

      }
      catch (BaseException be) {
        log.info("# delExhibitGiftTargetGoods BaseException e :: " + be.toString());
        throw be;
      }
      catch (Exception e) {
        log.info("# delExhibitGiftTargetGoods Exception e :: " + e.toString());
        throw new BaseException(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GIFT_TARGET_GOODS_DEL_FAIL);
      }

      //  ======================================================================
      // # 반환
      // ======================================================================
      return resultResDto;
    }

    /**
     * 증정행사 대상브랜드 삭제
     * @param exhibitGroupDetlList
     * @return
     * @throws BaseException
     */
    @SuppressWarnings("unused")
    protected ExhibitManageResponseDto delExhibitGiftTargetBrand (List<String> exhibitGiftTargetBrandList) throws BaseException {
      log.debug("# ######################################");
      log.debug("# ExhibitManageService.delExhibitGiftTargetBrand Start");
      log.debug("# ######################################");
      if (exhibitGiftTargetBrandList != null) {
        log.debug("# In.exhibitGiftTargetBrandList.size :: " + exhibitGiftTargetBrandList.size());
        log.debug("# In.exhibitGiftTargetBrandList      :: " + exhibitGiftTargetBrandList.toString());
      }
      else {
        log.debug("# In.exhibitGiftTargetBrandList is Null or size 0");
      }

      // ======================================================================
      // # 초기화
      // ======================================================================
      ExhibitManageResponseDto resultResDto = new ExhibitManageResponseDto();
      resultResDto.setResultCode(ExhibitMessage.EXHIBIT_MANAGE_SUCCESS.getCode());
      resultResDto.setResultMessage(ExhibitMessage.EXHIBIT_MANAGE_SUCCESS.getMessage());
      int resultInt = 0;
      int resultTotalInt = 0;

      // ======================================================================
      // # 처리
      // ======================================================================

      try {

        if (exhibitGiftTargetBrandList == null || exhibitGiftTargetBrandList.size() <= 0) {
          // 삭제대상골라담기추가상품 없음
          resultResDto.setResultCode(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GIFT_TARGET_BRAND_DEL_FAIL_INPUT_TARGET.getCode());
          resultResDto.setResultMessage(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GIFT_TARGET_BRAND_DEL_FAIL_INPUT_TARGET.getMessage());
          resultResDto.setTotal(0);
          return resultResDto;
        }

        for (String exhibitGiftTargetBrandId : exhibitGiftTargetBrandList) {

          // ------------------------------------------------------------------
          // 삭제 - 개별삭제
          // ------------------------------------------------------------------
          resultInt = exhibitManageMapper.delExhibitGiftTargetBrand(exhibitGiftTargetBrandId);

          if (resultInt <= 0) {
            // # 한건이라도 실패할 경우 모두 롤백
            log.debug("# 삭제건 없음");
            resultResDto.setResultCode(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GIFT_TARGET_BRAND_DEL_FAIL_PROC.getCode());
            resultResDto.setResultMessage(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GIFT_TARGET_BRAND_DEL_FAIL_PROC.getMessage());
            throw new BaseException(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GIFT_TARGET_BRAND_DEL_FAIL_PROC);
          }
          resultTotalInt++;
          // 현재 단건임, 다건인 경우 List로 반환 필요
          //resultResDto.setDetail(unitExhibitVo);

        } // End of for (ExhibitVo unitExhibitVo : exhibitList)

      }
      catch (BaseException be) {
        log.info("# delExhibitGiftTargetBrand BaseException e :: " + be.toString());
        throw be;
      }
      catch (Exception e) {
        log.info("# delExhibitGiftTargetBrand Exception e :: " + e.toString());
        throw new BaseException(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GIFT_TARGET_BRAND_DEL_FAIL);
      }

      //  ======================================================================
      // # 반환
      // ======================================================================
      return resultResDto;
    }



    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // @ 등록
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

    /**
     * 기획전 등록
     * @param exhibitManageRequestDto
     * @return
     * @throws BaseException
     */
    protected ExhibitManageResponseDto addExhibit (ExhibitManageRequestDto exhibitManageRequestDto) throws BaseException {
      log.debug("# ######################################");
      log.debug("# ExhibitManageService.addExhibit Start");
      log.debug("# ######################################");
      log.debug("# In.exhibitManageRequestDto      :: " + exhibitManageRequestDto.toString());

      // ======================================================================
      // # 초기화
      // ======================================================================
      ExhibitManageResponseDto resultResDto = new ExhibitManageResponseDto();
      resultResDto.setResultCode(ExhibitMessage.EXHIBIT_MANAGE_SUCCESS.getCode());
      resultResDto.setResultMessage(ExhibitMessage.EXHIBIT_MANAGE_SUCCESS.getMessage());
      int resultInt = 0;

      // ======================================================================
      // # 처리
      // ======================================================================

      try {

        // --------------------------------------------------------------------
        // 1. 기획전 기본정보 등록
        // --------------------------------------------------------------------
        // --------------------------------------------------------------------
        // 1.1. 세션정보 Set
        // --------------------------------------------------------------------
        if (SessionUtil.getBosUserVO() != null) {
          exhibitManageRequestDto.getExhibitInfo().setCreateId((SessionUtil.getBosUserVO()).getUserId());
          exhibitManageRequestDto.getExhibitInfo().setModifyId((SessionUtil.getBosUserVO()).getUserId());
        }
        else {
          exhibitManageRequestDto.getExhibitInfo().setCreateId("0");
          exhibitManageRequestDto.getExhibitInfo().setModifyId("0");
        }

        // 항목값 추가 Set
        exhibitManageRequestDto.getExhibitInfo().setDelYn("N");                             // 삭제여부
        exhibitManageRequestDto.getExhibitInfo().setExhibitStatus(ExhibitEnums.ExhibitStatus.SAVE.getCode());     // 기획전상태
        exhibitManageRequestDto.getExhibitInfo().setApprovalStatus(ApprovalEnums.ApprovalStatus.NONE.getCode());  // 승인상태

        resultInt = exhibitManageMapper.addExhibit(exhibitManageRequestDto.getExhibitInfo());

        if (resultInt <= 0) {
          log.debug("# 기획전 기본 정보등록 처리 오류입니다.");
          resultResDto.setResultCode(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_ADD_FAIL_PROC.getCode());
          resultResDto.setResultMessage(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_ADD_FAIL_PROC.getMessage());
          throw new BaseException(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_ADD_FAIL_PROC);
        }
        log.debug("# New evExhibitId :: " + exhibitManageRequestDto.getExhibitInfo().getEvExhibitId());

        // ---------------------------------------------------------------------
        // 1.2. 기획전 기본정보ID Set
        // ---------------------------------------------------------------------
        exhibitManageRequestDto.setEvExhibitId(exhibitManageRequestDto.getExhibitInfo().getEvExhibitId());

        // ---------------------------------------------------------------------
        // 1.3. 기획전 기본정보 접근권한 - 유저그룹 등록
        // ---------------------------------------------------------------------
        List<Long> userGroupIdList = exhibitManageRequestDto.getExhibitInfo().getUserGroupIdList();
        if(userGroupIdList != null && !userGroupIdList.isEmpty()){
          exhibitManageMapper.addExhibitUserGroup(exhibitManageRequestDto.getExhibitInfo().getEvExhibitId(), userGroupIdList);
        }

        // --------------------------------------------------------------------
        // 2. 기획전별 상세 등록
        // --------------------------------------------------------------------
        if (StringUtil.isEquals(exhibitManageRequestDto.getExhibitTp(), ExhibitTp.NORMAL.getCode())) {
          // ------------------------------------------------------------------
          // 일반기획전
          // ------------------------------------------------------------------
          resultResDto = this.addExhibitGroup(exhibitManageRequestDto);

        }
        else if (StringUtil.isEquals(exhibitManageRequestDto.getExhibitTp(), ExhibitTp.SELECT.getCode())) {
          // ------------------------------------------------------------------
          // 골라담기
          // ------------------------------------------------------------------
          resultResDto = this.addExhibitSelect(exhibitManageRequestDto);
        }
        else if (StringUtil.isEquals(exhibitManageRequestDto.getExhibitTp(), ExhibitTp.GIFT.getCode())) {
          // ------------------------------------------------------------------
          // 증정행사
          // ------------------------------------------------------------------
          resultResDto = this.addExhibitGift(exhibitManageRequestDto);

        }

        // --------------------------------------------------------------------
        // 3. 등록 기획전정보 Set
        // --------------------------------------------------------------------
        if (StringUtil.isEquals(resultResDto.getResultCode(), ExhibitMessage.EXHIBIT_MANAGE_SUCCESS.getCode())) {
          resultResDto.setDetail(exhibitManageRequestDto.getExhibitInfo());
          resultResDto.setTotal(1);
        }

      }
      catch (BaseException be) {
        log.info("# addExhibit BaseException e :: " + be.toString());
        throw be;
      }
      catch (Exception e) {
        log.info("# addExhibit Exception e :: " + e.toString());
        throw new BaseException(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_ADD_FAIL);
      }

      //  ======================================================================
      // # 반환
      // ======================================================================
      return resultResDto;
    }



    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // @ 수정
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

    /**
     * 기획전 수정
     * @param exhibitManageRequestDto
     * @return
     * @throws BaseException
     */
    protected ExhibitManageResponseDto putExhibit (ExhibitManageRequestDto exhibitManageRequestDto) throws BaseException {
      log.debug("# ######################################");
      log.debug("# ExhibitManageService.putExhibit Start");
      log.debug("# ######################################");
      log.debug("# In.exhibitManageRequestDto      :: " + exhibitManageRequestDto.toString());

      // ======================================================================
      // # 초기화
      // ======================================================================
      ExhibitManageResponseDto resultResDto = new ExhibitManageResponseDto();
      resultResDto.setResultCode(ExhibitMessage.EXHIBIT_MANAGE_SUCCESS.getCode());
      resultResDto.setResultMessage(ExhibitMessage.EXHIBIT_MANAGE_SUCCESS.getMessage());
      int resultInt = 0;
      int resultDelInt = 0;

      // ======================================================================
      // # 처리
      // ======================================================================

      try {

        // --------------------------------------------------------------------
        // 1. 기획전 기본정보 수정
        // --------------------------------------------------------------------
        // --------------------------------------------------------------------
        // 1.1. 세션정보 Set
        // --------------------------------------------------------------------
        if (SessionUtil.getBosUserVO() != null) {
          exhibitManageRequestDto.getExhibitInfo().setCreateId((SessionUtil.getBosUserVO()).getUserId());
          exhibitManageRequestDto.getExhibitInfo().setModifyId((SessionUtil.getBosUserVO()).getUserId());
        }
        else {
          exhibitManageRequestDto.getExhibitInfo().setCreateId("0");
          exhibitManageRequestDto.getExhibitInfo().setModifyId("0");
        }

        // 항목값 추가 Set
        exhibitManageRequestDto.getExhibitInfo().setDelYn("N");                             // 삭제여부
        exhibitManageRequestDto.getExhibitInfo().setExhibitStatus("EXHIBIT_STATUS.SAVE");   // 기획전상태

        resultInt = exhibitManageMapper.putExhibit(exhibitManageRequestDto.getExhibitInfo());

        if (resultInt <= 0) {
          log.debug("# 기획전 기본 정보등록 처리 오류입니다.");
          resultResDto.setResultCode(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_PUT_FAIL_PROC.getCode());
          resultResDto.setResultMessage(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_PUT_FAIL_PROC.getMessage());
          throw new BaseException(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_PUT_FAIL_PROC);
        }
        log.debug("# evExhibitId :: " + exhibitManageRequestDto.getExhibitInfo().getEvExhibitId());
        // ---------------------------------------------------------------------
        // 1.2. 기획전 기본정보ID Set
        // ---------------------------------------------------------------------
        exhibitManageRequestDto.setEvExhibitId(exhibitManageRequestDto.getExhibitInfo().getEvExhibitId());

        // ---------------------------------------------------------------------
        // 1.3. 기획전 기본정보 접근권한 - 유저그룹 등록
        // ---------------------------------------------------------------------
        exhibitManageMapper.delExhibitUserGroup(exhibitManageRequestDto.getExhibitInfo().getEvExhibitId());
        List<Long> userGroupIdList = exhibitManageRequestDto.getExhibitInfo().getUserGroupIdList();
        if(userGroupIdList != null && !userGroupIdList.isEmpty()){
          exhibitManageMapper.addExhibitUserGroup(exhibitManageRequestDto.getExhibitInfo().getEvExhibitId(), userGroupIdList);
        }

        // --------------------------------------------------------------------
        // 2. 기획전별 상세 수정
        //    - 삭제 후 재등록
        // --------------------------------------------------------------------
        if (StringUtil.isEquals(exhibitManageRequestDto.getExhibitTp(), ExhibitTp.NORMAL.getCode())) {
          // ------------------------------------------------------------------
          // 2.1. 일반기획전
          // ------------------------------------------------------------------

          // ------------------------------------------------------------------
          // 2.1.2. 그룹 삭제
          // ------------------------------------------------------------------
          resultDelInt = exhibitManageMapper.delExhibitGroupByEvExhibitId(exhibitManageRequestDto.getExhibitInfo().getEvExhibitId());
          log.debug("# 그룹 삭제 건수 :: " + resultDelInt);

          // ------------------------------------------------------------------
          // 2.1.1. 그룹상세 삭제
          // ------------------------------------------------------------------
          resultDelInt = exhibitManageMapper.delExhibitGroupDetlByEvExhibitId(exhibitManageRequestDto.getExhibitInfo().getEvExhibitId());
          log.debug("# 그룹상세 삭제 건수 :: " + resultDelInt);

          // ------------------------------------------------------------------
          // 2.1.0. 그룹/그룹상세 등록
          // ------------------------------------------------------------------
          resultResDto = this.addExhibitGroup(exhibitManageRequestDto);
        }
        else if (StringUtil.isEquals(exhibitManageRequestDto.getExhibitTp(), ExhibitTp.SELECT.getCode())) {
          // ------------------------------------------------------------------
          // 2.2. 골라담기
          // ------------------------------------------------------------------

          // ------------------------------------------------------------------
          // 2.2.3. 추가상품 삭제
          // ------------------------------------------------------------------
          resultDelInt = exhibitManageMapper.delExhibitSelectAddGoodsByEvExhibitId(exhibitManageRequestDto.getExhibitInfo().getEvExhibitId());

          // ------------------------------------------------------------------
          // 2.2.2. 상품 삭제
          // ------------------------------------------------------------------
          resultDelInt = exhibitManageMapper.delExhibitSelectGoodsByEvExhibitId(exhibitManageRequestDto.getExhibitInfo().getEvExhibitId());

          // ------------------------------------------------------------------
          // 2.2.1. 골라담기상세 삭제
          // ------------------------------------------------------------------
          resultDelInt = exhibitManageMapper.delExhibitSelectByEvExhibitId(exhibitManageRequestDto.getExhibitInfo().getEvExhibitId());

          // ------------------------------------------------------------------
          // 2.2.0 골라담기 등록
          // ------------------------------------------------------------------
          resultResDto = this.addExhibitSelect(exhibitManageRequestDto);
        }
        else if (StringUtil.isEquals(exhibitManageRequestDto.getExhibitTp(), ExhibitTp.GIFT.getCode())) {
          // ------------------------------------------------------------------
          // 2.3. 증정행사
          // ------------------------------------------------------------------

          // ------------------------------------------------------------------
          // 2.3.5. 그룹 삭제
          // ------------------------------------------------------------------
          // 그룹상세
          resultDelInt = exhibitManageMapper.delExhibitGroupDetlByEvExhibitId(exhibitManageRequestDto.getExhibitInfo().getEvExhibitId());
          log.debug("# 그룹상세 삭제 건수 :: " + resultDelInt);
          // 그룹
          resultDelInt = exhibitManageMapper.delExhibitGroupByEvExhibitId(exhibitManageRequestDto.getExhibitInfo().getEvExhibitId());
          log.debug("# 그룹 삭제 건수 :: " + resultDelInt);

          // ------------------------------------------------------------------
          // 2.3.4. 증정대상 상품 삭제
          // ------------------------------------------------------------------
          resultDelInt = exhibitManageMapper.delExhibitGiftTargetGoodsByEvExhibitId(exhibitManageRequestDto.getExhibitInfo().getEvExhibitId());
          log.debug("# 증정대상 상품 삭제 건수 :: " + resultDelInt);

          // ------------------------------------------------------------------
          // 2.3.3. 증정대상 브랜드 삭제
          // ------------------------------------------------------------------
          resultDelInt = exhibitManageMapper.delExhibitGiftTargetBrandByEvExhibitId(exhibitManageRequestDto.getExhibitInfo().getEvExhibitId());
          log.debug("# 증정대상 브랜드 삭제 건수 :: " + resultDelInt);

          // ------------------------------------------------------------------
          // 2.3.2. 증정행사 상품 삭제
          // ------------------------------------------------------------------
          resultDelInt = exhibitManageMapper.delExhibitGiftGoodsByEvExhibitId(exhibitManageRequestDto.getExhibitInfo().getEvExhibitId());
          log.debug("# 증정 상품 삭제 건수 :: " + resultDelInt);

          // ------------------------------------------------------------------
          // 2.3.1. 증정행사 상세 삭제
          // ------------------------------------------------------------------
          resultDelInt = exhibitManageMapper.delExhibitGiftByEvExhibitId(exhibitManageRequestDto.getExhibitInfo().getEvExhibitId());
          log.debug("# 증정 상세 삭제 건수 :: " + resultDelInt);

          // ------------------------------------------------------------------
          // 2.3.0. 증정행사 등록
          // ------------------------------------------------------------------
          resultResDto = this.addExhibitGift(exhibitManageRequestDto);
        }

        // --------------------------------------------------------------------
        // 3. 등록 기획전정보 Set
        // --------------------------------------------------------------------
        if (StringUtil.isEquals(resultResDto.getResultCode(), ExhibitMessage.EXHIBIT_MANAGE_SUCCESS.getCode())) {
          resultResDto.setDetail(exhibitManageRequestDto.getExhibitInfo());
          resultResDto.setTotal(1);
        }

      }
      catch (BaseException be) {
        log.info("# putExhibit BaseException e :: " + be.toString());
        be.printStackTrace();
        throw be;
      }
      catch (Exception e) {
        log.info("# putExhibit Exception e :: " + e.toString());
        e.printStackTrace();
        throw new BaseException(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_PUT_FAIL);
      }

      //  ======================================================================
      // # 반환
      // ======================================================================
      return resultResDto;
    }

    /**
     * 증정행사 대표상품 변경
     * @param exhibitManageRequestDto
     * @return
     * @throws BaseException
     */
    protected ExhibitManageResponseDto putExhibitGiftRepGoods (ExhibitManageRequestDto exhibitManageRequestDto) throws BaseException {
      log.debug("# ######################################");
      log.debug("# ExhibitManageService.putExhibitGiftRepGoods Start");
      log.debug("# ######################################");
      log.debug("# In.exhibitManageRequestDto      :: " + exhibitManageRequestDto.toString());

      // ======================================================================
      // # 초기화
      // ======================================================================
      ExhibitManageResponseDto resultResDto = new ExhibitManageResponseDto();
      resultResDto.setResultCode(ExhibitMessage.EXHIBIT_MANAGE_SUCCESS.getCode());
      resultResDto.setResultMessage(ExhibitMessage.EXHIBIT_MANAGE_SUCCESS.getMessage());
      int resultInt = 0;
      int resultDelInt = 0;

      // ======================================================================
      // # 처리
      // ======================================================================

      try {

        // --------------------------------------------------------------------
        // 1. 세션정보 Set
        // --------------------------------------------------------------------
        //if (SessionUtil.getBosUserVO() != null) {
        //  exhibitManageRequestDto.getExhibitInfo().setCreateId((SessionUtil.getBosUserVO()).getUserId());
        //  exhibitManageRequestDto.getExhibitInfo().setModifyId((SessionUtil.getBosUserVO()).getUserId());
        //}
        //else {
        //  exhibitManageRequestDto.getExhibitInfo().setCreateId("0");
        //  exhibitManageRequestDto.getExhibitInfo().setModifyId("0");
        //}
        //
        //// 항목값 추가 Set
        //exhibitManageRequestDto.getExhibitInfo().setDelYn("N");                             // 삭제여부
        //exhibitManageRequestDto.getExhibitInfo().setExhibitStatus("EXHIBIT_STATUS.SAVE");   // 기획전상태

        // --------------------------------------------------------------------
        // 2. 현 대표상품 혀재
        // --------------------------------------------------------------------
        resultInt = exhibitManageMapper.putExhibitGiftRepGoodsCancel(exhibitManageRequestDto.getEvExhibitId());

        if (resultInt <= 0) {
          log.debug("# 대표상품변경 해제 오류입니다.");
          resultResDto.setResultCode(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GIFT_REP_CANCEL_PUT_FAIL_PROC.getCode());
          resultResDto.setResultMessage(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GIFT_REP_CANCEL_PUT_FAIL_PROC.getMessage());
          throw new BaseException(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GIFT_REP_CANCEL_PUT_FAIL_PROC);
        }
        log.debug("# evExhibitId :: " + exhibitManageRequestDto.getEvExhibitId());

        // --------------------------------------------------------------------
        // 3. 신 대표상품 설정
        // --------------------------------------------------------------------
        resultInt = exhibitManageMapper.putExhibitGiftRepGoodsReg(exhibitManageRequestDto.getEvExhibitGiftGoodsId());

        if (resultInt <= 0) {
          log.debug("# 대표상품변경 설정 오류입니다.");
          resultResDto.setResultCode(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GIFT_REP_REGIST_PUT_FAIL_PROC.getCode());
          resultResDto.setResultMessage(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GIFT_REP_REGIST_PUT_FAIL_PROC.getMessage());
          throw new BaseException(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GIFT_REP_REGIST_PUT_FAIL_PROC);
        }
        log.debug("# evExhibitGiftGoodsId :: " + exhibitManageRequestDto.getEvExhibitGiftGoodsId());

      }
      catch (BaseException be) {
        log.info("# putExhibitGiftRepGoods BaseException e :: " + be.toString());
        be.printStackTrace();
        throw be;
      }
      catch (Exception e) {
        log.info("# putExhibitGiftRepGoods Exception e :: " + e.toString());
        e.printStackTrace();
        throw new BaseException(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GIFT_REP_PUT_FAIL);
      }

      //  ======================================================================
      // # 반환
      // ======================================================================
      return resultResDto;
    }

    // ########################################################################
    // private
    // ########################################################################
    /**
     * 일반기획전 등록
     * @param exhibitManageRequestDto
     * @return
     * @throws BaseException
     */
    private ExhibitManageResponseDto addExhibitGroup (ExhibitManageRequestDto exhibitManageRequestDto) throws BaseException {
      log.debug("# ######################################");
      log.debug("# ExhibitManageService.addExhibitGroup Start");
      log.debug("# ######################################");
      log.debug("# In.evExhibitId      :: " + exhibitManageRequestDto.getEvExhibitId());

      // ======================================================================
      // # 초기화
      // ======================================================================
      ExhibitManageResponseDto resultResDto = new ExhibitManageResponseDto();
      resultResDto.setResultCode(ExhibitMessage.EXHIBIT_MANAGE_SUCCESS.getCode());
      resultResDto.setResultMessage(ExhibitMessage.EXHIBIT_MANAGE_SUCCESS.getMessage());
      int resultInt = 0;
      int resultDetlInt = 0;

      // ======================================================================
      // # 처리
      // ======================================================================

      try {

        if (StringUtil.isNotEmpty(exhibitManageRequestDto.getGroupList()) && exhibitManageRequestDto.getGroupList().size() > 0) {

          for (ExhibitGroupVo exhibitGroupVo : exhibitManageRequestDto.getGroupList()) {

            // ----------------------------------------------------------------
            // 0. Param Set
            // ----------------------------------------------------------------
            // 기획전ID
            exhibitGroupVo.setEvExhibitId(exhibitManageRequestDto.getEvExhibitId());
            // 사용여부
            exhibitGroupVo.setUseYn(exhibitGroupVo.getGroupUseYn());

            // 배경컬러 사용안할 경우 배경색상 널 처리
            if (StringUtil.isEquals(exhibitGroupVo.getExhibitImgTp(), ExhibitImgTp.NOT_USE.getCode())) {
              exhibitGroupVo.setBgCd("");
            }

            // ----------------------------------------------------------------
            // 1. 그룹정보 등록
            // ----------------------------------------------------------------
            resultInt = exhibitManageMapper.addExhibitGroup(exhibitGroupVo);

            if (resultInt <= 0) {
              log.debug("# 기획전 그룹 정보등록 처리 오류입니다.");
              resultResDto.setResultCode(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GROUP_ADD_FAIL_PROC.getCode());
              resultResDto.setResultMessage(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GROUP_ADD_FAIL_PROC.getMessage());
              throw new BaseException(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GROUP_ADD_FAIL_PROC);
            }

            log.debug("# New evExhibitGroupId :: " + exhibitGroupVo.getEvExhibitGroupId());

            // ----------------------------------------------------------------
            // 2. 그룹상세정보 등록 (그룹상품정보)
            // ----------------------------------------------------------------
            if (StringUtil.isNotEmpty(exhibitGroupVo.getGroupGoodsList()) && exhibitGroupVo.getGroupGoodsList().size() > 0) {

              for (ExhibitGroupDetlVo exhibitGroupDetlVo : exhibitGroupVo.getGroupGoodsList()) {

                // ------------------------------------------------------------
                // 2.1. 기획전그룹ID Set
                // ------------------------------------------------------------
                exhibitGroupDetlVo.setEvExhibitGroupId(exhibitGroupVo.getEvExhibitGroupId());

                // ------------------------------------------------------------
                // 2.2. 기획전 그룹상세 등록
                // ------------------------------------------------------------
                resultDetlInt = exhibitManageMapper.addExhibitGroupDetl(exhibitGroupDetlVo);

                if (resultDetlInt <= 0) {
                  log.debug("# 기획전 그룹상세 정보등록 처리 오류입니다.");
                  resultResDto.setResultCode(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GROUP_DETL_ADD_FAIL_PROC.getCode());
                  resultResDto.setResultMessage(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GROUP_DETL_ADD_FAIL_PROC.getMessage());
                  throw new BaseException(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GROUP_DETL_ADD_FAIL_PROC);
                }
                log.debug("# New EV_EXHIBIT_GROUP_DETL_ID :: [" + exhibitGroupVo.getEvExhibitGroupId() + "][" + exhibitGroupDetlVo.getEvExhibitGroupDetlId() + "]");

              } // End of for (ExhibitGroupDetlVo exhibitGroupDetlVo : exhibitGroupVo.getGroupGoodsList())

            } // End of if (StringUtil.isNotEmpty(exhibitGroupVo.getGroupGoodsList()) && exhibitGroupVo.getGroupGoodsList().size() > 0)

          } // End of for (ExhibitGroupVo exhibitGroupVo : exhibitManageRequestDto.getGroupList())

        } // End of if (StringUtil.isNotEmpty(exhibitManageRequestDto.getGroupList()) && exhibitManageRequestDto.getGroupList().size() > 0)

      }
      catch (BaseException be) {
        log.info("# addExhibitNormal BaseException e :: " + be.toString());
        throw be;
      }
      catch (Exception e) {
        log.info("# addExhibitNormal Exception e :: " + e.toString());
        throw new BaseException(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GROUP_ADD_FAIL);
      }

      //  ======================================================================
      // # 반환
      // ======================================================================
      return resultResDto;
    }

    /**
     * 골라담기 등록
     * @param exhibitManageRequestDto
     * @return
     * @throws BaseException
     */
    private ExhibitManageResponseDto addExhibitSelect (ExhibitManageRequestDto exhibitManageRequestDto) throws BaseException {
      log.debug("# ######################################");
      log.debug("# ExhibitManageService.addExhibitSelect Start");
      log.debug("# ######################################");
      log.debug("# In.evExhibitId      :: " + exhibitManageRequestDto.getEvExhibitId());

      // ======================================================================
      // # 초기화
      // ======================================================================
      ExhibitManageResponseDto resultResDto = new ExhibitManageResponseDto();
      resultResDto.setResultCode(ExhibitMessage.EXHIBIT_MANAGE_SUCCESS.getCode());
      resultResDto.setResultMessage(ExhibitMessage.EXHIBIT_MANAGE_SUCCESS.getMessage());
      int resultInt = 0;

      // ======================================================================
      // # 처리
      // ======================================================================

      try {

        // --------------------------------------------------------------------
        // 0. Param Set
        // --------------------------------------------------------------------
        // 기획전ID
        exhibitManageRequestDto.getExhibitSelectInfo().setEvExhibitId(exhibitManageRequestDto.getEvExhibitId());

        // --------------------------------------------------------------------
        // 1. 골라담기상세 등록
        // --------------------------------------------------------------------
        resultInt = exhibitManageMapper.addExhibitSelect(exhibitManageRequestDto.getExhibitSelectInfo());

        if (resultInt <= 0) {
          log.debug("# 기획전 골라담기 정보등록 처리 오류입니다.");
          resultResDto.setResultCode(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_SELECT_ADD_FAIL_PROC.getCode());
          resultResDto.setResultMessage(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_SELECT_ADD_FAIL_PROC.getMessage());
          throw new BaseException(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_SELECT_ADD_FAIL_PROC);
        }

        // --------------------------------------------------------------------
        // 2. 골라담기상품 등록
        // --------------------------------------------------------------------
        if (exhibitManageRequestDto.getExhibitSelectInfo().getSelectGoodsList() != null && exhibitManageRequestDto.getExhibitSelectInfo().getSelectGoodsList().size() > 0) {

          List<ExhibitSelectGoodsVo> selectGodosList = exhibitManageRequestDto.getExhibitSelectInfo().getSelectGoodsList();

          for (ExhibitSelectGoodsVo exhibitSelectGoodsVo : selectGodosList) {

            // 기획전ID Set
            exhibitSelectGoodsVo.setEvExhibitId(exhibitManageRequestDto.getEvExhibitId());

            resultInt = exhibitManageMapper.addExhibitSelectGoods(exhibitSelectGoodsVo);

            if (resultInt <= 0) {
              log.debug("# 기획전 골라담기 상품 정보등록 처리 오류입니다.");
              resultResDto.setResultCode(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_SELECT_GOODS_ADD_FAIL_PROC.getCode());
              resultResDto.setResultMessage(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_SELECT_GOODS_ADD_FAIL_PROC.getMessage());
              throw new BaseException(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_SELECT_GOODS_ADD_FAIL_PROC);
            }

          } // End of for (ExhibitSelectGoodsVo exhibitSelectGoodsVo : selectGodosList)

        } // End of if (exhibitManageRequestDto.getExhibitSelectInfo().getSelectGoodsList() != null && exhibitManageRequestDto.getExhibitSelectInfo().getSelectGoodsList().size() > 0)

        // --------------------------------------------------------------------
        // 3. 골라담기추가상품 등록
        // --------------------------------------------------------------------
        if (exhibitManageRequestDto.getExhibitSelectInfo().getSelectAddGoodsList() != null && exhibitManageRequestDto.getExhibitSelectInfo().getSelectAddGoodsList().size() > 0) {

          List<ExhibitSelectGoodsVo> selectAddGodosList = exhibitManageRequestDto.getExhibitSelectInfo().getSelectAddGoodsList();

          for (ExhibitSelectGoodsVo exhibitSelectGoodsVo : selectAddGodosList) {

            // 기획전ID Set
            exhibitSelectGoodsVo.setEvExhibitId(exhibitManageRequestDto.getEvExhibitId());

            resultInt = exhibitManageMapper.addExhibitSelectAddGoods(exhibitSelectGoodsVo);

            if (resultInt <= 0) {
              log.debug("# 기획전 골라담기 추가상품 정보등록 처리 오류입니다.");
              resultResDto.setResultCode(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_SELECT_ADD_GOODS_ADD_FAIL_PROC.getCode());
              resultResDto.setResultMessage(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_SELECT_ADD_GOODS_ADD_FAIL_PROC.getMessage());
              throw new BaseException(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_SELECT_ADD_GOODS_ADD_FAIL_PROC);
            }

          } // End of for (ExhibitSelectGoodsVo exhibitSelectGoodsVo : selectAddGodosList)

        } // End of if (exhibitManageRequestDto.getExhibitSelectInfo().getSelectAddGoodsList() != null && exhibitManageRequestDto.getExhibitSelectInfo().getSelectAddGoodsList().size() > 0)


        // --------------------------------------------------------------------
        // 4. 승인처리
        // --------------------------------------------------------------------
        //UserVo userVo = SessionUtil.getBosUserVO();
        //if (userVo != null) {
        //  exhibitManageRequestDto.getExhibitInfo().setModifyId(userVo.getUserId());
        //}
        log.debug("# ################################################");
        log.debug("# approvalRequestYn  :: " + exhibitManageRequestDto.getExhibitInfo().getApprovalRequestYn());
        log.debug("# evExhibitId        :: " + exhibitManageRequestDto.getExhibitInfo().getEvExhibitId());
        log.debug("# approvalStatus     :: " + exhibitManageRequestDto.getExhibitInfo().getApprovalStatus());
        log.debug("# approvalSubUserId  :: " + exhibitManageRequestDto.getExhibitInfo().getApprovalSubUserId());
        log.debug("# approvalUserId     :: " + exhibitManageRequestDto.getExhibitInfo().getApprovalUserId());

        if (StringUtil.isEquals(exhibitManageRequestDto.getExhibitInfo().getApprovalRequestYn(), YES)) {
          // 승인요청인 경우
          ApprovalStatusVo approvalVo = ApprovalStatusVo.builder()
                  .taskPk(exhibitManageRequestDto.getExhibitInfo().getEvExhibitId())
                  .prevMasterStat(ExhibitEnums.ExhibitStatus.SAVE.getCode())
                  .prevApprStat(exhibitManageRequestDto.getExhibitInfo().getApprovalStatus())
                  .masterStat(ExhibitEnums.ExhibitStatus.SAVE.getCode())
                  .apprStat(ApprovalEnums.ApprovalStatus.REQUEST.getCode())
                  .apprSubUserId(exhibitManageRequestDto.getExhibitInfo().getApprovalSubUserId())
                  .apprUserId(exhibitManageRequestDto.getExhibitInfo().getApprovalUserId())
                  .approvalRequestUserId(SessionUtil.getBosUserVO().getUserId())
                  .build();
          log.debug("# approvalVo     :: " + approvalVo.toString());

          exhibitManageRequestDto.getExhibitInfo().setExhibitStatus(ExhibitEnums.ExhibitStatus.SAVE.getCode());
          exhibitManageRequestDto.getExhibitInfo().setApprovalStatus(ApprovalEnums.ApprovalStatus.REQUEST.getCode());
          log.debug("# exhibitStatus      :: " + exhibitManageRequestDto.getExhibitInfo().getExhibitStatus());
          log.debug("# approvalStatus     :: " + exhibitManageRequestDto.getExhibitInfo().getApprovalStatus());

          MessageCommEnum resultApprovalEnum = this.putApprovalRequestExhibit(exhibitManageRequestDto.getExhibitInfo(), approvalVo);

          log.debug("# resultApprovalEnum :: " + resultApprovalEnum.toString());
        }
        log.debug("# ################################################");

      }
      catch (BaseException be) {
        log.info("# addExhibitSelect BaseException e :: " + be.toString());
        throw be;
      }
      catch (Exception e) {
        log.info("# addExhibitSelect Exception e :: " + e.toString());
        throw new BaseException(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_SELECT_ADD_FAIL);
      }

      //  ======================================================================
      // # 반환
      // ======================================================================
      return resultResDto;
    }

    /**
     * 증정행사 등록
     * @param exhibitManageRequestDto
     * @return
     * @throws BaseException
     */
    private ExhibitManageResponseDto addExhibitGift (ExhibitManageRequestDto exhibitManageRequestDto) throws BaseException {
      log.debug("# ######################################");
      log.debug("# ExhibitManageService.addExhibitGift Start");
      log.debug("# ######################################");
      log.debug("# In.evExhibitId      :: " + exhibitManageRequestDto.getEvExhibitId());

      // ======================================================================
      // # 초기화
      // ======================================================================
      ExhibitManageResponseDto resultResDto = new ExhibitManageResponseDto();
      resultResDto.setResultCode(ExhibitMessage.EXHIBIT_MANAGE_SUCCESS.getCode());
      resultResDto.setResultMessage(ExhibitMessage.EXHIBIT_MANAGE_SUCCESS.getMessage());
      int resultInt = 0;

      // ======================================================================
      // # 처리
      // ======================================================================

      try {

        // --------------------------------------------------------------------
        // 0. Param Set
        // --------------------------------------------------------------------
        // 기획전ID
        exhibitManageRequestDto.getExhibitGiftInfo().setEvExhibitId(exhibitManageRequestDto.getEvExhibitId());

        // --------------------------------------------------------------------
        // 1. 증정행사상세 등록
        // --------------------------------------------------------------------
        //log.debug("# ExhibitGiftInfo :: " + exhibitManageRequestDto.getExhibitGiftInfo().toString());
        resultInt = exhibitManageMapper.addExhibitGift(exhibitManageRequestDto.getExhibitGiftInfo());

        if (resultInt <= 0) {
          log.debug("# 기획전 증정행사 정보등록 처리 오류입니다.");
          resultResDto.setResultCode(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GIFT_ADD_FAIL_PROC.getCode());
          resultResDto.setResultMessage(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GIFT_ADD_FAIL_PROC.getMessage());
          throw new BaseException(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GIFT_ADD_FAIL_PROC);
        }

        // --------------------------------------------------------------------
        // 2. 증정행사상품 등록
        // --------------------------------------------------------------------
        if (exhibitManageRequestDto.getExhibitGiftInfo().getGiftGoodsList() != null && exhibitManageRequestDto.getExhibitGiftInfo().getGiftGoodsList().size() > 0) {

          List<ExhibitGiftGoodsVo> giftGodosList = exhibitManageRequestDto.getExhibitGiftInfo().getGiftGoodsList();

          for (ExhibitGiftGoodsVo exhibitGiftGoodsVo : giftGodosList) {

            // 기획전ID Set
            exhibitGiftGoodsVo.setEvExhibitId(exhibitManageRequestDto.getEvExhibitId());

            resultInt = exhibitManageMapper.addExhibitGiftGoods(exhibitGiftGoodsVo);

            if (resultInt <= 0) {
              log.debug("# 기획전 증정행사 상품 정보등록 처리 오류입니다.");
              resultResDto.setResultCode(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GIFT_GOODS_ADD_FAIL_PROC.getCode());
              resultResDto.setResultMessage(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GIFT_GOODS_ADD_FAIL_PROC.getMessage());
              throw new BaseException(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GIFT_GOODS_ADD_FAIL_PROC);
            }

          } // End of for (ExhibitGiftGoodsVo exhibitGiftGoodsVo : giftGodosList)

        } // End of if (exhibitManageRequestDto.getExhibitGiftInfo().getGiftGoodsList() != null && exhibitManageRequestDto.getExhibitGiftInfo().getGiftGoodsList().size() > 0)

        // --------------------------------------------------------------------
        // 3. 증정대상상품/브랜드 등록
        // --------------------------------------------------------------------
        if (StringUtil.isEquals(exhibitManageRequestDto.getExhibitGiftInfo().getGiftTargetTp(), GiftTargetTp.GOODS.getCode())) {
          // ------------------------------------------------------------------
          // 3.1. 증정대상상품 등록
          // ------------------------------------------------------------------
          if (exhibitManageRequestDto.getExhibitGiftInfo().getGiftTargetGoodsList() != null && exhibitManageRequestDto.getExhibitGiftInfo().getGiftTargetGoodsList().size() > 0) {

            List<ExhibitGiftGoodsVo> giftTargetGodosList = exhibitManageRequestDto.getExhibitGiftInfo().getGiftTargetGoodsList();

            for (ExhibitGiftGoodsVo exhibitGiftTargetGoodsVo : giftTargetGodosList) {

              // 기획전ID Set
              exhibitGiftTargetGoodsVo.setEvExhibitId(exhibitManageRequestDto.getEvExhibitId());

              resultInt = exhibitManageMapper.addExhibitGiftTargetGoods(exhibitGiftTargetGoodsVo);

              if (resultInt <= 0) {
                log.debug("# 기획전 증정행사 대상상품 정보등록 처리 오류입니다.");
                resultResDto.setResultCode(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GIFT_TARGET_GOODS_ADD_FAIL_PROC.getCode());
                resultResDto.setResultMessage(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GIFT_TARGET_GOODS_ADD_FAIL_PROC.getMessage());
                throw new BaseException(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GIFT_TARGET_GOODS_ADD_FAIL_PROC);
              }

            } // End of for (ExhibitGiftGoodsVo exhibitGiftTargetGoodsVo : giftTargetGodosList)

          } // End of if (exhibitManageRequestDto.getExhibitGiftInfo().getGiftTargetGoodsList() != null && exhibitManageRequestDto.getExhibitGiftInfo().getGiftTargetGoodsList().size() > 0)

        }
        else if (StringUtil.isEquals(exhibitManageRequestDto.getExhibitGiftInfo().getGiftTargetTp(), GiftTargetTp.BRAND.getCode())) {
          // ------------------------------------------------------------------
          // 3.2. 증정대상브랜드 등록
          // ------------------------------------------------------------------
          if (exhibitManageRequestDto.getExhibitGiftInfo().getGiftTargetBrandList() != null && exhibitManageRequestDto.getExhibitGiftInfo().getGiftTargetBrandList().size() > 0) {

            List<ExhibitGiftGoodsVo> giftTargetBrandList = exhibitManageRequestDto.getExhibitGiftInfo().getGiftTargetBrandList();

            for (ExhibitGiftGoodsVo exhibitGiftTargetBrandVo : giftTargetBrandList) {

              // 기획전ID Set
              exhibitGiftTargetBrandVo.setEvExhibitId(exhibitManageRequestDto.getEvExhibitId());

              resultInt = exhibitManageMapper.addExhibitGiftTargetBrand(exhibitGiftTargetBrandVo);

              if (resultInt <= 0) {
                log.debug("# 기획전 증정행사 대상브랜드 정보등록 처리 오류입니다.");
                resultResDto.setResultCode(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GIFT_TARGET_BRAND_ADD_FAIL_PROC.getCode());
                resultResDto.setResultMessage(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GIFT_TARGET_BRAND_ADD_FAIL_PROC.getMessage());
                throw new BaseException(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GIFT_TARGET_BRAND_ADD_FAIL_PROC);
              }

            } // End of for (ExhibitGiftGoodsVo exhibitGiftTargetBrandVo : giftTargetBrandList)

          } // End of if (exhibitManageRequestDto.getExhibitGiftInfo().getGiftTargetBrandList() != null && exhibitManageRequestDto.getExhibitGiftInfo().getGiftTargetBrandList().size() > 0)
        }
        else {
          // 적용대상유형 오류
          // 기획전 증정행사 적용대상유형 입력정보 오류입니다.
          log.debug("# 기획전 증정행사 적용대상유형 입력정보 오류입니다.");
          resultResDto.setResultCode(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GIFT_TARGET_ADD_FAIL_INPUT_TARGET_TP.getCode());
          resultResDto.setResultMessage(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GIFT_TARGET_ADD_FAIL_INPUT_TARGET_TP.getMessage());
          throw new BaseException(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GIFT_TARGET_ADD_FAIL_INPUT_TARGET_TP);
        }

        // --------------------------------------------------------------------
        // 4. 그룹 등록
        // --------------------------------------------------------------------
        if (StringUtil.isEquals(exhibitManageRequestDto.getExhibitGiftInfo().getExhibitDispYn(), "Y")) {

          ExhibitManageResponseDto resultGroupDto = this.addExhibitGroup(exhibitManageRequestDto);

          if (StringUtil.isEquals(resultGroupDto.getResultCode(), ExhibitMessage.EXHIBIT_MANAGE_SUCCESS.getCode())) {
            // 성공
          }
          else {
            log.debug("# 기획전 증정행사 그룹 정보등록 처리 오류입니다.");
            resultResDto.setResultCode(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GIFT_GROUP_ADD_FAIL_PROC.getCode());
            resultResDto.setResultMessage(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GIFT_GROUP_ADD_FAIL_PROC.getMessage());
            throw new BaseException(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GIFT_GROUP_ADD_FAIL_PROC);
          }

        }


        // --------------------------------------------------------------------
        // 5. 승인처리
        // --------------------------------------------------------------------
        //UserVo userVo = SessionUtil.getBosUserVO();
        //if (userVo != null) {
        //  exhibitManageRequestDto.getExhibitInfo().setModifyId(userVo.getUserId());
        //}
        log.debug("# ################################################");
        log.debug("# approvalRequestYn  :: " + exhibitManageRequestDto.getExhibitInfo().getApprovalRequestYn());
        log.debug("# evExhibitId        :: " + exhibitManageRequestDto.getExhibitInfo().getEvExhibitId());
        log.debug("# approvalStatus     :: " + exhibitManageRequestDto.getExhibitInfo().getApprovalStatus());
        log.debug("# approvalSubUserId  :: " + exhibitManageRequestDto.getExhibitInfo().getApprovalSubUserId());
        log.debug("# approvalUserId     :: " + exhibitManageRequestDto.getExhibitInfo().getApprovalUserId());

        if (StringUtil.isEquals(exhibitManageRequestDto.getExhibitInfo().getApprovalRequestYn(), YES)) {
          // 승인요청인 경우
          ApprovalStatusVo approvalVo = ApprovalStatusVo.builder()
                  .taskPk(exhibitManageRequestDto.getExhibitInfo().getEvExhibitId())
                  .prevMasterStat(ExhibitEnums.ExhibitStatus.SAVE.getCode())
                  .prevApprStat(exhibitManageRequestDto.getExhibitInfo().getApprovalStatus())
                  .masterStat(ExhibitEnums.ExhibitStatus.SAVE.getCode())
                  .apprStat(ApprovalEnums.ApprovalStatus.REQUEST.getCode())
                  .apprSubUserId(exhibitManageRequestDto.getExhibitInfo().getApprovalSubUserId())
                  .apprUserId(exhibitManageRequestDto.getExhibitInfo().getApprovalUserId())
                  .approvalRequestUserId(SessionUtil.getBosUserVO().getUserId())
                  .build();
          log.debug("# approvalVo     :: " + approvalVo.toString());

          exhibitManageRequestDto.getExhibitInfo().setExhibitStatus(ExhibitEnums.ExhibitStatus.SAVE.getCode());
          exhibitManageRequestDto.getExhibitInfo().setApprovalStatus(ApprovalEnums.ApprovalStatus.REQUEST.getCode());
          log.debug("# exhibitStatus      :: " + exhibitManageRequestDto.getExhibitInfo().getExhibitStatus());
          log.debug("# approvalStatus     :: " + exhibitManageRequestDto.getExhibitInfo().getApprovalStatus());

          MessageCommEnum resultApprovalEnum = this.putApprovalRequestExhibit(exhibitManageRequestDto.getExhibitInfo(), approvalVo);

          log.debug("# resultApprovalEnum :: " + resultApprovalEnum.toString());
        }
        log.debug("# ################################################");

      }
      catch (BaseException be) {
        log.info("# addExhibitGift BaseException e :: " + be.toString());
        throw be;
      }
      catch (Exception e) {
        log.info("# addExhibitGift Exception e :: " + e.toString());
        throw new BaseException(ExhibitMessage.EXHIBIT_MANAGE_EXHIBIT_GIFT_ADD_FAIL);
      }

      //  ======================================================================
      // # 반환
      // ======================================================================
      return resultResDto;
    }

    /**
     * 사용자그룹조회
     * @param exExhibitId
     * @return
     * @throws BaseException
     */
    protected List<EvUserGroupVo> getExhibitUserGroup (String exExhibitId) throws BaseException {
      log.debug("# ######################################");
      log.debug("# ExhibitManageService.getExhibitUserGroup Start");
      log.debug("# ######################################");
      log.debug("# In.evExhibitId      :: " + exExhibitId);
      return exhibitManageMapper.getExhibitUserGroup(exExhibitId);
    }

//    /**
//     * 기획전 상세조회 - 일반
//     * @param resultResDto
//     * @throws BaseException
//     */
//    @SuppressWarnings("unused")
//    private void selectExhibitInfoNormal (ExhibitManageResponseDto resultResDto) throws BaseException {
//      log.debug("# ######################################");
//      log.debug("# ExhibitManageService.selectExhibitInfoNormal Start");
//      log.debug("# ######################################");
//
//      // ======================================================================
//      // # 초기화
//      // ======================================================================
//      // 기획전-그룹정보
//      List<ExhibitGroupVo>        resultGroupList             = null;
//      List<ExhibitGroupDetlVo>    resultGroupGoodsList        = null;
//
//      // ======================================================================
//      // # 처리
//      // ======================================================================
//
//      // ----------------------------------------------------------------------
//      // 1. 그룹 리스트조회
//      // ----------------------------------------------------------------------
//      resultGroupList = exhibitManageMapper.selectfExhibitGroupList(resultResDto.getDetail().getEvExhibitId());
//
//      if (resultGroupList == null || resultGroupList.size() <= 0) {
//        log.debug("# 그룹 정보가 없습니다.");
//        resultResDto.setResultCode(ExhibitMessage.EXHIBIT_MANAGE_GROUP_NO_DATA.getCode());
//        resultResDto.setResultMessage(ExhibitMessage.EXHIBIT_MANAGE_GROUP_NO_DATA.getMessage());
//        return;
//      }
//
//      // ----------------------------------------------------------------------
//      // 2. 상품 리스트조회
//      // ----------------------------------------------------------------------
//      for (ExhibitGroupVo unitExhibitGroupVo : resultGroupList) {
//
//        resultGroupGoodsList = exhibitManageMapper.selectfExhibitGroupGoodsList(unitExhibitGroupVo.getEvExhibitGroupId());
//        unitExhibitGroupVo.setGroupGoodsList(resultGroupGoodsList);
//
//      } // End of for (ExhibitGroupVo unitExhibitGroupVo : resultGroupList)
//
//      // ----------------------------------------------------------------------
//      // 3. 기획전상세.그룹정보 Set
//      // ----------------------------------------------------------------------
//      resultResDto.getDetail().setGroupInfoList(resultGroupList);
//
//      // ======================================================================
//      // # 반환
//      // ======================================================================
//    }
//
//    /**
//     * 기획전 상세조회 - 골라담기
//     * @param resultResDto
//     * @throws BaseException
//     */
//    @SuppressWarnings("unused")
//    private void selectExhibitInfoSelect (ExhibitManageResponseDto resultResDto) throws BaseException {
//      log.debug("# ######################################");
//      log.debug("# ExhibitManageService.selectExhibitInfoSelect Start");
//      log.debug("# ######################################");
//
//      // ======================================================================
//      // # 초기화
//      // ======================================================================
//      // 기획전-골라담기정보
//      ExhibitSelectVo             resultSelectInfo            = null;
//      List<ExhibitSelectGoodsVo>  resultSelectGoodsList       = null;
//      List<ExhibitSelectGoodsVo>  resultSelectAddGoodsList    = null;
//
//      // ======================================================================
//      // # 처리
//      // ======================================================================
//
//      // ----------------------------------------------------------------------
//      // 1. 골라담기 조회
//      // ----------------------------------------------------------------------
//      resultSelectInfo = exhibitManageMapper.selectfExhibitSelectInfo(resultResDto.getDetail().getEvExhibitId());
//
//      if (resultSelectInfo == null || StringUtil.isEmpty(resultSelectInfo.getEvExhibitSelectId()) || StringUtil.isEquals(resultSelectInfo.getEvExhibitSelectId(), "0")) {
//        log.debug("# 골라담기 정보가 없습니다.");
//        resultResDto.setResultCode(ExhibitMessage.EXHIBIT_MANAGE_SELECT_NO_DATA.getCode());
//        resultResDto.setResultMessage(ExhibitMessage.EXHIBIT_MANAGE_SELECT_NO_DATA.getMessage());
//        return;
//      }
//
//      // ----------------------------------------------------------------------
//      // 2. 상품 리스트조회
//      // ----------------------------------------------------------------------
//      // --------------------------------------------------------------------
//      // 2.1. 상품 리스트조회
//      // --------------------------------------------------------------------
//      resultSelectGoodsList = exhibitManageMapper.selectExhibitSelectGoodsList(resultSelectInfo.getEvExhibitSelectId());
//      resultSelectInfo.setSelectGoodsList(resultSelectGoodsList);
//
//      // --------------------------------------------------------------------
//      // 2.2. 추가상품 리스트조회
//      // --------------------------------------------------------------------
//      resultSelectAddGoodsList = exhibitManageMapper.selectExhibitSelectAddGoodsList(resultSelectInfo.getEvExhibitSelectId());
//      resultSelectInfo.setSelectAddGoodsList(resultSelectAddGoodsList);
//
//      // ----------------------------------------------------------------------
//      // 3. 기획전상세.골라담기정보 Set
//      // ----------------------------------------------------------------------
//      resultResDto.getDetail().setSelectExhibitInfo(resultSelectInfo);
//
//      // ======================================================================
//      // # 반환
//      // ======================================================================
//    }
//
//    /**
//     * 기획전 상세조회 - 증정행사
//     * @param resultResDto
//     * @throws BaseException
//     */
//    @SuppressWarnings("unused")
//    private void selectExhibitInfoGiftAaaa (ExhibitManageResponseDto resultResDto) throws BaseException {
//      log.debug("# ######################################");
//      log.debug("# ExhibitManageService.selectExhibitInfoGift Start");
//      log.debug("# ######################################");
//
//      // ======================================================================
//      // # 초기화
//      // ======================================================================
//      // 기획전-증정행사
//      ExhibitGiftVo               resultGiftInfo              = null;
//      List<ExhibitGiftGoodsVo>    resultGiftGoodsList         = null;
//      List<ExhibitGiftGoodsVo>    resultGgiftTargetGoodsList  = null;
//      List<ExhibitGiftGoodsVo>    resultGiftTargetBrandList   = null;
//
//      // ======================================================================
//      // # 처리
//      // ======================================================================
//
//      // ----------------------------------------------------------------------
//      // 1. 증정행사 조회
//      // ----------------------------------------------------------------------
//      resultGiftInfo = exhibitManageMapper.selectExhibitGiftInfo(resultResDto.getDetail().getEvExhibitId());
//
//      if (resultGiftInfo == null || StringUtil.isEmpty(resultGiftInfo.getEvExhibitGiftId()) || StringUtil.isEquals(resultGiftInfo.getEvExhibitGiftId(), "0")) {
//        log.debug("# 증정행사 정보가 없습니다.");
//        resultResDto.setResultCode(ExhibitMessage.EXHIBIT_MANAGE_GIFT_NO_DATA.getCode());
//        resultResDto.setResultMessage(ExhibitMessage.EXHIBIT_MANAGE_GIFT_NO_DATA.getMessage());
//        return;
//      }
//
//      // ----------------------------------------------------------------------
//      // 2. 상품/브랜드 리스트 조회
//      // ----------------------------------------------------------------------
//      // --------------------------------------------------------------------
//      // 2.1. 증정상품 리스트조회
//      // --------------------------------------------------------------------
// // TODO 요기까지 했네요
// // 여기서부터 XML 쿼리작업부터 하세요
//      resultGiftGoodsList = exhibitManageMapper.selectExhibitGiftGoodsList(resultGiftInfo.getEvExhibitGiftId());
//      resultGiftInfo.setGiftGoodsList(resultGiftGoodsList);
//
//      // --------------------------------------------------------------------
//      // 2.2. 적용상품 리스트조회
//      // --------------------------------------------------------------------
//      resultGgiftTargetGoodsList = exhibitManageMapper.selectExhibitGiftTargetGoodsList(resultGiftInfo.getEvExhibitGiftId());
//      resultGiftInfo.setGiftTargetGoodsList(resultGgiftTargetGoodsList);
//
//      // --------------------------------------------------------------------
//      // 2.3. 적용브랜드 리스트조회
//      // --------------------------------------------------------------------
//      resultGiftTargetBrandList =  exhibitManageMapper.selectExhibitGiftTargetBrandList(resultGiftInfo.getEvExhibitGiftId());
//      resultGiftInfo.setGiftTargetBrandList(resultGiftTargetBrandList);
//
//      // ----------------------------------------------------------------------
//      // 3. 기획전상세.증정행사정보 Set
//      // ----------------------------------------------------------------------
//      resultResDto.getDetail().setGiftExhibitInfo(resultGiftInfo);
//
//      // ======================================================================
//      // # 반환
//      // ======================================================================
//    }

  	/**
  	 * 기획전 상태이력 등록
  	 * @param ApprovalStatusVo
  	 * @return int
  	 */
  	protected int addExhibitStatusHistory(ApprovalStatusVo history){
  		return exhibitManageMapper.addExhibitStatusHistory(history);
  	}
  	/**
	 * 기획전 승인 목록 조회
	 *
	 * @param ApprovalExhibitRequestDto
	 * @return ExhibitApprovalResponseDto
	 */
    protected ExhibitApprovalResponseDto getApprovalExhibitList(ApprovalExhibitRequestDto requestDto) {
    	ExhibitApprovalResponseDto result = new ExhibitApprovalResponseDto();
    	ArrayList<String> approvalStatusArray = null;
    	if(!StringUtil.isEmpty(requestDto.getSearchApprovalStatus())) {
    		approvalStatusArray = StringUtil.getArrayList(requestDto.getSearchApprovalStatus().replace(" ", ""));
	  		requestDto.setApprovalStatusArray(approvalStatusArray);
    	}
    	PageMethod.startPage(requestDto.getPage(), requestDto.getPageSize());
    	Page<ExhibitApprovalResultVo> rows = exhibitManageMapper.getApprovalExhibitList(requestDto);
    	result.setTotal((int)rows.getTotal());
      	result.setRows(rows.getResult());
      	return result;
    }


    /**
     * 기획전 승인 요청철회
     *
     * @param ApprovalStatusVo
     * @return MessageCommEnum
     */
    protected MessageCommEnum putCancelRequestApprovalExhibit(ApprovalStatusVo approvalVo) throws Exception {
		if(exhibitManageMapper.putCancelRequestApprovalExhibit(approvalVo) > 0
			&& this.addExhibitStatusHistory(approvalVo) > 0 ) {
			return BaseEnums.Default.SUCCESS;
		}else {
			throw new BaseException(BaseEnums.CommBase.PROGRAM_ERROR);
		}
    }
    /**
     * 기획전 승인처리
     *
     * @param ApprovalStatusVo
     * @return MessageCommEnum
     */
    protected MessageCommEnum putApprovalProcessExhibit(ApprovalStatusVo approvalVo) throws Exception {
		if(exhibitManageMapper.putApprovalProcessExhibit(approvalVo) > 0
			&& this.addExhibitStatusHistory(approvalVo) > 0 ) {
			return BaseEnums.Default.SUCCESS;
		}else {
			throw new BaseException(BaseEnums.CommBase.PROGRAM_ERROR);
		}
    }

    /**
     * 기획전 승인 폐기처리
     *
     * @param ApprovalStatusVo
     * @return MessageCommEnum
     */
    protected MessageCommEnum putDisposalApprovalExhibit(ApprovalStatusVo approvalVo) throws Exception {
		if(exhibitManageMapper.putDisposalApprovalExhibit(approvalVo) > 0
			&& this.addExhibitStatusHistory(approvalVo) > 0 ) {
			return BaseEnums.Default.SUCCESS;
		}else {
			throw new BaseException(BaseEnums.CommBase.PROGRAM_ERROR);
		}
    }

    /**
   * 기획전 승인 요청
   *
   * @param exhibitVo ExhibitVo
   * @param approvalVo ApprovalStatusVo
   * @return MessageCommEnum
   * @throws Exception Exception
   */
  protected MessageCommEnum putApprovalRequestExhibit(ExhibitVo exhibitVo, ApprovalStatusVo approvalVo) throws Exception {
    if(exhibitManageMapper.putApprovalRequestExhibit(exhibitVo) > 0
            && this.addExhibitStatusHistory(approvalVo) > 0 ) {
      return BaseEnums.Default.SUCCESS;
    }else {
      throw new BaseException(BaseEnums.CommBase.PROGRAM_ERROR);
    }
  }

  /**
   * 기획전 상세조회 - 기본
   * @param exhibitManageRequestDto
   * @return
   * @throws BaseException
   */
  @UserMaskingRun(system = "BOS")
  protected ExhibitManageResponseDto selectExhibitDetlInfo (ExhibitManageRequestDto exhibitManageRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# ExhibitManageService.selectExhibitInfo Start");
    log.debug("# ######################################");
    log.debug("# In.evExhibitId :: " + exhibitManageRequestDto.getEvExhibitId());

    // ======================================================================
    // # 초기화
    // ======================================================================
    ExhibitManageResponseDto resultResDto = new ExhibitManageResponseDto();
    resultResDto.setResultCode(ExhibitMessage.EXHIBIT_MANAGE_SUCCESS.getCode());
    resultResDto.setResultMessage(ExhibitMessage.EXHIBIT_MANAGE_SUCCESS.getMessage());
    // 기획전-기본정보
    ExhibitVo resultDetailInfo = null;

    // ======================================================================
    // # 처리
    // ======================================================================

    // ----------------------------------------------------------------------
    // 1.기본정보조회
    // ----------------------------------------------------------------------
    resultDetailInfo = exhibitManageMapper.selectExhibitInfo(exhibitManageRequestDto.getEvExhibitId());

    List<ExhibitGroupVo> resultGroupList  = exhibitManageMapper.selectfExhibitGroupList(resultDetailInfo.getEvExhibitId());
    if(resultGroupList != null && resultGroupList.size() > 0) {
      for(ExhibitGroupVo exhibitGroupVo: resultGroupList) {
        List<ExhibitGroupDetlVo> resultGroupGoodsList = exhibitManageMapper.selectfExhibitGroupGoodsList(exhibitGroupVo.getEvExhibitGroupId());
        exhibitGroupVo.setGroupGoodsList( resultGroupGoodsList );
      }
      resultDetailInfo.setGroupInfoList( resultGroupList );
    }

    resultResDto.setDetail(resultDetailInfo);
    if (resultDetailInfo == null || StringUtil.isEmpty(resultDetailInfo.getEvExhibitId()) || StringUtil.isEquals(resultDetailInfo.getEvExhibitId(), "0")) {
      log.debug("# 기획전 기본정보가 없습니다.");
      resultResDto.setResultCode(ExhibitMessage.EXHIBIT_MANAGE_DETAIL_NO_DATA.getCode());
      resultResDto.setResultMessage(ExhibitMessage.EXHIBIT_MANAGE_DETAIL_NO_DATA.getMessage());
      return resultResDto;
    }
    resultResDto.setTotal(1);

    List<EvUserGroupVo> userGroupList = exhibitManageMapper.getExhibitUserGroup(exhibitManageRequestDto.getEvExhibitId());
    resultResDto.getDetail().setUserGroupList(userGroupList);


    // ======================================================================
    // # 처리
    // ======================================================================


    //// ----------------------------------------------------------------------
    //// 2.기획전유형별 상세조회
    //// ----------------------------------------------------------------------
    //if (StringUtil.isEquals(resultDetailInfo.getExhibitTp(), ExhibitTp.NORMAL.getCode())) {
    //  // 일반기획전조회
    //  this.selectExhibitInfoNormal(resultResDto);
    //
    //}
    //else if (StringUtil.isEquals(resultDetailInfo.getExhibitTp(), ExhibitTp.SELECT.getCode())) {
    //  // 골라담기(균일가)
    //  this.selectExhibitInfoSelect(resultResDto);
    //
    //}
    //else if (StringUtil.isEquals(resultDetailInfo.getExhibitTp(), ExhibitTp.GIFT.getCode())) {
    //  // 증정행사
    //  this.selectExhibitInfoGift(resultResDto);
    //}

    // ======================================================================
    // # 반환
    // ======================================================================
    return resultResDto;
  }

  /**
   * 골라담기 최대할인율을 구하기위한 쿼리
   * @param exExhibitId
   * @return
   * @throws BaseException
   */
  protected int getExhibitSelectGoodsListForMaxRate (String exExhibitId, int selPrice) throws BaseException {

    int returnRate = 0;   // 할인율
    int addCnt = 0;       // 구매누적수량
    int addPrice = 0;     // 최대선택한 금액의 총합
    // selPrice           // 골라담기 고정판매가

    List<ExhibitSelectVo> list = exhibitManageMapper.getExhibitSelectGoodsListForMaxRate(exExhibitId);

    for(ExhibitSelectVo row : list) {
      if(row.getGoodsBuyLimitCnt() == 0) {
        // 상품구매한도제한이 없을경우
        // 가장 비싼정상가에 최대구매수량을 곱해서 계산
        addPrice = row.getRecommendedPrice() * row.getDefaultBuyCnt();
        break;
      } else {
        // 상품구매한도제한이 있을경우
        if((addCnt + row.getGoodsBuyLimitCnt()) > row.getDefaultBuyCnt()) {
          // 누적상품구매갯수 한도 초과시
          addPrice += row.getRecommendedPrice() * (row.getDefaultBuyCnt()-addCnt);
          break;
        } else {
          // 누적상품구매갯수 한도 미초과시
          addPrice += row.getRecommendedPrice() * row.getGoodsBuyLimitCnt();
        }
      }
      // 구매총수량 누적
      addCnt += row.getGoodsBuyLimitCnt();
    }

    // System.out.println("정상가 최대금액 : " + addPrice);
    // System.out.println("골라담기 금액 : " + selPrice);

    if(addPrice > 0 && addPrice > selPrice) {
      returnRate = Math.round(((float)(addPrice - selPrice))/(float)addPrice * 100);
    } else {
      returnRate = 0;
    }

    // System.out.println("할인율 : " + returnRate);

    return returnRate;
  }
}
