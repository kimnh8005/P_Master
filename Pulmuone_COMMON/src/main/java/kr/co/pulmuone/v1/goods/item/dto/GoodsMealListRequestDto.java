package kr.co.pulmuone.v1.goods.item.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "일일상품 식단 등록 내역 리스트 조회 request dto")
public class GoodsMealListRequestDto extends BaseRequestPageDto {

    @ApiModelProperty(value = "코드검색")
    private String findKeyword;

    @ApiModelProperty(value = "코드검색 리스트")
    private List<String> findKeywordList;

    @ApiModelProperty(value = "상품명")
    private String searchGoodsName;

    @ApiModelProperty(value = "식단패턴코드")
    private String searchMealPatternCd;

    @ApiModelProperty(value = "식단패턴코드 리스트")
    private List<String> searchMealPatternCdList;

    @ApiModelProperty(value = "식단패턴명")
    private String searchMealPatternNm;

    @ApiModelProperty(value = "식단유형")
    private String mallDiv;
}
