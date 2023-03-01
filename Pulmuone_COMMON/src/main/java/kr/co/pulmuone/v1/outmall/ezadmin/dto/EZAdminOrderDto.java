package kr.co.pulmuone.v1.outmall.ezadmin.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class EZAdminOrderDto {

    @ApiModelProperty(value = "이미어드민정보PK")
    private String ifEasyadminInfoId;

    @ApiModelProperty(value = "이지어드민주문성공정보PK")
    private String ifEasyadminOrderSuccId;

    @ApiModelProperty(value = "이지어드민주문성공상세정보PK")
    private String ifEasyadminOrderSuccDetlId;

    @ApiModelProperty(value = "수집몰주문번호")
    private String collectionMallId;

    @ApiModelProperty(value = "수집몰주문상세번호")
    private String collectionMallDetailId;

    @ApiModelProperty(value = "판매처그룹코드")
    private String sellersGroupCd;

    @ApiModelProperty(value = "판매처 코드")
    private String omSellersId;

    @ApiModelProperty(value = "품목 PK")
    private String ilItemCd;

    @ApiModelProperty(value = "상품 PK")
    private String ilGoodsId;

    @ApiModelProperty(value = "상품명")
    private String goodsName;

    @ApiModelProperty(value = "주문수량")
    private String orderCount;

    @ApiModelProperty(value = "상품관리번호")
    private String prdSeq;

    @ApiModelProperty(value = "상품 총 금액")
    private String paidPrice;

    @ApiModelProperty(value = "주문자 명")
    private String buyerName;

    @ApiModelProperty(value = "주문자 연락처")
    private String buyerTel;

    @ApiModelProperty(value = "주문자 핸드폰")
    private String buyerMobile;

    @ApiModelProperty(value = "수령인 명")
    private String receiverName;

    @ApiModelProperty(value = "수령인 연락처")
    private String receiverTel;

    @ApiModelProperty(value = "수령인 핸드폰")
    private String receiverMobile;

    @ApiModelProperty(value = "수령인 이메일")
    private String receiverMail;

    @ApiModelProperty(value = "수령인 우편번호")
    private String receiverZipCode;

    @ApiModelProperty(value = "수령인 주소 1")
    private String receiverAddress1;

    @ApiModelProperty(value = "수령인 주소 2")
    private String receiverAddress2;

    @ApiModelProperty(value = "배송비")
    private String shippingPrice;

    @ApiModelProperty(value = "배송요청사항")
    private String deliveryMessage;

    @ApiModelProperty(value = "외부몰주문번호")
    private String outMallId;

    @ApiModelProperty(value = "외부몰주문상세번호")
    private String outMallDetailId;

    @ApiModelProperty(value = "택배사코드")
    private String logisticsCd;

    @ApiModelProperty(value = "송장번호")
    private String trackingNo;

    @ApiModelProperty(value = "성공여부")
    private boolean success;

    @ApiModelProperty(value = "실패사유")
    private String failMessage;

    @ApiModelProperty(value = "공급처 코")
    private String urSupplierId;

    @ApiModelProperty(value = "출고처 정보")
    private String urWarehouseId;

    @ApiModelProperty(value = "일일배송 타입")
    private String goodsDailyTp;

    @ApiModelProperty(value = "주소지별 그룹")
    private String grpShippingZone;

    @ApiModelProperty(value = "배송정책별 그룹")
    private String grpShippingPrice;

    @ApiModelProperty(value = "상품 타입")
    private String goodsTp;

    @ApiModelProperty(value = "출고처 그룹")
    private String warehouseGrpCd;

    @ApiModelProperty(value = "상품보관방법")
    private String storageMethodTp;

    @ApiModelProperty(value = "품목 바코드")
    private String itemBarcode;

    @ApiModelProperty(value = "과세여부")
    private String taxYn;

    @ApiModelProperty(value = "표준카테고리")
    private String ilCtgryStdId;

    @ApiModelProperty(value = "전시 카테고리")
    private String ilCtgryDisplayId;

    @ApiModelProperty(value = "몰인몰 카테고리")
    private String ilCtgryMallId;

    @ApiModelProperty(value = "상품 판매 타입")
    private String saleTp;

    @ApiModelProperty(value = "배송정책 PK")
    private String ilShippingTmplId;

    @ApiModelProperty(value = "원가")
    private int standardPrice;

    @ApiModelProperty(value = "정상가")
    private int recommendedPrice;

    @ApiModelProperty(value = "판매가")
    private int salePrice;

    @ApiModelProperty(value = "배치 연동상태")
    private String syncCd;

    @ApiModelProperty(value = "프로모션명")
    private String promotionNm;
}
