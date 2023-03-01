package kr.co.pulmuone.v1.promotion.exhibit.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class SelectByUserVo {

    @ApiModelProperty(value = "기획전 PK")
    private String evExhibitId;

    @ApiModelProperty(value = "제목")
    private String title;

    @ApiModelProperty(value = "설명")
    private String description;

    @ApiModelProperty(value = "상시진행여부")
    private String alwaysYn;

    @ApiModelProperty(value = "시작일자")
    private String startDate;

    @ApiModelProperty(value = "종료일자")
    private String endDate;

    @ApiModelProperty(value = "상품별 구매가능 수량")
    private int goodsBuyLimitCount;

    @ApiModelProperty(value = "기본 구매 수량")
    private int defaultBuyCount;

    @ApiModelProperty(value = "골라담기 균일가")
    private int selectPrice;

    @ApiModelProperty(value = "상세 HTML")
    private String detailHtml;

    @ApiModelProperty(value = "WEB PC 전시여부")
    private String displayWebPcYn;

    @ApiModelProperty(value = "WEB Mobile 전시여부")
    private String displayWebMobileYn;

    @ApiModelProperty(value = "APP 전시여부")
    private String displayAppYn;

    @ApiModelProperty(value = "비회원 노출 여부")
    private String displayNonmemberYn;

    @ApiModelProperty(value = "임직원 전용 유형")
    private String evEmployeeType;

    @ApiModelProperty(value = "종료여부")
    private String endYn;

    @ApiModelProperty(value = "접근권한 설정 유형")
    private List<ExhibitUserGroupByUserVo> userGroupList;

    @ApiModelProperty(value = "대표상품 PK")
    private Long ilGoodsId;

    @ApiModelProperty(value = "골라담기 상품 list")
    private List<Long> goodsList;

    @ApiModelProperty(value = "골라담기 추가 상품 list")
    private List<Long> addGoodsList;

}
