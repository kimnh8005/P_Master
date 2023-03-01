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
@ApiModel(description = "장바구니 담기 요청 DTO")
public class AddCartInfoRequestDto {

	@ApiModelProperty(value = "사용자 환경 정보 PK", hidden = true)
	private String urPcidCd;

	@ApiModelProperty(value = "회원 PK", hidden = true)
	private Long urUserId;

	@ApiModelProperty(value = "판매타입", hidden = true)
	private String saleType;

	@ApiModelProperty(value = "일일 상품 유형 타입", hidden = true)
	private String goodsDailyType;

	@ApiModelProperty(value = "배송타입", hidden = true)
	private String deliveryType;

	@ApiModelProperty(value = "상품 PK", required = true)
	private Long ilGoodsId;

	@ApiModelProperty(value = "구매 수량", required = true)
	private int qty;

	@ApiModelProperty(value = "추가 구성 상품 리스트")
	private List<AddCartInfoAdditionalGoodsRequestDto> additionalGoodsList;

	@ApiModelProperty(value = "예약 정보 PK")
	private Long ilGoodsReserveOptionId;

	@ApiModelProperty(value = "바로구매 여부 (Y: 바로구매)", required = true)
	private String buyNowYn;

	@ApiModelProperty(value = "일일 배송주기코드")
	private String goodsDailyCycleType;

	@ApiModelProperty(value = "일일 배송기간코드")
	private String goodsDailyCycleTermType;

	@ApiModelProperty(value = "일일 배송 녹즙 요일 코드 리스트")
	private String goodsDailyCycleGreenJuiceWeekType[];

	@ApiModelProperty(value = "알러지 식단 여부")
	private String goodsDailyAllergyYn;

	@ApiModelProperty(value = "일괄 배송 여부")
	private String goodsDailyBulkYn;

	@ApiModelProperty(value = "일괄배송 배송 세트 코드")
	private String goodsBulkType;

	@ApiModelProperty(value = "정기배송 구매 여부")
	private String regularDeliveryYn;

	@ApiModelProperty(value = "매장배송 구매 여부")
	private String storeDeliveryYn;

	@ApiModelProperty(value = "외부광고코드PK", hidden = true)
	private String pmAdExternalCd;

	@ApiModelProperty(value = "내부광고코드-페이지코드")
	private String pmAdInternalPageCd;

	@ApiModelProperty(value = "내부광고코드-영역코드")
	private String pmAdInternalContentCd;

	@ApiModelProperty(value = "증정품 판매 가능여부")
	private boolean isGoodsGiftPossible;
}
