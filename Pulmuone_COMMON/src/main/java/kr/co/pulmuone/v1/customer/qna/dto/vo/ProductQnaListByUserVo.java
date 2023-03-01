package kr.co.pulmuone.v1.customer.qna.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "상품 문의 목록 Result")
public class ProductQnaListByUserVo {
    @ApiModelProperty(value = "문의 PK")
    private Long csQnaId;

    @ApiModelProperty(value = "상품이미지")
    private String thumbnailPath;

    @ApiModelProperty(value = "상품 PK")
    private Long ilGoodsId;

    @ApiModelProperty(value = "상품명")
    private String goodsName;

    @ApiModelProperty(value = "품목명")
    private String itemName;

    @ApiModelProperty(value = "상품문의 유형명")
    private String productTypeName;

    @ApiModelProperty(value = "상품 문의 제목")
    private String title;

    @ApiModelProperty(value = "등록 일자")
    private String createDate;

    @ApiModelProperty(value = "최종 답변 일자")
    private String answerDate;

    @ApiModelProperty(value = "처리상태")
    private String status;

    @ApiModelProperty(value = "처리 상태명")
    private String statusName;

    @ApiModelProperty(value = "상품 문의 내용")
    private String question;

    @ApiModelProperty(value = "상품 문의 비공개 여부")
    private String secretType;

    @ApiModelProperty(value = "SMS 수신여부")
    private String answerSmsYn;

    @ApiModelProperty(value = "MAIL 수신여부")
    private String answerMailYn;

    @ApiModelProperty(value = "유저 확인 여부")
    private String userCheckYn;

    @ApiModelProperty(value = "상품 합 유형")
    private String packType;
    
    @ApiModelProperty(value = "답변 정보")
    private List<QnaAnswerByCsQnaIdVo> answer;
}
