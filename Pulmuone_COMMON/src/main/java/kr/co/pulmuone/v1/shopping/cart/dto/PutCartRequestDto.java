package kr.co.pulmuone.v1.shopping.cart.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "장바구니 수정 요청 DTO")
public class PutCartRequestDto {

	@ApiModelProperty(value = "장바구니 PK")
	private Long spCartId;

	@ApiModelProperty(value = "수량")
	private int qty;

	@ApiModelProperty(value = "추가 구성상품 리스트")
	private List<AddCartInfoAdditionalGoodsRequestDto> additionalGoodsList;

	@ApiModelProperty(value = "예약 정보 PK")
	private Long ilGoodsReserveOptionId;

	@ApiModelProperty(value = "일일 배송주기코드")
	private String goodsDailyCycleType;

	@ApiModelProperty(value = "일일 배송기간코드")
	private String goodsDailyCycleTermType;

	@ApiModelProperty(value = "일일 배송 녹즙 요일 코드")
	private String goodsDailyCycleGreenJuiceWeekType[];

	@ApiModelProperty(value = "알러지 식단 여부")
	private String goodsDailyAllergyYn;

	@ApiModelProperty(value = "일괄 배송 여부")
	private String goodsDailyBulkYn;

	@ApiModelProperty(value = "일괄배송 배송 세트 코드")
	private String goodsBulkType;


	@ApiModelProperty(value = "사용자 환경 정보 PK", hidden = true)
	private String urPcidCd;

	@ApiModelProperty(value = "회원 PK", hidden = true)
	private Long urUserId;

	@ApiModelProperty(value = "상품 PK", required = true)
	private Long ilGoodsId;

	@ApiModelProperty(value = "배송타입", required = true)
	private String deliveryType;

	@ApiModelProperty(value = "판매유형", required = true)
	private String saleType;

}
