package kr.co.pulmuone.v1.goods.stock.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.goods.stock.dto.vo.StockExprResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "StockExprResponseDto")
public class StockExprResponseDto extends BaseResponseDto {

	@ApiModelProperty(value = "결과값 row")
	private	List<StockExprResultVo> rows;

	@ApiModelProperty(value = "총 count")
	private long total;

	@ApiModelProperty(value = "연동 재고 내역관리 Vo")
	private StockExprResultVo stockExprResultVo;

}
