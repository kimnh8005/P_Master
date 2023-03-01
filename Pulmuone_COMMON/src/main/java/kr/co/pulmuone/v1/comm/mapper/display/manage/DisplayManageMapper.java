package kr.co.pulmuone.v1.comm.mapper.display.manage;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.pulmuone.v1.base.dto.vo.GetCodeListResultVo;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.display.manage.dto.DisplayManageRequestDto;
import kr.co.pulmuone.v1.display.manage.dto.vo.ContsVo;
import kr.co.pulmuone.v1.display.manage.dto.vo.InventoryGrpMappingVo;
import kr.co.pulmuone.v1.display.manage.dto.vo.InventoryGrpVo;
import kr.co.pulmuone.v1.display.manage.dto.vo.InventoryVo;
import kr.co.pulmuone.v1.display.manage.dto.vo.PageVo;

@Mapper
public interface DisplayManageMapper {

  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // 페이지관리
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  /**
   * 전시페이지 리스트조회
   * @param dpPageId
   * @param useAllYn
   * @return
   * @throws BaseException
   */
  List<PageVo> selectDpPageList (@Param("dpPageId") String dpPageId, @Param("useAllYn") String useAllYn, @Param("pageCd") String pageCd, @Param("dupCheckYn") String dupCheckYn) throws BaseException;

  /**
   * 페이지 Depth 조회
   * @param depth
   * @param useAllYn
   * @param mallDiv
   * @return
   * @throws BaseException
   */
  int selectDpPageDepth (@Param("dpPageId") String dpPageId) throws BaseException;

  /**
   * 전시카테고리 리스트조회
   * @param depth
   * @param useAllYn
   * @param mallDiv
   * @return
   * @throws BaseException
   */
  List<PageVo> selectDpCategoryList (@Param("dpPageId") String dpPageId, @Param("useAllYn") String useAllYn, @Param("mallDiv") String mallDiv, @Param("depth") String depth) throws BaseException;

  /**
   * 전시카테고리 리스트조회
   * @param depth
   * @param useAllYn
   * @param mallDiv
   * @return
   * @throws BaseException
   */
  List<PageVo> selectDpCategorySubList (@Param("dpPageId") String dpPageId, @Param("useAllYn") String useAllYn, @Param("mallDiv") String mallDiv) throws BaseException;

  /**
   * 페이지 상세조회(페이지)
   * @param dpPageId
   * @return
   * @throws BaseException
   */
  PageVo selectPageInfo (@Param("dpPageId") String dpPageId) throws BaseException;

  /**
   * 페이지  수정
   * @param pageVo
   * @return
   * @throws BaseException
   */
  int putPage (PageVo pageVo) throws BaseException;

  /**
   * 페이지  순서변경
   * @param pageVo
   * @return
   * @throws BaseException
   */
  int putPageSort (PageVo pageVo) throws BaseException;

  /**
   * 페이지  삭제
   * @param pageVo
   * @return
   * @throws BaseException
   */
  int delPage (PageVo pageVo) throws BaseException;

  /**
   * 페이지  등록
   * @param pageVo
   * @return
   * @throws BaseException
   */
  int addPage (PageVo pageVo) throws BaseException;

  /**
   * 페이지  등록 후처리
   * @param pageVo
   * @return
   * @throws BaseException
   */
  int putPageAfterAdd (PageVo pageVo) throws BaseException;

  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // 인벤토리관리
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  /**
   * 인벤토리 리스트조회
   * @param depth
   * @param useAllYn
   * @return
   * @throws BaseException
   */
  List<InventoryVo> selectInventoryList (InventoryVo inInventoryVo) throws BaseException;

  /**
   * 인벤토리코드 리스트조회
   * @param inventoryCd
   * @return
   * @throws BaseException
   */
  List<InventoryVo> selectInventoryCdList (@Param("inventoryCd") String inventoryCd, @Param("inventoryCdList") List<String> inventoryCdList, @Param("pageTp") String pageTp) throws BaseException;

  /**
   * 인벤토리코드리스트 조회 - 카테고리용
   * @param inventoryCd
   * @return
   * @throws BaseException
   */
  List<InventoryVo> selectInventoryCdListForCategory (@Param("inventoryCd") String inventoryCd, @Param("pageTp") String pageTp, @Param("ilCtgryId") String ilCtgryId) throws BaseException;

  /**
   * 인벤토리 상세조회
   * @param dpPageId
   * @return
   * @throws BaseException
   */
  InventoryVo selectInventoryInfo (@Param("dpInventoryId") String dpInventoryId) throws BaseException;

  /**
   * 인벤토리  수정
   * @param pageVo
   * @return
   * @throws BaseException
   */
  int putInventory (InventoryVo inventoryVo) throws BaseException;

  /**
   * 인벤토리  순서변경
   * @param pageVo
   * @return
   * @throws BaseException
   */
  int putInventorySort (InventoryVo inventoryVo) throws BaseException;

  /**
   * 인벤토리  삭제
   * @param pageVo
   * @return
   * @throws BaseException
   */
  int delInventory (InventoryVo inventoryVo) throws BaseException;

  /**
   * 인벤토리  등록
   * @param pageVo
   * @return
   * @throws BaseException
   */
  int addInventory (InventoryVo inventoryVo) throws BaseException;


  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // 컨텐츠관리
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  /**
   * 전시컨텐츠 리스트조회
   * @param depth
   * @param useAllYn
   * @return
   * @throws BaseException
   */
  List<ContsVo> selectDpContsList (ContsVo contsVo) throws BaseException;

  /**
   * 컨텐츠 상세조회
   * @param dpPageId
   * @return
   * @throws BaseException
   */
  ContsVo selectDpContsInfo (ContsVo contVo) throws BaseException;

  /**
   * 컨텐츠  수정
   * @param pageVo
   * @return
   * @throws BaseException
   */
  int putConts (ContsVo contsVo) throws BaseException;

  /**
   * 컨텐츠  수정 - 상품용
   * @param pageVo
   * @return
   * @throws BaseException
   */
  int putContsForGoods (ContsVo contsVo) throws BaseException;

  /**
   * 컨텐츠  순서변경
   * @param pageVo
   * @return
   * @throws BaseException
   */
  int putContsSort (ContsVo contsVo) throws BaseException;

  /**
   * 컨텐츠  삭제
   * @param pageVo
   * @return
   * @throws BaseException
   */
  int delConts (ContsVo contsVo) throws BaseException;

  /**
   * 상품컨텐츠 중복체크
   * @param contsVo
   * @return
   * @throws BaseException
   */
  List<ContsVo> selectDpGoodsContsDupList (ContsVo contsVo) throws BaseException;

  /**
   * 컨텐츠  등록
   * @param pageVo
   * @return
   * @throws BaseException
   */
  int addConts (ContsVo contsVo) throws BaseException;

  /**
   * 컨텐츠  등록 후처리
   * @param pageVo
   * @return
   * @throws BaseException
   */
  int putContsAfterAdd (ContsVo contsVo) throws BaseException;

  /**
   * 브랜드 리스트조회
   * @param contsVo
   * @return
   * @throws BaseException
   */
  List<GetCodeListResultVo> selectBrandList (ContsVo contsVo) throws BaseException;

  /**
   * 상품목록조회-키워드조회
   * @return
   * @throws BaseException
   */
  List<ContsVo> selectGoodsListByKeyword (DisplayManageRequestDto displayManageRequestDto) throws BaseException;

  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // 인벤토리그룹관리
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  /**
   * 인벤토리그룹 리스트조회
   * @param dpPageId
   * @param useAllYn
   * @return
   * @throws BaseException
   */
  List<PageVo> selectDpInventoryGroupList (@Param("userId") String userId, @Param("useYn") String useYn) throws BaseException;

  /**
   * 인벤토리그룹 상세조회
   * @param dpPageId
   * @param useAllYn
   * @return
   * @throws BaseException
   */
  PageVo selectDpInventoryGroupInfo (@Param("dpInventoryGrpId") String dpInventoryGrpId) throws BaseException;

  /**
   * 인벤토리그룹 인벤토리 리스트조회
   * @param dpPageId
   * @param useAllYn
   * @return
   * @throws BaseException
   */
  List<InventoryVo> selectDpGroupInventoryList (@Param("dpInventoryGrpId") String dpInventoryGrpId, @Param("useYn") String useYn) throws BaseException;

  /**
   * 인벤토리그룹구성 리스트조회
   * @param dpPageId
   * @param useAllYn
   * @return
   * @throws BaseException
   */
  List<InventoryVo> selectDpGroupInventoryMappingList (@Param("dpInventoryGrpId") String dpInventoryGrpId) throws BaseException;

  /**
   * 인벤토리그룹 등록
   * @param inventoryGrpVo
   * @return
   * @throws BaseException
   */
  int addInventoryGroup (InventoryVo inventoryVo) throws BaseException;

  /**
   * 인벤토리그룹 수정
   * @param inventoryGrpVo
   * @return
   * @throws BaseException
   */
  int putInventoryGroup (InventoryVo inventoryVo) throws BaseException;

  /**
   * 인벤토리그룹구성 사용여부/삭제여부 변경
   * @param inventoryGrpVo
   * @return
   * @throws BaseException
   */
  int putInventoryGroup (InventoryGrpVo inventoryGrpVo) throws BaseException;

  /**
   * 인벤토리그룹구성 등록
   * @param inventoryGrpMappingVo
   * @return
   * @throws BaseException
   */
  int addInventoryGroupMapping (InventoryVo inventoryVo) throws BaseException;

  /**
   * 인벤토리그룹 사용여부/삭제여부 변경
   * @param inventoryGrpMappingVo
   * @return
   * @throws BaseException
   */
  int putInventoryGroupUseDelYn (InventoryVo inventoryVo) throws BaseException;

  /**
   * 인벤토리그룹 순서변경
   * @param inventoryVo
   * @return
   * @throws BaseException
   */
  int putInventoryGroupSort (InventoryVo inventoryVo) throws BaseException;

  /**
   * 인벤토리그룹구성 사용여부/삭제여부 변경
   * @param inventoryGrpMappingVo
   * @return
   * @throws BaseException
   */
  int putInventoryGroupInventoryUseDelYn (InventoryVo inventoryVo) throws BaseException;



//  /**
//   * 페이지 Depth 조회
//   * @param depth
//   * @param useAllYn
//   * @param mallDiv
//   * @return
//   * @throws BaseException
//   */
//  int selectDpPageDepth (@Param("dpPageId") String dpPageId) throws BaseException;
//
//  /**
//   * 전시카테고리 리스트조회
//   * @param depth
//   * @param useAllYn
//   * @param mallDiv
//   * @return
//   * @throws BaseException
//   */
//  List<PageVo> selectDpCategoryList (@Param("dpPageId") String dpPageId, @Param("useAllYn") String useAllYn, @Param("mallDiv") String mallDiv, @Param("depth") String depth) throws BaseException;
//
//  /**
//   * 전시카테고리 리스트조회
//   * @param depth
//   * @param useAllYn
//   * @param mallDiv
//   * @return
//   * @throws BaseException
//   */
//  List<PageVo> selectDpCategorySubList (@Param("dpPageId") String dpPageId, @Param("useAllYn") String useAllYn, @Param("mallDiv") String mallDiv) throws BaseException;
//
//  /**
//   * 페이지 상세조회(페이지)
//   * @param dpPageId
//   * @return
//   * @throws BaseException
//   */
//  PageVo selectPageInfo (@Param("dpPageId") String dpPageId) throws BaseException;
//
//  /**
//   * 페이지  수정
//   * @param pageVo
//   * @return
//   * @throws BaseException
//   */
//  int putPage (PageVo pageVo) throws BaseException;
//
//  /**
//   * 페이지  순서변경
//   * @param pageVo
//   * @return
//   * @throws BaseException
//   */
//  int putPageSort (PageVo pageVo) throws BaseException;
//
//  /**
//   * 페이지  삭제
//   * @param pageVo
//   * @return
//   * @throws BaseException
//   */
//  int delPage (PageVo pageVo) throws BaseException;
//
//  /**
//   * 페이지  등록
//   * @param pageVo
//   * @return
//   * @throws BaseException
//   */
//  int addPage (PageVo pageVo) throws BaseException;
//
//  /**
//   * 페이지  등록 후처리
//   * @param pageVo
//   * @return
//   * @throws BaseException
//   */
//  int putPageAfterAdd (PageVo pageVo) throws BaseException;



  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // 전체건수
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  int selectTotalCount () throws BaseException;
}
