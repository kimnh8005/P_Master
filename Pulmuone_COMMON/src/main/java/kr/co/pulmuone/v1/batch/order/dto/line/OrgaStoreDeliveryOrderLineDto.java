package kr.co.pulmuone.v1.batch.order.dto.line;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 올가 매장배송 주문 line Dto
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "OrgaStoreDeliveryOrderLineDto")
public class OrgaStoreDeliveryOrderLineDto {

    @ApiModelProperty(value = "전송 대상 시스템 구분 코드값", required = true)
    private String crpCd;

    @ApiModelProperty(value = "Header와 Line의 join key", required = true)
    private String hrdSeq;

    @ApiModelProperty(value = "ERP 전용 key", required = true)
    private String oriSysSeq;

    @ApiModelProperty(value = "ERP 전용 Order Line Key 값 (주문상품 순번)")
    private String ordNoDtl;

    @ApiModelProperty(value = "품목 Code")
    private String erpItmNo;

    @ApiModelProperty(value = "KAN 코드")
    private String kanCd;

    @ApiModelProperty(value = "주문수량")
    private Integer ordCnt;

    @ApiModelProperty(value = "고객 배송 요청일.입고처 입고 요청일(YYYYMMDDHHMISS)(예시 : 20200912000000)")
    private String dlvReqDat;

    @ApiModelProperty(value = "데이터 생성일자(YYYYMMDDHHMISS)")
    private String creDat;

    @ApiModelProperty(value = "배송 요청 여부(Y/N) : 배송을 포함해서 매출처리 또는 배송 없이 매출처리만")
    private String dlvReqYn;

    @ApiModelProperty(value = "매출액표시기준 1:판매 -1:반품")
    private double salFg;

    @ApiModelProperty(value = "과세여부")
    private String taxYn;

    @ApiModelProperty(value = "판매단가")
    private double selPrc;

    @ApiModelProperty(value = "매출액(부가세가 포함된 판매단가 * 수량)")
    private double stdAmt;

    @ApiModelProperty(value = "판매여부 Y:판매 N:반품")
    private String salYn;

    @ApiModelProperty(value = "할인액(상품별 총할인액)")
    private double dcAmt;

    @ApiModelProperty(value = "부가세(과세상품인 경우에 한하여 실매출액에 부가세)")
    private double vatAmt;

    @ApiModelProperty(value = "할인액-쿠폰")
    private double dcCpnAmt;

    @ApiModelProperty(value = "POS 등록일시(주문일시)(YYYYMMDDHHMISS)")
    private String posInsDt;

    @ApiModelProperty(value = "(ERP 필요 정보)_샵구분")
    private String shpCd;

    @ApiModelProperty(value = "통합몰 주문번호")
    private String ordNum;

    @Builder
    public OrgaStoreDeliveryOrderLineDto(String crpCd, String hrdSeq, String oriSysSeq, String ordNoDtl, String erpItmNo, String kanCd, Integer ordCnt,
                                         String dlvReqDat, String creDat, double salFg, String taxYn, double selPrc, double stdAmt, String salYn, //double dcAmt,
                                         double vatAmt, String posInsDt, String shpCd, String ordNum) {
        this.crpCd = crpCd;
        this.hrdSeq = hrdSeq;
        this.oriSysSeq = oriSysSeq;
        this.ordNoDtl = ordNoDtl;
        this.erpItmNo = erpItmNo;
        this.kanCd = kanCd;
        this.ordCnt = ordCnt;
        this.dlvReqDat = dlvReqDat;
        this.creDat = creDat;
        this.dlvReqYn = "Y";
        this.salFg = salFg;
        this.taxYn = taxYn;
        this.selPrc = selPrc;
        this.stdAmt = stdAmt;
        this.salYn = salYn;
        this.dcAmt = 0;
        this.vatAmt = vatAmt;
        this.dcCpnAmt = 0;
        this.posInsDt = posInsDt;
        this.shpCd = shpCd;
        this.ordNum = ordNum;
    }

}
