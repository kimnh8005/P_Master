package kr.co.pulmuone.v1.customer.notice.dto.vo;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "공지사항 리스트 ResultVo")
public class GetNoticeListByUserResultVo {

	/*공지사항 PK ID*/
	private Long csNoticeId;

	/*공지사항 분류*/
	private String noticeType;

	/*공지사항 분류명*/
	private String noticeTypeName;

	/*상단고정여부*/
	private String topFixYn;

	/*제목*/
	private String title;

	/*조회수*/
	private String views;

	/*등록일*/
	private String createDate;

}