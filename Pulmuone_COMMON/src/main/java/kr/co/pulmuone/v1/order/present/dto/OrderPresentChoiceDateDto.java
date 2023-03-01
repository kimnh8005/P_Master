package kr.co.pulmuone.v1.order.present.dto;

import java.time.LocalDate;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 선물하기 DTO
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 07. 15.            홍진영         최초작성
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "주문 선물하기 DTO")
public class OrderPresentChoiceDateDto {

	/**
	 * 배송정책 PK
	 */
	@ApiModelProperty(value = "배송정책 PK")
	private String odShippingPriceId;

	/**
	 * 새벽배송YN
	 */
	@ApiModelProperty(value = "새벽배송YN")
	private String dawnDeliveryYn;

	/**
	 * 도착 예정일
	 */
	@ApiModelProperty(value = "도착 예정일")
	private LocalDate arrivalScheduledDate;

	/**
	 * 선택 도착 예정일 리스트
	 */
	@ApiModelProperty(value = "선택 도착 예정일 리스트")
	private List<LocalDate> choiceArrivalScheduledDateList;

	/**
	 * 새벽배송 도착 예정일 - 브릿지 페이지에서 새벽배송 가능할때만
	 */
	@ApiModelProperty(value = "새벽배송 도착 예정일 - 브릿지 페이지에서 새벽배송 가능할때만")
	private LocalDate dawnArrivalScheduledDate;

	/**
	 * 새벽배송 선택 도착 예정일 리스트 - 브릿지 페이지에서 새벽배송 가능할때만
	 */
	@ApiModelProperty(value = "새벽배송 선택 도착 예정일 리스트 - 브릿지 페이지에서 새벽배송 가능할때만")
	private List<LocalDate> dawnChoiceArrivalScheduledDateList;
}