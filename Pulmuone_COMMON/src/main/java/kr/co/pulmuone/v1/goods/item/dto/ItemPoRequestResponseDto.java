package kr.co.pulmuone.v1.goods.item.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemPoRequestVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemPoTypeVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "ItemPoRequestResponseDto")
public class ItemPoRequestResponseDto extends BaseResponseDto {

	@ApiModelProperty(value = "레코드 목록")
	private	List<ItemPoRequestVo> rows;

	@ApiModelProperty(value = "레코드 카운트 수")
	private long total;

	@ApiModelProperty(value = "레코드 상세")
	private	ItemPoRequestVo detail;
}
