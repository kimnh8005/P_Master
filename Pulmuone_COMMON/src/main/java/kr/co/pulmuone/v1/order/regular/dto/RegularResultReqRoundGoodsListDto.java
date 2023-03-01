package kr.co.pulmuone.v1.order.regular.dto;

import java.time.LocalDate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 정기배송 주문 상품 리스트 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 02. 09.	김명진		최초작성
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "정기배송 주문 상품 리스트 Dto")
public class RegularResultReqRoundGoodsListDto {

	@ApiModelProperty(value = "정기배송주문결과상세PK")
	private long odRegularResultDetlId;

	@ApiModelProperty(value = "정기배송주문결과PK")
	private long odRegularResultId;

	@ApiModelProperty(value = "회차")
	private int reqRound;

	@ApiModelProperty(value = "정기배송신청번호")
	private String reqId;

	@ApiModelProperty(value = "도착예정일자")
	private LocalDate arriveDt;

	@ApiModelProperty(value = "주문생성여부")
	private String orderCreateYn;

	@ApiModelProperty(value = "신청상태코드")
	private String regularStatusCd;

	@ApiModelProperty(value = "신청상태코드명")
	private String regularStatusCdNm;

	@ApiModelProperty(value = "건너뛰기여부")
	private String regularSkipYn;

	@ApiModelProperty(value = "건너뛰기가능여부")
	private String regularSkipPsbYn;

	@ApiModelProperty(value = "회차종료여부")
	private String regularRoundEndYn;

	@ApiModelProperty(value = "회차완료여부")
	private String reqRoundYn;

	@ApiModelProperty(value = "상품할인타입코드")
	private String discountTp;

	@ApiModelProperty(value = "상품할인타입코드명")
	private String discountTpNm;

	@ApiModelProperty(value = "상품정상가")
	private int recommendedPrice;

	@ApiModelProperty(value = "총상품금액")
	private int salePrice;

	@ApiModelProperty(value = "총할인금액")
	private int discountPrice;

	@ApiModelProperty(value = "추가할인금액")
	private int addDiscountPrice;

	@ApiModelProperty(value = "총배송비")
	private int shippingPrice;

	@ApiModelProperty(value = "총결제예정금액")
	private int paidPrice;

	@ApiModelProperty(value = "총상품금액")
	private int totSalePrice;

	@ApiModelProperty(value = "총할인금액")
	private int totDiscountPrice;

	@ApiModelProperty(value = "총배송비")
	private int totShippingPrice;

	@ApiModelProperty(value = "총결제예정금액")
	private int totPaidPrice;

	@ApiModelProperty(value = "출고처PK")
	private long urWarehouseId;

	@ApiModelProperty(value = "배송정책PK")
	private long ilShippingTmplId;

	@ApiModelProperty(value = "상품품목PK")
	private String ilItemCd;

	@ApiModelProperty(value = "상품품목바코드")
	private String itemBarcode;

	@ApiModelProperty(value = "상품PK")
	private long ilGoodsId;

	@ApiModelProperty(value = "상품판매상태")
	private String saleStatus;

	@ApiModelProperty(value = "상품판매상태명")
	private String saleStatusNm;

	@ApiModelProperty(value = "정기배송상품판매상태")
	private String regularSaleStatus;

	@ApiModelProperty(value = "정기배송상품판매상태명")
	private String regularSaleStatusNm;

	@ApiModelProperty(value = "부모상품PK")
	private long parentIlGoodsId;

	@ApiModelProperty(value = "상품명")
	private String goodsNm;

	@ApiModelProperty(value = "상품이미지")
	private String thumbnailPath;

	@ApiModelProperty(value = "주문수량")
	private int orderCnt;

	@ApiModelProperty(value = "신청상세상태공통코드")
	private String reqDetailStatusCd;

	@ApiModelProperty(value = "신청상세상태공통코드명")
	private String reqDetailStatusCdNm;

	@ApiModelProperty(value = "상품유형명")
	private String goodsTpNm;

	@ApiModelProperty(value = "출고처명")
	private String warehouseNm;

	@ApiModelProperty(value = "보관방법명")
	private String storageMethodTpNm;

	@ApiModelProperty(value = "배송정책명")
	private String deliveryTmplNm;

	@ApiModelProperty(value = "정기배송 기본 할인율")
	private int basicDiscountRate;

	@ApiModelProperty(value = "정기배송 기본 할인 금액")
	private int basicDiscountPrice;

	@ApiModelProperty(value = "정기배송 추가 할인 회차")
	private int addDiscountRound;

	@ApiModelProperty(value = "정기배송 추가 할인율")
	private int adddiscountRate;

	@ApiModelProperty(value = "rowspan")
	private int rowSpan;

	@ApiModelProperty(value = "출고처별 배송정책 그룹핑 값")
	private String grpWarehouseShippingTmplId;

	@ApiModelProperty(value = "정기배송 취소 가능 여부")
	private String regularCancelPsbYn;

	@ApiModelProperty(value = "합배송여부")
	private String bundleYn;

	@ApiModelProperty(value = "정상결제여부")
	private String paymentYn;

	@ApiModelProperty(value = "결제실패건수")
	private int paymentFailCnt;
}
