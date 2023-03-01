package kr.co.pulmuone.v1.promotion.manage.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.promotion.manage.dto.EventManageRequestDto;

/**
* <PRE>
* Forbiz Korea
* 프로모션 이벤트관리 COMMON Interface
*
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0       2021.01.12.              dgyoun         최초작성
* =======================================================================
* </PRE>
*/

public interface EventManageBiz {

  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // @ 조회 - 이벤트
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

  // ##########################################################################
  // 리스트조회
  // ##########################################################################
  /**
   * 이벤트 리스트조회
   * @param eventManageRequestDto
   * @return
   * @throws BaseException
   */
  ApiResult<?> selectEventList (EventManageRequestDto eventManageRequestDto) throws BaseException;

  /**
   * 이벤트 상세조회
   * @param eventManageRequestDto
   * @return
   * @throws BaseException
   */
  ApiResult<?> selectEventInfo (EventManageRequestDto eventManageRequestDto) throws BaseException;


  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // @ 조회 - 이벤트참여
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  /**
   * 이벤트참여 리스트조회
   * @param eventManageRequestDto
   * @return
   * @throws BaseException
   */
  ApiResult<?> selectEventJoinList (EventManageRequestDto eventManageRequestDto) throws BaseException;
  ApiResult<?> selectExcelEventJoinList (EventManageRequestDto eventManageRequestDto) throws BaseException;

  /**
   * 이벤트참여 직접입력 리스트조회
   * @param eventManageRequestDto
   * @return
   * @throws BaseException
   */
  ApiResult<?> selectEventJoinDirectJoinList (EventManageRequestDto eventManageRequestDto) throws BaseException;

  /**
   * 이벤트참여 설문항목 리스트조회
   * @param eventManageRequestDto
   * @return
   * @throws BaseException
   */
  ApiResult<?> selectEventJoinSurveyList (EventManageRequestDto eventManageRequestDto) throws BaseException;

  /**
   * 이벤트참여 설문항목참여 리스트조회
   * @param eventManageRequestDto
   * @return
   * @throws BaseException
   */
  public ApiResult<?> selectEventJoinSurveyItemJoinList (EventManageRequestDto eventManageRequestDto) throws BaseException;

  /**
   * 상품정보리스트(엑셀용)
   * @param eventManageRequestDto
   * @return
   * @throws BaseException
   */
  public ApiResult<?> selectGoodsInfoList (EventManageRequestDto eventManageRequestDto) throws BaseException;

  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // @ 삭제
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  /**
   * 이벤트 삭제
   * @param eventManageRequestDto
   * @return
   * @throws BaseException
   */
  public ApiResult<?> delEvent (EventManageRequestDto eventManageRequestDto) throws BaseException;


  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // @ 등록
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  /**
   * 이벤트 등록
   * @param eventManageRequestDto
   * @return
   * @throws BaseException
   */
  public ApiResult<?> addEvent (EventManageRequestDto eventManageRequestDto) throws BaseException;


  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // @ 수정
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  /**
   * 이벤트 수정
   * @param eventManageRequestDto
   * @return
   * @throws BaseException
   */
  public ApiResult<?> putEvent (EventManageRequestDto eventManageRequestDto) throws BaseException;

  /**
   * 이벤트 당첨자공지사항 수정
   * @param eventManageRequestDto
   * @return
   * @throws BaseException
   */
  public ApiResult<?> putEventWinnerNotice (EventManageRequestDto eventManageRequestDto) throws BaseException;

  /**
   * 그룹상세 삭제(그룹상품)
   * @param eventManageRequestDto
   * @return
   * @throws BaseException
   */
  public ApiResult<?> delEventGroupDetl (EventManageRequestDto eventManageRequestDto) throws BaseException;


  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // @ 수정 - 이벤트참여
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  /**
   * 당첨자 설정
   * @param eventManageRequestDto
   * @return
   * @throws BaseException
   */
  public ApiResult<?> putWinnerLottery (EventManageRequestDto eventManageRequestDto) throws BaseException;

  /**
   * 이벤트참여 댓글 차단/차단해제
   * @param eventManageRequestDto
   * @return
   * @throws BaseException
   */
  public ApiResult<?> putAdminSecretYn (EventManageRequestDto eventManageRequestDto) throws BaseException;

  // **************************************************************************
  // 일반(그룹)
  // **************************************************************************
  /**
   * 이벤트 상세조회 - 일반(그룹) - 그룹리스트
   * @param eventManageRequestDto
   * @return
   * @throws BaseException
   */
  public ApiResult<?> selectfEventGroupList (EventManageRequestDto eventManageRequestDto) throws BaseException;

  /**
   * 이벤트 상세조회 - 일반(그룹) - 그룹상품리스트
   * @param eventManageRequestDto
   * @return
   * @throws BaseException
   */
  public ApiResult<?> selectfEventGroupGoodsList (EventManageRequestDto eventManageRequestDto) throws BaseException;


}
