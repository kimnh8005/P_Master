package kr.co.pulmuone.v1.goods.item.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.framework.dto.UploadFileDto;
import kr.co.pulmuone.v1.goods.item.dto.ItemModifyRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.ItemRegisterRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.ItemSpecFieldModifiedMessageRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemImageRegisterVo;

public interface GoodsItemRegisterBiz {

    /*
     * ERP API 연동 관련 로직 Start
     */

    // ERP 품목 검색 API 로 ERP 연동 품목 검색
    public ApiResult<?> getErpItemApiList(String searchOption, String searchValue, String masterItemType);

    // ERP 영양정보 조회 API 로 해당 ERP 연동 품목의 영양정보 조회
    public ApiResult<?> getErpNutritionApi(String ilItemCode);

    // ERP 가격정보 조회 API 로 해당 ERP 연동 품목의 가격정보 조회
    public ApiResult<?> getErpPriceApi(String ilItemCode, String erpLegalTypeCode, String erpSalesType);

    // 올가 공급품목 발주 API 조회
    public ApiResult<?> getOrgaPoApi(String ilItemCode);

    /*
     * ERP API 연동 관련 로직 End
     */

    /*
     * 마스터 품목 복사 : ERP 연동 or 미연동 품목의 원본 정보 조회 로직 Start
     */

    // EPR 연동 or 미연동 품목의 원본 정보 조회
    public ApiResult<?> getMasterItem(String ilItemCode, String isErpItemLink);

    /*
     * 마스터 품목 복사 : ERP 연동 or 미연동 품목의 원본 정보 조회 로직 End
     */

    /*
     * 상품정보제공고시 조회 로직 Start
     */

    // 상품정보 제공고시 상품군 코드 목록 조회
    public ApiResult<?> getItemSpecCode();

    // 상품정보 제공고시 상품군별 분류 항목 전체 조회 => 특수 항목에 대한 세부 메시지 및 수정 가능여부 등 설정
    // 품목 코드 param 전달시에는 해당 품목 코드의 상품정보 제공고시 상세 항목을 같이 조회
    public ApiResult<?> getItemSpecList(String ilItemCode, String ilItemApprId);

    // 해당 상품정보제공고시 항목 코드의 수정 메시지 조회
    public ApiResult<?> getItemSpecFieldDetailMessage(ItemSpecFieldModifiedMessageRequestDto itemSpecFieldModifiedMessageRequestDto);

    /*
     * 상품정보제공고시 조회 로직 End
     */

    /*
     * 상품 영양정보 조회 로직 Start
     */

    // 해당 품목코드로 등록 가능한 영양정보 분류 코드 조회
    // ERP 미연동 품목인 경우, BOS 상에 등록된 해당 품목 코드의 영양정보 세부항목 조회 후 같이 반환 : 마스터 품목 복사 기능에서 사용함
    public ApiResult<?> getAddAvailableNutritionCodeList(String ilItemCode, String isErpItemLink, String ilItemApprId);

    /*
     * 상품 영양정보 조회 로직 End
     */

    /*
     * 상품 인증정보 조회 로직 Start
     */

    // 상품 인증정보 코드 조회
    public ApiResult<?> getItemCertificationCode();

    // 품목별 상품 인증정보 목록 조회
    public ApiResult<?> getItemCertificationList(String ilItemCode, String ilItemApprId);

    /*
     * 상품 인증정보 조회 로직 End
     */

    /*
     * 공급업체 / 출고처 조회 로직 Start
     */

    // 해당 공급업체 PK 값에 해당하는 출고처 그룹, 출고처 코드 정보 조회
    public ApiResult<?> getItemWarehouseCode(String urSupplierId, String masterItemType);

    // 해당 품목코드로 등록된 출고처 정보 조회
    public ApiResult<?> getItemWarehouseList(String ilItemCode);

    /*
     * 공급업체 / 출고처 조회 로직 End
     */

    /*
     * 배송 / 발주 정보 조회 로직 Start
     */

    // 해당 공급업체 PK 값에 해당하는 발주유형 정보 조회
    public ApiResult<?> getItemPoTypeCode(String urSupplierId, String erpPoType, String selectType);

    // 해당 품목 정보의 표준 카테고리, 보관방법에 따른 반품 가능여부, 반품 가능기간 계산
    public ApiResult<?> getReturnPeriod(String ilCategoryStandardId, String storageMethodType);

    /*
     * 배송 / 발주 정보 조회 로직 End
     */

    /*
     * 품목별 상품 상세 이미지 조회 로직 Start
     */

    // 해당 품목의 기등록된 상품 상세 이미지 목록 조회
    public ApiResult<?> getItemImage(String ilItemCode, String ilItemApprId);

    /*
     * 품목별 상품 상세 이미지 조회 로직 End
     */

    /*
     * 마스터 품목 등록 관련 로직 Start
     */

    // 마스터 품목 등록
    public ApiResult<?> addItem(ItemRegisterRequestDto itemRegisterRequestDto) throws BaseException, Exception;

    // 마스터 품목 가격정보ORIG 수정
    public ApiResult<?> addItemPriceOrig(ItemRegisterRequestDto itemRegisterRequestDto) throws Exception;

    // 신규 품목 이미지 등록시 해당 품목 이미지의 리사이징 이미지 파일들 생성 / 각 리사이징 파일명 VO 에 세팅
    // 마스터 품목 수정시에도 사용할 수 있도록 Biz 에 등록함
    public void resizeItemImage(ItemImageRegisterVo addItemImageVo, UploadFileDto uploadFileDto, String publicRootStoragePath);

    /*
     * 마스터 품목 등록 관련 로직 End
     */

}
