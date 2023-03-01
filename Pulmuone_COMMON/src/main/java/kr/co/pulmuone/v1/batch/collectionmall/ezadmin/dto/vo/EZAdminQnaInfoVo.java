package kr.co.pulmuone.v1.batch.collectionmall.ezadmin.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@ApiModel(description = "이지어드민 문의글 조회 API 문의글정보 응답 VO")
public class EZAdminQnaInfoVo {

	@ApiModelProperty(value = "이지어드민 문의글 정보 PK")
    private Long csOutmallQnaEasyadminInfoId;

	@ApiModelProperty(value = "판매처코드")
	private int shop_id;

	@ApiModelProperty(value = "판매처 이름")
	private String shop_name;

	@ApiModelProperty(value = "판매처 상품코드")
	private String shop_product_id;

	@ApiModelProperty(value = "주문번호")
	private String order_id;

	@ApiModelProperty(value = "주문상세번호")
	private String order_id_seq;

	@ApiModelProperty(value = "문의일시")
	private String reg_date;

	@ApiModelProperty(value = "문의글 유형 (디폴트 : 주문/배송)")
	private String cs_type;

	@ApiModelProperty(value = "문의글 제목")
	private String cs_title;

	@ApiModelProperty(value = "문의글 내용")
	private String cs_text;

	@ApiModelProperty(value = "문의글 답변")
	private String cs_answer;

	@ApiModelProperty(value = "문의글 이지리플 내 상태 (0:답변대기, 1:답변입력, 2:전송실패, 3:전송완료)")
	private int status;
	
	@ApiModelProperty(value = "문의글 관리번호")
	private int seq;

	@ApiModelProperty(value = "문의글 BOS 상태")
    private String outmallQnaStatus;

	@ApiModelProperty(value = "외부몰문의 ID")
    private Long csOutmallQnaId;

	@ApiModelProperty(value = "수집몰 주문번호")
    private String order_seq;
	
	@ApiModelProperty(value = "처리불가 여부")
    private String warning;
}
