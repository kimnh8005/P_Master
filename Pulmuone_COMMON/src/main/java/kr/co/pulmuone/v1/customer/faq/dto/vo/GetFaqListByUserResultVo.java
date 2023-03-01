package kr.co.pulmuone.v1.customer.faq.dto.vo;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "FAQ 게시판 리스트 ResultVo")
public class GetFaqListByUserResultVo {

	/* FAQ관리 PK */
	private Long csFaqId;

	/*제목*/
	private String title;

	/*내용*/
	private String content;

	/*게시판 분류*/
	private String faqType;

	/*게시판 분류명*/
	private String faqTypeName;

	/*등록일*/
	private String createDate;


}
