package kr.co.pulmuone.v1.order.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.aop.service.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "OrderListDto")
public class OrderDetailListDto {


    @ApiModelProperty(value = "주문 PK")
    private String odOrderId;

    @ApiModelProperty(value = "주문 상세 SEQ")
    private String odOrderDetlSeq;

    @ApiModelProperty(value = "주문 일자")
    private String createDt;

	@ApiModelProperty(value = "주문 번호")
    private String odid;

    @ApiModelProperty(value = "수집몰 주문번호")
    private String collectionMallId;

    @ApiModelProperty(value = "외부몰주문번호(이지어드민 ORDER_ID)")
    private String outMallId;

    @ApiModelProperty(value = "회원 그룹 ID")
    private Long urGroupId;

    @ApiModelProperty(value = "회원 그룹명")
    private String urGroupNm;

    @ApiModelProperty(value = "회원 ID")
    private Long urUserId;

    @ApiModelProperty(value = "로그인 ID")
    @UserMaskingLoginId
    private String loginId;

    @ApiModelProperty(value = "임직원사번")
    private String urEmployeeCd;

    @ApiModelProperty(value = "비회원 CI")
    private String guestCi;

    @ApiModelProperty(value = "주문자 명")
    @UserMaskingUserName
    private String buyerNm;

    @ApiModelProperty(value = "주문자 핸드폰")
    @UserMaskingMobile
    private String buyerHp;

    @ApiModelProperty(value = "주문자 연락처")
    @UserMaskingTelePhone
    private String buyerTel;

    @ApiModelProperty(value = "주문자 이메일")
    @UserMaskingEmail
    private String buyerMail;

    @ApiModelProperty(value = "상품명")
    private String goodsNm;

    @ApiModelProperty(value = "결제수단")
    private String orderPaymentType;

    @ApiModelProperty(value = "주문자 유형")
    private String buyerTypeCd;

    @ApiModelProperty(value = "주문 유형")
    private String agentType;

    @ApiModelProperty(value = "주문경로 ERP I/F 주문경로 필요")
    private String orderHpnCd;

    @ApiModelProperty(value = "일반주문: N, 선물하기주문: Y")
    private String giftYn;

    @ApiModelProperty(value = "주문상태코드")
    private String statusCd;

    @ApiModelProperty(value = "사용자환경정보")
    private String urPcidCd;

    @ApiModelProperty(value = "주문금액")
    private int orderPrice;

    @ApiModelProperty(value = "베송비합계")
    private int shippingPrice;

    @ApiModelProperty(value = "쿠폰할인합계")
    private int couponPrice;

    @ApiModelProperty(value = "결제금액, 주문상세리트 주문금액")
    private int paidPrice;

    @ApiModelProperty(value = "주문상세 PK")
    private Long odOrderDetlId;

    @ApiModelProperty(value = "정상주문상태")
    private String orderStatus;

    @ApiModelProperty(value = "주문 I/F 일자")
    private String orderIfDt;

    @ApiModelProperty(value = "출고예정일 일자")
    private String shippingDt;

    @ApiModelProperty(value = "도착예정일 일자")
    private String deliveryDt;

    @ApiModelProperty(value = "배송유형")
    private String deliveryTypeNm;

    @ApiModelProperty(value = "상품유형")
    private String goodsTp;

    @ApiModelProperty(value = "출고처이름")
    private String warehouseNm;

    @ApiModelProperty(value = "주문마감시간")
    private String cutoffTime;

    @ApiModelProperty(value = "마스터품목코드")
    private String ilItemCd;

    @ApiModelProperty(value = "품목바코드")
    private String itemBarcode;

    @ApiModelProperty(value = "상품 PK")
    private Long ilGoodsId;

    @ApiModelProperty(value = "할인유형")
    private String goodsDiscountTp;

    @ApiModelProperty(value = "할인유형명")
    private String goodsDiscountTpNm;

    @ApiModelProperty(value = "상품보관방법")
    private String storageType;

    @ApiModelProperty(value = "주문수량")
    private int orderCnt;

    @ApiModelProperty(value = "취소수량")
    private int cancelCnt;

    @ApiModelProperty(value = "배송번호")
    private Long odShippingZoneId;

    @ApiModelProperty(value = "송장번호")
    private String trackingNo;

    @ApiModelProperty(value = "택배사 PK")
    private String psShippingCompId;

    @ApiModelProperty(value = "택배사명")
    private String shippingCompNm;

    @ApiModelProperty(value = "정상가")
    private int recommendedPrice;

    @ApiModelProperty(value = "판매가")
    private int salePrice;

    @ApiModelProperty(value = "배송준비중 일자")
    private String drDt;

    @ApiModelProperty(value = "배송중 일자")
    private String diDt;

    @ApiModelProperty(value = "클레임 요청일자")
    private String crDt;

    @ApiModelProperty(value = "클레임PK")
    private String odClaimId;

    @ApiModelProperty(value = "클레임번호")
    private String odClaimDetlId;

    @ApiModelProperty(value = "클레임 승인일자")
    private String ceDt;

    @ApiModelProperty(value = "클레임상태")
    private String orderClaimStatus;

    @ApiModelProperty(value = "마지막클레임상태")
    private String lastOrderClaimStatus;

    @ApiModelProperty(value = "환불상태")
    private String refundStatusCd;

    @ApiModelProperty(value = "취소 요청일자")
    private String caDt;

    @ApiModelProperty(value = "결제완료 일자")
    private String icDt;

    @ApiModelProperty(value = "수취인명")
    @UserMaskingUserName
    private String recvNm;

    @ApiModelProperty(value = "수취인 핸드폰 번호")
    @UserMaskingUserName
    private String recvHp;

    @ApiModelProperty(value = "원가")
    private int standardPrice;

    @ApiModelProperty(value = "판매처")
    private String sellersGroupCdNm;

    @ApiModelProperty(value = "결제방법")
    private String payTp;

    @ApiModelProperty(value = "클레임사유")
    private String claimReasonCdNm;

    @ApiModelProperty(value = "클레임상세사유")
    private String claimReasonMsg;

    @ApiModelProperty(value = "MALL 클레임 사유 PK")
    private Long psClaimMallId;

    @ApiModelProperty(value = "MALL 클레임 사유")
    private String psClaimMallMsg;

    @ApiModelProperty(value = "반품회수여부")
    private String returnsYn;

    @ApiModelProperty(value = "환불금액")
    private int refundPrice;

    @ApiModelProperty(value = "첨부파일등록수")
    private int claimAttcCnt;

    @ApiModelProperty(value = "판매처명")
    private String omSellersNm;

    @ApiModelProperty(value = "상품 타입")
    private String goodsTpCd;

    @ApiModelProperty(value = "기획전 PK")
    private long evExhibitId;

    @ApiModelProperty(value = "기획전 PK 녹즙골라담기")
    private String promotionTp;

    @ApiModelProperty(value = "배송주기")
    private String goodsCycleTp;

    @ApiModelProperty(value = "배송기간")
    private String goodsCycleTermTp;

    @ApiModelProperty(value = "월요일 수량")
    private String monCnt;

    @ApiModelProperty(value = "화요일 수량")
    private String tueCnt;

    @ApiModelProperty(value = "수요일 수량")
    private String wedCnt;

    @ApiModelProperty(value = "목요일 수량")
    private String thuCnt;

    @ApiModelProperty(value = "금요일 수량")
    private String friCnt;

    @ApiModelProperty(value = "알러지여부")
    private String allergyYn;

    @ApiModelProperty(value = "연동여부")
    private String scheduleYn;

    @ApiModelProperty(value = "일일배송타입")
    private String goodsDailyTp;

    @ApiModelProperty(value = "클레임 주문상태 코드 : OD_STATUS.STATUS_CD")
    private String claimStatusCd;

    @ApiModelProperty(value = "마지막 클레임 주문상태 코드 : OD_STATUS.STATUS_CD")
    private String lastClaimStatusCd;

    @ApiModelProperty(value = "출고처 PK : UR_WAREHOUSE.UR_WAREHOUSE_ID")
    private Long urWarehouseId;

    @ApiModelProperty(value = "클레임 상품금액")
    private int claimGoodsPrice;

    @ApiModelProperty(value = "클레임 상품쿠폰 금액")
    private int claimGoodsCouponPrice;

    @ApiModelProperty(value = "클레임 장바구니쿠폰금액")
    private int claimCartCouponPrice;

    @ApiModelProperty(value = "클레임 배송비")
    private int claimShippingPrice;

	@ApiModelProperty(value = "BOS 클레임 사유 PK")
	private Long psClaimBosId;

    @ApiModelProperty(value = "BOS 클레임 사유 공급업체 PK")
    private Long psClaimBosSupplyId;

    @ApiModelProperty(value = "반품접수 연동 성공여부")
    private String recallType;

	@ApiModelProperty(value = "클레임 사유(대) 사유명")
    private String lClaimName;

	@ApiModelProperty(value = "클레임 사유(중) 사유명")
    private String mClaimName;

	@ApiModelProperty(value = "귀책처 사유명")
    private String sClaimName;

	@ApiModelProperty(value = "귀책구분")
    private String targetTp;

	@ApiModelProperty(value = "공급업체명")
    private String compNm;

	@ApiModelProperty(value = "반품회수여부에 따른 클레임사유")
    private String claimName;

	@ApiModelProperty(value = "CS환불구분 따른 클레임사유")
    private String csRefundTp;

	@ApiModelProperty(value = "CS환불승인상태코드")
    private String csRefundApproveCd;

	@ApiModelProperty(value = "CS환불승인상태명")
    private String csRefundApproveNm;

	@ApiModelProperty(value = "환불수단  D: 원결제 내역, C : 무통장입금")
    private String refundType;

	@ApiModelProperty(value = "환불 은행명")
    private String refundBankNm;

	@ApiModelProperty(value = "환불 예금주")
    private String refundAccountHolder;

	@ApiModelProperty(value = "환불 계좌번호")
    @UserMaskingAccountNumber
    private String refundAccountNumber;

    @ApiModelProperty(value = "클레임등록자 ID")
    private Long claimCreateId;

    @ApiModelProperty(value = "클레임환불 완료여부")
    private String claimRefundYn;

    @ApiModelProperty(value = "배송정보(송장번호,택배사 등)")
    private String shippingInfo;

	@ApiModelProperty(value = "미출일자")
    private String missDt;

	@ApiModelProperty(value = "미출 사유")
    private String missReason;

    @ApiModelProperty(value = "미출 상세사유")
    private String missMsg;

    @ApiModelProperty(value = "클레임상태구분")
    private String claimStatusTp;

    @ApiModelProperty(value = "클레임상태구분명")
    private String claimStatusTpNm;

    @ApiModelProperty(value = "미출정보 PK")
    private Long ifUnreleasedInfoId;

    @ApiModelProperty(value = "품목유형")
    private String itemTp;

    @ApiModelProperty(value = "품목 ERP 연동여부")
    private String erpIfYn;

    @ApiModelProperty(value = "세트수량 : 일괄배송해당")
    private int setCnt;

    @ApiModelProperty(value = "일괄배송여부")
    private String dailyBulkYn;

    @ApiModelProperty(value = "BOS JSON")
    private String bosJson;

    @ApiModelProperty(value = "미출수량")
    private int missCnt;

    @ApiModelProperty(value = "클레임수량")
    private int claimCnt;

    @ApiModelProperty(value = "선미출클레임수량")
    private int missClaimCnt;

    @ApiModelProperty(value = "미출정보PK")
    private long missIfUnreleasedInfoId;

    @ApiModelProperty(value = "미출 처리여부")
    private String missProcessYn;

    @ApiModelProperty(value = "미출수량(선미출수량)")
    private String returnMissCnt;

    @ApiModelProperty(value = "잔여처리수량")
    private int processMissCnt;

    @ApiModelProperty(value = "보스클레임사유")
    private String bosClaimNm;

    @ApiModelProperty(value = "클레임가능 수량")
    private int claimAbleCnt;
    
    @ApiModelProperty(value = "주문자 유형명")
    private String buyerTypeCdNm;
    
    @ApiModelProperty(value = "배송유형명 : ORDER_STATUS_DELI_TP")
    private String orderStatusDeliTpNm;

    @ApiModelProperty(value = "매장명")
    private String urStoreNm;

    @ApiModelProperty(value = "임직원 할인")
    private int discountEmployeePrice;

    @ApiModelProperty(value = "즉시 할인")
    private int directPrice;

    @ApiModelProperty(value = "스케줄 도착예정일")
    private String schDeliveryDt;

    @ApiModelProperty(value = "주문 상세 일일배송 스케쥴 라인번호")
    private long odOrderDetlDailySchSeq;

    @ApiModelProperty(value = "공급업체명")
    private String urSupplierNm;

    @ApiModelProperty(value = "rowNumber")
    private int rowNumber;

    @ApiModelProperty(value = "주문 BosJson")
    private String orderBosJson;

    @ApiModelProperty(value = "클레임 BosJson")
    private String claimBosJson;

    @ApiModelProperty(value = "취소완료 일자")
    private String ccDt;
    
    @ApiModelProperty(value = "반품완료 일자")
    private String rcDt;

    @ApiModelProperty(value = "녹즙, 내 맘대로 주문")
    private String selectGreenjuice;
    
}
