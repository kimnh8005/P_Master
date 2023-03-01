package kr.co.pulmuone.v1.promotion.point.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.enums.PointEnums;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@ApiModel(description = "적립금 미적립 내역 VO")
public class PointUnpaidIssueVo {

    @ApiModelProperty(value = "재적립(환불) 연관 적립금 이력 고유값")
    private Long refDproPointUsedDetlId;

    @ApiModelProperty(value = "재적립 요청 금액")
    private Long issueVal;

    @ApiModelProperty(value = "참조 1 ex 등주문번호, 프로모션 번호")
    private String refNo1;

    @ApiModelProperty(value = "참조 2")
    private String refNo2;

    @ApiModelProperty(value = "조직 코드")
    private String deptCd;

    @ApiModelProperty(value = "적립금 처리 유형")
    private PointEnums.PointProcessType pointProcessType;

    @ApiModelProperty(value = "적립금 정산 유형")
    private PointEnums.PointSettlementType pointSettlementType;

}
