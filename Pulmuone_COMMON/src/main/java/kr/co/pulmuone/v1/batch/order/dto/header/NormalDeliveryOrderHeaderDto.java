package kr.co.pulmuone.v1.batch.order.dto.header;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 용인물류 일반배송 주문 header Dto
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "NormalDeliveryOrderHeaderDto")
public class NormalDeliveryOrderHeaderDto {

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

    @ApiModelProperty(value = "주문일시 (YYYYMMDDHHMISS)")
    private String ordDat;

    @ApiModelProperty(value = "주문자명")
    private String ordNam;

    @ApiModelProperty(value = "주문자 전화번호 (0000-0000-0000)")
    private String ordTel;

    @ApiModelProperty(value = "주문자 휴대폰번호 (0000-0000-0000)")
    private String ordMobTel;

    @ApiModelProperty(value = "주문자 이메일")
    private String ordEml;

    @ApiModelProperty(value = "수령자명")
    private String dlvNam;

    @ApiModelProperty(value = "수령자 주소 전체")
    private String dlvAdr;

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

    @ApiModelProperty(value = "주문유형(SALES ORDER, TRANSFER ORDER)")
    private String ordCls;

    @ApiModelProperty(value = "사업부 코드(고정값 100)")
    private String divCod;

    @ApiModelProperty(value = "(ERP 필요 정보)_배송유형")
    private String dlvTyp;

    @ApiModelProperty(value = "(ERP 필요 정보)_개인고객식별코드")
    private String perCod;

    @ApiModelProperty(value = "포장유형")
    private String pacTyp;

    @ApiModelProperty(value = "ERP 전용 집하일자 (ATTRIBUTE3 에서 사용) (YYYY-MM-DD)")
    private String erpPicDat;

    @ApiModelProperty(value = "ERP 전용 쇼핑몰 주문번호(Attribute4 에서 사용)")
    private String ordNumErp;

    @ApiModelProperty(value = "line DTO", required = true)
    private List<?> line;

    @Builder
    public NormalDeliveryOrderHeaderDto(String srcSvr, String hrdSeq, String oriSysSeq, String hdrTyp, String ordNum, String ordDat,
                                        String ordNam, String ordTel, String ordMobTel, String ordEml, String dlvNam, String dlvAdr,
                                        String dlvAdr2, String dlvZip, String dlvTel, String dlvMobTel, String dlvCst1, String ordCls,
                                        String divCod, String dlvTyp, String perCod, String pacTyp, String erpPicDat, String ordNumErp,
                                        List<?> line) {
        this.srcSvr = srcSvr;
        this.hrdSeq = hrdSeq;
        this.oriSysSeq = oriSysSeq;
        this.hdrTyp = hdrTyp;
        this.ordNum = ordNum;
        this.ordDat = ordDat;
        this.ordNam = ordNam;
        this.ordTel = ordTel;
        this.ordMobTel = ordMobTel;
        this.ordEml = ordEml;
        this.dlvNam = dlvNam;
        this.dlvAdr = dlvAdr;
        this.dlvAdr2 = dlvAdr2;
        this.dlvZip = dlvZip;
        this.dlvTel = dlvTel;
        this.dlvMobTel = dlvMobTel;
        this.dlvCst1 = dlvCst1;
        this.ordCls = ordCls;
        this.divCod = divCod;
        this.dlvTyp = dlvTyp;
        this.perCod = perCod;
        this.pacTyp = pacTyp;
        this.erpPicDat = erpPicDat;
        this.ordNumErp = ordNumErp;
        this.line = line;
    }

}
