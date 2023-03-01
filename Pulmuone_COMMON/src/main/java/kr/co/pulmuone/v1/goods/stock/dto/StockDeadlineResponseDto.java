package kr.co.pulmuone.v1.goods.stock.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.goods.stock.dto.vo.StockDeadlineResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "StockDeadlineResponseDto")
public class StockDeadlineResponseDto extends BaseResponseDto {

	@ApiModelProperty(value = "결과값 row")
	private	List<StockDeadlineResultVo> rows;

	@ApiModelProperty(value = "총 count")
	private long total;

	@ApiModelProperty(value = "재고 기한관리 Vo")
	private StockDeadlineResultVo stockDeadlineResultVo;

}
