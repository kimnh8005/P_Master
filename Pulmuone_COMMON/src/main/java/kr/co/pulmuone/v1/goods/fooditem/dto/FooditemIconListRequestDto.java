package kr.co.pulmuone.v1.goods.fooditem.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "식단품목아이콘리스트 RequestDto")
public class FooditemIconListRequestDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "아이콘명")
	private String searchIconName;

	@ApiModelProperty(value = "사용여부")
	private String searchUseYn;

}
