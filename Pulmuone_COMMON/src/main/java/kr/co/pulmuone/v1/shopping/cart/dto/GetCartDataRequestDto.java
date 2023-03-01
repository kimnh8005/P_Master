package kr.co.pulmuone.v1.shopping.cart.dto;

import java.time.LocalDate;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "getCartData 요청 DTO")
public class GetCartDataRequestDto {

	@ApiModelProperty(value = "사용자 환경 정보 PK", hidden = true)
	private String urPcidCd;

	@ApiModelProperty(value = "회원 PK", hidden = true)
	private Long urUserId;

	@ApiModelProperty(value = "디바이스 정보", hidden = true)
	private String deviceInfo;

	@ApiModelProperty(value = "앱 여부", hidden = true)
	private boolean app;

	@ApiModelProperty(value = "회원 여부", hidden = true)
	private boolean member;

	@ApiModelProperty(value = "임직원 여부", hidden = true)
	private boolean employee;

	@ApiModelProperty(value = "임직원 코드", hidden = true)
	private String urErpEmployeeCode;

	@ApiModelProperty(value = "우편번호", hidden = true)
	private String receiverZipCode;

	@ApiModelProperty(value = "배송지 건물 관리 번호", hidden = true)
	private String buildingCode;

	@ApiModelProperty(value = "장바구니 타입", required = true)
	private String cartType;

	@ApiModelProperty(value = "장바구니 PK")
	private List<Long> spCartId;

	@ApiModelProperty(value = "Summery용 장바구니 PK")
	private List<Long> summerySpCartId;

	@ApiModelProperty(value = "임직원 구매 여부")
	private String employeeYn;

	@ApiModelProperty(value = "브릿지 페이지 정보 여부")
	private String bridgeYn;

	@ApiModelProperty(value = "선물하기여부")
	private String presentYn;

	// ↓↓↓주문서 작성시 사용
	@ApiModelProperty(value = "배송 스케쥴 변경 정보 리스트", hidden = true)
	private List<ChangeArrivalScheduledDto> arrivalScheduled;

	@ApiModelProperty(value = "상품 쿠폰 사용 정보", hidden = true)
	private List<UseGoodsCouponDto> useGoodsCoupon;

	public UseGoodsCouponDto findUseGoodsCoupon(Long spCartId) {
		if (useGoodsCoupon != null) {
			return useGoodsCoupon.stream().filter(dto -> dto.getSpCartId().equals(spCartId)).findAny().orElse(null);
		}
		return null;
	}

	@ApiModelProperty(value = "배송 쿠폰 사용 정보", hidden = true)
	private List<UseShippingCouponDto> useShippingCoupon;

	public UseShippingCouponDto findUseShippingCoupon(int shippingTemplateDataIndex) {
		if (useShippingCoupon != null) {
			return useShippingCoupon.stream()
					.filter(dto -> dto.getShippingIndex() == shippingTemplateDataIndex)
					.findAny().orElse(null);
		}

		return null;
	}

	@ApiModelProperty(value = "장바구니 쿠폰 사용 정보", hidden = true)
	private CouponDto useCartCoupon;

	@ApiModelProperty(value = "증정품 선택 리스트")
	private List<CartGiftDto> gift;

	@ApiModelProperty(value = "상품정보 리스트")
	private List<ArrivalGoodsDto> arrivalGoods;

	public ArrivalGoodsDto findArrivalGoodsDto(Long spCartId) {
		if(CollectionUtils.isNotEmpty(arrivalGoods)) {
			return arrivalGoods.stream().filter(dto -> dto.getSpCartId().equals(spCartId)).findAny().orElse(null);
		}
		return null;
	}

	@ApiModelProperty(value = "정기/매장 배송 도착예정일", hidden = true)
	private LocalDate nextArrivalScheduledDate;

	@ApiModelProperty(value = "매장 배송 유형")
	private String storeDeliveryType;

	@ApiModelProperty(value = "BOS 주문생성 주문여부(판매대기,관리자품절 판매가능 체크 위해서)")
	private boolean isBosCreateOrder;

	@ApiModelProperty(value = "배송비무료 여부")
	private String freeShippingPriceYn = "N";
}
