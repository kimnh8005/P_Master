package kr.co.pulmuone.v1.display.contents.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.display.contents.dto.vo.ContentsDetailVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "컨텐츠 정보 Category ResponseDto")
public class ContentsDetailCategoryResponseDto {

    @ApiModelProperty(value = "전시 컨텐츠 PK")
    private Long dpContsId;

    @ApiModelProperty(value = "전시 시작일시")
    private String dpStartDate;

    @ApiModelProperty(value = "전시 종료일시")
    private String dpEndDate;

    @ApiModelProperty(value = "전시 범위")
    private String dpRangeTypeName;

    @ApiModelProperty(value = "타이틀명")
    private String titleName;

    @ApiModelProperty(value = "노출순서")
    private int sort;

    @ApiModelProperty(value = "노출텍스트1")
    private String text1;

    @ApiModelProperty(value = "노출텍스트1 색상")
    private String text1Color;

    @ApiModelProperty(value = "노출텍스트2")
    private String text2;

    @ApiModelProperty(value = "노출텍스트2 색상")
    private String text2Color;

    @ApiModelProperty(value = "노출텍스트3")
    private String text3;

    @ApiModelProperty(value = "노출텍스트3 색상")
    private String text3Color;

    @ApiModelProperty(value = "카테고리 PK")
    private String dpCtgryId;

    @ApiModelProperty(value = "카테고리 명")
    private String categoryName;

    @ApiModelProperty(value = "level2 유형")
    private String level2ContentsType;

    @ApiModelProperty(value = "level2 수량")
    private int level2TotalCount;

    @ApiModelProperty(value = "level2 정보")
    private List<?> level2;

    public ContentsDetailCategoryResponseDto(ContentsDetailVo vo) {
        this.dpContsId = vo.getDpContsId();
        this.dpStartDate = vo.getDpStartDate();
        this.dpEndDate = vo.getDpEndDate();
        this.dpRangeTypeName = vo.getDpRangeTypeName();
        this.titleName = vo.getTitleName();
        this.sort = vo.getSort();
        this.text1 = vo.getText1();
        this.text1Color = vo.getText1Color();
        this.text2 = vo.getText2();
        this.text2Color = vo.getText2Color();
        this.text3 = vo.getText3();
        this.text3Color = vo.getText3Color();
        this.dpCtgryId = vo.getDpCtgryId();
        this.categoryName = vo.getCategoryName();
    }
}
