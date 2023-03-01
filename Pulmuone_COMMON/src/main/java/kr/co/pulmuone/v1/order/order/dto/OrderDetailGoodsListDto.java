package kr.co.pulmuone.v1.order.order.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingLoginId;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingUserName;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingUserNameLoginId;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.order.order.dto.mall.MallOrderDetailGoodsDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 상세 상품 리스트 관련 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 01. 05.            이명수         최초작성
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "주문 상세 상품 리스트 관련 Dto")
public class OrderDetailGoodsListDto {

    @ApiModelProperty(value = "주문 상세 pk")
    private int odOrderDetlId;

    @ApiModelProperty(value = "주문 상세 순번")
    private int odOrderDetlSeq;

    @ApiModelProperty(value = "주문 상세 뎁스")
    private int odOrderDetlDepthId;

    @ApiModelProperty(value = "주문상세 부모 ID")
    private long odOrderDetlParentId;

    @ApiModelProperty(value = "배치실행여부")
    private String batchExecFl;

    @ApiModelProperty(value = "마스터품목코드")
    private String ilItemCd;

    @ApiModelProperty(value = "품목바코드")
    private String itemBarcode;

    @ApiModelProperty(value = "상품코드")
    private long ilGoodsId;

    @ApiModelProperty(value = "상품명")
    private String goodsNm;

    @ApiModelProperty(value = "보관방법")
    private String storageType;

    @ApiModelProperty(value = "주문 상태")
    private String orderStatus;

    @ApiModelProperty(value = "주문 상태 코드")
    private String orderStatusCode;

    @ApiModelProperty(value = "수량")
    private int orderCnt;

    @ApiModelProperty(value = "클레임 수량")
    private int cancelCnt;

    @ApiModelProperty(value = "클레임가능 수량")
    private int claimAbleCnt;

    @ApiModelProperty(value = "주문상세번호별 클레임상태")
    private String claimDetlStatusNm;

    @ApiModelProperty(value = "주문I/F")
    private String orderIfDt;

    @ApiModelProperty(value = "출고예정일")
    private String shippingDt;

    @ApiModelProperty(value = "도착예정일")
    private String deliveryDt;

    @ApiModelProperty(value = "상품유형")
    private String goodsTpCd;

    @ApiModelProperty(value = "상품유형명")
    private String goodsTpCdNm;

    @ApiModelProperty(value = "출고처명")
    private String warehouseNm;

	@ApiModelProperty(value = "출고처ID(출고처PK)")
	private Long urWarehouseId;

    @ApiModelProperty(value = "송장번호")
    private String trackingNo;

    @ApiModelProperty(value = "택배사")
    private String shippingCompNm;

    @ApiModelProperty(value = "배송번호")
    private Long odShippingZoneId;

    @ApiModelProperty(value = "배송비")
    private int shippingPrice;

    @ApiModelProperty(value = "배송적책명")
    private String shippingNm;

    @ApiModelProperty(value = "결제금액(쿠폰까지 할인된 금액)")
    private int paidPrice;

    @ApiModelProperty(value = "판매가")
    private int salePrice;

    @ApiModelProperty(value = "총상품금액 : 판매가 * 수량")
    private int totalGoodsPrice;

    @ApiModelProperty(value = "쿠폰할인금액")
    private int couponPrice;

    @ApiModelProperty(value = "상품별 할인금액")
    private int discountPrice;

    @ApiModelProperty(value = "수집몰 주문상세번호")
    private String collectionMailDetlId;

    @ApiModelProperty(value = "외부몰 주문상세번호")
    private String outmallDetlId;

    @ApiModelProperty(value = "트래킹 추적 가능여부 (대한통운, 롯데), 그외 자체 추적")
    private String trackingYn;

    @ApiModelProperty(value = "송장추적 URL")
    private String trackingUrl;

    @ApiModelProperty(value = "HTTP 전송방법")
    private String httpRequestTp;

    @ApiModelProperty(value = "송장파라미터")
    private String invoiceParam;

    @ApiModelProperty(value = "택배사PK")
    private Long psShippingCompId;

    @ApiModelProperty(value = "I/F일자 변경가능여부 json")
    private String actionJson;

    @ApiModelProperty(value = "BOS json")
    private String bosJson;

    //클레임정보 > 클레임상품정보
    @ApiModelProperty(value = "클레임 신청번호")
    private long odClaimId;

    @ApiModelProperty(value = "클레임 신청 상세 번호")
    private long odClaimDetlId;

    @ApiModelProperty(value = "클레임 첨부파일 여부")
    private String odClaimAttcIdExist;

    @ApiModelProperty(value = "클레임 요청자")
    @UserMaskingUserNameLoginId
    private String claimRequester;

    @ApiModelProperty(value = "회원구분 : BUYER / EMPLOYEE")
    private String userTp;

    @ApiModelProperty(value = "관리자명")
    @UserMaskingUserName
    private String userNm;

    @ApiModelProperty(value = "관리자 아이디")
    @UserMaskingLoginId
    private String loginId;

    @ApiModelProperty(value = "클레임사유코드")
    private String claimReasonCd;

    @ApiModelProperty(value = "MALL 클레임 사유 PK")
   	private Long psClaimMallId;

    @ApiModelProperty(value = "클레임 사유 메세지")
    private String reasonMsg;

    @ApiModelProperty(value = "클레임 사유(대)")
    private String lclaimCtgryId;

    @ApiModelProperty(value = "클레임 사유(중)")
    private String mclaimCtgryId;

    @ApiModelProperty(value = "클레임 사유(소)")
    private String sclaimCtgryId;

    @ApiModelProperty(value = "클레임 상세 사유")
    private String claimReasonMsg;

    @ApiModelProperty(value = "클레임 요청일")
    private String claimDt;

    @ApiModelProperty(value = "클레임 승인일")
    private String claimApprovalDt;

    @ApiModelProperty(value = "클레임 상태코드")
    private String claimStatusCode;

    @ApiModelProperty(value = "클레임 상태명")
    private String claimStatus;

    @ApiModelProperty(value = "기획전 PK")
    private long evExhibitId;

    @ApiModelProperty(value = "기획전 PK 녹즙골라담기")
    private String promotionTp;

    @ApiModelProperty(value = "배송주기코드")
    private String goodsCycleTpCd;

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
    
    @ApiModelProperty(value = "세트수량 : 일괄배송해당")
    private String setCnt;
    
    @ApiModelProperty(value = "일괄배송여부")
    private String dailyBulkYn;

    @ApiModelProperty(value = "알러지여부")
    private String allergyYn;

    @ApiModelProperty(value = "연동여부")
    private String scheduleYn;

    @ApiModelProperty(value = "일일배송타입")
    private String goodsDailyTp;

    @ApiModelProperty(value = "새벽배송 가능여부")
    private String isDawnDelivery;

    @ApiModelProperty(value = "일반 - 일반/새벽구분")
    private String grpDeliveryDt;

    @ApiModelProperty(value = "귀책구분 B: 구매자, S: 판매자")
    private String targetTp;

    @ApiModelProperty(value = "BOS 클레임 대분류명")
    private String bosClaimLargeNm;

    @ApiModelProperty(value = "BOS 클레임 중분류명")
    private String bosClaimMiddleNm;

    @ApiModelProperty(value = "BOS 클레임 소분류명")
    private String bosClaimSmallNm;

    @ApiModelProperty(value = "erpIfYn")
    private String erpIfYn;

    @ApiModelProperty(value = "itemTp")
    private String itemTp;

    @ApiModelProperty(value = "적립금 환불")
    private int refundPointPrice;

    @ApiModelProperty(value = "결제수단 환불")
    private int refundPrice;

    @ApiModelProperty(value = "현제 클레임가능 수량")
    private int orgClaimAbleCnt;

    @ApiModelProperty(value = "배송정책 PK")
    private String ilShippingTmplId;

    @ApiModelProperty(value = "택배사코드")
    private String logisticsCd;

    @ApiModelProperty(value = "회수여부")
    private String returnsYn;

    @ApiModelProperty(value = "배송유형 : ORDER_STATUS_DELI_TP")
    private String orderStatusDeliTp;

    @ApiModelProperty(value = "합배송 여부 : Y 합배송, N 합배송제외")
    private String bundleYn;

    @ApiModelProperty(value = "수집몰 주문번호")
    private String collectionMallId;

    @ApiModelProperty(value = "외부몰 주문번호")
    private String outMallId;

    @ApiModelProperty(value = "매장명")
    private String urStoreNm;

    @ApiModelProperty(value = "배송유형")
    private String goodsDeliveryType;

    @ApiModelProperty(value = "스케쥴 배송완료 건 수")
    private int deliveryCnt;

    @ApiModelProperty(value = "정상가")
    private int recommendedPrice;

    @ApiModelProperty(value = "임직원 할인 금액")
    private int discountEmployeePrice;

    @ApiModelProperty(value = "즉시할인 금액")
    private int directPrice;

    @ApiModelProperty(value = "배송유형 : ORDER_STATUS_DELI_TP 코드")
    private String orderStatusDeliTpCd;

    @ApiModelProperty(value = "주문 BosJson")
    private String orderBosJson;

    @ApiModelProperty(value = "클레임 BosJson")
    private String claimBosJson;

    @ApiModelProperty(value = "미출수량")
    private String missCnt;

    @ApiModelProperty(value = "송장등록일시")
    private String sendEndDt;

    @ApiModelProperty(value = "출고처,배송정책 그룹 ID")
    private String grpShippingId;

    @ApiModelProperty(value = "판매타입")
    private String saleType;

}
