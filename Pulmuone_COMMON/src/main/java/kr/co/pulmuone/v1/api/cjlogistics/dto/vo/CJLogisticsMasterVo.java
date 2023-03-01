package kr.co.pulmuone.v1.api.cjlogistics.dto.vo;

import java.util.List;

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
@ApiModel(description = "CJ마스터 Vo")
public class CJLogisticsMasterVo {
    @JsonAlias({ "rcvernm" })
    @ApiModelProperty(value = "수신자명")
    private String rcvernm;

    @JsonAlias({ "insuernm" })
    @ApiModelProperty(value = "인수자명")
    private String insuernm;

    @JsonAlias({ "qty" })
    @ApiModelProperty(value = "품목수량")
    private String qty;

    @JsonAlias({ "sendernm" })
    @ApiModelProperty(value = "송화인명")
    private String sendernm;

    @ApiModelProperty(value = "CJ 트캐링 목록")
    List<CJLogisticsTrackingVo> tracking;
}
