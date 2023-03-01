package kr.co.pulmuone.v1.shopping.cart.dto;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "재고수량 부족으로 도착예정일 알림 DTO")
public class GoodsLackStockNotiDto {

	@ApiModelProperty(value = "장바구니 배송정책 index")
	private int cartShippingListIndex;

	@ApiModelProperty(value = "새벽배송 여부")
	private String dawnDeliveryYn;

	@ApiModelProperty(value = "변경전 도착 예정일자(yyyy-mm-dd)")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate prevArrivalScheduledDate;

	@ApiModelProperty(value = "변경후 도착 예정일자(yyyy-mm-dd)")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate afterArrivalScheduledDate;

	@ApiModelProperty(value = "대상 상품명")
	private List<String> goodsNameList;
}
