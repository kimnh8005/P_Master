package kr.co.pulmuone.v1.customer.notice.dto;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NoticeBosRequestDto  extends BaseRequestPageDto{

	@ApiModelProperty(value = "공지사항 분류")
    private String searchNoticeSelect;

	@ApiModelProperty(value = "단일조건 검색기준")
	private String searchSelect;

	@ApiModelProperty(value = "복수조건 검색기준")
	private String searchManySelect;

	@ApiModelProperty(value = "단일조건 검색조건")
	private String findKeyword;

	@ApiModelProperty(value = "복수조건 검색조건")
	private String findManyKeyword;

	@ApiModelProperty(value = "단일조건 검색조건")
	private ArrayList<String> findKeywordArray;

	@ApiModelProperty(value = "복수조건 검색조건")
	private ArrayList<String> findManyKeywordArray;

	@ApiModelProperty(value = "등록일 시작")
	private String createDateStart;

	@ApiModelProperty(value = "등록일 종료")
	private String createDateEnd;

	@ApiModelProperty(value = "공지사항 ID")
	private String noticeId;

	@ApiModelProperty(value = "상부고정시작일")
	private String topDdisplayStartDate;

	@ApiModelProperty(value = "상부고정종료일")
	private String topDdisplayEndDate;

	@ApiModelProperty(value = "공지사항 분류")
	private String noticeType;

	@ApiModelProperty(value = "공지사항 제목")
	private String noticeTitle;

	@ApiModelProperty(value = "공지사항 노출여부")
	private String displayYn;

	@ApiModelProperty(value = "상단고정설정")
	private String topDisplayYn;

	@ApiModelProperty(value = "상단고정시작일")
	private String topDisplayStartDate;

	@ApiModelProperty(value = "상단고정종료일")
	private String topDisplayEndDate;

	@ApiModelProperty(value = "공지채널")
	private String channelYn;

	@ApiModelProperty(value = "공지채널 PC")
	private String channelPcYn;

	@ApiModelProperty(value = "공지채널 Mobile")
	private String channelMobileYn;

	@ApiModelProperty(value = "공지채널")
	private List<String> channelYnList;

	@ApiModelProperty(value = "공지내용")
    String content;




}
