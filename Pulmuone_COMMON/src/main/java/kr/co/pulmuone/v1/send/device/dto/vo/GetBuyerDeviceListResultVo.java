package kr.co.pulmuone.v1.send.device.dto.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingEmail;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingLoginId;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingMobile;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingUserName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "푸시 가능 회원 조회 Result")
public class GetBuyerDeviceListResultVo {

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "No.")
    private long no;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "모바일디바이스정보 ID")
    private String deviceInfoId;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "회원유형")
    private String userTypeName;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "회원등급")
    private String userLevelName;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "회원명")
    @UserMaskingUserName
    private String userName;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "회원ID")
    @UserMaskingLoginId
    private String userId;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "휴대폰")
    @UserMaskingMobile
    private String mobile;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "EMAIL")
    @UserMaskingEmail
    private String email;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "가입일자")
    private String joinDate;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "최근 방문일자")
    private String lastVisitDate;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "기기타입")
    private String deviceType;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "기기타입명")
    private String deviceTypeName;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "수신여부")
    private String reception;


    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "야간 수신여부")
    private String nightReception;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "회원상태")
    private String userStatus;

}
