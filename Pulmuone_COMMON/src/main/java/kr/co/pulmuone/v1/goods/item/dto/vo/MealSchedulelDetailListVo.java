package kr.co.pulmuone.v1.goods.item.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.enums.StoreEnums;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@ApiModel(description = "식단 스케쥴정보 MealSchedulelDetailListVo")
public class MealSchedulelDetailListVo {

    @ApiModelProperty(value = "식단 스케쥴 상세ID")
    private long schId;

    @ApiModelProperty(value = "식단패턴기본정보 코드")
    private String patternCd;
    
    @ApiModelProperty(value = "식단패턴기본정보 코드명")
    private String patternNm;

    @ApiModelProperty(value = "식단 패턴 상세ID")
    private long patternDetlId;

    @ApiModelProperty(value = "식단품목코드")
    private String mealContsCd;

    @ApiModelProperty(value = "식단품목명")
    private String mealNm;

    @ApiModelProperty(value = "패턴순번")
    private int patternNo;

    @ApiModelProperty(value = "세트순번")
    private int setNo;

    @ApiModelProperty(value = "세트코드")
    private String setCd;

    @ApiModelProperty(value = "세트명")
    private String setNm;

    @ApiModelProperty(value = "도착예정일")
    private LocalDate deliveryDate;

    @ApiModelProperty(value = "도착예정일")
    private String deliveryDateStr;

    @ApiModelProperty(value = "도착예정일 (엑셀다운로드형식)")
    private String deliveryDateExcelStr;

    @ApiModelProperty(value = "도착요일")
    private String deliveryWeekCode;

    @ApiModelProperty(value = "휴일 여부")
    private String holidayYn;

    @ApiModelProperty(value = "휴일 명")
    private String holidayNm;

    @ApiModelProperty(value = "등록자")
    private String createId;

    @ApiModelProperty(value = "수정자")
    private String modifyId;

    @ApiModelProperty(value = "등록일")
    private String createDt;

    @ApiModelProperty(value = "수정일")
    private String modifyDt;

    @ApiModelProperty(value = "알러지 여부")
    private String allergyYn;

}
