package kr.co.pulmuone.v1.outmall.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "외부몰 클레임 주문 처리상태 변경 Request Dto")
public class ClaimOrderProgressRequestDto {

    @ApiModelProperty(value = "처리상태")
    private String processCode;

    @ApiModelProperty(value = "관리자 PK")
    private Long adminId;

    @ApiModelProperty(value = "클레임 PK - 단건")
    private Long ifEasyadminOrderClaimId;

    @ApiModelProperty(value = "클레임 PK Param - 다건")
    private String ifEasyadminOrderClaimIdParam;

    @ApiModelProperty(value = "클레임 PK List - 다건", hidden = true)
    private List<Long> ifEasyadminOrderClaimIdList;

}

