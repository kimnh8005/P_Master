package kr.co.pulmuone.v1.store.warehouse.service.dto.vo;

import java.time.LocalTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "출고처 정보")
public class UrWarehouseVo {
	// 몰에 필요한 것만 작업 처리 했습니다. 관리자에서 필요하시면 추가 하시면 됩니다.

	@ApiModelProperty(value = " 출고처 PK")
	private Long urWarehouseId;

	@ApiModelProperty(value = "일별 출고 한도")
	private int limitCnt;

	@ApiModelProperty(value = "새벽새송 일별 출고 한도")
	private int dawnLimitCnt;

	@ApiModelProperty(value = "주문마감시간")
	private LocalTime cutoffTime;

	@ApiModelProperty(value = "일반배송 배송패턴 PK")
	private Long psShippingPatternId;

	@ApiModelProperty(value = "새벽배송YN")
	private String dawnDeliveryYn;

	@ApiModelProperty(value = "새벽배송 주문마감시간")
	private LocalTime dawnDeliveryCutoffTime;

	@ApiModelProperty(value = "새벽배송 배송패턴 PK")
	private Long dawnDeliveryPsShippingPatternId;

	@ApiModelProperty(value = "매장택배배송YN")
	private String storeYn;

	@ApiModelProperty(value = "매장택배배송 일별 출고 한도")
	private int storeLimitCnt;

	@ApiModelProperty(value = "매장택배배송 주문마감시간")
	private LocalTime storeCutoffTime;

	@ApiModelProperty(value = "매장택배배송 배송패턴 PK")
	private Long storeShippingPatternId;

	@ApiModelProperty(value = "물류비 정산 여부YN")
	private String stlmnYn;

	@ApiModelProperty(value = "배송불가지역 (공통코드 - UNDELIVERABLE_TP) : 배송권역정책 신규로직 교체 시 미사용 예정")
	private String undeliverableAreaTp;

	@ApiModelProperty(value = "새벽배송불가지역 (공통코드 - UNDELIVERABLE_TP) : 배송권역정책 신규로직 교체 시 미사용 예정")
	private String dawnUndeliverableAreaTp;

	@ApiModelProperty(value = "출고처그룹 코드")
	private String warehouseGroupCode;

	@ApiModelProperty(value = "다중 배송불가지역 유형 구분자 , (공통코드 - UNDELIVERABLE_TP) : 배송권역정책 신규로직")
	private String undeliverableAreaTpGrp;

	@ApiModelProperty(value = "다중 새벽배송불가지역 유형 구분자 , (공통코드 - UNDELIVERABLE_TP) : 배송권역정책 신규로직")
	private String dawnUndeliverableAreaTpGrp;
}
