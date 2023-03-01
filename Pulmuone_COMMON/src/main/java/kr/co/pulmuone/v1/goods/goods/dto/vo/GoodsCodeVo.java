package kr.co.pulmuone.v1.goods.goods.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GoodsCodeVo")
public class GoodsCodeVo
{

	@ApiModelProperty(value = "풀무원샵 코드")
	private String goodsNo;

}
