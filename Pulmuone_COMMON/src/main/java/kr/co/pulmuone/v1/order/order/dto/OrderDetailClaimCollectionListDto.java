package kr.co.pulmuone.v1.order.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 상세 클레임 회수 정보 조회 관련 Dto
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "주문 상세 클레임 회수 정보 조회 관련 Dto")
public class OrderDetailClaimCollectionListDto {

    @ApiModelProperty(value = "출고처")
    private String warehouseNm;

    @ApiModelProperty(value = "송장번호")
    private String trackingNo;

    @ApiModelProperty(value = "택배사")
    private String shippingCompNm;

    @ApiModelProperty(value = "배송비")
    private int addPaymentShippingPrice;

    @ApiModelProperty(value = "귀책구분")
    private String targetTpNm;

    @ApiModelProperty(value = "상품명")
    private String goodsNm;

    @ApiModelProperty(value = "외 N건")
    private String goodsNmCnt;

    @ApiModelProperty(value = "받는분")
    private String recvNm;

    @ApiModelProperty(value = "휴대폰")
    private String recvHp;

    @ApiModelProperty(value = "우편번호")
    private String recvZipCd;

    @ApiModelProperty(value = "회수지 주소")
    private String recvAddr1;

    @ApiModelProperty(value = "상세주소")
    private String recvAddr2;

    @ApiModelProperty(value = "배송요청사항")
    private String deliveryMsg;

    @ApiModelProperty(value = "배송추적정보")
    private String shippingInfo;

}
