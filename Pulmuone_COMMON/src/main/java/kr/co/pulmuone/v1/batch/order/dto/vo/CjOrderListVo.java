package kr.co.pulmuone.v1.batch.order.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import lombok.Builder;
import lombok.Getter;

/**
 * <PRE>
 * Forbiz Korea
 * 백암물류 주문 List vo
 * </PRE>
 */

@Getter
@Builder
@ApiModel(description = "CjOrderListVo")
public class CjOrderListVo {

    @ApiModelProperty(value = "주문 PK")
    private String odOrderId;

    @ApiModelProperty(value = "주문번호")
    private String odid;

    @ApiModelProperty(value = "주문자명")
    private String buyerNm;

    @ApiModelProperty(value = "수령자명")
    private String recvNm;

    @ApiModelProperty(value = "수령자 주소 전체")
    private String recvAddr;

    @ApiModelProperty(value = "수령자 주소 우편번호")
    private String recvZipCd;

    @ApiModelProperty(value = "수령자 전화번호")
    private String recvTel;

    @ApiModelProperty(value = "수령자 휴대폰번호")
    private String recvHp;

    @ApiModelProperty(value = "고객 요청사항")
    private String deliveryMsg;

    @ApiModelProperty(value = "주문상세 PK")
    private String odOrderDetlId;

    @ApiModelProperty(value = "주문상품 순번")
    private String odOrderDetlSeq;

    @ApiModelProperty(value = "품목코드")
    private String ilItemCd;

    @ApiModelProperty(value = "주문수량-주문취소수량")
    private Integer orderCancelCnt;

    @ApiModelProperty(value = "인터페이스일자")
    private String orderIfDt;

    @ApiModelProperty(value = "상품명")
    private String goodsNm;

    @ApiModelProperty(value = "생성일자")
    private String createDt;

    @ApiModelProperty(value = "주문 유형")
    private String agentTypeCd;

    @ApiModelProperty(value = "물류구분")
    private String logisticsGubun;

    @ApiModelProperty(value = "시퀀스번호")
    private String seqNo;

    @ApiModelProperty(value = "상품유형")
    private String goodsTpCd;

    @ApiModelProperty(value = "대체배송여부")
    private BaseEnums.AlternateDeliveryType alternateDeliveryType;

}
