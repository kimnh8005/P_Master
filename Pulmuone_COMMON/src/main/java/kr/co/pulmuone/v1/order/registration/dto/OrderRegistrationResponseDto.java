package kr.co.pulmuone.v1.order.registration.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.enums.OrderEnums;
import lombok.Builder;
import lombok.Getter;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 생성 결과 관련 Response Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 15.            이명수         최초작성
 * =======================================================================
 * </PRE>
 */
@Builder
@Getter
@ApiModel(description = "주문 생성 결과 관련 Response Dto")
public class OrderRegistrationResponseDto {
    @ApiModelProperty(value = "주문 생성 결과 상태")
    private OrderEnums.OrderRegistrationResult orderRegistrationResult;

    @ApiModelProperty(value = "주문 PK")
    private String odOrderIds;

    @ApiModelProperty(value = "주문번호")
    private String odids;

	@ApiModelProperty(value = "결제마스터PK")
	private Long odPaymentMasterId;

    @ApiModelProperty(value = "고객상담 메모")
    private String memo;

    @ApiModelProperty(value = "주문 성공 건수")
    private int succCnt;

    @ApiModelProperty(value = "주문 실패 건수")
    private int failCnt;

    @ApiModelProperty(value = "결제금액")
    private int paymentPrice;

    @ApiModelProperty(value = "메모 리스트")
    List<List<OrderOutmallMemoDto>> memoList;
}
