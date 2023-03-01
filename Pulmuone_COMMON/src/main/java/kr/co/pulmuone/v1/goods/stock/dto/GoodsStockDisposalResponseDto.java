package kr.co.pulmuone.v1.goods.stock.dto;

import com.github.pagehelper.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.goods.stock.dto.vo.GoodsStockDisposalResultVo;
import kr.co.pulmuone.v1.goods.stock.dto.vo.StockExprResultVo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Builder
@Getter
@Setter
@ToString
@ApiModel(description = "GoodsStockDisposalResponseDto")
@NoArgsConstructor
@AllArgsConstructor
public class GoodsStockDisposalResponseDto extends BaseResponseDto {

    @ApiModelProperty(value = "재고기준일시")
    private String baseTimestamp;

    @ApiModelProperty(value = "총 count")
    private long total;

    @ApiModelProperty(value = "결과값 row")
    private Page<GoodsStockDisposalResultVo> rows;

}