package kr.co.pulmuone.v1.goods.stock.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.goods.stock.dto.vo.StockDeadlineHistResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "StockDeadlineHistResponseDto")
public class StockDeadlineHistResponseDto extends BaseResponseDto {

	@ApiModelProperty(value = "결과값 row")
	private	List<StockDeadlineHistResultVo> rows;

	@ApiModelProperty(value = "총 count")
	private long total;

	@ApiModelProperty(value = "재고 기한관리 이력 Vo")
	private StockDeadlineHistResultVo stockDeadlineHistResultVo;

}
