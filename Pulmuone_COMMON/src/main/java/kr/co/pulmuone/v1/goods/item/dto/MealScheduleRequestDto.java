package kr.co.pulmuone.v1.goods.item.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import kr.co.pulmuone.v1.goods.item.dto.vo.MealPatternDetailListVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.MealPatternGoodsListVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.MealSchedulelListVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "식단 스케쥴 MealScheduleRequestDto")
public class MealScheduleRequestDto extends BaseRequestPageDto{

	@ApiModelProperty(value = "식단패턴기본정보 코드")
    private String patternCd;

	@ApiModelProperty(value = "스케쥴 기간 시작일")
    private String dateSearchStart;

	@ApiModelProperty(value = "스케쥴 기간 종료일")
    private String dateSearchEnd;

	@ApiModelProperty(value = "패턴기간 시작일")
    private String patternStartDt;

	@ApiModelProperty(value = "패턴기간 종료일")
    private String patternEndDt;

	@ApiModelProperty(value = "등록자 ID")
    private long createId;

	@ApiModelProperty(value = "등록일")
    private String createDt;

	@ApiModelProperty(value = "수정자 ID")
    private long modifyId;

	@ApiModelProperty(value = "수정일")
    private String modifyDt;
	
	@ApiModelProperty(value = "휴일여부")
    private String holidayYn;
	
	@ApiModelProperty(value = "휴일명")
    private String holidayNm;

	@ApiModelProperty(value = "식단품목코드")
    private String mealContsCd;

	@ApiModelProperty(value = "기존 식단품목코드")
    private String originMealContsCd;
	
	@ApiModelProperty(value = "일괄변경여부")
    private String bulkUpdateYn;

	@ApiModelProperty(value = "식단 스케쥴 상세ID")
    private long schId;

}
