package kr.co.pulmuone.v1.promotion.event.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.MallBaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EventListByUserRequestDto extends MallBaseRequestPageDto {

    @ApiModelProperty(value = "진행여부(진행중 : IN_PROGRESS, END)")
    private String progress;

    @ApiModelProperty(value = "임직원 이벤트 보기 YN (Y:임직원 이벤트만, N : 전체조회)")
    private String employeeOnlyYn;

    @ApiModelProperty(value = "디바이스 유형", hidden = true)
    private String deviceType;

    @ApiModelProperty(value = "유저 상태", hidden = true)
    private String userStatus;

}
