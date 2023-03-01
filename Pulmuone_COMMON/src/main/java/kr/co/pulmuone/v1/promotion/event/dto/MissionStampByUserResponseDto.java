package kr.co.pulmuone.v1.promotion.event.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.promotion.event.dto.vo.MissionStampByUserVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MissionStampByUserResponseDto {

    @ApiModelProperty(value = "이벤트 PK")
    private Long evEventId;

    @ApiModelProperty(value = "스탬프 번호")
    private String stampCount;

    @ApiModelProperty(value = "총 스탬프 갯수")
    private int totalStampCount;

    @ApiModelProperty(value = "유저 참여 총 갯수")
    private int joinStampCount;

    @ApiModelProperty(value = "아이콘 경로")
    private String iconPath;

    @ApiModelProperty(value = "스탬프 URL")
    private String stampUrl;

    public MissionStampByUserResponseDto(MissionStampByUserVo vo) {
        this.evEventId = vo.getEvEventId();
        this.stampCount = vo.getStampCount();
        this.totalStampCount = vo.getTotalStampCount();
        this.joinStampCount = vo.getJoinStampCount();
        this.iconPath = vo.getIconPath();
        this.stampUrl = vo.getStampUrl().replaceAll("http://", "").replaceAll("https://", "");
    }

}
