package kr.co.pulmuone.v1.goods.goods.dto;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "묶음 상품 리스트 Request")
public class GoodsPackageListRequestDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "단일조건 _ 복수조건 검색")
	private String searchType;

	@ApiModelProperty(value = "상품코드 검색")
	private String codeType;

	@ApiModelProperty(value = "상품코드 검색")
	private String goodsCodes;

	@ApiModelProperty(value = "상품코드 ID")
	private String goodsId;

	@ApiModelProperty(value = "엑셀 양식 구분")
	private String excelDom;

	@ApiModelProperty(value = "상품코드 Array", required = false)
    private ArrayList<String> goodsCodeArray;

	@ApiModelProperty(value = "상품명")
	private String goodsName;

	@ApiModelProperty(value = "업체기준")
    private String companyStandardType;

    @ApiModelProperty(value = "공급업체")
    private String supplierId;

    @ApiModelProperty(value = "공급업체 기준 출고처")
    private String supplierByWarehouseId;

    @ApiModelProperty(value = "출고처 그룹")
    private String warehouseGroup;

    @ApiModelProperty(value = "출고처 그룹 기준 출고처")
    private String warehouseId;

    @ApiModelProperty(value = "카테고리 구분")
    private String categoryType;

    @ApiModelProperty(value = "표준카테고리 대분류")
    private String categoryStandardDepth1;

    @ApiModelProperty(value = "표준카테고리 중분류")
    private String categoryStandardDepth2;

    @ApiModelProperty(value = "표준카테고리 소분류")
    private String categoryStandardDepth3;

    @ApiModelProperty(value = "표준카테고리 세분류")
    private String categoryStandardDepth4;

    @ApiModelProperty(value = "전시카테고리 대분류")
    private String categoryDepth1;

    @ApiModelProperty(value = "전시카테고리 중분류")
    private String categoryDepth2;

    @ApiModelProperty(value = "전시카테고리 소분류")
    private String categoryDepth3;

    @ApiModelProperty(value = "전시카테고리 세분류")
    private String categoryDepth4;

    @ApiModelProperty(value = "브랜드")
    private String brandId;

    @ApiModelProperty(value = "전시브랜드")
    private String dpBrandId;

    @ApiModelProperty(value = "기간검색유형")
    private String dateSearchType;

    @ApiModelProperty(value = "기간검색 시작일자")
    private String dateSearchStart;

    @ApiModelProperty(value = "기간검색 종료일자")
    private String dateSearchEnd;

    @ApiModelProperty(value = "판매상태")
    private String saleStatus;

    @ApiModelProperty(value = "판매상태 리스트")
    private List<String> saleStatusList;

    @ApiModelProperty(value = "접근권한 출고처 ID 리스트")
    private List<String> listAuthWarehouseId;

    @ApiModelProperty(value = "접근권한 공급업체 ID 리스트")
    private List<String> listAuthSupplierId;

}
