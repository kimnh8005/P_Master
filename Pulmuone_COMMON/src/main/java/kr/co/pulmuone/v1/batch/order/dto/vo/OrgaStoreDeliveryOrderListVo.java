package kr.co.pulmuone.v1.batch.order.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import lombok.Builder;
import lombok.Getter;

/**
 * <PRE>
 * Forbiz Korea
 * 올가 매장배송 주문 List vo
 * </PRE>
 */

@Getter
@Builder
@ApiModel(description = "OrgaStoreDeliveryOrderListVo")
public class OrgaStoreDeliveryOrderListVo {

    @ApiModelProperty(value = "주문 PK")
    private String odOrderId;

    @ApiModelProperty(value = "주문번호")
    private String odid;

    @ApiModelProperty(value = "주문자명")
    private String buyerNm;

    @ApiModelProperty(value = "주문자 휴대폰번호")
    private String buyerHp;

    @ApiModelProperty(value = "수령자명")
    private String recvNm;

    @ApiModelProperty(value = "수령자 주소 우편번호")
    private String recvZipCd;

    @ApiModelProperty(value = "수령자 주소 앞")
    private String recvAddr1;

    @ApiModelProperty(value = "수령자 주소 뒤")
    private String recvAddr2;

    @ApiModelProperty(value = "수령자 휴대폰번호")
    private String recvHp;

    @ApiModelProperty(value = "고객 요청사항")
    private String deliveryMsg;

    @ApiModelProperty(value = "생성일시")
    private String createDt;

    @ApiModelProperty(value = "매장코드")
    private String shpCd;

    @ApiModelProperty(value = "총매출액")
    private double totSaleAmt;

    @ApiModelProperty(value = "총할인액")
    private double totDcAmt;

    @ApiModelProperty(value = "과세매출액")
    private double vatSalAmt;

    @ApiModelProperty(value = "면세매출액")
    private double noVatSalAmt;

    @ApiModelProperty(value = "배송비")
    private double shippingPrice;

    @ApiModelProperty(value = "결제일시")
    private String payOutDt;

    @ApiModelProperty(value = "배달-배송시간 from")
    private String dlvFroDt;

    @ApiModelProperty(value = "배달-배송시간 to")
    private String dlvToDt;

    @ApiModelProperty(value = "배달-차수")
    private String dlvIdx;

    @ApiModelProperty(value = "주문상세 PK")
    private String odOrderDetlId;

    @ApiModelProperty(value = "품목코드")
    private String ilItemCd;

    @ApiModelProperty(value = "품목바코드")
    private String itemBarcode;

    @ApiModelProperty(value = "주문수량-주문취소수량")
    private Integer orderCancelCnt;

    @ApiModelProperty(value = "과세구분")
    private String taxYn;

    @ApiModelProperty(value = "판매 단가")
    private double unitSalePrice;

    @ApiModelProperty(value = "판매가")
    private double salePrice;

    @ApiModelProperty(value = "할인가")
    private double discountPrice;

    @ApiModelProperty(value = "부가세")
    private double vatAmt;

    @ApiModelProperty(value = "부가세 제외 매출액")
    private double totalSaleNonTaxPrice;

    @ApiModelProperty(value = "시퀀스번호")
    private String seqNo;

    @ApiModelProperty(value = "주문상세 SEQ")
    private String odOrderDetlSeq;

    @ApiModelProperty(value = "주문상태 배송유형")
    private String orderStatusDeliTp;

    @ApiModelProperty(value = "대체배송여부")
    private BaseEnums.AlternateDeliveryType alternateDeliveryType;

}
