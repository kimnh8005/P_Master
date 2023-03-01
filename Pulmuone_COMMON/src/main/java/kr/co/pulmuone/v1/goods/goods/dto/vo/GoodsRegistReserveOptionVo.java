package kr.co.pulmuone.v1.goods.goods.dto.vo;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingUserName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "예약판매옵션 Vo")
public class GoodsRegistReserveOptionVo {

	@ApiModelProperty(value=" 예약판매 옵션 ID")
	String ilGoodsReserveOptionId;

	@ApiModelProperty(value="회차")
	String saleSequance;

	@ApiModelProperty(value="예약주문 시작날짜")
	String reservationStartDate;

	@ApiModelProperty(value="예약주문 시작시간")
	String reservationStartHour;

	@ApiModelProperty(value="예약주문 시작분")
	String reservationStartMinute;

	@ApiModelProperty(value="예약주문 마감날짜")
	String reservationEndDate;

	@ApiModelProperty(value="예약주문 마감시간")
	String reservationEndHour;

	@ApiModelProperty(value="예약주문 마감분")
	String reservationEndMinute;

	@ApiModelProperty(value="주문재고수")
	String stockQuantity;

	@ApiModelProperty(value="주문수집 IF 일")
	String orderIfDate;

	@ApiModelProperty(value="출고예정일")
	String releaseDate;

	@ApiModelProperty(value="도착예정일")
	String arriveDate;

	@ApiModelProperty(value="배송패턴 ID")
	String psShippingPatternId;

	@ApiModelProperty(value="주중CODE")
	String weekCode;

	@ApiModelProperty(value="주중CODE NUMBER")
	int weekCodeWeekday;

	@ApiModelProperty(value="예약주문가능기간 종료일 주중CODE NUMBER")
	int reserveEndDateWeekday;

	@ApiModelProperty(value="예약주문가능기간 종료일")
	String reserveEndDate;

	@ApiModelProperty(value="출고예정일계산날짜")
	int forwardingScheduledDay;

	@ApiModelProperty(value="도착예정일계산날짜")
	int arrivalScheduledDay;

	@ApiModelProperty(value="등록자 ID")
	String createId;

	@ApiModelProperty(value="등록일")
	String createDate;

	@ApiModelProperty(value="수정자 ID")
	String modifyId;

	@ApiModelProperty(value="수정일")
	String modifyDate;

	@ApiModelProperty(value="예약판매옵션설정삭제가능여부")
	boolean optnDelAllow;

	@ApiModelProperty(value="주문불가 주중CODE")
	List<String> ifOrderDatesToDisableList;

	@ApiModelProperty(value="주문마감시간")
	String cutoffTime;

	@ApiModelProperty(value="주문수량")
	int orderCnt;
}
