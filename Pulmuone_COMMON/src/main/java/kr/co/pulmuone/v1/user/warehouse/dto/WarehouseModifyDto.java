package kr.co.pulmuone.v1.user.warehouse.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Arrays;


@Getter
@Setter
@ToString
@ApiModel(description = "WarehouseModifyDto")
public class WarehouseModifyDto extends BaseRequestDto {

	@ApiModelProperty(value = "출고처 그롭 코드")
	private String warehouseGroupCode;

	@ApiModelProperty(value = "출고처 명")
	private String inputWarehouseName;

	@ApiModelProperty(value = "재고발주여부")
	private String stockOrderYn;

	@ApiModelProperty(value = "물류비 정산 여부")
	private String stlmnYn;

	@ApiModelProperty(value = "일별출고한도")
	private String limitCount;

	@ApiModelProperty(value = "매장(가맹점)")
	private String storeYn;

	@ApiModelProperty(value = "휴일그룹설정여부")
	private String holidayGroupYn;

	@ApiModelProperty(value = "휴일그룹 ID")
	private String holidayGroup;

	@ApiModelProperty(value = "주문마감 시간")
	private String hour;

	@ApiModelProperty(value = "주문마감 분")
	private String minute;

	@ApiModelProperty(value = "새벽배송 여부")
	private String dawnDlvryYn;

	@ApiModelProperty(value = "새벽배송 휴일그룹 설정 여부")
	private String dawnDlvryHldyGroupYn;

	@ApiModelProperty(value = "새벽배송 휴일그룹 ID")
	private String dawnDlvryHolidayGroup;

	@ApiModelProperty(value = "새벽배송 주문마감시간 /시간")
	private String dawnHour;

	@ApiModelProperty(value = "새벽배송 주문마감시간 /분")
	private String dawnMinute;

	@ApiModelProperty(value = "우편번호")
	private String receiverZipCode;

	@ApiModelProperty(value = "상세주소1")
	private String receiverAddress1;

	@ApiModelProperty(value = "상세주소2")
	private String receiverAddress2;

	@ApiModelProperty(value = "등록정보")
	private String createInfo;

	@ApiModelProperty(value = "수정정보")
	private String modifyInfo;

	@ApiModelProperty(value = "배송타입")
	private String deliveryType;


	@ApiModelProperty(value = "공급처")
	private String supplierCompany;

	public ArrayList<String> getSupplierCompanyList() {
		if(!StringUtil.isEmpty(this.supplierCompany)) {
			return new ArrayList<String>(Arrays.asList(supplierCompany.split(Constants.ARRAY_SEPARATORS)));
		}
		return new ArrayList<String>();

	}

	@ApiModelProperty(value = "삭제 공급처")
	private String deleteSupplierCompany;

	public ArrayList<String> getDeleteSupplierCompanyList() {
		if(!StringUtil.isEmpty(this.deleteSupplierCompany)) {
			return new ArrayList<String>(Arrays.asList(deleteSupplierCompany.split(Constants.ARRAY_SEPARATORS)));
		}
		return new ArrayList<String>();

	}


	@ApiModelProperty(value = "출고처 출고요일")
	private String holidayWeek;

	public ArrayList<String> getHolidayWeekList() {
		if(!StringUtil.isEmpty(this.holidayWeek)) {
			return new ArrayList<String>(Arrays.asList(holidayWeek.split(Constants.ARRAY_SEPARATORS)));
		}
		return new ArrayList<String>();

	}

	@ApiModelProperty(value = "새벽배송 출고요일")
	private String dawnDlvryWeek;

	public ArrayList<String> getDawnDlvryWeekList() {
		if(!StringUtil.isEmpty(this.dawnDlvryWeek)) {
			return new ArrayList<String>(Arrays.asList(dawnDlvryWeek.split(Constants.ARRAY_SEPARATORS)));
		}
		return new ArrayList<String>();

	}

	@ApiModelProperty(value = "출고처 휴일")
	private String holiday;

	public ArrayList<String> getHolidayList() {
		if(!StringUtil.isEmpty(this.holiday)) {
			return new ArrayList<String>(Arrays.asList(holiday.split(Constants.ARRAY_SEPARATORS)));
		}
		return new ArrayList<String>();

	}

	@ApiModelProperty(value = "새벽배송 휴일")
	private String dawnHoliday;

	public ArrayList<String> getDawnHolidayList() {
		if(!StringUtil.isEmpty(this.dawnHoliday)) {
			return new ArrayList<String>(Arrays.asList(dawnHoliday.split(Constants.ARRAY_SEPARATORS)));
		}
		return new ArrayList<String>();

	}

	@ApiModelProperty(value = "출고처 ID")
	private String urWarehouseId;

	@ApiModelProperty(value = "배송패턴 ID")
	private String deliveryPatternId;

	@ApiModelProperty(value = "새벽배송패턴 ID")
	private String dawnDeliveryPatternId;

	@ApiModelProperty(value = "주문변경여부")
	private String orderChangeType;

	@ApiModelProperty(value = "주문상태 알림 수신여부")
	private String orderStatusAlamYn;

	@ApiModelProperty(value = "주문상태 알림 수신 메일")
	private String warehouseMail;

	@ApiModelProperty(value = "출고처 연락처1")
	private String warehouseTelephone1;

	@ApiModelProperty(value = "출고처 연락처2")
	private String warehouseTelephone2;

	@ApiModelProperty(value = "출고처 연락처3")
	private String warehouseTelephone3;

	@ApiModelProperty(value = "새벽배송 일별출고한도")
	private String dawnLimitCnt;

	@ApiModelProperty(value = "매장(가맹점)택배출고한도")
	private String storeLimitCnt;

	@ApiModelProperty(value = "매장(가맹점)배송패턴 ID")
	private String storeShippingPatternId;

	@ApiModelProperty(value = "매장(가맹점) 주문마감시간 /시간")
	private String storeHour;

	@ApiModelProperty(value = "매장(가맹점) 주문마감시간 /분")
	private String storeMinute;

	@ApiModelProperty(value = "업체명")
	private String companyNm;

	@ApiModelProperty(value = "업체명 상세")
	private String inputCompanyName;

	@ApiModelProperty(value = "배송불가지역")
	private String undeliverableAreaTp;

	@ApiModelProperty(value = "배송불가지역그룹")
	private String undeliverableAreaTpGrp;

	@ApiModelProperty(value = "새벽 배송불가지역")
	private String dawnUndeliverableAreaTp;

	@ApiModelProperty(value = "새벽 배송불가지역그룹")
	private String dawnUndeliverableAreaTpGrp;

	@ApiModelProperty(value = "출고처 메모")
	private String warehouseMemo;

}
