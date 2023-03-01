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
 * 베이비밀 일일배송 주문 line Dto
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "BabymealDailyOrderLineDto")
public class BabymealDailyOrderLineDto {

    @ApiModelProperty(value = "전송 대상 시스템 구분 코드값", required = true)
    private String crpCd;

    @ApiModelProperty(value = "Header와 Line의 join key", required = true)
    private String hrdSeq;

    @ApiModelProperty(value = "ERP 전용 key", required = true)
    private String oriSysSeq;

    @ApiModelProperty(value = "ERP 전용 Order Line Key 값 (주문상품 순번)")
    private String ordNoDtl;

    @ApiModelProperty(value = "통합몰 주문번호")
    private String ordNum;

    @ApiModelProperty(value = "품목 Code")
    private String erpItmNo;

    @ApiModelProperty(value = "상품코드(이샵 제품코드) 예>채널상품코드(베이비밀 상품코드)")
    private String malItmNo;

    @ApiModelProperty(value = "주문수량")
    private Integer ordCnt;

    @ApiModelProperty(value = "고객 배송 요청일.입고처 입고 요청일(YYYYMMDDHHMISS)(예시 : 20200912000000)")
    private String dlvReqDat;

    @ApiModelProperty(value = "판매금액")
    private double stdPrc;

    @ApiModelProperty(value = "결제금액")
    private double selPrc;

    @ApiModelProperty(value = "ERP는 직송(U), 배송(S) LDS인 경우 배송구분 [ 0 : 가맹점배송, 1 : 택배 ]")
    private String dlvGrp;

    @ApiModelProperty(value = "데이터 생성일자(YYYYMMDDHHMISS)")
    private String creDat;

    @ApiModelProperty(value = "상품그룹AGEID")
    private String age;

    @ApiModelProperty(value = "배송형태 ONCEDLVCD ")
    private String onDlvCd;

    @ApiModelProperty(value = "배달방법 DLVMT ")
    private String dlvMt;

    @ApiModelProperty(value = "1회배달 수량ONCNT")
    private Integer onCnt;

    @ApiModelProperty(value = "주차WEEKSID")
    private Integer wekId;

    @ApiModelProperty(value = "알러지 대체식단 여부")
    private String argYn;

    @ApiModelProperty(value = "가맹점코드")
    private String stoCd;

    @ApiModelProperty(value = "과세구분")
    private String taxYn;

    @Builder
    public BabymealDailyOrderLineDto(String crpCd, String hrdSeq, String oriSysSeq, String ordNoDtl, String ordNum,
                                     String erpItmNo, String malItmNo, Integer ordCnt, String dlvReqDat, double stdPrc,
                                     double selPrc, String dlvGrp, String creDat, String age, String onDlvCd, String dlvMt,
                                     Integer onCnt, Integer wekId, String argYn, String stoCd, String taxYn) {
        this.crpCd = crpCd;
        this.hrdSeq = hrdSeq;
        this.oriSysSeq = oriSysSeq;
        this.ordNoDtl = ordNoDtl;
        this.ordNum = ordNum;
        this.erpItmNo = erpItmNo;
        this.malItmNo = malItmNo;
        this.ordCnt = ordCnt;
        this.dlvReqDat = dlvReqDat;
        this.stdPrc = stdPrc;
        this.selPrc = selPrc;
        this.dlvGrp = dlvGrp;
        this.creDat = creDat;
        this.age = age;
        this.onDlvCd = onDlvCd;
        this.dlvMt = dlvMt;
        this.onCnt = onCnt;
        this.wekId = wekId;
        this.argYn = argYn;
        this.stoCd = stoCd;
        this.taxYn = taxYn;
    }

}
