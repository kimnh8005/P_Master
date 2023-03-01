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
@ApiModel(description = "식단 컨텐츠 리스트 조회 request dto")
public class MealContsListRequestDto extends BaseRequestPageDto {

    @ApiModelProperty(value = "식단품목코드")
    private String searchIlGoodsDailyMealContsCd;

    @ApiModelProperty(value = "식단품목코드 리스트")
    private List<String> ilGoodsDailyMealContsCdList;

    @ApiModelProperty(value = "식단명")
    private String searchIlGoodsDailyMealNm;

    @ApiModelProperty(value = "판매처 그룹")
    private String mallDiv;

    @ApiModelProperty(value = "기간검색유형")
    private String dateSearchType;

    @ApiModelProperty(value = "기간검색 시작일자")
    private String startDt;

    @ApiModelProperty(value = "기간검색 종료일자")
    private String endDt;
}
