package kr.co.pulmuone.v1.batch.goods.item.dto;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "ErpGoods3PLHeaderRequestDto")
@Builder
public class ErpGoods3PLUpdateHeaderCondRequestDto {

	@JsonIgnore
    private String itmNo;

	/*
	 * condition
	 */
	@JsonProperty("condition")
    private Map<String, Object> condition() {

        Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("itmNo", this.itmNo);
        return conditionMap;
    }

    @ApiModelProperty(value = "ERP 상품명")
    private String itmNam;

    @ApiModelProperty(value = "KANCODE")
    private String kanCd;

    @ApiModelProperty(value = "상품박스규격(mm)")
    private String boxSiz;

    @ApiModelProperty(value = "상품낱개규격(mm)")
    private String eaSiz;

    @ApiModelProperty(value = "입수수량")
    private String boxEa;

    @ApiModelProperty(value = "박스바코드")
    private String boxKanCd;

    @ApiModelProperty(value = "고객사코드")
    private String strKey;

    @ApiModelProperty(value = "파렛트당박스수")
    private String boxCrr;

    @ApiModelProperty(value = "외박스코드")
    private String extBoxCd;

    @ApiModelProperty(value = "풀무원 전송일시")
    private String regDat;

    @ApiModelProperty(value = "정상유통기간정보")
    private String durDay;

    @ApiModelProperty(value = "온라인출고기한")
    private String limOut;

    @ApiModelProperty(value = "마감임박유통기한")
    private String lifDay;

    @ApiModelProperty(value = "온도값")
    private String tmpVal;

    @ApiModelProperty(value = "정보업데이트여부")
    private String updFlg;

    @ApiModelProperty(value = "정보 업데이트 일시")
    private String updDat;

    @ApiModelProperty(value = "CJ접수일시")
    private String itfDat;

    @ApiModelProperty(value = "CJ접수여부")
    private String itfFlg;
}
