package kr.co.pulmuone.v1.comm.mapper.promotion.manage;

import java.util.List;

import kr.co.pulmuone.v1.promotion.manage.dto.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.approval.auth.dto.ApprovalExhibitRequestDto;
import kr.co.pulmuone.v1.approval.auth.dto.vo.ApprovalStatusVo;
import kr.co.pulmuone.v1.base.dto.vo.GoodsSearchVo;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.promotion.manage.dto.ExhibitManageRequestDto;

@Mapper
public interface ExhibitManageMapper {

  // ==========================================================================
  // 조회
  // ==========================================================================
  // --------------------------------------------------------------------------
  // 기획전 리스트조회
  // --------------------------------------------------------------------------
  // 기획전 리스트 조회
  Page<ExhibitVo> selectExhibitList (ExhibitManageRequestDto exhibitManageRequestDto) throws BaseException;

  // 기획전 담당자리스트(조회조건 콤보용)
  List<ExhibitVo> selectExhibitManagerList (ExhibitManageRequestDto exhibitManageRequestDto) throws BaseException;

  // --------------------------------------------------------------------------
  // 기획전 상세조회
  // --------------------------------------------------------------------------
  // 기획전-기본정보 조회
  ExhibitVo selectExhibitInfo (@Param("evExhibitId") String evExhibitId) throws BaseException;

  // 일반기획전-그룹정보 리스트조회
  List<ExhibitGroupVo> selectfExhibitGroupList (@Param("evExhibitId") String evExhibitId) throws BaseException;

  // 일반기획전-그룹정보-그룹상품 리스트조회
  List<ExhibitGroupDetlVo> selectfExhibitGroupGoodsList (@Param("evExhibitGroupId") String evExhibitGroupId) throws BaseException;

  // 골라담기기획전-기본정보 조회
  ExhibitSelectVo selectfExhibitSelectInfo (@Param("evExhibitId") String evExhibitId) throws BaseException;

  // 골라담기기획전-상품 리스트조회
  List<ExhibitSelectGoodsVo> selectExhibitSelectGoodsList (@Param("evExhibitId") String evExhibitId) throws BaseException;

  // 골라담기기획전-추가상품 리스트조회
  List<ExhibitSelectGoodsVo> selectExhibitSelectAddGoodsList (@Param("evExhibitId") String evExhibitId) throws BaseException;

  // 증정행사기획전-기본정보 조회
  ExhibitGiftVo selectExhibitGiftInfo  (@Param("evExhibitId") String evExhibitId) throws BaseException;

  // 증정행사기획전-증정상품 리스트조회
  List<ExhibitGiftGoodsVo> selectExhibitGiftGoodsList  (@Param("evExhibitId") String evExhibitId) throws BaseException;

  // 증정행사기획전-적용상품 리스트조회
  List<ExhibitGiftGoodsVo> selectExhibitGiftTargetGoodsList  (@Param("evExhibitId") String evExhibitId) throws BaseException;

  // 증정행사기획전-적용브랜드 리스트조회
  List<ExhibitGiftGoodsVo> selectExhibitGiftTargetBrandList  (@Param("evExhibitId") String evExhibitId) throws BaseException;

  // 상품정보리스트(엑셀용)
  List<GoodsSearchVo> selectGoodsInfoList (ExhibitManageRequestDto exhibitManageRequestDto) throws BaseException;


  // ==========================================================================
  // 등록
  // ==========================================================================
  // --------------------------------------------------------------------------
  // 기획전메인
  // --------------------------------------------------------------------------
  // 기획전 등록 - 기본
  int addExhibit (ExhibitVo exhibitVo) throws BaseException;
  // 기획전 등록 후처리

  // --------------------------------------------------------------------------
  // 일반기획전
  // --------------------------------------------------------------------------
  // 기획전 등록 - 전시그룹
  int addExhibitGroup (ExhibitGroupVo exhibitGroupVo) throws BaseException;

  // 기획전 등록 - 전시그룹상세
  int addExhibitGroupDetl (ExhibitGroupDetlVo exhibitGroupDetlVo) throws BaseException;

  // --------------------------------------------------------------------------
  // 골라담기
  // --------------------------------------------------------------------------
  // 기획전 등록 - 골라담기
  int addExhibitSelect (ExhibitSelectVo exhibitSelectVo) throws BaseException;

  // 기획전 등록 - 골라담기 상품
  int addExhibitSelectGoods (ExhibitSelectGoodsVo exhibitSelectGoodsVo) throws BaseException;

  // 기획전 등록 - 골라담기 추가상품
  int addExhibitSelectAddGoods (ExhibitSelectGoodsVo exhibitSelectGoodsVo) throws BaseException;

  // --------------------------------------------------------------------------
  // 증정행사
  // --------------------------------------------------------------------------
  // 기획전 등록 - 증정행사
  int addExhibitGift (ExhibitGiftVo exhibitGiftVo) throws BaseException;

  // 기획전 등록 - 증정행사 상품
  int addExhibitGiftGoods (ExhibitGiftGoodsVo exhibitGiftGoodsVo) throws BaseException;

  // 기획전 등록 - 증정대상 상품
  int addExhibitGiftTargetGoods (ExhibitGiftGoodsVo exhibitGiftGoodsVo) throws BaseException;

  // 기획전 등록 - 증정대상 브랜드
  int addExhibitGiftTargetBrand (ExhibitGiftGoodsVo exhibitGiftGoodsVo) throws BaseException;

  // ==========================================================================
  // 수정
  // ==========================================================================
  // --------------------------------------------------------------------------
  // 기획전메인
  // --------------------------------------------------------------------------
  // 기획전 수정 - 기본
  int putExhibit (ExhibitVo exhibitVo) throws BaseException;

  // ==========================================================================
  // 삭제
  // ==========================================================================
  // --------------------------------------------------------------------------
  // 기획전메인
  // --------------------------------------------------------------------------
  // 기획전 삭제 - 기본
  int delExhibit (ExhibitVo exhibitVo) throws BaseException;

  // --------------------------------------------------------------------------
  // 일반기획전
  // --------------------------------------------------------------------------
  // 기획전 삭제 - 전시그룹 - 기획전ID 기준
  int delExhibitGroupByEvExhibitId (@Param("evExhibitId") String evExhibitId) throws BaseException;

  // 기획전 삭제 - 전시그룹상세 - 기획전ID 기준
  int delExhibitGroupDetlByEvExhibitId (@Param("evExhibitId") String evExhibitId) throws BaseException;

  // 개별 삭제 - 전시그룹
  int delExhibitGroup (@Param("evExhibitGroupId") String evExhibitGroupId) throws BaseException;

  // 개별 삭제 - 전시그룹상세
  int delExhibitGroupDetl (@Param("evExhibitGroupDetlId") String evExhibitGroupDetlId) throws BaseException;

  // --------------------------------------------------------------------------
  // 골라담기
  // --------------------------------------------------------------------------
  // 기획전 삭제 - 골라담기상세 - 기획전ID 기준
  int delExhibitSelectByEvExhibitId (@Param("evExhibitId") String evExhibitId) throws BaseException;

  // 기획전 삭제 - 골라담기상품 - 기획전ID 기준
  int delExhibitSelectGoodsByEvExhibitId (@Param("evExhibitId") String evExhibitId) throws BaseException;

  // 기획전 삭제 - 골라담기추가상품 - 기획전ID 기준
  int delExhibitSelectAddGoodsByEvExhibitId (@Param("evExhibitId") String evExhibitId) throws BaseException;

  // 개별 삭제 - 골라담기상세
  int delExhibitSelect (@Param("evExhibitSelectId") String evExhibitSelectId) throws BaseException;

  // 개별 삭제 - 골라담기상품
  int delExhibitSelectGoods (@Param("evExhibitSelectGoodsId") String evExhibitSelectGoodsId) throws BaseException;

  // 개별 삭제 - 골라담기추가상품
  int delExhibitSelectAddGoods (@Param("evExhibitSelectAddGoodsId") String evExhibitSelectAddGoodsId) throws BaseException;

  // --------------------------------------------------------------------------
  // 증정행사
  // --------------------------------------------------------------------------
  // 기획전 삭제 - 증정행사
  int delExhibitGiftByEvExhibitId (@Param("evExhibitId") String evExhibitId) throws BaseException;

  // 기획전 삭제 - 증정행사 상품
  int delExhibitGiftGoodsByEvExhibitId (@Param("evExhibitId") String evExhibitId) throws BaseException;

  // 기획전 삭제 - 증정대상 상품
  int delExhibitGiftTargetGoodsByEvExhibitId (@Param("evExhibitId") String evExhibitId) throws BaseException;

  // 기획전 삭제 - 증정대상 브랜드
  int delExhibitGiftTargetBrandByEvExhibitId (@Param("evExhibitId") String evExhibitId) throws BaseException;

  // 개별 삭제 - 증정행사
  int delExhibitGift (@Param("evExhibitGiftId") String evExhibitGiftId) throws BaseException;

  // 개별 삭제 - 증정행사 상품
  int delExhibitGiftGoods (@Param("evExhibitGiftGoodsId") String evExhibitGiftGoodsId) throws BaseException;

  // 개별 삭제 - 증정대상 상품
  int delExhibitGiftTargetGoods (@Param("evExhibitGiftTargetGoodsId") String evExhibitGiftTargetGoodsId) throws BaseException;

  // 개별 삭제 - 증정대상 브랜드
  int delExhibitGiftTargetBrand (@Param("evExhibitGiftTargetBrandId") String evExhibitGiftTargetBrandId) throws BaseException;

  // 대표상품해제
  int putExhibitGiftRepGoodsCancel (@Param("evExhibitId") String evExhibitId) throws BaseException;

  // 대표상품등록
  int putExhibitGiftRepGoodsReg (@Param("evExhibitGiftGoodsId") String evExhibitGiftGoodsId) throws BaseException;


  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // 전체건수
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  int selectTotalCount () throws BaseException;

  List<EvUserGroupVo> getExhibitUserGroup (@Param("exExhibitId") String exExhibitId) throws BaseException;

  void addExhibitUserGroup (@Param("evExhibitId") String evExhibitId, @Param("urGroupIdList") List<Long> urGroupIdList) throws BaseException;

  void delExhibitUserGroup (@Param("evExhibitId") String evExhibitId) throws BaseException;

	/*
	 * 승인 관리 추가
	 */
	Page<ExhibitApprovalResultVo> getApprovalExhibitList(ApprovalExhibitRequestDto approvalExhibitRequestDto);

	int putCancelRequestApprovalExhibit(ApprovalStatusVo approvalVo);

	int putApprovalProcessExhibit(ApprovalStatusVo approvalVo);

	int addExhibitStatusHistory(ApprovalStatusVo history);

	int putDisposalApprovalExhibit(ApprovalStatusVo approvalVo);

	int putApprovalRequestExhibit(ExhibitVo exhibitVo);

  ApprovalUserVo getApprovalUserInfo(String urUserId);

  List<ExhibitSelectVo> getExhibitSelectGoodsListForMaxRate(@Param("evExhibitId") String exExhibitId) throws BaseException;

}
