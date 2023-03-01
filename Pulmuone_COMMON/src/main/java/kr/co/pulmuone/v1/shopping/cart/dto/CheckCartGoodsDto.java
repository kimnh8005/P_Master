package kr.co.pulmuone.v1.shopping.cart.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "장바구니 체크 응답 DTO")
public class CheckCartGoodsDto {
	@ApiModelProperty(value = "상품 PK")
	private Long spCartId;

	@ApiModelProperty(value = "상품 PK")
	private Long ilGoodsId;

	@ApiModelProperty(value = "상품명")
	private String goodsName;

	@ApiModelProperty(value = "최대 또는 최소 구매수량")
	private int qty;

	@ApiModelProperty(value = "상품 상태 (프론트 정의 판매 상태)")
	private String saleStatus;

	@ApiModelProperty(value = "주소기반으로 배송 가능 여부")
	private boolean isShippingPossibility;

	@ApiModelProperty(value = "배송불가 내용")
	private String shippingImpossibilityMsg;

	@ApiModelProperty(value = "회원 구매 권한 타입")
	private String buyPurchaseType;


	public CheckCartGoodsDto(Long ilGoodsId, String goodsName, int qty) {
		this.ilGoodsId = ilGoodsId;
		this.goodsName = goodsName;
		this.qty = qty;
	}

	public CheckCartGoodsDto(Long spCartId, Long ilGoodsId, String goodsName, int qty) {
		this.spCartId = spCartId;
		this.ilGoodsId = ilGoodsId;
		this.goodsName = goodsName;
		this.qty = qty;
	}

	public CheckCartGoodsDto(Long spCartId, Long ilGoodsId, String goodsName, String saleStatus, boolean isShippingPossibility, String buyPurchaseType, String shippingImpossibilityMsg) {
		this.spCartId = spCartId;
		this.ilGoodsId = ilGoodsId;
		this.goodsName = goodsName;
		this.saleStatus = saleStatus;
		this.isShippingPossibility = isShippingPossibility;
		this.shippingImpossibilityMsg = shippingImpossibilityMsg;
		this.buyPurchaseType = buyPurchaseType;
	}
}
