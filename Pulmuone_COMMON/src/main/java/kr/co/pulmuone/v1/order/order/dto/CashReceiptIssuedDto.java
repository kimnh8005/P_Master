package kr.co.pulmuone.v1.order.order.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 상세 주문자정보 관련 VO
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021.05.18.             천혜현         최초작성
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "현금영수증 발급내역 DTO")
public class CashReceiptIssuedDto {
    
    /**
     * 주문번호
     */
    private String odid;

    /**
     * 주문PK
     */
    private Long odOrderId;

    /**
     * 주문결제 PK
     */
    private Long odPaymentMasterId;

    /**
     * 주문일시
     */
    private String createDt;

    /**
     * 회원 PK
     */
    private Long urUserId;

    /**
     * 비회원 CI
     */
    private String guestCi;

    /**
     * 결제타입
     */
    private String payTp;

    /**
     * pg구분
     */
    private String pgService;

    /**
     * 결제상태
     */
    private String status;

    /**
     * 결제상태 코드명
     */
    private String statusNm;

    /**
     * 과세
     */
    private int taxablePrice;

    /**
     * 면세
     */
    private int nonTaxablePrice;

    /**
     * 발급금액
     */
    private int paymentPrice;

    /**
     * 증빙번호
     */
    private String cashReceiptNo;

    /**
     * 승인번호
     */
    private String cashReceiptAuthNo;

    /**
     * 현금영수증 URL
     */
    private String billUrl;

    /**
     * 현금영수증 발급 타입
     */
    private String cashReceiptType;

    /**
     * no
     */
    private String rnum;

}
