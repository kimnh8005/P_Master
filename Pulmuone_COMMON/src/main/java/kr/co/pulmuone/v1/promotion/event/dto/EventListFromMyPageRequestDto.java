package kr.co.pulmuone.v1.promotion.event.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.MallBaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EventListFromMyPageRequestDto extends MallBaseRequestPageDto {

    @ApiModelProperty(value = "시작일")
    private String startDate;

    @ApiModelProperty(value = "종료일")
    private String endDate;

    @ApiModelProperty(value = "디바이스 유형", hidden = true)
    private String deviceType;

    @ApiModelProperty(value = "유저 PK", hidden = true)
    private Long urUserId;

}
