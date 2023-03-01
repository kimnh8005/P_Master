package kr.co.pulmuone.v1.goods.stock.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
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
@ApiModel(description = "GoodsStockDisposalRequestDto")
@NoArgsConstructor
@AllArgsConstructor
public class GoodsStockDisposalRequestDto extends BaseRequestPageDto {

    @ApiModelProperty(value = "기준일자")
    private String searchBaseDt;

    @ApiModelProperty(value = "재고구분")
    private String stockExprTp;

    @ApiModelProperty(value = "재고구분 array")
    private List<String> stockExprTpList;

    @ApiModelProperty(value = "공급업체")
    private String urSupplierId;

    @ApiModelProperty(value = "출고처")
    private String urWarehouseId;

}
