package kr.co.pulmuone.v1.statics.claim.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import kr.co.pulmuone.v1.comm.enums.StaticsEnums;
import kr.co.pulmuone.v1.comm.util.DateUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ClaimReasonStaticsRequestDto extends BaseRequestPageDto {

    @ApiModelProperty(value = "기준시작일시")
    private String startDe;

    @ApiModelProperty(value = "기준종료일시")
    private String endDe;

    @ApiModelProperty(value = "공급업체ID")
    private String urSupplierId;

    @ApiModelProperty(value = "공급업체 명")
    private String supplierName;

    @ApiModelProperty(value = "출고처그룹코드")
    private String urWarehouseGrpCd;

    @ApiModelProperty(value = "출고처그룹코드")
    private String urWarehouseGrpCdName;

    @ApiModelProperty(value = "출고처ID")
    private String urWarehouseId;

    @ApiModelProperty(value = "출고처ID")
    private String urWarehouseIdName;

    @ApiModelProperty(value = "판매처그룹ID")
    private String sellersGroupCd;

    @ApiModelProperty(value = "판매처그룹ID")
    private String sellersGroupCdName;

    @ApiModelProperty(value = "판매처ID")
    private String omSellersId;

    @ApiModelProperty(value = "판매처ID")
    private String omSellersIdName;

    @ApiModelProperty(value = "브랜드유형ID")
    private String brandTp;

    @ApiModelProperty(value = "브랜드유형ID")
    private String brandTpName;

    @ApiModelProperty(value = "표준브랜드ID")
    private String urBrandId;

    @ApiModelProperty(value = "표준브랜드ID")
    private String urBrandIdName;

    @ApiModelProperty(value = "판매채널유형ID")
    private String agentTypeCd;

    @ApiModelProperty(value = "판매채널유형ID")
    private String agentTypeCdName;

    @ApiModelProperty(value = "판매채널유형ID리스트")
    private List<String> agentTypeCdList;

    @ApiModelProperty(value = "회원유형ID")
    private String buyerTypeCd;

    @ApiModelProperty(value = "회원유형ID")
    private String buyerTypeCdName;

    @ApiModelProperty(value = "회원유형ID리스트")
    private List<String> buyerTypeCdList;

    @ApiModelProperty(value = "보관온도유형ID")
    private String storageMethodTp;

    @ApiModelProperty(value = "보관온도유형ID")
    private String storageMethodTpName;

    @ApiModelProperty(value = "카테고리분류")
    private String ctgryTp;

    @ApiModelProperty(value = "카테고리분류")
    private String ctgryTpName;

    @ApiModelProperty(value = "표준카테고리대분류ID")
    private String categoryStandardDepth1;

    @ApiModelProperty(value = "표준카테고리중분류ID")
    private String categoryStandardDepth2;

    @ApiModelProperty(value = "표준카테고리소분류ID")
    private String categoryStandardDepth3;

    @ApiModelProperty(value = "표준카테고리세분류ID")
    private String categoryStandardDepth4;

    @ApiModelProperty(value = "카테고리대분류ID")
    private String categoryDepth1;

    @ApiModelProperty(value = "카테고리중분류ID")
    private String categoryDepth2;

    @ApiModelProperty(value = "카테고리소분류ID")
    private String categoryDepth3;

    @ApiModelProperty(value = "카테고리세분류ID")
    private String categoryDepth4;

    @ApiModelProperty(value = "표준카테고리대분류ID")
    private String ctgryStdIdDepth1Name;

    @ApiModelProperty(value = "표준카테고리중분류ID")
    private String ctgryStdIdDepth2Name;

    @ApiModelProperty(value = "표준카테고리소분류ID")
    private String ctgryStdIdDepth3Name;

    @ApiModelProperty(value = "표준카테고리세분류ID")
    private String ctgryStdIdDepth4Name;

    @ApiModelProperty(value = "카테고리대분류ID")
    private String ctgryIdDepth1Name;

    @ApiModelProperty(value = "카테고리중분류ID")
    private String ctgryIdDepth2Name;

    @ApiModelProperty(value = "카테고리소분류ID")
    private String ctgryIdDepth3Name;

    @ApiModelProperty(value = "카테고리세분류ID")
    private String ctgryIdDepth4Name;

    @ApiModelProperty(value = "상품유형ID")
    private String goodsTpCd;

    @ApiModelProperty(value = "상품유형ID")
    private String goodsTpCdName;

    @ApiModelProperty(value = "상품유형ID리스트")
    private List<String> goodsTpCdList;

    public String getSearchInfo() {
        StringBuilder result = new StringBuilder();
        result.append("자료 갱신일:").append(DateUtil.getCurrentDate("yyyy-MM-dd"));

        result.append("/조회기간:");
        result.append(DateUtil.convertFormatNew(this.getStartDe(), "yyyyMMdd", "yyyy-MM-dd"))
                .append("~")
                .append(DateUtil.convertFormatNew(this.getEndDe(), "yyyyMMdd", "yyyy-MM-dd"));

        result.append("/공급업체:");
        if ("".equals(this.getUrSupplierId())) {
            result.append("전체");
        } else {
            result.append(this.getSupplierName());
        }

        result.append("/출고처 유형:");
        if ("".equals(this.getUrWarehouseGrpCd())) {
            result.append("출고처 그룹 전체");
        } else {
            result.append(this.getUrWarehouseGrpCdName());
        }
        result.append(",출고처:");
        if ("".equals(this.getUrWarehouseId())) {
            result.append("전체");
        } else {
            result.append(this.getUrWarehouseIdName());
        }
        result.append("/판매처 유형:");
        if ("".equals(this.getSellersGroupCd())) {
            result.append("판매처 그룹 전체");
        } else {
            result.append(this.getSellersGroupCdName());
        }
        result.append(",판매처:");
        if ("".equals(this.getOmSellersId())) {
            result.append("전체");
        } else {
            result.append(this.getOmSellersIdName());
        }
        result.append("/브랜드 유형:");
        if ("".equals(this.getBrandTp())) {
            result.append("전체");
        } else {
            result.append(this.getBrandTpName());
        }
        result.append(",브랜드:");
        if ("".equals(this.getUrBrandId())) {
            result.append("전체");
        } else {
            result.append(this.getUrBrandIdName());
        }
        result.append("/판매채널유형:");
        if (this.getAgentTypeCd().contains("ALL")) {
            result.append("전체");
        } else {
            result.append(this.getAgentTypeCdName());
        }
        result.append("/회원유형:");
        if (this.getBuyerTypeCd().contains("ALL")) {
            result.append("전체");
        } else {
            result.append(this.getBuyerTypeCdName());
        }
        result.append("/보관온도:");
        if ("".equals(this.getStorageMethodTp())) {
            result.append("전체");
        } else {
            result.append(this.getStorageMethodTpName());
        }

        result.append("/카테고리:");
        if (StaticsEnums.categoryType.STANDARD.getCode().equals(this.getCtgryTp())) {
            result.append(StaticsEnums.categoryType.STANDARD.getCodeName());
            result.append("/");
            if ("".equals(this.getCategoryStandardDepth1())) {
                result.append("전체");
            } else {
                result.append(this.getCtgryStdIdDepth1Name());
            }
            result.append(">");
            if ("".equals(this.getCategoryStandardDepth2())) {
                result.append("전체");
            } else {
                result.append(this.getCtgryStdIdDepth2Name());
            }
            result.append(">");
            if ("".equals(this.getCategoryStandardDepth3())) {
                result.append("전체");
            } else {
                result.append(this.getCtgryStdIdDepth3Name());
            }
            result.append(">");
            if ("".equals(this.getCategoryStandardDepth4())) {
                result.append("전체");
            } else {
                result.append(this.getCtgryStdIdDepth4Name());
            }
        } else if (StaticsEnums.categoryType.DISPLAY.getCode().equals(this.getCtgryTp())) {
            result.append(StaticsEnums.categoryType.DISPLAY.getCodeName());
            result.append("/");
            if ("".equals(this.getCategoryDepth1())) {
                result.append("전체");
            } else {
                result.append(this.getCtgryIdDepth1Name());
            }
            result.append(">");
            if ("".equals(this.getCategoryDepth2())) {
                result.append("전체");
            } else {
                result.append(this.getCtgryIdDepth2Name());
            }
            result.append(">");
            if ("".equals(this.getCategoryDepth3())) {
                result.append("전체");
            } else {
                result.append(this.getCtgryIdDepth3Name());
            }
            result.append(">");
            if ("".equals(this.getCategoryDepth4())) {
                result.append("전체");
            } else {
                result.append(this.getCtgryIdDepth4Name());
            }
        } else if (StaticsEnums.categoryType.ERP.getCode().equals(this.getCtgryTp())) {
            result.append(StaticsEnums.categoryType.ERP.getCodeName());
        }

        result.append("/상품유형:");
        if (this.getGoodsTpCd().contains("ALL")) {
            result.append("전체");
        } else {
            result.append(this.getGoodsTpCdName());
        }

        return result.toString();
    }

}
