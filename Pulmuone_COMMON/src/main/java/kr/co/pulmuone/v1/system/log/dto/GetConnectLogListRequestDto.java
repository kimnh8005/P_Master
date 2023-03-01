package kr.co.pulmuone.v1.system.log.dto;

import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetConnectLogListRequestDto")
public class GetConnectLogListRequestDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "조건타입")
	String condiType;

	@ApiModelProperty(value = "조건값")
	String condiValue;

	@ApiModelProperty(value = "디바이스")
	String device;

	@ApiModelProperty(value = "서비스")
	String service;

	@ApiModelProperty(value = "구분(성공/실패)")
	String successCondition;

	@ApiModelProperty(value = "시작생성일")
	String startCreateDate;

	@ApiModelProperty(value = "종료생성일")
	String endCreateDate;

}
