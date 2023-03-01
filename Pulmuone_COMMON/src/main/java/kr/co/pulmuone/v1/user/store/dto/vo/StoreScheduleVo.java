package kr.co.pulmuone.v1.user.store.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "매장 스케줄 VO")
public class StoreScheduleVo {

	@ApiModelProperty(value = "회차")
	private String scheduleNo;

	@ApiModelProperty(value = "주문 마감시간")
	private String cutoffTime;

	@ApiModelProperty(value = "주문 배송 시작시간")
	private String startTime;

	@ApiModelProperty(value = "주문 배송 종료시간")
	private String endTime;

	@ApiModelProperty(value = "출고한도")
	private int limitCnt;


}
