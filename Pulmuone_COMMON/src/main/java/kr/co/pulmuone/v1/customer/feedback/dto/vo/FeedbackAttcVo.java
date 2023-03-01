package kr.co.pulmuone.v1.customer.feedback.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FeedbackAttcVo {

    @ApiModelProperty(value = "후기첨부파일 PK")
    private Long fbFeedbackAttcId;

    @ApiModelProperty(value = "후기 PK")
    private Long fbFeedbackId;

    @ApiModelProperty(value = "원본 파일 명")
    private String imageOriginalName;

    @ApiModelProperty(value = "파일 명")
    private String imageName;

    @ApiModelProperty(value = "파일경로")
    private String imagePath;

    @ApiModelProperty(value = "파일경로 및 이름")
    private String imageNamePath;

    @ApiModelProperty(value = "썸네일 원본 파일 명")
    private String thumbnailOriginalName;

    @ApiModelProperty(value = "썸네일 파일 명")
    private String thumbnailName;

    @ApiModelProperty(value = "썸네일 파일 경로")
    private String thumbnailPath;

    @ApiModelProperty(value = "썸네일 파일 경로 및 이름")
    private String thumbnailNamePath;
}
