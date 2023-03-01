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
 * 풀무원건강생활(LDS) 반품 주문 풀무원식품 line Dto
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "LohasDirectSaleReturnOrderLineDto")
public class LohasDirectSaleReturnOrderLineDto {

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

    @ApiModelProperty(value = "상품코드(이샵 제품제드) 예>채널상품코드(베이비밀 상품코드)")
    private String malItmNo;

    @ApiModelProperty(value = "주문수량")
    private Integer ordCnt;

    @ApiModelProperty(value = "ERP는 직송(U), 배송(S) LDS인 경우 배송구분 [ 0 : 가맹점배송, 1 : 택배 ]")
    private String dlvGrp;

    @ApiModelProperty(value = "고객 배송 요청일.입고처 입고 요청일(YYYYMMDDHHMISS)(예시 : 20200912000000)")
    private String dlvReqDat;

    @ApiModelProperty(value = "과세적용 후 판매금액")
    private double ordAmt;

    @ApiModelProperty(value = "쇼핑몰에서 회원에게 제공하는 품목명")
    private String shpItmNam;

    @ApiModelProperty(value = "데이터 생성일자(YYYYMMDDHHMISS)")
    private String creDat;

    @ApiModelProperty(value = "배송 요청 여부(Y/N) : 배송을 포함해서 매출처리 또는 배송 없이 매출처리만")
    private String dlvReqYn;

    @ApiModelProperty(value = "주문구분 [ 1 : 주문, 2 : 취소 ] ")
    private String ordCnlFlg;

    @ApiModelProperty(value = "과세구분")
    private String taxYn;

    @ApiModelProperty(value = "주문 클레임 상세 PK")
    private String refVal01;

    @Builder
    public LohasDirectSaleReturnOrderLineDto(String crpCd, String hrdSeq, String oriSysSeq, String ordNoDtl, String ordNum,
                                             String erpItmNo, String malItmNo, Integer ordCnt, String dlvGrp, String dlvReqDat,
                                             double ordAmt, String shpItmNam, String creDat, String ordCnlFlg, String taxYn, String refVal01) {
        this.crpCd = crpCd;
        this.hrdSeq = hrdSeq;
        this.oriSysSeq = oriSysSeq;
        this.ordNoDtl = ordNoDtl;
        this.ordNum = ordNum;
        this.erpItmNo = erpItmNo;
        this.malItmNo = malItmNo;
        this.ordCnt = ordCnt;
        this.dlvGrp = dlvGrp;
        this.dlvReqDat = dlvReqDat;
        this.ordAmt = ordAmt;
        this.shpItmNam = shpItmNam;
        this.creDat = creDat;
        this.dlvReqYn = "N";
        this.ordCnlFlg = ordCnlFlg;
        this.taxYn = taxYn;
        this.refVal01 = refVal01;
    }

}
