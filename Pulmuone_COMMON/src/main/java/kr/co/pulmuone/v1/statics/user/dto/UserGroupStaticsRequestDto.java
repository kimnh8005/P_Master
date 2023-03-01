package kr.co.pulmuone.v1.statics.user.dto;

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
public class UserGroupStaticsRequestDto extends BaseRequestPageDto {

    @ApiModelProperty(value = "회원등급 그룹")
    private String userMaster;

    @ApiModelProperty(value = "회원등급 그룹")
    private String userMasterName;

    @ApiModelProperty(value = "검색기준")
    private String searchTp;

    @ApiModelProperty(value = "VAT 포함 여부")
    private String vatSumYn;

    @ApiModelProperty(value = "기준시작일시")
    private String startDe;

    @ApiModelProperty(value = "기준종료일시")
    private String endDe;

    @ApiModelProperty(value = "판매채널유형 코드")
    private String agentTypeCd;

    @ApiModelProperty(value = "판매채널유형 코드")
    private String agentTypeCdName;

    @ApiModelProperty(value = "판매채널유형 리스트")
    private List<String> agentTypeCdList;

    public String getSearchInfo() {
        StringBuilder result = new StringBuilder();
        result.append("회원등급:")
                .append(this.getUserMasterName());
        result.append("/검색기준:");
        if (this.getSearchTp().equals(StaticsEnums.searchType.ORDER_DATE.getCode())) {
            result.append(StaticsEnums.searchType.ORDER_DATE.getCodeName());
        } else if (this.getSearchTp().equals(StaticsEnums.searchType.PAYMENT_DATE.getCode())) {
            result.append(StaticsEnums.searchType.PAYMENT_DATE.getCodeName());
        } else if (this.getSearchTp().equals(StaticsEnums.searchType.SALES_DATE.getCode())) {
            result.append(StaticsEnums.searchType.SALES_DATE.getCodeName());
        }
        result.append("/VAT");
        if ("Y".equals(this.getVatSumYn())) {
            result.append("포함");
        } else {
            result.append("제외");
        }
        result.append("/기준기간:");
        result.append(DateUtil.convertFormatNew(this.getStartDe(), "yyyyMMdd", "yyyy-MM-dd"))
                .append("~")
                .append(DateUtil.convertFormatNew(this.getEndDe(), "yyyyMMdd", "yyyy-MM-dd"));
        result.append("/판매채널유형:");
        if (this.getAgentTypeCd().contains("ALL")) {
            result.append("전체");
        } else {
            result.append(this.getAgentTypeCdName());
        }

        return result.toString();
    }

}
