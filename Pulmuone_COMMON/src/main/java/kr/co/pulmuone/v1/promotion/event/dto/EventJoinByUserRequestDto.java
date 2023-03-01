package kr.co.pulmuone.v1.promotion.event.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.MallBaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EventJoinByUserRequestDto extends MallBaseRequestPageDto {

    @ApiModelProperty(value = "이벤트 PK")
    private Long evEventId;

}
