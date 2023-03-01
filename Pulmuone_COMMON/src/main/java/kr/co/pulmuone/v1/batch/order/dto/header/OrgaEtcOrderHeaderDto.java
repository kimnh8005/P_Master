package kr.co.pulmuone.v1.batch.order.dto.header;

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
 * 올가 기타주문 header Dto
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "OrgaEtcOrderHeaderDto")
public class OrgaEtcOrderHeaderDto {

    @ApiModelProperty(value = "입력시스템 코드값 ESHOP : 온라인몰", required = true)
    private String srcSvr;

    @ApiModelProperty(value = "전송 대상 시스템 구분 코드값", required = true)
    private String crpCd;

    @ApiModelProperty(value = "데이터 생성일자(YYYYMMDDHHMISS)")
    private String creDat;

    @ApiModelProperty(value = "상태값 (0 고정값)")
    private String proFlg;

    @ApiModelProperty(value = "생성일(YYYYMMDDHHMISS)")
    private String insDat;

    @ApiModelProperty(value = "주문일시 YYYYMMDDHH24MISS")
    private String ordDat;

    @ApiModelProperty(value = "배송메시지")
    private String dlvMsg;

    @ApiModelProperty(value = "주문자명")
    private String ordNam;

    @ApiModelProperty(value = "주문자 전화번호 (0000-0000-0000)")
    private String ordTel;

    @ApiModelProperty(value = "수령자명")
    private String dlvNam;

    @ApiModelProperty(value = "수령자 전화번호 (0000-0000-0000)")
    private String dlvTel;

    @ApiModelProperty(value = "수령자 주소 우편번호")
    private String dlvZip;

    @ApiModelProperty(value = "수령자 주소 앞")
    private String dlvAdr1;

    @ApiModelProperty(value = "수령자 주소 뒤")
    private String dlvAdr2;

    @ApiModelProperty(value = "품목 Code")
    private String erpItmNo;

    @ApiModelProperty(value = "쇼핑몰에서 회원에게 제공하는 품목명")
    private String shpItmNam;

    @ApiModelProperty(value = "주문수량")
    private Long ordCnt;

    @ApiModelProperty(value = "통합몰 주문번호")
    private String ordNum;

    @ApiModelProperty(value = "ERP 전용 Order Line Key 값 (주문상품 순번)")
    private String ordNoDtl;

    @ApiModelProperty(value = "매장코드")
    private String shpCd;

    @ApiModelProperty(value = "매장명")
    private String shpNam;

    @Builder
    public OrgaEtcOrderHeaderDto(String srcSvr, String crpCd, String creDat, String insDat, String ordDat,
                                 String dlvMsg, String ordNam, String ordTel, String dlvNam, String dlvTel,
                                 String dlvZip, String dlvAdr1, String dlvAdr2, String erpItmNo, String shpItmNam,
                                 Long ordCnt, String ordNum, String ordNoDtl, String shpCd, String shpNam) {
        this.srcSvr = srcSvr;
        this.crpCd = crpCd;
        this.creDat = creDat;
        this.proFlg = "0";
        this.insDat = insDat;
        this.ordDat = ordDat;
        this.dlvMsg = dlvMsg;
        this.ordNam = ordNam;
        this.ordTel = ordTel;
        this.dlvNam = dlvNam;
        this.dlvTel = dlvTel;
        this.dlvZip = dlvZip;
        this.dlvAdr1 = dlvAdr1;
        this.dlvAdr2 = dlvAdr2;
        this.erpItmNo = erpItmNo;
        this.shpItmNam = shpItmNam;
        this.ordCnt = ordCnt;
        this.ordNum = ordNum;
        this.ordNoDtl = ordNoDtl;
        this.shpCd = shpCd;
        this.shpNam = shpNam;
    }

}
