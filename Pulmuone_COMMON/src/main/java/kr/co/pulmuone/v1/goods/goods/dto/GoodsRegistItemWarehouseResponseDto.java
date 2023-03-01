package kr.co.pulmuone.v1.goods.goods.dto;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsRegistItemWarehouseVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "배송 출고처 Response")
public class GoodsRegistItemWarehouseResponseDto extends BaseResponseDto{
	@ApiModelProperty(value = "")
	private	List<GoodsRegistItemWarehouseVo> rows = new ArrayList<GoodsRegistItemWarehouseVo>();
}
