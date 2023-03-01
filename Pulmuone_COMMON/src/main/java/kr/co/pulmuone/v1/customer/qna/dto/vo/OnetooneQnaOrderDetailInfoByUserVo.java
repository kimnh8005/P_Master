package kr.co.pulmuone.v1.customer.qna.dto.vo;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "1:1문의 주문조회 팝업 상세 조회 Vo")
public class OnetooneQnaOrderDetailInfoByUserVo {

	/* 주문 상세 번호*/
    private Long odOrderDetlId;

    /* 상품명 */
    private String goodsName;

    /* 이미지 원본파일명 */
    private String imageOriginalName;

    /*이미지 경로 */
    private String imagePath;

    /*묶음상품명, 골라담기상품명 리스트*/
    private String goodsNmList;

    /*묶음상품, 골라담기상품 구분값*/
    private String promotionTp;

}
