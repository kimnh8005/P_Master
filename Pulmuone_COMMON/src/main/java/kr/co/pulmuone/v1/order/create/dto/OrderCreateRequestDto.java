package kr.co.pulmuone.v1.order.create.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

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
@Getter
@Setter
@ToString
@ApiModel(description = "주문 생성 Dto")
public class OrderCreateRequestDto extends BaseRequestDto {

	@ApiModelProperty(value = "입력")
	private String insert;

    @ApiModelProperty(value = "쿼리리스트")
    private String orderType;

    @ApiModelProperty(value = "주문자명")
    private String buyerNm;

    @ApiModelProperty(value = "주문자휴대폰")
    private String buyerHp;

    @ApiModelProperty(value = "주문자ID")
    private long urUserId;

    @ApiModelProperty(value = "주문자이메일")
    private String buyerMail;

    @ApiModelProperty(value = "결제 종류")
    private String psPayCd;

	@ApiModelProperty(value = "입력 리스트")
    List<OrderCreateDto> orderCreateList;

	@ApiModelProperty(value = "주문번호 리스트")
	List<String> findOdIdList;

    @ApiModelProperty(value = "환불 계좌 은행코드")
    private String bankCode;

    @ApiModelProperty(value = "환불 계좌 계좌번호")
    private String accountNumber;

    @ApiModelProperty(value = "환불 계좌 예금주")
    private String holderName;

    @ApiModelProperty(value = "배송비무료 여부")
    private String freeShippingPriceYn = "N";
}
