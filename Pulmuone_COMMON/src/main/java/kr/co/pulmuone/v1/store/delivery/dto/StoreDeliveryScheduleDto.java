package kr.co.pulmuone.v1.store.delivery.dto;

import java.time.LocalTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "스토어(매장/가맹점) 배송스케줄")
public class StoreDeliveryScheduleDto {

	@ApiModelProperty(value = "회차")
	private String urStoreId;

	@ApiModelProperty(value = "회차")
	private Long scheduleNo;

	@ApiModelProperty(value = "매장배송 회차 선택 가능 여부(true:선택가능, false:마감)")
	private boolean isPossibleSelect;

	@ApiModelProperty(value = "주문 배송 시작시간")
	private String startTime;

	@ApiModelProperty(value = "주문 배송 종료시간")
	private String endTime;

	public LocalTime getStartTimeLocalTime() {
		if(startTime == null) {
			return null;
		}
		String[] time = startTime.split(":");
		return LocalTime.of(Integer.valueOf(time[0]), Integer.valueOf(time[1]), time.length >= 3 ? Integer.valueOf(time[2]) : 0);
	}

	public LocalTime getEndTimeLocalTime() {
		if(endTime == null) {
			return null;
		}
		String[] time = endTime.split(":");
		return LocalTime.of(Integer.valueOf(time[0]), Integer.valueOf(time[1]), time.length >= 3 ? Integer.valueOf(time[2]) : 0);
	}
}
