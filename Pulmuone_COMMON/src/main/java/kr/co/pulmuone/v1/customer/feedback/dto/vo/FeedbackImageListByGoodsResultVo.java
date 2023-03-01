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
public class FeedbackImageListByGoodsResultVo {

    @ApiModelProperty(value = "후기 PK")
    private Long fbFeedbackId;

    @ApiModelProperty(value = "만족도")
    private String satisfactionScore;

    @ApiModelProperty(value = "베스트 평가 획득 횟수")
    private String bestCount;

    @ApiModelProperty(value = "후기 베스트 유저 정보 PK")
    private Long fbFeedbackBestId;

    @UserMaskingLoginId
    @ApiModelProperty(value = "로그인 ID")
    private String loginId;

    @ApiModelProperty(value = "생성일")
    private String createDate;

    @ApiModelProperty(value = "후기내용")
    private String comment;

    @ApiModelProperty(value = "이미지")
    List<FeedbackAttcVo> image;

    @ApiModelProperty(value = "건강식품 여부(Y : 건강식품)")
    private String healthGoodsYn;
}
