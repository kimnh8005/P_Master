package kr.co.pulmuone.v1.api.lotteglogis.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@ApiModel(description = "롯데 거래처 주문 API Request")
public class LotteGlogisClientOrderRequestDto{

    @ApiModelProperty(value = "거래처코드(6 자리 택배코드)")
    private String jobCustCd;

    @ApiModelProperty(value = "출고반품구분 (01:출고, 02:반품)")
    private String ustRtgSctCd;

    @ApiModelProperty(value = "오더구분 (1:일반, 2:교환, 3:AS)")
    private String ordSct;

    @ApiModelProperty(value = "운임구분 (03:신용, 04:복합)")
    private String fareSctCd;

    @ApiModelProperty(value = "주문번호")
    private String ordNo;

    @ApiModelProperty(value = "운송장번호")
    private String invNo;

    @ApiModelProperty(value = "원송장번호")
    private String orglInvNo;

    @ApiModelProperty(value = "송하인명")
    private String snperNm;

    @ApiModelProperty(value = "송하인전화번호")
    private String snperTel;

    @ApiModelProperty(value = "송하인휴대전화번호")
    private String snperCpno;

    @ApiModelProperty(value = "송하인우편번호")
    private String snperZipcd;

    @ApiModelProperty(value = "송하인주소 (기본주소 + 상세주소)")
    private String snperAdr;

    @ApiModelProperty(value = "수하인명")
    private String acperNm;

    @ApiModelProperty(value = "수하인전화번호")
    private String acperTel;

    @ApiModelProperty(value = "수하인휴대전화번호")
    private String acperCpno;

    @ApiModelProperty(value = "수하인우편번호")
    private String acperZipcd;

    @ApiModelProperty(value = "수하인주소 (기본주소 + 상세주소)")
    private String acperAdr;

    @ApiModelProperty(value = "박스크기 (A, B, C, D, E, F)")
    private String boxTypCd;

    @ApiModelProperty(value = "상품명")
    private String gdsNm;

    @ApiModelProperty(value = "배달메세지내용")
    private String dlvMsgCont;

    @ApiModelProperty(value = "고객메세지내용")
    private String cusMsgCont;

    @ApiModelProperty(value = "집하요청일")
    private String pickReqYmd;

    @ApiModelProperty(value = "합포장여부 ( Y / N)")
    private String bdpkSctCd;

    @ApiModelProperty(value = "합포장 KEY")
    private String bdpkKey;
}
