package kr.co.pulmuone.v1.api.ecs.dto.vo;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@ApiModel(description = "ECS 문의 Vo")
public class QnaToEcsVo {

	/*
	 * ECS DATA 정의
	 */
	private String receiptRoot;

	/*
	 * 문의 유형(상품Q&A,1:1문의)
	 */
	private String boardDiv;

	/*
	 * CS_QNA_ID
	 */
	private String boardSeq;

	/*
	 * 질문자 urUserId
	 */
	private String customerNum;

	/*
	 * 질문자 이름
	 */
	private String customerName;

	/*
	 * 질문자 모바일
	 */
	private String customerPhonearea;

	/*
	 * 질문자 모바일
	 */
	private String customerPhonefirst;

	/*
	 * 질문자 모바일
	 */
	private String customerPhonesecond;

	/*
	 * 질문자 이메일
	 */
	private String customerEmail;

	/*
	 * 문의 구분에 따른 코드값1
	 */
	private String hdBcode;

	/*
	 * 문의 구분에 따른 코드값2
	 */
	private String hdScode;

	/*
	 * 문의 구분에 따른 코드값3
	 */
	private String claimGubun;

	/*
	 * 문의내용(제목 + 내용)
	 */
	private String counselDesc;

	/*
	 * 답변내용
	 */
	private String reply;

	/*
	 * 답변자 urUserId
	 */
	private String counseler;

	/*
	 * 부문
	 */
	private String secCode;

}
