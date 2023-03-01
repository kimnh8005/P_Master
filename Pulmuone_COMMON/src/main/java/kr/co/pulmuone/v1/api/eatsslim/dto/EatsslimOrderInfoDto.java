package kr.co.pulmuone.v1.api.eatsslim.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "잇슬림 주문조회 Dto")
public class EatsslimOrderInfoDto{

	@JsonAlias({ "groupCode" })
	@ApiModelProperty(value = "I/F 상품 코드")
	private String ilGoodsId;

	@ApiModelProperty(value = "상품명")
	private String goodsNm;

	@JsonAlias({ "orderNum" })
	@ApiModelProperty(value = "I/F 주문번호")
	private String orderNum;

	@JsonAlias({ "oSubNum" })
	@ApiModelProperty(value = "I/F 서브주문번호")
	private String oSubNum;

	@ApiModelProperty(value = "최종배송예정일자")
	private String lastDeliveryDate;

	@ApiModelProperty(value = "배송요일")
	private String goodsDailyCycleTermDays;

	@JsonAlias({ "payType" })
	@ApiModelProperty(value = "결제수단 (10:신용카드, 20:계좌이체, 30:무통장 , 40:쿠폰)")
	private String orderPaymentType;

	@ApiModelProperty(value = "배송회차(남은회차)")
	private int saleSeq;
}