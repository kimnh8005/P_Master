package kr.co.pulmuone.v1.customer.qna.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "1:1 문의 현황 Vo")
public class OnetooneQnaByUserVo {

	@ApiModelProperty(value = "발급회원코드", hidden = true)
    private Long urUserId;

    @ApiModelProperty(value = "문의 PK")
    private Long csQnaId;

    @ApiModelProperty(value = "문의 분류 - 유형")
    private String onetooneType;

    @ApiModelProperty(value = "주문번호")
    private Long odOrderId;

    @ApiModelProperty(value = "주문상세번호")
    private Long odOrderDetlId;

    @ApiModelProperty(value = "주문 상품명")
    private String goodsName;

    @ApiModelProperty(value = "주문 상품 이미지 경로")
    private String imagePath;

    @ApiModelProperty(value = "주문 상품 이미지명")
    private String imageOriginalName;

    @ApiModelProperty(value = "1:1 문의 제목")
    private String title;

    @ApiModelProperty(value = "1:1 문의 내용")
    private String question;

    @ApiModelProperty(value = "SMS 수신여부")
    private String answerSmsYn;

    @ApiModelProperty(value = "E-mail 수신여부")
    private String answerMailYn;

    @ApiModelProperty(value = "상태")
    private String status;
}
