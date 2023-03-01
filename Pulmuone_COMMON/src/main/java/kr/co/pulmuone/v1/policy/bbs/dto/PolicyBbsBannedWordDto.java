package kr.co.pulmuone.v1.policy.bbs.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "게시판금칙어설정 Dto")
public class PolicyBbsBannedWordDto extends BaseResponseDto{

	@ApiModelProperty(value = "게시판금칙어설정.쇼핑몰금칙어")
	private String mallBannedWord;

	@ApiModelProperty(value = "게시판금칙어설정.쇼핑몰사용여부")
	private String mallUseYn;

	@ApiModelProperty(value = "게시판금칙어설정.BOS금칙어")
	private String bosBannedWord;

	@ApiModelProperty(value = "게시판금칙어설정.BOS사용여부")
	private String bosUseYn;

}
