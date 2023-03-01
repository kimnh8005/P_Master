package kr.co.pulmuone.v1.order.front.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
public class OrderInfoFromRewardRequestDto {

    @ApiModelProperty(value = "유저 PK")
    private Long urUserId;

    @ApiModelProperty(value = "조회 시작일자")
    private String startDate;

    @ApiModelProperty(value = "조회 종료일자")
    private String endDate;

    @ApiModelProperty(value = "고객보상제 PK")
    private Long csRewardId;

    @ApiModelProperty(value = "상품 PK")
    private List<Long> goodsIdList;

}
