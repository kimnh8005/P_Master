package kr.co.pulmuone.v1.goods.item.dto;

import java.util.List;

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
@ApiModel(description = "ItemPoTypeListResponseDto")
public class ItemPoTypeListResponseDto extends BaseResponseDto {

	@ApiModelProperty(value = "레코드 목록")
	private	List<ItemPoTypeVo> rows;

	@ApiModelProperty(value = "레코드 카운트 수")
	private long total;

}
