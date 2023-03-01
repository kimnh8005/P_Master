package kr.co.pulmuone.v1.goods.goods.dto;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "배송 출고처 Request")
public class GoodsRegistItemWarehouseRequestDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "품목코드", required = true)
	private String ilItemCode;

	@ApiModelProperty(value = "매장(가맹점) 여부", required = true)
	private String storeYn;

	@ApiModelProperty(value = "출고처 ID", required = true)
	private String urWarehouseId;

	@ApiModelProperty(value = "상품유형", required = true)
	private String goodsType;
}
