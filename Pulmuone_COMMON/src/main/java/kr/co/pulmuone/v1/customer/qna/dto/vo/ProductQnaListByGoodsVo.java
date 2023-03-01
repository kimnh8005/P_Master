package kr.co.pulmuone.v1.customer.qna.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingLoginId;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "상품 문의 목록 Result")
public class ProductQnaListByGoodsVo {
    @ApiModelProperty(value = "문의 PK")
    private Long csQnaId;

    @ApiModelProperty(value = "처리상태")
    private String status;

    @ApiModelProperty(value = "처리 상태명")
    private String statusName;

    @ApiModelProperty(value = "로그인 ID")
    @UserMaskingLoginId
    private String loginId;

    @ApiModelProperty(value = "등록 일시")
    private String createDateTime;

    @ApiModelProperty(value = "상품문의 유형명")
    private String productTypeName;

    @ApiModelProperty(value = "상품 문의 제목")
    private String title;

    @ApiModelProperty(value = "상품 문의 내용")
    private String question;

    @ApiModelProperty(value = "나의 문의 여부")
    private String myQnaYn;

    @ApiModelProperty(value = "상품 문의 비공개 여부")
    private String secretType;

    @ApiModelProperty(value = "상품 문의 SMS 수신 여부")
    private String answerSmsYn;

    @ApiModelProperty(value = "상품 문의 Email 수신 여부")
    private String answerMailYn;

    @ApiModelProperty(value = "답변 정보")
    private List<QnaAnswerByCsQnaIdVo> answer;

    @ApiModelProperty(value = "상품 문의 상품명")
    private String goodsName;
}
