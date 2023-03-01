package kr.co.pulmuone.v1.order.claim.dto;

import java.util.HashMap;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.user.buyer.dto.vo.CodeInfoVo;
import kr.co.pulmuone.v1.user.buyer.dto.vo.CommonGetRefundBankResultVo;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * Mall 주문 클레임 요청 정보 리스트 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 01. 22.	김명진		최초작성
 * =======================================================================
 * </PRE>
 */
@Builder
@Getter
@ToString
@ApiModel(description = "Mall 주문 클레임 요청 정보 리스트 Dto")
public class MallOrderClaimResponseDto {

    @ApiModelProperty(value = "주문 일자")
    private String createDt;

	@ApiModelProperty(value = "주문 번호")
    private String odid;

	@ApiModelProperty(value = "배송정책리스트")
	private List<MallOrderClaimDeliveryListDto> deliveryList;

	@ApiModelProperty(value = "수령인정보")
	private MallOrderClaimRecvDto receiver;

	@ApiModelProperty(value = "사유정보 리스트")
	private List<CodeInfoVo> reasonList;

	@ApiModelProperty(value = "환불계좌 은행정보 리스트")
	private List<CodeInfoVo> refundBankList;

	@ApiModelProperty(value = "환불 계좌 정보")
	private CommonGetRefundBankResultVo refundBank;

	@ApiModelProperty(value = "결제방법 정보 리스트")
	private List<HashMap<String,String>> paymentType;

	@ApiModelProperty(value = "카드 정보 리스트")
	private List<HashMap<String,String>> cardList;

}
