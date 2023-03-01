package kr.co.pulmuone.v1.customer.qna.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProductQnaRequestDto {
    @ApiModelProperty(value = "발급회원코드", hidden = true)
    private Long urUserId;

    @ApiModelProperty(value = "상품문의 유형코드")
    private String productType;

    @ApiModelProperty(value = "상품 PK")
    private Long ilGoodsId;

    @ApiModelProperty(value = "상품 문의 제목")
    private String title;

    @ApiModelProperty(value = "상품 문의 내용")
    private String question;

    @ApiModelProperty(value = "상품 문의 비공개 여부")
    private String secretType;

    @ApiModelProperty(value = "상품 문의 SMS 수신 여부")
    private String answerSmsYn;

    @ApiModelProperty(value = "상품 문의 Email 수신 여부")
    private String answerMailYn;

    @ApiModelProperty(value = "문의 PK", hidden = true)
    private Long csQnaId;

    @ApiModelProperty(value = "상품명", hidden = true)
    private String goodsName;

    /*
     * ECS연동 위한 회원 정보
     */
    @ApiModelProperty(value = "회원명", hidden = true)
    private String userName;

    @ApiModelProperty(value = "회원 모바일", hidden = true)
    private String userMobile;

    @ApiModelProperty(value = "회원 이메일", hidden = true)
    private String userEmail;

    /*
     * ECS연동 분류 코드
     */
    @ApiModelProperty(value = "ECS연동 상담대분류", hidden = true)
    private String hdBcode;

    @ApiModelProperty(value = "ECS연동 상담중분류", hidden = true)
    private String hdScode;

    @ApiModelProperty(value = "ECS연동 상담소분류", hidden = true)
    private String claimGubun;

}
