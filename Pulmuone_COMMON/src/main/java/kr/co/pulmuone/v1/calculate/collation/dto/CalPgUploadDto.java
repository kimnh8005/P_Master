package kr.co.pulmuone.v1.calculate.collation.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * <PRE>
 * Forbiz Korea
 * PG 거래 내역 대사 조회 Upload Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 03. 05.	이명수		최초작성
 * =======================================================================
 * </PRE>
 */
@Builder
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "PG 거래 내역 대사 조회 Upload Dto")
public class CalPgUploadDto {

	@ApiModelProperty(value = "상품MID ")
    private String no;

	@ApiModelProperty(value = "상품MID ")
    private String shopMid;

	@ApiModelProperty(value = "대금지급일")
	private String giveDt;

	@ApiModelProperty(value = "결제일자(환불일자)")
    private String approvalDt;

	@ApiModelProperty(value = "카드명 ")
    private String cardNm;

	@ApiModelProperty(value = "주문번호 ")
    private String odid;

    @ApiModelProperty(value = "거래 ID")
    private String tid;

    @ApiModelProperty(value = "거래 ID")
    private String buyerName;

    @ApiModelProperty(value = "거래 ID")
    private String goodsName;

    @ApiModelProperty(value = "결제금액(거래금액)")
    private String transAmt;

    @ApiModelProperty(value = "M포인트 금액")
    private String mPoint;

    @ApiModelProperty(value = "수수료 ")
    private String commission;

    @ApiModelProperty(value = "무이자 수수료")
    private String freeCommission;

    @ApiModelProperty(value = "M포인트 수수료")
    private String mPointCommission;

    @ApiModelProperty(value = "마케팅 수수료")
    private String marketingCommission;

    @ApiModelProperty(value = "부가세 ")
    private String vat;

    @ApiModelProperty(value = "M포인트 부가세")
    private String mPointVat;

    @ApiModelProperty(value = "마케팅 부가세")
    private String marketingVat;

    @ApiModelProperty(value = "마케팅 부가세")
    private String payAmt;

    @ApiModelProperty(value = "마케팅 부가세")
    private String escrowType;

    @ApiModelProperty(value = "마케팅 부가세")
    private String compactPayType;

    @ApiModelProperty(value = "마케팅 부가세")
    private String status;


	@ApiModelProperty(value = "PG 거래내역 상세 PK")
    private Long odPgCompareUploadDetailId;

    @ApiModelProperty(value = "PG 거래내역 업로드 정보PK")
    private Long odPgCompareUploadInfoId;

    @ApiModelProperty(value = "PG 서비스 코드")
    private String pgService;

    @ApiModelProperty(value = "결제타입 (G : 결제, F : 환불)")
    private String type;



    @ApiModelProperty(value = "카드 승인번호 ")
    private String cardAuthNum;

    @ApiModelProperty(value = "할부개월수 ")
    private String cardQuota;

    @ApiModelProperty(value = "구분타입 ")
    private String pgUploadGubun;







    @ApiModelProperty(value = "공제금액 (결제금액-정산금액)")
    private String deductAmt;

    @ApiModelProperty(value = "정산금액")
    private String accountAmt;






    @ApiModelProperty(value = "에스크로수수료 ")
    private String escrowCommission;

    @ApiModelProperty(value = "에스크로 부가세")
    private String escrowVat;











    @ApiModelProperty(value = "인증 수수료")
    private String certificationCommission;

    @ApiModelProperty(value = "인증 부가세")
    private String certificationVat;








    @ApiModelProperty(value = "은행명 ")
    private String bankNm;

    @ApiModelProperty(value = "계좌 번호 ")
    private String bankAccountNumber;

    @ApiModelProperty(value = "정산일자 ")
    private String accountDt;




    @ApiModelProperty(value = "PK")
    private Long odOutMallCompareUploadInfoId;

    @ApiModelProperty(value = "등록자 ID")
    private Long createId;

    @ApiModelProperty(value = "성공여부")
    private boolean success;

    @ApiModelProperty(value = "실패사유")
    private String failMessage;

    @ApiModelProperty(value = "업로드 성공여부")
    private String successYn;

    @ApiModelProperty(value = "이니시스 타입")
    private String inicisType;
    

}
