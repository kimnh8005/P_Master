package kr.co.pulmuone.v1.user.buyer.dto;

import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetUserDropListRequestDto")
public class GetUserDropListRequestDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "")
	private String loginId;

	@ApiModelProperty(value = "")
	private String startCreateDate;

	@ApiModelProperty(value = "")
	private String endCreateDate;

}
