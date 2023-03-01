package kr.co.pulmuone.v1.goods.item.dto;

import java.util.ArrayList;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "올가 할인연동 내역 request dto")
public class OrgaDiscountRequestDto extends BaseRequestPageDto{

	 /*
     * 마스터 품목 리스트 검색 조건 request dto
     */
    @ApiModelProperty(value = "검색조건종류", required = false)
    private String selectConditionType;

    @ApiModelProperty(value = "품목코드", required = false)
    private String itemCodes;

    @ApiModelProperty(value = "품목코드Array", required = false)
    private ArrayList<String> itemCodesArray;

    @ApiModelProperty(value = "검색날짜시작일", required = false)
    private String startDate;

    @ApiModelProperty(value = "검색날짜종료일", required = false)
    private String endDate;
}
