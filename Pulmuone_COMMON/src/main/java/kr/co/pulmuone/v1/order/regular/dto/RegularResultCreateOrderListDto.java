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
 * 정기배송 결과 주문생성 상품 목록 조회 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 02. 21.	김명진		최초작성
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "정기배송 결과 주문생성 상품 목록 조회 Dto")
public class RegularResultCreateOrderListDto {

	@ApiModelProperty(value = "정기배송주문신청PK")
	private long odRegularReqId;

	@ApiModelProperty(value = "정기배송결과PK")
	private long odRegularResultId;

	@ApiModelProperty(value = "회원그룹PK")
	private long urGroupId;

	@ApiModelProperty(value = "회원그룹명")
	private String urGroupNm;

	@ApiModelProperty(value = "회원PK")
	private long urUserId;

	@ApiModelProperty(value = "주문자명")
	private String buyerNm;

	@ApiModelProperty(value = "주문자핸드폰")
	private String buyerHp;

	@ApiModelProperty(value = "주문자연락처")
	private String buyerTel;

	@ApiModelProperty(value = "주문자이메일")
	private String buyerMail;

	@ApiModelProperty(value = "결제유형코드")
	private String paymentTypeCd;

	@ApiModelProperty(value = "주문자유형코드")
	private String buyerTypeCd;

	@ApiModelProperty(value = "주문유형코드")
	private String agentTypeCd;

	@ApiModelProperty(value = "사용자환경정보")
	private String urPcidCd;

	@ApiModelProperty(value = "배송타입")
	private String deliveryType;

	@ApiModelProperty(value = "수령인명")
	private String recvNm;

	@ApiModelProperty(value = "수령인핸드폰")
	private String recvHp;

	@ApiModelProperty(value = "수령인연락처")
	private String recvTel;

	@ApiModelProperty(value = "수령인이메일")
	private String recvMail;

	@ApiModelProperty(value = "수령인우편번호")
	private String recvZipCd;

	@ApiModelProperty(value = "수령인주소1")
	private String recvAddr1;

	@ApiModelProperty(value = "수령인주소2")
	private String recvAddr2;

	@ApiModelProperty(value = "빌딩번호")
	private String recvBldNo;

	@ApiModelProperty(value = "배송요청사항")
	private String deliveryMsg;

	@ApiModelProperty(value = "출입정보타입 공통코드")
	private String doorMsgCd;

	@ApiModelProperty(value = "배송출입 현관 비밀번호")
	private String doorMsg;

	@ApiModelProperty(value = "회차")
	private int reqRound;

	@ApiModelProperty(value = "마지막회차")
	private int lastReqRound;

	@ApiModelProperty(value = "정기배송 기본 할인율")
	private int basicDiscountRate;

	@ApiModelProperty(value = "정기배송 추가 할인 회차")
	private int addDiscountRound;

	@ApiModelProperty(value = "정기배송 추가 할인율")
	private int addDiscountRate;

	@ApiModelProperty(value = "공급처PK")
	private long urSupplierId;

	@ApiModelProperty(value = "출고처그룹코드")
	private String warehouseGrpCd;

	@ApiModelProperty(value = "상품보관방법")
	private String storageMethodTp;

	@ApiModelProperty(value = "상품유형")
	private String goodsTp;

	@ApiModelProperty(value = "판매유형")
	private String saleTp;

	@ApiModelProperty(value = "표준카테고리")
	private long ilCtgryStdId;

	@ApiModelProperty(value = "전시카테고리")
	private long ilCtgryId;

	@ApiModelProperty(value = "품목바코드")
	private String itemBarcode;

	@ApiModelProperty(value = "품목코드PK")
	private String ilItemCd;

	@ApiModelProperty(value = "상품PK")
	private long ilGoodsId;

	@ApiModelProperty(value = "상품명")
	private String goodsNm;

	@ApiModelProperty(value = "과세여부")
	private String taxYn;

	@ApiModelProperty(value = "주문수량")
	private int orderCnt;

	@ApiModelProperty(value = "원가")
	private int standardPrice;

	@ApiModelProperty(value = "정상가")
	private int recommendedPrice;

	@ApiModelProperty(value = "판매가")
	private int salePrice;

	@ApiModelProperty(value = "할인유형")
	private String discountTp;

	@ApiModelProperty(value = "출고처PK")
	private long urWarehouseId;

	@ApiModelProperty(value = "배송정책PK")
	private long ilShippingTmplId;

	@ApiModelProperty(value = "부모상품PK")
	private long parentIlGoodsId;

	@ApiModelProperty(value = "도착예정일")
	private LocalDate arriveDt;

	@ApiModelProperty(value = "추가할인여부")
	private String addDiscountYn;

	@ApiModelProperty(value = "판매상태")
	private String saleStatus;

	@ApiModelProperty(value = "출고처합배송그룹")
	private String grpWarehouseShippingTmplId;

	@ApiModelProperty(value = "I/F 날짜")
	private LocalDate orderIfDt;

	@ApiModelProperty(value = "출고예정일자")
	private LocalDate shippingDt;

	@ApiModelProperty(value = "도착일자")
	private LocalDate deliveryDt;
}
