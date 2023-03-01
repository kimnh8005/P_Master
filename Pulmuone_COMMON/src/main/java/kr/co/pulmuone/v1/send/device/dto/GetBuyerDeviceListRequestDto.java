package kr.co.pulmuone.v1.send.device.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "푸시 가능 회원 조회 Request")
public class GetBuyerDeviceListRequestDto extends BaseRequestPageDto {

    @ApiModelProperty(value = "검색구분")
    private String condiType;

    @ApiModelProperty(value = "검색 : 회원명, 회원ID")
    private String condiValue;

    @ApiModelProperty(value = "휴대폰")
    private String mobile;

    @ApiModelProperty(value = "EMAIL")
    private String mail;

    @ApiModelProperty(value = "회원유형")
    private String userType;

    @ApiModelProperty(value = "회원등급")
    private String userLevel;

    @ApiModelProperty(value = "가입 시작일")
    private String joinDateStart;

    @ApiModelProperty(value = "가입 종료일")
    private String joinDateEnd;

    @ApiModelProperty(value = "최근방문일자 시작일")
    private String lastVisitDateStart;

    @ApiModelProperty(value = "최근방문일자 종료일")
    private String lastVisitDateEnd;

    @ApiModelProperty(value = "기기타입")
    private String deviceType;

    @ApiModelProperty(value = "PUSH 수신여부")
    private String pushReception;

    @ApiModelProperty(value = "회원상태")
    private String userStatus;

    @ApiModelProperty(value = "야간 수신여부")
    private String nightPushReception;

}
