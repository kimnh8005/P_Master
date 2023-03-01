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
@ApiModel(description = "주문서 정보 조회 요청 DTO")
public class GetOrderPageInfoRequestDto {

	@ApiModelProperty(value = "장바구니 타입")
	private String cartType;

	@ApiModelProperty(value = "장바구니 PK")
	private List<Long> spCartId;

	@ApiModelProperty(value = "임직원 구매 여부")
	private String employeeYn;

	@ApiModelProperty(value = "배송 스케쥴 변경 정보 리스트")
	private List<ChangeArrivalScheduledDto> arrivalScheduled;

	@ApiModelProperty(value = "상품정보 리스트")
	private List<ArrivalGoodsDto> arrivalGoods;

	@ApiModelProperty(value = "매장 배송 유형")
	private String storeDeliveryType;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@ApiModelProperty(value = "매장 도착예정일")
	private LocalDate storeArrivalScheduledDate;

	@ApiModelProperty(value = "매장배송 회차 PK")
	private Long urStoreScheduleId;

	@ApiModelProperty(value = "선물하기여부")
	private String presentYn;
}
