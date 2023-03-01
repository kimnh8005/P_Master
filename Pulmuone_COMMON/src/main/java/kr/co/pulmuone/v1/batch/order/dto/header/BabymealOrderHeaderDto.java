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
 * 베이비밀 주문 header Dto (택배, 일일 공통)
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "BabymealOrderHeaderDto")
public class BabymealOrderHeaderDto {

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

    @ApiModelProperty(value = "주문자 이메일")
    private String ordEml;

    @ApiModelProperty(value = "수령자명")
    private String dlvNam;

    @ApiModelProperty(value = "수령자 전화번호 (0000-0000-0000)")
    private String dlvTel;

    @ApiModelProperty(value = "수령자 휴대폰번호 (0000-0000-0000)")
    private String dlvMobTel;

    @ApiModelProperty(value = "수령자 주소 전체")
    private String dlvAdr;

    @ApiModelProperty(value = "수령자 주소 앞")
    private String dlvAdr1;

    @ApiModelProperty(value = "수령자 주소 뒤")
    private String dlvAdr2;

    @ApiModelProperty(value = "수령자 주소 우편번호")
    private String dlvZip;

    @ApiModelProperty(value = "수령자 주소의 우편번호 정보 중 건물번호")
    private String bldNo;

    @ApiModelProperty(value = "배송메시지")
    private String dlvMsg;
    
    @ApiModelProperty(value = "(ERP 필요 정보)_새벽배송 공동현관 비밀번호")
    private String dlvErlPwd;
    
    @ApiModelProperty(value = "(ERP 필요 정보)_새벽배송 공동현관 출입방법")
    private String dlvErlDor;

    @ApiModelProperty(value = "데이터 생성일자(YYYYMMDDHHMISS)")
    private String creDat;

    @ApiModelProperty(value = "line DTO", required = true)
    private List<?> line;

    @Builder
    public BabymealOrderHeaderDto(String srcSvr, String hrdSeq, String oriSysSeq, String hdrTyp, String ordNum, String ordHpnCd,
                                  String ordDat, String ordNam, String ordTel, String ordMobTel, String ordEml, String dlvNam,
                                  String dlvTel, String dlvMobTel, String dlvAdr, String dlvAdr1, String dlvAdr2, String dlvZip,
                                  String bldNo, String dlvMsg, String dlvErlPwd, String dlvErlDor, String creDat,
                                  List<?> line) {
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
        this.ordEml = ordEml;
        this.dlvNam = dlvNam;
        this.dlvTel = dlvTel;
        this.dlvMobTel = dlvMobTel;
        this.dlvAdr = dlvAdr;
        this.dlvAdr1 = dlvAdr1;
        this.dlvAdr2 = dlvAdr2;
        this.dlvZip = dlvZip;
        this.bldNo = bldNo;
        this.dlvMsg = dlvMsg;
        this.dlvErlPwd = dlvErlPwd;
        this.dlvErlDor = dlvErlDor;
        this.creDat = creDat;
        this.line = line;
    }

}
