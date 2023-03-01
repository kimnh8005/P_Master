package kr.co.pulmuone.v1.order.email.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.order.order.dto.mall.MallOrderDetailGoodsDto;
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
@ApiModel(description = "자동메일 주문상세 리스트 Dto")
public class OrderDetailGoodsDto {

    @ApiModelProperty(value = "주문 PK")
    private long odOrderId;

	@ApiModelProperty(value = "주문상세 PK")
    private long odOrderDetlId;

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

    @ApiModelProperty(value = "결제금액 (쿠폰까지 할인된 금액)")
    private int paidPrice;

    @ApiModelProperty(value = "대표 상품 이미지")
    private String goodsImgNm;

    @ApiModelProperty(value = "추가상품 리스트")
    private List<MallOrderDetailGoodsDto> addGoodsList;

    @ApiModelProperty(value = "묶음상품 리스트")
    private List<MallOrderDetailGoodsDto> packageGoodsList;

    @ApiModelProperty(value = "재배송 리스트")
    private List<MallOrderDetailGoodsDto> reDeliveryGoodsList;

    @ApiModelProperty(value = "상품 정상가")
    private int recommendedPrice;

    @ApiModelProperty(value = "주문 배송비 템플릿 PK")
    private Long ilShippingTmplId;

    @ApiModelProperty(value = "주문 배송비 템플릿별 배송비")
    private int shippingPrice;

    /* 일일배송일 경우*/
	@ApiModelProperty(value = "배송요일")
	private String goodsDailyCycleTermDays;

	@ApiModelProperty(value = "배송기간")
	private String goodsDailyCycleTermType;

	@ApiModelProperty(value = "월요일 수량")
	private int monCnt;

	@ApiModelProperty(value = "화요일 수량")
	private int tueCnt;

	@ApiModelProperty(value = "수요일 수량")
	private int wedCnt;

	@ApiModelProperty(value = "목요일 수량")
	private int thuCnt;

	@ApiModelProperty(value = "금요일 수량")
	private int friCnt;

	/* 상품발송 */
	@ApiModelProperty(value = "송장번호")
	private String trackingNo;

	@ApiModelProperty(value = "택배사명")
	private String shippingCompName;

    @ApiModelProperty(value = "송장추적 URL")
    private String trackingUrl;

    @ApiModelProperty(value = "HTTP 전송방법")
    private String httpRequestTp;

    @ApiModelProperty(value = "송장파라미터")
    private String invoiceParam;

	@ApiModelProperty(value = "발송일자")
	private String diDate;

    @ApiModelProperty(value = "주문유형")
    private String agentTypeCd;

    @ApiModelProperty(value = "일일상품 베이비밀 일괄배송여부(Y:일괄배송)")
    private String dailyBulkYn;

	/* 정기배송 */
	@ApiModelProperty(value = "정기배송주문신청PK")
	private Long odRegularReqId;

	@ApiModelProperty(value = "정기배송 상품 판매가")
	private int salePriceByRegular;

    @ApiModelProperty(value = "정기배송 상품 정상가")
    private int recommendedPriceByRegular;
}