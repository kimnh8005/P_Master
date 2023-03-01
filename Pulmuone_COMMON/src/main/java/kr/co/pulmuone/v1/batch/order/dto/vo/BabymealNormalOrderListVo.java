package kr.co.pulmuone.v1.batch.order.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import lombok.Builder;
import lombok.Getter;

/**
 * <PRE>
 * Forbiz Korea
 * 베이비밀 택배배송 주문 List vo
 * </PRE>
 */

@Getter
@Builder
@ApiModel(description = "BabymealNormalOrderListVo")
public class BabymealNormalOrderListVo {

    @ApiModelProperty(value = "주문 PK")
    private String odOrderId;

    @ApiModelProperty(value = "주문번호")
    private String odid;

    @ApiModelProperty(value = "주문상세 PK")
    private String odOrderDetlId;

    @ApiModelProperty(value = "주문상품 순번")
    private String odOrderDetlSeq;

    @ApiModelProperty(value = "등록일")
    private String createDt;

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

    @ApiModelProperty(value = "수령자 전화번호")
    private String recvTel;

    @ApiModelProperty(value = "수령자 휴대폰번호")
    private String recvHp;

    @ApiModelProperty(value = "수령자 주소 전체")
    private String recvAddr;

    @ApiModelProperty(value = "수령자 주소 앞")
    private String recvAddr1;

    @ApiModelProperty(value = "수령자 주소 뒤")
    private String recvAddr2;

    @ApiModelProperty(value = "수령자 주소 우편번호")
    private String recvZipCd;

    @ApiModelProperty(value = "건물번호")
    private String recvBldNo;

    @ApiModelProperty(value = "고객 요청사항")
    private String deliveryMsg;
    
    @ApiModelProperty(value = "출입정보")
    private String doorMsgNm;
    
    @ApiModelProperty(value = "배송출입 현관 비밀번호")
    private String doorMsg;

    @ApiModelProperty(value = "품목코드")
    private String ilItemCd;

    @ApiModelProperty(value = "상품코드")
    private String ilGoodsId;

    @ApiModelProperty(value = "주문수량-주문취소수량")
    private Integer orderCancelCnt;

    @ApiModelProperty(value = "도착예정일")
    private String deliveryDt;
    
    @ApiModelProperty(value = "정상판매금액")
    private double recommendedPrice;
    
    @ApiModelProperty(value = "판매금액")
    private double salePrice;

    @ApiModelProperty(value = "결제금액")
    private int paidPrice;

    @ApiModelProperty(value = "공통출고처ID")
    private String psWarehouseId;

    @ApiModelProperty(value = "주문자 유형")
    private String buyerTypeCd;

    @ApiModelProperty(value = "주문 유형")
    private String agentTypeCd;

    @ApiModelProperty(value = "PDM 그룹코드")
    private String pdmGroupCd;

    @ApiModelProperty(value = "시퀀스번호")
    private String seqNo;

    @ApiModelProperty(value = "과세구분")
    private String taxYn;

    @ApiModelProperty(value = "대체배송여부")
    private BaseEnums.AlternateDeliveryType alternateDeliveryType;

}
