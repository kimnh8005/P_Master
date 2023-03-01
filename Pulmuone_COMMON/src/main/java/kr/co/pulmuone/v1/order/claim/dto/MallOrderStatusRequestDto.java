package kr.co.pulmuone.v1.order.claim.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import kr.co.pulmuone.v1.comm.base.vo.FileVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * Mall 주문 상태 변경 Request Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 01. 20.	김명진		최초작성
 * =======================================================================
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "Mall 주문 상태 변경 Request Dto")
public class MallOrderStatusRequestDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "주문PK", required = true)
    private long odOrderId;

	@ApiModelProperty(value = "주문상세정보", required = true)
    private List<MallOrderStatusListDto> odOrderStatusList;

	@ApiModelProperty(value = "결제 정보 PK")
	private String psPayCd;

	@ApiModelProperty(value = "카드 정보 PK")
	private String cardCode;

	@ApiModelProperty(value = "클레임상태코드")
    private String claimStatuscd;

    @ApiModelProperty(value = "MALL 클레임 사유 PK")
    private long psClaimMallId;

    @ApiModelProperty(value = "클레임상세사유")
    private String claimReasonMsg;

    @ApiModelProperty(value = "상품금액")
    private int goodsPrice;

    @ApiModelProperty(value = "상품쿠폰 금액")
    private int goodsCouponPrice;

    @ApiModelProperty(value = "장바구니쿠폰금액")
    private int cartCouponPrice;

    @ApiModelProperty(value = "배송비")
    private int shippingPrice;

    @ApiModelProperty(value = "환불금액")
    private int refundPrice;

    @ApiModelProperty(value = "환불적립금")
    private int refundPointPrice;

    /* 반품 배송지 START */
    @ApiModelProperty(value = "수령인명")
    private String recvNm;

    @ApiModelProperty(value = "수령인핸드폰")
    private String recvHp;

    @ApiModelProperty(value = "수령인연락처")
    private String recvTel;

    @ApiModelProperty(value = "수령인우편번호")
    private String recvZipCd;

    @ApiModelProperty(value = "수령인주소1")
    private String recvAddr1;

    @ApiModelProperty(value = "수령인주소2")
    private String recvAddr2;

    @ApiModelProperty(value = "건물번호")
    private String recvBldNo;

    @ApiModelProperty(value = "배송요청사항")
    private String deliveryMsg;

    @ApiModelProperty(value = "출입정보타입 공통코드(DOOR_MSG_CD)")
    private String doorMsgCd;

    @ApiModelProperty(value = "배송출입 현관 비밀번호")
    private String doorMsg;
    /* 반품 배송지 END */

    /* 환불계좌 START */
    @ApiModelProperty(value = "은행코드")
    private String bankCd;

    @ApiModelProperty(value = "예금주명")
    private String accountHolder;

    @ApiModelProperty(value = "계좌번호")
    private String accountNumber;
    /* 환불계좌 END */

    /* 첨부파일 등록 START */
	@ApiModelProperty(value = "업로드 파일 정보(json형식)")
	private String addFile;

	@ApiModelProperty(value = "업로드 파일 목록")
	List<FileVo> addFileList;
    /* 첨부파일 등록 END */
}