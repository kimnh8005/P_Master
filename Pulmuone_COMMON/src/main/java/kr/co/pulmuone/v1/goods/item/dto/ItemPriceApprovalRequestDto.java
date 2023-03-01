package kr.co.pulmuone.v1.goods.item.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "가격정보 승인 요청 request dto")
public class ItemPriceApprovalRequestDto {

    @ApiModelProperty(value = "품목가격 승인 PK")
    private String ilItemPriceApprId;

    @ApiModelProperty(value = "품목코드")
    private String ilItemCode;

    @ApiModelProperty(value = "시작일자")
    private String priceApplyStartDate;

    @ApiModelProperty(value = "원가")
    private String standardPrice;

    @ApiModelProperty(value = "원가 - 요청당시")
    private String standardPriceChange;

    @ApiModelProperty(value = "정상가")
    private String recommendedPrice;

    @ApiModelProperty(value = "정상가 - 요청당시")
    private String recommendedPriceChange;

    @ApiModelProperty(value = "가격 관리 유형")
    private String priceManageTp;

    @ApiModelProperty(value = "승인 상태 코드")
    private String approvalStatus;

    @ApiModelProperty(value = "승인 요청자")
    private long approvalRequestUserId;

    @ApiModelProperty(value = "승인 1차 담당자")
    private String approvalSubUserId;

    @ApiModelProperty(value = "승인 2차 담당자")
    private String approvalUserId;

    @ApiModelProperty(value = "생성자")
    private long createId;

}