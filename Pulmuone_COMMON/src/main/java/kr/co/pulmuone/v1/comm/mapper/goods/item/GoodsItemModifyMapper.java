package kr.co.pulmuone.v1.comm.mapper.goods.item;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsRegistVo;
import kr.co.pulmuone.v1.goods.item.dto.ItemModifyRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.ItemPriceHistoryRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.MasterItemListRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemApprovalResultVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemCertificationApprVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemDiscountVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemImageApprVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemNutritionApprVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemNutritionDetailVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemPoTypeVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemPriceVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemRegistApprVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemSpecApprVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.MasterItemVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GoodsItemModifyMapper {

    /*
     * 마스터 품목 수정
     */

    /**
     * @Desc 해당 품목코드로 IL_ITEM 테이블의 마스터 품목 정보 조회
     * @param ilItemCode : 품목코드
     * @return MasterItemVo : 해당 품목코드로 조회한 마스터 품목 정보 Vo
     */
    public MasterItemVo getMasterItem(@Param("ilItemCode") String ilItemCode, @Param("apprKindTp") String apprKindTp );

	//품목 승인 상태 항목
	List<ItemApprovalResultVo> itemApprStatusList(@Param("ilItemCd") String ilItemCd);

    /**
     * @Desc 마스터 품목 정보 수정
     * @param ItemModifyRequestDto : 마스터 품목 수정 request dto
     * @return int
     */
    public int modifyItem(ItemModifyRequestDto itemModifyRequestDto);

    /**
     * @Desc 해당 품목의 현재/예정 가격정보 목록 조회
     * @param ilItemCode              : 품목 코드
     * @param includePastPriceHistory : 과거 가격정보 이력을 조회 결과에 포함할지 여부
     * @return List<ItemPriceVo> 해당 품목의 가격정보 이력
     */
    List<ItemPriceVo> getItemPriceHistory(@Param("ilItemCode") String ilItemCode);

    List<ItemPriceVo> getItemPricePopup(@Param("ilItemCode") String ilItemCode, @Param("includePastPriceHistory") String includePastPriceHistory);

    Page<ItemPriceVo> getItemPricePopup(ItemPriceHistoryRequestDto dto);

    List<ItemPriceVo> getItemPriceListByDate(@Param("ilItemCode") String ilItemCode, @Param("targetDate") String targetDate);


    /**
     * @Desc 해당 품목의 ERP 가격 목록 조회
     * @param ilItemCode              : 품목 코드
     * @return List<ItemDiscountVo> 해당 품목의 ERP 가격 목록 조회
     */
    public List<ItemDiscountVo> getItemErpPriceSchedule(@Param("ilItemCode") String ilItemCode);

    /**
     * @Desc 해당 품목의 ERP 가격 목록 전체 조회
     * @param ilItemCode              : 품목 코드
     * @return List<ItemDiscountVo> 해당 품목의 ERP 가격 목록 전체 조회
     */
    public Page<ItemDiscountVo> getItemDiscountPriceHistory(ItemPriceHistoryRequestDto itemPriceHistoryRequestDto);

    /**
     * @Desc 해당 품목 코드의 가격 적용 시작일에 해당하는 가격정보 삭제
     * @param ilItemCode          : 품목 코드
     * @param priceApplyStartDate : 가격 적용 시작일
     * @return int
     */
    public int deleteItemPriceOrigin(@Param("ilItemCode") String ilItemCode, @Param("priceApplyStartDate") String priceApplyStartDate);

    /**
     * @Desc 해당 품목의 상품정보 제공고시 삭제
     * @param ilItemCode : 품목 코드
     * @return int
     */
    public int deleteItemSpecValue(@Param("ilItemCode") String ilItemCode);

    /**
     * @Desc 해당 품목의 영양정보 상세항목 삭제
     * @param ilItemCode : 품목 코드
     * @return int
     */
    public int deleteItemNutrition(@Param("ilItemCode") String ilItemCode);

    /**
     * @Desc 해당 품목의 상품 인증정보 삭제
     * @param ilItemCode : 품목 코드
     * @return int
     */
    public int deleteItemCertification(@Param("ilItemCode") String ilItemCode);

    /**
     * @Desc 해당 품목의 출고처 정보 삭제
     * @param ilItemCode : 품목 코드
     * @return int
     */
    public int deleteItemWarehouse(@Param("ilItemCode") String ilItemCode);

    /**
     * @Desc 해당 품목의 출고처 정보 삭제
     * @param ilItemCode : 품목 코드
     * @return int
     */
    public int deleteItemWarehouseId(@Param("ilItemWarehouseId") String ilItemWarehouseId);


    /**
     * @Desc 해당 품목의 이미지 정보 삭제
     * @param ilItemCode : 품목 코드
     * @return int
     */
    public int deleteItemImage(@Param("ilItemCode") String ilItemCode);

    Page<ItemPoTypeVo> getPoTpDetailInfoList(MasterItemListRequestDto masterItemListRequestDto);

    /**
     * @Desc 해당 품목코드로 IL_ITEM 테이블의 마스터 품목 승인 정보 조회
     * @param ilItemApprId : 품목승인 코드 ID
     * @return MasterItemVo : 해당 품목코드로 조회한 마스터 품목 정보 Vo
     */
    MasterItemVo getMasterItemApprClientDetail(@Param("ilItemApprId") String ilItemApprId);

    public ItemRegistApprVo getItemApprInfo(@Param("ilItemApprId") String ilItemApprId, @Param("ilItemCd") String ilItemCd, @Param("apprKindTp") String apprKindTp);

    public int putItemMasterApprInfo(ItemRegistApprVo itemRegistApprVo);

    public List<ItemCertificationApprVo> getItemCertApprList(@Param("ilItemApprId") String ilItemApprId, @Param("ilItemCd") String ilItemCd);

    public List<ItemImageApprVo> getItemImageApprList(@Param("ilItemApprId") String ilItemApprId, @Param("ilItemCd") String ilItemCd);

    public List<ItemNutritionApprVo> getItemNutrApprList(@Param("ilItemApprId") String ilItemApprId, @Param("ilItemCd") String ilItemCd);

    public List<ItemSpecApprVo> getItemSpecApprList(@Param("ilItemApprId") String ilItemApprId, @Param("ilItemCd") String ilItemCd);

}
