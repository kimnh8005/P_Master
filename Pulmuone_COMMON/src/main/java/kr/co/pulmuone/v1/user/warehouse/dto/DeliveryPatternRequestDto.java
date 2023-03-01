package kr.co.pulmuone.v1.user.warehouse.dto;

import java.util.ArrayList;
import java.util.Arrays;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.comm.constants.Constants;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "DeliveryPatternRequestDto")
public class DeliveryPatternRequestDto extends BaseRequestPageDto{

	@ApiModelProperty(value = "배송패턴 ID")
	private String psShippingPatternId;

	@ApiModelProperty(value = "배송패턴명 조회")
	private String searchTitle;

	@ApiModelProperty(value = "월요일 Check 구분")
	private String checkMon;

	@ApiModelProperty(value = "화요일 Check 구분")
	private String checkTue;

	@ApiModelProperty(value = "수요일 Check 구분")
	private String checkWed;

	@ApiModelProperty(value = "목요일 Check 구분")
	private String checkThu;

	@ApiModelProperty(value = "금요일 Check 구분")
	private String checkFri;

	@ApiModelProperty(value = "토요일 Check 구분")
	private String checkSat;

	@ApiModelProperty(value = "일요일 Check 구분")
	private String checkSun;

	@ApiModelProperty(value = "배송패턴 명")
	private String deliveryPatternName;

	@ApiModelProperty(value = "월요일 출고예정일")
	private String warehouseMon;

	@ApiModelProperty(value = "화요일 출고예정일")
	private String warehouseTue;

	@ApiModelProperty(value = "수요일 출고예정일")
	private String warehouseWed;

	@ApiModelProperty(value = "목요일 출고예정일")
	private String warehouseThu;

	@ApiModelProperty(value = "금요일 출고예정일")
	private String warehouseFri;

	@ApiModelProperty(value = "토요일 출고예정일")
	private String warehouseSat;

	@ApiModelProperty(value = "일요일 출고예정일")
	private String warehouseSun;

	@ApiModelProperty(value = "월요일 도착예정일")
	private String arrivedMon;

	@ApiModelProperty(value = "화요일 도착예정일")
	private String arrivedTue;

	@ApiModelProperty(value = "수요일 도착예정일")
	private String arrivedWed;

	@ApiModelProperty(value = "목요일 도착예정일")
	private String arrivedThu;

	@ApiModelProperty(value = "금요일 도착예정일")
	private String arrivedFri;

	@ApiModelProperty(value = "토요일 도착예정일")
	private String arrivedSat;

	@ApiModelProperty(value = "일요일 도착예정일")
	private String arrivedSun;

	@ApiModelProperty(value = "출고처예정일")
	private String forwardingScheduledDay;

	@ApiModelProperty(value = "도착예정일")
	private String arrivalScheduledDay;

	@ApiModelProperty(value = "요일코드")
	private String weekCode;

	@ApiModelProperty(value = "조회 요일코드")
	private String selectWeek;

	public ArrayList<String> getSelectWeekList() {

		if(!StringUtil.isEmpty(this.selectWeek)) {
			return new ArrayList<String>(Arrays.asList(selectWeek.split(Constants.ARRAY_SEPARATORS)));
		}
		return new ArrayList<String>();

	}

	@ApiModelProperty(value = "출고지시일 조회조건")
	private String selectDeliveryPattern;
}
