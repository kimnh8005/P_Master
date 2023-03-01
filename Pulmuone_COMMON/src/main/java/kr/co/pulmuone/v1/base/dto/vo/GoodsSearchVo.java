package kr.co.pulmuone.v1.base.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "상품검색 Vo")
public class GoodsSearchVo {

    @ApiModelProperty(value = "상품 ID")
    private Long goodsId;

    @ApiModelProperty(value = "상품명")
    private String goodsName;

    @ApiModelProperty(value = "품목코드")
    private String itemCode;

    @ApiModelProperty(value = "품목명")
    private String itemName;

    @ApiModelProperty(value = "품목 바코드")
    private String itemBarcode;

    @ApiModelProperty(value = "출고처 ID")
    private Long warehouseId;

    @ApiModelProperty(value = "출고처명")
    private String warehouseName;

    @ApiModelProperty(value = "공급업체 ID")
    private Long supplierId;

    @ApiModelProperty(value = "공급업체 회사 ID")
    private Long supplierCompanyId;

    @ApiModelProperty(value = "공급업체명")
    private String supplierName;

    @ApiModelProperty(value = "브랜드 ID")
    private Long brandId;

    @ApiModelProperty(value = "브랜드명")
    private String brandName;

    @ApiModelProperty(value = "전시 브랜드명")
    private String dpBrandName;

    @ApiModelProperty(value = "표준 카테고리 ID")
    private Long categoryStandardId;

    @ApiModelProperty(value = "표준카테고리명")
    private String categoryStandardDepthName;

    @ApiModelProperty(value = "전시 카테고리 ID")
    private Long categoryId;

    @ApiModelProperty(value = "전시카테고리명")
    private String categoryDepthName;

    @ApiModelProperty(value = "상품유형코드")
    private String goodsTypeCode;

    @ApiModelProperty(value = "상품유형명")
    private String goodsTypeName;

    @ApiModelProperty(value = "판매유형코드")
    private String saleTypeCode;

    @ApiModelProperty(value = "품목유형코드")
    private String itemTypeCode;

    @ApiModelProperty(value = "표장용량 구성정보 노출여부")
    private String packageUnitDisplayYn;

    @ApiModelProperty(value = "회원 구매여부")
    private String purchaseMemberYn;

    @ApiModelProperty(value = "임직원 구매여부")
    private String purchaseEmployeeYn;

    @ApiModelProperty(value = "비회원 구매여부")
    private String purchaseNonmemberYn;

    @ApiModelProperty(value = "WEB 전시여부")
    private String displayWebPcYn;

    @ApiModelProperty(value = "WEB 모바일 전시여부")
    private String displayWebMobileYn;

    @ApiModelProperty(value = "APP 전시여부")
    private String displayAppYn;

    @ApiModelProperty(value = "판매 시작일")
    private String saleStartDate;

    @ApiModelProperty(value = "판매 종료일")
    private String saleEndDate;

    @ApiModelProperty(value = "상품 전시여부")
    private String goodsDisplayYn;

    @ApiModelProperty(value = "판매상태 공통코드")
    private String saleStatusCode;

    @ApiModelProperty(value = "판매상태 공통코드명")
    private String saleStatusCodeName;

    @ApiModelProperty(value = "품목군")
    private String itemGroup;

    @ApiModelProperty(value = "보관방법 공통코드")
    private String storageMethodTypeCode;

    @ApiModelProperty(value = "보관방법명")
    private String storageMethodTypeName;

    @ApiModelProperty(value = "발주유형 ID")
    private String placeOrderId;

    @ApiModelProperty(value = "상품이미지경로")
    private String itemImagePath;

    @ApiModelProperty(value = "원가")
    private int standardPrice;

    @ApiModelProperty(value = "정상가")
    private int recommendedPrice;

    @ApiModelProperty(value = "판매가")
    private int salePrice;

    @ApiModelProperty(value = "배송정책ID")
    private String ilShippingTmplId;

    @ApiModelProperty(value = "배송불가지역 공통코드(UNDELIVERABLE_AREA_TP)")
    private String undeliverableAreaTp;

    @ApiModelProperty(value = "상품가격ID")
    private String ilGoodsPriceId;

    @ApiModelProperty(value = "배송정책명")
    private String name;

    @ApiModelProperty(value = "배송불가여부")
    private String areaShippingDeliveryYn;

    @ApiModelProperty(value = "전시브랜드ID")
    private String dpBrandId;

    @ApiModelProperty(value = "박스입수량")
    private String pcsPerBox;

    @ApiModelProperty(value = "UOM/OMS")
    private String oms;

    @ApiModelProperty(value = "발주유형명")
    private String poTpNm;

    @ApiModelProperty(value = "발주유형seq")
    private String ilPoTpId;

    @ApiModelProperty(value = "발주일(일)")
    private String poSunYn;

    @ApiModelProperty(value = "발주일(월)")
    private String poMonYn;

    @ApiModelProperty(value = "발주일(화)")
    private String poTueYn;

    @ApiModelProperty(value = "발주일(수)")
    private String poWedYn;

    @ApiModelProperty(value = "발주일(목)")
    private String poThuYn;

    @ApiModelProperty(value = "발주일(금)")
    private String poFriYn;

    @ApiModelProperty(value = "발주일(토)")
    private String poSatYn;

    @ApiModelProperty(value = "입고예정일(일)")
    private String scheduledSun;

    @ApiModelProperty(value = "입고예정일(월)")
    private String scheduledMon;

    @ApiModelProperty(value = "입고예정일(화)")
    private String scheduledTue;

    @ApiModelProperty(value = "입고예정일(수)")
    private String scheduledWed;

    @ApiModelProperty(value = "입고예정일(목)")
    private String scheduledThu;

    @ApiModelProperty(value = "입고예정일(금)")
    private String scheduledFri;

    @ApiModelProperty(value = "입고예정일(토)")
    private String scheduledSat;

    @ApiModelProperty(value = "입고예정일(토)")
    private String bundleYn;

    @ApiModelProperty(value = "할인유형")
    private String discountTp;

    @ApiModelProperty(value = "할인유형 명")
    private String discountTpName;

    @ApiModelProperty(value = "품목출고처ID")
    private String ilItemWarehouseId;

    @ApiModelProperty(value = "재고발주여부")
    private String stockOrderYn;

    @ApiModelProperty(value = "출고처 그룹 코드")
    private String warehouseGrpCd;

    @ApiModelProperty(value = "선물하기 허용 여부")
    private String presentYn;

}
