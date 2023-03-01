package kr.co.pulmuone.v1.user.join.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetIsCheckMailResultVo")
public class GetIsCheckMailResultVo
{

	@ApiModelProperty(value = "체크코드")
	private String checkCode;

}
