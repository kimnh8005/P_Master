package kr.co.pulmuone.v1.api.babymeal.dto;

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
@ApiModel(description = "베이비밀 주문조회 Dto")
public class BabymealOrderInfoDto{

	@JsonAlias({ "goodsCode" })
	@ApiModelProperty(value = "I/F 상품 코드")
	private String ilGoodsId;

	@ApiModelProperty(value = "상품명")
	private String goodsNm;

	@JsonAlias({ "orderNo" })
	@ApiModelProperty(value = "I/F 주문번호")
	private String orderNum;

	@ApiModelProperty(value = "최종배송예정일자")
	private String lastDeliveryDate;

	@ApiModelProperty(value = "배송요일")
	private String goodsDailyCycleTermDays;

	@ApiModelProperty(value = "배송회차(남은회차)")
	private int saleSeq;

	@JsonAlias({ "allergyType" })
	@ApiModelProperty(value = "제외식품 타입")
	private String allergyType;
}