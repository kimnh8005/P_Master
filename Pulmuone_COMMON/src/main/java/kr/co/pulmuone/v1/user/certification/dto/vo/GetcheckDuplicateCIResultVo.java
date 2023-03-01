package kr.co.pulmuone.v1.user.certification.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetcheckDuplicateCIResultVo")
public class GetcheckDuplicateCIResultVo
{

	@ApiModelProperty(value = "체크코드")
	private String checkCode;

}
