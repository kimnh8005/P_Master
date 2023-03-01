package kr.co.pulmuone.v1.batch.order.dto.line;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * <PRE>
 * Forbiz Korea
 * 용인물류 새벽배송 주문 올가홀푸드 line Dto
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "DawnDeliveryOrderOrgaLineDto")
public class DawnDeliveryOrderOrgaLineDto {

    @ApiModelProperty(value = "전송 대상 시스템 구분 코드값")
    private String crpCd;

    @ApiModelProperty(value = "Header와 Line의 join key")
    private String hrdSeq;

    @ApiModelProperty(value = "ERP 전용 key")
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

    @ApiModelProperty(value = "부가세포함 판매단가")
    private double ordTaxPrc;

    @ApiModelProperty(value = "단가에 대한 부가세")
    private double taxPrc;

    @ApiModelProperty(value = "쇼핑몰에서 회원에게 제공하는 품목명")
    private String shpItmNam;

    @ApiModelProperty(value = "(ERP 필요 정보)_샵구분")
    private String shpCd;

    @ApiModelProperty(value = "배송그룹유형")
    private String dlvGrpTyp;

    @ApiModelProperty(value = "쇼핑몰 총금액(부가세포함가)")
    private double totOrdAmt;

    @ApiModelProperty(value = "쇼핑몰 총금액(부가세제외)")
    private double totOrdAmtNonTax;

    @ApiModelProperty(value = "쇼핑몰 세액 (부가세 * dtl_cnt수량)")
    private double totOrdTax;

    @ApiModelProperty(value = "(ERP 필요 정보)_매장에서 추가할인 (직원할인 등) 경우는 FLAG를 'Y'로 입력(attribute7)")
    private String addDisFlg;

    @ApiModelProperty(value = "조직 Code. 출고처 창고코드값")
    private String wahCd;

    @ApiModelProperty(value = "배송 요청 여부(Y/N) : 배송을 포함해서 매출처리 또는 배송 없이 매출처리만")
    private String dlvReqYn;

    @ApiModelProperty(value = "(ERP 필요 정보)_단가계산플래그.ERP에 매출을 반영할 단가 기준(예 : Y / N)")
    private String cclPrcFlg;

    @ApiModelProperty(value = "(ERP 필요 정보)_CONTEXT.고정값 (NO)")
    private String ctx;

    @ApiModelProperty(value = "(ERP 필요 정보)_DRP에서 정보를 읽었는지 여부(Y/N)")
    private String drpFlg;

    @ApiModelProperty(value = "(ERP 필요 정보)_라인 유형 카테고리 코드(예 : Return / order)")
    private String linCatCd;

    @ApiModelProperty(value = "(ERP 필요 정보)_주문 Line 유형")
    private String linTyp;

    @ApiModelProperty(value = "(ERP 필요 정보)_Order Merge Flag.주문을 합침할지에 대한 flag")
    private String ordMrgFlg;

    @ApiModelProperty(value = "(ERP 필요 정보)_주문/발주 연계 유형.주문/발주 유형으로 각 유형에 따라 재고만이동 되거나 PO 만 발생하거나 주문이 생기거나 함")
    private String ordPoTyp;

    @ApiModelProperty(value = "(ERP 필요 정보)_단위.입수량 단위로 법인별 마스터를 따르지 않는다면 필수로 받아야함")
    private String ordCntUom;

    @ApiModelProperty(value = "(ERP 필요 정보)_OU.법인 구분을 위한 ID 값으로 필요")
    private Integer orgId;

    @ApiModelProperty(value = "(ERP 필요 정보)_단가적용일자(YYYYMMDDHHMISS)")
    private String prcDat;

    @ApiModelProperty(value = "(ERP 필요 정보)_Validation 시 : 프로모션 라인일 경우 'Y', Legacy에서 Insert 시 : 'N'으로 입력")
    private String pmtLinFlg;

    @ApiModelProperty(value = "(ERP 필요 정보)_푸드머스 증정용 : Parent ORIG_SYS_LINE_REF")
    private String pmtOriSysLinRef;

    @ApiModelProperty(value = "(ERP 필요 정보)_Receipt Org ID(물류).물류 입고구분을 위한 데이터로 Dblink 로 되어 넘겨줄것으로 보이나 실제 푸드머스 직매입까지 연결되여 관리되야 하는 항목")
    private Integer recOrgId;

    @ApiModelProperty(value = "(ERP 필요 정보)_정산유형")
    private String salCnlTyp;

    @ApiModelProperty(value = "(ERP 필요 정보)_Shipping Instruction.고객 요청사항 입력란(문앞에 놔줘/ 경비실 나줘/ 님이 드세요 등등)")
    private String shiIns;

    @ApiModelProperty(value = "(ERP 필요 정보)_Validation Status(Y/N) VALIDATION 에 대한 검증 flag 로 보임(필수값 같음)")
    private String vdtSta;

    @ApiModelProperty(value = "주문 Line 유형 ID")
    private Integer linTypId;

    @ApiModelProperty(value = "주문유형")
    private String ordTyp;

    @ApiModelProperty(value = "(ERP 필요 정보)SHI_CNT")
    private Integer shiToOrgId;

    @ApiModelProperty(value = "(ERP 필요 정보)_주문출처(ERS/BABS/ECOS/ECMS/ERF)")
    private String ordSrc;

    @ApiModelProperty(value = "(ERP 필요 정보)_고객요청일자 입력(반품시에는 반품에 대한 회수 받기를 원하는 요청일)")
    private String prmDat;

    @ApiModelProperty(value = "과세여부")
    private String taxYn;

    @Builder
    public DawnDeliveryOrderOrgaLineDto(String crpCd, String hrdSeq, String oriSysSeq, String ordNoDtl, String ordNum, String erpItmNo,
                                          String kanCd, Integer ordCnt, String dlvStrDat, String dlvReqDat, double selPrc, double ordTaxPrc, double taxPrc,
                                          String shpItmNam, String shpCd, String dlvGrpTyp, double totOrdAmt, double totOrdAmtNonTax, double totOrdTax,
                                          String wahCd, String ctx, String linCatCd, String linTyp, String ordPoTyp, String ordCntUom, Integer orgId,
                                          String prcDat, Integer recOrgId, String salCnlTyp, String shiIns, Integer linTypId, String ordTyp,
                                          Integer shiToOrgId, String ordSrc, String prmDat, String taxYn) {
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
        this.ordTaxPrc = ordTaxPrc;
        this.taxPrc = taxPrc;
        this.shpItmNam = shpItmNam;
        this.shpCd = shpCd;
        this.dlvGrpTyp = dlvGrpTyp;
        this.totOrdAmt = totOrdAmt;
        this.totOrdAmtNonTax = totOrdAmtNonTax;
        this.totOrdTax = totOrdTax;
        this.addDisFlg = "N";
        this.wahCd = wahCd;
        this.dlvReqYn = "Y";
        this.cclPrcFlg = "N";
        this.ctx = ctx;
        this.drpFlg = "N";
        this.linCatCd = linCatCd;
        this.linTyp = linTyp;
        this.ordMrgFlg = "N";
        this.ordPoTyp = ordPoTyp;
        this.ordCntUom = ordCntUom;
        this.orgId = orgId;
        this.prcDat = prcDat;
        this.pmtLinFlg = "N";
        this.pmtOriSysLinRef = "N";
        this.recOrgId = recOrgId;
        this.salCnlTyp = salCnlTyp;
        this.shiIns = shiIns;
        this.vdtSta = "Y";
        this.linTypId = linTypId;
        this.ordTyp = ordTyp;
        this.shiToOrgId = shiToOrgId;
        this.ordSrc = ordSrc;
        this.prmDat = prmDat;
        this.taxYn = taxYn;
    }

}
