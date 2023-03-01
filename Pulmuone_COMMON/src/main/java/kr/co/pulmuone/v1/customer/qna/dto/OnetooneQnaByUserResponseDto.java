package kr.co.pulmuone.v1.customer.qna.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.customer.qna.dto.vo.OnetooneQnaByUserAttcVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "1:1 문의 상세 조회 ResponseDto")
public class OnetooneQnaByUserResponseDto {

    @ApiModelProperty(value = "발급회원코드", hidden = true)
    private Long urUserId;

    @ApiModelProperty(value = "문의 pk")
    private Long csQnaId;

    @ApiModelProperty(value="1:1 문의 분류")
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

    @ApiModelProperty(value = "이미지 리스트")
    List<OnetooneQnaByUserAttcVo> image;

    @ApiModelProperty(value = "제목")
    private String title;

    @ApiModelProperty(value = "내용")
    private String question;

    @ApiModelProperty(value = "답변 알람 수신 E-mail")
    private String answerMailYn;

    @ApiModelProperty(value = "답변 알람 수신 SMS")
    private String answerSmsYn;

    @ApiModelProperty(value = "상태")
    private String status;

}
