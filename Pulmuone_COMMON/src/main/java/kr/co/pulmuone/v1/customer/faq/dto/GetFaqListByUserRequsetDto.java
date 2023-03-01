package kr.co.pulmuone.v1.customer.faq.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.MallBaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "FAQ 게시판 리스트 RequsetDto")
public class GetFaqListByUserRequsetDto extends MallBaseRequestPageDto{

	@ApiModelProperty(value= "검색어")
	private String searchWord;

	@ApiModelProperty(value= "게시판 분류")
	private String faqType;

}
