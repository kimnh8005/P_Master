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
@ApiModel(description = "컨텐츠 정보 Brand ResponseDto")
public class ContentsDetailBrandResponseDto {

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

    @ApiModelProperty(value = "PC 이미지")
    private String imagePathPc;

    @ApiModelProperty(value = "PC GIF 이미지")
    private String gifImagePathPc;

    @ApiModelProperty(value = "모바일 이미지")
    private String imagePathMobile;

    @ApiModelProperty(value = "모바일 GIF 이미지")
    private String gifImagePathMobile;

    @ApiModelProperty(value = "전시 브랜드 PK")
    private Long dpBrandId;

    @ApiModelProperty(value = "브랜드명")
    private String brandName;

    @ApiModelProperty(value = "브랜드 이미지 - main")
    private String brandImageMain;

    @ApiModelProperty(value = "브랜드 이미지 - main over")
    private String brandImageMainOver;

    @ApiModelProperty(value = "브랜드 이미지 - title banner")
    private String brandImageTitleBanner;

    @ApiModelProperty(value = "level2 유형")
    private String level2ContentsType;

    @ApiModelProperty(value = "level2 수량")
    private int level2TotalCount;

    @ApiModelProperty(value = "level2 정보")
    private List<?> level2;

    public ContentsDetailBrandResponseDto(ContentsDetailVo vo) {
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
        this.imagePathPc = vo.getImagePathPc();
        this.gifImagePathPc = vo.getGifImagePathPc();
        this.imagePathMobile = vo.getImagePathMobile();
        this.gifImagePathMobile = vo.getGifImagePathMobile();
        this.brandName = vo.getBrandName();
        this.brandImageMain = vo.getBrandImageMain();
        this.brandImageMainOver = vo.getBrandImageMainOver();
        this.brandImageTitleBanner = vo.getBrandImageTitleBanner();
        this.dpBrandId = vo.getContentsId();
    }
}
