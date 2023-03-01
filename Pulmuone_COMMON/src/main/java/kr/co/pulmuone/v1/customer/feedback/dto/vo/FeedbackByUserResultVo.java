package kr.co.pulmuone.v1.customer.feedback.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class FeedbackByUserResultVo {
    @ApiModelProperty(value = "후기 PK")
    private Long fbFeedbackId;

    @ApiModelProperty(value = "베스트 여부")
    private String bestYn;

    @ApiModelProperty(value = "체험단 여부")
    private String experienceYn;

    @ApiModelProperty(value = "상품 PK")
    private Long ilGoodsId;

    @ApiModelProperty(value = "상품 명")
    private String goodsName;

    @ApiModelProperty(value = "상품 이미지")
    private String thumbnailPath;

    @ApiModelProperty(value = "품목 PK")
    private String ilItemCd;

    @ApiModelProperty(value = "품목 명")
    private String itemName;

    @ApiModelProperty(value = "구매일자")
    private String icDate;

    @ApiModelProperty(value = "등록 일자")
    private String createDate;

    @ApiModelProperty(value = "만족도")
    private String satisfactionScore;

    @ApiModelProperty(value = "도움됐어요 횟수")
    private String bestCount;

    @ApiModelProperty(value = "후기 내용")
    private String comment;

    @ApiModelProperty(value = "합 타이틀")
    private String packTitle;

    @ApiModelProperty(value = "합 유형")
    private String packType;

    @ApiModelProperty(value = "묶음상품 상품 PK")
    private String packGoodsId;

    @ApiModelProperty(value = "PK - 이벤트 체험단 공용")
    private String evEventId;

    @ApiModelProperty(value = "이미지")
    List<FeedbackAttcVo> image;
}
