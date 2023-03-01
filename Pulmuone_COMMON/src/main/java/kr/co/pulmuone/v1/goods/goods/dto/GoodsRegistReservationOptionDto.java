package kr.co.pulmuone.v1.goods.goods.dto;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@ApiModel(description = "예약판매옵션설정")
public class GoodsRegistReservationOptionDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "예약판매옵션ID", required = false)
	private String ilGoodsReserveOptionId;

	@ApiModelProperty(value = "상품ID", required = false)
	private String ilGoodsId;

	@ApiModelProperty(value = "회차", required = false)
	private String saleSequance;

	@ApiModelProperty(value = "예약 주문 가능 기간 시작일", required = false)
	private String reservationStartDate;

	@ApiModelProperty(value = "예약 주문 가능 기간 종료일", required = false)
	private String reservationEndDate;

	@ApiModelProperty(value = "주문재고", required = false)
	private String stockQuantity;

	@ApiModelProperty(value = "주문수집 I/F일", required = false)
	private String orderIfDate;

	@ApiModelProperty(value = "출고예정일", required = false)
	private String releaseDate;

	@ApiModelProperty(value = "도착예정일", required = false)
	private String arriveDate;

	@ApiModelProperty(value = "등록자", required = false)
	private String createId;

	@ApiModelProperty(value = "수정자", required = false)
	private String modifyId;
}
