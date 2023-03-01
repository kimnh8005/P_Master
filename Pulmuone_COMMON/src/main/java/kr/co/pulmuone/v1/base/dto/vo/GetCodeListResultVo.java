package kr.co.pulmuone.v1.base.dto.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetCodeListResultVo")
public class GetCodeListResultVo {

	@JsonProperty("CODE")
	@ApiModelProperty(value = "")
	private String code;

	@JsonProperty("NAME")
	@ApiModelProperty(value = "")
	private String name;

	@JsonProperty("COMMENT")
	@ApiModelProperty(value = "")
	private String comment;

	@JsonProperty("ATTR1")
	@ApiModelProperty(value = "")
	private String attr1;

	@JsonProperty("ATTR2")
	@ApiModelProperty(value = "")
	private String attr2;

	@JsonProperty("ATTR3")
	@ApiModelProperty(value = "")
	private String attr3;

}
