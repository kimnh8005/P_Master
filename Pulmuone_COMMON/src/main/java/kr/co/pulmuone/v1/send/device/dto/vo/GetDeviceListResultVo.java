package kr.co.pulmuone.v1.send.device.dto.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingLoginId;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingUserName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "APP 설치 단말기 목록 조회 Result")
public class GetDeviceListResultVo {

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "No.")
    private long no;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "회원ID")
    @UserMaskingLoginId
    private String userId;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "기기타입명")
    private String deviceTypeName;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "푸시키")
    private String pushKey;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "등록일")
    private String createDate;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "사용여부")
    private String useYn;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "imageUrl")
    private String imageUrl;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "환경변수 값")
    private String envVal;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "회원명")
    @UserMaskingUserName
    private String userName;
}
