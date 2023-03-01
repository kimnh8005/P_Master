package kr.co.pulmuone.v1.display.contents.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "컨텐츠 조회 ResponseDto")
public class ContentsInfoResponseDto {

    @ApiModelProperty(value = "level2 유형")
    private String level2ContentsType;

    @ApiModelProperty(value = "level2 수량")
    private int level2TotalCount;

    @ApiModelProperty(value = "level2 정보")
    private List<?> level2;

    @ApiModelProperty(value = "level3 유형")
    private String level3ContentsType;

    @ApiModelProperty(value = "level3 수량")
    private int level3TotalCount;

    @ApiModelProperty(value = "level3 정보")
    private List<?> level3;
}
