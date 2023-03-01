package kr.co.pulmuone.v1.customer.faq.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingLoginId;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingUserName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FaqBosListVo {

	@ApiModelProperty(value = "분류")
    private String faqType;

	@ApiModelProperty(value = "분류명")
    private String faqTypeName;

	@ApiModelProperty(value = "제목")
    private String faqTitle;

    @ApiModelProperty(value = "등록자명")
    @UserMaskingUserName
    private String userName;

    @ApiModelProperty(value = "등록자 ID")
    @UserMaskingLoginId
    private String userId;

    @ApiModelProperty(value = "노출여부")
    private String displayYn;

    @ApiModelProperty(value = "등록일자")
    private String createDate;

    @ApiModelProperty(value = "조회 count")
    private String viewCount;

    @ApiModelProperty(value = "노출순서")
    private String viewSort;

    @ApiModelProperty(value = "FAQ 내용")
    private String content;

    @ApiModelProperty(value = "FAQ ID")
    private String faqId;

}
