package kr.co.pulmuone.v1.order.claim.dto;


import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.enums.OrderClaimEnums;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 클레임 상품조회 결과 Dto
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

@Getter
@Setter
@ToString
@ApiModel(description = "주문 클레임 상품조회 결과 Dto")
public class OrderClaimGoodsInfoDto {

	@ApiModelProperty(value = "행 순서")
	private long rowNum;

	@ApiModelProperty(value = "주문 pk")
	private long odOrderId;

	@ApiModelProperty(value = "주문상세 PK")
	private long odOrderDetlId;

	@ApiModelProperty(value = "주문상세 순번 주문번호에 대한 순번")
	private int odOrderDetlSeq;

	@ApiModelProperty(value = "주문상세 정렬 키")
	private long odOrderDetlStepId;

	@ApiModelProperty(value = "주문상세 뎁스")
	private long odOrderDetlDepthId;

	@ApiModelProperty(value = "주문상세 부모 ID")
	private long odOrderDetlParentId;

	@ApiModelProperty(value = "주문상태")
	private String orderStatusNm;

	@ApiModelProperty(value = "상품유형 IL_GOODS.GOODS_TP 공통코드(GOODS_TYPE) NORMAL: 일반, DISPOSAL: 폐기임박, GIFT: 증정, ADDITIONAL: 추가, PACKAGE: 묶음, DAILY: 일일, SHOP_ONLY: 매장, RENTAL: 렌탈, INCORPOREITY: 무형")
	private String goodsTpCd;

	@ApiModelProperty(value = "상품유형")
	private String goodsTpNm;

	@ApiModelProperty(value = "출고처ID")
	private long urWarehouseId;

	@ApiModelProperty(value = "출고처명")
	private String warehouseNm;

	@ApiModelProperty(value = "마스터품목코드")
	private String ilItemCd;

	@ApiModelProperty(value = "품목바코드")
	private String itemBarcode;

	@ApiModelProperty(value = "상품코드")
	private long ilGoodsId;

	@ApiModelProperty(value = "상품명")
	private String goodsNm;

	@ApiModelProperty(value = "상품배송타입 ")
	private String goodsDeliveryType;

	@ApiModelProperty("배송유형 공통코드(GOODS_DELIVERY_TYPE) NORMAL: 일반, DAWN: 새벽, SHOP: 매장, DAILY: 일일, NO_DELIVERY: 배송안함, RESERVATION: 예약")
	private String deliveryTypeCd;

	@ApiModelProperty(value = "상품보관방법코드 IL_ITEM.STORAGE_METHOD_TP 공통코드(ERP_STORAGE_TYPE) COOL: 냉장, FROZEN: 냉동, ROOM: 실온, ORDINARY: 상온, ETC: 기타, NOT_DEFINED: 미정")
	private String storageTypeCd;

	@ApiModelProperty(value = "상품보관방법명 ")
	private String storageTypeNm;

	@ApiModelProperty(value = "일일상품 유형(GOODS_DAILY_TP : GREENJUICE/BABYMEAL/EATSSLIM )")
	private String goodsDailyTp;

	@ApiModelProperty(value = "주문상태 배송유형 공통코드: ORDER_STATUS_DELI_TP")
	private String orderStatusDeliTp;

	@ApiModelProperty(value = "클레임수량")
	private int claimCnt;

	@ApiModelProperty(value = "주문수량")
	private int orderCnt;

	@ApiModelProperty(value = "실제상품주문수량")
	private int orgOrderCnt;

	@ApiModelProperty(value = "주문취소수량")
	private int cancelCnt;

	@ApiModelProperty(value = "정상가")
	private int recommendedPrice;

	@ApiModelProperty(value = "판매가")
	private int salePrice;

	@ApiModelProperty(value = "판매가총합")
	private int totSalePrice;

	@ApiModelProperty(value = "주문금액")
	private int orderPrice;

	@ApiModelProperty(value = "쿠폰할인")
	private int couponPrice;

	@ApiModelProperty(value = "장바구니쿠폰할인")
	private int cartCouponPrice;

	@ApiModelProperty(value = "상품쿠폰할인")
	private int goodsCouponPrice;

	@ApiModelProperty(value = "결제금액")
	private int paidPrice;

	@ApiModelProperty(value = "주문 클레임 상세 PK")
	private long odClaimDetlId;

    @ApiModelProperty(value = "BOS 클레임 사유 공급업체 PK")
    private int psClaimBosSupplyId;

    @ApiModelProperty(value = "BOS 클레임 사유 PK")
    private int psClaimBosId;

    @ApiModelProperty(value = "BOS 클레임 대분류 ID")
    private int bosClaimLargeId;

    @ApiModelProperty(value = "BOS 클레임 중분류 ID")
    private int bosClaimMiddleId;

    @ApiModelProperty(value = "BOS 클레임 소분류 ID")
    private int bosClaimSmallId;

    @ApiModelProperty(value = "주문 배송지 PK")
    private long odTrackingNumberId;

    @ApiModelProperty(value = "택배사 설정 PK")
    private long psShippingCompId;

    @ApiModelProperty(value = "추가결제배송비")
    private int addPaymentShippingPrice;

    @ApiModelProperty(value = "개별송장번호")
    private String trackingNo;

    @ApiModelProperty(value = "주문 I/F 일자")
    private String orderIfDt;

    @ApiModelProperty(value = "출고예정일 일자")
    private String shippingDt;

    @ApiModelProperty(value = "도착예정일 일자")
    private String deliveryDt;

	@ApiModelProperty(value = "도착예정일 일자 노출용")
	private String deliveryDtNm;

	@ApiModelProperty(value = "새벽배송여부")
	private Boolean isDawnDelivery;

	@ApiModelProperty(value = "일일 배송주기코드")
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

	@ApiModelProperty(value = "일괄배송 세트수량")
	private int setCnt;

	@ApiModelProperty(value = "알러지여부")
	private String allergyYn;

	@ApiModelProperty(value = "연동여부")
	private String scheduleYn;

	@ApiModelProperty(value = "주문 클레임 pk ")
	private long odClaimId;

	@ApiModelProperty(value = "주문 상태 odOrder에 있는 상태 : odStatus.statusCd")
	private String orderStatusCd;

	@ApiModelProperty(value = "클레임 주문상태 : odStatus.statusCd")
	private String claimStatusCd;

	@ApiModelProperty(value = "환불상태 : odStatus.statusCd")
	private String refundStatusCd;

	@ApiModelProperty(value = "주문 배송비 pk")
	private long odShippingPriceId;

	@ApiModelProperty(value = "주문 배송지 pk")
	private long odShippingZoneId;

	@ApiModelProperty(value = "배송비 정책 pk")
	private long ilGoodsShippingTemplateId;

	@ApiModelProperty(value = "원가")
	private int standardPrice;

	@ApiModelProperty(value = "상품,장바구니쿠폰 할인 제외한 할인금액")
	private int directPrice;

	@ApiModelProperty(value = "재배송구분 재배송 : R, 대체상품 : S")
	private String redeliveryType;

	@ApiModelProperty(value = "대표 상품 이미지")
	private String goodsImgNm;

	@ApiModelProperty(value = "배송비")
	private String deliveryPrice;

	@ApiModelProperty(value = "클레임수량 변경 상품 유무 Y :예, N : 아니오")
	private String claimGoodsYn;

	@ApiModelProperty(value = "공급업체 PK : IL_ITEM.UR_SUPPLIER_ID")
	private long urSupplierId;

	@ApiModelProperty(value = "배송비쿠폰발급PK")
	private long deliveryCouponIssueId;

	@ApiModelProperty(value = "배송비쿠폰명")
	private String pmCouponNm;

	@ApiModelProperty(value = "배송비쿠폰혜택정보")
	private String pmCouponBenefit;

	@ApiModelProperty(value = "배송비할인금액")
	private int shippingDiscountPrice;

    @ApiModelProperty(value = "취소완료 등록자")
    private long ccId;

    @ApiModelProperty(value = "배송비")
	private String shippingPrice;

	@ApiModelProperty(value = "배송정책명")
	private String ilShippingTmplNm;

	@ApiModelProperty(value = "배송정책 PK")
	private String ilShippingTmplId;

	@ApiModelProperty(value = "기획전 PK 녹즙골라담기")
	private String promotionTp;

	@ApiModelProperty(value = "추가상품 리스트")
	private List<OrderClaimGoodsInfoDto> addGoodsList;

	@ApiModelProperty(value = "묶음상품 리스트")
	private List<OrderClaimGoodsInfoDto> packageGoodsList;

	@ApiModelProperty(value = "재배송 리스트")
	private List<OrderClaimGoodsInfoDto> reDeliveryGoodsList;

	@ApiModelProperty(value = "일반 골라담기")
	private List<OrderClaimGoodsInfoDto> pickNormalList;

	@ApiModelProperty(value = "녹즙 골라담기 월요일")
	private List<OrderClaimGoodsInfoDto> pickMonList;

	@ApiModelProperty(value = "녹즙 골라담기 화요일")
	private List<OrderClaimGoodsInfoDto> pickTueList;

	@ApiModelProperty(value = "녹즙 골라담기 수요일")
	private List<OrderClaimGoodsInfoDto> pickWedList;

	@ApiModelProperty(value = "녹즙 골라담기 목요일")
	private List<OrderClaimGoodsInfoDto> pickThuList;

	@ApiModelProperty(value = "녹즙 골라담기 금요일")
	private List<OrderClaimGoodsInfoDto> pickFriList;

	@ApiModelProperty(value = "요일명")
	private String weekDayNm;

	@ApiModelProperty(value = "요일코드")
	private String weekDayCd;

	@ApiModelProperty(value = "BOS json")
	private String bosJson;

	@ApiModelProperty(value = "출고처별 배송정책 그룹핑 값")
	private String grpWarehouseShippingTmplId;

	@ApiModelProperty(value = "배송장소 코드")
	private String storeDeliveryTp;

	@ApiModelProperty(value = "조건배송비 구분")
	private String conditionTp;

	@ApiModelProperty(value = "조건배송비 조건 ( 주문금액별일경우 금액, 수량별일경우 수량 조건 )")
	private int conditionVal;

	@ApiModelProperty(value = "배송비 탬플릿 기본 배송비")
	private int templateShippingPrice;

	@ApiModelProperty(value = "배송주기 코드")
	private String goodsCycleTpCode;

	@ApiModelProperty(value = "배송기간 코드")
	private String goodsCycleTermTpCode;

	@ApiModelProperty(value = "과세여부")
	private String taxYn;

	@ApiModelProperty(value = "MALL 클레임 사유 PK")
	private  long psClaimMallId;

	@ApiModelProperty(value = "상세사유")
	private String claimReasonMsg;

	@ApiModelProperty(value = "주문자유형")
	private String buyerTypeCd;

	@ApiModelProperty(value = "재배송여부")
	private String redeliveryYn;

	@ApiModelProperty(value = "재배송상품 포함여부")
	private String redeliveryIncludeYn;

	@ApiModelProperty(value = "재배송 상품 대체상품 인덱스")
	private int redeliveryIndex;

	@ApiModelProperty(value = "클레임 등록 수")
	private int claimRegistCnt;

	@ApiModelProperty(value = "합배송여부")
	private String bundleYn;

	@ApiModelProperty(value = "미출수량")
	private int missCnt;

	/** 녹즙 스케쥴 일련번호 추가 */
	@ApiModelProperty(value= "주문 상세 일일배송 패턴 PK")
	private long odOrderDetlDailySchId;

	@ApiModelProperty(value = "일괄배송여부 Y:일괄배송")
	private String dailyBulkYn;

	@ApiModelProperty(value = "처리이력 메세지")
	private String histMsg;

	@ApiModelProperty(value = "BOS 클레임 대분류 명")
	private String bosClaimLargeNm;

	@ApiModelProperty(value = "BOS 클레임 중분류 명")
	private String bosClaimMiddleNm;

	@ApiModelProperty(value = "BOS 클레임 소분류 명")
	private String bosClaimSmallNm;

	@ApiModelProperty(value = "재배송 시 대체상품 할인비율")
	private int discountRate;
	
	@ApiModelProperty(value = "임직원 지원금")
	private int employeeDiscountPrice;

	@ApiModelProperty(value = "주문 상세 일일배송 스케쥴 라인번호")
	private String odOrderDetlDailySchSeq;

	@ApiModelProperty(value = "재고 증감처리용 주문여부")
	private String orderYn;

	@ApiModelProperty(value = "기획전PK")
	private long evExhibitId;

	@ApiModelProperty(value = "전시브랜드PK")
	private long dpBrandId;

	@ApiModelProperty(value = "증정품클레임처리요청여부 - Y : 클레임처리 요청, N : 클레임처리 요청제외")
	private String giftClaimReqYn = OrderClaimEnums.AllTypeYn.ALL_TYPE_Y.getCode();

	@ApiModelProperty(value = "회수여부")
	private String returnsYn;
}
