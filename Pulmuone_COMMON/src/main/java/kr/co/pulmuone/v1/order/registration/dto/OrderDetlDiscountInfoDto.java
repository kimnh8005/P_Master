package kr.co.pulmuone.v1.order.registration.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderCashReceiptVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetlVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderPaymentMasterVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderVo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 승인 관련 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 01. 21.            이명수         최초작성
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@ApiModel(description = "주문 할인정보 등록 Dto")
public class OrderDetlDiscountInfoDto {

    @ApiModelProperty(value = "주문PK")
    private long odOrderId;

    @ApiModelProperty(value = "주문상세PK")
    private long odOrderDetlId;

    @ApiModelProperty(value = "대상주문상세번호")
    private long targetOdOrderDetlId;

    @ApiModelProperty(value = "할인비율")
    private int discountRate;

    @ApiModelProperty(value = "주문수량")
    private int orderCnt;

    @ApiModelProperty(value = "클레임수량")
    private int claimCnt;

    @ApiModelProperty(value = "마지막노드 여부")
    private String isLastYn;

    @ApiModelProperty(value = "주문상세PK 리스트")
    private List<Long> odOrderDetlIds;

    @ApiModelProperty(value = "재배송타입")
    private String reDeliveryType;

    @ApiModelProperty(value = "상품쿠폰금액")
    private int goodsCouponPrice;
}
