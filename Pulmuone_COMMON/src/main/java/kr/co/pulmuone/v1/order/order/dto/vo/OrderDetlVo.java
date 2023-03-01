package kr.co.pulmuone.v1.order.order.dto.vo;

import java.time.LocalDate;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 상세 관련 VO
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 15.            이명수         최초작성
 *  1.1	   2020. 01. 25.            김명진         날짜필드추가
 * =======================================================================
 * </PRE>
 */
@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "주문 상세 OD_ORDER_DETL VO")
public class OrderDetlVo {

	@ApiModelProperty(value = "주문 상세 PK")
    private long odOrderDetlId;

    @ApiModelProperty(value = "주문상세 순번")
    private long odOrderDetlSeq;

    @ApiModelProperty(value = "주문 PK")
    private long odOrderId;

    @ApiModelProperty(value = "주문번호")
    private String odid;

    @ApiModelProperty(value = "주문상세 정렬 키")
    private long odOrderDetlStepId;

    @ApiModelProperty(value = "주문상세 정렬 뎁스")
    private long odOrderDetlDepthId;

    @ApiModelProperty(value = "주문상세 부모 ID")
    private long odOrderDetlParentId;

    @ApiModelProperty(value = "주문 배송비 PK")
    private long odShippingPriceId;

    @ApiModelProperty(value = "주문 배송지 PK")
    private long odShippingZoneId;

    @ApiModelProperty(value = "배송비 정책 PK")
    private long ilGoodsShippingTemplateId;

    @ApiModelProperty(value = "출고처 PK")
    private long urWarehouseId;

    @ApiModelProperty(value = "공급업체 PK")
    private long urSupplierId;

    @ApiModelProperty(value = "예약정보 PK")
    private long ilGoodsReserveOptnId;

    @ApiModelProperty(value = "기획전 증정행사 PK")
    private long evExhibitId;

    @ApiModelProperty(value = "기획전 PK 녹즙골라담기")
    private String promotionTp;

    @ApiModelProperty(value = "정상주문상태")
    private String orderStatusCd;

    @ApiModelProperty(value = "수집몰 상세번호")
    private String collectionMallDetailId;

    @ApiModelProperty(value = "외부몰 상세번호")
    private String outmallDetailId;

    @ApiModelProperty(value = "출고처그룹")
    private String urWarehouseGrpCd;

    @ApiModelProperty(value = "상품보관방")
    private String storageTypeCd;

    @ApiModelProperty(value = "상품유형")
    private String goodsTpCd;

    @ApiModelProperty(value = "일일 상품 배송유형")
    private String goodsDailyTp;

    @ApiModelProperty(value = "주문배송유형")
    private String orderStatusDeliTp;

    @ApiModelProperty(value = "배송유형")
    private String goodsDeliveryType;

    @ApiModelProperty(value = "판매유형")
    private String saleTpCd;

    @ApiModelProperty(value = "표준 카테고리")
    private long ilCtgryStdId;

    @ApiModelProperty(value = "전시 카테고리")
    private long ilCtgryDisplayId;

    @ApiModelProperty(value = "몰인몰 카테고리")
    private long ilCtgryMallId;

    @ApiModelProperty(value = "품목 바코드")
    private String itemBarcode;

    @ApiModelProperty(value = "품목 코드 PK")
    private String ilItemCd;

    @ApiModelProperty(value = "상품 코드 PK")
    private long ilGoodsId;

    @ApiModelProperty(value = "상품명")
    private String goodsNm;

    @ApiModelProperty(value = "과세구분")
    private String taxYn;

    @ApiModelProperty(value = "할인유형")
    private String goodsDiscountTp;

    @ApiModelProperty(value = "이지어드민 상품관리번호")
    private int prdSeq;

    @ApiModelProperty(value = "주문수량")
    private int orderCnt;

    @ApiModelProperty(value = "주문취소수량")
    private int cancelCnt;

    @ApiModelProperty(value = "원가")
    private int standardPrice;

    @ApiModelProperty(value = "정상가")
    private int recommendedPrice;

    @ApiModelProperty(value = "판매가")
    private double salePrice;

    @ApiModelProperty(value = "총판매가")
    private int totSalePrice;

    @ApiModelProperty(value = "장바구니 쿠폰 할인금액 합")
    private int cartCouponPrice;

    @ApiModelProperty(value = "상품 쿠폰 할인금액")
    private int goodsCouponPrice;

    @ApiModelProperty(value = "즉시 할인금액 합")
    private int directPrice;

    @ApiModelProperty(value = "결제금액금액 합")
    private int paidPrice;

    @ApiModelProperty(value = "재배송구분")
    private String redeliveryType;

    @ApiModelProperty(value = "배송준비중등록자")
    private long drId;

    @ApiModelProperty(value = "배송준비중일자")
    private LocalDateTime drDt;

    @ApiModelProperty(value = "배송중등록자")
    private long diId;

    @ApiModelProperty(value = "배송중일자")
    private LocalDateTime diDt;

    @ApiModelProperty(value = "배송완료등록자")
    private long dcId;

    @ApiModelProperty(value = "배송완료일자")
    private LocalDateTime dcDt;

    @ApiModelProperty(value = "구매확정등록자")
    private long bfId;

    @ApiModelProperty(value = "구매확정일자")
    private LocalDateTime bfDt;

    @ApiModelProperty(value = "재배송등록자")
    private long ecId;

    @ApiModelProperty(value = "재배송일자")
    private LocalDateTime ecDt;

    @ApiModelProperty(value = "주문 I/F 등록자")
    private long orderIfId;

    @ApiModelProperty(value = "주문 I/F 일자")
    private LocalDate orderIfDt;

    @ApiModelProperty(value = "출고예정일등록자")
    private long shippingId;

    @ApiModelProperty(value = "출고예정일일자")
    private LocalDate shippingDt;

    @ApiModelProperty(value = "도착예정일등록자")
    private long deliveryId;

    @ApiModelProperty(value = "도착예정일일자")
    private LocalDate deliveryDt;

    @ApiModelProperty(value = "배치실행여부")
    private String batchExecFl;

    @ApiModelProperty(value = "배치실행일자")
    private LocalDate batchExecDt;

    @ApiModelProperty(value = "등록일자 YYYYMMDD")
    private String createDt;

    @ApiModelProperty(value = "외부몰타입")
    private String outmallType;

    @ApiModelProperty(value = "외부광고코드PK")
	private String pmAdExternalCd;

    @ApiModelProperty(value = "내부광고코드-페이지코드")
	private String pmAdInternalPageCd;

    @ApiModelProperty(value = "내부광고코드-영역코드")
	private String pmAdInternalContentCd;

    @ApiModelProperty(value = "엑셀 업로드 성공 정보 PK")
    private String ifOutmallExcelSuccId;

    @ApiModelProperty(value = "실패사유")
    private String failMessage;

    @ApiModelProperty(value = "매출연동여부")
    private String salesExecFl;

    @ApiModelProperty(value = "매출연동일자")
    private LocalDate salesExecDt;
}


