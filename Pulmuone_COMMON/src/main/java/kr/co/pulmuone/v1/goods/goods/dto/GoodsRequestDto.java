package kr.co.pulmuone.v1.goods.goods.dto;

import java.time.LocalDate;
import java.util.HashMap;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.store.delivery.dto.ShippingPossibilityStoreDeliveryAreaDto;
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
@ApiModel(description = "상품상세조회 RequestDto")
public class GoodsRequestDto {
	@ApiModelProperty(value = "조회시 상품 PK로만 조회여부")
	private boolean onlySearchIlGoodsId;

	@ApiModelProperty(value = "상품 PK")
	private Long ilGoodsId;

	@ApiModelProperty(value = "디바이스 정보")
	private String deviceInfo;

	@ApiModelProperty(value = "앱 여부")
	private boolean isApp;

	@ApiModelProperty(value = "회원 여부")
	private boolean isMember;

	@ApiModelProperty(value = "임직원 여부")
	private boolean isEmployee;

	@ApiModelProperty(value = "새벽 배송 여부")
	private boolean isDawnDelivery;

	@ApiModelProperty(value = "도착 예정일")
	private LocalDate arrivalScheduledDate;

	@ApiModelProperty(value = "일일 배송주기코드")
	private String goodsDailyCycleType;

    @Builder.Default
	@ApiModelProperty(value = "구매수량")
	private int buyQty = 1;

    @ApiModelProperty(value = "매장 배송 상품 여부")
	private boolean isStoreDelivery;

    @ApiModelProperty(value = "매장 배송 정보")
	private ShippingPossibilityStoreDeliveryAreaDto storeDeliveryInfo;

    @ApiModelProperty(value = "일일상품 택배배송 여부")
	private boolean isDailyDelivery;

	@ApiModelProperty(value = "BOS 주문생성 주문여부(판매대기,관리자품절 판매가능 체크 위해서)")
	private boolean isBosCreateOrder;

	@ApiModelProperty(value = "매장 재고 실시간 조회 여부")
	private boolean isRealTimeStoreStock;

	@ApiModelProperty(value = "중복 품목 재고 정보")
	private HashMap<String, Integer> overlapBuyItem;
	
	@ApiModelProperty(value = "베이비밀 일괄배송 여부")
	private boolean isGoodsDailyBulk;
}
