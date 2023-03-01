package kr.co.pulmuone.v1.display.manage.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.display.manage.dto.DisplayManageRequestDto;

/**
* <PRE>
* Forbiz Korea
* 전시관리 COMMON Interface
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

public interface DisplayManageBiz {

  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // 전시페이지관리
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  /*
   * 전시페이지 리스트조회
   */
  ApiResult<?> selectDpPageList (DisplayManageRequestDto displayManageRequestDto) throws BaseException;

  /*
   * 전시카테고리 리스트조회
   */
  ApiResult<?> selectDpCategoryList (DisplayManageRequestDto displayManageRequestDto) throws BaseException;

  /*
   * 페이지 상세조회
   */
  ApiResult<?> selectPageInfo (DisplayManageRequestDto displayManageRequestDto) throws BaseException;

  /*
   * 페이지 수정
   */
  ApiResult<?> putPage (DisplayManageRequestDto displayManageRequestDto) throws BaseException;

  /*
   * 페이지 순서변경
   */
  ApiResult<?> putPageSort (DisplayManageRequestDto displayManageRequestDto) throws BaseException;

  /*
   * 페이지 삭제
   */
  ApiResult<?> delPage (DisplayManageRequestDto displayManageRequestDto) throws BaseException;

  /*
   * 페이지 등록
   */
  ApiResult<?> addPage (DisplayManageRequestDto displayManageRequestDto) throws BaseException;

  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // 전시인벤토리관리
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  /*
   * 인벤토리 리스트조회
   */
  ApiResult<?> selectInventoryList (DisplayManageRequestDto displayManageRequestDto) throws BaseException;

  /*
   * 인벤토리 상세조회
   */
  ApiResult<?> selectInventoryInfo (DisplayManageRequestDto displayManageRequestDto) throws BaseException;

  /*
   * 인벤토리 수정
   */
  ApiResult<?> putInventory (DisplayManageRequestDto displayManageRequestDto) throws BaseException;

  /*
   * 인벤토리 순서변경
   */
  ApiResult<?> putInventorySort (DisplayManageRequestDto displayManageRequestDto) throws BaseException;

  /*
   * 인벤토리 삭제
   */
  ApiResult<?> delInventory (DisplayManageRequestDto displayManageRequestDto) throws BaseException;

  /*
   * 인벤토리 등록
   */
  ApiResult<?> addInventory (DisplayManageRequestDto displayManageRequestDto) throws BaseException;


  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // 전시컨텐츠관리
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  /*
   * 컨텐츠 리스트조회
   */
  ApiResult<?> selectDpContsList (DisplayManageRequestDto displayManageRequestDto) throws BaseException;

  /*
   * 컨텐츠 상세조회
   */
  ApiResult<?> selectDpContsInfo (DisplayManageRequestDto displayManageRequestDto) throws BaseException;

  /*
   * 컨텐츠 수정
   */
  ApiResult<?> putConts (DisplayManageRequestDto displayManageRequestDto) throws BaseException;

  /*
   * 컨텐츠 순서변경
   */
  ApiResult<?> putContsSort (DisplayManageRequestDto displayManageRequestDto) throws BaseException;

  /*
   * 컨텐츠 삭제
   */
  ApiResult<?> delConts (DisplayManageRequestDto displayManageRequestDto) throws BaseException;

  /*
   * 컨텐츠 등록
   */
  ApiResult<?> addConts (DisplayManageRequestDto displayManageRequestDto) throws BaseException;

  /**
   * 브랜드 리스트조회(콤보용)
   * @param displayManageRequestDto
   * @return
   * @throws BaseException
   */
  ApiResult<?> selectBrandList (DisplayManageRequestDto displayManageRequestDto) throws BaseException;

  /**
   * 상품목록조회-키워드조회
   * @param displayManageRequestDto
   * @return
   * @throws BaseException
   */
  ApiResult<?> selectGoodsListByKeyword (DisplayManageRequestDto displayManageRequestDto) throws BaseException;

  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // 인벤토리그룹관리
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  /*
   * 인벤토리그룹 리스트조회
   */
  ApiResult<?> selectDpInventoryGroupList (DisplayManageRequestDto displayManageRequestDto) throws BaseException;

  /*
   * 인벤토리그룹 인벤토리 리스트조회
   */
  ApiResult<?> selectDpGroupInventoryList (DisplayManageRequestDto displayManageRequestDto) throws BaseException;

  /*
   * 인벤토리그룹구성 리스트조회
   */
  ApiResult<?> selectDpGroupInventoryMappingList (DisplayManageRequestDto displayManageRequestDto) throws BaseException;

  /*
   * 인벤토리그룹 등록
   */
  ApiResult<?> addInventoryGroup (DisplayManageRequestDto displayManageRequestDto) throws BaseException;

  /*
   * 인벤토리그룹 수정
   */
  ApiResult<?> putInventoryGroup (DisplayManageRequestDto displayManageRequestDto) throws BaseException;

  /*
   * 인벤토리그룹 삭제
   */
  ApiResult<?> delInventoryGroup (DisplayManageRequestDto displayManageRequestDto) throws BaseException;

  /*
   * 인벤토리그룹 순서변경
   */
  ApiResult<?> putInventoryGroupSort (DisplayManageRequestDto displayManageRequestDto) throws BaseException;

}
