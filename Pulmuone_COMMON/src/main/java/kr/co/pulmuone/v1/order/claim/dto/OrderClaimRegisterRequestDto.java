package kr.co.pulmuone.v1.order.claim.dto;


import java.time.LocalDateTime;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import kr.co.pulmuone.v1.comm.enums.OrderClaimEnums;
import kr.co.pulmuone.v1.order.claim.dto.vo.OrderClaimSendShippingZoneInfoDto;
import kr.co.pulmuone.v1.pg.dto.VirtualAccountDataResponseDto;
import kr.co.pulmuone.v1.pg.service.inicis.dto.InicisNonAuthenticationCartPayResponseDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 클레임 등록 관련 Request Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 15.   강상국         최초작성
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "주문 클레임 등록 관련 Dto")
public class OrderClaimRegisterRequestDto extends BaseRequestPageDto{
	@ApiModelProperty(value = "부분취소여부 true : 부분취소, false : 전체취소")
	private boolean partial;

	@ApiModelProperty(value = "프론트 구분 (0 : Bos, 1:Front, 2:클레임생성배치)")
	private int frontTp;

	@ApiModelProperty(value = "주문 클레임 PK")
	private long odClaimId;

	@ApiModelProperty(value = "주문 PK")
	private long odOrderId;

	@ApiModelProperty(value = "클레임상태구분 (CANCEL : 취소, RETURN : 반품, CS_REFUND: CS환불, RETURN_DELIVERY: 재배송 )")
	private String claimStatusTp;

	@ApiModelProperty(value = "클레임주문상태코드")
	private String claimStatusCd;

	@ApiModelProperty(value = "주문상태코드")
	private String orderStatusCd;

	@ApiModelProperty(value = "CS환불구분(CS_REFUND_TP.PAYMENT_PRICE_REFUND : 결제금액 환불, CS_REFUND_TP.POINT_PRICE_REFUND : 적립금 환불)")
	private String csRefundTp;

	@ApiModelProperty(value = "CS환불승인상태(APPR_STAT.SAVE : 저장, APPR_STAT.REQUEST : 승인요청, APPR_STAT.APPROVED : 승인완료, APPR_STAT.DENIED : 승인반려)")
	private String csRefundApproveCd;

	@ApiModelProperty(value = "클레임사유코드")
	private long psClaimMallId;

	@ApiModelProperty(value = "귀책구분 B: 구매자, S: 판매자")
	private String targetTp;

	@ApiModelProperty(value = "상세사유")
	private String claimReasonMsg;

	@ApiModelProperty(value = "거부사유")
	private String rejectReasonMsg;

	@ApiModelProperty(value = "반품회수여부")
	private String returnsYn;

	@ApiModelProperty(value = "환불타입")
	private String refundType;

	@ApiModelProperty(value = "대표 상품명")
	private String goodsNm;

	@ApiModelProperty(value = "상품금액")
	private int goodsPrice;

	@ApiModelProperty(value = "환불예정상품금액(환불 상품금액 - 할인금액)")
	private int refundGoodsPrice;

	@ApiModelProperty(value = "상품쿠폰금액")
	private int goodsCouponPrice;

	@ApiModelProperty(value = "장바구니쿠폰금액")
	private int cartCouponPrice;

	@ApiModelProperty(value = "임직원할인금액")
	private int employeePrice;

	@ApiModelProperty(value = "주문시 부과 배송비")
	private int orderShippingPrice;

	@ApiModelProperty(value = "기 결제 추가 배송비")
	private int prevAddPaymentShippingPrice;

	@ApiModelProperty(value = "추가 배송비")
	private int addPaymentShippingPrice;

	@ApiModelProperty(value = "추가결제리스트")
	private List<OrderClaimAddPaymentShippingPriceDto> addPaymentList;

	@ApiModelProperty(value = "배송비")
	private int shippingPrice;

	@ApiModelProperty(value = "부분취소 남은 금액")
	private int remaindPrice;

	@ApiModelProperty(value = "환불금액")
	private int refundPrice;

	@ApiModelProperty(value = "CS환불금액")
	private int csRefundPrice;

	@ApiModelProperty(value = "포인트 남은금액")
	private int remainPointPrice;

	@ApiModelProperty(value = "환불적립금")
	private int refundPointPrice;

	@ApiModelProperty(value = "CS환불적립금")
	private int csRefundPointPrice;

	@ApiModelProperty(value = "추가결제 결제마스터 PK")
	private long odPaymentMasterId;

    @ApiModelProperty(value = "직접결제여부")
    private String directPaymentYn;

    @ApiModelProperty(value = "추가결제방법")
    private String addPaymentTp;

	@ApiModelProperty(value = "처리이력내용")
	private String histMsg;

	@ApiModelProperty(value = "주문클레임 환불계좌 PK")
	private long odClaimAccountId;

    @ApiModelProperty(value = "은행코드")
    private String bankCd;

    @ApiModelProperty(value = "예금주")
    private String accountHolder;

    @ApiModelProperty(value = "계좌번호")
    private String accountNumber;

	@ApiModelProperty(value = "결제타입 (G : 결제, F : 환불 , A : 추가)")
	private String type;

	@ApiModelProperty(value = "결제상태(IR:입금예정,IC:입금완료)")
	private String status;

	@ApiModelProperty(value = "결제타입 (G : 결제, F : 환불 , A : 추가)")
	private String reType;

	@ApiModelProperty(value = "과세결제금액")
	private int taxablePrice;

	@ApiModelProperty(value = "비과세결제금액")
	private int nonTaxablePrice;

	@ApiModelProperty(value = "결제타입")
	private String payTp;

	@ApiModelProperty(value = "PG서비스")
	private String pgService;

	@ApiModelProperty(value = "응답데이터")
	private String responseData;

	@ApiModelProperty(value = "보내는사람 우편번호")
	private String recvZipCd;

	//보내는 배송지 start; 현재는 이걸로 사용중 나중에 일괄적으로 삭제 예정
	@ApiModelProperty(value = "주문클레임 보내는 배송지 PK")
	private long odClaimSendShippingZoneId;

    @ApiModelProperty(value = "송화인명")
    private String sendRecvNm;

    @ApiModelProperty(value = "송화인핸드폰")
    private String sendRecvHp;

    @ApiModelProperty(value = "송화인핸드폰 첫번째 자릿수")
    private String sendRecvHp1;

    @ApiModelProperty(value = "송화인핸드폰 두번째 자릿수")
    private String sendRecvHp2;

    @ApiModelProperty(value = "송화인핸드폰 세번째 자릿수")
    private String sendRecvHp3;

    @ApiModelProperty(value = "송화인연락처")
    private String sendRecvTel;

    @ApiModelProperty(value = "송화인우편번호")
    private String sendRecvZipCd;

    @ApiModelProperty(value = "송화인주소1")
    private String sendRecvAddr1;

    @ApiModelProperty(value = "송화인주소2")
    private String sendRecvAddr2;

    @ApiModelProperty(value = "송화인 건물번호")
    private String sendRecvBldNo;

    @ApiModelProperty(value = "송화인 배송요청사항")
    private String sendDeliveryMsg;

    @ApiModelProperty(value = "송화인 출입정보타입 공통코드(DOOR_MSG_CD)")
    private String sendDoorMsgCd;

    @ApiModelProperty(value = "송화인 배송출입 현관 비밀번호")
    private String sendDoorMsg;
    //보내는 배송지 end; 삭제예정

    @ApiModelProperty(value = "택배사 PK")
    private long psShippingCompId;

    @ApiModelProperty(value = "개별송장번호")
    private String trackingNo;

    // 주문마스터 dto 넣을 예정 start  현재는 이걸로 사용중 나중에 일괄적으로 삭제 예정
	@ApiModelProperty(value = "주문번호")
	private String odid;

	@ApiModelProperty(value = "로그인ID")
	private String loginId;

    @ApiModelProperty(value = "MALL 회원 ID : UR_USER.UR_USER_ID")
    private String customUrUserId;

    @ApiModelProperty(value = "회원 ID : UR_USER.UR_USER_ID")
    private String urUserId;

    @ApiModelProperty(value = "임직원사번 : UR_EMPLOYEE.UR_EMPLOYEE_CD")
    private String urEmployeeCd;

    @ApiModelProperty(value = "비회원 CI")
    private String guestCi;

    @ApiModelProperty(value = "비회원 여부")
    private boolean nonMember = false;

	@ApiModelProperty(value = "주문자 명")
	private String buyerNm;

	@ApiModelProperty(value = "주문자 핸드폰")
	private String buyerHp;

	@ApiModelProperty(value = "주문자 이메일")
	private String buyerMail;
	// 주문마스터 dto 넣을 예정 end ------

	@ApiModelProperty(value = "보내는 배송지 정보 Dto")
    OrderClaimSendShippingZoneInfoDto sendShippingZoneInfo;

	@ApiModelProperty(value = "추가결제 카드 또는 계좌 정보")
	OrderClaimAddPaymentDto addPaymentInfo;

	@ApiModelProperty(value = "파일첨부 목록 문자열")
	private String attcString;

	@ApiModelProperty(value = "파일첨부 목록 리스트")
	private List<OrderClaimAttcInfoDto> attcInfoList;

	@ApiModelProperty(value = "받는배송지 목록 문자열")
	private String recvShippingString;

	@ApiModelProperty(value = "받는 배송지 리스트")
	private List<OrderClaimRecvShippingZoneInfoDto> recvShippingList;

	@ApiModelProperty(value = "상품정보목록 문자열")
	private String goodsString;

	@ApiModelProperty(value = "상품스케쥴정보 리스트")
	List<OrderClaimGoodsScheduleInfoDto> goodSchList;

	@ApiModelProperty(value = "상품정보 리스트")
	List<OrderClaimGoodsInfoDto> goodsInfoList;

	@ApiModelProperty(value = "상품쿠폰정보목록 문자열")
	private String goodsCouponString;

	@ApiModelProperty(value = "상품쿠폰정보목록 리스트")
	List<OrderClaimCouponInfoDto> goodsCouponInfoList;

	@ApiModelProperty(value = "장바구니쿠폰정보목록 문자열")
	private String cartCouponString;

	@ApiModelProperty(value = "장바구니쿠폰정보목록 리스트")
	List<OrderClaimCouponInfoDto> cartCouponInfoList;

	@ApiModelProperty(value="비인증결제 Dto")
	InicisNonAuthenticationCartPayResponseDto inicisNonAuthenticationCartPay;

	@ApiModelProperty(value="가상계좌발급 Dto")
	VirtualAccountDataResponseDto virtualAccountData;

	@ApiModelProperty(value = "배송비쿠폰 재발급 대상 목록")
	List<OrderClaimCouponInfoDto> deliveryCouponList;

	@ApiModelProperty(value = "추가결제요청정보PK")
	private long odAddPaymentReqInfoId;

	@ApiModelProperty(value = "에스크로 결제 여부")
	private String escrowYn;

	@ApiModelProperty(value = "거래ID")
	private String tid;

	@ApiModelProperty(value = "승인코드")
	private String authCode;

	@ApiModelProperty(value = "카드번호")
	private String cardNumber;

	@ApiModelProperty(value = "카드무이자구분")
	private String cardQuotaInterest;

	@ApiModelProperty(value = "카드할부기간")
	private String cardQuota;

	@ApiModelProperty(value = "가상계좌번호")
	private String virtualAccountNumber;

	@ApiModelProperty(value = "입금은행명")
	private String bankName;

	@ApiModelProperty(value = "결제정보")
	private String info;

	@ApiModelProperty(value = "입금기한")
	private LocalDateTime paidDueDate;

	@ApiModelProperty(value = "입금자명")
	private String paidHolder;

	@ApiModelProperty(value = "부분취소가능여부")
	private String partCancelYn;

	@ApiModelProperty(value = "철회 이전 상태")
	private String sourceStatusCd;

	@ApiModelProperty(value = "철회 이후 상태")
	private String targetStatusCd;

	@ApiModelProperty(value = "선미출여부")
	private String priorityUndelivered;

	@ApiModelProperty(value = "미출정보PK")
	private long ifUnreleasedInfoId;

	@ApiModelProperty(value = "녹즙 클레임 타입")
	private String claimType;

	@ApiModelProperty(value = "미출 클레임 처리여부")
	private String undeliveredClaimYn;

	@ApiModelProperty(value = "과세배송비")
	private int taxableOrderShippingPrice;

	@ApiModelProperty(value = "정기배송주문서생성 시 입금전취소여부")
	private String regularOrderIbFlag;
}

