package kr.co.pulmuone.v1.order.create.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;


/**
 * <PRE>
 * Forbiz Korea
 * 주문생성 주문 관련 VO
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 18.            이명수         최초작성
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "주문생성 주문  OD_CREATE_ORDER VO")
public class CreateOrderVo {
    @ApiModelProperty(value = "주문생성 PK")
    private long odCreateOrderId;

    @ApiModelProperty(value = "주문생성정보 PK")
    private long odCreateInfoId;

    @ApiModelProperty(value = "판매처그룹:")
    private String sellersGroupCd;

    @ApiModelProperty(value = "회원 그룹 ID")
    private long urGroupId;

    @ApiModelProperty(value = "회원 그룹명")
    private String urGroupNm;

    @ApiModelProperty(value = "회원 ID")
    private long urUserId;

    @ApiModelProperty(value = "로그인 아이디")
    private String loginId;

    @ApiModelProperty(value = "주문자 명")
    private String buyerNm;

    @ApiModelProperty(value = "주문자 핸드폰")
    private String buyerHp;

    @ApiModelProperty(value = "주문자 연락처")
    private String buyerTel;

    @ApiModelProperty(value = "주문자 이메일")
    private String buyerMail;

    @ApiModelProperty(value = "주문자 우편번호")
    private String buyerZip;

    @ApiModelProperty(value = "주문자 주소1")
    private String buyerAddr1;

    @ApiModelProperty(value = "주문자 주소2")
    private String buyerAddr2;

    @ApiModelProperty(value = "결제수단")
    private String paymentTypeCd;

    @ApiModelProperty(value = "주문자 유형")
    private String buyerTypeCd;

    @ApiModelProperty(value = "주문 유형 ")
    private String agentTypeCd;

    @ApiModelProperty(value = "등록일")
    private LocalDateTime createDt;
}
