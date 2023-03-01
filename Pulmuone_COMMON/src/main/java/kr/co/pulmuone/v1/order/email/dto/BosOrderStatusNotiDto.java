package kr.co.pulmuone.v1.order.email.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 정기배송 결과 결제 대상 목록 조회 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 05. 10.	        천혜현		최초작성
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "BOS 주문 상태 알림 발송 대상 거래처 조회 Dto")
public class BosOrderStatusNotiDto {

	@ApiModelProperty(value = "거래처명")
	private String compNm;

    @ApiModelProperty(value = "거래처 대표 이메일")
    private String compMail;

    @ApiModelProperty(value = "거래처 PK")
    private Long urClientId;

}
