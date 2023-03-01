package kr.co.pulmuone.v1.statics.outbound.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import kr.co.pulmuone.v1.comm.enums.StaticsEnums;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "OutboundStaticsRequestDto")
public class OutboundStaticsRequestDto extends BaseRequestPageDto {

    @ApiModelProperty(value = "검색기준")
    private String searchType;

    @ApiModelProperty(value = "기준기간 조회구분")
    private String searchDateType;

    @ApiModelProperty(value = "기준기간 조회년")
    private String findYear;

    @ApiModelProperty(value = "기준기간 조회월")
    private String findMonth;

    @ApiModelProperty(value = "조회 시작일자")
    private String searchDateStart;

    @ApiModelProperty(value = "조회 종료일자")
    private String searchDateEnd;

    @ApiModelProperty(value = "취소주문 제외여부")
    private String exceptClaimOrderYn;

    @ApiModelProperty(value = "공급업체 Filter")
    private String supplierFilter;

    @ApiModelProperty(value = "공급업체 Filter")
    private String supplierFilterName;

    @ApiModelProperty(value = "공급업체 List")
    private List<String> supplierList;

    @ApiModelProperty(value = "보관방법 Filter")
    private String storageMethodFilter;

    @ApiModelProperty(value = "보관방법 Filter")
    private String storageMethodFilterName;

    @ApiModelProperty(value = "보관방법 List")
    private List<String> storageMethodList;

    @ApiModelProperty(value = "출고처그룹코드")
    private String urWarehouseGrpCd;

    @ApiModelProperty(value = "출고처그룹코드")
    private String urWarehouseGrpCdName;

    @ApiModelProperty(value = "출고처 ID")
    private String urWarehouseId;

    @ApiModelProperty(value = "출고처 ID")
    private String urWarehouseIdName;

    @ApiModelProperty(value = "판매처그룹 ID")
    private String sellersGroupCd;

    @ApiModelProperty(value = "판매처그룹 ID")
    private String sellersGroupCdName;

    @ApiModelProperty(value = "판매처 ID")
    private String omSellersId;

    @ApiModelProperty(value = "판매처 ID")
    private String omSellersIdName;

    public String getSearchInfo() {
        String result = "검색기준:";

        if (StaticsEnums.OutboundSearchType.WAREHOUSE.getCode().equals(this.getSearchType())) {
            result += "출고처 기준";
        } else {
            result += "판매처 기준";
        }

        result += "/검색기간:";
        if (StaticsEnums.OutboundSearchDateType.ORDER_DT.getCode().equals(this.getSearchDateType())) {
            result += "주문일자";
        } else if (StaticsEnums.OutboundSearchDateType.IC_DT.getCode().equals(this.getSearchDateType())) {
            result += "결제완료일자";
        } else if (StaticsEnums.OutboundSearchDateType.IF_DT.getCode().equals(this.getSearchDateType())) {
            result += "주문I/F일자";
        } else {
            result += "배송중일자";
        }
        result += " " + this.getFindYear() + "." + this.getFindMonth();
        if ("Y".equals(this.getExceptClaimOrderYn())) {
            result += "(취소주문제외)";
        } else {
            result += "(취소주문포함)";
        }

        result += "/공급업체:";
        if (this.getSupplierFilter().contains("ALL")) {
            result += "전체";
        } else {
            result += this.getSupplierFilterName();
        }

        result += "/보관방법:";
        if (this.getStorageMethodFilter().contains("ALL")) {
            result += "전체";
        } else {
            result += this.getStorageMethodFilterName();
        }

        result += "/출고처 유형:";
        if ("".equals(this.getUrWarehouseGrpCd())) {
            result += "출고처 그룹 전체";
        } else {
            result += this.getUrWarehouseGrpCdName();
        }
        result += ",출고처:";
        if ("".equals(this.getUrWarehouseId())) {
            result += "전체";
        } else {
            result += this.getUrWarehouseIdName();
        }

        result += "/판매처 유형:";
        if ("".equals(this.getSellersGroupCd())) {
            result += "판매처 그룹 전체";
        } else {
            result += this.getSellersGroupCdName();
        }
        result += ",판매처:";
        if ("".equals(this.getOmSellersId())) {
            result += "전체";
        } else {
            result += this.getOmSellersIdName();
        }

        return result;
    }
}
