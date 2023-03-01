package kr.co.pulmuone.v1.goods.item.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemPoTypeVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "ItemPoTypeResponseDto")
public class ItemPoTypeResponseDto extends BaseResponseDto{

	@ApiModelProperty(value = "레코드 상세")
	private	ItemPoTypeVo rows;

}
