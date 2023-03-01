package kr.co.pulmuone.v1.order.order.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 관련 VO
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 15.            이명수         최초작성
 * =======================================================================
 * </PRE>
 */
@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "주문 OD_ORDER VO")
public class OrderVo {

    @ApiModelProperty(value = "주문 PK")
    private long odOrderId;

    @ApiModelProperty(value = "주문번호")
    private String odid;

    @ApiModelProperty(value = "판매처 PK")
    private long omSellersId;

    @ApiModelProperty(value = "수집몰주문번호")
    private String collectionMallId;

    @ApiModelProperty(value = "외부몰주문번호")
    private String outmallId;

    @ApiModelProperty(value = "회원그룹아이디")
    private long urGroupId;

    @ApiModelProperty(value = "회원그룹명")
    private String urGroupNm;

    @ApiModelProperty(value = "회원아이디")
    private long urUserId;

    @ApiModelProperty(value = "임직원사번")
    private String urEmployeeCd;

    @ApiModelProperty(value = "비회원 CI")
    private String guestCi;

    @ApiModelProperty(value = "주문자 명")
    private String buyerNm;

    @ApiModelProperty(value = "주문자 핸드폰")
    private String buyerHp;

    @ApiModelProperty(value = "주문자 연락처")
    private String buyerTel;

    @ApiModelProperty(value = "주문자 이메일")
    private String buyerMail;

    @ApiModelProperty(value = "주문 상품 코드")
    private long ilGoodsId;

    @ApiModelProperty(value = "주문 상품명")
    private String goodsNm;

    @ApiModelProperty(value = "판매처그룹")
    private String sellersGroupCd;

    @ApiModelProperty(value = "결제수단")
    private String orderPaymentType;

    @ApiModelProperty(value = "주문자 유형 : 공통코드(BUYER_TYPE)")
    private String buyerTypeCd;

    @ApiModelProperty(value = "주문 유형 : 공통코드(AGENT_TYPE)")
    private String agentTypeCd;

    @ApiModelProperty(value = "주문경로 ERP I/F 주문경로 필요")
    private String orderHpnCd;

    @ApiModelProperty(value = "일반주문: N, 선물하기주문: Y")
    private String giftYn;

    @ApiModelProperty(value = "사용자환경정보")
    private String urPcidCd;

    @ApiModelProperty(value = "주문금액")
    private int orderPrice;

    @ApiModelProperty(value = "배송비합계")
    private int shippingPrice;

    @ApiModelProperty(value = "쿠폰할인합계")
    private int couponPrice;

    @ApiModelProperty(value = "결제금액")
    private int paidPrice;

    @ApiModelProperty(value = "정상주문여부")
    private String orderYn;

    @ApiModelProperty(value = "주문일자")
    private String createDt;

    @ApiModelProperty(value = "매장(배송/픽업)-스토어PK")
    private String urStoreId;

    @ApiModelProperty(value = "매장(배송/픽업) 스케줄 PK")
    private Long urStoreScheduleId;

    @ApiModelProperty(value = "매장(배송/픽업)-회차")
    private Long storeScheduleNo;

    @ApiModelProperty(value = "매장(배송/픽업)-주문배송시작시간")
    private LocalDateTime storeStartTime;

    @ApiModelProperty(value = "매장(배송/픽업)-주문배송종료시간")
    private LocalDateTime storeEndTime;

    @ApiModelProperty(value = "주문생성여부")
    private String createYn;

    @ApiModelProperty(value = "엑셀 업로드 성공 정보 PK")
    private String ifOutmallExcelSuccId;

    @ApiModelProperty(value = "실패사유")
    private String failMessage;

    @ApiModelProperty(value = "주문복사 매출만 연동 여부")
    private String orderCopySalIfYn;

    @ApiModelProperty(value = "주문복사 원본 주문 번호")
    private String orderCopyOdid;

    @ApiModelProperty(value = "주문복사여부")
    private String orderCopyYn;

    @ApiModelProperty(value = "주문생성여부")
    private String orderCreateYn;
}
