package kr.co.pulmuone.v1.app.setting.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "앱 설정 화면 API Dto")
public class AppSettingResponseDto {

    @ApiModelProperty(value = "푸시 허용 여부")
    private String pushAllowed;

    @ApiModelProperty(value = "야간 푸시 허용 여부")
    private String nightAllowed;

    @ApiModelProperty(value = "앱 최신 버전")
    private String version;

}
