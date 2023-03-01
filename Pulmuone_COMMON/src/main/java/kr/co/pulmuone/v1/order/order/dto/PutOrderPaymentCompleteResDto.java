package kr.co.pulmuone.v1.order.order.dto;

import io.swagger.annotations.ApiModel;
import kr.co.pulmuone.v1.comm.enums.PgEnums;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 결제 업데이트 응답 DTO
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021.02.08.             홍진영         최초작성
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "주문 결제 업데이트 응답 DTO")
public class PutOrderPaymentCompleteResDto {
	// 결과
    private PgEnums.PgErrorType result;

    // 주문데이터
    PgApprovalOrderDataDto orderData;

    // 결제데이터
    private PgApprovalOrderPaymentDataDto paymentData;
}
