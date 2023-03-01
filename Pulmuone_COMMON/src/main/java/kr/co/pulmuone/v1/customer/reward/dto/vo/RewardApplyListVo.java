package kr.co.pulmuone.v1.customer.reward.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.order.front.dto.vo.OrderInfoFromMypageRewardVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class RewardApplyListVo {

    @ApiModelProperty(value = "고객보상제 PK")
    private Long csRewardId;

    @ApiModelProperty(value = "신청일")
    private String requestDate;

    @ApiModelProperty(value = "신청번호")
    private Long csRewardApplyId;

    @ApiModelProperty(value = "보상제명")
    private String rewardName;

    @ApiModelProperty(value = "보상구분")
    private String rewardPayType;

    @ApiModelProperty(value = "처리상태")
    private String rewardApplyStatus;

    @ApiModelProperty(value = "처리상태 기준")
    private String rewardApplyStandard;

    @ApiModelProperty(value = "처리일시")
    private String modifyDate;

    @ApiModelProperty(value = "처리결과")
    private String rewardApplyResult;

    @ApiModelProperty(value = "처리사유")
    private String answer;

    @ApiModelProperty(value = "관리자메모")
    private String adminComment;

    @ApiModelProperty(value = "지급내역상세")
    private String rewardPayDetail;

    @ApiModelProperty(value = "주문일자")
    private String orderDate;

    @ApiModelProperty(value = "주문번호")
    private String odid;

    @ApiModelProperty(value = "배송예정일")
    private String deliveryDate;

    @ApiModelProperty(value = "상품유형")
    private String packType;

    @ApiModelProperty(value = "타이틀")
    private String packTitle;

    @ApiModelProperty(value = "이미지경로")
    private String thumbnailPath;

    @ApiModelProperty(value = "기획전 PK")
    private String evExhibitId;

    @ApiModelProperty(value = "상품 PK")
    private Long ilGoodsId;

    @ApiModelProperty(value = "상품명")
    private String goodsName;

    @ApiModelProperty(value = "주문상품건수")
    private Integer orderGoodsCount;

    @ApiModelProperty(value = "신청내용")
    private String rewardApplyContent;

    @ApiModelProperty(value = "회원 확인 여부")
    private String userCheckYn;

    @ApiModelProperty(value = "주문 PK")
    private Long odOrderId;

    @ApiModelProperty(value = "주문 상세 PK")
    private Long odOrderDetlId;

    @ApiModelProperty(value = "배송유형 코드")
    private String goodsDeliveryType;

    @ApiModelProperty(value = "주문 배송비")
    private String odShippingPriceId;

    @ApiModelProperty(value = "보상제 상품유형")
    private String rewardGoodsType;

    @ApiModelProperty(value = "파일정보")
    private List<RewardAttcVo> file;

    public void setOrderInfo(OrderInfoFromMypageRewardVo vo) {
        this.orderDate = vo.getOrderDate();
        this.odid = vo.getOdid();
        this.packType = vo.getPackType();
        this.packTitle = vo.getPackTitle();
        this.thumbnailPath = vo.getThumbnailPath();
        this.ilGoodsId = vo.getIlGoodsId();
        this.goodsName = vo.getGoodsName();
        this.evExhibitId = vo.getEvExhibitId();
    }

}
