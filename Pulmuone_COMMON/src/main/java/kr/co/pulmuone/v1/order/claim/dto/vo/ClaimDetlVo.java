package kr.co.pulmuone.v1.order.claim.dto.vo;

import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 클레임 상세 관련 VO
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
@ApiModel(description = "주문클레임 상세 OD_CLAIM_DETL VO")
public class ClaimDetlVo {

    @ApiModelProperty(value = "주문클레임 상세 PK")
    private long odClaimDetlId;

    @ApiModelProperty(value = "주문클레임 PK")
    private long odClaimId;

    @ApiModelProperty(value = "주문상세 PK")
    private long odOrderDetlId;

    @ApiModelProperty(value = "클레임처리수량")
    private int claimCnt;

    @ApiModelProperty(value = "주문상태")
    private String orderStatusCd;

    @ApiModelProperty(value = "클레임상태")
    private String claimStatusCd;

    @ApiModelProperty(value = "판매가 : IL_GOODS.SALE_PRICE'")
    private int salePrice;

    @ApiModelProperty(value = "판매가총합")
    private int totSalePrice;

    @ApiModelProperty(value = "장바구니쿠폰할인금액")
    private int cartCouponPrice;

    @ApiModelProperty(value = "상품쿠폰할인금액")
    private int goodsCouponPrice;

    @ApiModelProperty(value = "상품,장바구니쿠폰 할인 제외한 할인금액")
    private int directPrice;

    @ApiModelProperty(value = "결제금액 (쿠폰까지 할인된 금액)")
    private int paidPrice;

    @ApiModelProperty(value = "재배송구분 재배송 : R, 대체상품 : S")
    private String redeliveryType;

    @ApiModelProperty(value = "환불상태")
    private String refundStatusCd;

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

    @ApiModelProperty(value = "배송정책PK")
    private long ilGoodsShippingTemplateId;

    @ApiModelProperty(value = "출고처PK")
    private long urWarehouseId;

    @ApiModelProperty(value = "추가결제배송비")
    private int addPaymentShippingPrice;

    @ApiModelProperty(value = "취소요청 등록자")
    private long caId;

    @ApiModelProperty(value = "취소요청 일자")
    private LocalDateTime caDt;

    @ApiModelProperty(value = "취소철회 등록자")
    private long cwId;

    @ApiModelProperty(value = "취소철회 일자")
    private LocalDateTime cwDt;

    @ApiModelProperty(value = "취소완료 등록자")
    private long ccId;

    @ApiModelProperty(value = "취소완료 일자")
    private LocalDateTime ccDt;

    @ApiModelProperty(value = "반품요청 등록자")
    private long raId;

    @ApiModelProperty(value = "반품요청 일자")
    private LocalDateTime raDt;

    @ApiModelProperty(value = "반품철회 등록자")
    private long rwId;

    @ApiModelProperty(value = "반품철회 일자")
    private LocalDateTime rwDt;

    @ApiModelProperty(value = "반품승인 등록자")
    private long riId;

    @ApiModelProperty(value = "반품승인 일자")
    private LocalDateTime riDt;

    @ApiModelProperty(value = "반품보류 등록자")
    private long rfId;

    @ApiModelProperty(value = "반품보류 일자")
    private LocalDateTime rfDt;

    @ApiModelProperty(value = "반품완료 등록자")
    private long rcId;

    @ApiModelProperty(value = "반품완료 일자")
    private LocalDateTime rcDt;

    @ApiModelProperty(value = "재배송 등록자")
    private long ecId;

    @ApiModelProperty(value = "재배송 일자")
    private LocalDateTime ecDt;

    @ApiModelProperty(value = "CS환불 등록자")
    private long csId;

    @ApiModelProperty(value = "CS환불 일자")
    private LocalDateTime csDt;

    @ApiModelProperty(value = "환불요청 등록자")
    private long faId;

    @ApiModelProperty(value = "환불요청 일자")
    private LocalDateTime faDt;

    @ApiModelProperty(value = "환불완료 등록자")
    private long fcId;

    @ApiModelProperty(value = "환불완료 일자")
    private LocalDateTime fcDt;

    @ApiModelProperty(value = "클레임요청 등록자")
    private long crId;

    @ApiModelProperty(value = "클레임요청 일자")
    private LocalDateTime crDt;

    @ApiModelProperty(value = "클레임승인 등록자")
    private long ceId;

    @ApiModelProperty(value = "클레임승인 일자")
    private LocalDateTime ceDt;

    @ApiModelProperty(value = "주문 I/F 등록자")
    private long orderIfId;

    @ApiModelProperty(value = "주문 I/F 일자")
    private LocalDateTime orderIfDt;

    @ApiModelProperty(value = "출고예정일 등록자")
    private long shippingId;

    @ApiModelProperty(value = "출고예정일 일자")
    private LocalDateTime shippingDt;

    @ApiModelProperty(value = "도착예정일 등록자")
    private long deliveryId;

    @ApiModelProperty(value = "도착예정일 일자")
    private LocalDateTime deliveryDt;

    @ApiModelProperty(value = "선미출여부")
    private String priorityUndelivered;

    @ApiModelProperty(value = "클레임 사용여부(거부,철회시 'N')")
    private String claimYn;

    @ApiModelProperty(value = "반품매출연동여부")
    private String returnSalesExecFl;

    @ApiModelProperty(value = "미출정보PK")
    private long ifUnreleasedInfoId;

    @ApiModelProperty(value = "반품접수 연동 성공여부")
    private String recallType;
}
