package kr.co.pulmuone.v1.user.certification.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetStopReasonResultVo")
public class GetStopReasonResultVo
{
	@ApiModelProperty(value = "정지사유")
	private String reason;
}
