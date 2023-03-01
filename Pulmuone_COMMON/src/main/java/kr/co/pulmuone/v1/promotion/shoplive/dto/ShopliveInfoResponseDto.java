package kr.co.pulmuone.v1.promotion.shoplive.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.promotion.shoplive.dto.vo.ShopliveInfoVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "샵라이브 정보 조회 ResponseDto")
public class ShopliveInfoResponseDto {

    @ApiModelProperty(value = "샵라이브 PK")
    private Long evShopliveId;

    @ApiModelProperty(value = "샵라이브 방송 제목")
    private String title;

    @ApiModelProperty(value = "캠페인키")
    private String campaignKey;

    @ApiModelProperty(value = "jwt 인증키")
    private String jwtAuthId;

    @ApiModelProperty(value = "샵라이브 정보 Vo")
    private ShopliveInfoVo rows;

    public ShopliveInfoResponseDto() {}

    public ShopliveInfoResponseDto(ShopliveInfoVo vo) {
        this.evShopliveId = vo.getEvShopliveId();
        this.campaignKey = vo.getCampaignKey();
        this.title = vo.getTitle();
    }
}
