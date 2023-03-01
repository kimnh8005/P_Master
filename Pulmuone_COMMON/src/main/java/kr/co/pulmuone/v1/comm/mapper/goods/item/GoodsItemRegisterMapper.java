package kr.co.pulmuone.v1.comm.mapper.goods.item;

import java.util.List;

import kr.co.pulmuone.v1.goods.item.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.pulmuone.v1.base.dto.vo.GetCodeListResultVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsChangeLogVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsRegistApprVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.AvailableNutritionVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.ErpLinkMasterItemRegisterVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.ErpLinkMasterItemVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.ErpNotLinkMasterItemVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemCertificationApprVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemCertificationCodeVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemCertificationListVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemChangeLogVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemDiscountVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemImageApprVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemImageRegisterVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemImageVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemNutritionApprVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemNutritionDetailVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemPoTypeCodeVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemPoTypeVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemPriceOriginVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemPriceVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemRegistApprVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemReturnPeriodVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemSpecApprVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemSpecValueVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemSpecVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemWarehouseCodeVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemWarehouseVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.MasterItemListVo;

@Mapper
public interface GoodsItemRegisterMapper {

    /*
     * 마스터 품목 등록
     */

    /**
     * @Desc ERP 연동 마스터 품목 조회
     * @param ilItemCode : 품목 코드
     * @return ErpLinkMasterItemVo : ERP 연동 마스터 품목 Vo
     */
    ErpLinkMasterItemVo getErpLinkMasterItem(String ilItemCode);

    /**
     * @Desc ERP 미연동 마스터 품목 조회
     * @param ilItemCode : 품목 코드
     * @return ErpNotLinkMasterItemVo : ERP 미연동 마스터 품목 Vo
     */
    ErpNotLinkMasterItemVo getErpNotLinkMasterItem(String ilItemCode);

    /**
     * @Desc 인자로 받은 품목코드 리스트 중에서 BOS 상에 등록된 품목 코드 목록 조회 : ERP API 로 품목 검색시 등록 여부 확인용
     * @param ilItemCodeList : 품목코드 목록
     * @return List<String> : BOS 상에 등록된 품목 코드 목록
     */
    List<ErpLinkMasterItemRegisterVo> getRegisteredIlItemCodeList(@Param("ilItemCodeList") List<String> ilItemCodeList);

    /**
     * @Desc 인자로 받은 품목바코드 리스트 중에서 BOS 상에 등록된 품목 바코드 목록 조회 : ERP API 로 품목 검색시 등록 여부 확인용
     * @param ilItemBarCodeList : 품목바코드 목록
     * @return List<String> : BOS 상에 등록된 품목 코드 목록
     */
    List<ErpLinkMasterItemRegisterVo> getRegisteredIlItemBarCodeList(@Param("ilItemBarCodeList") List<String> ilItemBarCodeList);


    /**
     * @Desc BOS 상에 등록된 품목의 기초 정보 조회
     * @param ilItemCode : 품목 코드
     * @return MasterItemListVo : 마스터 품목 리스트 조회 VO
     */
    MasterItemListVo getMasterItem(@Param("ilItemCode") String ilItemCode);

    /**
     * @Desc 해당 품목 코드 가격정보 등록
     * @param ilItemCode          : 품목 코드
     * @param priceApplyStartDate : 가격 적용 시작일
     * @return int
     */
    public int addItemPriceOrig(ItemPriceVo itemPriceVo);

    /**
     * @Desc BOS 상에 등록된 품목의 기초 정보 조회
     * @param itemBarcode : 품목 바코드
     * @return MasterItemListVo : 마스터 품목 리스트 조회 VO
     */
    MasterItemListVo getMasterItemBarcode(@Param("itemBarcode") String itemBarcode);

    /**
     * @Desc 등록 가능한 영양정보 분류 코드 조회
     * @param
     * @return List<AvailableNutritionVo> : 등록 가능한 영양정보 분류 코드 목록
     */
    List<AvailableNutritionVo> getAddAvailableNutritionCodeList();

    /**
     * @Desc 해당 품목코드로 IL_GOODS_NUTRITION 에 등록된 영양정보 세부항목 조회
     * @param ilItemCode : 품목 코드
     * @return List<ItemNutritionDetailVo> : 해당 품목 코드로 등록된 영양정보 세부항목
     */
    List<ItemNutritionDetailVo> getItemNutritionDetailList(String ilItemCode);
    List<ItemNutritionDetailVo> getItemApprNutritionDetailList(String ilItemApprId);

    /**
     * @Desc 상품 영양정보 상세항목 등록
     * @param itemNutritionDetailDto : 품목별 영양정보 상세항목 등록 dto
     * @return int
     */
    int addItemNutritionDetail(ItemNutritionDetailDto itemNutritionDetailDto);

    /**
     * @Desc 상품 인증정보 코드 조회
     * @param
     * @return List<ItemCertificationCodeVo> : 상품 인증정보 코드 목록
     */
    List<ItemCertificationCodeVo> getItemCertificationCode();

    /**
     * @Desc 품목별 상품 인증정보 조회
     * @param ilItemCode : 품목 코드
     * @return List<ItemCertificationCodeVo> : 품목별 상품 인증정보 목록
     */
    List<ItemCertificationListVo> getItemCertificationList(String ilItemCode);
    List<ItemCertificationListVo> getItemApprCertificationList(String ilItemApprId);

    /**
     * @Desc 품목별 상품 인증정보 등록
     * @param ItemCertificationDto : 품목별 상품 인증정보 등록 dto
     * @return int
     */
    int addItemCertification(ItemCertificationDto itemCertificationDto);

    /**
     * @Desc 상품정보 제공고시 상품군 코드 조회
     * @param
     * @return List<GetCodeListResultVo> : 상품정보 제공고시 상품군 코드 목록
     */
    List<GetCodeListResultVo> getItemSpecCode();

    /**
     * @Desc 상품정보 제공고시 분류 / 항목 전체 조회
     * @param
     * @return List<ItemSpecVo> : 상품정보 제공고시 전체 목록
     */
    List<ItemSpecVo> getItemSpecList();

    /**
     * @Desc 품목별 상품정보제공고시 세부 항목 조회
     * @param ilItemCode : 품목 코드
     * @return List<ItemSpecValueVo> : 품목별 상품정보 제공고시 상세항목 목록
     */
    List<ItemSpecValueVo> getItemSpecValueList(String ilItemCode);
    List<ItemSpecValueVo> getItemApprSpecValueList(String ilItemApprId);

    /**
     * @Desc 해당 품목의 공급업체 PK / 유통기한 정보로 출고기준일 조회
     * @param urSupplierId       : 공급업체 PK
     * @param distributionPeriod : 화면에서 선택한 유통기한
     * @return List<ItemSpecValueVo> : 품목별 상품정보 제공고시 상세항목 목록
     */
    Integer getItemDeliveryBySupplierAndDistributionPeriod(@Param("urSupplierId") String urSupplierId, @Param("distributionPeriod") int distributionPeriod);

    /**
     * @Desc 상품정보 제공고시 상세항목과 관련된 고정 메시지 조회 ( "풀무원고객기쁨센터 전화번호" 등등 )
     * @param specFieldCode : 상품정보 제공고시 상세항목의 specFieldCode 값, ItemEnums 의 SpecFieldCode enum 참조
     * @return String
     */
    String getItemSpecFieldFixedMessage(@Param("specFieldCode") String specFieldCode);

    /**
     * @Desc 품목별 상품정보제공고시 등록
     * @param itemSpecValueRequestDto : 품목별 상품정보 제공고시 상세항목 Dto
     * @return int
     */
    int addItemSpecValue(ItemSpecValueRequestDto itemSpecValueRequestDto);

    /**
     * @Desc 해당 공급업체 PK 값에 해당하는 발주유형 코드 정보 조회
     * @param urSupplierId : 공급업체 PK
     * @return List<ItemPoTypeCodeVo> : 발주유형 코드 정보 목록
     */
    List<ItemPoTypeCodeVo> getItemPoTypeCode(String urSupplierId);

    /**
     * @Desc 해당 공급업체 PK 값에 해당하는 발주유형 코드 정보 조회
     * @param urSupplierId : 공급업체 PK
     * @return List<ItemPoTypeCodeVo> : 발주유형 코드 정보 목록 - 올가
     */
    List<ItemPoTypeCodeVo> getItemPoTypeOrgaCode(String urSupplierId);

    /**
     * @Desc 해당 공급업체 PK 값에 해당하는 발주유형 코드 정보 조회
     * @param urSupplierId : 공급업체 PK
     * @return List<ItemPoTypeCodeVo> : 발주유형 코드 정보 목록 - 전체조회
     */
    List<ItemPoTypeCodeVo> getItemPoTypeAllCode(String urSupplierId);



    /**
     * @Desc 해당 공급업체 PK 값에 해당하는 출고처 그룹, 출고처 코드 정보 조회
     * @param urSupplierId : 공급업체 PK
     * @return List<ItemWarehouseCodeVo> : 출고처 그룹, 출고처 코드 목록
     */
    List<ItemWarehouseCodeVo> getItemWarehouseCodeList(@Param("urSupplierId") String urSupplierId, @Param("masterItemType") String masterItemType);

    /**
     * @Desc 해당 품목코드로 IL_ITEM_WAREHOUSE 에 등록된 품목별 출고처 정보 조회
     * @param ilItemCode : 품목 코드
     * @return List<ItemWarehouseVo> : 품목별 출고처 목록
     */
    List<ItemWarehouseVo> getItemWarehouseList(String ilItemCode);

    /**
     * @Desc 품목별 출고처 정보 등록
     * @param ItemWarehouseDto : 품목별 출고처 등록 Dto
     * @return int
     */
    int addItemWarehouse(ItemWarehouseDto itemWarehouseDto);

    /**
     * @Desc 품목별 출고처 정보 수정
     * @param ItemWarehouseDto : 품목별 출고처 등록 Dto
     * @return int
     */
    int putItemWarehouse(ItemWarehouseDto itemWarehouseDto);

    /**
     * @Desc 해당 품목의 등록된 품목 이미지 조회
     * @param ilItemCode : 품목 코드
     * @return List<ItemImageVo> : 해당 품목의 등록된 품목 이미지 목록
     */
    List<ItemImageVo> getItemImage(String ilItemCode);
    List<ItemImageVo> getItemApprImage(String ilItemApprId);

    /**
     * @Desc 품목별 이미지 등록
     * @param ItemImageRegisterVo : 품목 이미지 등록 VO
     * @return int
     */
    int addItemImage(ItemImageRegisterVo itemImageRegisterVo);

    /**
     * @Desc 품목별 가격정보 원본 등록
     * @param ItemPriceOriginVo : 품목별 가격정보 원본 Vo
     * @return int
     */
    int addItemPriceOrigin(ItemPriceOriginVo itemPriceOriginVo);

    /**
     * @Desc 품목 할인 등록 => 올가 ERP 품목 등록시 ERP API 로 조회한 행사가격 등록
     * @param ItemDiscountVo : 품목 할인 Vo
     * @return int
     */
    int addItemDiscount(ItemDiscountVo itemDiscountVo);

    /**
     * @Desc 해당 품목의 표준 카테고리, 보관방법에 따른 반품 가능여부, 반품 가능기간 조회
     * @param ilCategoryStandardId : 화면에서 선택한 해당 품목의 표준 카테고리
     * @param storageMethodType    : 화면에서 선택한 해당 품목의 보관방법
     * @return ItemReturnPeriodVo : 품목별 반품 요청가능 여부 / 반품 가능기간 조회 Vo
     */
    ItemReturnPeriodVo getReturnPeriod(@Param("ilCategoryStandardId") String ilCategoryStandardId, @Param("storageMethodType") String storageMethodType);

    /**
     * @Desc 품목 등록
     * @param itemRegisterRequestDto : 마스터 품목 등록 Request dto
     * @return int
     */
    int addItem(ItemRegisterRequestDto itemRegisterRequestDto);

    /**
     * @Desc ERP 미연동 품목코드 생성
     * @param itemRegisterRequestDto : 마스터 품목 등록 Request dto
     * @return String : 신규 ERP 미연동 품목 코드
     */
    String getErpNotLinkItemCode(ItemRegisterRequestDto itemRegisterRequestDto);

    int addPoSearch(ItemPoTypeVo itemPoTypeVo);

    int addPoSearchMerge(ItemPoTypeVo itemPoTypeVo);

    int delPoSearch();

    /**
     * @Desc 품목/묶음상품 이미지 재생성
     * @param ilItemCode : IL_ITEM_CD 또는 IL_GOODS_IMG_ID
     * @return List<ItemImageVo> : 해당 품목/묶음상품의 등록된 이미지 목록
     */
    List<ItemImageVo> getRegenImageList(String ilItemCode);

	//품목변경사항 등록
	int addItemChangeLog(ItemChangeLogVo goodsChangeLogVo);

	//품목 승인 내역 확인, 승인 내역 존재시 요청 자격 확인
	ItemRegistApprVo itemApprInfo(@Param("userId") String userId, @Param("ilItemCd") String ilItemCd, @Param("ilItemApprId") String ilItemApprId, @Param("apprKindTp") String apprKindTp) throws Exception;

	//품목 승인 내역 저장
	int addItemAppr(ItemRegistApprVo itemRegistApprVo);

	//품목 승인 상태 이력 저장
	int addItemApprStatusHistory(ItemRegistApprVo itemRegistApprVo);

	//품목 상태변경
	int updateItemRequestStatus(@Param("ilItemCd") String ilItemCd, @Param("itemStatusTp") String itemStatusTp);

	//품목 승인 인증정보 저장
	int addItemCertificationAppr(ItemCertificationApprVo itemCertificationApprVo);

	//품목 승인 이미지 저장
	int addItemImageAppr(ItemImageRegisterVo itemImageRegisterVo);

	int addItemNutritionAppr(ItemNutritionApprVo itemNutritionApprVo);

	int addItemImageUpdate(ItemImageApprVo itemImageApprVo);

	int addItemSpecAppr(ItemSpecApprVo itemSpecApprVo);


    void setItemPriceAppr(ItemPriceApprovalRequestDto itemPriceApprovalRequestDto);


}
