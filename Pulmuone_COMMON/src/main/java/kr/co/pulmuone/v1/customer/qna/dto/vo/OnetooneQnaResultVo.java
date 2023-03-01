package kr.co.pulmuone.v1.customer.qna.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(description = "1:1 문의 현황 Vo")
public class OnetooneQnaResultVo {

	//문의접수 시 정보
	@ApiModelProperty(value = "회원코드" , hidden = true)
	private String urUserId;

	@ApiModelProperty(value = "회원명")
	private String userNm;

	@ApiModelProperty(value = "이메일")
	private String mail;

	@ApiModelProperty(value = "모바일 전화번호")
	private String mobile;

	//문의답변 시 문의내용정보
    @ApiModelProperty(value = "SMS 수신여부")
    private String answerSmsYn;

    @ApiModelProperty(value = "E-mail 수신여부")
    private String answerMailYn;

    @ApiModelProperty(value = "문의 분류 - 유형")
    private String onetooneType;

    @ApiModelProperty(value = "주문번호")
    private Long odOrderId;

	@ApiModelProperty(value = "1:1 문의 제목")
    private String title;

    @ApiModelProperty(value = "1:1 문의 내용")
    private String question;

    @ApiModelProperty(value="등록일자")
    private String createDt;

}
