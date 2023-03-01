package kr.co.pulmuone.v1.customer.notice.dto.vo;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "이전글/다음글 조회 ResultVo")
public class GetNoticePreNextListByUserResultVo {

	/*이전글/다음글 공지사항 PK*/
	private Long csNoticeId;

	/*이전글/다음글 공지사항 분류명*/
	private String noticeTypeName;

	/*이전글/다음글 공지사항 제목*/
	private String title;

	/*이전글/다음글 공지사항 분류명*/
	private String preNextType;

}