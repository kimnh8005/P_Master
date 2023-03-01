package kr.co.pulmuone.v1.display.contents.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.search.searcher.dto.GoodsSearchResultDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "컨테츠 정보 Result")
public class ContentsDetailVo implements Cloneable {

    @ApiModelProperty(value = "전시 컨텐츠 PK")
    private Long dpContsId = 0L;

    @ApiModelProperty(value = "전시 시작일시")
    private String dpStartDate = "";

    @ApiModelProperty(value = "전시 종료일시")
    private String dpEndDate = "";

    @ApiModelProperty(value = "전시 범위")
    private String dpRangeTypeName = "";

    @ApiModelProperty(value = "타이틀명")
    private String titleName = "";

    @ApiModelProperty(value = "노출순서")
    private int sort = 0;

    @ApiModelProperty(value = "노출텍스트1")
    private String text1 = "";

    @ApiModelProperty(value = "노출텍스트1 색상")
    private String text1Color = "";

    @ApiModelProperty(value = "노출텍스트2")
    private String text2 = "";

    @ApiModelProperty(value = "노출텍스트2 색상")
    private String text2Color = "";

    @ApiModelProperty(value = "노출텍스트3")
    private String text3 = "";

    @ApiModelProperty(value = "노출텍스트3 색상")
    private String text3Color = "";

    @ApiModelProperty(value = "PC 링크 URL")
    private String linkUrlPc = "";

    @ApiModelProperty(value = "모바일 링크 URL")
    private String linkUrlMobile = "";

    @ApiModelProperty(value = "PC HTML")
    private String htmlPc = "";

    @ApiModelProperty(value = "모바일 HTML")
    private String htmlMobile = "";

    @ApiModelProperty(value = "PC 이미지")
    private String imagePathPc = "";

    @ApiModelProperty(value = "PC GIF 이미지")
    private String gifImagePathPc = "";

    @ApiModelProperty(value = "모바일 이미지")
    private String imagePathMobile = "";

    @ApiModelProperty(value = "모바일 GIF 이미지")
    private String gifImagePathMobile = "";

    @ApiModelProperty(value = "카테고리 PK")
    private String dpCtgryId = "";

    @ApiModelProperty(value = "카테고리 명")
    private String categoryName = "";

    @ApiModelProperty(value = "브랜드명")
    private String brandName = "";

    @ApiModelProperty(value = "컨테츠 PK - (전시브랜드, 상품)")
    private Long contentsId = 0L;

    @ApiModelProperty(value = "컨텐츠 유형")
    private String contentsType = "";

    @ApiModelProperty(value = "브랜드 이미지 - main")
    private String brandImageMain = "";

    @ApiModelProperty(value = "브랜드 이미지 - main over")
    private String brandImageMainOver = "";

    @ApiModelProperty(value = "브랜드 이미지 - title banner")
    private String brandImageTitleBanner = "";

    @ApiModelProperty(value = "표준 카테고리 PK")
    private String ilCtgryId = "";

    @ApiModelProperty(value = "인벤토리 코드")
    private String inventoryCode = "";

    @ApiModelProperty(value = "인벤토리 PK")
    private Long dpInventoryId = 0L;

    @ApiModelProperty(value = "컨텐츠 레벨")
    private String contentsLevel = "";

    @ApiModelProperty(value = "레벨1 컨텐츠 PK")
    private Long level1ContentsId = 0L;

    @ApiModelProperty(value = "레벨2 컨텐츠 PK")
    private Long level2ContentsId = 0L;

    @ApiModelProperty(value = "전시 브랜드 사용여부")
    private String dpBrandUseYn = "";

    @ApiModelProperty(value = "컨텐츠 레벨 하위 정보")
    private List<ContentsDetailVo> contentsLowerList;

    @ApiModelProperty(value = "상품 정보 조회 결과")
    private GoodsSearchResultDto goodsInfo;

    @ApiModelProperty(value = "컨텐츠 혼합사용을 위한 하위 정보")
    private List<ContentsDetailVo> tmpContentsLowerList;

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
