package kr.co.pulmuone.v1.goods.item.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import kr.co.pulmuone.v1.goods.item.dto.vo.MealPatternGoodsListVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "식단 정보 엑셀 MealInfoExcelRequestDto")
public class MealInfoExcelRequestDto extends BaseRequestPageDto{

    //다운로드
	@ApiModelProperty(value = "다운로드 정보 타입 (패턴/스케쥴)")
    private String downloadType;

	@ApiModelProperty(value = "식단분류 명(몰 구분 : MALL_DIV.BABYMEAL, MALL_DIV.EATSLIM)")
    private String mallDivNm;

	@ApiModelProperty(value = "식단패턴기본정보 코드")
    private String patternCd;

    @ApiModelProperty(value = "식단패턴 명")
    private String patternNm;

    @ApiModelProperty(value = "식단패턴 명")
    private String patternNmExcel;

	@ApiModelProperty(value = "다운로드 시작일")
    private String downloadDateStart;

	@ApiModelProperty(value = "다운로드 종료일")
    private String downloadDateEnd;

	//업로드 (추가)
    @ApiModelProperty(value = "패턴순번")
    private int patternNo;

    @ApiModelProperty(value = "세트순번")
    private int setNo;

    @ApiModelProperty(value = "세트코드")
    private String setCd;

    @ApiModelProperty(value = "세트명")
    private String setNm;

    @ApiModelProperty(value = "식단품목코드")
    private String mealContsCd;

}
