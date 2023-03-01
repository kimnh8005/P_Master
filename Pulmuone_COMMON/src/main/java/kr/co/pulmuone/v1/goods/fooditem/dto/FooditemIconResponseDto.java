package kr.co.pulmuone.v1.goods.fooditem.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.goods.etc.dto.vo.CertificationVo;
import kr.co.pulmuone.v1.goods.fooditem.dto.vo.FooditemIconVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "식단품목아이콘상세 ResponseDto")
public class FooditemIconResponseDto {

	@ApiModelProperty(value = "식단품목아이콘상세")
	private FooditemIconVo fooditemIconVo;
}
