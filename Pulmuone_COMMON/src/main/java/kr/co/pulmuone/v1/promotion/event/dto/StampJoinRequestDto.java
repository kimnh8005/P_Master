package kr.co.pulmuone.v1.promotion.event.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class StampJoinRequestDto {

    @ApiModelProperty(value = "이벤트 PK")
    private Long evEventId;

    @ApiModelProperty(value = "URL방문여부(Y : URL방문 유형 일 경우 스탬프 적용, N : 스탬프 미적용)")
    private String urlVisitYn;

    @ApiModelProperty(value = "스탬프 번호")
    private int stampCount;

    @ApiModelProperty(value = "유저 PK", hidden = true)
    private Long urUserId;

    @ApiModelProperty(value = "디바이스 유형", hidden = true)
    private String deviceType;

    @ApiModelProperty(value = "유저 상태", hidden = true)
    private String userStatus;

    @ApiModelProperty(value = "유저 등급 PK", hidden = true)
    private Long urGroupId;

    @ApiModelProperty(value = "스탬프 참여 PK", hidden = true)
    private Long evEventJoinId;

}
