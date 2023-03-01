package kr.co.pulmuone.v1.pg.dto;

import java.util.List;

import kr.co.pulmuone.v1.comm.enums.OrderEnums;
import kr.co.pulmuone.v1.shopping.cart.dto.CartGiftDto;
import kr.co.pulmuone.v1.shopping.cart.dto.GoodsLackStockNotiDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import io.swagger.annotations.ApiModelProperty;

@Getter
@Setter
@ToString
public class BasicDataResponseDto {
	@ApiModelProperty(value = "PG 정보")
	private List<PaymentFormDto> pgFormDataList;
	
	@ApiModelProperty(value = "PG 실행 스크립트 타입")
	private String exeScriptType;

	@ApiModelProperty(value = "주문PK")
	private String odOrderId;

	@ApiModelProperty(value = "주문번호")
	private String odid;

	@ApiModelProperty(value = "결제마스터PK")
	private Long odPaymentMasterId;

	@ApiModelProperty(value = "결제금액")
	private int paymentPrice;

	@ApiModelProperty(value = "사은품")
	private List<CartGiftDto> gift;

	@ApiModelProperty(value = "재고부족 여부")
	private String goodsLackStockNotiYn;

	@ApiModelProperty(value = "재고부족 안내 정보")
	private List<GoodsLackStockNotiDto> goodsLackStockNoti;

	@ApiModelProperty(value = "주문 생성 결과 상태")
	private OrderEnums.OrderRegistrationResult result;

	@ApiModelProperty(value = "실패메세지")
	private String failMessage;
}
