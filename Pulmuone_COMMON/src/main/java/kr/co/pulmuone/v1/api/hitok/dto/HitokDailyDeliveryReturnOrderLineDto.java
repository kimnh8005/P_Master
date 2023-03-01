package kr.co.pulmuone.v1.api.hitok.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 하이톡 일일배송 반품 line Dto
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "HitokDailyDeliveryReturnOrderLineDto")
public class HitokDailyDeliveryReturnOrderLineDto {

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

    @ApiModelProperty(value = "주문수량")
    private Integer ordCnt;

    @ApiModelProperty(value = "ERP는 직송(U), 배송(S) LDS인 경우 배송구분 [ 0 : 가맹점배송, 1 : 택배 ]")
    private String dlvGrp;

    @ApiModelProperty(value = "고객 배송 요청일.입고처 입고 요청일(YYYYMMDDHHMISS)(예시 : 20200912000000)")
    private String dlvReqDat;

    @ApiModelProperty(value = "과세적용 후 판매금액")
    private double ordAmt;

    @ApiModelProperty(value = "총주문수량")
    private Integer totOrdCnt;

    @ApiModelProperty(value = "주문유형(주문/증정)(택배정산용)통합몰에서 재정의 후 legacy 에 전달 주문의 매출 TRX 가 라인별로 구분되기에 ERP 에서 어떤 주문타입으로 매출인지 본 컬럼에 수신되야함")
    private String ordTyp;

    @ApiModelProperty(value = "(ERP 필요 정보)SHI_CNT")
    private Integer shiToOrgId;

    @ApiModelProperty(value = "스케줄순번")
    private Integer schLinNo;

    @ApiModelProperty(value = "가맹정코드")
    private String stoCd;

    @ApiModelProperty(value = "음용패턴")
    private String drnkPtrn;

    @ApiModelProperty(value = "지사채널(ex.홈,오피스)")
    private String prtnChnl;

    @ApiModelProperty(value = "취소 또는 일부 반품 시 원주문라인 번호")
    private String ordNoDtlCnl;

    @ApiModelProperty(value = "과세구분")
    private String taxYn;

    @ApiModelProperty(value = "주문 클레임 상세 PK")
    private String refVal01;

    @Builder
    public HitokDailyDeliveryReturnOrderLineDto(String crpCd, String hrdSeq, String oriSysSeq, String ordNoDtl, String ordNum,
                                                String erpItmNo, Integer ordCnt, String dlvGrp, String dlvReqDat, double ordAmt,
                                                Integer totOrdCnt, String ordTyp, Integer shiToOrgId, Integer schLinNo, String stoCd,
                                                String drnkPtrn, String prtnChnl, String ordNoDtlCnl, String taxYn, String refVal01) {
        this.crpCd = crpCd;
        this.hrdSeq = hrdSeq;
        this.oriSysSeq = oriSysSeq;
        this.ordNoDtl = ordNoDtl;
        this.ordNum = ordNum;
        this.erpItmNo = erpItmNo;
        this.ordCnt = ordCnt;
        this.dlvGrp = dlvGrp;
        this.dlvReqDat = dlvReqDat;
        this.ordAmt = ordAmt;
        this.totOrdCnt = totOrdCnt;
        this.ordTyp = ordTyp;
        this.shiToOrgId = shiToOrgId;
        this.schLinNo = schLinNo;
        this.stoCd = stoCd;
        this.drnkPtrn = drnkPtrn;
        this.prtnChnl = prtnChnl;
        this.ordNoDtlCnl = ordNoDtlCnl;
        this.taxYn = taxYn;
        this.refVal01 = refVal01;
    }

}
