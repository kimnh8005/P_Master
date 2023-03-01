package kr.co.pulmuone.v1.batch.order.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import lombok.Builder;
import lombok.Getter;

/**
 * <PRE>
 * Forbiz Korea
 * 용인물류 주문 List vo
 * </PRE>
 */

@Getter
@Builder
@ApiModel(description = "DeliveryOrderListVo")
public class DeliveryOrderListVo {

    @ApiModelProperty(value = "주문 PK")
    private String odOrderId;

    @ApiModelProperty(value = "주문번호")
    private String odid;

    @ApiModelProperty(value = "주문일자")
    private String orderDt;

    @ApiModelProperty(value = "주문자명")
    private String buyerNm;

    @ApiModelProperty(value = "주문자 전화번호")
    private String buyerTel;

    @ApiModelProperty(value = "주문자 휴대폰번호")
    private String buyerHp;

    @ApiModelProperty(value = "주문자 이메일")
    private String buyerMail;

    @ApiModelProperty(value = "수령자명")
    private String recvNm;

    @ApiModelProperty(value = "수령자 주소 전체")
    private String recvAddr;

    @ApiModelProperty(value = "수령자 주소 뒤")
    private String recvAddr2;

    @ApiModelProperty(value = "수령자 주소 우편번호")
    private String recvZipCd;

    @ApiModelProperty(value = "수령자 전화번호")
    private String recvTel;

    @ApiModelProperty(value = "수령자 휴대폰번호")
    private String recvHp;

    @ApiModelProperty(value = "배송비")
    private String shippingPrice;

    @ApiModelProperty(value = "회원번호")
    private String urUserId;

    @ApiModelProperty(value = "인터페이스일자")
    private String orderIfDt;

    @ApiModelProperty(value = "주문상세 PK")
    private String odOrderDetlId;

    @ApiModelProperty(value = "주문상품 순번")
    private String odOrderDetlSeq;

    @ApiModelProperty(value = "공급처")
    private String urSupplierId;

    @ApiModelProperty(value = "품목코드")
    private String ilItemCd;

    @ApiModelProperty(value = "바코드")
    private String itemBarcode;

    @ApiModelProperty(value = "주문수량-주문취소수량")
    private Integer orderCancelCnt;

    @ApiModelProperty(value = "도착예정일자")
    private String deliveryDt;

    @ApiModelProperty(value = "상품판매 단가(부가세포함)")
    private double unitSalePrice;

    @ApiModelProperty(value = "상품판매 단가(부가세 과세여부 상관없이 무조건 제외, 올가용)")
    private double unitSaleNonTaxOrgaPrice;

    @ApiModelProperty(value = "상품판매 단가(부가세제외)")
    private double unitSaleNonTaxPrice;

    @ApiModelProperty(value = "상품판매 단가(부가세)")
    private double unitTaxPrice;

    @ApiModelProperty(value = "상품명")
    private String goodsNm;

    @ApiModelProperty(value = "상품판매 총금액(부가세포함)")
    private double totalSalePrice;

    @ApiModelProperty(value = "상품판매 총금액(부가세제외)")
    private double totalSaleNonTaxPrice;

    @ApiModelProperty(value = "상품판매 총금액(부가세)")
    private double totalTaxPrice;

    @ApiModelProperty(value = "주문경로")
    private String goodsTpCd;

    @ApiModelProperty(value = "수집몰 상세번호 (이지어드민 SEQ)")
    private String collectionMallDetailId;

    @ApiModelProperty(value = "고객 요청사항")
    private String deliveryMsg;

    @ApiModelProperty(value = "출입정보")
    private String doorMsgNm;

    @ApiModelProperty(value = "배송출입 현관 비밀번호")
    private String doorMsg;

    @ApiModelProperty(value = "주문 유형 ")
    private String agentTypeCd;

    @ApiModelProperty(value = "공급처 코드")
    private String supplierCd;

    @ApiModelProperty(value = "시퀀스번호")
    private String seqNo;

    @ApiModelProperty(value = "과세여부")
    private String taxYn;

    @ApiModelProperty(value = "식품마케팅증정품코드")
    private Integer giftFoodMarketingCd;

    @ApiModelProperty(value = "대체배송여부")
    private BaseEnums.AlternateDeliveryType alternateDeliveryType;

}
