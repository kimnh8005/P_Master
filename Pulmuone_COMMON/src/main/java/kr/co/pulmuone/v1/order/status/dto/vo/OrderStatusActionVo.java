package kr.co.pulmuone.v1.order.status.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "주문상태실행 VO")
public class OrderStatusActionVo {

	@ApiModelProperty(value = "주문상태 실행 PK")
    private Long actionId;

    @ApiModelProperty(value = "실행구분")
    private String actionType;

    @ApiModelProperty(value = "실행 ID")
    private String actionExecId;

    @ApiModelProperty(value = "실행구분명")
    private String actionExecNm;

    @ApiModelProperty(value = "실행타겟")
    private String actionTarget;

    @ApiModelProperty(value = "실행할 URL ")
    private String actionTargetUrl;

    @ApiModelProperty(value = "Confirm 메세지")
    private String actionConfirm;

    @ApiModelProperty(value = "실행 보조 컬럼1")
    private String actionAttr1;

    @ApiModelProperty(value = "실행 보조 컬럼2")
    private String actionAttr2;

    @ApiModelProperty(value = "실행 보조 컬럼3")
    private String actionAttr3;


}
