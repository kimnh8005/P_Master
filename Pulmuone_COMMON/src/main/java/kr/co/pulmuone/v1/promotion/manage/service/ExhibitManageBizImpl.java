package kr.co.pulmuone.v1.promotion.manage.service;

import java.util.List;

import kr.co.pulmuone.v1.comm.aop.service.UserMaskingRun;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.approval.auth.dto.ApprovalExhibitRequestDto;
import kr.co.pulmuone.v1.approval.auth.dto.vo.ApprovalStatusVo;
import kr.co.pulmuone.v1.approval.auth.service.ApprovalAuthBiz;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.ApprovalEnums;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.ExhibitEnums;
import kr.co.pulmuone.v1.comm.enums.MessageCommEnum;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.promotion.manage.dto.ExhibitManageRequestDto;
import kr.co.pulmuone.v1.promotion.manage.dto.ExhibitManageResponseDto;
import kr.co.pulmuone.v1.promotion.manage.dto.vo.ExhibitVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
* <PRE>
* Forbiz Korea
* 기획전관리 COMMON Impl
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
public class ExhibitManageBizImpl implements ExhibitManageBiz {

  @Autowired
  private ExhibitManageService exhibitManageService;

  @Autowired
  private ApprovalAuthBiz approvalAuthBiz;

  //@Autowired
  //private SystemBasicEnvironmentBiz systemBasicEnvironmentBiz;
  //호스트
  //private String hostUrl; // public 저장소 접근 url

  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // @ 조회
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  /**
   * 기획전 리스트조회
   */
  @UserMaskingRun(system = "MUST_MASKING")  // 강제 마스킹
  //@UserMaskingRun(system = "BOS")
  @Override
  public ApiResult<?> selectExhibitList (ExhibitManageRequestDto exhibitManageRequestDto) throws BaseException {
    log.info("# ######################################");
    log.info("# ExhibitManageBizImpl.selectExhibitList Start");
    log.info("# ######################################");
    if (exhibitManageRequestDto != null) {
      log.info("# In.exhibitManageRequestDto :: " + exhibitManageRequestDto.toString());
    }
    else {
      log.info("# In.exhibitManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    ExhibitManageResponseDto result = new ExhibitManageResponseDto();

    // ========================================================================
    // # 처리
    // ========================================================================
    Page<ExhibitVo> voList = exhibitManageService.selectExhibitList(exhibitManageRequestDto);
    result.setTotal(voList.getTotal());
    List<ExhibitVo> responseList = voList.getResult();
    //for (ExhibitVo vo : responseList ) {
    //  vo.setUserGroupList(exhibitManageService.getExhibitUserGroup(vo.getEvExhibitId()));
    //}
    result.setRows(responseList);

    // ========================================================================
    // # 반환
    // ========================================================================
    return ApiResult.success(result);
  }

  /**
   * 기획전 담당자리스트(조회조건 콤보용)
   */
  @UserMaskingRun(system = "MUST_MASKING")  // 테스트 시 사용하세요.
  //@UserMaskingRun(system = "BOS")
  @Override
  public ApiResult<?> selectExhibitManagerList (ExhibitManageRequestDto exhibitManageRequestDto) throws BaseException {
    log.info("# ######################################");
    log.info("# ExhibitManageBizImpl.selectExhibitManagerList Start");
    log.info("# ######################################");
    if (exhibitManageRequestDto != null) {
      log.info("# In.exhibitManageRequestDto :: " + exhibitManageRequestDto.toString());
    }
    else {
      log.info("# In.exhibitManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    ExhibitManageResponseDto result = null;

    // ========================================================================
    // # 처리
    // ========================================================================
    result = exhibitManageService.selectExhibitManagerList(exhibitManageRequestDto);

    // ========================================================================
    // # 반환
    // ========================================================================
    return ApiResult.success(result);
  }

  /**
   * 기획전 상세조회 - 기본정보
   */
  @Override
  public ApiResult<?> selectExhibitInfo (ExhibitManageRequestDto exhibitManageRequestDto) throws BaseException {
    log.info("# ######################################");
    log.info("# ExhibitManageBizImpl.selectExhibitInfo Start");
    log.info("# ######################################");
    if (exhibitManageRequestDto != null) {
      log.info("# In.exhibitManageRequestDto :: " + exhibitManageRequestDto.toString());
    }
    else {
      log.info("# In.exhibitManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    ExhibitManageResponseDto result = null;

    // ========================================================================
    // # 처리
    // ========================================================================
    result = exhibitManageService.selectExhibitInfo(exhibitManageRequestDto);

    // ========================================================================
    // # 반환
    // ========================================================================
    return ApiResult.success(result);
  }

  // **************************************************************************
  // 일반(그룹)
  // **************************************************************************
  /**
   * 기획전 상세조회 - 일반(그룹) - 그룹리스트
   */
  @Override
  public ApiResult<?> selectfExhibitGroupList (ExhibitManageRequestDto exhibitManageRequestDto) throws BaseException {
    log.info("# ######################################");
    log.info("# ExhibitManageBizImpl.selectfExhibitGroupList Start");
    log.info("# ######################################");
    if (exhibitManageRequestDto != null) {
      log.info("# In.exhibitManageRequestDto :: " + exhibitManageRequestDto.toString());
    }
    else {
      log.info("# In.exhibitManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    ExhibitManageResponseDto result = null;

    // ========================================================================
    // # 처리
    // ========================================================================
    result = exhibitManageService.selectfExhibitGroupList(exhibitManageRequestDto);

    // ========================================================================
    // # 반환
    // ========================================================================
    return ApiResult.success(result);
  }

  /**
   * 기획전 상세조회 - 일반(그룹) - 그룹상품리스트
   */
  @Override
  public ApiResult<?> selectfExhibitGroupGoodsList (ExhibitManageRequestDto exhibitManageRequestDto) throws BaseException {
    log.info("# ######################################");
    log.info("# ExhibitManageBizImpl.selectfExhibitGroupGoodsList Start");
    log.info("# ######################################");
    if (exhibitManageRequestDto != null) {
      log.info("# In.exhibitManageRequestDto :: " + exhibitManageRequestDto.toString());
    }
    else {
      log.info("# In.exhibitManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    ExhibitManageResponseDto result = null;

    // ========================================================================
    // # 처리
    // ========================================================================
    result = exhibitManageService.selectfExhibitGroupGoodsList(exhibitManageRequestDto);

    // ========================================================================
    // # 반환
    // ========================================================================
    return ApiResult.success(result);
  }

  // **************************************************************************
  // 골라담기
  // **************************************************************************
  /**
   * 기획전 상세조회 - 골라담기 - 기본정보
   */
  @Override
  public ApiResult<?> selectfExhibitSelectInfo (ExhibitManageRequestDto exhibitManageRequestDto) throws BaseException {
    log.info("# ######################################");
    log.info("# ExhibitManageBizImpl.selectfExhibitSelectInfo Start");
    log.info("# ######################################");
    if (exhibitManageRequestDto != null) {
      log.info("# In.exhibitManageRequestDto :: " + exhibitManageRequestDto.toString());
    }
    else {
      log.info("# In.exhibitManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    ExhibitManageResponseDto result = null;

    // ========================================================================
    // # 처리
    // ========================================================================
    result = exhibitManageService.selectfExhibitSelectInfo(exhibitManageRequestDto);

    // ========================================================================
    // # 반환
    // ========================================================================
    return ApiResult.success(result);
  }

  /**
   * 기획전 상세조회 - 골라담기 - 상품리스트
   */
  @Override
  public ApiResult<?> selectExhibitSelectGoodsList (ExhibitManageRequestDto exhibitManageRequestDto) throws BaseException {
    log.info("# ######################################");
    log.info("# ExhibitManageBizImpl.selectExhibitSelectGoodsList Start");
    log.info("# ######################################");
    if (exhibitManageRequestDto != null) {
      log.info("# In.exhibitManageRequestDto :: " + exhibitManageRequestDto.toString());
    }
    else {
      log.info("# In.exhibitManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    ExhibitManageResponseDto result = null;

    // ========================================================================
    // # 처리
    // ========================================================================
    result = exhibitManageService.selectExhibitSelectGoodsList(exhibitManageRequestDto);

    // ========================================================================
    // # 반환
    // ========================================================================
    return ApiResult.success(result);
  }

  /**
   * 기획전 상세조회 - 골라담기 - 추가상품리스트
   */
  @Override
  public ApiResult<?> selectExhibitSelectAddGoodsList (ExhibitManageRequestDto exhibitManageRequestDto) throws BaseException {
    log.info("# ######################################");
    log.info("# ExhibitManageBizImpl.selectExhibitSelectAddGoodsList Start");
    log.info("# ######################################");
    if (exhibitManageRequestDto != null) {
      log.info("# In.exhibitManageRequestDto :: " + exhibitManageRequestDto.toString());
    }
    else {
      log.info("# In.exhibitManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    ExhibitManageResponseDto result = null;

    // ========================================================================
    // # 처리
    // ========================================================================
    result = exhibitManageService.selectExhibitSelectAddGoodsList(exhibitManageRequestDto);

    // ========================================================================
    // # 반환
    // ========================================================================
    return ApiResult.success(result);
  }

  // **************************************************************************
  // 증정행사
  // **************************************************************************
  /**
   * 기획전 상세조회 - 증정행사 - 기본정보
   */
  @Override
  public ApiResult<?> selectExhibitGiftInfo (ExhibitManageRequestDto exhibitManageRequestDto) throws BaseException {
    log.info("# ######################################");
    log.info("# ExhibitManageBizImpl.selectExhibitGiftInfo Start");
    log.info("# ######################################");
    if (exhibitManageRequestDto != null) {
      log.info("# In.exhibitManageRequestDto :: " + exhibitManageRequestDto.toString());
    }
    else {
      log.info("# In.exhibitManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    ExhibitManageResponseDto result = null;

    // ========================================================================
    // # 처리
    // ========================================================================
    result = exhibitManageService.selectExhibitGiftInfo(exhibitManageRequestDto);

    // ========================================================================
    // # 반환
    // ========================================================================
    return ApiResult.success(result);
  }

  /**
   * 기획전 상세조회 - 증정행사 - 증정상품리스트
   */
  @Override
  public ApiResult<?> selectExhibitGiftGoodsList (ExhibitManageRequestDto exhibitManageRequestDto) throws BaseException {
    log.info("# ######################################");
    log.info("# ExhibitManageBizImpl.selectExhibitGiftGoodsList Start");
    log.info("# ######################################");
    if (exhibitManageRequestDto != null) {
      log.info("# In.exhibitManageRequestDto :: " + exhibitManageRequestDto.toString());
    }
    else {
      log.info("# In.exhibitManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    ExhibitManageResponseDto result = null;

    // ========================================================================
    // # 처리
    // ========================================================================
    result = exhibitManageService.selectExhibitGiftGoodsList(exhibitManageRequestDto);

    // ========================================================================
    // # 반환
    // ========================================================================
    return ApiResult.success(result);
  }

  /**
   * 기획전 상세조회 - 증정행사 - 적용상품리스트
   */
  @Override
  public ApiResult<?> selectExhibitGiftTargetGoodsList (ExhibitManageRequestDto exhibitManageRequestDto) throws BaseException {
    log.info("# ######################################");
    log.info("# ExhibitManageBizImpl.selectExhibitGiftTargetGoodsList Start");
    log.info("# ######################################");
    if (exhibitManageRequestDto != null) {
      log.info("# In.exhibitManageRequestDto :: " + exhibitManageRequestDto.toString());
    }
    else {
      log.info("# In.exhibitManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    ExhibitManageResponseDto result = null;

    // ========================================================================
    // # 처리
    // ========================================================================
    result = exhibitManageService.selectExhibitGiftTargetGoodsList(exhibitManageRequestDto);

    // ========================================================================
    // # 반환
    // ========================================================================
    return ApiResult.success(result);
  }

  /**
   * 기획전 상세조회 - 증정행사 - 적용브랜드리스트
   */
  @Override
  public ApiResult<?> selectExhibitGiftTargetBrandList (ExhibitManageRequestDto exhibitManageRequestDto) throws BaseException {
    log.info("# ######################################");
    log.info("# ExhibitManageBizImpl.selectExhibitGiftTargetBrandList Start");
    log.info("# ######################################");
    if (exhibitManageRequestDto != null) {
      log.info("# In.exhibitManageRequestDto :: " + exhibitManageRequestDto.toString());
    }
    else {
      log.info("# In.exhibitManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    ExhibitManageResponseDto result = null;

    // ========================================================================
    // # 처리
    // ========================================================================
    result = exhibitManageService.selectExhibitGiftTargetBrandList(exhibitManageRequestDto);

    // ========================================================================
    // # 반환
    // ========================================================================
    return ApiResult.success(result);
  }

  /**
   * 상품정보리스트(엑셀용)
   */
  @Override
  public ApiResult<?> selectGoodsInfoList (ExhibitManageRequestDto exhibitManageRequestDto) throws BaseException {
    log.info("# ######################################");
    log.info("# ExhibitManageBizImpl.selectGoodsInfoList Start");
    log.info("# ######################################");
    if (exhibitManageRequestDto != null) {
      log.info("# In.exhibitManageRequestDto :: " + exhibitManageRequestDto.toString());
    }
    else {
      log.info("# In.exhibitManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    ExhibitManageResponseDto result = null;

    // ========================================================================
    // # 처리
    // ========================================================================
    result = exhibitManageService.selectGoodsInfoList(exhibitManageRequestDto);

    // ========================================================================
    // # 반환
    // ========================================================================
    return ApiResult.success(result);
  }

  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // @ 삭제
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  /**
   * 기획전 삭제
   * @param exhibitManageRequestDto
   * @return
   * @throws BaseException
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
  public ApiResult<?> delExhibit (ExhibitManageRequestDto exhibitManageRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# ExhibitManageBizImpl.delExhibit Start");
    log.debug("# ######################################");
    if (exhibitManageRequestDto != null) {
      log.debug("# In.exhibitManageRequestDto :: " + exhibitManageRequestDto.toString());
    }
    else {
      log.debug("# In.exhibitManageRequestDto is Null");
    }

    // Controller에서 List<String> 형태로 Set 한다
    ExhibitManageResponseDto result = exhibitManageService.delExhibit(exhibitManageRequestDto.getEvExhibitIdList());

    return ApiResult.success(result);
  }

  /**
   * 그룹상세 삭제(그룹상품)
   * @param exhibitManageRequestDto
   * @return
   * @throws BaseException
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
  public ApiResult<?> delExhibitGroupDetl (ExhibitManageRequestDto exhibitManageRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# ExhibitManageBizImpl.delExhibitGroupDetl Start");
    log.debug("# ######################################");
    if (exhibitManageRequestDto != null) {
      log.debug("# In.exhibitManageRequestDto :: " + exhibitManageRequestDto.toString());
    }
    else {
      log.debug("# In.exhibitManageRequestDto is Null");
    }

    // Controller에서 List<String> 형태로 Set 한다
    ExhibitManageResponseDto result = exhibitManageService.delExhibitGroupDetl(exhibitManageRequestDto.getEvExhibitGroupDetlIdList());

    return ApiResult.success(result);
  }

  /**
   * 골라담기 상품 삭제
   * @param exhibitManageRequestDto
   * @return
   * @throws BaseException
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
  public ApiResult<?> delExhibitSelectGoods (ExhibitManageRequestDto exhibitManageRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# ExhibitManageBizImpl.delExhibitSelectGoods Start");
    log.debug("# ######################################");
    if (exhibitManageRequestDto != null) {
      log.debug("# In.exhibitManageRequestDto :: " + exhibitManageRequestDto.toString());
    }
    else {
      log.debug("# In.exhibitManageRequestDto is Null");
    }

    // Controller에서 List<String> 형태로 Set 한다
    ExhibitManageResponseDto result = exhibitManageService.delExhibitSelectGoods(exhibitManageRequestDto.getEvExhibitSelectGoodsIdList());

    return ApiResult.success(result);
  }

  /**
   * 골라담기 추가상품 삭제
   * @param exhibitManageRequestDto
   * @return
   * @throws BaseException
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
  public ApiResult<?> delExhibitSelectAddGoods (ExhibitManageRequestDto exhibitManageRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# ExhibitManageBizImpl.delExhibitSelectAddGoods Start");
    log.debug("# ######################################");
    if (exhibitManageRequestDto != null) {
      log.debug("# In.exhibitManageRequestDto :: " + exhibitManageRequestDto.toString());
    }
    else {
      log.debug("# In.exhibitManageRequestDto is Null");
    }

    // Controller에서 List<String> 형태로 Set 한다
    ExhibitManageResponseDto result = exhibitManageService.delExhibitSelectAddGoods(exhibitManageRequestDto.getEvExhibitSelectAddGoodsIdList());

    return ApiResult.success(result);
  }

  /**
   * 증정행사 상품 삭제
   * @param exhibitManageRequestDto
   * @return
   * @throws BaseException
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
  public ApiResult<?> delExhibitGiftGoods (ExhibitManageRequestDto exhibitManageRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# ExhibitManageBizImpl.delExhibitGiftGoods Start");
    log.debug("# ######################################");
    if (exhibitManageRequestDto != null) {
      log.debug("# In.exhibitManageRequestDto :: " + exhibitManageRequestDto.toString());
    }
    else {
      log.debug("# In.exhibitManageRequestDto is Null");
    }

    // Controller에서 List<String> 형태로 Set 한다
    ExhibitManageResponseDto result = exhibitManageService.delExhibitGiftGoods(exhibitManageRequestDto.getEvExhibitGiftGoodsIdList());

    return ApiResult.success(result);
  }

  /**
   * 증정행사 대상상품 삭제
   * @param exhibitManageRequestDto
   * @return
   * @throws BaseException
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
  public ApiResult<?> delExhibitGiftTargetGoods (ExhibitManageRequestDto exhibitManageRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# ExhibitManageBizImpl.delExhibitGiftTargetGoods Start");
    log.debug("# ######################################");
    if (exhibitManageRequestDto != null) {
      log.debug("# In.exhibitManageRequestDto :: " + exhibitManageRequestDto.toString());
    }
    else {
      log.debug("# In.exhibitManageRequestDto is Null");
    }

    // Controller에서 List<String> 형태로 Set 한다
    ExhibitManageResponseDto result = exhibitManageService.delExhibitGiftTargetGoods(exhibitManageRequestDto.getEvExhibitGiftTargetGoodsIdList());

    return ApiResult.success(result);
  }

  /**
   * 증정행사 대상브랜드 삭제
   * @param exhibitManageRequestDto
   * @return
   * @throws BaseException
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
  public ApiResult<?> delExhibitGiftTargetBrand (ExhibitManageRequestDto exhibitManageRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# ExhibitManageBizImpl.delExhibitGiftTargetBrand Start");
    log.debug("# ######################################");
    if (exhibitManageRequestDto != null) {
      log.debug("# In.exhibitManageRequestDto :: " + exhibitManageRequestDto.toString());
    }
    else {
      log.debug("# In.exhibitManageRequestDto is Null");
    }

    // Controller에서 List<String> 형태로 Set 한다
    ExhibitManageResponseDto result = exhibitManageService.delExhibitGiftTargetBrand(exhibitManageRequestDto.getEvExhibitGiftTargetBrandIdList());

    return ApiResult.success(result);
  }



  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // @ 등록
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  /**
   * 기획전 등록
   * @param exhibitManageRequestDto
   * @return
   * @throws BaseException
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
  public ApiResult<?> addExhibit (ExhibitManageRequestDto exhibitManageRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# ExhibitManageBizImpl.addExhibit Start");
    log.debug("# ######################################");
    if (exhibitManageRequestDto != null) {
      log.debug("# In.exhibitManageRequestDto :: " + exhibitManageRequestDto.toString());
    }
    else {
      log.debug("# In.exhibitManageRequestDto is Null");
    }

    // ------------------------------------------------------------------------
    // 1. 기획전 기본정보 등록
    // ------------------------------------------------------------------------
    ExhibitManageResponseDto result = exhibitManageService.addExhibit(exhibitManageRequestDto);

    return ApiResult.success(result);
  }


  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // @ 수정
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  /**
   * 기획전 수정
   * @param exhibitManageRequestDto
   * @return
   * @throws BaseException
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
  public ApiResult<?> putExhibit (ExhibitManageRequestDto exhibitManageRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# ExhibitManageBizImpl.putExhibit Start");
    log.debug("# ######################################");
    if (exhibitManageRequestDto != null) {
      log.debug("# In.exhibitManageRequestDto :: " + exhibitManageRequestDto.toString());
    }
    else {
      log.debug("# In.exhibitManageRequestDto is Null");
    }

    // ------------------------------------------------------------------------
    // 1. 기획전 기본정보 등록
    // ------------------------------------------------------------------------
    ExhibitManageResponseDto result = exhibitManageService.putExhibit(exhibitManageRequestDto);

    return ApiResult.success(result);
  }

  /**
   * 증정행사 대표상품 변경
   * @param exhibitManageRequestDto
   * @return
   * @throws BaseException
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
  public ApiResult<?> putExhibitGiftRepGoods (ExhibitManageRequestDto exhibitManageRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# ExhibitManageBizImpl.putExhibitGiftRepGoods Start");
    log.debug("# ######################################");
    if (exhibitManageRequestDto != null) {
      log.debug("# In.exhibitManageRequestDto :: " + exhibitManageRequestDto.toString());
    }
    else {
      log.debug("# In.exhibitManageRequestDto is Null");
    }

    // ------------------------------------------------------------------------
    // 증정행사 대표상품 변경
    // ------------------------------------------------------------------------
    ExhibitManageResponseDto result = exhibitManageService.putExhibitGiftRepGoods(exhibitManageRequestDto);

    return ApiResult.success(result);
  }


  	/**
	 * 기획전 승인 목록 조회
	 *
	 * @param ApprovalExhibitRequestDto
	 * @return ExhibitApprovalResponseDto
	 */
	@Override
	public ApiResult<?> getApprovalExhibitList(ApprovalExhibitRequestDto requestDto) {
		return ApiResult.success(exhibitManageService.getApprovalExhibitList(requestDto));
	}

	/**
     * 기획전 승인 요청철회
     *
     * @param ApprovalExhibitRequestDto
     * @return ApiResult
     * @throws 	Exception
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
    public ApiResult<?> putCancelRequestApprovalExhibit(ApprovalExhibitRequestDto requestDto) throws Exception {

    	String taskCode = "";
    	if(ExhibitEnums.ExhibitTp.SELECT.getCode().equals(requestDto.getExhibitTp())) {
    		taskCode = ApprovalEnums.ApprovalAuthType.APPR_KIND_TP_EXHIBIT_SELECT.getCode();
    	}else if(ExhibitEnums.ExhibitTp.GIFT.getCode().equals(requestDto.getExhibitTp())) {
    		taskCode = ApprovalEnums.ApprovalAuthType.APPR_KIND_TP_EXHIBIT_GIFT.getCode();
    	}

    	if(CollectionUtils.isNotEmpty(requestDto.getEvExhibitIdList())) {
    		for(String evExhibitId : requestDto.getEvExhibitIdList()) {
    			ApiResult<?> apiResult = approvalAuthBiz.checkCancelable(taskCode, evExhibitId);

    			if(apiResult.getCode().equals(ApprovalEnums.ApprovalValidation.CANCELABLE.getCode())) {
    				ApprovalStatusVo approvalVo = (ApprovalStatusVo)apiResult.getData();
        			MessageCommEnum emums = exhibitManageService.putCancelRequestApprovalExhibit(approvalVo);
        			if(!BaseEnums.Default.SUCCESS.equals(emums)) {
        				throw new BaseException(emums);
        			}
    			}else {
    				return apiResult;
    			}
    		}
    	}
    	else return ApiResult.fail();

    	return ApiResult.success();
    }

    /**
     * 기획전 승인처리
     *
     * @param ApprovalExhibitRequestDto
     * @return ApiResult
     * @throws 	Exception
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
    public ApiResult<?> putApprovalProcessExhibit(ApprovalExhibitRequestDto requestDto) throws Exception {

    	String reqApprStat = requestDto.getApprStat();
    	if(!ApprovalEnums.ApprovalStatus.DENIED.getCode().equals(reqApprStat)
    			&& !ApprovalEnums.ApprovalStatus.APPROVED.getCode().equals(reqApprStat)) {
    		return ApiResult.result(ApprovalEnums.ApprovalValidation.NONE_REQUEST);
    	}

    	String taskCode = "";
    	if(ExhibitEnums.ExhibitTp.SELECT.getCode().equals(requestDto.getExhibitTp())) {
    		taskCode = ApprovalEnums.ApprovalAuthType.APPR_KIND_TP_EXHIBIT_SELECT.getCode();
    	}else if(ExhibitEnums.ExhibitTp.GIFT.getCode().equals(requestDto.getExhibitTp())) {
    		taskCode = ApprovalEnums.ApprovalAuthType.APPR_KIND_TP_EXHIBIT_GIFT.getCode();
    	}

    	if(CollectionUtils.isNotEmpty(requestDto.getEvExhibitIdList())) {
    		for(String evExhibitId : requestDto.getEvExhibitIdList()) {
    			ApiResult<?> apiResult = approvalAuthBiz.checkApprovalProcess(taskCode, evExhibitId);

    			if(apiResult.getCode().equals(ApprovalEnums.ApprovalValidation.APPROVABLE.getCode())) {
    				ApprovalStatusVo approvalVo = (ApprovalStatusVo)apiResult.getData();
    				if(ApprovalEnums.ApprovalStatus.DENIED.getCode().equals(reqApprStat)) {
    					approvalVo.setApprStat(reqApprStat);
    					approvalVo.setStatusComment(requestDto.getStatusComment());
        			}
    				if(ApprovalEnums.ApprovalStatus.APPROVED.getCode().equals(reqApprStat)
    					&& ApprovalEnums.ApprovalStatus.APPROVED.getCode().equals(approvalVo.getApprStat())
        				) {
    					approvalVo.setMasterStat(ExhibitEnums.ExhibitStatus.APPROVED.getCode());
        			}
        			MessageCommEnum emums = exhibitManageService.putApprovalProcessExhibit(approvalVo);
        			if(!BaseEnums.Default.SUCCESS.equals(emums)) {
        				throw new BaseException(emums);
        			}
    			}else {
    				//스킵? 혹은 계속진행? 결정대기중
    				return apiResult;
    			}
    		}
    	}else return ApiResult.fail();
    	return ApiResult.success();
    }

	/**
	 * @Desc 기획전 승인 폐기처리
	 * @param approvalGoodsRequestDto
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
	public ApiResult<?> putDisposalApprovalExhibit(ApprovalExhibitRequestDto requestDto) throws Exception {

    	String taskCode = "";
    	if(ExhibitEnums.ExhibitTp.SELECT.getCode().equals(requestDto.getExhibitTp())) {
    		taskCode = ApprovalEnums.ApprovalAuthType.APPR_KIND_TP_EXHIBIT_SELECT.getCode();
    	}else if(ExhibitEnums.ExhibitTp.GIFT.getCode().equals(requestDto.getExhibitTp())) {
    		taskCode = ApprovalEnums.ApprovalAuthType.APPR_KIND_TP_EXHIBIT_GIFT.getCode();
    	}

    	if(CollectionUtils.isNotEmpty(requestDto.getEvExhibitIdList())) {
    		for(String evExhibitId : requestDto.getEvExhibitIdList()) {
    			ApiResult<?> apiResult = approvalAuthBiz.checkDisposable(taskCode, evExhibitId);

    			if(apiResult.getCode().equals(ApprovalEnums.ApprovalValidation.DISPOSABLE.getCode())) {
    				ApprovalStatusVo approvalVo = (ApprovalStatusVo)apiResult.getData();
        			MessageCommEnum emums = exhibitManageService.putDisposalApprovalExhibit(approvalVo);
        			if(!BaseEnums.Default.SUCCESS.equals(emums)) {
        				throw new BaseException(emums);
        			}
    			}else {
    				return apiResult;
    			}
    		}
    	}
    	else return ApiResult.fail();

		return ApiResult.success();
	}

	public ApiResult<?> putApprovalRequestExhibit(ExhibitVo exhibitVo) throws Exception {
		UserVo userVo = SessionUtil.getBosUserVO();
		if (userVo != null) {
		  exhibitVo.setModifyId(userVo.getUserId());
		}

		ApprovalStatusVo approvalVo = ApprovalStatusVo.builder()
		        .taskPk(exhibitVo.getEvExhibitId())
		        .prevMasterStat(ExhibitEnums.ExhibitStatus.SAVE.getCode())
		        .prevApprStat(exhibitVo.getApprovalStatus())
		        .masterStat(ExhibitEnums.ExhibitStatus.SAVE.getCode())
		        .apprStat(ApprovalEnums.ApprovalStatus.REQUEST.getCode())
		        .apprSubUserId(exhibitVo.getApprovalSubUserId())
		        .apprUserId(exhibitVo.getApprovalUserId())
		        .approvalRequestUserId(SessionUtil.getBosUserVO().getUserId())
		        .build();

		exhibitVo.setExhibitStatus(ExhibitEnums.ExhibitStatus.SAVE.getCode());
		exhibitVo.setApprovalStatus(ApprovalEnums.ApprovalStatus.REQUEST.getCode());
		exhibitManageService.putApprovalRequestExhibit(exhibitVo, approvalVo);
		return ApiResult.success();
	}

  /**
   * 기획전 상세조회 - 기본정보
   */
  @Override
  public ApiResult<?> selectExhibitDetlInfo (ExhibitManageRequestDto exhibitManageRequestDto) throws BaseException {
    log.info("# ######################################");
    log.info("# ExhibitManageBizImpl.selectExhibitInfo Start");
    log.info("# ######################################");
    if (exhibitManageRequestDto != null) {
      log.info("# In.exhibitManageRequestDto :: " + exhibitManageRequestDto.toString());
    }
    else {
      log.info("# In.exhibitManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    ExhibitManageResponseDto result = null;

    // ========================================================================
    // # 처리
    // ========================================================================
    result = exhibitManageService.selectExhibitDetlInfo(exhibitManageRequestDto);

    // ========================================================================
    // # 반환
    // ========================================================================
    return ApiResult.success(result);
  }

  /**
   * 골라담기 최대할인율을 구하기위한 쿼리
   * @param exExhibitId
   * @return
   * @throws BaseException
   */
  @Override
  public int getExhibitSelectGoodsListForMaxRate(String exExhibitId, int selectPrice) throws BaseException {
    return exhibitManageService.getExhibitSelectGoodsListForMaxRate(exExhibitId, selectPrice);
  }
}
