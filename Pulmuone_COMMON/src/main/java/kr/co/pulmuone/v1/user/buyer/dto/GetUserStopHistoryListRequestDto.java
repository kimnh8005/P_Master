package kr.co.pulmuone.v1.user.buyer.dto;

import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;

import java.util.ArrayList;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetUserStopHistoryListRequestDto")
public class GetUserStopHistoryListRequestDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "")
	private String condiType;

	@ApiModelProperty(value = "")
	private String condiValue;

	@ApiModelProperty(value = "")
	private String mobile;

	@ApiModelProperty(value = "")
	private String mail;

	@ApiModelProperty(value = "")
	private String userType;

	@ApiModelProperty(value = "")
	private String startStopDate;

	@ApiModelProperty(value = "")
	private String endStopDate;

	@ApiModelProperty(value = "")
	private String startNormalDate;

	@ApiModelProperty(value = "")
	private String endNormalDate;

	@ApiModelProperty(value = "")
	private ArrayList<String> condiValueArray;

}
