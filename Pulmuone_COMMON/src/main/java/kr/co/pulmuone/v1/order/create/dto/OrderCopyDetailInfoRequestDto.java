package kr.co.pulmuone.v1.order.create.dto;

import java.time.LocalDate;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 주문복사 상세내역 Request Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일                		:	작성자      :	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 02. 24.		이규한	최초작성
 * =======================================================================
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "주문복사 상세내역 Request Dto")
public class OrderCopyDetailInfoRequestDto extends BaseRequestDto {

	@ApiModelProperty(value = "조회구분 (ORDER_ID : 주문번호, COLLECTION_MALL_ID : 수집몰 주문번호, OUTMALL_ID : 외부몰 주문번호)")
	private String searchOrderType;

	@ApiModelProperty(value = "조회번호")
	private String conditionValue;

	@ApiModelProperty(value = "접근권한 출고처 ID 리스트")
    private List<String> listAuthWarehouseId;

	@ApiModelProperty(value = "접근권한 공급처 ID 리스트")
    private List<String> listAuthSupplierId;

	@ApiModelProperty(value = "상품 주문복사 수량 변경 리스트")
	private List<OrderCopyDto> orderCntChangList;

	@ApiModelProperty(value = "공급처")
	private String sellersGroupCd;

	@ApiModelProperty(value = "주문 PK")
	private String odOrderId;

	@ApiModelProperty(value = "주문번호")
	private String odid;

	@ApiModelProperty(value = "주문 I/F 일자")
	private LocalDate orderIfDt;

	@ApiModelProperty(value = "출고예정일일자")
	private LocalDate shippingDt;

	@ApiModelProperty(value = "도착예정일일자")
	private LocalDate deliveryDt;
}
