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
 * 매출 주문 (풀무원식품) line Dto
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "SalesOrderFoodLineDto")
public class SalesOrderFoodLineDto {

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

    @ApiModelProperty(value = "KAN 코드")
    private String kanCd;

    @ApiModelProperty(value = "주문수량")
    private Integer ordCnt;

    @ApiModelProperty(value = "물류 배송 출발 요청일 (Header 에 필요한 데이터임)(YYYYMMDDHHMISS)(예시 : 20200912000000)")
    private String dlvStrDat;

    @ApiModelProperty(value = "고객 배송 요청일.입고처 입고 요청일(YYYYMMDDHHMISS)(예시 : 20200912000000)")
    private String dlvReqDat;

    @ApiModelProperty(value = "판매단가")
    private double selPrc;

    @ApiModelProperty(value = "쇼핑몰에서 회원에게 제공하는 품목명")
    private String shpItmNam;

    @ApiModelProperty(value = "쇼핑몰 총금액(부가세포함가)")
    private double totOrdAmt;

    @ApiModelProperty(value = "쇼핑몰 총금액(부가세제외)")
    private double totOrdAmtNonTax;

    @ApiModelProperty(value = "쇼핑몰 세액 (부가세 * dtl_cnt수량)")
    private double totOrdTax;

    @ApiModelProperty(value = "조직 Code. 출고처 창고코드값")
    private String wahCd;

    @ApiModelProperty(value = "배송 요청 여부(Y/N) : 배송을 포함해서 매출처리 또는 배송 없이 매출처리만")
    private String dlvReqYn;

    @ApiModelProperty(value = "(ERP 필요 정보)_CONTEXT.고정값 (NO)")
    private String ctx;

    @ApiModelProperty(value = "(ERP 필요 정보)_DRP에서 정보를 읽었는지 여부(Y/N)")
    private String drpFlg;

    @ApiModelProperty(value = "(ERP 필요 정보)_라인 유형 카테고리 코드(예 : Return / order)")
    private String linCatCd;

    @ApiModelProperty(value = "(ERP 필요 정보)_주문 Line 유형")
    private String linTyp;

    @ApiModelProperty(value = "(ERP 필요 정보)_단위.입수량 단위로 법인별 마스터를 따르지 않는다면 필수로 받아야함")
    private String ordCntUom;

    @ApiModelProperty(value = "(ERP 필요 정보)_OU.법인 구분을 위한 ID 값으로 필요")
    private Integer orgId;

    @ApiModelProperty(value = "(ERP 필요 정보)_Validation Status(Y/N)")
    private String vdtSta;

    @ApiModelProperty(value = "주문 Line 유형 ID")
    private Integer linTypId;

    @ApiModelProperty(value = "주문유형")
    private String ordTyp;

    @ApiModelProperty(value = "(ERP 필요 정보)SHI_CNT")
    private Integer shiToOrgId;

    @ApiModelProperty(value = "(ERP 필요 정보)_주문출처(ERS/BABS/ECOS/ECMS/ERF)")
    private String ordSrc;

    @ApiModelProperty(value = "(ERP 필요 정보)_단가적용일자(YYYYMMDDHHMISS)")
    private String prcDat;

    @ApiModelProperty(value = "출하우선순위 Code(ERP 필요 정보)")
    private String shiPriCd;

    @ApiModelProperty(value = "(ERP 필요 정보)_고객요청일자 입력(반품시에는 반품에 대한 회수 받기를 원하는 요청일)")
    private String prmDat;

    @ApiModelProperty(value = "과세구분")
    private String taxYn;

    @ApiModelProperty(value = "매출만 연동 여부")
    private String refVal02;

    @Builder
    public SalesOrderFoodLineDto(String crpCd, String hrdSeq, String oriSysSeq, String ordNoDtl, String ordNum,
                                 String erpItmNo, String kanCd, Integer ordCnt, String dlvStrDat, String dlvReqDat,
                                 double selPrc, String shpItmNam, double totOrdAmt, double totOrdAmtNonTax, double totOrdTax,
                                 String wahCd, String ctx, String linCatCd, String linTyp, String ordCntUom, Integer orgId,
                                 Integer linTypId, String ordTyp, Integer shiToOrgId, String ordSrc, String prcDat, String shiPriCd,
                                 String prmDat, String taxYn, String refVal02) {
        this.crpCd = crpCd;
        this.hrdSeq = hrdSeq;
        this.oriSysSeq = oriSysSeq;
        this.ordNoDtl = ordNoDtl;
        this.ordNum = ordNum;
        this.erpItmNo = erpItmNo;
        this.kanCd = kanCd;
        this.ordCnt = ordCnt;
        this.dlvStrDat = dlvStrDat;
        this.dlvReqDat = dlvReqDat;
        this.selPrc = selPrc;
        this.shpItmNam = shpItmNam;
        this.totOrdAmt = totOrdAmt;
        this.totOrdAmtNonTax = totOrdAmtNonTax;
        this.totOrdTax = totOrdTax;
        this.wahCd = wahCd;
        this.dlvReqYn = "N";
        this.ctx = ctx;
        this.drpFlg = "N";
        this.linCatCd = linCatCd;
        this.linTyp = linTyp;
        this.ordCntUom = ordCntUom;
        this.orgId = orgId;
        this.vdtSta = "Y";
        this.linTypId = linTypId;
        this.ordTyp = ordTyp;
        this.shiToOrgId = shiToOrgId;
        this.ordSrc = ordSrc;
        this.prcDat = prcDat;
        this.shiPriCd = shiPriCd;
        this.prmDat = prmDat;
        this.taxYn = taxYn;
        this.refVal02 = refVal02;
    }

}
