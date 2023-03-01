package kr.co.pulmuone.v1.promotion.exhibit.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.MallBaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ExhibitListByUserRequestDto extends MallBaseRequestPageDto {

    @ApiModelProperty(value = "몰인몰 유형")
    private String mallDiv;

    @ApiModelProperty(value = "임직원 기획전 조회 여부")
    private String employeeOnlyYn = "N";

    @ApiModelProperty(value = "디바이스 유형", hidden = true)
    private String deviceType;

    @ApiModelProperty(value = "유저 상태", hidden = true)
    private String userStatus;

}
