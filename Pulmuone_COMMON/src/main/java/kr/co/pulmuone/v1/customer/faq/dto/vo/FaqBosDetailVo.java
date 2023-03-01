package kr.co.pulmuone.v1.customer.faq.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FaqBosDetailVo {

	@ApiModelProperty(value = "분류")
    private String faqType;

	@ApiModelProperty(value = "분류명")
    private String faqTypeName;

	@ApiModelProperty(value = "제목")
    private String faqTitle;

    @ApiModelProperty(value = "FAQ 내용")
    private String content;

    @ApiModelProperty(value = "조회 count")
    private String viewCount;

    @ApiModelProperty(value = "노출순서")
    private String viewSort;

    @ApiModelProperty(value = "노출여부")
    private String displayYn;

    @ApiModelProperty(value = "FAQ ID")
    private String faqId;
}
