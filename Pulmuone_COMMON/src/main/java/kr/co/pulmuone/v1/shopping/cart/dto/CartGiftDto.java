package kr.co.pulmuone.v1.shopping.cart.dto;


import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "주문서 증정품 정보")
public class CartGiftDto {

	/**
	 * 증정품 행사 PK
	 */
	private Long evExhibitId;

	/**
	 * 증정품 상품 PK
	 */
	private Long ilGoodsId;

	/**
	 * 증정품 행사 제목
	 */
	private String title;

	/**
	 * 증정품 지급상태
	 */
	private String giftState;

}
