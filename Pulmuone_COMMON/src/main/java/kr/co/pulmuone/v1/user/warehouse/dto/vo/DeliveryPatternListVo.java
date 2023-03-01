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
public class DeliveryPatternListVo {

	@ApiModelProperty(value = "No")
	private int no;

	@ApiModelProperty(value = "출고/도착 예정일 구분")
	private String forwardArrived;

	@ApiModelProperty(value = "배송패턴 목록 title")
	private String title;

	@ApiModelProperty(value = "배송패턴 ID")
	private String psShippingPatternId;

	@ApiModelProperty(value = "출발예정 월요일")
	private String forwardMonDay;

	@ApiModelProperty(value = "도착예정 월요일")
	private String arrivedMonDay;

	@ApiModelProperty(value = "출발예정 화요일")
	private String forwardTueDay;

	@ApiModelProperty(value = "도착예정 화요일")
	private String arrivedTueDay;

	@ApiModelProperty(value = "출발예정 수요일")
	private String forwardWedDay;

	@ApiModelProperty(value = "도착예정 수요일")
	private String arrivedWedDay;

	@ApiModelProperty(value = "출발예정 목요일")
	private String forwardThuDay;

	@ApiModelProperty(value = "도착예정 목요일")
	private String arrivedThuDay;

	@ApiModelProperty(value = "출발예정 금요일")
	private String forwardFriDay;

	@ApiModelProperty(value = "도착예정 금요일")
	private String arrivedFriDay;

	@ApiModelProperty(value = "출발예정 토요일")
	private String forwardSatDay;

	@ApiModelProperty(value = "도착예정 토요일")
	private String arrivedSatDay;

	@ApiModelProperty(value = "출발예정 일요일")
	private String forwardSunDay;

	@ApiModelProperty(value = "도착예정 일요일")
	private String arrivedSunDay;



}
