package kr.co.pulmuone.v1.system.log.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetConnectLogListResultVo")
public class GetConnectLogListResultVo {

	@ApiModelProperty(value = "접속ID")
	String loginId;

	@ApiModelProperty(value = "접속자")
	String userName;

	@ApiModelProperty(value = "서비스")
	String serviceName;

	@ApiModelProperty(value = "접속IP")
	String ip;

	@ApiModelProperty(value = "로그인 시간")
	String loginDate;

	@ApiModelProperty(value = "로그아웃 시간")
	String logoutDate;

	@ApiModelProperty(value = "userAgent 값")
	String agent;

	@ApiModelProperty(value = "구분 값")
	String successYn;


}