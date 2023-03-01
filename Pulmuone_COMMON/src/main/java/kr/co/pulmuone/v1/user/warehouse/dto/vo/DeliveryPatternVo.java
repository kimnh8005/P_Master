package kr.co.pulmuone.v1.user.warehouse.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "DeliveryPatternListVo")
public class DeliveryPatternVo {

	@ApiModelProperty(value = "배송패턴 ID")
	private String psShippingPatternId;

	@ApiModelProperty(value = "배송패턴 명")
	private String deliveryPatternName;

	@ApiModelProperty(value = "출발예정 월요일")
	private String warehouseMon;

	@ApiModelProperty(value = "도착예정 월요일")
	private String arrivedMon;

	@ApiModelProperty(value = "출발예정 화요일")
	private String warehouseTue;

	@ApiModelProperty(value = "도착예정 화요일")
	private String arrivedTue;

	@ApiModelProperty(value = "출발예정 수요일")
	private String warehouseWed;

	@ApiModelProperty(value = "도착예정 수요일")
	private String arrivedWed;

	@ApiModelProperty(value = "출발예정 목요일")
	private String warehouseThu;

	@ApiModelProperty(value = "도착예정 목요일")
	private String arrivedThu;

	@ApiModelProperty(value = "출발예정 금요일")
	private String warehouseFri;

	@ApiModelProperty(value = "도착예정 금요일")
	private String arrivedFri;

	@ApiModelProperty(value = "출발예정 토요일")
	private String warehouseSat;

	@ApiModelProperty(value = "도착예정 토요일")
	private String arrivedSat;

	@ApiModelProperty(value = "출발예정 일요일")
	private String warehouseSun;

	@ApiModelProperty(value = "도착예정 일요일")
	private String arrivedSun;

	@ApiModelProperty(value = "등록정보")
	private String createInfo;

	@ApiModelProperty(value = "수정정보")
	private String modifyInfo;



}
