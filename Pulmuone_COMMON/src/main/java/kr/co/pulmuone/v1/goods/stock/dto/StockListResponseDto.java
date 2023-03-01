package kr.co.pulmuone.v1.goods.stock.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.goods.stock.dto.vo.StockListResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "StockListResponseDto")
public class StockListResponseDto extends BaseResponseDto {

	@ApiModelProperty(value = "결과값 row")
	private	List<StockListResultVo> rows;

	@ApiModelProperty(value = "총 count")
	private long total;

	@ApiModelProperty(value = "품목별 재고리스트 Vo")
	private StockListResultVo stockListResultVo;

}
