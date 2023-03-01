package kr.co.pulmuone.v1.customer.reward.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "이미지 VO")
public class RewardAttcVo {

    @ApiModelProperty(value = "이미지 원본 파일명")
    private String imageOriginalName;

    @ApiModelProperty(value = "이미지 파일명")
    private String imageName;

    @ApiModelProperty(value = "이미지 저장경로")
    private String imagePath;

    @ApiModelProperty(value = "썸네일 원본 파일명")
    private String thumbnailOriginalName;

    @ApiModelProperty(value = "썸네일 파일명")
    private String thumbnailName;

    @ApiModelProperty(value = "썸네일 파일경로")
    private String thumbnailPath;

}