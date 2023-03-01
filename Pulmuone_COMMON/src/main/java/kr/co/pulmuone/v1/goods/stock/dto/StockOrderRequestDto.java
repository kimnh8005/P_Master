package kr.co.pulmuone.v1.goods.stock.dto;

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
@ApiModel(description = "StockOrderRequestDto")
public class StockOrderRequestDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "상품ID")
	private long ilGoodsId;

	@ApiModelProperty(value = "등록자ID")
	private long createId;

	@ApiModelProperty(value = "수정자ID")
	private long modifyId;

	@ApiModelProperty(value = "주문수량")
	private int orderQty;

	@ApiModelProperty(value = "예정일")
	private String scheduleDt;

	@ApiModelProperty(value = "주문여부 : Y(주문), N(주문취소)")
	private String orderYn;

	@ApiModelProperty(value = "매장주문여부")
	private String storeYn;

	@ApiModelProperty(value = "기타정보(주문번호등)")
	private String memo;

	@ApiModelProperty(value = "주문리스트")
	private List<StockOrderRequestDto> orderList;

}
