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
public class MissOutboundStaticsRequestDto extends BaseRequestPageDto {

    @ApiModelProperty(value = "검색시작일자")
    private String startDe;

    @ApiModelProperty(value = "검색종료일자")
    private String endDe;

    @ApiModelProperty(value = "VAT 포함")
    private String includeVatYn;

    @ApiModelProperty(value = "공급업체 Filter")
    private String supplierFilter;

    @ApiModelProperty(value = "공급업체 Filter")
    private String supplierFilterName;

    @ApiModelProperty(value = "공급업체 List")
    private List<String> supplierList;

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

    @ApiModelProperty(value = "배송유형 Filter")
    private String searchDelivFilter;

    @ApiModelProperty(value = "배송유형 Filter")
    private String searchDelivFilterName;

    @ApiModelProperty(value = "배송유형 List")
    private List<String> searchDelivList;

    public String getSearchInfo() {
        String result = "검색기준:기준기간:" + this.getStartDe() + "~" + this.getEndDe();

        if ("Y".equals(this.getIncludeVatYn())) {
            result += "(VAT 포함)";
        } else {
            result += "(VAT 미포함)";
        }

        result += "/공급업체:";
        if (this.getSupplierFilter().contains("ALL")) {
            result += "전체";
        } else {
            result += this.getSupplierFilterName();
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

        result += "/배송유형:";
        if (this.getSearchDelivFilter().contains("ALL")) {
            result += "전체";
        } else {
            result += this.getSearchDelivFilterName();
        }

        return result;
    }

}
