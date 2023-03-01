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
@ApiModel(description = "GetProgramListResultVo")
public class GetProgramListResultVo {

	@JsonProperty("PG_ID")
	@ApiModelProperty(value = "")
	private String programId;

	@JsonProperty("URL")
	@ApiModelProperty(value = "")
	private String url;

	@JsonProperty("HTML_PATH")
	@ApiModelProperty(value = "")
	private String htmlPath;
}
