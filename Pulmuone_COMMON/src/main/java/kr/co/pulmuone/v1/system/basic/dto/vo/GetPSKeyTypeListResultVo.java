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
@ApiModel(description = "GetPSKeyTypeListResultVo")
public class GetPSKeyTypeListResultVo implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "정책 value")
	private String psValue;

}
