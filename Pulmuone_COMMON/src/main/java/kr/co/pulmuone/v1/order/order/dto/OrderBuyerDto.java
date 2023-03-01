package kr.co.pulmuone.v1.order.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.aop.service.*;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 상세 주문자정보 관련 VO
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021.01.26.             최윤지         최초작성
 * =======================================================================
 * </PRE>
 */
@Builder
@Getter
@ToString
@ApiModel(description = "주문 상세 주문자정보 OD_ORDER VO")
public class OrderBuyerDto {

    private Long odOrderId;
    /*
     * 주문번호
     */
    private Long odid;
    /**
     * 판매처그룹코드
     */
    private String sellersGroupCd;
    /**
     * 판매처그룹명
     */
    private String sellersGroupNm;
    /**
     * 주문자명
     */
    @UserMaskingUserName
    private String buyerNm;
    /**
     * 로그인 아이디
     */
    @UserMaskingLoginId
    private String loginId;
    /**
     * 로그인 아이디
     */
    private String urUserId;
    /**
     * 회원 그룹명
     */
    private String urGroupNm;
    /**
     * 회원 그룹 ID
     */
    private Long urGroupId;
    /**
     * 임직원 회원 여부
     */
    private String urEmployeeYn;
    /**
     * 주문자 휴대폰
     */
    @UserMaskingMobile
    private String buyerHp;
    /**
     * 주문자 연락처
     */
    @UserMaskingTelePhone
    private String buyerTel;

    /**
     * 주문자 이메일
     */
    @UserMaskingEmail
    private String buyerMail;
    /**
     * 주문일시
     */
    private String createDt;
    /**
     * 수집몰 주문번호
     */
    private String collectionMallId;
    /**
     * 외부몰 주문번호
     */
    private String outmallId;

    /**
     * 판매처 URL
     */
    private String sellersUrl;

    /**
     * 판매처 관리자 URL
     */
    private String sellersAdminUrl;

    /**
     * 판매처 명
     */
    private String sellersNm;

    /**
     * 비회원Ci
     */
    private String guestCi;

    /**
     * 결제일시
     */
    private String approvalDt;

    @ApiModelProperty(value = "주문 유형")
    private String agentType;

    @ApiModelProperty(value = "주문복사여부")
    private String orderCopyYn;

    @ApiModelProperty(value = "주문복사 매출만연동여부")
    private String orderCopySalIfYn;

    @ApiModelProperty(value = "주문복사 원본 주문번호")
    private String orderCopyOdid;

    @ApiModelProperty(value = "주문생성여부")
    private String orderCreateYn;

    @ApiModelProperty(value = "외부몰 타입")
    private String outmallType;

    @ApiModelProperty(value = "정기배송신청PK")
    private long odRegularReqId;

    @ApiModelProperty(value = "정기배송신청ID")
    private String reqId;

    @ApiModelProperty(value = "일반주문: N, 선물하기주문: Y")
    private String giftYn;
}
