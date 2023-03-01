package kr.co.pulmuone.v1.order.status.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "주문유형별 상태실행 VO")
public class OrderStatusTypeActionVo {

	@ApiModelProperty(value = "주문상태코드")
    private String statusCd;

    @ApiModelProperty(value = "주문상태코드명")
    private String statusNm;

    @ApiModelProperty(value = "주문상태코드+(주문상태코드명)")
    private String statusExplain;

    @ApiModelProperty(value = "주문상태 사용구분")
    private String useType;

    @ApiModelProperty(value = "유형코드")
    private String typeCd;

    @ApiModelProperty(value = "노출상태명")
    private String actionStatusNm;

    @ApiModelProperty(value = "노출버튼명")
    private String actionNm;

    @ApiModelProperty(value = "실행ID")
    private Long actionId;

    @ApiModelProperty(value = "실행순번")
    private Long actionSeq;
}
