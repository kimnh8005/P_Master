package kr.co.pulmuone.v1.batch.order.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import lombok.Builder;
import lombok.Getter;

/**
 * <PRE>
 * Forbiz Korea
 * 올가 기타주문 List vo
 * </PRE>
 */

@Getter
@Builder
@ApiModel(description = "OrgaEtcOrderListVo")
public class OrgaEtcOrderListVo {

    @ApiModelProperty(value = "주문 PK")
    private String odOrderId;

    @ApiModelProperty(value = "주문상세 PK")
    private String odOrderDetlId;

    @ApiModelProperty(value = "등록일")
    private String createDt;

    @ApiModelProperty(value = "고객 요청사항")
    private String deliveryMsg;

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

    @ApiModelProperty(value = "품목코드")
    private String ilItemCd;

    @ApiModelProperty(value = "상품명")
    private String goodsNm;

    @ApiModelProperty(value = "주문수량-주문취소수량")
    private Long orderCancelCnt;

    @ApiModelProperty(value = "주문번호")
    private String odid;

    @ApiModelProperty(value = "주문상세 순번")
    private String odOrderDetlSeq;

    @ApiModelProperty(value = "대체배송여부")
    private BaseEnums.AlternateDeliveryType alternateDeliveryType;
}
