package kr.co.pulmuone.v1.customer.feedback.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FeedbackAttcRequestDto {

    @ApiModelProperty(value = "후기 PK", hidden = true)
    private Long fbFeedbackId;

    @ApiModelProperty(value = "원본파일명")
    private String imageOriginalName;

    @ApiModelProperty(value = "파일명")
    private String imageName;

    @ApiModelProperty(value = "파일전체경로")
    private String imagePath;

    @ApiModelProperty(value = "썸네일 원본파일명")
    private String thumbnailOriginalName;

    @ApiModelProperty(value = "썸네일 파일명")
    private String thumbnailName;

    @ApiModelProperty(value = "썸네일 파일전체경로")
    private String thumbnailPath;

}
