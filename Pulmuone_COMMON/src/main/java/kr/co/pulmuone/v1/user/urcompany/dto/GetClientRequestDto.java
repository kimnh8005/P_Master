package kr.co.pulmuone.v1.user.urcompany.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetClientRequestDto")
public class GetClientRequestDto extends BaseRequestDto{

	@ApiModelProperty(value = "거래처 ID")
	private String urClientId;

	@ApiModelProperty(value = "거래처")
	private String getClient;

	@ApiModelProperty(value = "거래처 타입")
	private String clientTp;

	@ApiModelProperty(value = "출고처 ID")
	private String urCompanyId;
}
