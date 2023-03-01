package kr.co.pulmuone.v1.customer.stndpnt.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class StandingPointQnaAttachVo {

    @ApiModelProperty(value = "파일경로")
    private String filePath;

    @ApiModelProperty(value = "물리파일명")
    private String fileName;

    @ApiModelProperty(value = "파일명")
    private String realFileName;

}
