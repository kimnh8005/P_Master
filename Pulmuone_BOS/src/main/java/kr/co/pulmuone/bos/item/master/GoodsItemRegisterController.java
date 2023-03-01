package kr.co.pulmuone.bos.item.master;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import kr.co.pulmuone.bos.comm.constant.BosStorageInfoEnum;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.interfaceType.StorageInfoBaseType.StorageType;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.goods.item.dto.ItemModifyRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.ItemRegisterRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.ItemSpecFieldModifiedMessageRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.MasterItemListRequestDto;
import kr.co.pulmuone.v1.goods.item.service.GoodsItemRegisterBiz;
import lombok.extern.slf4j.Slf4j;

/**
 * <PRE>
 * Forbiz Korea
 * 마스터 품목 등록 Controller
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 10. 26.               박주형         최초작성
 * =======================================================================
 * </PRE>
 */
@Slf4j
@RestController
class GoodsItemRegisterController {

    @Autowired
    private GoodsItemRegisterBiz goodsItemRegisterBiz;

    /**
     * @Desc ERP API 로 품목 정보 조회 API
     *
     * @param searchOption   : ERP API 품목 검색 옵션
     * @param searchValue    : ERP API 품목 검색 값
     * @param masterItemType : 마스터 품목유형
     *
     * @return ApiResult<?> : ERP API 품목 검색 ApiResult
     */
    @GetMapping("/admin/item/master/register/getErpItemPopupList")
    @ApiOperation(value = "ERP API 로 품목 정보 검색")
    @ApiImplicitParams({ //
            @ApiImplicitParam(name = "searchOption", value = "ERP 품목 검색 옵션", required = true, dataType = "string"), //
            @ApiImplicitParam(name = "searchValue", value = "ERP 품목 검색 값", required = true, dataType = "string"), //
            @ApiImplicitParam(name = "masterItemType", value = "마스터 품목유형", required = true, dataType = "string"), //
    })
    public ApiResult<?> getErpItemSearchList( //
            @RequestParam(value = "searchOption", required = true) String searchOption //
            , @RequestParam(value = "searchValue", required = true) String searchValue //
            , @RequestParam(value = "masterItemType", required = true) String masterItemType //
    ) {

        return goodsItemRegisterBiz.getErpItemApiList(searchOption, searchValue, masterItemType);

    }

    /**
     * @Desc ERP API 로 품목 영양정보 조회 API
     *
     * @param ilItemCode : 품목 코드
     *
     * @return ApiResult<?> : ERP API 영양 정보 조회 ApiResult
     */
    @GetMapping("/admin/item/master/register/getErpNutritionApi")
    @ApiOperation(value = "ERP API 로 품목 영양정보 조회")
    @ApiImplicitParams({ //
            @ApiImplicitParam(name = "ilItemCode", value = "품목 코드", required = true, dataType = "string") //
    })
    public ApiResult<?> getErpNutritionApi( //
            @RequestParam(value = "ilItemCode", required = true) String ilItemCode //
    ) {

        return goodsItemRegisterBiz.getErpNutritionApi(ilItemCode);

    }

    /**
     * @Desc ERP API 로 품목 가격정보 조회 API
     *
     * @param ilItemCode       : 품목 코드
     * @param erpLegalTypeCode : ERP 법인코드 ( "PFF" ( 풀무원식품 ), "OGH" ( 올가 ), "FDM" ( 푸드머스 ), "PGS" ( 건강생활 ) )
     * @param erpSalesType     : ERP 행사구분 ( "정상", "행사" )
     *
     * @return ApiResult<?> : ERP API 가격 정보 조회 ApiResult
     */
    @GetMapping("/admin/item/master/register/getErpPriceApi")
    @ApiOperation(value = "ERP API 로 품목 가격정보 조회")
    @ApiImplicitParams({ //
            @ApiImplicitParam(name = "ilItemCode", value = "품목 코드", required = true, dataType = "string") //
            , @ApiImplicitParam(name = "erpLegalTypeCode", value = "ERP 법인코드", required = false, dataType = "string") //
            , @ApiImplicitParam(name = "erpSalesType", value = "ERP 행사구분", required = false, dataType = "string") //
    })
    public ApiResult<?> getErpPriceApi( //
            @RequestParam(value = "ilItemCode", required = true) String ilItemCode // 품목 코드
            , @RequestParam(value = "erpLegalTypeCode", required = false) String erpLegalTypeCode // ERP 법인코드 ( 필수 아님 )
            , @RequestParam(value = "erpSalesType", required = false) String erpSalesType // ERP 행사 구분 ( 필수 아님 )
    ) {

        return goodsItemRegisterBiz.getErpPriceApi(ilItemCode, erpLegalTypeCode, erpSalesType);

    }

    /**
     * @Desc 마스터 품목 복사 : ERP 연동 or 미연동 품목의 원본 정보 조회 API
     *
     * @param ilItemCode    : 품목 코드
     * @param isErpItemLink : ERP 연동 여부
     *
     * @return ApiResult<?> : ERP 연동 or 미연동 품목의 원본 정보 ApiResult
     */
    @GetMapping("/admin/item/master/register/getMasterItem")
    @ApiOperation(value = "마스터 품목 복사 : ERP 연동 or 미연동 품목의 원본 정보 조회")
    @ApiImplicitParams({ //
            @ApiImplicitParam(name = "ilItemCode", value = "품목 코드", required = true, dataType = "string") //
            , @ApiImplicitParam(name = "isErpItemLink", value = "ERP 연동 여부", required = true, dataType = "string") //
    })
    public ApiResult<?> getMasterItem( //
            @RequestParam(value = "ilItemCode", required = true) String ilItemCode //
            , @RequestParam(value = "isErpItemLink", required = true) String isErpItemLink //
    ) {

        return goodsItemRegisterBiz.getMasterItem(ilItemCode, isErpItemLink);

    }

    /**
     * @Desc 상품정보 제공고시 상품군 코드 목록 조회 API
     *
     * @return ApiResult<?> : 상품정보 제공고시 상품군 코드 목록 조회 ApiResult
     */
    @GetMapping("/admin/item/master/register/getItemSpecCode")
    @ApiOperation(value = "상품정보 제공고시 상품군 코드 조회")
    public ApiResult<?> getItemSpecCode() {

        return goodsItemRegisterBiz.getItemSpecCode();

    }

    /**
     * @Desc 상품정보 제공고시 상품군별 분류 항목 전체 조회  API => 특수 항목에 대한 세부 메시지 및 수정 가능여부 등 설정
     *
     *       품목 코드 param 전달시에는 해당 품목 코드의 상품정보 제공고시 상세 항목을 같이 조회
     *
     * @param ilItemCode : 품목 코드
     * @param ilItemApprId : 품목 승인 코드
     *
     * @return ApiResult<?> : 상품정보 제공고시 상품군별 분류 항목 전체 조회 ApiResult
     */
    @GetMapping("/admin/item/master/register/getItemSpecList")
    @ApiOperation(value = "상품정보 제공고시 상품군별 분류 항목 전체 조회")
    @ApiImplicitParams({ //
            @ApiImplicitParam(name = "ilItemCode", value = "품목 코드", required = false, dataType = "string") //
            , @ApiImplicitParam(name = "ilItemApprId", value = "품목 승인 코드", required = false, dataType = "string") //
    })
    public ApiResult<?> getItemSpecList( //
            @RequestParam(value = "ilItemCode", required = false) String ilItemCode //
            , @RequestParam(value = "ilItemApprId", required = false) String ilItemApprId //
    ) {

        return goodsItemRegisterBiz.getItemSpecList(ilItemCode, ilItemApprId);

    }

    /**
     * @Desc 해당 상품정보제공고시 항목 코드의 수정 메시지 조회 API
     *
     * @param specFieldCode      : 상품정보 제공고시 항목 코드
     * @param urSupplierId       : 공급업체 PK
     * @param distributionPeriod : 유통기한
     *
     * @return ApiResult<?> : 해당 상품정보제공고시 항목 코드의 수정 메시지 조회 ApiResult
     */
    @GetMapping("/admin/item/master/register/getItemSpecFieldDetailMessage")
    @ApiOperation(value = "해당 상품정보제공고시 항목 코드의 수정 메시지 조회")
    @ApiImplicitParams({ //
            @ApiImplicitParam(name = "specFieldCode", value = "상품정보 제공고시 항목 코드", required = true, dataType = "string") //
            , @ApiImplicitParam(name = "urSupplierId", value = "공급업체 PK", required = true, dataType = "string") //
            , @ApiImplicitParam(name = "distributionPeriod", value = "유통기한", required = true, dataType = "string") //
    })
    public ApiResult<?> getItemSpecFieldDetailMessage( //
            @RequestParam(value = "specFieldCode", required = true) String specFieldCode // 상품정보 제공고시 항목 코드
            , @RequestParam(value = "urSupplierId", required = true) String urSupplierId // 공급업체 PK
            , @RequestParam(value = "distributionPeriod", required = true) String distributionPeriod // 유통기한
    ) {

        // 추후 확장성을 고려하여 request dto 사용
        ItemSpecFieldModifiedMessageRequestDto itemSpecFieldModifiedMessageRequestDto = ItemSpecFieldModifiedMessageRequestDto.builder() //
                .specFieldCode(specFieldCode) // 상품정보 제공고시 항목 코드
                .urSupplierId(urSupplierId) // 공급업체 PK
                .distributionPeriod(Integer.valueOf(distributionPeriod)) // 유통기한
                .build();

        return goodsItemRegisterBiz.getItemSpecFieldDetailMessage(itemSpecFieldModifiedMessageRequestDto);

    }

    /**
     * @Desc 해당 품목코드로 등록 가능한 영양정보 분류 코드 조회 API
     *
     *       ERP 미연동 품목인 경우, BOS 상에 등록된 해당 품목 코드의 영양정보 세부항목 조회 후 같이 반환 : 마스터 품목 복사 기능에서 사용함
     *
     * @param ilItemCode    : 품목 코드
     * @param isErpItemLink : ERP 연동 여부, 단, 마스터 품목 수정 화면에서 품목 정보 수정시에는 화면에서 항상 false 전달함
     *
     * @return ApiResult : 해당 품목코드로 등록 가능한 영양정보 분류 코드 ApiResult
     */
    @GetMapping("/admin/item/master/register/getAddAvailableNutritionCodeList")
    @ApiOperation(value = "등록 가능한 영양정보 분류 코드 조회 / ERP 미연동 품목 or 품목 정보 수정시 해당 품목코드로 등록된 영양정보 세부항목 같이 조회")
    @ApiImplicitParams({ //
            @ApiImplicitParam(name = "ilItemCode", value = "품목 코드", required = true, dataType = "string") //
            , @ApiImplicitParam(name = "isErpItemLink", value = "ERP 연동 여부", required = true, dataType = "string") //
            , @ApiImplicitParam(name = "ilItemApprId", value = "품목 승인 코드", required = false, dataType = "string") //
    })
    public ApiResult<?> getAddAvailableNutritionCodeList( //
            @RequestParam(value = "ilItemCode", required = true) String ilItemCode //
            , @RequestParam(value = "isErpItemLink", required = true) String isErpItemLink //
            , @RequestParam(value = "ilItemApprId", required = false) String ilItemApprId //
    ) {

        return goodsItemRegisterBiz.getAddAvailableNutritionCodeList(ilItemCode, isErpItemLink, ilItemApprId);

    }

    /**
     * @Desc 상품 인증정보 코드 조회 API
     *
     * @return ApiResult : 상품 인증정보 코드 조회 ApiResult
     */
    @GetMapping("/admin/item/master/register/getItemCertificationCode")
    @ApiOperation(value = "상품 인증정보 코드 조회")
    public ApiResult<?> getItemCertificationCode() {

        return goodsItemRegisterBiz.getItemCertificationCode();

    }

    /**
     * @Desc 품목별 상품 인증정보 조회 API
     *
     * @param ilItemCode : 품목 코드
     *
     * @return ApiResult : 품목별 상품 인증정보 조회 ApiResult
     */
    @GetMapping("/admin/item/master/register/getItemCertificationList")
    @ApiOperation(value = "품목별 상품 인증정보 조회")
    @ApiImplicitParams({ //
            @ApiImplicitParam(name = "ilItemCode", value = "품목 코드", required = true, dataType = "string") //
            , @ApiImplicitParam(name = "ilItemApprId", value = "승인 코드", required = false, dataType = "string") //
    })
    public ApiResult<?> getItemCertificationList( //
            @RequestParam(value = "ilItemCode", required = true) String ilItemCode //
            , @RequestParam(value = "ilItemApprId", required = false) String ilItemApprId //
    ) {

        return goodsItemRegisterBiz.getItemCertificationList(ilItemCode, ilItemApprId);

    }

    /**
     * @Desc 해당 공급업체 PK 값에 해당하는 출고처 그룹, 출고처 정보 조회 API
     *
     * @param urSupplierId : 공급업체 PK
     *
     * @return ApiResult : 공급업체 PK 별 출고처 그룹, 출고처 정보 조회 dto
     */
    @GetMapping("/admin/item/master/register/getItemWarehouseCode")
    @ApiOperation(value = "해당 공급업체 PK 값에 해당하는 출고처 그룹, 출고처 코드 정보 조회")
    @ApiImplicitParams({ //
            @ApiImplicitParam(name = "urSupplierId", value = "공급업체 PK", required = true, dataType = "string")
            , @ApiImplicitParam(name = "masterItemType", value = "마스터 품목 유형", required = true, dataType = "masterItemType")
    })
    public ApiResult<?> getItemWarehouseCode( //
            @RequestParam(value = "urSupplierId", required = true) String urSupplierId
            , @RequestParam(value = "masterItemType", required = true) String masterItemType
    ) {

        return goodsItemRegisterBiz.getItemWarehouseCode(urSupplierId, masterItemType);

    }

    /**
     * @Desc 해당 품목코드로 등록된 출고처 정보 조회 API
     *
     * @param ilItemCode : 품목 코드
     *
     * @return ApiResult : 해당 품목코드로 등록된 출고처 정보 조회 ApiResult
     */
    @GetMapping("/admin/item/master/register/getItemWarehouseList")
    @ApiOperation(value = "해당 품목코드로 등록된 출고처 정보 조회")
    @ApiImplicitParams({ //
            @ApiImplicitParam(name = "ilItemCode", value = "품목 코드", required = true, dataType = "string") //
    })
    public ApiResult<?> getItemWarehouseList( //
            @RequestParam(value = "ilItemCode", required = true) String ilItemCode //
    ) {

        return goodsItemRegisterBiz.getItemWarehouseList(ilItemCode);

    }

    /**
     * @Desc 해당 공급업체 PK 값에 해당하는 발주유형 정보 조회 API
     *
     * @param urSupplierId : 공급업체 PK
     *
     * @return ApiResult : 발주유형 정보 ApiResult
     */
    @GetMapping("/admin/item/master/register/getItemPoTypeCode")
    @ApiOperation(value = "해당 공급업체 PK 값에 해당하는 발주유형 정보 조회")
    @ApiImplicitParams({ //
            @ApiImplicitParam(name = "urSupplierId", value = "공급업체 PK", required = true, dataType = "string") //
    })
    public ApiResult<?> getItemPoTypeCode( //
            @RequestParam(value = "urSupplierId", required = true) String urSupplierId,
            @RequestParam(value = "erpPoType", required = false) String erpPoType,
            @RequestParam(value = "selectType", required = false) String selectType

    ) {
        return goodsItemRegisterBiz.getItemPoTypeCode(urSupplierId, erpPoType, selectType);

    }

    /**
     * @Desc 해당 품목 정보의 표준 카테고리, 보관방법에 따른 반품 가능여부, 반품 가능기간 계산 API
     *
     * @param ilCategoryStandardId : 표준 카테고리 PK
     * @param storageMethodType    : 보관방법
     *
     * @return ApiResult : 반품 가능여부, 반품가능기간 계산 ApiResult
     */
    @GetMapping("/admin/item/master/register/getReturnPeriod")
    @ApiOperation(value = "해당 품목 정보의 반품 가능여부, 반품가능기간 계산 API")
    @ApiImplicitParams({ //
            @ApiImplicitParam(name = "ilCategoryStandardId", value = "표준 카테고리 PK", required = true, dataType = "string") //
            , @ApiImplicitParam(name = "storageMethodType", value = "보관방법", required = true, dataType = "string") //
    })
    public ApiResult<?> getReturnPeriod( //
            @RequestParam(value = "ilCategoryStandardId", required = true) String ilCategoryStandardId //
            , @RequestParam(value = "storageMethodType", required = true) String storageMethodType //
    ) {

        return goodsItemRegisterBiz.getReturnPeriod(ilCategoryStandardId, storageMethodType);

    }

    /**
     * @Desc 해당 품목코드로 등록된 상품 상세 이미지 목록 조회 API
     *
     * @param ilItemCode : 품목 코드
     *
     * @return ApiResult<?> : 해당 품목의 기등록된 상품 상세 이미지 목록 조회 ApiResult
     */
    @GetMapping("/admin/item/master/register/getItemImageList")
    @ApiOperation(value = "해당 품목코드로 등록된 상품 상세 이미지 목록 조회")
    @ApiImplicitParams({ //
            @ApiImplicitParam(name = "ilItemCode", value = "품목 코드", required = true, dataType = "string") //
            , @ApiImplicitParam(name = "ilItemApprId", value = "승인 코드", required = false, dataType = "string") //
    })
    public ApiResult<?> getItemImageList( //
            @RequestParam(value = "ilItemCode", required = true) String ilItemCode //
            , @RequestParam(value = "ilItemApprId", required = false) String ilItemApprId //
    ) {

    	return goodsItemRegisterBiz.getItemImage(ilItemCode, ilItemApprId);

    }

    /**
     * @Desc 마스터 품목 등록 API
     *
     * @param ItemRegisterRequestDto : 마스터 품목 등록 request dto
     *
     * @return ApiResult : 마스터 품목 등록 ApiResult
     */
    @PostMapping("/admin/item/master/register/addItem")
    public ApiResult<?> addItem(@RequestBody ItemRegisterRequestDto itemRegisterRequestDto) throws Exception {

        // 품목 이미지 등록 관련 : BosStorageInfoEnum 에 선언된 public 저장소의 최상위 저장 디렉토리 경로를 dto 에 세팅
        String publicRootStoragePath = BosStorageInfoEnum.getRootStoragePath(StorageType.PUBLIC.name());
        itemRegisterRequestDto.setImageRootStoragePath(publicRootStoragePath);

        try {

            return goodsItemRegisterBiz.addItem(itemRegisterRequestDto);

        } catch (BaseException be) {
            log.error(be.getMessage(), be);
            return ApiResult.result(be.getMessageEnum());

        } catch (Exception e) { // 기타 예외 발생시
            log.error(e.getMessage(), e);
            throw e;

        }
    }

    /**
     * @Desc 마스터 품목 정보 수정 API
     *
     * @param ItemModifyRequestDto : 마스터 품목 수정 request dto
     *
     * @return ApiResult : 마스터 품목 수정 ApiResult
     * @throws Exception
     */
    @PostMapping("/admin/item/master/register/addItemPriceOrig")
    @ApiOperation(value = "마스터 품목 정보 수정")
    public ApiResult<?> addItemPriceOrig( //
            @RequestBody ItemRegisterRequestDto itemRegisterRequestDto // 마스터 품목 정보 수정 request dto
    ) throws Exception {
        try {

            return goodsItemRegisterBiz.addItemPriceOrig(itemRegisterRequestDto);

        } catch (BaseException be) {
            log.error(be.getMessage(), be);
            return ApiResult.result(be.getMessageEnum());

        } catch (Exception e) { // 기타 예외 발생시
            log.error(e.getMessage(), e);
            throw e;

        }

    }

}
