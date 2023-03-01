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
@ApiModel(description = "GetUserChangeHistoryListRequestDto")
public class GetUserChangeHistoryListRequestDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "")
	private String condiType;

	@ApiModelProperty(value = "")
	private String condiValue;

	@ApiModelProperty(value = "")
	private String startModifyDate;

	@ApiModelProperty(value = "")
	private String endModifyDate;

	@ApiModelProperty(value = "")
	private ArrayList<String> condiValueArray;

}
