package kr.co.pulmuone.v1.order.create.dto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import kr.co.pulmuone.v1.comm.enums.OrderEnums;
import kr.co.pulmuone.v1.pg.dto.VirtualAccountDataResponseDto;
import kr.co.pulmuone.v1.pg.service.inicis.dto.InicisNonAuthenticationCartPayRequestDto;
import kr.co.pulmuone.v1.pg.service.inicis.dto.InicisNonAuthenticationCartPayResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 클레임 신용카드 추가결제 Response Dto
 * </PRE>
 */

@Builder
@Getter
@Setter
@ToString
@ApiModel(description = "OrderClaimCardPayResponseDto")
public class OrderClaimCardPayResponseDto {

	private OrderEnums.OrderRegistrationResult orderRegistrationResult;

	private String message;

	private InicisNonAuthenticationCartPayResponseDto inicisNonAuthenticationCartPay;

	private VirtualAccountDataResponseDto virtualAccountData;

	private long odAddPaymentReqInfoId;
}
