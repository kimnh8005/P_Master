package kr.co.pulmuone.v1.user.dormancy.dto;

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
@ApiModel(description = "GetUserBlackListRequestDto")
public class GetUserBlackListRequestDto extends BaseRequestPageDto {

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
	private String startCreateDate;

	@ApiModelProperty(value = "")
	private String endCreateDate;

	@ApiModelProperty(value = "이벤트 제한 여부")
	private String eventLimitTp;

	@ApiModelProperty(value = "")
	private ArrayList<String> condiValueArray;

}
