package kr.co.pulmuone.v1.customer.notice.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.customer.notice.dto.vo.GetNoticePreNextListByUserResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
@ApiModel(description = "공지사항 상세 조회 ResponseDto")
public class GetNoticeByUserResponseDto  extends BaseResponseDto{

	@ApiModelProperty(value = "공지사항 PK")
	private Long csNoticeId;

	@ApiModelProperty(value = "공지사항 분류")
	private String noticeType;

	@ApiModelProperty(value = "공지사항 분류명")
	private String noticeTypeName;

	@ApiModelProperty(value = "제목")
	private String title;

	@ApiModelProperty(value = "조회수")
	private String views;

	@ApiModelProperty(value = "공지사항 내용")
	private String content;

	@ApiModelProperty(value = "등록일")
	private String createDate;

	@ApiModelProperty(value = "이전글/다음글 조회 ResultVo")
	private	List<GetNoticePreNextListByUserResultVo> preNextList;




}
