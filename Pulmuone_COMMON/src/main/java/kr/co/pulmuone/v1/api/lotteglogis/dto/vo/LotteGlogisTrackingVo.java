package kr.co.pulmuone.v1.api.lotteglogis.dto.vo;

import com.fasterxml.jackson.annotation.JsonAlias;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@ApiModel(description = "롯데트래킹 Vo")
public class LotteGlogisTrackingVo {

    @JsonAlias({ "GODS_STAT_CD" })
    @ApiModelProperty(value = "화물상태코드")
    private String trackingStatusCode;

    @JsonAlias({ "GODS_STAT_NM" })
    @ApiModelProperty(value = "화물상태명")
    private String trackingStatusName;

    @JsonAlias({ "SCAN_YMD" })
    @ApiModelProperty(value = "처리일자")
    private String scanDate;

    @JsonAlias({ "SCAN_TME" })
    @ApiModelProperty(value = "처리시간")
    private String scanTime;

    @JsonAlias({ "BRNSHP_CD" })
    @ApiModelProperty(value = "처리점소코드")
    private String processingShopCode;

    @JsonAlias({ "BRNSHP_NM" })
    @ApiModelProperty(value = "처리점소명")
    private String processingShopName;

    @JsonAlias({ "BRNSHP_TEL" })
    @ApiModelProperty(value = "처리점소전화번호")
    private String processingShopTelephone;

    @JsonAlias({ "PTN_BRNSHP_CD" })
    @ApiModelProperty(value = "상대점소코드")
    private String partnerShopCode;

    @JsonAlias({ "PTN_BRNSHP_NM" })
    @ApiModelProperty(value = "상대점소명")
    private String partnerShopName;

    @JsonAlias({ "PTN_BRNSHP_TEL" })
    @ApiModelProperty(value = "상대점소전화번호")
    private String partnerShopTelephone;

    @JsonAlias({ "EMP_NO" })
    @ApiModelProperty(value = "처리사원번호")
    private String processingEmployeeNumber;

    @JsonAlias({ "EMP_NM" })
    @ApiModelProperty(value = "처리사원명")
    private String processingEmployeeName;

    @JsonAlias({ "EMP_TEL" })
    @ApiModelProperty(value = "처리사원전화번호")
    private String processingEmployeeTelephone;
}
