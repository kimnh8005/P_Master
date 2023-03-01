package kr.co.pulmuone.v1.order.claim.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.pg.dto.BasicDataResponseDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * Mall 주문 클레임 추가결제 정보 Response Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 05. 14.	김명진		최초작성
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "Mall 주문 클레임 추가결제 정보 Response Dto")
public class MallOrderClaimAddPaymentResult {

	@ApiModelProperty(value = "주문PK")
	private long odOrderId;

	@ApiModelProperty(value = "주문클레임PK")
	private long odClaimId;

	@ApiModelProperty(value = "주문번호")
	private String odid;

	@ApiModelProperty(value = "직접결제여부")
	private String directPaymentYn;

	@ApiModelProperty(value = "추가배송비")
	private int addPaymentShippingPrice;

	@ApiModelProperty(value = "클레임상태구분")
	private String claimStatusTp;

	@ApiModelProperty(value = "클레임상태코드")
	private String claimStatusCd;

	@ApiModelProperty(value = "회수여부")
	private String returnsYn;

	@ApiModelProperty(value = "상품명")
	private String goodsNm;

	@ApiModelProperty(value = "주문자명")
	private String buyerNm;

	@ApiModelProperty(value = "주문자휴대폰")
	private String buyerHp;

	@ApiModelProperty(value = "주문자메일")
	private String buyerMail;

	@ApiModelProperty(value = "사용자PK")
	private long urUserId;
}
