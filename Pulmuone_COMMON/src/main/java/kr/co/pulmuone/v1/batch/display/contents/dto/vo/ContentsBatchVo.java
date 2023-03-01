package kr.co.pulmuone.v1.batch.display.contents.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.enums.DisplayEnums;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ContentsBatchVo {

    @ApiModelProperty(value = "전시 컨텐츠 PK")
    private Long dpContsId;

    @ApiModelProperty(value = "전시 인벤토리 PK")
    private Long dpInventoryId;

    @ApiModelProperty(value = "카테고리 PK")
    private String ilCtgryId;

    @ApiModelProperty(value = "컨텐츠 유형")
    private String contentsType;

    @ApiModelProperty(value = "컨텐츠 레벨")
    private String contentsLevel;

    @ApiModelProperty(value = "상위 전시 컨텐츠 PK")
    private Long parentsContentsId;

    @ApiModelProperty(value = "레벨1 전시 컨텐츠 PK")
    private Long level1ContentsId;

    @ApiModelProperty(value = "레벨2 전시 컨텐츠 PK")
    private Long level2ContentsId;

    @ApiModelProperty(value = "레벨3 전시 컨텐츠 PK")
    private Long level3ContentsId;

    @ApiModelProperty(value = "전시 시작 일시")
    private String displayStartDate;

    @ApiModelProperty(value = "전시 종료 일시")
    private String displayEndDate;

    @ApiModelProperty(value = "전시 범위")
    private String displayRangeType;

    @ApiModelProperty(value = "타이틀 명")
    private String titleName;

    @ApiModelProperty(value = "전시 카테고리 PK")
    private Long dpCtgryId;

    @ApiModelProperty(value = "텍스트1")
    private String text1;

    @ApiModelProperty(value = "텍스트1 색상")
    private String text1Color;

    @ApiModelProperty(value = "텍스트2")
    private String text2;

    @ApiModelProperty(value = "텍스트2 색상")
    private String text2Color;

    @ApiModelProperty(value = "텍스트3")
    private String text3;

    @ApiModelProperty(value = "텍스트3 색상")
    private String text3Color;

    @ApiModelProperty(value = "PC 링크 URL")
    private String linkUrlPc;

    @ApiModelProperty(value = "모바일 링크 URL")
    private String linkUrlMobile;

    @ApiModelProperty(value = "PC HTML")
    private String htmlPc;

    @ApiModelProperty(value = "모바일 HTML")
    private String htmlMobile;

    @ApiModelProperty(value = "PC 이미지 경로")
    private String imagePathPc;

    @ApiModelProperty(value = "PC 이미지 원본 파일 명")
    private String imageOriginalNamePc;

    @ApiModelProperty(value = "모바일 이미지 경로")
    private String imagePathMobile;

    @ApiModelProperty(value = "모바일 이미지 원본 파일 명")
    private String imageOriginalNameMobile;

    @ApiModelProperty(value = "PC GIF 이미지 경로")
    private String gifImagePathPc;

    @ApiModelProperty(value = "PC GIF 이미지 원본 파일 명")
    private String gifImageOriginalNamePc;

    @ApiModelProperty(value = "모바일 GIF 이미지 경로")
    private String gifImagePathMobile;

    @ApiModelProperty(value = "모바일 GIF 이미지 원본 파일 명")
    private String gifImageOriginalNameMobile;

    @ApiModelProperty(value = "컨텐츠 ID")
    private Long contentsId;

    @ApiModelProperty(value = "전시 노출 조건 유형")
    private String displayContentsType;

    @ApiModelProperty(value = "전시 노출 순서 유형")
    private String displaySortType;

    @ApiModelProperty(value = "노출 순서")
    private int sort;

    @ApiModelProperty(value = "사용 여부")
    private String useYn;

    @ApiModelProperty(value = "삭제 여부")
    private String delYn;

    @ApiModelProperty(value = "등록자 PK")
    private String createId;

    @ApiModelProperty(value = "전시 카테고리 PK List")
    private List<Long> dpCtgryIdList;

    public void setLevel2(ContentsBatchVo vo) {
        this.dpInventoryId = vo.getDpInventoryId();
        this.contentsType = DisplayEnums.ContentsType.GOODS.getCode();
        this.contentsLevel = "2";
        this.parentsContentsId = vo.getDpContsId();
        this.level1ContentsId = vo.getDpContsId();
        this.displayRangeType = vo.getDisplayRangeType();
        this.titleName = "자동상품";
        this.displayContentsType = DisplayEnums.DpCondType.AUTO.getCode();
        this.displaySortType = DisplayEnums.DpSortType.MANUAL.getCode();
        this.useYn = "Y";
        this.delYn = "N";
        this.createId = "0";
    }

    public void setLevel3(ContentsBatchVo vo) {
        this.dpInventoryId = vo.getDpInventoryId();
        this.contentsType = DisplayEnums.ContentsType.GOODS.getCode();
        this.contentsLevel = "3";
        this.parentsContentsId = vo.getDpContsId();
        this.level1ContentsId = vo.getLevel1ContentsId();
        this.level2ContentsId = vo.getDpContsId();
        this.displayRangeType = vo.getDisplayRangeType();
        this.titleName = "자동상품";
        this.displayContentsType = DisplayEnums.DpCondType.AUTO.getCode();
        this.displaySortType = DisplayEnums.DpSortType.MANUAL.getCode();
        this.useYn = "Y";
        this.delYn = "N";
        this.createId = "0";
    }
}
