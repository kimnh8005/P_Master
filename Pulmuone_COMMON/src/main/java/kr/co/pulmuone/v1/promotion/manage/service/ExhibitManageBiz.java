package kr.co.pulmuone.v1.promotion.manage.service;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.approval.auth.dto.ApprovalExhibitRequestDto;
import kr.co.pulmuone.v1.approval.auth.dto.vo.ApprovalStatusVo;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.promotion.coupon.dto.CouponApprovalRequestDto;
import kr.co.pulmuone.v1.promotion.manage.dto.ExhibitManageRequestDto;
import kr.co.pulmuone.v1.promotion.manage.dto.vo.ExhibitApprovalResultVo;
import kr.co.pulmuone.v1.promotion.manage.dto.vo.ExhibitVo;

/**
* <PRE>
* Forbiz Korea
* 프로모션 기획전관리 COMMON Interface
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

public interface ExhibitManageBiz {

  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // @ 조회
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

  // ##########################################################################
  // 리스트조회
  // ##########################################################################
  /**
   * 기획전 리스트조회
   * @param exhibitManageRequestDto
   * @return
   * @throws BaseException
   */
  ApiResult<?> selectExhibitList (ExhibitManageRequestDto exhibitManageRequestDto) throws BaseException;

  /**
   * 기획전 담당자리스트(조회조건 콤보용)
   * @param exhibitManageRequestDto
   * @return
   * @throws BaseException
   */
  ApiResult<?> selectExhibitManagerList (ExhibitManageRequestDto exhibitManageRequestDto) throws BaseException;

  // ##########################################################################
  // 상세조회
  // ##########################################################################
  /**
   * 기획전 상세조회 - 기본정보
   * @param exhibitManageRequestDto
   * @return
   * @throws BaseException
   */
  ApiResult<?> selectExhibitInfo (ExhibitManageRequestDto exhibitManageRequestDto) throws BaseException;

  // **************************************************************************
  // 일반(그룹)
  // **************************************************************************
  /**
   * 기획전 상세조회 - 일반(그룹) - 그룹리스트
   * @param exhibitManageRequestDto
   * @return
   * @throws BaseException
   */
  public ApiResult<?> selectfExhibitGroupList (ExhibitManageRequestDto exhibitManageRequestDto) throws BaseException;

  /**
   * 기획전 상세조회 - 일반(그룹) - 그룹상품리스트
   * @param exhibitManageRequestDto
   * @return
   * @throws BaseException
   */
  public ApiResult<?> selectfExhibitGroupGoodsList (ExhibitManageRequestDto exhibitManageRequestDto) throws BaseException;

  // **************************************************************************
  // 골라담기
  // **************************************************************************
  /**
   * 기획전 상세조회 - 골라담기 - 기본정보
   * @param exhibitManageRequestDto
   * @return
   * @throws BaseException
   */
  public ApiResult<?> selectfExhibitSelectInfo (ExhibitManageRequestDto exhibitManageRequestDto) throws BaseException;

  /**
   * 기획전 상세조회 - 골라담기 - 상품리스트
   * @param exhibitManageRequestDto
   * @return
   * @throws BaseException
   */
  public ApiResult<?> selectExhibitSelectGoodsList (ExhibitManageRequestDto exhibitManageRequestDto) throws BaseException;

  /**
   * 기획전 상세조회 - 골라담기 - 추가상품리스트
   * @param exhibitManageRequestDto
   * @return
   * @throws BaseException
   */
  public ApiResult<?> selectExhibitSelectAddGoodsList (ExhibitManageRequestDto exhibitManageRequestDto) throws BaseException;

  // **************************************************************************
  // 증정행사
  // **************************************************************************
  /**
   * 기획전 상세조회 - 증정행사 - 기본정보
   * @param exhibitManageRequestDto
   * @return
   * @throws BaseException
   */
  public ApiResult<?> selectExhibitGiftInfo (ExhibitManageRequestDto exhibitManageRequestDto) throws BaseException;

  /**
   * 기획전 상세조회 - 증정행사 - 증정상품리스트
   * @param exhibitManageRequestDto
   * @return
   * @throws BaseException
   */
  public ApiResult<?> selectExhibitGiftGoodsList (ExhibitManageRequestDto exhibitManageRequestDto) throws BaseException;

  /**
   * 기획전 상세조회 - 증정행사 - 적용상품리스트
   * @param exhibitManageRequestDto
   * @return
   * @throws BaseException
   */
  public ApiResult<?> selectExhibitGiftTargetGoodsList (ExhibitManageRequestDto exhibitManageRequestDto) throws BaseException;

  /**
   * 기획전 상세조회 - 증정행사 - 적용브랜드리스트
   * @param exhibitManageRequestDto
   * @return
   * @throws BaseException
   */
  public ApiResult<?> selectExhibitGiftTargetBrandList (ExhibitManageRequestDto exhibitManageRequestDto) throws BaseException;

  /**
   * 상품정보리스트(엑셀용)
   * @param exhibitManageRequestDto
   * @return
   * @throws BaseException
   */
  public ApiResult<?> selectGoodsInfoList (ExhibitManageRequestDto exhibitManageRequestDto) throws BaseException;

  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // @ 삭제
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  /**
   * 기획전 삭제
   * @param exhibitManageRequestDto
   * @return
   * @throws BaseException
   */
  public ApiResult<?> delExhibit (ExhibitManageRequestDto exhibitManageRequestDto) throws BaseException;

  /**
   * 그룹상세 삭제(그룹상품)
   * @param exhibitManageRequestDto
   * @return
   * @throws BaseException
   */
  public ApiResult<?> delExhibitGroupDetl (ExhibitManageRequestDto exhibitManageRequestDto) throws BaseException;

  /**
   * 골라담기 상품 삭제
   * @param exhibitManageRequestDto
   * @return
   * @throws BaseException
   */
  public ApiResult<?> delExhibitSelectGoods (ExhibitManageRequestDto exhibitManageRequestDto) throws BaseException;

  /**
   * 골라담기 추가상품 삭제
   * @param exhibitManageRequestDto
   * @return
   * @throws BaseException
   */
  public ApiResult<?> delExhibitSelectAddGoods (ExhibitManageRequestDto exhibitManageRequestDto) throws BaseException;

  /**
   * 증정행사 상품 삭제
   * @param exhibitManageRequestDto
   * @return
   * @throws BaseException
   */
  public ApiResult<?> delExhibitGiftGoods (ExhibitManageRequestDto exhibitManageRequestDto) throws BaseException;

  /**
   * 증정행사 대상상품 삭제
   * @param exhibitManageRequestDto
   * @return
   * @throws BaseException
   */
  public ApiResult<?> delExhibitGiftTargetGoods (ExhibitManageRequestDto exhibitManageRequestDto) throws BaseException;

  /**
   * 증정행사 대상브랜드 삭제
   * @param exhibitManageRequestDto
   * @return
   * @throws BaseException
   */
  public ApiResult<?> delExhibitGiftTargetBrand (ExhibitManageRequestDto exhibitManageRequestDto) throws BaseException;

  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // @ 등록
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  /**
   * 기획전 등록
   * @param exhibitManageRequestDto
   * @return
   * @throws BaseException
   */
  public ApiResult<?> addExhibit (ExhibitManageRequestDto exhibitManageRequestDto) throws BaseException;






  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // @ 수정
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  /**
   * 기획전 수정
   * @param exhibitManageRequestDto
   * @return
   * @throws BaseException
   */
  public ApiResult<?> putExhibit (ExhibitManageRequestDto exhibitManageRequestDto) throws BaseException;

  /**
   * 증정행사 대표상품 변경
   * @param exhibitManageRequestDto
   * @return
   * @throws BaseException
   */
  public ApiResult<?> putExhibitGiftRepGoods (ExhibitManageRequestDto exhibitManageRequestDto) throws BaseException;

  public ApiResult<?> getApprovalExhibitList(ApprovalExhibitRequestDto approvalExhibitRequestDto);

  public ApiResult<?> putCancelRequestApprovalExhibit(ApprovalExhibitRequestDto approvalExhibitRequestDto) throws Exception;

  public ApiResult<?> putApprovalProcessExhibit(ApprovalExhibitRequestDto approvalExhibitRequestDto) throws Exception;

  public ApiResult<?> putDisposalApprovalExhibit(ApprovalExhibitRequestDto approvalExhibitRequestDto) throws Exception;

  ApiResult<?> putApprovalRequestExhibit(ExhibitVo exhibitVo) throws Exception;

  // ##########################################################################
  // 상세조회
  // ##########################################################################
  /**
   * 기획전 상세조회 - 기본정보
   * @param exhibitManageRequestDto
   * @return
   * @throws BaseException
   */
  ApiResult<?> selectExhibitDetlInfo (ExhibitManageRequestDto exhibitManageRequestDto) throws BaseException;

  /**
   * 골라담기 최대할인율을 구하기위한 쿼리
   * @param exExhibitId
   * @return
   * @throws BaseException
   */
  public int getExhibitSelectGoodsListForMaxRate(String exExhibitId, int selectPrice) throws BaseException;
}
