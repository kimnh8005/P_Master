package kr.co.pulmuone.v1.customer.qna.dto.vo;

import java.util.List;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "1:1문의 주문조회 팝업 조회 Vo")
public class OnetooneQnaOrderInfoByUserVo {

	/* 발급회원코드 */
    private Long urUserId;

	/* 주문일자 */
    private String createDate;

    /* 주문번호 (SEQ) */
    private Long odOrderId;

    /* 주문번호 */
    private String odId;

   /* 주문 상세 리스트 */
   List<OnetooneQnaOrderDetailInfoByUserVo> orderDetail;

}
