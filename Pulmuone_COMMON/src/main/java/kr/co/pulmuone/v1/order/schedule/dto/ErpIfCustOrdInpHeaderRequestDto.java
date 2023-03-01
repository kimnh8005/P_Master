package kr.co.pulmuone.v1.order.schedule.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "ErpIfCustOrdInpHeaderRequestDto")
@Builder
public class ErpIfCustOrdInpHeaderRequestDto {

	 /*
     * ERP API 일일배송 녹즙 입력 Header dto
     */
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

    @ApiModelProperty(value = "수령자 주소의 우편번호 정보 중 건물번호")
    private String bldNo;

    @ApiModelProperty(value = "수령자 주소 우편번호")
    private String dlvZip;

    @ApiModelProperty(value = "배송메시지")
    private String dlvMsg;

    @ApiModelProperty(value = "(ERP 필요 정보)_새벽배송 공동현관 비밀번호")
    private String dlvErlPwd;

    @ApiModelProperty(value = "(ERP 필요 정보)_새벽배송 공동현관 출입방법")
    private String dlvErlDor;

    @ApiModelProperty(value = "주문상태코드")
    private String ordStuCd;

    @ApiModelProperty(value = "line DTO")
    private List<?> line;

}
