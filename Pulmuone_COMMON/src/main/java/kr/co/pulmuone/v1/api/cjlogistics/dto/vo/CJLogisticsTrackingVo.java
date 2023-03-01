package kr.co.pulmuone.v1.api.cjlogistics.dto.vo;

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
@ApiModel(description = "CJ트래킹 Vo")
public class CJLogisticsTrackingVo {

    @JsonAlias({ "brannm" })
    @ApiModelProperty(value = "점소명")
    private String shopName;

    @JsonAlias({ "gbnm" })
    @ApiModelProperty(value = "스캔구분값")
    private String scanTypeName;

    @JsonAlias({ "scandt" })
    @ApiModelProperty(value = "스캔일자")
    private String scanDate;

    @JsonAlias({ "scanhr" })
    @ApiModelProperty(value = "스캔시간")
    private String scanTime;

    @JsonAlias({ "tel" })
    @ApiModelProperty(value = "연락처")
    private String telephone;
}
