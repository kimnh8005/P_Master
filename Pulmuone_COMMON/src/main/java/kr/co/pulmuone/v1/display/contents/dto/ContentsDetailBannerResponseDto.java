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
@ApiModel(description = "컨텐츠 정보 Banner ResponseDto")
public class ContentsDetailBannerResponseDto {

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

    @ApiModelProperty(value = "PC 링크 URL")
    private String linkUrlPc;

    @ApiModelProperty(value = "모바일 링크 URL")
    private String linkUrlMobile;

    @ApiModelProperty(value = "PC 이미지")
    private String imagePathPc;

    @ApiModelProperty(value = "PC GIF 이미지")
    private String gifImagePathPc;

    @ApiModelProperty(value = "모바일 이미지")
    private String imagePathMobile;

    @ApiModelProperty(value = "모바일 GIF 이미지")
    private String gifImagePathMobile;

    @ApiModelProperty(value = "표준 카테고리 PK")
    private String ilCtgryId;

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

    public ContentsDetailBannerResponseDto(ContentsDetailVo vo) {
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
        this.linkUrlPc = vo.getLinkUrlPc();
        this.linkUrlMobile = vo.getLinkUrlMobile();
        this.imagePathPc = vo.getImagePathPc();
        this.gifImagePathPc = vo.getGifImagePathPc();
        this.imagePathMobile = vo.getImagePathMobile();
        this.gifImagePathMobile = vo.getGifImagePathMobile();
        this.ilCtgryId = vo.getIlCtgryId();
    }
}
