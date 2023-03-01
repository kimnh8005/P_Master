package kr.co.pulmuone.v1.user.group.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EstimationGroupInfoResponseDto {

    @ApiModelProperty(value = "등급 명")
    private String groupName;

    @ApiModelProperty(value = "등급 이미지")
    private String topImagePath;

    @ApiModelProperty(value = "리스트 이미지")
    private String listImagePath;

}
