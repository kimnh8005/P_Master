package kr.co.pulmuone.v1.system.help.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetHelpResultVo")
public class GetHelpResultVo {

	@ApiModelProperty(value = "아이디")
	private String id;

	@ApiModelProperty(value = "제목")
	private String title;

	@ApiModelProperty(value = "내용")
	private String content;

	@ApiModelProperty(value = "입력구분코드")
	private String inputDivisionCode;

	@ApiModelProperty(value = "구분코드명")
	private String divisionName;

	@ApiModelProperty(value = "구분아이디")
	private String divisionId;

	@ApiModelProperty(value = "입력사용여부")
	private String inputUseYn;


}
