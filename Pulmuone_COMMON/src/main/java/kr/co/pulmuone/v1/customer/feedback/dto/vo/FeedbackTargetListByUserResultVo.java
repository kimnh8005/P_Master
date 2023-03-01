package kr.co.pulmuone.v1.customer.feedback.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.order.front.dto.vo.OrderInfoFromFeedbackVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FeedbackTargetListByUserResultVo {

    @ApiModelProperty(value = "체험단 여부")
    private String experienceYn = "N";

    @ApiModelProperty(value = "체험단 PK")
    private Long evEventId;

    @ApiModelProperty(value = "주문 PK")
    private Long odOrderId;

    @ApiModelProperty(value = "주문상세 PK")
    private Long odOrderDetlId;

    @ApiModelProperty(value = "상품 PK")
    private Long ilGoodsId;

    @ApiModelProperty(value = "아이템 PK")
    private String ilItemCd;

    @ApiModelProperty(value = "상품 이미지 경로")
    private String thumbnailPath;

    @ApiModelProperty(value = "주문 상품 유형")
    private String packType;

    @ApiModelProperty(value = "주문 합명")
    private String packTitle;

    @ApiModelProperty(value = "묶음상품 상품 PK")
    private String packGoodsId;

    @ApiModelProperty(value = "상품명")
    private String goodsName;

    @ApiModelProperty(value = "결제완료 일자")
    private String icDate;

    @ApiModelProperty(value = "배송중 일자")
    private String diDate;

    @ApiModelProperty(value = "d-day")
    private long dday;

    @ApiModelProperty(value = "후기 작성 종료 일자")
    private String feedbackEndDate;

    @ApiModelProperty(value = "건강기능식품 여부")
    private String healthGoodsYn;

    @ApiModelProperty(value = "적립금 존재여부")
    private String existPointYn;

    @ApiModelProperty(value = "적립금 - 일반후기")
    private Long normalAmount;

    @ApiModelProperty(value = "적립금 - 포토후기")
    private Long photoAmount;

    @ApiModelProperty(value = "적립금 - 프리미엄후기")
    private Long premiumAmount;

    public FeedbackTargetListByUserResultVo(OrderInfoFromFeedbackVo vo) {
        this.evEventId = vo.getEvEventId();
        this.odOrderId = vo.getOdOrderId();
        this.odOrderDetlId = vo.getOdOrderDetlId();
        this.ilGoodsId = vo.getIlGoodsId();
        this.ilItemCd = vo.getIlItemCd();
        this.thumbnailPath = vo.getGoodsImagePath();
        this.packTitle = vo.getPackTitle();
        this.goodsName = vo.getGoodsName();
        this.icDate = vo.getIcDate();
        this.diDate = vo.getDiDate();
        this.packType = vo.getPackType();
        this.packGoodsId = vo.getPackGoodsId();
        this.healthGoodsYn = vo.getHealthGoodsYn();
    }
}
