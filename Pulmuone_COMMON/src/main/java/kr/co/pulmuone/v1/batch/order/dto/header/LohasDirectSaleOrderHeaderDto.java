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
 * 풀무원건강생활(LDS) 주문 header Dto
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "LohasDirectSaleOrderHeaderDto")
public class LohasDirectSaleOrderHeaderDto {

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

    @ApiModelProperty(value = "주문경로 ['0003' : 임직원(웹),0024' : 임직원(모바일) ,기타 : 지점] 주문경로값은 더 많이 존재")
    private String ordHpnCd;

    @ApiModelProperty(value = "주문일자(YYYYMMDDHHMISS) (예시 : 20200912000000)")
    private String ordDat;

    @ApiModelProperty(value = "주문자명")
    private String ordNam;

    @ApiModelProperty(value = "주문자 전화번호 (0000-0000-0000)")
    private String ordTel;

    @ApiModelProperty(value = "주문자 휴대폰번호 (0000-0000-0000)")
    private String ordMobTel;

    @ApiModelProperty(value = "수령자명")
    private String dlvNam;

    @ApiModelProperty(value = "수령자 주소 전체")
    private String dlvAdr;

    @ApiModelProperty(value = "수령자 주소 앞")
    private String dlvAdr1;

    @ApiModelProperty(value = "수령자 주소 뒤")
    private String dlvAdr2;

    @ApiModelProperty(value = "수령자 주소 우편번호")
    private String dlvZip;

    @ApiModelProperty(value = "수령자 전화번호 (0000-0000-0000)")
    private String dlvTel;

    @ApiModelProperty(value = "수령자 휴대폰번호 (0000-0000-0000)")
    private String dlvMobTel;

    @ApiModelProperty(value = "배송비 ERP/LDS (LDS 의 경우기획전/이벤트에서 LDS제품을 한정수량으로 택배비만 내면 제공하기 위한 용도임)")
    private String dlvCst1;

    @ApiModelProperty(value = "배송메시지")
    private String dlvMsg;

    @ApiModelProperty(value = "line DTO", required = true)
    private List<?> line;

    @Builder
    public LohasDirectSaleOrderHeaderDto(String srcSvr, String hrdSeq, String oriSysSeq, String hdrTyp, String ordNum, String ordHpnCd,
                                         String ordDat, String ordNam, String ordTel, String ordMobTel, String dlvNam, String dlvAdr,
                                         String dlvAdr1, String dlvAdr2, String dlvZip, String dlvTel, String dlvMobTel, String dlvCst1,
                                         String dlvMsg, List<?> line) {
        this.srcSvr = srcSvr;
        this.hrdSeq = hrdSeq;
        this.oriSysSeq = oriSysSeq;
        this.hdrTyp = hdrTyp;
        this.ordNum = ordNum;
        this.ordHpnCd = ordHpnCd;
        this.ordDat = ordDat;
        this.ordNam = ordNam;
        this.ordTel = ordTel;
        this.ordMobTel = ordMobTel;
        this.dlvNam = dlvNam;
        this.dlvAdr = dlvAdr;
        this.dlvAdr1 = dlvAdr1;
        this.dlvAdr2 = dlvAdr2;
        this.dlvZip = dlvZip;
        this.dlvTel = dlvTel;
        this.dlvMobTel = dlvMobTel;
        this.dlvCst1 = dlvCst1;
        this.dlvMsg = dlvMsg;
        this.line = line;
    }

}
