package kr.co.pulmuone.v1.order.order.dto.mall;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * Mall 주문상세 리스트 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 01. 20.	이규한		최초작성
 * =======================================================================
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "Mall 주문상세 리스트 Dto")
public class MallOrderDetailGoodsDto {

    @ApiModelProperty(value = "주문 PK")
    private long odOrderId;

	@ApiModelProperty(value = "주문상세 PK")
    private long odOrderDetlId;

	@ApiModelProperty(value = "주문클레임 PK")
    private long odClaimId;

    @ApiModelProperty(value = "주문상세 정렬값")
    private long odOrderDetlStepId;

    @ApiModelProperty(value = "주문상세 뎁스")
    private long odOrderDetlDepthId;

    @ApiModelProperty(value = "주문상세 부모값")
    private long odOrderDetlParentId;

    @ApiModelProperty(value = "주문상세 순번 주문번호에 대한 순번")
    private int odOrderDetlSeq;

	@ApiModelProperty(value = "품목코드PK")
	private String ilItemCd;

    @ApiModelProperty(value = "주문상태")
    private String orderStatusCd;

    @ApiModelProperty(value = "주문상태명")
    private String orderStatusNm;

    @ApiModelProperty(value = "상품유형 IL_GOODS.GOODS_TP 공통코드(GOODS_TYPE)")
    private String goodsTpCd;

    @ApiModelProperty(value = "주문상태 배송유형 공통코드: ORDER_STATUS_DELI_TP")
    private String orderStatusDeliTp;

    @ApiModelProperty(value = "배송유형 공통코드(GOODS_DELIVERY_TYPE)")
    private String goodsDeliveryType;

    @ApiModelProperty(value = "배송유형명")
    private String goodsDeliveryTypeNm;

    @ApiModelProperty(value = "배송유형 공통코드(GOODS_DELIVERY_TYPE), 그룹(일반/새벽)")
    private String grpGoodsDeliveryType;

    @ApiModelProperty(value = "상품 PK")
    private String ilGoodsId;

    @ApiModelProperty(value = "상품명 : IL_GOODS.GOODS_NM")
    private String goodsNm;

    @ApiModelProperty(value = "주문수량")
    private int orderCnt;

    @ApiModelProperty(value = "상품 정가 (*수량)")
	private int recommendedPriceMltplQty;

    @ApiModelProperty(value = "결제금액 (쿠폰까지 할인된 금액)")
    private int paidPrice;

    @ApiModelProperty(value = "도착 예정일")
    private String deliveryDt;

    @ApiModelProperty(value = "상태 JSON 데이터")
    private String frontJson;

    @ApiModelProperty(value = "액션 JSON 데이터")
    private String actionJson;

    @ApiModelProperty(value = "대표 상품 이미지")
    private String goodsImgNm;

    @ApiModelProperty(value = "송장번호")
    private String trackingNo;

    @ApiModelProperty(value = "택배사명")
    private String shippingCompNm;

    @ApiModelProperty(value = "배송추적 URL")
    private String trackingUrl;

    @ApiModelProperty(value = "HTTP 전송방법 공통코드(HTTP_REQUEST_TP : GET, POST)")
    private String httpRequestTp;

    @ApiModelProperty(value = "송장파라미터")
    private String invoiceParam;

    @ApiModelProperty(value = "택배사 코드")
    private String logisticsCd;

    @ApiModelProperty(value = "기획전 PK")
    private long evExhibitId;

    @ApiModelProperty(value = "기획전 PK 녹즙골라담기")
    private String promotionTp;

    @ApiModelProperty(value = "택배사 설정 PK")
    private long psShippingCompId;

    @ApiModelProperty(value = "출고처 PK")
    private long urWarehouseId;

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

    @ApiModelProperty(value = "새벽배송 가능여부")
    private String isDawnDelivery;

    @ApiModelProperty(value = "일반 - 일반/새벽구분")
    private String grpDeliveryDt;

    @ApiModelProperty(value = "추가상품 리스트")
    private List<MallOrderDetailGoodsDto> addGoodsList;

    @ApiModelProperty(value = "묶음상품 리스트")
    private List<MallOrderDetailGoodsDto> packageGoodsList;

    @ApiModelProperty(value = "재배송 리스트")
    private List<MallOrderDetailGoodsDto> reDeliveryGoodsList;

    @ApiModelProperty(value = "후기 작성 갯수")
    private int feedbackWriteCnt;

    @ApiModelProperty(value = "배송중 이후 날짜")
    private int feedbackWriteUseDay;

    @ApiModelProperty(value = "일반 골라담기")
    private List<MallOrderDetailGoodsDto> pickNormalList;

    @ApiModelProperty(value = "녹즙 골라담기 월요일")
    private List<MallOrderDetailGoodsDto> pickMonList;

    @ApiModelProperty(value = "녹즙 골라담기 화요일")
    private List<MallOrderDetailGoodsDto> pickTueList;

    @ApiModelProperty(value = "녹즙 골라담기 수요일")
    private List<MallOrderDetailGoodsDto> pickWedList;

    @ApiModelProperty(value = "녹즙 골라담기 목요일")
    private List<MallOrderDetailGoodsDto> pickThuList;

    @ApiModelProperty(value = "녹즙 골라담기 금요일")
    private List<MallOrderDetailGoodsDto> pickFriList;

    @ApiModelProperty(value = "요일명")
    private String weekDayNm;

    @ApiModelProperty(value = "배송비")
    private String shippingPrice;

    @ApiModelProperty(value = "배송정책명")
    private String ilShippingTmplNm;

    @ApiModelProperty(value = "배송정책 PK")
    private String ilShippingTmplId;

    @ApiModelProperty(value = "출고처별 배송정책 그룹핑 값")
    private String grpWarehouseShippingTmplId;

    @ApiModelProperty(value = "상품 리스트")
    List<MallOrderDetailGoodsDto> goodsDetailList;

    @ApiModelProperty(value = "배송주기 코드")
    private String goodsCycleTpCode;

    @ApiModelProperty(value = "배송기간 코드")
    private String goodsCycleTermTpCode;

    @ApiModelProperty(value = "배송장소 코드")
    private String storeDeliveryTp;

    @ApiModelProperty(value = "체험단 여부")
    private String experienceYn;

    @ApiModelProperty(value = "체험단 이벤트 PK")
    private Long evEventId;

    @ApiModelProperty(value = "주문타입(order:정상주문 claim:클레임주문)")
    private String orderType;

    @ApiModelProperty(value = "클레임 거부 사유")
    private String rejectReasonMsg;

    @ApiModelProperty(value = "묶음상품내 구성상품 상태 동일 여부 (Y: 동일, N: 동일하지 않음)")
    private String sameOrderStatusCdYn;

    @ApiModelProperty(value = "냉장 / 냉동상품 반품 가능 여부")
    private String returnYn;

    @ApiModelProperty(value = "Y: 택배사가 롯데이고 출고처그룹이 온라인사업부인 경우")
    private String lotteWarehouseGroupOwnYn;

    @ApiModelProperty(value = "클레임 total(취소요청,반품요청시 사용)")
    private int claimTotal;

    @ApiModelProperty(value = "취소요청 클레임 total")
    private int caClaimTotal;

    @ApiModelProperty(value = "반품요청 클레임 total")
    private int raClaimTotal;

    @ApiModelProperty(value = "주문배송지 PK")
    private long odShippingZoneId;

    @ApiModelProperty(value = "패키지상품일 경우, 첫번째 구성상품 주문상세PK")
    private long odOrderDetlChildId;

    @ApiModelProperty(value = "주문번호")
    private String odid;

    @ApiModelProperty(value = "렌탈상품일 경우, 해피콜 일자")
    private int callDate;

    @ApiModelProperty(value = "일괄배송 여부 Y:일괄배송")
    private String dailyBulkYn;

    @ApiModelProperty(value = "일괄배송 세트수량")
    private int setCnt;

    @ApiModelProperty(value = "회원그룹 PK")
    private Long urGroupId;

    @ApiModelProperty(value = "적립금 존재여부 (N:적립금 정보 없음)")
    private String existPointYn;

    @ApiModelProperty(value = "일반 후기 적립금")
    private Long normalAmount;

    @ApiModelProperty(value = "포토 후기 적립금")
    private Long photoAmount;

    @ApiModelProperty(value = "프리미엄 후기 적립금")
    private Long premiumAmount;

    @ApiModelProperty(value = "재배송구분 재배송 : R, 대체상품 : S")
    private String redeliveryType;

    @ApiModelProperty(value = "매장(배송/픽업)-주문배송시작시간")
    private String storeStartTime;

    @ApiModelProperty(value = "매장(배송/픽업)-주문배송종료시간")
    private String storeEndTime;

    @ApiModelProperty(value = "매장(배송/픽업)- 매장명")
    private String storeName;

    @ApiModelProperty(value = "건강기능식품 여부")
    private String healthGoodsYn;

    @ApiModelProperty(value = "배송정책 PK")
    private String odShippingPriceId;

    @ApiModelProperty(value = "정기배송결제실패건수")
    private int regularPaymentFailCnt;

    @ApiModelProperty(value = "택배사 전화번호")
    private String shippingCompTel;
}
