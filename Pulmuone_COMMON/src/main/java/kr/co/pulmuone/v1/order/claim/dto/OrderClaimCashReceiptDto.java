package kr.co.pulmuone.v1.order.claim.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 클레임, 금액, 배송지 Response Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 06. 24.	천혜현		최초작성
 * =======================================================================
 * </PRE>
 */
@Builder
@Getter
@ToString
@ApiModel(description = "주문 클레임 현금영수증 DTO")
public class OrderClaimCashReceiptDto{

    /* 현금영수증 발행정보 조회 */
	@ApiModelProperty(value = "주문 현금영수증 정보 PK")
    private long odOrderCashReceiptId;

	@ApiModelProperty(value = "현금영수증 발급구분")
	private String cashReceiptType;

	@ApiModelProperty(value = "발급금액")
	private int cashPrice;

	@ApiModelProperty(value = "발급번호")
    private String cashNo;

    @ApiModelProperty(value = "승인번호")
    private String cashReceiptAuthNo;

    @ApiModelProperty(value = "거래 ID")
    private String tid;

    @ApiModelProperty(value = "결제방법 공통코드(PAY_TP)")
    private String payTp;

    @ApiModelProperty(value = "PG 종류 공통코드(PG_ACCOUNT_TYPE)")
    private String pgService;

    @ApiModelProperty(value = "주문결제마스터 PK")
    private long odPaymentMasterId;

    @ApiModelProperty(value = "회원 PK")
    private long urUserId;

    @ApiModelProperty(value = "비회원 CI")
    private String guestCi;

    @ApiModelProperty(value = "주문번호")
    private String odid;

    @ApiModelProperty(value = "주문 PK")
    private long odOrderId;

    @ApiModelProperty(value = "상품명")
    private String goodsNm;

    @ApiModelProperty(value = "구매자명")
    private String buyerNm;

    @ApiModelProperty(value = "구매자 메일")
    private String buyerMail;

    @ApiModelProperty(value = "구매자 핸드폰번호")
    private String buyerHp;

}