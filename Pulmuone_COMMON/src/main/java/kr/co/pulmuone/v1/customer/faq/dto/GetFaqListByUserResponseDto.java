package kr.co.pulmuone.v1.customer.faq.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.customer.faq.dto.vo.GetFaqListByUserResultVo;
import kr.co.pulmuone.v1.user.buyer.dto.vo.CodeInfoVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "FAQ 게시판 리스트 ResponseDto")
public class GetFaqListByUserResponseDto{

	@ApiModelProperty(value= "FAQ 게시판 리스트 ResultVo")
	private List<GetFaqListByUserResultVo> faq;

	@ApiModelProperty(value="FAQ 게시판 분류 리스트")
	private List<CodeInfoVo> faqType;

	@ApiModelProperty(value = "total")
	private long total;


}
