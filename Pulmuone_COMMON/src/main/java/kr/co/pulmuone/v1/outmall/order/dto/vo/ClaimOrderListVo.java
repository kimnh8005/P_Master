package kr.co.pulmuone.v1.outmall.order.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingMobile;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingTelePhone;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingUserName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ClaimOrderListVo {

    @ApiModelProperty(value = "easyadmin claim PK")
    private Long ifEasyadminOrderClaimId;

    @ApiModelProperty(value = "외부몰")
    private String shopName;
    
    @ApiModelProperty(value = "외부몰주문번호")
    private String orderId;

    @ApiModelProperty(value = "외부몰주문상세번호1")
    private String orderIdSeq1;

    @ApiModelProperty(value = "외부몰주문상세번호2")
    private String orderIdSeq2;

    @ApiModelProperty(value = "외부몰주문상세번호")
    private String seq;

    @ApiModelProperty(value = "주문자명")
    @UserMaskingUserName
    private String orderName;

    @ApiModelProperty(value = "주문자연락처")
    @UserMaskingMobile
    private String orderMobile;

    @ApiModelProperty(value = "수취인명")
    @UserMaskingUserName
    private String receiverName;

    @ApiModelProperty(value = "수취인연락처")
    @UserMaskingMobile
    private String receiverMobile;

    @ApiModelProperty(value = "주문상태")
    private String orderCsName;

    @ApiModelProperty(value = "주문일")
    private String orderDate;

    @ApiModelProperty(value = "수집몰상품코드")
    private String productId;

    @ApiModelProperty(value = "주문상세번호")
    private String orderDetailId;

    @ApiModelProperty(value = "마스터품목코드")
    private String masterProductCodeName;

    @ApiModelProperty(value = "상품코드")
    private String shopProductId;

    @ApiModelProperty(value = "상품판매상태")
    private String enableSaleName;

    @ApiModelProperty(value = "상품명")
    private String name;

    @ApiModelProperty(value = "수량")
    private String qty;

    @ApiModelProperty(value = "판매가")
    private String shopPrice;

    @ApiModelProperty(value = "처리상태 코드")
    private String processCode;

    @ApiModelProperty(value = "처리상태 코드 명")
    private String processCodeName;

    @ApiModelProperty(value = "관리자 PK")
    private String adminId;

    @ApiModelProperty(value = "관리자 명")
    private String adminName;

}
