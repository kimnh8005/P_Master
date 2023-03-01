package kr.co.pulmuone.v1.customer.reward.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.customer.reward.dto.vo.RewardApplyVo;
import kr.co.pulmuone.v1.customer.reward.dto.vo.RewardAttcVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class GetRewardApplyResponseDto {

    @ApiModelProperty(value = "보상제 PK")
    private Long csRewardId;

    @ApiModelProperty(value = "보상제명")
    private String rewardName;

    @ApiModelProperty(value = "유의사항")
    private String rewardNotice;

    @ApiModelProperty(value = "신청기준")
    private String rewardApplyStandard;

    @ApiModelProperty(value = "주문 PK")
    private Long odOrderId;

    @ApiModelProperty(value = "주문 상세 PK")
    private Long odOrderDetlId;

    @ApiModelProperty(value = "상품 PK")
    private Long ilGoodsId;

    @ApiModelProperty(value = "주문번호")
    private String odid;

    @ApiModelProperty(value = "배송예정일")
    private String deliveryDate;

    @ApiModelProperty(value = "배송유형 코드")
    private String goodsDeliveryType;

    @ApiModelProperty(value = "주문 배송비")
    private String odShippingPriceId;

    @ApiModelProperty(value = "상품유형")
    private String packType;

    @ApiModelProperty(value = "타이틀")
    private String packTitle;

    @ApiModelProperty(value = "이미지경로")
    private String thumbnailPath;

    @ApiModelProperty(value = "상품명")
    private String goodsName;

    @ApiModelProperty(value = "주문상품건수(현재상품제외)")
    private Integer orderGoodsCount;

    @ApiModelProperty(value = "신청사유")
    private String rewardApplyContent;

    @ApiModelProperty(value = "답변알림 이메일")
    private String answerMailYn;

    @ApiModelProperty(value = "답변알림 SMS")
    private String answerSmsYn;

    @ApiModelProperty(value = "파일정보")
    private List<RewardAttcVo> file;

    public GetRewardApplyResponseDto(RewardApplyVo vo) {
        this.csRewardId = vo.getCsRewardId();
        this.rewardName = vo.getRewardName();
        this.rewardNotice = vo.getRewardNotice();
        this.rewardApplyStandard = vo.getRewardApplyStandard();
        this.odOrderId = vo.getOdOrderId();
        this.odOrderDetlId = vo.getOdOrderDetlId();
        this.ilGoodsId = vo.getIlGoodsId();
        this.odid = vo.getOdid();
        this.deliveryDate = vo.getDeliveryDate();
        this.goodsDeliveryType = vo.getGoodsDeliveryType();
        this.odShippingPriceId = vo.getOdShippingPriceId();
        this.packType = vo.getPackType();
        this.packTitle = vo.getPackTitle();
        this.thumbnailPath = vo.getThumbnailPath();
        this.goodsName = vo.getGoodsName();
        this.orderGoodsCount = vo.getOrderGoodsCount();
        this.rewardApplyContent = vo.getRewardApplyContent();
        this.answerMailYn = vo.getAnswerMailYn();
        this.answerSmsYn = vo.getAnswerSmsYn();
        this.file = vo.getFile();
    }
}
