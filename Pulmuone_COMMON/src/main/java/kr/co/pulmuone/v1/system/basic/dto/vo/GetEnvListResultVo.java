package kr.co.pulmuone.v1.system.basic.dto.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetEnvListResultVo")
public class GetEnvListResultVo implements Serializable {

	private static final long serialVersionUID = 8946606454683158407L;

	@ApiModelProperty(value = "환경설정키")
	private String envKey;

	@ApiModelProperty(value = "환경설정값")
	private String envValue;
}
