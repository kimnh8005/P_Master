package kr.co.pulmuone.v1.api.storedelivery.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 매장배송 API header Dto
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "StoreDeliveryHeaderDto")
public class StoreDeliveryApiHeaderDto {

    @ApiModelProperty(value = "입력시스템 코드값 ESHOP : 온라인몰", required = true)
    private String srcSvr;

    @ApiModelProperty(value = "Header와 Line의 join Key", required = true)
    private String hrdSeq;

    @ApiModelProperty(value = "ERP 전용 key 값", required = true)
    private String oriSysSeq;

    @ApiModelProperty(value = "Header 유형 주문:1, (현재 미사용)취소:2, (현재 미사용)재배송:3, 반품:4 , 5:매출", required = true)
    private String hdrTyp;

    @ApiModelProperty(value = "통합몰 주문번호", required = true)
    private String ordNum;

    @ApiModelProperty(value = "주문자명")
    private String ordNam;

    @ApiModelProperty(value = "주문자 전화번호 (0000-0000-0000)")
    private String ordTel;

    @ApiModelProperty(value = "수령자명")
    private String dlvNam;

    @ApiModelProperty(value = "수령자 주소 우편번호")
    private String dlvZip;

    @ApiModelProperty(value = "수령자 주소 앞")
    private String dlvAdr1;

    @ApiModelProperty(value = "수령자 주소 뒤")
    private String dlvAdr2;

    @ApiModelProperty(value = "수령자 전화번호 (0000-0000-0000)")
    private String dlvTel;

    @ApiModelProperty(value = "배송메시지")
    private String dlvMsg;

    @ApiModelProperty(value = "등록일자(SYSDATE 임) DMIF> 풀샵 데이터 생성일 (YYYYMMDDHHMISS)")
    private String creDat;

    @ApiModelProperty(value = "매장코드")
    private String shpCd;

    @ApiModelProperty(value = "판매여부 Y:판매 N:반품")
    private String salYn;

    @ApiModelProperty(value = "매출액표시기준 1:판매 -1:반품")
    private Integer salFg;

    @ApiModelProperty(value = "총매출액(올가 O2O 주문건에 대한 총 상품 할인 전 정상 판매가격기준의 주문 금액의 합 + 배송비 포함)")
    private Integer totSalAmt;

    @ApiModelProperty(value = "총할인액(올가 O2O 주문건에 대한 총 상품별 할인 금액의 합)")
    private Integer totDcAmt;

    @ApiModelProperty(value = "실매출액(올가 O2O 주문건에 대한 총 상품별 정산 판매가에서 할인 금액을 제외한 합계 금액+ 배송비 포함)")
    private Integer dcmSalAmt;

    @ApiModelProperty(value = "과세매출액 (올가 O2O 주문건에 대한 상품 중 과세상품에서 실매출액 기준으로 부가세 제외한 합계 금액)")
    private Integer vatSalAmt;

    @ApiModelProperty(value = "부가세 (올가 O2O 주문건에 대한 상품 중 과세상품에서 실매출액 기준으로 부가세 합계 금액)")
    private Integer vatAmt;

    @ApiModelProperty(value = "면세매출액 (올가 O2O 주문건에 대한 상품 중 면세상품의 실매출액 기준의 판매 합계 금액)")
    private Integer noVatSalAmt;

    @ApiModelProperty(value = "순매출액(올가 O2O 주문건에 대한 상품으로써 실매출액 - 부가세 금액)")
    private Integer noTaxSalAmt;

    @ApiModelProperty(value = "할인액-쿠폰(향후 활용도를 위한 항목 : 0으로 기본값 셋팅)")
    private Integer dcCpnAmt;

    @ApiModelProperty(value = "배달주문구분 0:일반/대기 2:배달")
    private String dlvOrdFg;

    @ApiModelProperty(value = "등록일시 (주문일시와 동일한 값) (YYYYMMDDHHMISS)")
    private String posInsDt;

    @ApiModelProperty(value = "결제시각(신용카드 또는 무통장 입금 시 결제된 시각) (YYYYMMDDHHMISS)")
    private String payOutDt;

    @ApiModelProperty(value = "배달-배송시간from (YYYYMMDDHHMISS)")
    private String dlvFroDt;

    @ApiModelProperty(value = "배달-배송시간새 (YYYYMMDDHHMISS)")
    private String dlvToDt;

    @ApiModelProperty(value = "배달-차수")
    private String dlvIdx;

    @ApiModelProperty(value = "line DTO", required = true)
    private List<?> line;

    @Builder
    public StoreDeliveryApiHeaderDto(String srcSvr, String hrdSeq, String oriSysSeq, String hdrTyp, String ordNum, String ordNam,
                                     String ordTel, String dlvNam, String dlvZip, String dlvAdr1, String dlvAdr2, String dlvTel,
                                     String dlvMsg, String creDat, String shpCd, String salYn, Integer salFg,
                                     String dlvOrdFg, String posInsDt, String payOutDt, String dlvFroDt, String dlvToDt, String dlvIdx,
                                     List<?> line) {
        this.srcSvr = srcSvr;
        this.hrdSeq = hrdSeq;
        this.oriSysSeq = oriSysSeq;
        this.hdrTyp = hdrTyp;
        this.ordNum = ordNum;
        this.ordNam = ordNam;
        this.ordTel = ordTel;
        this.dlvNam = dlvNam;
        this.dlvZip = dlvZip;
        this.dlvAdr1 = dlvAdr1;
        this.dlvAdr2 = dlvAdr2;
        this.dlvTel = dlvTel;
        this.dlvMsg = dlvMsg;
        this.creDat = creDat;
        this.shpCd = shpCd;
        this.salYn = salYn;
        this.salFg = salFg;
        this.totSalAmt = 0;
        this.totDcAmt = 0;
        this.dcmSalAmt = 0;
        this.vatSalAmt = 0;
        this.vatAmt = 0;
        this.noVatSalAmt = 0;
        this.noTaxSalAmt = 0;
        this.dcCpnAmt = 0;
        this.dlvOrdFg = dlvOrdFg;
        this.posInsDt = posInsDt;
        this.payOutDt = payOutDt;
        this.dlvFroDt = dlvFroDt;
        this.dlvToDt = dlvToDt;
        this.dlvIdx = dlvIdx;
        this.line = line;
    }

}
