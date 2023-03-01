package kr.co.pulmuone.v1.user.warehouse.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;


@Getter
@Setter
@ToString
@ApiModel(description = "WarehouseResultVo")
public class WarehouseResultVo {


	@ApiModelProperty(value = "출고처 ID")
	private String urWarehouseId;

	@ApiModelProperty(value = "출고처 그룹 코드")
	private String warehouseGroupCd;

	@ApiModelProperty(value = "출고처 그룹명")
	private String warehouseGroupName;

	@ApiModelProperty(value = "출고처 명")
	private String warehouseName;

	@ApiModelProperty(value = "매장(가맹점)")
	private String storeYn;

	@ApiModelProperty(value = "주문마감시간")
	private String cutoffTime;

	@ApiModelProperty(value = "거래처명")
	private String clientName;

	@ApiModelProperty(value = "등록일시")
	private String createDate;

	@ApiModelProperty(value = "No")
	private int no;

	@ApiModelProperty(value = "공급처")
	private String supplierCompany;

	@ApiModelProperty(value = "휴일그룹ID")
	private String psHolidayGroupId;

	@ApiModelProperty(value = "휴일그룹명")
	private String psHolidayGroupName;

	@ApiModelProperty(value = "등록정보")
	private String createInfo;

	@ApiModelProperty(value = "수정정보")
	private String modifyInfo;

	@ApiModelProperty(value = "출고처 명")
	private String inputWarehouseName;

	@ApiModelProperty(value = "출고처 그룹 코드")
	private String warehouseGroupCode;

	@ApiModelProperty(value = "재발주여부")
	private String stockOrderYn;

	@ApiModelProperty(value = "물류비 정산 여부")
	private String stlmnYn;

	@ApiModelProperty(value = "일별출고한도")
	private String limitCount;

	@ApiModelProperty(value = "휴일그룹 설정여부")
	private String holidayGroupYn;

	@ApiModelProperty(value = "휴일그룹 ID")
	private String holidayGroup;

	@ApiModelProperty(value = "주문마감 시간 /시간")
	private String hour;

	@ApiModelProperty(value = "주문마감 시간 /분")
	private String minute;

	@ApiModelProperty(value = "새별배송 여부")
	private String dawnDlvryYn;

	@ApiModelProperty(value = "새벽배송 휴일그룹 설정 여부")
	private String dawnDlvryHldyGroupYn;

	@ApiModelProperty(value = "새벽배송 휴일그룹 ID")
	private String dawnDlvryHolidayGroup;

	@ApiModelProperty(value = "새벽배송 주문마감 시간 /시간")
	private String dawnHour;

	@ApiModelProperty(value = "새벽배송 주문마감 시간 /분")
	private String dawnMinute;

	@ApiModelProperty(value = "우편번호")
	private String receiverZipCode;

	@ApiModelProperty(value = "주소1")
	private String receiverAddress1;

	@ApiModelProperty(value = "주소2")
	private String receiverAddress2;

	@ApiModelProperty(value = "출고일정보 월요일")
	private String holyMon;

	@ApiModelProperty(value = "출고일정보 화요일")
	private String holyTue;

	@ApiModelProperty(value = "출고일정보 수요일")
	private String holyWen;

	@ApiModelProperty(value = "출고일정보 목요일")
	private String holyThu;

	@ApiModelProperty(value = "출고일정보 금요일")
	private String holyFri;

	@ApiModelProperty(value = "출고일정보 토요일")
	private String holySat;

	@ApiModelProperty(value = "출고일정보 일요일")
	private String holySun;

	@ApiModelProperty(value = "새벽배송 출고일정보 월요일")
	private String dawnMon;

	@ApiModelProperty(value = "새벽배송 출고일정보 화요일")
	private String dawnTue;

	@ApiModelProperty(value = "새벽배송 출고일정보 수요일")
	private String dawnWen;

	@ApiModelProperty(value = "새벽배송 출고일정보 목요일")
	private String dawnThu;

	@ApiModelProperty(value = "새벽배송 출고일정보 금요일")
	private String dawnFri;

	@ApiModelProperty(value = "새벽배송 출고일정보 토요일")
	private String dawnSat;

	@ApiModelProperty(value = "새벽배송 출고일정보 일요일")
	private String dawnSun;

	@ApiModelProperty(value = "공급처 출고처 Id")
	private String urSupplierWarehouseId;

	@ApiModelProperty(value = "공급처 명")
	private String supplierName;

	@ApiModelProperty(value = "휴일그룹")
	private String holiday;

	@ApiModelProperty(value = "리스트")
	private	List<WarehouseResultVo> supplierList;

	@ApiModelProperty(value = "리스트")
	private	List<WarehouseResultVo> holidayList;

	@ApiModelProperty(value = "리스트")
	private	List<WarehouseResultVo> dawnHolidayList;

	@ApiModelProperty(value = "공급업체 ID")
	private String urSupplierId;

	@ApiModelProperty(value = "공급업체 등록여부")
	private String supplierYn;

	@ApiModelProperty(value = "배송패턴 ID")
	private String psShippingPatternId;

	@ApiModelProperty(value = "새벽배송패턴 ID")
	private String dawnDeliveryPatternId;

	@ApiModelProperty(value = "배송패턴 ID")
	private String deliveryPatternId;

	@ApiModelProperty(value = "배송패턴 명")
	private String deliveryPatternName;

	@ApiModelProperty(value = "새벽 배송패턴 명")
	private String dawnDeliveryPatternName;

	@ApiModelProperty(value = "배송정책 count")
	private int shippingTemplateCnt;

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

	@ApiModelProperty(value = "연휴 시작일")
	private String fromDate;

	@ApiModelProperty(value = "연휴 종료일")
	private String toDate;

	@ApiModelProperty(value = "새벽배송 일별출고한도")
	private String dawnLimitCnt;

	@ApiModelProperty(value = "매장(가맹점) 배송패턴 ID")
	private String storeShippingPatternId;

	@ApiModelProperty(value = "매장(가맹점) 배송패턴 명")
	private String storePatternName;

	@ApiModelProperty(value = "매장(가맹점) 주문마감 시간 /시간")
	private String storeHour;

	@ApiModelProperty(value = "매장(가맹점) 주문마감 시간 /분")
	private String storeMinute;

	@ApiModelProperty(value = "매장(가맹점) 출고한도")
	private String storeLimitCnt;

	@ApiModelProperty(value = "업체명 상세")
	private String inputCompanyName;

	@ApiModelProperty(value = "업체명")
	private String companyName;

	@ApiModelProperty(value = "배송불가지역")
	private String undeliverableAreaTp;

	@ApiModelProperty(value = "배송불가지역그룹")
	private String undeliverableAreaTpGrp;

	@ApiModelProperty(value = "배송불가지역그룹 체크 값")
	private List<String> undeliverableAreaTpGrpList;

	@ApiModelProperty(value = "새벽 배송불가지역")
	private String dawnUndeliverableAreaTp;

	@ApiModelProperty(value = "새벽 배송불가지역그룹")
	private String dawnUndeliverableAreaTpGrp;

	@ApiModelProperty(value = "새벽 배송불가지역그룹 체크 값")
	private List<String> dawnUndeliverableAreaTpGrpList;

	@ApiModelProperty(value = "출고처 메모")
	private String warehouseMemo;

}
