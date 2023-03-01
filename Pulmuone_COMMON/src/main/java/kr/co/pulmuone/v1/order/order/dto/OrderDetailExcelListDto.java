package kr.co.pulmuone.v1.order.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 상세 엑셀 다운로드 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 01. 18.    김명진         최초작성
 * =======================================================================
 * </PRE>
 */
@Setter
@Getter
@ToString
@ApiModel(description = "OrderDetailExcelListDto")
public class OrderDetailExcelListDto {

    @ApiModelProperty(value = "주문 PK")
    private String odOrderId;

    @ApiModelProperty(value = "주문번호")
    private String odid;

    @ApiModelProperty(value = "주문 클레임 PK")
    private String odClaimId;

    @ApiModelProperty(value = "주문상세 PK")
    private String odOrderDetlId;

    @ApiModelProperty(value = "주문상세 순번(라인번호) 주문번호에 대한 순번")
    private int odOrderDetlSeq;

    @ApiModelProperty(value = "판매처 PK")
    private String omSellersId;

    @ApiModelProperty(value = "판매처명")
    private String sellersNm;

    @ApiModelProperty(value = "수집몰주문번호 (이지어드민 pack, seq 조합)")
    private String collectionMallId;

    @ApiModelProperty(value = "외부몰주문번호(이지어드민 orderId)")
    private String outmallId;

    @ApiModelProperty(value = "회원 그룹명 : urGroup.urGroupNm")
    private String urGroupNm;

    @ApiModelProperty(value = "회원 pk : urUser.urUserId")
    private String urUserId;

    @ApiModelProperty(value = "회원 로그인 id : urUser.loginId")
    private String loginId;

    @ApiModelProperty(value = "임직원사번 : urEmployee.urEmployeeCd")
    private String urEmployeeCd;

    @ApiModelProperty(value = "주문자명")
    private String buyerNm;

    @ApiModelProperty(value = "주문자핸드폰")
    private String buyerHp;

    @ApiModelProperty(value = "주문자연락처")
    private String buyerTel;

    @ApiModelProperty(value = "주문자이메일")
    private String buyerMail;

    @ApiModelProperty(value = "배송타입명")
    private String deliveryType;

    @ApiModelProperty(value = "주문타입명(배송등록타입명)")
    private String shippingType;

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

    @ApiModelProperty(value = "건물번호")
    private String recvBldNo;

    @ApiModelProperty(value = "배송요청사항")
    private String deliveryMsg;

    @ApiModelProperty(value = "출입정보타입명")
    private String doorMsgCd;

    @ApiModelProperty(value = "배송출입 현관 비밀번호(출입정보)")
    private String doorMsg;

    @ApiModelProperty(value = "결제수단 : 공통코드(payTp)")
    private String payTp;

    @ApiModelProperty(value = "결제수단명")
    private String orderPaymentTypeNm;

    @ApiModelProperty(value = "주문금액 (정상가 : ilGoods.recommendedPrice * 주문수량)")
    private int orderPrice;

    @ApiModelProperty(value = "배송비합계")
    private int shippingPrice;

    @ApiModelProperty(value = "쿠폰할인합계")
    private int couponPrice;

    @ApiModelProperty(value = "즉시할인합계")
    private int directPrice;

    @ApiModelProperty(value = "즉시할인명")
    private String directPriceNm;

    @ApiModelProperty(value = "결제금액 (쿠폰까지 할인된 금액)")
    private int paidPrice;

    @ApiModelProperty(value = "주문상태")
    private String orderStatusNm;

    @ApiModelProperty(value = "클레임상태")
    private String claimStatusNm;

    @ApiModelProperty(value = "결제완료일자")
    private String orderIcDt;

    @ApiModelProperty(value = "주문등록일")
    private String orderCreateDt;

    @ApiModelProperty(value = "출고처그룹 코드")
    private String warehouseGrpCd;

    @ApiModelProperty(value = "출고처그룹명")
    private String urWarehouseGrpNm;

    @ApiModelProperty(value = "상품보관방법 ilItem.storageMethodTp 공통코드(erpStorageType)")
    private String storageTypeCd;

    @ApiModelProperty(value = "상품보관방법명")
    private String storageTypeNm;

    @ApiModelProperty(value = "상품유형 ilGoods.goodsTp 공통코드(goodsType)")
    private String goodsTpCd;

    @ApiModelProperty(value = "상품유형명")
    private String goodsTpNm;

    @ApiModelProperty(value = "주문상태 배송유형 공통코드: orderStatusDeliTp")
    private String orderStatusDeliTp;

    @ApiModelProperty(value = "주문상태 배송유형명")
    private String orderStatusDeliTpNm;

    @ApiModelProperty(value = "판매유형 ilGoods.saleTp 공통코드(saleType)")
    private String saleTpCd;

    @ApiModelProperty(value = "판매유형명")
    private String saleTpNm;

    @ApiModelProperty(value = "표준카테고리 : ilItem.ilCtgryStdId")
    private String ilCtgryStdId;

    @ApiModelProperty(value = "표준카테고리명")
    private String ilCtgryStdNm;

    @ApiModelProperty(value = "전시카테고리 : ilGoodsCtgry.ilCtgryId")
    private String ilCtgryDisplayId;

    @ApiModelProperty(value = "전시카테고리명")
    private String ilCtgryDisplayNm;

    @ApiModelProperty(value = "몰인몰카테고리 : ilGoodsCtgry.ilCtgryId")
    private String ilCtgryMallId;

    @ApiModelProperty(value = "몰인몰카테고리명")
    private String ilCtgryMallNm;

    @ApiModelProperty(value = "마스터품목코드")
    private String ilItemCd;

    @ApiModelProperty(value = "품목바코드")
    private String itemBarcode;

    @ApiModelProperty(value = "품목명")
    private String itemNm;

    @ApiModelProperty(value = "상품 pk : ilGoods.ilGoodsId")
    private String ilGoodsId;

    @ApiModelProperty(value = "상품명 : ilGoods.goodsNm")
    private String goodsNm;

    @ApiModelProperty(value = "배송유형 공통코드(goodsDeliveryType)")
    private String goodsDeliveryType;

    @ApiModelProperty(value = "배송유형명")
    private String scheduleType;

    @ApiModelProperty(value = "배송주기 공통코드:goodsCycleTp")
    private String goodsCycleTp;

    @ApiModelProperty(value = "배송주기명")
    private String oneDeliveryTermCd;

    @ApiModelProperty(value = "배송기간 공통코드:goodsCycleTermTp")
    private String goodsCycleTermTp;

    @ApiModelProperty(value = "배송기간명")
    private String oneCycleTermTypeCd;

    @ApiModelProperty(value = "배송요일")
    private String deliveryWeekNm;

    @ApiModelProperty(value = "알러지 식단 여부")
    private String allergyDietYn;

    @ApiModelProperty(value = "주문수량")
    private int orderCnt;

    @ApiModelProperty(value = "클레임 수량")
    private int cancelCnt;

    @ApiModelProperty(value = "클레임 가능 수량")
    private int cancelAbleCnt;

    @ApiModelProperty(value = "출고요청수량")
    private int warehouseReqCnt;

    @ApiModelProperty(value = "과세여부")
    private String taxNm;

    @ApiModelProperty(value = "할인유형(none, 우선, 올가, 즉시, 적용불가) : 공통코드(goodsDiscountTp)")
    private String goodsDiscountTp;

    @ApiModelProperty(value = "할인유형 명")
    private String goodsDiscountTpNm;

    @ApiModelProperty(value = "원가 : ilGoods.standardPrice")
    private int standardPrice;

    @ApiModelProperty(value = "정상가 : ilGoods.recommendedPrice")
    private int recommendedPrice;

    @ApiModelProperty(value = "판매가 : ilGoods.salePrice")
    private int salePrice;

    @ApiModelProperty(value = "주문상세 상태")
    private String orderDetailStatusCd;

    @ApiModelProperty(value = "주문상세 상태명")
    private String orderDetlStatusNm;

    @ApiModelProperty(value = "클레임상세 상태")
    private String claimDetailStatusCd;

    @ApiModelProperty(value = "클레임상세 상태명")
    private String claimDetlStatusNm;

    @ApiModelProperty(value = "클레임상세 환불 상태명")
    private String refundStatusNm;

    @ApiModelProperty(value = "클레임 사유 명")
    private String claimReasonNm;

    @ApiModelProperty(value = "클레임상세 사유")
    private String claimReasonMsg;

    @ApiModelProperty(value = "배송준비중등록자")
    private String drId;

    @ApiModelProperty(value = "배송준비중일자")
    private String drDt;

    @ApiModelProperty(value = "배송중등록자")
    private String diId;

    @ApiModelProperty(value = "배송중일자")
    private String diDt;

    @ApiModelProperty(value = "배송완료등록자")
    private String dcId;

    @ApiModelProperty(value = "배송완료일자")
    private String dcDt;

    @ApiModelProperty(value = "구매확정등록자")
    private String bfId;

    @ApiModelProperty(value = "구매확정일자")
    private String bfDt;

    @ApiModelProperty(value = "취소요청등록자")
    private String caId;

    @ApiModelProperty(value = "취소요청일자")
    private String caDt;

    @ApiModelProperty(value = "취소완료등록자")
    private String ccId;

    @ApiModelProperty(value = "취소완료일자")
    private String ccDt;

    @ApiModelProperty(value = "반품요청등록자")
    private String raId;

    @ApiModelProperty(value = "반품요청일자")
    private String raDt;

    @ApiModelProperty(value = "반품승인등록자")
    private String riId;

    @ApiModelProperty(value = "반품승인일자")
    private String riDt;

    @ApiModelProperty(value = "반품보류등록자")
    private String rfId;

    @ApiModelProperty(value = "반품보류일자")
    private String rfDt;

    @ApiModelProperty(value = "반품완료등록자")
    private String rcId;

    @ApiModelProperty(value = "반품완료일자")
    private String rcDt;

    @ApiModelProperty(value = "재배송등록자")
    private String ecId;

    @ApiModelProperty(value = "재배송일자")
    private String ecDt;

    @ApiModelProperty(value = "CS환불등록자")
    private String csId;

    @ApiModelProperty(value = "CS환불일자")
    private String csDt;

    @ApiModelProperty(value = "환불요청등록자")
    private String faId;

    @ApiModelProperty(value = "환불요청일자")
    private String faDt;

    @ApiModelProperty(value = "환불완료등록자")
    private String fcId;

    @ApiModelProperty(value = "환불완료일자")
    private String fcDt;

    @ApiModelProperty(value = "클레임요청등록자")
    private String crId;

    @ApiModelProperty(value = "클레임요청일자")
    private String crDt;

    @ApiModelProperty(value = "클레임승인등록자")
    private String ceId;

    @ApiModelProperty(value = "클레임승인일자")
    private String ceDt;

    @ApiModelProperty(value = "주문I/F일자등록자")
    private String orderIfId;

    @ApiModelProperty(value = "주문I/F일자")
    private String orderIfDt;

    @ApiModelProperty(value = "출고예정일등록자")
    private String shippingId;

    @ApiModelProperty(value = "출고예정일일자")
    private String shippingDt;

    @ApiModelProperty(value = "도착예정일등록자")
    private String deliveryId;

    @ApiModelProperty(value = "도착예정일일자")
    private String deliveryDt;

    @ApiModelProperty(value = "송장번호")
    private String odTrackingNumberId;

    @ApiModelProperty(value = "택배사 PK")
    private String psShippingCompId;

    @ApiModelProperty(value = "현금영수증발급번호구분(임시)")
    private String cashReceiptIssueGbnCd;

    @ApiModelProperty(value = "현금영수증발급여부(임시)")
    private String issueNum;

    @ApiModelProperty(value = "현금영수증발급승인번호(임시)")
    private String issueYn;

    @ApiModelProperty(value = "미출사유")
    private String missReason;

    @ApiModelProperty(value = "미출상세사유")
    private String missMsg;

    @ApiModelProperty(value = "반품택배사명")
    private String returnPsShippingCompId;

    @ApiModelProperty(value = "반품송장번호")
    private String returnOdTrackingNumberId;

    @ApiModelProperty(value = "에이젼트 타입")
    private String agentTypeCdNm;

    @ApiModelProperty(value = "주문자유형")
    private String buyerTypeCdNm;

    @ApiModelProperty(value = "수집물 주문상세번호")
    private String collectionMallDetailId;

    @ApiModelProperty(value = "외부몰 주문상세번호")
    private String outmallDetailId;

    @ApiModelProperty(value = "출고처명")
    private String urWarehouseNm;

    @ApiModelProperty(value = "배송패턴명")
    private String psShippingPatternNm;

    @ApiModelProperty(value = "공급업체명")
    private String urSupplierNm;

    @ApiModelProperty(value = "정산일자")
    private String settleDt;

    @ApiModelProperty(value = "예상상품매출(VAT포함)")
    private int finalGoodsPriceVat;

    @ApiModelProperty(value = "예상상품매출(VAT제외)")
    private int finalGoodsPriceNotVat;

    @ApiModelProperty(value = "ERP 카테고리")
    private String erpCtgryNm;
    
    @ApiModelProperty(value = "임직원 할인 금액")
    private int discountEmployeePrice;

    @ApiModelProperty(value = "클레임사유(대)")
    private String lclaimName;

    @ApiModelProperty(value = "클레임사유(중)")
    private String mclaimName;

    @ApiModelProperty(value = "귀책처")
    private String sclaimName;

    @ApiModelProperty(value = "귀책구분 B: 구매자, S: 판매자")
    private String targetTp;

    @ApiModelProperty(value = "주문상담 내용")
    private String odConsultMsg = "";
}
