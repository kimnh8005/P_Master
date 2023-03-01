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
@ApiModel(description = "1:1문의 현황 Result")
public class QnaListByUserVo {
    
    @ApiModelProperty(value = "문의 PK")
    private Long csQnaId;

    @ApiModelProperty(value = "주문번호")
    private String odid;

    @ApiModelProperty(value = "1:1문의 분류 명")
    private String onetooneTypeName;

    @ApiModelProperty(value = "제목")
    private String title;

    @ApiModelProperty(value = "작성일자")
    private String createDate;

    @ApiModelProperty(value = "답변일자")
    private String answerDate;

    @ApiModelProperty(value = "처리상태 명")
    private String statusName;

    @ApiModelProperty(value = "처리상태")
    private String status;

    @ApiModelProperty(value = "상품이미지")
    private String thumbnailPath;

    @ApiModelProperty(value = "상품 PK")
    private Long ilGoodsId;

    @ApiModelProperty(value = "상품명")
    private String goodsName;

    @ApiModelProperty(value = "주문 상품 수량")
    private String orderGoodsCount;

    @ApiModelProperty(value = "주문 일자")
    private String orderDate;

    @ApiModelProperty(value = "품목명")
    private String itemName;

    @ApiModelProperty(value = "문의내용")
    private String question;

    @ApiModelProperty(value = "유저 확인 여부")
    private String userCheckYn;

    @ApiModelProperty(value = "합 타이틀")
    private String packTitle;

    @ApiModelProperty(value = "합 유형")
    private String packType;

    private List<QnaAttcByCsQnaIdVo> image;
    private List<QnaAnswerByCsQnaIdVo> answer;

}
