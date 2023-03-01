package kr.co.pulmuone.v1.user.buyer.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.customer.feedback.dto.vo.FeedbackTargetListByUserResultVo;
import kr.co.pulmuone.v1.customer.qna.dto.vo.ProductQnaListByUserVo;
import kr.co.pulmuone.v1.customer.qna.dto.vo.QnaListByUserVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "Mypage 메인 정보")
public class MypageMainInfoResponseDto {

    @ApiModelProperty(value = "회원 등급")
    private String groupName;

    @ApiModelProperty(value = "회원 등급 아이콘")
    private String topImagePath;

    @ApiModelProperty(value = "회원 적립금")
    private int pointUsable;

    @ApiModelProperty(value = "회원 쿠폰 보유 수량")
    private int couponCount;

    @ApiModelProperty(value = "주문 입금대기 수량")
    private int depositReadyCount;

    @ApiModelProperty(value = "주문 결제완료 수량")
    private int depositCompleteCount;

    @ApiModelProperty(value = "주문 배송준비중 수량")
    private int deliveryReadyCount;

    @ApiModelProperty(value = "주문 배송중 수량")
    private int deliveryDoingCount;

    @ApiModelProperty(value = "주문 배송완료 수량")
    private int deliveryCompleteCount;

    @ApiModelProperty(value = "주문 취소 수량")
    private int orderCancelCount;

    @ApiModelProperty(value = "주문 반품/환불 수량")
    private int orderReturnRefundCount;

    @ApiModelProperty(value = "일반배송수량")
    private int normalDeliveryCount;

    @ApiModelProperty(value = "일일배송 수량")
    private int dailyDeliveryCount;

    @ApiModelProperty(value = "정기배송수량")
    private int regularDeliveryCount;

    @ApiModelProperty(value = "매장배송 수량")
    private int storeDeliveryCount;

    @ApiModelProperty(value = "작성 가능한 구매 후기 총 갯수")
    private int feedbackTargetCount;

    @ApiModelProperty(value = "후기")
    List<FeedbackTargetListByUserResultVo> feedback;

    @ApiModelProperty(value = "1:1문의 확인 해야할 답변 유무")
    private String onetooneNewAnswerYn = "N";

    @ApiModelProperty(value = "1:1 문의")
    List<QnaListByUserVo> onetoone;

    @ApiModelProperty(value = "상품문의 확인 해야할 답변 유무")
    private String productNewAnswerYn = "N";

    @ApiModelProperty(value = "상품 문의")
    List<ProductQnaListByUserVo> product;

}
