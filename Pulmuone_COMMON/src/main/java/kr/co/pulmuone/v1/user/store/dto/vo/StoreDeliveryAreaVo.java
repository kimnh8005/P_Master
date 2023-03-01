package kr.co.pulmuone.v1.user.store.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "매장/가맹점 배송권역 VO")
public class StoreDeliveryAreaVo {

	@ApiModelProperty(value = "매장/가맹점 코드")
	private String urStoreId;

	@ApiModelProperty(value = "매장/가맹점 명")
	private String storeName;

	@ApiModelProperty(value = "매장/가맹점 카테고리1")
	private String storeCategory1Name;

	@ApiModelProperty(value = "우편번호")
	private String zipCode;

	@ApiModelProperty(value = "빌딩번호")
	private String buildingNumber;

	@ApiModelProperty(value = "권역명")
	private String deliveryAreaName;

	@ApiModelProperty(value = "배송간격")
	private String deliveryIntervalTypeName;

	@ApiModelProperty(value = "배송타입")
	private String deliveryTypeName;

	@ApiModelProperty(value = "배송가능품목유형")
	private String deliverableItemTypeName;

	@ApiModelProperty(value = "최근수정일")
	private String modifyDate;

	@ApiModelProperty(value = "No")
	private int no;

	@ApiModelProperty(value = "회차")
	private String scheduleNo;

	@ApiModelProperty(value = "주문마감시간")
	private String cutoffTime;

	@ApiModelProperty(value = "출고한도")
	private String limitCnt;

	@ApiModelProperty(value = "권역명")
	private String deliveryAreaNm;

	@ApiModelProperty(value = "권역 ID")
	private String urDeliveryAreaId;

	@ApiModelProperty(value = "주문시간")
	private String storeOrderTime;
	
	@ApiModelProperty(value = "배송타입코드")
	private String storeDeliveryTp;
	
	@ApiModelProperty(value = "배송타입명")
	private String storeDeliveryTpNm;
	

}
