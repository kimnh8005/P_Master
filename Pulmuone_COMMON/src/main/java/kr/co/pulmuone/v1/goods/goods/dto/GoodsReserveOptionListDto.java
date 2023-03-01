package kr.co.pulmuone.v1.goods.goods.dto;

import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "예약상품 옵션 리스트 Dto")
public class GoodsReserveOptionListDto
{
	@ApiModelProperty(value = "예약 정보 PK")
	private Long ilGoodsReserveOptionId;

	@ApiModelProperty(value = "예약 정보 배송회차")
	private int saleSeq;

	@ApiModelProperty(value = "예약주문 시작시간")
	private LocalDateTime reserveStartDate;

	@ApiModelProperty(value = "예약주문 마감시간")
	private LocalDateTime reserveEndDate;

	@ApiModelProperty(value = "예약 정보 도착일자")
	private String arriveDate;

	@ApiModelProperty(value = "예약 정보 주문 가능 수량 ")
	private int stockQty;
}
