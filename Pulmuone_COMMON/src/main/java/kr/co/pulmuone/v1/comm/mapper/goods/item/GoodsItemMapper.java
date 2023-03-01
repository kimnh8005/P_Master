package kr.co.pulmuone.v1.comm.mapper.goods.item;

import java.util.List;

import kr.co.pulmuone.v1.approval.auth.dto.vo.ApprovalAuthInfoVo;
import kr.co.pulmuone.v1.goods.item.dto.ItemPriceApprovalRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.ItemPriceDelRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.vo.*;
import kr.co.pulmuone.v1.goods.itemprice.dto.vo.ItemPriceOrigVo;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.approval.auth.dto.ApprovalItemRegistRequestDto;
import kr.co.pulmuone.v1.approval.auth.dto.vo.ApprovalStatusVo;
import kr.co.pulmuone.v1.goods.item.dto.MasterItemListRequestDto;

@Mapper
public interface GoodsItemMapper {

    /**
     * @Desc 상품ID 별 품목상세 조회
     * @param goodsId
     * @return ItemInfoVo
     */
    ItemInfoVo getGoodsIdByItemInfo(@Param("goodsId") Long goodsId);

    List<ItemInfoVo> getGoodsIdListByItemCd(String ilItemCode);

    /*
     * 마스터 품목 리스트
     */

    /**
     * @Desc 마스터 품목 리스트 검색 목록 조회
     * @param masterItemListRequestDto : 마스터 품목 검색 request dto
     * @return Page<MasterItemListVo> : 마스터 품목 검색 목록
     */
    Page<MasterItemListVo> getItemList(MasterItemListRequestDto masterItemListRequestDto);

    /**
     * @Desc 마스터 품목 리스트 엑셀 다운로드 목록 조회
     * @param masterItemListRequestDto : 마스터 품목 검색 request dto
     * @return List<MasterItemListVo> : 마스터 품목 검색 목록
     */
    List<MasterItemListVo> getItemListExcel(MasterItemListRequestDto masterItemListRequestDto);

    /*
	 * 승인 관리 추가
	 */
	Page<ItemApprovalResultVo> getApprovalItemRegistList(ApprovalItemRegistRequestDto approvalItemRequestDto);

	int putCancelRequestApprovalItemRegist(ApprovalStatusVo approvalVo);

	int putApprovalProcessItemRegist(ApprovalStatusVo approvalVo);

	int addItemRegistStatusHistory(ApprovalStatusVo history);

	Page<ItemApprovalResultVo> getApprovalItemPriceList(ApprovalItemRegistRequestDto approvalItemRequestDto);

	ItemApprovalPriceVo getApprovalItemPriceChangeNowInfo(ApprovalStatusVo approvalVo);

	String getApprovalDeniedInfo(Long ilItemPriceApprId);

	int addItemPriceOrigByApproval(ItemPriceOrigVo approvalVo);

	int updateItemPriceOrigByItemPriceOrigId(ItemPriceOrigVo itemPriceOrigVo);

	ItemPriceOrigVo selectItemPriceOrigByItemPriceOrigId(@Param("ilItemPriceOrigId") String ilItemPriceOrigId);

	ItemPriceOrigVo selectPrevItemPriceOrigByItemPriceOrigId(@Param("ilItemPriceOrigId") String ilItemPriceOrigId);

	ItemPriceOrigVo getApprovedItemPriceOrigInfo(ApprovalStatusVo approvalVo);

	List<ItemPriceOrigVo> getItemPriceOrigInfoForApprovalSync(ItemPriceOrigVo itemPriceOrigVo);

	int delItemPriceOrigByItemPriceOrigId(@Param("ilItemPriceOrigId") String ilItemPriceOrigId);

	List<ItemPriceOrigVo> getItemPriceOrigInfoForDeleteSync(ItemPriceOrigVo itemPriceOrigVo);

	int addItemPriceApprove(ItemPriceApprovalRequestDto dto);

	int putCancelRequestApprovalItemPrice(ApprovalStatusVo approvalVo);

	int putApprovalProcessItemPrice(ApprovalStatusVo approvalVo);

	int addItemPriceStatusHistory(ApprovalStatusVo history);

	int putDisposalApprovalItemPrice(ApprovalStatusVo approvalVo);

	/**
     * @Desc 마스터 품목 - 묶음 상품  목록 조회
     * @param masterItemListRequestDto : 마스터 품목 검색 request dto
     * @return Page<MasterItemListVo> : 마스터 품목 검색 목록
     */
    Page<MasterItemListVo> getItemGoodsPackageList(MasterItemListRequestDto masterItemListRequestDto);

}
