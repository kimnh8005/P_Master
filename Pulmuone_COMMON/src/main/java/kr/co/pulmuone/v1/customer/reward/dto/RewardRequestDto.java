package kr.co.pulmuone.v1.customer.reward.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.customer.reward.dto.vo.RewardAttcVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class RewardRequestDto {

    @ApiModelProperty(value = "고객 보상제 신청 PK")
    private Long csRewardApplyId;

    @ApiModelProperty(value = "고객 보상제 PK")
    private Long csRewardId;

    @ApiModelProperty(value = "유저 PK", hidden = true)
    private Long urUserId;

    @ApiModelProperty(value = "주문 PK")
    private Long odOrderId;

    @ApiModelProperty(value = "주문 상세 PK")
    private Long odOrderDetlId;

    @ApiModelProperty(value = "상품 PK")
    private Long ilGoodsId;

    @ApiModelProperty(value = "배송기준 - 도착예정일")
    private String deliveryDate;

    @ApiModelProperty(value = "배송기준 - 배송유형")
    private String goodsDeliveryType;

    @ApiModelProperty(value = "배송기준 - 배송비")
    private String odShippingPriceId;

    @ApiModelProperty(value = "신청사유")
    private String rewardApplyContent;

    @ApiModelProperty(value = "신청 유형", hidden = true)
    private String rewardApplyStatus;

    @ApiModelProperty(value = "답변알림-이메일")
    private String answerSmsYn;

    @ApiModelProperty(value = "답변알림-SMS")
    private String answerMailYn;

    @ApiModelProperty(value = "파일정보")
    private List<RewardAttcVo> file;

}
