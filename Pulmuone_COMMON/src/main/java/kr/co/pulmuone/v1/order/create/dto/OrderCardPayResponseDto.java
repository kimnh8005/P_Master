package kr.co.pulmuone.v1.order.create.dto;


import io.swagger.annotations.ApiModel;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import kr.co.pulmuone.v1.comm.enums.OrderEnums;
import kr.co.pulmuone.v1.pg.service.inicis.dto.InicisNonAuthenticationCartPayResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 생성 관련 Request Dto
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
@Setter
@ToString
@ApiModel(description = "신용카드 비인증 결과 Dto")
public class OrderCardPayResponseDto extends BaseRequestPageDto{

	private OrderEnums.OrderRegistrationResult result;

	private InicisNonAuthenticationCartPayResponseDto inicisDto;

	private String odid;

	private String message;
}
