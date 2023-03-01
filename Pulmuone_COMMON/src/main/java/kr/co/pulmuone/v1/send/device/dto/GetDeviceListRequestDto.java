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
@ApiModel(description = "APP 설치 단말기 목록 조회 Request")
public class GetDeviceListRequestDto extends BaseRequestPageDto {

    @ApiModelProperty(value = "단말등록일 시작")
    private String terminalRegistrationDateStart;

    @ApiModelProperty(value = "단말등록일 종료")
    private String terminalRegistrationDateEnd;

    @ApiModelProperty(value = "기기타입")
    private String deviceType;

    @ApiModelProperty(value = "회원정보 조회타입")
    private String condiType;

    @ApiModelProperty(value = "회원정보 조회 값")
    private String condiTypeValue;

}
