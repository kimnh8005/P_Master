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
@ApiModel(description = "GetPsConfigListResultVo")
public class GetPsConfigListResultVo implements Serializable {

	private static final long serialVersionUID = 4410541135267510758L;

	@ApiModelProperty(value = "정책key")
	private String psKey;

	@ApiModelProperty(value = "정책value")
	private String psValue;

}
