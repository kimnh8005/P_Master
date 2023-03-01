package kr.co.pulmuone.v1.system.basic.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GetTypeListResultVo {
	@ApiModelProperty(value = "타입아이디")
	private String typeId;

	@ApiModelProperty(value = "타입명")
	private String typeName;

	@ApiModelProperty(value = "순서")
	private int sort;
}
