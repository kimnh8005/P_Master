package kr.co.pulmuone.v1.customer.feedback.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.enums.CodeCommEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class FeedbackRequestDto {
    @ApiModelProperty(value = "주문 PK", required = true)
    private String odOrderId;

    @ApiModelProperty(value = "주문상세 PK", required = true)
    private String odOrderDetlId;

    @ApiModelProperty(value = "상품 PK", required = true)
    private Long ilGoodsId;

    @ApiModelProperty(value = "품목 PK", required = true)
    private String ilItemCd;

    @ApiModelProperty(value = "별점갯수", required = true)
    private String satisfactionScore;

    @ApiModelProperty(value = "구매후기")
    private String comment;

    @ApiModelProperty(value = "이벤트 PK")
    private Long evEventId;

    @ApiModelProperty(value = "이미지 정보")
    List<FeedbackAttcRequestDto> list;

    @ApiModelProperty(value = "user pk", hidden = true)
    private Long urUserId;

    @ApiModelProperty(value = "유저 등급 PK", hidden = true)
    private Long urGroupId;

    @ApiModelProperty(value = "후기 상품 유형", hidden = true)
    private String feedbackProductType;

    @ApiModelProperty(value = "노출 여부", hidden = true)
    private String displayYn;

    @ApiModelProperty(value = "베스트 평가 획득 횟수", hidden = true)
    private String bestCount;

    @ApiModelProperty(value = "관리자 베스트후기", hidden = true)
    private String adminBestYn;

    @ApiModelProperty(value = "관리자 우수후기", hidden = true)
    private String adminExcellentYn;

    @ApiModelProperty(value = "후기유형", hidden = true)
    private String feedbackType;

    @ApiModelProperty(value = "후기 PK", hidden = true)
    private Long fbFeedbackId;
}
