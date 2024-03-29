package kr.co.pulmuone.v1.customer.feedback.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingLoginId;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingUserName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FeedbackBosListVo {

	@ApiModelProperty(value = "상품유형")
    private String feedbackProductType;

	@ApiModelProperty(value = "상품유형명")
    private String feedbackProductTypeName;

    @ApiModelProperty(value = "후기유형")
    private String feedbackType;

    @ApiModelProperty(value = "후기유형 명")
    private String feedbackTypeName;

    @ApiModelProperty(value = "품목명")
    private String itemName;

    @ApiModelProperty(value = "품목코드")
    private String itemCode;

    @ApiModelProperty(value = "후기내용")
    private String comment;

    @ApiModelProperty(value = "회원명")
    @UserMaskingUserName
    private String userName;

    @ApiModelProperty(value = "회원 ID")
    @UserMaskingLoginId
    private String userId;

    @ApiModelProperty(value = "만족도")
    private String satisfactionScore;

    @ApiModelProperty(value = "등록일")
    private String createDate;

    @ApiModelProperty(value = "노출여부")
    private String displayYn;

    @ApiModelProperty(value = "상품명")
    private String goodsName;

    @ApiModelProperty(value = "상품 PK")
    private String ilGoodsId;

    @ApiModelProperty(value = "후기 PK")
    private String feedbackId;

    @ApiModelProperty(value = "순번")
    private String rowNumber;


}
