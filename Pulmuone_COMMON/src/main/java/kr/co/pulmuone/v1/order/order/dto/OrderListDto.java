package kr.co.pulmuone.v1.order.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingAddress;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingAddressDetail;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingLoginId;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingUserName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "OrderListDto")
public class OrderListDto {

	@ApiModelProperty(value = "주문 pk")
    private String odOrderId;

    @ApiModelProperty(value = "주문 일자")
    private String createDt;

	@ApiModelProperty(value = "주문 번호")
    private String odid;

    @ApiModelProperty(value = "판매처코드")
    private String sellersGroupCd;

    @ApiModelProperty(value = "판매처명")
    private String sellersGroupCdNm;

    @ApiModelProperty(value = "외부몰주문번호(이지어드민 ORDER_ID)")
    private String outMallId;

    @ApiModelProperty(value = "주문자 명")
    @UserMaskingUserName
    private String buyerNm;

    @ApiModelProperty(value = "회원 ID")
    private Long urUserId;

    @ApiModelProperty(value = "로그인 ID")
    @UserMaskingLoginId
    private String loginId;

    @ApiModelProperty(value = "비회원 CI")
    private String guestCi;

    @ApiModelProperty(value = "수취인명")
    @UserMaskingUserName
    private String recvNm;

    @ApiModelProperty(value = "주소1")
    @UserMaskingAddress
    private String recvAddr1;

    @ApiModelProperty(value = "주소2")
    @UserMaskingAddressDetail
    private String recvAddr2;

    @ApiModelProperty(value = "상품명")
    private String goodsNm;

    @ApiModelProperty(value = "주문금액")
    private int orderPrice;

    @ApiModelProperty(value = "배송비합계")
    private int shippingPrice;

    @ApiModelProperty(value = "쿠폰할인합계")
    private int couponPrice;

    @ApiModelProperty(value = "결제금액, 주문상세리트 주문금액")
    private int paidPrice;

    @ApiModelProperty(value = "결제수단")
    private String orderPaymentType;

    @ApiModelProperty(value = "주문 유형")
    private String agentType;

    @ApiModelProperty(value = "주문상태")
    private String statusNm;

    @ApiModelProperty(value = "클레임주문상태")
    private String claimStatusNm;

    @ApiModelProperty(value = "수집몰주문번호")
    private String collectionMallId;

    @ApiModelProperty(value = "판매처명")
    private String sellersNm;

    @ApiModelProperty(value = "주문복사여부")
    private String orderCopyYn;

    @ApiModelProperty(value = "주문복사 매출만연동여부")
    private String orderCopySalIfYn;

    @ApiModelProperty(value = "주문복사 원본 주문번호")
    private String orderCopyOdid;

    @ApiModelProperty(value = "주문생성여부")
    private String orderCreateYn;

    @ApiModelProperty(value = "회원 그룹 ID")
    private Long urGroupId;

    @ApiModelProperty(value = "매장명")
    private String urStoreNm;

	@ApiModelProperty(value = "배송유형")
    private String deliveryTypeNm;

	@ApiModelProperty(value = "매장(배송/픽업) 회차")
    private long storeScheduleNo;

    @ApiModelProperty(value = "매장(배송/픽업) - 주문배송시작시간")
    private String storeStartTime;

    @ApiModelProperty(value = "매장(배송/픽업) - 주문배송종료시간")
    private String storeEndTime;

    @ApiModelProperty(value = "도착예정일")
    private String deliveryDt;

    @ApiModelProperty(value = "주문 BosJson")
    private String orderBosJson;

    @ApiModelProperty(value = "클레임 BosJson")
    private String claimBosJson;


}
