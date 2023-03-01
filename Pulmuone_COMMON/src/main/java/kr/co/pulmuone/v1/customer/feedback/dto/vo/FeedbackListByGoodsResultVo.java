package kr.co.pulmuone.v1.customer.feedback.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingLoginId;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class FeedbackListByGoodsResultVo {

    @ApiModelProperty(value = "후기 PK")
    private Long fbFeedbackId;

    @ApiModelProperty(value = "상품 유형")
    private String feedbackProductType;

    @ApiModelProperty(value = "후기 유형")
    private String feedbackType;

    @ApiModelProperty(value = "만족도")
    private String satisfactionScore;

    @ApiModelProperty(value = "후기 추천 수")
    private String bestCount;

    @ApiModelProperty(value = "후기 추천 PK")
    private Long fbFeedbackBestId;

    @UserMaskingLoginId
    @ApiModelProperty(value = "로그인 ID")
    private String loginId;

    @ApiModelProperty(value = "관리자 베스트 후기")
    private String adminBestYn;

    @ApiModelProperty(value = "전시 여부")
    private String displayYn;

    @ApiModelProperty(value = "생성일")
    private String createDate;

    @ApiModelProperty(value = "후기 내용")
    private String comment;

    @ApiModelProperty(value = "상품 명")
    private String goodsName;

    @ApiModelProperty(value = "품목 명")
    private String itemName;

    @ApiModelProperty(value = "이미지")
    List<FeedbackAttcVo> image;

    @ApiModelProperty(value = "건강식품 여부(Y : 건강식품)")
    private String healthGoodsYn;

}
