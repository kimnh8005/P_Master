package kr.co.pulmuone.v1.goods.goods.dto;

import java.util.HashMap;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "일일배송 주기 정보 Dto")
public class GoodsDailyCycleDto
{

	@ApiModelProperty(value = "일일 배송주기코드")
	private String goodsDailyCycleType;

	@ApiModelProperty(value = "일일 배송주기 요일 개수")
	private int goodsDailyCycleTypeDayQty;

	@ApiModelProperty(value = "일일 배송주기명")
	private String goodsDailyCycleTypeName;

	@ApiModelProperty(value = "일일 배송주기명")
	private String goodsDailyCycleTypeWeekText;

	@ApiModelProperty(value = "선택 가능 요일 개수")
	private int goodsDailyCycleTypeQty;

	@ApiModelProperty(value = "일일 배송기간 정보 리스트")
	private List<HashMap<String,String>> term;

	@ApiModelProperty(value = "일일 배송 녹즙 요일 정보 리스트")
	private List<HashMap<String,String>> week;

	@ApiModelProperty(value = "일일 배송주기 활성화 여부")
	private Boolean isEnable;

	@ApiModelProperty(value = "알러지 식단 선택여부")
	private String goodsDailyAllergyYn;

	@ApiModelProperty(value = "일괄 배송 가능 여부")
	private String goodsDailyBulkYn;

	@ApiModelProperty(value = "일괄배송 배송 세트 정보 리스트")
	private List<HashMap<String, String>> goodsDailyBulk;

}
