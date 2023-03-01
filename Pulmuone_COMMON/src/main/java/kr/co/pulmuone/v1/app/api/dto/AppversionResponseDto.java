package kr.co.pulmuone.v1.app.api.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "앱 버전 결과 API Dto")
public class AppversionResponseDto {

    @ApiModelProperty(value = "앱 최신 버전")
    private String version;

    @ApiModelProperty(value = "이벤트 스플래시 이미지 사용 여부 Y,N")
    private String eventAllowed;

    @ApiModelProperty(value = "이벤트 스플래시 이미지 주소")
    private String eventImg;

    @ApiModelProperty(value = "필수 업데이트 여부")
    private String necessary;

    @ApiModelProperty(value = "아이폰 앱스토어 주소")
    private String appleStore;

}
