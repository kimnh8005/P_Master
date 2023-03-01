package kr.co.pulmuone.v1.order.order.dto.mall;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * Mall 정기배송 정보
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 03. 03.	이명수		최초작성
 * =======================================================================
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "Mall 주문 Dto")
public class MallOrderDto {
	@ApiModelProperty(value = "주문 PK")
    private long odOrderId;

	@ApiModelProperty(value = "주문번호")
    private String odid;

	@ApiModelProperty(value = "주문 생성일")
    private String createDt;

	@ApiModelProperty(value = "회원 PK")
    private Long urUserId;

	@ApiModelProperty(value = "회원 로그인 ID")
    private String loginId;

	@ApiModelProperty(value = "주문자명")
    private String buyerNm;

	@ApiModelProperty(value = "주문자 핸드폰 번호")
    private String buyerHp;

	@ApiModelProperty(value = "주문자 이메일")
    private String buyerMail;

	@ApiModelProperty(value = "주문 상품명")
    private String goodsNm;

	@ApiModelProperty(value = "일반주문: N, 선물하기주문: Y")
    private String presentYn;
}